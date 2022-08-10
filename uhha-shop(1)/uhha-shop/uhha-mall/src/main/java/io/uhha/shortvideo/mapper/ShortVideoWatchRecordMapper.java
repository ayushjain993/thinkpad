package io.uhha.shortvideo.mapper;

import java.util.List;
import io.uhha.shortvideo.domain.ShortVideoWatchRecord;

/**
 * 短视频观看记录Mapper接口
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
public interface ShortVideoWatchRecordMapper 
{
    /**
     * 查询短视频观看记录
     * 
     * @param id 短视频观看记录主键
     * @return 短视频观看记录
     */
    public ShortVideoWatchRecord selectShortVideoWatchRecordById(Long id);

    /**
     * 查询短视频观看记录列表
     * 
     * @param shortVideoWatchRecord 短视频观看记录
     * @return 短视频观看记录集合
     */
    public List<ShortVideoWatchRecord> selectShortVideoWatchRecordList(ShortVideoWatchRecord shortVideoWatchRecord);

    /**
     * 新增短视频观看记录
     * 
     * @param shortVideoWatchRecord 短视频观看记录
     * @return 结果
     */
    public int insertShortVideoWatchRecord(ShortVideoWatchRecord shortVideoWatchRecord);

    /**
     * 修改短视频观看记录
     * 
     * @param shortVideoWatchRecord 短视频观看记录
     * @return 结果
     */
    public int updateShortVideoWatchRecord(ShortVideoWatchRecord shortVideoWatchRecord);

    /**
     * 删除短视频观看记录
     * 
     * @param id 短视频观看记录主键
     * @return 结果
     */
    public int deleteShortVideoWatchRecordById(Long id);

    /**
     * 批量删除短视频观看记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteShortVideoWatchRecordByIds(Long[] ids);
}
