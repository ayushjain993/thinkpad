package io.uhha.shortvideo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UmsMemberAttention {
    private Long id;

    private Long uid;

    private Long followedUid;

    private Date createTime;
}
