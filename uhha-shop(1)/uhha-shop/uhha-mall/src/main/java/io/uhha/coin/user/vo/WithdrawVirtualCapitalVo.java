package io.uhha.coin.user.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class WithdrawVirtualCapitalVo {
    @NotNull
    private Integer addressid;
    @NotNull
    private BigDecimal withdrawAmount;
    @NotNull
    private String tradePwd;
    private String googleCode;
    private String phoneCode;
    @NotNull
    Integer coinid;
    /**
     * 如果是btc则需要选择btc的费率，默认为0
     */
    Integer btcfees = 0;
}
