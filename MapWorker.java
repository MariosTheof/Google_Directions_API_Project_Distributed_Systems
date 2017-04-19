import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MapWorker extends Worker{
	final int WORKERID;
	int port;
	ServerSocket workerSocket;
	Socket connection = null;
	MapWorker(int id, int p){
		this.WORKERID = id;
		this.port = p;
	}
	public static void main(String args[]){
		new MapWorker(1, 4321).initialize();
		new MapWorker(2, 4322).initialize();
		new MapWorker(3, 4323).initialize();
	}
	@Override
	public void initialize() {
		waitForTasksThread();
	}

	@Override
	public void waitForTasksThread() {
		try {
			workerSocket = new ServerSocket(this.port, 10);

			//while (true) {
				connection = workerSocket.accept();

				Thread masterThread = new Thread(new mapWorkerActionsForMaster(connection));
				masterThread.start();
				
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				workerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
		
	}
	
}
