package io.uhha.im.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.im.domain.Session;

import java.util.List;

/**
 * 存储连接信息，便于查看用户的链接信息
 */
public interface SessionService extends IService<Session> {

	void add(Session session);

	void delete(String uid, String nid);

	/**
	 * 删除本机的连接记录
	 */
	void deleteLocalhost();

	void updateState(String uid, String nid, int state);

	List<Session> findAll();

	Session getSessionByUidAndDeviceId(String uid, String deviceId);

    boolean getSessionByUid(String friendId);
}
