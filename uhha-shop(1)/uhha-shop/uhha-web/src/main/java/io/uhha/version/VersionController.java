package io.uhha.version;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.util.ArgsConstant;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * app版本校验
 * @author ZKF
 */
@Slf4j
@Api(tags = "app版本校验控制器")
@RestController
public class VersionController extends BaseController {

	
	@Autowired
	private RedisCryptoHelper redisHelper;
	
	@ApiOperation(value = "IOS版本效验" , notes = "version_ios", response = AjaxResult.class)
	@RequestMapping(value = "/version/ios", method = RequestMethod.POST)
	@ResponseBody
	@UnAuth
	public AjaxResult version_ios() throws Exception {
		String iosVersion = redisHelper.getSystemArgs(ArgsConstant.IosVersion);
		String iosUpdate = redisHelper.getSystemArgs(ArgsConstant.IosIsUpdate);
		String iosDownloadUrl = redisHelper.getSystemArgs(ArgsConstant.IosDownloadUrl);
		String iosUpdateContent = redisHelper.getSystemArgs(ArgsConstant.IosUpdateContent);
		if (iosVersion == null) {
			return AjaxResult.error(MessageUtils.message("app.com.err.2"));
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("version", iosVersion);
		jsonObject.put("isupdate", iosUpdate);
		jsonObject.put("downloadurl", iosDownloadUrl);
		//增加iOS更新公告内容
		jsonObject.put("iosUpdateContent",iosUpdateContent);
		return AjaxResult.success(jsonObject);
	}

	
	// 效验版本
	@ApiOperation(value = "Android版本效验" , notes = "version_android", response = AjaxResult.class)
	@RequestMapping(value = "/version/android", method = RequestMethod.POST)
	@ResponseBody
	@UnAuth
	public AjaxResult version_android(HttpServletRequest request) throws Exception {
		String version  = redisHelper.getSystemArgs(ArgsConstant.AndroidVersion);
		String update = redisHelper.getSystemArgs(ArgsConstant.AndroidIsUpdate);
		String downloadUrl = redisHelper.getSystemArgs(ArgsConstant.AndroidDownloadUrl);
		String updateUrl = redisHelper.getSystemArgs(ArgsConstant.AndroidUpdateUrl);
		String androidUpdateContent = redisHelper.getSystemArgs(ArgsConstant.AndroidUpdateContent);
		if (version == null) {
			return AjaxResult.error(MessageUtils.message("app.com.err.2"));
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("version", version);
		jsonObject.put("isupdate", update);
		jsonObject.put("downloadurl", downloadUrl);
		jsonObject.put("updateUrl", updateUrl);
		//增加Android更新公告内容
		jsonObject.put("androidUpdateContent",androidUpdateContent);

		return AjaxResult.success(jsonObject);
	}	

}
