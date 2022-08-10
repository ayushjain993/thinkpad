package io.uhha.member.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.uhha.common.utils.StringUtils;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;

@Data
public class UmsWithdrawVo {
    private BigDecimal money;
    private String account;
    private String name;
    private String code;
    private String password;

    /**
     * 校验参数
     *
     * @return 校验通过返回true 否则返回false
     */
    public boolean checkParams() {
        return !(ObjectUtils.isEmpty(money) || StringUtils.isEmpty(name) || StringUtils.isEmpty(account)) && checkMoney();
    }

    /**
     * 校验金额
     *
     * @return 大于0返回true 否则返回false
     */
    @JsonIgnore
    public boolean checkMoney() {
        return new BigDecimal(0).compareTo(this.money) < 0;
    }
}
