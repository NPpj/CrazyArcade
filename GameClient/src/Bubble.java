import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class Bubble{
	private Image userBubble = new ImageIcon(GamingView.class.getResource("/assets/1.png"))
			.getImage();
	private int x;
	private int y;
	private long startCnt;
			
	public Bubble(int x, int y, long startCnt) {
		this.x = x;
		this.y = y;
		this.startCnt = startCnt;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public long getStartCnt() {
		return startCnt;
	}

	public Image getUserBubble() {
		return userBubble;
	}
}
