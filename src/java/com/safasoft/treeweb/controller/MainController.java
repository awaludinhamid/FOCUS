/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.controller;

import com.safasoft.treeweb.bean.support.UserProfileBean;
import com.safasoft.treeweb.service.UsersService;
import com.safasoft.treeweb.util.FileUtil;
import com.safasoft.treeweb.util.SessionUtil;
import id.co.fif.json.JSONArray;
import id.co.fif.json.JSONObject;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @created Jun 19, 2015
 * @author awal
 */
 
/**
 * Handles and retrieves the common or admin page depending on the URI template.
 * A user must be log-in first he can access these pages.  Only the admin can see
 * the adminpage, however.
 */
@Controller
@RequestMapping("/main")
public class MainController {

 protected static Logger logger = Logger.getLogger("controller");


 /**
  * Handles and retrieves the application JSP page that registered user can see
  *
   * @param model
   * @param httpRequest
  * @return the name of the JSP page
  */
    @RequestMapping(value = "/application", method = RequestMethod.GET)
    public String getAppPage(ModelMap model, HttpServletRequest httpRequest) {
     logger.debug("Received request to show app page");
     // Do your work here. Whatever you like
     // i.e call a custom service to do your business
     // Prepare a model to be used by the JSP page
     // uncomment to create json through file
     /*String user = httpRequest.getUserPrincipal().getName();
     String path = httpRequest.getSession().getServletContext()
             .getRealPath("/json")+"/list_member_"+user+".json";
     setProfileFile(user,path);*/
     // This will resolve to /jsp/applicationpage.jsp
     return "applicationpage";
 }

 /**
  * Handles and retrieves the home JSP page that all users can see
  *
   * @param model
   * @param httpRequest
  * @return the name of the JSP page
  */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHomePage(ModelMap model, HttpServletRequest httpRequest) {
     logger.debug("Received request to show home page");

     // Do your work here. Whatever you like
     // i.e call a custom service to do your business
     // Prepare a model to be used by the JSP page

     // This will resolve to /jsp/homepage.jsp
     return "homepage";
 }

    /**
     * Handles and retrieves the admin JSP page that only admins can see
     *
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getAdminPage() {
     logger.debug("Received request to show admin page");

     // Do your work here. Whatever you like
     // i.e call a custom service to do your business
     // Prepare a model to be used by the JSP page

     // This will resolve to /jsp/adminpage.jsp
     return "adminpagedev";
 }

    /**
     * Handles error JSP page that launched when error produced
     *
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
     logger.debug("Received request to show error page");

     // Do your work here. Whatever you like
     // i.e call a custom service to do your business
     // Prepare a model to be used by the JSP page

     // This will resolve to /jsp/adminpage.jsp
     return "errorpage";
    }
    
    private String getName(String text) {
      int start = text.indexOf("cn=");
      String temp = text.substring(start + 3);
      int end = temp.indexOf(",");
      String name = temp.substring(0,end);
      return name;
    }
    
    private void setProfileFile(String user, String path) {
      UsersService userServ =
              new SessionUtil<UsersService>().getAppContext("usersService");
      try {
        JSONArray jsonArr = new JSONArray();
        for(UserProfileBean upb : userServ.getUserProfile(user)) {
          jsonArr.put(new JSONObject(upb));
        }
        FileUtil.writeFile(path, jsonArr.toString());
     } catch(IOException iox) {
       logger.debug(iox);
     }
      
    }
}
