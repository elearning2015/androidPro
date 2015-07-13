import java.awt.*;
import java.util.Iterator;
import java.util.List;


public class Missile 
{
		public static final int XSPEED = 10;
		public static final int YSPEED = 10;
		public static final int WIDTH = 10;
		public static final int HEIGHT = 10;
		
		private int x, y;
		Dir dir;
		private boolean live = true;
		private TankClient tc;
		private boolean good;    //敌方坦克对应的子弹为false，我方对应的子弹为true。
											//为了区分自己人不大自己人
		
		public Missile(int x, int y, Dir dir) 
		{
				this.x = x;
				this.y = y;
				this.dir = dir;
		}
		
		public Missile(int x, int y, boolean good, Dir dir, TankClient tc)
		{
				this(x, y, dir);
				this.tc = tc;
				this.good = good;
		}
		
		public void draw(Graphics g)
		{
				if(!this.live)
				{
					tc.missiles.remove(this);
					return;
				}
				Color c = g.getColor();
				if(this.good)
				{
					g.setColor(Color.YELLOW);  //我方子弹颜色
				}
				else
				{
					g.setColor(Color.black);
				}
				g.fillOval(x, y, WIDTH, HEIGHT);
				g.setColor(c);
				
				move();
		}

		private void move() 
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
			}
			if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT)
			{
					this.live = false;
			}
		}

		private Rectangle getRect()
		{
				return new Rectangle(x, y, WIDTH, HEIGHT);
		}
		
		public boolean hitTank(Tank t)
		{
				if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood())
				{
						t.setLive(false);
						this.live = false;
						Explode e = new Explode(x, y, tc);
						tc.explodes.add(e);
						return true;
				}
				return false;
		}
		
		public boolean hitTanks(List<Tank> tanks)
		{
				for(int i = 0; i < tanks.size(); i++ )
				{
						if( this.hitTank( tanks.get(i) ) )
						{
								return true;
						}
				}
				return false;
		}
		
		public boolean isLive() 
		{
				return live;
		}

}
