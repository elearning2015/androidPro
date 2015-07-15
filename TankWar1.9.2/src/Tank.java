import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Tank 
{
		public static final int TANK_STEP = 5;
		public static final int XSPEED = 7;
		public static final int YSPEED = 7;
		public static final int WIDTH = 30;
		public static final int HEIGHT = 30;
		
		private boolean bL = false, bR = false, bU = false, bD = false;
		
		private Dir dir = Dir.STOP; //̹�˷���
		private Dir ptDir = Dir.D;    //��Ͳ����
		private int x, y;
		private boolean good;
		int id; //̹�˵�id������̹�˼以��ʶ�����ɷ���������ģ�Ȼ����Ҫд�����������Ȼ�����������͸�����̹��

		private boolean live = true;
		private TankClient tc;
		private static Random rn = new Random();  //�Եз�̹�˲����������
																		//�����Math.random���ã���Ϊֻ��Ҫһ���������������
																		//���Ѷ�ΪΪstatic
		private int step = rn.nextInt(12) + 3;     		//�õз�̹����ĳһ�����ƶ������ٻ�����
		
		public Tank(int x, int y, boolean good, Dir dir) {
			this.x = x;
			this.y = y;
			this.good = good;
			this.dir = dir;
		}
	
		public Tank(int x, int y, boolean good, Dir dir, TankClient tc)
		{
			this(x, y, good, dir);
			this.tc = tc;
		}
		
		public void draw(Graphics g)
		{
				if(!this.live)
				{
					if(!good)
					{
						tc.tanks.remove(this);
					}
					return;
				}
				Color c = g.getColor();
				if(good)
				{
					g.setColor(Color.red);
				}
				else
				{
					g.setColor(Color.blue);
				}
				
				g.fillOval(x, y, WIDTH, HEIGHT);
				g.drawString("id:" + id, x, y);
				g.setColor(c);
				
				switch(ptDir)
				{
				case L:
						g.drawLine(x + Tank.WIDTH /2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT /2);
					break;
				case LU:
					g.drawLine(x + Tank.WIDTH /2, y + Tank.HEIGHT / 2, x, y);
					break;
				case LD:
					g.drawLine(x + Tank.WIDTH /2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT);
					break;
				case R:
					g.drawLine(x + Tank.WIDTH /2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT / 2);
					break;
				case RU:
					g.drawLine(x + Tank.WIDTH /2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y );
					break;
				case RD:
					g.drawLine(x + Tank.WIDTH /2, y + Tank.HEIGHT/2, x + WIDTH, y + Tank.HEIGHT);
					break;
				case U:
					g.drawLine(x + Tank.WIDTH /2, y + Tank.HEIGHT / 2, x + Tank.WIDTH /2, y);
					break;
				case D:
					g.drawLine(x + Tank.WIDTH /2, y + Tank.HEIGHT / 2, x + Tank.WIDTH /2, y + Tank.HEIGHT);
					break;
				}
				
				move();
		}
		
		public void move()
		{
				switch(dir)
				{
				case L:
					x -= YSPEED;
					break;
				case LU:
					x -= 	XSPEED;
					y -= YSPEED;
					break;
				case LD:
					x -= 	XSPEED;
					y += YSPEED;
					break;
				case R:
					x += 	XSPEED;
					break;
				case RU:
					x += 	XSPEED;
					y -= YSPEED;
					break;
				case RD:
					x += 	XSPEED;
					y += YSPEED;
					break;
				case U:
					y -= YSPEED;
					break;
				case D:
					y += YSPEED;
					break;
				case STOP:
					x += 0;
					y += 0;
					break;
				}
				if(dir != Dir.STOP)
				{
					ptDir = dir;
				}
				if(x < 0)  x = 0;
				if(y < 25)  y = 25;  //���ڱ�������һ���Ŀ��
				if(x + Tank.WIDTH> TankClient.GAME_WIDTH)  x = TankClient.GAME_WIDTH - Tank.WIDTH;
				if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT)  y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
				
				if(!good)
				{
						Dir[] dirs = Dir.values();  //��ö��ת��Ϊ����
						if(0 == step)
						{	
							int rnNum = rn.nextInt(dirs.length);   //����0~dirs.length-1���������
							this.dir = dirs[rnNum];
							step = rn.nextInt(12) + 3;
						}
						step--;	
						
						if(rn.nextInt(10) > 8)  // �õз���Ъ�Եط��ӵ�
						{
							this.fire();
						}
						
				}
		}
		
		public void keyPressed(KeyEvent e)
		{
				int key = e.getKeyCode();
				switch(key)
				{
				case KeyEvent.VK_UP:
					bU = true;
					break;
				case KeyEvent.VK_DOWN:
					bD = true;
					break;
				case KeyEvent.VK_LEFT:
					bL = true;
					break;
				case KeyEvent.VK_RIGHT:
					bR = true;
					break;
					
				}
				locateDirection();
		}
		
		public void keyReleased(KeyEvent e) 
		{
				int key = e.getKeyCode();
				switch(key)
				{
				case KeyEvent.VK_CONTROL:
					fire();
					break;
				case KeyEvent.VK_UP:
					bU = false;
					break;
				case KeyEvent.VK_DOWN:
					bD = false;
					break;
				case KeyEvent.VK_LEFT:
					bL = false;
					break;
				case KeyEvent.VK_RIGHT:
					bR = false;
					break;
				}
				locateDirection();
		}
		
		public void locateDirection()
		{
				if(bL && !bR && !bU && !bD)  dir = Dir.L; 
				if(bL && !bR && bU && !bD)  dir = Dir.LU; 
				if(bL && !bR && !bU && bD)  dir = Dir.LD; 
				if(!bL && bR && !bU && !bD)  dir = Dir.R; 
				if(!bL && bR && bU && !bD)  dir = Dir.RU; 
				if(!bL && bR && !bU && bD)  dir = Dir.RD; 
				if(!bL && !bR && bU && !bD)  dir = Dir.U; 
				if(!bL && !bR && !bU && bD)  dir = Dir.D; 
				if(!bL && !bR && !bU && !bD)  dir = Dir.STOP; 

		}

		public Missile fire()
		{
				if(!this.live)
				{
						return null;
				}
				int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
				int y = this.y + Tank.HEIGHT /2 - Missile.HEIGHT / 2;
				Missile m = new Missile(x, y, this.good, ptDir, this.tc);
				tc.missiles.add(m);
				return m;
		}
	
		public Rectangle getRect()
		{
				return new Rectangle(x, y, WIDTH, HEIGHT);
		}
		
		public boolean isLive() 
		{
			return live;
		}

		public void setLive(boolean live)
		{
			this.live = live;
		}
		
		public boolean isGood()
		{
			return good;
		}
}
