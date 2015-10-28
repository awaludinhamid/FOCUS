/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.bean.pk;

import java.io.Serializable;
import javax.persistence.Column;

/**
 * Composite key of ListBean
 * @created Oct 13, 2015
 * @author awal
 */
public class ListBeanPk implements Serializable {

  @Column(name="CODE")
  private String code;
  @Column(name="NAME")
  private String name;

  @Override
  public int hashCode() {
      return (int) code.hashCode() + name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
      if(obj == this) return true;
      if(!(obj instanceof ListBeanPk)) return false;
      //if(obj == null) return false;
      ListBeanPk amczevp = (ListBeanPk) obj;
      return amczevp.code.equals(code) && amczevp.name.equals(name);
  }

}
