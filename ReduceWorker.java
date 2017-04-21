import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ReduceWorker extends Worker{
	//private static final int THREADFORWORKER = 1;
	//private static final int THREADFORMASTER = 2;
	//int threadtype;
	int port;
	//ServerSocket reducerSocket;
	//Socket connection = null;
	ReduceWorker(int p){
		this.port = p;
		//this.threadtype = tt;
	}

	public static void main(String args[]){
		new ReduceWorker(4324).initialize();
	}
	@Override
	public void initialize() {
		waitForTasksThread();
	}
	@Override
	public void waitForTasksThread() {
			
				//reducerSocket = new ServerSocket(this.port, 10);

				//while (true) {
					//connection = reducerSocket.accept();
					//TODO MAYBE OBJECT
					Thread masterThread = new Thread(new reducerActionsForMaster(this.port));
					masterThread.start();
					System.out.println("Started thread for master...");//DEBUGGING
					
					
			/* catch (IOException ioException) {
				ioException.printStackTrace();
			} finally {
				try {
					reducerSocket.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}*/
	}
}	







/*
threadformaster
	threadforworker
		accept
	threadforworker
		accept
	threadforworker
		accept
	accept
	join 3 threads
*/	























