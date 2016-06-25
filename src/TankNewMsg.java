import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankNewMsg {
	Tank tank;

	public TankNewMsg(Tank tank) {
		this.tank = tank;
	}

	public TankNewMsg() {
		// TODO Auto-generated constructor stub
	}

	public void send(DatagramSocket ds, String IP, int udpport) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
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
			System.out.println("id:" + id + "-x:" + "-y:" + y + "-dir:"+dir+"-good:" + good);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
