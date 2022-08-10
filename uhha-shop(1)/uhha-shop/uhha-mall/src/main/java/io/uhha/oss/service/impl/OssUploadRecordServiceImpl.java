package io.uhha.oss.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.uuid.UUID;
import io.uhha.setting.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.oss.mapper.OssUploadRecordMapper;
import io.uhha.oss.domain.OssUploadRecord;
import io.uhha.oss.service.IOssUploadRecordService;

/**
 * OSS对象存储系统上传记录Service业务层处理
 * 
 * @author uhha
 */
@Service
@Slf4j
public class OssUploadRecordServiceImpl implements IOssUploadRecordService 
{
    @Autowired
    private OssUploadRecordMapper ossUploadRecordMapper;

    @Autowired
    private OssService ossService;

    /**
     * 查询OSS对象存储系统上传记录
     * 
     * @param id OSS对象存储系统上传记录主键
     * @return OSS对象存储系统上传记录
     */
    @Override
    public OssUploadRecord selectOssUploadRecordById(String id)
    {
        return ossUploadRecordMapper.selectOssUploadRecordById(id);
    }

    /**
     * 查询OSS对象存储系统上传记录列表
     * 
     * @param ossUploadRecord OSS对象存储系统上传记录
     * @return OSS对象存储系统上传记录
     */
    @Override
    public List<OssUploadRecord> selectOssUploadRecordList(OssUploadRecord ossUploadRecord)
    {
        return ossUploadRecordMapper.selectOssUploadRecordList(ossUploadRecord);
    }

    /**
     * 新增OSS对象存储系统上传记录
     * 
     * @param ossUploadRecord OSS对象存储系统上传记录
     * @return 结果
     */
    @Override
    public int insertOssUploadRecord(OssUploadRecord ossUploadRecord)
    {
        ossUploadRecord.setId(UUID.fastUUID().toString().replace("-",""));
        ossUploadRecord.setCreateTime(DateUtils.getNowDate());
        return ossUploadRecordMapper.insertOssUploadRecord(ossUploadRecord);
    }

    /**
     * 修改OSS对象存储系统上传记录
     * 
     * @param ossUploadRecord OSS对象存储系统上传记录
     * @return 结果
     */
    @Override
    public int updateOssUploadRecord(OssUploadRecord ossUploadRecord)
    {
        return ossUploadRecordMapper.updateOssUploadRecord(ossUploadRecord);
    }

    /**
     * 批量删除OSS对象存储系统上传记录
     * 
     * @param ids 需要删除的OSS对象存储系统上传记录主键
     * @return 结果
     */
    @Override
    public int deleteOssUploadRecordByIds(String[] ids)
    {
        int delCounts = 0;

        for (String id: ids
             ) {
            OssUploadRecord record = ossUploadRecordMapper.selectOssUploadRecordById(id);
            ossService.deleteFromAliOss(record.getObjectid());
            ossUploadRecordMapper.deleteOssUploadRecordById(id);
            delCounts++;
        }
        return delCounts;
    }

    /**
     * 删除OSS对象存储系统上传记录信息
     * 
     * @param id OSS对象存储系统上传记录主键
     * @return 结果
     */
    @Override
    public int deleteOssUploadRecordById(String id)
    {
        List<String> keys = new ArrayList<>();
        keys.add(id);
        ossService.deleteFromAliOss(keys);
        return ossUploadRecordMapper.deleteOssUploadRecordById(id);
    }
}
