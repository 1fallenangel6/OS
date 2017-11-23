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
	
	//从控制台输入矩阵，初始化各个矩阵
	public void initInput() {
		Scanner read=new Scanner(System.in);
		System.out.print("请输入资源种类：");
		column = read.nextInt();
		System.out.print("请输入进程个数：");
		row = read.nextInt();
		Allocation = new int[row][column];
		Need = new int[row][column];
		System.out.println("请输入分配矩阵：");
		read.nextLine();
		for(int i=0;i<row;i++) {
			String[] Line=read.nextLine().split(" ");
			if(Line.length==column) {
				for(int j=0;j<Line.length;j++) {
					Allocation[i][j]=Integer.parseInt(Line[j]);
				}
			}else {
				System.out.println("矩阵行元素个数不正确");
			}
		}
		
		
		System.out.println("请输入需求矩阵：");
		for(int i=0;i<row;i++) {
			String[] Line=read.nextLine().split(" ");
			if(Line.length==column) {
				for(int j=0;j<Line.length;j++) {
					Need[i][j]=Integer.parseInt(Line[j]);
				}
			}else {
				System.out.println("矩阵行元素个数不正确");
			}
		}
		
		System.out.println("请输入可利用资源向量：");
		String[] Line=read.nextLine().split(" ");
		if(Line.length==column) {
			Available = new int[column];
			for(int j=0;j<Line.length;j++) {
				Available[j]=Integer.parseInt(Line[j]);
			}
		}else {
			System.out.println("矩阵行元素个数不正确");
		}

		System.out.println("请输入请求进程序号（序号从1开始）：");
		reqIndex=read.nextInt();
		
		System.out.println("请输入请求向量：");
		read.nextLine();
		String[] Line0=read.nextLine().split(" ");
		if(Line0.length==column) {
			Request = new int[column];
			for(int j=0;j<Line0.length;j++) {
				Request[j]=Integer.parseInt(Line0[j]);
			}
		}else {
			System.out.println("矩阵行元素个数不正确");
		}
	}
	

	
	public void initFinish() {
		Finish = new boolean[row];
		for(int i=0;i<row;i++)
			Finish[i]=false;
	}
	
	public void Banker() {
		if(RequestFit(Request,Need[reqIndex])==false) {
			System.out.println("请求资源数超过需要的资源数");
		}else {
			if(RequestFit(Request,Available)==false)
				System.out.println("请求资源数超过可利用资源数");
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
			System.out.println("找不到安全序列");
		else {
			System.out.print("找到一个安全序列：");
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
