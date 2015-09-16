/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @created Aug 10, 2015
 * @author awal
 */
@Entity
public class UserProfileBean  implements Serializable {

  @Id
  @Column(name="ID")
  private int id;
  @Column(name="USER_NAME")
  private String userName;
  @Column(name="DEPT_CODE")
  private String deptCode;
  @Column(name="DEPT_NAME")
  private String deptName;
  @Column(name="MEMBER_CODE")
  private String memberCode;
  @Column(name="MEMBER_NAME")
  private String memberName;
  @Column(name="MEMBER_LIST_CODE")
  private String memberListCode;
  @Column(name="MEMBER_LIST_NAME")
  private String memberListName;
  @Column(name="PARENT_MEMBER_CODE")
  private String parentMemberCode;

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
   * @return the deptCode
   */
  public String getDeptCode() {
    return deptCode;
  }

  /**
   * @param deptCode the deptCode to set
   */
  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
  }

  /**
   * @return the deptName
   */
  public String getDeptName() {
    return deptName;
  }

  /**
   * @param deptName the deptName to set
   */
  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  /**
   * @return the memberCode
   */
  public String getMemberCode() {
    return memberCode;
  }

  /**
   * @param memberCode the memberCode to set
   */
  public void setMemberCode(String memberCode) {
    this.memberCode = memberCode;
  }

  /**
   * @return the memberName
   */
  public String getMemberName() {
    return memberName;
  }

  /**
   * @param memberName the memberName to set
   */
  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  /**
   * @return the memberListCode
   */
  public String getMemberListCode() {
    return memberListCode;
  }

  /**
   * @param memberListCode the memberListCode to set
   */
  public void setMemberListCode(String memberListCode) {
    this.memberListCode = memberListCode;
  }

  /**
   * @return the memberListName
   */
  public String getMemberListName() {
    return memberListName;
  }

  /**
   * @param memberListName the memberListName to set
   */
  public void setMemberListName(String memberListName) {
    this.memberListName = memberListName;
  }

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
   * @return the parentMemberCode
   */
  public String getParentMemberCode() {
    return parentMemberCode;
  }

  /**
   * @param parentMemberCode the parentMemberCode to set
   */
  public void setParentMemberCode(String parentMemberCode) {
    this.parentMemberCode = parentMemberCode;
  }

}
