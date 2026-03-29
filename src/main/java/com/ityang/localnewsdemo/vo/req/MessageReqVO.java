package com.ityang.localnewsdemo.vo.req;

import lombok.Data;

/**
 * 插入消息请求实体类
 *
 * @author lenovo
 * @date 2026-03-22
 */
@Data
public class MessageReqVO {

    private Long id;

    /**
     * 关联的订单ID
     */
    private Long orderId;

    private Integer status;

    private Integer retryCount;


}
