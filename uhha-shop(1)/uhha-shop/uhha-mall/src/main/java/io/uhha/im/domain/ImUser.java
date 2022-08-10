package io.uhha.im.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.uhha.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author uhha
 * @date 2022-01-18 21:02:36
 *
 * @description im用户对象 ImUser
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("im_user")
public class ImUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId
    private Long id;

    /** 昵称 */
    private String nickName;

    /** IM用户code */
    private String userCode;

    /**
     * UmsMember对应的id
     */
    private Long userId;

    /** 类型 */
    private String type;

    @TableField(exist = false)
    private String img;

}
