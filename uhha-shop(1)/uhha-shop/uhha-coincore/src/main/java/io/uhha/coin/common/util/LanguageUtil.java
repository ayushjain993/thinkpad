package io.uhha.coin.common.util;

import io.uhha.coin.common.Enum.validate.LocaleEnum;

/**
 * 语言参数
 */
public class LanguageUtil {
	/**
	 * 根据传入语言字符串返回语言枚举类对象
	 * @param lan
	 * @return
	 */
	public static LocaleEnum getLan(String lan){
		LocaleEnum localeLan = LocaleEnum.ZH_CN;
		for (LocaleEnum locale : LocaleEnum.values()) {
			if (locale.getName().equalsIgnoreCase(lan)) {
				return localeLan = locale;
			}
		}
		return localeLan;
	}
}
