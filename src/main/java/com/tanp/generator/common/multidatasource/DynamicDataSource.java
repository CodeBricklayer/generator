package com.tanp.generator.common.multidatasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.tanp.generator.entity.DatabaseSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

/**
 * @author CodeBricklayer
 * @date 2020/1/3 15:57
 * @description TODO
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

  private boolean debug = true;
  private Map<Object, Object> dynamicTargetDataSources;
  private Object dynamicDefaultTargetDataSource;

  @Override
  protected Object determineCurrentLookupKey() {
    String datasource = DruidContextHolder.getDataSource();
    if (!StringUtils.isEmpty(datasource)) {
      Map<Object, Object> dynamicTargetDataSources = this.dynamicTargetDataSources;
      if (dynamicTargetDataSources.containsKey(datasource)) {
        log.info("---当前数据源：" + datasource + "---");
      } else {
        log.info("不存在的数据源：");
        return null;
      }
    } else {
      log.info("---当前数据源：默认数据源---");
    }
    return datasource;
  }

  @Override
  public void setTargetDataSources(Map<Object, Object> targetDataSources) {
    super.setTargetDataSources(targetDataSources);
    this.dynamicTargetDataSources = targetDataSources;
  }


  /**
   * 创建数据源
   */
  public boolean createDataSource(String key, String driverClassName, String url, String username,
      String password) {
    if (testDatasource(key, driverClassName, url, username, password)) {
      try {
        @SuppressWarnings("resource")
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setName(key);
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        //初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
        druidDataSource.setInitialSize(50);
        //最大连接池数量
        druidDataSource.setMaxActive(200);
        //获取连接时最大等待时间，单位毫秒。当链接数已经达到了最大链接数的时候，应用如果还要获取链接就会出现等待的现象，等待链接释放并回到链接池，如果等待的时间过长就应该踢掉这个等待，不然应用很可能出现雪崩现象
        druidDataSource.setMaxWait(60000);
        //最小连接池数量
        druidDataSource.setMinIdle(40);
        String validationQuery = "select 1 from dual";
        //申请连接时执行validationQuery检测连接是否有效，这里建议配置为TRUE，防止取到的连接不可用
        druidDataSource.setTestOnBorrow(true);
        //建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        druidDataSource.setTestWhileIdle(true);
        //用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
        druidDataSource.setValidationQuery(validationQuery);
        //属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：监控统计用的filter:stat日志用的filter:log4j防御sql注入的filter:wall
        druidDataSource.setFilters("stat,wall");
        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        //配置一个连接在池中最小生存的时间，单位是毫秒，这里配置为3分钟180000
        druidDataSource.setMinEvictableIdleTimeMillis(180000);
        //打开druid.keepAlive之后，当连接池空闲时，池中的minIdle数量以内的连接，空闲时间超过minEvictableIdleTimeMillis，则会执行keepAlive操作，即执行druid.validationQuery指定的查询SQL，一般为select * from dual，只要minEvictableIdleTimeMillis设置的小于防火墙切断连接时间，就可以保证当连接空闲时自动做保活检测，不会被防火墙切断
        druidDataSource.setKeepAlive(true);
        //是否移除泄露的连接/超过时间限制是否回收。
        druidDataSource.setRemoveAbandoned(true);
        //泄露连接的定义时间(要超过最大事务的处理时间)；单位为秒。这里配置为1小时
        druidDataSource.setRemoveAbandonedTimeout(3600);
        //移除泄露连接发生是是否记录日志
        druidDataSource.setLogAbandoned(true);
        druidDataSource.init();
        this.dynamicTargetDataSources.put(key, druidDataSource);
        // 将map赋值给父类的TargetDataSources
        setTargetDataSources(this.dynamicTargetDataSources);
        // 将TargetDataSources中的连接信息放入resolvedDataSources管理
        super.afterPropertiesSet();
        log.info(key + "数据源初始化成功");
        //log.info(key+"数据源的概况："+druidDataSource.dump());
        return true;
      } catch (Exception e) {
        log.error(e + "");
        return false;
      }
    }
    return false;
  }

  /**
   * 删除数据源
   */
  public boolean delDataSources(String dataSourceId) {
    Map<Object, Object> dynamicTargetDataSources2 = this.dynamicTargetDataSources;
    if (dynamicTargetDataSources2.containsKey(dataSourceId)) {
      Set<DruidDataSource> druidDataSourceInstances = DruidDataSourceStatManager
          .getDruidDataSourceInstances();
      for (DruidDataSource l : druidDataSourceInstances) {
        if (dataSourceId.equals(l.getName())) {
          dynamicTargetDataSources2.remove(dataSourceId);
          DruidDataSourceStatManager.removeDataSource(l);
          // 将map赋值给父类的TargetDataSources
          setTargetDataSources(dynamicTargetDataSources2);
          // 将TargetDataSources中的连接信息放入resolvedDataSources管理
          super.afterPropertiesSet();
          return true;
        }
      }
      return false;
    } else {
      return false;
    }
  }

  /**
   * 测试数据源连接是否有效
   */
  public boolean testDatasource(String key, String driverClassName, String url, String username,
      String password) {
    try {
      Class.forName(driverClassName);
      DriverManager.getConnection(url, username, password);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
    super.setDefaultTargetDataSource(defaultTargetDataSource);
    this.dynamicDefaultTargetDataSource = defaultTargetDataSource;
  }

  /**
   * @param debug the debug to set
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  /**
   * @return the debug
   */
  public boolean isDebug() {
    return debug;
  }

  /**
   * @return the dynamicTargetDataSources
   */
  public Map<Object, Object> getDynamicTargetDataSources() {
    return dynamicTargetDataSources;
  }

  /**
   * @param dynamicTargetDataSources the dynamicTargetDataSources to set
   */
  public void setDynamicTargetDataSources(Map<Object, Object> dynamicTargetDataSources) {
    this.dynamicTargetDataSources = dynamicTargetDataSources;
  }

  /**
   * @return the dynamicDefaultTargetDataSource
   */
  public Object getDynamicDefaultTargetDataSource() {
    return dynamicDefaultTargetDataSource;
  }

  /**
   * @param dynamicDefaultTargetDataSource the dynamicDefaultTargetDataSource to set
   */
  public void setDynamicDefaultTargetDataSource(Object dynamicDefaultTargetDataSource) {
    this.dynamicDefaultTargetDataSource = dynamicDefaultTargetDataSource;
  }

  public void createDataSourceWithCheck(DatabaseSource databaseSource) throws Exception {
    String databaseSourceName = databaseSource.getDatabaseSourceName();
    log.info("正在检查数据源：" + databaseSourceName);
    Map<Object, Object> dynamicTargetDataSources2 = this.dynamicTargetDataSources;
    if (dynamicTargetDataSources2.containsKey(databaseSourceName)) {
      log.info("数据源" + databaseSourceName + "之前已经创建，准备测试数据源是否正常...");
      DruidDataSource druidDataSource = (DruidDataSource) dynamicTargetDataSources2
          .get(databaseSourceName);
      boolean rightFlag = true;
      Connection connection = null;
      try {
        log.info(databaseSourceName + "数据源的概况->当前闲置连接数：" + druidDataSource.getPoolingCount());
        long activeCount = druidDataSource.getActiveCount();
        log.info(databaseSourceName + "数据源的概况->当前活动连接数：" + activeCount);
        if (activeCount > 0) {
          log.info(
              databaseSourceName + "数据源的概况->活跃连接堆栈信息：" + druidDataSource
                  .getActiveConnectionStackTrace());
        }
        log.info("准备获取数据库连接...");
        connection = druidDataSource.getConnection();
        log.info("数据源" + databaseSourceName + "正常");
      } catch (Exception e) {
        //把异常信息打印到日志文件
        log.error(e.getMessage(), e);
        rightFlag = false;
        log.info("缓存数据源" + databaseSourceName + "已失效，准备删除...");
        if (delDataSources(databaseSourceName)) {
          log.info("缓存数据源删除成功");
        } else {
          log.info("缓存数据源删除失败");
        }
      } finally {
        if (null != connection) {
          connection.close();
        }
      }
      if (rightFlag) {
        log.info("不需要重新创建数据源");
        return;
      } else {
        log.info("准备重新创建数据源...");
        createDataSource(databaseSource);
        log.info("重新创建数据源完成");
      }
    } else {
      createDataSource(databaseSource);
    }

  }

  /**
   * 创建数据源
   */
  private void createDataSource(DatabaseSource databaseSource) throws Exception {
    String databaseSourceName = databaseSource.getDatabaseSourceName();
    log.info("准备创建数据源" + databaseSourceName);
    String username = databaseSource.getUserName();
    String password = databaseSource.getPassWord();
    String url = databaseSource.getUrl();
    String driveClass = databaseSource.getDriverClassName();
    if (testDatasource(databaseSourceName, driveClass, url, username, password)) {
      boolean result = this
          .createDataSource(databaseSourceName, driveClass, url, username, password);
      if (!result) {
        log.error("数据源" + databaseSourceName + "配置正确，但是创建失败");
      }
    } else {
      log.error("数据源配置有错误");
    }
  }


}
