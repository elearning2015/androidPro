package zzw.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import zzw.utils.FileUtils;

public class HttpDownloder 
{
	private URL url = null;
	
	/**
	 * 下载文本文件，没有存盘
	 */
	public String download(String urlStr)
	{
		StringBuffer sb = new StringBuffer();
		BufferedReader buffer = null;
		String line = null;
		try 
		{
			this.url = new URL(urlStr);
			HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
			buffer = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			while((line = buffer.readLine()) != null)
			{
				sb.append(line);
			}
		} catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}finally
		{
			try 
			{
				buffer.close();
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return
	 */
	public int downloadFile(String urlStr, String path, String fileName)
	{
		InputStream input = null;
		File file = null;
		
		try
		{
			FileUtils utils = new FileUtils();
			if(utils.isFileExist(path, fileName))
				return 1;
			else
			{
				input = this.getInputStreamFromURL(urlStr);
				//写SD卡，注意需要再manifest文件中声明权限
				file = utils.write2SDFromInput(path, fileName, input);
				if(null == file)
				{
					return -1;
				}
			}
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
			return -1;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return -1;
		}
		finally
		{
			try 
			{
				if(null != input)  //否则，当文件存在时，报空指针错误，因为此时input没有实例化
				{
					input.close();
				}
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromURL(String urlStr) 
			throws MalformedURLException, IOException 														
	{
		InputStream input = null;
		URL url = new URL(urlStr);
		HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
		input = httpConn.getInputStream();

		return input;
	}

}
