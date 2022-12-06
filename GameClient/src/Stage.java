import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

public class Stage implements Runnable{
	private Thread th;
	public static int cnt; // 무한루프를 카운터 하기 위한 변수
	private Image img = null;
	private int x,y;
	private ArrayList<Item> itemList = new ArrayList<>(); // 아이템 담을 리스트
	private Monster boss = null;
	
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
	
	public void drawItems(Graphics g) {
		for(int i = 0; i< itemList.size(); i++) {
			if(itemList.get(i) instanceof ItemSpeed) {
				ItemSpeed speed = (ItemSpeed)itemList.get(i);
				img = speed.getItemSpeed();
				x = speed.getX(); y = speed.getY();
			}
			else if(itemList.get(i) instanceof ItemBubble) {
				ItemBubble bubble = (ItemBubble)itemList.get(i);
				img = bubble.getItemBubble();
				x = bubble.getX(); y = bubble.getY();
			}
			else if(itemList.get(i) instanceof ItemFluid) {
				ItemFluid fluid = (ItemFluid)itemList.get(i);
				img = fluid.getItemFluid();
				x = fluid.getX(); y = fluid.getY();
			}
			g.setClip(x, y, 56, 70);
			if (cnt/10 % 2 == 0) g.drawImage(img, x - ( 56 * 0 ), y, null);
			else if(cnt/10% 2 == 1) g.drawImage(img, x - ( 56 * 1 ), y, null);
		}
	}
	
	public void getItems() {
		for(int i=itemList.size()-1; i>=0; i--) {
			// 아이템의 위치와 플레이어의 위치가 같다면
			if (GamingView.player.getMapX(GamingView.player.getPos_X()) == itemList.get(i).getX()
					&& GamingView.player.getMapY(GamingView.player.getPos_Y()) == itemList.get(i).getY()) {
				// 스피드 아이템을 먹었다면
				if(itemList.get(i) instanceof ItemSpeed) {
					if (GamingView.player.PLAYER_MOVE < 8)
						GamingView.player.PLAYER_MOVE += 2;
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
		cnt++;
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
		
		while(itemList.size() > 0) {
			getItems();
		}
	}
}
