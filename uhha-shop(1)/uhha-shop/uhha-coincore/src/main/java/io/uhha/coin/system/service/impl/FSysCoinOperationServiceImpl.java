package io.uhha.coin.system.service.impl;

import io.uhha.coin.system.domain.FSysCoinOperation;
import io.uhha.coin.system.mapper.FSysCoinOperationMapper;
import io.uhha.coin.system.service.IFSysCoinOperationService;
import io.uhha.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统钱包操作日志Service业务层处理
 *
 * @author uhha
 * @date 2021-10-05
 */
@Service
public class FSysCoinOperationServiceImpl implements IFSysCoinOperationService
{
    @Autowired
    private FSysCoinOperationMapper fSysCoinOperationMapper;

    /**
     * 查询系统钱包操作日志
     *
     * @param fid 系统钱包操作日志主键
     * @return 系统钱包操作日志
     */
    @Override
    public FSysCoinOperation selectFSysCoinOperationByFid(Long fid)
    {
        return fSysCoinOperationMapper.selectFSysCoinOperationByFid(fid);
    }

    /**
     * 查询系统钱包操作日志列表
     *
     * @param fSysCoinOperation 系统钱包操作日志
     * @return 系统钱包操作日志
     */
    @Override
    public List<FSysCoinOperation> selectFSysCoinOperationList(FSysCoinOperation fSysCoinOperation)
    {
        return fSysCoinOperationMapper.selectFSysCoinOperationList(fSysCoinOperation);
    }

    /**
     * 查询系统钱包操作日志列表
     *
     * @param coinid 系统钱包操作日志
     * @return 系统钱包操作日志
     */
    @Override
    public List<FSysCoinOperation> selectFSysCoinOperationListByCoinId(Integer coinid) {
        return fSysCoinOperationMapper.selectFSysCoinOperationListByCoinId(coinid);
    }

    /**
     * 新增系统钱包操作日志
     *
     * @param fSysCoinOperation 系统钱包操作日志
     * @return 结果
     */
    @Override
    public int insertFSysCoinOperation(FSysCoinOperation fSysCoinOperation)
    {
        fSysCoinOperation.setUpdateTime(DateUtils.getNowDate());
        return fSysCoinOperationMapper.insertFSysCoinOperation(fSysCoinOperation);
    }

    /**
     * 修改系统钱包操作日志
     *
     * @param fSysCoinOperation 系统钱包操作日志
     * @return 结果
     */
    @Override
    public int updateFSysCoinOperation(FSysCoinOperation fSysCoinOperation)
    {
        fSysCoinOperation.setUpdateTime(DateUtils.getNowDate());
        return fSysCoinOperationMapper.updateFSysCoinOperation(fSysCoinOperation);
    }

    /**
     * 批量删除系统钱包操作日志
     *
     * @param fids 需要删除的系统钱包操作日志主键
     * @return 结果
     */
    @Override
    public int deleteFSysCoinOperationByFids(Long[] fids)
    {
        return fSysCoinOperationMapper.deleteFSysCoinOperationByFids(fids);
    }

    /**
     * 删除系统钱包操作日志信息
     *
     * @param fid 系统钱包操作日志主键
     * @return 结果
     */
    @Override
    public int deleteFSysCoinOperationByFid(Long fid)
    {
        return fSysCoinOperationMapper.deleteFSysCoinOperationByFid(fid);
    }
}
