import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class Item {
	//int item_x = Tile.START_W + Tile.BLOCK_W * Integer.parseInt(xy[0]);
	//int item_y = Tile.START_H + Tile.BLOCK_H * Integer.parseInt(xy[1]) - 20;
	private int x;
	private int y;
	
	public Item(int x, int y) {
		this.x = x;
		this.y = y;
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
}
