import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankDeadMsg implements Msg {

	int msgType = Msg.TANK_DEAD_MSG;
	private int id;
	public TankClient tc = null;

	public TankDeadMsg(int id) {
		this.id = id;
	}

	public TankDeadMsg(TankClient tc) {
		this.tc = tc;
	}

	@Override
	public void send(DatagramSocket ds, String IP, int port) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, port));
			ds.send(dp);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		int id;
		try {
			id = dis.readInt();
			if (id == tc.myTank.id)
				return;
			for (int i = 0; i < tc.tanks.size(); i++) {
				Tank t = tc.tanks.get(i);
				if (id == t.id) {
					t.setLive(false);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
