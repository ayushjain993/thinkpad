package io.uhha.coin.system.domain;

import java.io.Serializable;

/**
 * CR
 * 币种资料多语言列表
 */
public class SystemCoinTypeLan implements Serializable {
    private static final long serialVersionUID = 1L;
    // 主键ID
    private Integer id;
    //coinid
    private Integer coinId;
    // 硬币简介简写
    private String shortName;
    // 硬币简介
    private String coinInstroduce;
    //更新管理员
    private Integer fadminId;
    //币种优势
    private String coinAdvantage;
    //多语言1、简体中文 2、繁体中文 3、英文 4、韩文 5、日文
    private Integer lanId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public String getCoinInstroduce() {
        return coinInstroduce;
    }

    public void setCoinInstroduce(String coinInstroduce) {
        this.coinInstroduce = coinInstroduce;
    }

    public Integer getFadminId() {
        return fadminId;
    }

    public void setFadminId(Integer fadminId) {
        this.fadminId = fadminId;
    }

    public String getCoinAdvantage() {
        return coinAdvantage;
    }

    public void setCoinAdvantage(String coinAdvantage) {
        this.coinAdvantage = coinAdvantage;
    }

    public Integer getLanId() {
        return lanId;
    }

    public void setLanId(Integer lanId) {
        this.lanId = lanId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}