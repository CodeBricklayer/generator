package com.tanp.generator.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanp.generator.entity.Table;
import com.tanp.generator.mapper.GeneratorMapper;
import com.tanp.generator.service.GeneratorService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 15:33
 */
@Service
public class GeneratorServiceImpl extends ServiceImpl<GeneratorMapper, Table> implements GeneratorService {

  @Autowired
  GeneratorMapper generatorMapper;

  /**
   * 查询表列表
   */
  @Override
  public IPage<Table> queryList(IPage page,String tableName) {
    return page.setRecords(generatorMapper.queryList(page,tableName));
  }

  /**
   * 查询单个表
   */
  @Override
  public Map<String, String> queryTable(String tableName) {
    return generatorMapper.queryTable(tableName);
  }

  /**
   * 查询表字段
   */
  @Override
  public List<Map<String, String>> queryColumns(String tableName) {
    return generatorMapper.queryColumns(tableName);
  }
}
