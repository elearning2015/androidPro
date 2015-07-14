package com.zzw.tankWar;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 坦克大战游戏的主控类
 * @author zzw
 *
 */

public class TankClient extends Frame
{       
	/**
	 * 整个游戏的宽度
	 */
	public static final int GAME_WIDTH = 640;
	/**
	 * 整个游戏的高度
	 */
	public static final int GAME_HEIGHT = 480;
	
	private Image offScreen = null;
	
	
    Tank myTank = new Tank(140, 250, true, Direction.STOP, this);
    Wall w1 = new Wall(140, 180, 50, 250, this);
    Wall w2 = new Wall(360, 130, 220, 50, this);
    Blood blood = new Blood(15, 15);
    List<Missile> missiles = new ArrayList<Missile>();
    List<Explode> explodes = new ArrayList<Explode>();
    List<Tank> tanks = new ArrayList<Tank>();
       
    /**
     * 解决因频繁paint导致的图像闪烁现象，即paint还未画完，屏幕就更新了。画图过程：rapaint-->update-->paint，
     * 可采用“双缓冲”：截获update函数，在屏幕背后（即一个image上）先画一个想要的图像，再一次性画在屏幕上。
     */
	public void update(Graphics g)
	{
		if(null == offScreen)
		{
			offScreen = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffSrreen = offScreen.getGraphics();  //获取imge上画笔
		Color c = gOffSrreen.getColor();
		gOffSrreen.setColor(Color.green);
		gOffSrreen.fillRect(0, 0,GAME_WIDTH, GAME_HEIGHT);     //用想要图像的背景色刷一下背景，否则，所有圆形连成一条线了
		gOffSrreen.setColor(c);
		paint(gOffSrreen);    							//调用paint将想要的图像画在image上
		
		g.drawImage(offScreen, 0, 0, null); 		//用屏幕画笔调用
	}

	public void paint(Graphics g) 
	{
		g.drawString("missiles count: " + missiles.size(), 10,  50);
		g.drawString("explodes count: " + explodes.size(), 10, 70);
		g.drawString("tanks count: " + tanks.size(), 10, 90);
		g.drawString("tanks life: " + myTank.getLife(), 10, 110);
		g.drawString("my score : " + myTank.getScore(), 10, 130);
		
		/**
		 * 敌军全被消灭了，又新加一批
		 */
		if(tanks.size() <= 0)
		{
			for(int i = 0; i < Integer.parseInt(PropertyMgr.getProperty("rebornTankCount")); i++)
			{
				Tank t = new Tank(50 + 40*(i+1), 50, false, Direction.D,  this);
				tanks.add(t);
			}
		}
		
		for(int i = 0; i < missiles.size(); i++)
		{
			Missile m = missiles.get(i);
			m.hitTanks(tanks);     	//我方子弹打敌方
			m.hitTank(myTank);    //敌方的子弹打我方
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		for(int i = 0; i < explodes.size(); i++)
		{
			Explode ep = explodes.get(i);
			ep.draw(g);
		}
		for(int i = 0; i  < tanks.size(); i++ )
		{
			Tank t = tanks.get(i);
			t.hitWall(w1);
			t.hitWall(w2);
			//注释掉，我方坦克可以穿墙
			//myTank.hitWall(w1);  
			//myTank.hitWall(w2);
			t.collideswithTanks(tanks);
			myTank.collideswithTanks(tanks);
			t.draw(g);
		}
		
		w1.draw(g);      //墙先画出来，坦克后画，则我方坦克躲在墙后面就可见了
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
		for(int i = 0; i < Integer.parseInt(PropertyMgr.getProperty("initTankCount")); i++)
		{
			Tank t = new Tank(50 + 40*(i+1), 50, false, Direction.D,  this);
			tanks.add(t);
		}
		this.setLocation(30, 40);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setResizable(false);
		this.setBackground(Color.green);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMonitor());
		
		
		this.setVisible(true);
		new Thread(new TankClientThread()).start();
	}

	public static void main(String[] args) 
	{
		TankClient tc = new TankClient();
		tc.launchFrame();
	}

	private class  TankClientThread implements Runnable
	{
        public void run() 
        {
            while(true)
            {
                repaint();
                try
                {
                    Thread.sleep(50);
                }catch(InterruptedException   e)
                {
                    e.printStackTrace();
                }
            }	  
        } 
	}
	
	private class KeyMonitor extends KeyAdapter
	{
		public void keyReleased(KeyEvent e)
		{
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) 
		{
			myTank.keyPressed(e);
		}
	}
}
