import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import java.util.TimerTask;
import java.util.Timer;
import java.awt.Color;

public class GamingView extends JFrame implements Runnable {
	GameUser user = GameUser.getInstance();
	private ListenNetwork net;
	private ObjectInputStream ois;
	public static ObjectOutputStream oos;
	public static int userIndex;

	private Image screenImage;
	public static Graphics screenGraphics;
	private Graphics img_g; // 이중 버퍼림 위함

	private Thread th;
	public static long cnt; // 무한루프를 카운터 하기 위한 변수
	private ImageObserver observer = this;

	public static final int PLAYER_LEFT_RIGHT_MOVING_FRAME = 8;
	public static final int PLAYER_UP_DOWN_MOVING_FRAME = 6;

	public static final int Frame_W = 1019;
	public static final int Frame_H = 929;

	private ImageIcon backgroundIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/game/test.png"));
	private Image backgroundImage = backgroundIcon.getImage(); // 이미지 객체

	private Image background = backgroundImage;

	// 스테이지 생성
	public static Stage stage = new Stage();
	public static GamePlayerBubble bubbleThread = new GamePlayerBubble();

	private KeyListener keyListener;
	public static ArrayList<GamePlayer> playerList = new ArrayList<>(); // 게임 플레이어 리스트
	private int playerNum;
	public static int roomNum;
	public static Monster boss = new Monster(320, 100);
	// 초기 플레이어 x, y 좌표
	private int[] init_X = { 200, 275, 480, 530 };
	private int[] init_Y = { 790, 790, 790, 790 };
	
	// 블록 크기 
	public static final int BLOCK_W = 50;
	public static final int BLOCK_H = 60;
	public static final int GRASS_W = 50;
	public static final int GRASS_H = 50;
	public static final int FLOWER_W = 60;
	public static final int FLOWER_H = 70;

