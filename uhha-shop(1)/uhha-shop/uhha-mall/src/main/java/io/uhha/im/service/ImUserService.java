package io.uhha.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.common.enums.ImUserTypeEnum;
import io.uhha.im.domain.ImUser;

/**
 * @author uhha
 * @date 2022-01-18 21:02:36
 * @description im用户Service接口
 */
public interface ImUserService extends IService<ImUser> {
    /**
     * 查询im用户
     *
     * @param type
     * @param userId
     * @return
     */
    ImUser queryImUserInfo(ImUserTypeEnum type, String userId);

    boolean checkExist(ImUserTypeEnum type, Long id);

    /**
     * 通过UmsUser的id和用户类型查询及时通讯用户
     * @param type
     * @param id
     * @return
     */
    ImUser queryImUserInfo(ImUserTypeEnum type, Long id);

    ImUser queryImUserInfoByUserCode(String userCode);
}
