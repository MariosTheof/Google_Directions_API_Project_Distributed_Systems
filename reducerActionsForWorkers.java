import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class reducerActionsForWorkers implements Runnable{

	ServerSocket reducerSocket;
	Socket connection = null;
	ObjectInputStream inFromWorker;
	ObjectOutputStream outToWorker;
	Routes [] r;
	public reducerActionsForWorkers(int port) {
		try {
			
			reducerSocket = new ServerSocket(port, 10);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){

		try {
			//while (true) {
			System.out.println("Opened Socket for worker with port " + reducerSocket.getLocalPort());//DEBUGGING
			connection = reducerSocket.accept();
			outToWorker = new ObjectOutputStream(connection.getOutputStream());
			inFromWorker = new ObjectInputStream(connection.getInputStream());

			System.out.println("Reading routes from worker...");//DEBUGGING
			r = (Routes[]) inFromWorker.readObject();

			System.out.println("Changing routes...");//DEBUGGING
			r[0].start.Lat ++;
			r[1].start.Lat --;
			r[2].start.Lat ++;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
			
	}
	public Routes[] getRoutes(){
		return r;
	}
}