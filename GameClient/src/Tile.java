import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tile {
	private String type;
	
	public Tile(int x, int y, int w, int h, String type, Container c) {
		this.type=type;
	    JLabel lbl = new JLabel(getImageIcon());
	    lbl.setBounds((x*50)+25, (y*50)+48, w, h);
	    c.add(lbl);
	}
	
	public ImageIcon getImageIcon() {
		ImageIcon tileIcon = new ImageIcon();
		switch(type) {
		case "풀1":
			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_6.png"));
			break;
		case "풀2":
			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_7.png"));
			break;
		case "풀3":
			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_8.png"));
			break;
		case "풀4":
			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_9.png"));
			break;
		case "새싹1":
			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_10.png"));
			break;
		case "새싹2":
			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_5.png"));
			break;
		case "상자":
			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/block/block_1.png"));
			break;
		case "꽃1":
			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/block/block_4.png"));
			break;
		case "꽃2":
			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/block/block_7.png"));
			break;
			
		}
		return tileIcon;
	}
		

	
    	
}
