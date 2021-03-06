import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Scanner;

public class Client {
	static final String MasterIP = "127.0.0.1";
	static final int MasterPort = 4320;
	static Socket clientToMasterSocket = null;
	static ObjectOutputStream outToMaster = null;
	static ObjectInputStream inFromMaster = null;
	static Query q = null;
	
	
	public static void initialize() {
		try {
			
			clientToMasterSocket = new Socket(MasterIP, MasterPort);

			System.out.println("Created Socket for master");//DEBUGGING
			sendQueryToServer(q);
			System.out.println("Sent Query to master");//DEBUGGING
			
			Routes result = getResults();
			
			showResults(result);

			outToMaster.close();
			inFromMaster.close();
			clientToMasterSocket.close();
			
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch(IOException ioException) {
				ioException.printStackTrace();
		}
	}
	
	private static void sendQueryToServer(Query q){
		try{
			outToMaster = new ObjectOutputStream(clientToMasterSocket.getOutputStream());
			outToMaster.writeObject(q);
			outToMaster.flush();
			System.out.println("Query flushed");//DEBUGGING
			} catch (IOException ioException) {
				ioException.printStackTrace();
		}
	}
	private static Query createQuery(Point a, Point b){
		Query query = new Query(a,b);
		return query;
	}
	private static Routes getResults(){
		Routes r = null;
		try {
			inFromMaster = new ObjectInputStream(clientToMasterSocket.getInputStream());
			System.out.println("Waiting for response...");//DEBUGGING
			r = (Routes) inFromMaster.readObject(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return r;
	}
	private static void showResults(Routes r){
		System.out.println(r.start.Lat + " " + r.start.Long);
		System.out.println(r.destination.Lat + " " + r.destination.Long);
		System.out.println(r.direction.direction);
	}
	public static void main(String args[]) throws IOException{
		/*Scanner scan = new Scanner(System.in).useLocale(Locale.US);
		System.out.println("Type in the starting point coordinates(Latitude then Longitude): ");
		Point startPoint = new Point(scan.nextDouble(), scan.nextDouble());
		System.out.println("Type in the destination point coordinates(Latitude then Longitude): ");
		Point destinationPoint = new Point(scan.nextDouble(), scan.nextDouble());
		scan.close();*/
		Point startPoint = new Point(38.064157, 23.772037);//test
		Point destinationPoint = new Point(38.055447, 23.833666);//test
		q = createQuery(startPoint, destinationPoint);
		initialize();
	}
}