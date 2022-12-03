import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class Bubble extends addModel{
	private int cnt;
	private ImageObserver observer;
	private int x;
	private int y;
	
	public Bubble(int x, int y, Graphics g, int cnt, ImageObserver observer) {
		super(Tile.START_W +Tile.BLOCK_W*x, Tile.START_H+Tile.BLOCK_H*y,56,54,g);
		
		this.observer=observer;
		this.cnt=cnt;
		setImage(new ImageIcon(GamePlayer.class.getResource("/assets/1.png")).getImage());
	}

	public void drawImage() {
		drawAllImage(getImage(),getX(),getY(),cnt,4,observer);
	}
}
