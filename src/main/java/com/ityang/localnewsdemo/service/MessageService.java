package com.ityang.localnewsdemo.service;

import com.ityang.localnewsdemo.vo.req.MessageReqVO;

import java.util.List;

public interface MessageService {

    boolean insertMessage(MessageReqVO reqVO);

    boolean updateMessage(MessageReqVO reqVO);

    Integer batchInsertMessage(List<MessageReqVO> voList);
}
