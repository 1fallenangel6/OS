package banker;

import java.util.Scanner;

public class MyBanker {
	int[][] Allocation;
	int[][] Need;
	int[] Available;
	int[] Request;
	int[] Work;
	boolean[] Finish;
	int count=0;
	int[] Record;
	int row;
	int column;
	int reqIndex;
	
	public MyBanker() {
		initInput();
		Record = new int[row];
		initFinish();
	}
	
	//�ӿ���̨������󣬳�ʼ����������
	public void initInput() {
		Scanner read=new Scanner(System.in);
		System.out.print("��������Դ���ࣺ");
		column = read.nextInt();
		System.out.print("��������̸�����");
		row = read.nextInt();
		Allocation = new int[row][column];
		Need = new int[row][column];
		System.out.println("������������");
		read.nextLine();
		for(int i=0;i<row;i++) {
			String[] Line=read.nextLine().split(" ");
			if(Line.length==column) {
				for(int j=0;j<Line.length;j++) {
					Allocation[i][j]=Integer.parseInt(Line[j]);
				}
			}else {
				System.out.println("������Ԫ�ظ�������ȷ");
			}
		}
		
		
		System.out.println("�������������");
		for(int i=0;i<row;i++) {
			String[] Line=read.nextLine().split(" ");
			if(Line.length==column) {
				for(int j=0;j<Line.length;j++) {
					Need[i][j]=Integer.parseInt(Line[j]);
				}
			}else {
				System.out.println("������Ԫ�ظ�������ȷ");
			}
		}
		
		System.out.println("�������������Դ������");
		String[] Line=read.nextLine().split(" ");
		if(Line.length==column) {
			Available = new int[column];
			for(int j=0;j<Line.length;j++) {
				Available[j]=Integer.parseInt(Line[j]);
			}
		}else {
			System.out.println("������Ԫ�ظ�������ȷ");
		}

		System.out.println("���������������ţ���Ŵ�1��ʼ����");
		reqIndex=read.nextInt();
		
		System.out.println("����������������");
		read.nextLine();
		String[] Line0=read.nextLine().split(" ");
		if(Line0.length==column) {
			Request = new int[column];
			for(int j=0;j<Line0.length;j++) {
				Request[j]=Integer.parseInt(Line0[j]);
			}
		}else {
			System.out.println("������Ԫ�ظ�������ȷ");
		}
	}
	

	
	public void initFinish() {
		Finish = new boolean[row];
		for(int i=0;i<row;i++)
			Finish[i]=false;
	}
	
	public void Banker() {
		if(RequestFit(Request,Need[reqIndex])==false) {
			System.out.println("������Դ��������Ҫ����Դ��");
		}else {
			if(RequestFit(Request,Available)==false)
				System.out.println("������Դ��������������Դ��");
			else {
				for(int i=0;i<column;i++) {
					Available[i]=Available[i]-Request[i];
					Need[reqIndex][i]=Need[reqIndex][i]-Request[i];
					Allocation[reqIndex][i]=Allocation[reqIndex][i]+Request[i];
				}
				SearchSafe();
			}
		}
	}
	
	public boolean RequestFit(int[] request,int[] need) {
		boolean judge=true;
		for(int i=0;i<need.length;i++) {
			if(request[i]>need[i]) {
				judge=false;
				break;
			}	
		}
		return judge;
	}
	
	public void SearchSafe() {
		Work=Available;
		printWork();
		int i=0;
		while(count<row) {
			i=getNeed();
			if(i==-1)
				break;
			printNeed(i);
			printWork();
			Record[count]=i;
			count=count+1;
			Finish[i]=true;
			System.out.print("Work = Work + Allocation["+i+"]");
			addWork(Allocation[i]);
		}
		if(i==-1)
			System.out.println("�Ҳ�����ȫ����");
		else {
			System.out.print("�ҵ�һ����ȫ���У�");
			for(int x:Record) {
				System.out.print("P"+x+" ");
			}
			System.out.println("");
		}
			
		
	}
	
	public void printNeed(int index) {
		printRow("Need["+index+"] = ",Need[index]);
		System.out.print(" <= ");
	}
	
	public void printRow(String notice,int[] row) {
		System.out.print(notice);
		System.out.print("(");
		for(int i=0;i<row.length;i++) {
			System.out.print(row[i]);
			if(i!=row.length-1)
				System.out.print(",");
		}
		System.out.print(")");
	}
	
	public void addWork(int[] Alloc) {
		printRow("= ",Work);
		printRow(" + ",Alloc);
		for(int i=0;i<Alloc.length;i++) {
			Work[i]=Work[i]+Alloc[i];
		}
		printRow(" = ",Work);
		System.out.println("");
	}
	public void printWork() {
		printRow("Work = ",Work);
		System.out.println("");
	}
	public boolean NeedLess(int[] need,int[] work) {
		boolean judge=true;
		for(int i=0;i<need.length;i++) {
			if(need[i]>work[i]) {
				judge=false;
				break;
			}	
		}
		return judge;
	}
	
	public int getNeed() {
		int position=-1;
		for(int i=0;i<row;i++) {
			if(Finish[i]==false) {
				boolean flag=NeedLess(Need[i],Work);
				if(flag) {
					position=i;
					break;
				}
					
			}
		}
		return position;
	}
	
}
