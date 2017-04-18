import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class masterActionsForClient implements Runnable{

	ObjectInputStream inFromClient;
	ObjectOutputStream outToClient;
	Query q = null;
	Routes r = null;
	public masterActionsForClient(Socket connectionWithClient) {
		try {
			outToClient = new ObjectOutputStream(connectionWithClient.getOutputStream());
			inFromClient = new ObjectInputStream(connectionWithClient.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {
			q = (Query) inFromClient.readObject();
			for(int i = 0; i<10; i++){
				q.startPoint.Lat ++;
				q.startPoint.Long --;
				System.out.println(q.startPoint.Lat + " " + q.startPoint.Long);
			}
			r = new Routes(q.startPoint, q.endPoint, null);
			outToClient.writeObject(r);
			outToClient.flush();
			inFromClient.close();
			outToClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	private Routes searchCache(){
		for(int i = 0; i < Master.cache.length; i++){
			if(Master.cache[i].start == q.startPoint && Master.cache[i].destination == q.endPoint){}
		}
		return null;
	}
	private void updateCache(){}
}
