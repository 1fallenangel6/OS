package RR;

public class RR {
	
	private String name;
	private int reachTime;
	private int serveTime;
	
	private int RRendTime;
	
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void setReachTime(int reachTime) {
		this.reachTime=reachTime;
	}
	public void setServeTime(int serveTime) {
		this.serveTime=serveTime;
	}

	public void setRRendTime(int RRendTime) {
		this.RRendTime=RRendTime;
	}
	
	
	
	public String getName() {
		return this.name;
	}
	
	public int getReachTime() {
		return this.reachTime;
	}
	
	public int getServeTime() {
		return this.serveTime;
	}
	
	public int getRRendTime() {
		return this.RRendTime;
	}
	
	
	public void printRR() {
		System.out.print(this.name+" ");
		System.out.print(this.reachTime+" ");
		System.out.print(this.serveTime+" ");
		System.out.println(this.RRendTime);
	}
}
