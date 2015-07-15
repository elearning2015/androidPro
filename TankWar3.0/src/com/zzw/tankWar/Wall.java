package com.zzw.tankWar;
import java.awt.*;

/**
 * 墙类
 * @author 张
 *
 */
public class Wall 
{
	private int x, y, w, h;
	private Image wall;
	TankClient tc;
	
	
	public Wall(int x, int y, Image wall, int w, int h, TankClient tc) 
	{
		this.x = x;
		this.y = y;
		this.wall = wall;
		this.w = w; //wall.getWidth(null)；这个获取的始终为-1
		this.h = h; //wall.getHeight(null);
	//	System.out.println("w"+w+"\n" + "h"+h);
		this.tc = tc;
	}
	
	public void draw(Graphics g)
	{
//		Color c = g.getColor();
//		g.setColor(Color.darkGray);
//		g.fillRect(x, y, w, h);
//		g.setColor(c);
		g.drawImage(wall, x, y, null);
	}
	
	public Rectangle getRect()
	{
		return new Rectangle(x, y, w, h);
	}
}
