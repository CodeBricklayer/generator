package com.tanp.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author CodeBricklayer
 * @date 2020/1/3 15:39
 * @description TODO
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DatabaseSource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * 数据源的别名
   */
  private String databaseSourceName;

  /**
   * 连接信息
   */
  private String url;

  /**
   * 用户名
   */
  private String userName;

  /**
   * 密码
   */
  private String passWord;

  /**
   * 数据库驱动
   */
  private String driverClassName;

  /**
   * 数据库类型(0-mysql,1-oracle)
   */
  private Integer databaseType;


}
