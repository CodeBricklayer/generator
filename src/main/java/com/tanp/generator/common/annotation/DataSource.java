package com.tanp.generator.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CodeBricklayer
 * @Target 注解可以作用在参数或者方法上
 * @Retention 注解生命周期作用范围
 * @date 2020/1/3 16:30
 * @description TODO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface DataSource {

}
