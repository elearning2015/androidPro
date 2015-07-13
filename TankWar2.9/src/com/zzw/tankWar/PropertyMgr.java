package com.zzw.tankWar;

import java.io.IOException;
import java.util.Properties;

/**
 * ProperMgr���ڴ���ֻ��һ�ݣ����ڴ���Ҳֻ��һ��tank.properties�����ļ����Ժ����
 * getProperty����ֱ�Ӵ��ڴ��ж�ȡ 
 * @author ��
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
	
	private PropertyMgr() {};  //��������ʱ�ϸ�ĵ���ģʽsingleton�ˣ��ڴ���ֻ��һ�ݶ��������಻��new��������
	
	public static String getProperty(String key)
	{
		return props.getProperty(key);
	}
	
}
