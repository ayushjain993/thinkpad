package io.uhha.coin.common.Enum.msg;

/**
 * @author XinSai
 *
 */
public enum MsgInfromTypeEnum {
	PAYMENT("1","购买成功通知用户付款"),
	RECEIVABLE("2", "出售成功通知用户收款");
	
	private String code;
	
	private String value;

	/**
	 * @param code
	 * @param value
	 */
	private MsgInfromTypeEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}
	
	public static String getValueByCode(String code) {
        for (MsgInfromTypeEnum e : MsgInfromTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getValue();
            }
        }
        return null;
    }

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
