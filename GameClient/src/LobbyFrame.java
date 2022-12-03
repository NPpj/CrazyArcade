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
	private int roomPosition;
	GameUser user = GameUser.getInstance();
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	final String[] idx = {"1", "2", "3", "4", "5", "6"};
	JButton[] btnEnterRoom = new JButton[idx.length];
	
	public LobbyFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame이 정상적으로 종료되게
		setTitle("Lobby");
		setContentPane(lobby);
		c = getContentPane();
		c.setLayout(null);
		setBounds(100, 100, 1015, 760);
		//setBounds(100, 100, 1000, 750);

		customCursor();
		setVisible(true);
		
		this.ois=user.getNet().getOIS();
		this.oos=user.getNet().getOOS();
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
			ImageIcon makeRoomImage = new ImageIcon(JavaGameClientMain.class.getResource("/assets/lobby/btn_makeRoom.png"));
			JButton makeRoomBtn = new JButton(makeRoomImage);
			makeRoomBtn.setSize(180, 36);
			makeRoomBtn.setLocation(30, 35);
			add(makeRoomBtn);
			
			makeRoomBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == makeRoomBtn) {
						makeRoomDialog = new MakeRoomDialog(c);
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
	
	public JButton makeEnterRoomBtn() {
		JButton btnEnterRoom = new JButton();
		//btnEnterRoom.setContentAreaFilled(false);// 배경색 없음
		btnEnterRoom.setBorderPainted(false); // 테두리 없음
		btnEnterRoom.addActionListener(new MyactionListener());
		
		return btnEnterRoom;
	}
	
	private class MyactionListener implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			ChatMsg obcm = new ChatMsg(user.getId(), "102", e.getActionCommand());
			SendObject(obcm);
		}
	}

	public void setRoomPos(int roomPosition) {
		this.roomPosition = roomPosition;
	}
	public int getRoomPos() {
		return this.roomPosition;
	}
	
	public void makeRoom(int roomNum, String title) {
		
		for (int i = 0; i < idx.length; i++) {
			btnEnterRoom[i] = new JButton(idx[i]);
			btnEnterRoom[i].setContentAreaFilled(false);// 배경색 없음
			btnEnterRoom[i].setBorderPainted(false); // 테두리 없음
			btnEnterRoom[i].addActionListener(new MyactionListener());
        }
		//JButton btnEnterRoom = makeEnterRoomBtn();
		
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
			btnEnterRoom[0].setBounds(36,104,461,159);
			getContentPane().add(btnEnterRoom[0]);
			setRoomPos(1);
			lbl.setBounds(25,120,160,120);
			tf1.setBounds(220,132,70,38);
			tf2.setBounds(370,132,100,38);
			break;
		case 2:
			btnEnterRoom[1].setBounds(507,104,461,159);
			getContentPane().add(btnEnterRoom[1]);
			setRoomPos(2);
			 lbl.setBounds(495,120,160,120);
			 tf1.setBounds(700,132,70,38);
			 tf2.setBounds(875,132,70,38);
			 break;
		case 3:
			btnEnterRoom[2].setBounds(36,104+174,461,159);
			getContentPane().add(btnEnterRoom[2]);
			setRoomPos(3);
			 lbl.setBounds(25,120+175,160,120);
			 tf1.setBounds(220,132+175,70,38);
			 tf2.setBounds(400,132+175,70,38);
			 break;
		case 4:
			btnEnterRoom[3].setBounds(507,104+175,461,159);
			getContentPane().add(btnEnterRoom[3]);
			setRoomPos(4);
			 lbl.setBounds(495,120+175,160,120);
			 tf1.setBounds(700,132+175,70,38);
			 tf2.setBounds(875,132+175,70,38);
			 break;
		case 5:
			btnEnterRoom[4].setBounds(36,104+174*2,461,159);
			getContentPane().add(btnEnterRoom[4]);
			setRoomPos(5);
			 lbl.setBounds(25,120+175*2,160,120);
			 tf1.setBounds(220,127+175*2,70,38);
			 tf2.setBounds(400,127+175*2,70,38);
			 break;
		case 6:
			btnEnterRoom[5].setBounds(507,104+175*2,462,459);
			getContentPane().add(btnEnterRoom[5]);
			setRoomPos(6);
			 lbl.setBounds(495,120+175*2,160,120);
			 tf1.setBounds(700,127+175*2,70,38);
			 tf2.setBounds(875,127+175*2,70,38);
			 break;
		}
		
		getContentPane().add(lbl);
		getContentPane().add(tf1);
		getContentPane().add(tf2);
	}
	
	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			System.out.println("SendObject Error");
		}
	}
}
