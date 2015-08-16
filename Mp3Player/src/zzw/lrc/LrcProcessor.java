package zzw.lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zzw.download.HttpDownloder;
import zzw.mp3player.AppConstant;



public class LrcProcessor
{
	
	public ArrayList<Queue> process(InputStream input)
	{
		if(null == input)
		{
			System.out.println("输入流为空");
			System.exit(0);
		}
		Queue<Long> timeMills = new LinkedList<Long>();
		Queue<String> msg = new LinkedList<String>();
		ArrayList<Queue> queues = new ArrayList<Queue>();
		
		InputStreamReader isr = null;
		BufferedReader br = null;
		try
		{
			isr = new InputStreamReader(input, "GB2312");  //解决中文乱码
			br =  new BufferedReader(isr);
		} catch (UnsupportedEncodingException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Pattern pattern = Pattern.compile("\\[([\\d\\.:^\\]]+)\\]"); 
		String result = null;
		String tmp = null;
		String lastTimeStr = null;
		boolean first = true;
		try
		{
			while((tmp = br.readLine()) != null)
			{
				//System.out.println(tmp);
				Matcher m = pattern.matcher(tmp);
				if(m.find())
				{
					String timeStr = m.group(1);
					//System.out.println(""+timeStr);
					Long mills = this.time2Long(timeStr.substring(1, timeStr.length() - 1));
					timeMills.offer(mills);
					String remianMsgLine = tmp.substring(timeStr.length()+2);
					//System.out.println(remianMsgLine.length());
					if(0 != remianMsgLine.length())
					{
						result = remianMsgLine + "\n";	
						//System.out.println(remianMsgLine);
						lastTimeStr = remianMsgLine;
						first = false;
					}
					else
					{
						result = lastTimeStr + "\n";
					}
					msg.add(result);
				}
				else
				{
					//将不带时间的歌词行内容加到一起（这种情况即一个是时间管多行歌词）
					if(null != result)
					{
						result = result + tmp + "\n";  
					}
				}
			}
			msg.add(result);
			queues.add(timeMills);
			queues.add(msg);
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queues;
	}

	/**
	 * 将时间转化为毫秒
	 * @param timeStr
	 * @return
	 */
	private Long time2Long(String timeStr)
	{
		String[] str = timeStr.split(":");
		int min = Integer.parseInt(str[0]);
		String[] ss = str[1].split("\\.");
		int second = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		
		return min * 60 * 1000 + second * 1000 + mill * 100L;
	}
	
}
