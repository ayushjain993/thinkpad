package io.uhha.sms.mapper;

import io.uhha.sms.domain.SmsHomeAdvertise;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 首页轮播广告Mapper接口
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Mapper
public interface SmsHomeAdvertiseMapper {
    /**
     * 查询首页轮播广告
     *
     * @param id 首页轮播广告ID
     * @return 首页轮播广告
     */
    public SmsHomeAdvertise selectSmsHomeAdvertiseById(Long id);

    /**
     * 查询首页轮播广告列表
     *
     * @param smsHomeAdvertise 首页轮播广告
     * @return 首页轮播广告集合
     */
    public List<SmsHomeAdvertise> selectSmsHomeAdvertiseList(SmsHomeAdvertise smsHomeAdvertise);

    /**
     * 新增首页轮播广告
     *
     * @param smsHomeAdvertise 首页轮播广告
     * @return 结果
     */
    public int insertSmsHomeAdvertise(SmsHomeAdvertise smsHomeAdvertise);

    /**
     * 修改首页轮播广告
     *
     * @param smsHomeAdvertise 首页轮播广告
     * @return 结果
     */
    public int updateSmsHomeAdvertise(SmsHomeAdvertise smsHomeAdvertise);

    /**
     * 删除首页轮播广告
     *
     * @param id 首页轮播广告ID
     * @return 结果
     */
    public int deleteSmsHomeAdvertiseById(Long id);

    /**
     * 批量删除首页轮播广告
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSmsHomeAdvertiseByIds(Long[] ids);
}
