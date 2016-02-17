/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

/**
 * List of KPI column
 * @created Dec 11, 2015
 * @author awal
 */
public class ListKpiColumn extends KpiColumn {
 
  private ListProp listProp;

  /**
   * @return the listProp
   */
  public ListProp getListProp() {
    return listProp;
  }

  /**
   * @param listProp the listProp to set
   */
  public void setListProp(ListProp listProp) {
    this.listProp = listProp;
  }
}
