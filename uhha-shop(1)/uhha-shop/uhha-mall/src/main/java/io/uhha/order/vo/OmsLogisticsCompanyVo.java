package io.uhha.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class OmsLogisticsCompanyVo {
    /**
     * 主键id
     */
    private Long id;


    /**
     * 店铺id
     */
    @Excel(name = "店铺id")
    private Long storeId;

    /**
     * 店铺使用的物流公司 id
     */
    @Excel(name = "店铺使用的物流公司 id ")
    private Long companyId;

    /**
     * 物流公司名称
     */
    @Excel(name = "物流公司名称")
    private String name;

    /**
     * 物流公司代码
     */
    @Excel(name = "物流公司代码")
    private String code;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modifyTime;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date delTime;
}
