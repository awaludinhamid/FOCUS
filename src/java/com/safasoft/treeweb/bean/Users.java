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
 * @created Jun 22, 2015
 * @author awal
 */
@Entity
@Table(name="USERS")
public class Users implements Serializable {

  @Id
  @Column(name="USER_NAME")
  private String userName;

  @Column(name="USER_ACCESS")
  private int userAccess;

  /**
   * @return the userName
   */
  public String getUserName() {
    return userName;
  }

  /**
   * @param username the userName to set
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * @return the userAccess
   */
  public int getUserAccess() {
    return userAccess;
  }

  /**
   * @param access the userAccess to set
   */
  public void setUserAccess(int userAccess) {
    this.userAccess = userAccess;
  }
}
