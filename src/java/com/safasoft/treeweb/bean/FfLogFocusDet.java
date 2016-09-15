/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Log Focus Detail table
 * @created Apr 15, 2016
 * @author awal
 */
@Entity
@Table(name="FF_LOG_FOCUS_DET")
public class FfLogFocusDet implements Serializable {

  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="LOG_FOCUS_DET_SEQ")
  @SequenceGenerator(name="LOG_FOCUS_DET_SEQ",sequenceName="LOG_FOCUS_DET_SEQ",allocationSize=1)
  @Column(name="ID")
  private int id;
  @Column(name="PARENT_ID")
  private int parentId;
  @Column(name="DTS_CODE")
  private String dtsCode;
  @Column(name="LAYER_CODE")
  private String layerCode;
  @Column(name="NODE_CODE")
  private String nodeCode;
  @Column(name="COY_ID")
  private String coyId;
  @Column(name="BUSS_UNIT")
  private String bussUnit;
  @Column(name="KPI_ID")
  private int kpiId;
  @Column(name="SUCCESS_FLAG")
  private String successFlag = "Y";
  @Column(name="LOGGING_TIME")
  private String loggingTime;
  @Column(name="APPS_NAME")
  private String appsName;

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
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
   * @return the successFlag
   */
  public String getSuccessFlag() {
    return successFlag;
  }

  /**
   * @param successFlag the successFlag to set
   */
  public void setSuccessFlag(String successFlag) {
    this.successFlag = successFlag;
  }

  /**
   * @return the loggingTime
   */
  public String getLoggingTime() {
    return loggingTime;
  }

  /**
   * @param loggingTime the loggingTime to set
   */
  public void setLoggingTime(String loggingTime) {
    this.loggingTime = loggingTime;
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
   * @return the layerCode
   */
  public String getLayerCode() {
    return layerCode;
  }

  /**
   * @param layerCode the layerCode to set
   */
  public void setLayerCode(String layerCode) {
    this.layerCode = layerCode;
  }

  /**
   * @return the nodeCode
   */
  public String getNodeCode() {
    return nodeCode;
  }

  /**
   * @param nodeCode the nodeCode to set
   */
  public void setNodeCode(String nodeCode) {
    this.nodeCode = nodeCode;
  }

  /**
   * @return the coyId
   */
  public String getCoyId() {
    return coyId;
  }

  /**
   * @param coyId the coyId to set
   */
  public void setCoyId(String coyId) {
    this.coyId = coyId;
  }

  /**
   * @return the bussUnit
   */
  public String getBussUnit() {
    return bussUnit;
  }

  /**
   * @param bussUnit the bussUnit to set
   */
  public void setBussUnit(String bussUnit) {
    this.bussUnit = bussUnit;
  }

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
   * @return the appsName
   */
  public String getAppsName() {
    return appsName;
  }

  /**
   * @param appsName the appsName to set
   */
  public void setAppsName(String appsName) {
    this.appsName = appsName;
  }
}
