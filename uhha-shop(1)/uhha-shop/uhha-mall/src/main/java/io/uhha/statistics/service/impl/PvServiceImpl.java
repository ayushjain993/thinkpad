package io.uhha.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.redis.RedisMallHelper;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.statistics.bean.Pv;
import io.uhha.statistics.bean.PvuvStatistics;
import io.uhha.statistics.mapper.PvMapper;
import io.uhha.statistics.service.IPvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PvServiceImpl extends ServiceImpl<PvMapper, Pv> implements IPvService {
    @Autowired
    private PvMapper pvMapper;


    @Autowired
    private RedisMallHelper redisMallHelper;

    /**
     * 查询PV
     *
     * @param id PVID
     * @return PV
     */
    @Override
    public Pv selectPvById(Long id) {
        return pvMapper.selectPvById(id);
    }

    /**
     * 查询PV列表
     *
     * @param pv PV
     * @return PV
     */
    @Override
    public List<Pv> selectPvList(Pv pv) {
        return pvMapper.selectPvList(pv);
    }

    @Override
    public List<PvuvStatistics> queryPvUvStatistics(String startTime, String endTime){
        return pvMapper.selectPvUvStatistics(startTime, endTime);
    }

    /**
     * 新增PV
     *
     * @param pv PV
     * @return 结果
     */
    @Override
    public int insertPv(Pv pv) {
        pv.setCreateTime(DateUtils.getNowDate());
        return pvMapper.insertPv(pv);
    }

    /**
     * 修改PV
     *
     * @param pv PV
     * @return 结果
     */
    @Override
    public int updatePv(Pv pv) {
        return pvMapper.updatePv(pv);
    }

    /**
     * 批量删除PV
     *
     * @param ids 需要删除的PVID
     * @return 结果
     */
    @Override
    public int deletePvByIds(Long[] ids) {
        return pvMapper.deletePvByIds(ids);
    }

    /**
     * 删除PV信息
     *
     * @param id PVID
     * @return 结果
     */
    @Override
    public int deletePvById(Long id) {
        return pvMapper.deletePvById(id);
    }

    @Override
    public Long selectHitsByPageId(String pageId) {
        QueryWrapper<Pv> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("page_id", pageId);
        return pvMapper.selectCount(queryWrapper);
    }

    @Override
    public Long selectHitsByUrl(String url) {
        QueryWrapper<Pv> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        return pvMapper.selectCount(queryWrapper);
    }

//    @Override
//    public int isPvuvRecorded(Pv pv) {
//        return pvMapper.isPvuvRecorded(pv);
//    }
}
