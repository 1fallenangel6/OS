package test1;

import java.util.Scanner;

public class Test1 {
	public static void main(String args[]) {
		Scanner scanner=new Scanner(System.in);
		String[] name= {"A","B","C","D","E"};
		
		double[] reachTime= {0,1,2,3,4};
		double[] serveTime= {4,3,5,2,4};
		/*
		System.out.println("请输入作业个数：");
		String line=scanner.nextLine();
		int len=Integer.parseInt(line);
		String[] name=new String[len];
		double[] reachTime=new double[len];
		double[] serveTime=new double[len];
		System.out.println(len);
		for(int i=0;i<len;i++) {
			line=scanner.nextLine();
			name[i]=line;
		}
		for(int i=0;i<len;i++) {
			line=scanner.nextLine();
			reachTime[i]=Double.parseDouble(line);
		}
		for(int i=0;i<len;i++) {
			line=scanner.nextLine();
			serveTime[i]=Double.parseDouble(line);
		}
		for(int i=0;i<len;i++) {
			System.out.print(reachTime[i]+" ");
			System.out.println("");
		}
		for(int i=0;i<len;i++) {
			System.out.print(serveTime[i]+" ");
			System.out.println("");
		}
		*/
		
		Task task=new Task(5);
		task.setName(name);
		task.setreachTime(reachTime);
		task.setserveTime(serveTime);
		FCFS fcfs=new FCFS(task);
		fcfs.printEle(fcfs.getEndTime());
		fcfs.printEle(fcfs.getCircleTime());
		fcfs.printEle(fcfs.getCWeightTime());
	
		double[] a= {2,5,1,8,4,6};
		SJF sjf=new SJF(task);
		int[] index=sjf.getIndex(a);
		sjf.printEle(index);
		
	}
	
}
