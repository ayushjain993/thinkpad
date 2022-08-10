package io.uhha.coin.system.mapper;

import io.uhha.coin.system.domain.FSystemArgs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 系统参数-数据库访问接口
 *
 * @author ZKF
 */
@Mapper
public interface FSystemArgsMapper {

    /**
     * 更新
     *
     * @param record 系统参数实体
     * @return 更新记录数
     */
    int updateByValue(FSystemArgs record);

    /**
     * 查询业务参数列表
     *
     * @param fSystemArgs 业务参数
     * @return 业务参数集合
     */
    public List<FSystemArgs> selectFSystemArgsList(FSystemArgs fSystemArgs);

    /**
     * 插入
     *
     * @param record 系统参数
     * @return 插入记录数
     */
    int insert(FSystemArgs record);

    /**
     * 查询
     *
     * @param fid 系统参数id
     * @return 系统参数实体
     */
    FSystemArgs selectByPrimaryKey(Integer fid);

    /**
     * 查找所有
     *
     * @return 系统参数列表
     */
    List<FSystemArgs> selectAll();

    /**
     * 更新
     *
     * @param record 系统参数实体
     * @return 更新记录数
     */
    int updateByPrimaryKey(FSystemArgs record);


    /*****Admin*****/

    /**
     * 分页查询系统参数
     *
     * @param map 参数map
     * @return 查询记录列表
     */
    List<FSystemArgs> getSystemArgsPageList(Map<String, Object> map);

    /**
     * 查询系统参数的记录数
     *
     * @param map 参数map
     * @return 查询记录数
     */
    int countSystemArgsByParam(Map<String, Object> map);

    /**
     * 删除业务参数
     *
     * @param fid 业务参数主键
     * @return 结果
     */
    public int deleteFSystemArgsByFid(Integer fid);

    /**
     * 批量删除业务参数
     *
     * @param fids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFSystemArgsByFids(Integer[] fids);

}