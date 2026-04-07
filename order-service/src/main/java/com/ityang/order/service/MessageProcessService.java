package com.ityang.order.service;

import com.ityang.order.entity.dataobject.MessageDO;

import java.util.List;

public interface MessageProcessService {

    void processMessage(MessageDO message);

    void processMessageList(List<MessageDO> failMsgList);
}
