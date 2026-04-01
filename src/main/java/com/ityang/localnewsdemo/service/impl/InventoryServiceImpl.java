package com.ityang.localnewsdemo.service.impl;

import com.ityang.localnewsdemo.constant.exception.InventoryShortageException;
import com.ityang.localnewsdemo.entity.dataobject.InventoryDO;
import com.ityang.localnewsdemo.mapper.InventoryMapper;
import com.ityang.localnewsdemo.service.InventoryService;
import com.ityang.localnewsdemo.vo.req.InventoryReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 库存Service层实现类
 *
 * @author lenovo
 * @date 2026-03-29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;

    /**
     * 扣减商品库存
     *
     * @param reqVO
     * @return
     */
    @Override
    public boolean deductInventory(InventoryReqVO reqVO){
        InventoryDO inventoryDO = new InventoryDO();

        inventoryDO.setProductId(reqVO.getProductId());
        Integer quantity = reqVO.getQuantity();

        Integer row = inventoryMapper.updateByProductId(inventoryDO,quantity);
        /*int i = 1/0;*/
        if(row>0){
            log.info("扣减库存成功");
            return true;
        }else{
            log.error("库存不足，剩余已不足{}件，扣减库存失败",quantity);
            throw new InventoryShortageException("库存不足，扣减库存失败！");
        }
    }
}
