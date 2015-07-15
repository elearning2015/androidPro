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
import java.awt.TextArea;
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
import java.util.Random;

/**
 * 坦克大战游戏的主控类
 * 
 * @author zzw
 * 
 */

public class TankClient extends Frame {
	/**
	 * 整个游戏的宽度
	 */
	public static final int GAME_WIDTH = 1100;
	/**
	 * 整个游戏的高度
	 */
	public static final int GAME_HEIGHT = 680;

	private Image offScreen = null; // 虚拟屏幕，先在这个上面画好，然后一起画到真实屏幕上，可防止闪烁现象
	private boolean init = true; // 判断是否初次打开画面
	private boolean clicked = false;
	private Panel pl = null, pn = null;
	Label desLb = null;
	private Font font = null;  //label中字体
	private Color color = null; //label中字体颜色
	static Image initImg, bkImg, wall1, wall2;
	static Toolkit tk;
	private Random rn = new Random();
	
	static 
	{
		tk = Toolkit.getDefaultToolkit();
		initImg = tk.getImage(TankClient.class.getClassLoader().getResource("images/tankWar.jpg"));
		bkImg = Toolkit.getDefaultToolkit().getImage(TankClient.class.getClassLoader().getResource("images/tankWar1.jpg"));
		wall1 = tk.getImage(TankClient.class.getClassLoader().getResource("images/wall1.png"));
		wall2 = tk.getImage(TankClient.class.getClassLoader().getResource("images/wall2.png"));
	}
	
	Tank myTank = new Tank(this.getX() + GAME_WIDTH/5, this.getY() + GAME_HEIGHT/2, true, Direction.STOP, this);
	Wall w1 = new Wall(this.getX() + GAME_WIDTH/2, this.getY() + GAME_HEIGHT/4, wall1, 432, 68,this);
	Wall w2 = new Wall(this.getX() + GAME_WIDTH/5, this.getY() + GAME_HEIGHT/3, wall2, 68, 432, this);
	Blood blood = new Blood(15, 15);
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	Map<String,Label> choices = new HashMap<String,Label>(); 

	/**
	 * 解决因频繁paint导致的图像闪烁现象，即paint还未画完，屏幕就更新了。画图过程：rapaint-->update-->paint，
	 * 可采用“双缓冲”：截获update函数，在屏幕背后（即一个image上）先画一个想要的图像，再一次性画在屏幕上。
	 */
	public void update(Graphics g) {
		if (null == offScreen) {
			offScreen = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffSrreen = offScreen.getGraphics(); // 获取imge上画笔
		Color c = gOffSrreen.getColor();
		gOffSrreen.setColor(new Color(10, 100, 150));
		//gOffSrreen.setColor(new Color(1f, 1f, 0f, 0.f));
		gOffSrreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT); // 用想要图像的背景色刷一下背景，否则，所有圆形连成一条线了
		gOffSrreen.setColor(c);
		paint(gOffSrreen); // 调用paint将想要的图像画在image上

		g.drawImage(offScreen, 0, 0, null); // 用屏幕画笔调用
	}

	public void paint(Graphics g) {
		
		if (init && !clicked) {
			g.drawImage(initImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
			return;
		}
		
		if(!init && clicked)  //在开始界面选择了，就根据游戏等级获取不同的参数配置
		{
			Font fn = new Font("Serif",Font.PLAIN,20);
			this.setForeground(Color.blue);  	//设置字体颜色
			this.setFont(fn); 					//设置字体大小
			
			init = true;
			int num = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
			int x, y;
			for (int i = 0; i < num; i++) 
			{
				Tank t;
				do
				{
					x = this.getX() + rn.nextInt(GAME_WIDTH);
					y = this.getY() + rn.nextInt(GAME_HEIGHT);
					t = new Tank(x, y, false, Direction.D, this);
				}while(t.collideswithTanks(tanks) || t.hitWall(w1) || t.hitWall(w2));
				//do while:检测当前坦克坐标是否与其它坦克产生的互相重合、是否与墙重合，
				//如果是，则重新产生坐标，否则坦克与坦克、坦克与墙黏在一起
				
				tanks.add(t);
				
			}
		}
		
		g.drawImage(bkImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
		g.drawString("missiles count: " + missiles.size(), GAME_WIDTH/2, 40);
		g.drawString("explodes count: " + explodes.size(), GAME_WIDTH/2, 60);
		g.drawString("tanks count: " + tanks.size(), GAME_WIDTH/2, 80);
		g.drawString("tanks life: " + myTank.getLife(), GAME_WIDTH/2, 100);
		g.drawString("my score : " + myTank.getScore(), GAME_WIDTH/2, 120);

		/**
		 * 敌军全被消灭了，又新加一批
		 */
		if (tanks.size() <= 0) 
		{
			int num = Integer.parseInt(PropertyMgr.getProperty("rebornTankCount"));
			int x, y;
			for (int i = 0; i < num; i++) 
			{
				Tank t;
				do
				{
					x = rn.nextInt(GAME_WIDTH);
					y = rn.nextInt(GAME_HEIGHT);
					t = new Tank(x, y, false, Direction.D, this);
				}while(t.collideswithTanks(tanks) || t.hitWall(w1) || t.hitWall(w2));
				//do while:检测当前坦克坐标是否与其它坦克产生的互相重合、是否与墙重合，
				//如果是，则重新产生坐标，否则坦克与坦克、坦克与墙黏在一起
				
				tanks.add(t);
			}
		}

		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks); // 我方子弹打敌方
			m.hitTank(myTank); // 敌方的子弹打我方
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
			// 注释掉，我方坦克可以穿墙
			// myTank.hitWall(w1);
			// myTank.hitWall(w2);
			t.collideswithTanks(tanks);
			myTank.collideswithTanks(tanks);
			t.draw(g);
		}

		w1.draw(g); // 墙先画出来，坦克后画，则我方坦克躲在墙后面就可见了
		w2.draw(g);
		myTank.draw(g);
		myTank.eatBlood(blood);

		blood.draw(g);
	}

