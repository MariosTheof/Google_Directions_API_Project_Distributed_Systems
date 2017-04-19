import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import us.monoid.web.Resty;
import java.io.UnsupportedEncodingException;


public class MapWorker extends Worker{
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
		new MapWorker(2, 4322).initialize();
		new MapWorker(3, 4323).initialize();
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

	public Directions askGoogleDirectionsAPI(String direction) {
		private static String BASE_URL = "http://maps.googleapis.com/maps/api/directions/json?";
		private static String ARGS = "origin=%s&destination=%s&sensor=true";
		private static String LOCATION_ARG = "%s,%s";
		private static String ENCODING = "UTF-8";

		try{
			String start = String.format(LOCATION_ARG, direction.lat0, direction.lng0);
			String end = String.format(LOCATION_ARG, direction.lat1, direction.lng1);
			String args = String.format(ARGS, encode(start), encode(end));
			String url = BASE_URL + args;

			return new Resty().text(url).toString();

		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private String encode(String arg) throws UnsupportedEncodingException {
        return URLEncoder.encode(arg, ENCODING);
    }
}
