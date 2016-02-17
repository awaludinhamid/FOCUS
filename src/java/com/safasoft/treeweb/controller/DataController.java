/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.controller;

import com.safasoft.treeweb.bean.support.ListKpi;
import com.safasoft.treeweb.bean.support.ListKpiHie;
import com.safasoft.treeweb.bean.MstDts;
import com.safasoft.treeweb.bean.support.TableContent;
import com.safasoft.treeweb.bean.UserAccess;
import com.safasoft.treeweb.bean.support.ColumnHeader;
import com.safasoft.treeweb.bean.support.ListBean;
import com.safasoft.treeweb.bean.support.TableValue;
import com.safasoft.treeweb.bean.support.UserProfileBean;
import com.safasoft.treeweb.service.MstDtsService;
import com.safasoft.treeweb.service.UserAccessService;
import com.safasoft.treeweb.service.UsersService;
import com.safasoft.treeweb.util.FileUtil;
import com.safasoft.treeweb.util.SessionUtil;
import com.safasoft.treeweb.util.DataConverter;
import com.safasoft.treeweb.util.OutputFileCreator;
import id.co.fif.json.JSONArray;
import id.co.fif.json.JSONException;
import id.co.fif.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Data controller, requested via AJAX
 * Handles and retrieves the data page depending on the URI template
 * A user must be log-in first he can access these pages
 * @created Jun 19, 2015
 * @author awal
 */
@Controller
@RequestMapping("/data")
public class DataController {

 protected static Logger logger = Logger.getLogger("controller");
 private Workbook wb;//excel object store
 private File fileDownload;//downloaded file
 private List<ListKpi> knList;//list of generated KPI
 private ListKpiHie listJsonKpi;//list of generated KPI with hierarchy
 private String jsonName;//json parameter sent by front end apps

