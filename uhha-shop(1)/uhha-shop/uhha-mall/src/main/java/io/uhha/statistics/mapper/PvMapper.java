package io.uhha.statistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.statistics.bean.PvuvStatistics;
import io.uhha.statistics.bean.Pv;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PvMapper extends BaseMapper<Pv> {
    /**
     * 查询PV
     *
     * @param id PVID
     * @return PV
     */
    public Pv selectPvById(Long id);

    /**
     * 查询PV列表
     *
     * @param pv PV
     * @return PV集合
     */
    public List<Pv> selectPvList(Pv pv);

    public List<PvuvStatistics> selectPvUvStatistics(@Param("startTime")String startTime, @Param("endTime")String endTime);

    /**
     * 新增PV
     *
     * @param pv PV
     * @return 结果
     */
    public int insertPv(Pv pv);

    /**
     * 修改PV
     *
     * @param pv PV
     * @return 结果
     */
    public int updatePv(Pv pv);

    /**
     * 删除PV
     *
     * @param id PVID
     * @return 结果
     */
    public int deletePvById(Long id);

    /**
     * 批量删除PV
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePvByIds(Long[] ids);

    public int isPvuvRecorded(Pv pv);
}
