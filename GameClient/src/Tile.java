import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tile extends addModel{
	public static final int BLOCK_W = 51;
	public static final int BLOCK_H = 62;

	public static final int START_W= 25;
	public static final int START_H = 62;
	
	private String type;
	
	public Tile(int x, int y, String type, Graphics g) {
		super(START_W +BLOCK_W*x, START_H+BLOCK_H*y, BLOCK_W,BLOCK_H,g);
		
		switch(type) {
		case "block1":
			setImage(new ImageIcon(GamePlayer.class.getResource("/assets/map/forest/block/block_1.png")).getImage());
			break;
		case "flower1":
			setImage(new ImageIcon(GamePlayer.class.getResource("/assets/map/forest/block/block_4.png"))
					.getImage());
			break;
		case "flower2":
			setImage(new ImageIcon(GamePlayer.class.getResource("/assets/map/forest/block/block_7.png"))
					.getImage());
			break;
		case "tile":
			setImage(new ImageIcon(GamePlayer.class.getResource("/assets/map/forest/tile/tile_6.png"))
					.getImage());
			break;
		}
	}
	
	public void drawImage(){
		drawOneImage();
	}

	
}
