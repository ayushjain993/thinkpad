package io.uhha.coin.user.service.impl;

import io.uhha.coin.capital.mapper.FPoolMapper;
import io.uhha.coin.common.Enum.SystemCoinSortEnum;
import io.uhha.coin.system.domain.SystemCoinTypeVO;
import io.uhha.coin.common.Enum.SystemCoinStatusEnum;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.common.coin.CoinDriverFactory;
import io.uhha.coin.common.coin.driver.BTCDriver;
import io.uhha.coin.common.coin.driver.ETHDriver;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.coin.system.service.ICoinRedisInitService;
import io.uhha.common.constant.Constant;
import io.uhha.common.utils.Utils;
import io.uhha.coin.system.domain.SystemCoinSetting;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.system.mapper.SystemCoinSettingMapper;
import io.uhha.coin.system.mapper.SystemCoinTypeMapper;
import io.uhha.coin.user.service.IAdminSystemCoinTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service("adminSystemCoinTypeService")
public class AdminSystemCoinTypeServiceImpl implements IAdminSystemCoinTypeService {


    @Autowired
    private SystemCoinTypeMapper systemCoinTypeMapper;
    @Autowired
    private FPoolMapper poolMapper;
    @Autowired
    private ICoinRedisInitService systemRedisInitService;
    @Autowired
    private AdminVirtualCoinServiceTx adminVirtualCoinServiceTx;
    @Autowired
    private SystemCoinSettingMapper systemCoinSettingMapper;

    /**
     * 获取虚拟币列表
     *
     * @param page 分页实体对象
     * @param type 虚拟币实体对象
     * @return 分页实体对象
     */
    @Override
    public Pagination<SystemCoinType> selectVirtualCoinList(Pagination<SystemCoinType> page, SystemCoinType type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", page.getOffset());
        map.put("limit", page.getPageSize());
        map.put("keyword", page.getKeyword());
        map.put("orderfield", page.getOrderField());
        map.put("orderdirection", page.getOrderDirection());
        map.put("coinType", type.getCoinType());

        int count = systemCoinTypeMapper.getSystemCoinTypeCount(map);
        if (count > 0) {
            List<SystemCoinType> articleList = systemCoinTypeMapper.getSystemCoinTypeList(map);
            page.setData(articleList);
        }
        page.setTotalRows(count);

        return page;
    }

    /**
     * 管理后台获取虚拟币列表
     *
     * @param page 分页实体对象
     * @param type 虚拟币实体对象
     * @return 分页实体对象
     */
    @Override
    public Pagination<SystemCoinTypeVO> selectVirtualCoinVOList(Pagination<SystemCoinTypeVO> page, SystemCoinType type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", page.getOffset());
        map.put("limit", page.getPageSize());
        map.put("keyword", page.getKeyword());
        map.put("orderfield", page.getOrderField());
        map.put("orderdirection", page.getOrderDirection());
        map.put("coinType", type.getCoinType());

        int count = systemCoinTypeMapper.getSystemCoinTypeCount(map);
        if (count > 0) {
            List<SystemCoinType> dataList = systemCoinTypeMapper.getSystemCoinTypeList(map);
            List<SystemCoinTypeVO> voDataList = new ArrayList<>();

            //取出ETH的统计信息
            AtomicReference<SystemCoinTypeVO> aEth = new AtomicReference<>();
            dataList.stream().forEach(x -> {
                if ("ETH".equalsIgnoreCase(x.getShortName())) {
                    aEth.set(poolMapper.countVirtualCoinAddressNum(x.getId()));
                }
            });

            SystemCoinTypeVO eth = aEth.get();
            dataList.stream().forEach(x -> {
                SystemCoinTypeVO vo = poolMapper.countVirtualCoinAddressNum(x.getId());
                BeanUtils.copyProperties(x,vo);
                //ETH或者ETH类
                if ("ETH".equalsIgnoreCase(x.getShortName())||SystemCoinSortEnum.ETH.getCode().equals(x.getCoinType())) {
                    vo.setUsed(eth.getUsed());
                    vo.setNotused(eth.getNotused());
                }else{
                    //其他，暂时不支持
                    vo.setUsed(0);
                    vo.setNotused(0);
                }
                voDataList.add(vo);
            });

            page.setData(voDataList);
        }
        page.setTotalRows(count);

        return page;
    }

