package lifegame;

public class MultiThread implements Runnable{
	private BoardModel model;
	private boolean flag;
	public MultiThread(BoardModel m) {
		this.model = m;
		flag = true;
	}
	
	public void run() {
		while(flag) {
			try {
				Thread.sleep(1000);
				//System.out.println("MultiTread: " + (i + 1));
				model.next();
			}catch(InterruptedException e) {
				System.out.println("Interrupted");
			}
		}
	}
}
