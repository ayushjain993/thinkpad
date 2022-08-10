package io.uhha.statistics.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import io.uhha.common.utils.CustomLocalDateTimeDeserializer;
import io.uhha.common.utils.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PvuvStatistics {
    private Long pv;
    private Long uv;
    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    private LocalDateTime createTime;
}
