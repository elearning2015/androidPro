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
	 * �ڵ��menu��ťʱ������������������������������Ӱ�ť�ؼ�
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
	 * �����ĳ���˵���ť��������������
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		System.out.println("itemID-->"+item.getItemId());
		if (UPDATA == item.getItemId())   //�����б�
		{
			this.updataListView();
		}
		else if(ABOUT == item.getItemId()) //����
		{
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id)
	{
		//�����û�����б��е�λ�ã����õ���Ӧ��Mp3Info����
		Mp3Info info = this.mp3Infos.get(position);
		Intent intent = new Intent();
		//��Mp3Info�������intent������
		intent.putExtra("mp3Info", info);
		//��intent����service
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
			//�����������������ʵ�ֵ�
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
	 * ��info��MP3��Ϣ��ʾ��listview�У���������Ǵ�updataListView�з�������Ķ
	 * Ŀ���Ǳ����ع����������ܾ����ܵ�һ��
	 * @param infos
	 * @return
	 */
	private SimpleAdapter buildSimpleAdapter(List<Mp3Info> infos)
	{	
		Mp3Info info = null;
		HashMap<String, String> map = null;
		//����һ��List��������SimpleAdapter��׼����infos�е�������ӵ�List��
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
	 * ����MP3�ļ��б�
	 */
	private void updataListView()
	{
		//���ذ���MP3�ļ���Ϣ��xml�ļ�
		String xml = this.downloadXML(AppConstant.URL.BASE_URL + "resources.xml");
		//����xml�ļ��������������Mp3Info�����У������ЩMp3Info�������List��
		this.mp3Infos = this.parse(xml);
		this.buildSimpleAdapter(mp3Infos);
	}
}
