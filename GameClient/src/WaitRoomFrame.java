import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class WaitRoomFrame  extends JFrame {
	private WaitPanel waitPanel = new WaitPanel();
	private Container c;
	private JButton startGameBtn;
	JTextPane textArea;
	private JTextField txtInput;
	Color blue = new Color(7,119,210);
	Color darkBlue = new Color(7,71,161);
	

	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓

	private String userName;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public WaitRoomFrame(String userName, ObjectInputStream ois, ObjectOutputStream oos) {
		this.userName=userName;
		this.ois = ois;
		this.oos= oos;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame이 정상적으로 종료되게
		setTitle("Game");
		setContentPane(waitPanel);
		c = getContentPane();
		c.setLayout(null);
		setBounds(100, 100, 998, 783);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 380, 480, 260);
		c.add(scrollPane);

		textArea = new JTextPane();
		textArea.setEditable(true);
		textArea.setBackground(blue);
		
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);
		

		txtInput = new JTextField();
		txtInput.setBounds(40,655, 470, 27);
		txtInput.setBackground(darkBlue);
		c.add(txtInput);
		txtInput.setColumns(10);
		
		ListenNetwork net = new ListenNetwork();
		net.start();
		TextSendAction action = new TextSendAction();
		txtInput.addActionListener(action);
		txtInput.requestFocus();

		
	
	}
		// 대기방 패널 
		class WaitPanel extends JPanel{
			private ImageIcon backgroundIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/lobby/waitRoom.png"));
			private Image backgroundImage  = backgroundIcon.getImage(); //이미지 객체
			
			public WaitPanel() {
				
				//게임 시작하기 버튼 
				ImageIcon startGameIcon =  new ImageIcon(JavaGameClientMain.class.getResource("/assets/lobby/startBtn.png"));
				startGameBtn = new JButton(startGameIcon);
				startGameBtn.setBounds(660, 620, 250,68);
				add(startGameBtn);
				
				Myaction action = new Myaction();
				startGameBtn.addActionListener(action);
				
		
				
			}
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.drawImage(backgroundImage,0,0,getWidth(),getHeight(), this);
			}
		
			
		
		}

		class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
		{
			@Override
			public void actionPerformed(ActionEvent e) {
	                if(e.getSource()==startGameBtn) {
	                	setVisible(false);
	                	
	                	GameFrame gameFrame = new GameFrame();
	                	gameFrame.setVisible(true);
	                }
			}
	
		}


		public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
			try {
				oos.writeObject(ob);
			} catch (IOException e) {
				System.out.println("SendObject Error");
			}
		}
		
		// keyboard enter key 치면 서버로 전송
		class TextSendAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Send button을 누르거나 메시지 입력하고 Enter key 치면
				if (e.getSource() == txtInput) {
					String msg = null;
					 msg = String.format("[%s] %s\n", userName, txtInput.getText());
//					msg = txtInput.getText();
//					SendMessage(msg);
					AppendText(msg);
					
					txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
					txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
					if (msg.contains("/exit")) // 종료 처리
						System.exit(0);
				}
			}
		}
		
		
		// 화면에 출력
		public void AppendText(String msg) {
			// textArea.append(msg + "\n");
			//AppendIcon(icon1);
			msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
			int len = textArea.getDocument().getLength();
			// 끝으로 이동
			textArea.setCaretPosition(len);
			textArea.replaceSelection(msg + "\n");
		}
		
		// Server에게 network으로 전송
		public void SendMessage(String msg) {
			try {
				ChatMsg obcm = new ChatMsg(userName, "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				// AppendText("dos.write() error");
				AppendText("oos.writeObject() error");
				try {
//					dos.close();
//					dis.close();
					ois.close();
					oos.close();
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.exit(0);
				}
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
						
						// code
						switch (cm.getCode()) {
							
							case "200": // chat message
								AppendText(msg);
								break;
							}
//								case "300": // Image 첨부
//									AppendText("[" + cm.getId() + "]");
//									AppendImage(cm.img);
//									break;
//								}
					} catch (IOException e) {
//								AppendText("ois.readObject() error");
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
