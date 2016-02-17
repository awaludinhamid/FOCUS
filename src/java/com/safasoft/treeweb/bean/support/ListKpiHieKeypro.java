/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

/**
 * List which store KPI column value with additional hierarchy on KEYPRO project
 * @created Dec 9, 2015
 * @author awal
 */
public class ListKpiHieKeypro extends ListKpiKeypro {

  private Object children;

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
}
