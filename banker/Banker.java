package banker;

public class Banker {
	int[][] Allocation = {{3,0,2},
						 {2,1,1},
						 {0,0,2},
						 {0,1,0},
						 {3,0,2}};
	int[][] Need = {{0,2,0},
			 		{0,1,1},
			 		{4,3,1},
			 		{7,4,3},
			 		{6,0,0}};
	int[] Available = {2,3,0};
	int[] Work;
	boolean[] Finish;
	int count=0;
	int[] Record;
	int row = 5;
	int column = 3;
	
	public Banker() {
		Finish = new boolean[row];
		for(int i=0;i<row;i++)
			Finish[i]=false;
		Record = new int[row];
	}
	
	
	public void myBanker() {
		Work=Available;
		int i;
		while(count<row-1) {
			i=getNeed();
			Record[count]=i;
			count=count+1;
			Finish[i]=true;
			addWork(Allocation[i]);
		}
		
	}
	
	public void addWork(int[] Alloc) {
		for(int i=0;i<Alloc.length;i++) {
			Work[i]=Work[i]+Alloc[i];
		}
		printWork();
	}
	public void printWork() {
		for(int x:Work)
			System.out.print(x+" ");
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
