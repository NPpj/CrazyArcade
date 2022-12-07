
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

public class KeyListener extends KeyAdapter {
	private static boolean pressedKeyUp = false;
	private static boolean pressedKeyDown = false;
	private static boolean pressedKeyLeft = false;
	private static boolean pressedKeyRight = false;
	public static int pressedKeySpace_X = 0; 
	public static int pressedKeySpace_Y = 0; 
	public static boolean pushSpace= false;
	private Graphics screenGraphics;
	private int userIndex = GamingView.userIndex;
	
	public int n =0;


	public void keyProcess(){
		//여기서는 단순 케릭터가 이동하는 좌표 말고도
		//케릭터의 움직임 여부및 방향을 체크 합니다.
		GamingView.playerList.get(userIndex).playerMove = false;

		if ( pressedKeyUp ){
			GameInfo obcm = new GameInfo("400",GamingView.roomNum,userIndex, "pressedKeyUp");
			SendObject(obcm);
		}

		if ( pressedKeyDown){
			GameInfo obcm = new GameInfo("400",GamingView.roomNum,userIndex, "pressedKeyDown");
			SendObject(obcm);
		}

		if ( pressedKeyLeft){
			GameInfo obcm = new GameInfo("400",GamingView.roomNum,userIndex, "pressedKeyLeft");
			SendObject(obcm);
		}

		if ( pressedKeyRight){
			GameInfo obcm = new GameInfo("400",GamingView.roomNum,userIndex, "pressedKeyRight");
			SendObject(obcm);
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
		case KeyEvent.VK_SPACE: // 물풍선 놓기 
//			if(GamingView.playerList.get(userIndex).getBubbleNum() < GamingView.playerList.get(userIndex).getMaxBubbleNum()) {
//				GamingView.playerList.get(userIndex).addBubbleNum();
				// x,y 는 map 좌표 
				int x = GamingView.playerList.get(userIndex).getMapX(GamingView.playerList.get(userIndex).getPos_X()-20);
				int y = GamingView.playerList.get(userIndex).getMapY(GamingView.playerList.get(userIndex).getPos_Y()-10);
//				GamingView.Bubble_XY.add(String.valueOf(x)+","+String.valueOf(y));
				GameInfo obcm = new GameInfo("401", GamingView.roomNum, userIndex, String.valueOf(x)+","+String.valueOf(y));
				SendObject(obcm);
//			}
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
	
	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			GamingView.oos.writeObject(ob);
		} catch (IOException e) {
			System.out.println("SendObject Error");
		}
	}

	
}

