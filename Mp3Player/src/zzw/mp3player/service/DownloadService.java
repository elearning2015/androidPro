package zzw.mp3player.service;

import zzw.download.HttpDownloder;
import zzw.model.Mp3Info;
import zzw.mp3player.AppConstant;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service
{

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Mp3Info info = (Mp3Info)intent.getSerializableExtra("mp3Info");	
		///生成下载线程，并在线程中实例化Mp3Info对象
		DownloadThread downloadThread = new DownloadThread(info);
		Thread thread = new Thread(downloadThread);
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	//下载线程
	private class  DownloadThread implements Runnable
	{
		private Mp3Info mp3Info = null;
		private HttpDownloder httpDownloder = null;
		String urlMp3 = null;
		String urlLrc = null;
		String resultMsg = null;
		int retMp3 = -1;
		int retLrc = -1;
		
		public DownloadThread(Mp3Info mp3Info)
		{
			this.mp3Info = mp3Info;
		}
		
		@Override
		public void run()
		{
			this.urlMp3 = AppConstant.URL.BASE_URL + mp3Info.getMp3Name();
			this.urlLrc = AppConstant.URL.BASE_URL + mp3Info.getLrcName();
			this.httpDownloder = new HttpDownloder();
			this.retMp3 = this.httpDownloder.downloadFile(urlMp3, "mp3", mp3Info.getMp3Name());
			this.retLrc = this.httpDownloder.downloadFile(urlLrc, "mp3", mp3Info.getLrcName());
			if(-1 == retMp3)
			{
				//this.resultMsg = "文件下载失败";
				System.out.println("mp3下载失败");
			}
			else if(1 == retMp3)
			{
//				this.resultMsg = "文件已存在";
				System.out.println("mp3文件已存在");
			}
			else if(0 == retMp3)
			{
//				this.resultMsg = "文件下载成功";
				System.out.println("MP3下载成功");
			}
			if(-1 == retLrc)
			{
				//this.resultMsg = "文件下载失败";
				System.out.println("Lrc下载失败");
			}
			else if(1 == retLrc)
			{
//				this.resultMsg = "文件已存在";
				System.out.println("Lrc文件已存在");
			}
			else if(0 == retLrc)
			{
//				this.resultMsg = "文件下载成功";
				System.out.println("Lrc下载成功");
			}
			
		}
		
	}
	

}
