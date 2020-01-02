package com.tanp.generator.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.tanp.generator.common.constant.DataSourceType;
import com.tanp.generator.common.multidatasource.DruidProperties;
import com.tanp.generator.common.multidatasource.DynamicDataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 10:53
 * @description 数据源配置
 */

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.tanp.generator.mapper")
@Slf4j
public class MybatisPlusConfig {

  @Bean("firstDataSource")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.druid.first")
  public DataSource firstDataSource(DruidProperties druidProperties) {
    DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
    return druidProperties.dataSource(dataSource);
  }

  @Bean("secondDataSource")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.druid.second")
  @ConditionalOnProperty(prefix = "spring.datasource.druid.second", name = "enabled", havingValue = "true")
  public DataSource secondDataSource(DruidProperties druidProperties) {
    DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
    return druidProperties.dataSource(dataSource);
  }

  @Bean
  public DynamicDataSource dataSource(@Qualifier("firstDataSource") DataSource firstDataSource,
      @Qualifier("secondDataSource") DataSource secondDataSource) {
    Map<Object, Object> map = new HashMap<>();
    map.put(DataSourceType.FIRST.getDb(), firstDataSource);
    map.put(DataSourceType.SECOND.getDb(), secondDataSource);
    try {
      firstDataSource.getConnection();
      log.info("firstDataSource连接成功:{}" + firstDataSource);
      secondDataSource.getConnection();
      log.info("secondDataSource连接成功:{}" + secondDataSource);
    } catch (SQLException e) {
      log.error("数据库连接异常：{}", e);
    }
    DynamicDataSource dynamicDataSource = new DynamicDataSource();
    dynamicDataSource.setTargetDataSources(map);
    dynamicDataSource.setDefaultTargetDataSource(firstDataSource);
    return dynamicDataSource;
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dynamicDataSource);
    //设置mapper.xml的位置路径
    Resource[] resources = new PathMatchingResourcePatternResolver()
        .getResources("classpath:mapper/*.xml");
    factoryBean.setPlugins(this.paginationInterceptor());
    factoryBean.setMapperLocations(resources);
    return factoryBean.getObject();
  }

  @Bean
  public PlatformTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
    return new DataSourceTransactionManager(dynamicDataSource);
  }

  /**
   * 分页插件
   */
  @Bean
  public PaginationInterceptor paginationInterceptor() {
    return new PaginationInterceptor();
  }
}
