package io.uhha.coin.user.service;

import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.system.domain.SystemCoinType;

import java.math.BigDecimal;

public interface UserwalletRechargeService {
	void txSuccess(FVirtualCapitalOperation fvirtualcaptualoperation, SystemCoinType coinType, BigDecimal amount) throws Exception;
}
