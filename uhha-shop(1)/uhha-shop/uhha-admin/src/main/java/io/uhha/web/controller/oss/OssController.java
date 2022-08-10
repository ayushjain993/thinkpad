package io.uhha.web.controller.oss;


import io.uhha.common.utils.StringUtils;
import io.uhha.setting.bean.OssSetting;
import io.uhha.setting.bean.UploadData;
import io.uhha.setting.service.OssService;
import io.uhha.util.CommonConstant;
import io.swagger.annotations.*;
import io.uhha.web.utils.AdminLoginUtils;
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
@Api(tags = "Oss管理Controller")
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
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "name", value = "上传文件的name 默认为image"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "type", value = "上传文件的类型 默认为图片 0 图片 1 视频"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回图片、视频在配置的云服务器的地址", response = String.class)
    })
    public String uploadToOSSYun(MultipartHttpServletRequest request, @RequestParam("name") String name, @RequestParam("type") String type) throws Exception {

        //获取referer头的值
        String referer = request.getHeader("referer");

        //获取当前用户名
        String userName = AdminLoginUtils.getInstance().getManagerName();

        if (StringUtils.isEmpty(name)) {
            name = "image";
        }

        // 默认上传图片
        if (StringUtils.isEmpty(type)) {
            type = CommonConstant.UPLOAD_PIC;
        }

        MultipartFile multipartFile = request.getFile(name);
        if (Objects.isNull(multipartFile)) {
            return "";
        }
        return ossService.uploadToOss(Arrays.asList(UploadData.build(multipartFile.getInputStream(), multipartFile.getBytes(), multipartFile.getOriginalFilename(), type, multipartFile)),referer, userName).stream().findFirst().orElse("");
    }

//    /**
//     * 上传图片、视频到腾讯云
//     *
//     * @param name 上传文件的name 默认为image
//     * @param type 上传文件的类型 默认为图片 0 图片 1 视频
//     * @return 返回图片在腾讯云的地址
//     * @throws Exception
//     */
//    @PostMapping("uploadToQqOSSYun")
//    @ApiOperation(value = "上传图片", notes = "上传图片（不需要认证）")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "form", dataType = "String", name = "name", value = "上传文件的name 默认为image"),
//            @ApiImplicitParam(paramType = "form", dataType = "String", name = "type", value = "上传文件的类型 默认为图片 0 图片 1 视频"),
//    })
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "返回图片在腾讯云的地址", response = String.class)
//    })
//    public String uploadToQqOSSYun(MultipartHttpServletRequest request, String name, String type) throws Exception {
//        if (StringUtils.isEmpty(name)) {
//            name = "image";
//        }
//
//        // 默认上传图片
//        if (StringUtils.isEmpty(type)) {
//            type = CommonConstant.UPLOAD_PIC;
//        }
//
//        MultipartFile multipartFile = request.getFile(name);
//        if (Objects.isNull(multipartFile)) {
//            return "";
//        }
//        return ossService.uploadToOss(Arrays.asList(UploadData.build(multipartFile.getInputStream(), multipartFile.getBytes(), multipartFile.getOriginalFilename(), type, multipartFile))).stream().findFirst().orElse("");
//    }

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
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "name", value = "上传文件的name 默认为image"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "type", value = "上传文件的类型 默认为图片 0 图片 1 视频"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回图片、视频在阿里云的地址", response = String.class)
    })
    public String uploadToAliOSSYun(MultipartHttpServletRequest request, @RequestParam("name") String name, @RequestParam("type") String type) throws Exception {
        if (StringUtils.isEmpty(name)) {
            name = "image";
        }
        // 默认上传图片
        if (StringUtils.isEmpty(type)) {
            type = CommonConstant.UPLOAD_PIC;
        }

        MultipartFile multipartFile = request.getFile(name);
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
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "name", value = "上传文件的name 默认为image"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "type", value = "上传文件的类型 默认为图片 0 图片 1 视频"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回图片、视频在阿里云的地址", response = String.class)
    })
    public String uploadToQiniuOSS(MultipartHttpServletRequest request, @RequestParam("name") String name, @RequestParam("type") String type) throws Exception {

        if (StringUtils.isEmpty(name)) {
            name = "image";
        }
        // 默认上传图片
        if (StringUtils.isEmpty(type)) {
            type = CommonConstant.UPLOAD_PIC;
        }

        MultipartFile multipartFile = request.getFile(name);
        if (Objects.isNull(multipartFile)) {
            return "";
        }
        return ossService.uploadToQiniuOss(Arrays.asList(UploadData.build(multipartFile.getInputStream(), multipartFile.getBytes(), multipartFile.getOriginalFilename(), type, multipartFile))).stream().findFirst().orElse("");
    }

}
