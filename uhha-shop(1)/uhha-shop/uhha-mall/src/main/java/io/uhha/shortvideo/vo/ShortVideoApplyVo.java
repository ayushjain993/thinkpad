package io.uhha.shortvideo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ShortVideoApplyVo {
    private String videoId;
    private Long uid;
    private String reason;
    private String status;
    private Date createTime;
}
