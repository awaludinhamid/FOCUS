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
 * Log Focus table
 * @created Apr 15, 2016
 * @author awal
 */
@Entity
@Table(name="FF_LOG_FOCUS")
public class FfLogFocus implements Serializable {

  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="LOG_FOCUS_SEQ")
  @SequenceGenerator(name="LOG_FOCUS_SEQ",sequenceName="LOG_FOCUS_SEQ",allocationSize=1)
  @Column(name="ID")
  private int id;
  @Column(name="USER_NAME")
  private String userName;
  @Column(name="LOGIN_TIME")
  private String loginTime;
  @Column(name="LOGOUT_TIME")
  private String logoutTime;

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
   * @return the loginTime
   */
  public String getLoginTime() {
    return loginTime;
  }

  /**
   * @param loginTime the loginTime to set
   */
  public void setLoginTime(String loginTime) {
    this.loginTime = loginTime;
  }

  /**
   * @return the logoutTime
   */
  public String getLogoutTime() {
    return logoutTime;
  }

  /**
   * @param logoutTime the logoutTime to set
   */
  public void setLogoutTime(String logoutTime) {
    this.logoutTime = logoutTime;
  }
}
