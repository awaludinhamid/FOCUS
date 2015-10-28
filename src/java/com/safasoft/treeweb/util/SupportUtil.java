/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

/**
 * Various functions which supporting this project
 * @created 24 Aug, 2014
 * @author awal
 */
public class SupportUtil {

  /**
   * Get SQL order by column clause through id of current java bean
   * @param clazz
   * @return String statement
   */
  public static String getOrderColumns(Class clazz) {
    List<String> fields = new ArrayList<String>();
    for(Field field : clazz.getDeclaredFields()) {
      Annotation[] annotations = field.getDeclaredAnnotations();
      for(Annotation annotation : annotations) {
        if(annotation.toString().startsWith("@javax.persistence.Id"))
            fields.add(field.getName());
      }
    }
    return getStringFromList(fields, " order by ", ",");
  }

  /**
   * Formatting number, consist of thousand and decimal separator
   * @param number
   * @param separator
   * @return String of formatted number
   */
  public static String getFormattedNumber(long number, char separator) {
    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
    symbols.setGroupingSeparator(separator);
    formatter.setDecimalFormatSymbols(symbols);
    return formatter.format(number);
  }
    
  /**
   * Creation of session variable especially user id and user display
   * @param httpRequest 
   */
  public static void setSessionVariable(HttpServletRequest httpRequest) {
    String cnname = (String) httpRequest.getSession().getAttribute("cnname");
    if(cnname.equals("")) {
      try {
        String principal = httpRequest.getUserPrincipal().toString();
        int start = principal.indexOf("cn=");
        String tmp = principal.substring(start + 3);
        int end = tmp.indexOf(",");
        cnname = tmp.substring(0,end);
        httpRequest.getSession().setAttribute("cnname", cnname);
        httpRequest.getSession().setAttribute("uid", httpRequest.getUserPrincipal().getName());
      } catch(NullPointerException npe) {
        System.out.println(npe);
      }
    }
  }

  //transform list of value into flat text
  private static String getStringFromList(List<String> strList, String initStr, String delimit) {
      StringBuilder sb = new StringBuilder(initStr);
      for(String str : strList)
        sb.append(str).append(delimit);
      return sb.replace(sb.lastIndexOf(delimit),sb.length(), " ").toString();
  }
  
}
