package test1;

public class Task {
	private String[] name;
	private double[] reachTime;
	private double[] serveTime;
	private int length;
	
	public Task(int length) {
		this.length=length;
		name=new String[length];
		reachTime=new double[length];
		serveTime=new double[length];
	}
	public int getLength() {
		return length;
	}
	public void setName(String[] Name) {
		name=Name;
	}
	public void setreachTime(double[] ReachTime) {
		reachTime=ReachTime;
	}
	public void setserveTime(double[] ServeTime) {
		serveTime=ServeTime;
	}
	public String[] getName() {
		return name;
	}
	public double[] getreachTime() {
		return reachTime;
	}
	public double[] getserveTime() {
		return serveTime;
	}
	
	
}
