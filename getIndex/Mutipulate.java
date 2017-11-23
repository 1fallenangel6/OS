package getIndex;

import java.util.Formatter;

public class Mutipulate {

	static Formatter formatter = new Formatter(System.out);
	
	private String[] name;
	private int[] reachTime;
	private int[] serveTime;
	
	private int[] FcfsendTime;
	private int[] FcfsTurnOverTime;
	private double[] FcfsWeightTime;
	
	private int[] SjfendTime;
	private int[] SjfTurnOverTime;
	private double[] SjfWeightTime;
	
	private int count;
	private int[] RRendTime;
	private int[] RRTurnOverTime;
	private double[] RRWeightTime;
	
	private boolean[] index;
	
	public Mutipulate(String[] name,int[] reachTime,int[] serveTime) {
		this.reachTime=reachTime;
		this.serveTime=serveTime;
		this.name=name;
		
		this.FcfsendTime=new int[reachTime.length];
		this.FcfsTurnOverTime=new int[reachTime.length];
		this.FcfsWeightTime=new double[reachTime.length];
		
		this.SjfendTime=new int[reachTime.length];
		this.SjfTurnOverTime=new int[reachTime.length];
		this.SjfWeightTime=new double[reachTime.length];
		
		this.count=name.length;
		this.RRendTime=new int[reachTime.length];
		this.RRTurnOverTime=new int[reachTime.length];
		this.RRWeightTime=new double[reachTime.length];
		
		this.index=new boolean[reachTime.length];
		
		printRS();
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
	
	public void initIndex() {
		for(int i=0;i<index.length;i++)
			index[i]=true;		
	}
	
	
	public void fcfs() {
		initIndex();
		int zero=0,next;
		while(reachTime[zero]!=0)
			zero++;
		FcfsendTime[zero]=reachTime[zero]+serveTime[zero];
		index[zero]=false;
		for(int i=1;i<index.length;i++) {
			next=getMinIndex(reachTime,index);
			if(FcfsendTime[zero]<reachTime[next])
				FcfsendTime[next]=reachTime[next]+serveTime[next];
			else
				FcfsendTime[next]=FcfsendTime[zero]+serveTime[next];
			index[next]=false;
			zero=next;
		}
		for(int i=0;i<index.length;i++) {
			FcfsTurnOverTime[i]=FcfsendTime[i]-reachTime[i];
			FcfsWeightTime[i]=(double)FcfsTurnOverTime[i]/serveTime[i];
		}
		printFCFS();
	}
	
	public void RR(int rr) {
		int max=0;
		int t;
		int i=0;
		int length=this.count;
		int[] serveCopy=new int[length];
		System.arraycopy(this.serveTime, 0, serveCopy, 0, length);
		while(this.count>0) {
			if(serveCopy[i]>rr) {
				serveCopy[i]=serveCopy[i]-rr;
				max=max+rr;
				i=(i+1)%length;
				continue;
			}
			if(serveCopy[i]==rr)
			{
				serveCopy[i]=serveCopy[i]-rr;
				count--;
				max=max+rr;
				this.RRendTime[i]=max;
				i=(i+1)%length;
				continue;
			}
			if(serveCopy[i]==0) {
				i=(i+1)%length;
				continue;
			}
			if(serveCopy[i]<rr) {
				max=max+serveCopy[i];
				serveCopy[i]=0;
				count--;
				this.RRendTime[i]=max;
				i=(i+1)%length;
			}
		}
		
		for(int j=0;j<length;j++) {
			RRTurnOverTime[j]=RRendTime[j]-reachTime[j];
			RRWeightTime[j]=(double)RRTurnOverTime[j]/serveTime[j];
		}
		printRR();
	}
	public void printRR() {
		System.out.println("-----------------------------------------------");
		System.out.printf("%20s","完成时间");
		for(int i=0;i<this.FcfsendTime.length;i++)
			System.out.printf("%6d",this.RRendTime[i]);
		System.out.println("");
		
		System.out.printf("%-4s%8s","RR","周转时间");
		//System.out.printf("%20s","周转时间");
		for(int i=0;i<this.RRendTime.length;i++)
			System.out.printf("%6d",this.RRTurnOverTime[i]);
		System.out.println("");
		
		System.out.printf("%15s","带权周转时间");
		for(int i=0;i<this.RRendTime.length;i++)
			System.out.printf("%6.2f", this.RRWeightTime[i]);
		System.out.println("");
	}
	
	public void sjf() {
		initIndex();
		int zero=0,next;
		while(reachTime[zero]!=0)
			zero++;
		SjfendTime[zero]=reachTime[zero]+serveTime[zero];
		index[zero]=false;
		for(int i=1;i<index.length;i++) {
			next=getMinIndex(serveTime,index);
			if(SjfendTime[zero]<reachTime[next])
				SjfendTime[next]=reachTime[next]+serveTime[next];
			else
				SjfendTime[next]=SjfendTime[zero]+serveTime[next];
			index[next]=false;
			zero=next;
		}
		for(int i=0;i<index.length;i++) {
			SjfTurnOverTime[i]=SjfendTime[i]-reachTime[i];
			SjfWeightTime[i]=(double)SjfTurnOverTime[i]/serveTime[i];
		}
		printSJF();
			
	}
	
	public void printSJF() {
		System.out.println("-----------------------------------------------");
		System.out.printf("%20s","完成时间");
		for(int i=0;i<this.SjfendTime.length;i++)
			System.out.printf("%6d",this.SjfendTime[i]);
		System.out.println("");
		
		System.out.printf("%-4s%8s","SJF","周转时间");
		for(int i=0;i<this.SjfendTime.length;i++)
			//formatter.format("%6d",this.SjfWeightTime[i]);
			System.out.printf("%6d",this.SjfTurnOverTime[i]);
			//System.out.print(this.SjfTurnOverTime[i]+" ");
		System.out.println("");
		
		System.out.printf("%15s","带权周转时间");
		for(int i=0;i<this.SjfendTime.length;i++)
			System.out.printf("%6.2f",this.SjfWeightTime[i]);
		System.out.println("");
		
	}

	public void printFCFS() {
		System.out.println("-----------------------------------------------");
		System.out.printf("%20s","完成时间");
		for(int i=0;i<this.FcfsendTime.length;i++)
			System.out.printf("%6d",this.FcfsendTime[i]);
		System.out.println("");
		
		System.out.printf("%-4s%8s","FCFS","周转时间");
		//System.out.printf("%20s","周转时间");
		for(int i=0;i<this.FcfsendTime.length;i++)
			System.out.printf("%6d",this.FcfsTurnOverTime[i]);
		System.out.println("");
		
		System.out.printf("%15s","带权周转时间");
		for(int i=0;i<this.FcfsendTime.length;i++)
			System.out.printf("%6.2f", this.FcfsWeightTime[i]);
		System.out.println("");
		
		
	}
	
	
	public int getMinIndex(int[] source,boolean[] index) {

		int count=0;
		for(int i=0;i<index.length;i++) {
			if(index[i]==true)
				count++;
		}
		int[] indexrange=new int[count];
		count=0;
		for(int i=0;i<index.length;i++) {
			if(index[i]==true)
				indexrange[count++]=i;
		}
		int min=indexrange[0];
		for(int i=1;i<indexrange.length;i++) {
			if(source[min]>source[indexrange[i]])
				min=indexrange[i];
		}
		return min;
	}
	
}
