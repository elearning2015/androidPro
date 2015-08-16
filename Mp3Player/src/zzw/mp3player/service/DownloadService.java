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
		///���������̣߳������߳���ʵ����Mp3Info����
		DownloadThread downloadThread = new DownloadThread(info);
		Thread thread = new Thread(downloadThread);
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	//�����߳�
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
				//this.resultMsg = "�ļ�����ʧ��";
				System.out.println("mp3����ʧ��");
			}
			else if(1 == retMp3)
			{
//				this.resultMsg = "�ļ��Ѵ���";
				System.out.println("mp3�ļ��Ѵ���");
			}
			else if(0 == retMp3)
			{
//				this.resultMsg = "�ļ����سɹ�";
				System.out.println("MP3���سɹ�");
			}
			if(-1 == retLrc)
			{
				//this.resultMsg = "�ļ�����ʧ��";
				System.out.println("Lrc����ʧ��");
			}
			else if(1 == retLrc)
			{
//				this.resultMsg = "�ļ��Ѵ���";
				System.out.println("Lrc�ļ��Ѵ���");
			}
			else if(0 == retLrc)
			{
//				this.resultMsg = "�ļ����سɹ�";
				System.out.println("Lrc���سɹ�");
			}
			
		}
		
	}
	

}
