package com.ityang.localnewsdemo.service.impl;

import com.ityang.localnewsdemo.entity.dataobject.PointsDO;
import com.ityang.localnewsdemo.mapper.PointsMapper;
import com.ityang.localnewsdemo.service.PointsService;
import com.ityang.localnewsdemo.vo.req.PointsReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 积分Service层实现类
 *
 * @author lenovo
 * @date 2026-03-29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PointsServiceImpl implements PointsService {

    private final PointsMapper pointsMapper;

    /**
     * 更新某用户积分
     *
     * @return boolean
     */
    @Override
    public boolean updatePoints(PointsReqVO reqVO){
        PointsDO pointsDO = new PointsDO();

        pointsDO.setUserId(reqVO.getUserId());
        pointsDO.setPoints(reqVO.getPoints());

        pointsMapper.updateByUserId(pointsDO);
        return true;
    }
}
