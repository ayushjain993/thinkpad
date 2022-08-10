package io.uhha.goods.service.impl;

import io.uhha.common.utils.DateUtils;
import io.uhha.goods.domain.PmsCommentReplay;
import io.uhha.goods.mapper.PmsCommentReplayMapper;
import io.uhha.goods.service.IPmsCommentReplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论回复Service业务层处理
 *
 * @author mj
 * @date 2020-07-24
 */
@Service
public class PmsCommentReplayServiceImpl implements IPmsCommentReplayService {
    @Autowired
    private PmsCommentReplayMapper pmsCommentReplayMapper;

    /**
     * 查询评论回复
     *
     * @param id 评论回复ID
     * @return 评论回复
     */
    @Override
    public PmsCommentReplay selectPmsCommentReplayById(Long id) {
        return pmsCommentReplayMapper.selectPmsCommentReplayById(id);
    }

    /**
     * 查询评论回复列表
     *
     * @param pmsCommentReplay 评论回复
     * @return 评论回复
     */
    @Override
    public List<PmsCommentReplay> selectPmsCommentReplayList(PmsCommentReplay pmsCommentReplay) {
        return pmsCommentReplayMapper.selectPmsCommentReplayList(pmsCommentReplay);
    }

    /**
     * 新增评论回复
     *
     * @param pmsCommentReplay 评论回复
     * @return 结果
     */
    @Override
    public int insertPmsCommentReplay(PmsCommentReplay pmsCommentReplay) {
        pmsCommentReplay.setCreateTime(DateUtils.getNowDate());
        return pmsCommentReplayMapper.insertPmsCommentReplay(pmsCommentReplay);
    }

    /**
     * 修改评论回复
     *
     * @param pmsCommentReplay 评论回复
     * @return 结果
     */
    @Override
    public int updatePmsCommentReplay(PmsCommentReplay pmsCommentReplay) {
        return pmsCommentReplayMapper.updatePmsCommentReplay(pmsCommentReplay);
    }

    /**
     * 批量删除评论回复
     *
     * @param ids 需要删除的评论回复ID
     * @return 结果
     */
    @Override
    public int deletePmsCommentReplayByIds(Long[] ids) {
        return pmsCommentReplayMapper.deletePmsCommentReplayByIds(ids);
    }

    /**
     * 删除评论回复信息
     *
     * @param id 评论回复ID
     * @return 结果
     */
    @Override
    public int deletePmsCommentReplayById(Long id) {
        return pmsCommentReplayMapper.deletePmsCommentReplayById(id);
    }
}
