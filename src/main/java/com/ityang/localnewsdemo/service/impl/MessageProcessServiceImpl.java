package com.ityang.localnewsdemo.service.impl;

import cn.hutool.json.JSONUtil;
import com.ityang.localnewsdemo.constant.ServiceTypeEnum;
import com.ityang.localnewsdemo.constant.exception.InventoryShortageException;
import com.ityang.localnewsdemo.entity.dataobject.MessageDO;
import com.ityang.localnewsdemo.mapper.MessageMapper;
import com.ityang.localnewsdemo.service.InventoryService;
import com.ityang.localnewsdemo.service.MessageProcessService;
import com.ityang.localnewsdemo.service.PointsService;
import com.ityang.localnewsdemo.vo.req.InventoryReqVO;
import com.ityang.localnewsdemo.vo.req.PointsReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

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

    private final InventoryService inventoryService;

    private final PointsService pointsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processMessage(MessageDO message){
        // 消息发送
        log.info("模拟消息发送,订单ID为: {}",message.getOrderId());
        // 如果某条消息发送失败，那么该条消息应该进入重试机制
        Random r = new Random();
        int i = r.nextInt(10)+1;
        try {
            // 判断消息的任务类型
            if(ServiceTypeEnum.INVENTORY.getCode().equals(message.getServiceType()) && i > 0){
                // 库存服务消息
                // 概率大于3时认为消息发送成功
                // 将消息的状态由0改为1（已发送）
                message.setStatus(1);
                messageMapper.updateById(message);
                // 调用库存服务
                InventoryReqVO vo = JSONUtil.toBean(message.getContent(), InventoryReqVO.class);
                inventoryService.deductInventory(vo);
                log.info("订单ID为: {}的消息已发送",message.getOrderId());
                log.info("ID为{}的商品库存已扣减",vo.getProductId());
            }else if(ServiceTypeEnum.POINTS.getCode().equals(message.getServiceType()) && i > 0) {
                // 积分服务消息
                // 概率大于3时认为消息发送成功
                // 将消息的状态由0改为1（已发送）
                message.setStatus(1);
                messageMapper.updateById(message);
                // 调用库存服务
                PointsReqVO vo = JSONUtil.toBean(message.getContent(), PointsReqVO.class);
                pointsService.updatePoints(vo);
                log.info("订单ID为: {}的消息已发送",message.getOrderId());
                log.info("ID为{}的用户积分已计增",vo.getUserId());
            }else {
                // 概率不大于3时认为消息发送失败
                // 消息的状态仍为0，需要重试，每重试一次消息的重试次数就要加1
                message.setRetryCount(message.getRetryCount()+1);
                log.info("订单ID为: {}的消息发送失败,进入重试机制",message.getOrderId());
                retrySendMessage(message);
            }
        } catch (InventoryShortageException ex){
            log.error("订单ID为: {}的商品库存不足，无法扣减库存！",message.getId());
            throw new RuntimeException();
        } catch (Exception e) {
            // 当抛出异常时，消息也发送失败
            // 进入重试机制
            message.setRetryCount(message.getRetryCount()+1);
            log.info("订单ID为: {}的消息发送失败,进入重试机制",message.getOrderId());
            retrySendMessage(message);
        }
    }

    private void retrySendMessage(MessageDO message) {
        Integer re = message.getRetryCount();
        while(re<4) {
            // 消息发送
            log.info("模拟消息发送,订单ID为: {}", message.getOrderId());
            Random r = new Random();
            int i = r.nextInt(10)+1;
            try {
                if (ServiceTypeEnum.INVENTORY.getCode().equals(message.getServiceType()) && i > 0) {
                    // 概率大于3时认为消息发送成功
                    // 将消息的状态由0改为1（已发送）
                    message.setStatus(1);
                    messageMapper.updateById(message);
                    // 调用库存服务
                    InventoryReqVO vo = JSONUtil.toBean(message.getContent(), InventoryReqVO.class);
                    inventoryService.deductInventory(vo);
                    log.info("订单ID为: {}的消息已发送", message.getOrderId());
                    log.info("ID为{}的商品库存已扣减", vo.getProductId());
                    return;
                } else if(ServiceTypeEnum.POINTS.getCode().equals(message.getServiceType()) && i > 0) {
                    // 概率大于3时认为消息发送成功
                    // 将消息的状态由0改为1（已发送）
                    message.setStatus(1);
                    messageMapper.updateById(message);
                    // 调用库存服务
                    PointsReqVO vo = JSONUtil.toBean(message.getContent(), PointsReqVO.class);
                    pointsService.updatePoints(vo);
                    log.info("订单ID为: {}的消息已发送", message.getOrderId());
                    log.info("ID为{}的用户积分已计增", vo.getUserId());
                    return;
                }else {
                    // 概率不大于3时认为消息发送失败
                    // 消息的状态仍为0，需要重试，每重试一次消息的重试次数就要加1
                    log.info("订单ID为: {}的消息重试发送失败{}次", message.getOrderId(), message.getRetryCount());
                    re++;
                    message.setRetryCount(re);
                }
            } catch (InventoryShortageException ex){
                log.error("订单ID为: {}的商品库存不足，无法扣减库存！",message.getId());
                throw new RuntimeException();
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
