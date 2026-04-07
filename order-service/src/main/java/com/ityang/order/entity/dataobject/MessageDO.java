package com.ityang.order.entity.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息DO类
 *
 * @author lenovo
 * @date 2026-03-22
 */
@Data
@TableName("`message`")
public class MessageDO {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联的订单ID
     */
    private Long orderId;

    /**
     * 消息状态
     */
    private Integer status;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 消息类型 1-inventory，2-points
     */
    private Integer serviceType;

    /**
     * 消息内容
     */
    private String content;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
