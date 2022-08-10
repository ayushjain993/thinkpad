package io.uhha.member;

import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.member.domain.UmsMemberAddress;
import io.uhha.member.service.IUmsMemberAddressService;
import io.uhha.setting.domain.LsCity;
import io.uhha.setting.domain.LsDistrict;
import io.uhha.setting.domain.LsProvince;
import io.uhha.setting.service.AreaService;
import io.uhha.setting.vo.AreaItem;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户收货地址控制器
 */
@Controller
@Api(tags = "用户收货地址接口")
public class AddressController {


    /**
     * 注入区域服务接口
     */
    @Autowired
    private AreaService areaService;


    /**
     * 注入用户收货地址服务接口
     */
    @Autowired
    private IUmsMemberAddressService customerAddressService;


    /**
     * 查询用户收货地址
     *
     * @return 返回用户所有的收货地址
     */
    @RequestMapping("querycustomeraddress")
    @ResponseBody
    @ApiOperation(value = "查询用户所有收货地址", notes = "查询用户所有收货地址(需要认证)", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户所有的收货地址", response = UmsMemberAddress.class)
    })
    public AjaxResult queryCustomerAddress(HttpServletRequest request) {
        return AjaxResult.success(customerAddressService.queryCustomerAllAddress(AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 根据省份id查询下面的市
     *
     * @param countryId 国家id
     * @return 返回该国家所有省信息
     */
    @ApiOperation(value = "查询国家省的信息", notes = "查询国家省的信息（不需要认证）")
    @GetMapping(value = "/queryprovincesbycountryid")
    @ResponseBody
    @UnAuth
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "countryId", value = "国家id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回该国家所有省信息", response = LsProvince.class)
    })
    public AjaxResult queryProvincesByCountryId(@RequestParam("countryId") Long countryId) {
        return AjaxResult.success(areaService.queryProvincesByCountryId(countryId));
    }

    /**
     * 查询所有省信息
     *
     * @return 返回所有省信息
     */
    @GetMapping(value = "/queryallprovinces")
    @ResponseBody
    @UnAuth
    @ApiOperation(value = "查询所有省市区信息", notes = "查询所有省市区信息(不需要认证)", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回所有省信息", response = LsProvince.class)
    })
    public AjaxResult queryAllProvinces() {
        return AjaxResult.success(areaService.queryAllProvinces(AreaItem.NO));
    }

    /**
     * 根据省份id查询下面的市
     *
     * @param provinceId 省份id
     * @return 返回该省下面的市
     */
    @ApiOperation(value = "根据省份查询下面的市", notes = "根据省份查询下面的市（不需要认证）")
    @GetMapping(value = "/querycitybyprovinceid")
    @ResponseBody
    @UnAuth
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "provinceId", value = "省份id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回该省下面的市", response = LsCity.class)
    })
    public AjaxResult queryCityByProvinceId(@RequestParam("provinceId") Long provinceId) {
        return AjaxResult.success(areaService.queryCityByProvinceId(provinceId));
    }

    /**
     * 根据市id查询下面的区县
     *
     * @param cityId 市id
     * @return 返回市下面的区县
     */
    @ApiOperation(value = "根据市询下面的区县", notes = "根据市询下面的区县（不需要认证）")
    @GetMapping(value = "/querydistrictbycityid")
    @ResponseBody
    @UnAuth
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "cityId", value = "市id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回市下面的区县", response = LsDistrict.class)
    })
    public AjaxResult queryDistrictByCityId(@RequestParam("cityId") Long cityId) {
        return AjaxResult.success(areaService.queryDistrictByCityId(cityId));
    }


    /**
     * 根据收货地址id查询收货地址
     *
     * @param id 收货地址id
     * @return 返回收货信息详情
     */
    @GetMapping(value = "/querycustomeraddressbyid")
    @ResponseBody
    @ApiOperation(value = "根据收货地址id查询收货地址", notes = "根据收货地址id查询收货地址(需要认证)", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "id", value = "收货地址id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回收货信息详情", response = UmsMemberAddress.class)
    })
    public AjaxResult queryCustomerAddress(HttpServletRequest request, @RequestParam("id") Long id) {
        return AjaxResult.success(customerAddressService.queryCustomerAddressById(AppletsLoginUtils.getInstance().getCustomerId(request), id));
    }

    /**
     * 新增用户收货地址
     *
     * @param customerAddress 用户收货地址
     * @return 成功返回1  失败返回0 达到上限返回-1
     */
    @RequestMapping("addaddress")
    @ResponseBody
    @ApiOperation(value = "新增用户收货地址", notes = "新增用户收货地址(需要认证)", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功返回1  失败返回0 达到上限返回-1", response = Integer.class)
    })
    public AjaxResult addAddress(HttpServletRequest request, @RequestBody UmsMemberAddress customerAddress) {
        customerAddress.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        return AjaxResult.success(customerAddressService.addCustomerAddress(customerAddress));
    }

    /**
     * 修改收货地址
     *
     * @param customerAddress 收货地址
     * @return 1:成功 -1:手机格式不对  -2:电话格式不对
     */
    @RequestMapping("updateaddress")
    @ResponseBody
    @ApiOperation(value = "修改收货地址", notes = "修改收货地址(需要认证)", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "1:成功 -1:手机格式不对  -2:电话格式不对", response = Integer.class)
    })
    public AjaxResult updateAddress(HttpServletRequest request, @RequestBody UmsMemberAddress customerAddress) {
        customerAddress.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        return AjaxResult.success(customerAddressService.updateCustomerAddress(customerAddress));
    }

    /**
     * 删除地址
     *
     * @param addressId 地址id
     * @return 1:成功
     */
    @ResponseBody
    @RequestMapping("/deletecustomeraddress")
    @ApiOperation(value = "删除收货地址", notes = "删除收货地址(需要认证)", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "addressId", value = "地址id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "1:成功", response = Integer.class)
    })
    public AjaxResult deleteCustomerAddressById(HttpServletRequest request, @RequestParam("id")Long addressId) {
        return AjaxResult.success(customerAddressService
                .deleteCustomerAddressById(AppletsLoginUtils.getInstance().getCustomerId(request), addressId));
    }
}
