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
	int port;
	ServerSocket workerSocket;
	Socket connection = null;
	MapWorker(int id, int p){
		this.WORKERID = id;
		this.port = p;
	}
	public static void main(String args[]){
		new MapWorker(1, 4321).initialize();
		//new MapWorker(2, 4322).initialize();//TODO
		//new MapWorker(3, 4323).initialize();
	}
	@Override
	public void initialize() {
		waitForTasksThread();
	}

	@Override
	public void waitForTasksThread() {
		try {
			workerSocket = new ServerSocket(this.port, 10);

			//while (true) {
				connection = workerSocket.accept();

				Thread masterThread = new Thread(new mapWorkerActionsForMaster(connection));
				masterThread.start();
				
		} catch (IOException ioException) {
			ioException.printStackTrace();
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