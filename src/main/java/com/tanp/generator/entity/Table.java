package com.tanp.generator.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 14:03
 * @description 表数据
 */
@Data
public class Table {

  /**
   * 表的名称
   */
  private String tableName;

  /**
   * 表的备注
   */
  private String comments;

  /**
   * 表的引擎
   */
  private String engine;

  /**
   * 创建日期
   */
  private Date createTime;

  /**
   * 表的主键
   */
  private Column pk;

  /**
   * 表的列名(不包含主键)
   */
  private List<Column> columns;

  /**
   * 类名(第一个字母大写)，如：sys_user => SysUser
   */
  private String className;

  /**
   * 类名(第一个字母小写)，如：sys_user => sysUser
   */
  private String classname;
}
