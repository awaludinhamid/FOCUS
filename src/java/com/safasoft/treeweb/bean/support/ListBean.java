/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.support;

import com.safasoft.treeweb.bean.pk.ListBeanPk;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * List store which has two info, code as value and name as label
 * @created Sep 23, 2015
 * @author awal
 */
@Entity
@IdClass(ListBeanPk.class)
public class ListBean implements Serializable {

  @Id
  private String code;
  @Id
  private String name;

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code the code to set
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  
}
