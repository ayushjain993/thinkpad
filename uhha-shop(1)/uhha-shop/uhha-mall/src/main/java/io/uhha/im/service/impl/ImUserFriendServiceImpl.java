package io.uhha.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.lang.Assert;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.enums.ImUserTypeEnum;
import io.uhha.common.exception.CustomException;
import io.uhha.im.domain.ImUser;
import io.uhha.im.domain.ImUserFriend;
import io.uhha.im.model.ImUserFriendsVo;
import io.uhha.im.service.ImUserService;
import io.uhha.im.service.MessageService;
import io.uhha.im.service.SessionService;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.system.service.ISysStoreUserService;
import io.uhha.system.service.ISysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.im.mapper.ImUserFriendMapper;
import io.uhha.im.service.ImUserFriendService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author uhha
 * @date 2022-01-18 21:05:05
 * @description 好友列表Service业务层
 */
@Service
public class ImUserFriendServiceImpl extends ServiceImpl<ImUserFriendMapper, ImUserFriend> implements ImUserFriendService {

    @Autowired
    private ImUserService imUserService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysStoreUserService sysStoreUserService;

    @Autowired
    private IUmsMemberService iUmsMemberService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SessionService sessionService;

    @Override
    public List<SysUser> getUserFriends(String userId) {
        return null;
    }

    @Override
    public boolean addUserFriend(String customerId, String friendId) {
        return false;
    }

    @Override
    public synchronized void bindRelationship(String sender, String receiver) {
        Assert.hasText(sender, "发送人不能为空");
        Assert.hasText(receiver, "接收人不能为空");
        ImUser senderUser = imUserService.lambdaQuery().eq(ImUser::getUserCode, sender).one();
        if (senderUser == null) {
            throw new CustomException("发送人不存在");
        }
        ImUser receiverUser = imUserService.lambdaQuery().eq(ImUser::getUserCode, receiver).one();
        if (receiverUser == null) {
            throw new CustomException("接收人不存在");
        }
        ImUserFriend senderUserFriend = lambdaQuery().eq(ImUserFriend::getUserId, sender).eq(ImUserFriend::getFriendId, receiver).one();
        ImUserFriend imUserFriend;
        if (senderUserFriend == null) {
            imUserFriend = new ImUserFriend();
            imUserFriend.setUserId(sender);
            imUserFriend.setFriendId(receiver);
            imUserFriend.setCreateTime(new Date());
            imUserFriend.setUpdateTime(new Date());
            imUserFriend.setCreateBy(sender);
            save(imUserFriend);
        }
        ImUserFriend receiverUserFriend = lambdaQuery().eq(ImUserFriend::getUserId, receiver).eq(ImUserFriend::getFriendId, sender).one();
        if (receiverUserFriend == null) {
            imUserFriend = new ImUserFriend();
            imUserFriend.setUserId(receiver);
            imUserFriend.setFriendId(sender);
            imUserFriend.setCreateTime(new Date());
            imUserFriend.setUpdateTime(new Date());
            imUserFriend.setCreateBy(sender);
            save(imUserFriend);
        }
    }

    @Override
    public List<ImUserFriendsVo> queryUserFriend(QueryWrapper<ImUserFriend> queryWrapper) {
        List<ImUserFriend> list = list(queryWrapper);
        return list.stream().map(this::tranceFormImUserFriend).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
    }

    private ImUserFriendsVo tranceFormImUserFriend(ImUserFriend imUserFriend) {
        ImUser imUser = imUserService.lambdaQuery().eq(ImUser::getUserCode, imUserFriend.getFriendId()).one();
        ImUserFriendsVo imUserFriendsVo = null;
        if (imUser != null) {
            imUserFriendsVo = new ImUserFriendsVo();
            imUserFriendsVo.setId(imUser.getUserCode());
            imUserFriendsVo.setName(imUser.getNickName());
            ImUserTypeEnum imUserType = ImUserTypeEnum.valueOf(imUser.getType());
            String img = null;
            switch (imUserType) {
                case SYSTEM:
                    SysUser sysUser = sysUserService.getUser(imUser.getUserId());
                    img = Optional.ofNullable(sysUser).map(SysUser::getAvatar).orElse(null);
                    break;
                case STORE:
                    SysUser user = sysStoreUserService.getUser(imUser.getUserId());
                    img = Optional.ofNullable(user).map(SysUser::getAvatar).orElse(null);
                    break;
                case PERSONAL:
                    UmsMember umsMember = iUmsMemberService.getUser(imUser.getUserId());
                    img = Optional.ofNullable(umsMember).map(UmsMember::getImage).orElse(null);
                    break;
            }
            boolean isOnline = sessionService.getSessionByUid(imUserFriend.getFriendId());
            imUserFriendsVo.setOnline(isOnline);
            imUserFriendsVo.setImg(img);
            imUserFriendsVo.setDept(imUserType.getValue());
            Long count = messageService.getNoReadCount(imUserFriend);
            imUserFriendsVo.setLastMessage(messageService.getLastMessage(imUserFriend));
            imUserFriendsVo.setReadNum(count);
            imUserFriendsVo.setType(0);
        }
        return imUserFriendsVo;
    }
}
