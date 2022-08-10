package io.uhha.setting.service;


import io.uhha.common.utils.bean.AliOssSet;
import io.uhha.common.utils.bean.QiniuOssSet;
import io.uhha.setting.bean.OssSetCommon;
import io.uhha.setting.bean.UploadData;

import java.util.List;

/**
 * Created by luozhuo on 2020/7/7.
 * 云存储服务接口
 */
public interface OssService {

//    /**
//     * 上传到云存储
//     *
//     * @param uploadDatas 上传的参数
//     * @return 返回图片在云存储的地址
//     */
//    List<String> uploadToQqOss(List<UploadData> uploadDatas);
//
//    /**
//     * 上传到腾讯云存储（上传base64图片）
//     *
//     * @param uploadDatas 上传的参数
//     * @return 返回图片在云存储的地址
//     */
//    List<String> uploadToQqOssForBase64(List<UploadData> uploadDatas);
//
    /**
     * 上传到云存储（通用）
     *
     * @param uploadDatas 上传的参数
     * @return 返回图片在云存储的地址
     */
    List<String> uploadToOss(List<UploadData> uploadDatas);

    /**
     * 上传到云存储（通用）
     *
     * @param uploadDatas 上传的参数
     * @param refer 源链接
     * @param userName 上传用户
     * @return 返回图片在云存储的地址
     */
    List<String> uploadToOss(List<UploadData> uploadDatas, String refer, String userName);

    /**
     * 上传到云存储（上传base64图片）
     *
     * @param uploadDatas 上传的参数
     * @return 返回图片在云存储的地址
     */
    List<String> uploadToOssForBase64(List<UploadData> uploadDatas);

    /**
     * 上传到阿里云云存储
     *
     * @param uploadDatas 上传的参数
     * @return 返回图片在云存储的地址
     */
    List<String> uploadToAliyunOss(List<UploadData> uploadDatas);

    /**
     * 上传到阿里云存储（上传base64图片）
     *
     * @param uploadDatas 上传的参数
     * @return 返回图片在云存储的地址
     */
    List<String> uploadToAliyunOssBase64(List<UploadData> uploadDatas);

    /**
     * 上传到七牛云存储
     *
     * @param uploadDatas 上传的参数
     * @return 返回图片在云存储的地址
     */
    List<String> uploadToQiniuOss(List<UploadData> uploadDatas);

    /**
     * 上传到七牛云存储（上传base64图片）
     *
     * @param uploadDatas 上传的参数
     * @return 返回图片在云存储的地址
     */
    List<String> uploadToQiniuOssBase64(List<UploadData> uploadDatas);

    /**
     * 从阿里云存储上删除
     * @param key
     */
    String deleteFromAliOss(String key);

    /**
     * 从阿里云存储上删除
     * @param keys
     */
    List<String> deleteFromAliOss(List<String> keys);

    /**
     * 查询Oss接口设置
     *
     * @return 返回OssSetCommon
     */
    OssSetCommon queryOssSet() ;


    /**
     * 查询系统设置的阿里云Oss云存储信息
     *
     * @return 返回云存储信息
     */
    public AliOssSet queryAliOssSetting();

    /**
     * 查询系统设置的阿里云Oss云存储信息
     *
     * @return 返回云存储信息
     */
    public QiniuOssSet queryQiniuOssSetting();

}
