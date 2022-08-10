package io.uhha.goods.service;

import io.uhha.common.core.page.TableDataInfo;
import io.uhha.goods.domain.PmsGoods;
import io.uhha.goods.domain.PmsGoodsImport;

import java.io.IOException;

public interface IPmsGood3rdService {


    /**
     * 查询spu
     * @param keyword
     * @param page
     * @return
     */
    public TableDataInfo searchSpu(String channel, String keyword, Integer page ) throws IOException;


    /**
     * 根据numIid获取spu
     * @param pmsGoodsImport
     * @return
     * @throws IOException
     */
    public Integer saveSpu(PmsGoodsImport pmsGoodsImport, Long storeId) throws IOException;
}
