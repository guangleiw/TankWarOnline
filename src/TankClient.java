import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;

	Tank myTank = new Tank(50, 50, true, Dir.STOP, this);

	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();

	Image offScreenImage = null;

	NetCliet nc = new NetCliet(this);
	ConnDialog dialog = new ConnDialog();

	@Override
	public void paint(Graphics g) {
		g.drawString("missiles count:" + missiles.size(), 10, 50);
		g.drawString("explodes count:" + explodes.size(), 10, 70);
		g.drawString("tanks    count:" + tanks.size(), 10, 90);

		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
//			m.hitTanks(tanks);
			if(m.hitTank(myTank))
			{
				TankDeadMsg msg = new TankDeadMsg(myTank.id);
				nc.send(msg);
			}
			m.draw(g);
		}

		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}

		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.draw(g);
		}

		myTank.draw(g);
	}

	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(800, 600);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void launchFrame() {

		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);

		this.addKeyListener(new KeyMonitor());

		this.setVisible(true);

		new Thread(new PaintThread()).start();
		// nc.connect("127.0.0.1", TankServer.TCP_PORT);
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}

	class PaintThread implements Runnable {

		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	class ConnDialog extends Dialog {
		Button b = new Button("确定");
		TextField tfip = new TextField("127.0.0.1", 12);
		TextField tfport = new TextField("6677", 4);
		TextField tfmyudpport = new TextField("7766", 4);

		public ConnDialog() {
			super(TankClient.this, true);
			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfip);
			this.add(new Label("port:"));
			this.add(tfport);
			this.add(new Label("My udp port:"));
			this.add(tfmyudpport);
			
			b.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String ip = tfip.getText().trim();
					int port = Integer.parseInt(tfport.getText().trim());
					int my_udp_port = Integer.parseInt(tfmyudpport.getText().trim());
					nc.setUdp_port(my_udp_port);
					nc.connect(ip, port);
					dialog.setVisible(false);
				}
			});
			this.add(b);
			this.pack();
			this.setLocation(500, 300);
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Auto-generated method stub
					dialog.setVisible(false);
				}

			});

		}

	}

	class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
			int key = e.getKeyCode();
			if (KeyEvent.VK_C == key) {
				dialog.setVisible(true);
			} else {
				myTank.keyPressed(e);
			}
		}

	}

}
