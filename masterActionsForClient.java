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
			r = searchCache();
			if(r == null){
				//TODO WORKERS, API:String Parser(URL + JSON)
			}
			
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
			if(Master.cache[i].start.equals(q.startPoint) && Master.cache[i].destination.equals(q.endPoint)){
				return Master.cache[i];
			}
		}
		return null;
	}
	private void updateCache(Routes r){
		if(Master.oldestCachedRoute < 100){
			Master.cache[Master.oldestCachedRoute] = r;
			Master.oldestCachedRoute++;
		}
		else{
			Master.oldestCachedRoute = 0;
			updateCache(r);
		}
	}
}
