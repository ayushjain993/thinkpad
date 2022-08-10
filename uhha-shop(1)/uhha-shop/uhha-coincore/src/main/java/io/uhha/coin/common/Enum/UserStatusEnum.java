package io.uhha.coin.common.Enum;

/**
 * 用户状态 1 正常 2 冻结  3 未启用 默认1
 */
public class UserStatusEnum {
	public static final int NORMAL_VALUE = 1;// 正常
	public static final int FORBBIN_VALUE = 2;// 禁用
	public static final int NOT_INUSE_VALUE = 3;// 未启用

	public static String getEnumString(Integer value) {
		String name = "";
		if(value==null){
		    value=-1;
        }
		if (value == NORMAL_VALUE) {
			name = "正常";
		} else if (value == FORBBIN_VALUE) {
			name = "禁用";
		}else if (value == NOT_INUSE_VALUE) {
			name = "未启用";
		}
		return name;
	}

}
