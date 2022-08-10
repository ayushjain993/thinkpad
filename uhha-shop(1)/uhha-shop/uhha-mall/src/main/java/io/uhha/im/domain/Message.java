package io.uhha.im.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息对象
 */
@Data
@TableName("im_message")
@Accessors(chain = true)
public class Message {

    /**
     * 消息类型，用户自定义消息类别
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息类型，用户自定义消息类别
     */
    private String action;
    /**
     * 1单聊2群聊
     */
    private Integer type;
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息类容，于action 组合为任何类型消息，content 根据 format 可表示为 text,json ,xml数据格式
     */
    private String content;

    /**
     * 消息发送者账号
     */
    private String sender;
    /**
     * 消息发送者接收者
     */
    private String receiver;

    /**
     * content 内容格式
     */
    private String format;

    /**
     * 是否已读
     */
    private Boolean readStatus;

    /**
     * 附加内容 内容
     */
    private String extra;

    private long timestamp;

}
