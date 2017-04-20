import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class masterActionsForWorkers implements Runnable{

	ObjectInputStream inFromWorker;
	ObjectOutputStream outToWorker;
	Query q = null;
	Routes r = null;
	//Boolean workerDone = false;
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
			
			
			q = (Query) inFromWorker.readObject();
			 
			//workerDone = inFromWorker.readBoolean();
			
			inFromWorker.close();
			outToWorker.close();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public Query getQuery(){
		return q;
	}
}
