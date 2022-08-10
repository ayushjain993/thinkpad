package io.uhha.store.service.impl;

import io.uhha.common.constant.UserConstants;
import io.uhha.common.core.domain.entity.SysDept;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.enums.*;
import io.uhha.common.exception.CustomException;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.uuid.IdUtils;
import io.uhha.goods.domain.PmsBrand;
import io.uhha.goods.domain.PmsBrandApply;
import io.uhha.goods.service.*;
import io.uhha.im.domain.ImUser;
import io.uhha.im.service.ImUserService;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.vo.AccountUser;
import io.uhha.setting.service.BaseInfoSetService;
import io.uhha.settlement.domain.SettlementAccount;
import io.uhha.settlement.service.ISettlementManager;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.domain.TStoreSignedCategory;
import io.uhha.store.mapper.TStoreInfoMapper;
import io.uhha.store.service.*;
import io.uhha.store.vo.OnSaleStoreQueryParam;
import io.uhha.store.vo.StoreBusiness;
import io.uhha.store.vo.StoreBusinessInfo;
import io.uhha.store.vo.StoreReview;
import io.uhha.system.domain.SysUserRole;
import io.uhha.system.service.ISysStoreDeptService;
import io.uhha.system.service.ISysStoreRoleService;
import io.uhha.system.service.ISysStoreUserService;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

/**
 * 店铺信息Service业务层处理
 *
 * @author mj
 * @date 2020-07-28
 */
@Service
public class TStoreInfoServiceImpl implements ITStoreInfoService {
    private static final Logger logger = LoggerFactory.getLogger(TStoreInfoServiceImpl.class);

    /**
     * 注入店铺信息mapper
     */
    @Autowired
    private TStoreInfoMapper storeInfoMapper;

    /**
     * 注入会员service
     */
    @Autowired
    private ISysStoreUserService sysStoreUserService;
    /**
     * 签约分类service
     */
    @Autowired
    private ITStoreSignedCategoryService storeSignedCategoryService;

    /**
     * 品牌申请service
     */
    @Autowired
    private IPmsBrandApplyService brandApplyService;

    /**
     * 品牌service
     */
    @Autowired
    private IPmsBrandService brandService;

    /**
     * 分类service
     */
    @Autowired
    private IPmsCategoryService categoryService;

    /**
     * 注入店铺角色service
     */
    @Autowired
    private ISysStoreRoleService storeRoleService;


    /**
     * 注入店铺评论服务
     */
    @Autowired
    private ITStoreCommentService storeCommentService;
    /**
     * 注入结算服务
     */
    @Autowired
    private ISettlementManager settlementManager;


    /**
     * 注入单品服务
     */
    @Autowired
    private IPmsSkuService skuService;


    /**
     * 注入商品服务
     */
    @Autowired
    private IPmsGoodsService spuService;


    /**
     * 注入门店评价服务
     */
    @Autowired
    private ITStoreEvaluationService storeEvaluationService;

    /**
     * 基本信息
     */
    @Autowired
    private BaseInfoSetService baseInfoSetService;

    @Autowired
    private ISysStoreDeptService deptService;

    @Autowired
    private ImUserService imUserService;

    @Autowired
    private ITStoreInfoChangeHistoryService changeHistoryService;


    /**
     * 根据店铺id查询店铺信息
     *
     * @param storeId 店铺id
     * @return 店铺信息
     */
    @Override
    public TStoreInfo queryStoreInfo(Long storeId) {
        logger.debug("queryAuditPassStoreInfo and storeId:{}", storeId);

        if (CommonConstant.ADMIN_STOREID.equals(storeId)) {
            TStoreInfo storeInfo = new TStoreInfo();
            storeInfo.setId(0L);
            storeInfo.setStoreName("商城自营");
            //TODO 商城自营 logo
            storeInfo.setAvatarPicture("http://yanxuan.nosdn.127.net/56da5270172244be56c00fdc8eb24fae.png");
            return storeInfo;
        }

        return storeInfoMapper.queryStoreInfo(storeId);
    }


    /**
     * 查询开店时填写的信息
     *
     * @param customerId 会员ID
     * @return 店铺信息
     */
    @Override
    public TStoreInfo findOpenStoreInfo(long customerId) {
        logger.debug("findOpenStoreInfo and customerId:{}", customerId);
        SysUser customer = sysStoreUserService.selectUserById(customerId);
        TStoreInfo returnStoreInfo = new TStoreInfo();
        returnStoreInfo.setDelFlag(1);
        TStoreInfo storeInfo = queryStoreInfo(customer.getStoreId());
        return storeInfo == null ? returnStoreInfo : storeInfo;
    }

