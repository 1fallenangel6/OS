package getIndex;

public class Sort {
	
	private int[] reachTime;
	private int[] serveTime;
	private int[] endTime1;
	private int[] endTime2;
	private boolean[] index;
	public Sort(int[] reachTime,int[] serveTime) {
		this.reachTime=reachTime;
		this.serveTime=serveTime;
		this.endTime1=new int[reachTime.length];
		this.endTime2=new int[reachTime.length];
		this.index=new boolean[reachTime.length];
		initIndex();
	}
	
	public void initIndex() {
		for(int i=0;i<index.length;i++)
			index[i]=true;		
	}
	
	public void fcfs() {
		int zero=0,next;
		while(reachTime[zero]!=0)
			zero++;
		endTime1[zero]=reachTime[zero]+serveTime[zero];
		index[zero]=false;
		for(int i=1;i<index.length;i++) {
			next=getMinIndex(reachTime,index);
			endTime1[next]=endTime1[zero]+serveTime[next];
			index[next]=false;
			zero=next;
		}
		
		
		
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
			if(source[min]>source[indexrange[]])
				min=i;
		}
		return min;
	}
	
	public void sort(int[] s,int[] index) {
		int i,j,min,temp;
		for(i=0;i<s.length;i++) {
			min=i;
			for(j=i+1;j<s.length;j++) {
				if(s[min]>s[j])
					min=j;
			}
			temp=s[i];
			s[i]=s[min];
			s[min]=temp;
			
			temp=index[i];
			index[i]=index[min];
			index[min]=temp;
		}
	}
	
	
	public void getindex() {
		
	}
}
