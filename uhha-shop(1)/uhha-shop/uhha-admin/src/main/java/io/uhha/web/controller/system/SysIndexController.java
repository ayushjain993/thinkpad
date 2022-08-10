package io.uhha.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.uhha.common.config.UhhaConfig;
import io.uhha.common.utils.StringUtils;

/**
 * 首页
 *
 * @author ruoyi
 */
@RestController
public class SysIndexController
{
    /** 系统基础配置 */
    @Autowired
    private UhhaConfig uhhaConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("Thanks for choosing {} framework, current version：v{}, please use specific address to visit.", uhhaConfig.getName(), uhhaConfig.getVersion());
    }
}
