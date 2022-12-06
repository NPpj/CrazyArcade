import java.awt.Image;
import javax.swing.ImageIcon;

public class ItemSpeed extends Item{
	private Image itemSpeed = new ImageIcon(GamingView.class.getResource("/assets/item/roller.png"))
		.getImage();
		
	public ItemSpeed(int x, int y) {
		super(x, y);
	}
	
	public Image getItemSpeed() {
		return itemSpeed;
	}
}