    /**
     * 查询虚拟币基本信息
     *
     * @param id 虚拟币ID
     * @return 虚拟币实体对象
     * @see IAdminSystemCoinTypeService#selectVirtualCoinById(int)
     */
    @Override
    public SystemCoinType selectVirtualCoinById(int id) {
        return systemCoinTypeMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增虚拟币
     *
     * @param coin 虚拟币实体对象
     * @return true：成功，false：失败
     * @see IAdminSystemCoinTypeService#insert
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insert(SystemCoinType coin) {
        int result = systemCoinTypeMapper.insert(coin);
        if (result <= 0) {
            return false;
        }
        //更新虚拟币手续费
        for (Integer level : Constant.VIP_LEVEL) {
            SystemCoinSetting setting = new SystemCoinSetting();
            setting.setCoinId(coin.getId());
            setting.setLevelVip(level);
            setting.setWithdrawMax(BigDecimal.ZERO);
            setting.setWithdrawMin(BigDecimal.ZERO);
            setting.setWithdrawFee(BigDecimal.ZERO);
            setting.setWithdrawTimes(0);
            setting.setWithdrawDayLimit(BigDecimal.ZERO);
            setting.setGmtCreate(new Date());
            setting.setGmtModified(new Date());
            setting.setVersion(0);
            systemCoinSettingMapper.insert(setting);
        }
        //跟新redis中的虚拟币列表
        systemRedisInitService.initSystemCoinType();
        systemRedisInitService.initCoinSetting();
        return true;
    }

    /**
     * 修改虚拟币基本信息
     *
     * @param coin 虚拟币实体对象
     * @return true：成功，false：失败
     * @see IAdminSystemCoinTypeService#updateVirtualCoin
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateVirtualCoin(SystemCoinType coin) {
        int result = systemCoinTypeMapper.updateSystemCoinType(coin);
        if (result <= 0) {
            return false;
        }
        boolean updateFage = false;
        for (Integer level : Constant.VIP_LEVEL) {
            SystemCoinSetting feesList = systemCoinSettingMapper.selectSystemCoinSetting(coin.getId(), level);
            if (feesList == null) {
                SystemCoinSetting setting = new SystemCoinSetting();
                setting.setCoinId(coin.getId());
                setting.setLevelVip(level);
                setting.setWithdrawMax(BigDecimal.ZERO);
                setting.setWithdrawMin(BigDecimal.ZERO);
                setting.setWithdrawFee(BigDecimal.ZERO);
                setting.setWithdrawTimes(0);
                setting.setWithdrawDayLimit(BigDecimal.ZERO);
                setting.setGmtCreate(new Date());
                setting.setGmtModified(new Date());
                setting.setVersion(0);
                systemCoinSettingMapper.insert(setting);
                updateFage = true;
            }
        }
        //跟新redis中的虚拟币列表
        systemRedisInitService.initSystemCoinType();
        if (updateFage) {
            systemRedisInitService.initCoinSetting();
        }
        return true;
    }

    /**
     * 启用虚拟币钱包
     *
     * @param coin 虚拟币实体对象
     * @return true：成功，false：失败
     */
    @Override
    public boolean updateVirtualCoinByEnabled(SystemCoinType coin) {
        int result = systemCoinTypeMapper.updateSystemCoinTypeStatus(coin);
        if (result <= 0) {
            return false;
        }
        if (!coin.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode())) {
            boolean updateFage = false;
            for (Integer level : Constant.VIP_LEVEL) {
                SystemCoinSetting feesList = systemCoinSettingMapper.selectSystemCoinSetting(coin.getId(), level);
                if (feesList == null) {
                    SystemCoinSetting setting = new SystemCoinSetting();
                    setting.setCoinId(coin.getId());
                    setting.setLevelVip(level);
                    setting.setWithdrawMax(BigDecimal.ZERO);
                    setting.setWithdrawMin(BigDecimal.ZERO);
                    setting.setWithdrawFee(BigDecimal.ZERO);
                    setting.setWithdrawTimes(0);
                    setting.setWithdrawDayLimit(BigDecimal.ZERO);
                    setting.setGmtCreate(new Date());
                    setting.setGmtModified(new Date());
                    setting.setVersion(0);
                    systemCoinSettingMapper.insert(setting);
                    updateFage = true;
                }
                if (updateFage) {
                    systemRedisInitService.initCoinSetting();
                }
            }
            //分配虚拟币钱
            //TODO 分配虚拟币钱
//            Integer coinId = coin.getId();
//            List<FUser> userList = userMapper.selectAll();
//            for (FUser user : userList) {
//                try {
//                    adminVirtualCoinServiceTx.insertCoinWallet(coinId, user.getFid());
//                } catch (Exception e) {
//                }
//            }
        }
        //跟新redis中的虚拟币列表
        systemRedisInitService.initSystemCoinType();
        return true;
    }

