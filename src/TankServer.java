import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TankServer {

	public static final int TCP_PORT = 8876;
	private List<Client> clients = new ArrayList<Client>();

	public void start() {
		try {
			ServerSocket ss = new ServerSocket(TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpport = dis.readInt();
				Client c = new Client(s.getInetAddress().getHostAddress(), udpport);
				System.out.println("A client connect! Addr:" + s.getInetAddress() + ":" + s.getPort());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TankServer().start();
	}

	private class Client {
		private String ip;
		private int port;

		public Client(String ip, int port) {
			this.ip = ip;
			this.port = port;
		}
	}

}
