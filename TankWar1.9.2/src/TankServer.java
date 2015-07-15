import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class TankServer
{
	
	public static final int TCP_PORT = 8888;
	public static int ID = 100;  //记录每辆坦克的身份，用于坦克间互相识别
	
	private List<Client> clients = new ArrayList<Client>();
	
	public void start()
	{
		ServerSocket ss = null;
		try 
		{
			ss = new ServerSocket(TCP_PORT);  //当端口后被占用时，构建ss会失败
		} catch (IOException e)
		{
			e.printStackTrace();
		} 
		
		while(true)
		{
			Socket s = null;
			try
			{
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String ip = s.getInetAddress().getHostAddress();
				int udpPort = dis.readInt();
				clients.add(new Client(ip, udpPort));
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID++);  //当坦克连接服务器，就将坦克ID给它自己
				//getPort得到的是客户端连接服务器使用的TCP端口
				System.out.println("A conneted:" + s.getInetAddress() + ":" + s.getPort() + "--udp port:" + udpPort);
			} catch(IOException e)
			{
				e.printStackTrace();
			} finally
			{
				try 
				{
					if(null != s)
					{
						s.close();
						s = null;
					}
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public static void main(String[] args) 
	{
		TankServer ts = new TankServer();
		ts.start();
	}
	
	private class Client
	{
		private int udpPort;
		private String ip;
		
		public Client(String ip, int port)
		{
			this.udpPort = port;
			this.ip = ip;
		}
	}

}
