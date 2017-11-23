
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

memArea* begin2end(memArea* begin,memArea* end,int process,int len){
	memArea* p = begin;
	while(p!=end){
		if(p->status==true && p->length>=len){
			return p;
		}
		p = p->next;
	}
	return NULL;
}

memArea* newLocation(memArea* begin,memArea* end){
	memArea* p = begin;
	while(p!=end){
		if((p->status==true)){
			return p;
		}
		p = p->next;
	}
	return NULL;
}

memArea* distributeNF(memArea **head,memArea** current,int process,int len){
	memArea* p = NULL;
	int flag = 0;

	p = begin2end(*current,NULL,process,len);
	if(p==NULL){
		p = begin2end((*head)->next,*current,process,len);
		flag = 1;
	}
	if(p!=NULL && p->length>len){
		memArea* memory = new memArea;
		memory->length = p->length-len;
		memory->begin = p->begin+len;
		memory->status = true;
		memory->next = p->next;

		p->length = len;
		p->process = process;
		p->status = false;
		p->next = memory;
	}
	if(p!=NULL && p->length==len){
		p->process = process;
		p->status = false;
	}

	*current = newLocation(p,NULL);
	if(*current==NULL){
		*current = newLocation((*head)->next,p);
	}
	return p;
}

memArea* recycle(memArea *head,memArea **current,int process){
	memArea* p = head;
	while(p->next!=NULL){
		if(p->next->status==false && p->next->process==process){
			memArea* q = p->next;
			if(p!=head && p->status==true){
				p->length = p->length+q->length;
				p->next = q->next;
				if(q==*current)
					*current = p;
				delete q;
				q = p->next;
				if(q->status==true){
					p->length = p->length+q->length;
					p->next = q->next;
					if(q==*current)
					*current = p;
					delete q;
				}
			}else{
				p = q;
				p->status = true;
				q = p->next;
				if(q->status==true){
					p->length = p->length+q->length;
					p->next = q->next;
					if(q==*current)
					*current = p;
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
	cout<<endl<<"-----------------------��ǰ�ڴ�ʹ���������------------------------"<<endl;
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
	cout<<"-------------------------------------------------------------------"<<endl<<endl;

}

int processId = 0;

int main(){
	ifstream ifile;               //���������ļ�
    ifile.open("d:\\myfile.txt");     //��Ϊ�����ļ���
	memArea* head = initMemory();
	memArea* current = head->next;
	MemoryUseCondiditon(head);
	int process;
	int len;
	while(!ifile.eof()){
		ifile>>process>>len;
		if(len!=0){
			cout<<"---���� "<<process<<" ��Ҫ "<<len<<" KB �ڴ�ռ�---"<<endl;
			memArea* p = distributeNF(&head,&current,process,len);

			if(p==NULL){
				cout<<"���� "<<process<<" ��Ҫ�Ŀռ���󣬿ռ����ʧ�ܣ�����"<<endl;
			}
			else{
				MemoryUseCondiditon(head);
			}
		}else{

			cout<<" ���ս��� "<<process<<" ���պ���ڴ�����:"<<endl;
			head = recycle(head,&current,process);
			MemoryUseCondiditon(head);
		}

	}

	ifile.close();
	return 0;
}
