package com.zzw.tankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

//��ctrl+shift+O���Զ�import�������������


/**
 * ��ը��
 * @author ��
 *
 */
public class Explode 
{
	private int x, y;
	private boolean live = true;
	private TankClient tc;
	private int step = 0;
	
	//Toolkit������java���뱾��OS�йصĲ���������ļ�����ͬOS������Ӳ�̵ķ�ʽ��һ�������Բ���ֱ����java������
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	//1�������˷�����ƣ�Explode.class��Class��Ķ���Class�����������౾���һЩ��Ϣ����getClassLoader
	//��Class�ĳ�Ա��������������ࣨ��Explode����װ��������ClassLoader���󣩣�getResource����װ����
	//�ĳ�Ա���������ᵽ��Ŀ��classpath����./bin/...,����ֻҪ����Դ�ļ������binĿ¼�£�����򿽱���
	//�κεط�����ִ�У��²���ָ������Դ�ļ��������������Դ�ļ���url��
	//2�����⣬Class���Ƕ��ض�����Ϣ�ı����������Ԫ��Ϣ��metadate��metainfo��
	//3��imgs�����static������ÿnewһ������󣬾ͻ�=��һ��Ӳ�̣���Ӧ��Toolkit����ҲӦ��Ϊstatic
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
	
	//Ϊ�˽����һ������ըʱ����ʾ��ըͼƬ��ԭ������ǣ�drawImage�Ĺ�����ʽ���첽�ģ�����ͼƬ��û����
	//���ڴ�ʱ�����ȸɱ����ȥ�ˣ�����������ţ���Ϊwindows��ͼƬ���ص��Դ�ʱ��������һ����������ͼƬ
	//��С�Ŀտ�������Ϊ�˽�ʡ�ڴ棬����word���нϴ�ͼƬʱ���������ʱֻ�ܿ���һ����ֻ�е��Ķ���ͣ��
	//ʱ��ϳ�ʱ����Ϊ���ڿ�ͼƬ���Ž�ͼƬ��ʾ��������Ȼ���ٽ�ͼƬʵ�����ݼ��ص��Դ��С�
	//�����Ǿ����ȼ������еı�ըͼƬ�������Դ�����ͼƬ�˾ͻ���ʾ������
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
