import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public abstract class addModel {
	private Image img;
	private int x;
	private int y;
	private Graphics g;
	private int width;
	private int height;
	
	public addModel(int x, int y, int width, int height,Graphics g) {
		this.x=x;
		this.y=y;
		this.g=g;
		this.width=width;
		this.height=height;
	}

	public Image getImage() {
		return img;
	}
	public void setImage(Image image) {
		this.img = image;
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

	public void drawOneImage() {
		g.drawImage(getImage(), x, y, null);
	}
	
	public void drawAllImage(Image img,int x,int y, int cnt, int n,ImageObserver observer) {
		if(n==4) {
			g.setClip(x , y, 56, 54);
			if (cnt / 10 %n== 0) g.drawImage(img, x - ( width * 0 ), y, observer);
			else if(cnt/10%n == 1) g.drawImage(img, x - ( width * 1 ), y, observer);
			else if(cnt/10%n == 2) g.drawImage(img, x - ( width * 2 ), y, observer);
			else if(cnt/10%n == 3) g.drawImage(img, x - ( width * 3 ), y, observer);
		}else if(n==11) {
			g.setClip(x , y, 52, 52);
			if (cnt / 10 %n== 0) g.drawImage(img, x - ( width * 0 ), y, observer);
			else if(cnt/10%n == 1) g.drawImage(img, x - ( width * 1 ), y, observer);
			else if(cnt/10%n == 2) g.drawImage(img, x - ( width * 2 ), y, observer);
			else if(cnt/10%n == 3) g.drawImage(img, x - ( width * 3 ), y, observer);
			else if (cnt/10%n== 4) g.drawImage(img, x - ( width * 4 ), y, observer);
			else if(cnt/10%n == 5) g.drawImage(img, x - ( width * 5 ), y, observer);
			else if(cnt/10%n == 6) g.drawImage(img, x - ( width * 6 ), y, observer);
			else if(cnt/10%n == 7) g.drawImage(img, x - ( width * 7 ), y, observer);
			else if(cnt/10%n == 8) g.drawImage(img, x - ( width * 8 ), y, observer);
			else if(cnt/10%n == 9) g.drawImage(img, x - ( width * 9 ), y, observer);
			else if(cnt/10%n == 10) g.drawImage(img, x - ( width * 10 ), y, observer);
		}
		else if(n==3){
			g.setClip(x , y, 56, 54);
			if (cnt / 10 % 3 == 0) g.drawImage(img, x - ( 56 * 0 ), y, observer);
			else if(cnt/10% 3 == 1) g.drawImage(img, x - ( 56 * 1 ), y, observer);
			else if(cnt/10% 3 == 2) g.drawImage(img, x - ( 56 * 0 ), y, observer);
		}else if(n==12){
			g.setClip(x,y,8,144);
			setWidth(85);
			if (cnt / 10 %n== 0) g.drawImage(img, x - ( width * 0 ), y, observer);
			else if(cnt/10%n == 1) g.drawImage(img, x - ( width * 1 ), y, observer);
			else if(cnt/10%n == 2) g.drawImage(img, x - ( width * 2 ), y, observer);
			else if(cnt/10%n == 3) g.drawImage(img, x - ( width * 3 ), y, observer);
			else if (cnt/10%n== 4) g.drawImage(img, x - ( width * 4 ), y, observer);
			else if(cnt/10%n == 5) g.drawImage(img, x - ( width * 5 ), y, observer);
			else if(cnt/10%n == 6) g.drawImage(img, x - ( width * 6 ), y, observer);
			else if(cnt/10%n == 7) g.drawImage(img, x - ( width * 7 ), y, observer);
			else if(cnt/10%n == 8) g.drawImage(img, x - ( width * 8 ), y, observer);
			else if(cnt/10%n == 9) g.drawImage(img, x - ( width * 9 ), y, observer);
			else if(cnt/10%n == 10) g.drawImage(img, x - ( width * 10 ), y, observer);
			else if(cnt/10%n == 11) g.drawImage(img, x - ( width * 11 ), y, observer);
		}else if(n==23) {
			g.setClip(x,y,88,82);
			setWidth(88);
			if (cnt / 23 %n== 0) g.drawImage(img, x - ( width * 0 ), y, observer);
			else if(cnt/23%n == 1) g.drawImage(img, x - ( width * 1 ), y, observer);
			else if(cnt/23%n == 2) g.drawImage(img, x - ( width * 2 ), y, observer);
			else if(cnt/23%n == 3) g.drawImage(img, x - ( width * 3 ), y, observer);
			else if(cnt/23%n == 4) g.drawImage(img, x - ( width * 4 ), y, observer);
			else if(cnt/23%n == 5) g.drawImage(img, x - ( width * 5 ), y, observer);
			else if(cnt/23%n == 6) g.drawImage(img, x - ( width * 6 ), y, observer);
			else if(cnt/23%n == 7) g.drawImage(img, x - ( width * 7 ), y, observer);
			else if(cnt/23%n == 8) g.drawImage(img, x - ( width * 8 ), y, observer);
			else if(cnt/23%n == 9) g.drawImage(img, x - ( width * 9 ), y, observer);
			else if(cnt/23%n == 10) g.drawImage(img, x - ( width * 10 ), y, observer);
			else if(cnt/23%n == 11) g.drawImage(img, x - ( width * 11 ), y, observer);
			else if(cnt/23%n == 12) g.drawImage(img, x - ( width * 12 ), y, observer);
			else if(cnt/23%n == 13) g.drawImage(img, x - ( width * 13 ), y, observer);
			else if(cnt/23%n == 14) g.drawImage(img, x - ( width * 14 ), y, observer);
			else if(cnt/23%n == 15) g.drawImage(img, x - ( width * 15 ), y, observer);
			else if(cnt/23%n == 16) g.drawImage(img, x - ( width * 16 ), y, observer);
			else if(cnt/23%n == 17) g.drawImage(img, x - ( width * 17 ), y, observer);
			else if(cnt/23%n == 18) g.drawImage(img, x - ( width * 18 ), y, observer);
			else if(cnt/23%n == 19) g.drawImage(img, x - ( width * 19 ), y, observer);
			else if(cnt/23%n == 20) g.drawImage(img, x - ( width * 20 ), y, observer);
			else if(cnt/23%n == 21) g.drawImage(img, x - ( width * 21 ), y, observer);
			else if(cnt/23%n == 22) g.drawImage(img, x - ( width * 22 ), y, observer);
		}
	}
	
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public abstract void drawImage();
}
