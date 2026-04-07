package com.ityang.points.service.impl;

import com.ityang.common.exception.BusinessException;
import com.ityang.common.exception.enums.ErrorCode;
import com.ityang.points.entity.dataobject.PointsDO;
import com.ityang.points.mapper.PointsMapper;
import com.ityang.points.service.PointsService;
import com.ityang.points.vo.req.PointsReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean addPoints(PointsReqVO reqVO){
        try {
            PointsDO pointsDO = new PointsDO();

            pointsDO.setUserId(reqVO.getUserId());
            pointsDO.setPoints(reqVO.getPoints());

            pointsMapper.updateByUserId(pointsDO);
            /*int i = 1/0;*/
            return true;
        } catch (Exception e) {
            log.error("积分计增失败,异常为: {}",e.getMessage());
            throw new BusinessException(ErrorCode.POINTS_ADD_FAIL);
        }
    }
}
