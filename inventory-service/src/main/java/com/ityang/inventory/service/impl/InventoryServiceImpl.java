package com.ityang.inventory.service.impl;

import com.ityang.common.exception.BusinessException;
import com.ityang.common.exception.enums.ErrorCode;
import com.ityang.inventory.constant.exception.InventoryShortageException;
import com.ityang.inventory.entity.dataobject.InventoryDO;
import com.ityang.inventory.mapper.InventoryMapper;
import com.ityang.inventory.service.InventoryService;
import com.ityang.inventory.vo.req.InventoryReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean deductInventory(InventoryReqVO reqVO){
        Integer row = 0;
        Integer quantity = 0;
        try {
            InventoryDO inventoryDO = new InventoryDO();

            inventoryDO.setProductId(reqVO.getProductId());
            quantity = reqVO.getQuantity();

            row = inventoryMapper.updateByProductId(inventoryDO,quantity);
            /*int i = 1/0;*/
        } catch (BusinessException e) {
            log.error("库存扣减失败,异常为: {}",e.getMessage());
            throw new BusinessException(ErrorCode.INVENTORY_DEDUCT_FAIL);
        }
        if(row > 0){
            log.info("扣减库存成功");
            return true;
        }else{
            log.error("库存不足，剩余已不足{}件，扣减库存失败",quantity);
            throw new BusinessException(ErrorCode.INVENTORY_SHORTAGE);
        }
    }
}
