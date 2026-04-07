package com.ityang.order.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ityang.order.entity.dataobject.MessageDO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author lenovo
 */
@Mapper
public interface MessageMapper extends BaseMapper<MessageDO> {
    default List<MessageDO> selectByStatusIsZero(){
        return selectList(new LambdaQueryWrapper<MessageDO>()
                .eq(MessageDO::getStatus,0));
    }


    default List<MessageDO> selectByStatusAndRetryCount(){
        return selectList(new LambdaQueryWrapper<MessageDO>()
                .eq(MessageDO::getStatus,0)
                .le(MessageDO::getRetryCount,3));
    }

    void batchInsert(List<MessageDO> list);

    @MapKey("id")
    Map<Long, MessageDO> selectByStatusIsFail(Long startId, Integer batchSize);
}
