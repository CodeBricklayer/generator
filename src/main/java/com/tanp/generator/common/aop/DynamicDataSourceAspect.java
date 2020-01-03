//package com.tanp.generator.common.aop;
//
//import com.tanp.generator.common.annotation.DataSource;
//import com.tanp.generator.common.multidatasource.DruidContextHolder;
//import com.tanp.generator.common.multidatasource.DynamicDataSource;
//import com.tanp.generator.entity.DatabaseSource;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author CodeBricklayer
// * @date 2020/1/3 16:33
// * @description 注解参数选择数据源
// */
//@Aspect
//@Component
//public class DynamicDataSourceAspect {
//
//  @Pointcut("@annotation(com.tanp.generator.common.annotation.DataSource)")
//  private void pointcut() {
//
//  }
//
//  @Autowired
//  private DynamicDataSource dynamicDataSource;
//
//
//  @Before("pointcut()")
//  public void beforeSwitchDataSource(JoinPoint point) {
//
//    //获得当前访问的class
//    Class<?> className = point.getTarget().getClass();
//    //获得访问的方法名
//    String methodName = point.getSignature().getName();
//    Object[] objects = point.getArgs();
//    //得到方法的参数的类型
//    Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
//    String dataSource = null;
//    try {
//      // 得到访问的方法对象
//      Method method = className.getMethod(methodName, argClass);
//      Parameter[] parameters = method.getParameters();
//      // 判断是否存在注解
//      for (int i = 0; i < parameters.length; i++) {
//        Parameter parameter = parameters[i];
//        if (parameter.isAnnotationPresent(DataSource.class)) {
//          DatabaseSource databaseSource = (DatabaseSource) objects[i];
//          dynamicDataSource.createDataSourceWithCheck(databaseSource);
//          dataSource = databaseSource.getDatabaseSourceName();
//          break;
//        }
//      }
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    } finally {
//      // 切换数据源
//      DruidContextHolder.setDataSource(dataSource);
//    }
//  }
//
//  @After("pointcut()")
//  public void afterSwitchDataSource(JoinPoint point) {
//    DruidContextHolder.clearDataSource();
//  }
//
//}
