package com.tanp.generator.common.multidatasource;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 11:31
 * @description 动态数据源，每执行一次数据库，动态获取数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

  private static Map<Object, Object> dataSourceMap = new HashMap<>();
  private static DynamicDataSource instance;
  private static byte[] lock = new byte[0];

  /**
   * 重写setTargetDataSources，通过入参targetDataSources进行数据源的添加
   */
  @Override
  public void setTargetDataSources(Map<Object, Object> targetDataSources) {
    super.setTargetDataSources(targetDataSources);
    dataSourceMap.putAll(targetDataSources);
    super.afterPropertiesSet();
  }

  /**
   * 单例模式，保证获取到的都是同一个对象
   */
  public static synchronized DynamicDataSource getInstance() {
    if (instance == null) {
      synchronized (lock) {
        if (instance == null) {
          instance = new DynamicDataSource();
        }
      }
    }
    return instance;
  }

  @Override
  protected Object determineCurrentLookupKey() {
    return DataSourceContextHolder.getDataSource();
  }

  /**
   * 获取到原有的多数据源，并从该数据源基础上添加一个或多个数据源后，通过上面的setTargetDataSources进行加载
   */
  public Map<Object, Object> getDataSourceMap() {
    return dataSourceMap;
  }
}
