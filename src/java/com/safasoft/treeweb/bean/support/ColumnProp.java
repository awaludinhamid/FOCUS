/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Column property store such a id, name, etc
 * @created Sep 9, 2015
 * @author awal
 */
@Entity
public class ColumnProp implements Serializable {

  @Id
  @Column(name="COLUMN_ID")
  private int columnId;
  @Column(name="COLUMN_NAME")
  private String columnName;
  @Column(name="DATA_TYPE")
  private String dataType;

  /**
   * @return the columnId
   */
  public int getColumnId() {
    return columnId;
  }

  /**
   * @param columnId the columnId to set
   */
  public void setColumnId(int columnId) {
    this.columnId = columnId;
  }

  /**
   * @return the columnName
   */
  public String getColumnName() {
    return columnName;
  }

  /**
   * @param columnName the columnName to set
   */
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  /**
   * @return the dataType
   */
  public String getDataType() {
    return dataType;
  }

  /**
   * @param dataType the dataType to set
   */
  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

}
