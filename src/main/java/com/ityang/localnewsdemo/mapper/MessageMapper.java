package com.ityang.localnewsdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ityang.localnewsdemo.entity.dataobject.MessageDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
}
