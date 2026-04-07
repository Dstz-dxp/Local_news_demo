package com.ityang.order.service;

import com.ityang.order.vo.req.OrderReqVO;

public interface OrderService {

    boolean insert(OrderReqVO reqVO);

    boolean insertSync(OrderReqVO reqVO);

    void retryMessage();
}
