package io.uhha.member.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.member.domain.UmsMemberChangeHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户修改历史记录Mapper接口
 *
 * @author peter
 * @date 2021-12-16
 */
@Mapper
public interface UmsMemberChangeHistoryMapper extends BaseMapper<UmsMemberChangeHistory>
{
    /**
     * 查询用户修改历史记录列表
     *
     * @param mobile 用户手机号码
     * @return 用户修改历史记录集合
     */
    public List<UmsMemberChangeHistory> selectUmsMemberChangeHistoryList(@Param("mobile") String mobile);

    /**
     * 新增用户修改历史记录
     *
     * @param umsMemberChangeHistory 用户修改历史记录
     * @return 结果
     */
    public int insertUmsMemberChangeHistory(UmsMemberChangeHistory umsMemberChangeHistory);


}
