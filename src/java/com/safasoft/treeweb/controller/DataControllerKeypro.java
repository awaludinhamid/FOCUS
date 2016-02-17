/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.controller;

import com.safasoft.treeweb.bean.support.ColumnProp;
import com.safasoft.treeweb.bean.support.ListKpiKeypro;
import com.safasoft.treeweb.bean.support.KpiColumn;
import com.safasoft.treeweb.bean.support.ListKpiColumn;
import com.safasoft.treeweb.bean.support.ListKpiHieKeypro;
import com.safasoft.treeweb.bean.support.ListProp;
import com.safasoft.treeweb.bean.support.SubmitJobContent;
import com.safasoft.treeweb.bean.support.TableContentKeypro;
import com.safasoft.treeweb.service.FfSubmitJobService;
import com.safasoft.treeweb.service.SupportService;
import com.safasoft.treeweb.util.SessionUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Data controller on KEYPRO project, requested via AJAX
 * Handles and retrieves the data page depending on the URI template
 * A user must be log-in first he can access these pages
 * @created Dec 9, 2015
 * @author awal
 */
@Controller
@RequestMapping("/keypro/data")
public class DataControllerKeypro {

  protected static Logger logger = Logger.getLogger("controller");
  private static final String COLUMN_DELIMITER = ","; //default column delimiter
  private static final int MAX_COLUMN_DATA = 60; //maximum column data amount
  
  /**
   * Generate requested KPI data
   * @param parentKpi
   * @return kpi data through json
   */
  @RequestMapping(value="/kpi", method=RequestMethod.GET)
  public @ResponseBody ListKpiHieKeypro getListKpi(
          @RequestParam("parentKpi") Integer parentKpi
    ) {
    logger.debug("Received request to get KPI data");
    List<ListKpiHieKeypro> listJson = new ArrayList<ListKpiHieKeypro>();
    //retrieve data and populate them into returned object
    try {
      SupportService supportServ =
               new SessionUtil<SupportService>().getAppContext("supportService");
      //List<ListKpiKeypro> listKpi = supportServ.getListKpi(parentKpi);
      for(ListKpiKeypro kn : supportServ.getListKpi(parentKpi)) {
        ListKpiHieKeypro lkh = new ListKpiHieKeypro();
        lkh.setKpiId(kn.getKpiId());
        lkh.setParentId(kn.getParentId());
        lkh.setKpiName(kn.getKpiName());
        lkh.setFactName(kn.getFactName());
        lkh.setColor(kn.getColor());
        List<Integer> listJsonDel = new ArrayList<Integer>();
        List<ListKpiHieKeypro> jsonArray = new ArrayList<ListKpiHieKeypro>();
        for(int idxJson = 0; idxJson < listJson.size(); idxJson++) {
          ListKpiHieKeypro json = listJson.get(idxJson);
          if(kn.getKpiId() == json.getParentId()) {
            jsonArray.add(json);
            listJsonDel.add(idxJson);
          }
        }
        for(int idxJson = listJsonDel.size()-1; idxJson >= 0; idxJson--) {
          Integer intJson = listJsonDel.get(idxJson);
          listJson.remove(intJson.intValue());
        }
        if(jsonArray.size() > 0) {
          lkh.setChildren(jsonArray);
        }
        listJson.add(lkh);
       }
    } catch(Exception x) {
      logger.error(x);
    }
    //only last element would be returned
    return listJson.get(listJson.size()-1);
  }
  
  /**
   * Generate list of KPI column option
   * @param kpiId
   * @param pageNo
   * @return list of KPI column option
   */
  @RequestMapping(value="/kpicolumn", method=RequestMethod.GET)
  public @ResponseBody List<ListKpiColumn> getListKpiColumn(
          @RequestParam("kpiId") Integer kpiId,
          @RequestParam("pageNo") Integer pageNo) {
    logger.debug("Received request to get KPI column option");
    List<ListKpiColumn> listKpiColumns = new ArrayList<ListKpiColumn>();
    SupportService suppServ =
            new SessionUtil<SupportService>().getAppContext("supportService");
    //set KPI column value
    for (KpiColumn kc : suppServ.getByKpiId(kpiId)) {
      ListKpiColumn lkc = new ListKpiColumn();
      ListProp lp = new ListProp();
      lkc.setId(kc.getId());
      lkc.setName(kc.getName());
      lkc.setCode(kc.getCode());
      lkc.setType(kc.getType());
      lkc.setDataType(kc.getDataType());
      lp.setRecordCount(suppServ.getRecordCountList(kc.getListQuery(),"",""));
      lp.setListOptions(suppServ.getListOption(kc.getListQuery(),pageNo));
      lkc.setListProp(lp);
      listKpiColumns.add(lkc);
    }
    return listKpiColumns;
  }
  
