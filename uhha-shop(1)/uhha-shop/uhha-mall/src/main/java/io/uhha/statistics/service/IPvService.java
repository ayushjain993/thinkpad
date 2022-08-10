package io.uhha.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.statistics.bean.Pv;
import io.uhha.statistics.bean.PvuvStatistics;

import java.util.List;

/**
 * PVService接口
 *
 * @author peter
 * @date 2020-11-17
 */
public interface IPvService extends IService<Pv> {
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

    public List<PvuvStatistics> queryPvUvStatistics(String startTime, String endTime);

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
     * 批量删除PV
     *
     * @param ids 需要删除的PVID
     * @return 结果
     */
    public int deletePvByIds(Long[] ids);

    /**
     * 删除PV信息
     *
     * @param id PVID
     * @return 结果
     */
    public int deletePvById(Long id);

    /**
     * 根据pageId获取点击量
     *
     * @param pageId
     * @return
     */
    public Long selectHitsByPageId(String pageId);

    /**
     * 根据url获取点击量
     *
     * @param url
     * @return
     */
    public Long selectHitsByUrl(String url);

//    /**
//     * 判断pv/uv记录已经记录
//     * @param pv
//     * @return
//     */
//    public int isPvuvRecorded(Pv pv);
}
