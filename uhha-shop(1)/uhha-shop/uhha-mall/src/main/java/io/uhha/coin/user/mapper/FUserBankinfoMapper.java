package io.uhha.coin.user.mapper;


import io.uhha.coin.user.domain.FUserBankinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户银行卡数据操作接口
 * @author ZKF
 */
@Mapper
public interface FUserBankinfoMapper {
	
    /**
     * 新增用户银行卡
     * @param record 实体对象
     * @return 成功条数
     */
    int insert(FUserBankinfo record);

    /**
     * 根据id查询用户银行卡
     * @param fid 主键ID
     * @return 实体对象
     */
    FUserBankinfo selectByPrimaryKey(Integer fid);

    /**
     * 更新用户银行卡
     * @param record 实体对象
     * @return 成功条数
     */
    int updateByPrimaryKey(FUserBankinfo record);

    /**
     * 更新用户银行卡
     * @param record 实体对象
     * @return 成功条数
     */
    int updateByUid(FUserBankinfo record);

    /**
     * 根据用户和类型查询银行卡
     * @param fuid 用户ID
     * @param ftype 银行卡类型
     * @return 实体对象列表
     */
    List<FUserBankinfo> getBankInfoListByUser(@Param("fuid") Long fuid,@Param("ftype") Integer ftype);

    /**
     * 根据用户和类型查询银行卡
     * @param fuid 用户ID
     * @return 实体对象列表
     */
    FUserBankinfo getBankInfoListByUid(@Param("fuid") Long fuid);

    /**
     * 根据实体查询银行卡
     * @param fBankinfo 实体对象
     * @return 实体对象列表
     */
    List<FUserBankinfo> getBankInfoListByBankInfo(FUserBankinfo fBankinfo);
    
}