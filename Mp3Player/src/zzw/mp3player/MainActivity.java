package zzw.mp3player;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		
		TabHost tabHost = this.getTabHost();
		//生成一个intent，指向一个Activity
		Intent remoteIntent = new Intent();
		remoteIntent.setClass(this, RemoteMp3ListActivity.class);
		//生成一个TabSpec对象，代表一页
		TabHost.TabSpec remoteSpec = tabHost.newTabSpec("Remote");
		//得到系统资源，用于设置系统自带图标
		Resources res = this.getResources();
		//设置该页的indicator
		remoteSpec.setIndicator("Remote", res.getDrawable(android.R.drawable.stat_sys_download));
		//设置该页的内容
		remoteSpec.setContent(remoteIntent);
		//将设置好的该页内容添加到TabHost中
		tabHost.addTab(remoteSpec);
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, LocalMp3ListActivity.class);
		TabHost.TabSpec localSpec = tabHost.newTabSpec("Local");
		localSpec.setIndicator("Local", res.getDrawable(android.R.drawable.stat_sys_upload));
		localSpec.setContent(localIntent);
		tabHost.addTab(localSpec);
		
	}
	

}
