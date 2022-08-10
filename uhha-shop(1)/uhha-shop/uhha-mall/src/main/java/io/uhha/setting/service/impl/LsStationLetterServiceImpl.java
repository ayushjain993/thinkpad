package io.uhha.setting.service.impl;

import io.uhha.common.utils.DateUtils;
import io.uhha.setting.domain.LsStationLetter;
import io.uhha.setting.mapper.LsStationLetterMapper;
import io.uhha.setting.service.ILsStationLetterService;
import io.uhha.util.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站内信Service业务层处理
 *
 * @author mj
 * @date 2020-07-29
 */
@Service
public class LsStationLetterServiceImpl implements ILsStationLetterService {
    private static final Logger logger = LoggerFactory.getLogger(LsStationLetterServiceImpl.class);
    @Autowired
    private LsStationLetterMapper lsStationLetterMapper;

    /**
     * 查询站内信
     *
     * @param id 站内信ID
     * @return 站内信
     */
    @Override
    public LsStationLetter selectLsStationLetterById(Long id) {
        return lsStationLetterMapper.selectLsStationLetterById(id);
    }

    /**
     * 查询站内信列表
     *
     * @param lsStationLetter 站内信
     * @return 站内信
     */
    @Override
    public List<LsStationLetter> selectLsStationLetterList(LsStationLetter lsStationLetter) {
        return lsStationLetterMapper.selectLsStationLetterList(lsStationLetter);
    }

    /**
     * 新增站内信
     *
     * @param lsStationLetter 站内信
     * @return 结果
     */
    @Override
    public int insertLsStationLetter(LsStationLetter lsStationLetter) {
        lsStationLetter.setCreateTime(DateUtils.getNowDate());
        return lsStationLetterMapper.insertLsStationLetter(lsStationLetter);
    }

    /**
     * 修改站内信
     *
     * @param lsStationLetter 站内信
     * @return 结果
     */
    @Override
    public int updateLsStationLetter(LsStationLetter lsStationLetter) {
        return lsStationLetterMapper.updateLsStationLetter(lsStationLetter);
    }


    @Override
    public int addStationLetters(List<LsStationLetter> stationLetters) {
        logger.debug("addStationLetters and stationLetters:{}", stationLetters);

        if (CollectionUtils.isEmpty(stationLetters)) {
            logger.error("addStationLetters fail due to params is null...");
            return 0;
        }
        return lsStationLetterMapper.addStationLetters(stationLetters);
    }

    /**
     * 删除站内信
     *
     * @param ids 站内信id
     * @return 成功返回1，失败返回0
     */
    @Override
    public int deleteStationLetters(Long[] ids) {
        logger.debug("deleteStationLetters and ids :{}", ids);
        return lsStationLetterMapper.deleteStationLetters(ids);
    }

    /**
     * 删除站内信
     *
     * @param id 站内信id
     * @return 成功返回1，失败返回0
     */
    @Override
    public int deleteStationLetter(Long id) {
        logger.debug("deleteStationLetters and id :{}", id);
        Long[] ids = {id};
        return lsStationLetterMapper.deleteStationLetters(ids);
    }

    /**
     * 更新会员的站内信的阅读状态
     *
     * @param id 站内信id
     * @return 成功返回1，失败返回0
     */
    @Override
    public int updateStationLettersIsRead(long id, long customerId) {
        logger.debug("updateStationLettersIsRead and id :{} \r\n customerId:{}", id, customerId);
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("customerId", customerId);
        return lsStationLetterMapper.updateStationLettersIsRead(params);
    }

    @Override
    public PageHelper<LsStationLetter> queryStationLettersByCustomerId(PageHelper<LsStationLetter> pageHelper, long customerId) {
        logger.debug("queryStationLettersByCustomerId and customerId:{} and pageHelper:{}", customerId, pageHelper);
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", customerId);
        return pageHelper.setListDates(lsStationLetterMapper.queryStationLettersByCustomerId(pageHelper.getQueryParams(params, lsStationLetterMapper.queryStationLettersCountByCustomerId(params))));
    }

    @Override
    public int unReadNum(long customerId) {
        logger.debug("unReadNum and customerId:{}", customerId);
        return lsStationLetterMapper.unReadNum(customerId);
    }

    /**
     * 清空用户的所有站内信
     * @param customerId
     * @return 更新的数量
     */
    @Override
    public int cleanStationLetters(Long customerId){
        logger.debug("cleanStationLetters and customerId:{}", customerId);
        return lsStationLetterMapper.cleanStationLetters(customerId);
    }

    /**
     * 更改用户的所有站内信状态为已读
     * @param customerId
     * @return 更新的数量
     */
    @Override
    public int readAllStationLetters(Long customerId){
        logger.debug("readAllStationLetters and customerId:{}", customerId);
        return lsStationLetterMapper.readAllStationLetters(customerId);
    }
}
