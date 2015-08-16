package zzw.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import zzw.model.Mp3Info;
import android.os.Environment;

public class FileUtils
{
	private String SDCardRoot;
	
	public String getSDPATH() 
	{
		return SDCardRoot;
	}

	public void setSDPATH(String sDPATH) 
	{
		SDCardRoot = sDPATH;
	}

	/**
	 * 构造函数中，获取当前设备的SD卡的路径
	 */
	public FileUtils()
	{
		this.SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 在SD卡上指定目录下生成一个文件
	 * @param dir
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createFileInSDCard(String dir, String fileName) throws IOException
	{
		File file = new File(this.SDCardRoot + File.separator + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}
	
	public File createDirInSDCard(String dirName)
	{
		File dir = new File(this.SDCardRoot + File.separator + dirName);
		dir.mkdirs();
		return dir;
	}
	
	public boolean isDirExist(String dir)
	{
		File dirFile = new File(this.SDCardRoot + File.separator + dir);
		return dirFile.exists();
	}
	
	public boolean isFileExist(String dir, String fileName)
	{
		File file = new File(this.SDCardRoot + File.separator + dir + File.separator + fileName);
		return file.exists();
	}
	
	public File write2SDFromInput(String path, String fileName, InputStream input)
	{
		File file = null;
		FileOutputStream output = null;
		
		try 
		{
			this.createDirInSDCard(path);
			file = this.createFileInSDCard(path, fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[4 * 1024];
			int tmp = 0;
			while((tmp = input.read(buffer)) != -1)
			{
				output.write(buffer, 0, tmp);
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				output.close();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}
	
	public List<Mp3Info> getMp3Files(String dir)
	{
		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		if(false == this.isDirExist(dir))
		{
			this.createDirInSDCard(dir);
		}
		File file = new File(this.SDCardRoot + File.separator + dir);
		File[] files = file.listFiles();
		for(int i = 0; i < files.length; i++)
		{	
			Mp3Info mp3Info = new Mp3Info();
			if(files[i].getName().endsWith("mp3"))
			{
				//这里只设置了Mp3Info对象的name和size，
				//实际上，无法获取Mp3Info对象的id、lrc_name、lrc_size（除非下载了lrc文件）
				//这在，是文件对象了，不是Mp3Info对象实体
				mp3Info.setMp3Name(files[i].getName());
				mp3Info.setMp3Size(files[i].length() + "");
				mp3Info.setLrcName(files[i].getName().substring(0, files[i].getName().length()-4) + ".lrc");
				mp3Infos.add(mp3Info);
			}
		}
		return mp3Infos;
	}
}
