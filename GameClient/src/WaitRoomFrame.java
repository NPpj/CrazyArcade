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
	private ListenNetwork net;
	
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

	public WaitRoomFrame(String userName, ObjectInputStream ois, ObjectOutputStream oos, ListenNetwork net) {
		this.userName=userName;
		this.ois = ois;
		this.oos= oos;
		this.net=net;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame이 정상적으로 종료되게
		setTitle("Game");
		setContentPane(waitPanel);
		c = getContentPane();
		c.setLayout(null);
		setBounds(100, 100, 1013, 793);
		//setBounds(100, 100, 998, 783);
		
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
	                	
//	                	GameFrame gameFrame = new GameFrame();
//	                	gameFrame.setVisible(true);
	                	
	                	GamingView game = new GamingView();
	                	game.setVisible(true);
	                			
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
					
                	ChatMsg obcm = new ChatMsg(userName, "200", txtInput.getText());
        			SendObject(obcm);
					
					txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
					txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
					if (txtInput.getText().contains("/exit")) // 종료 처리
						System.exit(0);
				}
			}
		}
		
		
			
			
}
