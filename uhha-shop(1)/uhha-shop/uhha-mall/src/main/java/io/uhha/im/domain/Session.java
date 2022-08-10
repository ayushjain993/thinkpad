package io.uhha.im.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("im_session")
public class Session implements Serializable {

    private static final transient long serialVersionUID = 1L;
    public static final transient int STATE_ACTIVE = 0;
    public static final transient int STATE_INACTIVE = 2;

    public static final transient String CHANNEL_IOS = "ios";
    public static final transient String CHANNEL_ANDROID = "android";
    public static final transient String CHANNEL_WINDOWS = "windows";
    public static final transient String CHANNEL_MAC = "mac";
    public static final transient String CHANNEL_WEB = "web";

    /**
     * 数据库主键ID
     */
   @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * session绑定的用户账号
     */
    private String uid;

    /**
     * session在本台服务器上的ID
     */
    private String nid;

    /**
     * 客户端ID (设备号码+应用包名),ios为deviceToken
     */

    private String deviceId;

    /**
     * 终端设备型号
     */
    private String deviceName;

    /**
     * session绑定的服务器IP
     */
    private String host;

    /**
     * 终端设备类型
     */
    private String channel;

    /**
     * 终端应用版本
     */
    private String appVersion;

    /**
     * 终端系统版本
     */
    private String osVersion;

    /**
     * 终端语言
     */
    private String language;

    /**
     * 登录时间
     */
    private Long bindTime;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 维度
     */
    private Double latitude;

    /**
     * 位置
     */
    private String location;

    /**
     * 状态
     */
    private int state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Long getBindTime() {
        return bindTime;
    }

    public void setBindTime(Long bindTime) {
        this.bindTime = bindTime;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
