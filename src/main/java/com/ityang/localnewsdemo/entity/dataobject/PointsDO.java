package com.ityang.localnewsdemo.entity.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分实体类DO
 *
 * @author lenovo
 * @date 2026-03-29
 */
@Data
@TableName("`points`")
public class PointsDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联的用户ID
     */
    private Long userId;

    /**
     * 积分数量
     */
    private Integer points;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
