import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
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

public class WaitRoomFrame  extends JFrame{
	//private WaitPanel waitPanel = new WaitPanel();
	private JPanel contentPane;
	//private Container c;
	String userName;
	GameUser user = GameUser.getInstance();
	int roomNum;
	Boolean isOwner;
	
	private ImageIcon backgroundIcon = new ImageIcon(JavaGameClientMain.class.getResource("/assets/lobby/waitRoom.png"));
	private Image backgroundImage  = backgroundIcon.getImage(); //이미지 객체
	
	private JButton startGameBtn;
	JTextPane textArea;
	private JTextField txtInput;
	Color blue = new Color(7,119,210);
	Color darkBlue = new Color(7,71,161);
	Color white = new Color(255,255,255);
	Color black = new Color(0,0,0);
	

	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	
	public WaitRoomFrame(int roomNum, Boolean isOwner) {
		this.userName=user.getId();
		this.ois = user.getNet().getOIS();
		this.oos= user.getNet().getOOS();
		this.roomNum = roomNum;
		this.isOwner = isOwner;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame이 정상적으로 종료되게
		setTitle("Game");
		
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Dimension d = getSize();
				g.drawImage(backgroundImage, 0, 0, d.width, d.height, null);
			}
		};
		
		
		 
		//setContentPane(waitPanel);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//c = getContentPane();
		//c.setLayout(null);
		setBounds(100, 100, 1013, 793);
		//setBounds(100, 100, 998, 783);
		
		if(isOwner) {
			//게임 시작하기 버튼 
			ImageIcon startGameIcon =  new ImageIcon(JavaGameClientMain.class.getResource("/assets/lobby/startBtn.png"));
			startGameBtn = new JButton(startGameIcon);
			startGameBtn.setBounds(660, 620, 250,68);
			contentPane.add(startGameBtn);
			
			Myaction action = new Myaction();
			startGameBtn.addActionListener(action);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 380, 480, 260);
		contentPane.add(scrollPane);

		// 채팅 보이는 창
		textArea = new JTextPane();
		textArea.setForeground(new Color(255, 255, 255));
		textArea.setEnabled(false);
		textArea.setEditable(true);
		textArea.setBackground(blue);
		textArea.setFont(new Font("굴림체", Font.BOLD, 16));
		scrollPane.setViewportView(textArea);
		
		// 채팅 입력창
		txtInput = new JTextField();
		txtInput.setForeground(new Color(255, 255, 255));
		txtInput.setBounds(40,655, 470, 27);
		txtInput.setBackground(darkBlue);
		contentPane.add(txtInput);
		txtInput.setColumns(10);
		
		TextSendAction action1 = new TextSendAction();
		txtInput.addActionListener(action1);
		txtInput.requestFocus();
	}
		
	public void makeWaitingUser(String waitingInfo) {
		String[] result = waitingInfo.split(" "); // 0:몇번째 유저인지, 1:해당 유저 이름
		int waitingNum = Integer.parseInt(result[0])+1; // 받은 배열은 인덱스가 0부터 시작하기 때문에 +1해줌.
		String waitingName = result[1];
		
		// 게임 방 참가한 유저
		ImageIcon waitingUserIcon = new ImageIcon(WaitRoomFrame.class.getResource("/assets/player/bazzi/wait.png"));
		JLabel waitingUserLabel = new JLabel(waitingUserIcon);
		int pWidth = waitingUserIcon.getIconWidth(); // 플레이어의 너비와 높이
		int pHeight = waitingUserIcon.getIconHeight();
		Font name = new Font("배달의민족 도현",Font.PLAIN, 15); // 플레이어 이름
	    JLabel nameLabel = new JLabel(waitingName);
	    nameLabel.setFont(name);
	    nameLabel.setForeground(white); // 글자색
	    nameLabel.setHorizontalAlignment(JLabel.CENTER); // 글자 가운데로
	    ImageIcon readyIcon = new ImageIcon(WaitRoomFrame.class.getResource("/assets/readyImg.png")); // 레디이미지
	    JLabel readyLabel = new JLabel(readyIcon);
		
		switch(waitingNum) {
		case 1:
			// 방장 입장
			nameLabel.setBounds(40, 270, 114, 22);
			waitingUserLabel.setBounds(72, 184, pWidth, pHeight);
			break;
		case 2:
			nameLabel.setBounds(40+114+17, 270, 114, 22);
			waitingUserLabel.setBounds(200, 184, pWidth, pHeight);
			readyLabel.setBounds(40+114+17, 294, 114, 22);
			break;
		case 3:
			nameLabel.setBounds(40+114*2+17*2, 270, 114, 22);
			waitingUserLabel.setBounds(200+pWidth+78, 184, pWidth, pHeight);
			readyLabel.setBounds(40+114*2+17*2, 294, 114, 22);
			break;
		case 4:
			nameLabel.setBounds(40+114*3+17*3, 270, 114, 22);
			waitingUserLabel.setBounds(200+pWidth*2+78*2,184, pWidth, pHeight);
			readyLabel.setBounds(40+114*3+17*3, 294, 114, 22);
			break;
		default:
			System.out.println("WaitingRoomFrame.makeWaitingUser 오류");
		}
		
		contentPane.add(nameLabel);
		contentPane.add(waitingUserLabel);
		contentPane.add(readyLabel);
	}
	
	// 화면에 출력
	public void AppendText(String msg) {
		if(textArea != null) {
			msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
			int len = textArea.getDocument().getLength();
			// 끝으로 이동
			textArea.setCaretPosition(len);
			textArea.replaceSelection(msg + "\n");
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			System.out.println("SendObject Error");
		}
	}
	
	private class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==startGameBtn) {
            	ChatMsg obcm = new ChatMsg(user.getId(), "300", String.valueOf(roomNum));
    			SendObject(obcm);
            	
//    			setVisible(false);
//            	GamingView game = new GamingView();
//            	game.setVisible(true);
            }
		}
	}
	
	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == txtInput) {
				
            	ChatMsg obcm = new ChatMsg(user.getId(), "200", txtInput.getText());
    			SendObject(obcm);
				
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (txtInput.getText().contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}
}
