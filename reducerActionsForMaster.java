import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class reducerActionsForMaster implements Runnable{

	ServerSocket reducerSocket;
	Socket connection = null;
	ObjectInputStream inFromMaster;
	ObjectOutputStream outToMaster;
	Boolean mastersignal = false;
	int portsforworkers[] = {4331, 4332, 4333};
	Routes [] r;
	public reducerActionsForMaster(int port) {
		try {
			
			reducerSocket = new ServerSocket(port, 10);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){

		reducerActionsForWorkers rAFW1 = new reducerActionsForWorkers(portsforworkers[0]);
		Thread worker1Thread = new Thread(rAFW1);
		worker1Thread.start();
		System.out.println("Created thread for worker...");//DEBUGGING
		TApair taw1 = new TApair (worker1Thread, rAFW1);
		/*
		reducerActionsForWorkers rAFW2 = new reducerActionsForWorkers(portsforworkers[1]);
		Thread worker2Thread = new Thread(rAFW2);
		worker2Thread.start();
		TApair taw2 = new TApair (worker2Thread, rAFW2);
		
		reducerActionsForWorkers rAFW3 = new reducerActionsForWorkers(portsforworkers[2]);
		Thread worker3Thread = new Thread(rAFW3);
		worker3Thread.start();
		TApair taw3 = new TApair (worker3Thread, rAFW3);*/
		try {
			//while (true) {
			System.out.println("Opened Socket for Master with port " + reducerSocket.getLocalPort());//DEBUGGING
			connection = reducerSocket.accept();
			outToMaster = new ObjectOutputStream(connection.getOutputStream());
			inFromMaster = new ObjectInputStream(connection.getInputStream());
			
			mastersignal = inFromMaster.readBoolean();
			System.out.println("Got signal from master...");//DEBUGGING
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			taw1.thread.join();
			System.out.println("thread for worker just died... " + taw1.thread.isAlive());//DEBUGGING
			//taw2.thread.join();
			//taw3.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Worker Thread 1 ended...");//DEBUGGING
		//r = MergeRoutes(taw1.actionsrw.getRoutes(), taw2.actionsrw.getRoutes(), taw3.actionsrw.getRoutes());
		Routes r1[] = taw1.actionsrw.getRoutes();

		System.out.println("thread for worker returned r array with 1st query with points: " + r1[0].start.Lat + "  " + r1[0].start.Long + " " + r1[0].destination.Lat + " " + r1[0].destination.Long);//DEBUGGING
		r = MergeRoutes(r1, r1, r1);

		System.out.println("Merge Finished...");//DEBUGGING
		try {
			System.out.println("Sending routes to master...");//DEBUGGING
			outToMaster.writeObject(r);
			outToMaster.flush();
			
			
			
			outToMaster.close();
			inFromMaster.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private Routes[] MergeRoutes(Routes [] r1, Routes [] r2, Routes [] r3){
		if(r1 == null && r2 == null && r3 == null){
			System.out.println("einai null");
			return null;
		}
		if(r1.length == 0 && r2.length == 0 && r3.length == 0){
			return null;
		}
		r = new Routes [r1.length + r2.length + r3.length];
		for (int i = 0; i < r1.length + r2.length + r3.length; i++){
			if(i < r1.length){
				r[i] = r1[i];
			}else if(i < r1.length + r2.length){
				r[i] = r2[i - r1.length];
			}else if(i < r1.length + r2.length + r3.length){
				r[i] = r3[i - r1.length - r2.length];				
			}
		}
		return r;
	}
}