	/*
	 * //방 정보 서버로 넘기기 -> 캐릭터 움직일 때 마다. ChatMsg obcm = new ChatMsg(userName, "101",
	 * RoomInfo); SendObject(obcm);
	 */

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			System.out.println("SendObject Error");
		}
	}

	public GamingView(int roomNum, int userIndex, int playerNum) {
		this.userIndex=userIndex;
		this.playerNum=playerNum;
		this.roomNum=roomNum;
		this.net = user.getNet();
		this.ois = net.getOIS();
		this.oos = net.getOOS();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame이 정상적으로 종료되게
		setBounds(100, 100, Frame_W, Frame_H);
		setTitle("CrazyArcade");
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

		keyListener = new KeyListener();
		// 키 리스너 생성
		addKeyListener(keyListener);

		// 플레이어들 생성
		for(int i=0;i<playerNum;i++) {
			playerList.add(new GamePlayer(i));
			playerList.get(i).init(init_X[i], init_Y[i], "down");
		}
		
		// 맵의 아이템 그려줌
		stage.start();
		// 물풍선
		bubbleThread.start();

		// 스레드 시작
		start();
	}

	public void start() {
		th = new Thread(this);
		th.start();
	}

	@Override
	public void run() { // 스레드 메소드, 무한 루프
		while (true) {
			try {
				keyListener.keyProcess();
				repaint();
				Thread.sleep(20);

				cnt++;

			} catch (Exception e) {
				System.out.println("쓰레드 오류 " + e.getMessage());
			}
		}
	}

	public void paint(Graphics g) {
		// 오프스크린 생성
		screenImage = createImage(Frame_W, Frame_H);
		// 생성한 오프스크린에 대해 Graphics 객체 생성
		screenGraphics = screenImage.getGraphics();

		// 생성한 screenGraphics 객체를 이용하여 오프스크린에 그림
		screenDraw(screenGraphics);

		// 더블 버퍼링 이용해 버퍼에 그려진 것 가져옴
		// 오프스크린에 그린 내용 실제 화면에 그림
		g.drawImage(screenImage, 0, 0, null);
	}

	// 이중 버퍼링
	public void screenDraw(Graphics g) {
		// background를 오프스크린에 그림
		Dimension d = getSize();
		g.drawImage(background, 0, 0, d.width, d.height, null);

		// 타일 그리기
		for (int y = 12; y >= 0; y--) {
			for (int x = 0; x < 15; x++) {
				Tile block = new Tile(x, y, "tile", screenGraphics);
				block.drawImage();

			}
		}
		// 꽃, 박스 그리기
		stage.drawTile(g);
		stage.drawBoss(g);
		stage.drawItems(g);
		bubbleThread.drawBubbles(g);
		
		
		for(int i=0;i<playerNum;i++) {
		// player 상태에 따른 이미지 변경
			if (playerList.get(i).getPlayerState() == "live")
				drawPlayer(i);
			else if (playerList.get(i).getPlayerState() == "trap")
				trapPlayer(i);
			else if (playerList.get(i).getPlayerState() == "die") {
				playerList.get(i).dieCnt++;
				if(playerList.get(i).dieCnt>100)
					playerList.get(i).setPlayerState("dispose");
				else
					diePlayer(i);
		}
		}
		
		this.repaint();
	}
	
	/*
	public void drawTile() {
		Image temp_img = null;
		screenGraphics.setClip(null);
		// 초기 맵 깔기
		for (int y = 12; y >= 0; y--) {
			for (int x = 0; x < 15; x++) {
				switch (playerList.get(userIndex).map[y][x]) {
				case 0:
					break;
				case 1:
					Tile block = new Tile(x, y, "block1", screenGraphics);
					block.drawImage();
					break;
				case 2:
					Tile flower1 = new Tile(x, y, "flower1", screenGraphics);
					flower1.drawImage();
					break;
				case 3:
					Tile flower2 = new Tile(x, y, "flower2", screenGraphics);
					flower2.drawImage();
					break;
				}
			}
			;
		}
	}
	*/

	
	
	public void drawPlayer(int i) {
		screenGraphics.setFont(new Font("Default", Font.BOLD, 20));
		screenGraphics.drawString(Long.toString(cnt), 50, 50);
		// 위는 단순히 무한루프 적용여부와 케릭터 방향 체크를 위해
		// 눈으로 보면서 테스트할 용도로 쓰이는 텍스트 표출입니다.
		movePlayer(playerList.get(i).getState(), playerList.get(i).getPos_X(), playerList.get(i).getPos_Y(), 64, 100);
		
	}

	public void movePlayer(Image img, int x, int y, int width, int height) {
		// 플레이어 이미지, 플레이어 위치, 플레이어 크기를 받습니다.
		// 받은 값을 이용해서 위의 이미지칩셋에서 플레이어를 잘라내 표출하도록 계산하는 메소드 입니다.
		screenGraphics.setClip(x, y, width, height);
		// 현재 좌표에서 케릭터의 크기 만큼 이미지를 잘라 그립니다.
		String direction = playerList.get(userIndex).getDirection();

		if (GamingView.playerList.get(userIndex).playerMove) {
			System.out.println(userIndex+" : "+GamingView.playerList.get(userIndex).playerMove);
			if (direction.equals("up") || direction.equals("down")) { // 케릭터의 움직임 여부를 판단합니다.
				// 케릭터의 방향에 따라 걸어가는 모션을 취하는
				// 케릭터 이미지를 시간차를 이용해 순차적으로 그립니다.
				if (cnt / 10 % 6 == 0)
					screenGraphics.drawImage(img, x - (width * 0), y, this);
				else if (cnt / 10 % 6 == 1)
					screenGraphics.drawImage(img, x - (width * 1), y, this);
				else if (cnt / 10 % 6 == 2)
					screenGraphics.drawImage(img, x - (width * 2), y, this);
				else if (cnt / 10 % 6 == 3)
					screenGraphics.drawImage(img, x - (width * 3), y, this);
				else if (cnt / 10 % 6 == 4)
					screenGraphics.drawImage(img, x - (width * 4), y, this);
				else if (cnt / 10 % 6 == 5)
					screenGraphics.drawImage(img, x - (width * 5), y, this);
			} else if (direction.equals("left") || direction.equals("right")) {
				if (cnt / 10 % 8 == 0)
					screenGraphics.drawImage(img, x - (width * 0), y, this);
				else if (cnt / 10 % 8 == 1)
					screenGraphics.drawImage(img, x - (width * 1), y, this);
				else if (cnt / 10 % 8 == 2)
					screenGraphics.drawImage(img, x - (width * 2), y, this);
				else if (cnt / 10 % 8 == 3)
					screenGraphics.drawImage(img, x - (width * 3), y, this);
				else if (cnt / 10 % 8 == 4)
					screenGraphics.drawImage(img, x - (width * 4), y, this);
				else if (cnt / 10 % 8 == 5)
					screenGraphics.drawImage(img, x - (width * 5), y, this);
				else if (cnt / 10 % 8 == 6)
					screenGraphics.drawImage(img, x - (width * 6), y, this);
				else if (cnt / 10 % 8 == 7)
					screenGraphics.drawImage(img, x - (width * 7), y, this);
			}
		} else
			screenGraphics.drawImage(img, x - (width * 0), y, this); // 케릭터가 움직이지 않으면 정지한 케릭터를 그립니다.
	}

	/*
	public void viewWave() {
		for(int i =0;i<waveList.size(); i++) {
//			int x = waveList.get(i).getX();
//			int y = waveList.get(i).getY();
			waveList.get(i).drawImage();

		}
	}
	
	public void addBubble() {
		for (int i = 0; i < Bubble_XY.size(); i++) {
			String str = Bubble_XY.get(i);
			String[] xy = Bubble_XY.get(i).split(",");

			int x = Integer.parseInt(xy[0]);
			int y = Integer.parseInt(xy[1]);

			// 물풍선 객체 추가
			Bubble bubble = new Bubble(x, y, screenGraphics, cnt, observer);
			bubbleList.add(bubble);
			bubble.drawImage();

			Timer timer=new Timer();
			TimerTask task=new TimerTask(){
			    @Override
			    public void run() {
			    	Bubble_XY.remove(str);
			    	breakBlock(x,y);
			    	playerList.get(userIndex).setBubbleNum(Bubble_XY.size());
			    	Wave wave = new Wave(x, y, screenGraphics, cnt, observer);
					waveList.add(wave);
			    }	
			};
			timer.schedule(task, 2000); //실행 Task, 1초뒤 실행

		}
	}
	*/

	// 물줄기 그리기
