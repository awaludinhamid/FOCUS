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
  private Double target;
  @Column(name="ACTUAL")
  private Double actual;
  @Column(name="LAST_MONTH")
  private Double lastMonth;
  @Column(name="ACHIEVE")
  private Double achieve;
  @Column(name="GROWTH")
  private Double growth;
  @Column(name="BATAS_ATAS")
  private Double batasAtas;
  @Column(name="BATAS_BAWAH")
  private Double batasBawah;
  @Column(name="COLOR")
  private String color;
  @Column(name="DEPT")
  private String dept;
  @Column(name="BUTTON")
  private String button;
  @Column(name="ICON")
  private String icon;
  @Column(name="TYPE_KPI")
  private String typeKpi;
  @Column(name="SATUAN")
  private String satuan;
  @Column(name="DATE_POPULATE")
  private String datePopulate;
  @Column(name="SYSTEM_ID")
  private int systemId;
  
  @Column(name="COLOR_A")
  private String colorA;
  @Column(name="COLOR_B")
  private String colorB;
  @Column(name="COLOR_C")
  private String colorC;
  @Column(name="COLOR_D")
  private String colorD;
  @Column(name="COLOR_E")
  private String colorE;
  @Column(name="COLOR_ICON")
  private String colorIcon;
  @Column(name="COLOR_ICON_IN")
  private String colorIconIn;
  @Column(name="ICON_IN")
  private String iconIn;
  @Column(name="COLOR_SQUARE")
  private String colorSquare;
  @Column(name="COLOR_SQUARE_IN")
  private String colorSquareIn;
  @Column(name="SPEED_A")
  private String speedA;
  @Column(name="SPEED_B")
  private String speedB;
  @Column(name="ACHIEVE_LAST_MONTH")
  private Double achieveLastMonth;
  @Column(name="PROYEKSI")
  private Double proyeksi;
  @Column(name="ACTUAL_BEFORE_YEST")
  private Double actualBeforeYest;
  @Column(name="ACHIEVE_PROYEKSI")
  private Double achieveProyeksi;

  /**
   * @return the ID
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the ID to set
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
   * @param parent the PARENT to set
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
  public Double getTarget() {
    return target;
  }

  /**
   * @param target the target to set
   */
  public void setTarget(Double target) {
    this.target = target;
  }

  /**
   * @return the actual
   */
  public Double getActual() {
    return actual;
  }

  /**
   * @param actual the actual to set
   */
  public void setActual(Double actual) {
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
  public Double getAchieve() {
    return achieve;
  }

  /**
   * @param achieve the achieve to set
   */
  public void setAchieve(Double achieve) {
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
  public Double getLastMonth() {
    return lastMonth;
  }

  /**
   * @param lastMonth the lastMonth to set
   */
  public void setLastMonth(Double lastMonth) {
    this.lastMonth = lastMonth;
  }

  /**
   * @return the growth
   */
  public Double getGrowth() {
    return growth;
  }

  /**
   * @param growth the growth to set
   */
  public void setGrowth(Double growth) {
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

  /**
   * @return the typeKpi
   */
  public String getTypeKpi() {
    return typeKpi;
  }

  /**
   * @param typeKpi the typeKpi to set
   */
  public void setTypeKpi(String typeKpi) {
    this.typeKpi = typeKpi;
  }

  /**
   * @return the satuan
   */
  public String getSatuan() {
    return satuan;
  }

  /**
   * @param satuan the satuan to set
   */
  public void setSatuan(String satuan) {
    this.satuan = satuan;
  }

  /**
   * @return the batasAtas
   */
  public Double getBatasAtas() {
    return batasAtas;
  }

  /**
   * @param batasAtas the batasAtas to set
   */
  public void setBatasAtas(Double batasAtas) {
    this.batasAtas = batasAtas;
  }

  /**
   * @return the batasBawah
   */
  public Double getBatasBawah() {
    return batasBawah;
  }

  /**
   * @param batasBawah the batasBawah to set
   */
  public void setBatasBawah(Double batasBawah) {
    this.batasBawah = batasBawah;
  }

  /**
   * @return the datePopulate
   */
  public String getDatePopulate() {
    return datePopulate;
  }

  /**
   * @param datePopulate the datePopulate to set
   */
  public void setDatePopulate(String datePopulate) {
    this.datePopulate = datePopulate;
  }

  /**
   * @return the systemId
   */
  public int getSystemId() {
    return systemId;
  }

  /**
   * @param systemId the systemId to set
   */
  public void setSystemId(int systemId) {
    this.systemId = systemId;
  }

  /**
   * @return the colorSquare
   */
  public String getColorSquare() {
    return colorSquare;
  }

  /**
   * @param colorSquare the colorSquare to set
   */
  public void setColorSquare(String colorSquare) {
    this.colorSquare = colorSquare;
  }

  /**
   * @return the iconIn
   */
  public String getIconIn() {
    return iconIn;
  }

  /**
   * @param iconIn the iconIn to set
   */
  public void setIconIn(String iconIn) {
    this.iconIn = iconIn;
  }

  /**
   * @return the colorA
   */
  public String getColorA() {
    return colorA;
  }

  /**
   * @param colorA the colorA to set
   */
  public void setColorA(String colorA) {
    this.colorA = colorA;
  }

  /**
   * @return the colorB
   */
  public String getColorB() {
    return colorB;
  }

  /**
   * @param colorB the colorB to set
   */
  public void setColorB(String colorB) {
    this.colorB = colorB;
  }

  /**
   * @return the colorC
   */
  public String getColorC() {
    return colorC;
  }

  /**
   * @param colorC the colorC to set
   */
  public void setColorC(String colorC) {
    this.colorC = colorC;
  }

  /**
   * @return the colorD
   */
  public String getColorD() {
    return colorD;
  }

  /**
   * @param colorD the colorD to set
   */
  public void setColorD(String colorD) {
    this.colorD = colorD;
  }

  /**
   * @return the colorE
   */
  public String getColorE() {
    return colorE;
  }

  /**
   * @param colorE the colorE to set
   */
  public void setColorE(String colorE) {
    this.colorE = colorE;
  }

  /**
   * @return the colorIcon
   */
  public String getColorIcon() {
    return colorIcon;
  }

  /**
   * @param colorIcon the colorIcon to set
   */
  public void setColorIcon(String colorIcon) {
    this.colorIcon = colorIcon;
  }

  /**
   * @return the colorIconIn
   */
  public String getColorIconIn() {
    return colorIconIn;
  }

  /**
   * @param colorIconIn the colorIconIn to set
   */
  public void setColorIconIn(String colorIconIn) {
    this.colorIconIn = colorIconIn;
  }

  /**
   * @return the colorSquareIn
   */
  public String getColorSquareIn() {
    return colorSquareIn;
  }

  /**
   * @param colorSquareIn the colorSquareIn to set
   */
  public void setColorSquareIn(String colorSquareIn) {
    this.colorSquareIn = colorSquareIn;
  }

  /**
   * @return the speedA
   */
  public String getSpeedA() {
    return speedA;
  }

  /**
   * @param speedA the speedA to set
   */
  public void setSpeedA(String speedA) {
    this.speedA = speedA;
  }

  /**
   * @return the speedB
   */
  public String getSpeedB() {
    return speedB;
  }

  /**
   * @param speedB the speedB to set
   */
  public void setSpeedB(String speedB) {
    this.speedB = speedB;
  }

  /**
   * @return the achieveLastMonth
   */
  public Double getAchieveLastMonth() {
    return achieveLastMonth;
  }

  /**
   * @param achieveLastMonth the achieveLastMonth to set
   */
  public void setAchieveLastMonth(Double achieveLastMonth) {
    this.achieveLastMonth = achieveLastMonth;
  }

  /**
   * @return the proyeksi
   */
  public Double getProyeksi() {
    return proyeksi;
  }

  /**
   * @param proyeksi the proyeksi to set
   */
  public void setProyeksi(Double proyeksi) {
    this.proyeksi = proyeksi;
  }

  /**
   * @return the actualBeforeYest
   */
  public Double getActualBeforeYest() {
    return actualBeforeYest;
  }

  /**
   * @param actualBeforeYest the actualBeforeYest to set
   */
  public void setActualBeforeYest(Double actualBeforeYest) {
    this.actualBeforeYest = actualBeforeYest;
  }

  /**
   * @return the achieveProyeksi
   */
  public Double getAchieveProyeksi() {
    return achieveProyeksi;
  }

  /**
   * @param achieveProyeksi the achieveProyeksi to set
   */
  public void setAchieveProyeksi(Double achieveProyeksi) {
    this.achieveProyeksi = achieveProyeksi;
  }

}
