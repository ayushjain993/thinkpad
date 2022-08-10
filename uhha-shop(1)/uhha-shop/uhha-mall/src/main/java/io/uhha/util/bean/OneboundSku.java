package io.uhha.util.bean;

import lombok.Data;

@Data
public class OneboundSku {
    /**
     * {"sku": [{"properties": "0:0", "properties_name": "0:0:颜色:小号", "price": "7.70", "orginal_price": "7.70", "quantity": 716, "sku_id": 3184603612387 }]}
     */
    private String properties;
    private String properties_name;
    private String price;
    private String quantity;
    private String orginal_price;
    private String sku_id;
}

