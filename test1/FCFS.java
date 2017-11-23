package test1;


public class FCFS extends Mutipulate{

	private Task task;
	
	public FCFS(Task myTask) {
		this.task=myTask;
		EndTime=new double[this.task.getLength()];
		CircleTime=new double[this.task.getLength()];
		CWeightTime=new double[this.task.getLength()];
	}

	@Override
	double[] getEndTime() {
		EndTime[0]=task.getreachTime()[0] + task.getserveTime()[0];
		for(int i=1;i<this.task.getName().length;i++) {
			EndTime[i]=EndTime[i-1] + task.getserveTime()[i];
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
			CWeightTime[i]=CircleTime[i]/(task.getserveTime()[i]);
		}
		return CWeightTime;
	}
	

	
	void printEle(double[] Time) {
		for(int i = 0;i<Time.length;i++) {
			System.out.println(Time[i]);
		}
	}
	
}
