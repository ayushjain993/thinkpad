package  io.uhha.im.server.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * java |android 客户端请求结构
 *
 */
public class SentBody implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;

	private final HashMap<String, String> data = new HashMap<>();

	private long timestamp;

	public String getKey() {
		return key;
	}

	public String get(String k) {
		return data.get(k);
	}

	public Integer getInteger(String k) {
		return data.containsKey(k) ? Integer.parseInt(data.get(k)) : null;
	}

	public Long getLong(String k) {
		return data.containsKey(k) ? Long.parseLong(data.get(k)) : null;
	}

	public Double getDouble(String k) {
		return data.containsKey(k) ? Double.parseDouble(data.get(k)) : null;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void remove(String k) {
		data.remove(k);
	}

	public void put(String k, String v) {
		if (v != null && k != null) {
			data.put(k, v);
		}
	}

	public void putAll(Map<String, String> map) {
		data.putAll(map);
	}

	public Set<String> getKeySet() {
		return data.keySet();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[SentBody]").append("\n");
		buffer.append("key:").append(key).append("\n");
		buffer.append("timestamp:").append(timestamp).append("\n");

		buffer.append("data:{");
		data.forEach((k, v) -> buffer.append("\n").append(k).append(":").append(v));
		buffer.append("\n}");

		return buffer.toString();
	}

}
