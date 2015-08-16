package zzw.mp3player;

public interface AppConstant
{
	public static final String LRC_MSG_ACTION = "zzw.mp3player.lrcMsg.action";
	
	public class PlayerMsg
	{
		public static final int PLAY_MSG = 1;
		public static final int PAUSE_MSG = 2;
		public static final int STOP_MSG = 3;
	}
	public class URL
	{
		public static final String  BASE_URL = "http://10.0.2.2:8080/mp3/";
	}
}
