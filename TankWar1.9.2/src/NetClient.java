import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class NetClient 
{
	public static int UDP_PORT_START = 2222;
	
	private int udpPort;
	
	public NetClient()
	{
		this.udpPort = UDP_PORT_START++;
	}
	
	public void connect(String ip, int port)
	{
		Socket s = null;
		try 
		{
			s = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(this.udpPort);
			System.out.println("Connected to server");
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}finally
		{
			if(null != s)
			{
				try
				{
					s.close();
					s = null;
				}catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}	
}
