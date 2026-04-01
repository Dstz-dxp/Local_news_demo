package com.ityang.localnewsdemo.job;

import cn.hutool.json.JSONUtil;
import com.ityang.localnewsdemo.constant.ServiceTypeEnum;
import com.ityang.localnewsdemo.entity.dataobject.MessageDO;
import com.ityang.localnewsdemo.mapper.MessageMapper;
import com.ityang.localnewsdemo.service.InventoryService;
import com.ityang.localnewsdemo.service.MessageProcessService;
import com.ityang.localnewsdemo.service.PointsService;
import com.ityang.localnewsdemo.vo.req.InventoryReqVO;
import com.ityang.localnewsdemo.vo.req.PointsReqVO;
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

    private final MessageProcessService messageProcessService;

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
            try {
                messageProcessService.processMessage(message);
            } catch (Exception e) {
                log.error("消息ID为{},消息内容为{}的消息发送失败",message.getId(),message.getContent());
                log.error("异常为: {}",e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
