/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

/**
 * List which store KPI column value with additional hierarchy
 * @created Sep 15, 2015
 * @author awal
 */
public class ListKpiHie extends ListKpi implements Comparable<ListKpiHie> {

  private Object children;
  private String parentName;

  /**
   * @return the children
   */
  public Object getChildren() {
    return children;
  }

  /**
   * @param children the children to set
   */
  public void setChildren(Object children) {
    this.children = children;
  }

  /**
   * @return the parentName
   */
  public String getParentName() {
    return parentName;
  }

  /**
   * @param parentName the parentName to set
   */
  public void setParentName(String parentName) {
    this.parentName = parentName;
  }

  /**
   * Sorting method
   * Added as much as field you want to include as an order by key
   * @param o
   * @return int 1=more, 0=equal, -1=less
   */
  @Override
  public int compareTo(ListKpiHie o) {
    Integer intParent = getParent();
    return intParent.compareTo(o.getParent());
  }
}
