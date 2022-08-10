package io.uhha.common.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.common.core.redis.RedisObject;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.goods.domain.PmsCategory;
import io.uhha.goods.vo.SpuDetail;
import io.uhha.order.domain.OmsLogisticsCompany;
import io.uhha.setting.domain.LsEmailSetting;
import io.uhha.setting.domain.LsSmsSetting;
import io.uhha.sms.domain.SmsHomeNewProduct;
import io.uhha.sms.domain.SmsHomeRecommendProduct;
import io.uhha.sms.domain.SmsHomeRecommendSubject;
import io.uhha.statistics.bean.Pv;
import io.uhha.system.domain.FCountry;
import io.uhha.system.service.IMallRedisInitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisMallHelper {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IMallRedisInitService  mallRedisInitService;

    /**
     * 默认过期时间为1个小时
     */
    private static final int expire_time = 60 * 60;

    /**
     * 默认过期时间为24个小时
     */
    private static final int HOURS_24 = 60 * 60 * 24;

    /**
     * 新增不过期的数据
     *
     * @param key   键
     * @param value 值
     * @return ok
     */
    public void setNoExpire(String key, String value) {
        redisCache.setCacheObject(key, value);
    }

    public void set(String key, RedisObject t) {
        set(key, t, expire_time);
    }

    public void setNoExpire(String key, RedisObject t) {
        redisCache.setCacheObject(key, JSON.toJSONString(t));
    }

    public void set(String key, RedisObject t, int seconds) {
        redisCache.setCacheObject(key, JSON.toJSONString(t), seconds, TimeUnit.SECONDS);
    }


    public void remove(String key) {
        redisCache.deleteObject(key);
    }

    public String get(String key) {
        return redisCache.getCacheObject(key);
    }

    public void delete(String key) {
        redisCache.deleteObject(key);
    }

    /**
     * 获取短消息配置信息
     *
     * @return 短消息配置信息
     */
    public LsSmsSetting getActiveSmsSetting() {
        String smsSetting = redisCache.getCacheObject(RedisConstant.ACTIVE_SMS_SETTING);
        if (StringUtils.isEmpty(smsSetting)) {
            mallRedisInitService.initSmsSettings();
            smsSetting = redisCache.getCacheObject(RedisConstant.ACTIVE_SMS_SETTING);
            if(StringUtils.isEmpty(smsSetting)){
                log.error("ActiveSmsSetting data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(smsSetting);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            return null;
        }
        LsSmsSetting activeSmsSetting = JSONObject.parseObject(resultStr,
                LsSmsSetting.class);
        return activeSmsSetting;
    }

    /**
     * 获取Email配置信息
     *
     * @return Email配置信息
     */
    public LsEmailSetting getActiveEmailSetting() {
        String emailSetting = redisCache.getCacheObject(RedisConstant.ACTIVE_EMAIL_SETTING);
        if (StringUtils.isEmpty(emailSetting)) {
            mallRedisInitService.initEmailSettings();
            emailSetting = redisCache.getCacheObject(RedisConstant.ACTIVE_SMS_SETTING);
            if(StringUtils.isEmpty(emailSetting)){
                log.error("ActiveEmailSetting data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(emailSetting);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            return null;
        }
        LsEmailSetting activeEmailSetting = JSONObject.parseObject(resultStr,
                LsEmailSetting.class);
        return activeEmailSetting;
    }

    /**
     * 获取物流公司列表
     *
     * @return 物流公司列表
     */
    public List<OmsLogisticsCompany> getLogisticCompanies() {
        String logisticCompanies = redisCache.getCacheObject(RedisConstant.LOGISTIC_COMPANIES);
        if (StringUtils.isEmpty(logisticCompanies)) {
            mallRedisInitService.initLogisticCompany();
            logisticCompanies = redisCache.getCacheObject(RedisConstant.LOGISTIC_COMPANIES);
            if(StringUtils.isEmpty(logisticCompanies)){
                log.error("OmsLogisticsCompany data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(logisticCompanies);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            return null;
        }
        List<OmsLogisticsCompany> logisticCompanyList = JSONArray.parseArray(resultStr,
                OmsLogisticsCompany.class);
        return logisticCompanyList;
    }

    /**
     * 获取国家列表
     *
     * @return 国家列表
     */
    public List<FCountry> getCountries() {
        String countries = redisCache.getCacheObject(RedisConstant.COUNTRIES);
        if (StringUtils.isEmpty(countries)) {
            mallRedisInitService.initCountries();
            countries = redisCache.getCacheObject(RedisConstant.COUNTRIES);
            if(StringUtils.isEmpty(countries)){
                log.error("FCountry data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(countries);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            return null;
        }
        List<FCountry> countryList = JSONArray.parseArray(resultStr,
                FCountry.class);
        return countryList;
    }

    /**
     * 通过国家名称获取国家
     *
     * @return 国家列表
     */
    public FCountry getCountryByName(String countryName) {
        List<FCountry> countryList = getCountries();
        Optional<FCountry> countryOptional = countryList.stream().filter(x->x.getEnglishName().equalsIgnoreCase(countryName)).findFirst();
        if(countryOptional.isPresent()){
            return countryList.get(0);
        }
        return null;
    }

    /**
     * 通过国家码获取国家
     *
     * @return 国家列表
     */
    public FCountry getCountryByAreacode(String areacode) {
        List<FCountry> countryList = getCountries();
        if(CollectionUtils.isEmpty(countryList)){
            log.error("country list is empty.");
            return null;
        }
        Optional<FCountry> countryOptional = countryList.stream().filter(x->areacode.equalsIgnoreCase(x.getAreacode())).findFirst();
        if(countryOptional.isPresent()){
            return countryOptional.get();
        }
        return null;
    }

    /**
     * 获取首页新产品列表
     *
     * @return 新产品列表
     */
    public List<SmsHomeNewProduct> getHomeNewProductList(Long storeId) {
        String products = redisCache.getCacheObject(RedisConstant.NEW_PRODUCT+storeId);
        if (StringUtils.isEmpty(products)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(products);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            return null;
        }
        List<SmsHomeNewProduct> newProductList = JSONArray.parseArray(resultStr,
                SmsHomeNewProduct.class);
        return newProductList;
    }

    /**
     * 设置新产品列表
     *
     * @param newProductList 新产品列表
     */
    public void setHomeNewProductList(List<SmsHomeNewProduct> newProductList, Long storeId) {
        if (CollectionUtils.isNotEmpty(newProductList)) {
            RedisObject redisObject = new RedisObject();
            redisObject.setExtObject(newProductList);
            setNoExpire(RedisConstant.NEW_PRODUCT+storeId, redisObject);
        }
    }

    /**
     * 获取首页推荐产品列表
     *
     * @return 推荐产品列表
     */
    public List<SmsHomeRecommendProduct> getHomeRecommendProductList(Long storeId) {
        String products = redisCache.getCacheObject(RedisConstant.RECOMMEND_PRODUCT+storeId);
        if (StringUtils.isEmpty(products)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(products);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            return null;
        }
        List<SmsHomeRecommendProduct> recommendProducts = JSONArray.parseArray(resultStr,
                SmsHomeRecommendProduct.class);
        return recommendProducts;
    }

    /**
     * 设置首页推荐产品列表
     *
     * @param recommendProductList 推荐商品列表
     */
    public void setHomeRecommendProductList(List<SmsHomeRecommendProduct> recommendProductList, Long storeId) {
        if (CollectionUtils.isNotEmpty(recommendProductList)) {
            RedisObject redisObject = new RedisObject();
            redisObject.setExtObject(recommendProductList);
            setNoExpire(RedisConstant.RECOMMEND_PRODUCT+storeId, redisObject);
        }
    }

    /**
     * 获取首页推荐话题列表
     *
     * @return 推荐话题列表
     */
    public List<SmsHomeRecommendSubject> getHomeRecommendSubjectList(Long storeId) {
        String subjects = redisCache.getCacheObject(RedisConstant.RECOMMEND_SUBJECT+storeId);
        if (StringUtils.isEmpty(subjects)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(subjects);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            return null;
        }
        List<SmsHomeRecommendSubject> recommendSubjects = JSONArray.parseArray(resultStr,
                SmsHomeRecommendSubject.class);
        return recommendSubjects;
    }

    /**
     * 设置首页推荐话题列表
     *
     * @param recommendSubjects 推荐话题列表
     */
    public void setHomeRecommendSubjectList(List<SmsHomeRecommendSubject> recommendSubjects, Long storeId) {
        if (CollectionUtils.isNotEmpty(recommendSubjects)) {
            RedisObject redisObject = new RedisObject();
            redisObject.setExtObject(recommendSubjects);
            setNoExpire(RedisConstant.RECOMMEND_SUBJECT+storeId, redisObject);
        }
    }

    /**
     * 获取第一和第二级商品分类目录
     *
     * @return 第一和第二级商品分类目录
     */
    public List<PmsCategory> getAllFirstAndSecondCategory() {
        String categories = redisCache.getCacheObject(RedisConstant.ALL_1_2_CATEGORY);
        if (StringUtils.isEmpty(categories)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(categories);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            return null;
        }
        return JSONArray.parseArray(resultStr,
                PmsCategory.class);
    }

    /**
     * 设置第一和第二级商品分类目录
     *
     * @param pmsCategoryList 第一和第二级商品分类目录
     */
    public void setAllFirstAndSecondCategory(List<PmsCategory> pmsCategoryList) {
        if (CollectionUtils.isNotEmpty(pmsCategoryList)) {
            RedisObject redisObject = new RedisObject();
            redisObject.setExtObject(pmsCategoryList);
            setNoExpire(RedisConstant.ALL_1_2_CATEGORY, redisObject);
        }
    }

    /**
     * 获取spu详情，供商品详情页使用
     *
     * @param skuId skuId
     */
    @Deprecated
    public SpuDetail getSpuDetail(String skuId) {
        String strSpu = redisCache.getCacheObject(RedisConstant.SPU_DETAIL + skuId);
        if (StringUtils.isEmpty(strSpu)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(strSpu);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            return null;
        }
        return JSONArray.parseObject(resultStr, SpuDetail.class);
    }

    /**
     * 缓存spu详情，供商品详情页使用
     *
     * @param spuDetail
     */
    @Deprecated
    public void setSpuDetail(String skuId, SpuDetail spuDetail) {
        if (ObjectUtils.isNotEmpty(spuDetail)) {
            RedisObject redisObject = new RedisObject();
            redisObject.setExtObject(spuDetail);
            setNoExpire(RedisConstant.SPU_DETAIL + skuId, redisObject);
        }
    }

    /**
     * 获取goods详情，供商品详情页使用
     *
     * @param goodsId goodsId
     */
    public SpuDetail getGoodsDetail(Long goodsId) {

        String strSpu = redisCache.getCacheObject(RedisConstant.GOODS_DETAIL + goodsId);
        if (StringUtils.isEmpty(strSpu)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(strSpu);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            return null;
        }
        return JSONArray.parseObject(resultStr, SpuDetail.class);
    }

    /**
     * 缓存goods详情，供商品详情页使用
     *
     * @param spuDetail
     */
    public void setGoodsDetail(Long goodsId, SpuDetail spuDetail) {

        if (ObjectUtils.isNotEmpty(spuDetail)) {
            RedisObject redisObject = new RedisObject();
            redisObject.setExtObject(spuDetail);
            String value = JSON.toJSONString(redisObject);
            redisCache.setCacheObject(RedisConstant.GOODS_DETAIL + goodsId, value);
        }
    }

    /**
     * 删除spu缓存
     *
     * @param goodsId
     */
    public void removeGoods(Long goodsId) {
        remove(RedisConstant.GOODS_DETAIL + goodsId);
    }

    /**
     * 删除spu缓存
     *
     * @param ids
     */
    public void removeGoods(Long[] ids) {
        List<Long> lids = Arrays.asList(ids);
        lids.forEach(x -> {
            remove(RedisConstant.GOODS_DETAIL + x);
        });
    }

    public List<Pv> getPvuvList(Date date){
        String strToday = DateUtils.parseDateToStr("YYYY-MM-dd", date);
        String pvStr = redisCache.getCacheObject(RedisConstant.PVUV_TODAY+strToday);
        if(StringUtils.isEmpty(pvStr)){
            return null;
        }
        JSONObject obj = JSON.parseObject(pvStr);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            log.error("invalid cached pv result");
            return null;
        }
        return JSONArray.parseArray(resultStr, Pv.class);
    }

    /**
     * 存入redis
     * @param pv
     * @return 1 成功， 0 已存在，
     */
    public int addPvuvInRedis(Pv pv){
        Date today = pv.getCreateTime();
        String strToday = DateUtils.parseDateToStr("YYYY-MM-dd", today);
        List<Pv> pvList = getPvuvList(pv.getCreateTime());;

        //新的一天时
        if(CollectionUtils.isEmpty(pvList)){
            pvList = new ArrayList<>();
            pvList.add(pv);
            RedisObject redisObject = new RedisObject();
            redisObject.setExtObject(pvList);
            String value = JSON.toJSONString(redisObject);
            redisCache.setCacheObject(RedisConstant.PVUV_TODAY+strToday, value,1, TimeUnit.DAYS);
            return 1;
        }else{
            //查看本条pv记录是否存在
            boolean exist = pvList.stream().anyMatch(x->{
                String date1 = DateUtils.parseDateToStr("YYYY-MM-dd", x.getCreateTime());
                String date2 = DateUtils.parseDateToStr("YYYY-MM-dd", pv.getCreateTime());
                String ip1 = x.getIp();
                String ip2 = pv.getIp();
                String uid1 = x.getUid();
                String uid2 = pv.getUid();
                return (date1.equalsIgnoreCase(date2) &&ip1.equalsIgnoreCase(ip2) && StringUtils.isEmpty(uid2))
                        ||(date1.equalsIgnoreCase(date2) &&ip1.equalsIgnoreCase(ip2) && StringUtils.isNotEmpty(uid2)&&uid2.equalsIgnoreCase(uid1));
            });

            if(!exist){
                pvList.add(pv);
                RedisObject redisObject = new RedisObject();
                redisObject.setExtObject(pvList);
                String value = JSON.toJSONString(redisObject);
                redisCache.setCacheObject(RedisConstant.PVUV_TODAY+strToday, value,1, TimeUnit.DAYS);
                return 1;
            }else{
                return 0;
            }
        }
    }

    public BigDecimal getUSDCNYRate(){
        String strToday = DateUtils.parseDateToStr("YYYY-MM-dd", DateUtils.getNowDate());
        String pvStr = redisCache.getCacheObject(RedisConstant.USD_CNY_RATE+strToday);
        if(StringUtils.isEmpty(pvStr)){
            return null;
        }
        JSONObject obj = JSON.parseObject(pvStr);
        String resultStr = obj.getString("extObject");
        if (resultStr == null) {
            log.error("invalid cached usd/cny result");
            return null;
        }
        return JSONObject.parseObject(resultStr, BigDecimal.class);
    }

    public void setUSDCNYRate(BigDecimal rate){
        String strToday = DateUtils.parseDateToStr("YYYY-MM-dd", DateUtils.getNowDate());

        if (ObjectUtils.isNotEmpty(rate)) {
            RedisObject redisObject = new RedisObject();
            redisObject.setExtObject(rate);
            String value = JSON.toJSONString(redisObject);
            redisCache.setCacheObject(RedisConstant.USD_CNY_RATE + strToday, value,4, TimeUnit.HOURS);
        }
    }
}
