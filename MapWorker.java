import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import us.monoid.web.Resty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MapWorker extends Worker{
	private static String BASE_URL = "http://maps.googleapis.com/maps/api/directions/json?";
	private static String ARGS = "origin=%s&destination=%s&sensor=true";
	private static String LOCATION_ARG = "%s,%s";
	private static String ENCODING = "UTF-8";
	final int WORKERID;
	int portformaster;
	ServerSocket workerSocket;
	Socket connection = null;
	MapWorker(int id, int p){
		this.WORKERID = id;
		this.portformaster = p;
	}
	public static void main(String args[]){
		new MapWorker(1, 4321).initialize();//TODO CHANGE PORTS AND WORKERID ACCORDINGLY
	}
	@Override
	public void initialize() {
		waitForTasksThread();
	}

	@Override
	public void waitForTasksThread() {
		try {
			workerSocket = new ServerSocket(this.portformaster, 10);
			System.out.println("Opened Socket for Master with port " + this.portformaster);//DEBUGGING

			//while (true) {
				connection = workerSocket.accept();
				
				Thread masterThread = new Thread(new mapWorkerActionsForMaster(connection));
				masterThread.start();
				System.out.println("Started thread for master...");//DEBUGGING
				masterThread.join();
				
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				workerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
		
	}

	public Directions askGoogleDirectionsAPI(Query q) {

		try{
			String start = String.format(LOCATION_ARG, q.startPoint.Lat, q.startPoint.Long);
			String end = String.format(LOCATION_ARG, q.endPoint.Lat, q.endPoint.Long);
			String args = String.format(ARGS, encode(start), encode(end));
			String url = BASE_URL + args;

			return new Directions(new Resty().text(url).toString());

		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private String encode(String arg) throws UnsupportedEncodingException {
        return URLEncoder.encode(arg, ENCODING);
    }
}