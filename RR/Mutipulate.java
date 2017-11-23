package RR;

import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;

public class Mutipulate {

	private LinkedList<RR> NQ=new LinkedList<RR>();
	private LinkedList<RR> SQ=new LinkedList<RR>();
	private HashMap<String, RR> REMap = new HashMap<String, RR>();  
	private String[] name;
	private int[] reachTime;
	private int[] serveTime;
	
	private int count;
	private int[] RRendTime;
	private int[] RRTurnOverTime;
	private double[] RRWeightTime;

	
	public Mutipulate(String[] name,int[] reachTime,int[] serveTime) {
		this.reachTime=reachTime;
		this.serveTime=serveTime;
		this.name=name;

		this.count=name.length;
		this.RRendTime=new int[reachTime.length];
		this.RRTurnOverTime=new int[reachTime.length];
		this.RRWeightTime=new double[reachTime.length];
		
		
		initNQ();
		
		printRS();
	}
	
	public void initNQ() {
		for(int i=0;i<this.reachTime.length;i++) {
			RR myRR=new RR();
			myRR.setName(this.name[i]);
			myRR.setReachTime(this.reachTime[i]);
			myRR.setServeTime(this.serveTime[i]);
			myRR.setRRendTime(0);
			this.NQ.addLast(myRR);
		}
	}
	
	public void myRR(int pice) {
		int count=this.reachTime.length;
		int current=0;
		RR F=this.NQ.removeFirst();
		this.SQ.addLast(F);
		while(count>0) {
			printNQ();
			printSQ();
			RR X=this.SQ.removeFirst();
			if(X.getServeTime()<=pice) {
				current=current+X.getServeTime();
				X.setRRendTime(current);
				X.setServeTime(0);
				count--;
				System.out.println("----------"+X.getName()+":"+X.getRRendTime()+"------------");
			}else {
				X.setServeTime(X.getServeTime()-pice);
				current=current+pice;
				if(!this.NQ.isEmpty()) {
					RR First=this.NQ.removeFirst();
					if(current>=First.getReachTime()) {
						this.SQ.addLast(First);
					}else {
						this.NQ.addFirst(First);
					}
				}
				this.SQ.addLast(X);
			}
		}
		
	}
	
	public void useRR(int pice) {
		int count=this.reachTime.length;
		int current=0;
		RR F=this.NQ.removeFirst();
		this.SQ.addLast(F);
		while(count>0) {

			RR X=this.SQ.removeFirst();
			if(X.getServeTime()<=pice) {
				current=current+X.getServeTime();
				X.setRRendTime(current);
				X.setServeTime(0);
				count--;
				this.REMap.put(X.getName(), X);
			}else {
				X.setServeTime(X.getServeTime()-pice);
				current=current+pice;
				if(!this.NQ.isEmpty()) {
					RR First=this.NQ.removeFirst();
					if(current>=First.getReachTime()) {
						this.SQ.addLast(First);
					}else {
						this.NQ.addFirst(First);
					}
				}
				this.SQ.addLast(X);
			}
		}
		
		getResult();
		printRR();
	}
	
	public void getResult() {
		for(int i=0;i<this.reachTime.length;i++) {
			String name=this.name[i];
			RR X = this.REMap.get(name);
			this.RRendTime[i]=X.getRRendTime();
			this.RRTurnOverTime[i]=this.RRendTime[i]-this.reachTime[i];
			this.RRWeightTime[i]=(double)this.RRTurnOverTime[i]/this.serveTime[i];
		}
	}
	
	
	public void printSQ() {
		System.out.println("----------------SQ----------------");
		for(RR myRR:this.SQ) {
			myRR.printRR();
		}
	}
	public void printNQ() {
		System.out.println("----------------NQ----------------");
		for(RR myRR:this.NQ) {
			myRR.printRR();
		}
	}
	
	public void printRS() {
		System.out.println("-----------------------------------------------");
		
		System.out.printf("%20s","进程名称");
		for(int i=0;i<this.reachTime.length;i++)
			System.out.printf("%6s",this.name[i]);
		System.out.println("");
		
		System.out.printf("%20s","到达时间");
		for(int i=0;i<this.reachTime.length;i++)
			System.out.printf("%6d",this.reachTime[i]);
		System.out.println("");
		
		System.out.printf("%20s","服务时间");
		for(int i=0;i<this.serveTime.length;i++)
			System.out.printf("%6d",this.serveTime[i]);
		System.out.println("");
	}
	
	public void printRR() {
		System.out.println("-----------------------------------------------");
		System.out.printf("%20s","完成时间");
		for(int i=0;i<this.RRendTime.length;i++)
			System.out.printf("%6d",this.RRendTime[i]);
		System.out.println("");
		
		System.out.printf("%-4s%8s","RR","周转时间");
		for(int i=0;i<this.RRendTime.length;i++)
			//formatter.format("%6d",this.SjfWeightTime[i]);
			System.out.printf("%6d",this.RRTurnOverTime[i]);
			//System.out.print(this.SjfTurnOverTime[i]+" ");
		System.out.println("");
		
		System.out.printf("%15s","带权周转时间");
		for(int i=0;i<this.RRendTime.length;i++)
			System.out.printf("%6.2f",this.RRWeightTime[i]);
		System.out.println("");
	}
}
