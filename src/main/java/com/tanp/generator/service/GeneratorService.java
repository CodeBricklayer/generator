package com.tanp.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanp.generator.entity.Table;
import java.util.List;
import java.util.Map;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 15:33
 */

public interface GeneratorService {

  IPage<Table> queryList(IPage page,String tableName);

  Map<String, String> queryTable(String tableName);

  List<Map<String, String>> queryColumns(String tableName);
}
