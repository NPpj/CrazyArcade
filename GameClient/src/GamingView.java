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

public class GamingView extends JFrame implements Runnable{
	private ListenNetwork net;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	//private String userName;
	GameUser user = GameUser.getInstance();
	
	private Image screenImage;
	public static Graphics screenGraphics;
	private Graphics img_g; // 이중 버퍼림 위함
	//private Container c;
	
	private Thread th;
	public static int cnt; // 무한루프를 카운터 하기 위한 변수
	private ImageObserver observer = this;
	
	public static final int PLAYER_LEFT_RIGHT_MOVING_FRAME = 8;
	public static final int PLAYER_UP_DOWN_MOVING_FRAME = 6;
	
	public static final int Frame_W =1019;
	public static final int Frame_H =929;
	
	private ImageIcon backgroundIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/game/test.png"));
	private Image backgroundImage  = backgroundIcon.getImage(); //이미지 객체
	
	private Image background = backgroundImage;
	
	private KeyListener keyListener;
	public static GamePlayer player = new GamePlayer();
	
	// 초기 플레이어 x, y 좌표
	private int[] init_X = {225, 275, 480, 530};
	private int[] init_Y = {790, 790, 790, 790};
	
	// 블록 크기 
	public static final int BLOCK_W = 50;
	public static final int BLOCK_H = 60;
	public static final int GRASS_W = 50;
	public static final int GRASS_H = 50;
	public static final int FLOWER_W = 60;
	public static final int FLOWER_H = 70;
	
