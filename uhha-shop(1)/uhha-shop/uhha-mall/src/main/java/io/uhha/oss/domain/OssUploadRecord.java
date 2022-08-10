package io.uhha.oss.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.DataSource;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * OSS对象存储系统上传记录对象 oss_upload_record
 * 
 * @author uhha
 * @date 2021-09-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OssUploadRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;

    /** 供应商 */
    @Excel(name = "供应商")
    private String vendor;

    /** referer url */
    @Excel(name = "referer url")
    private String referer;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String type;

    /** 外部访问路径 */
    @Excel(name = "外部访问路径")
    private String extUrl;

    /** 供应商对象id */
    @Excel(name = "供应商对象id")
    private String objectid;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /** 删除者 */
    @Excel(name = "删除者")
    private String delBy;

    /** 删除时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deleteTime;
}