    /**
     * 开店处理店铺信息
     *
     * @param storeInfo  店铺实体类
     * @param customerId 会员ID
     * @return 返回值跳转页面  0 出错 1 下一页 2店铺首页 3 拒绝通过页 4 登录页
     */
    @Override
    public int dealStoreInfo(TStoreInfo storeInfo, long customerId) {
        logger.debug("dealStoreInfo and storeInfo:{}\r\n customerId:{}", storeInfo, customerId);
        SysUser customer = sysStoreUserService.selectUserById(customerId);
        TStoreInfo queryStoreInfo = storeInfoMapper.queryStoreInfo(customer.getStoreId());
        storeInfo.setStatus("0");
        //店铺信息有无信息

        //无信息   新增
        if (Objects.isNull(queryStoreInfo)) {
            //新增
            if (checkCompanyNameExist(storeInfo.getCompanyName(), CommonConstant.QUERY_WITH_NO_STORE) > 0) {
                logger.error("dealStoreInfo fail :");
                return -1;
            }
            if (storeInfoMapper.addStoreInfo(storeInfo) == 1) {
                //更新会员表中的数据
                SysUser reCustomer = new SysUser();
                reCustomer.setStoreId(storeInfo.getId());
                reCustomer.setUserId(customer.getUserId());
                //更新会员表中的storeI的和type
                return sysStoreUserService.updateUser(reCustomer);
            }
            return 0;
        }

        //有信息 编辑
        storeInfo.setId(queryStoreInfo.getId());
        if (checkCompanyNameExist(storeInfo.getCompanyName(), storeInfo.getId()) > 0) {
            logger.error("dealStoreInfo fail :");
            return -1;
        }
        //编辑
        return storeInfoMapper.editStoreInfo(storeInfo);

    }

    /**
     * 开店-处理店铺经营信息
     *
     * @param customerId  会员ID
     * @param storeName   店铺名称
     * @param categoryIds 分类ids
     * @param brandIds    品牌ids
     * @param brands      自定义品牌集合
     * @return 返回码 1处理成功
     */
    @Override
    @Transactional
    public int dealStoreBusinessInfo(long customerId, String storeName, long[] categoryIds, long[] brandIds, List<PmsBrand> brands) {
        logger.debug("dealStoreBusinessInfo and customerId:{}\r\n storeName:{}\r\n categoryIds:{}\r\n categoryIds:{}\r\n" +
                "brandIds:{}\r\n brands:{}", customerId, storeName, categoryIds, brandIds, brands);
        TStoreInfo storeInfo = storeInfoMapper.queryStoreInfoByName(storeName);
        SysUser customer = sysStoreUserService.selectUserById(customerId);
        if (!Objects.isNull(storeInfo) && customer.getStoreId() != storeInfo.getId()) {
            return -1;
        }
        long storeId = customer.getStoreId();
        String name = customer.getUserName();
        saveOtherStoreInfo(categoryIds, brandIds, brands, storeId, name);
        //更新店铺名称
        return storeInfoMapper.editStoreName(new TStoreInfo().getStoreInfoToEditStoreName(storeId, "1", storeName));
    }

