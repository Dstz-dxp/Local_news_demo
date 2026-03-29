package com.ityang.localnewsdemo.service;

import com.ityang.localnewsdemo.vo.req.MessageReqVO;

public interface MessageService {

    boolean InsertMessage(MessageReqVO reqVO);

    boolean updateMessage(MessageReqVO reqVO);
}
