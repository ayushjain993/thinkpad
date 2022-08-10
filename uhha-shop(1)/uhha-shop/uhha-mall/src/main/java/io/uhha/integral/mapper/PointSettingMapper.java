package io.uhha.integral.mapper;


import io.uhha.integral.domain.PointSetting;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by mj on 17/5/23.
 * 积分设置数据库接口
 */
@Mapper
public interface PointSettingMapper {

    /**
     * 修改积分设置
     *
     * @param pointSetting 积分设置
     * @return 成功返回 1 失败返回0
     */

    int updatePointSetting(PointSetting pointSetting);

    /**
     * 查询积分设置
     *
     * @return 返回积分设置
     */

    PointSetting queryPointSetting();
}
