package io.uhha.setting.bean;

import io.uhha.common.utils.bean.AliOssSet;
import io.uhha.common.utils.bean.QiniuOssSet;

import java.util.List;

public class OssSetCommon {
    private AliOssSet aliOssSet = new AliOssSet();
    private QiniuOssSet qiniuOssSet = new QiniuOssSet();
//    private AwsOssSet awsOssSet = new AwsOssSet();

    /**
     * 用于组装Oss设置对象在前端显示
     *
     * @param ossSetCommon oss设置对象
     * @param ossSettings      数据库映射对象
     * @return oss设置对象
     */
    public static OssSetCommon getOssSetCommon(OssSetCommon ossSetCommon, List<OssSetting> ossSettings) {
        ossSettings.forEach(x -> {
            String value = x.getColumnValue();
            //阿里云Oss
            if ("1".equals(x.getCodeType())) {
                if ("accessKey".equals(x.getColumnName())) {
                    ossSetCommon.aliOssSet.setAccessKey(value);
                }
                if ("secretKey".equals(x.getColumnName())) {
                    ossSetCommon.aliOssSet.setSecretKey(value);
                }
                if ("bucket".equals(x.getColumnName())) {
                    ossSetCommon.aliOssSet.setBucket(value);
                }
                if ("endPoint".equals(x.getColumnName())) {
                    ossSetCommon.aliOssSet.setEndPoint(value);
                }
                if ("isUse".equals(x.getColumnName())) {
                    ossSetCommon.aliOssSet.setIsUse(value);
                }
            }else if("2".equals(x.getCodeType())){
                if ("accessKey".equals(x.getColumnName())) {
                    ossSetCommon.qiniuOssSet.setAccessKey(value);
                }
                if ("secretKey".equals(x.getColumnName())) {
                    ossSetCommon.qiniuOssSet.setSecretKey(value);
                }
                if ("bucket".equals(x.getColumnName())) {
                    ossSetCommon.qiniuOssSet.setBucket(value);
                }
                if ("host".equals(x.getColumnName())) {
                    ossSetCommon.qiniuOssSet.setHost(value);
                }
                if ("isUse".equals(x.getColumnName())) {
                    ossSetCommon.qiniuOssSet.setIsUse(value);
                }
            }else if("3".equals(x.getCodeType())){
                //TODO
            }
        });
        return ossSetCommon;
    }

    public AliOssSet getAliOssSet() {
        return aliOssSet;
    }

    public void setAliOssSet(AliOssSet aliOssSet) {
        this.aliOssSet = aliOssSet;
    }

    public QiniuOssSet getQiniuOssSet() {
        return qiniuOssSet;
    }

    public void setQiniuOssSet(QiniuOssSet qiniuOssSet) {
        this.qiniuOssSet = qiniuOssSet;
    }

//    public AwsOssSet getAwsOssSet() {
//        return awsOssSet;
//    }
//
//    public void setAwsOssSet(AwsOssSet awsOssSet) {
//        this.awsOssSet = awsOssSet;
//    }
}
