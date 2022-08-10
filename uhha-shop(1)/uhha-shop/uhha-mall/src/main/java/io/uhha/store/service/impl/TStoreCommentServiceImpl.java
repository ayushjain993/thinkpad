package io.uhha.store.service.impl;

import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.enums.ImUserTypeEnum;
import io.uhha.common.utils.DateUtils;
import io.uhha.im.domain.ImUser;
import io.uhha.im.service.ImUserService;
import io.uhha.store.domain.TStoreComment;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.mapper.TStoreCommentMapper;
import io.uhha.store.service.AttentionStoreService;
import io.uhha.store.service.ITStoreCommentService;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.store.vo.StoreScore;
import io.uhha.system.service.ISysStoreUserService;
import io.uhha.system.service.ISysUserService;
import io.uhha.util.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 店铺评论Service业务层处理
 *
 * @author mj
 * @date 2020-07-28
 */
@Service
public class TStoreCommentServiceImpl implements ITStoreCommentService {
    private static final Logger logger = LoggerFactory.getLogger(TStoreCommentServiceImpl.class);
    @Autowired
    private TStoreCommentMapper tStoreCommentMapper;

    /**
     * 注入店铺服务接口
     */
    @Autowired
    private ITStoreInfoService storeInfoService;

    /**
     * 注入店铺关注服务接口
     */
    @Autowired
    private AttentionStoreService attentionStoreService;

    /**
     * im服务接口
     */
    @Autowired
    private ImUserService imUserService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysStoreUserService sysStoreUserService;


    /**
     * 查询店铺评论
     *
     * @param id 店铺评论ID
     * @return 店铺评论
     */
    @Override
    public TStoreComment selectTStoreCommentById(Long id) {
        return tStoreCommentMapper.selectTStoreCommentById(id);
    }

    /**
     * 查询店铺评论列表
     *
     * @param tStoreComment 店铺评论
     * @return 店铺评论
     */
    @Override
    public List<TStoreComment> selectTStoreCommentList(TStoreComment tStoreComment) {
        return tStoreCommentMapper.selectTStoreCommentList(tStoreComment);
    }

    @Override
    public TStoreComment queryByOrderIdAndCutomerId(long orderId, long customerId) {
        logger.debug("queryByOrderIdAndCutomerId and orderId:{} \r\n customerId:{}", orderId, customerId);
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("customerId", customerId);

        // 店铺评论
        TStoreComment storeComment = tStoreCommentMapper.queryByOrderIdAndCutomerId(params);

        if (Objects.isNull(storeComment)) {
            return storeComment;
        }

        // 店铺信息
        TStoreInfo storeInfo = storeInfoService.queryStoreInfo(storeComment.getStoreId());

        if (Objects.nonNull(storeInfo)) {
            storeComment.setStoreName(storeInfo.getStoreName());
        }

        return storeComment;
    }

    @Override
    public StoreScore queryStoreScore(Long storeId) {
        logger.debug("queryStoreScore and storeId:{}", storeId);
        if (ObjectUtils.isEmpty(storeId)) {
            logger.error("queryStoreScore fail: storeId is null");
            return null;
        }
        return tStoreCommentMapper.queryStoreScore(storeId);
    }

    @Override
    public StoreScore queryStoreScoreWithStoreInfo(Long storeId) {
        logger.debug("queryStoreScoreWithStoreInfo and storeId:{}", storeId);
        if (ObjectUtils.isEmpty(storeId)) {
            logger.error("queryStoreScoreWithStoreInfo fail: storeId is null");
            return null;
        }
        StoreScore storeScore = tStoreCommentMapper.queryStoreScore(storeId);
        if (ObjectUtils.isEmpty(storeScore)) {
            storeScore = new StoreScore();
        }
        //设置店铺信息
        TStoreInfo storeInfo = storeInfoService.queryStoreInfo(storeId);
        //设置店铺关注人数
        if (!ObjectUtils.isEmpty(storeInfo)) {
            //如果平台自营，设置默认关注人数
            ImUserTypeEnum imUserType = null;
            List<SysUser> sysUsers;

            sysUsers = sysStoreUserService.getStoreSysUserByStoreId(storeId);
            imUserType = ImUserTypeEnum.STORE;
            storeInfo.setFollowNum(attentionStoreService.queryNumByStore(storeId));
            if (!CollectionUtils.isEmpty(sysUsers)) {
                ImUserTypeEnum finalImUserType = imUserType;
                sysUsers.stream().anyMatch(sysUser -> {
                    ImUser imUser = imUserService.queryImUserInfo(finalImUserType, String.valueOf(sysUser.getUserId()));
                    boolean flag = false;
                    if (imUser != null) {
                        storeInfo.setUserCode(imUser.getUserCode());
                        flag = true;
                    }
                    return flag;
                });
            }
        }
        return storeScore.buildStoreInfo(storeInfo);
    }

    /**
     * 新增店铺评论
     *
     * @param tStoreComment 店铺评论
     * @return 结果
     */
    @Override
    public int insertTStoreComment(TStoreComment tStoreComment) {
        tStoreComment.setCreateTime(DateUtils.getNowDate());
        return tStoreCommentMapper.insertTStoreComment(tStoreComment);
    }

    /**
     * 修改店铺评论
     *
     * @param tStoreComment 店铺评论
     * @return 结果
     */
    @Override
    public int updateTStoreComment(TStoreComment tStoreComment) {
        return tStoreCommentMapper.updateTStoreComment(tStoreComment);
    }

    /**
     * 批量删除店铺评论
     *
     * @param ids 需要删除的店铺评论ID
     * @return 结果
     */
    @Override
    public int deleteTStoreCommentByIds(Long[] ids) {
        return tStoreCommentMapper.deleteTStoreCommentByIds(ids);
    }

    /**
     * 删除店铺评论信息
     *
     * @param id 店铺评论ID
     * @return 结果
     */
    @Override
    public int deleteTStoreCommentById(Long id) {
        return tStoreCommentMapper.deleteTStoreCommentById(id);
    }
}
