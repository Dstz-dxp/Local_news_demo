package com.ityang.points.vo.req;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 积分请求实体类
 *
 * @author lenovo
 * @date 2026-03-29
 */
@Data
@AllArgsConstructor
public class PointsReqVO {

    /**
     * 关联的用户ID
     */
    private Long userId;

    /**
     * 积分
     */
    private Integer points;
}
