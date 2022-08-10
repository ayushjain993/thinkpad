package io.uhha.setting.service;

import io.uhha.setting.domain.LsSystemSeo;

import java.util.List;

/**
 * 系统seo设置Service接口
 *
 * @author mj
 * @date 2020-07-29
 */
public interface ILsSystemSeoService {
    /**
     * 查询系统seo设置
     *
     * @param id 系统seo设置ID
     * @return 系统seo设置
     */
    public LsSystemSeo selectLsSystemSeoById(Long id);

    /**
     * 查询系统seo设置列表
     *
     * @param lsSystemSeo 系统seo设置
     * @return 系统seo设置集合
     */
    public List<LsSystemSeo> selectLsSystemSeoList(LsSystemSeo lsSystemSeo);

    /**
     * 新增系统seo设置
     *
     * @param lsSystemSeo 系统seo设置
     * @return 结果
     */
    public int insertLsSystemSeo(LsSystemSeo lsSystemSeo);

    /**
     * 修改系统seo设置
     *
     * @param lsSystemSeo 系统seo设置
     * @return 结果
     */
    public int updateLsSystemSeo(LsSystemSeo lsSystemSeo);

    /**
     * 批量删除系统seo设置
     *
     * @param ids 需要删除的系统seo设置ID
     * @return 结果
     */
    public int deleteLsSystemSeoByIds(Long[] ids);

    /**
     * 删除系统seo设置信息
     *
     * @param id 系统seo设置ID
     * @return 结果
     */
    public int deleteLsSystemSeoById(Long id);
}
