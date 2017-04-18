import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Master{
	int portForClients;
	Master(int P){
		this.portForClients = P;
	}
	public static void main(String args[]) {
		new Master(4320).initialize();//CHANGE THE PORT ARGUMENT DEPENDING ON THE COMPUTER THE MAPPER WILL RUN ON (THE OTHER 2 MAPPERS WILL HAVE PORTS 4322, 4323)
	}

	ServerSocket masterSocket;
	Socket connectionWithClient = null;
	static Routes cache[] = new Routes[100];
	public void initialize() {
		try {
			masterSocket = new ServerSocket(this.portForClients, 10);

			//while (true) {
				connectionWithClient = masterSocket.accept();
				boolean cachefound = false;
				
				if (!cachefound){
					Thread clientThread = new Thread(new masterActionsForClient(connectionWithClient));
					clientThread.start();
				}
			//}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				masterSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
}