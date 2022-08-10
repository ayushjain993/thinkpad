package io.uhha.member.service.impl;

import io.uhha.common.utils.DateUtils;
import io.uhha.member.domain.UmsMemberPoint;
import io.uhha.member.mapper.UmsMemberPointMapper;
import io.uhha.member.service.IUmsMemberPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员积分详情Service业务层处理
 *
 * @author mj
 * @date 2020-07-25
 */
@Service
public class UmsMemberPointServiceImpl implements IUmsMemberPointService {
    @Autowired
    private UmsMemberPointMapper umsMemberPointMapper;

    /**
     * 查询会员积分详情
     *
     * @param id 会员积分详情ID
     * @return 会员积分详情
     */
    @Override
    public UmsMemberPoint selectUmsMemberPointById(Long id) {
        return umsMemberPointMapper.selectUmsMemberPointById(id);
    }

    /**
     * 查询会员积分详情列表
     *
     * @param umsMemberPoint 会员积分详情
     * @return 会员积分详情
     */
    @Override
    public List<UmsMemberPoint> selectUmsMemberPointList(UmsMemberPoint umsMemberPoint) {
        return umsMemberPointMapper.selectUmsMemberPointList(umsMemberPoint);
    }

    /**
     * 新增会员积分详情
     *
     * @param umsMemberPoint 会员积分详情
     * @return 结果
     */
    @Override
    public int insertUmsMemberPoint(UmsMemberPoint umsMemberPoint) {
        umsMemberPoint.setCreateTime(DateUtils.getNowDate());
        return umsMemberPointMapper.insertUmsMemberPoint(umsMemberPoint);
    }

    /**
     * 修改会员积分详情
     *
     * @param umsMemberPoint 会员积分详情
     * @return 结果
     */
    @Override
    public int updateUmsMemberPoint(UmsMemberPoint umsMemberPoint) {
        return umsMemberPointMapper.updateUmsMemberPoint(umsMemberPoint);
    }

    /**
     * 批量删除会员积分详情
     *
     * @param ids 需要删除的会员积分详情ID
     * @return 结果
     */
    @Override
    public int deleteUmsMemberPointByIds(Long[] ids) {
        return umsMemberPointMapper.deleteUmsMemberPointByIds(ids);
    }

    /**
     * 删除会员积分详情信息
     *
     * @param id 会员积分详情ID
     * @return 结果
     */
    @Override
    public int deleteUmsMemberPointById(Long id) {
        return umsMemberPointMapper.deleteUmsMemberPointById(id);
    }
}
