package io.uhha.sms.mapper;

import io.uhha.sms.domain.SmsHomeRecommendSubject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 首页推荐专题Mapper接口
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Mapper
public interface SmsHomeRecommendSubjectMapper {
    /**
     * 查询首页推荐专题
     *
     * @param id 首页推荐专题ID
     * @return 首页推荐专题
     */
    public SmsHomeRecommendSubject selectSmsHomeRecommendSubjectById(Long id);

    /**
     * 查询首页推荐专题列表
     *
     * @param smsHomeRecommendSubject 首页推荐专题
     * @return 首页推荐专题集合
     */
    public List<SmsHomeRecommendSubject> selectSmsHomeRecommendSubjectList(SmsHomeRecommendSubject smsHomeRecommendSubject);

    /**
     * 新增首页推荐专题
     *
     * @param smsHomeRecommendSubject 首页推荐专题
     * @return 结果
     */
    public int insertSmsHomeRecommendSubject(SmsHomeRecommendSubject smsHomeRecommendSubject);

    /**
     * 修改首页推荐专题
     *
     * @param smsHomeRecommendSubject 首页推荐专题
     * @return 结果
     */
    public int updateSmsHomeRecommendSubject(SmsHomeRecommendSubject smsHomeRecommendSubject);

    /**
     * 删除首页推荐专题
     *
     * @param id 首页推荐专题ID
     * @return 结果
     */
    public int deleteSmsHomeRecommendSubjectById(Long id);

    /**
     * 批量删除首页推荐专题
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSmsHomeRecommendSubjectByIds(Long[] ids);
}
