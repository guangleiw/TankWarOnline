import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetCliet {

	private static int UDP_PORT_START = 2223;
	private int udp_port;
	Socket s = null;

	public NetCliet() {
		udp_port = UDP_PORT_START++;
	}

	public void connect(String ip, int port) {
		try {
			s = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udp_port);
			System.out.println("connected to server!");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
