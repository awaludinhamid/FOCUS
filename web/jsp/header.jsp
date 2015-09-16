<%-- 
    Document   : header
    Created on : Jun 25, 2015, 2:30:49 PM
    Author     : awal
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%  String cnname = "";
    String uid = "";
    try {
      String principal = request.getUserPrincipal().toString();
      int start = principal.indexOf("cn=");
      String tmp = principal.substring(start + 3);
      int end = tmp.indexOf(",");
      cnname = tmp.substring(0,end);
      uid = request.getUserPrincipal().getName();
    } catch(NullPointerException npe) {
      System.out.println(npe);
    }
%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="icon" type="image/png" href="../../fificon.png" />
    <link rel="stylesheet" href="../../jQuery/css/jquery-ui.min.css"/>
    <link rel="stylesheet" href="../../bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../css/focus.css"/>
    <script src="../../jQuery/js/jquery.min.js"></script>
    <script src="../../jQuery/js/jquery-ui.min.js"></script>
    <script src="../../bootstrap/js/bootstrap.min.js"></script>
    <script src="../../js/globalvar.js"></script>
    <script src="../../js/globalfunc.js"></script>
    <script src="../../js/header.js"></script>
    <title>FIFGROUP Control System</title>
  </head>
  <body>
    <div id="insetBgd">
      <div id="logo">
        <a href="http://www.fifgroup.co.id"><img src="../../img/logo FIFGROUP vertical.png" alt="FIFGROUP" width="61" height="64"/></a>
      </div>
      <div id="otherMenu">
        <span>Powered by MIS<sup>&trade;</sup></span>
        <!--span id="idLogging"></span-->
      </div>
      <div id="logUser">
        <!--sec:authentication property="principal.username"/-->
        <span id="cnname" hidden><%=cnname%></span>
        <span id="uid" hidden><%=uid%></span>
        <div id="userMenu" class="btn-group">
        </div>
        <h4><span id="layer" class="label label-default full-width" style="float: right"></span></h4>
      </div>
      <div>
        <span class="insetType">FIFGROUP Control System </span>
        <br/>
        <span id="pageLabel" class="label label-primary"></span>
      </div>
    </div>
    <div class="modal fade" id="mdl-logout" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">Logout</h4>
          </div>
          <div class="modal-body">
            <span>Do you really want to logout?</span>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
            <button id="btn-ok" type="button" class="btn btn-primary" data-dismiss="modal">Yes</button>
          </div>
        </div>
      </div>
    </div>
    <div id="loading" style="display: none">
      <img src="../../img/loading3.gif" alt="loading.."/>
      <br/>
      <span>Loading..</span>
    </div>
    <div id="backImage">
      <img src="../../img/logo-jempol.png" alt="background-image"/>
    </div>
    <script>
      $("#insetBgd").css("width",$(window).width());
    </script>
  </body>
</html>
