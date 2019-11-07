package com.nowcoder.community.controller.advice;

import com.nowcoder.community.util.CommunityUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author xuming
 * @Date 2019/11/7 16:52
 * 统一异常处理
 */
@ControllerAdvice(annotations = Controller.class) /** 只扫描Controller */
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        logger.error("服务器发生异常: " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }

        // 同步请求与异步请求的异常要分别处理
        String xRequestedWith = request.getHeader("x-requested-with");
        // 如果是异步请求
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "服务器异常!"));
        } else {
            // 如果是同步请求,则重定向到特定的错误页面
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
