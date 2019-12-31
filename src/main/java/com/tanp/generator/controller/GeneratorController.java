package com.tanp.generator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanp.generator.common.constant.ResponseResult;
import com.tanp.generator.entity.Table;
import com.tanp.generator.service.impl.GeneratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 15:31
 * @description 代码生成页面接口
 */
@Controller
@RequestMapping("/sys/generator")
public class GeneratorController {
  @Autowired
  private GeneratorServiceImpl generatorCode;

  /**
   * 列表
   */
  @ResponseBody
  @RequestMapping("/list")
  public ResponseResult list( @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
      @RequestParam(value = "tableName", required = false) String tableName){
    Page<Table> page = new Page<>(pageNumber, pageSize);
    IPage<Table> tableList = generatorCode.queryList(page,tableName);
    return ResponseResult.ok().put("page", tableList);
  }

}
