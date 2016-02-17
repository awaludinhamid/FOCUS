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
 * KPI column store
 * @created Dec 10, 2015
 * @author awal
 */
@Entity
public class KpiColumn implements Serializable {

  @Id
  @Column(name="ID")
  private int id;
  @Column(name="NAME")
  private String name;
  @Column(name="CODE")
  private String code;
  @Column(name="TYPE")
  private String type;
  @Column(name="LIST_QUERY")
  private String listQuery; 
  @Column(name="DATA_TYPE")
  private String dataType; 

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
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
   * @return the listQuery
   */
  public String getListQuery() {
    return listQuery;
  }

  /**
   * @param listQuery the listQuery to set
   */
  public void setListQuery(String listQuery) {
    this.listQuery = listQuery;
  }

  /**
   * @return the dataType
   */
  public String getDataType() {
    return dataType;
  }

  /**
   * @param dataType the dataType to set
   */
  public void setDataType(String dataType) {
    this.dataType = dataType;
  }
}
