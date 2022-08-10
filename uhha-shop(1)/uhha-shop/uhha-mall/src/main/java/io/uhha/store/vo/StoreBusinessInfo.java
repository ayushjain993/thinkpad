package io.uhha.store.vo;


import io.uhha.goods.domain.PmsBrand;
import io.uhha.goods.domain.PmsCategory;
import io.uhha.store.domain.TStoreInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 开店信息实体类用于开店流程查询
 *
 * @author mj on 2017/6/20.
 */
@Data
@ApiModel(description = "开店信息实体类用于开店流程查询")
public class StoreBusinessInfo {

    /**
     * 公司信息
     */
    @ApiModelProperty(value = "公司信息")
    private TStoreInfo storeInfo;
    /**
     * 二级分类
     */
    @ApiModelProperty(value = "二级分类")
    private List<PmsCategory> twoCategoryList;
    /**
     * 三级分类（签约分类）
     */
    @ApiModelProperty(value = "三级分类（签约分类）")
    private List<PmsCategory> threeCategoryList;
    /**
     * 主营店铺品牌
     */
    @ApiModelProperty(value = "主营店铺品牌")
    private List<PmsBrand> storeBrandList;
    /**
     * 自定义品牌
     */
    @ApiModelProperty(value = "自定义品牌")
    private List<PmsBrand> mySelfBrandList;

    public StoreBusinessInfo getStoreBusinessInfo(TStoreInfo storeInfo, List<PmsCategory> twoCategoryList, List<PmsCategory> threeCategoryList, List<PmsBrand> storeBrandList, List<PmsBrand> mySelfBrandList) {
        this.storeInfo = storeInfo;
        this.twoCategoryList = twoCategoryList;
        this.threeCategoryList = threeCategoryList;
        this.storeBrandList = storeBrandList;
        this.mySelfBrandList = mySelfBrandList;
        return this;
    }

}
