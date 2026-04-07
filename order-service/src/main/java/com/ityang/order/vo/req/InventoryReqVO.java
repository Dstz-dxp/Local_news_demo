package com.ityang.order.vo.req;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 库存请求实体类
 *
 * @author lenovo
 * @date 2026-03-29
 */
@Data
@AllArgsConstructor
public class InventoryReqVO {

    /**
     * 关联的商品ID
     */
    private Long productId;

    /**
     * 购买的商品数量
     */
    private Integer quantity;
}
