import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.sun.tools.javac.Main;

public class GamePlayer {
	public GamePlayer(int userIndex) {
		this.userIndex=userIndex;
	}
	
	public static final int SCREEN_X = 998, SCREEN_Y = 773;
	public static int PLAYER_MOVE=3;
	
	private static final int GROUND_START_X=18;
	private static final int GROUND_START_Y=45;
	private static final int GROUND_END_X=720;
	private static final int GROUND_END_Y=800;
	
	private int maxBubbleNum = 3;
	private int bubbleNum = 0;
	public int waveLen = 1;
	private int userIndex;
	public static boolean playerMove=false;
	
	private String playerState = "live";
	
	public String getPlayerState() {
		return playerState;
	}

	public void setPlayerState(String playerState) {
		this.playerState = playerState;
	}
	
	// 이미지 파일
	private Image playerLeftMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/left.png"))
			.getImage();
	private Image playerRightMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/right.png"))
			.getImage();
	private Image playerUpMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/up.png"))
			.getImage();
	private Image playerDownMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/down.png"))
			.getImage();
	public Image pushBubble = new ImageIcon(GamePlayer.class.getResource("/assets/1.png"))
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
		switch(direction) {
			case "up":
				GamingView.playerList.get(userIndex).moveToUp();
				GamingView.playerList.get(userIndex).playerMove = true;
				break;
			case "down":
				GamingView.playerList.get(userIndex).moveToDown();
				GamingView.playerList.get(userIndex).playerMove = true;
				break;
			case "left":
				GamingView.playerList.get(userIndex).moveToLeft();
				GamingView.playerList.get(userIndex).playerMove = true;
				break;
			case "right":
				GamingView.playerList.get(userIndex).moveToRight();
				GamingView.playerList.get(userIndex).playerMove = true;
				break;
		}
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
		if(pos_X <= GROUND_END_X-PLAYER_MOVE && Stage.map[getMapY(pos_Y)][getMapX(pos_X+1)] == 0)
			pos_X += PLAYER_MOVE;
		
		state = playerRightMove;
	}
	public void moveToLeft() {
		if(pos_X >= GROUND_START_X+PLAYER_MOVE && Stage.map[getMapY(pos_Y)][getMapX(pos_X-50)] == 0)
			pos_X -= PLAYER_MOVE;
		
		state = playerLeftMove;
	}
	public void moveToUp() {
		if(pos_Y >= GROUND_START_Y+PLAYER_MOVE && Stage.map[getMapY(pos_Y-3)][getMapX(pos_X-1)] == 0)
			pos_Y -= PLAYER_MOVE;
		
		state = playerUpMove;
	}
	public void moveToDown() {
		if(pos_Y <= GROUND_END_Y-PLAYER_MOVE && Stage.map[getMapY(pos_Y+10)][getMapX(pos_X-1)] == 0)
			pos_Y += PLAYER_MOVE;
		
		state = playerDownMove;
	}
	
	public int getMapX(int x) {
		int i;
		for(i=0;i<15;i++) {
			if(x <= (Tile.START_W+Tile.BLOCK_W*i)) {
				return i;
			}
		}
		return i;
	}
	public int getMapY(int y) {
		int i;
		for(i=0;i<13;i++) {
			if(y <= (Tile.START_H+Tile.BLOCK_H*i)) {
				return i;
			}
		}
		return i;	
	}
	
	public void addBubbleNum() {
		this.bubbleNum += 1;
	}
	
	public void downBubbleNum() {
		this.bubbleNum -= 1;
	}
	
	public void setBubbleNum(int n) {
		this.bubbleNum =n;
	}
	
	public int getBubbleNum() {
		return this.bubbleNum;
	}
	
	public void addMaxBubbleNum() {
		this.maxBubbleNum+= 1;
	}
	
	public int getMaxBubbleNum() {
		return this.maxBubbleNum;
	}
	
	
}
