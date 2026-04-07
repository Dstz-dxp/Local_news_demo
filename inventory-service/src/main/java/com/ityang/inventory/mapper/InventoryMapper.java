package com.ityang.inventory.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ityang.inventory.entity.dataobject.InventoryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventoryMapper extends BaseMapper<InventoryDO> {


    default Integer updateByProductId(InventoryDO inventoryDO, Integer quantity){
        return update(new LambdaUpdateWrapper<InventoryDO>()
                .eq(InventoryDO::getProductId,inventoryDO.getProductId())
                .ge(InventoryDO::getStock,quantity)
                .setSql("stock = stock - " + quantity));
    }
}
