package io.uhha.coin.common.Enum;

public class SystemBankInfoEnum {
	public static final int NORMAL_VALUE = 1;// 正常
	public static final int ABNORMAL = 2;// 停用

	public static String getEnumString(int value) {
		String name = "";
		if (value == NORMAL_VALUE) {
			name = "正常";
		} else if (value == ABNORMAL) {
			name = "停用";
		}
		return name;
	}

	public static Boolean getEnumBoolean(int value){
		Boolean flag = false;
		if (value == NORMAL_VALUE) {
			flag = true;
		}
		return flag;
	}
}
