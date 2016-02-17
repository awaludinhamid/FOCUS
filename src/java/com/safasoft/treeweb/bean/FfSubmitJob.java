/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Submit job table
 * @created Dec 12, 2015
 * @author awal
 */
@Entity
@Table(name="FF_SUBMIT_JOB")
public class FfSubmitJob implements Serializable {

  @Id
  @Column(name="ID")
  private int id;
  @Column(name="JOB_ID")
  private int jobId;
  @Column(name="USER_NAME")
  private String userName;
  @Column(name="FILE_NAME")
  private String fileName;
  @Column(name="STATUS")
  private String status;
  @Column(name="STARTING_TIME")
  private String startingTime;
  @Column(name="ENDING_TIME")
  private String endingTime;

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
   * @return the jobId
   */
  public int getJobId() {
    return jobId;
  }

  /**
   * @param jobId the jobId to set
   */
  public void setJobId(int jobId) {
    this.jobId = jobId;
  }

  /**
   * @return the userName
   */
  public String getUserName() {
    return userName;
  }

  /**
   * @param userName the userName to set
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @param fileName the fileName to set
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return the startingTime
   */
  public String getStartingTime() {
    return startingTime;
  }

  /**
   * @param startingTime the startingTime to set
   */
  public void setStartingTime(String startingTime) {
    this.startingTime = startingTime;
  }

  /**
   * @return the endingTime
   */
  public String getEndingTime() {
    return endingTime;
  }

  /**
   * @param endingTime the endingTime to set
   */
  public void setEndingTime(String endingTime) {
    this.endingTime = endingTime;
  }
}
