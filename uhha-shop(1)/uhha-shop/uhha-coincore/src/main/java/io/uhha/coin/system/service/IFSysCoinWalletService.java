package io.uhha.coin.system.service;

import io.uhha.coin.system.domain.FSysCoinWallet;

import java.util.List;

/**
 * 系统钱包Service接口
 *
 * @author uhha
 * @date 2021-10-04
 */
public interface IFSysCoinWalletService
{
    /**
     * 查询系统钱包
     *
     * @param id 系统钱包主键
     * @return 系统钱包
     */
    public FSysCoinWallet selectFSysCoinWalletById(Long id);

    /**
     * 根据coinId查询系统钱包
     *
     * @param coinId 币种编号
     * @return 系统钱包
     */
    public FSysCoinWallet getSysCoinWalletByCoinId(Integer coinId);

    /**
     * 查询系统钱包列表
     *
     * @param fSysCoinWallet 系统钱包
     * @return 系统钱包集合
     */
    public List<FSysCoinWallet> selectFSysCoinWalletList(FSysCoinWallet fSysCoinWallet);

    /**
     * 修改系统钱包
     *
     * @param fSysCoinWallet 系统钱包
     * @return 结果
     */
    public int updateFSysCoinWallet(FSysCoinWallet fSysCoinWallet);

}