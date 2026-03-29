package com.ityang.localnewsdemo.controller;

import com.ityang.localnewsdemo.service.OrderService;
import com.ityang.localnewsdemo.vo.req.OrderReqVO;
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
     * 创建订单
     */
    @PostMapping("/create")
    public boolean createOrder(@RequestBody OrderReqVO reqVO){
        return orderService.insert(reqVO);
    }
}
