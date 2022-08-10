package io.uhha.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.jsonwebtoken.lang.Assert;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.redis.RedisMallHelper;
import io.uhha.common.utils.SnowflakeIdWorker;
import io.uhha.common.utils.StringUtils;
import io.uhha.common.utils.http.OKHttp3ClientUtils;
import io.uhha.goods.domain.*;
import io.uhha.goods.service.*;
import io.uhha.setting.service.ILsSkuSourceSettingService;
import io.uhha.store.domain.TStoreCustomizeBrand;
import io.uhha.store.service.ITStoreCustomizeBrandService;
import io.uhha.util.bean.OneboundItemSearchResponse;
import io.uhha.util.bean.OneboundSku;
import io.uhha.util.enums.YiwugoErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OneBoundSpuUtils {

    @Autowired
    private ILsSkuSourceSettingService skuSourceSettingService;

    @Autowired
    private IPmsSpecService pmsSpecService;

    @Autowired
    private IPmsSpecValueService pmsSpecValueService;

    @Autowired
    private IPmsGoodsService pmsGoodsService;

    @Autowired
    private ITStoreCustomizeBrandService pmsBrandService;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private IPmsGoodsImportService goodsImportService;

    @Autowired
    private RedisMallHelper redisMallHelper;

    private static final String ONEBOUND_YIWUGOU = "https://api-gw.onebound.cn/yiwugo";
    private static final String ONEBOUND_TAOBAO = "https://api-gw.onebound.cn/taobao";

    private String getUrlByName(String name) {
        if ("taobao".equalsIgnoreCase(name)) {
            return ONEBOUND_TAOBAO;
        }
        if ("yiwugo".equalsIgnoreCase(name)) {
            return ONEBOUND_YIWUGOU;
        }

        return null;
    }

    /**
     * 通过义乌购编号查询PmsGoods
     *
     * @param pmsGoodsImport 导入pms对象
     * @param lang           语言
     * @return PmsGoods
     * @throws IOException
     */
    public Integer saveSpu(PmsGoodsImport pmsGoodsImport, Long storeId, String lang) throws IOException {

        String apiKey = skuSourceSettingService.querySkuSourceSet().getOneBoundSourceSet().getAccessKey();
        String secret = skuSourceSettingService.querySkuSourceSet().getOneBoundSourceSet().getApiSecret();

        String url = getUrlByName(pmsGoodsImport.getChannel()) + "/item_get/?key=" + apiKey
                + "&secret=" + secret
                + "&lang=" + lang
                + "&num_iid=" + pmsGoodsImport.getNumIid();
        Response res = OKHttp3ClientUtils.getInstance().getData(url);

        if (res == null) {
            log.error("onebound server return null.");
            return 0;
        }

        if (res.isSuccessful()) {
            String jsonStr = Objects.requireNonNull(res.body()).string();
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            String itemStr = jsonObject.getString("item");
            String productName = pmsGoodsImport.getName();

            convertAndSaveToPmsGoods(productName,
                    storeId,
                    pmsGoodsImport,
                    itemStr);

            //修改pmsGoodsImport状态为已发布
            JSONObject forTmall = JSON.parseObject(itemStr);
            Boolean tmall = forTmall.getBoolean("tmall");
            String orgUrl = forTmall.getString("detail_url");
            pmsGoodsImport.setIsRelease("1");
            pmsGoodsImport.setTmall(tmall==null? 1 : 0);
            pmsGoodsImport.setOrgUrl(orgUrl);
            goodsImportService.updatePmsGoodsImport(pmsGoodsImport);

        } else {
            log.error("onebound server response failure.");
            return 0;
        }
        return 1;
    }

    /**
     * 输出义乌购查询结果为TableDataInfo
     *
     * @param keyword 关键字
     * @param page    页码
     * @param lang    语言
     * @return 数据表
     * @throws IOException
     */
    public TableDataInfo itemSearch(String channel, String keyword, Integer page, String lang) throws IOException {

        String apiKey = skuSourceSettingService.querySkuSourceSet().getOneBoundSourceSet().getAccessKey();
        String secret = skuSourceSettingService.querySkuSourceSet().getOneBoundSourceSet().getApiSecret();

        String url = getUrlByName(channel) + "/item_search/?key=" + apiKey
                + "&q=" + keyword
                + "&page=" + page
                + "&lang=" + lang
                + "&secret=" + secret;
        log.debug(url);

//异步查询
//        final List<YiwugoItem>[] yiwugoItems = new List[]{new ArrayList<>()};
//
//        OKHttp3ClientUtils.getInstance().getDataAsync(url, new OKHttp3ClientUtils.MyNetCall() {
//            @Override
//            public void success(Call call, Response response) throws IOException {
//                String jsonStr = Objects.requireNonNull(response.body()).string();
//                JSONObject jsonObject = JSON.parseObject(jsonStr);
//                String errorCode = jsonObject.getString("error_code");
//                if (!YiwugoErrorCodeEnum.SUCCESS.getErrorCode().equalsIgnoreCase(errorCode)) {
//                    log.debug(jsonStr);
//                    log.error("return failed test");
//                } else {
//                    String items = jsonObject.getString("items");
//                    JSONObject obj = JSON.parseObject(items);
//                    yiwugoItems[0] = JSON.parseArray(obj.getString("item"), YiwugoItem.class);
//                }
//            }
//
//            @Override
//            public void failed(Call call, IOException e) {
//                log.debug("onebound server return:[{}]", e.toString());
//            }
//        });

        Response res = OKHttp3ClientUtils.getInstance().getData(url);
        if (res == null) {
            log.error("onebound server return null.");
            return null;
        }

        TableDataInfo tableDataInfo = new TableDataInfo();

        if (res.isSuccessful()) {
            String jsonStr = Objects.requireNonNull(res.body()).string();
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            String errorCode = jsonObject.getString("error_code");
            if (!YiwugoErrorCodeEnum.SUCCESS.getErrorCode().equalsIgnoreCase(errorCode)) {
                log.debug(jsonStr);
                log.error("return failed");
                tableDataInfo.setCode(500);
                tableDataInfo.setMsg("Server return error code={}" + errorCode);
            } else {
                String items = jsonObject.getString("items");
                Assert.notNull(items);
                JSONObject obj = JSON.parseObject(items);
                String strItemArray = obj.getString("item");
                Integer total = obj.getInteger("total_results");

                //组装结果
                tableDataInfo.setCode(200);
                tableDataInfo.setMsg("success");
                tableDataInfo.setRows(JSON.parseArray(strItemArray, OneboundItemSearchResponse.class));
                tableDataInfo.setTotal(total);
            }
        } else {
            tableDataInfo.setCode(500);
            tableDataInfo.setMsg("onebound response failure");
            log.debug("onebound response failure. return:[{}]", res);
        }
        return tableDataInfo;
    }

    /**
     * 解析输出为PmsGoods并保存
     *
     * @param name
     * @param pmsGoodsImport
     * @param json
     * @return
     */
    private Integer convertAndSaveToPmsGoods(String name, Long storeId, PmsGoodsImport pmsGoodsImport, String json) {
        log.debug("convertAndSaveToPmsGoods: \r\n name:{} \r\n storeId:{} \n json: {}", name, storeId, json);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(json);

        //按照系统反馈的格式分拆和填充结构体
        Long numId = Long.parseLong(jsonObject.getString("num_iid"));
        String desc = jsonObject.getString("desc");
//        String strPrice = jsonObject.getString("price");
        String vendorNickname = jsonObject.getString("nick");

        //获取价格信息接口不稳定，暂时从列表数据获取
//        BigDecimal price = BigDecimal.ZERO;
//        if (!StringUtils.isEmpty(strPrice) && !"[]".equalsIgnoreCase(strPrice)) {
//            strPrice = strPrice.replace(",", "");
//            price = new BigDecimal(strPrice);
//        }

        //解析规格参数并保存
        String propsList = jsonObject.getString("props_list");
        List<PmsSpec> specs = convert2PmsSpecs(propsList, numId, pmsGoodsImport.getChannel());
        specs.forEach(x -> {
            pmsSpecService.addSpec(x);
        });



        //通过名称和storeId 品牌
        String strBrand = jsonObject.getString("brand");
        TStoreCustomizeBrand brand;
        if (StringUtils.isEmpty(strBrand)) {
            brand = pmsBrandService.queryBrandByNameAndStoreId(vendorNickname, storeId);
            //使用输入信息创建自定义品牌
            if (ObjectUtils.isEmpty(brand)) {
                TStoreCustomizeBrand newBrand = TStoreCustomizeBrand.buildCustomizeBrand(vendorNickname, "System", storeId);
                pmsBrandService.insertTStoreCustomizeBrand(newBrand);
                //通过名称和storeId 品牌
                brand = pmsBrandService.queryBrandByNameAndStoreId(vendorNickname, storeId);
                //导入品牌自动审批通过
                pmsBrandService.passBrandAudit(brand.getId());
            }
        } else {
            brand = pmsBrandService.queryBrandByNameAndStoreId(strBrand, storeId);
            //使用昵称创建自定义品牌
            if (ObjectUtils.isEmpty(brand)) {
                TStoreCustomizeBrand newBrand = TStoreCustomizeBrand.buildCustomizeBrand(strBrand, "System", storeId);
                pmsBrandService.insertTStoreCustomizeBrand(newBrand);
                //通过名称和storeId 品牌
                brand = pmsBrandService.queryBrandByNameAndStoreId(strBrand, storeId);
                //导入品牌自动审批通过
                pmsBrandService.passBrandAudit(brand.getId());
            }
        }

        //解析图片
        String strImageObjs = jsonObject.getString("item_imgs");
        List<PmsGoodsImage> goodsImageList = convert2PmsImage(strImageObjs, pmsGoodsImport.getChannel());

        //解析Sku列表并保存
        JSONObject skusJsonObject = jsonObject.getJSONObject("skus");
        JSONArray skuArray = skusJsonObject.getJSONArray("sku");
        List<PmsSku> skus = new ArrayList<>();
        if (skuArray != null && !skuArray.isEmpty()) {
            skus = convert2PmsSku(skuArray,name, storeId, numId);
        }

        //使用spu图片填充到sku图片中
        skus.forEach(x->{
            x.setSkuImages(buildSkuImageWithSpuImage(goodsImageList));
        });

        //解析PmsGoods并保存
        PmsGoods spu = new PmsGoods();

        spu.setSpuImages(goodsImageList);
        spu.setId(numId);
        spu.setPrice(convertPrice2Usd(pmsGoodsImport.getPrice()));
        spu.setStoreName("代购商城");
        spu.setName(name);
        spu.setPcDesc(desc);
        spu.setMobileDesc(desc);
        spu.setStoreId(storeId);
        //自定义品牌
        spu.setCustomizeBrand(1);
        //设置typeid为1L
        spu.setTypeId(1L);
        spu.setBrandId(brand.getId());
        spu.setUrl(pmsGoodsImport.getPicUrl());
        spu.setFirstCateId(pmsGoodsImport.getFirstCateId());
        spu.setSecondCateId(pmsGoodsImport.getSecondCateId());
        spu.setThirdCateId(pmsGoodsImport.getThirdCateId());
        spu.setSpuImportId(pmsGoodsImport.getId());

        // 新增商品图片
        spu.setSpuImages(goodsImageList);

        //生成spu规格集合
        spu.setSpuSpecValues(generatePmsGoodSpecValue(specs));

        //设置sku
        spu.setSkus(skus);

        pmsGoodsService.insertPmsGoods(spu);

        return 1;
    }

    /**
     * 使用spu的图片填充到sku中
     * @param goodsImages
     * @return
     */
    private List<PmsSkuImage> buildSkuImageWithSpuImage(List<PmsGoodsImage> goodsImages){
        List<PmsSkuImage> skuImages = new ArrayList<>();
        goodsImages.forEach(x->{
            PmsSkuImage skuImage = new PmsSkuImage();
            skuImage.setUrl(x.getUrl());
            skuImages.add(skuImage);
        });
        return skuImages;
    }

    /**
     * 转化为PmsGoodsImage
     *
     * @return
     */
    private List<PmsGoodsImage> convert2PmsImage(String jsonImages, String channel) {
        List<PmsGoodsImage> goodsImageList = new ArrayList<>();

        if ("Taobao".equalsIgnoreCase(channel)) {
            JSONArray jsonArray = JSON.parseArray(jsonImages);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String url = obj.getString("url");
                if (url.startsWith("//")) {
                    url = "http:" + url;
                }
                PmsGoodsImage goodsImage = PmsGoodsImage.buildPmsGoodsImageforAdd(url);
                goodsImageList.add(goodsImage);
            }
        } else if ("Yiwugo".equalsIgnoreCase(channel)) {
            JSONObject jsonObject = JSON.parseObject(jsonImages);
            for (int i = 0; i < jsonObject.size() - 1; i++) {
                String url = jsonObject.getString(Integer.toString(i));
                if (url.startsWith("//")) {
                    url = "http:" + url;
                }
                PmsGoodsImage goodsImage = PmsGoodsImage.buildPmsGoodsImageforAdd(url);
                goodsImageList.add(goodsImage);
            }
        } else {
            log.error("not supported channel: {}", channel);
            return null;
        }

        return goodsImageList;
    }

    /**
     * sku转化为PmsSku
     * @param jsonArray
     * @param spuName
     * @param storeId
     * @param numId
     * @return
     */
    private List<PmsSku> convert2PmsSku(JSONArray jsonArray, String spuName, Long storeId, Long numId) {

        List<PmsSku> pmsSkuList = new ArrayList<>();

        List<PmsSpec> pmsSpecs = pmsSpecService.querySpecByNumIid(Long.toString(numId));

        List<OneboundSku> oneboundSkuList = jsonArray.toJavaList(OneboundSku.class);
        oneboundSkuList.stream().forEach(x -> {
            PmsSku sku = new PmsSku();
            sku.setPrice(convertPrice2Usd(new BigDecimal(x.getPrice())));
            sku.setStock(Integer.parseInt(x.getQuantity()));
            sku.setName(spuName);
            sku.setSkuNo(snowflakeIdWorker.nextUuid());
            sku.setWarningStock(-1L);
            sku.setWeight(BigDecimal.ZERO);
            sku.setStoreId(storeId);
            //添加图片
            List<PmsSkuImage> skuImages = new ArrayList<>();
            skuImages.add(PmsSkuImage.build(sku));
            sku.setSkuImages(skuImages);

            String properties = x.getProperties();
            String propertiesName = x.getProperties_name();
            String subtitle = convertSubtitle(properties, propertiesName);
            sku.setSubtitle(subtitle);
            sku.setRemark(subtitle);
            List<PmsSkuSpecValue> skuSpecValues = getSkuSpecValueBySubtitle(pmsSpecs, subtitle);
            sku.setSkuSpecValues(skuSpecValues);
            pmsSkuList.add(sku);
        });

        return pmsSkuList;
    }

    /**
     * 20509:28316;1627207:15347169409
     * 20509:28316:尺码:L 腰围2.1-2.3尺/100-120斤;1627207:15347169409:颜色分类:83043-4丨主图款1丨纯棉抗菌-4条装
     * 转化为： L 腰围2.1-2.3尺/100-120斤 - 83043-4丨主图款1丨纯棉抗菌-4条装
     * @param properties 属性id
     * @param propertiesName 属性名
     * @return 组合的subtitle
     */
    private String convertSubtitle(String properties, String propertiesName){
        String[] props = properties.split(";");
        String[] propsName = propertiesName.split(";");

        if(ArrayUtils.isEmpty(props) || ArrayUtils.isEmpty(propsName) || props.length!=2 || propsName.length !=2){
            log.error("properties or names are invalid. \r\n properties:{} \r\n propertiesName:{}", props, propsName);
            return "";
        }

        String part1 = propsName[0].substring(props[0].length()+1);
        String part2 = propsName[1].substring(props[1].length()+1);

        String[] tmp1 = part1.split(":");
        String[] tmp2 = part2.split(":");

        return tmp1[1] + "-"+ tmp2[1];
    }

    /**
     * props_list转化为PmsSpec
     *
     * @return
     */
    private List<PmsSpec> convert2PmsSpecs(String propsJson, Long numId, String channel) {
        List<PmsSpec> pmsSpecList = new ArrayList<>();
        Multimap<String, PmsSpecValue> multiMap = ArrayListMultimap.create();

        if ("Taobao".equalsIgnoreCase(channel)) {
            JSONObject jsonObject = JSON.parseObject(propsJson);
            if (propsJson == null) {
                log.warn("props_list is empty.");
                return null;
            }

            jsonObject.keySet().forEach(x -> {
                String spec = jsonObject.getString(x);
                String[] items = spec.split(":");
                String specName = items[0];
                String specValue = items[1];
                PmsSpecValue v = new PmsSpecValue();
                v.setName(specValue);
                multiMap.put(specName, v);
            });

            multiMap.keySet().forEach(x -> {
                List<PmsSpecValue> specValues = new ArrayList<>(multiMap.get(x));
                PmsSpec pmsSpec = new PmsSpec();
                pmsSpec.setName(x);
                pmsSpec.setNickName(x);
                pmsSpec.setNumIid(Long.toString(numId));
                pmsSpec.setSpecValues(specValues);
                pmsSpecList.add(pmsSpec);
            });

            return pmsSpecList;
        } else if ("Yiwugo".equalsIgnoreCase(channel)) {
            JSONObject jsonObject = JSON.parseObject(propsJson);
            if (propsJson == null) {
                log.warn("props_list is empty.");
                return null;
            }

            jsonObject.keySet().forEach(x -> {
                String spec = jsonObject.getString(x);
                String[] items = spec.split(":");
                String specName = items[0];
                String specValue = items[1];
                PmsSpecValue v = new PmsSpecValue();
                v.setName(specValue);
                multiMap.put(specName, v);
            });

            multiMap.keySet().forEach(x -> {
                List<PmsSpecValue> specValues = new ArrayList<>(multiMap.get(x));
                PmsSpec pmsSpec = new PmsSpec();
                pmsSpec.setName(x);
                pmsSpec.setNickName(x);
                pmsSpec.setNumIid(Long.toString(numId));
                pmsSpec.setSpecValues(specValues);
                pmsSpecList.add(pmsSpec);
            });
        } else {
            log.error("not supported channel: {}", channel);
            return null;
        }
        multiMap.clear();

        return pmsSpecList;
    }

    /**
     * 生成商品规格集合（计算规格的笛卡尔积）
     *
     * @return
     */
    List<PmsGoodsSpecValue> generatePmsGoodSpecValue(List<PmsSpec> pmsSpecs) {
        // 给商品使用的规格值
        List<PmsGoodsSpecValue> pmsGoodsSpecValues = new ArrayList<>();

        pmsSpecs.forEach(x->{
            List<PmsSpecValue> pmsSpecValues = x.getSpecValues();
            pmsSpecValues.forEach(y->{
                PmsGoodsSpecValue goodsSpecValue = new PmsGoodsSpecValue();
                goodsSpecValue.setSpecId(x.getId());
                goodsSpecValue.setSpecValueId(y.getId());
                goodsSpecValue.setValueRemark(y.getName());
                pmsGoodsSpecValues.add(goodsSpecValue);
            });
        });

        return pmsGoodsSpecValues;
    }

    /**
     * 通过商品规格参数生成单品规格参数(PmsGoodsSpecValue->PmsSkuSpecValue)
     *
     * @param subtitle 子标题
     *
     * @return PmsSkuSpecValue 单品规格参数
     */
    List<PmsSkuSpecValue> getSkuSpecValueBySubtitle(List<PmsSpec> pmsSpecs, String subtitle) {
        List<PmsSkuSpecValue> skuSpecValues = new ArrayList<>();

        //准备数据
        List<PmsSpecValue> pmsSpecValues = new ArrayList<>();
        pmsSpecs.forEach(x->{
            List<PmsSpecValue> temp = pmsSpecValueService.querySpecValuesBySpecId(x.getId());
            pmsSpecValues.addAll(temp);
        });

        String[] strSpecs  = subtitle.split("-");
        if(strSpecs.length!=2){
            log.error("invalid subtitle: {}", subtitle);
            return null;
        }

        pmsSpecValues.forEach(x->{
            if(x.getName().equalsIgnoreCase(strSpecs[0]) || x.getName().equalsIgnoreCase(strSpecs[1])) {
                PmsSkuSpecValue pmsSkuSpecValue = new PmsSkuSpecValue();
                pmsSkuSpecValue.setSpecId(x.getSpecId());
                pmsSkuSpecValue.setSpecValueId(x.getId());
                pmsSkuSpecValue.setValueRemark(x.getName());
                skuSpecValues.add(pmsSkuSpecValue);
            }
        });

        return skuSpecValues;
    }

