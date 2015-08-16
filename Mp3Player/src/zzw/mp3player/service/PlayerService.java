package zzw.mp3player.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import zzw.lrc.LrcProcessor;
import zzw.model.Mp3Info;
import zzw.mp3player.AppConstant;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;

public class PlayerService extends Service
{
	private boolean isPlaying = false;
	private boolean isPausing = false;
	private boolean isReleased = false;
	private boolean isStartLrc = false;
	
	
	MediaPlayer mediaPlayer = null;	
	Mp3Info mp3Info = null;
	
	private ArrayList<Queue> queue = null;
	private long restartTimeMills = 0;
	private long curTimeMills = 0;
	private long nextTimeMills = 0;
	private long beginTimeMills = 0;
	private long pauseTimeMills = 0;	
	private String lrcMsg = null;
	private Handler handler = null;   					//用于启动显示歌词线程
	private UpdataTimeCallback updataTimeCallback = null; 	//线程歌词词线程
	Queue<Long> time = null;
	Queue<String> lrc = null;
	Iterator<Long> iterator = null;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		this.mp3Info = (Mp3Info)intent.getSerializableExtra("mp3_name");
		int MSG = intent.getIntExtra("MSG", 0);
		if(null != mp3Info)
		{
			if(MSG == AppConstant.PlayerMsg.PLAY_MSG)
			{
				this.play(mp3Info);
			}
			else if(MSG == AppConstant.PlayerMsg.PAUSE_MSG)
			{
				this.pause();
			}
			else if(MSG == AppConstant.PlayerMsg.STOP_MSG)
			{
				this.stop();
			}	
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private String getMp3Path(Mp3Info mp3Info)
	{
		String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
		String path = SDCardRoot + File.separator + "mp3" + File.separator + mp3Info.getMp3Name();
		return path;
	}
	
	private void play(Mp3Info mp3Info)
	{
		if(!isPlaying)
		{
			//加载歌词
			prepareLrc(mp3Info.getLrcName());	
			this.time = queue.get(0);
			this.lrc = queue.get(1);
			this.iterator = this.time.iterator();
			
			String path = this.getMp3Path(mp3Info);
			mediaPlayer = MediaPlayer.create(this, Uri.parse("file://" + path));
			//开始计时	
			beginTimeMills = System.currentTimeMillis();
			mediaPlayer.setLooping(false);
			//开始播放
			mediaPlayer.start();
			//5ms后启动歌词显示线程,发送广播消息给PlayerActivity中的receiver，在PlayerActivity中显示歌词
			handler.postDelayed(updataTimeCallback, 0);
			
			isPlaying = true;
			isReleased = false;
			isStartLrc = true;
		}
	}
	
	private void pause()
	{
		if(null != mediaPlayer)
		{
			if(isPlaying)
			{
				mediaPlayer.pause();
				//关闭歌词显示线程，不发送歌词显示广播
				handler.removeCallbacks(updataTimeCallback);
				//记录当前暂停时间点
				pauseTimeMills = System.currentTimeMillis();
			}
			else
			{
				mediaPlayer.start();
				//再次点击暂停，又开始播放音乐、发送显示歌词广播
				handler.postDelayed(updataTimeCallback, 5);
				//将开始时间点往前推，这样歌词才会同步显示，不然歌词会超前显示
				beginTimeMills += (System.currentTimeMillis() - pauseTimeMills);
			}
			isPlaying = isPlaying? false:true;
		}		
	}
	
	private void stop()
	{
		if(null != mediaPlayer)
		{
			if(isPlaying)
			{
				if(!isReleased)
				{
					handler.removeCallbacks(updataTimeCallback);
					mediaPlayer.stop();
					mediaPlayer.release();
					isReleased = true;
					isPlaying = false;
					isStartLrc = true;
				}
			}
		}
	}
	
	private void prepareLrc(String lrcName)
	{
		try
		{	
			InputStream input = new FileInputStream(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator
					+ "mp3" + File.separator + lrcName);
//			System.out.println(lrcName);
			LrcProcessor lrcProcessor = new LrcProcessor();
			this.queue = lrcProcessor.process(input);
			this.updataTimeCallback = new UpdataTimeCallback();
			this.handler = new Handler();
			this.curTimeMills = 0;
			this.nextTimeMills = 0;
			this.beginTimeMills = 0;
			this.pauseTimeMills = 0;			
			
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class UpdataTimeCallback implements Runnable 
	{
		boolean isFinished = false;
		Intent intent2PlayActivity = null;
		
		public UpdataTimeCallback()
		{
			this.intent2PlayActivity = new Intent();
		}
		
		@Override
		public void run()
		{
			long offset = System.currentTimeMillis() - beginTimeMills;
			if(true == isStartLrc )  //获取第一句歌词
			{
				nextTimeMills = PlayerService.this.time.poll();
				lrcMsg = PlayerService.this.lrc.poll();
				isStartLrc = false;
//				System.out.println("start");
			}
			if(offset >= nextTimeMills)
			{
//				System.out.println(lrcMsg);
				intent2PlayActivity.setAction(AppConstant.LRC_MSG_ACTION);
				intent2PlayActivity.putExtra("lrc_msg", lrcMsg);
				PlayerService.this.sendBroadcast(intent2PlayActivity);
				if(PlayerService.this.iterator.hasNext())
				{					
					nextTimeMills = PlayerService.this.time.poll();
					lrcMsg = PlayerService.this.lrc.poll();
				}
				else
				{
					intent2PlayActivity.setAction(AppConstant.LRC_MSG_ACTION);
					intent2PlayActivity.putExtra("lrc_msg", "over");
					PlayerService.this.sendBroadcast(intent2PlayActivity);
					handler.removeCallbacks(updataTimeCallback);
					isFinished = true;
					while(mediaPlayer.isPlaying())
					{
						/*空循环，等待播放完成*/
					}
					stop();
				}
			}
			if(false == isFinished)
			{
				curTimeMills += 10;
				//没5ms启动一次线程
				handler.postDelayed(updataTimeCallback, 5);
			}
		}
	}
	
}
