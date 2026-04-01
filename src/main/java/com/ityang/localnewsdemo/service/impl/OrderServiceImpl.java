package com.ityang.localnewsdemo.service.impl;

import cn.hutool.json.JSONUtil;
import com.ityang.localnewsdemo.constant.ServiceTypeEnum;
import com.ityang.localnewsdemo.entity.dataobject.InventoryDO;
import com.ityang.localnewsdemo.entity.dataobject.MessageDO;
import com.ityang.localnewsdemo.entity.dataobject.OrderDO;
import com.ityang.localnewsdemo.mapper.MessageMapper;
import com.ityang.localnewsdemo.mapper.OrderMapper;
import com.ityang.localnewsdemo.service.MessageService;
import com.ityang.localnewsdemo.service.OrderService;
import com.ityang.localnewsdemo.vo.req.InventoryReqVO;
import com.ityang.localnewsdemo.vo.req.MessageReqVO;
import com.ityang.localnewsdemo.vo.req.OrderReqVO;
import com.ityang.localnewsdemo.vo.req.PointsReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(OrderReqVO reqVO) {
        log.info("开始订单创建。。。。");
        try {
            OrderDO orderDO = new OrderDO();
            orderDO.setOrderName(reqVO.getOrderName());

            /**
             * 生成订单编号  编号规则：订单时间+订单名称的哈希值
             */

            String number = Integer.toHexString(reqVO.getOrderName().hashCode())+
                    LocalDateTime.now().format(FORMATTER);
            orderDO.setOrderNo(number);
            orderDO.setStatus(0);

            orderMapper.insert(orderDO);
            MessageReqVO reqVO1 = new MessageReqVO();
            MessageReqVO reqVO2 = new MessageReqVO();

            reqVO1.setOrderId(orderDO.getId());
            reqVO2.setOrderId(orderDO.getId());

            // 此处直接取默认值
            reqVO1.setServiceType(ServiceTypeEnum.INVENTORY.getCode());
            InventoryReqVO inventoryReqVO = new InventoryReqVO(1L,2);
            reqVO1.setContent(JSONUtil.toJsonStr(inventoryReqVO));

            reqVO2.setServiceType(ServiceTypeEnum.POINTS.getCode());
            PointsReqVO pointsReqVO = new PointsReqVO(1L,30);
            reqVO2.setContent(JSONUtil.toJsonStr(pointsReqVO));

            List<MessageReqVO> voList = Arrays.asList(reqVO1, reqVO2);

            messageService.batchInsertMessage(voList);
            log.info("ID为{}的订单创建成功",orderDO.getId());
            return true;
        } catch (Exception e) {
            log.error("订单创建失败，抛出的异常为：{}",e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
