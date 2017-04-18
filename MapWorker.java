import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;


public class MapWorker extends Worker{
	int Port;
	MapWorker(int P){
		this.Port = P;
	}
	public static void main(String args[]) {
		new MapWorker(4321).initialize();//CHANGE THE PORT ARGUMENT DEPENDING ON THE COMPUTER THE MAPPER WILL RUN ON (THE OTHER 2 MAPPERS WILL HAVE PORTS 4322, 4323)
	}

	ServerSocket mapperSocket;
	Socket connectionToDummyClient = null;


	public void initialize() {
		try {
			mapperSocket = new ServerSocket(this.Port, 10);

			//while (true) {
				connectionToDummyClient = mapperSocket.accept();

				Thread t = new mapperActions(connectionToDummyClient);
				t.start();

			//}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				mapperSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}




	/*Needs
	map(Object, Object)	// Map<String, Object>
	notifyMaster();
	calculateHash();
	sendToReducers(Map<String,Object>);


	*/
}
