package io.uhha.statistics.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.uhha.common.annotation.Excel;
import io.uhha.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pv")
public class Pv extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String uid;

    /** 模块 */
    @Excel(name = "模块")
    private String module;

    /** 浏览器 */
    @Excel(name = "浏览器")
    private String browser;

    /** referer */
    @Excel(name = "referer")
    private String referer;

    /** 操作系统 */
    @Excel(name = "操作系统")
    private String os;

    /** 页面ID */
    @Excel(name = "页面ID")
    private String pageId;

    /** URL */
    @Excel(name = "URL")
    private String url;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String deviceType;

    /** 时区 */
    @Excel(name = "时区")
    private String timeZone;

    /** ip地址 */
    @Excel(name = "ip地址")
    private String ip;

    /** 地址 */
    @Excel(name = "地址")
    private String location;


}
