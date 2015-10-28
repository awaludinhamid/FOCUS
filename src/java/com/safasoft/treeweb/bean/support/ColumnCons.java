/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Column constraint store
 * @created Sep 18, 2015
 * @author awal
 */
@Entity
public class ColumnCons implements Serializable {

  @Id
  @Column(name="CONSTRAINT_NAME")
  private String constraintName;
  @Column(name="CONSTRAINT_TYPE")
  private String constraintType;
  @Column(name="TABLE_REF")
  private String tableRef;
  @Column(name="COLUMN1_REF")
  private String column1Ref;
  @Column(name="COLUMN2_REF")
  private String column2Ref;

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

  /**
   * @return the constraintName
   */
  public String getConstraintName() {
    return constraintName;
  }

  /**
   * @param constraintName the constraintName to set
   */
  public void setConstraintName(String constraintName) {
    this.constraintName = constraintName;
  }

}
