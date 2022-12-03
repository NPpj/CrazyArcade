import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class Item extends addModel{
	private int cnt;
	private ImageObserver observer;
	private int x;
	private int y;
	private Image img;
	
	public Item(String type, int x, int y, Graphics g, int cnt, ImageObserver observer) {
		super(x,y,56,70,g);
		
		this.observer=observer;
		this.cnt=cnt;
		
		g.setClip(x , y, 56, 70);
		
		if(type == "물풍선")
			img =new ImageIcon(GamePlayer.class.getResource("/assets/item/bubble.png")).getImage();
		else if(type =="물줄기")
			img =new ImageIcon(GamePlayer.class.getResource("/assets/item/fluid.png")).getImage();
		else
			img =new ImageIcon(GamePlayer.class.getResource("/assets/item/roller.png")).getImage();
		
	
		setImage(img);
	}

	public void drawImage() {
		drawAllImage(getImage(),getX(),getY(),cnt,3,observer);
	}
}
