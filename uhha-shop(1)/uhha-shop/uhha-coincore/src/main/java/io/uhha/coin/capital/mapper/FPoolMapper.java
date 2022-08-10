package io.uhha.coin.capital.mapper;

import io.uhha.coin.capital.domain.FPool;
import io.uhha.coin.system.domain.SystemCoinTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 虚拟币地址池-数据库访问接口
 *
 * @author ZKF
 */
@Mapper
public interface FPoolMapper {

    /**
     * 插入
     *
     * @param record 地址实体
     * @return 插入记录数
     */
    int insert(FPool record);

    /**
     * 获取剩余数量
     *
     * @param map 参数map
     * @return 查询记录列表
     */
    List<Map<String, Object>> getVirtualCoinAddressNumList(Map<String, Object> map);

    /**
     * 获取剩余数量总条数
     *
     * @param map 参数map
     * @return 查询记录数
     */
    int countVirtualCoinAddressNumList(Map<String, Object> map);

    /**
     * 查询一个充值地址
     * @param fcoinid 币种ID
     * @return 实体对象
     */
    FPool selectOneFpool(Integer fcoinid);

    /**
     * 更新充值地址为已使用状态（0-未使用，1-已使用）
     * @param fpool 实体对象
     * @return 成功条数
     */
    int updatePoolStatus(FPool fpool);


    SystemCoinTypeVO countVirtualCoinAddressNum(@Param("fcoinid") Integer fcoinid);


}