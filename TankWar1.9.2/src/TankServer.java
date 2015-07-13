import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class TankServer
{
	
	public static int TCP_PORT = 8888;
	
	private List<Client> clients = new ArrayList<Client>();
	
	public void start()
	{
		try 
		{
			ServerSocket ss = new ServerSocket(TCP_PORT);
			while(true)
			{
				Socket s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String ip = s.getInetAddress().getHostAddress();
				int udpPort = dis.readInt();
				clients.add(new Client(ip, udpPort));
				s.close();
				System.out.println("A conneted:" + s.getInetAddress() + ":" + s.getPort());
			}
		} catch (IOException e)
		{
			e.printStackTrace();
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
