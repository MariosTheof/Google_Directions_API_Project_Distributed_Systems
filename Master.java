import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Master{
	Integer port;
	static final String WORKER1IP = "127.0.0.1";
	static final String WORKER2IP = "127.0.0.1";
	static final String WORKER3IP = "127.0.0.1";
	static final String REDUCERIP = "127.0.0.1";
	static BigInteger WHArray[] = {md5hash(WORKER1IP + "4321"), md5hash(WORKER2IP + "4322"), md5hash(WORKER3IP + "4323")};
	
	
	/*static final int CLIENT_ENUM = 0;
	static final int WORKER_ENUM = 1;
	static final int REDUCER_ENUM = 2;*/
	Master(int P){
		this.port = P;
	}
	public static void main(String args[]) {
		Master.hashorder();
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
				try {
					clientThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
	public TApair initialize(int WorkerID, Query q){
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
				return new TApair(workerThread, mAFW);
			//}
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return null;
		}
		//TODO close
	}
	
	public TApair initialize(boolean b){//TODO MIGHT HAVE TO CHANGE ARG
		try {
			//while (true) {
			connection = new Socket(REDUCERIP, port);

			masterActionsForReducer mAFR = new masterActionsForReducer(connection);
			Thread reducerThread = new Thread(mAFR);
			reducerThread.start();
			return new TApair(reducerThread, mAFR);
			//}
		}  catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BigInteger md5hash(String s) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(s.getBytes(), 0, s.length());	
		return new BigInteger(1, m.digest());
	}
	
	private static void hashorder(){
		int[] order = new int[3];
		BigInteger max = Master.WHArray[0];
		BigInteger min = Master.WHArray[0];
		int maxi = 0;
		int mini = 0;
		for(int i = 0; i < 2; i++){
			if(max.compareTo(Master.WHArray[i + 1]) == -1){
				max = Master.WHArray[i + 1];
				maxi = i + 1;
			}
		}
		order[0] = maxi;
		for(int i = 0; i < 2; i++){
			if(min.compareTo(Master.WHArray[i + 1]) == 1){
				min = Master.WHArray[i + 1];
				mini = i + 1;
			}
		}
		order[2] = mini;
		order[1] = 3 - order[0] - order[2];
		BigInteger temp[] = new BigInteger[3];
		for(int i = 0; i < 3; i++){
			temp[i] = WHArray[order[i]];
		}
		WHArray = temp;
	}
}