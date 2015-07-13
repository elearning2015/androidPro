package com.zzw.tankWar;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �ӵ���
 * @author ��
 *
 */
public class Missile 
{
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private int x, y;
	Direction dir;
	private boolean live = true;

	private TankClient tc;
	private boolean good;    			//�з�̹�˶�Ӧ���ӵ�Ϊfalse���ҷ���Ӧ���ӵ�Ϊtrue��
										//Ϊ�������Լ��˲����Լ���
	private static int score = 0; 		//��¼�ҷ�̹�˵÷֣�ÿ����һ���з���һ��
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] missileImgs = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();

	//��̬����������֤�˵�����ڵ�һ�α�load���ڴ�ʱ����ִ�У�����Explode����ֱ�Ӷ���imgs��һ���ġ�
	//��static�������ڿ�����һЩ�����ĳ�ʼ�����ر�ע����ǣ�������д���루�������������Ӧд�ڷ����ڣ���
	static 
	{
		missileImgs = new Image[] {
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileL.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileLU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileLD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileR.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileRU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileRD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileD.gif"))
			};
		
		//�ڷ�static��������������Щ���Ͳ���ֱ������д�ˣ���Ϊ���Ǹ�ֵ
		imgs.put("L", missileImgs[0]);
		imgs.put("LU", missileImgs[1]);
		imgs.put("LD", missileImgs[2]);
		imgs.put("R", missileImgs[3]);
		imgs.put("RU", missileImgs[4]);
		imgs.put("RD", missileImgs[5]);
		imgs.put("U", missileImgs[6]);
		imgs.put("D", missileImgs[7]);
	}
	
	public Missile(int x, int y, Direction dir) 
	{
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good, Direction dir, TankClient tc)
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
//		Color c = g.getColor();
//		if(this.good)
//		{
//			g.setColor(Color.YELLOW);  //�ҷ��ӵ���ɫ
//		}
//		else
//		{
//			g.setColor(Color.black);
//		}
//		g.fillOval(x, y, WIDTH, HEIGHT); //����ֻ�����ǰ��ӵ��������ˣ����㲻����һ������x��y��w��h��getRect��
										//Ȼ�����hitTank��ͬ����������̹��
//		g.setColor(c);
		
		switch(dir)
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

	public Rectangle getRect()
	{
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean hitTank(Tank t)
	{
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood())
		{
			if(t.isGood())
			{
				t.setLife(t.getLife() -20);
				if(t.getLife() <= 0)
				{
					t.setLive(false);
				}
				else
				{
					t.setLive(true);
				}
			}
			else
			{
				t.setLive(false);
			}
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
					score++;
					tc.myTank.setScore(score);
					return true;
				}
		}
		return false;
	}
	
	public boolean hitWall(Wall w)
	{
		//�����������!this.good����з��ӵ����ܴ�ǽ�����ҷ�����
		if(this.live && this.getRect().intersects(w.getRect()))
		{
			this.live = false;
			return true;
		}
		return false;
	}
	
	public boolean isLive() 
	{
		return live;
	}
	
	public void setLive(boolean live) 
	{
		this.live = live;
	}
}
