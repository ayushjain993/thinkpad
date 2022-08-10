package io.uhha.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.enums.ImUserTypeEnum;
import io.uhha.common.exception.CustomException;
import io.uhha.common.utils.uuid.IdUtils;
import io.uhha.im.domain.ImUser;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.system.service.ISysStoreUserService;
import io.uhha.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.im.mapper.ImUserMapper;
import io.uhha.im.service.ImUserService;

import java.util.Date;
import java.util.Optional;

/**
 * @author uhha
 * @date 2022-01-18 21:02:36
 * @description im用户Service业务层
 */
@Service
public class ImUserServiceImpl extends ServiceImpl<ImUserMapper, ImUser> implements ImUserService {
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysStoreUserService sysStoreUserService;

    @Autowired
    private IUmsMemberService iUmsMemberService;

    @Override
    public ImUser queryImUserInfo(ImUserTypeEnum type, String userId) {
        ImUser imUser = lambdaQuery().eq(ImUser::getType, type).eq(ImUser::getUserId, userId).one();
        String img = null;
        String nickName = null;
        SysUser sysUser;
        switch (type) {
            case SYSTEM:
                sysUser = sysUserService.getUser(Long.valueOf(userId));
                if (sysUser == null) {
                    throw new CustomException("用户不存在");
                }
                img = sysUser.getAvatar();
                nickName = sysUser.getNickName();
                break;
            case STORE:
                sysUser = sysStoreUserService.getUser(Long.valueOf(userId));
                if (sysUser == null) {
                    throw new CustomException("用户不存在");
                }
                img = sysUser.getAvatar();
                nickName = sysUser.getNickName();
                break;
            case PERSONAL:
                UmsMember umsMember = iUmsMemberService.getUser(Long.valueOf(userId));
                if (umsMember == null) {
                    throw new CustomException("用户不存在");
                }
                img = umsMember.getImage();
                nickName = umsMember.getNickname();
                break;
            default:
                break;
        }
        if (imUser == null) {
            imUser = new ImUser();
            imUser.setUserCode(IdUtils.fastSimpleUUID());
            imUser.setUserId(Long.valueOf(userId));
            imUser.setType(type.name());
            imUser.setNickName(nickName);
            imUser.setCreateBy(nickName);
            imUser.setCreateTime(new Date());
            imUser.setUpdateTime(new Date());
            save(imUser);
        }
        imUser.setImg(img);
        return imUser;
    }

    @Override
    public ImUser queryImUserInfo(ImUserTypeEnum type, Long userId){
        ImUser imUser = lambdaQuery().eq(ImUser::getType, type).eq(ImUser::getUserId, userId).one();
        return imUser;
    }

    @Override
    public boolean checkExist(ImUserTypeEnum type, Long id) {
        Long count = lambdaQuery().eq(ImUser::getType, type).eq(ImUser::getUserId, id).count();
        return count > 0;
    }

    @Override
    public ImUser queryImUserInfoByUserCode(String userCode) {
        ImUser imUser = lambdaQuery().eq(ImUser::getUserCode, userCode).one();
        if(imUser == null){
            throw new CustomException("聊天用户不存在");
        }
        String img = null;
        SysUser sysUser;
        ImUserTypeEnum imUserTypeEnum = ImUserTypeEnum.valueOf(imUser.getType());
        if(imUserTypeEnum == null){
            throw new CustomException("聊天用户类型不存在");
        }
        switch (imUserTypeEnum) {
            case SYSTEM:
                sysUser = sysUserService.getUser(Long.valueOf(imUser.getUserId()));
                if (sysUser == null) {
                    throw new CustomException("用户不存在");
                }
                img = sysUser.getAvatar();
                break;
            case STORE:
                sysUser = sysStoreUserService.getUser(Long.valueOf(imUser.getUserId()));
                if (sysUser == null) {
                    throw new CustomException("用户不存在");
                }
                img = sysUser.getAvatar();
                break;
            case PERSONAL:
                UmsMember umsMember = iUmsMemberService.getUser(Long.valueOf(imUser.getUserId()));
                if (umsMember == null) {
                    throw new CustomException("用户不存在");
                }
                img = umsMember.getImage();
                break;
            default:
                break;
        }
        imUser.setImg(img);
        return imUser;
    }
}
