package com.ityang.localnewsdemo.job;

import com.ityang.localnewsdemo.entity.dataobject.MessageDO;
import com.ityang.localnewsdemo.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * 消息定时任务发送类
 *
 * @author lenovo
 * @date 2026-03-22
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class MessageSchedule {

    private final MessageMapper messageMapper;

    /*@Scheduled(fixedDelay = 5000)
    public void SendMessage(){
        // 查询消息表中status = 0的记录
        List<MessageDO> messages = messageMapper.selectByStatusIsZero();

        // 遍历记录，打印日志
        for (MessageDO message : messages) {
            log.info("模拟消息发送,订单ID为: {}",message.getOrderId());
            // 将消息的状态由0改为1（已发送）
            message.setStatus(1);
            messageMapper.updateById(message);
        }
    }*/

    @Scheduled(fixedDelay = 5000)
    public void sendMessage(){
        // 查询消息表中status = 0且retryCount小于等于3的的记录
        List<MessageDO> messages = messageMapper.selectByStatusAndRetryCount();

        // 遍历记录，打印日志
        for (MessageDO message : messages) {
            // 消息发送
            log.info("模拟消息发送,订单ID为: {}",message.getOrderId());
            // 如果某条消息发送失败，那么该条消息应该进入重试机制
            Random r = new Random();
            int i = r.nextInt(10);
            if(i>3){
                // 概率大于3时认为消息发送成功
                // 将消息的状态由0改为1（已发送）
                message.setStatus(1);
                messageMapper.updateById(message);
                log.info("订单ID为: {}的消息已发送",message.getOrderId());
            }else {
                // 概率不大于3时认为消息发送失败
                // 消息的状态仍为0，需要重试，每重试一次消息的重试次数就要加1
                message.setRetryCount(message.getRetryCount()+1);
                log.info("订单ID为: {}的消息发送失败,进入重试机制",message.getOrderId());
                retrySendMessage(message);
            }
        }
    }

    private void retrySendMessage(MessageDO message) {
        Integer re = message.getRetryCount();
        while(re<4){
            // 消息发送
            log.info("模拟消息发送,订单ID为: {}",message.getOrderId());
            Random r = new Random();
            int i = r.nextInt(10);
            if(i>3){
                // 概率大于3时认为消息发送成功
                // 将消息的状态由0改为1（已发送）
                message.setStatus(1);
                messageMapper.updateById(message);
                log.info("订单ID为: {}的消息已发送",message.getOrderId());
                return;
            }else {
                // 概率不大于3时认为消息发送失败
                // 消息的状态仍为0，需要重试，每重试一次消息的重试次数就要加1
                log.info("订单ID为: {}的消息重试发送失败{}次",message.getOrderId(),message.getRetryCount());
                re++;
                message.setRetryCount(re);
            }
        }
        // 代码进行到这里，说明订单重试3次也完全失败
        message.setStatus(2);
        message.setRetryCount(message.getRetryCount());
        // 记录错误日志，有待人工介入
        messageMapper.updateById(message);
        log.error("订单ID为: {}的消息重试3次发送失败，需要人工介入",message.getOrderId());

    }
}
