package io.uhha.shortvideo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmsMemberLikes {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull
    private Long uid;

    @NotNull
    private String parentId;

    @NotNull
    private String resourceId;

    @NotNull
    private Long resourceOwnerId;

    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
