/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

import java.util.List;

/**
 * List of option property
 * @created Dec 17, 2015
 * @author awal
 */
public class ListProp {

  private Integer recordCount;
  private List<ListOption> listOptions;

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
   * @return the listOptions
   */
  public List<ListOption> getListOptions() {
    return listOptions;
  }

  /**
   * @param listOptions the listOptions to set
   */
  public void setListOptions(List<ListOption> listOptions) {
    this.listOptions = listOptions;
  }
}
