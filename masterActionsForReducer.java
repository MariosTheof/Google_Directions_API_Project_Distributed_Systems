import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class masterActionsForReducer implements Runnable{

	ObjectInputStream inFromReducer;
	ObjectOutputStream outToReducer;
	Routes r[];//TODO ROUTES LIST
	public masterActionsForReducer(Socket connection){
		try{
			outToReducer = new ObjectOutputStream(connection.getOutputStream());
			inFromReducer = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			outToReducer.writeBoolean(true);
			outToReducer.flush();

			System.out.println("Sent boolean to reducer...");//DEBUGGING
			//int length = inFromReducer.readInt(); //test
			//r = new Routes[length];
			
			r = (Routes[]) inFromReducer.readObject();
			
			
			

			outToReducer.close();
			inFromReducer.close();
			
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
//TODO EUCLEDEAN
