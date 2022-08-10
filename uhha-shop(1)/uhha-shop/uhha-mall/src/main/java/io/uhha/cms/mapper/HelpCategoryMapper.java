package io.uhha.cms.mapper;

import io.uhha.cms.bean.HelpCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 帮助分类数据库接口
 *
 * Created by mj on 2017/5/23.
 */
@Mapper
public interface HelpCategoryMapper {

    /**
     * 分页查询帮助分类
     *
     * @param params 查询参数
     * @return       返回帮助分类
     */

    List<HelpCategory> queryHelpCategory(Map<String, Object> params);

    /**
     * 查询帮助分类的总记录数
     *
     * @param params 查询参数
     * @return       返回总记录数
     */

    int queryHelpCategoryCount(Map<String, Object> params);

    /**
     * 添加帮助分类
     *
     * @param helpCategory 帮助分类
     * @return             返回 0 失败，1 成功
     */

    int addHelpCategory(HelpCategory helpCategory);

    /**
     * 删除帮助分类
     *
     * @param id 帮助分类id
     * @return   成功返回1  失败返回0
     */

    int deleteHelpCategory(Long id);

    /**
     * 批量删除帮助分类
     *
     * @param ids 帮助分类id数组
     * @return    成功返回1  失败返回0
     */

    int batchDeleteHelpCategory(Long [] ids);

    /**
     * 修改帮助分类
     *
     * @param helpCategory 帮助分类
     * @return             成功返回1，失败返回0
     */

    int updateHelpCategory(HelpCategory helpCategory);

    /**
     * 通过id查找帮助分类
     *
     * @param id 帮助分类id
     * @return   返回帮助分类
     */

    HelpCategory queryHelpCategoryById(Long id);

    /**
     * 查找帮助分类
     *
     * @return 返回帮助分类集合
     */

    List<HelpCategory> queryHelpAllCate();

}
