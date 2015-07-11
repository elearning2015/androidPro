import java.awt.*;

public class Explode 
{
		private int x, y;
		private boolean live = true;
		private TankClient tc;
		private int diameter[] = {4, 8, 18, 28, 35, 45, 30, 20, 10, 4};
		private int step = 0;
		
		public Explode(int x, int y, TankClient tc) 
		{
				this.x = x;
				this.y = y;
				this.tc = tc;
		}
		
		public void draw(Graphics g)
		{
				if(!live)
				{
					tc.explodes.remove(this);
					return;
				}
				if(step == this.diameter.length)
				{
						live = false;
						return;
				}
				Color c = g.getColor();
				g.setColor(Color.orange);
				g.fillOval(x, y, diameter[step], diameter[step]);
				g.setColor(c);
				step++;
		}
		
		public boolean isLive() 
		{
				return live;
		}
}
