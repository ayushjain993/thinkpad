package io.uhha.appletsutil;


import io.swagger.annotations.Api;
import io.uhha.common.annotation.UnAuth;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by mj on 2019/5/25.
 * 错误控制器
 */
@Api(tags = "全局错误控制器")
@RestController
public class ErrorController extends AbstractErrorController {

    /**
     * 错误地址
     */
    private static final String ERROR_PATH = "/error";

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

//    @Override
//    public String getErrorPath() {
//        return ERROR_PATH;
//    }

    @RequestMapping(ERROR_PATH)
    @UnAuth
    public Map<String, Object> handleError(HttpServletRequest request) {
        return getErrorAttributes(request, ErrorAttributeOptions.defaults());
    }
}
