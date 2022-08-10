package io.uhha.sms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import lombok.*;

/**
 * 首页推荐专题对象 sms_home_recommend_subject
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SmsHomeRecommendSubject extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 话题id
     */
    @Excel(name = "话题id")
    private Long subjectId;

    /**
     * 话题名
     */
    @Excel(name = "话题名")
    private String subjectName;

    /**
     * 推荐状态
     */
    @Excel(name = "推荐状态")
    private Integer recommendStatus;

    /**
     * 序号
     */
    @Excel(name = "${comment}")
    private Integer sort;

    /**
     * 所属店铺
     */
    @Excel(name = "所属店铺")
    private Long storeId;

    /**
     * 图片
     */
    @Excel(name = "图片")
    private String pic;

    /**
     * 阅读数
     */
    @Excel(name = "所属店铺")
    private Integer readCount;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String description;


}
