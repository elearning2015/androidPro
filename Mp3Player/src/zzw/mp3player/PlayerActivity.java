package zzw.mp3player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;
import org.apache.http.util.EncodingUtils;
import zzw.lrc.LrcProcessor;
import zzw.model.Mp3Info;
import zzw.mp3player.service.PlayerService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlayerActivity extends ActionBarActivity
{
	private ImageButton beginBtn = null;
	private ImageButton pauseBtn = null;
	private ImageButton stopBtn = null;
	
	private LrcMsgBroadcastReceiver receiver = null;
	private IntentFilter receiverIntentFilter = null;
	private TextView lrcTextView = null;
	
	private boolean isStoped = false;
	
	Mp3Info mp3Info = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.player);
		Intent intent = this.getIntent();
		mp3Info = (Mp3Info)intent.getSerializableExtra("mp3_name");
		this.beginBtn = (ImageButton)findViewById(R.id.begin);
		this.pauseBtn = (ImageButton)findViewById(R.id.pause);
		this.stopBtn = (ImageButton)findViewById(R.id.stop);
		this.beginBtn.setOnClickListener(new BeginListener());
		this.pauseBtn.setOnClickListener(new PauseListener());
		this.stopBtn.setOnClickListener(new StopListener());
		this.lrcTextView = (TextView)findViewById(R.id.lrcText);
		TextPaint tp = this.lrcTextView.getPaint();
		tp.setFakeBoldText(true);	
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(receiver);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		this.receiver = new LrcMsgBroadcastReceiver();
		registerReceiver(receiver, this.getIntentFilter());
	}
	
	private class BeginListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{	
			if(null != mp3Info)
			{
				Intent intent = new Intent();
				intent.putExtra("mp3_name", mp3Info);
				intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
				//使用sevice来播放
				intent.setClass(PlayerActivity.this, PlayerService.class);
				//启动service
				PlayerActivity.this.startService(intent);
				isStoped = false;
			}
		}
		
	}
	
	private class PauseListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			if(null != mp3Info && false == isStoped)
			{				
				Intent intent = new Intent();
				intent.putExtra("mp3_name", mp3Info);
				intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
				intent.setClass(PlayerActivity.this, PlayerService.class);
				PlayerActivity.this.startService(intent);
			}
		}
	}
	
	private class StopListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			if(null != mp3Info)
			{
				Intent intent = new Intent();
				intent.putExtra("mp3_name", mp3Info);
				intent.putExtra("MSG", AppConstant.PlayerMsg.STOP_MSG);
				intent.setClass(PlayerActivity.this, PlayerService.class);
				PlayerActivity.this.startService(intent);
				isStoped = true;
			}			
		}
		
	}
	
	private IntentFilter  getIntentFilter()
	{
		if(null == this.receiverIntentFilter)
		{
			this.receiverIntentFilter = new IntentFilter();
			this.receiverIntentFilter.addAction(AppConstant.LRC_MSG_ACTION);
		}
		return this.receiverIntentFilter;
	}
	
	
	private class LrcMsgBroadcastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String lrcMsg = intent.getStringExtra("lrc_msg");
			PlayerActivity.this.lrcTextView.setText(lrcMsg);
		}
		
	}
	
}
