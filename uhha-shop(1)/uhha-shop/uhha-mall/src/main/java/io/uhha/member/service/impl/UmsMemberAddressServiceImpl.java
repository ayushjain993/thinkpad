package io.uhha.member.service.impl;

import io.uhha.common.utils.DateUtils;
import io.uhha.member.domain.UmsMemberAddress;
import io.uhha.member.mapper.UmsMemberAddressMapper;
import io.uhha.member.service.IUmsMemberAddressService;
import io.uhha.setting.domain.LsCity;
import io.uhha.setting.domain.LsDistrict;
import io.uhha.setting.domain.LsProvince;
import io.uhha.setting.service.ILsCityService;
import io.uhha.setting.service.ILsDistrictService;
import io.uhha.setting.service.ILsProvinceService;
import io.uhha.system.domain.FCountry;
import io.uhha.system.service.IFCountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * 用户收货地址Service业务层处理
 *
 * @author mj
 * @date 2020-07-25
 */
@Service
public class UmsMemberAddressServiceImpl implements IUmsMemberAddressService {
    @Autowired
    private UmsMemberAddressMapper umsMemberAddressMapper;
    /**
     * 调试日志
     */
    private Logger logger = LoggerFactory.getLogger(UmsMemberAddressServiceImpl.class);

    /**
     * 注入数据库接口
     */
    @Autowired
    private UmsMemberAddressMapper customerAddressMapper;

    @Autowired
    private IFCountryService countryService;

    @Autowired
    private ILsProvinceService provinceService;

    @Autowired
    private ILsCityService cityService;

    @Autowired
    private ILsDistrictService districtService;

    @Override
    public UmsMemberAddress queryCustomerDefaultAddress(Long customerId) {
        logger.debug("queryCustomerDefaultAddress and customerId:{}", customerId);
        return customerAddressMapper.queryCustomerDefaultAddressByCustomerId(customerId);
    }

