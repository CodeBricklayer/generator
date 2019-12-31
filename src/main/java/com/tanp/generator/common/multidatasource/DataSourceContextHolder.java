package com.tanp.generator.common.multidatasource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 11:27
 * @description 动态数据源上下文管理：设置数据源，获取数据源，清除数据源
 */
@Slf4j
public class DataSourceContextHolder {

  /**
   * 存放当前线程使用的数据源类型
   */
  private static final ThreadLocal<String> CONTEXT_HOLDER  = new ThreadLocal<>();

  /**
   * 设置数据源
   */
  public static void setDateSourceType(String dbType) {
    log.info("切换到{}数据源",dbType);
    CONTEXT_HOLDER .set(dbType);
  }

  /**
   * 获取数据源
   */
  public static String getDataSource() {
    return CONTEXT_HOLDER .get();
  }

  /**
   * 清除数据源
   */
  public static void clearDataSource() {
    CONTEXT_HOLDER .remove();
  }

}
