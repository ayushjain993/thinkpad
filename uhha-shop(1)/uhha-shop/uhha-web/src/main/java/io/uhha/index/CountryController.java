package io.uhha.index;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.redis.RedisMallHelper;
import io.uhha.system.domain.FCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "国家Controller")
@RestController
@RequestMapping("/country")
public class CountryController {

    @Autowired
    private RedisMallHelper redisMallHelper;


    /**
     * 获取国家列表
     */
    @UnAuth
    @GetMapping
    public AjaxResult list() {
        List<FCountry> countries = redisMallHelper.getCountries();
        return AjaxResult.success(countries);
    }
}
