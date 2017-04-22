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
	Boolean threadlock = false;
	Boolean startAPI = false;
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
			System.out.println("Sent query to worker with points: " + q.startPoint.Lat + "  " + q.startPoint.Long + " " + q.endPoint.Lat + " " + q.endPoint.Long);//DEBUGGING
			
			
			//q = (Query) inFromWorker.readObject(); //test
			
			workerDone = inFromWorker.readBoolean();

			System.out.println("Worker is done...");//DEBUGGING
			
			/*while (startAPI == null){
				Thread.sleep(500);
			}*/
			//Thread.sleep(2000);//test
			synchronized(threadlock){
				while(!threadlock){
					System.out.println("Waiting TRELE MOU...");
					threadlock.wait();
				}
			}
			
			outToWorker.writeBoolean(startAPI);
			outToWorker.flush();
			System.out.println("ELA");
			if(startAPI == true){
				r = (Routes) inFromWorker.readObject();
			}
			
			outToWorker.close();
			inFromWorker.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	public Query getQuery(){
		return q;
	}
	public Routes getAPIRoutes(){
		return r;
	}
}
