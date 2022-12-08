import java.awt.Image;

import javax.swing.ImageIcon;

public class Fluid {
	private Image leftFluid = new ImageIcon(GamingView.class.getResource("/assets/wave/left_end.png"))
			.getImage();
	private Image rightFluid = new ImageIcon(GamingView.class.getResource("/assets/wave/right_end.png"))
			.getImage();
	private Image upFluid = new ImageIcon(GamingView.class.getResource("/assets/wave/up_end.png"))
			.getImage();
	private Image downFluid = new ImageIcon(GamingView.class.getResource("/assets/wave/down_end.png"))
			.getImage();
	
	private int x;
	private int y;
	private long startCnt;
			
	public Fluid(int x, int y, long startCnt) {
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
}
