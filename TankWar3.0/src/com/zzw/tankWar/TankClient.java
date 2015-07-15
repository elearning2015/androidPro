package com.zzw.tankWar;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * ̹�˴�ս��Ϸ��������
 * 
 * @author zzw
 * 
 */

public class TankClient extends Frame {
	/**
	 * ������Ϸ�Ŀ��
	 */
	public static final int GAME_WIDTH = 1100;
	/**
	 * ������Ϸ�ĸ߶�
	 */
	public static final int GAME_HEIGHT = 680;

	private Image offScreen = null; // ������Ļ������������滭�ã�Ȼ��һ�𻭵���ʵ��Ļ�ϣ��ɷ�ֹ��˸����
	private boolean init = true; // �ж��Ƿ���δ򿪻���
	private boolean clicked = false;    //�Ƿ�ѡ�����Ϸ����
	private boolean desClicked = false; //�Ƿ�������Ϸ����
	private Panel pl = null, pn = null, plDes = null, plExit = null;
	private Button btn = null, btnExit = null;
	Label desLb = null;
	private Font font = null;  //label������
	private Color color = null; //label��������ɫ
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
	
	Tank myTank = new Tank(GAME_WIDTH/5+10, GAME_HEIGHT/2, true, Direction.STOP, this);
	Wall w1 = new Wall(this.getX() + GAME_WIDTH/2, this.getY() + GAME_HEIGHT/4, wall1, 432, 68,this);
	Wall w2 = new Wall(this.getX() + GAME_WIDTH/5, this.getY() + GAME_HEIGHT/3, wall2, 68, 432, this);
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
		gOffSrreen.setColor(new Color(10, 100, 150));
		//gOffSrreen.setColor(new Color(1f, 1f, 0f, 0.f));
		gOffSrreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT); // ����Ҫͼ��ı���ɫˢһ�±�������������Բ������һ������
		gOffSrreen.setColor(c);
		paint(gOffSrreen); // ����paint����Ҫ��ͼ����image��

		g.drawImage(offScreen, 0, 0, null); // ����Ļ���ʵ���
	}

	public void paint(Graphics g) 
	{
		if (init && !clicked)
		{
			g.drawImage(initImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

			if(this.desClicked)
			{
				//this.desClicked = false;
				g.drawString("��Ϸ˵����" , GAME_WIDTH/3+50, 130);
				g.drawString("1�����ĸ���λ��������Ͽ��ڰ˸������ƶ��ҷ�̹�ˡ�" , GAME_WIDTH/3-100, 180);
				g.drawString("2����S���ͷŵ����ӵ���" , GAME_WIDTH/3-100, 210);
				g.drawString("3����A����˸������ͷ��ӵ���" , GAME_WIDTH/3-100, 240);
				g.drawString("4���ҷ�̹��ÿ�ε�5��Ѫ����ʼΪ100�㡣" , GAME_WIDTH/3-100, 270);
				g.drawString("5���з�̹�˴��о͹ҡ�" , GAME_WIDTH/3-100, 300);
				g.drawString("6���ҷ�̹�˿ɴ�ǽ���з����ɡ�" , GAME_WIDTH/3-100, 330);
				g.drawString("7���ҷ�̹�˿ɶ���ǽ�ڣ��з����ɡ�" , GAME_WIDTH/3-100, 360);
				g.drawString("8����ESC�����˳���Ϸ��" , GAME_WIDTH/3-100, 390);
			}
			
			return;
		}
		
		if(!init && clicked)  //�ڿ�ʼ����ѡ���ˣ��͸�����Ϸ�ȼ���ȡ��ͬ�Ĳ�������
		{				
			Font fn = new Font("Serif",Font.PLAIN,20);
			this.setForeground(Color.blue);  	//����������ɫ
			this.setFont(fn); 					//���������С

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
				//do while:��⵱ǰ̹�������Ƿ�������̹�˲����Ļ����غϡ��Ƿ���ǽ�غϣ�
				//����ǣ������²������꣬����̹����̹�ˡ�̹����ǽ���һ��
				
				tanks.add(t);
				
			}
		}
		
		g.drawImage(bkImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
		g.drawString("missiles count: " + missiles.size(), GAME_WIDTH/2, 40);
		g.drawString("explodes count: " + explodes.size(), GAME_WIDTH/2, 60);
		g.drawString("tanks count:      " + tanks.size(), GAME_WIDTH/2, 80);
		g.drawString("tanks life:         " + myTank.getLife(), GAME_WIDTH/2, 100);
		g.drawString("my score :         " + myTank.getScore(), GAME_WIDTH/2, 120);

		/**
		 * �о�ȫ�������ˣ����¼�һ��
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
				//do while:��⵱ǰ̹�������Ƿ�������̹�˲����Ļ����غϡ��Ƿ���ǽ�غϣ�
				//����ǣ������²������꣬����̹����̹�ˡ�̹����ǽ���һ��
				
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
	public void launchFrame() 
	{
		this.setLocation(30, 40);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setResizable(false);
		this.setForeground(new Color(200, 255, 225));  				//������Ϸ����������ɫ
		this.setFont(new Font("Serif",Font.PLAIN,25)); 	//���������С
		//this.setState(TankClient.MAXIMIZED_BOTH);
		//this.setBackground(new Color(0, 100, 10));  	//�������õı���ɫ�������ã���update�����õĸ�����
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMonitor());
		
		this.plExit = new Panel();
		plExit.setBounds(GAME_WIDTH- 250, GAME_HEIGHT-200, 100, 40);
		plExit.setFont(new Font("Serif",Font.BOLD,20));  //���������С
		plExit.setForeground(Color.black);
		plExit.setLayout(new BorderLayout());
		this.btnExit = new Button("�˳�");
		btnExit.setBackground(new Color(50, 150 ,180));
		plExit.add(btnExit);
		
		
		//
		pl = new Panel();
		pl.setLayout(new GridLayout(4,1));
		choices.put("Des", new Label("DESCRIPTION", Label.CENTER));
		choices.put("LEVEL1", new Label("LEVEL 1", Label.CENTER));
		choices.put("LEVEL2", new Label("LEVEL 2", Label.CENTER));
		choices.put("LEVEL3", new Label("LEVEL 3", Label.CENTER));
		pl.setFont(new Font("Serif",Font.PLAIN,20));  //���������С
		pl.add(choices.get("Des"));
		pl.add(choices.get("LEVEL1"));
		pl.add(choices.get("LEVEL2"));
		pl.add(choices.get("LEVEL3"));
		//Color c = new Color(1f, 1f, 0f, 0.f);
		pl.setBackground(new Color(10, 100, 150, 255));
		pl.setForeground(Color.black);
		pl.setBounds(GAME_WIDTH/3, GAME_HEIGHT/4, 200, 150);
		
		plDes = new Panel();
		plDes.setLayout(new BorderLayout());
		plDes.setBounds(GAME_WIDTH/3+50, GAME_HEIGHT-200, 100, 40);
		plDes.setFont(new Font("Serif",Font.BOLD,20));  //���������С
		plDes.setForeground(Color.black);
		this.btn = new Button("����");
		btn.setBackground(new Color(50, 150 ,180));
		plDes.add(btn);
		
		
		plDes.setVisible(false);
		this.add(plDes);
		this.setLayout(null);
		this.add(pl);   //�Լ�����᣺�ӵ�Frame�ϵĿؼ�������󻭳�������������ʱ����һ������ͼƬinitImgҲ���Ḳ�����Panel
		this.add(plExit);
		
		btnExit.addActionListener(new ButtonMonitor());
		btn.addActionListener(new ButtonMonitor());
		choices.get("Des").addMouseListener(new MouseMonitor());
		choices.get("LEVEL1").addMouseListener(new MouseMonitor());
		choices.get("LEVEL2").addMouseListener(new MouseMonitor());
		choices.get("LEVEL3").addMouseListener(new MouseMonitor());
		
		this.setVisible(true);
		this.setFocusable(true);   //ʹ�ý�����frame�ϣ��������һ�䣬������Ϸ��ʼ��frane�������������¼�
		//Component cp = this.getFocusOwner();
		//System.out.println(cp.getName());
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

	private class ButtonMonitor implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent act) 
		{
			Object cmp = act.getSource();
			if(cmp.equals(btn))
			{
				plDes.setVisible(false);
				init = true;
				clicked = false;
				desClicked = false;
				pl.setVisible(true);
				//btn.removeActionListener(this); //���Ƴ������¼��������btnֻ�ܱ�����һ��
			}
			else if(cmp.equals(btnExit))
			{
				System.exit(-1);
				btnExit.removeActionListener(this);
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
			plDes.setVisible(false);
			plExit.setVisible(false);
			Component cmp = e.getComponent();
			if(cmp == choices.get("Des"))
			{
				//to do....
				desClicked = true;
				clicked = false;
				init = true;
				plDes.setVisible(true);
				plExit.setVisible(true);
			}
			else if(cmp == choices.get("LEVEL1"))
			{
				PropertyMgr.setProperty("initTankCount", "10");
				PropertyMgr.setProperty("rebornTankCount", "5");

			}
			else if(cmp == choices.get("LEVEL2"))			
			{
				PropertyMgr.setProperty("initTankCount", "15");
				PropertyMgr.setProperty("rebornTankCount", "10");
			}
			else if(cmp == choices.get("LEVEL3"))			
			{
				PropertyMgr.setProperty("initTankCount", "30");
				PropertyMgr.setProperty("rebornTankCount", "15");
			}
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