//    public static void main(String[] args) {
//        test3();
//    }
//
//    public static void test1() {
//        try {
//            TableDataInfo items = OneBoundSpuUtils.getInstance().item_search("连衣裙", 1, "en");
//            if (null != items) {
//                log.debug("found items=" + items.getTotal());
//            } else {
//                log.debug("server return null.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void test2() {
//        PmsGoods spu = null;
//        try {
//            spu = OneBoundSpuUtils.getInstance().item_get("936496960", "en");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        log.debug(spu.toString());
//    }

    public static void test3() {
        String json = "{\n" +
                "  \"shipping_to\": \"\",\n" +
                "  \"detail_url\": \"http://www.yiwugo.com/product/detail/936496960.html\",\n" +
                "  \"express_fee\": \"\",\n" +
                "  \"props_alias\": [\n" +
                "    \"白色\",\n" +
                "    \"粉色\",\n" +
                "    \"黄色\",\n" +
                "    \"红色\",\n" +
                "    \"绿色\",\n" +
                "    \"浅蓝色\",\n" +
                "    \"黑色\",\n" +
                "    \"深蓝色\"\n" +
                "  ],\n" +
                "  \"num\": \"171458\",\n" +
                "  \"item_imgs\": {\n" +
                "    \"0\": \"//img1.yiwugou.com/i004/2021/09/13/47/8545dba8c03642aaa86e546014cb43df.jpg\",\n" +
                "    \"1\": \"//img1.yiwugou.com/i004/2021/09/13/47/d1bcccc18f4a390f45964d6b88555d5f.jpg\",\n" +
                "    \"2\": \"//img1.yiwugou.com/i004/2021/09/13/45/06a7369ddc9d4ad6abda5559b95a5e25.jpg\",\n" +
                "    \"3\": \"//img1.yiwugou.com/i004/2021/09/13/20/d23875e388c585d3a7894be44fcba5fd.jpg\",\n" +
                "    \"4\": \"//img1.yiwugou.com/i004/2021/09/13/91/5b6d3f88bdd17a3c5a1e9b54ea755451.jpg\",\n" +
                "    \"url\": \"\"\n" +
                "  },\n" +
                "  \"num_iid\": \"936496960\",\n" +
                "  \"title\": \"\",\n" +
                "  \"sales\": \"\",\n" +
                "  \"desc_short\": \"\",\n" +
                "  \"props\": {\n" +
                "    \"name\": [\n" +
                "      \" 商品重量: 1.0 kg/件\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"props_list\": [\n" +
                "    {\n" +
                "      \"0:0\": \"颜色:白色\",\n" +
                "      \"0:1\": \"颜色:粉色\",\n" +
                "      \"1:0\": \"尺码:S\",\n" +
                "      \"0:2\": \"颜色:黄色\",\n" +
                "      \"1:1\": \"尺码:M\",\n" +
                "      \"0:3\": \"颜色:红色\",\n" +
                "      \"1:2\": \"尺码:L\",\n" +
                "      \"0:4\": \"颜色:绿色\",\n" +
                "      \"1:3\": \"尺码:XL\",\n" +
                "      \"0:5\": \"颜色:浅蓝色\",\n" +
                "      \"1:4\": \"尺码:XXL\",\n" +
                "      \"0:6\": \"颜色:黑色\",\n" +
                "      \"1:5\": \"尺码:XXXL\",\n" +
                "      \"0:7\": \"颜色:深蓝色\",\n" +
                "      \"1:6\": \"尺码:4XL\",\n" +
                "      \"1:7\": \"尺码:5XL\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"nick\": \"琪琪电子商务商行\",\n" +
                "  \"shop_id\": \"\",\n" +
                "  \"seller_info\": {\n" +
                "    \"nick\": \"琪琪电子商务商行\",\n" +
                "    \"QQ\": \"\",\n" +
                "    \"dizhi\": \"\",\n" +
                "    \"seller_name\": \"\",\n" +
                "    \"phone\": \"\",\n" +
                "    \"user_num_id\": \"\",\n" +
                "    \"desc\": \"厂爆款产品研发基地，专注生产流行热卖产品，主营商品：袜子 手表 内裤 地垫 冰袖 抹布 打火机 拖把 洗发水 浴巾 保温杯 衣架 牙刷 毛巾 对联 雨伞 仿真花\"\n" +
                "  },\n" +
                "  \"min_num\": \"2\",\n" +
                "  \"price\": \"27\",\n" +
                "  \"prop_imgs\": {\n" +
                "    \"0\": \"//img1.yiwugou.com/i004/2021/09/13/00/fe14eaa392954a00eb929f5bb18d40d1.jpg\",\n" +
                "    \"1\": \"//img1.yiwugou.com/i004/2021/09/13/10/da2df6fdbc32ca51530b805144b338db.jpg\",\n" +
                "    \"2\": \"//img1.yiwugou.com/i004/2021/09/13/38/b31cb3cb70317135868c84eb8bf66518.jpg\",\n" +
                "    \"3\": \"//img1.yiwugou.com/i004/2021/09/13/46/a00961b1efcfada211d77e53da537749.jpg\",\n" +
                "    \"4\": \"//img1.yiwugou.com/i004/2021/09/13/55/1e2ae207d7d120fa053df30691b319a0.jpg\",\n" +
                "    \"5\": \"//img1.yiwugou.com/i004/2021/09/13/68/0996a0272b9881c7e8b9cac3746b8721.jpg\",\n" +
                "    \"6\": \"//img1.yiwugou.com/i004/2021/09/13/61/813d70b58658ae33652e9de1943f0076.jpg\",\n" +
                "    \"7\": \"//img1.yiwugou.com/i004/2021/09/13/18/6e81784089ec4a473fe934ff54cf50f4.jpg\",\n" +
                "    \"properties\": \"0\"\n" +
                "  },\n" +
                "  \"pic_url\": \"//img1.yiwugou.com/i004/2021/09/13/47/8545dba8c03642aaa86e546014cb43df.jpg\",\n" +
                "  \"sku\": [\n" +
                "    [\n" +
                "      {\n" +
                "        \"properties_name\": \"0:0：颜色:白色1:0:尺码:S\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:0;1:0\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:0：颜色:白色1:1:尺码:M\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:0;1:1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:0：颜色:白色1:2:尺码:L\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:0;1:2\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:0：颜色:白色1:3:尺码:XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:0;1:3\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:0：颜色:白色1:4:尺码:XXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:0;1:4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:0：颜色:白色1:5:尺码:XXXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:0;1:5\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:0：颜色:白色1:6:尺码:4XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:0;1:6\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:0：颜色:白色1:7:尺码:5XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:0;1:7\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:1：颜色:粉色1:0:尺码:S\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:1;1:0\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:1：颜色:粉色1:1:尺码:M\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:1;1:1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:1：颜色:粉色1:2:尺码:L\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:1;1:2\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:1：颜色:粉色1:3:尺码:XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:1;1:3\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:1：颜色:粉色1:4:尺码:XXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:1;1:4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:1：颜色:粉色1:5:尺码:XXXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:1;1:5\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:1：颜色:粉色1:6:尺码:4XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:1;1:6\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:1：颜色:粉色1:7:尺码:5XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:1;1:7\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:2：颜色:黄色1:0:尺码:S\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:2;1:0\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:2：颜色:黄色1:1:尺码:M\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:2;1:1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:2：颜色:黄色1:2:尺码:L\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:2;1:2\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:2：颜色:黄色1:3:尺码:XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:2;1:3\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:2：颜色:黄色1:4:尺码:XXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:2;1:4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:2：颜色:黄色1:5:尺码:XXXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:2;1:5\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:2：颜色:黄色1:6:尺码:4XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:2;1:6\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:2：颜色:黄色1:7:尺码:5XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:2;1:7\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:3：颜色:红色1:0:尺码:S\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:3;1:0\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:3：颜色:红色1:1:尺码:M\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:3;1:1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:3：颜色:红色1:2:尺码:L\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:3;1:2\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:3：颜色:红色1:3:尺码:XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:3;1:3\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:3：颜色:红色1:4:尺码:XXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:3;1:4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:3：颜色:红色1:5:尺码:XXXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:3;1:5\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:3：颜色:红色1:6:尺码:4XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:3;1:6\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:3：颜色:红色1:7:尺码:5XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:3;1:7\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:4：颜色:绿色1:0:尺码:S\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:4;1:0\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:4：颜色:绿色1:1:尺码:M\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:4;1:1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:4：颜色:绿色1:2:尺码:L\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:4;1:2\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:4：颜色:绿色1:3:尺码:XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:4;1:3\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:4：颜色:绿色1:4:尺码:XXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:4;1:4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:4：颜色:绿色1:5:尺码:XXXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:4;1:5\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:4：颜色:绿色1:6:尺码:4XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:4;1:6\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:4：颜色:绿色1:7:尺码:5XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:4;1:7\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:5：颜色:浅蓝色1:0:尺码:S\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:5;1:0\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:5：颜色:浅蓝色1:1:尺码:M\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:5;1:1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:5：颜色:浅蓝色1:2:尺码:L\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:5;1:2\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:5：颜色:浅蓝色1:3:尺码:XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:5;1:3\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:5：颜色:浅蓝色1:4:尺码:XXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:5;1:4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:5：颜色:浅蓝色1:5:尺码:XXXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:5;1:5\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:5：颜色:浅蓝色1:6:尺码:4XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:5;1:6\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:5：颜色:浅蓝色1:7:尺码:5XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:5;1:7\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:6：颜色:黑色1:0:尺码:S\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:6;1:0\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:6：颜色:黑色1:1:尺码:M\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:6;1:1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:6：颜色:黑色1:2:尺码:L\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:6;1:2\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:6：颜色:黑色1:3:尺码:XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:6;1:3\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:6：颜色:黑色1:4:尺码:XXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:6;1:4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:6：颜色:黑色1:5:尺码:XXXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:6;1:5\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:6：颜色:黑色1:6:尺码:4XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:6;1:6\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:6：颜色:黑色1:7:尺码:5XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:6;1:7\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:7：颜色:深蓝色1:0:尺码:S\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:7;1:0\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:7：颜色:深蓝色1:1:尺码:M\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:7;1:1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:7：颜色:深蓝色1:2:尺码:L\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:7;1:2\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:7：颜色:深蓝色1:3:尺码:XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:7;1:3\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:7：颜色:深蓝色1:4:尺码:XXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:7;1:4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:7：颜色:深蓝色1:5:尺码:XXXL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:7;1:5\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:7：颜色:深蓝色1:6:尺码:4XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:7;1:6\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"properties_name\": \"0:7：颜色:深蓝色1:7:尺码:5XL\",\n" +
                "        \"quantity\": \"171458\",\n" +
                "        \"properties\": \"0:7;1:7\"\n" +
                "      }\n" +
                "    ]\n" +
                "  ],\n" +
                "  \"seller_id\": \"\",\n" +
                "  \"desc\": \"<p><img alt=\\\"09\\\" height=\\\"412.3781212841855\\\" src=\\\"https://cbu01.alicdn.com/img/ibank/O1CN01D2slD61WqlUmPx2x1_!!2208127942840-0-cib.jpg\\\" width=\\\"790\\\" /><br /><br /><img alt=\\\"01\\\" src=\\\"https://cbu01.alicdn.com/img/ibank/O1CN01MIZPyW1WqlUfkXyec_!!2208127942840-0-cib.jpg\\\" /><br /><br /><img alt=\\\"02\\\" src=\\\"https://cbu01.alicdn.com/img/ibank/O1CN01dkMasZ1WqlUlPz8wH_!!2208127942840-0-cib.jpg\\\" /><br /><br /><img alt=\\\"03\\\" src=\\\"https://cbu01.alicdn.com/img/ibank/O1CN016TmgN21WqlUkyveUs_!!2208127942840-0-cib.jpg\\\" /><br /><br /><img alt=\\\"04\\\" src=\\\"https://cbu01.alicdn.com/img/ibank/O1CN01QoxzUE1WqlUkyu6pw_!!2208127942840-0-cib.jpg\\\" /><br /><br /><img alt=\\\"05\\\" src=\\\"https://cbu01.alicdn.com/img/ibank/O1CN01QwOznK1WqlUlPyCik_!!2208127942840-0-cib.jpg\\\" /><br /><br /><img alt=\\\"06\\\" src=\\\"https://cbu01.alicdn.com/img/ibank/O1CN0175BG6a1WqlUoU9u1b_!!2208127942840-0-cib.jpg\\\" /><br /><br /><img alt=\\\"08\\\" src=\\\"https://cbu01.alicdn.com/img/ibank/O1CN015BoqDO1WqlUiq6KkX_!!2208127942840-0-cib.jpg\\\" /><br /><br /><img alt=\\\"011 (2)\\\" src=\\\"https://cbu01.alicdn.com/img/ibank/O1CN01OrmE8L1WqlV9hyqzF_!!2208127942840-0-cib.jpg\\\" /><br /><br /><img alt=\\\"5c6b42771447b2d3c3b5509b6d0349\\\" src=\\\"https://cbu01.alicdn.com/img/ibank/O1CN01fBegPb1WqlVuidvwn_!!2208127942840-0-cib.jpg\\\" /><br /><br /></p>\"\n" +
                "}";

//        OneBoundSpuUtils oneBoundSpuUtils = new OneBoundSpuUtils();
//        oneBoundSpuUtils.convertAndSaveToPmsGoods("test", 0L,0L, 0L, 0L, json);
    }

    /**
     * 换算为美元
     * @param price
     * @return
     */
    private BigDecimal convertPrice2Usd(BigDecimal price){
        BigDecimal rate = redisMallHelper.getUSDCNYRate();
        if(rate == null || BigDecimal.ZERO.compareTo(rate)==0){
            rate = new BigDecimal("6.330");
        }
        return price.divide(rate,2, RoundingMode.HALF_DOWN);
    }
}
