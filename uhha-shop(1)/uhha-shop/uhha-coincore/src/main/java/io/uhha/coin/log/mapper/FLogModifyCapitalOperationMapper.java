package io.uhha.coin.log.mapper;

import io.uhha.coin.log.domain.FLogModifyCapitalOperation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 充值资金修改日志-数据库访问接口
 *
 * @author LY
 */
@Mapper
public interface FLogModifyCapitalOperationMapper {

    /**
     * 新增修改日志
     *
     * @param record 修改记录实体
     * @return 插入记录数
     */
    int insert(FLogModifyCapitalOperation record);
}
