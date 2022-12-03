
// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:로그인, 101:방만들기, 200:채팅메시지, 300:Keyboard Event, 400: Mouse Event
	public String UserName;
	public String data;
	//public KeyboardEvent key_e;
	public MouseEvent mouse_e;
	
	public ChatMsg(String UserName, String code, String msg) {
		this.UserName = UserName;
		this.code = code;
		this.data = msg;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String UserName) {
		this.UserName = UserName;
	}

	public void setData(String data) {
		this.data = data;
	}

}