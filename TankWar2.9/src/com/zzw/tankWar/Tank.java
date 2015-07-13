package com.zzw.tankWar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



/**
 * 坦克类
 * @author zzw
 *
 */
public class Tank 
{
	public static final int TANK_STEP = 5;
	public static final int XSPEED = 7;
	public static final int YSPEED = 7;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	public static final int LIFE_INIT = 100;
	
	private boolean bL = false, bR = false, bU = false, bD = false;
	
	private Direction dir = Direction.STOP;   //坦克方向
	private Direction ptDir = Direction.D;    //炮筒方向
	private int x, y, oldX, oldY;
	private boolean good;
	private boolean live = true;
	private int life = 100; 			      //我方坦克的生命值
	private TankClient tc;
	private static Random rn = new Random();  //对敌方坦克产生随机方向。
											  //这个比Math.random好用，因为只需要一个随机数产生器，
											  //对已定为为static
	private int step = rn.nextInt(12) + 3;    //让敌方坦克在某一方向移动数步再换方向
	private bloodBar bb = new bloodBar();
	private int score;  //得分

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] tankImgs = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();

	//静态代码区（保证了当这个在第一次被load到内存时首先执行），与Explode类中直接定义imgs是一样的。
	//在static代码区内可以做一些变量的初始化，特别注意的是：还可以写代码（正常情况，代码应写在方法内）。
	static 
	{
		tankImgs = new Image[] {
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif"))
			};
		
		//在非static代码区，下面这些语句就不能直接这样写了，因为不是赋值
		imgs.put("L", tankImgs[0]);
		imgs.put("LU", tankImgs[1]);
		imgs.put("LD", tankImgs[2]);
		imgs.put("R", tankImgs[3]);
		imgs.put("RU", tankImgs[4]);
		imgs.put("RD", tankImgs[5]);
		imgs.put("U", tankImgs[6]);
		imgs.put("D", tankImgs[7]);
	}
	
	public Tank(int x, int y,boolean good, Direction dir) 
	{
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
		this.dir = dir;
		this.life = LIFE_INIT;
		this.score = 0;
	}

	public Tank(int x, int y, boolean good, Direction dir, TankClient tc)
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
//		Color c = g.getColor();
//		if(good)
//		{
//			g.setColor(Color.red);
//		}
//		else
//		{
//			g.setColor(Color.blue);
//		}
//		
//		g.fillOval(x, y, WIDTH, HEIGHT);
//		g.setColor(c);
		
		switch(ptDir)
		{
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		}
		
		//我方坦克加一个血条标示生命值
		if(this.isGood())
		{
			bb.draw(g);
		}
		
		move();
	}
	
	public void move()
	{
		//记录前一个位置
		this.oldX = x;
		this.oldY = y;
		
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
		if(dir != Direction.STOP)
		{
			ptDir = dir;
		}
		if(x < 0)  x = 0;
		if(y < 25)  y = 25;  //窗口标题栏有一定的宽度
		if(x + Tank.WIDTH> TankClient.GAME_WIDTH)  x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT)  y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		if(!good)
		{
			//让敌方坦克在某个方向上移动数步，不要换方向太快，造成闪烁感
			Direction[] dirs = Direction.values();  //将枚举转化为数组
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
		//按F2重新开始游戏，坦克出现位置随机。
		//按F2只有我方坦克会重现，因为敌方坦克是加在list中，当其live==false时，直接使用remove从内存删除了
		//再令其live=true，也不会画出来
		case KeyEvent.VK_F2:  
			if(!this.isLive())
			{
				this.live = true;
				this.x = rn.nextInt(TankClient.GAME_WIDTH) + 10;
				this.y = rn.nextInt(TankClient.GAME_HEIGHT) + 10; 
				//this.setLife(Tank.LIFE_INIT);
				this.life = Tank.LIFE_INIT;
			}
			break;
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
//		case KeyEvent.VK_A:
//			break;
			
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
		case KeyEvent.VK_A:
			this.superFire();
			break;
		}
		locateDirection();
	}
	
	public void locateDirection()
	{
		if(bL && !bR && !bU && !bD)  dir = Direction.L; 
		if(bL && !bR && bU && !bD)  dir = Direction.LU; 
		if(bL && !bR && !bU && bD)  dir = Direction.LD; 
		if(!bL && bR && !bU && !bD)  dir = Direction.R; 
		if(!bL && bR && bU && !bD)  dir = Direction.RU; 
		if(!bL && bR && !bU && bD)  dir = Direction.RD; 
		if(!bL && !bR && bU && !bD)  dir = Direction.U; 
		if(!bL && !bR && !bU && bD)  dir = Direction.D; 
		if(!bL && !bR && !bU && !bD)  dir = Direction.STOP; 

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
	
	public Missile fire(Direction dir)
	{
		if(!this.live)
		{
			return null;
		}
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT /2 - Missile.HEIGHT / 2;
		Missile m = new Missile(x, y, this.good, dir, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	private void superFire()
	{
		Direction []dirs = Direction.values();
		for(int i = 0; i < dirs.length; i++)
		{
			if(dirs[i] == Direction.STOP)
			{
				continue;
			}
			fire(dirs[i]);
		}
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	//恢复之前的那个位置
	public void stay()
	{
		this.x = this.oldX;
		this.y = this.oldY;
	}
	
	public boolean hitWall(Wall w)
	{
		if(this.live && this.getRect().intersects(w.getRect()))
		{
			//this.dir = direction.STOP; //这样坦克就停在墙上不动了
			this.stay();   //恢复成撞到墙之前的那个位置
			return true;
		}
		return false;
	}
	
	public boolean collideswithTank(Tank t)
	{
		if(this.live && t.live && this.getRect().intersects(t.getRect()))
		{
			this.stay();
			t.stay();
			return true;
		}
		return false;
	}
	
	public boolean collideswithTanks(java.util.List<Tank> tanks)
	{
		for(int i = 0; i < tanks.size(); i++)
		{
			Tank t = tanks.get(i);
			if(this != t && this.collideswithTank(t))
			{
				this.stay();
				t.stay();
				return true;
			}
		}
		return false;
	}
	
	public void eatBlood(Blood b)
	{
		//this.life != LIFE_INIT)表示满血的时候不吃
		if(this.isGood() && b.isLive() && this.live && this.getRect().intersects(b.getRect()) && this.life != LIFE_INIT)
		{
			this.setLife(LIFE_INIT);
			b.setLive(false);
		}
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
	
	public int getLife() 
	{
		return life;
	}

	public void setLife(int life) 
	{
		this.life = life;
	}
	
	private class bloodBar
	{
		public void draw(Graphics g)
		{
			Color c = g.getColor();
			g.setColor(Color.red);
			//坦克换成图片了，上面带的bloodbar长度没对应，修改坐标和宽度即可
			g.drawRect(x+7, y -10, Tank.WIDTH+6, 10);  
			int w = (Tank.WIDTH+6) * life / LIFE_INIT;
			g.fillRect(x+7, y-10, w, 10);
			g.setColor(c);
		}
	}
	
	public int getScore() 
	{
		return score;
	}

	public void setScore(int score)
	{
		this.score = score;
	}
}







