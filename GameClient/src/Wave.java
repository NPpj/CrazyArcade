import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public class Wave extends addModel{
	private int cnt;
	private ImageObserver observer;
	private Image down;
	private Image up;
	private Image left;
	private Image right;
	private Image center;
	private Image bubble;
	
	public Wave(int x, int y, Graphics g,int cnt, ImageObserver observer) {
		super(Tile.START_W +Tile.BLOCK_W*x, Tile.START_H+Tile.BLOCK_H*y,52,52,g);
		
		this.observer=observer;
		this.cnt=cnt;
		
		center = new ImageIcon(GamePlayer.class.getResource("/assets/pop.png")).getImage();
		down = new ImageIcon(GamePlayer.class.getResource("/assets/wave/down_end.png")).getImage();
		up = new ImageIcon(GamePlayer.class.getResource("/assets/wave/up_end.png")).getImage();
		left = new ImageIcon(GamePlayer.class.getResource("/assets/wave/left_end.png")).getImage();
		right = new ImageIcon(GamePlayer.class.getResource("/assets/wave/right_end.png")).getImage();
		bubble = new ImageIcon(GamePlayer.class.getResource("/assets/1.png")).getImage();
	}

	public void drawImage() {
		drawAllImage(down,getX(),getY()+52,cnt,11,observer);
		drawAllImage(up,getX(),getY()-52,cnt,11,observer);
		drawAllImage(left,getX()-52,getY(),cnt,11,observer);
		drawAllImage(right,getX()+52,getY(),cnt,11,observer);
		drawAllImage(center,getX(),getY(),cnt,11,observer);
		
	}
}
