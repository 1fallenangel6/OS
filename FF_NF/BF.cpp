#include<fstream>
#include<iostream>
#include<iomanip>
#include <string>


using namespace std;

typedef struct memArea{
	int begin;	//������ʼ��ַ
	int length; //������С
	bool status; //����״̬ 0��ʾ���з�����1��ʾ���̷���������������ʹ�ã�
	int process; //����ʹ�ô˷����Ľ���ID
	struct memArea *next;	//ʹ������ṹ�����з����ͽ��̷�����ͬһ�������У�ʹ��
							  //status ��Ƿ�����״̬
}memArea; 

//��ʼ�����з�����������ͷ�ڵ�,��ʼ���з�����СΪ1024KB
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

//��ʼ�����̱�����ͷ�ڵ�
memArea* initProcess(){
	memArea* head = new memArea;
	head->next = NULL;
	return head;
}




//�����Ӧ�㷨���ڿ��з������в����������������ռ����С����
//�˺����ķ��ص�ʱ�����Ӧ�㷨�ҵ��ķ����ڵ��ǰһ���ڵ��ָ��prev
//����ҵ��ķ����Ĵ�С���õ��ڽ�������Ŀռ��С����Ҫ���˷����ڵ��
//���з���������ɾ������ʱ�ͻ��õ�prevָ��
memArea* searchSmallIdlePrev(memArea *idle,int len){
	memArea *p = idle;
	memArea *smallPrev = NULL;
	if(p->next)
		smallPrev = p;
	while(p->next){
		if(p->next->length<=smallPrev->next->length && p->next->length>=len)//����ҵ����µ��������������ռ�ĸ�С�ķ���������ָ��prev
			smallPrev = p;
		p = p->next;
	}
	if(smallPrev && smallPrev->next->length>=len)
		return smallPrev;
	else
		return NULL;
}

//���Ӧ�㷨���ڿ��з������в����������������ռ��������
//�˺����ķ��ص�ʱ���Ӧ�㷨�ҵ��ķ����ڵ��ǰһ���ڵ��ָ��prev
//����ҵ��ķ����Ĵ�С���õ��ڽ�������Ŀռ��С����Ҫ���˷����ڵ��
//���з���������ɾ������ʱ�ͻ��õ�prevָ��
memArea* searchBigIdlePrev(memArea *idle,int len){
	memArea *p = idle;
	memArea *bigPrev = NULL;
	if(p->next)
		bigPrev = p;//�����С������ǰһ��������ָ��
	while(p->next){
		if(p->next->length>bigPrev->next->length)//����ҵ��˸���ķ���������ָ��prev
			bigPrev = p;
		p = p->next;
	}
	if(bigPrev && bigPrev->next->length>=len)//�����������������ռ�Ƚ�
		return bigPrev;
	else
		return NULL;
}


//��������ɾ��һ���ڵ�
memArea * removeNode(memArea *prev){
	memArea *p = prev->next;
	if(p){
		prev->next = p->next;
		return p;
	}
}
//���ڵ���뵽�����ײ�
void insertNode(memArea *proc,memArea *process){
	process->next = proc->next;
	proc->next = process;
}
//����һ���ڵ㣬�ڿ��з������в�������ǰ�ϲ��Ľڵ��ǰһ���ڵ�
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
//����һ���ڵ㣬�ڿ��з������в��������ϲ��Ľڵ��ǰһ���ڵ�
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

//�ϲ����з����Ľڵ�
void mergeNode(memArea *idle,memArea *process){
	memArea *prevprev = searchAddressPrev(idle,process);//��������ǰ�ϲ��ķ����ڵ��ǰһ���ڵ�
	memArea *nextprev = searchAddressNext(idle,process);//���������ϲ��ķ����ڵ��ǰһ���ڵ�
	memArea *prev = NULL,*next = NULL;
	if(prevprev)//�ҵ���ǰ�ϲ��ķ����ڵ��ǰһ���ڵ�
		prev = prevprev->next;
	if(nextprev)//�ҵ����ϲ��ķ����ڵ��ǰһ���ڵ�
		next = nextprev->next;
	if(prev && next){//����ǰ�ϲ�&&�����ϲ�
		prev->length = prev->length + process->length + next->length;
		memArea *p = removeNode(nextprev);
		delete p;
		delete process;
	}
	if(prev && !next){//����ǰ�ϲ�&&�������ϲ�
		prev->length = prev->length + process->length;
		delete process;
	}
	if(!prev && next){//������ǰ�ϲ�&&�����ϲ�
		next->length = next->length + process->length;
		delete process;
	}
	if(!prev && !next){//������ǰ�ϲ�&&�������ϲ�
		process->status = true;
		insertNode(idle,process);
	}

}
//�������̽ڵ�
memArea *createProcess(int procId,int length){
	memArea *process = new memArea;
	process->process = procId;
	process->length = length;
	process->status = false;
	return process;
}
//��������ռ䣬�ӿ��з������з���ռ�����̣��������̷������������
bool distribute(memArea *idle,memArea *proc,memArea *process){
	//memArea *prev = searchSmallIdlePrev(idle,process->length),*p = NULL;//ʹ�������Ӧ�㷨�Һ��ʽڵ��ǰһ���ڵ�
	memArea *prev = searchBigIdlePrev(idle,process->length),*p = NULL;//ʹ�����Ӧ�㷨�Һ��ʽڵ��ǰһ���ڵ�
	if(prev)//�ҵ����ʽڵ��ǰһ���ڵ�
		p = prev->next;
	if(p && (p->length)>(process->length)){//�ҵ����з������ҿ��з�����������ռ�
		process->begin = p->begin;
		//�޸Ŀ��з����Ĵ�С����ʼ��ַ
		p->length = p->length-process->length;
		p->begin = process->begin+process->length;
		insertNode(proc,process);//�����̽ڵ�������������
		return true;//��������ռ�ɹ�
	}else if(p && p->length==process->length){//�ҵ����з������ҿ��з�����������ռ�
		process->begin = p->begin;
		memArea *q = removeNode(prev);//�����з����ڵ�ӿ��з����������Ƴ�
		delete q;
		insertNode(proc,process);//�����̽ڵ�������������
		return true;//��������ռ�ɹ�
	}else{
		return false;//��������ռ�ʧ��
	}
}

