package com.ityang.localnewsdemo.service.impl;

import com.ityang.localnewsdemo.entity.dataobject.MessageDO;
import com.ityang.localnewsdemo.entity.dataobject.OrderDO;
import com.ityang.localnewsdemo.mapper.MessageMapper;
import com.ityang.localnewsdemo.mapper.OrderMapper;
import com.ityang.localnewsdemo.service.MessageService;
import com.ityang.localnewsdemo.service.OrderService;
import com.ityang.localnewsdemo.vo.req.MessageReqVO;
import com.ityang.localnewsdemo.vo.req.OrderReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

/**
 * @author lenovo
 * @date 2026-03-21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    private final MessageService messageService;

    private final MessageMapper messageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(OrderReqVO reqVO) {
        try {
            OrderDO orderDO = new OrderDO();
            orderDO.setOrderName(reqVO.getOrderName());

            /**
             * 生成订单编号  编号规则：订单时间+订单名称的哈希值
             */
            String number = LocalDateTime.now().toString() + reqVO.getOrderName().hashCode();
            orderDO.setOrderNo(number);
            orderDO.setStatus(0);

            orderMapper.insert(orderDO);
            MessageReqVO reqVO1 = new MessageReqVO();
            reqVO1.setOrderId(orderDO.getId());
            messageService.InsertMessage(reqVO1);

            return true;
        } catch (Exception e) {
            log.error("抛出的异常为：{}",e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
