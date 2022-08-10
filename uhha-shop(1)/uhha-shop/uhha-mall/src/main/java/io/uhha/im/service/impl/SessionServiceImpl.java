package io.uhha.im.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.utils.StringUtils;
import io.uhha.im.domain.Session;
import io.uhha.im.mapper.SessionMapper;
import io.uhha.im.service.SessionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, Session> implements SessionService {

    @Resource
    private SessionMapper sessionMapper;

    private final String host;

    public SessionServiceImpl() throws UnknownHostException {
        host = InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public void add(Session session) {
        session.setBindTime(System.currentTimeMillis());
        session.setHost(host);
        sessionMapper.insert(session);
    }

    @Override
    public void delete(String uid, String nid) {
        sessionMapper.delete(uid, nid);
    }

    @Override
    public void deleteLocalhost() {
        sessionMapper.deleteAll(host);
    }

    @Override
    public void updateState(String uid, String nid, int state) {
        sessionMapper.updateState(uid, nid, state);
    }

    @Override
    public List<Session> findAll() {
        return lambdaQuery().list();
    }

    @Override
    public Session getSessionByUidAndDeviceId(String uid, String deviceId) {
        return lambdaQuery().eq(Session::getDeviceId, deviceId)
                .eq(Session::getUid, uid).one();
    }

    @Override
    public boolean getSessionByUid(String friendId) {
        if (StringUtils.isBlank(friendId)) {
            return false;
        }
        return lambdaQuery().eq(Session::getUid, friendId).count() > 0;
    }
}
