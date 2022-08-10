package io.uhha.coin.common.Enum;

/**
 * 用户身份类型
 * @author ZGY
 *
 */
public enum UserIndentityTypeEnum {
	NORMAL_USER(0, "普通用户"), COMPANY_EMPLOYEES(1, "公司员工");

	private Integer code;
	private Object value;

	private UserIndentityTypeEnum(Integer code, Object value) {
		this.code = code;
		this.value = value;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public static String getValueByCode(Integer code) {
		for (UserIndentityTypeEnum financesState : UserIndentityTypeEnum.values()) {
			if (financesState.getCode().equals(code)) {
				return financesState.getValue().toString();
			}
		}
		return null;
	}
}
