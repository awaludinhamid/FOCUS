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
 * List which store KPI column value on KEYPRO project
 * @created Dec 9, 2015
 * @author awal
 */
@Entity
public class ListKpiKeypro implements Serializable {

  @Id
  @Column(name="KPI_ID")
  private int kpiId;
  @Column(name="PARENT_ID")
  private int parentId;
  @Column(name="KPI_NAME")
  private String kpiName;
  @Column(name="FACT_NAME")
  private String factName;
  @Column(name="COLOR")
  private String color;

  /**
   * @return the kpiId
   */
  public int getKpiId() {
    return kpiId;
  }

  /**
   * @param kpiId the kpiId to set
   */
  public void setKpiId(int kpiId) {
    this.kpiId = kpiId;
  }

  /**
   * @return the parentId
   */
  public int getParentId() {
    return parentId;
  }

  /**
   * @param parentId the parentId to set
   */
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  /**
   * @return the kpiName
   */
  public String getKpiName() {
    return kpiName;
  }

  /**
   * @param kpiName the kpiName to set
   */
  public void setKpiName(String kpiName) {
    this.kpiName = kpiName;
  }

  /**
   * @return the factName
   */
  public String getFactName() {
    return factName;
  }

  /**
   * @param factName the factName to set
   */
  public void setFactName(String factName) {
    this.factName = factName;
  }

  /**
   * @return the color
   */
  public String getColor() {
    return color;
  }

  /**
   * @param color the color to set
   */
  public void setColor(String color) {
    this.color = color;
  }
}
