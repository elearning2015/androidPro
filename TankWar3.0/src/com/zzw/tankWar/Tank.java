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
 * ̹����
 * @author zzw
 *
 */
public class Tank 
{
	public static final int TANK_STEP = 5;
	public static final int XSPEED = 7;
	public static final int YSPEED = 7;
	public static final int DELTA_SPEED = 3; //�ҷ�̹���ٶ�Ҫ��3
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	public static final int LIFE_INIT = 100;
	private static final Color MYTANK_COLOR = new Color(128, 150, 230);  //�����ҷ�̹�˵ĵ�ɫ
	
	private boolean bL = false, bR = false, bU = false, bD = false;
	
	private Direction dir = Direction.STOP;   //̹�˷���
	private Direction ptDir = Direction.D;    //��Ͳ����
	private int x, y, oldX, oldY;
	private boolean good;
	private boolean live = true;
	private int life = 100; 			      //�ҷ�̹�˵�����ֵ
	private TankClient tc;
	private static Random rn = new Random();  //�Եз�̹�˲����������
											  //�����Math.random���ã���Ϊֻ��Ҫһ���������������
											  //���Ѷ�ΪΪstatic
	private int step = rn.nextInt(12) + 3;    //�õз�̹����ĳһ�����ƶ������ٻ�����
	private bloodBar bb = new bloodBar();
	private int score;  //�÷�

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] tankImgs = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();

	//��̬����������֤�˵�����ڵ�һ�α�load���ڴ�ʱ����ִ�У�����Explode����ֱ�Ӷ���imgs��һ���ġ�
	//��static�������ڿ�����һЩ�����ĳ�ʼ�����ر�ע����ǣ�������д���루�������������Ӧд�ڷ����ڣ���
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
		
		//�ڷ�static��������������Щ���Ͳ���ֱ������д�ˣ���Ϊ���Ǹ�ֵ
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
			if(this.good)  //�ҷ�̹��������ɫ��ɫ
			{
				Color c = g.getColor();
				g.setColor(MYTANK_COLOR);
				g.fillRect(x, y, imgs.get("L").getWidth(null), imgs.get("L").getHeight(null));
				g.setColor(c);
			}
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			if(this.good)
			{
				Color c = g.getColor();
				g.setColor(MYTANK_COLOR);
				g.fillRect(x, y, imgs.get("LU").getWidth(null), imgs.get("LU").getHeight(null));
				g.setColor(c);
			}
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case LD:
			if(this.good)
			{
				Color c = g.getColor();
				g.setColor(MYTANK_COLOR);
				g.fillRect(x, y, imgs.get("LD").getWidth(null), imgs.get("LD").getHeight(null));
				g.setColor(c);
			}
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		case R:
			if(this.good)
			{
				Color c = g.getColor();
				g.setColor(MYTANK_COLOR);
				g.fillRect(x, y, imgs.get("R").getWidth(null), imgs.get("R").getHeight(null));
				g.setColor(c);
			}
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RU:
			if(this.good)
			{
				Color c = g.getColor();
				g.setColor(MYTANK_COLOR);
				g.fillRect(x, y, imgs.get("RU").getWidth(null), imgs.get("RU").getHeight(null));
				g.setColor(c);
			}
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case RD:
			if(this.good)
			{
				Color c = g.getColor();
				g.setColor(MYTANK_COLOR);
				g.fillRect(x, y, imgs.get("RD").getWidth(null), imgs.get("RD").getHeight(null));
				g.setColor(c);
			}
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case U:
			if(this.good)
			{
				Color c = g.getColor();
				g.setColor(MYTANK_COLOR);
				g.fillRect(x, y, imgs.get("U").getWidth(null), imgs.get("U").getHeight(null));
				g.setColor(c);
			}
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case D:
			if(this.good)
			{
				Color c = g.getColor();
				g.setColor(MYTANK_COLOR);
				g.fillRect(x, y, imgs.get("D").getWidth(null), imgs.get("D").getHeight(null));
				g.setColor(c);
			}
			g.drawImage(imgs.get("D"), x, y, null);
			break;		
		}
		
		//�ҷ�̹�˼�һ��Ѫ����ʾ����ֵ
		if(this.isGood())
		{
			bb.draw(g);
		}
		
		move();
	}
	
	public void move()
	{
		//��¼ǰһ��λ��
		this.oldX = x;
		this.oldY = y;
		
		switch(dir)
		{
		case L:
			x -= YSPEED;
			if(this.good)
			{
				x -= DELTA_SPEED;
			}
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			if(this.good)
			{
				x -= DELTA_SPEED;
				y -= DELTA_SPEED;
			}
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			if(this.good)
			{
				x -= DELTA_SPEED;
				y += DELTA_SPEED;
			}
			break;
		case R:
			x += XSPEED;
			if(this.good)
			{
				x += DELTA_SPEED;
			}
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			if(this.good)
			{
				x += 3;
				y -= 3;
			}
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			if(this.good)
			{
				x += DELTA_SPEED;
				y += DELTA_SPEED;
			}
			break;
		case U:
			y -= YSPEED;
			if(this.good)
			{
				y -= DELTA_SPEED;
			}
			break;
		case D:
			y += YSPEED;
			if(this.good)
			{
				y += DELTA_SPEED;
			}
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
		if(y < 25)  y = 25;  //���ڱ�������һ���Ŀ��
		if(x + Tank.WIDTH> TankClient.GAME_WIDTH)  x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT)  y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		if(!good)
		{
			//�õз�̹����ĳ���������ƶ���������Ҫ������̫�죬�����˸��
			Direction[] dirs = Direction.values();  //��ö��ת��Ϊ����
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
		//��F2���¿�ʼ��Ϸ��̹�˳���λ�������
		//��F2ֻ���ҷ�̹�˻����֣���Ϊ�з�̹���Ǽ���list�У�����live==falseʱ��ֱ��ʹ��remove���ڴ�ɾ����
		//������live=true��Ҳ���ử����
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
		case KeyEvent.VK_S:
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
	
	//�ָ�֮ǰ���Ǹ�λ��
	public void stay()
	{
		this.x = this.oldX;
		this.y = this.oldY;
	}
	
	public boolean hitWall(Wall w)
	{
		if(this.live && this.getRect().intersects(w.getRect()))
		{
			//this.dir = direction.STOP; //����̹�˾�ͣ��ǽ�ϲ�����
			this.stay();   //�ָ���ײ��ǽ֮ǰ���Ǹ�λ��
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
		//this.life != LIFE_INIT)��ʾ��Ѫ��ʱ�򲻳�
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
			//̹�˻���ͼƬ�ˣ��������bloodbar����û��Ӧ���޸�����Ϳ�ȼ���
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







