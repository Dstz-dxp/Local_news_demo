package com.ityang.inventory.constant.exception;

/**
 * 库存不足异常
 *
 * @author lenovo
 * @date 2026-04-01
 */
public class InventoryShortageException extends RuntimeException{

    public InventoryShortageException(String message) {
        super(message);
    }
}
