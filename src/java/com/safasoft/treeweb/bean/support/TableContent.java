/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

import java.util.List;
import java.util.Map;

/**
 * Table content store such a value, column, etc
 * @created Sep 4, 2015
 * @author awal
 */
public class TableContent {

  private List<TableValue> contents;
  private List<ColumnProp> columns;
  private int maxPage;
  private int maxId;
  private Map<String,List<ListBean>> dropDownList;
  private Map<String,List<ColumnCons>> columnsCons;
  private String columnsSerial;
  private String columnsSerialExt;
  private String orderByColumn;
  private String idColumn;

  /**
   * @return the contents
   */
  public List<TableValue> getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(List<TableValue> contents) {
    this.contents = contents;
  }

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
   * @return the maxPage
   */
  public int getMaxPage() {
    return maxPage;
  }

  /**
   * @param maxPage the maxPage to set
   */
  public void setMaxPage(int maxPage) {
    this.maxPage = maxPage;
  }

  /**
   * @return the maxId
   */
  public int getMaxId() {
    return maxId;
  }

  /**
   * @param maxId the maxId to set
   */
  public void setMaxId(int maxId) {
    this.maxId = maxId;
  }

  /**
   * @return the dropDownList
   */
  public Map<String,List<ListBean>> getDropDownList() {
    return dropDownList;
  }

  /**
   * @param dropDownList the dropDownList to set
   */
  public void setDropDownList(Map<String,List<ListBean>> dropDownList) {
    this.dropDownList = dropDownList;
  }

  /**
   * @return the columnsCons
   */
  public Map<String,List<ColumnCons>> getColumnsCons() {
    return columnsCons;
  }

  /**
   * @param columnsCons the columnsCons to set
   */
  public void setColumnsCons(Map<String,List<ColumnCons>> columnsCons) {
    this.columnsCons = columnsCons;
  }

  /**
   * @return the columnsSerial
   */
  public String getColumnsSerial() {
    return columnsSerial;
  }

  /**
   * @param columnsSerial the columnsSerial to set
   */
  public void setColumnsSerial(String columnsSerial) {
    this.columnsSerial = columnsSerial;
  }

  /**
   * @return the orderByColumn
   */
  public String getOrderByColumn() {
    return orderByColumn;
  }

  /**
   * @param orderByColumn the orderByColumn to set
   */
  public void setOrderByColumn(String orderByColumn) {
    this.orderByColumn = orderByColumn;
  }

  /**
   * @return the columnsSerialExt
   */
  public String getColumnsSerialExt() {
    return columnsSerialExt;
  }

  /**
   * @param columnsSerialExt the columnsSerialExt to set
   */
  public void setColumnsSerialExt(String columnsSerialExt) {
    this.columnsSerialExt = columnsSerialExt;
  }

  /**
   * @return the idColumn
   */
  public String getIdColumn() {
    return idColumn;
  }

  /**
   * @param idColumn the idColumn to set
   */
  public void setIdColumn(String idColumn) {
    this.idColumn = idColumn;
  }
}
