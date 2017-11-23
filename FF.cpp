#include<fstream>
#include<iostream>
#include<iomanip>
#include <string>


using namespace std;

typedef struct memArea{
	int begin;
	int length;
	bool status;
	int process;
	struct memArea *next;
}memArea;

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

bool distribute(memArea *head,int process,int len){
	memArea* p = head->next;
	while(p!=NULL){
		if(p->status==false){
			p = p->next;
			continue;
		}
		if(p->length>len){
			memArea* memory = new memArea;
			memory->length = p->length-len;
			memory->begin = p->begin+len;
			memory->status = true;
			memory->next = p->next;

			p->length = len;
			p->process = process;
			p->status = false;
			p->next = memory;
			return true;
		}else if(p->length==len){
			p->process = process;
			p->status = false;
			return true;
		}else{
			p = p->next;
		}
	}
	return false;
}

memArea* recycle(memArea *head,int process){
	memArea* p = head;
	while(p->next!=NULL){
		if(p->next->status==false && p->next->process==process){
			memArea* q = p->next;
			if(p!=head && p->status==true){
				p->length = p->length+q->length;
				p->next = q->next;
				delete q;
				q = p->next;
				if(q->status==true){
					p->length = p->length+q->length;
					p->next = q->next;
					delete q;
				}
			}else{
				p = q;
				p->status = true;
				q = p->next;
				if(q->status==true){
					p->length = p->length+q->length;
					p->next = q->next;
					delete q;
				}
			}
			break;
		}
		p = p->next;
	}

	return head;
}

void MemoryUseCondiditon(memArea *head){
	memArea* p = head->next;
	int memId=0;
	cout<<endl<<"-----------------------当前内存使用情况如下------------------------"<<endl;
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
	cout<<"-------------------------------------------------------------------"<<endl<<endl;

}

int main(){
	ifstream ifile;               //定义输入文件
    ifile.open("d:\\myfile.txt");     //作为输入文件打开
	memArea* head = initMemory();
	MemoryUseCondiditon(head);
	int process;
	int len;
	while(!ifile.eof()){
		ifile>>process>>len;
		if(len!=0){
			cout<<"---进程 "<<process<<" 需要 "<<len<<" KB 内存空间---"<<endl;
			bool flag = distribute(head,process,len);

			if(flag==false){
				cout<<"进程 "<<process<<" 需要的空间过大，空间分配失败！！！"<<endl;
			}
			else{
				MemoryUseCondiditon(head);
			}
		}else{
			cout<<" 回收进程 "<<process<<" 回收后的内存如下:"<<endl;
			head = recycle(head,process);
			MemoryUseCondiditon(head);
		}
		
	}

	ifile.close();
	return 0;
}
