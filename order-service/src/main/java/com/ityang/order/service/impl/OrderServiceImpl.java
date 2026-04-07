package com.ityang.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.ityang.common.exception.BusinessException;
import com.ityang.common.exception.enums.ErrorCode;
import com.ityang.order.api.InventoryApi;
import com.ityang.order.api.PointsApi;
import com.ityang.order.constant.ServiceTypeEnum;
import com.ityang.order.entity.dataobject.MessageDO;
import com.ityang.order.entity.dataobject.OrderDO;
import com.ityang.order.mapper.MessageMapper;
import com.ityang.order.mapper.OrderMapper;
import com.ityang.order.service.MessageProcessService;
import com.ityang.order.service.MessageService;
import com.ityang.order.service.OrderService;
import com.ityang.order.vo.req.InventoryReqVO;
import com.ityang.order.vo.req.MessageReqVO;
import com.ityang.order.vo.req.OrderReqVO;
import com.ityang.order.vo.req.PointsReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    private final MessageProcessService messageProcessService;

    private final InventoryApi inventoryApi;

    private final PointsApi pointsApi;

    private final MessageMapper messageMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(OrderReqVO reqVO) {
        log.info("开始订单创建。。。。");
        try {
            OrderDO orderDO = new OrderDO();
            orderDO.setOrderName(reqVO.getOrderName());

            // 生成订单编号  编号规则：订单时间+订单名称的哈希值
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
            throw new BusinessException(ErrorCode.ORDER_CREATE_FAIL);
        }
    }

    @Override
    public boolean insertSync(OrderReqVO reqVO){
        try {
            OrderDO orderDO = new OrderDO();
            orderDO.setOrderName(reqVO.getOrderName());

            // 生成订单编号  编号规则：订单时间+订单名称的哈希值
            String number = Integer.toHexString(reqVO.getOrderName().hashCode())+
                    LocalDateTime.now().format(FORMATTER);
            orderDO.setOrderNo(number);
            orderDO.setStatus(0);

            orderMapper.insert(orderDO);

            InventoryReqVO inventoryReqVO = new InventoryReqVO(1L,2);
            inventoryApi.deductInventory(inventoryReqVO);

            PointsReqVO pointsReqVO = new PointsReqVO(1L,30);
            pointsApi.addPoints(pointsReqVO);
            return true;
        } catch (Exception e) {
            log.error("订单创建失败，抛出的异常为：{}",e.getMessage());
            e.printStackTrace();
            throw new BusinessException(ErrorCode.ORDER_CREATE_FAIL);
        }
    }

    @Override
    public void retryMessage() {
        try {
            // 为了避免大数据量一次性加载到内存，使用id分段查询
            Long startId = 0L;
            Integer batchSize = 500;
            Map<Long,MessageDO> batch = new HashMap<>();

            while(true){
                // 批量获取消息批次数据
                batch = messageMapper.selectByStatusIsFail(startId,batchSize);
                // 如果批次数据为空，跳出循环
                if(CollUtil.isEmpty(batch)){
                    break;
                }

                // 批量处理失败消息
                List<MessageDO> failMsgList = new ArrayList<>(batch.values());
                messageProcessService.processMessageList(failMsgList);

                // 计算下一个startId
                List<Long> ids = new ArrayList<>(batch.keySet());
                List<Long> sortedList = ids.stream().sorted().toList();
                startId = sortedList.get(sortedList.size()-1) + 1;
            }
        } catch (Exception e) {
            log.error("手动补偿失败，抛出的异常为：{}",e.getMessage());
            e.printStackTrace();
            throw new BusinessException(ErrorCode.MANUAL_RETRY_SEND_FAIL_MESSAGE_FAIL);
        }
    }

}
