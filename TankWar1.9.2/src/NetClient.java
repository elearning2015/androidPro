import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class NetClient 
{
	public static int UDP_PORT_START = 2222;
	
	TankClient tc;
	private int udpPort;
	
	public NetClient(TankClient tc)
	{
		this.udpPort = UDP_PORT_START++;
		this.tc = tc;
	}
	
	public void connect(String ip, int port)
	{
		Socket s = null;
		try 
		{
			s = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(this.udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id  = dis.readInt();
			tc.myTank.id = id;   //获取主战坦克自己的id
			System.out.println("Connected to server" + "server gives me id:" + id);
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
