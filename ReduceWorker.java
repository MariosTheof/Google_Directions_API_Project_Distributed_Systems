import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ReduceWorker extends Worker{
	private static final int TOP_K = 10;
	int Port;
	ReduceWorker(int P){
		this.Port = P;
	}
	public static void main(String args[]) {
		new ReduceWorker(4320).initialize();
	}
	ObjectInputStream inFromMapper1;
	ObjectInputStream inFromMapper2;
	ObjectInputStream inFromMapper3;
	ObjectOutputStream outToClient;
	ServerSocket reducerSocket;
	Socket connectionToMapper1 = null;
	Socket connectionToMapper2 = null;
	Socket connectionToMapper3 = null;
	Socket connectionToDummyClient = null;


	public void initialize() {
		try {
			reducerSocket = new ServerSocket(this.Port, 10);
			connectionToDummyClient = reducerSocket.accept();

			//while (true) {
			connectionToMapper1 = reducerSocket.accept();
			try {
				inFromMapper1 = new ObjectInputStream(connectionToMapper1.getInputStream());
				try {

					PointOfInterest [] PointOfInterestArray1 = (PointOfInterest []) inFromMapper1.readObject();

			connectionToMapper2 = reducerSocket.accept();

				inFromMapper2 = new ObjectInputStream(connectionToMapper2.getInputStream());

					PointOfInterest [] PointOfInterestArray2 = (PointOfInterest []) inFromMapper2.readObject();

			connectionToMapper3 = reducerSocket.accept();

				inFromMapper3 = new ObjectInputStream(connectionToMapper3.getInputStream());

					PointOfInterest [] PointOfInterestArray3 = (PointOfInterest []) inFromMapper3.readObject();
					PointOfInterest [] MergedPOIArray = new PointOfInterest[PointOfInterestArray1.length + PointOfInterestArray2.length + PointOfInterestArray3.length];
					for(int i = 0; i < PointOfInterestArray1.length; i++){
						MergedPOIArray[i] = PointOfInterestArray1[i];
					}
					for(int i = 0; i < PointOfInterestArray2.length; i++){
						MergedPOIArray[i + PointOfInterestArray1.length] = PointOfInterestArray2[i];
					}
					for(int i = 0; i < PointOfInterestArray3.length; i++){
						MergedPOIArray[i + PointOfInterestArray1.length + PointOfInterestArray2.length] = PointOfInterestArray3[i];
					}
					QuickSort.quickSort(MergedPOIArray, 0, MergedPOIArray.length - 1);
					PointOfInterest [] FinalTopK = new PointOfInterest[TOP_K];
					for (int i = MergedPOIArray.length - 1; i >= MergedPOIArray.length-TOP_K; i--) {
						FinalTopK[MergedPOIArray.length - i - 1] = MergedPOIArray[i];
		 			}
					outToClient = new ObjectOutputStream(connectionToDummyClient.getOutputStream());
					outToClient.writeObject(FinalTopK);
					outToClient.flush();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inFromMapper1.close();
					inFromMapper2.close();
					inFromMapper3.close();
					outToClient.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
			//}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				reducerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	/* Needs
	
	waitForMasteAck()
	reduce(String, Object)
	sendResults(Map<Integer,Object>)

	*/

}
