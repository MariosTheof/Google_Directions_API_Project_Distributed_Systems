import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;

import us.monoid.web.Resty;

public class mapWorkerActionsForMaster implements Runnable{
	
	static final String REDUCERIP = "127.0.0.1";
	static final int PORTFORREDUCER = 4331;
	
	private static String BASE_URL = "http://maps.googleapis.com/maps/api/directions/json?";
	private static String ARGS = "origin=%s&destination=%s&sensor=true";
	private static String LOCATION_ARG = "%s,%s";
	private static String ENCODING = "UTF-8";
	
	ObjectInputStream inFromMaster;
	ObjectOutputStream outToMaster;
	
	Query q = null;
	Routes [] r;
	Directions dfromgoogle;
	Routes rfromgoogle;
	public mapWorkerActionsForMaster(Socket connection) {
		try {
			outToMaster = new ObjectOutputStream(connection.getOutputStream());
			inFromMaster = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {
			System.out.println("Waiting for query from master...");//DEBUGGING
			q = (Query) inFromMaster.readObject();

			System.out.println("Got query from master with points: " + q.startPoint.Lat + "  " + q.startPoint.Long + " " + q.endPoint.Lat + " " + q.endPoint.Long);//DEBUGGING
			r = map(q);

			//System.out.println("map function returned r array with 1st query with points: " + r[0].start.Lat + "  " + r[0].start.Long + " " + r[0].destination.Lat + " " + r[0].destination.Long);//DEBUGGING
			//q.startPoint.Lat++; //test
			//outToMaster.writeObject(q); //test

			System.out.println("Connecting with reducer with port " + PORTFORREDUCER);//DEBUGGING
			Socket connectiontoreducer = new Socket(REDUCERIP, PORTFORREDUCER);
			Thread reducerThread = new Thread(new mapWorkerActionsForReducer(connectiontoreducer, r));
			reducerThread.start();

			System.out.println("Started thread for Reducer...");//DEBUGGING
			outToMaster.writeBoolean(true);
			outToMaster.flush();
			System.out.println("Sent boolean to master...");//DEBUGGING
			
			Boolean b = inFromMaster.readBoolean();
			System.out.println("Got boolean from master...");//DEBUGGING
			if (b == true){
				System.out.println("About to ask google...");//DEBUGGING
				dfromgoogle = askGoogleDirectionsAPI(q);
				System.out.println("About to ask create route...");//DEBUGGING
				rfromgoogle = new Routes(q, dfromgoogle);
				System.out.println("About to send route...");//DEBUGGING
				outToMaster.writeObject(rfromgoogle);
				outToMaster.flush();
			}
			
			outToMaster.close();
			inFromMaster.close();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	private Routes[] map(Query q){
		//Routes[] routes = {new Routes(q), new Routes(q), new Routes(q)};//test
		//TODO TXT
		return null;//test
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
