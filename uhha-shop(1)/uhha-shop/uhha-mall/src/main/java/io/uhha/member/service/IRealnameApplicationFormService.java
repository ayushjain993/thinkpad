package io.uhha.member.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.member.domain.RealnameApplicationForm;

/**
 * 实名认证申请Service接口
 *
 * @author uhha
 * @date 2022-03-13
 */
public interface IRealnameApplicationFormService extends IService<RealnameApplicationForm>
{

    /**
     * 查询实名认证申请
     *
     * @param uid 实名认证申请uid
     * @return 实名认证申请
     */
    public RealnameApplicationForm selectRealnameApplicationFormByUid(Long uid);

    /**
     * 查询实名认证申请
     *
     * @param id 实名认证申请主键
     * @return 实名认证申请
     */
    public RealnameApplicationForm selectRealnameApplicationFormById(Long id);

    /**
     * 查询实名认证申请列表
     *
     * @param realnameApplicationForm 实名认证申请
     * @return 实名认证申请集合
     */
    public List<RealnameApplicationForm> selectRealnameApplicationFormList(RealnameApplicationForm realnameApplicationForm);

    /**
     * 新增实名认证申请
     *
     * @param realnameApplicationForm 实名认证申请
     * @return 结果
     */
    public int insertRealnameApplicationForm(RealnameApplicationForm realnameApplicationForm);

    /**
     * 修改实名认证申请
     *
     * @param realnameApplicationForm 实名认证申请
     * @return 结果
     */
    public int updateRealnameApplicationForm(RealnameApplicationForm realnameApplicationForm);

    /**
     * 批量删除实名认证申请
     *
     * @param ids 需要删除的实名认证申请主键集合
     * @return 结果
     */
    public int deleteRealnameApplicationFormByIds(Long[] ids);

    /**
     * 删除实名认证申请信息
     *
     * @param id 实名认证申请主键
     * @return 结果
     */
    public int deleteRealnameApplicationFormById(Long id);

    public int approveRealname(Long id, String approvedBy);

    public int rejectRealname(Long id, String reason, String rejectedBy);

}