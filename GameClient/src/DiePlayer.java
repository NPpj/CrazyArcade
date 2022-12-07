import java.awt.Graphics;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class DiePlayer extends addModel{
	private long cnt;
	private ImageObserver observer;
	private int x;
	private int y;
	
	public DiePlayer(int x, int y, Graphics g, long cnt, ImageObserver observer) {
		super(x,y,95,144,g);
		
		this.observer=observer;
		this.cnt=cnt;
		setImage(new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/die.png")).getImage());
	}

	public void drawImage() {
		drawAllImage(getImage(),getX(),getY(),cnt,12,observer);
	}
}
