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
 * List which store KPI column value
 * @created Aug 24, 2015
 * @author awal
 */
@Entity
public class ListKpi implements Serializable {

  @Id
  @Column(name="ID")
  private int id;
  @Column(name="PARENT")
  private int parent;
  @Column(name="KPI")
  private String kpi;
  @Column(name="MEMBERS")
  private String members;
  @Column(name="COY")
  private String coy;
  @Column(name="LOB")
  private String lob;
  @Column(name="KPI_TYPE")
  private String kpiType;
  @Column(name="NAME")
  private String name;
  @Column(name="URL")
  private String url;
  @Column(name="TARGET")
  private double target;
  @Column(name="ACTUAL")
  private double actual;
  @Column(name="LAST_MONTH")
  private double lastMonth;
  @Column(name="ACHIEVE")
  private double achieve;
  @Column(name="GROWTH")
  private double growth;
  @Column(name="COLOR")
  private String color;
  @Column(name="DEPT")
  private String dept;
  @Column(name="BUTTON")
  private String button;
  @Column(name="ICON")
  private String icon;

  /**
   * @return the ID
   */
  public int getId() {
    return id;
  }

  /**
   * @param ID the ID to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the PARENT
   */
  public int getParent() {
    return parent;
  }

  /**
   * @param PARENT the PARENT to set
   */
  public void setParent(int parent) {
    this.parent = parent;
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
   * @return the target
   */
  public double getTarget() {
    return target;
  }

  /**
   * @param target the target to set
   */
  public void setTarget(double target) {
    this.target = target;
  }

  /**
   * @return the actual
   */
  public double getActual() {
    return actual;
  }

  /**
   * @param actual the actual to set
   */
  public void setActual(double actual) {
    this.actual = actual;
  }

  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return the achieve
   */
  public double getAchieve() {
    return achieve;
  }

  /**
   * @param achieve the achieve to set
   */
  public void setAchieve(double achieve) {
    this.achieve = achieve;
  }

  /**
   * @return the color
   */
  public String getColor() {
    return color;
  }

  /**
   * @param color the color to set
   */
  public void setColor(String color) {
    this.color = color;
  }

  /**
   * @return the lastMonth
   */
  public double getLastMonth() {
    return lastMonth;
  }

  /**
   * @param last_month the lastMonth to set
   */
  public void setLastMonth(double lastMonth) {
    this.lastMonth = lastMonth;
  }

  /**
   * @return the growth
   */
  public double getGrowth() {
    return growth;
  }

  /**
   * @param growth the growth to set
   */
  public void setGrowth(double growth) {
    this.growth = growth;
  }

  /**
   * @return the kpiType
   */
  public String getKpiType() {
    return kpiType;
  }

  /**
   * @param kpiType the kpiType to set
   */
  public void setKpiType(String kpiType) {
    this.kpiType = kpiType;
  }

  /**
   * @return the kpi
   */
  public String getKpi() {
    return kpi;
  }

  /**
   * @param kpi the kpi to set
   */
  public void setKpi(String kpi) {
    this.kpi = kpi;
  }

  /**
   * @return the members
   */
  public String getMembers() {
    return members;
  }

  /**
   * @param members the members to set
   */
  public void setMembers(String members) {
    this.members = members;
  }

  /**
   * @return the coy
   */
  public String getCoy() {
    return coy;
  }

  /**
   * @param coy the coy to set
   */
  public void setCoy(String coy) {
    this.coy = coy;
  }

  /**
   * @return the lob
   */
  public String getLob() {
    return lob;
  }

  /**
   * @param lob the lob to set
   */
  public void setLob(String lob) {
    this.lob = lob;
  }

  /**
   * @return the dept
   */
  public String getDept() {
    return dept;
  }

  /**
   * @param dept the dept to set
   */
  public void setDept(String dept) {
    this.dept = dept;
  }

  /**
   * @return the button
   */
  public String getButton() {
    return button;
  }

  /**
   * @param button the button to set
   */
  public void setButton(String button) {
    this.button = button;
  }

  /**
   * @return the icon
   */
  public String getIcon() {
    return icon;
  }

  /**
   * @param icon the icon to set
   */
  public void setIcon(String icon) {
    this.icon = icon;
  }

}
