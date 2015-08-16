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
		//����һ��intent��ָ��һ��Activity
		Intent remoteIntent = new Intent();
		remoteIntent.setClass(this, RemoteMp3ListActivity.class);
		//����һ��TabSpec���󣬴���һҳ
		TabHost.TabSpec remoteSpec = tabHost.newTabSpec("Remote");
		//�õ�ϵͳ��Դ����������ϵͳ�Դ�ͼ��
		Resources res = this.getResources();
		//���ø�ҳ��indicator
		remoteSpec.setIndicator("Remote", res.getDrawable(android.R.drawable.stat_sys_download));
		//���ø�ҳ������
		remoteSpec.setContent(remoteIntent);
		//�����úõĸ�ҳ������ӵ�TabHost��
		tabHost.addTab(remoteSpec);
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, LocalMp3ListActivity.class);
		TabHost.TabSpec localSpec = tabHost.newTabSpec("Local");
		localSpec.setIndicator("Local", res.getDrawable(android.R.drawable.stat_sys_upload));
		localSpec.setContent(localIntent);
		tabHost.addTab(localSpec);
		
	}
	

}
