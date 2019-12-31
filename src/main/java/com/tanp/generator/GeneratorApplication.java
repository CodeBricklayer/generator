package com.tanp.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 10:47
 * @description TODO
 */
@MapperScan("com.tanp.generator.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GeneratorApplication {

  public static void main(String[] args) {
    SpringApplication.run(GeneratorApplication.class, args);
  }
}
