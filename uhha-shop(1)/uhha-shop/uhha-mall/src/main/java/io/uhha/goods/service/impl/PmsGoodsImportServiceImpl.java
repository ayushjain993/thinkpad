package io.uhha.goods.service.impl;

import io.uhha.common.utils.StringUtils;
import io.uhha.goods.domain.PmsGoods;
import io.uhha.goods.domain.PmsGoodsImport;
import io.uhha.goods.mapper.PmsGoodsImportMapper;
import io.uhha.goods.service.IPmsGood3rdService;
import io.uhha.goods.service.IPmsGoodsImportService;
import io.uhha.goods.service.IPmsGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 商品导入Service业务层处理
 *
 * @author mj
 * @date 2020-07-24
 */
@Service
@Slf4j
public class PmsGoodsImportServiceImpl implements IPmsGoodsImportService {
    @Autowired
    private PmsGoodsImportMapper pmsGoodsImportMapper;

    @Autowired
    private IPmsGood3rdService pmsGood3rdService;

    private final String TAOBAO_DETAIL ="";
    private final String TMALL_DETAIL = "https://detail.tmall.com/item.htm?spm=a230r.1.14.23.76413a9fa3cClf&ns=1&abbucket=1";
    private final String YIWUGOU_DETAIL = "http://www.yiwugo.com/product/detail/";

    private String getProdDetailUrlByName(PmsGoodsImport pmsGoodsImport){
        String name = pmsGoodsImport.getChannel();
        if("taobao".equalsIgnoreCase(name)){
            if(pmsGoodsImport.getTmall() ==1){
                return TMALL_DETAIL+"&"+pmsGoodsImport.getNumIid();
            }
            return TAOBAO_DETAIL;
        }
        if("yiwugo".equalsIgnoreCase(name)){
            return YIWUGOU_DETAIL+pmsGoodsImport.getNumIid()+".html";
        }

        return null;
    }

    /**
     * 查询商品导入
     *
     * @param id 商品导入ID
     * @return 商品导入
     */
    @Override
    public PmsGoodsImport selectPmsGoodsImportById(Long id) {
        return pmsGoodsImportMapper.selectPmsGoodsImportById(id);
    }

    /**
     * 查询商品导入列表
     *
     * @param pmsGoodsImport 商品导入
     * @return 商品导入
     */
    @Override
    public List<PmsGoodsImport> selectPmsGoodsImportList(PmsGoodsImport pmsGoodsImport) {
        return pmsGoodsImportMapper.selectPmsGoodsImportList(pmsGoodsImport);
    }

    /**
     * 新增商品导入
     *
     * @param pmsGoodsImport 商品导入
     * @return 结果
     */
    @Override
    public int insertPmsGoodsImport(PmsGoodsImport pmsGoodsImport) {
        if(!StringUtils.isEmpty(pmsGoodsImport.getNumIid())){
            pmsGoodsImport.setOrgUrl(getProdDetailUrlByName(pmsGoodsImport));
        }
        String picUrl = pmsGoodsImport.getPicUrl();
        if(picUrl.startsWith("//")){
            picUrl = "http:"+picUrl;
            pmsGoodsImport.setPicUrl(picUrl);
        }
        return pmsGoodsImportMapper.insertPmsGoodsImport(pmsGoodsImport);
    }


    /**
     * 修改商品导入
     * @param pmsGoodsImport 商品导入
     * @return
     */
    @Override
    public int updatePmsGoodsImport(PmsGoodsImport pmsGoodsImport) {
        return pmsGoodsImportMapper.updatePmsGoodsImport(pmsGoodsImport);
    }

    /**
     * 发布商品导入
     * @param id 商品导入
     * @return
     * @throws IOException
     */
    @Override
    public int publishPmsGoodsImport(Long id, Long storeId) throws IOException {
        PmsGoodsImport pmsGoodsImport = pmsGoodsImportMapper.selectPmsGoodsImportById(id);
        if(null == pmsGoodsImport){
            log.error("PmsGoodsImport with id {} was not found", id);
            return 0;
        }
        //同步第三方获取商品详情
        return pmsGood3rdService.saveSpu(pmsGoodsImport, storeId);
    }

    /**
     * 批量删除商品导入
     *
     * @param ids 需要删除的商品导入ID
     * @return 结果
     */
    @Override
    public int deletePmsGoodsImportByIds(Long[] ids) {
        return pmsGoodsImportMapper.deletePmsGoodsImportByIds(ids);
    }

    /**
     * 删除商品导入信息
     *
     * @param id 商品导入ID
     * @return 结果
     */
    @Override
    public int deletePmsGoodsImportById(Long id) {
        return pmsGoodsImportMapper.deletePmsGoodsImportById(id);
    }
}
