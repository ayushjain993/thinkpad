package io.uhha.member.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.AuditStatusEnum;
import io.uhha.common.enums.RealnameScreenStatusEnum;
import io.uhha.common.notify.MallNotifyHelper;
import io.uhha.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.member.mapper.RealnameApplicationFormMapper;
import io.uhha.member.domain.RealnameApplicationForm;
import io.uhha.member.service.IRealnameApplicationFormService;

/**
 * 实名认证申请Service业务层处理
 *
 * @author uhha
 * @date 2022-03-13
 */
@Service
@Slf4j
public class RealnameApplicationFormServiceImpl extends ServiceImpl<RealnameApplicationFormMapper, RealnameApplicationForm> implements IRealnameApplicationFormService
{
    @Autowired
    private RealnameApplicationFormMapper realnameApplicationFormMapper;


    @Autowired
    private IUmsMemberService memberService;

    @Autowired
    private MallNotifyHelper notifyHelper;

    @Override
    public RealnameApplicationForm selectRealnameApplicationFormByUid(Long uid) {
        QueryWrapper<RealnameApplicationForm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        return realnameApplicationFormMapper.selectOne(queryWrapper);
    }

    /**
     * 查询实名认证申请
     *
     * @param id 实名认证申请主键
     * @return 实名认证申请
     */
    @Override
    public RealnameApplicationForm selectRealnameApplicationFormById(Long id)
    {
        return realnameApplicationFormMapper.selectRealnameApplicationFormById(id);
    }

    /**
     * 查询实名认证申请列表
     *
     * @param realnameApplicationForm 实名认证申请
     * @return 实名认证申请
     */
    @Override
    public List<RealnameApplicationForm> selectRealnameApplicationFormList(RealnameApplicationForm realnameApplicationForm)
    {
        return realnameApplicationFormMapper.selectRealnameApplicationFormList(realnameApplicationForm);
    }

    /**
     * 新增实名认证申请
     *
     * @param realnameApplicationForm 实名认证申请
     * @return 结果
     */
    @Override
    public int insertRealnameApplicationForm(RealnameApplicationForm realnameApplicationForm)
    {
        realnameApplicationForm.setCreateTime(DateUtils.getNowDate());
        return realnameApplicationFormMapper.insertRealnameApplicationForm(realnameApplicationForm);
    }

    /**
     * 修改实名认证申请
     *
     * @param realnameApplicationForm 实名认证申请
     * @return 结果
     */
    @Override
    public int updateRealnameApplicationForm(RealnameApplicationForm realnameApplicationForm)
    {
        return realnameApplicationFormMapper.updateRealnameApplicationForm(realnameApplicationForm);
    }

    /**
     * 批量删除实名认证申请
     *
     * @param ids 需要删除的实名认证申请主键
     * @return 结果
     */
    @Override
    public int deleteRealnameApplicationFormByIds(Long[] ids)
    {
        return realnameApplicationFormMapper.deleteRealnameApplicationFormByIds(ids);
    }

    /**
     * 删除实名认证申请信息
     *
     * @param id 实名认证申请主键
     * @return 结果
     */
    @Override
    public int deleteRealnameApplicationFormById(Long id)
    {
        return realnameApplicationFormMapper.deleteRealnameApplicationFormById(id);
    }

    @Override
    public int approveRealname(Long id, String approvedBy) {
        RealnameApplicationForm form = selectRealnameApplicationFormById(id);
        if (form == null) {
            log.error("realname app form with id {} was not found", id);
            return -1;
        }
        form.setStatus(RealnameScreenStatusEnum.APPROVED.getCode());
        form.setUpdateBy(approvedBy);
        form.setUpdateTime(DateUtils.getNowDate());
        //更新用户实名状态
        int ret = memberService.changeRealNameStatus(RealnameScreenStatusEnum.APPROVED.getCode(), form.getUid());
        if (ret != 1) {
            log.error("change realname status failed!");
            return 0;
        }
        //更新实名认证申请表单
        updateRealnameApplicationForm(form);
        //发送通知
        notifyHelper.adminApproveRejectRealname(form.getUid(), "", AuditStatusEnum.PASS.getCode());
        return 1;
    }

    @Override
    public int rejectRealname(Long id, String reason, String rejectedBy) {
        RealnameApplicationForm form = selectRealnameApplicationFormById(id);
        if (form == null) {
            log.error("realname app form with id {} was not found", id);
            return -1;
        }
        form.setStatus(RealnameScreenStatusEnum.REJECTED.getCode());
        form.setOpinion(reason);
        form.setUpdateBy(rejectedBy);
        form.setUpdateTime(DateUtils.getNowDate());

        //更新用户实名状态
        int ret = memberService.changeRealNameStatus(RealnameScreenStatusEnum.REJECTED.getCode(), form.getUid());
        if (ret != 1) {
            log.error("change realname status failed!");
            return -1;
        }
        //更新实名认证申请表单
        updateRealnameApplicationForm(form);

        //发送通知
        notifyHelper.adminApproveRejectRealname(form.getUid(), reason, AuditStatusEnum.REJECTED.getCode());
        return 1;
    }
}