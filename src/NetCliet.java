import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import sun.security.krb5.internal.NetClient;

public class NetCliet {

	private static int UDP_PORT_START = 2229;
	private int udp_port;
	private TankClient tc;
	Socket s = null;
	private DatagramSocket ds = null;

	public NetCliet(TankClient tc) {
		udp_port = UDP_PORT_START++;
		this.tc = tc;
		
		try {
			ds = new DatagramSocket(this.udp_port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		new Thread(new UDPRecvThread()).start();
	}

	public  void send(Msg msg) {
		msg.send(ds, "127.0.0.1", TankServer.UDP_PORT);
	}

	private class UDPRecvThread implements Runnable {

		byte[] buf = new byte[1024];

		@Override
		public void run() {
			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					System.out.println("A packet received from server !");
					parse(dp);					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void parse(DatagramPacket dp) {
			// TODO Auto-generated method stub
			ByteArrayInputStream bis = new ByteArrayInputStream(buf,0,dp.getLength());
			DataInputStream dis = new DataInputStream(bis);
			int msgType= 0;
			try {
				msgType = dis.readInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Msg msg = null;
			switch(msgType){
			case Msg.TANK_NEW_MSG:

				msg = new TankNewMsg(tc);
				break;
			case Msg.TANK_MOVE_MSG:
				msg = new TankMoveMsg(tc);
				break;
			}
			msg.parse(dis);
		}

	}

}
