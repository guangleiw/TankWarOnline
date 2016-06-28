import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class MissileDeadMsg implements Msg {

	int id;
	TankClient tc;
	int tankId;
	int msgType = Msg.MISSILE_DEAD_MSG;

	public MissileDeadMsg(int tankID, int id) {
		this.tankId = tankID;
		this.id = id;
	}

	public MissileDeadMsg(TankClient tc) {
		this.tc = tc;
	}

	@Override
	public void send(DatagramSocket ds, String IP, int port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
			dos.writeInt(msgType);
			dos.writeInt(tankId);
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

		try {
			int tankID = dis.readInt();
//			if (tankID == tc.myTank.id)
//				return;
			int id = dis.readInt();
			for (int i = 0; i < tc.missiles.size(); i++) {
				Missile m = tc.missiles.get(i);
				if (m.tankId == tankID && id == m.id) {
					m.live = false;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
