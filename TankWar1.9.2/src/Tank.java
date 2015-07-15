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
		
		private Dir dir = Dir.STOP; //坦克方向
		private Dir ptDir = Dir.D;    //炮筒方向
		private int x, y;
		private boolean good;
		int id; //坦克的id，用于坦克间互相识别，是由服务器分配的，然后又要写会给服务器，然服务器来传送给其他坦克

		private boolean live = true;
		private TankClient tc;
		private static Random rn = new Random();  //对敌方坦克产生随机方向。
																		//这个比Math.random好用，因为只需要一个随机数产生器，
																		//对已定为为static
		private int step = rn.nextInt(12) + 3;     		//让敌方坦克在某一方向移动数步再换方向
		
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
				if(y < 25)  y = 25;  //窗口标题栏有一定的宽度
				if(x + Tank.WIDTH> TankClient.GAME_WIDTH)  x = TankClient.GAME_WIDTH - Tank.WIDTH;
				if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT)  y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
				
				if(!good)
				{
						Dir[] dirs = Dir.values();  //将枚举转化为数组
						if(0 == step)
						{	
							int rnNum = rn.nextInt(dirs.length);   //产生0~dirs.length-1的随机整数
							this.dir = dirs[rnNum];
							step = rn.nextInt(12) + 3;
						}
						step--;	
						
						if(rn.nextInt(10) > 8)  // 让敌方间歇性地发子弹
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