    @Override
    public UmsMemberAddress queryCustomerAddressById(Long customerId, Long addressId) {
        logger.debug("queryCustomerAddressById and customerId:{} \r\n addressId:{}", customerId, addressId);
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customerId);
        if (ObjectUtils.isEmpty(addressId)) {
            logger.error("queryCustomerAddressById fail: addressId is null");
            return null;
        }
        map.put("addressId", addressId);
        UmsMemberAddress customerAddress = customerAddressMapper.queryCustomerAddressById(map);
        if (ObjectUtils.isEmpty(customerAddress)) {
            logger.error("queryCustomerAddressById fail: query null");
            return null;
        }
        return customerAddress;
    }

    @Override
    public List<UmsMemberAddress> queryCustomerAllAddress(Long customerId) {
        logger.debug("queryCustomerAllAddress and customerId:{}", customerId);
        return customerAddressMapper.queryCustomerAllAddressByCustomerId(customerId)
                .stream().map(customerAddress -> customerAddress.hideMobileAndPhone()).collect(toList());
    }

    @Override
    @Transactional
    public int deleteCustomerAddressById(Long customerId, Long addressId) {
        logger.debug("deleteCustomerAddressById and customerId:{} \r\n addressId:{}", customerId, addressId);
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customerId);
        map.put("addressId", addressId);
        return customerAddressMapper.deleteCustomerAddressById(map);
    }

    @Override
    @Transactional
    public int clearDefaultAddressByCustomerId(Long customerId) {
        logger.debug("clearDefaultAddressByCustomerId and customerId:{}", customerId);
        return customerAddressMapper.clearDefaultAddressByCustomerId(customerId) >= 0 ? 1 : 0;
    }

    @Override
    @Transactional
    public int setDefaultAddressById(Long customerId, Long addressId) {
        logger.debug("setDefaultAddressById and  customerId:{} \n addressId:{}", customerId, addressId);
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customerId);
        map.put("addressId", addressId);
        clearDefaultAddressByCustomerId(customerId);
        return customerAddressMapper.setDefaultAddressById(map);
    }

    @Override
    @Transactional
    public int addCustomerAddress(UmsMemberAddress customerAddress) {
        logger.debug("addCustomerAddress and customerAddress:{}", customerAddress);
        if (queryCustomerAllAddress(customerAddress.getCustomerId()).size() < 20) {
            //如果设置默认地址，清除其他默认地址
            if (!StringUtils.isEmpty(customerAddress.getIsDefault()) && "1".equals(customerAddress.getIsDefault())) {
                clearDefaultAddressByCustomerId(customerAddress.getCustomerId());
            }
            //填充地址信息
            setOtherAddressInfo(customerAddress);
            return customerAddressMapper.addCustomerAddress(customerAddress);
        } else {
            logger.error("addCustomerAddress fail : over 20");
            return -1;
        }
    }

    /**
     * 填充国家省市县信息
     * @param customerAddress 用户地址
     */
    private void setOtherAddressInfo(UmsMemberAddress customerAddress){

        FCountry country = countryService.selectFCountryById(customerAddress.getCountryId());
        LsProvince province = provinceService.selectLsProvinceById(customerAddress.getProvinceId());
        LsCity city = cityService.selectLsCityById(customerAddress.getCityId());
        LsDistrict district = districtService.selectLsDistrictById(customerAddress.getCountyId());

        String address="";

        String strCountry = country==null? "":country.getEnglishName();

        if("China".equalsIgnoreCase(strCountry)){
            String strProvince = province ==null? "":province.getName();
            String strCity = city==null? "":city.getName();
            String strDistrict = district==null? "":district.getName();

            address = strCountry+strProvince+strCity+strDistrict+customerAddress.getDetailAddress();
        }else{
            String strProvince = province ==null? "":province.getName();
            String strCity = city==null? "":city.getName();
            String strDistrict = district==null? "":district.getName();

            address = customerAddress.getDetailAddress()+","+strDistrict+","+strCity+","+strProvince+","+strCountry;
        }

        customerAddress.setAddress(address);
    }

    @Override
    @Transactional
    public int updateCustomerAddress(UmsMemberAddress customerAddress) {
        logger.debug("updateCustomerAddress and customerAddress:{}", customerAddress);
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customerAddress.getCustomerId());
        map.put("addressId", customerAddress.getId());
        UmsMemberAddress old = customerAddressMapper.queryCustomerAddressById(map);
        UmsMemberAddress oldHidden = new UmsMemberAddress();
        BeanUtils.copyProperties(old, oldHidden);
        oldHidden.hideMobileAndPhone();
        if (!StringUtils.isEmpty(customerAddress.getMobile()) && !StringUtils.isEmpty(oldHidden.getMobile())) {
            if (customerAddress.getMobile().equals(oldHidden.getMobile())) {
                customerAddress.setMobile(old.getMobile());
            } else {
                if (customerAddress.getMobile().contains("*")) {
                    logger.error("updateCustomerAddress fail : mobile format error");
                    return -1;
                }
            }
        }
        if (!StringUtils.isEmpty(customerAddress.getPhone()) && !StringUtils.isEmpty(oldHidden.getPhone())) {
            if (customerAddress.getPhone().equals(oldHidden.getPhone())) {
                customerAddress.setPhone(old.getPhone());
            } else {
                if (customerAddress.getPhone().contains("*")) {
                    logger.error("updateCustomerAddress fail : phone format error");
                    return -2;
                }
            }
        }
        //更新时如果设置默认地址，清除其他默认地址
        if (!StringUtils.isEmpty(customerAddress.getIsDefault()) && "1".equals(customerAddress.getIsDefault())) {
            clearDefaultAddressByCustomerId(customerAddress.getCustomerId());
        }
        //填充地址信息
        setOtherAddressInfo(customerAddress);
        return customerAddressMapper.updateCustomerAddress(customerAddress);
    }

    @Override
    public UmsMemberAddress queryCustomerDefaultChosenAddress(long customerId) {
        logger.debug("queryCustomerDefaultChosenAddress and customerId:{}", customerId);
        return customerAddressMapper.queryCustomerDefaultChosenAddress(customerId);
    }

    /**
     * 查询用户收货地址
     *
     * @param id 用户收货地址ID
     * @return 用户收货地址
     */
    @Override
    public UmsMemberAddress selectUmsMemberAddressById(Long id) {
        return umsMemberAddressMapper.selectUmsMemberAddressById(id);
    }

    /**
     * 查询用户收货地址列表
     *
     * @param umsMemberAddress 用户收货地址
     * @return 用户收货地址
     */
    @Override
    public List<UmsMemberAddress> selectUmsMemberAddressList(UmsMemberAddress umsMemberAddress) {
        return umsMemberAddressMapper.selectUmsMemberAddressList(umsMemberAddress);
    }

    /**
     * 新增用户收货地址
     *
     * @param umsMemberAddress 用户收货地址
     * @return 结果
     */
    @Override
    public int insertUmsMemberAddress(UmsMemberAddress umsMemberAddress) {
        umsMemberAddress.setCreateTime(DateUtils.getNowDate());
        return umsMemberAddressMapper.insertUmsMemberAddress(umsMemberAddress);
    }

    /**
     * 修改用户收货地址
     *
     * @param umsMemberAddress 用户收货地址
     * @return 结果
     */
    @Override
    public int updateUmsMemberAddress(UmsMemberAddress umsMemberAddress) {
        umsMemberAddress.setUpdateTime(DateUtils.getNowDate());
        return umsMemberAddressMapper.updateUmsMemberAddress(umsMemberAddress);
    }

    /**
     * 批量删除用户收货地址
     *
     * @param ids 需要删除的用户收货地址ID
     * @return 结果
     */
    @Override
    public int deleteUmsMemberAddressByIds(Long[] ids) {
        return umsMemberAddressMapper.deleteUmsMemberAddressByIds(ids);
    }

    /**
     * 删除用户收货地址信息
     *
     * @param id 用户收货地址ID
     * @return 结果
     */
    @Override
    public int deleteUmsMemberAddressById(Long id) {
        return umsMemberAddressMapper.deleteUmsMemberAddressById(id);
    }
}
