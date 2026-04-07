package com.ityang.inventory.controller;

import com.ityang.common.pojo.CommonResult;
import com.ityang.inventory.service.InventoryService;
import com.ityang.inventory.vo.req.InventoryReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 库存controller
 *
 * @author lenovo
 * @date 2026-04-02
 */
@RestController
@Slf4j
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 更新商品库存
     */
    @PostMapping("/update")
    public CommonResult<Boolean> deductInventory(@RequestBody InventoryReqVO reqVO){
        inventoryService.deductInventory(reqVO);
        return CommonResult.success(true);
    }
}
