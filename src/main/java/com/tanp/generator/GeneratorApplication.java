package com.tanp.generator;

import com.tanp.generator.common.multidatasource.DruidContextHolder;
import com.tanp.generator.common.multidatasource.DynamicDataSource;
import com.tanp.generator.entity.DatabaseSource;
import com.tanp.generator.service.DatabaseSourceService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 10:47
 * @description TODO
 */
@SpringBootApplication
@MapperScan("com.tanp.generator.mapper")
@Slf4j
public class GeneratorApplication implements CommandLineRunner {

  @Autowired
  private DynamicDataSource dynamicDataSource;
  @Autowired
  private DatabaseSourceService databaseSourceService;

  public static void main(String[] args) {
    SpringApplication.run(GeneratorApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    List<DatabaseSource> databaseSources = databaseSourceService.list();
    for (DatabaseSource databaseSource : databaseSources) {
      try {
        dynamicDataSource.createDataSourceWithCheck(databaseSource);
      } catch (Exception e) {
        log.error("连接数据库异常:{}", e);
      } finally {
        DruidContextHolder.clearDataSource();
      }
    }
  }
}
