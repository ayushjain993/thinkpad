package io.uhha.web.controller.system;

import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.redis.RedisMallHelper;
import io.uhha.system.domain.FCountry;
import io.uhha.system.service.IFCountryService;
import io.uhha.system.service.IMallRedisInitService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/country")
public class CountryController {

    @Autowired
    private IFCountryService countryService;

    @Autowired
    private RedisMallHelper redisMallHelper;

    @Autowired
    private IMallRedisInitService mallRedisInitService;



    /**
     * 获取国家列表
     */
    @GetMapping
    public AjaxResult list() {
        List<FCountry> countries = redisMallHelper.getCountries();
        return AjaxResult.success(countries);
    }
}
