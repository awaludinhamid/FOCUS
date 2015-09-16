/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @created Sep 3, 2015
 * @author awal
 */
@Entity
public class ListDataTemp implements Serializable {

  @Id
  @Column(name="ID")
  private String id;
  @Column(name="GROUP_TEXT")
  private String groupText;

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the groupText
   */
  public String getGroupText() {
    return groupText;
  }

  /**
   * @param groupText the groupText to set
   */
  public void setGroupText(String groupText) {
    this.groupText = groupText;
  }
}
