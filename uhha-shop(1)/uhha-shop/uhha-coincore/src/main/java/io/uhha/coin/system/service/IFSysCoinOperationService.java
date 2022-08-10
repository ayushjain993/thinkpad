package io.uhha.coin.system.service;

import io.uhha.coin.system.domain.FSysCoinOperation;

import java.util.List;

/**
 * 系统钱包操作日志Service接口
 *
 * @author uhha
 * @date 2021-10-05
 */
public interface IFSysCoinOperationService
{
    /**
     * 查询系统钱包操作日志
     *
     * @param fid 系统钱包操作日志主键
     * @return 系统钱包操作日志
     */
    public FSysCoinOperation selectFSysCoinOperationByFid(Long fid);

    /**
     * 查询系统钱包操作日志列表
     *
     * @param fSysCoinOperation 系统钱包操作日志
     * @return 系统钱包操作日志集合
     */
    public List<FSysCoinOperation> selectFSysCoinOperationList(FSysCoinOperation fSysCoinOperation);

    /**
     * 查询系统钱包操作日志列表
     *
     * @param coinid 系统钱包操作日志
     * @return 系统钱包操作日志集合
     */
    public List<FSysCoinOperation> selectFSysCoinOperationListByCoinId(Integer coinid);

    /**
     * 新增系统钱包操作日志
     *
     * @param fSysCoinOperation 系统钱包操作日志
     * @return 结果
     */
    public int insertFSysCoinOperation(FSysCoinOperation fSysCoinOperation);

    /**
     * 修改系统钱包操作日志
     *
     * @param fSysCoinOperation 系统钱包操作日志
     * @return 结果
     */
    public int updateFSysCoinOperation(FSysCoinOperation fSysCoinOperation);

    /**
     * 批量删除系统钱包操作日志
     *
     * @param fids 需要删除的系统钱包操作日志主键集合
     * @return 结果
     */
    public int deleteFSysCoinOperationByFids(Long[] fids);

    /**
     * 删除系统钱包操作日志信息
     *
     * @param fid 系统钱包操作日志主键
     * @return 结果
     */
    public int deleteFSysCoinOperationByFid(Long fid);
}