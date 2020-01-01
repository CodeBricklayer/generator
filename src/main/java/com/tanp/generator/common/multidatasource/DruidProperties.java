package com.tanp.generator.common.multidatasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 13:41
 * @description 数据源配置文件封装类
 */
@Configuration
@Slf4j
public class DruidProperties {

  @Value("${spring.datasource.druid.initialSize}")
  private int initialSize;

  @Value("${spring.datasource.druid.minIdle}")
  private int minIdle;

  @Value("${spring.datasource.druid.maxActive}")
  private int maxActive;

  @Value("${spring.datasource.druid.maxWait}")
  private int maxWait;

  @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
  private int timeBetweenEvictionRunsMillis;

  @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
  private int minEvictableIdleTimeMillis;

  @Value("${spring.datasource.druid.maxEvictableIdleTimeMillis}")
  private int maxEvictableIdleTimeMillis;

  @Value("${spring.datasource.druid.validationQuery}")
  private String validationQuery;

  @Value("${spring.datasource.druid.testWhileIdle}")
  private boolean testWhileIdle;

  @Value("${spring.datasource.druid.testOnBorrow}")
  private boolean testOnBorrow;

  @Value("${spring.datasource.druid.testOnReturn}")
  private boolean testOnReturn;

  private String filters = "stat,wall";

  public DruidDataSource dataSource(DruidDataSource datasource) {
    /** 配置初始化大小、最小、最大 */
    datasource.setInitialSize(initialSize);
    datasource.setMaxActive(maxActive);
    datasource.setMinIdle(minIdle);

    /** 配置获取连接等待超时的时间 */
    datasource.setMaxWait(maxWait);

    /** 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */
    datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

    /** 配置一个连接在池中最小、最大生存的时间，单位是毫秒 */
    datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);

    /**
     * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
     */
    datasource.setValidationQuery(validationQuery);
    /** 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
    datasource.setTestWhileIdle(testWhileIdle);
    /** 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
    datasource.setTestOnBorrow(testOnBorrow);
    /** 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
    datasource.setTestOnReturn(testOnReturn);
    try {
      datasource.setFilters(filters);
      datasource.setProxyFilters(druidFilters());
    } catch (SQLException e) {
      log.error("设置过滤器异常:{}", e);
    }
    return datasource;
  }

  /**
   * druid过滤器
   */
  public List<Filter> druidFilters() {
    StatFilter statFilter = new StatFilter();
    statFilter.setLogSlowSql(true);
    statFilter.setSlowSqlMillis(1000);
    statFilter.setMergeSql(false);
    WallFilter wallFilter = new WallFilter();
    WallConfig wallConfig = new WallConfig();
    wallConfig.setMultiStatementAllow(true);
    wallFilter.setConfig(wallConfig);
    List<Filter> filters = new LinkedList<>();
    filters.add(statFilter);
    filters.add(wallFilter);
    return filters;
  }
}
