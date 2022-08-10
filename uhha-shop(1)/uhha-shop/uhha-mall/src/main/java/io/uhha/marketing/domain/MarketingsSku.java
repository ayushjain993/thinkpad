package io.uhha.marketing.domain;

import io.uhha.goods.domain.PmsSku;
import lombok.Data;

@Data
public class MarketingsSku  extends PmsSku {
    
    private String marketingPrice;
    private String type;
    private String marketingType;
    private String marketingName;

}
