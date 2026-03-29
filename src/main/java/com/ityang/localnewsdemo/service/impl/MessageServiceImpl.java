package com.ityang.localnewsdemo.service.impl;

import com.ityang.localnewsdemo.entity.dataobject.MessageDO;
import com.ityang.localnewsdemo.mapper.MessageMapper;
import com.ityang.localnewsdemo.service.MessageService;
import com.ityang.localnewsdemo.vo.req.MessageReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 消息Service实现类
 *
 * @author lenovo
 * @date 2026-03-22
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;


    @Override
    public boolean InsertMessage(MessageReqVO reqVO) {

        MessageDO messageDO = new MessageDO();
        messageDO.setOrderId(reqVO.getOrderId());
        messageDO.setStatus(0);

        /*int i = 1/0;*/

        messageMapper.insert(messageDO);
        return true;

    }

    @Override
    public boolean updateMessage(MessageReqVO reqVO) {
        try {
            MessageDO messageDO = new MessageDO();
            messageDO.setId(reqVO.getId());
            messageDO.setOrderId(reqVO.getOrderId());
            messageDO.setStatus(reqVO.getStatus());

            messageMapper.updateById(messageDO);
            return true;
        } catch (Exception e) {
            log.error("抛出的异常为：{}",e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
