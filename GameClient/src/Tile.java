import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tile {
	private String type;
	public static final int BLOCK_W = 50;
	public static final int BLOCK_H = 60;
	public static final int GRASS_W = 50;
	public static final int GRASS_H = 50;
	public static final int FLOWER_W = 60;
	public static final int FLOWER_H = 70;
	
	public Tile(int x, int y, String state, Graphics g) {
		switch(state) {
		case "block1":
			setState(getTileBlock1());
			g.drawImage(getState(), (x*50)+25,(y*50)+48, BLOCK_W, BLOCK_H, null);
			break;
		case "flower1":
			setState(getTileFlower1());
			g.drawImage(getState(), (x*50)+25,(y*50)+48, FLOWER_W, FLOWER_H, null);
			break;
		case "flower2":
			setState(getTileFlower2());
			g.drawImage(getState(), (x*50)+25,(y*50)+48, FLOWER_W, FLOWER_H, null);
			break;
		}
	}
	// 이미지 파일
	private Image block1 = new ImageIcon(GamePlayer.class.getResource("/assets/map/forest/block/block_1.png"))
			.getImage();
	private Image flower1 = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/block/block_4.png"))
			.getImage();
	private Image flower2 = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/block/block_7.png"))
			.getImage();
	
	// 표시할 이미지
	private Image state;
	
	public Image getState() {
		return state;
	}
	public void setState(Image image) {
		this.state = image;
	}
	
	public Image getTileBlock1() {
		return block1;
	}
	public Image getTileFlower1() {
		return flower1;
	}
	public Image getTileFlower2() {
		return flower2;
	}
	
//	public Tile(int x, int y, int w, int h, String type, Container c) {
//		this.type=type;
//	    JLabel lbl = new JLabel(getImageIcon());
//	    lbl.setBounds((x*50)+25, (y*50)+48, w, h);
//	    c.add(lbl);
//	}
//	
//	public ImageIcon getImageIcon() {
//		ImageIcon tileIcon = new ImageIcon();
//		switch(type) {
//		case "풀1":
//			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_6.png"));
//			break;
//		case "풀2":
//			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_7.png"));
//			break;
//		case "풀3":
//			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_8.png"));
//			break;
//		case "풀4":
//			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_9.png"));
//			break;
//		case "새싹1":
//			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_10.png"));
//			break;
//		case "새싹2":
//			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/tile/tile_5.png"));
//			break;
//		case "상자":
//			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/block/block_1.png"));
//			break;
//		case "꽃1":
//			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/block/block_4.png"));
//			break;
//		case "꽃2":
//			tileIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/map/forest/block/block_7.png"));
//			break;
//			
//		}
//		return tileIcon;
//	}
}
