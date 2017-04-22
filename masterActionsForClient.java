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
			//if(endresult == null){
			System.out.println("Got query from client with points: " + q.startPoint.Lat + "  " + q.startPoint.Long + " " + q.endPoint.Lat + " " + q.endPoint.Long);//DEBUGGING
				TApair taw1 = new Master(4321).initialize(1, q);
				//TApair taw2 = new Master(4322).initialize(2, q);
				//TApair taw3 = new Master(4323).initialize(3, q);
				try {
					while(taw1.actionsw.workerDone == false /* && */){
						Thread.sleep(250);
					}
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
					/*try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {//test
						e1.printStackTrace();
					}*/
					QueryHash = Master.md5hash(Double.toString(q.startPoint.Lat)+ Double.toString(q.startPoint.Long) + Double.toString(q.endPoint.Lat) + Double.toString(q.endPoint.Long));
					int WorkerIDforAPI = assignWorker(QueryHash);
					System.out.println("WorkerIDforAPI: " + WorkerIDforAPI);//test
					synchronized(taw1.actionsw.threadlock){
						System.out.println("About to notify the other thread...");
						taw1.actionsw.threadlock.notify();
						taw1.actionsw.threadlock = true;//test
						taw1.actionsw.startAPI = true;//test
					}
					endresult = taw1.actionsw.getAPIRoutes();//test
					try {
						if(WorkerIDforAPI == 1){
							synchronized(taw1.actionsw.threadlock){
								System.out.println("About to notify the other thread...");
								taw1.actionsw.threadlock.notify();
								taw1.actionsw.threadlock = true;//test
								taw1.actionsw.startAPI = true;//test
							}
							/*
							synchronized(taw2.actionsw.threadlock){
								System.out.println("About to notify the other thread...");
								taw2.actionsw.threadlock.notify();
								taw2.actionsw.threadlock = true;//test
								taw2.actionsw.startAPI = false;//test
							}*/
							/*
							synchronized(taw3.actionsw.threadlock){
								System.out.println("About to notify the other thread...");
								taw3.actionsw.threadlock.notify();
								taw3.actionsw.threadlock = true;//test
								taw3.actionsw.startAPI = false;//test
							}*/
							taw1.thread.join();
							endresult = taw1.actionsw.getAPIRoutes();
						}else if(WorkerIDforAPI == 2){
							synchronized(taw1.actionsw.threadlock){
								System.out.println("About to notify the other thread...");
								taw1.actionsw.threadlock.notify();
								taw1.actionsw.threadlock = true;//test
								taw1.actionsw.startAPI = false;//test
							}
							/*
							synchronized(taw2.actionsw.threadlock){
								System.out.println("About to notify the other thread...");
								taw2.actionsw.threadlock.notify();
								taw2.actionsw.threadlock = true;//test
								taw2.actionsw.startAPI = true;//test
							}*/
							/*
							synchronized(taw3.actionsw.threadlock){
								System.out.println("About to notify the other thread...");
								taw3.actionsw.threadlock.notify();
								taw3.actionsw.threadlock = true;//test
								taw3.actionsw.startAPI = false;//test
							}*/
							//taw2.thread.join();
							//endresult = taw2.actionsw.getAPIRoutes();						
						}else if(WorkerIDforAPI == 3){
							synchronized(taw1.actionsw.threadlock){
								System.out.println("About to notify the other thread...");
								taw1.actionsw.threadlock.notify();
								taw1.actionsw.threadlock = true;//test
								taw1.actionsw.startAPI = false;//test
							}
							/*
							synchronized(taw2.actionsw.threadlock){
								System.out.println("About to notify the other thread...");
								taw2.actionsw.threadlock.notify();
								taw2.actionsw.threadlock = true;//test
								taw2.actionsw.startAPI = false;//test
							}*/
							/*
							synchronized(taw3.actionsw.threadlock){
								System.out.println("About to notify the other thread...");
								taw3.actionsw.threadlock.notify();
								taw3.actionsw.threadlock = true;//test
								taw3.actionsw.startAPI = true;//test
							}*/
							//taw3.thread.join();	
							//endresult = taw3.actionsw.getAPIRoutes();					
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				} else{
					synchronized(taw1.actionsw.threadlock){
						System.out.println("About to notify the other thread...");
						taw1.actionsw.threadlock.notify();
						taw1.actionsw.threadlock = true;//test
						taw1.actionsw.startAPI = false;//test
					}
					/*
					synchronized(taw2.actionsw.threadlock){
						System.out.println("About to notify the other thread...");
						taw2.actionsw.threadlock.notify();
						taw2.actionsw.threadlock = true;//test
						taw2.actionsw.startAPI = false;//test
					}*/
					/*
					synchronized(taw3.actionsw.threadlock){
						System.out.println("About to notify the other thread...");
						taw3.actionsw.threadlock.notify();
						taw3.actionsw.threadlock = true;//test
						taw3.actionsw.startAPI = false;//test
					}*/
					//TODO FIND BEST RESULT WITH EUCLEDEAN METHOD
					endresult = tar.actionsr.getRoutes()[0];
				}

				
				updateCache(endresult);
			//}
			outToClient.writeObject(endresult);
			outToClient.flush();

			outToClient.close();
			inFromClient.close();
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
