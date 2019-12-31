package com.tanp.generator.common.aop;

import com.tanp.generator.common.constant.DataSource;
import com.tanp.generator.common.multidatasource.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 11:51
 * @description 多数据源切面处理
 */
@Slf4j
@Aspect
@Component
public class DataSourceAspect {
  @Before("@annotation(ds)")
  public void beforeDataSource(DataSource ds){
    String value = ds.value().getDb();
    DataSourceContextHolder.setDateSourceType(value);
    log.info("当前使用的数据源为：{}",value);
  }

  @After("@annotation(ds)")
  public void afterDataSource(DataSource ds){
    DataSourceContextHolder.clearDataSource();
  }

}
