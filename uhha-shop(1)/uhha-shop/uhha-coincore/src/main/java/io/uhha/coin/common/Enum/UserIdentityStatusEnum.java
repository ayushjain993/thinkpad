package io.uhha.coin.common.Enum;

public class UserIdentityStatusEnum {
	public static final int FRO_VALUE = 0;// 未审核
	public static final int FRONT_VALUE = 1;// 审核中
	public static final int EXIMIN_VALUE = 2;// 通过
	public static final int FORBBIN_VALUE = 3;// 不通过
	public static String getEnumString(Integer value) {
		String name = "";
		if(value==null){
		    value=-0;
        }
		if (value == FRONT_VALUE) {
			name = "待审核";
		} else if (value == EXIMIN_VALUE) {
			name = "已认证";
		}else if (value == FORBBIN_VALUE) {
			name = "认证失败";
		}else if (value == FRO_VALUE) {
			name = "未认证";
		}
		return name;
	}

}
