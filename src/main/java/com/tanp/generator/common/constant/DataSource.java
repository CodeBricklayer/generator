package com.tanp.generator.common.constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 11:46
 * @description 自定义注解，用于类或方法上，优先级：方法>类
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

  DataSourceType value() default DataSourceType.FIRST;
}
