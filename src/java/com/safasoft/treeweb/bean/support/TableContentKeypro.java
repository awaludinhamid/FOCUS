/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

import java.util.List;

/**
 * Table content store such a value, column, etc on KEYPRO project
 * @created Dec 11, 2015
 * @author awal
 */
public class TableContentKeypro {

  private Integer recordCount;
  private String orderBy;
  private List<ColumnProp> columns;
  private List<TableValueKeypro> data;

  /**
   * @return the columns
   */
  public List<ColumnProp> getColumns() {
    return columns;
  }

  /**
   * @param columns the columns to set
   */
  public void setColumns(List<ColumnProp> columns) {
    this.columns = columns;
  }

  /**
   * @return the data
   */
  public List<TableValueKeypro> getData() {
    return data;
  }

  /**
   * @param data the data to set
   */
  public void setData(List<TableValueKeypro> data) {
    this.data = data;
  }

  /**
   * @return the recordCount
   */
  public Integer getRecordCount() {
    return recordCount;
  }

  /**
   * @param recordCount the recordCount to set
   */
  public void setRecordCount(Integer recordCount) {
    this.recordCount = recordCount;
  }

  /**
   * @return the orderBy
   */
  public String getOrderBy() {
    return orderBy;
  }

  /**
   * @param orderBy the orderBy to set
   */
  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }
}
