package test1;

public class SJF extends Mutipulate{
	private Task task;
	

	public SJF(Task myTask) {
		this.task=myTask;
		/*
		EndTime=new double[this.task.getLength()];
		CircleTime=new double[this.task.getLength()];
		CWeightTime=new double[this.task.getLength()];*/
	}
	
	
	int[] getIndex(double time[]) {
		int[] index=new int[time.length];
		boolean[] flag=new boolean[time.length];
		for(int i=0;i<flag.length;i++) {
			flag[i]=true;
		}
		int min;
		for(int i=0;i<time.length;i++) {
			min=i;
			int j = 0;
			while(flag[j]==false) {
				j++;
			}
			for(j=min+1;j<time.length;j++) {
				if(time[min]>time[j] && flag[j]==true) {
					min=j;
					index[i]=j;
				}
			}
			flag[min]=false;
			
		}
		return index;
	}
	
	@Override
	double[] getEndTime() {
		int[] index=getIndex(task.getreachTime());
		EndTime[index[0]]=task.getreachTime()[index[0]] + task.getserveTime()[index[0]];
		for(int i=1;i<this.task.getName().length;i++) {
			EndTime[index[i]]=EndTime[index[i-1]] + task.getserveTime()[index[i]];
		}
		return EndTime;
	}

	@Override
	double[] getCircleTime() {
		for(int i=0;i<this.task.getName().length; i++) {
			CircleTime[i]=EndTime[i]-task.getreachTime()[i];
		}
		return CircleTime;
	}

	@Override
	double[] getCWeightTime() {
		for(int i=0;i<this.task.getName().length; i++) {
			CWeightTime[i]=CircleTime[i]/task.getserveTime()[i];
		}
		return CWeightTime;
	}
	void printEle(int[] Time) {
		for(int i = 0;i<Time.length;i++) {
			System.out.println(Time[i]);
		}
	}
}
