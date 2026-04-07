package com.ityang.order.entity.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单DO类
 *
 * @author lenovo
 * @date 2026-03-21
 */
@Data
@TableName("`order`")
public class OrderDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 订单状态
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
