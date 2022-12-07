import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public class Stage implements Runnable{
	private Thread th;
	private int userIndex = GamingView.userIndex;
	
	private Image tileImg = null; // 맵 타일
	public static final int BLOCK_W = 51;
	public static final int BLOCK_H = 62;
	public static final int START_W= 25;
	public static final int START_H = 62;
	
	public static ArrayList<Item> itemList = new ArrayList<>(); // 아이템 담을 리스트
	private Image itemImg = null;
	private int item_X, item_Y;
	
	private Monster boss = null; // 보스
	
	
	
	public static int [][] map = {
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
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
	};
	
	public ArrayList<Item> getItemList(){
		return itemList;
	}
	
	public void makeItems(Item item) {
		// 맵의 블럭이 0이면
		itemList.add(item);
	}
	
	public void makeBoss(Monster boss) {
		this.boss = boss;
	}
	
	public void drawTile(Graphics g) {
		//super(START_W +BLOCK_W*x, START_H+BLOCK_H*y, BLOCK_W,BLOCK_H,g);
		Image tileImg = null;
		g.setClip(null);
		// 초기 맵 깔기
		for (int y = 12; y >= 0; y--) {
			for (int x = 0; x < 15; x++) {
				switch (map[y][x]) {
				case 0:
					break;
				case 1:
					tileImg = new ImageIcon(Stage.class.getResource("/assets/map/forest/block/block_1.png")).getImage();
					g.drawImage(tileImg, START_W +BLOCK_W*x, START_H+BLOCK_H*y, null);
					break;
				case 2:
					tileImg = new ImageIcon(Stage.class.getResource("/assets/map/forest/block/block_4.png")).getImage();
					g.drawImage(tileImg, START_W +BLOCK_W*x, START_H+BLOCK_H*y, null);
					break;
				case 3:
					tileImg = new ImageIcon(Stage.class.getResource("/assets/map/forest/block/block_7.png")).getImage();
					g.drawImage(tileImg, START_W +BLOCK_W*x, START_H+BLOCK_H*y, null);
					break;
				}
				
			}
		}
	}
	
	public void breakBlock(int x, int y) {
		int len = GamingView.playerList.get(userIndex).waveLen;
		for (int i = 1; i <= len; i++) {
			if (x >= i)
				map[y][x - i] = 0;
			if (x + i < 15)
				map[y][x + i] = 0;
			if (y >= i)
				map[y - i][x] = 0;
			if (y + i < 13)
				map[y + i][x] = 0;
			map[y][x] = 0;
		}
	}
	
	
	
	public void drawItems(Graphics g) {
		// super(Tile.START_W + Tile.BLOCK_W *x, Tile.START_H + Tile.BLOCK_H *y);
		for(int i = 0; i< itemList.size(); i++) {
			itemImg = null;
			if(itemList.get(i) instanceof ItemSpeed
					&& map[itemList.get(i).getY()][itemList.get(i).getX()] == 0) {
				ItemSpeed speed = (ItemSpeed)itemList.get(i);
				itemImg = speed.getItemSpeed();
				item_X = Tile.START_W + Tile.BLOCK_W *speed.getX(); 
				item_Y = Tile.START_H + Tile.BLOCK_H *speed.getY()-20;
			}
			else if(itemList.get(i) instanceof ItemBubble
					&& map[itemList.get(i).getY()][itemList.get(i).getX()] == 0) {
				ItemBubble bubble = (ItemBubble)itemList.get(i);
				itemImg = bubble.getItemBubble();
				item_X = Tile.START_W + Tile.BLOCK_W *bubble.getX(); 
				item_Y = Tile.START_H + Tile.BLOCK_H *bubble.getY()-20;
			}
			else if(itemList.get(i) instanceof ItemFluid
					&& map[itemList.get(i).getY()][itemList.get(i).getX()] == 0) {
				ItemFluid fluid = (ItemFluid)itemList.get(i);
				itemImg = fluid.getItemFluid();
				item_X = Tile.START_W + Tile.BLOCK_W *fluid.getX(); 
				item_Y = Tile.START_H + Tile.BLOCK_H *fluid.getY()-20;
			}
			if(itemImg != null) {
				g.setClip(item_X, item_Y, 56, 70);
				if (GamingView.cnt/10 % 2 == 0) g.drawImage(itemImg, item_X - ( 56 * 0 ), item_Y, null);
				else if(GamingView.cnt/10% 2 == 1) g.drawImage(itemImg, item_X - ( 56 * 1 ), item_Y, null);
			}
		}
	}
	
	public void getItems() {
		for(int i=itemList.size()-1; i>=0; i--) {
			// 아이템의 위치와 플레이어의 위치가 같다면
			if (GamingView.playerList.get(userIndex).getMapX(GamingView.playerList.get(userIndex).getPos_X()) == itemList.get(i).getX()
					&& GamingView.playerList.get(userIndex).getMapY(GamingView.playerList.get(userIndex).getPos_Y()) == itemList.get(i).getY()) {
				// 스피드 아이템을 먹었다면
				if(itemList.get(i) instanceof ItemSpeed) {
					if (GamingView.playerList.get(userIndex).PLAYER_MOVE < 8)
						GamingView.playerList.get(userIndex).PLAYER_MOVE += 2;
				}
				else if(itemList.get(i) instanceof ItemBubble) {
					GamingView.playerList.get(userIndex).addBubbleNum();
				}
				// 아이템 리스트에서 삭제하여 화면에 보이지 않게한다.
				itemList.remove(i);
			}
		}
	}
	
	public void drawBoss(Graphics g) {
		g.drawImage(boss.getCurrentState(), boss.getX(), boss.getY(), null);
	}
	
	public void start() {
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void run() {
		makeItems(new ItemSpeed(1,3));
		makeItems(new ItemSpeed(12,3));
		makeItems(new ItemSpeed(7,6));
		makeItems(new ItemSpeed(2,10));
		makeItems(new ItemSpeed(6,11));
		
		makeItems(new ItemBubble(8,3));
		makeItems(new ItemBubble(1,6));
		makeItems(new ItemBubble(11,5));
		makeItems(new ItemBubble(1,11));
		makeItems(new ItemBubble(13,11));
		
		makeItems(new ItemFluid(2,2));
		makeItems(new ItemFluid(11,1));
		makeItems(new ItemFluid(3,7));
		makeItems(new ItemFluid(13,6));
		makeItems(new ItemFluid(8,9));
		
		makeBoss(GamingView.boss);
		
		while(itemList.size() > 0) {
			getItems();
		}
	}
}
