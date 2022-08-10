package io.uhha.setting.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.setting.bean.PlatformFeeSet;
import io.uhha.setting.domain.LsPlatformFeeSetting;

/**
 * 平台费用设置Service接口
 * 
 * @author peter
 * @date 2022-01-26
 */
public interface ILsPlatformFeeSettingService extends IService<LsPlatformFeeSetting>
{
    /**
     * 查询平台费率配置设置
     *
     * @return 返回PlatformFeeSet
     */
    public PlatformFeeSet queryPlatformFeeSet();

    /**
     * 修改平台费率配置设置
     *
     * @return 返回 是否成功
     */
    public int editPlatformFeeSet(PlatformFeeSet platformFeeSet);
}
