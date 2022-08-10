package io.uhha.coin.system.service.impl;


import io.uhha.coin.system.domain.FSysCoinWallet;
import io.uhha.coin.system.mapper.FSysCoinWalletMapper;
import io.uhha.coin.system.service.IFSysCoinWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统钱包Service业务层处理
 *
 * @author uhha
 * @date 2021-10-04
 */
@Service
@Slf4j
public class FSysCoinWalletServiceImpl implements IFSysCoinWalletService
{
    @Autowired
    private FSysCoinWalletMapper fSysCoinWalletMapper;

    /**
     * 查询系统钱包
     *
     * @param id 系统钱包主键
     * @return 系统钱包
     */
    @Override
    public FSysCoinWallet selectFSysCoinWalletById(Long id)
    {
        return fSysCoinWalletMapper.selectFSysCoinWalletById(id);
    }

    @Override
    public FSysCoinWallet getSysCoinWalletByCoinId(Integer coinId) {
        List<FSysCoinWallet> coinWallets =  fSysCoinWalletMapper.selectFSysCoinWalletByCoinId(coinId);
        if(coinWallets.size()!=1){
            log.error("System setting error! Multiple SysCoinWallet found for coinid={}",coinId);
            return null;
        }else{
            return coinWallets.get(0);
        }
    }

    /**
     * 查询系统钱包列表
     *
     * @param fSysCoinWallet 系统钱包
     * @return 系统钱包
     */
    @Override
    public List<FSysCoinWallet> selectFSysCoinWalletList(FSysCoinWallet fSysCoinWallet)
    {
        return fSysCoinWalletMapper.selectFSysCoinWalletList(fSysCoinWallet);
    }

    /**
     * 修改系统钱包
     *
     * @param fSysCoinWallet 系统钱包
     * @return 结果
     */
    @Override
    public int updateFSysCoinWallet(FSysCoinWallet fSysCoinWallet)
    {
        return fSysCoinWalletMapper.updateFSysCoinWallet(fSysCoinWallet);
    }
}