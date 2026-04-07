package com.ityang.points.controller;

import com.ityang.common.pojo.CommonResult;
import com.ityang.points.service.PointsService;
import com.ityang.points.vo.req.PointsReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 积分controller
 *
 * @author lenovo
 * @date 2026-04-01
 */
@RestController
@Slf4j
@RequestMapping("/points")
public class PointsController {

    @Autowired
    private PointsService pointsService;

    /**
     * 计增用户积分
     */
    @PostMapping("/add_points")
    public CommonResult<Boolean> addPoints(@RequestBody PointsReqVO reqVO){
        pointsService.addPoints(reqVO);
        return CommonResult.success(true);
    }

    /**
     * 计增用户积分1
     */
    @PostMapping("/add_points1")
    public boolean addPoints1(@RequestBody PointsReqVO reqVO){
        return pointsService.addPoints(reqVO);
    }
}
