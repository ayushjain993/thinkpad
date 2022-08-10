package io.uhha.goods.service.impl;

import io.uhha.common.core.page.TableDataInfo;
import io.uhha.goods.domain.PmsGoods;
import io.uhha.goods.domain.PmsGoodsImport;
import io.uhha.goods.service.IPmsGood3rdService;
import io.uhha.goods.service.IPmsGoodsImportService;
import io.uhha.util.OneBoundSpuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class PmsGood3rdServiceImpl implements IPmsGood3rdService {

    @Autowired
    private OneBoundSpuUtils oneBoundSpuUtils;


    @Override
    public TableDataInfo searchSpu(String channel, String keyword, Integer page) throws IOException {
        return oneBoundSpuUtils.itemSearch(channel,keyword, page, "us");
    }

    @Override
    @Transactional
    public Integer saveSpu(PmsGoodsImport pmsGoodsImport, Long storeId) throws IOException {
        //保存SPU
        return oneBoundSpuUtils.saveSpu(pmsGoodsImport,storeId, "us");
    }
}
