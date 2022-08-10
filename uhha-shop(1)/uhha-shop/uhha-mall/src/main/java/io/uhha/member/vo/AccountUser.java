package io.uhha.member.vo;

import lombok.Data;

/**
 * 会员对象 ums_member
 *
 * @author uhha
 * @date 2020-07-25
 */
@Data
public class AccountUser {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

}
