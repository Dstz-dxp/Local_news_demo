package com.ityang.localnewsdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ityang.localnewsdemo.entity.dataobject.PointsDO;
import com.ityang.localnewsdemo.vo.req.PointsReqVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointsMapper extends BaseMapper<PointsDO> {


    default void updateByUserId(PointsDO pointsDO){
        update(new LambdaUpdateWrapper<PointsDO>()
                .setSql("points = points + "+pointsDO.getPoints())
                .eq(PointsDO::getUserId,pointsDO.getUserId()));
    }
}
