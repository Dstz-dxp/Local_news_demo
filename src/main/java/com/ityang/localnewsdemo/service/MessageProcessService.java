package com.ityang.localnewsdemo.service;

import com.ityang.localnewsdemo.entity.dataobject.MessageDO;

public interface MessageProcessService {

    void processMessage(MessageDO message);
}
