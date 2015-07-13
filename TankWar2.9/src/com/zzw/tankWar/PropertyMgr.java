package com.zzw.tankWar;

import java.io.IOException;
import java.util.Properties;

/**
 * ProperMgr在内存中只有一份，且内存中也只有一份tank.properties配置文件，以后调用
 * getProperty函数直接从内存中读取 
 * @author 张
 *
 */

public class PropertyMgr 
{
	private static Properties props = new Properties();
	
	static
	{
		try 
		{
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	private PropertyMgr() {};  //加上这句计时严格的单例模式singleton了，内存中只有一份对象，其他类不能new这个类对象
	
	public static String getProperty(String key)
	{
		return props.getProperty(key);
	}
	
}
