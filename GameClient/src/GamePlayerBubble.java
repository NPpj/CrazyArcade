import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
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
	private int userIndex = GamingView.userIndex;
	
	public static ArrayList<Bubble> bubbleList = new ArrayList(); // 물풍선 담을 리스트
	public static ArrayList<Fluid> fluidList = new ArrayList();
	private Image bubbleImg = new ImageIcon(GamingView.class.getResource("/assets/1.png")).getImage();
	private int bubble_X, bubble_Y;
	private int width=56, height=54;
	
	private Graphics g;
	
	private Image centerFluid = new ImageIcon(GamingView.class.getResource("/assets/pop.png"))
			.getImage();
	private Image leftFluid = new ImageIcon(GamingView.class.getResource("/assets/wave/left_end.png"))
			.getImage();
	private Image rightFluid = new ImageIcon(GamingView.class.getResource("/assets/wave/right_end.png"))
			.getImage();
	private Image upFluid = new ImageIcon(GamingView.class.getResource("/assets/wave/up_end.png"))
			.getImage();
	private Image downFluid = new ImageIcon(GamingView.class.getResource("/assets/wave/down_end.png"))
			.getImage();
	
	
	public void removeBubble() {
		for(int i=0;i<bubbleList.size();i++) {
			if(GamingView.cnt - bubbleList.get(i).getStartCnt() >= 90) {
				//stage.breakBlock(bubbleList.get(i).getX(),bubbleList.get(i).getY());
				int x=bubbleList.get(i).getX(); int y=bubbleList.get(i).getY();
				GameInfo obcm = new GameInfo("403", GamingView.roomNum, userIndex, x+","+y); // 블럭 깨기
				SendObject(obcm);
				bubbleList.remove(i);
				GamingView.playerList.get(userIndex).downBubbleNum();
			}
		}
	}
	
	public void start() {
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void run(){
		
		while(!GamingView.playerList.get(userIndex).getPlayerState().equals("die")) {
			System.out.println("현재 물풍선 리스트 크기: "+bubbleList.size());
			removeBubble();
		}
	}
	
	public void drawBubbles(Graphics g) {
		this.g = g;
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
	
	public void drawFluid(Graphics g) {
		for(int i = 0; i< fluidList.size(); i++) {
			if(GamingView.cnt - fluidList.get(i).getStartCnt()>=90) {
				int tempX = fluidList.get(i).getX(); // 물줄기 시작 x좌표
				int tempY = fluidList.get(i).getY(); // 물줄기 시작 y좌표
				int x = Stage.START_W + Stage.BLOCK_W*tempX;
				int y = Stage.START_H + Stage.BLOCK_H*tempY;
				
				g.setClip(x , y, 52, 52);
				if (GamingView.cnt/10 %11== 0) g.drawImage(centerFluid, x - ( width * 0 ), y, null);
				else if(GamingView.cnt/10%11 == 1) g.drawImage(centerFluid, x - ( width * 1 ), y, null);
				else if(GamingView.cnt/10%11 == 2) g.drawImage(centerFluid, x - ( width * 2 ), y, null);
				else if(GamingView.cnt/10%11 == 3) g.drawImage(centerFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 4) g.drawImage(centerFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 5) g.drawImage(centerFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 6) g.drawImage(centerFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 7) g.drawImage(centerFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 8) g.drawImage(centerFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 9) g.drawImage(centerFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 10) g.drawImage(centerFluid, x - ( width * 3 ), y, null);
				
				g.setClip(x-60, y, 52, 52);
				if (GamingView.cnt/10 %11== 0) g.drawImage(leftFluid, x - ( width * 0 ), y, null);
				else if(GamingView.cnt/10%11 == 1) g.drawImage(leftFluid, x - ( width * 1 ), y, null);
				else if(GamingView.cnt/10%11 == 2) g.drawImage(leftFluid, x - ( width * 2 ), y, null);
				else if(GamingView.cnt/10%11 == 3) g.drawImage(leftFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 4) g.drawImage(leftFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 5) g.drawImage(leftFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 6) g.drawImage(leftFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 7) g.drawImage(leftFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 8) g.drawImage(leftFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 9) g.drawImage(leftFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 10) g.drawImage(leftFluid, x - ( width * 3 ), y, null);
				
				g.setClip(x+60, y, 52, 52);
				if (GamingView.cnt/10 %11== 0) g.drawImage(rightFluid, x - ( width * 0 ), y, null);
				else if(GamingView.cnt/10%11 == 1) g.drawImage(rightFluid, x - ( width * 1 ), y, null);
				else if(GamingView.cnt/10%11 == 2) g.drawImage(rightFluid, x - ( width * 2 ), y, null);
				else if(GamingView.cnt/10%11 == 3) g.drawImage(rightFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 4) g.drawImage(rightFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 5) g.drawImage(rightFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 6) g.drawImage(rightFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 7) g.drawImage(rightFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 8) g.drawImage(rightFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 9) g.drawImage(rightFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 10) g.drawImage(rightFluid, x - ( width * 3 ), y, null);
				
				g.setClip(x, y-60, 52, 52);
				if (GamingView.cnt/10 %11== 0) g.drawImage(upFluid, x - ( width * 0 ), y, null);
				else if(GamingView.cnt/10%11 == 1) g.drawImage(upFluid, x - ( width * 1 ), y, null);
				else if(GamingView.cnt/10%11 == 2) g.drawImage(upFluid, x - ( width * 2 ), y, null);
				else if(GamingView.cnt/10%11 == 3) g.drawImage(upFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 4) g.drawImage(upFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 5) g.drawImage(upFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 6) g.drawImage(upFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 7) g.drawImage(upFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 8) g.drawImage(upFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 9) g.drawImage(upFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 10) g.drawImage(upFluid, x - ( width * 3 ), y, null);
				
				g.setClip(x , y+60, 52, 52);
				if (GamingView.cnt/10 %11== 0) g.drawImage(downFluid, x - ( width * 0 ), y, null);
				else if(GamingView.cnt/10%11 == 1) g.drawImage(downFluid, x - ( width * 1 ), y, null);
				else if(GamingView.cnt/10%11 == 2) g.drawImage(downFluid, x - ( width * 2 ), y, null);
				else if(GamingView.cnt/10%11 == 3) g.drawImage(downFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 4) g.drawImage(downFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 5) g.drawImage(downFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 6) g.drawImage(downFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 7) g.drawImage(downFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 8) g.drawImage(downFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 9) g.drawImage(downFluid, x - ( width * 3 ), y, null);
				else if(GamingView.cnt/10%11 == 10) g.drawImage(downFluid, x - ( width * 3 ), y, null);
				
				fluidList.remove(i);
			}
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
