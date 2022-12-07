import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JTextPane;

class ListenNetwork extends Thread {
	GameUser user = GameUser.getInstance();
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket socket; // 연결소켓
	JTextPane textArea;
	LobbyFrame lobbyFrame;
	WaitRoomFrame waitRoomFrame;
	GamingView gamingView;

	public ListenNetwork(ObjectInputStream ois, ObjectOutputStream oos, Socket socket) {
		this.ois = ois;
		this.oos = oos;
		this.socket = socket;
	}

	public ObjectInputStream getOIS() {
		return this.ois;
	}

	public ObjectOutputStream getOOS() {
		return this.oos;
	}

	public void PlayerMove(String data,int userIndex) {
		switch(data) {
		case "pressedKeyUp":
			GamingView.playerList.get(userIndex).moveToUp();
			GamingView.playerList.get(userIndex).playerMove = true;
			break;
		case "pressedKeyDown":
			GamingView.playerList.get(userIndex).moveToDown();
			GamingView.playerList.get(userIndex).playerMove = true;
			break;
		case "pressedKeyLeft":
			GamingView.playerList.get(userIndex).moveToLeft();
			GamingView.playerList.get(userIndex).playerMove = true;
			break;
		case "pressedKeyRight":
			GamingView.playerList.get(userIndex).moveToRight();
			GamingView.playerList.get(userIndex).playerMove = true;
			break;
		}
	}

	public void PlayerEatItem(String n, String i, int userIndex) {
		switch(n) {
		case "1":
			GamingView.playerList.get(userIndex).addMaxBubbleNum();
			Stage.itemList.remove(i);
			break;
		case "2":
			Stage.itemList.remove(i);
			break;
		case "3":
			if (GamingView.playerList.get(userIndex).PLAYER_MOVE < 8)
				GamingView.playerList.get(userIndex).PLAYER_MOVE += 2;
			Stage.itemList.remove(i);
			break;
		}
	}
	public void run() {
		while (true) {
			try {

				Object obcm = null;
				String msg = null;
				ChatMsg cm;
				GameInfo gi = null;
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
				

				// code
				switch (cm.getCode()) {
				case "100": // 로그인시 로비 frame 열기
					lobbyFrame = new LobbyFrame();
					lobbyFrame.setVisible(true);
					break;
				case "101": // 방만들기
					String[] Result101 = cm.getData().split("/"); // [0]:result, [1]:방 id [2]: 현재 방 주인
					if (Result101[0].matches("success")) {
						waitRoomFrame = new WaitRoomFrame(Integer.parseInt(Result101[1]), true); // 방 id와 방장여부 넘겨줌. true면 방 주인, false면 방 손님
						waitRoomFrame.setVisible(true);
						lobbyFrame.setVisible(false);
						waitRoomFrame.makeWaitingUser(0 + " " + Result101[2]);
						waitRoomFrame.repaint();
					}
					else if (cm.getData().matches("fail")) {
						// 최대 방 수 다이얼로그
						System.out.println("방을 더이상 만들수 없습니다.");
					}
					else {
						String[] RoomInfo = cm.getData().split(" "); // [0]:roomId, [1]:roomName
						lobbyFrame.makeRoom(Integer.parseInt(RoomInfo[0]), RoomInfo[1]);
						lobbyFrame.repaint();
					}
					break;

				case "102": // 방 들어가기
					String[] Result102 = cm.getData().split("/"); // [0]:result, [1]:방 id, [2]:현재 방 userList
					String UserListStr = Result102[2].substring(1, Result102[2].length() - 1);
					String[] UserList = UserListStr.split(", "); // userName들

					if (Result102[0].matches("success")) {
						lobbyFrame.setVisible(false);
						waitRoomFrame = new WaitRoomFrame(Integer.parseInt(Result102[1]), false); // 방 id와 userList, 방장인지 여부
						waitRoomFrame.setVisible(true);

						for (int i = 0; i < UserList.length; i++) {
							waitRoomFrame.makeWaitingUser(i + " " + UserList[i]); // ex. 0번째 유저, 0번째 유저 이름
						}

						waitRoomFrame.repaint();
					} else if (Result102[0].matches("fail")) {
						// 방 꽉 찼다는 다이얼로그
					} else if (Result102[0].matches("change")) { // 이미 방에 들어가있는 유저라면
						for (int i = 0; i < UserList.length; i++) {
							waitRoomFrame.makeWaitingUser(i + " " + UserList[i]);
						}
						waitRoomFrame.repaint();
					} else {
						System.out.println("102 잘못된 Result");
					}
				case "200": // chat message
					waitRoomFrame.AppendText(msg);
					break;
					
				case "300": // 게임시작
					String[] Result300 = cm.getData().split("/"); // [0]:roomId [1]:현재 방 userList
					String UserListStr300 = Result300[1].substring(1, Result300[1].length() - 1);
					String[] UserList300 = UserListStr300.split(", "); // userName들
					int userIndex = Arrays.asList(UserList300).indexOf(user.getId()); // 자기가 방에 몇번째로 들어온 user인지
					
					// 게임 시작 
					gamingView = new GamingView(Integer.parseInt(Result300[0]), userIndex, UserList300.length);
					gamingView.setVisible(true);
					waitRoomFrame.setVisible(false);
					
//					for (int i = 0; i < UserList300.length; i++) {
//						gamingView.makeGamingUser(i + " " + UserList300[i]);
//					}
					
					gamingView.repaint();
					break;
					}
				} else if(obcm instanceof GameInfo) {
					gi = (GameInfo) obcm;
					if(gi.code.matches("400")) {// 플레이어 움직임  
						int roomId = gi.getRoomId();
						int userId = gi.getUserId();
						String data = gi.getData();
						PlayerMove(data,userId);
					}else if(gi.code.matches("401")) {// 플레이어 물풍선 놓기  
						int roomId = gi.getRoomId();
						int userId = gi.getUserId();
						String data = gi.getData();
						GamingView.Bubble_XY.add(data);
					}else if(gi.code.matches("402")) {//플레이어 아이템 먹기 
						int roomId = gi.getRoomId();
						int userId = gi.getUserId();
						String data = gi.getData();
						String[] d = data.split(",");
						PlayerEatItem(d[0],d[1],userId);
					}
				}
				else
					continue;
			} catch (IOException e) {
				System.out.println("ois.readObject() error Net");
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
		this.textArea = textArea;

	}
}