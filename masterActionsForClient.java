import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

public class masterActionsForClient implements Runnable{

	ObjectInputStream inFromClient;
	ObjectOutputStream outToClient;
	Query q = null;
	Routes endresult = null;
	BigInteger QueryHash;
	public masterActionsForClient(Socket connection) {
		try {
			outToClient = new ObjectOutputStream(connection.getOutputStream());
			inFromClient = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {
			q = (Query) inFromClient.readObject();
			//endresult = searchCache();
			//if(endresult == null){//TODO UNCOMMENT
						
				TApair taw1 = new Master(4321).initialize(1, q);
				//TApair taw2 = new Master(4322).initialize(2, q);
				//TApair taw3 = new Master(4323).initialize(3, q);
				try {
					taw1.thread.join();
					//taw2.thread.join();
					//taw3.thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//q = ta.actions.getQuery(); //test
				//r = new Routes(q); //test
				

				TApair tar = new Master(4324).initialize(true);
				try {
					tar.thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(tar.actionsr.getRoutes() == null){

					//QueryHash = Master.md5hash(Double.toString(q.startPoint.Lat)+ Double.toString(q.startPoint.Long) + Double.toString(q.endPoint.Lat) + Double.toString(q.endPoint.Long));

					//TODO CASE ASSIGNWORKER FOR GOOGLE DIRECTIONS API
				} else{
					//TODO FIND BEST RESULT WITH EUCLEDEAN METHOD
				}
				//updateCache(r);
			//}
			endresult = tar.actionsr.getRoutes()[0];
			outToClient.writeObject(endresult);
			outToClient.flush();
			
			//inFromClient.close();
			//outToClient.close();
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
		synchronized(Master.cache){
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
	private int assignWorker(BigInteger qh){
		for(int i = 0; i < 3; i++) {
			if(QueryHash.mod(Master.WHArray[0]).compareTo(Master.WHArray[2 - i]) == -1){
				return i + 1;
			}
		}
		return -1;
	}
}
