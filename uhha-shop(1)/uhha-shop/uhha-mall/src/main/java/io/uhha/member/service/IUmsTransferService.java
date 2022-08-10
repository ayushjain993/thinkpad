package io.uhha.member.service;

import io.uhha.member.vo.UmsTransferVo;

/**
 * 用户分销余额与可用余额转账记录Service接口
 * 
 * @author uhha
 * @date 2022-03-15
 */
public interface IUmsTransferService
{

    /**
     * 从用户分销余额与可用余额转账
     *
     * @param umsTransfer 用户分销余额与可用余额转账记录
     * @return 结果
     */
    public int umsTransfer(UmsTransferVo umsTransfer, Long userId);
}
