import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class masterActionsForAPI implements Runnable{

	ObjectInputStream inFromWorker;
	ObjectOutputStream outToWorker;
	Query q = null;
	Routes r = null;
	//Boolean workerDone = false;
	public masterActionsForAPI(Socket connection, Query query) {
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
			
			
			//q = (Query) inFromWorker.readObject(); //test
			 
			//workerDone = inFromWorker.readBoolean();

			outToWorker.close();
			inFromWorker.close();
		} catch (IOException e) {
			e.printStackTrace();
		}/*catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/ //test
	}
}