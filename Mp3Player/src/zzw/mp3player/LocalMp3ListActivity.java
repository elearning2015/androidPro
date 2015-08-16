package zzw.mp3player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import zzw.model.Mp3Info;
import zzw.utils.FileUtils;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LocalMp3ListActivity extends ListActivity
{
	private List<Mp3Info> mp3Infos = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.local_mp3_list);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		FileUtils fileUtil = new FileUtils();
		mp3Infos = new ArrayList<Mp3Info>();
		//注意：这里只能获取Mp3Info对象的name和size，无法获取其id、lrc信息（除非也下载了lrc文件）
		mp3Infos = fileUtil.getMp3Files("mp3");  
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (Iterator<Mp3Info> iterator = mp3Infos.iterator(); iterator.hasNext();)
		{
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", mp3Info.getMp3Name());
			map.put("mp3_size", mp3Info.getMp3Size());
			list.add(map);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.mp3info_item,
				new String[] {"mp3_name", "mp3_size"}, new int[] {R.id.mp3_name, R.id.mp3_size});
		this.setListAdapter(simpleAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		if(null != this.mp3Infos)
		{
			//List中Mp3Info对象的排列顺序与ListView中显示的各Mp3Info对象是一样的
			//所以可以通过position位置（0、1、2...）来获取mp3Infos中对象
			Mp3Info mp3Info = this.mp3Infos.get(position);
			Intent intent = new Intent();
			intent.putExtra("mp3_name", mp3Info);
			intent.setClass(this, PlayerActivity.class);
			this.startActivity(intent);
		}
	}
	
	

}
