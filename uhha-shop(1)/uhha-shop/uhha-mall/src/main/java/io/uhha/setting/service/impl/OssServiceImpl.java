package io.uhha.setting.service.impl;


import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.common.utils.YunUploadUtils;
import io.uhha.common.utils.bean.AliOssSet;
import io.uhha.common.utils.bean.QiniuOssSet;
import io.uhha.setting.bean.OssSetCommon;
import io.uhha.setting.bean.UploadData;
import io.uhha.setting.mapper.LsOssSettingMapper;
import io.uhha.setting.service.ILsOssSettingService;
import io.uhha.setting.service.OssService;
import io.uhha.oss.domain.OssUploadRecord;
import io.uhha.oss.service.IOssUploadRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by luozhuo on 2020/7/7.
 * 云存储服务实现类
 */
@Service
public class OssServiceImpl implements OssService {

    private Logger logger = LoggerFactory.getLogger(OssServiceImpl.class);

    @Autowired
    private ILsOssSettingService ossSettingService;

    @Autowired
    private LsOssSettingMapper ossSettingMapper;

    @Autowired
    private IOssUploadRecordService uploadRecordService;

//    @Override
//    public List<String> uploadToQqOss(List<UploadData> uploadDatas) {
//        logger.debug("Begin to uploadToQqOss....");
//        if (CollectionUtils.isEmpty(uploadDatas)) {
//            logger.error("uploadDatas fail  due to params is empty...");
//            return Arrays.asList("");
//        }
//        //TODO bug for qq
//        return uploadDatas.stream().filter(Objects::nonNull).map(uploadData -> YunUploadUtils.getInstance().uploadToQqOss(queryOssSetting().getUpYunConf(), uploadData.getMultipartFile(), uploadData.getDatas(), uploadData.getFileOriginName(), uploadData.getType())).collect(Collectors.toList());
//        return null;
//
//    }
//
//    @Override
//    public List<String> uploadToQqOssForBase64(List<UploadData> uploadDatas) {
//        if (CollectionUtils.isEmpty(uploadDatas)) {
//            logger.error("uploadToQqOssForBase64 fail  due to params is empty...");
//            return Arrays.asList("");
//        }
//        return uploadDatas.stream().filter(Objects::nonNull).map(uploadData -> YunUploadUtils.getInstance().
//                uploadToQqForBase64(queryOssSetting().getUpYunConf(), uploadData.getInputStream(), uploadData.getDatas(), uploadData.getFileOriginName())).collect(Collectors.toList());
//
//    }
//
    @Override
    public List<String> uploadToOss(List<UploadData> uploadDatas) {
        logger.debug("Begin to uploadToOss....");
        if (CollectionUtils.isEmpty(uploadDatas)) {
            logger.error("uploadDatas fail  due to params is empty...");
            return Arrays.asList("");
        }

        List<String> urls;

        OssSetCommon ossSetCommon = queryOssSet();

        if( "1".equalsIgnoreCase(ossSetCommon.getAliOssSet().getIsUse())){
            urls= uploadToAliyunOss(uploadDatas);
        }else if("1".equalsIgnoreCase(ossSetCommon.getQiniuOssSet().getIsUse())){
            urls = uploadToQiniuOss(uploadDatas);
        }else{
            logger.error("No OSS activated");
            return null;
        }

        return urls;

    }

    @Override
    public List<String> uploadToOss(List<UploadData> uploadDatas, String refer, String userName) {
        logger.debug("Begin to uploadToOss....");
        if (CollectionUtils.isEmpty(uploadDatas)) {
            logger.error("uploadDatas fail  due to params is empty...");
            return Arrays.asList("");
        }

        List<String> urls;
        String vendor = "";

        OssSetCommon ossSetCommon = queryOssSet();

        if( "1".equalsIgnoreCase(ossSetCommon.getAliOssSet().getIsUse())){
            urls= uploadToAliyunOss(uploadDatas);
            vendor = "Ali";
        }else if("1".equalsIgnoreCase(ossSetCommon.getQiniuOssSet().getIsUse())){
            urls = uploadToQiniuOss(uploadDatas);
            vendor = "Qiniu";
        }else{
            logger.error("No OSS activated");
            return null;
        }

        //记录本次上传记录
        String finalVendor = vendor;
        for (int i =0;i<uploadDatas.size();i++) {
            UploadData uploadData = uploadDatas.get(i);
            String url = urls.get(i);
            //提取id
            String[] items = url.split("/");
            String objectid = items[items.length-1];

            OssUploadRecord ossUploadRecord = OssUploadRecord.builder()
                    .referer(refer)
                    .type(uploadData.getType())
                    .extUrl(url)
                    .objectid(objectid)
                    .createBy(userName)
                    .createTime(DateUtils.getNowDate())
                    .vendor(finalVendor)
                    .build();
            uploadRecordService.insertOssUploadRecord(ossUploadRecord);
        }

        return urls;
    }

