package io.uhha.member.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.member.domain.RealnameApplicationForm;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实名认证申请Mapper接口
 *
 * @author uhha
 * @date 2022-03-13
 */
@Mapper
public interface RealnameApplicationFormMapper extends BaseMapper<RealnameApplicationForm>
{
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
     * 删除实名认证申请
     *
     * @param id 实名认证申请主键
     * @return 结果
     */
    public int deleteRealnameApplicationFormById(Long id);

    /**
     * 批量删除实名认证申请
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRealnameApplicationFormByIds(Long[] ids);
}