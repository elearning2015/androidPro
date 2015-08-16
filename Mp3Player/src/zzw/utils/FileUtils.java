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
	 * ���캯���У���ȡ��ǰ�豸��SD����·��
	 */
	public FileUtils()
	{
		this.SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * ��SD����ָ��Ŀ¼������һ���ļ�
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
				//����ֻ������Mp3Info�����name��size��
				//ʵ���ϣ��޷���ȡMp3Info�����id��lrc_name��lrc_size������������lrc�ļ���
				//���ڣ����ļ������ˣ�����Mp3Info����ʵ��
				mp3Info.setMp3Name(files[i].getName());
				mp3Info.setMp3Size(files[i].length() + "");
				mp3Info.setLrcName(files[i].getName().substring(0, files[i].getName().length()-4) + ".lrc");
				mp3Infos.add(mp3Info);
			}
		}
		return mp3Infos;
	}
}
