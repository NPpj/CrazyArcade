import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;

public class GamePlayerBubble implements Runnable{
	//super(Tile.START_W +Tile.BLOCK_W*x, Tile.START_H+Tile.BLOCK_H*y,56,54,g);
	public static Stage stage = new Stage();
	
	private Thread th;
	private KeyListener keyListener;
	private int cnt1;
	
	//public static ArrayList<Bubble> tempBubbleList = new ArrayList();
	public static ArrayList<Bubble> bubbleList = new ArrayList(); // 물풍선 담을 리스트
	private Image bubbleImg = new ImageIcon(GamingView.class.getResource("/assets/1.png")).getImage();
	private int bubble_X, bubble_Y;
	private int width=56, height=54;
	
	
	//public void spaceListener(int x, int y) {
	//	makeBubble(new Bubble(x,y));
	//}
	/*
	public void syncBubble() {
		bubbleList = GamingView.player.getMyBubbleList();
	}*/
	/*
	public void addBubble() {
		if(keyListener.isPressedKeySpaece()) {
			System.out.println("내가 공격");
			if(GamingView.player.getBubbleNum() < GamingView.player.getMaxBubbleNum()) {
				GamingView.player.addBubbleNum();
				// x,y 는 map 좌표 
				int x = GamingView.player.getMapX(GamingView.player.getPos_X()-20);
				int y = GamingView.player.getMapY(GamingView.player.getPos_Y()-10);
				bubbleList.add(new Bubble(x,y));
			}
		}
	}*/
	public void removeBubble() {
		for(int i=0;i<bubbleList.size();i++) {
			if(GamingView.cnt - bubbleList.get(i).getStartCnt() >= 90) {
				stage.breakBlock(bubbleList.get(i).getX(),bubbleList.get(i).getY());
				bubbleList.remove(i);
				GamingView.player.downBubbleNum();
			}
		}
	}
	
	// 2초뒤 물풍선 없어짐
	public void makeBubble(Bubble newBubble) {
		if(GamingView.player.getBubbleNum() < GamingView.player.getMaxBubbleNum()) {
			GamingView.player.addBubbleNum();
			
			Timer timer=new Timer();
			TimerTask task=new TimerTask(){
			    @Override
			    public void run() {
			    	bubbleList.remove(newBubble);
			    	GamingView.player.downBubbleNum();
			    }
			};
			timer.schedule(task, 2000);
		}
	}
	
	public void start() {
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void run(){
		while(!GamingView.player.getPlayerState().equals("die")) {
			System.out.println("현재 물풍선 리스트 크기: "+bubbleList.size());
			removeBubble();
		}
	}
	
	public void drawBubbles(Graphics g) {
		// super(Tile.START_W +Tile.BLOCK_W*x, Tile.START_H+Tile.BLOCK_H*y,56,54,g);
		for(int i = 0; i< bubbleList.size(); i++) {
			int tempX = bubbleList.get(i).getX(); // 물풍선 x좌표
			int tempY = bubbleList.get(i).getY(); // 물풍선 y좌표
			int x = Stage.START_W + Stage.BLOCK_W*tempX;
			int y = Stage.START_H + Stage.BLOCK_H*tempY;
			
			g.setClip(x , y, 56, 54);
			if (GamingView.cnt/10 %4== 0) g.drawImage(bubbleImg, x - ( width * 0 ), y, null);
			else if(GamingView.cnt/10%4 == 1) g.drawImage(bubbleImg, x - ( width * 1 ), y, null);
			else if(GamingView.cnt/10%4 == 2) g.drawImage(bubbleImg, x - ( width * 2 ), y, null);
			else if(GamingView.cnt/10%4 == 3) g.drawImage(bubbleImg, x - ( width * 3 ), y, null);
		}
	}

}
