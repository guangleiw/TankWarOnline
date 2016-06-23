import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetCliet {

	private static int UDP_PORT_START = 2223;
	private int udp_port;
	private TankClient tc;
	Socket s = null;
	private DatagramSocket ds = null;

	public NetCliet(TankClient tc) {
		udp_port = UDP_PORT_START++;
		this.tc =tc;
	}

	public void connect(String ip, int port) {
		try {
			s = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udp_port);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			tc.myTank.id = dis.readInt();
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
		
		TankNewMsg msg = new TankNewMsg(tc.myTank);
		send(msg);
	}
	
	private void send(TankNewMsg msg){
		msg.send(ds, "127.0.0.1", udp_port);
	}
	
	private class UDPThread implements Runnable{

		@Override
		public void run() {
			
		}
		
	}
	

}
