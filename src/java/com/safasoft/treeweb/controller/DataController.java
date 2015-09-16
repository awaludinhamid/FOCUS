/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.controller;

import com.safasoft.treeweb.bean.ListKpiTemp;
import com.safasoft.treeweb.bean.ListKpiTempHie;
import com.safasoft.treeweb.bean.MstDts;
import com.safasoft.treeweb.bean.TableContentTemp;
import com.safasoft.treeweb.bean.UserAccess;
import com.safasoft.treeweb.bean.support.ListBean;
import com.safasoft.treeweb.bean.support.UserProfileBean;
import com.safasoft.treeweb.service.MstDtsService;
import com.safasoft.treeweb.service.UserAccessService;
import com.safasoft.treeweb.service.UsersService;
import com.safasoft.treeweb.util.FileUtil;
import com.safasoft.treeweb.util.SessionUtil;
import id.co.fif.json.JSONArray;
import id.co.fif.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @created Jun 19, 2015
 * @author awal
 */

/**
 * Handles and retrieves the data page depending on the URI template.
 * A user must be log-in first he can access these pages.
 */
@Controller
@RequestMapping("/data")
public class DataController {

 protected static Logger logger = Logger.getLogger("controller");

 /**
  * Handles and retrieves data that user can see
  *
  * @return kpi data through json
  */
  @RequestMapping(value = "/kpi", method = RequestMethod.GET)
  public @ResponseBody ListKpiTempHie getKpiPage(ModelMap model, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
     logger.debug("Received request to get kpi data");
     //no cache applicable
     httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
     httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
     httpResponse.setDateHeader("Expires", 0); // Proxies
     // Do your work here. Whatever you like
     // i.e call a custom service to do your business
     //jsonName template:
     //kpi_[timeseries]_[layer]_[level]_[coy]_[lob]_[kpi_id|null]_[timestamp]
     String jsonName = httpRequest.getParameter("jsonName");
     String[] tmpArr = jsonName.split("_");
     List<ListKpiTempHie> listJson = new ArrayList<ListKpiTempHie>();
     try {
       List<ListKpiTemp> knList = new ArrayList<ListKpiTemp>();
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
       for(ListKpiTemp kn : knList) {
        ListKpiTempHie lkth = new ListKpiTempHie();
        lkth.setId(kn.getId());
        lkth.setParent(kn.getParent());
        lkth.setKpi(kn.getKpi());
        lkth.setMembers(kn.getMembers());
        lkth.setCoy(kn.getCoy());
        lkth.setLob(kn.getLob());
        lkth.setKpiType(kn.getKpiType());
        lkth.setName(kn.getName());
        lkth.setUrl(kn.getUrl());
        lkth.setTarget(kn.getTarget());
        lkth.setActual(kn.getActual());
        lkth.setAchieve(kn.getAchieve());
        lkth.setLastMonth(kn.getLastMonth());
        lkth.setGrowth(kn.getGrowth());
        lkth.setColor(kn.getColor());
        lkth.setDept(kn.getDept());
        lkth.setButton(kn.getButton());
        lkth.setIcon(kn.getIcon());
        List<Integer> listJsonDel = new ArrayList<Integer>();
        List<ListKpiTempHie> jsonArray = new ArrayList<ListKpiTempHie>();
        for(int idxJson = 0; idxJson < listJson.size(); idxJson++) {
          ListKpiTempHie json = listJson.get(idxJson);
          if(kn.getId() == json.getParent()) {
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
      logger.debug(x);
    }
     // Prepare data
     // This will resolve to json data
     return listJson.get(listJson.size()-1);
  }

 /**
  * Handles and retrieves the application JSP page that registered user can see
  *
  * @return the name of the JSP page
  */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public @ResponseBody List<UserProfileBean> getAppTempPage(ModelMap model, HttpServletRequest httpRequest) {
     logger.debug("Received request to get profile data");
     // Do your work here. Whatever you like
     // i.e call a custom service to do your business
     String user = httpRequest.getUserPrincipal().getName();
     // This will resolve json data
     return new SessionUtil<UsersService>().getAppContext("usersService").getUserProfile(user);
 }

  /**
  * Handles and retrieves data that user can see
  *
  * @return json data
  */
  @RequestMapping(value = "/useraccess", method = RequestMethod.GET)
  public @ResponseBody List<UserAccess> getUserAccessList(ModelMap model, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    return new SessionUtil<UserAccessService>().getAppContext("userAccessService").getAll();
  }

  /**
  * Handles and retrieves data that user can see
  *
  * @return json data
  */
  @RequestMapping(value = "/table", method = RequestMethod.GET)
  public @ResponseBody TableContentTemp getDataList(ModelMap model, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    String tableName = httpRequest.getParameter("tableName");
    int pageNo = Integer.parseInt(httpRequest.getParameter("pageNo"));
    return new SessionUtil<UsersService>().getAppContext("usersService").getListTableValue(tableName, pageNo);
  }

  /**
  * Handles and retrieves data that user can see
  *
  * @return save page name
  */
  @RequestMapping(value = "/table/save", method = RequestMethod.POST)
  public String saveDataList(ModelMap model, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    String sql = httpRequest.getParameter("sql");
    new SessionUtil<UsersService>().getAppContext("usersService").saveTable(sql);
    return "savepage";
  }

  /**
  * Handles and retrieves data that user can see
  *
  * @return json data
  */
  @RequestMapping(value = "/table/list", method = RequestMethod.GET)
  public @ResponseBody List<ListBean> getListDdl (ModelMap model, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    String tableName = "MST_LAYER";//httpRequest.getParameter("tableName");
    String code = "LAYER_ID";//httpRequest.getParameter("code");
    String name = "LAYER_NAME";//httpRequest.getParameter("name");
    return new SessionUtil<UsersService>().getAppContext("usersService").getListDdl(tableName,code,name);
  }

 /**
  * Execute method to clear directory
  *
  * @return status success
  */
  @RequestMapping(value = "/refresh", method = RequestMethod.POST)
  public String refreshPage(ModelMap model, HttpServletRequest httpRequest) {
    File dir = new File(httpRequest.getSession().getServletContext()
             .getRealPath("/json"));
    for(File file : dir.listFiles()) {
      file.delete();
    }
    return "refreshpage";
  }


 /**
  * Handles and retrieves data that user can see (an alternative data access through file)
  *
  * @return kpi page name
  */
  @RequestMapping(value = "/kpifile", method = RequestMethod.POST)
  public String getKpiPageFile(ModelMap model, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
     logger.debug("Received request to get kpi data");
     //no cache applicable
     httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
     httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
     httpResponse.setDateHeader("Expires", 0); // Proxies
     // Do your work here. Whatever you like
     // i.e call a custom service to do your business
     //jsonName template:
     //kpi_[timeseries]_[layer]_[level]_[coy]_[lob]_[kpi_id|null]_[timestamp]
     String jsonName = httpRequest.getParameter("jsonName");
     String[] tmpArr = jsonName.split("_");
     String path = httpRequest.getSession().getServletContext()
             .getRealPath("/json")+"/"+jsonName+".json";
     List<JSONObject> listJson = new ArrayList<JSONObject>();
     try {
       List<ListKpiTemp> knList = new ArrayList<ListKpiTemp>();
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
       for(ListKpiTemp kn : knList) {
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


    } catch(Exception x) {
      logger.debug(x);
    }
     // Prepare a model to be used by the JSP page
     // This will resolve to /jsp/kpipage.jsp
     return "kpipage";
  }

}
