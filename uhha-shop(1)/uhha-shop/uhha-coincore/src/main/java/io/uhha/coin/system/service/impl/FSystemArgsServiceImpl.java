package io.uhha.coin.system.service.impl;

import io.uhha.coin.system.domain.FSystemArgs;
import io.uhha.coin.system.mapper.FSystemArgsMapper;
import io.uhha.coin.system.service.IFSystemArgsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务参数Service业务层处理
 *
 * @author uhha
 * @date 2021-09-27
 */
@Service
public class FSystemArgsServiceImpl implements IFSystemArgsService
{
    @Autowired
    private FSystemArgsMapper fSystemArgsMapper;

    /**
     * 查询业务参数
     *
     * @param fid 业务参数主键
     * @return 业务参数
     */
    @Override
    public FSystemArgs selectFSystemArgsByFid(Integer fid)
    {
        return fSystemArgsMapper.selectByPrimaryKey(fid);
    }

    /**
     * 查询业务参数列表
     *
     * @param fSystemArgs 业务参数
     * @return 业务参数
     */
    @Override
    public List<FSystemArgs> selectFSystemArgsList(FSystemArgs fSystemArgs)
    {
        return fSystemArgsMapper.selectFSystemArgsList(fSystemArgs);
    }

    /**
     * 新增业务参数
     *
     * @param fSystemArgs 业务参数
     * @return 结果
     */
    @Override
    public int insertFSystemArgs(FSystemArgs fSystemArgs)
    {
        return fSystemArgsMapper.insert(fSystemArgs);
    }

    /**
     * 修改业务参数
     *
     * @param fSystemArgs 业务参数
     * @return 结果
     */
    @Override
    public int updateFSystemArgs(FSystemArgs fSystemArgs)
    {
        return fSystemArgsMapper.updateByPrimaryKey(fSystemArgs);
    }

    @Override
    public int deleteFSystemArgsByFids(Integer[] fids) {
        return fSystemArgsMapper.deleteFSystemArgsByFids(fids);
    }

    @Override
    public int deleteFSystemArgsByFid(Integer fid) {
        return fSystemArgsMapper.deleteFSystemArgsByFid(fid);
    }

}
