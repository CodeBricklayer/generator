package com.tanp.generator.common.constant;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 11:25
 * @description 列出所有数据源
 */
public enum DataSourceType {
  FIRST("first"),
  SECOND("second");
  private String db;

  DataSourceType(String db) {
    this.db = db;
  }

  public String getDb() {
    return db;
  }

  public void setDb(String db) {
    this.db = db;
  }
}
