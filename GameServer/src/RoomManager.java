import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomManager {
	private static List<GameRoom> roomList;
	private static AtomicInteger atomicInteger;
	static {
		roomList = new ArrayList<GameRoom>();
		atomicInteger = new AtomicInteger();
	}
	public RoomManager() {}
	
	public static GameRoom createRoom(String owner, String roomName) {
		int roomId = atomicInteger.incrementAndGet();
		
		GameRoom room = new GameRoom(roomId, owner, roomName);
		
		roomList.add(room);
		System.out.println("Room Created : "+roomId);
		return room;
	}
	
	public static GameRoom getGameRoom(String roomIdStr) {
		int roomId = Integer.parseInt(roomIdStr);
		return roomList.get(roomId-1);
	}
	
	public static int getRoomListSize() {
		return roomList.size();
	}
}
