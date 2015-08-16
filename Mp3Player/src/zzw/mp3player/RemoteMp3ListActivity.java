package zzw.mp3player;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import zzw.download.HttpDownloder;
import zzw.model.Mp3Info;
import zzw.mp3player.service.DownloadService;
import zzw.xml.Mp3ListContentHandler;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RemoteMp3ListActivity extends ListActivity 
{
	private static final int UPDATA = 1;
	private static final int ABOUT = 2;
	private List<Mp3Info> mp3Infos = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_mp3_list);
		this.updataListView();
	}
	
	
	/**
	 * 在点击menu按钮时，调用这个函数，可在这个函数中添加按钮控件
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.mp3_player, menu);
		menu.add(0, UPDATA, 1, R.string.mp3list_updata);
		menu.add(0, ABOUT, 2, R.string.mp3list_about);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 点击了某个菜单按钮，会调用这个函数
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		System.out.println("itemID-->"+item.getItemId());
		if (UPDATA == item.getItemId())   //更新列表
		{
			this.updataListView();
		}
		else if(ABOUT == item.getItemId()) //关于
		{
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id)
	{
		//根据用户点击列表当中的位置，来得到相应的Mp3Info对象
		Mp3Info info = this.mp3Infos.get(position);
		Intent intent = new Intent();
		//将Mp3Info对象存入intent对象当中
		intent.putExtra("mp3Info", info);
		//将intent传入service
		intent.setClass(this, DownloadService.class);
		this.startService(intent);
		super.onListItemClick(listView, view, position, id);
	}
	
	private String downloadXML(String urlStr)
	{
		HttpDownloder httpDown = new HttpDownloder();
		String result = httpDown.download(urlStr);
		return result;
	}
	
	private List<Mp3Info> parse(String xmlStr)
	{
		List<Mp3Info> infos = null;
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try
		{
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			infos = new ArrayList<Mp3Info>();
			//解析工作在这个类中实现的
			Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(infos);
			xmlReader.setContentHandler(mp3ListContentHandler);
			try
			{
				xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			for (Iterator<Mp3Info> iterator = infos.iterator(); iterator.hasNext();)
//			{
//				Mp3Info mp3Info = (Mp3Info) iterator.next();
//				System.out.println(mp3Info);
//				
//			}
		} catch (SAXException | ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return infos;
	}
	
	/**
	 * 将info中MP3信息显示在listview中，这个函数是从updataListView中分离出来的额，
	 * 目的是便于重构，函数功能尽可能单一化
	 * @param infos
	 * @return
	 */
	private SimpleAdapter buildSimpleAdapter(List<Mp3Info> infos)
	{	
		Mp3Info info = null;
		HashMap<String, String> map = null;
		//生成一个List，并按照SimpleAdapter标准，将infos中的数据添加到List中
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (Iterator<Mp3Info> iterator = infos.iterator(); iterator.hasNext();)
		{
			info = (Mp3Info) iterator.next();
			map = new HashMap<String, String>();
			map.put("mp3_name", info.getMp3Name());
			map.put("mp3_size", info.getMp3Size());
			list.add(map);
		}
	
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.mp3info_item, 
				new String[] {"mp3_name", "mp3_size"}, new int[] {R.id.mp3_name, R.id.mp3_size});
		this.setListAdapter(simpleAdapter);
		
		return simpleAdapter;
	}
	
	/**
	 * 更新MP3文件列表
	 */
	private void updataListView()
	{
		//下载包含MP3文件信息的xml文件
		String xml = this.downloadXML(AppConstant.URL.BASE_URL + "resources.xml");
		//解析xml文件，并将结果放至Mp3Info对象当中，最后将这些Mp3Info对象放至List中
		this.mp3Infos = this.parse(xml);
		this.buildSimpleAdapter(mp3Infos);
	}
}