    /**
     * 修改钱包链接
     *
     * @param coin 虚拟币实体对象
     * @return true：成功，false：失败
     * @see IAdminSystemCoinTypeService#updateVirtualCoinWalletLink
     */
    @Override
    public boolean updateVirtualCoinWalletLink(SystemCoinType coin) {
        int result = systemCoinTypeMapper.updateSystemCoinTypeLink(coin);
        if (result <= 0) {
            return false;
        }
        boolean updateFage = false;
        for (Integer level : Constant.VIP_LEVEL) {
            SystemCoinSetting feesList = systemCoinSettingMapper.selectSystemCoinSetting(coin.getId(), level);
            if (feesList == null) {
                SystemCoinSetting setting = new SystemCoinSetting();
                setting.setCoinId(coin.getId());
                setting.setLevelVip(level);
                setting.setWithdrawMax(BigDecimal.ZERO);
                setting.setWithdrawMin(BigDecimal.ZERO);
                setting.setWithdrawFee(BigDecimal.ZERO);
                setting.setWithdrawTimes(0);
                setting.setWithdrawDayLimit(BigDecimal.ZERO);
                setting.setGmtCreate(new Date());
                setting.setGmtModified(new Date());
                setting.setVersion(0);
                systemCoinSettingMapper.insert(setting);
                updateFage = true;
            }
        }
        //跟新redis中的虚拟币列表
        systemRedisInitService.initSystemCoinType();
        if (updateFage) {
            systemRedisInitService.initCoinSetting();
        }
        return true;
    }


    /***************虚拟币地址操作****************/

    /**
     * 查询虚拟币地址数量列表
     *
     * @param page 分页实体对象
     * @return 分页实体对象
     * @see IAdminSystemCoinTypeService#selectVirtualCoinAddressNumList
     */
    @Override
    public Pagination<Map<String, Object>> selectVirtualCoinAddressNumList(Pagination<Map<String, Object>> page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", page.getOffset());
        map.put("limit", page.getPageSize());
        map.put("keyword", page.getKeyword());
        int count = poolMapper.countVirtualCoinAddressNumList(map);
        if (count > 0) {
            List<Map<String, Object>> addressList = poolMapper.getVirtualCoinAddressNumList(map);
            page.setData(addressList);
        }
        page.setTotalRows(count);

        return page;
    }

    /**
     * 生成虚拟币地址
     *
     * @param coinType 虚拟币实体对象
     * @param count    生成数量
     * @param password 钱包密码
     * @return 200添加成功, 302钱包连接失败，请检查配置信息，303取地址受限，304钱包连接失败，请检查配置信息，未知错误
     * @see IAdminSystemCoinTypeService#createVirtualCoinAddress
     * int, String)
     */
    @Override
    public int createVirtualCoinAddress(SystemCoinType coinType, int count, String password) {
        String accesskey = coinType.getAccessKey();
        String secretkey = coinType.getSecrtKey();
        String ip = coinType.getIp();
        String port = coinType.getPort();
        if (accesskey == null || secretkey == null || ip == null || port == null) {
            return 301;
        }
        CoinDriver coinDriver = CoinDriverFactory.getDriver(coinType.getCoinType(), accesskey, secretkey, ip, port,
                password, coinType.getAssetId(), coinType.getEthAccount(), coinType.getShortName());

        String address = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        try {
            for (int i = 0; i < count; i++) {
                if ("BTC".equalsIgnoreCase(coinType.getShortName())) {
                    address = ((BTCDriver) coinDriver).getNewAddress();
                } else if ("ETH".equalsIgnoreCase(coinType.getShortName())) {
                    //FIXME
                    address = ((ETHDriver) coinDriver).getNewAddress(sdf.format(Utils.getTimestamp()));
                } else {
                    address = coinDriver.getNewAddress(sdf.format(Utils.getTimestamp()));
                }
                if (address == null || address.trim().length() == 0) {
                    continue;
                }
                try {
                    adminVirtualCoinServiceTx.insertPoolInfo(coinType.getId(), address);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return 200;
        } catch (Exception e) {
            return 304;
        } finally {
            try {
                coinDriver.walletLock();
            } catch (Exception e) {
                return 304;
            }
        }
    }

    @Override
    public List<SystemCoinSetting> selectSystemCoinSettingList(Integer coinId) {
        return systemCoinSettingMapper.selectListByCoinId(coinId);
    }

    @Override
    public List<SystemCoinSetting> selectSystemCoinSettingListByLevel(Integer coinId, Integer level) {
        return systemCoinSettingMapper.selectListByLevel(coinId, level);
    }

    @Override
    public boolean updateSystemCoinSetting(SystemCoinSetting record) {
        if (systemCoinSettingMapper.updateByPrimaryKey(record) > 0) {
            systemRedisInitService.initCoinSetting();
            return true;
        }
        return false;
    }

}
