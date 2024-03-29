import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public class Monster implements Runnable{
	// 좌표:320,100
	private Thread th;
	private static int count;
	public static int BOSS_MOVE=50;
	
	// 사용할 이미지
	private Image bossBasicImage = new ImageIcon(JavaGameClientMain.class.getResource("/assets/monster/boss.png"))
		.getImage();
	private Image bossTrapImage = new ImageIcon(JavaGameClientMain.class.getResource("/assets/monster/trap_boss.png"))
		.getImage();
	private Image bossAngryImage = new ImageIcon(JavaGameClientMain.class.getResource("/assets/monster/angry_boss.png"))
		.getImage();
	
	private int x, y;
	
	// 현재 상태
	private Image currentState = bossBasicImage;
	
	// 한계 위치
	private static final int GROUND_START_X=18;
	private static final int GROUND_START_Y=45;
	private static final int GROUND_END_X=720;
	private static final int GROUND_END_Y=800;
	
	// 방향
	private String direction = "stop";
	private int directionInt = 0;
	private int n =0 ;
	
	private int[] directionIntList = {1,1,4,3,2,4,4,1,3,3,4,2,1,1,2,3,4,1,1,2,3,1,2,4,2,4,3};
	// 생성자
	public Monster(int x, int y) {
		this.x = x;
		this.y = y;
		start();
	}
	
	public Image getCurrentState() {
		return currentState;
	}
	public void setCurrentState(Image state) {
		this.currentState = state;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	// 게임 시작 후 0.1초 간격으로 7번 공격함
	public void initBossAttack() {
		setCurrentState(bossAngryImage);
		
		for(int i=0;i<7;i++) {
			System.out.println("보스가 초기 공격");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 게임 도중 1초 간격으로 3번 공격함
	public void bossAttack() {
		setCurrentState(bossAngryImage);
		
		for(int i=0;i<3;i++) {
			System.out.println("보스가 공격");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void changeBasic() {
		setCurrentState(bossBasicImage);
//		directionInt = (int)(Math.random() * 3)+1; // 1:left, 2:right, 3:top, 4:down
		directionInt = directionIntList[n++];
		if(n==directionIntList.length)
			n=0;
		for(int i=0;i<8;i++) {
			switch(directionInt) {
			case 0:
				direction = "stop";
				break;
			case 1:
				direction = "left";
				
				if((x-BOSS_MOVE) <= GROUND_START_X+BOSS_MOVE) {
					direction = "right";
					directionInt=2;
					System.out.println("현재 좌표:"+x+", "+y);
				}else
					x-=BOSS_MOVE;
					
				break;
			case 2:
				direction = "right";
				
				if((x+BOSS_MOVE) >= GROUND_END_X-BOSS_MOVE) {
					direction = "left";
					directionInt=1;
					System.out.println("현재 좌표:"+x+", "+y);
				}else
					x+=BOSS_MOVE;
					
				break;
			case 3:
				direction = "top";
				
				if((y-BOSS_MOVE) <= GROUND_START_Y+BOSS_MOVE) {
					direction = "down";
					directionInt=4;
					System.out.println("현재 좌표:"+x+", "+y);
				}else
					y-=BOSS_MOVE;
				break;
			case 4:
				direction = "down";
				
				if((y+BOSS_MOVE) >= GROUND_END_Y-BOSS_MOVE) {
					direction = "top";
					directionInt=3;
					System.out.println("현재 좌표:"+x+", "+y);
				} else
					y+=BOSS_MOVE;
				break;
			}
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} // for문 끝
	}

	
	public void start() {
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void run() {
		initBossAttack();
		while(true) {
			changeBasic();
			changeBasic();
			bossAttack();
		}
	}
}