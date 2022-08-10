package io.uhha.web.controller.store;


import io.swagger.annotations.*;
import io.uhha.coin.common.Enum.validate.BusinessTypeEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.common.notify.ValidateNotifyHelper;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.utils.MessageUtils;
import io.uhha.common.utils.SecurityUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.setting.domain.LsCity;
import io.uhha.setting.domain.LsDistrict;
import io.uhha.setting.domain.LsProvince;
import io.uhha.setting.service.AreaService;
import io.uhha.setting.vo.AreaItem;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.store.vo.StoreBusiness;
import io.uhha.store.vo.StoreBusinessUser;
import io.uhha.system.service.ISysStoreUserService;
import io.uhha.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mj
 * @date 2019-08-16 09:31
 * <p>
 * 新增门店控制器
 */
@RestController
@Api(tags = "新增门店接口")
@Slf4j
public class AddShopController extends BaseController {

    /**
     * 会员服务接口
     */
    @Autowired
    private ISysStoreUserService customerService;


    /**
     * 注入开店店铺信息service
     */
    @Autowired
    private ITStoreInfoService storeInfoService;

    /**
     * 注入地区服务接口
     */
    @Autowired
    private AreaService areaService;

    @Autowired
    private ValidateNotifyHelper validateNotifyHelper;

    /**
     * 查找所有没有开店的用户手机号
     *
     * @return 没有开店用户手机号集合
     */
    @GetMapping("/addshop/customer")
    @ApiOperation(value = "查找所有没有开店的用户手机号", notes = "查找所有没有开店的用户手机号（需要认证）")
    @ApiResponses({
            @ApiResponse(code = 200, message = "没有开店用户手机号集合", response = String.class)
    })
    public List<String> queryAllCustomerMobileForCreateStore() {
        return customerService.queryAllCustomerMobileForCreateStore();
    }


    /**
     * 新增门店时新增管理员用户
     *
     * @param user 管理员信息
     * @return 成功返回 1 失败返回0
     */
    @PostMapping("/addshop/customer")
    @ApiOperation(value = "新增门店时新增管理员用户", notes = "新增门店时新增管理员用户（需要认证）")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功返回 1 失败返回0", response = Integer.class)
    })
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addCustomer(@RequestBody StoreBusinessUser user) {

        SysUser customer = new SysUser();
        BeanUtils.copyProperties(user, customer);

        String code = user.getVerificationCode();
        if (StringUtils.isEmpty(code)) {
            log.error("Invalid user registration. SMS verification code is empty.");
            return AjaxResult.error("Invalid user registration. SMS verification code is empty.");
        } else {
            boolean valid = validateNotifyHelper.validateSmsCode(user.getAreacode(), user.getPhonenumber(), PlatformEnum.SMS.getCode(), BusinessTypeEnum.SMS_STORE_REGISTER.getCode(), code);
            if (!valid) {
                log.warn("Mobile phone verification code error");
                return AjaxResult.error(MessageUtils.message("com.csj.erro.phone"));
            }
        }

        //判断手机号是否已经注册
        SysUser sysUser = customerService.selectUserByMobile(user.getPhonenumber());

        if (sysUser != null) {
            if (sysUser.getStoreId() < 0) {
                customer.setUserId(sysUser.getUserId());
                customer.setPassword(SecurityUtils.encryptPassword(customer.getPassword()));
                customerService.updateUser(customer);
            } else {
                log.error("Invalid user registration. Mobile Number already registered.");
                return AjaxResult.error("Invalid user registration. Mobile Number already registered.");
            }

        } else {
            if (customerService.addCustomer(customer) != 1) {
                log.error("Fail to add customer users.");
                return AjaxResult.error("Fail to add customer users.");
            }
        }

        return toAjax(1);
    }


    /**
     * 校验手机号码是否存在
     *
     * @param mobile 手机号码
     * @return 存在返回>0  不存在返回0
     */
    @GetMapping("/addshop/checkmobile")
    @ApiOperation(value = "校验手机号码是否存在", notes = "校验手机号码是否存在（不需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "mobile", value = "手机号码"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "存在返回>0  不存在返回0", response = Integer.class)
    })
    public int checkMobileExist(String mobile) {
        return customerService.isMobileExist(mobile);
    }


    /**
     * 查询所有省份信息
     *
     * @return 返回所有省份信息
     */
