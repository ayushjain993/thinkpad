package io.uhha.shortvideo.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.shortvideo.mapper.ShortVideoWatchRecordMapper;
import io.uhha.shortvideo.domain.ShortVideoWatchRecord;
import io.uhha.shortvideo.service.IShortVideoWatchRecordService;

/**
 * 短视频观看记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
@Service
public class ShortVideoWatchRecordServiceImpl implements IShortVideoWatchRecordService 
{
    @Autowired
    private ShortVideoWatchRecordMapper shortVideoWatchRecordMapper;

    /**
     * 查询短视频观看记录
     * 
     * @param id 短视频观看记录主键
     * @return 短视频观看记录
     */
    @Override
    public ShortVideoWatchRecord selectShortVideoWatchRecordById(Long id)
    {
        return shortVideoWatchRecordMapper.selectShortVideoWatchRecordById(id);
    }

    /**
     * 查询短视频观看记录列表
     * 
     * @param shortVideoWatchRecord 短视频观看记录
     * @return 短视频观看记录
     */
    @Override
    public List<ShortVideoWatchRecord> selectShortVideoWatchRecordList(ShortVideoWatchRecord shortVideoWatchRecord)
    {
        return shortVideoWatchRecordMapper.selectShortVideoWatchRecordList(shortVideoWatchRecord);
    }

    /**
     * 新增短视频观看记录
     * 
     * @param shortVideoWatchRecord 短视频观看记录
     * @return 结果
     */
    @Override
    public int insertShortVideoWatchRecord(ShortVideoWatchRecord shortVideoWatchRecord)
    {
        return shortVideoWatchRecordMapper.insertShortVideoWatchRecord(shortVideoWatchRecord);
    }

    /**
     * 修改短视频观看记录
     * 
     * @param shortVideoWatchRecord 短视频观看记录
     * @return 结果
     */
    @Override
    public int updateShortVideoWatchRecord(ShortVideoWatchRecord shortVideoWatchRecord)
    {
        return shortVideoWatchRecordMapper.updateShortVideoWatchRecord(shortVideoWatchRecord);
    }

    /**
     * 批量删除短视频观看记录
     * 
     * @param ids 需要删除的短视频观看记录主键
     * @return 结果
     */
    @Override
    public int deleteShortVideoWatchRecordByIds(Long[] ids)
    {
        return shortVideoWatchRecordMapper.deleteShortVideoWatchRecordByIds(ids);
    }

    /**
     * 删除短视频观看记录信息
     * 
     * @param id 短视频观看记录主键
     * @return 结果
     */
    @Override
    public int deleteShortVideoWatchRecordById(Long id)
    {
        return shortVideoWatchRecordMapper.deleteShortVideoWatchRecordById(id);
    }
}