	/**
	 * 游戏主窗口登入
	 */
	public void launchFrame() 
	{
		this.setLocation(30, 40);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setResizable(false);
		//this.setState(TankClient.MAXIMIZED_BOTH);
		//this.setBackground(new Color(0, 100, 10));  //这里设置的背景色不起作用，被update中设置的覆盖了
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMonitor());
		
		//
		pl = new Panel();
		pl.setLayout(new GridLayout(4,1));
		choices.put("Des", new Label("Description", Label.CENTER));
		choices.put("LEVEL1", new Label("LEVEL 1", Label.CENTER));
		choices.put("LEVEL2", new Label("LEVEL 2", Label.CENTER));
		choices.put("LEVEL3", new Label("LEVEL 3", Label.CENTER));
		pl.setFont(new Font("Serif",Font.PLAIN,20));  //设置字体大小
		pl.add(choices.get("Des"));
		pl.add(choices.get("LEVEL1"));
		pl.add(choices.get("LEVEL2"));
		pl.add(choices.get("LEVEL3"));
		//Color c = new Color(1f, 1f, 0f, 0.f);
		pl.setBackground(new Color(10, 100, 150, 255));
		pl.setBounds(this.getX() +GAME_WIDTH/3, this.getY() + GAME_HEIGHT/4, 200, 150);
		//System.out.println("w"+lb.getWidth()+"h"+lb.getHeight());
		//pl.setLocation(this.getX() + GAME_WIDTH/2, this.getY() + GAME_HEIGHT/2);
		this.setLayout(null);
		this.add(pl);   //自己的体会：加到Frame上的控件，会最后画出来，所以启动时加了一个背景图片initImg也不会覆盖这个Panel

		choices.get("Des").addMouseListener(new MouseMonitor());
		choices.get("LEVEL1").addMouseListener(new MouseMonitor());
		choices.get("LEVEL2").addMouseListener(new MouseMonitor());
		choices.get("LEVEL3").addMouseListener(new MouseMonitor());

//		if(!init && clicked)
//		{
//			for (int i = 0; i < Integer.parseInt(PropertyMgr.getProperty("initTankCount")); i++) 
//			{
//				System.out.println(PropertyMgr.getProperty("initTankCount"));
//				Tank t = new Tank(this.getX() + rn.nextInt(GAME_WIDTH), this.getY() + rn.nextInt(GAME_HEIGHT), false, Direction.D, this);
//				tanks.add(t);
//			}
//		}
		
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
					Thread.sleep(50); // 每50ms画一次
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
			cmp.setFont(new Font("Serif",Font.PLAIN,25));
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
			clicked = true;
			init = false;
			pl.setVisible(false);
			Component cmp = e.getComponent();
			if(cmp == choices.get("Des"))
			{
				//to do....
				choices.get("LEVEL2").setVisible(false);
				choices.get("LEVEL3").setVisible(false);
			}
			else if(cmp == choices.get("LEVEL1"))
			{
				PropertyMgr.setProperty("initTankCount", "10");
				PropertyMgr.setProperty("rebornTankCount", "5");
				choices.get("Des").setVisible(false);
				choices.get("LEVEL2").setVisible(false);
				choices.get("LEVEL3").setVisible(false);
			}
			else if(cmp == choices.get("LEVEL2"))			
			{
				PropertyMgr.setProperty("initTankCount", "15");
				PropertyMgr.setProperty("rebornTankCount", "10");
				choices.get("Des").setVisible(false);
				choices.get("LEVEL1").setVisible(false);
				choices.get("LEVEL3").setVisible(false);
			}
			else if(cmp == choices.get("LEVEL3"))			
			{
				PropertyMgr.setProperty("initTankCount", "30");
				PropertyMgr.setProperty("rebornTankCount", "15");
				choices.get("Des").setVisible(false);
				choices.get("LEVEL1").setVisible(false);
				choices.get("LEVEL2").setVisible(false);
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

			frameKeyPressed(e); // 响应ESC键退出事件，放在TankClient类中比放在Tank类中合适些。
								// （内部类可直接调用外部类的成员函数。）
		}
	}

	private void frameKeyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_ESCAPE: // 按ESC退出
			System.exit(-1);
			break;
		}
	}
}
