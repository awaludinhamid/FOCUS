/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

import com.safasoft.treeweb.bean.FfSubmitJob;
import java.util.List;

/**
 * List of submit job content (data and property)
 * @created Dec 14, 2015
 * @author awal
 */
public class SubmitJobContent {

  private int recordCount;
  private List<FfSubmitJob> data;

  /**
   * @return the recordCount
   */
  public int getRecordCount() {
    return recordCount;
  }

  /**
   * @param recordCount the recordCount to set
   */
  public void setRecordCount(int recordCount) {
    this.recordCount = recordCount;
  }

  /**
   * @return the data
   */
  public List<FfSubmitJob> getData() {
    return data;
  }

  /**
   * @param data the data to set
   */
  public void setData(List<FfSubmitJob> data) {
    this.data = data;
  }
}
