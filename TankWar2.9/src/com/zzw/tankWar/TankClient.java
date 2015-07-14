package com.zzw.tankWar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ̹�˴�ս��Ϸ��������
 * 
 * @author zzw
 * 
 */

public class TankClient extends Frame {
	/**
	 * ������Ϸ�Ŀ��
	 */
	public static final int GAME_WIDTH = 800;
	/**
	 * ������Ϸ�ĸ߶�
	 */
	public static final int GAME_HEIGHT = 600;

	private Image offScreen = null; // ������Ļ������������滭�ã�Ȼ��һ�𻭵���ʵ��Ļ�ϣ��ɷ�ֹ��˸����
	private boolean init = true; // �ж��Ƿ���δ򿪻���
	private boolean clicked = false;
	private Panel pl = null;
	private Font font = null;  //label������
	private Color color = null; //label��������ɫ
	private Image initImg = Toolkit.getDefaultToolkit().getImage(TankClient.class.getClassLoader().getResource("images/tankWar.jpg"));
	private Image bkImg = Toolkit.getDefaultToolkit().getImage(TankClient.class.getClassLoader().getResource("images/tankWar1.jpg"));
	
	Tank myTank = new Tank(140, 250, true, Direction.STOP, this);
	Wall w1 = new Wall(140, 180, 60, 235, this);
	Wall w2 = new Wall(360, 130, 235, 60, this);
	Blood blood = new Blood(15, 15);
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	Map<String,Label> choices = new HashMap<String,Label>(); 

	/**
	 * �����Ƶ��paint���µ�ͼ����˸���󣬼�paint��δ���꣬��Ļ�͸����ˡ���ͼ���̣�rapaint-->update-->paint��
	 * �ɲ��á�˫���塱���ػ�update����������Ļ���󣨼�һ��image�ϣ��Ȼ�һ����Ҫ��ͼ����һ���Ի�����Ļ�ϡ�
	 */
	public void update(Graphics g) {
		if (null == offScreen) {
			offScreen = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffSrreen = offScreen.getGraphics(); // ��ȡimge�ϻ���
		Color c = gOffSrreen.getColor();
		gOffSrreen.setColor(Color.green);
		gOffSrreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT); // ����Ҫͼ��ı���ɫˢһ�±�������������Բ������һ������
		gOffSrreen.setColor(c);
		paint(gOffSrreen); // ����paint����Ҫ��ͼ����image��

		g.drawImage(offScreen, 0, 0, null); // ����Ļ���ʵ���
	}

	public void paint(Graphics g) {
		
		if (init && !clicked) {
			g.drawImage(initImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
			return;
		}

		g.drawImage(bkImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
		g.drawString("missiles count: " + missiles.size(), 10, 50);
		g.drawString("explodes count: " + explodes.size(), 10, 70);
		g.drawString("tanks count: " + tanks.size(), 10, 90);
		g.drawString("tanks life: " + myTank.getLife(), 10, 110);
		g.drawString("my score : " + myTank.getScore(), 10, 130);

		/**
		 * �о�ȫ�������ˣ����¼�һ��
		 */
		if (tanks.size() <= 0) {
			for (int i = 0; i < Integer.parseInt(PropertyMgr
					.getProperty("rebornTankCount")); i++) {
				Tank t = new Tank(50 + 40 * (i + 1), 50, false, Direction.D,
						this);
				tanks.add(t);
			}
		}

		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks); // �ҷ��ӵ���з�
			m.hitTank(myTank); // �з����ӵ����ҷ�
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		for (int i = 0; i < explodes.size(); i++) {
			Explode ep = explodes.get(i);
			ep.draw(g);
		}
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.hitWall(w1);
			t.hitWall(w2);
			// ע�͵����ҷ�̹�˿��Դ�ǽ
			// myTank.hitWall(w1);
			// myTank.hitWall(w2);
			t.collideswithTanks(tanks);
			myTank.collideswithTanks(tanks);
			t.draw(g);
		}

		w1.draw(g); // ǽ�Ȼ�������̹�˺󻭣����ҷ�̹�˶���ǽ����Ϳɼ���
		w2.draw(g);
		myTank.draw(g);
		myTank.eatBlood(blood);

		blood.draw(g);
	}

