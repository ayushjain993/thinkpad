package io.uhha.coin.user.mapper;

import io.uhha.coin.user.domain.FLogCountlimit;
import io.uhha.coin.user.domain.FUserLoginlimit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 次数限制数据操作接口
 * @author ZKF
 */
@Mapper
public interface FLogCountlimitMapper {
	

    /**
     * 新增次数限制
     * @param record 实体对象
     * @return 成功条数
     */
    int insert(FLogCountlimit record);

    /**
     * 查询次数限制
     * @param ip IP地址
     * @param type 限制类型
     * @return
     */
    FLogCountlimit selectByLimit(@Param("fip") String ip,@Param("ftype") Integer type);


    /**
     * 查询用户错误次数
     * @param fuid 用户id
     * @return
     */
    FUserLoginlimit selectCountByfuid(@Param("fuid") Integer fuid);


    /**
     * 更新次数限制
     * @param limit 实体对象
     * @return 成功条数
     */
    int updateByLimit(FLogCountlimit limit);

    /**
     * 更新user登陆次数限制
     * @param fUserLoginlimit 实体对象
     * @return 成功条数
     */
    int updateByFuid(FUserLoginlimit fUserLoginlimit);

    /**
     * 通过ip删除次数限制
     * @param ip IP地址
     * @param type 限制类型
     * @return 成功对象
     */
    int deleteByIp(@Param("fip") String ip,@Param("ftype") Integer type);

    /**
     * 删除用户登陆错误次数
     * @param fuid
     * @return
     */
    int deleteByFuid(Integer fuid);

    /**
     * 新增用户登陆失败
     * @param fUserLoginlimit
     * @return
     */
    int insertByUser(FUserLoginlimit fUserLoginlimit);
}