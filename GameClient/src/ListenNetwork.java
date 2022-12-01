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
	
	private int roomNum = 1;
	
	
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
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.replaceSelection(msg + "\n");
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
						lobbyFrame = new LobbyFrame(cm.getUserName(),this);
						lobbyFrame.setVisible(true);
						break;
					case "101":    // 방만들기
						String[] RoomInfo = cm.getData().split(" ");
						lobbyFrame.makeRoom(roomNum, RoomInfo[0]);
						lobbyFrame.repaint();
						roomNum += 1;
						break;
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
//						AppendText("ois.readObject() error");
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