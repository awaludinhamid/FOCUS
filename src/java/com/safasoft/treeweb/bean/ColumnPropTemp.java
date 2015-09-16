/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @created Sep 9, 2015
 * @author awal
 */
@Entity
public class ColumnPropTemp implements Serializable {

  @Id
  @Column(name="COLUMN_ID")
  private int columnId;
  @Column(name="COLUMN_NAME")
  private String columnName;
  @Column(name="DATA_TYPE")
  private String dataType;
  @Column(name="CONSTRAINT_TYPE")
  private String constraintType;
  @Column(name="TABLE_REF")
  private String tableRef;
  @Column(name="COLUMN1_REF")
  private String column1Ref;
  @Column(name="COLUMN2_REF")
  private String column2Ref;

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

  /**
   * @return the constraintType
   */
  public String getConstraintType() {
    return constraintType;
  }

  /**
   * @param constraintType the constraintType to set
   */
  public void setConstraintType(String constraintType) {
    this.constraintType = constraintType;
  }

  /**
   * @return the tableRef
   */
  public String getTableRef() {
    return tableRef;
  }

  /**
   * @param tableRef the tableRef to set
   */
  public void setTableRef(String tableRef) {
    this.tableRef = tableRef;
  }

  /**
   * @return the column1Ref
   */
  public String getColumn1Ref() {
    return column1Ref;
  }

  /**
   * @param column1Ref the column1Ref to set
   */
  public void setColumn1Ref(String column1Ref) {
    this.column1Ref = column1Ref;
  }

  /**
   * @return the column2Ref
   */
  public String getColumn2Ref() {
    return column2Ref;
  }

  /**
   * @param column2Ref the column2Ref to set
   */
  public void setColumn2Ref(String column2Ref) {
    this.column2Ref = column2Ref;
  }

}
