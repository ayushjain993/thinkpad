package io.uhha.coin.common.dto.system;

import java.io.Serializable;
//友情链接类
public class Links implements Serializable {
    private static final long serialVersionUID = -5896357206972915438L;
    private String url; //友情链接url
    private String name; //友情链接公司名称


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