  /**
   * Generate table content of column list
   * @param tableName
   * @param whereClause
   * @param pageNo
   * @return table content object
   */
  @RequestMapping(value="/content", method=RequestMethod.POST)
  public @ResponseBody TableContentKeypro getListColumn(
          @RequestParam("tableName") String tableName,
          @RequestParam("whereClause") String whereClause,
          @RequestParam("pageNo") Integer pageNo) {
    logger.debug("Received request to get table content");
    //generate column select statement, order by statement (limited to 5 columns only)
    //tranform each column type into character type before execution
    StringBuilder sbColumnSelect = new StringBuilder();
    int orderByLimit = 5;
    StringBuilder sbOrderBy = new StringBuilder();
    SupportService suppServ =
            new SessionUtil<SupportService>().getAppContext("supportService");
    List<ColumnProp> listColumn = suppServ.getListColumn(tableName);
    for(int idx = 0; idx < orderByLimit; idx++)
      sbOrderBy.append(listColumn.get(idx).getColumnName()).append(COLUMN_DELIMITER);
    for(int idx = 0; idx < listColumn.size(); idx++) {
      String columnName;
      if(listColumn.get(idx).getDataType().equals("DATE"))
        columnName = "TO_CHAR("+listColumn.get(idx).getColumnName()+",'DD-MON-YYYY')";
      else if(listColumn.get(idx).getDataType().equals("NUMBER"))
        columnName = "TO_CHAR("+listColumn.get(idx).getColumnName()+")";
      else
        columnName = listColumn.get(idx).getColumnName();
      sbColumnSelect.append(columnName).append(" col").append(idx+1).append(COLUMN_DELIMITER);
    }
    for(int idx = listColumn.size(); idx < MAX_COLUMN_DATA; idx++)
      sbColumnSelect.append("NULL col").append(idx+1).append(COLUMN_DELIMITER); 
    sbColumnSelect.deleteCharAt(sbColumnSelect.lastIndexOf(COLUMN_DELIMITER));
    sbOrderBy.deleteCharAt(sbOrderBy.lastIndexOf(COLUMN_DELIMITER));
    //set table content
    TableContentKeypro tc = new TableContentKeypro();
    tc.setColumns(listColumn);
    tc.setData(suppServ.getListTableValue(tableName,
            sbColumnSelect.toString(),
            whereClause,
            sbOrderBy.toString(),
            pageNo));
    tc.setRecordCount(suppServ.getRecordCount(tableName,whereClause));
    tc.setOrderBy(sbOrderBy.toString());
    return tc;
  }
  
  /**
   * Generate submit job table content from database
   * @param userName
   * @param pageNo
   * @return submit job table content
   */
  @RequestMapping(value="/submitjob", method=RequestMethod.GET)
  public @ResponseBody SubmitJobContent getListSubmitJob(
          @RequestParam("userName") String userName,
          @RequestParam("pageNo") Integer pageNo
          ) {
    logger.debug("Received request to get submit job table content");
    //retrieve and set submit job data
    SubmitJobContent sjc = new SubmitJobContent();
    FfSubmitJobService fsjServ =
            new SessionUtil<FfSubmitJobService>().getAppContext("ffSubmitJobService");
    sjc.setData(fsjServ.getByUser(userName,pageNo));
    sjc.setRecordCount(fsjServ.countByUser(userName));
    return sjc;
  }
  
  /**
   * Execute process through database job scheduler
   * @param userName
   * @param fileName
   * @param query
   * @param columns
   * @param receipt
   * @return job id
   */
  @RequestMapping(value="/execjob", method=RequestMethod.GET)
  public @ResponseBody String execSubmitJob(
          @RequestParam("userName") String userName,
          @RequestParam("fileName") String fileName,
          @RequestParam("query") String query,
          @RequestParam("columns") String columns,
          @RequestParam("receipt") String receipt
          ) {
    logger.debug("Received request to execute proses via job");
    //execute job and retrieve job id
    SupportService suppServ =
            new SessionUtil<SupportService>().getAppContext("supportService");
    FfSubmitJobService fsjServ =
            new SessionUtil<FfSubmitJobService>().getAppContext("ffSubmitJobService");
    suppServ.execSubmitJob(userName, fileName, query, columns, receipt);
    return fsjServ.getJobByUserAndFile(userName, fileName);
  }
  
  /**
   * Generate list of searched option data
   * @param columnCode
   * @param pageNo
   * @param searchText
   * @param excludeText
   * @return list of searched option data
   */
  @RequestMapping(value="/listoption", method=RequestMethod.GET)
  public @ResponseBody ListProp getListProp(
          @RequestParam("columnCode") String columnCode,
          @RequestParam("pageNo") Integer pageNo,
          @RequestParam("searchText") String searchText,
          @RequestParam("excludeText") String excludeText) {
    logger.debug("Received request to get searched option data");
    //retrieve and set searched option data
    SupportService suppServ =
            new SessionUtil<SupportService>().getAppContext("supportService");
    ListProp lp = new ListProp();
    String sql = suppServ.getListQueryByColumn(columnCode);
    lp.setRecordCount(suppServ.getRecordCountList(sql,searchText,excludeText));
    lp.setListOptions(suppServ.getListOption(sql, pageNo, searchText, excludeText));
    return lp;
  }
}
