import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JTextPane;

class ListenNetwork extends Thread {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket socket; // 연결소켓
	JTextPane textArea;
	LobbyFrame lobbyFrame;
	WaitRoomFrame waitRoomFrame;
	
	
	public ListenNetwork(ObjectInputStream ois, ObjectOutputStream oos, Socket socket) {
		this.ois=ois;
		this.oos = oos;
		this.socket=socket;
	}
	
	public ObjectInputStream getOIS() {
		return this.ois;
	}
	
	public ObjectOutputStream getOOS() {
		return this.oos;
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
					case "100":		// 로그인시 로비 frame 열기 
						lobbyFrame = new LobbyFrame();
						lobbyFrame.setVisible(true);
						break;
					case "101":    // 방만들기
						String[] Result101 = cm.getData().split("/"); // [0]:result, [1]:현재 방 주인
						if(Result101[0].matches("success")) {
							waitRoomFrame = new WaitRoomFrame(true); // true면 방 주인, false면 방 손님
							waitRoomFrame.setVisible(true);
							lobbyFrame.setVisible(false);
							waitRoomFrame.makeWaitingUser(0+" "+Result101[1]);
							waitRoomFrame.repaint();
						}
						else if(cm.getData().matches("fail")){
							// 최대 방 수 다이얼로그
							System.out.println("방을 더이상 만들수 없습니다.");
						}else {
							String[] RoomInfo = cm.getData().split(" "); // [0]:roomId, [1]:roomName
							lobbyFrame.makeRoom(Integer.parseInt(RoomInfo[0]), RoomInfo[1]);
							lobbyFrame.repaint();
						}
						break;
						
					case "102": // 방 들어가기
						String[] Result102 = cm.getData().split("/"); // [0]:result, [1]:현재 방 userList
						System.out.println("test1 "+cm.getData().toString());
						String UserListStr = Result102[1].substring(1, Result102[1].length() - 1);
						System.out.println("test2 "+UserListStr.toString());
						String[] UserList = UserListStr.split(", "); // userName들
						
						if(Result102[0].matches("success")) {
							lobbyFrame.setVisible(false);
							waitRoomFrame = new WaitRoomFrame(false);
							waitRoomFrame.setVisible(true);
							
							for(int i=0;i<UserList.length;i++) {
								waitRoomFrame.makeWaitingUser(i+" "+UserList[i]);
							}
							
							waitRoomFrame.repaint();
						}
						else if(Result102[0].matches("fail")){
							// 방 꽉 찼다는 다이얼로그
						}
						else if(Result102[0].matches("change")) { // 이미 방에 들어가있는 유저라면
							for(int i=0;i<UserList.length;i++) {
								waitRoomFrame.makeWaitingUser(i+" "+UserList[i]);
								System.out.println("실행:"+i+" "+UserList[i]);
							}
							waitRoomFrame.repaint();
						}
						else {
							System.out.println("102 잘못된 Result");
						}
					case "200": // chat message
						AppendText(msg);
						break;
					}
//						case "300": // Image 첨부
//							AppendText("[" + cm.getId() + "]");
//							AppendImage(cm.img);
//							break;
//						}
			} catch (IOException e) {
						AppendText("ois.readObject() error Net");
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
	
	public void setJTextPane(JTextPane textArea) {
		this.textArea=textArea;
		
	}
}