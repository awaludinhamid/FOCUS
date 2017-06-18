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
 * @created Jun 15, 2017
 * @author awal
 */
@Entity
public class UploadTableBean implements Serializable {

  @Id
  @Column(name="ID")
  private int id;
  @Column(name="NAME")
  private String name;
  @Column(name="TABLE_NAME")
  private String tableName;
  @Column(name="DESCRIPTION")
  private String description;
  @Column(name="USER_ACCESS")
  private String userAccess; 
  @Column(name="SCHEMA_NAME")
  private String schemaName; 
  @Column(name="DB_LINK")
  private String dbLink; 

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
   * @return the tableName
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * @param tableName the tableName to set
   */
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the userAccess
   */
  public String getUserAccess() {
    return userAccess;
  }

  /**
   * @param userAccess the userAccess to set
   */
  public void setUserAccess(String userAccess) {
    this.userAccess = userAccess;
  }

  /**
   * @return the schemaName
   */
  public String getSchemaName() {
    return schemaName;
  }

  /**
   * @param schemaName the schemaName to set
   */
  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  /**
   * @return the dbLink
   */
  public String getDbLink() {
    return dbLink;
  }

  /**
   * @param dbLink the dbLink to set
   */
  public void setDbLink(String dbLink) {
    this.dbLink = dbLink;
  }
}
