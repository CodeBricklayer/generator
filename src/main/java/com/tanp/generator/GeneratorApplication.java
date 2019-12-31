package com.tanp.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 10:47
 * @description TODO
 */
@SpringBootApplication
@MapperScan("com.tanp.generator.mapper")
public class GeneratorApplication {

  public static void main(String[] args) {
    SpringApplication.run(GeneratorApplication.class, args);
  }
}
