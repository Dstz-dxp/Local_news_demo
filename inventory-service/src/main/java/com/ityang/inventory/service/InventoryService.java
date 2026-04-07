package com.ityang.inventory.service;

import com.ityang.inventory.vo.req.InventoryReqVO;

public interface InventoryService {

    boolean deductInventory(InventoryReqVO reqVO);
}
