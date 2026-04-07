package com.ityang.order.constant;

/**
 * 消息类型枚举类
 */
public enum ServiceTypeEnum {

    INVENTORY("库存服务",1),

    POINTS("积分服务",2);


    private String desc;

    private Integer code;

    ServiceTypeEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }
}
