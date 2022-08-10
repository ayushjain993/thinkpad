package io.uhha.coin.system.mapper;

import io.uhha.coin.system.domain.FSysCoinWallet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统钱包Mapper接口
 *
 * @author uhha
 * @date 2021-10-04
 */
@Mapper
public interface FSysCoinWalletMapper
{
    /**
     * 查询系统钱包
     *
     * @param id 系统钱包主键
     * @return 系统钱包
     */
    public FSysCoinWallet selectFSysCoinWalletById(Long id);

    /**
     * 查询系统钱包
     *
     * @param coinId 系统钱包主键
     * @return 系统钱包
     */
    public List<FSysCoinWallet> selectFSysCoinWalletByCoinId(Integer coinId);

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
