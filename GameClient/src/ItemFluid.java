import java.awt.Image;

import javax.swing.ImageIcon;

public class ItemFluid extends Item{
	private Image itemFluid = new ImageIcon(GamingView.class.getResource("/assets/item/fluid.png"))
			.getImage();
	
	public ItemFluid(int x, int y) {
		super(x, y);
	}
	
	public Image getItemFluid() {
		return itemFluid;
	}
}
