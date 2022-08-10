package io.uhha.setting.mapper;

import io.uhha.setting.domain.LsStationLetter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 站内信Mapper接口
 *
 * @author mj
 * @date 2020-07-29
 */
@Mapper
public interface LsStationLetterMapper {
    /**
     * 新增站内信
     *
     * @param stationLetters 站内信
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
     * 根据会员id查询会员的站内信
     *
     * @param params 查询参数
     * @return 返回会员的站内信
     */

    List<LsStationLetter> queryStationLettersByCustomerId(Map<String, Object> params);

    /**
     * 查询会员的站内信总纪录数
     *
     * @param params 查询参数
     * @return 返回会员的站内信总记录数
     */

    int queryStationLettersCountByCustomerId(Map<String, Object> params);

    /**
     * 更新会员的站内信的阅读状态
     *
     * @param params 参数
     * @return 成功返回1，失败返回0
     */

    int updateStationLettersIsRead(Map<String, Object> params);

    /**
     * 获得用户未读消息的数量
     *
     * @param customerId 用户id
     * @return 返回用户未读消息的数量
     */

    int unReadNum(long customerId);

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

    public int cleanStationLetters(@Param("customerId") Long customerId);

    public int readAllStationLetters(@Param("customerId") Long customerId);

}
