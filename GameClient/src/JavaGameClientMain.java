// JavaObjClient.java
// ObjecStream 사용하는 채팅 Client

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import com.sun.tools.javac.Main;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class JavaGameClientMain extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUserName;
	private JPasswordField txtUserPassword;
	private JTextField txtIpAddress;
	private JTextField txtPortNumber;
	private ListenNetwork net;

	private String userName;

	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	// jw1. 배경 이미지
	private Image background = new ImageIcon(JavaGameClientMain.class.getResource("/assets/background/login_bg.png")).getImage();
	
	private static String IP_ADDR = "127.0.0.1";
	private static String PORT_NUMBER = "30000";
	
	String []id = new String[] {"test", "jiwon", "mihye"," "};
	String []password = new String[] {"test1234", "jiwon1234", "mihye1234"," "};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaGameClientMain frame = new JavaGameClientMain();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JavaGameClientMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1015, 760);
		//setBounds(100, 100, 1000, 750);
		setResizable(false); // 창의 크기를 변경하지 못하게
		
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Dimension d = getSize();
				g.drawImage(background, 0, 0, d.width, d.height, null);
			}
		};
		contentPane.setBackground(new Color(255, 0,0,0)); // 투명
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		customCursor();
		
		// jw2. 사용자 아이디
		txtUserName = new JTextField();
		txtUserName.setBorder(BorderFactory.createEmptyBorder(0, 10, 0 , 0));
		txtUserName.setHorizontalAlignment(SwingConstants.LEFT);
		txtUserName.setBounds(472, 581, 187, 24);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		// jw2. 사용자 비밀번호
		txtUserPassword = new JPasswordField();
		// 내부 여백. 상, 좌, 하, 우
		txtUserPassword.setBorder(BorderFactory.createEmptyBorder(0, 10, 0 , 0));
		txtUserPassword.setHorizontalAlignment(SwingConstants.LEFT);
		txtUserPassword.setBounds(472, 612, 187, 24);
		contentPane.add(txtUserPassword);
		txtUserPassword.setColumns(10);
		
		// jw3. 로그인 버튼
		JButton btnConnect = new JButton("");
		btnConnect.setBorderPainted(false);
		ImageIcon imgConnect = new ImageIcon(JavaGameClientMain.class.getResource("/assets/login/btn_login.png"));
		// icon 크기 조정
		btnConnect.setIcon(resizeIcon(imgConnect.getImage(), 186, 44));
		btnConnect.setBounds(297, 669, 186, 44);
		contentPane.add(btnConnect);
		
		// jw4. 종료 버튼
		JButton btnExit = new JButton("");
		btnExit.setBorderPainted(false);
		ImageIcon imgExit = new ImageIcon(JavaGameClientMain.class.getResource("/assets/login/btn_exit.png"));
		btnExit.setIcon(resizeIcon(imgExit.getImage(), 186, 44));
		btnExit.setBounds(518, 669, 186, 44);
		contentPane.add(btnExit);
		btnExit.revalidate();
		btnExit.repaint();
		contentPane.revalidate();
		contentPane.repaint();
		
		Myaction action = new Myaction();
		btnConnect.addActionListener(action);
		txtUserName.addActionListener(action);
		txtUserPassword.addActionListener(action);
		
		
	}
	
	
	public ImageIcon resizeIcon(Image img, Integer width, Integer height) {
		Image resizeImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon resizeIcon = new ImageIcon(resizeImg);
		
		return resizeIcon;
	}
	
	public void customCursor() {
        // Custom Cursor 설정하기 
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image cursorimage = tk.getImage(JavaGameClientMain.class.getResource("/assets/cursor.png"));
        Point point = new Point(10,10);
        Cursor cursor = tk.createCustomCursor(cursorimage, point, "");
        
        contentPane.setCursor(cursor);
    }
	
	public boolean checkUserData(String id, String password) {
		Boolean isUser = false;
		
		for(int i=0; i<this.id.length; i++) {
			if(this.id[i].equals(id)) {
				if(this.password[i].equals(password))
					isUser=true;
				else 
					break;
			}
		}
		
		if(isUser)
			return true;
		else 
			return false;
	}
	
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = txtUserName.getText().trim();
			String user_password = txtUserPassword.getText().trim();
			String ip_addr = IP_ADDR;
			String port_no = PORT_NUMBER;
			
			if(!txtUserName.getText().isEmpty() && !txtUserPassword.getText().isEmpty()) {
				if(checkUserData(txtUserName.getText(), txtUserPassword.getText())) {
					setVisible(false);

					// 소켓 연결
					try {
						socket = new Socket(IP_ADDR, Integer.parseInt(PORT_NUMBER));

						oos = new ObjectOutputStream(socket.getOutputStream());
						oos.flush();
						ois = new ObjectInputStream(socket.getInputStream());


					} catch (NumberFormatException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						// AppendText("connect error");
					}
					
					// 서버로 부터 받아오는 네트워크 생성 
					net = new ListenNetwork(ois,oos,socket);
					net.start();
					

					GamingView game = new GamingView(txtUserName.getName(),net);
					game.setVisible(true);
//					ChatMsg obcm = new ChatMsg(txtUserName.getText(), "100", "Login");
//					SendObject(obcm);
				}
				else {
					
				}
			} else {
				
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
	
	
}


