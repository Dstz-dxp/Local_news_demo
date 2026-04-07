package com.ityang.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.ityang.common.exception.BusinessException;
import com.ityang.common.exception.enums.ErrorCode;
import com.ityang.common.pojo.CommonResult;
import com.ityang.order.api.InventoryApi;
import com.ityang.order.api.PointsApi;
import com.ityang.order.constant.ServiceTypeEnum;
import com.ityang.order.constant.exception.InventoryShortageException;
import com.ityang.order.entity.dataobject.MessageDO;
import com.ityang.order.mapper.MessageMapper;
import com.ityang.order.service.MessageProcessService;
import com.ityang.order.vo.req.InventoryReqVO;
import com.ityang.order.vo.req.PointsReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 消息定时任务处理类
 *
 * @author lenovo
 * @date 2026-03-31
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageProcessServiceImpl implements MessageProcessService {

    private final MessageMapper messageMapper;

    private final InventoryApi inventoryApi;

    private final PointsApi pointsApi;

    private static final Integer MAX_RETRY_COUNT = 3;

    /**
     * 处理单条消息
     *
     * @param message 消息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processMessage(MessageDO message){
        // 消息发送
        log.info("消息发送,订单ID为: {}",message.getOrderId());
        // 如果某条消息发送失败，那么该条消息应该进入重试机制
        try {
            // 判断消息的任务类型
            if(ServiceTypeEnum.INVENTORY.getCode().equals(message.getServiceType())){
                processInventoryMsg(message);
            }else if(ServiceTypeEnum.POINTS.getCode().equals(message.getServiceType())) {
                processPointsMsg(message);
            }
        } catch (Exception e) {
            // 当抛出异常时，消息也发送失败
            // 进入重试机制
            message.setRetryCount(message.getRetryCount()+1);
            log.info("订单ID为: {}的消息发送失败,进入重试机制",message.getOrderId());
            retrySendMessage(message);
        }
    }

    /**
     * 批量处理失败消息列表
     *
     * @param failMsgList 失败消息列表
     */
    @Override
    public void processMessageList(List<MessageDO> failMsgList) {
        // 对失败消息列表分类处理
        Map<Integer, List<MessageDO>> map = failMsgList.stream()
                .collect(Collectors.groupingBy(MessageDO::getServiceType));

        List<MessageDO> invenList = map.get(ServiceTypeEnum.INVENTORY.getCode());
        List<MessageDO> pointsList = map.get(ServiceTypeEnum.POINTS.getCode());

        if(!CollUtil.isEmpty(invenList)){
            for (MessageDO messageDO : invenList) {
                processInventoryMsg(messageDO);
            }
        }

        if (!CollUtil.isEmpty(pointsList)){
            for (MessageDO messageDO : pointsList) {
                processPointsMsg(messageDO);
            }
        }
    }

    /**
     * 发送积分消息，计增用户积分
     *
     * @param message
     */
    private void processPointsMsg(MessageDO message) {
        // 积分服务消息
        // 将消息的状态由0改为1（已发送）
        message.setStatus(1);
        messageMapper.updateById(message);
        // 调用库存服务
        PointsReqVO vo = JSONUtil.toBean(message.getContent(), PointsReqVO.class);
        CommonResult<Boolean> result = pointsApi.addPoints(vo);
        if(ErrorCode.SUCCESS.getCode().equals(result.getCode())){
            log.info("订单ID为: {}的消息已发送", message.getOrderId());
            log.info("ID为{}的用户积分已计增",vo.getUserId());
        }else {
            // 当积分计增方法执行失败时，消息需要重试
            // 抛出异常，进入重试
            throw new BusinessException(ErrorCode.POINTS_ADD_FAIL);
        }
    }

    /**
     * 发送库存消息，扣减商品库存
     *
     * @param message
     */
    private void processInventoryMsg(MessageDO message) {
        // 库存服务消息
        // 将消息的状态由0改为1（已发送）
        message.setStatus(1);
        messageMapper.updateById(message);
        // 调用库存服务
        InventoryReqVO vo = JSONUtil.toBean(message.getContent(), InventoryReqVO.class);
        CommonResult<Boolean> result = inventoryApi.deductInventory(vo);
        if(ErrorCode.SUCCESS.getCode().equals(result.getCode())){
            log.info("订单ID为: {}的消息已发送", message.getOrderId());
            log.info("ID为{}的商品库存已扣减",vo.getProductId());
        }else if(ErrorCode.INVENTORY_SHORTAGE.getCode().equals(result.getCode())){
            log.warn("订单ID为: {}的商品库存不足，无法扣减库存！", message.getOrderId());
            // 库存不足时不再重试，消息直接设置为发送失败
            message.setStatus(2);
            message.setRetryCount(message.getRetryCount());
            // 记录错误日志，有待人工介入
            messageMapper.updateById(message);
            log.error("订单ID为: {},内容为{}的消息因库存不足发送失败，需要人工介入", message.getOrderId(), message.getContent());
        }else {
            // 当因扣减方法执行失败时，消息需要重试
            // 抛出异常，进入重试
            throw new BusinessException(ErrorCode.INVENTORY_DEDUCT_FAIL);
        }
    }

    /**
     * 消息重试
     *
     * @param message
     */
    private void retrySendMessage(MessageDO message) {
        Integer re = message.getRetryCount();
        while(re <= MAX_RETRY_COUNT) {
            // 消息发送
            log.info("消息发送,订单ID为: {}", message.getOrderId());
            try {
                if (ServiceTypeEnum.INVENTORY.getCode().equals(message.getServiceType())) {
                    processInventoryMsg(message);
                    return;
                } else if(ServiceTypeEnum.POINTS.getCode().equals(message.getServiceType())) {
                    processPointsMsg(message);
                    return;
                }
            } catch (Exception e) {
                // 当抛出异常时，消息也发送失败
                log.info("订单ID为: {}的消息重试发送失败{}次", message.getOrderId(), message.getRetryCount());
                re++;
                message.setRetryCount(re);
            }
        }
        // 代码进行到这里，说明订单消息重试3次也完全失败
        message.setStatus(2);
        message.setRetryCount(message.getRetryCount());
        // 记录错误日志，有待人工介入
        messageMapper.updateById(message);
        log.error("订单ID为: {},内容为{}的消息重试3次发送失败，需要人工介入",message.getOrderId(),message.getContent());
    }
}
