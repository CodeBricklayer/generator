package com.tanp.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanp.generator.entity.Table;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 15:11
 * @description 数据库接口
 */

@Repository
public interface GeneratorMapper extends BaseMapper<Table> {

  List<Table> queryList(IPage page,@Param("tableName") String tableName);

  Map<String, String> queryTable(String tableName);

  List<Map<String, String>> queryColumns(String tableName);
}
