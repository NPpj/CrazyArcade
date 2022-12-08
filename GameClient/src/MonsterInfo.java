import java.io.Serializable;

public class MonsterInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public int roomId;
	public int userId;
	public String code;
	public Monster boss;
	
	public MonsterInfo(String code, int roomId, Monster boss) {
		this.code=code;
		this.roomId=roomId;
//		this.userId=userId;
		this.boss=boss;
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

	public Monster getBoss() {
		return boss;
	}

	public void setBoss(Monster boss) {
		this.boss = boss;
	}


	

}
