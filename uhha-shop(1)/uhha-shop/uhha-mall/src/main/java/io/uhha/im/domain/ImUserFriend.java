package io.uhha.im.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.uhha.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author uhha
 * @date 2022-01-18 21:05:05
 *
 * @description 好友列表对象 ImUserFriend
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("im_user_friends")
public class ImUserFriend extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableId()
    private String userId;

    /** 好友ID */
    private String friendId;


}
