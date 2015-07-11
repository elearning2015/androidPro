import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class TankClient extends Frame
{        
		public static final int GAME_WIDTH = 640;
		public static final int GAME_HEIGHT = 480;
	
		int x = 50, y =  50;
        Image offScreen = null;
        Tank myTank = new Tank(x, y, true, Tank.direction.STOP, this);
        List<Missile> missiles = new ArrayList<Missile>();
        List<Explode> explodes = new ArrayList<Explode>();
        List<Tank> tanks = new ArrayList<Tank>();
      
        
        //解决因频繁paint导致的图像闪烁现象，即paint还未画完，屏幕就更新了。画图过程：rapaint-->update-->paint，
        //可采用“双缓冲”：截获update函数，在屏幕背后（即一个image上）先画一个想要的图像，再一次性画在屏幕上。
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
				g.drawString("missiles count:" + missiles.size(), 10,  50);
				g.drawString("explodes count:" + explodes.size(), 10, 70);
				g.drawString("tanks count:" + tanks.size(), 10, 90);
				
				for(int i = 0; i < missiles.size(); i++)
				{
						Missile m = missiles.get(i);
						m.hitTanks(tanks);     	//我方子弹打敌方
						m.hitTank(myTank);    //敌方的子弹打我方
						m.draw(g);
//						if(!m.isLive())
//						{
//							missiles.remove(m);
//						}
//						else
//						{
//							m.draw(g);
//						}
				}
//				for(Iterator<Missile> it = missiles.iterator(); it.hasNext();)
//				{
//						it.next().draw(g);
//				} //Iterator用起来还有点问题
				for(int i = 0; i < explodes.size(); i++)
				{
						Explode ep = explodes.get(i);
//						if(!ep.isLive())  
//						{
//							explodes.remove(ep);
//						}
//						else
//						{
//							ep.draw(g);
//						}
						ep.draw(g);
				}
				for(int i = 0; i  < tanks.size(); i++ )
				{
						Tank t = tanks.get(i);
						t.draw(g);
				}
				myTank.draw(g);

		}
		
		public void launchFrame()
		{
				for(int i = 0; i < 10; i++)
				{
						Tank t = new Tank(50 + 40*(i+1), 50, false, Tank.direction.D,  this);
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
