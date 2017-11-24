#include<fstream>
#include<iostream>
#include<iomanip>
#include <string>


using namespace std;

typedef struct memArea{
	int begin;	//分区起始地址
	int length; //分区大小
	bool status; //分区状态 0表示空闲分区，1表示进程分区（分区被进程使用）
	int process; //正在使用此分区的进程ID
	struct memArea *next;	//使用链表结构，空闲分区和进程分区在同一个链表中，使用
							  //status 标记分区的状态
}memArea; 

//初始化空闲分区链，创建头节点,初始空闲分区大小为1024KB
memArea* initMemory(){
	memArea* head = new memArea;
	memArea *ma= new memArea;
	head->next = ma;
	ma->begin = 0;
	ma->length = 1024;
	ma->status = true;
	ma->next = NULL;
	return head;
}

//初始化进程表，创建头节点
memArea* initProcess(){
	memArea* head = new memArea;
	head->next = NULL;
	return head;
}




//最佳适应算法，在空闲分区表中查找能满足进程所需空间的最小分区
//此函数的返回的时最佳适应算法找到的分区节点的前一个节点的指针prev
//如果找到的分区的大小正好等于进程申请的空间大小，需要将此分区节点从
//空闲分区链表中删除，这时就会用到prev指针
memArea* searchSmallIdlePrev(memArea *idle,int len){
	memArea *p = idle;
	memArea *smallPrev = NULL;
	if(p->next)
		smallPrev = p;
	while(p->next){
		if(p->next->length<=smallPrev->next->length && p->next->length>=len)//如果找到了新的能满足进程所需空间的更小的分区，更改指针prev
			smallPrev = p;
		p = p->next;
	}
	if(smallPrev && smallPrev->next->length>=len)
		return smallPrev;
	else
		return NULL;
}

//最坏适应算法，在空闲分区表中查找能满足进程所需空间的最大分区
//此函数的返回的时最坏适应算法找到的分区节点的前一个节点的指针prev
//如果找到的分区的大小正好等于进程申请的空间大小，需要将此分区节点从
//空闲分区链表中删除，这时就会用到prev指针
memArea* searchBigIdlePrev(memArea *idle,int len){
	memArea *p = idle;
	memArea *bigPrev = NULL;
	if(p->next)
		bigPrev = p;//标记最小分区的前一个分区的指针
	while(p->next){
		if(p->next->length>bigPrev->next->length)//如果找到了更大的分区，更改指针prev
			bigPrev = p;
		p = p->next;
	}
	if(bigPrev && bigPrev->next->length>=len)//将最大分区与进程所需空间比较
		return bigPrev;
	else
		return NULL;
}


//从链表上删除一个节点
memArea * removeNode(memArea *prev){
	memArea *p = prev->next;
	if(p){
		prev->next = p->next;
		return p;
	}
}
//将节点插入到链表首部
void insertNode(memArea *proc,memArea *process){
	process->next = proc->next;
	proc->next = process;
}
//给定一个节点，在空闲分区表中查找能向前合并的节点的前一个节点
memArea* searchAddressPrev(memArea *idle,memArea *process){
	memArea *prev = idle;
	int end = 0;
	while(prev->next){
		end = prev->next->begin+prev->next->length;
		if(process->begin==end)
			return prev;
		prev = prev->next;
	}
	return NULL;
}
//给定一个节点，在空闲分区表中查找能向后合并的节点的前一个节点
memArea* searchAddressNext(memArea *idle,memArea *process){
	memArea *Next = idle;
	int end = 0;
	if(Next)
		end = process->begin+process->length;
	while(Next->next){
		if(Next->next->begin==end)
			return Next;
		Next = Next->next;
	}
	return NULL;
}

//合并空闲分区的节点
void mergeNode(memArea *idle,memArea *process){
	memArea *prevprev = searchAddressPrev(idle,process);//查找能向前合并的分区节点的前一个节点
	memArea *nextprev = searchAddressNext(idle,process);//查找能向后合并的分区节点的前一个节点
	memArea *prev = NULL,*next = NULL;
	if(prevprev)//找到向前合并的分区节点的前一个节点
		prev = prevprev->next;
	if(nextprev)//找到向后合并的分区节点的前一个节点
		next = nextprev->next;
	if(prev && next){//能向前合并&&能向后合并
		prev->length = prev->length + process->length + next->length;
		memArea *p = removeNode(nextprev);
		delete p;
		delete process;
	}
	if(prev && !next){//能向前合并&&不能向后合并
		prev->length = prev->length + process->length;
		delete process;
	}
	if(!prev && next){//不能向前合并&&能向后合并
		next->length = next->length + process->length;
		delete process;
	}
	if(!prev && !next){//不能向前合并&&不能向后合并
		process->status = true;
		insertNode(idle,process);
	}

}
//创建进程节点
memArea *createProcess(int procId,int length){
	memArea *process = new memArea;
	process->process = procId;
	process->length = length;
	process->status = false;
	return process;
}
//进程申请空间，从空闲分区表中分配空间给进程，并将进程放入进程链表中
bool distribute(memArea *idle,memArea *proc,memArea *process){
	//memArea *prev = searchSmallIdlePrev(idle,process->length),*p = NULL;//使用最佳适应算法找合适节点的前一个节点
	memArea *prev = searchBigIdlePrev(idle,process->length),*p = NULL;//使用最坏适应算法找合适节点的前一个节点
	if(prev)//找到合适节点的前一个节点
		p = prev->next;
	if(p && (p->length)>(process->length)){//找到空闲分区，且空闲分区大于所需空间
		process->begin = p->begin;
		//修改空闲分区的大小和起始地址
		p->length = p->length-process->length;
		p->begin = process->begin+process->length;
		insertNode(proc,process);//将进程节点放入进程链表中
		return true;//进程申请空间成功
	}else if(p && p->length==process->length){//找到空闲分区，且空闲分区等于所需空间
		process->begin = p->begin;
		memArea *q = removeNode(prev);//将空闲分区节点从空闲分区链表中移除
		delete q;
		insertNode(proc,process);//将进程节点放入进程链表中
		return true;//进程申请空间成功
	}else{
		return false;//进程申请空间失败
	}
}

