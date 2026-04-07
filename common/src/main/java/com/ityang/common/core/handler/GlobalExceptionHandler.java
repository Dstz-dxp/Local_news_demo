package com.ityang.common.core.handler;

import com.ityang.common.exception.BusinessException;
import com.ityang.common.exception.enums.ErrorCode;
import com.ityang.common.pojo.CommonResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author lenovo
 * @date 2026-04-03
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public CommonResult<Void> handleBusinessException(BusinessException ex, HttpServletRequest request){
        log.error("业务异常,code = {},msg = {} request={}",ex.getCode(),ex.getMessage(),request.getRequestURI());
        return CommonResult.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public CommonResult<Void> handleException(Exception ex, HttpServletRequest request){
        log.error("系统异常,{}, request={}",ex,request.getRequestURI());
        return CommonResult.error(ErrorCode.SYSTEM_ERROR);
    }
}
