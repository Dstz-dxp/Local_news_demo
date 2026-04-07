package com.ityang.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 错误编码
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 成功
    SUCCESS(0,"成功"),

    // 系统级错误
    SYSTEM_ERROR(500,"系统内部错误"),
    PARAM_ERROR(400,"参数校验错误"),
    RESOURCES_NOT_FOUND(404,"资源不存在"),

    // 业务级错误(20xxx)
    INVENTORY_SHORTAGE(20_001,"库存不足,扣减库存失败"),
    ORDER_CREATE_FAIL(20_002,"订单创建失败"),
    MESSAGE_INSERT_FAIL(20_003,"消息插入失败"),
    INVENTORY_DEDUCT_FAIL(20_004,"库存扣减失败"),
    POINTS_ADD_FAIL(20_005,"积分计增失败"),
    SEND_MESSAGE_FAIL(20_006,"消息发送失败"),
    MANUAL_RETRY_SEND_FAIL_MESSAGE_FAIL(20_007,"手动补偿发送已失败消息失败");


    private final Integer code;
    private final String msg;

}
