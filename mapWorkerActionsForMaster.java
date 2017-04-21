import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class mapWorkerActionsForMaster implements Runnable{
	
	static final String REDUCERIP = "127.0.0.1";
	static final int PORTFORREDUCER = 4331;
	
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
			System.out.println("Waiting for query from master...");//DEBUGGING
			q = (Query) inFromMaster.readObject();

			System.out.println("Got query from master with points: " + q.startPoint.Lat + "  " + q.startPoint.Long + " " + q.endPoint.Lat + " " + q.endPoint.Long);//DEBUGGING
			r = map(q);

			System.out.println("map function returned r array with 1st query with points: " + r[0].start.Lat + "  " + r[0].start.Long + " " + r[0].destination.Lat + " " + r[0].destination.Long);//DEBUGGING
			//q.startPoint.Lat++; //test
			//outToMaster.writeObject(q); //test

			System.out.println("Connecting with reducer with port " + PORTFORREDUCER);//DEBUGGING
			Socket connectiontoreducer = new Socket(REDUCERIP, PORTFORREDUCER);
			Thread reducerThread = new Thread(new mapWorkerActionsForReducer(connectiontoreducer, r));
			reducerThread.start();

			System.out.println("Started thread for Reducer...");//DEBUGGING
			outToMaster.writeBoolean(true);
			outToMaster.flush();
			System.out.println("Sent boolean to master...");//DEBUGGING
			
			outToMaster.close();
			inFromMaster.close();
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
