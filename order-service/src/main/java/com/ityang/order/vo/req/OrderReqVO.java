package com.ityang.order.vo.req;

import lombok.Data;
import lombok.NonNull;

/**
 * 创建订单请求实体类
 *
 * @author lenovo
 * @date 2026-03-21
 */
@Data
public class OrderReqVO {

    /**
     * 订单名称
     */
    @NonNull
    private String orderName;

    /**
     * 订单地址
     */
    private String orderAddress;
}
