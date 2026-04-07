package com.ityang.order.api;

import com.ityang.common.pojo.CommonResult;
import com.ityang.order.vo.req.PointsReqVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "points-service",url = "http://localhost:8083")
public interface PointsApi {

    @PostMapping("/points/add_points")
    CommonResult<Boolean> addPoints(@RequestBody PointsReqVO reqVO);

    @PostMapping("/points/add_points1")
    boolean addPoints1(@RequestBody PointsReqVO reqVO);
}
