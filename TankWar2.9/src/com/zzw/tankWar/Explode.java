package com.zzw.tankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

//按ctrl+shift+O：自动import程序中引入的类


/**
 * 爆炸类
 * @author 张
 *
 */
public class Explode 
{
	private int x, y;
	private boolean live = true;
	private TankClient tc;
	private int step = 0;
	
	//Toolkit用于替java做与本地OS有关的操作，如读文件，不同OS，访问硬盘的方式不一样，所以不能直接由java来操作
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	//1、用来了反射机制：Explode.class是Class类的对象（Class类用于描述类本身的一些信息），getClassLoader
	//是Class的成员函数，返回这个类（即Explode）的装载器（即ClassLoader对象），getResource是类装载器
	//的成员函数，它会到项目的classpath（即./bin/...,所以只要把资源文件与放在bin目录下，则程序拷贝到
	//任何地方都可执行）下查找指定的资源文件，并返回这个资源文件的url。
	//2、另外，Class类是对特定类信息的表述，是类的元信息（metadate或metainfo）
	//3、imgs定义成static，否则每new一个类对象，就会=读一次硬盘，相应的Toolkit对象也应该为static
	private static Image []imgs = {
		tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif")),
	};
	
	//为了解决第一个报爆炸时不显示爆炸图片。原因可能是：drawImage的工作方式是异步的，即当图片还没加载
	//到内存时，它先干别的事去了，不在这儿等着，以为windows将图片加载到显存时，会先找一个代理，加载图片
	//大小的空框（这样是为了节省内存，比如word中有较大图片时，快速浏览时只能看到一个框，只有当阅读者停留
	//时间较长时，认为是在看图片，才将图片显示出来），然后再将图片实际数据加载到显存中。
	//那我们就事先加载所有的爆炸图片，这样显存中有图片了就会显示出来。
	private static boolean init = false;   
	
	public Explode(int x, int y, TankClient tc) 
	{
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g)
	{
		if(false == init)
		{
			for (int j = 0; j < imgs.length; j++) 
			{
				g.drawImage(imgs[j], x, y, null);
			}
			init = true;
		}
		
		if(!live)
		{
			tc.explodes.remove(this);
			return;
		}
		if(step == this.imgs.length)
		{
			live = false;
			return;
		}
		g.drawImage(imgs[step], x, y, null);
		step++;
	}
	
	public boolean isLive() 
	{
		return live;
	}
}
