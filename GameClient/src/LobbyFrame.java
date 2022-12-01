import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LobbyFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private LobbyPanel lobby = new LobbyPanel();
	private MakeRoomDialog makeRoomDialog;
	private Container c;
	private String userName;
	private ListenNetwork net;

	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	/*
	 * Create the frame.
	 */
	public LobbyFrame(String userName, ListenNetwork net) {
		this.userName = userName;
		this.net=net;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame이 정상적으로 종료되게
		setTitle("Lobby");
		setContentPane(lobby);
		c = getContentPane();
		c.setLayout(null);
		setBounds(100, 100, 1000, 750);

		customCursor();
		setVisible(true);
		
	}

	public void customCursor() {
		// Custom Cursor 설정하기
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image cursorimage = tk.getImage(JavaGameClientMain.class.getResource("/assets/cursor.png"));
		Point point = new Point(10, 10);
		Cursor cursor = tk.createCustomCursor(cursorimage, point, "");

		c.setCursor(cursor);
	}

	class LobbyPanel extends JPanel {
		private ImageIcon backgroundIcon = new ImageIcon(
				JavaGameClientMain.class.getResource("/assets/lobby/lobby_background.png"));
		private Image backgroundImage = backgroundIcon.getImage(); // 이미지 객체

		public LobbyPanel() {
			// 방만들기 버튼
			ImageIcon makeRoomImage = new ImageIcon(
					JavaGameClientMain.class.getResource("/assets/lobby/btn_makeRoom.png"));
			JButton makeRoomBtn = new JButton(makeRoomImage);
			makeRoomBtn.setSize(180, 36);
			makeRoomBtn.setLocation(30, 35);
			add(makeRoomBtn);

			makeRoomBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == makeRoomBtn) {
						makeRoomDialog = new MakeRoomDialog(c,userName,net);
						makeRoomDialog.setVisible(true);

					}
				}
			});
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}

	}
	

	public void makeRoom(int roomNum, String title) {
		
		ImageIcon icon =new ImageIcon(JavaGameClientMain.class.getResource("/assets/player/seal.png"));
		JLabel lbl = new JLabel(icon);
	    
	    Font font = new Font("배달의민족 도현",Font.BOLD, 15);
	    Font font2 = new Font("배달의민족 도현",Font.BOLD, 25);
	    
	    JLabel tf1 = new JLabel(Integer.toString(roomNum));
		tf1.setFont(font);
		
		

		JLabel tf2 = new JLabel(title);
		tf2.setFont(font2);
		
		
		switch(roomNum) {
		case 1:
			 lbl.setBounds(25,120,160,120);
			 tf1.setBounds(220,132,70,38);
			 tf2.setBounds(370,132,100,38);
			 break;
		case 2:
			 lbl.setBounds(495,120,160,120);
			 tf1.setBounds(700,132,70,38);
			 tf2.setBounds(875,132,70,38);
			 break;
		case 3:
			 lbl.setBounds(25,120+175,160,120);
			 tf1.setBounds(220,132+175,70,38);
			 tf2.setBounds(400,132+175,70,38);
			 break;
		case 4:
			 lbl.setBounds(495,120+175,160,120);
			 tf1.setBounds(700,132+175,70,38);
			 tf2.setBounds(875,132+175,70,38);
			 break;
		case 5:
			 lbl.setBounds(25,120+175*2,160,120);
			 tf1.setBounds(220,127+175*2,70,38);
			 tf2.setBounds(400,127+175*2,70,38);
			 break;
		case 6:
			 lbl.setBounds(495,120+175*2,160,120);
			 tf1.setBounds(700,127+175*2,70,38);
			 tf2.setBounds(875,127+175*2,70,38);
			 break;
		}
		
		add(lbl);
		add(tf1);
		add(tf2);
		
		
	}
}
