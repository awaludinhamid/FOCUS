/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.controller;

import com.safasoft.treeweb.util.SupportUtil;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Page controller
 * Handles and retrieves various page depending on the URI template.
 * A user must be log-in first he can access these pages.
 * Specific page can be accessed by specific user, however.
 * @created Jun 19, 2015
 * @author awal
 */
@Controller
@RequestMapping("/main")
public class MainController {

 protected static Logger logger = Logger.getLogger("controller");


 /**
  * Handles and retrieves the application JSP page
  *
   * @param httpRequest
  * @return the name of the JSP page
  */
    @RequestMapping(value = "/application", method = RequestMethod.GET)
    public String getAppPage(HttpServletRequest httpRequest) {
     logger.debug("Received request to show app page");
     //retrieve current user info
     SupportUtil.setSessionVariable(httpRequest);
     // This will resolve to /jsp/applicationpage.jsp
     return "applicationpage";
 }

 /**
  * Handles and retrieves the home JSP page
  *
   * @param httpRequest
  * @return the name of the JSP page
  */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHomePage(HttpServletRequest httpRequest) {
     logger.debug("Received request to show home page");
     //retrieve current user info
     SupportUtil.setSessionVariable(httpRequest);
     // This will resolve to /jsp/homepage.jsp
     return "homepage";
 }

    /**
     * Handles and retrieves the admin JSP page
     *
     * @param httpRequest
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getAdminPage(HttpServletRequest httpRequest) {
     logger.debug("Received request to show admin page");
     //retrieve current user info
     SupportUtil.setSessionVariable(httpRequest);
     // This will resolve to /jsp/adminpage.jsp
     return "adminpage";
 }

    /**
     * Handles and retrieves the upload JSP page
     *
     * @param httpRequest
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String getUploadPage(HttpServletRequest httpRequest) {
     logger.debug("Received request to show upload page");
     //retrieve current user info
     SupportUtil.setSessionVariable(httpRequest);
     // This will resolve to /jsp/uploadpage.jsp
     return "uploadpage";
 }

    /**
     * Handles error JSP page that launched when error produced
     *
     * @param httpRequest
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String getErrorPage(HttpServletRequest httpRequest) {
     logger.debug("Received request to show error page");
     //retrieve current user info
     SupportUtil.setSessionVariable(httpRequest);
     // This will resolve to /jsp/errorpage.jsp
     return "errorpage";
    }


 /**
  * Just example
  *
   * @param httpRequest
  * @return the name of the JSP page
  */
    @RequestMapping(value = "/application/radial", method = RequestMethod.GET)
    public String getAppRadPage(HttpServletRequest httpRequest) {
     logger.debug("Received request to show radial page");
     // This will resolve to /jsp/applicationradialpage.jsp
     return "applicationradialpage";
 }
}