//��������ID�������������в��Ҷ�Ӧ�Ľ��̽ڵ��ǰһ���ڵ�
memArea *searchNodePrev(memArea *proc,int processId){
	memArea *p = proc;
	while(p->next){
		if(p->next->process==processId)
			return p;
		p = p->next;
	}
	return NULL;
}
//���ս��̿ռ�
memArea* recycle(memArea *idle,memArea *proc,int processId){
	memArea *processPrev = searchNodePrev(proc,processId);//�����������в��Ҷ�Ӧ�Ľ��̽ڵ��ǰһ���ڵ�
	memArea *process=NULL;
	if(processPrev)//����ҵ������˽��̽ڵ�ӽ���������ɾ��
		process = removeNode(processPrev);
	else
		cout<<"���������в����ڴ˽���ID"<<endl;
	if(process)//�����յĽ��̿ռ�ŵ����з��������в��ϲ����з���
		mergeNode(idle,process);
	return idle;
}
//������з�������
void MemoryUseCondiditon(memArea *idle){
	memArea* p = idle->next;
	int memId=0;
	cout<<endl<<"-----------------------���з�������------------------------"<<endl;
	while(p!=NULL){
		if(p->status==true){
			cout<<setw(8)<<"���з�����"<<setw(2)<<"ID "<<setw(3)<<memId<<setw(8)
				<<"  ��ʼ��ַ"<<setw(6)<<p->begin<<setw(8)<<"  ������С"<<setw(6)<<p->length<<"    ����   "<<endl;
			memId++;
		}else{
			cout<<setw(8)<<"���̷�����"<<setw(2)<<"ID "<<setw(3)<<p->process<<setw(8)
				<<"  ��ʼ��ַ"<<setw(6)<<p->begin<<setw(8)<<"  ������С"<<setw(6)<<p->length<<"  �ѷ���"<<endl;
		}
		p = p->next;
	}
	cout<<endl;

}
//�����������
void processList(memArea *proc){
	memArea* p = proc->next;
	cout<<endl<<"-----------------------��������------------------------"<<endl;
	while(p!=NULL){
		cout<<setw(8)<<"����ID��"<<setw(6)<<p->process<<setw(8)
			<<"  ��ʼ��ַ"<<setw(6)<<p->begin<<setw(8)<<"  ������С"<<setw(6)<<p->length<<endl;
		p = p->next;
	}
	cout<<"-------------------------------------------------------------------"<<endl<<endl;
}

int main(){
	ifstream ifile;               //���������ļ�
    ifile.open("d:\\myfile.txt");     //��Ϊ�����ļ���
	memArea* idle = initMemory();
	MemoryUseCondiditon(idle);
	memArea *proc = initProcess();
	int process;
	int len;
	ifile>>process>>len;//���ļ��ж�ȡ����
	//���len!=0��ʾ�Ӵ������̣���������ռ�
	//���len==0��ʾ���ս���
	while(!ifile.eof()){
		if(len!=0){//���len!=0��ʾ�Ӵ������̣���������ռ�
			cout<<"---���� "<<process<<" ��Ҫ "<<len<<" KB �ڴ�ռ�---"<<endl;
			memArea *newProc = createProcess(process,len);
			bool flag = distribute(idle,proc,newProc);//��������ռ�

			if(flag==false){
				cout<<"���� "<<process<<" ��Ҫ�Ŀռ���󣬿ռ����ʧ�ܣ�����"<<endl;
			}
			else{
				MemoryUseCondiditon(idle);//������з�����
				processList(proc);//�����������
			}
		}else{	//���len==0��ʾ���ս���
			cout<<" ���ս��� "<<process<<" ���պ���ڴ�����:"<<endl;
			idle = recycle(idle,proc,process);//���ս��̿ռ�
			MemoryUseCondiditon(idle);//������з�����
			processList(proc);//�����������
		}
		ifile>>process>>len;//ѭ�����ļ��ж�ȡ����
	}

	ifile.close();
	return 0;
}
