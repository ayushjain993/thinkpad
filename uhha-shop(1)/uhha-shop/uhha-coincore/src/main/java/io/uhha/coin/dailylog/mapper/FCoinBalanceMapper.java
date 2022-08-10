package io.uhha.coin.dailylog.mapper;

import io.uhha.coin.dailylog.domain.FCoinBalance;
import org.apache.ibatis.annotations.Mapper;

/**
 * 每日虚拟币余额
 *
 */
@Mapper
public interface FCoinBalanceMapper {

    /**
     * 新增数据
     */
    int insert(FCoinBalance fCoinBalance);
}
