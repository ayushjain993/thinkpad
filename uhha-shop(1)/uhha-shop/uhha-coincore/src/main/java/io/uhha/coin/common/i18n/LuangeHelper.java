package io.uhha.coin.common.i18n;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 语言 
 * @author LY
 */
public class LuangeHelper{

	/**
	 * 根据当前语言获取错误消息
	 * 
	 * @param key
	 * @return msg
	 */
	public static String GetR18nMsg(HttpServletRequest request,String key) {
		RequestContext requestContext = new RequestContext(request);
		return requestContext.getMessage(key);
	}

	/**
	 * 根据当前语言获取错误消息
	 * 
	 * @param key
	 * @param args
	 * @return msg
	 */
	public static String GetR18nMsg(HttpServletRequest request,String key, Object... args) {
		RequestContext requestContext = new RequestContext(request);
		String msg = "";
		try {
			msg = String.format(requestContext.getMessage(key), args);
		} catch (Exception e) {

		}
		return msg;
	}
	/**
	 * 获取语言包简称
	 * 
	 * @return en,cn,tw
	 */
	public static String getLanguageShotName(HttpServletRequest request) {
		return RequestContextUtils.getLocaleResolver(request).resolveLocale(request).toString();
	}

	/**
	 * app服务端组装返回json
	 * @param code 返回值的编码
	 * @param key 错误消息key
	 * @return JSON格式的 字符串
	 */
	public static String GetAppR18nMsg(HttpServletRequest request,int code, String key) {
		JSONObject jsonObject = new JSONObject();
		String msg = "";
		if (!key.isEmpty()) {
			RequestContext requestContext = new RequestContext(request);
			try {
				msg = requestContext.getMessage(key);
			} catch (Exception e) {

			}
		}
		jsonObject.accumulate("resultCode", code);
		jsonObject.accumulate("resultMsg", msg);
		return jsonObject.toString();
	}
	
	/**
	 * app服务端组装返回json
	 * @param code 返回值的编码
	 * @param msg JSONObject格式数据
	 * @return JSON格式的 字符串
	 */
	public static String GetAppR18nMsg(int code, JSONObject msg) {
		JSONObject jsonObject = new JSONObject();		
		jsonObject.accumulate("resultCode", code);
		jsonObject.accumulate("resultMsg", msg.toString());
		return jsonObject.toString();
	}
	/**
	 * app服务端组装返回json
	 * @param code 返回值的编码
	 * @param msg JSONObject格式数据
	 * @return JSON格式的 字符串
	 */
	public static String GetAppR18nMsg(int code, JSONArray msg) {
		JSONObject jsonObject = new JSONObject();		
		jsonObject.accumulate("resultCode", code);
		jsonObject.accumulate("resultMsg", msg.toString());
		return jsonObject.toString();
	}
	
	/**
	 * app服务端组装返回json
	 * @param code 返回值的编码
	 * @param msg  String格式数据
	 * @return JSON格式的 字符串
	 */
	public static String GetAppReturnMsg(int code, String msg) {
		JSONObject jsonObject = new JSONObject();		
		jsonObject.accumulate("resultCode", code);
		jsonObject.accumulate("resultMsg", msg.toString());
		return jsonObject.toString();
	}
	
	/**
	 * app服务端组装返回json
	 * @param key 错误消息key
	 * @return JSON格式的 字符串
	 */
	public static String GetAppR18nMsg(HttpServletRequest request,String key) {
		String msg = "";
		if (!key.isEmpty()) {
			RequestContext requestContext = new RequestContext(request);
			try {
				msg = requestContext.getMessage(key);
			} catch (Exception e) {

			}
		}
		return msg;
	}

	/**
	 * 根据当前语言获取错误消息
	 *
	 * @param key
	 * @param args
	 * @return msg
	 */
	public static String GetAppR18nMsg(HttpServletRequest request,String key, Object... args) {
		RequestContext requestContext = new RequestContext(request);
		String msg = "";
		try {
			msg = String.format(requestContext.getMessage(key), args);
		} catch (Exception e) {

		}
		return msg;
	}

	/**
	 * 获取语言包简称
	 *
	 * @return en,cn,tw
	 */
	public static String getLan(HttpServletRequest request) {
		return RequestContextUtils.getLocaleResolver(request).resolveLocale(request).toString();
	}

	/**
	 * 设置语言
	 * @param request
	 * @param response
	 * @param lan 语言(cn,tw,en)
	 */
	public static boolean setLan(HttpServletRequest request, HttpServletResponse response, String lan) {
		try {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			localeResolver.setLocale(request, response, StringUtils.parseLocaleString(lan));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
