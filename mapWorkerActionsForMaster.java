import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class mapWorkerActionsForMaster implements Runnable{
	
	static final String REDUCERIP = "127.0.0.1";
	static final int PORTFORREDUCER = 4444;
	
	ObjectInputStream inFromMaster;
	ObjectOutputStream outToMaster;
	
	Query q = null;
	Routes [] r;
	public mapWorkerActionsForMaster(Socket connection) {
		try {
			outToMaster = new ObjectOutputStream(connection.getOutputStream());
			inFromMaster = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {
			q = (Query) inFromMaster.readObject();
			r = map(q);
			//q.startPoint.Lat++; //test
			//outToMaster.writeObject(q); //test
			//TODO REDUCER THREAD

			System.out.println("Connecting with reducer with port " + PORTFORREDUCER);//DEBUGGING
			Socket connectiontoreducer = new Socket(REDUCERIP, PORTFORREDUCER);
			Thread reducerThread = new Thread(new mapWorkerActionsForReducer(connectiontoreducer, r));
			reducerThread.start();

			System.out.println("Started thread for Reducer...");//DEBUGGING
			outToMaster.writeBoolean(true);
			outToMaster.flush();
			System.out.println("Sent boolean to master...");//DEBUGGING
			
			//inFromMaster.close();
			//outToMaster.close();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	private Routes[] map(Query q){
		Routes[] routes = {new Routes(q), new Routes(q), new Routes(q)};
		//TODO TXT
		return routes;
	}
}
