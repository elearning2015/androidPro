package zzw.xml;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import zzw.model.Mp3Info;

public class Mp3ListContentHandler extends DefaultHandler
{
	private List<Mp3Info> infos = null;
	private Mp3Info mp3Info = null;
	private String tagName = null;
	
	public Mp3ListContentHandler(List<Mp3Info> infos)
	{
		super();
		this.infos = infos;
	}

	public List<Mp3Info> getInfos()
	{
		return infos;
	}

	public void setInfos(List<Mp3Info> infos)
	{
		this.infos = infos;
	}

	@Override
	public void startDocument() throws SAXException
	{
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException
	{
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		this.tagName = localName;
		if(this.tagName.equals("resource"))
		{
			this.mp3Info = new Mp3Info();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		if(qName.equals("resource"))
		{
//			System.out.println("--------------");
			this.infos.add(mp3Info);
		}
		this.tagName = "";
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		String tmp = new String(ch, start, length);
		if(this.tagName.equals("id"))
		{
			this.mp3Info.setId(tmp);
		}
		else if(this.tagName.equals("mp3_name"))
		{
			this.mp3Info.setMp3Name(tmp);
		}
		else if(this.tagName.equals("mp3_size"))
		{
			this.mp3Info.setMp3Size(tmp);
		}
		else if(this.tagName.equals("lrc_name"))
		{
			this.mp3Info.setLrcName(tmp);
		}
		else if(this.tagName.equals("lrc_size"))
		{
			this.mp3Info.setLrcSize(tmp);
		}
	}
	
}
