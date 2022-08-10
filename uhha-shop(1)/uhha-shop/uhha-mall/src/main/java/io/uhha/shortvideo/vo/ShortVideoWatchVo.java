package io.uhha.shortvideo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class ShortVideoWatchVo {

    /** 观看者用户id */
    private Long uid;

    /** 视频id */
    @Excel(name = "视频id")
    private String videoId;

    /** 时长小时 */
    @Excel(name = "时长小时")
    private Long hours;

    /** 时长分钟 */
    @Excel(name = "时长分钟")
    private Long minutes;

    /** 时长秒 */
    @Excel(name = "时长秒")
    private Long seconds;

    /** 位置名称 */
    @Excel(name = "位置名称")
    private String location;


    /** 话题列表 */
    @Excel(name = "话题列表")
    private String topicList;

    /** 观看开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "观看开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date watchStartTime;

    /** 观看结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "观看结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date watchEndTime;

    /** 观看大概位置 */
    @Excel(name = "观看大概位置")
    private String envLocation;

    /** 使用网络（移动、WIFI） */
    @Excel(name = "使用网络", readConverterExp = "移=动、WIFI")
    private String envNetwork;

    /** 人使用机型 */
    @Excel(name = "人使用机型")
    private String personPhoneModel;

    /**
     * 性别  0 保密 1男 2女 默认0
     */
    @Excel(name = "性别  0 保密 1男 2女 默认0")
    private String gender;

    /**
     * 兴趣爱好
     */
    @Excel(name = "兴趣爱好")
    private String interest;
}
