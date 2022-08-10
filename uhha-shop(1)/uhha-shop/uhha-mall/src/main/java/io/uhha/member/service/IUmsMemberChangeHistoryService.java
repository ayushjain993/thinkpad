package io.uhha.member.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.common.enums.UmsMemberChangeEventEnum;
import io.uhha.member.domain.UmsMemberChangeHistory;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户修改历史记录Service接口
 *
 * @author peter
 * @date 2021-12-16
 */
public interface IUmsMemberChangeHistoryService extends IService<UmsMemberChangeHistory>
{
    /**
     * 查询用户修改历史记录列表
     *
     * @param mobile 用户手机号码
     * @return 用户修改历史记录集合
     */
    public List<UmsMemberChangeHistory> selectUmsMemberChangeHistoryList(String mobile);

    /**
     * 新增用户修改历史记录
     *
     * @param umsMemberChangeHistory 用户修改历史记录
     * @return 结果
     */
    public int insertUmsMemberChangeHistory(UmsMemberChangeHistory umsMemberChangeHistory);


    /**
     * 记录用户事件
     * @param eventEnum 事件枚举
     * @param mobile 手机号码
     * @param createBy 创建者名称
     */
    public void logMemberEvent(UmsMemberChangeEventEnum eventEnum, String mobile, String createBy);
}