package  io.uhha.im.server.model;

import java.io.Serializable;

/**
 * 客户端心跳响应
 */
public class Pong implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = "PONG";
	private static final Pong INSTANCE = new Pong();

	private Pong() {
	}

	public static Pong getInstance() {
		return INSTANCE;
	}

	@Override
	public String toString() {
		return TAG;
	}

}
