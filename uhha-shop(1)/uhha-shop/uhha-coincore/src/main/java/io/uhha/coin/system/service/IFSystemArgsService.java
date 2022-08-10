package io.uhha.coin.system.service;


import io.uhha.coin.system.domain.FSystemArgs;

import java.util.List;

/**
 * 业务参数Service接口
 *
 * @author uhha
 * @date 2021-09-27
 */
public interface IFSystemArgsService
{
    /**
     * 查询业务参数
     *
     * @param fid 业务参数主键
     * @return 业务参数
     */
    public FSystemArgs selectFSystemArgsByFid(Integer fid);

    /**
     * 查询业务参数列表
     *
     * @param fSystemArgs 业务参数
     * @return 业务参数集合
     */
    public List<FSystemArgs> selectFSystemArgsList(FSystemArgs fSystemArgs);

    /**
     * 新增业务参数
     *
     * @param fSystemArgs 业务参数
     * @return 结果
     */
    public int insertFSystemArgs(FSystemArgs fSystemArgs);

    /**
     * 修改业务参数
     *
     * @param fSystemArgs 业务参数
     * @return 结果
     */
    public int updateFSystemArgs(FSystemArgs fSystemArgs);

    /**
     * 批量删除业务参数
     *
     * @param fids 需要删除的业务参数主键集合
     * @return 结果
     */
    public int deleteFSystemArgsByFids(Integer[] fids);

    /**
     * 删除业务参数信息
     *
     * @param fid 业务参数主键
     * @return 结果
     */
    public int deleteFSystemArgsByFid(Integer fid);
}