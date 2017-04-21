import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class masterActionsForWorkers implements Runnable{

	ObjectInputStream inFromWorker;
	ObjectOutputStream outToWorker;
	Query q = null;
	Routes r = null;
	Boolean workerDone = false;
	public masterActionsForWorkers(Socket connection, Query query) {
		try {
			q = query;
			outToWorker = new ObjectOutputStream(connection.getOutputStream());
			inFromWorker = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {			
			
			outToWorker.writeObject(q);
			outToWorker.flush();
			System.out.println("Sent query to worker...");//DEBUGGING
			
			
			//q = (Query) inFromWorker.readObject(); //test
			
			workerDone = inFromWorker.readBoolean();

			System.out.println("Worker is done...");//DEBUGGING
			//inFromWorker.close();
			//outToWorker.close();
		} catch (IOException e) {
			e.printStackTrace();
		}/*catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/ //test
	}
	public Query getQuery(){
		return q;
	}
}
