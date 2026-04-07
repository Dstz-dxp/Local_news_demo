package com.ityang.order.api;

import com.ityang.common.pojo.CommonResult;
import com.ityang.order.vo.req.InventoryReqVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service",url = "http://localhost:8082")
public interface InventoryApi {

    @PostMapping("/inventory/update")
    CommonResult<Boolean> deductInventory(@RequestBody InventoryReqVO reqVO);
}
