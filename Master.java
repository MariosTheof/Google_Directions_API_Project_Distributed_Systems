import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Master{
	int port;
	static final String WORKER1IP = "127.0.0.1";
	static final String WORKER2IP = "127.0.0.1";
	static final String WORKER3IP = "127.0.0.1";
	static final String REDUCERIP = "127.0.0.1";
	/*static final int CLIENT_ENUM = 0;
	static final int WORKER_ENUM = 1;
	static final int REDUCER_ENUM = 2;*/
	Master(int P){
		this.port = P;
	}
	public static void main(String args[]) {
		new Master(4320).initialize();//CHANGE THE PORT ARGUMENT DEPENDING ON THE COMPUTER THE MAPPER WILL RUN ON (THE OTHER 2 MAPPERS WILL HAVE PORTS 4322, 4323)
	}

	ServerSocket masterSocket;
	Socket connection = null;
	static Routes cache[] = new Routes[100];
	static int oldestCachedRoute = 0;
	public void initialize() {
		try {
			masterSocket = new ServerSocket(this.port, 10);

			//while (true) {
				connection = masterSocket.accept();

				Thread clientThread = new Thread(new masterActionsForClient(connection));
				clientThread.start();
				
				/*if(en == CLIENT_ENUM){
					Thread clientThread = new Thread(new masterActionsForClient(connection));
					clientThread.start();
				}
				else if(en == WORKER_ENUM){
					Thread workerThread = new Thread(new masterActionsForWorkers(connection));
					workerThread.start();					
				}
				else if(en == REDUCER_ENUM){
					Thread reducerThread = new Thread(new masterActionsForReducer(connection));
					reducerThread.start();					
				}*/
				
			//}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				masterSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	public void initialize(int WorkerID, Query q){
		try {

			//while (true) {
			if(WorkerID == 1){
				connection = new Socket(WORKER1IP, port);
			}
			else if(WorkerID == 2){
				connection = new Socket(WORKER2IP, port);
			}
			else if(WorkerID == 3){
				connection = new Socket(WORKER3IP, port);
			}
				masterActionsForWorkers mAFW = new masterActionsForWorkers(connection, q);
				Thread workerThread = new Thread(mAFW);
				workerThread.start();
			//}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				masterSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
	}
}