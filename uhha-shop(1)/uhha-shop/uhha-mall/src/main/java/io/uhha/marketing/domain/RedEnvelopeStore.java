package io.uhha.marketing.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.uhha.common.utils.CustomLocalDateTimeDeserializer;
import io.uhha.common.utils.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by mj on 18/1/17.
 * 红包和店铺的关联实体
 */
@Data
public class RedEnvelopeStore {

    /**
     * 主键id
     */
    private long id;

    /**
     * 红包id
     */
    private long redEnvelopeId;

    /**
     * 店铺id
     */
    private long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
}
