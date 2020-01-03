package com.tanp.generator.common.aop;

import com.tanp.generator.common.annotation.DataSource;
import com.tanp.generator.common.multidatasource.DruidContextHolder;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author CodeBricklayer
 * @date 2020/1/3 21:13
 * @description 请求头选择数据源
 */
@Aspect
@Component
public class DataSourceSelectorAspect {


  @Pointcut("execution(public * com.tanp.generator.controller.*.*(..))")
  public void datasourcePointcut() {
  }

  /**
   * 前置操作，拦截具体请求，获取header里的数据源id，设置线程变量里，用于后续切换数据源
   */
  @Before("datasourcePointcut()")
  public void doBefore(JoinPoint joinPoint) {
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();

    // 排除不可切换数据源的方法
    DataSource annotation = method.getAnnotation(DataSource.class);
    if (null != annotation) {
      DruidContextHolder.clearDataSource();
    } else {
      RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
      ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
      HttpServletRequest request = attributes.getRequest();
      String dataSource = request.getHeader("dataSource");
      if (StringUtils.hasText(dataSource)) {
        DruidContextHolder.setDataSource(dataSource);
      } else {
        DruidContextHolder.clearDataSource();
      }
    }
  }

  /**
   * 后置操作，设置回默认的数据源id
   */
  @AfterReturning("datasourcePointcut()")
  public void doAfter() {
    DruidContextHolder.clearDataSource();
  }
}