//	public void drawLine() {
//		for (int i = 0; i < Bubble_XY.size(); i++) {
//			String str = Bubble_XY.get(i);
//			String[] xy = Bubble_XY.get(i).split(",");
//			int x = Tile.START_W + Tile.BLOCK_W * Integer.parseInt(xy[0]);
//			int y = Tile.START_H + Tile.BLOCK_H * Integer.parseInt(xy[1]);
//			Wave wave = new Wave(x, y, screenGraphics, cnt, observer);
//			wave.drawImage();
//			Bubble_XY.remove(str);
////			bubblePop=false;
//		}
//	}

	// 블록 깨기
	/*
	public void breakBlock(int x, int y) {
		int len = playerList.get(userIndex).waveLen;
		for (int i = 1; i <= len; i++) {
			if (x >= i)
				playerList.get(userIndex).map[y][x - i] = 0;
			if (x + i < 15)
				playerList.get(userIndex).map[y][x + i] = 0;
			if (y >= i)
				playerList.get(userIndex).map[y - i][x] = 0;
			if (y + i < 13)
				playerList.get(userIndex).map[y + i][x] = 0;
			playerList.get(userIndex).map[y][x] = 0;
		}
	}
	*/

	/*
	// 아이템 그리기
	public void drawItems() {
		for (int i = 0; i < Item_XY.size(); i++) {
			String[] xy = Item_XY.get(i).split(",");
			int item_x = Tile.START_W + Tile.BLOCK_W * Integer.parseInt(xy[0]);
			int item_y = Tile.START_H + Tile.BLOCK_H * Integer.parseInt(xy[1]) - 20;
			int type = Integer.parseInt(xy[2]);
			if (playerList.get(userIndex).map[Integer.parseInt(xy[1])][Integer.parseInt(xy[0])] == 0) {
				if (type % 3 == 0) {
					Item item = new Item("물풍선", item_x, item_y, screenGraphics, cnt, observer);
					item.drawImage();
				} else if (type % 3 == 1) {
					Item item2 = new Item("물줄기", item_x, item_y, screenGraphics, cnt, observer);
					item2.drawImage();
				} else {
					Item item3 = new Item("달리기", item_x, item_y, screenGraphics, cnt, observer);
					item3.drawImage();
				}
			}
		}
	}

	// 아이템 먹기
	public void eatItem() {
		for (int i = 0; i < Item_XY.size(); i++) {
			String[] xy = Item_XY.get(i).split(",");
			int type = Integer.parseInt(xy[2]);
			if (playerList.get(userIndex).getMapX(playerList.get(userIndex).getPos_X()) == Integer.parseInt(xy[0])
					&& playerList.get(userIndex).getMapY(playerList.get(userIndex).getPos_Y()) == Integer.parseInt(xy[1])) {
				if (type % 3 == 0) { // 물풍선 먹기
//					playerList.get(userIndex).addMaxBubbleNum();
//					Item_XY.remove(i);
					GameInfo obcm = new GameInfo("402", GamingView.roomNum, userIndex, "1,"+i);
					SendObject(obcm);
				} else if (type % 3 == 1) { // 물줄기 먹기
//					Item_XY.remove(i);
					GameInfo obcm = new GameInfo("402", GamingView.roomNum, userIndex, "2,"+i);
					SendObject(obcm);
//					player.waveLen += 1;	
				} else { // 달리기 먹기
					GameInfo obcm = new GameInfo("402", GamingView.roomNum, userIndex, "3,"+i);
					SendObject(obcm);
//					if (playerList.get(userIndex).PLAYER_MOVE < 8)
//						playerList.get(userIndex).PLAYER_MOVE += 2;
//					Item_XY.remove(i);
				}
			}

		}
	}
	*/

	// 죽기
	public void diePlayer(int i) {
		DiePlayer die = new DiePlayer(playerList.get(i).getPos_X() - 10, playerList.get(i).getPos_Y() - 40, screenGraphics, cnt, observer);
		die.drawImage();

	}

	// 갇히기
	public void trapPlayer(int i) {
		TrapPlayer trap = new TrapPlayer(playerList.get(i).getPos_X() - 10, playerList.get(i).getPos_Y(), screenGraphics, cnt, observer);
		trap.drawImage();

	}

}
