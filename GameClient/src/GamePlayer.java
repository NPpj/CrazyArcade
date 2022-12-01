import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import com.sun.tools.javac.Main;

public class GamePlayer {
	public GamePlayer() {}
	
	public static final int SCREEN_X = 998, SCREEN_Y = 773;
	private static final int PLAYER_MOVE=3;
	
	// 이미지 파일
	private Image playerLeftMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/left.png"))
			.getImage();
	private Image playerRightMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/right.png"))
			.getImage();
	private Image playerUpMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/up.png"))
			.getImage();
	private Image playerDownMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/down.png"))
			.getImage();
	
	// 위치
	private int pos_X, pos_Y;
	
	// 보고있는 방향
	private String direction;
	
	// 표시할 이미지
	private Image state;
	
	public void init(int pos_X, int pos_Y, String direction) {
		switch (direction) {
		case "up":
			setState(getPlayerUpMove());
			break;
		case "down":
			setState(getPlayerDownMove());
			break;
		case "left":
			setState(getPlayerLeftMove());
			break;
		case "right":
			setState(getPlayerRightMove());
			break;
		} // end switch
		
		this.pos_X = pos_X; 
		this.pos_Y = pos_Y;
		this.direction = direction;
	}
	
	public Image getState() {
		return state;
	}
	
	public void setState(Image image) {
		this.state = image;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public int getPos_X() {
		return pos_X;
	}
	
	public void setPos_X(int pos_X) {
		this.pos_X = pos_X;
	}
	
	public int getPos_Y() {
		return pos_Y;
	}
	
	public void setPos_Y(int pos_Y) {
		this.pos_Y = pos_Y;
	}
	
	public Image getPlayerLeftMove() {
		return playerLeftMove;
	}
	
	public Image getPlayerRightMove() {
		return playerRightMove;
	}
	
	public Image getPlayerUpMove() {
		return playerUpMove;
	}
	
	public Image getPlayerDownMove() {
		return playerDownMove;
	}
	
	public void moveToRight() {
		if(pos_X <= 720)
			pos_X += PLAYER_MOVE;
		
		state = playerRightMove;
	}
	public void moveToLeft() {
		if(pos_X >= 18)
			pos_X -= PLAYER_MOVE;
		
		state = playerLeftMove;
	}
	public void moveToUp() {
		if(pos_Y >= 45)
			pos_Y -= PLAYER_MOVE;
		
		state = playerUpMove;
	}
	public void moveToDown() {
		if(pos_Y <= 640)
			pos_Y += PLAYER_MOVE;
		
		state = playerDownMove;
	}
	public int getMapX() {
		int i;
		for(i=0;i<15;i++) {
			if(pos_X <= (18+47*i)) {
				return i;
			}
		}
		return i;
	}
	public int getMapY() {
		int i;
		for(i=0;i<15;i++) {
			if(pos_Y <= (48+46*i)) {
				return i;
			}
		}
		return i;
			
	}
}
