package io.uhha.setting.service.impl;

import io.uhha.setting.bean.OssSetting;
import io.uhha.setting.bean.OssSetCommon;
import io.uhha.setting.domain.LsOssSetting;
import io.uhha.setting.mapper.LsOssSettingMapper;
import io.uhha.setting.service.ILsOssSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Oss设置Service业务层处理
 *
 * @author mj
 * @date 2020-07-28
 */
@Service
public class LsOssSettingServiceImpl implements ILsOssSettingService {
    private static final Logger logger = LoggerFactory.getLogger(LsOssSettingServiceImpl.class);
    /**
     * Oss类型列表
     */
    private final static List<String> codeTypeList = Arrays.asList("1", "2", "3");

    @Autowired
    private LsOssSettingMapper lsOssSettingMapper;

    /**
     * 查询Oss设置
     *
     * @param id Oss设置ID
     * @return Oss设置
     */
    @Override
    public LsOssSetting selectLsOssSettingById(Long id) {
        return lsOssSettingMapper.selectLsOssSettingById(id);
    }

    /**
     * 查询Oss接口设置
     *
     * @return 返回OssSetCommon
     */
    @Override
    public OssSetCommon queryOssSet() {
        logger.debug("queryOssSet...");
        return OssSetCommon.getOssSetCommon(new OssSetCommon(), lsOssSettingMapper.queryOssSet());
    }

    /**
     * 编辑Oss接口设置
     *
     * @param OssSetCommon 实体类参数
     * @param codeType     Oss设置类型 1 阿里云Oss 2 七牛云Oss 3 Aws
     * @return -1编辑出错 >=1成功
     */
    @Override
    @Transactional
    public int editOssSet(OssSetCommon OssSetCommon, String codeType) {
        List<OssSetting> list = new ArrayList<>();
        if (codeTypeList.indexOf(codeType) < 0) {
            logger.error("editOssSet error codeType is illegal");
            return -1;
        }
        //先删后增
        lsOssSettingMapper.deleteOssSet(codeType);
        if ("1".equals(codeType)) {
            logger.debug("editOssSet aliOss...");
            list.add(OssSetting.getOssSet(new OssSetting(), codeType, OssSetCommon.getAliOssSet().getAccessKey(), "accessKey"));
            list.add(OssSetting.getOssSet(new OssSetting(), codeType, OssSetCommon.getAliOssSet().getSecretKey(), "secretKey"));
            list.add(OssSetting.getOssSet(new OssSetting(), codeType, OssSetCommon.getAliOssSet().getBucket(), "bucket"));
            list.add(OssSetting.getOssSet(new OssSetting(), codeType, OssSetCommon.getAliOssSet().getEndPoint(), "endPoint"));
            list.add(OssSetting.getOssSet(new OssSetting(), codeType, OssSetCommon.getAliOssSet().getIsUse(), "isUse"));
        }
        if ("2".equals(codeType)) {
            logger.debug("editOssSet qiniuOss...");
            list.add(OssSetting.getOssSet(new OssSetting(), codeType, OssSetCommon.getQiniuOssSet().getAccessKey(), "accessKey"));
            list.add(OssSetting.getOssSet(new OssSetting(), codeType, OssSetCommon.getQiniuOssSet().getSecretKey(), "secretKey"));
            list.add(OssSetting.getOssSet(new OssSetting(), codeType, OssSetCommon.getQiniuOssSet().getBucket(), "bucket"));
            list.add(OssSetting.getOssSet(new OssSetting(), codeType, OssSetCommon.getQiniuOssSet().getHost(), "host"));
            list.add(OssSetting.getOssSet(new OssSetting(), codeType, OssSetCommon.getQiniuOssSet().getIsUse(), "isUse"));
        }
        if ("3".equals(codeType)) {
            logger.debug("Aws Oss setting not done");
        }
        return lsOssSettingMapper.addOssSet(list);
    }

    /**
     * 查询Oss设置列表
     *
     * @param lsOssSetting Oss设置
     * @return Oss设置
     */
    @Override
    public List<LsOssSetting> selectLsOssSettingList(LsOssSetting lsOssSetting) {
        return lsOssSettingMapper.selectLsOssSettingList(lsOssSetting);
    }

    /**
     * 新增Oss设置
     *
     * @param lsOssSetting Oss设置
     * @return 结果
     */
    @Override
    public int insertLsOssSetting(LsOssSetting lsOssSetting) {
        return lsOssSettingMapper.insertLsOssSetting(lsOssSetting);
    }

    /**
     * 修改Oss设置
     *
     * @param lsOssSetting Oss设置
     * @return 结果
     */
    @Override
    public int updateLsOssSetting(LsOssSetting lsOssSetting) {
        return lsOssSettingMapper.updateLsOssSetting(lsOssSetting);
    }

    /**
     * 批量删除Oss设置
     *
     * @param ids 需要删除的Oss设置ID
     * @return 结果
     */
    @Override
    public int deleteLsOssSettingByIds(Long[] ids) {
        return lsOssSettingMapper.deleteLsOssSettingByIds(ids);
    }

    /**
     * 删除Oss设置信息
     *
     * @param id Oss设置ID
     * @return 结果
     */
    @Override
    public int deleteLsOssSettingById(Long id) {
        return lsOssSettingMapper.deleteLsOssSettingById(id);
    }
}
