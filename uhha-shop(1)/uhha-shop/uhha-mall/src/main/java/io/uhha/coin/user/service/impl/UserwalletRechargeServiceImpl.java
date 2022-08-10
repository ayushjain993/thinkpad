package io.uhha.coin.user.service.impl;

import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.common.Enum.LogUserActionEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationInStatusEnum;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.common.exception.BCException;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.coin.common.util.MQSend;
import io.uhha.coin.capital.mapper.FVirtualCapitalOperationMapper;
import io.uhha.coin.user.mapper.UserCoinWalletMapper;
import io.uhha.coin.user.service.UserwalletRechargeService;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.common.notify.ValidateNotifyHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author bingo
 */
@Service("userwalletRechargeService")
@Slf4j
public class UserwalletRechargeServiceImpl implements UserwalletRechargeService {
	@Autowired
	private UserCoinWalletMapper userCoinWalletMapper;
	@Autowired
	private MQSend mqSend;
	@Autowired
	private RedisCryptoHelper redisCryptoHelper;
//	@Autowired
//	private FUserMapper userMapper;
	@Autowired
	private ValidateNotifyHelper validateNotifyHelper;
	@Autowired
	private FVirtualCapitalOperationMapper fVirtualCapitalOperationMapper;

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void txSuccess(FVirtualCapitalOperation fvirtualcaptualoperation, SystemCoinType coinType, BigDecimal amount) throws Exception {
		log.debug("txSuccess....");

		fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.SUCCESS);
		if(fvirtualcaptualoperation.getFid()!=null) {
			if (this.fVirtualCapitalOperationMapper.updateByPrimaryKey(fvirtualcaptualoperation) <= 0) {
				log.debug("txSuccess, fail to update VirtualCapitalOperation ....");
				throw new Exception();
			}
		}else {
			if(fVirtualCapitalOperationMapper.insert(fvirtualcaptualoperation)<=0){
				log.debug("txSuccess, fail to update VirtualCapitalOperation ....");
				throw new Exception();
			}
		}

		log.debug("txSuccess, update wallet ....");
		// 更新钱包
		UserCoinWallet userCoinWallet = userCoinWalletMapper.selectByUidAndCoinLock(fvirtualcaptualoperation.getFuid(), fvirtualcaptualoperation.getFcoinid());
		if (userCoinWallet == null) {
			log.error("No wallet found for userId={},coinId={}",fvirtualcaptualoperation.getFcoinid(),fvirtualcaptualoperation.getFcoinid());
			throw new BCException();
		}
		userCoinWallet.setTotal(MathUtils.add(userCoinWallet.getTotal(), fvirtualcaptualoperation.getFamount()));
		if (this.userCoinWalletMapper.updateByPrimaryKey(userCoinWallet) <= 0) {
			log.error("Update user wallet failed! userId={}, type={}, amount={}",fvirtualcaptualoperation.getFuid(), fvirtualcaptualoperation.getFcoinid(), fvirtualcaptualoperation.getFamount());
			throw new BCException();
		}

		//充值，添加用户行为日志
		mqSend.SendUserAction(fvirtualcaptualoperation.getFuid(), LogUserActionEnum.COIN_RECHARGE,
				fvirtualcaptualoperation.getFcoinid(), amount, "");
		log.info("Update user wallet succeed! userId={}, type={}, amount={}",fvirtualcaptualoperation.getFuid(), fvirtualcaptualoperation.getFcoinid(), fvirtualcaptualoperation.getFamount());

		// 风控短信
		//TODO 修复风控
//		if (fvirtualcaptualoperation.getFamount().compareTo(coinType.getRiskNum()) >= 0) {
//			String riskphone = redisHelper.getSystemArgs(ArgsConstant.RISKPHONE);
//			String[] riskphones = riskphone.split("#");
//			if (riskphones.length > 0) {
//				FUser fuser = userMapper.selectByPrimaryKey(fvirtualcaptualoperation.getFuid());
//				for (String string : riskphones) {
//					try {
//						validateHelper.smsRiskManage(fuser.getFloginname(), string, PlatformEnum.BC.getCode(),
//								BusinessTypeEnum.SMS_RISKMANAGE.getCode(), "充值",
//								fvirtualcaptualoperation.getFamount(), coinType.getName());
//					} catch (Exception e) {
//						logger.error("updateCoinCome riskphones err");
//						e.printStackTrace();
//					}
//				}
//			}
//		}

	}
}
