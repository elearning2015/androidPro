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
      
        
        //�����Ƶ��paint���µ�ͼ����˸���󣬼�paint��δ���꣬��Ļ�͸����ˡ���ͼ���̣�rapaint-->update-->paint��
        //�ɲ��á�˫���塱���ػ�update����������Ļ���󣨼�һ��image�ϣ��Ȼ�һ����Ҫ��ͼ����һ���Ի�����Ļ�ϡ�
    	public void update(Graphics g)
    	{
	    		if(null == offScreen)
	    		{
	    				offScreen = this.createImage(GAME_WIDTH, GAME_HEIGHT);
	    		}
    			Graphics gOffSrreen = offScreen.getGraphics();  //��ȡimge�ϻ���
    			Color c = gOffSrreen.getColor();
    			gOffSrreen.setColor(Color.green);
    			gOffSrreen.fillRect(0, 0,GAME_WIDTH, GAME_HEIGHT);     //����Ҫͼ��ı���ɫˢһ�±�������������Բ������һ������
    			gOffSrreen.setColor(c);
    			paint(gOffSrreen);    							//����paint����Ҫ��ͼ����image��
    			
    			g.drawImage(offScreen, 0, 0, null); 		//����Ļ���ʵ���
    	}

		public void paint(Graphics g) 
		{
				g.drawString("missiles count:" + missiles.size(), 10,  50);
				g.drawString("explodes count:" + explodes.size(), 10, 70);
				g.drawString("tanks count:" + tanks.size(), 10, 90);
				
				for(int i = 0; i < missiles.size(); i++)
				{
						Missile m = missiles.get(i);
						m.hitTanks(tanks);     	//�ҷ��ӵ���з�
						m.hitTank(myTank);    //�з����ӵ����ҷ�
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
//				} //Iterator���������е�����
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
