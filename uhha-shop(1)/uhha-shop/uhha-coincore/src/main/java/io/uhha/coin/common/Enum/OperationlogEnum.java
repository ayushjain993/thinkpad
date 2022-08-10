package io.uhha.coin.common.Enum;

public class OperationlogEnum {
	/**
	 * 暂存
	 */
	public static final int SAVE = 1;

	/**
	 * 审核
	 */
	public static final int AUDIT = 2;

	/**
	 * 冻结
	 */
	public static final int FFROZEN = 3;

	public static String getEnumString(int value) {
		String name = "";
		if (value == SAVE) {
			name = "暂存";
		} else if (value == AUDIT) {
			name = "已审核";
		} else if (value == FFROZEN) {
			name = "冻结";
		}
		return name;
	}
}
