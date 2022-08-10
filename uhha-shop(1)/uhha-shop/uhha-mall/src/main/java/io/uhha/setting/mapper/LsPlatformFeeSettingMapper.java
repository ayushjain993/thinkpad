package io.uhha.setting.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.setting.bean.PlatformFeeSet;
import io.uhha.setting.domain.LsPlatformFeeSetting;
import org.apache.ibatis.annotations.Mapper;

/**
 * 平台费用设置Mapper接口
 * 
 * @author peter
 * @date 2022-01-26
 */
@Mapper
public interface LsPlatformFeeSettingMapper extends BaseMapper<LsPlatformFeeSetting>
{

    /**
     * 查询平台费用设置
     *
     * @return 返回删除行数
     */
    public List<LsPlatformFeeSetting> queryPlatformFeeSet();

    /**
     * 删除平台费用设置
     *
     * @return 返回删除行数
     */
    public int deletePlatformFeeSet();

    /**
     * 删除平台费用设置
     *
     * @return 返回删除行数
     */
    public int addPlatformFeeSet(List<LsPlatformFeeSetting> settings);


}
