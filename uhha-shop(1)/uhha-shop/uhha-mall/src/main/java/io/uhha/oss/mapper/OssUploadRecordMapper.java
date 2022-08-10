package io.uhha.oss.mapper;

import java.util.List;
import io.uhha.oss.domain.OssUploadRecord;

/**
 * OSS对象存储系统上传记录Mapper接口
 * 
 * @author uhha
 * @date 2021-09-16
 */
public interface OssUploadRecordMapper 
{
    /**
     * 查询OSS对象存储系统上传记录
     * 
     * @param id OSS对象存储系统上传记录主键
     * @return OSS对象存储系统上传记录
     */
    public OssUploadRecord selectOssUploadRecordById(String id);

    /**
     * 查询OSS对象存储系统上传记录列表
     * 
     * @param ossUploadRecord OSS对象存储系统上传记录
     * @return OSS对象存储系统上传记录集合
     */
    public List<OssUploadRecord> selectOssUploadRecordList(OssUploadRecord ossUploadRecord);

    /**
     * 新增OSS对象存储系统上传记录
     * 
     * @param ossUploadRecord OSS对象存储系统上传记录
     * @return 结果
     */
    public int insertOssUploadRecord(OssUploadRecord ossUploadRecord);

    /**
     * 修改OSS对象存储系统上传记录
     * 
     * @param ossUploadRecord OSS对象存储系统上传记录
     * @return 结果
     */
    public int updateOssUploadRecord(OssUploadRecord ossUploadRecord);

    /**
     * 删除OSS对象存储系统上传记录
     * 
     * @param id OSS对象存储系统上传记录主键
     * @return 结果
     */
    public int deleteOssUploadRecordById(String id);

    /**
     * 批量删除OSS对象存储系统上传记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOssUploadRecordByIds(String[] ids);
}
