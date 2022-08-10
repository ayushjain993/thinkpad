package io.uhha.coin.capital.mapper;

import io.uhha.coin.capital.domain.FTokenCapitalOperation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 钱包充值提现记录数据操作接口
 *
 * @author ZKF
 */
@Mapper
public interface FTokenCapitalOperationMapper {

    /**
     * 新增记录
     *
     * @param record 实体对象
     * @return 成功条数
     */
    int insert(FTokenCapitalOperation record);

    int countTokenCapitalOperation(Map<String, Object> map);

    List<FTokenCapitalOperation> getPageTokenCapitalOperation(Map<String, Object> map);

    FTokenCapitalOperation selectByPrimaryKey(Integer fid);

    int updateByPrimaryKey(FTokenCapitalOperation record);

    /**
     * 根据id查询淘币操作记录
     *
     * @param fid 操作id
     * @return 淘币操作实体
     */
    FTokenCapitalOperation selectTokenById(Integer fid);


    /**
     * 分页查询数据总条数
     *
     * @param map 参数map
     * @return 查询记录数
     */
    int countAdminPage(Map<String, Object> map);

    /**
     * 分页查询数据
     *
     * @param map 参数map
     * @return 操作列表
     */
    List<FTokenCapitalOperation> getAdminPageList(Map<String, Object> map);
}