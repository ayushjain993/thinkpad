package io.uhha.setting.service;

import io.uhha.setting.domain.LsStationLetter;
import io.uhha.util.PageHelper;

import java.util.List;

/**
 * 站内信Service接口
 *
 * @author mj
 * @date 2020-07-29
 */
public interface ILsStationLetterService {
    /**
     * 查询站内信
     *
     * @param id 站内信ID
     * @return 站内信
     */
    public LsStationLetter selectLsStationLetterById(Long id);

    /**
     * 查询站内信列表
     *
     * @param lsStationLetter 站内信
     * @return 站内信集合
     */
    public List<LsStationLetter> selectLsStationLetterList(LsStationLetter lsStationLetter);

    /**
     * 新增站内信
     *
     * @param lsStationLetter 站内信
     * @return 结果
     */
    public int insertLsStationLetter(LsStationLetter lsStationLetter);

    /**
     * 修改站内信
     *
     * @param lsStationLetter 站内信
     * @return 结果
     */
    public int updateLsStationLetter(LsStationLetter lsStationLetter);

    /**
     * 新增站内信
     *
     * @param stationLetters 站内信集合
     * @return 成功返回>1 失败返回0
     */
    int addStationLetters(List<LsStationLetter> stationLetters);

    /**
     * 删除站内信
     *
     * @param ids 站内信id
     * @return 成功返回1，失败返回0
     */
    int deleteStationLetters(Long[] ids);


    /**
     * 删除站内信
     *
     * @param id 站内信id
     * @return 成功返回1，失败返回0
     */
    int deleteStationLetter(Long id);

    /**
     * 根据会员di查询会员的站内信
     *
     * @param pageHelper 分页帮助类
     * @param customerId 会员id
     * @return 返回会员的站内信
     */
    PageHelper<LsStationLetter> queryStationLettersByCustomerId(PageHelper<LsStationLetter> pageHelper, long customerId);

    /**
     * 更新会员的站内信的阅读状态
     *
     * @param id         站内信id
     * @param customerId 用户id
     * @return 成功返回1，失败返回0
     */
    int updateStationLettersIsRead(long id, long customerId);

    /**
     * 获得用户未读的消息数量
     *
     * @param customerId 会员id
     * @return 返回用户未读消息的数量
     */
    int unReadNum(long customerId);

    /**
     * 清空用户的所有站内信
     * @param customerId
     * @return 更新的数量
     */
    int cleanStationLetters(Long customerId);

    /**
     * 更改用户的所有站内信状态为已读
     * @param customerId
     * @return 更新的数量
     */
    int readAllStationLetters(Long customerId);


}
