package  io.uhha.im.server.model;


import io.uhha.common.im.constant.IMConstant;

import java.io.Serializable;

/**
 * 服务端心跳请求
 */
public class Ping implements Serializable, Transportable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = "PING";
	private static final String DATA = "PING";
	private static final Ping INSTANCE = new Ping();

	private Ping() {

	}

	public static Ping getInstance() {
		return INSTANCE;
	}

	@Override
	public String toString() {
		return TAG;
	}

	@Override
	public byte[] getBody() {
		return DATA.getBytes();
	}

	@Override
	public byte getType() {
		return IMConstant.DATA_TYPE_PING;
	}

}
