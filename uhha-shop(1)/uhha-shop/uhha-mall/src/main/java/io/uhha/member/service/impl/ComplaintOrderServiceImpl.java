package io.uhha.member.service.impl;

import io.uhha.member.domain.ComplaintOrder;
import io.uhha.member.mapper.ComplaintOrderMapper;
import io.uhha.member.service.ComplaintOrderService;
import io.uhha.util.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单投诉服务实现类
 */
@Service
public class ComplaintOrderServiceImpl implements ComplaintOrderService {

    /**
     * 注入订单投诉数据库接口
     */
    @Autowired
    private ComplaintOrderMapper complaintOrderMapper;

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(ComplaintOrderServiceImpl.class);

    @Override
    public int addComplaintOrder(ComplaintOrder complaintOrder) {
        logger.debug("addComplaintOrder and complaintOrder:{}", complaintOrder);
        return complaintOrderMapper.addComplaintOrder(complaintOrder);
    }

    @Override
    public int replyComplaintOrder(ComplaintOrder complaintOrder) {
        logger.debug("replyComplaintOrder and complaintOrder:{}", complaintOrder);
        return complaintOrderMapper.replyComplaintOrder(complaintOrder);
    }

    @Override
    public PageHelper<ComplaintOrder> queryComplaintOrder(PageHelper<ComplaintOrder> pageHelper, ComplaintOrder.QueryCriteria queryCriteria) {
        logger.debug("queryComplaintOrder and pageHelper:{} \r\n queryCriteria:{}", pageHelper, queryCriteria);
        return pageHelper.setListDates(complaintOrderMapper.queryComplaintOrder(pageHelper.getQueryParams(queryCriteria.getQueryMap(), complaintOrderMapper.queryComplaintOrderCount(queryCriteria.getQueryMap()))));
    }

    @Override
    public ComplaintOrder queryComplaintOrderById(Long id) {
        logger.debug("queryComplaintOrderById id:{}",  id);
        return complaintOrderMapper.queryComplaintOrderById(id);
    }
}
