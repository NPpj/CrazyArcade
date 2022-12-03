
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

public class KeyListener extends KeyAdapter {
	public static boolean playerMove = false;
	private static boolean pressedKeyUp = false;
	private static boolean pressedKeyDown = false;
	private static boolean pressedKeyLeft = false;
	private static boolean pressedKeyRight = false;
	public static int pressedKeySpace_X = 0; 
	public static int pressedKeySpace_Y = 0; 
	public static boolean pushSpace= false;
	private Graphics screenGraphics;
	
	public int n =0;


	public void keyProcess(){
		//여기서는 단순 케릭터가 이동하는 좌표 말고도
		//케릭터의 움직임 여부및 방향을 체크 합니다.
		playerMove = false;

		if ( pressedKeyUp ){
			GamingView.player.moveToUp();
			playerMove = true;
		}

		if ( pressedKeyDown){
			GamingView.player.moveToDown();
			playerMove = true;
		}

		if ( pressedKeyLeft){
			GamingView.player.moveToLeft();
			playerMove = true;
		}

		if ( pressedKeyRight){
			GamingView.player.moveToRight();
			playerMove = true;
		}
	}
		 
	public void keyPressed(KeyEvent e) {

		switch(e.getKeyCode()){
		case KeyEvent.VK_LEFT :
			pressedKeyLeft = true;
			break;
		case KeyEvent.VK_RIGHT :
			pressedKeyRight = true;
			break;
		case KeyEvent.VK_UP :
			pressedKeyUp = true;
			break;
		case KeyEvent.VK_DOWN :
			pressedKeyDown = true;
			break;
		case KeyEvent.VK_SPACE:
			if(GamingView.player.getBubbleNum() < GamingView.player.getMaxBubbleNum()) {
				GamingView.player.addBubbleNum();
				int x = GamingView.player.getMapX(GamingView.player.getPos_X()-20);
				int y = GamingView.player.getMapY(GamingView.player.getPos_Y()-10);
				//스페이지바 누른 좌표
				GamingView.Bubble_XY.add(String.valueOf(x)+","+String.valueOf(y)+","+"false");
			}
			break;
		}
		
		
	}

	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_LEFT :
			pressedKeyLeft = false;
			break;
		case KeyEvent.VK_RIGHT :
			pressedKeyRight = false;
			break;
		case KeyEvent.VK_UP :
			pressedKeyUp = false;
			break;
		case KeyEvent.VK_DOWN :
			pressedKeyDown = false;
			break;
		case KeyEvent.VK_SPACE:
			pushSpace =false;
			break;
		}
	}
	
	public void setScreenGraphics(Graphics screenGraphics) {
		this.screenGraphics=screenGraphics;
	}
	
	/*
	// 키 눌렀을 때 실행되도록 하는 곳
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		// 키 입력에 따라 플레이어 레이블을 PLAYER_MOVE에 설정한 깂만큼 움직인다
		switch (keyCode) {
		// 위쪽 화살표를 클릭했을 때
		case KeyEvent.VK_UP:
			GamingView.player.moveToUp();
			break;

		// 아래 화살표키
		case KeyEvent.VK_DOWN:
			GamingView.player.moveToDown();
			break;

		// 왼쪽 화살표키
		case KeyEvent.VK_LEFT:
			GamingView.player.moveToLeft();
			break;

		// 오른쪽 화살표키
		case KeyEvent.VK_RIGHT:
			GamingView.player.moveToRight();
			break;
		} // end switch

		// 아바타의 위치가 변경되었으니 다시 찍어준다(그려준다)
		// 이전에 위치하던 아바타를 지우고 다니 나타나게 해야 움직이는 것처럼 보이니까
		// 아바타의 부모 패널에게 다시 그리기를 지시함
	}
	*/
}

