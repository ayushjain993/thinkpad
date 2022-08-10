package io.uhha.coin.common.domain;

import java.io.Serializable;

/**
 * 用户API
 * @author ZGY
 */
public class FApi implements Serializable {

    private static final long serialVersionUID = 8735837042417629874L;
    // 主键Id
    private Integer fid;
    // 用户Id
    private Integer fuid;
    // API访问密钥
    private String apiaccesskey;
    // API私有密钥
    private String apisecretkey;
    // 密钥绑定IP
    private String apiip;

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getFuid() {
        return fuid;
    }

    public void setFuid(Integer fuid) {
        this.fuid = fuid;
    }

    public String getApiaccesskey() {
        return apiaccesskey;
    }

    public void setApiaccesskey(String apiaccesskey) {
        this.apiaccesskey = apiaccesskey;
    }

    public String getApisecretkey() {
        return apisecretkey;
    }

    public void setApisecretkey(String apisecretkey) {
        this.apisecretkey = apisecretkey;
    }

    public String getApiip() {
        return apiip;
    }

    public void setApiip(String apiip) {
        this.apiip = apiip;
    }
}
