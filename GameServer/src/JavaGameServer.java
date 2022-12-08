//JavaObjServer.java ObjectStream ��� ä�� Server

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

	private ServerSocket socket; // ��������
	private Socket client_socket; // accept() ���� ������ client ����
	private Vector UserVec = new Vector(); // ����� ����ڸ� ������ ����
	private Vector UserNameVec = new Vector();
	private Vector RoomVec = new Vector(); // ����� �� ������ ����
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����

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

		//���� ���� ��ư 
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
				btnServerStart.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
				txtPortNumber.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// ���ο� ������ accept() �ϰ� user thread�� ���� �����Ѵ�.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept�� �Ͼ�� �������� ���� �����
					AppendText("���ο� ������ from " + client_socket);
					// User �� �ϳ��� Thread ����
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // ���ο� ������ �迭�� �߰�
					new_user.start(); // ���� ��ü�� ������ ����
					AppendText("���� ������ �� " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("����ڷκ��� ���� �޼��� : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("����ڷκ��� ���� object : " + str+"\n");
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.UserName + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User �� �����Ǵ� Thread
	// Read One ���� ��� -> Write All
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
			// �Ű������� �Ѿ�� �ڷ� ����
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
			String msg = "[" + UserName + "]���� ���� �Ͽ����ϴ�.\n";
			UserNameVec.remove(UserName);
			UserVec.removeElement(this); // Logout�� ���� ��ü�� ���Ϳ��� �����
			WriteAll("200", msg); // ���� ������ �ٸ� User�鿡�� ����
		}

		// ��� User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
		public void WriteAll(String port, String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOne(port, str);
			}
		}
		
		// ���ϴ� User�鿡�� ���.
		public void WriteSome(String port, String str, List<String> userList) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				// ���ӷ� �����ִ� �������Ը� ���
				if (userList.contains(user_name_vc.elementAt(i))) {
					user.WriteOne(port, str);
				}
			}
		}
		
		// ���� ������ User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
		public void WriteOthers(String port, String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.UserStatus == "O")
					user.WriteOne(port, str);
			}
		}

		
		// ��� User�鿡�� Object�� ���. ä�� message�� image object�� ���� �� �ִ�
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}
		
		// ���ϴ� User�鿡�� Object�� ���. ä�� message�� image object�� ���� �� �ִ�
		public void WriteSomeObject(Object ob, List<String> userList) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				// ���� ������ ���ӷ� �����ִ� �������Ը� ���
				if (userList.contains(user_name_vc.elementAt(i))) {
					user.WriteOneObject(ob);
				}
			}
		}

		
		// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
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
	
		// UserService Thread�� ����ϴ� Client ���� 1:1 ����
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
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
			}
		}

		// �ӼӸ� ����
		public void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("�ӼӸ�", "200", msg);
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
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
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
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
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
					// �α��� 
					if (cm.code.matches("100")) {  
						UserName = cm.getUserName();
						UserStatus = "O"; // Online ����
						if(UserNameVec.contains(UserName)) {
							AppendText(UserName + "�� �̹� �α��� ���� �����Դϴ�.");
						}else {
							UserNameVec.add(UserName);
							AppendText("���ο� ������ " + UserName + " ����.");
							WriteOneObject(cm);
							if(RoomManager.getRoomListSize()>0) {
								int roomListSize = RoomManager.getRoomListSize();
								for(int i=roomListSize; i>0; i--) {
									String idStr = String.valueOf(i);
									gameRoom = RoomManager.getGameRoom(idStr);
									WriteOne("101", gameRoom.getRoomId()+" "+gameRoom.getRoomName()); // ���� ��� ���� �α��� �� �������� ����
								}
							}
						}
						
					}
					// �游���
					else if(cm.code.matches("101")) {    
						String[] RoomInfo = cm.getData().split(" ");
						gameRoom = RoomManager.createRoom(cm.getUserName(), RoomInfo[0]); // Ư�� ������ �� ������� ��.
						AppendText("[��"+gameRoom.getRoomId()+"����] ����: " + cm.getUserName() +", ������: "+ RoomInfo[0]);
						WriteOne("101", "success/"+gameRoom.getRoomId()+"/"+cm.getUserName()); // �� ���� �������� ����
						WriteOthers("101", gameRoom.getRoomId()+" "+gameRoom.getRoomName()); // �� ���� ��� �����鿡�� ����
					}
					// �� ����
					else if(cm.code.matches("102")){ 
						String[] RoomInfo = cm.getData().split(" "); // 0:�� id
						gameRoom = RoomManager.getGameRoom(RoomInfo[0]);
						// �� �� ���̸� ���� �Ұ�
						if(!gameRoom.isFullRoom()) {
							gameRoom.enterUser(cm.getUserName());
							AppendText("[��"+gameRoom.getRoomId()+" ����] ������: " + gameRoom.getRoomName() +", ����: "+ cm.getUserName());
							AppendText("  ��"+gameRoom.getRoomId()+" ���� ����: " + gameRoom.getUserList());
							WriteOne("102", "success/"+gameRoom.getRoomId()+"/"+gameRoom.getUserList()); // �� �÷��̾�� �� â
							WriteSome("102", "change/"+gameRoom.getRoomId()+"/"+gameRoom.getUserList(),gameRoom.getUserList()); // ���� �÷��̾�� repaint
						}
						else {
							AppendText("[��"+gameRoom.getRoomId()+" ���� ����]");
							WriteOne("102", "fail");
						}
					}
					// ä�ø޽��� 
					else if(cm.code.matches("200")){ 
//						msg = String.format("[%s] %s", cm.getUserName(), cm.getData());
						int roomNum = RoomManager.whereInUser(cm.getUserName());
						if(roomNum != -1) {
							gameRoom = RoomManager.getGameRoom(String.valueOf(roomNum));
							//ChatMsg _cm = new ChatMsg(cm.getUserName(), "200", cm.getData(), gameRoom.getUserList());
							WriteSomeObject(cm, gameRoom.getUserList());
						}
					}
					// ���� �����ϱ�
					else if(cm.code.matches("300")) {
						String roomId = cm.getData();
						gameRoom = RoomManager.getGameRoom(cm.getData());
						gameRoom.setIsPlaying(true);
						WriteSome("300", roomId+"/"+gameRoom.getUserList(), gameRoom.getUserList());
			
					}
					else if (cm.code.matches("500")) { // logout message ó��
						Logout();
						break;
					} else { // 300, 500, ... ��Ÿ object�� ��� ����Ѵ�.
						WriteAllObject(cm);
					} 
					
				
					
					/* ---------- ���� ���� ��ü ------------ */
					} else if(obcm instanceof GameInfo) {
						gi = (GameInfo) obcm;
						if(gi.code.matches("400")) {// �÷��̾� ������  
							int roomId = gi.getRoomId();
							int userId = gi.getUserId();
							String data = gi.getData();
							WriteSomeObject(gi,RoomManager.getGameRoom(String.valueOf(roomId)).getUserList());
						}
						else if(gi.code.matches("401")) {// �÷��̾� ��ǳ�� ����
							int roomId = gi.getRoomId();
							int userId = gi.getUserId();
							String data = gi.getData();
							WriteSomeObject(gi,RoomManager.getGameRoom(String.valueOf(roomId)).getUserList());
						}else if(gi.code.matches("402")) {// �÷��̾� ������ �Ա� 
							int roomId = gi.getRoomId();
							int userId = gi.getUserId();
							String data = gi.getData();
							AppendText(data);
							WriteSomeObject(gi,RoomManager.getGameRoom(String.valueOf(roomId)).getUserList());
						}else if(gi.code.matches("403")) { // ��ǳ���� ���� �� ������
							int roomId = gi.getRoomId();
							int userId = gi.getUserId();
							String data = gi.getData();
							WriteSomeObject(gi,RoomManager.getGameRoom(String.valueOf(roomId)).getUserList());

						}else if(gi.code.matches("404")) {// �ױ�
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
						Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
			} // while
		} // run
	}

}
