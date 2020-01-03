package com.tanp.generator.common.multidatasource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author CodeBricklayer
 * @date 2020/1/3 16:23
 * @description TODO
 */
@Slf4j
public class DruidContextHolder {

  /**
   * 对当前线程的操作-线程安全的
   */
  private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

  /**
   * 调用此方法，切换数据源
   */
  public static void setDataSource(String dataSource) {
    contextHolder.set(dataSource);
    log.info("已切换到数据源:{}", dataSource);
  }

  /**
   * 获取数据源
   */
  public static String getDataSource() {
    return contextHolder.get();
  }

  /**
   * 删除数据源
   */
  public static void clearDataSource() {
    contextHolder.remove();
    log.info("已切换到主数据源");
  }
}
