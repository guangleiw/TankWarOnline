import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankNewMsg implements Msg {

	int msgType = Msg.TANK_NEW_MSG;

	Tank tank;
	TankClient tc = null;

	public TankNewMsg(Tank tank) {
		this.tank = tank;
	}

	public TankNewMsg(TankClient tc) {
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int udpport) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
			dos.writeInt(msgType);
			dos.writeInt(tank.id);
			dos.writeInt(tank.x);
			dos.writeInt(tank.y);
			dos.writeInt(tank.dir.ordinal());
			dos.writeBoolean(tank.good);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, udpport));
			ds.send(dp);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		int id;
		try {
			id = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			Dir dir = Dir.values()[dis.readInt()];
			boolean good = dis.readBoolean();
			if (id == tc.myTank.id)
				return;

			boolean exist = false;
			for (int i = 0; i < tc.tanks.size(); i++) {
				Tank t = tc.tanks.get(i);
				if (id == t.id) {
					exist = true;
					break;
				}
			}

			if (!exist) {

				TankNewMsg tnm = new TankNewMsg(tc.myTank);
				tc.nc.send(tnm);

				Tank t = new Tank(x, y, good, dir, tc);
				t.id = id;
				tc.tanks.add(t);
				System.out.println("A new Tank added , id:" + id + "-x:" + "-y:" + y + "-dir:" + dir + "-good:" + good);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
