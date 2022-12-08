//JavaObjServer.java ObjectStream 기반 채팅 Server

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class JavaGameServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private Vector UserNameVec = new Vector();
	private Vector RoomVec = new Vector(); // 연결된 방 저장할 벡터
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaGameServer frame = new JavaGameServer();
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
	public JavaGameServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		//서버 시작 버튼 
		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Game Server Running..");
				btnServerStart.setText("Game Server Running..");
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("사용자로부터 들어온 object : " + str+"\n");
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.UserName + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User 당 생성되는 Thread
	// Read One 에서 대기 -> Write All
	class UserService extends Thread {

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		private Vector user_name_vc;
		public String UserName = "";
		public String UserStatus;

		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// 매개변수로 넘어온 자료 저장
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			this.user_name_vc = UserNameVec;
			try {

				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());
				
			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public void Logout() {
			String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
			UserNameVec.remove(UserName);
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			WriteAll("200", msg); // 나를 제외한 다른 User들에게 전송
		}

		// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteAll(String port, String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOne(port, str);
			}
		}
		
		// 원하는 User들에게 방송.
		public void WriteSome(String port, String str, List<String> userList) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				// 게임룸 들어와있는 유저에게만 방송
				if (userList.contains(user_name_vc.elementAt(i))) {
					user.WriteOne(port, str);
				}
			}
		}
		
		// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteOthers(String port, String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.UserStatus == "O")
					user.WriteOne(port, str);
			}
		}

		
		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}
		
		// 원하는 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public void WriteSomeObject(Object ob, List<String> userList) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				// 나를 제외한 게임룸 들어와있는 유저에게만 방송
				if (userList.contains(user_name_vc.elementAt(i))) {
					user.WriteOneObject(ob);
				}
			}
		}

		
		// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
		public byte[] MakePacket(String msg) {
			byte[] packet = new byte[BUF_LEN];
			byte[] bb = null;
			int i;
			for (i = 0; i < BUF_LEN; i++)
				packet[i] = 0;
			try {
				bb = msg.getBytes("euc-kr");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}
	
		// UserService Thread가 담당하는 Client 에게 1:1 전송
		public void WriteOne(String port, String msg) {
			try {
				ChatMsg obcm = new ChatMsg("SERVER", port, msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}

		// 귓속말 전송
		public void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("귓속말", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}
		
		public void WriteOneObject(Object ob) {
			try {
			    oos.writeObject(ob);
			} 
			catch (IOException e) {
				AppendText("oos.writeObject(ob) error in WriteOneOject");
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}
		
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					GameInfo gi = null;
					MonsterInfo mi = null;
					GameRoom gameRoom = null;
					if (socket == null)
						break;
					
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						AppendText("ois.readObject() error0");
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
//						AppendObject(cm);
					// 로그인 
					if (cm.code.matches("100")) {  
						UserName = cm.getUserName();
						UserStatus = "O"; // Online 상태
						if(UserNameVec.contains(UserName)) {
							AppendText(UserName + "은 이미 로그인 중인 유저입니다.");
						}else {
							UserNameVec.add(UserName);
							AppendText("새로운 참가자 " + UserName + " 입장.");
							WriteOneObject(cm);
							if(RoomManager.getRoomListSize()>0) {
								int roomListSize = RoomManager.getRoomListSize();
								for(int i=roomListSize; i>0; i--) {
									String idStr = String.valueOf(i);
									gameRoom = RoomManager.getGameRoom(idStr);
									WriteOne("101", gameRoom.getRoomId()+" "+gameRoom.getRoomName()); // 방의 모든 정보 로그인 한 유저에게 전송
								}
							}
						}
						
					}
					// 방만들기
					else if(cm.code.matches("101")) {    
						String[] RoomInfo = cm.getData().split(" ");
						gameRoom = RoomManager.createRoom(cm.getUserName(), RoomInfo[0]); // 특정 유저가 방 만드려고 함.
						AppendText("[방"+gameRoom.getRoomId()+"생성] 방장: " + cm.getUserName() +", 방제목: "+ RoomInfo[0]);
						WriteOne("101", "success/"+gameRoom.getRoomId()+"/"+cm.getUserName()); // 방 만든 유저에게 전송
						WriteOthers("101", gameRoom.getRoomId()+" "+gameRoom.getRoomName()); // 방 정보 모든 유저들에게 전송
					}
					// 방 들어가기
					else if(cm.code.matches("102")){ 
						String[] RoomInfo = cm.getData().split(" "); // 0:방 id
						gameRoom = RoomManager.getGameRoom(RoomInfo[0]);
						// 다 찬 방이면 입장 불가
						if(!gameRoom.isFullRoom()) {
							gameRoom.enterUser(cm.getUserName());
							AppendText("[방"+gameRoom.getRoomId()+" 입장] 방제목: " + gameRoom.getRoomName() +", 유저: "+ cm.getUserName());
							AppendText("  방"+gameRoom.getRoomId()+" 현재 유저: " + gameRoom.getUserList());
							WriteOne("102", "success/"+gameRoom.getRoomId()+"/"+gameRoom.getUserList()); // 들어간 플레이어는 새 창
							WriteSome("102", "change/"+gameRoom.getRoomId()+"/"+gameRoom.getUserList(),gameRoom.getUserList()); // 기존 플레이어는 repaint
						}
						else {
							AppendText("[방"+gameRoom.getRoomId()+" 입장 실패]");
							WriteOne("102", "fail");
						}
					}
					// 채팅메시지 
					else if(cm.code.matches("200")){ 
//						msg = String.format("[%s] %s", cm.getUserName(), cm.getData());
						int roomNum = RoomManager.whereInUser(cm.getUserName());
						if(roomNum != -1) {
							gameRoom = RoomManager.getGameRoom(String.valueOf(roomNum));
							//ChatMsg _cm = new ChatMsg(cm.getUserName(), "200", cm.getData(), gameRoom.getUserList());
							WriteSomeObject(cm, gameRoom.getUserList());
						}
					}
					// 게임 시작하기
					else if(cm.code.matches("300")) {
						String roomId = cm.getData();
						gameRoom = RoomManager.getGameRoom(cm.getData());
						gameRoom.setIsPlaying(true);
						WriteSome("300", roomId+"/"+gameRoom.getUserList(), gameRoom.getUserList());
			
					}
					else if (cm.code.matches("500")) { // logout message 처리
						Logout();
						break;
					} else { // 300, 500, ... 기타 object는 모두 방송한다.
						WriteAllObject(cm);
					} 
					
				
					
					/* ---------- 게임 정보 객체 ------------ */
					} else if(obcm instanceof GameInfo) {
						gi = (GameInfo) obcm;
						if(gi.code.matches("400")) {// 플레이어 움직임  
							int roomId = gi.getRoomId();
							int userId = gi.getUserId();
							String data = gi.getData();
							WriteSomeObject(gi,RoomManager.getGameRoom(String.valueOf(roomId)).getUserList());
						}
						else if(gi.code.matches("401")) {// 플레이어 물풍선 놓기
							int roomId = gi.getRoomId();
							int userId = gi.getUserId();
							String data = gi.getData();
							WriteSomeObject(gi,RoomManager.getGameRoom(String.valueOf(roomId)).getUserList());
						}else if(gi.code.matches("402")) {// 플레이어 아이템 먹기 
							int roomId = gi.getRoomId();
							int userId = gi.getUserId();
							String data = gi.getData();
							AppendText(data);
							WriteSomeObject(gi,RoomManager.getGameRoom(String.valueOf(roomId)).getUserList());
						}else if(gi.code.matches("403")) { // 물풍선에 의한 블럭 깨지기
							int roomId = gi.getRoomId();
							int userId = gi.getUserId();
							String data = gi.getData();
							WriteSomeObject(gi,RoomManager.getGameRoom(String.valueOf(roomId)).getUserList());

						}else if(gi.code.matches("404")) {// 죽기
							int roomId = gi.getRoomId();
							int userId = gi.getUserId();
							String data = gi.getData();
							WriteSomeObject(gi,RoomManager.getGameRoom(String.valueOf(roomId)).getUserList());
						}
					}else
						continue;
					
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					if(e.getCause() != null)
						AppendText(e.getCause().toString());
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
			} // while
		} // run
	}

}
