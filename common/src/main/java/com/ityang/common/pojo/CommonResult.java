package com.ityang.common.pojo;

import com.ityang.common.exception.enums.ErrorCode;
import lombok.Data;
import org.springframework.util.Assert;

/**
 * 通用返回结果类
 *
 * @author lenovo
 * @date 2026-04-03
 */
@Data
public class CommonResult<T> {

    /**
     * 结果码
     */
    public Integer code;

    public String message;

    public T data;

    public static <T> CommonResult<T> success(T data){
        CommonResult<T> result = new CommonResult<>();
        result.code = ErrorCode.SUCCESS.getCode();
        result.message = ErrorCode.SUCCESS.getMsg();
        result.data = data;
        return result;
    }

    public static <T> CommonResult<T> error(Integer code,String msg){
        Assert.isTrue(!ErrorCode.SUCCESS.getCode().equals(code),"code必须是错误码");
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.message = msg;
        return result;
    }

    public static <T> CommonResult<T> error(ErrorCode errorCode){
        return error(errorCode.getCode(),errorCode.getMsg());
    }

}
