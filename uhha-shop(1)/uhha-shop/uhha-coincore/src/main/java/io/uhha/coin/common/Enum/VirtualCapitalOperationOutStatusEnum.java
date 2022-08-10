package io.uhha.coin.common.Enum;

/**
 * 提币状态
 */
public class VirtualCapitalOperationOutStatusEnum {
	/**
	 * 等待提现
	 */
	public static final int WaitForOperation = 1;

	/**
	 * 锁定，正在处理
	 */
	public static final int OperationLock = 2;

	/**
	 * 提现成功
	 */
	public static final int OperationSuccess = 3;

	/**
	 * 用户取消
	 */
	public static final int Cancel = 4;

	/**
	 * 锁定订单
	 */
	public static final int LockOrder = 5;

	public static String getEnumString(int value) {
		String name = "";
		if (value == WaitForOperation) {
			name = "等待提现";
		} else if (value == OperationLock) {
			name = "正在处理";
		} else if (value == OperationSuccess) {
			name = "提现成功";
		} else if (value == Cancel) {
			name = "用户撤销";
		} else if (value == LockOrder) {
			name = "正在提币";
		}
		return name;
	}
}

