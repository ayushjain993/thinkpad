package io.uhha.marketing.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.uhha.common.utils.CustomLocalDateTimeDeserializer;
import io.uhha.common.utils.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by mj on 2018/4/23
 * <p>
 * 众筹进度实体类
 */
@Data
@ApiModel(description = "众筹进度实体")
public class CrowdfundingProgress {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private long id;

    /**
     * 众筹id  对应sms_marketing表中的id
     */
    @ApiModelProperty(value = "众筹id  对应sms_marketing表中的id")
    private long marketingId;

    /**
     * 进度描述
     */
    @ApiModelProperty(value = "进度描述")
    private String desc;

    /**
     * 时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "时间")
    private LocalDateTime time;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String picUrl;
}