    @Override
    public List<String> uploadToOssForBase64(List<UploadData> uploadDatas) {
        logger.debug("Begin to uploadToOss....");
        if (CollectionUtils.isEmpty(uploadDatas)) {
            logger.error("uploadDatas fail  due to params is empty...");
            return Arrays.asList("");
        }

        List<String> urls;

        OssSetCommon ossSetCommon = queryOssSet();

        if( "1".equalsIgnoreCase(ossSetCommon.getAliOssSet().getIsUse())){
            urls= uploadToAliyunOssBase64(uploadDatas);
        }else if("1".equalsIgnoreCase(ossSetCommon.getQiniuOssSet().getIsUse())){
            urls = uploadToQiniuOssBase64(uploadDatas);
        }else{
            logger.error("No OSS activated");
            return null;
        }

        return urls;

    }

    @Override
    public List<String> uploadToAliyunOss(List<UploadData> uploadDatas) {
        logger.debug("Begin to uploadToAliyunOss....");
        if (CollectionUtils.isEmpty(uploadDatas)) {
            logger.error("uploadDatas fail  due to params is empty...");
            return Arrays.asList("");
        }
        return uploadDatas.stream().filter(Objects::nonNull).map(uploadData -> {
            try {
                return YunUploadUtils.getInstance().uploadToAliOssYun(queryAliOssSetting(),
                        uploadData.getMultipartFile().getInputStream(), uploadData.getDatas(), uploadData.getFileOriginName(), uploadData.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> uploadToAliyunOssBase64(List<UploadData> uploadDatas) {
        return null;
    }

    @Override
    public List<String> uploadToQiniuOss(List<UploadData> uploadDatas) {
        logger.debug("Begin to uploadToAliyunOss....");
        if (CollectionUtils.isEmpty(uploadDatas)) {
            logger.error("uploadDatas fail  due to params is empty...");
            return Arrays.asList("");
        }
        return uploadDatas.stream().filter(Objects::nonNull).map(uploadData -> {
            try {
                return YunUploadUtils.getInstance().uploadToQiniuOssYun(queryQiniuOssSetting(),
                        uploadData.getMultipartFile().getInputStream(), uploadData.getDatas(), uploadData.getFileOriginName(), uploadData.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> uploadToQiniuOssBase64(List<UploadData> uploadDatas) {
        return null;
    }

    @Override
    public String deleteFromAliOss(String key) {
        logger.debug("Begin to deleteFromAliOss....");
        if (StringUtils.isEmpty(key)) {
            logger.error("aliOssDelete fail  due to params is empty...");
            return null;
        }
        List<String> keys = new ArrayList<>();
        keys.add(key);
        List<String> deleted = YunUploadUtils.getInstance().deleteFromAliOss(queryAliOssSetting(), keys);
        if(CollectionUtils.isEmpty(deleted)){
            return null;
        }
        logger.debug("Done with deleteFromAliOss....");
        return deleted.get(0);

    }

    @Override
    public List<String> deleteFromAliOss(List<String> keys) {
        logger.debug("Begin to deleteFromAliOss....");
        if (CollectionUtils.isEmpty(keys)) {
            logger.error("aliOssDelete fail  due to params is empty...");
            return null;
        }
        return YunUploadUtils.getInstance().deleteFromAliOss(queryAliOssSetting(), keys);
    }

    /**
     * 查询Oss接口设置
     *
     * @return 返回OssSetCommon
     */
    @Override
    public OssSetCommon queryOssSet() {
        logger.debug("queryOssSet...");
        return OssSetCommon.getOssSetCommon(new OssSetCommon(), ossSettingMapper.queryOssSet());
    }

    @Override
    public AliOssSet queryAliOssSetting() {
        logger.debug("Begin to queryAliOssSetting....");
        OssSetCommon ossSetCommon = ossSettingService.queryOssSet();
        return ossSetCommon.getAliOssSet();
    }

    @Override
    public QiniuOssSet queryQiniuOssSetting() {
        logger.debug("Begin to queryQiniuOssSetting....");
        OssSetCommon ossSetCommon = ossSettingService.queryOssSet();
        return ossSetCommon.getQiniuOssSet();
    }
}
