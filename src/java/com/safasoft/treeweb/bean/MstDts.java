/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Master date time series table
 * @created Aug 26, 2015
 * @author awal
 */
@Entity
@Table(name="MST_DTS")
public class MstDts implements Serializable {

  @Id
  @Column(name="DTS_ID")
  private int dtsId;
  @Column(name="DTS_NAME")
  private String dtsName;
  @Column(name="DTS_CODE")
  private String dtsCode;
  @Column(name="DTS_TABLE")
  private String dtsTable;
  @Column(name="DTS_TABLE_LAST_MONTH")
  private String dtsTableLastMonth;
  @Column(name="DESCRIPTION")
  private String description;

  /**
   * @return the dtsId
   */
  public int getDtsId() {
    return dtsId;
  }

  /**
   * @param dtsId the dtsId to set
   */
  public void setDtsId(int dtsId) {
    this.dtsId = dtsId;
  }

  /**
   * @return the dtsName
   */
  public String getDtsName() {
    return dtsName;
  }

  /**
   * @param dtsName the dtsName to set
   */
  public void setDtsName(String dtsName) {
    this.dtsName = dtsName;
  }

  /**
   * @return the dtsCode
   */
  public String getDtsCode() {
    return dtsCode;
  }

  /**
   * @param dtsCode the dtsCode to set
   */
  public void setDtsCode(String dtsCode) {
    this.dtsCode = dtsCode;
  }

  /**
   * @return the dtsTable
   */
  public String getDtsTable() {
    return dtsTable;
  }

  /**
   * @param dtsTable the dtsTable to set
   */
  public void setDtsTable(String dtsTable) {
    this.dtsTable = dtsTable;
  }

  /**
   * @return the dtsTableLastMonth
   */
  public String getDtsTableLastMonth() {
    return dtsTableLastMonth;
  }

  /**
   * @param dtsTableLastMonth the dtsTableLastMonth to set
   */
  public void setDtsTableLastMonth(String dtsTableLastMonth) {
    this.dtsTableLastMonth = dtsTableLastMonth;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