//给定进程ID，到进程链表中查找对应的进程节点的前一个节点
memArea *searchNodePrev(memArea *proc,int processId){
	memArea *p = proc;
	while(p->next){
		if(p->next->process==processId)
			return p;
		p = p->next;
	}
	return NULL;
}
//回收进程空间
memArea* recycle(memArea *idle,memArea *proc,int processId){
	memArea *processPrev = searchNodePrev(proc,processId);//到进程链表中查找对应的进程节点的前一个节点
	memArea *process=NULL;
	if(processPrev)//如果找到，将此进程节点从进程链表中删除
		process = removeNode(processPrev);
	else
		cout<<"进程链表中不存在此进程ID"<<endl;
	if(process)//将回收的进程空间放到空闲分区链表中并合并空闲分区
		mergeNode(idle,process);
	return idle;
}
//输出空闲分区链表
void MemoryUseCondiditon(memArea *idle){
	memArea* p = idle->next;
	int memId=0;
	cout<<endl<<"-----------------------空闲分区链表------------------------"<<endl;
	while(p!=NULL){
		if(p->status==true){
			cout<<setw(8)<<"空闲分区："<<setw(2)<<"ID "<<setw(3)<<memId<<setw(8)
				<<"  起始地址"<<setw(6)<<p->begin<<setw(8)<<"  分区大小"<<setw(6)<<p->length<<"    空闲   "<<endl;
			memId++;
		}else{
			cout<<setw(8)<<"进程分区："<<setw(2)<<"ID "<<setw(3)<<p->process<<setw(8)
				<<"  起始地址"<<setw(6)<<p->begin<<setw(8)<<"  分区大小"<<setw(6)<<p->length<<"  已分配"<<endl;
		}
		p = p->next;
	}
	cout<<endl;

}
//输出进程链表
void processList(memArea *proc){
	memArea* p = proc->next;
	cout<<endl<<"-----------------------进程链表------------------------"<<endl;
	while(p!=NULL){
		cout<<setw(8)<<"进程ID："<<setw(6)<<p->process<<setw(8)
			<<"  起始地址"<<setw(6)<<p->begin<<setw(8)<<"  分区大小"<<setw(6)<<p->length<<endl;
		p = p->next;
	}
	cout<<"-------------------------------------------------------------------"<<endl<<endl;
}

int main(){
	ifstream ifile;               //定义输入文件
    ifile.open("d:\\myfile.txt");     //作为输入文件打开
	memArea* idle = initMemory();
	MemoryUseCondiditon(idle);
	memArea *proc = initProcess();
	int process;
	int len;
	ifile>>process>>len;//从文件中读取数据
	//如果len!=0表示从创建进程，进程申请空间
	//如果len==0表示回收进程
	while(!ifile.eof()){
		if(len!=0){//如果len!=0表示从创建进程，进程申请空间
			cout<<"---进程 "<<process<<" 需要 "<<len<<" KB 内存空间---"<<endl;
			memArea *newProc = createProcess(process,len);
			bool flag = distribute(idle,proc,newProc);//进程申请空间

			if(flag==false){
				cout<<"进程 "<<process<<" 需要的空间过大，空间分配失败！！！"<<endl;
			}
			else{
				MemoryUseCondiditon(idle);//输出空闲分区表
				processList(proc);//输出进程链表
			}
		}else{	//如果len==0表示回收进程
			cout<<" 回收进程 "<<process<<" 回收后的内存如下:"<<endl;
			idle = recycle(idle,proc,process);//回收进程空间
			MemoryUseCondiditon(idle);//输出空闲分区表
			processList(proc);//输出进程链表
		}
		ifile>>process>>len;//循环从文件中读取数据
	}

	ifile.close();
	return 0;
}
