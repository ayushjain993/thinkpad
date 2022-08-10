package io.uhha.coin.user.service;

import io.uhha.coin.system.domain.SystemCoinSetting;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.coin.system.domain.SystemCoinTypeVO;

import java.util.List;
import java.util.Map;

/**
 * 后台虚拟币接口
 *
 * @author ZKF
 */
public interface IAdminSystemCoinTypeService {

    /**
     * 获取虚拟币列表
     *
     * @param page 分页实体对象
     * @param type 虚拟币实体对象
     * @return 分页实体对象
     */
    Pagination<SystemCoinType> selectVirtualCoinList(Pagination<SystemCoinType> page, SystemCoinType type);

    /**
     * 管理后台使用获取虚拟币列表
     *
     * @param page 分页实体对象
     * @param type 虚拟币实体对象
     * @return 分页实体对象
     */
    Pagination<SystemCoinTypeVO> selectVirtualCoinVOList(Pagination<SystemCoinTypeVO> page, SystemCoinType type);

    /**
     * 查询虚拟币基本信息
     *
     * @param id 虚拟币ID
     * @return 虚拟币实体对象
     */
    SystemCoinType selectVirtualCoinById(int id);

    /**
     * 新增虚拟币
     *
     * @param coin 虚拟币实体对象
     * @return true：成功，false：失败
     */
    boolean insert(SystemCoinType coin);

    /**
     * 修改虚拟币基本信息
     *
     * @param coin 虚拟币实体对象
     * @return true：成功，false：失败
     */
    boolean updateVirtualCoin(SystemCoinType coin);

    /**
     * 启用虚拟币钱包
     *
     * @param coin 虚拟币实体对象
     * @return true：成功，false：失败
     */
    boolean updateVirtualCoinByEnabled(SystemCoinType coin);

    /**
     * 修改钱包链接
     *
     * @param coin 虚拟币实体对象
     * @return true：成功，false：失败
     */
    boolean updateVirtualCoinWalletLink(SystemCoinType coin);

    /***************虚拟币地址操作****************/
    /**
     * 查询虚拟币地址数量列表
     *
     * @param page 分页实体对象
     * @return 分页实体对象
     */
    Pagination<Map<String, Object>> selectVirtualCoinAddressNumList(Pagination<Map<String, Object>> page);

    /**
     * 生成虚拟币地址(内部执行事物，该方法不走事务)
     *
     * @param virtualCoinType 虚拟币实体对象
     * @param count           生成数量
     * @param password        钱包密码
     * @return 200添加成功, 302钱包连接失败，请检查配置信息，303取地址受限，304钱包连接失败，请检查配置信息，未知错误
     */
    int createVirtualCoinAddress(SystemCoinType virtualCoinType, int count, String password);

    /**
     * 根据币种ID查询币种设置
     */
    List<SystemCoinSetting> selectSystemCoinSettingList(Integer coinId);

    /**
     * 根据币种ID,vip1设置 查询币种设置
     */
    List<SystemCoinSetting> selectSystemCoinSettingListByLevel(Integer coinId, Integer level);

    /**
     * 更新币种设置
     */
    boolean updateSystemCoinSetting(SystemCoinSetting record);


}
