package com.ityang.order.controller;

import com.ityang.common.pojo.CommonResult;
import com.ityang.order.service.OrderService;
import com.ityang.order.vo.req.OrderReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单controller
 *
 * @author lenovo
 * @date 2026-03-21
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单-本地消息表
     */
    @PostMapping("/create")
    public CommonResult<Boolean> createOrder(@RequestBody OrderReqVO reqVO){
        orderService.insert(reqVO);
        return CommonResult.success(true);
    }

    /**
     * 创建订单-同步
     */
    @PostMapping("/create1")
    public boolean createOrderSync(@RequestBody OrderReqVO reqVO){
        return orderService.insertSync(reqVO);
    }

    /**
     * 重发消息-手动补偿
     */
    @PostMapping("/retryMsg")
    public CommonResult<Boolean> retryMessage(){
        orderService.retryMessage();
        return CommonResult.success(true);
    }

}
