package com.ityang.localnewsdemo.service;

import com.ityang.localnewsdemo.vo.req.InventoryReqVO;

public interface InventoryService {

    boolean deductInventory(InventoryReqVO reqVO);
}
