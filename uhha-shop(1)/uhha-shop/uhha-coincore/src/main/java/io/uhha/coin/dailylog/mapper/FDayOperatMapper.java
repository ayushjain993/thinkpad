package io.uhha.coin.dailylog.mapper;

import io.uhha.coin.dailylog.domain.FDayOperat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 运营统计-数据库访问接口
 *
 * @author ZKF
 */
@Mapper
public interface FDayOperatMapper {

    /**
     * 分页查询
     *
     * @param map 参数map
     * @return 查询记录列表
     */
    List<FDayOperat> getPageList(Map<String, Object> map);

    /**
     * 查询记录数
     *
     * @param map 参数map
     * @return 查询记录数
     */
    int countPageList(Map<String, Object> map);
}