 /**
  * Generate requested KPI data
  *
  * @param httpRequest
  * @param httpResponse
  * @return kpi data through json
  */
  @RequestMapping(value = "/kpi", method = RequestMethod.GET)
  public @ResponseBody ListKpiHie getKpiPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
     logger.debug("Received request to get kpi data");
     //no cache applicable
     httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
     httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
     httpResponse.setDateHeader("Expires", 0); // Proxies
     //jsonName template:
     //kpi_[timeseries]_[layer]_[level]_[coy]_[lob]_[kpi_id|null]_[timestamp]
     jsonName = httpRequest.getParameter("jsonName");
     String delimiter = httpRequest.getParameter("delimiter");
     String[] tmpArr = jsonName.split(delimiter);
     List<ListKpiHie> listJson = new ArrayList<ListKpiHie>();
     //retrieve data and populate them into returned object
     try {
       UsersService usersServ =
                new SessionUtil<UsersService>().getAppContext("usersService");
       MstDtsService mdServ =
                new SessionUtil<MstDtsService>().getAppContext("mstDtsService");
       MstDts mstDts = mdServ.getByCode(tmpArr[1]);
       String layerSuffixTable = "";//"_"+tmpArr[2].toUpperCase();
       if(tmpArr.length == 7) {
         knList = usersServ.getListKpi(tmpArr[2],tmpArr[3],tmpArr[4],tmpArr[5],
                 mstDts.getDtsTable()+layerSuffixTable,mstDts.getDtsTableLastMonth()+layerSuffixTable);
       } else {
         knList = usersServ.getListKpi(tmpArr[2],tmpArr[3],tmpArr[4],tmpArr[5],tmpArr[6],
                 mstDts.getDtsTable()+layerSuffixTable,mstDts.getDtsTableLastMonth()+layerSuffixTable);
       }
       for(ListKpi kn : knList) {
        ListKpiHie lkth = new ListKpiHie();
        lkth.setId(kn.getId());
        lkth.setParent(kn.getParent());
        lkth.setKpi(kn.getKpi());
        lkth.setMembers(kn.getMembers());
        lkth.setCoy(kn.getCoy());
        lkth.setLob(kn.getLob());
        lkth.setKpiType(kn.getKpiType());
        lkth.setName(kn.getName());
        lkth.setUrl(kn.getUrl());
        lkth.setTypeKpi(kn.getTypeKpi());
        lkth.setSatuan(kn.getSatuan());
        lkth.setTarget(kn.getTarget());
        lkth.setActual(kn.getActual());
        lkth.setAchieve(kn.getAchieve());
        lkth.setLastMonth(kn.getLastMonth());
        lkth.setGrowth(kn.getGrowth());
        lkth.setBatasAtas(kn.getBatasAtas());
        lkth.setBatasBawah(kn.getBatasBawah());
        lkth.setColor(kn.getColor());
        lkth.setDept(kn.getDept());
        lkth.setButton(kn.getButton());
        lkth.setIcon(kn.getIcon());
        lkth.setDatePopulate(kn.getDatePopulate());
        lkth.setSystemId(kn.getSystemId());
        List<Integer> listJsonDel = new ArrayList<Integer>();
        List<ListKpiHie> jsonArray = new ArrayList<ListKpiHie>();
        for(int idxJson = 0; idxJson < listJson.size(); idxJson++) {
          ListKpiHie json = listJson.get(idxJson);
          if(kn.getId() == json.getParent()) {
            json.setParentName(kn.getName());
            jsonArray.add(json);
            listJsonDel.add(idxJson);
          }
        }
        for(int idxJson = listJsonDel.size()-1; idxJson >= 0; idxJson--) {
          Integer intJson = listJsonDel.get(idxJson);
          listJson.remove(intJson.intValue());
        }
        if(jsonArray.size() > 0) {
          lkth.setChildren(jsonArray);
        }
        listJson.add(lkth);
       }
    } catch(Exception x) {
      logger.error(x);
    }
    //only last element would be returned
    listJsonKpi = listJson.get(listJson.size()-1);
    return listJsonKpi;
  }

 /**
  * Generate user profile data
  *
  * @param httpRequest
  * @return json data
  */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public @ResponseBody List<UserProfileBean> getAppTempPage(HttpServletRequest httpRequest) {
     logger.debug("Received request to get profile data");
     //grab current user
     String user = httpRequest.getUserPrincipal().getName();
     //retrieve his profile
     return new SessionUtil<UsersService>().getAppContext("usersService").getUserProfile(user);
 }

  /**
  * Generate user access data
  *
  * @return json data
  */
  @RequestMapping(value = "/useraccess", method = RequestMethod.GET)
  public @ResponseBody List<UserAccess> getUserAccessList() {
     logger.debug("Received request to get user access");
    //retrieve data
    return new SessionUtil<UserAccessService>().getAppContext("userAccessService").getAll();
  }

  /**
  * Generate table content by given table and page no
  *
  * @param httpRequest
  * @return json data
  */
  @RequestMapping(value = "/table", method = RequestMethod.GET)
  public @ResponseBody TableContent getTableContentByPage(HttpServletRequest httpRequest) {
    logger.debug("Received request to get table content");
    //grab current table and page no
    String tableName = httpRequest.getParameter("tableName");
    int pageNo = Integer.parseInt(httpRequest.getParameter("pageNo"));
    //retrieve table content
    return new SessionUtil<UsersService>().getAppContext("usersService").getListTableContentByPage(tableName, pageNo);
  }

  /**
  * Generate table value
  *
  * @param httpRequest
  * @return json data
  */
  @RequestMapping(value = "/table/value", method = RequestMethod.GET)
  public @ResponseBody List<TableValue> getTableValue(HttpServletRequest httpRequest) {
    logger.debug("Received request to get table value");
    //grab table, columns, and order by clause
    String tableName = httpRequest.getParameter("tableName");
    String columnsSerialExt = httpRequest.getParameter("columnsSerialExt");
    String orderByColumn = httpRequest.getParameter("orderByColumn");
    String searchText = httpRequest.getParameter("searchText");
    int currPageNoFind = Integer.parseInt(httpRequest.getParameter("currPageNoFind"));
    //retrieve value
    if(searchText != null && !searchText.equals(""))
      return new SessionUtil<UsersService>().getAppContext("usersService")
              .getListTableValue(tableName,columnsSerialExt,orderByColumn,searchText,currPageNoFind);
    return new SessionUtil<UsersService>().getAppContext("usersService").getListTableValue(tableName,columnsSerialExt,orderByColumn,currPageNoFind);
  }

  /**
  * Generate page no
  *
  * @param httpRequest
  * @return int
  */
  @RequestMapping(value = "/table/pageno", method = RequestMethod.GET)
  public @ResponseBody Integer getPageNo(HttpServletRequest httpRequest) {
    logger.debug("Received request to get page no");
    //grab table, columns, order by clause, and record id
    String tableName = httpRequest.getParameter("tableName");
    String columnsSerialExt = httpRequest.getParameter("columnsSerialExt");
    String orderByColumn = httpRequest.getParameter("orderByColumn");
    String id = httpRequest.getParameter("id");
    return new SessionUtil<UsersService>().getAppContext("usersService").getPageNo(tableName,id,columnsSerialExt,orderByColumn);
  }

  /**
  * Execute SQL statement
  *
  * @param httpRequest
  * @return save page name
  */
  @RequestMapping(value = "/table/save", method = RequestMethod.POST)
  public String saveTable(HttpServletRequest httpRequest) {
    logger.debug("Received request to commit sql statement into database");
    //grab statement
    String sql = httpRequest.getParameter("sql");
    //execute
    new SessionUtil<UsersService>().getAppContext("usersService").saveTable(sql);
    return "savepage";
  }

  /**
  * Save uploaded excel file to database
  *
  * @param httpRequest
  * @return upload page name
  */
  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public String upload(HttpServletRequest httpRequest) {
    logger.debug("Received request to upload file");
    //grab table and columns
    String tableName = httpRequest.getParameter("tableName");
    String columnsSerial = httpRequest.getParameter("columnsSerial");
    DataConverter dc = new DataConverter();
    UsersService usersServ =
                new SessionUtil<UsersService>().getAppContext("usersService");
    //flush table before insert
    usersServ.saveTable("TRUNCATE TABLE " + tableName + " DROP STORAGE");
    //convert excel object into SQL statement
    Sheet sheet = wb.getSheetAt(0);
    for(Row row : sheet) {
      if(row != null) {
        StringBuilder sb = new StringBuilder("INSERT INTO " + tableName + " ("+columnsSerial+") VALUES (");
        for(Cell cell : row) {
          switch(cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                dc.setConverter(cell.getRichStringCellValue().getString(),"'");
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)) {
                    dc.setConverter(cell.getDateCellValue(),"yyyy-MM-dd","'");
                } else {
                    dc.setConverter(cell.getNumericCellValue(),"#0.############");
                }
                break;
            default:
                dc.setConverter("");
          }
          sb.append(dc.getConverter()).append(",");
        }
        //save data
        usersServ.saveTable(sb.substring(0,sb.lastIndexOf(",")) + ")");
      }
    }
    return "uploadpage";
  }
  
  /**
  * Upload excel file into server
  *
  * @param file
  * @param fileType
  * @param isHeader
  * @return json data
  */
  @RequestMapping(value = "/upload/current", method = RequestMethod.POST)
  public @ResponseBody Map<String,Object> getUploadCurrent(
          @RequestParam("file") MultipartFile file, @RequestParam("fileType") String fileType, @RequestParam("isHeader") boolean isHeader) {
    logger.debug("Received request to save uploaded file");
    Map<String,Object> contents = new HashMap<String,Object>();//return store
    //read excel file and store them into object
    //remember to add column header
    try {
      List<String> headers = new ArrayList<String>();
      List<Map<String,String>> values = new ArrayList<Map<String,String>>();
      if(fileType.contains("excel"))
        wb = new HSSFWorkbook(file.getInputStream());//.xls
      else
        wb = new XSSFWorkbook(file.getInputStream());//.xlsx
      //file has header
      if(isHeader)
        wb.getSheetAt(0).removeRow(wb.getSheetAt(0).getRow(0));
      Sheet sheet = wb.getSheetAt(0);
      DataConverter dc = new DataConverter();
      int numOfColumn = sheet.getRow(sheet.getLastRowNum()).getLastCellNum();
      for(int idxCol = 1; idxCol <= numOfColumn; idxCol++)
        headers.add("COL"+idxCol);
      for(Row row : sheet) {
        if(row != null) {
          Map<String,String> value = new HashMap<String,String>();
          for(Cell cell : row) {
             switch(cell.getCellType()) {
               case Cell.CELL_TYPE_STRING:
                   dc.setConverter(cell.getRichStringCellValue().getString());
                   break;
               case Cell.CELL_TYPE_NUMERIC:
                   if(DateUtil.isCellDateFormatted(cell)) {
                       dc.setConverter(cell.getDateCellValue(),"dd-MMM-yy");
                   } else {
                       dc.setConverter(cell.getNumericCellValue(),"#0.############");
                   }
                   break;
               default:
                   dc.setConverter("");
             }
            value.put("col"+(cell.getColumnIndex()+1),dc.getConverter());
          }
          values.add(value);
        }
      }
      contents.put("headers", headers);
      contents.put("values", values);
    } catch (IOException iox) {
      logger.error(iox);
    }
    return contents;
  }
  
  /**
  * Identify user privilege on current table
  *
  * @param httpRequest
  * @return json data
  */
  @RequestMapping(value = "/upload/access", method = RequestMethod.GET)
  public @ResponseBody List<ListBean> getUploadTableAccess(HttpServletRequest httpRequest) {
    logger.debug("Received request to get uploaded file access");
    //grab table
    String uid = httpRequest.getParameter("uid");
    //retrieve privilege
    return new SessionUtil<UsersService>().getAppContext("usersService").getListUploadTableByUser(uid);
  }

 /**
  * Execute method to clear directory
  *
  * @param httpRequest
  * @return status success
  */
  @RequestMapping(value = "/refresh", method = RequestMethod.POST)
  public String refreshPage(HttpServletRequest httpRequest) {
    logger.debug("Received request to delete json created file on server");
    //grab directory path
    File dir = new File(httpRequest.getSession().getServletContext()
             .getRealPath("/json"));
    //clear directory
    for(File file : dir.listFiles()) {
      file.delete();
    }
    return "refreshpage";
  }

  /**
   * Processing download file
   * @param httpRequest 
   * @param httpResponse 
   */
  @RequestMapping(value = "/download/request", method = RequestMethod.POST)
  public void requestDownload(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    logger.debug("Received request to create downloadable file");
    //grab current view (0=KPI view, 1=breakdown view)
    String kpiBrkdwnFlag = httpRequest.getParameter("kpiBrkdwnFlag");
    //title setup
    String reportTitle = kpiBrkdwnFlag.equals("0") ? "Report KPI" : "Report Breakdown KPI";
    List<String> titles = new ArrayList<String>();
    titles.add(reportTitle);
    titles.add("Department: " + httpRequest.getParameter("deptName"));
    titles.add("Time Series: " + httpRequest.getParameter("dtsName"));
    titles.add("Company: " + httpRequest.getParameter("coyName"));
    titles.add("Business Unit: " + httpRequest.getParameter("lobName"));
    titles.add("Level: " + httpRequest.getParameter("membersName"));
    if(kpiBrkdwnFlag.equals("1"))
      titles.add(httpRequest.getParameter("kpiName"));
    //header setup
    String[] nameArr = {"Parent Id","Parent Description","Id","Description","Batas Atas","Batas Bawah",
      "Satuan","Target","Actual","Last Month","Achieve(%)","Growth(%)","Populate"};
    int[] widthArr = {30,133,24,133,48,48,24,48,48,48,48,48,60};
    List<ColumnHeader> colsHdr = new ArrayList<ColumnHeader>();
    for(int idxArr = 0; idxArr < nameArr.length; idxArr++) {
      ColumnHeader colHdr = new ColumnHeader();
      colHdr.setName(nameArr[idxArr]);
      colHdr.setWidth(widthArr[idxArr]);
      colsHdr.add(colHdr);
    }
    //rename file to identify easily including its extention
    Calendar cal = Calendar.getInstance();
    String strDate = cal.get(Calendar.DATE) + "-" + cal.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.ENGLISH) +
            "-" + cal.get(Calendar.YEAR) + "_" + cal.get(Calendar.HOUR_OF_DAY) + "." + cal.get(Calendar.MINUTE);
    String fileExt = httpRequest.getParameter("extType");
    jsonName = jsonName.substring(0,jsonName.lastIndexOf("_")+1)+strDate+"."+fileExt;
    //prepare dowloadable file
    //retrieve current KPI data and store them into object with spesific chosen file type (excel, pdf or text)
    //do not forget to order the object content
    fileDownload = new File(jsonName);
    List<ListKpiHie> listKpiTemp = new ArrayList<ListKpiHie>();    
    transformObjectToList(listKpiTemp,listJsonKpi,kpiBrkdwnFlag);
    Collections.sort(listKpiTemp);
    try {
      new OutputFileCreator(fileDownload,titles,colsHdr,listKpiTemp,fileExt,kpiBrkdwnFlag).createFile();
    } catch(IOException iox) {
      logger.error(iox);
    } catch(COSVisitorException cox) {
      logger.error(cox);
    }
  }

  /**
   * Handles download page
   * @param httpResponse 
   */
  @RequestMapping(value = "/download", method = RequestMethod.GET)
  public void download(HttpServletResponse httpResponse) {
    logger.debug("Received request to download file");
    //read data and rewrite it into downloadable file
    try {
      httpResponse.setHeader("Content-disposition","attachment; filename="+jsonName);
      InputStream in = new FileInputStream(fileDownload);
      FileCopyUtils.copy(in, httpResponse.getOutputStream());
      httpResponse.flushBuffer();
    } catch (IOException ex) {
     logger.error(ex);
    }
  }
  
  @RequestMapping(value = "/currentsession", method = RequestMethod.GET)
  public @ResponseBody String getCurrentSession(HttpServletRequest httpRequest) {
    logger.debug("Received request to get current session");
    return httpRequest.getSession().getId();
  }

 /**
  * Generate requested KPI data (an alternative data access through file)
  *
  * @param httpRequest
  * @param httpResponse
  * @return kpi page name
  */
  @RequestMapping(value = "/kpifile", method = RequestMethod.POST)
  public String getKpiPageFile(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
     logger.debug("Received request to get kpi data");
     //no cache applicable
     httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
     httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
     httpResponse.setDateHeader("Expires", 0); // Proxies
     //jsonName template:
     //kpi_[timeseries]_[layer]_[level]_[coy]_[lob]_[kpi_id|null]_[timestamp]
     jsonName = httpRequest.getParameter("jsonName");
     String[] tmpArr = jsonName.split("_");
     String path = httpRequest.getSession().getServletContext()
             .getRealPath("/json")+"/"+jsonName+".json";
     List<JSONObject> listJson = new ArrayList<JSONObject>();
     try {
       UsersService usersServ =
                new SessionUtil<UsersService>().getAppContext("usersService");
       MstDtsService mdServ =
                new SessionUtil<MstDtsService>().getAppContext("mstDtsService");
       MstDts mstDts = mdServ.getByCode(tmpArr[1]);
       if(tmpArr.length == 7) {
         knList = usersServ.getListKpi(tmpArr[2],tmpArr[3],tmpArr[4],tmpArr[5],mstDts.getDtsTable(),mstDts.getDtsTableLastMonth());
       } else {
         knList = usersServ.getListKpi(tmpArr[2],tmpArr[3],tmpArr[4],tmpArr[5],tmpArr[6],mstDts.getDtsTable(),mstDts.getDtsTableLastMonth());
       }
       //read data and write them into file
       for(ListKpi kn : knList) {
        Map<String,Object> jsonContent = new HashMap<String,Object>();
        jsonContent.put("id",kn.getId());
        jsonContent.put("parent",kn.getParent());
        jsonContent.put("kpi",kn.getKpi());
        jsonContent.put("members",kn.getMembers());
        jsonContent.put("coy",kn.getCoy());
        jsonContent.put("lob",kn.getLob());
        jsonContent.put("kpiType",kn.getKpiType());
        jsonContent.put("name",kn.getName());
        jsonContent.put("url",kn.getUrl());
        jsonContent.put("target",kn.getTarget());
        jsonContent.put("actual",kn.getActual());
        jsonContent.put("achieve",kn.getAchieve());
        jsonContent.put("lastMonth",kn.getLastMonth());
        jsonContent.put("growth",kn.getGrowth());
        jsonContent.put("color",kn.getColor());
        jsonContent.put("dept",kn.getDept());
        jsonContent.put("button",kn.getButton());
        jsonContent.put("icon",kn.getIcon());
        List<Integer> listJsonDel = new ArrayList<Integer>();
        JSONArray jsonArray = new JSONArray();
        for(int idxJson = 0; idxJson < listJson.size(); idxJson++) {
          JSONObject json = listJson.get(idxJson);
          if(kn.getId() == json.getInt("parent")) {
            jsonArray.put(json);
            listJsonDel.add(idxJson);
          }
        }
        for(int idxJson = listJsonDel.size()-1; idxJson >= 0; idxJson--) {
          Integer intJson = listJsonDel.get(idxJson);
          listJson.remove(intJson.intValue());
        }
        if(jsonArray.length() > 0) {
          jsonContent.put("children",jsonArray);
        }
        JSONObject jsonObject = new JSONObject(jsonContent);
        listJson.add(jsonObject);
       }
       FileUtil.writeFile(path, listJson.get(listJson.size()-1).toString());


    } catch(JSONException x) {
      logger.debug(x);
    } catch (IOException x) {
      logger.debug(x);
   }
     // This will resolve to /jsp/kpipage.jsp
     return "kpipage";
  }
  
  // Transform object which has hierarchy field into list
  private void transformObjectToList(List<ListKpiHie> listsKpiHie, ListKpiHie listKpiHie, String kpiBrkdwnFlag) {
    if(listKpiHie.getParent() != -1 && !(listKpiHie.getParent() == 0 && kpiBrkdwnFlag.equals("1"))) {
      ListKpiHie lkh = new ListKpiHie();
      lkh.setAchieve(listKpiHie.getAchieve());
      lkh.setActual(listKpiHie.getActual());
      lkh.setBatasAtas(listKpiHie.getBatasAtas());
      lkh.setBatasBawah(listKpiHie.getBatasBawah());
      lkh.setDatePopulate(listKpiHie.getDatePopulate());
      lkh.setGrowth(listKpiHie.getGrowth());
      lkh.setId(listKpiHie.getId());
      lkh.setKpi(listKpiHie.getKpi());
      lkh.setLastMonth(listKpiHie.getLastMonth());
      lkh.setName(listKpiHie.getName());
      lkh.setParent(listKpiHie.getParent());
      lkh.setParentName(listKpiHie.getParentName());
      lkh.setSatuan(listKpiHie.getSatuan());
      lkh.setTarget(listKpiHie.getTarget());
      listsKpiHie.add(lkh);
    }
    List<ListKpiHie> children = (List<ListKpiHie>) listKpiHie.getChildren();
    if(children != null && children.size() > 0) {
      for(ListKpiHie lkhTemp : children)
        transformObjectToList(listsKpiHie,lkhTemp,kpiBrkdwnFlag);
    }
  }
  /*@ExceptionHandler(Exception.class)
  public ModelAndView handleError(HttpServletRequest httpRequest, Exception exception) {
    //System.out.println(exception);
    //httpRequest.setAttribute("springException", exception.toString());
    ModelAndView model = new ModelAndView("errorpage");
    model.addObject("exception", exception);
    return model;
  }*/

  Map<String, Object> getUploadCurrent(MultipartFile file, String fileType) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
