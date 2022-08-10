package io.uhha.coin.common.dto.system;

import java.io.Serializable;
import java.util.ArrayList;
//公司信息类
public class CompanyInfo implements Serializable {


    private static final long serialVersionUID = 8343171680113678631L;

    private String headerLogo;  //网站主logo图片地址
    private String webName;                      //网站名
    private String faviconLogo;                  //网站favicon图片地址
    private String footerLogo;                   //网站底部logo图片地址
    private String walletAddress;                //钱包下载地址
    private String feeInfo;                      //费率信息
    private String selfCoinName;                 //公司自己的币种名称
    private String companyProfile;               //公司简介跳转链接
    private String api;                          //公司api跳转链接
    private String companyAddress;               //公司地址
    private String appErcode;                    //app下载二维码图片

    private String[] qqGroupNumList;             //公司qq群号码数组
    private String[] qqForCumstorService;        //公司客服qq
    private String[] qqForRecharge;              //公司充值qq
    private String[] telegraphGroup;             //公司电报群
    private String[] telNum;                     //公司电话
    private String[] fax;                        //公司传真
    private String[] enterpriseMailbox;          //企业邮箱

    private String workday;                      //工作时间
    private String holidays;                     //工作日时间
    private String copyright;                    //版权信息
    private String twitter;                      //推特
    private String facebook;                     //脸书
    private String qq;                           //QQ
    private String wechat;                       //微信
    private String telegram;                     //电报群
    private ArrayList<Links> links;              //友情链接

    private String linkStr;
    private String qqGroupNumListStr;             //公司qq群号码字符串
    private String qqForCumstorServiceStr;        //公司客服qq
    private String qqForRechargeStr;              //公司充值qq
    private String telegraphGroupStr;             //公司电报群
    private String telNumStr;                     //公司电话
    private String faxStr;                        //公司传真
    private String enterpriseMailboxStr;          //企业邮箱
    private String businessHours;                 //工作时间


    public String getQqGroupNumListStr() {
        return qqGroupNumListStr;
    }

    public void setQqGroupNumListStr(String qqGroupNumListStr) {
        this.qqGroupNumListStr = qqGroupNumListStr;
    }

    public String getQqForCumstorServiceStr() {
        return qqForCumstorServiceStr;
    }

    public void setQqForCumstorServiceStr(String qqForCumstorServiceStr) {
        this.qqForCumstorServiceStr = qqForCumstorServiceStr;
    }

    public String getQqForRechargeStr() {
        return qqForRechargeStr;
    }

    public void setQqForRechargeStr(String qqForRechargeStr) {
        this.qqForRechargeStr = qqForRechargeStr;
    }

    public String getTelegraphGroupStr() {
        return telegraphGroupStr;
    }

    public void setTelegraphGroupStr(String telegraphGroupStr) {
        this.telegraphGroupStr = telegraphGroupStr;
    }

    public String getTelNumStr() {
        return telNumStr;
    }

    public void setTelNumStr(String telNumStr) {
        this.telNumStr = telNumStr;
    }

    public String getFaxStr() {
        return faxStr;
    }

    public void setFaxStr(String faxStr) {
        this.faxStr = faxStr;
    }

    public String getEnterpriseMailboxStr() {
        return enterpriseMailboxStr;
    }

    public void setEnterpriseMailboxStr(String enterpriseMailboxStr) {
        this.enterpriseMailboxStr = enterpriseMailboxStr;
    }

    public String getHeaderLogo() {
        return headerLogo;
    }

    public void setHeaderLogo(String headerLogo) {
        this.headerLogo = headerLogo;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getFaviconLogo() {
        return faviconLogo;
    }

    public void setFaviconLogo(String faviconLogo) {
        this.faviconLogo = faviconLogo;
    }

    public String getFooterLogo() {
        return footerLogo;
    }

    public void setFooterLogo(String footerLogo) {
        this.footerLogo = footerLogo;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getFeeInfo() {
        return feeInfo;
    }

    public void setFeeInfo(String feeInfo) {
        this.feeInfo = feeInfo;
    }

    public String getSelfCoinName() {
        return selfCoinName;
    }

    public void setSelfCoinName(String selfCoinName) {
        this.selfCoinName = selfCoinName;
    }

    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getAppErcode() {
        return appErcode;
    }

    public void setAppErcode(String appErcode) {
        this.appErcode = appErcode;
    }

    public String[] getQqGroupNumList() {
        return qqGroupNumList;
    }

    public void setQqGroupNumList(String[] qqGroupNumList) {
        this.qqGroupNumList = qqGroupNumList;
    }

    public String[] getQqForCumstorService() {
        return qqForCumstorService;
    }

    public void setQqForCumstorService(String[] qqForCumstorService) {
        this.qqForCumstorService = qqForCumstorService;
    }

    public String[] getQqForRecharge() {
        return qqForRecharge;
    }

    public void setQqForRecharge(String[] qqForRecharge) {
        this.qqForRecharge = qqForRecharge;
    }

    public String[] getTelegraphGroup() {
        return telegraphGroup;
    }

    public void setTelegraphGroup(String[] telegraphGroup) {
        this.telegraphGroup = telegraphGroup;
    }

    public String[] getTelNum() {
        return telNum;
    }

    public void setTelNum(String[] telNum) {
        this.telNum = telNum;
    }

    public String[] getFax() {
        return fax;
    }

    public void setFax(String[] fax) {
        this.fax = fax;
    }

    public String[] getEnterpriseMailbox() {
        return enterpriseMailbox;
    }

    public void setEnterpriseMailbox(String[] enterpriseMailbox) {
        this.enterpriseMailbox = enterpriseMailbox;
    }

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public ArrayList<Links> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Links> links) {
        this.links = links;
    }

    public String getLinkStr() {
        return linkStr;
    }

    public void setLinkStr(String linkStr) {
        this.linkStr = linkStr;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }
}
