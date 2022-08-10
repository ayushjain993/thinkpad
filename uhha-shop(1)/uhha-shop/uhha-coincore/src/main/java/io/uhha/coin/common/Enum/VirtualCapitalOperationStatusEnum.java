package io.uhha.coin.common.Enum;

/**
 * 虚拟币管理操作类型，1 锁定 2 取消锁定 3 取消提现 4 恢复提现
 * 
 * @author LY
 *
 */
public enum VirtualCapitalOperationStatusEnum {

	LOCK(1, "锁定"),
	UNLOCK(2, "取消锁定"),
	CANCEL_WITHDRAW(3, "取消提现"),
	RESUME_WITHDRAW(4, "恢复提现"),
	;

	private Integer code;
	private String value;

	private VirtualCapitalOperationStatusEnum(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String getValueByCode(Integer code) {
		for (VirtualCapitalOperationStatusEnum operationType : VirtualCapitalOperationStatusEnum.values()) {
			if (operationType.getCode().equals(code)) {
				return operationType.getValue();
			}
		}
		return null;
	}
}
