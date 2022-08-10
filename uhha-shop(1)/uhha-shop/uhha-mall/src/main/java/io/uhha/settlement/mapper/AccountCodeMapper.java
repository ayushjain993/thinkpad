package io.uhha.settlement.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.settlement.domain.AccountCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 账户号码池Mapper接口
 * 
 * @author uhha
 * @date 2022-02-23
 */
@Mapper
public interface AccountCodeMapper extends BaseMapper<AccountCode>
{
    /**
     * 查询账户号码池
     * 
     * @param id 账户号码池主键
     * @return 账户号码池
     */
    public AccountCode selectAccountCodeById(Long id);

    /**
     * 查询账户号码池列表
     * 
     * @param accountCode 账户号码池
     * @return 账户号码池集合
     */
    public List<AccountCode> selectAccountCodeList(AccountCode accountCode);

    /**
     * 新增账户号码池
     * 
     * @param accountCode 账户号码池
     * @return 结果
     */
    public int insertAccountCode(AccountCode accountCode);

    /**
     * 修改账户号码池
     *
     * @param id 账户id
     * @return 结果
     */
    public int allocateAccountCode(Long id);

    /**
     * 选一个可用的账户号码
     *
     * @param accountType 账户号码
     * @return 结果
     */
    public AccountCode selectFreeAccountCode(@Param("accountType") String accountType);

    /**
     * 查询id最大值
     *
     * @return 结果
     */
    public Long selectMaxId();

}
