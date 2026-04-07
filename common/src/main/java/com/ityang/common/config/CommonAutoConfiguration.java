package com.ityang.common.config;

import com.ityang.common.core.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lenovo
 * @date 2026-04-03
 */
@Configuration
@Import(GlobalExceptionHandler.class)
public class CommonAutoConfiguration {
}
