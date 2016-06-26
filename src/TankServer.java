import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class TankServer {

	public static final int TCP_PORT = 8876;
	public static final int UDP_PORT = 6676;
	private List<Client> clients = new ArrayList<Client>();
	private int start_tank_id = 100;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TankServer().start();
	}

	public void start() {

		new Thread(new UDPThread()).start();

		Socket s = null;
		try {
			ServerSocket ss = new ServerSocket(TCP_PORT);
			while (true) {
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpport = dis.readInt();
				Client c = new Client(s.getInetAddress().getHostAddress(), udpport);
				clients.add(c);
				System.out.println("A client connect! Addr:" + s.getInetAddress() + ":" + s.getPort());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(start_tank_id++);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != s)
					s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class Client {
		private String ip;
		private int port;

		public Client(String ip, int port) {
			this.ip = ip;
			this.port = port;
		}
	}

	private class UDPThread implements Runnable {

		byte[] buf = new byte[1024];

		@Override
		public void run() {

			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("UDP thread has started!.");
			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					for (int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.ip, c.port));
						ds.send(dp);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

}
