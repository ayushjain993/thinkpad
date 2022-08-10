package io.uhha.util.enums;

/**
 * 万邦义乌购api
 */
public enum YiwugoErrorCodeEnum {
    SUCCESS("0000", "success", "接口调用成功并返回相关数据", "是"),
    NO_RESULT("2000", "Search success but no result", "接口访问成功，但是搜索没有结果", "是"),
    INTERNAL_ERROR("4000", "Server internal error", "服务器内部错误", "否"),
    NETWORK_ERROR("4001", "Network error", "网络错误", "否"),
    TARGET_SERVER_ERROR("4002", "Target server error", "目标服务器错误", "否"),
    PARAMS_ERROR("4003", "Param error", "用户输入参数错误", "否"),
    ACCOUNT_NOT_FOUND("4004", "Account not found", "用户帐号不存在", "否"),
    INVALID_AUTHENTICATION("4005", "Invalid authentication credentials", "授权失败", "否"),
    API_DISABLED("4006", "API stopped", "您的当前API已停用", "否"),
    ACCOUNT_SUSPEND("4007", "Account stopped", "您的账户已停用", "否"),
    REACH_LIMIT("4008", "API rate limit exceeded", "并发已达上限", "否"),
    API_UNDER_MAINTENANCE("4009", "API maintenance", "API维护中", "否"),
    API_NOT_EXIST("4010", "API not found with these values", "API不存在", "否"),
    ADD_API_FIRST("4012", "Please add api first", "请先添加api", "否"),
    NUM_CALL_EXCEEDED("4013", "Number of calls exceeded", "调用次数超限", "否"),
    MISSING_URL_PARAM("4014", "Missing url param", "参数缺失", "否"),
    WRONG_PAGE_TOKEN("4015", "Wrong pageToken", "参数pageToken有误", "否"),
    INSUFFICIENT_BALANCE("4016", "Insufficient balance", "余额不足", "否"),
    TIMEOUT_ERROR("4017", "timeout error", "请求超时", "否"),
    UNKNOWN_ERROR("5000", "unknown error", "未知错误", "否");

    private String errorCode;
    private String statusInfo;
    private String description;
    private String billable;

    YiwugoErrorCodeEnum(String errorCode, String statusInfo, String description, String billable) {
        this.errorCode = errorCode;
        this.statusInfo = statusInfo;
        this.description = description;
        this.billable = billable;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBillable() {
        return billable;
    }

    public void setBillable(String billable) {
        this.billable = billable;
    }
}
