import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.List;

public class GameInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public int roomId;
	public int userId;
	public String code;
	public String data;
	
	public GameInfo(String code, int roomId, int userId, String data) {
		this.code=code;
		this.roomId=roomId;
		this.userId=userId;
		this.data=data;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public void setData(String data) {
		this.data = data;
	}
	

}
