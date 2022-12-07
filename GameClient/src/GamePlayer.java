import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.sun.tools.javac.Main;

public class GamePlayer {
	public GamePlayer(int userIndex) {
		this.userIndex = userIndex;
	}

	public static final int SCREEN_X = 998, SCREEN_Y = 773;
	public static int PLAYER_MOVE = 3;

	private static final int GROUND_START_X = 18;
	private static final int GROUND_START_Y = 45;
	private static final int GROUND_END_X = 720;
	private static final int GROUND_END_Y = 800;

	private int maxBubbleNum = 3;
	private int bubbleNum = 0;
	public int waveLen = 1;
	private int userIndex;
	public static boolean playerMove = false;

	private String playerState = "live";

	public String getPlayerState() {
		return playerState;
	}

	public void setPlayerState(String playerState) {
		this.playerState = playerState;
	}

	// 물풍선 배열 좌표
	public String[] bubbles = new String[maxBubbleNum];

	/*
	 * public int [][] map = { {1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
	 * {0,2,3,2,0,0,2,3,2,0,0,2,3,2,0}, {0,3,2,3,0,0,3,2,3,0,0,3,2,3,0},
	 * {0,2,3,2,0,0,2,3,2,0,0,2,3,2,0}, {1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
	 * {0,2,3,2,0,0,2,3,2,0,0,2,3,2,0}, {0,3,2,3,0,0,3,2,3,0,0,3,2,3,0},
	 * {0,2,3,2,0,0,2,3,2,0,0,2,3,2,0}, {1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
	 * {0,2,3,2,0,0,2,3,2,0,0,2,3,2,0}, {0,3,2,3,0,0,3,2,3,0,0,3,2,3,0},
	 * {0,2,3,2,0,0,2,3,2,0,0,2,3,2,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0} };
	 */

	// 이미지 파일
	private Image playerLeftMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/left.png"))
			.getImage();
	private Image playerRightMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/right.png"))
			.getImage();
	private Image playerUpMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/up.png")).getImage();
	private Image playerDownMove = new ImageIcon(GamePlayer.class.getResource("/assets/player/bazzi/down.png"))
			.getImage();
	public Image pushBubble = new ImageIcon(GamePlayer.class.getResource("/assets/1.png")).getImage();

	// 위치
	private int pos_X, pos_Y;

	// 보고있는 방향
	private String direction;

	// 표시할 이미지
	private Image state;

	public static int dieCnt = 0;

	// 물풍선
	// private ArrayList<Bubble> myBubbleList = new ArrayList();

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
		switch (direction) {
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

	/*
	 * public void addMyBubble() { System.out.println("내가 공격"); if(getBubbleNum() <
	 * getMaxBubbleNum()) { // x,y 는 map 좌표 int x =
	 * getMapX(GamingView.player.getPos_X()-20); int y =
	 * getMapY(GamingView.player.getPos_Y()-10); myBubbleList.add(new Bubble(x,y));
	 * addBubbleNum(); System.out.print("물풍선 리스트: "); for(int i
	 * =0;i<myBubbleList.size();i++) {
	 * System.out.println(myBubbleList.get(i).getX()+","+myBubbleList.get(i).getX()
	 * +" // "); } System.out.println("물풍선 개수: "+getBubbleNum()); } } public void
	 * removeMyBubble() { if(myBubbleList.size()>0) { myBubbleList.remove(0);
	 * downBubbleNum(); System.out.println("물풍선 사라짐 남은 물풍선 개수: "+getBubbleNum()); }
	 * }
	 * 
	 * public ArrayList<Bubble> getMyBubbleList() { return myBubbleList; }
	 * 
	 * public void setMyBubbleList(ArrayList<Bubble> myBubbleList) {
	 * this.myBubbleList = myBubbleList; }
	 */

	public void moveToRight() {
		isDie();
		if (pos_X <= GROUND_END_X && Stage.map[getMapY(pos_Y)][getMapX(pos_X + 1)] == 0)
			pos_X += PLAYER_MOVE;

		state = playerRightMove;
	}

	public void moveToLeft() {
		isDie();
		if (pos_X >= GROUND_START_X && Stage.map[getMapY(pos_Y)][getMapX(pos_X - 50)] == 0)
			pos_X -= PLAYER_MOVE;

		state = playerLeftMove;
	}

	public void moveToUp() {
		isDie();
		if (pos_Y >= GROUND_START_Y && Stage.map[getMapY(pos_Y - 3)][getMapX(pos_X - 1)] == 0)
			pos_Y -= PLAYER_MOVE;

		state = playerUpMove;
	}

	public void moveToDown() {
		isDie();
		if (pos_Y <= GROUND_END_Y && Stage.map[getMapY(pos_Y + 10)][getMapX(pos_X - 1)] == 0)
			pos_Y += PLAYER_MOVE;

		state = playerDownMove;
	}

	public int getMapX(int x) {
		int i;
		for (i = 0; i < 15; i++) {
			if (x <= (Tile.START_W + Tile.BLOCK_W * i)) {
				return i;
			}
		}
		return i;
	}

	public int getMapY(int y) {
		int i;
		for (i = 0; i < 13; i++) {
			if (y <= (Tile.START_H + Tile.BLOCK_H * i)) {
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
		this.bubbleNum = n;
	}

	public int getBubbleNum() {
		return this.bubbleNum;
	}

	public void addMaxBubbleNum() {
		this.maxBubbleNum += 1;
	}

	public int getMaxBubbleNum() {
		return this.maxBubbleNum;
	}

	public void isDie() {
		int bos_X = getMapX(GamingView.boss.getX());
		int bos_Y = getMapX(GamingView.boss.getY());
		if (getMapX(pos_X) == bos_X && getMapX(pos_Y) == bos_Y) {
			GameInfo obcm = new GameInfo("404", GamingView.roomNum, userIndex, "die");
			SendObject(obcm);
		} else if (getMapX(pos_X) == bos_X + 2 && getMapX(pos_Y) == bos_Y + 1) {
			GameInfo obcm = new GameInfo("404", GamingView.roomNum, userIndex, "die");
			SendObject(obcm);
		} else if (getMapX(pos_X) == bos_X + 2 && getMapX(pos_Y) == bos_Y + 2) {
			GameInfo obcm = new GameInfo("404", GamingView.roomNum, userIndex, "die");
			SendObject(obcm);
		} else if (getMapX(pos_X) == bos_X + 2 && getMapX(pos_Y) == bos_Y + 3) {
			GameInfo obcm = new GameInfo("404", GamingView.roomNum, userIndex, "die");
			SendObject(obcm);
		} else if (getMapX(pos_X) == bos_X + 1 && getMapX(pos_Y) == bos_Y + 1) {
			GameInfo obcm = new GameInfo("404", GamingView.roomNum, userIndex, "die");
			SendObject(obcm);
		} else if (getMapX(pos_X) == bos_X + 1 && getMapX(pos_Y) == bos_Y + 2) {
			GameInfo obcm = new GameInfo("404", GamingView.roomNum, userIndex, "die");
			SendObject(obcm);
		} else if (getMapX(pos_X) == bos_X + 1 && getMapX(pos_Y) == bos_Y + 3) {
			GameInfo obcm = new GameInfo("404", GamingView.roomNum, userIndex, "die");
			SendObject(obcm);
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			GamingView.oos.writeObject(ob);
		} catch (IOException e) {
			System.out.println("SendObject Error");
		}
	}

}
