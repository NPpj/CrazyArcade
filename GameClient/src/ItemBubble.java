import java.awt.Image;

import javax.swing.ImageIcon;

public class ItemBubble extends Item{
	private Image itemBubble = new ImageIcon(GamingView.class.getResource("/assets/item/bubble.png"))
			.getImage();
			
	public ItemBubble(int x, int y) {
		super(x, y);
	}
	
	public Image getItemBubble() {
		return itemBubble;
	}
}
