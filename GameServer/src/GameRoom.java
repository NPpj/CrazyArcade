import java.util.ArrayList;
import java.util.List;

public class GameRoom {
	private int roomId; // Room Id
	private List<String> userList;
	private String roomOwner;
	private String roomName;
	private Boolean isFull;
	private Boolean isPlaying;

	public GameRoom(int roomId, String roomOwner, String roomName) {
		this.roomId = roomId;
		this.roomOwner = roomOwner;
		this.roomName = roomName;
		this.isFull = false;
		this.isPlaying = false;
		
		userList = new ArrayList();
		userList.add(roomOwner);
		System.out.println("룸 생성 id: "+this.roomId+" owner: "+this.roomOwner+" name: "+this.roomName);
	}
	
	public Boolean isUser(String userName) {
		if(userList.contains(userName))
			return true;
		else
			return false;
	}
	
	public Boolean isFullRoom() {
		if(isFull)
			return true;
		else
			return false;
	}
	
	public void enterUser(String user) {
		userList.add(user);
		if(userList.size() >= 4)
			isFull = true;
		else
			isFull = false;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}
	
	public int getUserListSize() {
		return userList.size();
	}
	

	public String getRoomOwner() {
		return roomOwner;
	}

	public void setRoomOwner(String roomOwner) {
		this.roomOwner = roomOwner;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	

	public Boolean getIsPlaying() {
		return isPlaying;
	}

	public void setIsPlaying(Boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
}
