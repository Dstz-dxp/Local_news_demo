package com.ityang.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ityang.order.entity.dataobject.OrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {

    @Override
    int insert(OrderDO orderDO);

}
