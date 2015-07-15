package com.zzw.tankWar;
import java.awt.*;
import java.util.*;

/**
 * 生命值类
 * @author 张
 *
 */
public class Blood
{
	private int x, y, w, h;
	Random rn = new Random();
	private boolean live = true;   //被吃掉就消失
	private int step;
	
	//血块运动轨迹
	private int [][]pos = {{350, 300}, {330, 250}, {300, 230},
			{370, 330}, {390, 270}, {300, 300}, {280, 240}, {350, 300}};
	TankClient tc;
	
	
	public Blood(int w, int h)
	{
		this.w = w;
		this.h = h;
	}
	
	public Blood(int x, int y, int w, int h) 
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void draw(Graphics g)
	{
		if(!this.live)  return;
		Color c = g.getColor();
		g.setColor(Color.magenta);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}
	
	public void move()
	{
		step++;
		if(pos.length == step)
		{
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect()
	{
		return new Rectangle(x, y, w, h);
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
