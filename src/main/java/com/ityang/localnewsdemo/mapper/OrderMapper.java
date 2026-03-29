package com.ityang.localnewsdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ityang.localnewsdemo.entity.dataobject.OrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {

    @Override
    int insert(OrderDO orderDO);

}
