import java.io.Serializable;

public class GameUser{
	private String id;
	private ListenNetwork net;
	
	private GameUser() {}
	
	public void init(String id, ListenNetwork net) {
		this.id=id;
		this.net=net;
	}
	
	// holder에 의한 초기화
	private static class LazyHolder{
		public static final GameUser uniqueInstance = new GameUser();
	}
	
	public static GameUser getInstance() {
		return LazyHolder.uniqueInstance;
	}

	public String getId() {
		return id;
	}
	
	public ListenNetwork getNet() {
		return net;
	}
}
