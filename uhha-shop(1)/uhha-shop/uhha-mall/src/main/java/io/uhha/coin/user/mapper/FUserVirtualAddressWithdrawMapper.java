package io.uhha.coin.user.mapper;

import io.uhha.coin.user.domain.FUserVirtualAddressWithdraw;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户虚拟币提现地址数据操作接口
 * @author ZKF
 */
@Mapper
public interface FUserVirtualAddressWithdrawMapper {
	
    /**
     * 新增地址
     * @param record 实体对象
     * @return 成功条数
     */
    int insert(FUserVirtualAddressWithdraw record);
    
    /**
     * 更新地址
     * @param record 实体对象
     * @return 成功条数
     */
    int updateByPrimaryKey(FUserVirtualAddressWithdraw record);
    
    /**
     * 根据实体查询地址列表
     * @param address 实体对象
     * @return 实体对象列表
     */
    List<FUserVirtualAddressWithdraw> getVirtualCoinWithdrawAddressList(FUserVirtualAddressWithdraw address);
    
    /**
     * 根据id查询地址
     * @param fid 主键ID
     * @return 实体对象
     */
    FUserVirtualAddressWithdraw selectByPrimaryKey(Integer fid);
}