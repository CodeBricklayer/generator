package com.tanp.generator.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.tanp.generator.common.multidatasource.DynamicDataSource;
import com.tanp.generator.entity.DatabaseSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author CodeBricklayer
 * @date 2020/1/3 15:42
 * @description TODO
 */
@Configuration
@EnableTransactionManagement
@Slf4j
@Data
public class DruidConfig {

  /**
   * 主库连接信息
   */
  @Value("${spring.datasource.druid.url}")
  private String url;
  @Value("${spring.datasource.druid.username}")
  private String username;
  @Value("${spring.datasource.druid.password}")
  private String password;
  @Value("${spring.datasource.druid.driver-class-name}")
  private String driverClassName;
  /**
   * 连接池连接信息
   */
  @Value("${spring.datasource.druid.initialSize}")
  private int initialSize;
  @Value("${spring.datasource.druid.minIdle}")
  private int minIdle;
  @Value("${spring.datasource.druid.maxActive}")
  private int maxActive;
  @Value("${spring.datasource.druid.maxWait}")
  private int maxWait;
  @Value("${spring.datasource.druid.validationQuery}")
  private String validationQuery;
  @Value("${spring.datasource.druid.testWhileIdle}")
  private boolean testWhileIdle;
  @Value("${spring.datasource.druid.testOnBorrow}")
  private boolean testOnBorrow;
  @Value("${spring.datasource.druid.testOnReturn}")
  private boolean testOnReturn;

  /**
   * @Bean 声明其为Bean实例
   * @Primary 在同样的DataSource中，首先使用被标注的DataSource
   */
  @Bean
  @Primary
  @Qualifier("mainDataSource")
  public DataSource dataSource() throws SQLException {
    DruidDataSource datasource = new DruidDataSource();
    // 基础连接信息
    datasource.setUrl(this.url);
    datasource.setUsername(username);
    datasource.setPassword(password);
    datasource.setDriverClassName(driverClassName);
    // 连接池连接信息
    datasource.setInitialSize(initialSize);
    datasource.setMinIdle(minIdle);
    datasource.setMaxActive(maxActive);
    datasource.setMaxWait(maxWait);
    //是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
    datasource.setPoolPreparedStatements(true);
    datasource.setMaxPoolPreparedStatementPerConnectionSize(50);
    //对于耗时长的查询sql，会受限于ReadTimeout的控制，单位毫秒
    //  datasource.setConnectionProperties("oracle.net.CONNECT_TIMEOUT=6000;oracle.jdbc.ReadTimeout=60000");
    //对于耗时长的查询sql，会受限于ReadTimeout的控制，单位毫秒
    datasource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
    //申请连接时执行validationQuery检测连接是否有效，这里建议配置为TRUE，防止取到的连接不可用
    datasource.setTestOnBorrow(testOnBorrow);
    //建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
    datasource.setTestWhileIdle(testWhileIdle);
    //归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
    datasource.setTestOnReturn(testOnReturn);
    //用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
    datasource.setValidationQuery(validationQuery);
    //属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：监控统计用的filter:stat日志用的filter:log4j防御sql注入的filter:wall
    datasource.setFilters("stat,wall");
    //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    datasource.setTimeBetweenEvictionRunsMillis(60000);
    //配置一个连接在池中最小生存的时间，单位是毫秒，这里配置为3分钟180000
    datasource.setMinEvictableIdleTimeMillis(180000);
    //打开druid.keepAlive之后，当连接池空闲时，池中的minIdle数量以内的连接，空闲时间超过minEvictableIdleTimeMillis，则会执行keepAlive操作，即执行druid.validationQuery指定的查询SQL，一般为select * from dual，只要minEvictableIdleTimeMillis设置的小于防火墙切断连接时间，就可以保证当连接空闲时自动做保活检测，不会被防火墙切断
    datasource.setKeepAlive(true);
    //是否移除泄露的连接/超过时间限制是否回收。
    datasource.setRemoveAbandoned(true);
    //泄露连接的定义时间(要超过最大事务的处理时间)；单位为秒。这里配置为1小时
    datasource.setRemoveAbandonedTimeout(3600);
    //移除泄露连接发生是是否记录日志
    datasource.setLogAbandoned(true);
    return datasource;
  }

  /**
   * 注册一个StatViewServlet    druid监控页面配置1-帐号密码配置
   *
   * @return servlet registration bean
   */
  @Bean
  public ServletRegistrationBean druidStatViewServlet() {
    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
        new StatViewServlet(), "/druid/*");
    servletRegistrationBean.addInitParameter("loginUsername", "admin");
    servletRegistrationBean.addInitParameter("loginPassword", "123456");
    servletRegistrationBean.addInitParameter("resetEnable", "false");
    return servletRegistrationBean;
  }

  /**
   * 注册一个：filterRegistrationBean   druid监控页面配置2-允许页面正常浏览
   *
   * @return filter registration bean
   */
  @Bean
  public FilterRegistrationBean druidStatFilter() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(
        new WebStatFilter());
    // 添加过滤规则.
    filterRegistrationBean.addUrlPatterns("/*");
    // 添加不需要忽略的格式信息.
    filterRegistrationBean
        .addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
    return filterRegistrationBean;
  }

  @Bean(name = "dynamicDataSource")
  @Qualifier("dynamicDataSource")
  public DynamicDataSource dynamicDataSource() throws SQLException {
    DynamicDataSource dynamicDataSource = new DynamicDataSource();
    dynamicDataSource.setDebug(false);
    //配置缺省的数据源
    // 默认数据源配置 DefaultTargetDataSource
    dynamicDataSource.setDefaultTargetDataSource(dataSource());
    Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
    //额外数据源配置 TargetDataSources
    targetDataSources.put("mainDataSource", dataSource());
    dynamicDataSource.setTargetDataSources(targetDataSources);
    return dynamicDataSource;
  }

  @Bean
  public MybatisSqlSessionFactoryBean sqlSessionFactory() throws Exception {
    MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dynamicDataSource());
    //解决手动创建数据源后字段到bean属性名驼峰命名转换失效的问题
    sqlSessionFactoryBean.setConfiguration(configuration());

    // 设置mybatis的主配置文件
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    // Resource mybatisConfigXml = resolver.getResource("classpath:mybatis/mybatis-config.xml");
    //  sqlSessionFactoryBean.setConfigLocation(mybatisConfigXml);
    // 设置别名包
    //  sqlSessionFactoryBean.setTypeAliasesPackage("com.testdb.dbsource.pojo");
    //就是这句代码，只能指定单个mapper.xml文件，加通配符的话找不到文件
    sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
    //设置分页
    sqlSessionFactoryBean.setPlugins(paginationInterceptor());
    return sqlSessionFactoryBean;
  }

  /**
   * 分页插件
   *
   * @return
   */
  @Bean
  public PaginationInterceptor paginationInterceptor() {
    return new PaginationInterceptor();
  }

  /**
   * 读取驼峰命名设置
   *
   * @return
   */
  @Bean
  @ConfigurationProperties(prefix = "mybatis.configuration")
  public MybatisConfiguration configuration() {
    return new MybatisConfiguration();
  }

  /**
   * 使主数据源保持一致
   *
   * @return
   */
  @Bean
  public DatabaseSource getDataBaseSource() {
    DatabaseSource databaseSource = new DatabaseSource();
    databaseSource.setDatabaseSourceName("mainDataSource");
    databaseSource.setUrl(url);
    databaseSource.setUserName(username);
    databaseSource.setPassWord(password);
    databaseSource.setDatabaseType(0);
    databaseSource.setDriverClassName(driverClassName);
    return databaseSource;
  }

}
