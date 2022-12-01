import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;
import java.awt.Color;

public class GamingView extends JFrame implements Runnable{
	private Image screenImage;
	private Graphics screenGraphics;
	private Graphics img_g; // 이중 버퍼림 위함
	//private Container c;
	
	private Thread th;
	private int cnt; // 무한루프를 카운터 하기 위한 변수
	
	public static final int PLAYER_LEFT_RIGHT_MOVING_FRAME = 8;
	public static final int PLAYER_UP_DOWN_MOVING_FRAME = 6;
	
	private ImageIcon backgroundIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/game/GameBackGround.png"));
	private Image backgroundImage  = backgroundIcon.getImage(); //이미지 객체
	
	private Image background = backgroundImage;
	
	private KeyListener keyListener;
	public static GamePlayer player = new GamePlayer();
	//블록 크기 
	public static final int BLOCK_W = 50;
	public static final int BLOCK_H = 60;
	public static final int GRASS_W = 50;
	public static final int GRASS_H = 50;
	public static final int FLOWER_W = 60;
	public static final int FLOWER_H = 70;
	
	private int [][] map = {
			{1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
			{0,2,3,2,0,0,2,3,2,0,0,2,3,2,0},
			{0,3,2,3,0,0,3,2,3,0,0,3,2,3,0},
			{0,2,3,2,0,0,2,3,2,0,0,2,3,2,0},
			{1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
			{0,2,3,2,0,0,2,3,2,0,0,2,3,2,0},
			{0,3,2,3,0,0,3,2,3,0,0,3,2,3,0},
			{0,2,3,2,0,0,2,3,2,0,0,2,3,2,0},
			{1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
			{0,2,3,2,0,0,2,3,2,0,0,2,3,2,0},
			{0,3,2,3,0,0,3,2,3,0,0,3,2,3,0},
			{0,2,3,2,0,0,2,3,2,0,0,2,3,2,0},
			{1,0,0,0,1,1,0,0,0,1,1,0,0,0,1}
	};

	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓

	private String userName;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	// 생성자
	public GamingView(String userName, ObjectInputStream ois, ObjectOutputStream oos) {
		getContentPane().setBackground(new Color(255, 255, 0));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame이 정상적으로 종료되게
//		setBounds(100, 100, 1013, 793);
		setBounds(100, 100, 998, 783);
		setTitle("CrazyArcade");
		setResizable(false);
		setLocationRelativeTo(null);
		//setSize(998,773);
		setVisible(true);
		
		keyListener = new KeyListener();
		// 키 리스너 생성
		addKeyListener(keyListener);
		
		// 플레이어 초기 설정
		player.init(250,300,"down");
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
				System.out.println("쓰레드 실행중");
				keyListener.keyProcess();
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
		screenImage = createImage(998,773);
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
		drawPlayer();
		drawTile();
		
		this.repaint();
	}
	
	public void drawTile() {
		Image temp_img = null;
		screenGraphics.setClip(null);
		// 초기 맵 깔기 
		for(int y=12; y>=0; y--) {
			for(int x=0; x<15;x++) {
				switch(map[y][x]) {
					case 0:
						break;
					case 1:
						Tile block = new Tile(x,y,"block1",screenGraphics);
						break;
					case 2:
						Tile flower1 = new Tile(x,y,"flower1",screenGraphics);
						break;
					case 3:
						Tile flower2 = new Tile(x,y,"flower2",screenGraphics);
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
	
//	public static void main(String[] ar){
//		GamingView gv = new GamingView();
//	}
}
