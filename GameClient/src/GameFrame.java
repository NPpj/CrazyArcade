import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
	private GamePanel gamePanel = new GamePanel();
	private Container c;
	
	//블록 크기 
	public static final int BLOCK_W = 50;
	public static final int BLOCK_H = 60;
	public static final int GRASS_W = 50;
	public static final int GRASS_H = 50;
	public static final int FLOWER_W = 60;
	public static final int FLOWER_H = 70;
	
	private int [][] map = {
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
			{1,0,0,0,1,1,0,0,0,1,1,0,0,0,1}
	};
	
	public GameFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame이 정상적으로 종료되게
		setTitle("Game");
		setContentPane(gamePanel);
		c = getContentPane();
		c.setLayout(null);
		setBounds(100, 100, 998, 783);
		
		
		// 초기 맵 깔기 
		for(int y=12; y>=0; y--) {
			for(int x=0; x<15;x++) {
				switch(map[y][x]) {
				case 0:
					break;
				case 1:
					Tile block = new Tile(x,y,BLOCK_W,BLOCK_H,"상자",c);
					break;
				case 2:
					Tile flower1 = new Tile(x,y,FLOWER_W,FLOWER_H,"꽃1",c);
					break;
				case 3:
					Tile flower2 = new Tile(x,y,FLOWER_W,FLOWER_H,"꽃2",c);
					break;
			}
			};
		
		}
		setVisible(true);
	}
	
	
	class GamePanel extends JPanel{
		private ImageIcon backgroundIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/game/GameBackGround.png"));
		private Image backgroundImage  = backgroundIcon.getImage(); //이미지 객체
		
		public GamePanel() {
			
			
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.drawImage(backgroundImage,0,0,getWidth(),getHeight(), this);
		}
	}
	

	
	public void customCursor() {
        // Custom Cursor 설정하기 
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image cursorimage = tk.getImage(JavaGameClientMain.class.getResource("/assets/cursor.png"));
        Point point = new Point(10,10);
        Cursor cursor = tk.createCustomCursor(cursorimage, point, "");
        
        c.setCursor(cursor);
    }

}
