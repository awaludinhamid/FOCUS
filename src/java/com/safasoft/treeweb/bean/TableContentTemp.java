/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean;

import java.util.List;

/**
 * @created Sep 4, 2015
 * @author awal
 */
public class TableContentTemp {

  private List<ListDataTemp> contents;
  private List<ColumnPropTemp> columns;
  private int maxPage;
  private int maxId;

  /**
   * @return the contents
   */
  public List<ListDataTemp> getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(List<ListDataTemp> contents) {
    this.contents = contents;
  }

  /**
   * @return the columns
   */
  public List<ColumnPropTemp> getColumns() {
    return columns;
  }

  /**
   * @param columns the columns to set
   */
  public void setColumns(List<ColumnPropTemp> columns) {
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
}
