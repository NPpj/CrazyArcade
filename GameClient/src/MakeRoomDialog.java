
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class MakeRoomDialog extends JDialog {

	private MakeRoomPanel makeRoomPanel = new MakeRoomPanel();
	private ImageIcon backgroundIcon = new ImageIcon(
			JavaGameClientMain.class.getResource("/assets/lobby/makeRoom.png"));
	private Image backgroundImage = backgroundIcon.getImage(); // 이미지 객체
	private JPasswordField roomPassword;
	private JTextField roomTitle;
	//private String userName;
	private Container c;
	//private ListenNetwork net;
	GameUser user = GameUser.getInstance();
	
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	/**
	 * Create the dialog.
	 */
	public MakeRoomDialog(Container c) {
		this.c=c;
		//this.userName=userName;
		//this.net=net;
		this.ois = user.getNet().getOIS();
		this.oos = user.getNet().getOOS();
		setBounds(300, 300, 450, 350);
		getContentPane().setLayout(new BorderLayout());
		makeRoomPanel.setLayout(null);
		makeRoomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(makeRoomPanel);

	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			System.out.println("SendObject Error");
		}
	}

	class MakeRoomPanel extends JPanel {

		public MakeRoomPanel() {

			// 방 제목 입력
			roomTitle = new JTextField();
			roomTitle.setBounds(149, 100, 282, 20);
			add(roomTitle);

			// 방 비밀번호 설정 체크박스
			JCheckBox check = new JCheckBox("");
			check.setBounds(150, 149, 282, 20);
			add(check);

			Color enableColor = new Color(225, 225, 225);

			check.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == check) {
						if (roomPassword.getBackground() == enableColor) {
							roomPassword.setEnabled(true);
							roomPassword.setBackground(Color.white);
						} else {
							roomPassword.setEnabled(false);
							roomPassword.setBackground(enableColor);
						}

					}
				}
			});

			// 방 비밀번호 입력
			roomPassword = new JPasswordField(10);
			roomPassword.setBounds(149, 180, 200, 30);
			roomPassword.setEnabled(false);
			roomPassword.setBackground(enableColor);
			roomPassword.setEchoChar('*');
			add(roomPassword);

			// 확인 버튼
			ImageIcon okBtnImage = new ImageIcon(JavaGameClientMain.class.getResource("/assets/lobby/btn_ok.png"));
			JButton okBtn = new JButton(okBtnImage);
			okBtn.setBounds(95, 260, 110, 40);
			add(okBtn);
			
			okBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if(e.getSource()==okBtn) {
	                	String RoomInfo = roomTitle.getText()+ " " +roomPassword.getText();
	                	
	                	//방 정보 서버로 넘기기 
	                	ChatMsg obcm = new ChatMsg(user.getId(), "101", RoomInfo);
	        			SendObject(obcm);
	                	
	             
	                	MakeRoomDialog.this.dispose();

////						대기방 열기 
//	                	WaitRoomFrame waitFrame = new WaitRoomFrame(userName,ois,oos,net);
//	                	waitFrame.setVisible(true);
	                	
	                	
	                }	
	            }
	        });
			
			//취소 버튼 
			ImageIcon cancelBtnImage = new ImageIcon(JavaGameClientMain.class.getResource("/assets/lobby/btn_cancel.png"));
			JButton cancelBtn = new JButton(cancelBtnImage);
			cancelBtn.setBounds(235, 260, 110, 40);
			add(cancelBtn);

			cancelBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == cancelBtn) {
						MakeRoomDialog.this.dispose();

					}
				}
			});
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}

		public void customCursor() {

	        // Custom Cursor 설정하기 
	        Toolkit tk = Toolkit.getDefaultToolkit();
	        Image cursorimage = tk.getImage(JavaGameClientMain.class.getResource("/assets/cursor.png"));
	        Point point = new Point(10,10);
	        Cursor cursor = tk.createCustomCursor(cursorimage, point, "");
	        
	        getContentPane().setCursor(cursor);
	    }
		
	}

}
