package io.uhha.oss;


import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.utils.StringUtils;
import io.uhha.setting.bean.OssSetting;
import io.uhha.setting.bean.UploadData;
import io.uhha.setting.service.OssService;
import io.uhha.util.CommonConstant;
import io.swagger.annotations.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;

/**
 * Oss相关操作接口
 * https://github.com/shenzhuan/mallplus on 2018/4/26.
 */
@RestController
@Api(tags = "对象管理工具Controller")
@RequestMapping("/oss")
public class OssController {
    @Resource
    private OssService ossService;

    /**
     * 上传图片、视频（系统配置的云服务器）
     *
     * @param type 上传文件的类型 默认为图片 0 图片 1 视频
     * @return 返回图片在腾讯云的地址
     * @throws Exception
     */
    @PostMapping("/uploadToOSSYun")
    @ApiOperation(value = "上传图片、视频", notes = "上传图片（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "type", required = false, defaultValue = "0", value = "上传文件的类型 默认为图片 0 图片 1 视频"),
            @ApiImplicitParam(paramType = "query", dataType = "MultipartFile", name = "multipartFile", value = "上传文件"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回图片、视频在配置的云服务器的地址", response = String.class)
    })
    public String uploadToOSSYun(@RequestParam("file") MultipartFile multipartFile,
                                 @RequestParam(value = "type", required = false, defaultValue = "0") String type) throws Exception {

        // 默认上传图片
        if (StringUtils.isEmpty(type)) {
            type = CommonConstant.UPLOAD_PIC;
        }

        if (Objects.isNull(multipartFile)) {
            return "";
        }
        return ossService.uploadToOss(Arrays.asList(UploadData.build(multipartFile.getInputStream(), multipartFile.getBytes(), multipartFile.getOriginalFilename(), type, multipartFile))).stream().findFirst().orElse("");
    }

    /**
     * app上传图片、视频（系统配置的云服务器）
     *
     * @param type 上传文件的类型 默认为图片 0 图片 1 视频
     * @return 返回图片在腾讯云的地址
     * @throws Exception
     */
    @PostMapping("/appUploadToOSSYun")
    @ApiOperation(value = "上传图片、视频", notes = "上传图片（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "type", required = false, defaultValue = "0", value = "上传文件的类型 默认为图片 0 图片 1 视频"),
            @ApiImplicitParam(paramType = "query", dataType = "MultipartFile", name = "multipartFile", value = "上传文件"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回图片、视频在配置的云服务器的地址", response = String.class)
    })
    public AjaxResult appUploadToOSSYun(@RequestParam("file") MultipartFile multipartFile,
                                        @RequestParam(value = "type", required = false, defaultValue = "0") String type) throws Exception {

        // 默认上传图片
        if (StringUtils.isEmpty(type)) {
            type = CommonConstant.UPLOAD_PIC;
        }

        if (Objects.isNull(multipartFile)) {
            return AjaxResult.error("file is empty");
        }
        String url= ossService.uploadToOss(Arrays.asList(UploadData.build(multipartFile.getInputStream(), multipartFile.getBytes(), multipartFile.getOriginalFilename(), type, multipartFile))).stream().findFirst().orElse("");
        return AjaxResult.success("success", url);
    }

    /**
     * 往阿里云上传图片、视频
     *
     * @param type 上传文件的类型 默认为图片 0 图片 1 视频
     * @return 返回图片在阿里云的地址
     * @throws Exception
     */
    @PostMapping("/uploadToAliOss")
    @ApiOperation(value = "往阿里云上传图片、视频", notes = "往阿里云上传图片（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "type", required = false, defaultValue = "0", value = "上传文件的类型 默认为图片 0 图片 1 视频"),
            @ApiImplicitParam(paramType = "query", dataType = "MultipartFile", name = "multipartFile", value = "上传文件"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回图片、视频在阿里云的地址", response = String.class)
    })
    public String uploadToAliOSSYun(@RequestParam("file") MultipartFile multipartFile,
                                    @RequestParam(value = "type", required = false, defaultValue = "0") String type) throws Exception {

        // 默认上传图片
        if (StringUtils.isEmpty(type)) {
            type = CommonConstant.UPLOAD_PIC;
        }

        if (Objects.isNull(multipartFile)) {
            return "";
        }
        return ossService.uploadToAliyunOss(Arrays.asList(UploadData.build(multipartFile.getInputStream(), multipartFile.getBytes(), multipartFile.getOriginalFilename(), type, multipartFile))).stream().findFirst().orElse("");
    }

    /**
     * 往七牛云上传图片、视频
     *
     * @param type 上传文件的类型 默认为图片 0 图片 1 视频
     * @return 返回图片在阿里云的地址
     * @throws Exception
     */
    @PostMapping("/uploadToQiniuOss")
    @ApiOperation(value = "往七牛云上传图片、视频", notes = "往七牛云上传图片（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "type", required = false, defaultValue = "0", value = "上传文件的类型 默认为图片 0 图片 1 视频"),
            @ApiImplicitParam(paramType = "query", dataType = "MultipartFile", name = "multipartFile", value = "上传文件"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回图片、视频在阿里云的地址", response = String.class)
    })
    public String uploadToQiniuOSS(@RequestParam("file") MultipartFile multipartFile,
                                   @RequestParam(value = "type", required = false, defaultValue = "0") String type) throws Exception {

        // 默认上传图片
        if (StringUtils.isEmpty(type)) {
            type = CommonConstant.UPLOAD_PIC;
        }

        if (Objects.isNull(multipartFile)) {
            return "";
        }
        return ossService.uploadToQiniuOss(Arrays.asList(UploadData.build(multipartFile.getInputStream(), multipartFile.getBytes(), multipartFile.getOriginalFilename(), type, multipartFile))).stream().findFirst().orElse("");
    }

}
