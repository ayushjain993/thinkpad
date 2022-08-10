package io.uhha.coin.capital.mapper;

import io.uhha.coin.capital.domain.QVirtualCapitalSmall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 虚拟币最小充值记录
 * 
 * @author lgp
 */
@Mapper
public interface QVirtualCapitalSmallMapper {

	/**
	 * 新增记录
	 * 
	 * @param record
	 * @return
	 */
	public int insert(QVirtualCapitalSmall record);

	/**
	 * 根据quniquenumber查询记录数
	 * 
	 * @param quniquenumber
	 * @return
	 */
	public int countByUniquenumber(@Param("quniquenumber") String quniquenumber);
}