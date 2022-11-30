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

public class LobbyFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private LobbyPanel lobby = new LobbyPanel();
	private MakeRoomDialog makeRoomDialog;
	private Container c;

	private String userName;

	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private String ip_addr;
	private String port_no;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	/**
	 * Create the frame.
	 */
	public LobbyFrame(String userName, String ip_addr, String port_no) {
		this.ip_addr = ip_addr;
		this.port_no = port_no;
		this.userName = userName;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame이 정상적으로 종료되게
		setTitle("Lobby");
		setContentPane(lobby);
		c = getContentPane();
		c.setLayout(null);
		setBounds(100, 100, 1000, 750);

		customCursor();
		setVisible(true);

		// 소켓 연결
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			ChatMsg obcm = new ChatMsg(userName, "100", "Login");
			SendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// AppendText("connect error");
		}

	}

	public void customCursor() {
		// Custom Cursor 설정하기
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image cursorimage = tk.getImage(JavaGameClientMain.class.getResource("/assets/cursor.png"));
		Point point = new Point(10, 10);
		Cursor cursor = tk.createCustomCursor(cursorimage, point, "");

		c.setCursor(cursor);
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			System.out.println("SendObject Error");
		}
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
			makeRoomBtn.setLocation(30, 30);
			add(makeRoomBtn);

			makeRoomBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == makeRoomBtn) {
						makeRoomDialog = new MakeRoomDialog(userName, ois, oos);
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

	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {

					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s] %s", cm.getUserName(), cm.getData());
					} else
						continue;
//					switch (cm.getCode()) {
//					case "200": // chat message
//						AppendText(msg);
//						break;
//					case "300": // Image 첨부
//						AppendText("[" + cm.getId() + "]");
//						AppendImage(cm.img);
//						break;
//					}
				} catch (IOException e) {
//					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝

			}
		}
	}

}
