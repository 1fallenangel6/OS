package RR;

import java.util.Scanner;

public class MyRR {
	
	public static void main(String[] args) {
		
		Scanner read=new Scanner(System.in);
		System.out.print("进程名称：");
		String[] name=read.nextLine().split(" ");
		System.out.print("到达时间：");
		String[] rTime=read.nextLine().split(" ");
		System.out.print("服务时间：");
		String[] sTime=read.nextLine().split(" ");
		
		int[] reachTime=new int[name.length];
		int[] serveTime=new int[name.length];
		for(int i=0;i<name.length;i++) {
			reachTime[i]=Integer.parseInt(rTime[i]);
			serveTime[i]=Integer.parseInt(sTime[i]);
		}
		
		
		Mutipulate mp=new Mutipulate(name,reachTime,serveTime);
		mp.useRR(1);
	}
}
