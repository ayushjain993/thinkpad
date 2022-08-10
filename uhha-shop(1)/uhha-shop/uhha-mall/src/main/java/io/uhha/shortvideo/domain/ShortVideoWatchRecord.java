package io.uhha.shortvideo.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;

/**
 * 短视频观看记录对象 short_video_watch_record
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortVideoWatchRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 视频观看记录id */
    private Long id;

    /** 视频发布者uid */
    @Excel(name = "视频发布者uid")
    private Long producerUid;

    /** 观看者uid */
    @Excel(name = "观看者uid")
    private Long uid;

    /** 视频id */
    @Excel(name = "视频id")
    private String videoId;

    /** 视频标签 */
    @Excel(name = "视频标签")
    private String videoTags;

    /** 观看时长小时 */
    @Excel(name = "观看时长小时")
    private Long hours;

    /** 观看时长分钟 */
    @Excel(name = "观看时长分钟")
    private Long minutes;

    /** 观看时长秒 */
    @Excel(name = "观看时长秒")
    private Long seconds;

    /** 观看次数 */
    @Excel(name = "观看次数")
    private Long times;

    /** 观看开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date watchStartTime;

    /** 观看结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

    /** 人职业 */
    @Excel(name = "人职业")
    private String personOccuption;

    /** 性别 */
    @Excel(name = "性别")
    private String personGender;

    /** 兴趣 */
    @Excel(name = "兴趣")
    private String personInterest;

    /** 来源 */
    @Excel(name = "来源")
    private String personSource;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProducerUid(Long producerUid) 
    {
        this.producerUid = producerUid;
    }

    public Long getProducerUid() 
    {
        return producerUid;
    }
    public void setUid(Long uid) 
    {
        this.uid = uid;
    }

    public Long getUid() 
    {
        return uid;
    }
    public void setVideoId(String videoId) 
    {
        this.videoId = videoId;
    }

    public String getVideoId() 
    {
        return videoId;
    }
    public void setVideoTags(String videoTags) 
    {
        this.videoTags = videoTags;
    }

    public String getVideoTags() 
    {
        return videoTags;
    }
    public void setHours(Long hours) 
    {
        this.hours = hours;
    }

    public Long getHours() 
    {
        return hours;
    }
    public void setMinutes(Long minutes) 
    {
        this.minutes = minutes;
    }

    public Long getMinutes() 
    {
        return minutes;
    }
    public void setSeconds(Long seconds) 
    {
        this.seconds = seconds;
    }

    public Long getSeconds() 
    {
        return seconds;
    }
    public void setTimes(Long times) 
    {
        this.times = times;
    }

    public Long getTimes() 
    {
        return times;
    }
    public void setWatchStartTime(Date watchStartTime) 
    {
        this.watchStartTime = watchStartTime;
    }

    public Date getWatchStartTime() 
    {
        return watchStartTime;
    }
    public void setWatchEndTime(Date watchEndTime) 
    {
        this.watchEndTime = watchEndTime;
    }

    public Date getWatchEndTime() 
    {
        return watchEndTime;
    }
    public void setEnvLocation(String envLocation) 
    {
        this.envLocation = envLocation;
    }

    public String getEnvLocation() 
    {
        return envLocation;
    }
    public void setEnvNetwork(String envNetwork) 
    {
        this.envNetwork = envNetwork;
    }

    public String getEnvNetwork() 
    {
        return envNetwork;
    }
    public void setPersonPhoneModel(String personPhoneModel) 
    {
        this.personPhoneModel = personPhoneModel;
    }

    public String getPersonPhoneModel() 
    {
        return personPhoneModel;
    }
    public void setPersonOccuption(String personOccuption) 
    {
        this.personOccuption = personOccuption;
    }

    public String getPersonOccuption() 
    {
        return personOccuption;
    }
    public void setPersonGender(String personGender) 
    {
        this.personGender = personGender;
    }

    public String getPersonGender() 
    {
        return personGender;
    }
    public void setPersonInterest(String personInterest) 
    {
        this.personInterest = personInterest;
    }

    public String getPersonInterest() 
    {
        return personInterest;
    }
    public void setPersonSource(String personSource) 
    {
        this.personSource = personSource;
    }

    public String getPersonSource() 
    {
        return personSource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("producerUid", getProducerUid())
            .append("uid", getUid())
            .append("videoId", getVideoId())
            .append("videoTags", getVideoTags())
            .append("hours", getHours())
            .append("minutes", getMinutes())
            .append("seconds", getSeconds())
            .append("times", getTimes())
            .append("watchStartTime", getWatchStartTime())
            .append("watchEndTime", getWatchEndTime())
            .append("envLocation", getEnvLocation())
            .append("envNetwork", getEnvNetwork())
            .append("personPhoneModel", getPersonPhoneModel())
            .append("personOccuption", getPersonOccuption())
            .append("personGender", getPersonGender())
            .append("personInterest", getPersonInterest())
            .append("personSource", getPersonSource())
            .toString();
    }
}