	/* 
	 * //방 정보 서버로 넘기기 -> 캐릭터 움직일 때 마다.
	      ChatMsg obcm = new ChatMsg(userName, "101", RoomInfo);
	      SendObject(obcm);
	 * */

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			System.out.println("SendObject Error");
		}
	}	
	
	private String bubble_xy;
	
	public static ArrayList<String> Item_XY = new ArrayList();
	public void setItemPos() {
		Item_XY.add("2,2,1");
		Item_XY.add("1,3,2");
		Item_XY.add("8,3,3");
		Item_XY.add("11,1,1");
		Item_XY.add("12,3,2");
		Item_XY.add("1,6,3");
		Item_XY.add("3,7,1");
		Item_XY.add("7,6,2");
		Item_XY.add("11,5,3");
		Item_XY.add("13,6,1");
		Item_XY.add("2,10,2");
		Item_XY.add("1,11,3");
		Item_XY.add("8,9,1");
		Item_XY.add("6,11,2");
		Item_XY.add("13,11,3");
	}
	
	// space바 누른 키 저장
	public static ArrayList<String> Bubble_XY = new ArrayList();
	
	// 생성자
	public GamingView(int roomNum, int userIndex) {
		this.net = user.getNet();
		this.ois = net.getOIS();
		this.oos= net.getOOS();
		
		getContentPane().setBackground(new Color(255, 255, 0));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame이 정상적으로 종료되게
		setBounds(100, 100,Frame_W,Frame_H);
		setTitle("CrazyArcade");
		setResizable(false);
		setLocationRelativeTo(null);
		//setSize(998,773);
		setVisible(true);
		
		keyListener = new KeyListener();
		// 키 리스너 생성
		addKeyListener(keyListener);
		
		// 플레이어 초기 설정
		player.init(init_X[userIndex], init_Y[userIndex], "down");
		setItemPos();
		
		start();
	}
	
	
	public void start() {
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void run() { // 스레드 메소드, 무한 루프
		while(true) {
			try {
//				System.out.println("쓰레드 실행중");
				keyListener.keyProcess();
				if(keyListener.playerMove) {
					String PlayerMovingData = player.getDirection();
					ChatMsg obcm = new ChatMsg(user.getId(), "300", PlayerMovingData);
					SendObject(obcm);
				}
				repaint();
				Thread.sleep(20);
				
				cnt++;
				
			}catch(Exception e) {
				System.out.println("쓰레드 오류");
			}
		}
	}
	
	public void paint(Graphics g) {
		// 오프스크린 생성
		screenImage = createImage(Frame_W,Frame_H);
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
		
		
		//타일 그리기
		for(int y=12; y>=0; y--) {
			for(int x=0; x<15;x++) {
				Tile block = new Tile(x,y,"tile",screenGraphics);
				block.drawImage();
				
				
			}
		}
		
		drawTile();
		
		drawItems();
		eatItem();
		
		drawBuble(); 
//		drawLine();
		
		drawPlayer();
		
		this.repaint();
	}
	
	public void drawTile() {
		Image temp_img = null;
		screenGraphics.setClip(null);
		// 초기 맵 깔기 
		for(int y=12; y>=0; y--) {
			for(int x=0; x<15;x++) {
				switch(player.map[y][x]) {
					case 0:
						break;
					case 1:
						Tile block = new Tile(x,y,"block1",screenGraphics);
						block.drawImage();
						break;
					case 2:
						Tile flower1 = new Tile(x,y,"flower1",screenGraphics);
						flower1.drawImage();
						break;
					case 3:
						Tile flower2 = new Tile(x,y,"flower2",screenGraphics);
						flower2.drawImage();
						break;
				}
			};
		}
	}
	
	public void drawMap() {
		drawTile();
	}
	
	public void drawPlayer() {
		screenGraphics.setFont(new Font("Default", Font.BOLD, 20));
		screenGraphics.drawString(Integer.toString(cnt), 50, 50);
		//위는 단순히 무한루프 적용여부와 케릭터 방향 체크를 위해
		//눈으로 보면서 테스트할 용도로 쓰이는 텍스트 표출입니다.
		movePlayer(player.getState(), player.getPos_X(), player.getPos_Y(), 64, 100);
	}
	
	public void movePlayer(Image img, int x, int y, int width, int height){
		// 플레이어 이미지, 플레이어 위치, 플레이어 크기를 받습니다.
		// 받은 값을 이용해서 위의 이미지칩셋에서 플레이어를 잘라내 표출하도록 계산하는 메소드 입니다.
		screenGraphics.setClip(x  , y, width, height);
		//현재 좌표에서 케릭터의 크기 만큼 이미지를 잘라 그립니다.
		String direction = player.getDirection();
		
		if(keyListener.playerMove) {
//			System.out.println("x:"+player.getMapX(player.getPos_X())+" y:"+player.getMapY(player.getPos_Y())+"("+player.getPos_X()+","+player.getPos_Y()+")");
			if(direction.equals("up") || direction.equals("down")){ // 케릭터의 움직임 여부를 판단합니다.
				//케릭터의 방향에 따라 걸어가는 모션을 취하는 		
				//케릭터 이미지를 시간차를 이용해 순차적으로 그립니다.
				if (cnt / 10 % 6 == 0) screenGraphics.drawImage(img, x - ( width * 0 ), y, this);
				else if(cnt/10%6 == 1) screenGraphics.drawImage(img, x - ( width * 1 ), y, this);
				else if(cnt/10%6 == 2) screenGraphics.drawImage(img, x - ( width * 2 ), y, this);
				else if(cnt/10%6 == 3) screenGraphics.drawImage(img, x - ( width * 3 ), y, this);
				else if(cnt/10%6 == 4) screenGraphics.drawImage(img, x - ( width * 4 ), y, this);
				else if(cnt/10%6 == 5) screenGraphics.drawImage(img, x - ( width * 5 ), y, this);
			}else if(direction.equals("left") || direction.equals("right")) {
				if (cnt / 10 % 8 == 0) screenGraphics.drawImage(img, x - ( width * 0 ), y, this);
				else if(cnt/10%8 == 1) screenGraphics.drawImage(img, x - ( width * 1 ), y, this);
				else if(cnt/10%8 == 2) screenGraphics.drawImage(img, x - ( width * 2 ), y, this);
				else if(cnt/10%8 == 3) screenGraphics.drawImage(img, x - ( width * 3 ), y, this);
				else if(cnt/10%8 == 4) screenGraphics.drawImage(img, x - ( width * 4 ), y, this);
				else if(cnt/10%8 == 5) screenGraphics.drawImage(img, x - ( width * 5 ), y, this);
				else if(cnt/10%8 == 6) screenGraphics.drawImage(img, x - ( width * 6 ), y, this);
				else if(cnt/10%8 == 7) screenGraphics.drawImage(img, x - ( width * 7 ), y, this);
			}
		}else screenGraphics.drawImage(img, x - ( width * 0 ), y, this); //케릭터가 움직이지 않으면 정지한 케릭터를 그립니다.
	}
	
	// 물풍선 그리기 
	public void drawBuble() {
		for(int i=0;i<Bubble_XY.size();i++) {
			String str = Bubble_XY.get(i);
			String[] xy = Bubble_XY.get(i).split(",");
			int x= Tile.START_W + Tile.BLOCK_W*Integer.parseInt(xy[0]);
			int y= Tile.START_H + Tile.BLOCK_H*Integer.parseInt(xy[1]);

			Bubble bubble = new Bubble(x,y,screenGraphics,cnt,observer);
			bubble.drawImage();
			
			Timer m_timer = new Timer();
			TimerTask m_task = new TimerTask() {
				public void run() {
					Bubble_XY.remove(str);
					breakBlock(Integer.parseInt(xy[0]),Integer.parseInt(xy[1]));
					player.downBubbleNum();
				}
			};
			m_timer.schedule(m_task, 2000);
		}
	}
	
	// 물줄기 그리기 
	public void drawLine() {
		for(int i=0;i<Bubble_XY.size();i++) {
			String str = Bubble_XY.get(i);
			String[] xy = Bubble_XY.get(i).split(",");
			Wave wave = new Wave(Integer.parseInt(xy[0]),Integer.parseInt(xy[1]),screenGraphics,cnt,observer);
			wave.drawImage();
		}
	}
	
	// 블록 깨기
	public void breakBlock(int x, int y) {
		int len = player.waveLen;
		for(int i=1;i<=len;i++) {
			if(x>=i)
				player.map[y][x-i]=0;
			if(x+i<15)
				player.map[y][x+i]=0;
			if(y>=i)
				player.map[y-i][x]=0;
			if(y+i<13)
				player.map[y+i][x]=0;
		}
	}
		
	// 아이템 그리기 
	public void drawItems() {
		for(int i=0;i<Item_XY.size();i++) {
			String[] xy = Item_XY.get(i).split(",");
			int item_x = Tile.START_W + Tile.BLOCK_W*Integer.parseInt(xy[0]);
			int item_y = Tile.START_H + Tile.BLOCK_H*Integer.parseInt(xy[1])-20;
			int type = Integer.parseInt(xy[2]);
			if(player.map[Integer.parseInt(xy[1])][Integer.parseInt(xy[0])] == 0) {
				if(type%3==0) {
					Item item = new Item("물풍선", item_x, item_y, screenGraphics, cnt, observer);
					item.drawImage();
				}else if(type%3==1) {
					Item item2 = new Item("물줄기", item_x, item_y, screenGraphics, cnt, observer);
					item2.drawImage();
				}else {
					Item item3 = new Item("달리기", item_x, item_y, screenGraphics, cnt, observer);
					item3.drawImage();
				}
			}
		}
	}
	
	// 아이템 먹기 
	public void eatItem() {
		for(int i=0;i<Item_XY.size();i++) {
			String[] xy = Item_XY.get(i).split(",");
			int type = Integer.parseInt(xy[2]);
			if(player.getMapX(player.getPos_X()) ==  Integer.parseInt(xy[0]) && player.getMapY(player.getPos_Y()) ==  Integer.parseInt(xy[1])) {
				if(type%3==0) {  // 물풍선 먹기 
					player.addMaxBubbleNum();
					Item_XY.remove(i);
				}
				else if(type%3==1) { // 물줄기 먹기 
					Item_XY.remove(i);
//					player.waveLen += 1;	
				}
				else{ // 달리기 먹기 
					if(player.PLAYER_MOVE<8)
						player.PLAYER_MOVE +=2;
					Item_XY.remove(i);
				}
			}
			
		}
	}
	
	//죽기 
	public void diePlayer() {
		DiePlayer die = new DiePlayer(100, 100, screenGraphics, cnt, observer);
		die.drawImage();
		
	}
	
	// 갇히기 
	public void trapPlayer() {
		TrapPlayer trap = new TrapPlayer(100, 100, screenGraphics, cnt, observer);
		trap.drawImage();
		
	}
	
//	public static void main(String[] ar){
//		GamingView gv = new GamingView();
//	}
	
}
