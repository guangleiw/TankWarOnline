import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankMoveMsg implements Msg {
	int msgType = Msg.TANK_MOVE_MSG;
	int id;
	int x, y;
	Dir dir;
	TankClient tc = null;

	public TankMoveMsg(int id, int x, int y, Dir dir) {
		this.id = id;
		this.dir = dir;
		this.x = x;
		this.y = y;
	}

	public TankMoveMsg(TankClient tc) {
		// TODO Auto-generated constructor stub
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
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(dir.ordinal());
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
			x = dis.readInt();
			y =  dis.readInt();
			Dir dir = Dir.values()[dis.readInt()];
			if (id == tc.myTank.id)
				return;
			boolean exist = false;

			for (int i = 0; i < tc.tanks.size(); i++) {
				Tank t = tc.tanks.get(i);
				if (id == t.id) {
					t.dir = dir;
					exist = true;
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
