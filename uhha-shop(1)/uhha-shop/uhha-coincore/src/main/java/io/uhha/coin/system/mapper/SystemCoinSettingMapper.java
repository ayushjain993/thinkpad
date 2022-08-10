package io.uhha.coin.system.mapper;

import io.uhha.coin.system.domain.SystemCoinSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 币种等级设置数据操作接口
 *
 * @author LY
 */
@Mapper
public interface SystemCoinSettingMapper {

    /**
     * 插入
     */
    int insert(SystemCoinSetting record);

    /**
     * 查询
     */
    List<SystemCoinSetting> selectAll();

    /**
     * 更新
     */
    int updateByPrimaryKey(SystemCoinSetting record);

    /**
     * 根据等级和币种查询
     */
    SystemCoinSetting selectSystemCoinSetting(@Param("coinId") Integer coinId, @Param("levelVip") Integer levelVip);

    /**
     * 根据币种ID查询设置
     */
    List<SystemCoinSetting> selectListByCoinId(@Param("coinId") Integer coinId);

    /**
     * 根据币种ID，VIP0设置 查询设置
     */
    List<SystemCoinSetting> selectListByLevel(@Param("coinId") Integer coinId, @Param("levelVip") Integer levelVip);

}