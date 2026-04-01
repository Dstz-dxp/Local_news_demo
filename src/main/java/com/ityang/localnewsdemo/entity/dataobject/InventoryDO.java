package com.ityang.localnewsdemo.entity.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存实体类DO
 *
 * @author lenovo
 * @date 2026-03-29
 */
@Data
@TableName("`inventory`")
public class InventoryDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联的商品ID
     */
    private Long productId;

    /**
     * 库存数量
     */
    private Integer stock;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