	/**
	 * ��Ϸ�����ڵ���
	 */
	public void launchFrame() {
		for (int i = 0; i < Integer.parseInt(PropertyMgr
				.getProperty("initTankCount")); i++) {
			Tank t = new Tank(50 + 40 * (i + 1), 50, false, Direction.D, this);
			tanks.add(t);
		}
		this.setLocation(30, 40);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setResizable(false);
		this.setBackground(Color.green);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMonitor());
		
		pl = new Panel();
		pl.setLayout(new GridLayout(3,1));
		choices.put("START", new Label("START", Label.CENTER));
		choices.put("LEVEL1", new Label("LEVEL 1", Label.CENTER));
		choices.put("LEVEL2", new Label("LEVEL 2", Label.CENTER));
		pl.add(choices.get("START"));
		pl.add(choices.get("LEVEL1"));
		pl.add(choices.get("LEVEL2"));
		pl.setBackground(Color.white);
		pl.setBounds(this.getX() +GAME_WIDTH/3, this.getY() + GAME_HEIGHT/4, 120, 90);
		//System.out.println("w"+lb.getWidth()+"h"+lb.getHeight());
		//pl.setLocation(this.getX() + GAME_WIDTH/2, this.getY() + GAME_HEIGHT/2);
		this.setLayout(null);
		this.add(pl);
		choices.get("START").addMouseListener(new MouseMonitor());
		choices.get("LEVEL1").addMouseListener(new MouseMonitor());
		choices.get("LEVEL2").addMouseListener(new MouseMonitor());

		this.setVisible(true);
		new Thread(new TankClientThread()).start();
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}

	private class TankClientThread implements Runnable {
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50); // ÿ50ms��һ��
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class MouseMonitor extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e)
		{
			//lb.setEnabled(true);
			Component cmp = e.getComponent();
			font = cmp.getFont();
			color = cmp.getForeground();
			cmp.setFont(new Font("Serif",Font.PLAIN,20));
			cmp.setForeground(Color.orange);
			
		}

		@Override
		public void mouseExited(MouseEvent e) 
		{
			//lb.setEnabled(false);
			Component cmp = e.getComponent();
			cmp.setFont(font);
			cmp.setForeground(color);
		}

		@Override
		public void mouseClicked(MouseEvent e) 
		{
			pl.setVisible(false);
			Component cmp = e.getComponent();
			clicked = true;
			init = false;
			if(cmp == choices.get("START"))
			{
				choices.get("LEVEL1").setVisible(false);
				choices.get("LEVEL2").setVisible(false);
			}
			else if(cmp == choices.get("LEVEL1"))			
			{
				PropertyMgr.setProperty("initTankCount", "8");
				PropertyMgr.setProperty("rebornTankCount", "5");
				choices.get("START").setVisible(false);
				choices.get("LEVEL2").setVisible(false);
			}
			else if(cmp == choices.get("LEVEL2"))			
			{
				PropertyMgr.setProperty("initTankCount", "12");
				PropertyMgr.setProperty("rebornTankCount", "10");
				choices.get("START").setVisible(false);
				choices.get("LEVEL1").setVisible(false);
			}
			
			cmp.setVisible(false);
		}

	}

	private class KeyMonitor extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);

			frameKeyPressed(e); // ��ӦESC���˳��¼�������TankClient���бȷ���Tank���к���Щ��
								// ���ڲ����ֱ�ӵ����ⲿ��ĳ�Ա��������
		}
	}

	private void frameKeyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_ESCAPE: // ��ESC�˳�
			System.exit(-1);
			break;
		}
	}
}
