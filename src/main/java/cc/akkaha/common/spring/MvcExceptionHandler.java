package cc.akkaha.common.spring;


import cc.akkaha.common.util.JsonUtils;
import cc.akkaha.shop.model.ApiCode;
import cc.akkaha.shop.model.ApiRes;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MvcExceptionHandler implements HandlerExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public ModelAndView resolveException(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception e
    ) {
        String stackTrace = ExceptionUtils.getStackTrace(e);
        logger.error(stackTrace);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            ApiRes res = new ApiRes();
            res.setCode(ApiCode.ERROR);
            res.setMsg(stackTrace);
            writer.println(JsonUtils.stringfy(res));
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            logger.warn(ExceptionUtils.getStackTrace(e1));
        }
        return null;
    }
}
