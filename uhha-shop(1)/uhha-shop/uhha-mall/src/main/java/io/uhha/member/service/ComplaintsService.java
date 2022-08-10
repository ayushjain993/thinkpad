package io.uhha.member.service;

import io.uhha.member.domain.Complaints;
import io.uhha.util.PageHelper;


/**
 * Created By Caizize on 2018.3.21
 *
 * 平台投诉服务接口
 */
public interface ComplaintsService {

    /**
     * 新增平台投诉
     *
     * @param complaints 平台投诉实体
     * @return 成功1 ，失败0
     */
    int addComplaints(Complaints complaints);

    /**
     * 平台回复投诉
     *
     * @param complaints 平台投诉实体
     * @return 成功1，失败0
     */
    int updateComplaintsReplay(Complaints complaints);

    /**
     * 分页查询平台投诉
     *
     * @param pageHelper 分页帮助类
     * @param customerId 会员id
     * @param status     状态
     * @return 平台投诉集合
     */
    PageHelper<Complaints> queryComplaints(PageHelper<Complaints> pageHelper, long customerId, String status);

    /**
     * 分页查询平台投诉（管理后台使用）
     *
     * @param pageHelper 分页帮助类
     * @param status     状态
     * @return 平台投诉集合
     */
    PageHelper<Complaints> queryComplaints(PageHelper<Complaints> pageHelper, String status);

    Complaints queryComplaintsById(Long id);

}
