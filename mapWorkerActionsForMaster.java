import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class mapWorkerActionsForMaster implements Runnable{

	ObjectInputStream inFromMaster;
	ObjectOutputStream outToMaster;
	Query q = null;
	Routes r = null;
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
			q.startPoint.Lat++;
			outToMaster.writeObject(q);
			//outToMaster.writeBoolean(true);
			outToMaster.flush();
			
			inFromMaster.close();
			outToMaster.close();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
}
