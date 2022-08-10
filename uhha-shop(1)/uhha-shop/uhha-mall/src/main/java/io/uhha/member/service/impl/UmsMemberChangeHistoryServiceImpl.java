package io.uhha.member.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.enums.UmsMemberChangeEventEnum;
import io.uhha.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.member.mapper.UmsMemberChangeHistoryMapper;
import io.uhha.member.domain.UmsMemberChangeHistory;
import io.uhha.member.service.IUmsMemberChangeHistoryService;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户修改历史记录Service业务层处理
 *
 * @author peter
 * @date 2021-12-16
 */
@Service
public class UmsMemberChangeHistoryServiceImpl extends ServiceImpl<UmsMemberChangeHistoryMapper, UmsMemberChangeHistory> implements IUmsMemberChangeHistoryService
{
    @Autowired
    private UmsMemberChangeHistoryMapper umsMemberChangeHistoryMapper;

    /**
     * 查询用户修改历史记录列表
     *
     * @param id 用户id
     * @return 用户修改历史记录
     */
    @Override
    public List<UmsMemberChangeHistory> selectUmsMemberChangeHistoryList(String mobile)
    {
        return umsMemberChangeHistoryMapper.selectUmsMemberChangeHistoryList(mobile);
    }

    /**
     * 新增用户修改历史记录
     *
     * @param umsMemberChangeHistory 用户修改历史记录
     * @return 结果
     */
    @Override
    public int insertUmsMemberChangeHistory(UmsMemberChangeHistory umsMemberChangeHistory)
    {
        umsMemberChangeHistory.setCreateTime(DateUtils.getNowDate());
        return umsMemberChangeHistoryMapper.insertUmsMemberChangeHistory(umsMemberChangeHistory);
    }

    /**
     * 记录用户修改日志
     * @param eventEnum 事件枚举
     * @param mobile 用户的电话号码
     * @param createBy 发起者名称
     */
    @Override
    public void logMemberEvent(UmsMemberChangeEventEnum eventEnum, String mobile, String createBy) {
        UmsMemberChangeHistory item = UmsMemberChangeHistory.builder()
                .mobile(mobile)
                .createBy(createBy)
                .createTime(DateUtils.getNowDate())
                .eventName(eventEnum.toString())
                .eventContent(eventEnum.getDescription())
                .build();
        umsMemberChangeHistoryMapper.insertUmsMemberChangeHistory(item);
    }
}