//    @GetMapping("/addshop/provinces")
//    @ApiOperation(value = "查询所有省份信息", notes = "查询所有省份信息（需要认证）")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "返回所有省份信息", response = LsProvince.class)
//    })
//    public List<LsProvince> queryAllProvinces() {
//        return areaService.queryAllProvinces(AreaItem.NO);
//    }

    /**
     * 查询国家下的省份信息
     *
     * @return 返回所有省份信息
     */
    @GetMapping("/addshop/provinces/{parentId}")
    @ApiOperation(value = "查询国家下的省份信息", notes = "查询国家下的省份信息（需要认证）")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回所有省份信息", response = LsProvince.class)
    })
    public List<LsProvince> queryAllProvinces(@PathVariable long parentId) {
        return areaService.queryProvincesByCountryId(parentId);
    }


    /**
     * 根据省份id查询市
     *
     * @param parentId 省份id
     * @return 返回市信息
     */
    @GetMapping("/addshop/city/{parentId}")
    @ApiOperation(value = "根据省份id查询市", notes = "根据省份id查询市（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "parentId", value = "省份id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回市信息", response = LsCity.class)
    })
    public List<LsCity> queryCityByProvinceId(@PathVariable long parentId) {
        return areaService.queryCityByProvinceId(parentId);
    }

    /**
     * 根据市id查询下面的区
     *
     * @param parentId 市id
     * @return 返回市下面的区
     */
    @GetMapping("/addshop/district/{parentId}")
    @ApiOperation(value = "根据市id查询下面的区", notes = "根据市id查询下面的区（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "parentId", value = "市id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回市下面的区", response = LsDistrict.class)
    })
    public List<LsDistrict> queryDistrictByCityId(@PathVariable long parentId) {
        return areaService.queryDistrictByCityId(parentId);
    }


    /**
     * 校验店铺名是否存在
     *
     * @param storeName 店铺名
     * @return >0存在 否则不存在
     */
    @GetMapping("/addshop/storename")
    @ApiOperation(value = "校验店铺名是否存在", notes = "校验店铺名是否存在（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "storeName", value = "店铺名"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = ">0存在 否则不存在", response = Integer.class)
    })
    public int checkStoreNameExist(String storeName) {
        return storeInfoService.checkStoreNameExist(storeName, CommonConstant.QUERY_WITH_NO_STORE);
    }

    /**
     * 校验公司名是否存在
     *
     * @param companyName 公司名
     * @return >0存在 否则不存在
     */
    @GetMapping("/addshop/companyname")
    @ApiOperation(value = "校验公司名是否存在", notes = "校验公司名是否存在（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "companyName", value = "公司名"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = ">0存在 否则不存在", response = Integer.class)
    })
    public int checkCompanyNameExist(String companyName) {
        return storeInfoService.checkCompanyNameExist(companyName, CommonConstant.QUERY_WITH_NO_STORE);
    }


    /**
     * 新增店铺
     *
     * @param storeBusiness 店铺信息
     * @return -1用户不存在 1成功 -2 该用户下已有店铺
     */
    @PostMapping("/addshop")
    @ApiOperation(value = "新增店铺", notes = "新增店铺（需要认证）")
    @ApiResponses({
            @ApiResponse(code = 200, message = "-1用户不存在 1成功 -2 该用户下已有店铺", response = Integer.class)
    })
    public int fillAllStoreInfo(@RequestBody StoreBusiness storeBusiness) {
        return storeInfoService.addStore(storeBusiness);
    }


}