    /**
     * 保存店铺其他信息
     *
     * @param categoryIds  分类ids
     * @param brandIds     品牌ids
     * @param brands       自定义品牌集合
     * @param storeId      店铺id
     * @param customerName 用户名
     */
    private void saveOtherStoreInfo(long[] categoryIds, long[] brandIds, List<PmsBrand> brands, long storeId, String customerName) {
        //先删后增
        storeSignedCategoryService.deleteSignedCategory(storeId);
        brandApplyService.deleteStoreBrand(storeId);
        brandService.batchDeleteCustomBrand(storeId);
        //添加签约分类
        if (!ArrayUtils.isEmpty(categoryIds)) {
            List<TStoreSignedCategory> storeSignedCategories = new ArrayList<>();
            Arrays.stream(categoryIds).forEach(categoryId -> storeSignedCategories.add(new TStoreSignedCategory().getStoreSignedCategory(storeId, categoryId, true)));
            storeSignedCategoryService.addSignedCategory(storeSignedCategories);
        }
        //添加店铺品牌
        if (!ArrayUtils.isEmpty(brandIds)) {
            List<PmsBrandApply> brandApplies = new ArrayList<>();
            Arrays.stream(brandIds).forEach(brandId -> brandApplies.add(new PmsBrandApply().getBrandApply(storeId, brandId)));
            brandApplyService.addStoreBrand(brandApplies);
        }
        //添加自定义品牌
        if (CollectionUtils.isNotEmpty(brands)) {
            List<PmsBrand> brandList = new ArrayList<>();
            brands.forEach(brand -> brandList.add(new PmsBrand().addMySelfBrand(brand.getName(), brand.getUrl(), brand.getCertificatUrl(), storeId, customerName)));
            brandService.batchAddCustomBrand(brandList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addStore(StoreBusiness storeBusiness) {
        logger.debug("addStore and storeBusiness:{}", storeBusiness);

        SysUser customer = sysStoreUserService.selectUserByMobile(storeBusiness.getMobile());
        if (Objects.isNull(customer)) {
            logger.error("addStore fail:member is null");
            return -1;
        }
        //store id was set to -1 by default when creating sysStoreUser
        if (-1 != customer.getStoreId() && CommonConstant.ADMIN_STOREID != customer.getStoreId()) {
            logger.error("addStore fail:already has store");
            return -2;
        }

        //判断名字必须唯一
        TStoreInfo tStoreInfo = storeBusiness.getStoreInfo();
        if (storeInfoMapper.queryStoreInfoByName(tStoreInfo.getStoreName()) != null) {
            logger.error("addStore fail: storename exists!");
            return -3;
        }

        //插入数据库
        storeInfoMapper.insertTStoreInfo(storeBusiness.getStoreInfo());

        //获取所创建的storeId
        TStoreInfo createdStoreInfo = storeInfoMapper.queryStoreInfoByName(tStoreInfo.getStoreName());
        if (createdStoreInfo == null) {
            logger.error("addStore fail: store add failed!");
            return -3;
        }
        long storeId = createdStoreInfo.getId();

        //部门信息初始化
        SysDept storeDept = new SysDept();
        storeDept.setDeptName(tStoreInfo.getStoreName());
        //root的deptId为100
        storeDept.setParentId(100L);
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(storeDept))) {
            logger.error("addStore fail: dept name exists");
            return -2;
        }
        storeDept.setCreateBy("System");
        storeDept.setOrderNum("0");
        storeDept.setPhone(storeBusiness.getMobile());
        storeDept.setStatus(OnOffEnum.ON.getCode());
        storeDept.setEmail(tStoreInfo.getCompanyEmail());

        if (1 != deptService.insertDept(storeDept)) {
            logger.error("addStore fail:fail to add dept");
            return -2;
        }

        SysDept createdDept = deptService.selectDeptByName(tStoreInfo.getStoreName());

        //添加storeId和deptId
        customer.setStoreId(storeId);
        customer.setDeptId(createdDept.getDeptId());

        sysStoreUserService.updateUser(customer);
        saveOtherStoreInfo(storeBusiness.getCategoryIds(), storeBusiness.getBrandIds(), storeBusiness.getBrands(), storeId, customer.getUserName());
        //为该用户添加权限

        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();

        SysUserRole ur = new SysUserRole();
        ur.setUserId(customer.getUserId());
        ur.setRoleId(2L);
        list.add(ur);

        if (list.size() > 0) {
            storeRoleService.batchUserRole(list);
        }
        //是否开启自动审批，开启则设置店铺审核通过
        if (!baseInfoSetService.queryBaseInfoSet().isSpuNeedAudit()) {
            storeBusiness.getStoreInfo().setStatus(StoreStatusEnum.APPROVED.getCode());
        } else {
            storeBusiness.getStoreInfo().setStatus(StoreStatusEnum.UNDER_SCREENING.getCode());
        }
        int res = storeInfoMapper.passStoreAudit(storeBusiness.getStoreInfo());
        if (res == 1) {
            //初始化商家模板
//                   storeTemplateService.initTemplate(storeBusiness.getStoreInfo().getId());
            //添加结算账户
            SettlementAccount settlementAccount = SettlementAccount.builder()
                    .accountName(storeBusiness.getStoreInfo().getStoreName())
                    .accountType(AccountTypeEnum.STORE.name())
                    .associateId(storeBusiness.getStoreInfo().getId())
                    .availableBalance(BigDecimal.ZERO)
                    .frozenBalance(BigDecimal.ZERO)
                    .settlementAfterBalance(BigDecimal.ZERO)
                    .settlementBeforeBalance(BigDecimal.ZERO)
                    .settlementTime(DateUtils.getNowDate())
                    .settlementType(SettlementTypeEnum.AUTO.name()).build();
            settlementManager.insertSettlementAccount(settlementAccount);
            boolean isExist = imUserService.checkExist(ImUserTypeEnum.STORE, customer.getUserId());
            if (!isExist) {
                ImUser imUser = new ImUser();
                imUser.setUserCode(IdUtils.fastSimpleUUID());
                imUser.setUserId(customer.getUserId());
                imUser.setType(ImUserTypeEnum.STORE.name());
                imUser.setNickName(customer.getUserName());
                imUser.setCreateBy(customer.getUserName());
                imUser.setCreateTime(new Date());
                imUser.setUpdateTime(new Date());
                imUserService.save(imUser);
            }
            //记录店铺创建事件
            changeHistoryService.logStoreEvent(StoreChangeEventEnum.CREATED,storeId, storeBusiness.getMobile());
        }


        return 1;


    }

    /**
     * 开店查询店铺信息
     *
     * @param customerId 会员id
     * @param status     品牌状态 状态  0 申请中  1通过 2 拒绝
     * @return 店铺信息
     */
    @Override
    public StoreBusinessInfo queryStoreBusinessInfoForOpneStore(long customerId, String status) {
        long storeId = sysStoreUserService.selectUserById(customerId).getStoreId();
        return new StoreBusinessInfo().getStoreBusinessInfo(queryStoreInfo(storeId), categoryService.queryTwoCategoryByStoreId(storeId),
                categoryService.queryThreeCategoryByStoreId(storeId), brandService.queryStoreBrands(storeId, status),
                brandService.queryCustomBrandByStoreIdAndStatus(storeId, status));
    }

    /**
     * 开店-查询店铺信息
     *
     * @param storeId 店铺id
     * @param status  品牌状态 状态  0 申请中  1通过 2 拒绝
     * @return 店铺信息
     */
    @Override
    @Transactional
    public StoreBusinessInfo queryStoreBusinessInfo(long storeId, String status) {
        logger.debug("queryStoreBusinessInfo and storeId:{}\r\n status:{}", storeId, status);
        return new StoreBusinessInfo().getStoreBusinessInfo(queryStoreInfo(storeId), categoryService.queryTwoCategoryByStoreId(storeId),
                categoryService.queryThreeCategoryByStoreId(storeId), brandService.queryStoreBrands(storeId, status),
                brandService.queryCustomBrandByStoreIdAndStatus(storeId, status));
    }

    /**
     * 查询已审核/未审核商家集合
     *
     * @param pageHelper     分页帮助类
     * @param status         店铺状态 0填写资料中 1店铺审核中 2审核通过 3审核不通过 4店铺关闭
     * @param companyName    公司名称
     * @param storeName      店铺名称
     * @param createTime     创建时间
     * @param customerMobile 用户手机号
     * @param provinceId     省份id
     * @return 已审核/未审核商家集合
     */
    @Override
    public PageHelper<TStoreInfo> queryStoreInfoForAuditList(PageHelper<TStoreInfo> pageHelper, String status, String companyName, String storeName, String createTime, String customerMobile, long provinceId) {
        logger.debug("queryStoreInfoForAuditList and status:{}\r\n companyName:{}\r\n storeName:{}\r\n createTime:{}", status, companyName, storeName, createTime);
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("companyName", companyName);
        params.put("storeName", storeName);
        params.put("createTime", createTime);
        if (!StringUtils.isEmpty(customerMobile)) {
            params.put("mobile", customerMobile);
        }
        params.put("provinceId", provinceId);
        return pageHelper.setListDates(storeInfoMapper.queryStoreInfoForAuditList(pageHelper.getQueryParams(params, storeInfoMapper.queryStoreInfoForAuditListCount(params))));
    }

    /**
     * 编辑店铺有效期,结算周期,是否关店
     *
     * @param storeInfo 店铺信息
     * @return 编辑返回码
     */
    @Override
    @Transactional
    public int editStoreTimeAndIsClose(TStoreInfo storeInfo, Consumer<Integer> consumer) {
        logger.debug("editStoreTimeAndIsClose and storeInfo:{}", storeInfo);
        if (StoreStatusEnum.CLOSED.getCode().equalsIgnoreCase(storeInfo.getStatus())) {
            //更改商品为下架状态
            spuService.updateShelvesStatusByStoreIds("0", Arrays.asList(storeInfo.getId()));
        }
        //更改店铺信息
        storeInfoMapper.editStoreTimeAndIsClose(storeInfo);
        //回调
        consumer.accept(1);
        return 1;
    }

    /**
     * 通过商家审核
     *
     * @param storeInfo 商家实例
     * @return 成功返回1，失败返回0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int passStoreAudit(TStoreInfo storeInfo) {
        logger.debug("passStoreAudit and storeInfo :{}", storeInfo);
        passStoreAuditDealData(storeInfo.getId());
        int res = storeInfoMapper.passStoreAudit(storeInfo);
        if (res == 1) {
            //初始化商家模板
            //storeTemplateService.initTemplate(storeInfo.getId());
        }
        return res;
    }

    /**
     * 拒绝商家审核
     *
     * @param storeInfo 商家实例
     * @return 成功返回1，失败返回0
     */
    @Override
    public int refuseStoreAudit(TStoreInfo storeInfo) {
        logger.debug("refuseStoreAudit and storeInfo :{}", storeInfo);
        return storeInfoMapper.refuseStoreAudit(storeInfo);
    }

    @Override
    public int freeze(TStoreInfo storeInfo) {
        storeInfo.setStatus(StoreStatusEnum.FROZEN.getCode());
        return storeInfoMapper.changeFreezeStatus(storeInfo);
    }

    @Override
    public int unfreeze(TStoreInfo storeInfo) {
        storeInfo.setStatus(StoreStatusEnum.APPROVED.getCode());
        return storeInfoMapper.changeFreezeStatus(storeInfo);
    }

    /**
     * 删除商家
     *
     * @param id 商家id
     * @return 成功返回1，失败返回0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStore(long id) {
        logger.debug("deleteStore and id :{}", id);
        deleteStoreDealData(id);
        return storeInfoMapper.deleteStore(id);
    }

    /**
     * 编辑店铺信息-客服QQ-公司信息-银行信息
     *
     * @param storeInfo 店铺信息实体类
     * @param flag      1客服QQ 2公司信息 3银行信息
     * @return -1参数错误编辑失败 1 编辑成功
     */
    @Override
    public int editMyStoreInfo(TStoreInfo storeInfo, String flag) {
        logger.debug("editMyStoreInfo and storeInfo :{}\r\n flag:{}", storeInfo, flag);
        if ("1".equals(flag)) {
            return storeInfoMapper.editStoreInfoForServiceQQ(storeInfo);
        }
        if ("2".equals(flag)) {
            return storeInfoMapper.editStoreInfoForCompanyInfo(storeInfo);
        }
        if ("3".equals(flag)) {
            return storeInfoMapper.editStoreInfoForBankInfo(storeInfo);
        }
        return -1;
    }

    @Override
    public int queryStoreState(Long storeId) {
        logger.debug("queryStoreState and storeId:{}", storeId);
        if (ObjectUtils.isEmpty(storeId) || storeId <= 0) {
            logger.error("queryStoreState error : storeId is null ");
            return -1;
        }
        TStoreInfo storeInfo = storeInfoMapper.queryStoreInfo(storeId);
        if (ObjectUtils.isEmpty(storeInfo)) {
            logger.error("queryStoreState error : store not exist ");
            return -2;
        }
        if ("1".equals(storeInfo.getDelFlag())) {
            logger.error("queryStoreState error : store is deleted ");
            return -3;
        }
        if (StoreStatusEnum.UNDER_SCREENING.getCode().equalsIgnoreCase(storeInfo.getStatus())) {
            logger.error("queryStoreState error : store is waiting for review ");
            return -4;
        }
        if (StoreStatusEnum.REJECTED.getCode().equalsIgnoreCase(storeInfo.getStatus())) {
            logger.error("queryStoreState error : store is been rejected ");
            return -5;
        }
        if (StoreStatusEnum.CLOSED.getCode().equalsIgnoreCase(storeInfo.getStatus())) {
            logger.error("queryStoreState error : store is close ");
            return -6;
        }
        if (!StoreStatusEnum.APPROVED.getCode().equalsIgnoreCase(storeInfo.getStatus())) {
            logger.error("queryStoreState error : store is not pass ");
            return -4;
        }
        if (storeInfo.getEffectiveTime().isBefore(LocalDateTime.now())) {
            logger.error("queryStoreState error : store is not effective ");
            return -7;
        }
        return 1;
    }

    @Override
    public PageHelper<TStoreInfo> queryStoreInfoForSearch(PageHelper<TStoreInfo> pageHelper, String keyword, int orderBy) {
        logger.debug("queryStoreInfoForSearch and keyword:{} \r\n orderBy:{}", keyword, orderBy);
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("orderBys", orderBy);
        return pageHelper.setListDates(storeInfoMapper.queryStoreInfoForSearch(pageHelper.getQueryParams(params, storeInfoMapper.queryStoreInfoForSearchCount(params)))
                .stream().map(storeInfo -> storeInfo.buildStoreScore(storeCommentService.queryStoreScore(storeInfo.getId()))).collect(toList())
                .stream().map(storeInfo -> storeInfo.bulidSkus(skuService.queryFiveDataForAttentionStore(storeInfo.getId()))).collect(toList())
                .stream().map(storeInfo ->
                        storeInfo.buildSkusCount(skuService.querySkuCountByStoreId(storeInfo.getId()))
                ).collect(toList()));
    }

    @Override
    @Transactional
    public int closeStores(List<Long> ids, Consumer<Integer> consumer) {
        logger.debug("closeStores and ids:{} ", ids);
        if (CollectionUtils.isEmpty(ids)) {
            logger.error("closeStores fail : ids is empty");
            return 0;
        }
        //更改店铺商品状态为下架
        spuService.updateShelvesStatusByStoreIds("0", ids);
        //关闭店铺
        storeInfoMapper.closeStores(ids);
        //回调
        consumer.accept(1);
        return 1;
    }

    @Override
    public int checkStoreNameExist(String storeName, long storeId) {
        logger.debug("checkStoreNameExist and storeName:{}", storeName);
        Map<String, Object> params = new HashMap<>();
        params.put("storeName", storeName);
        params.put("storeId", storeId);
        return storeInfoMapper.queryStoreCountByStoreName(params);
    }

    /**
     * 校验公司名是否存在(store端开店用)
     *
     * @param companyName 店铺名称
     * @param customerId  用户ID
     * @return 0 可用  1 不可用
     */
    @Override
    public int checkCompanyNameForOpenStore(String companyName, long customerId) {
        logger.debug("checkCompanyNameForOpenStore and companyName:{} , customerId: {}", companyName, customerId);
        Map<String, Object> params = new HashMap<>(2);
        params.put("companyName", companyName);
        params.put("storeId", sysStoreUserService.selectUserById(customerId).getStoreId());
        return storeInfoMapper.queryStoreCountByCompanyName(params);
    }

    @Override
    public int checkCompanyNameExist(String companyName, long storeId) {
        logger.debug("checkCompanyNameExist and companyName:{}", companyName);
        Map<String, Object> params = new HashMap<>();
        params.put("companyName", companyName);
        params.put("storeId", storeId);
        return storeInfoMapper.queryStoreCountByCompanyName(params);
    }

    @Override
    public TStoreInfo selStoreInfo(Long storeId) {
        logger.debug("selStoreInfo and storeId:{}", storeId);

        if (Objects.isNull(storeId)) {
            logger.error("selStoreInfo fail : storeId is null");
            return null;
        }
        TStoreInfo storeInfo = this.queryStoreInfo(storeId);
        //获取店铺评分平均数
        storeInfo.setAveScore(storeEvaluationService.queryStoreAveScore(storeId));
        return storeInfo;
    }

    @Override
    public TStoreInfo queryOneOnSaleStore(String skuId, long cityId) {
        logger.debug("queryOneOnSaleStore and skuId:{} \r\n cityId:{}", skuId, cityId);
        TStoreInfo storeInfo = storeInfoMapper.queryOneOnSaleStore(skuId, cityId);
        if (Objects.isNull(storeInfo)) {
            logger.error("queryOneOnSaleStore fail : no storeInfo");
            return null;
        }
        return storeInfo.buildAveScore(storeEvaluationService.queryStoreAveScore(storeInfo.getId()));
    }

    @Override
    public List<TStoreInfo> queryOnSaleStoreList(String skuId, long cityId) {
        logger.debug("queryOnSaleStoreList and skuId:{} \r\n cityId:{}", skuId, cityId);
        return storeInfoMapper.queryOnSaleStoreList(skuId, cityId);
    }

    @Override
    public TStoreInfo queryOneOnSaleStoreByCoordinate(OnSaleStoreQueryParam onSaleStoreQueryParam) {
        logger.debug("queryOneOnSaleStore and OnSaleStoreQueryParam:{}", onSaleStoreQueryParam);
        return storeInfoMapper.queryOneOnSaleStoreByCoordinate(onSaleStoreQueryParam);
    }

    @Override
    public List<TStoreInfo> queryOnSaleStoreListByCoordinate(OnSaleStoreQueryParam onSaleStoreQueryParam) {
        logger.debug("queryOnSaleStoreList and OnSaleStoreQueryParam:{}", onSaleStoreQueryParam);
        return storeInfoMapper.queryOnSaleStoreListByCoordinate(onSaleStoreQueryParam);
    }

    @Override
    public List<TStoreInfo> queryNearByStoreList(BigDecimal longitude, BigDecimal latitude, int distance) {
        logger.debug("queryNearByStoreList and longitude:{} \r\n latitude:{}", longitude, latitude);
        return storeInfoMapper.queryNearByStoreList(longitude, latitude, distance);
    }

    @Override
    public PageHelper<TStoreInfo> queryStoreInfoList(PageHelper<TStoreInfo> pageHelper, String companyName, String storeName, String customerMobile) {
        logger.debug("queryStoreInfoList and pageHelper:{} \r\n companyName:{} \r\n storeName:{} \r\n customerMobile:{} \r\n", pageHelper, companyName, storeName, customerMobile);
        Map<String, Object> params = new HashMap<>();
        params.put("companyName", companyName);
        params.put("storeName", storeName);
        if (!StringUtils.isEmpty(customerMobile)) {
            params.put("mobile", customerMobile);
        }
        return pageHelper.setListDates(storeInfoMapper.queryStoreInfoList(pageHelper.getQueryParams(params, storeInfoMapper.queryStoreInfoListCount(params))));

    }

    @Override
    public int openStoreForOutLetStore(long storeId) {
        logger.debug("openStoreForOutLetStore and storeId:{}", storeId);
        return storeInfoMapper.openStoreForOutLetStore(storeId);
    }

    @Override
    public boolean isEffective(long storeId) {
        logger.debug("isEffective and storeId:{}", storeId);
        if (CommonConstant.ADMIN_STOREID == storeId) {
            return true;
        }
        TStoreInfo storeInfo = storeInfoMapper.queryStoreInfo(storeId);
        return Objects.nonNull(storeInfo) && storeInfo.isEffective();
    }


    @Override
    public List<TStoreInfo> queryNearStoreList(BigDecimal longitude, BigDecimal latitude, int distance) {
        logger.debug("queryNearStoreList and longitude:{} \r\n latitude:{}", longitude, latitude);
        return storeInfoMapper.queryNearStoreList(longitude, latitude, distance)
                .stream().map(storeInfo -> storeInfo.buildStoreScore(storeCommentService.queryStoreScore(storeInfo.getId()))).collect(toList())
                .stream().map(storeInfo -> storeInfo.bulidSkus(skuService.queryFiveDataForAttentionStore(storeInfo.getId()))).collect(toList())
                .stream().map(storeInfo ->
                        storeInfo.buildSkusCount(skuService.querySkuCountByStoreId(storeInfo.getId()))
                ).collect(toList());
    }

    @Override
    public StoreReview queryStoreReview(UmsMember customer) {
        logger.debug("queryStoreReview and member :{}", customer);

        if (Objects.isNull(customer)) {
            logger.error("queryStoreReview fail due to member is null....");
            return StoreReview.buildFail("", "");
        }

        // 根据会员的店铺id查询店铺信息
        TStoreInfo storeInfo = this.queryStoreInfo(sysStoreUserService.selectUserById(customer.getId()).getStoreId());

        logger.info("queryStoreReview and  and storeInfo:{}", storeInfo);

        //如果没有店铺信息，或者开店状态为0，则返回资料未提交完成
        if (Objects.isNull(storeInfo) || "0".equals(storeInfo.getStatus()) || 0 == storeInfo.getId()) {
            logger.info("queryStoreReview :storeInfo is null or status is 0");
            return StoreReview.buildUnFinishData(customer.getUsername());
        }

        //开店状态为2，表示通过审核
        if (StoreStatusEnum.APPROVED.getCode().equalsIgnoreCase(storeInfo.getStatus())) {
            logger.info("queryStoreReview : and revire succes....");
            return StoreReview.buildSuccess(customer.getUsername());
        }
        //审核中
        if (StoreStatusEnum.UNDER_SCREENING.getCode().equalsIgnoreCase(storeInfo.getStatus())) {
            logger.info("queryStoreReview and under revirew....");
            return StoreReview.buildUnderReview(customer.getUsername());
        }

        //审核失败
        if (StoreStatusEnum.REJECTED.getCode().equalsIgnoreCase(storeInfo.getStatus())) {
            logger.error("queryStoreReview and review fail...");
            return StoreReview.buildFail(customer.getUsername(), storeInfo.getReason());
        }

        // 店铺关闭或失效
        if (StoreStatusEnum.CLOSED.getCode().equalsIgnoreCase(storeInfo.getStatus()) || !storeInfo.isEffective()) {
            logger.error("queryStoreReview and store close or not effective...");
            return StoreReview.buildNotEffectiveReview(customer.getUsername());
        }

        return StoreReview.buildFail("", "");
    }

    @Override
    public TStoreInfo checkStore(Long storeId) {
        TStoreInfo tStoreInfo = storeInfoMapper.queryStoreByStoreId(storeId);
        if (tStoreInfo == null) {
            throw new CustomException("店铺不存在");
        }
        //冻结或者关闭不予登录
        if (StoreStatusEnum.FROZEN.getCode().equalsIgnoreCase(tStoreInfo.getStatus())
                ||StoreStatusEnum.CLOSED.getCode().equalsIgnoreCase(tStoreInfo.getStatus())) {
            String description = StoreStatusEnum.getDescription(tStoreInfo.getStatus());
            throw new CustomException(Optional.ofNullable(description).orElse("店铺状态异常"));
        }
        //审批过了但是不在有效期内也不予登录
        if(StoreStatusEnum.APPROVED.getCode().equalsIgnoreCase(tStoreInfo.getStatus())){
            if (!tStoreInfo.isEffective()) {
                throw new CustomException("店铺不在有效期内");
            }
        }
        return tStoreInfo;
    }

    /**
     * 店铺审核通过后处理相关数据
     *
     * @param storeId 店铺id
     */
    @Transactional(rollbackFor = Exception.class)
    protected void passStoreAuditDealData(long storeId) {
        //为该用户添加权限
        //  storeRoleService.linkStaffRole(new RoleAndCustomer().getRoleAndCustomer(sysStoreUserService.queryCustomerIdByStoreId(storeId).getId(), 3));
        //将该店铺下的自定义品牌变为审核通过
        brandService.passCustomBrandByStoreId(storeId);
        //将店铺的签约品牌变为审核通过
        brandApplyService.passBrandAuditByStoreId(storeId);
    }

    /**
     * 删除店铺后处理数据
     *
     * @param storeId 店铺id
     */
    @Transactional(rollbackFor = Exception.class)
    protected void deleteStoreDealData(long storeId) {
        //删除签约分类
        storeSignedCategoryService.deleteSignedCategory(storeId);
        //删除店铺品牌
        brandApplyService.deleteStoreBrand(storeId);
        //删除自定义品牌
        brandService.batchDeleteCustomBrand(storeId);
    }

    /**
     * 更新商铺类型和费率
     * @param merchantType
     * @param platformFeeRate
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateMerchantTypeOrFee(Long storeId, String merchantType, BigDecimal platformFeeRate) {
        int ret = storeInfoMapper.updateMerchantTypeOrFee(storeId, merchantType, platformFeeRate);

        //记录店铺创建事件
        if(ret ==1) {
            changeHistoryService.logStoreEvent(StoreChangeEventEnum.MODIFY_MERCHANT_PLARTOFMR_RATE, storeId, "");
        }
        return ret;
    }


    /**
     * 查询店铺信息
     *
     * @param id 店铺信息ID
     * @return 店铺信息
     */
    @Override
    public TStoreInfo selectTStoreInfoById(Long id) {
        return storeInfoMapper.selectTStoreInfoById(id);
    }

    /**
     * 查询店铺信息列表
     *
     * @param tStoreInfo 店铺信息
     * @return 店铺信息
     */
    @Override
    public List<TStoreInfo> selectTStoreInfoList(TStoreInfo tStoreInfo) {
        return storeInfoMapper.selectTStoreInfoList(tStoreInfo);
    }

    /**
     * 新增店铺信息
     *
     * @param tStoreInfo 店铺信息
     * @return 结果
     */
    @Override
    public int insertTStoreInfo(TStoreInfo tStoreInfo) {
        tStoreInfo.setCreateTime(DateUtils.getNowDate());
        return storeInfoMapper.insertTStoreInfo(tStoreInfo);
    }

    /**
     * 修改店铺信息
     *
     * @param tStoreInfo 店铺信息
     * @return 结果
     */
    @Override
    public int updateTStoreInfo(TStoreInfo tStoreInfo) {
        tStoreInfo.setUpdateTime(DateUtils.getNowDate());
        return storeInfoMapper.updateTStoreInfo(tStoreInfo);
    }

    /**
     * 批量删除店铺信息
     *
     * @param ids 需要删除的店铺信息ID
     * @return 结果
     */
    @Override
    public int deleteTStoreInfoByIds(Long[] ids) {
        return storeInfoMapper.deleteTStoreInfoByIds(ids);
    }

    /**
     * 删除店铺信息信息
     *
     * @param id 店铺信息ID
     * @return 结果
     */
    @Override
    public int deleteTStoreInfoById(Long id) {
        return storeInfoMapper.deleteTStoreInfoById(id);
    }

    @Override
    public List<AccountUser> queryNotSettlementAccount() {
        return storeInfoMapper.queryNotSettlementAccount();
    }
}
