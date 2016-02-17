<%-- 
    Document   : header
    Created on : Jun 25, 2015, 2:30:49 PM
    Author     : awal
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" href="../../fificon.png" />
    <link rel="stylesheet" href="../../jQuery/css/jquery-ui.min.css"/>
    <link rel="stylesheet" href="../../bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../css/focus.css"/>
    <link rel="stylesheet" href="../../css/loading.css"/>
    <script src="../../jQuery/js/jquery.min.js"></script>
    <script src="../../jQuery/js/jquery-ui.min.js"></script>
    <script src="../../bootstrap/js/bootstrap.min.js"></script>
    <script src="../../js/globalvar.js"></script>
    <script src="../../js/globalfunc.js"></script>
    <script src="../../js/header.js"></script>
    <title>FIFGROUP Control System</title>
  </head>
  <body>
    <div class="left-back-pos back-def-style" hidden></div>
    <div class="split-back back-def-style" hidden></div>
    <div class="center-back-pos back-def-style" hidden></div>
    <div id="period-anime">
      <svg width="20" height="10" overflow="hidden">
        <ellipse cx="10" cy="5" rx="10" ry="5" fill="white"/>
      </svg>
    </div>
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
        <span id="cnname" hidden>${sessionScope.cnname}</span>
        <span id="uid" hidden>${sessionScope.uid}</span>
        <span id="sessionid" hidden>${sessionScope.sessionid}</span>
        <div id="userMenu" class="btn-group">
        </div>
      </div>
      <div id="right-title">
        <h4 style="opacity: 0.9"><span class="label label-primary full-width"></span></h4>
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
            <button type="button" class="btn btn-danger" data-dismiss="modal">No</button>
            <button id="btn-yes" type="button" class="btn btn-primary" data-dismiss="modal">Yes</button>
          </div>
        </div>
      </div>
    </div>
    <div id="loading" style="display: none">
      <img src="../../img/loading3.gif" alt="loading.."/>
      <br/>
      <span>Loading..</span>
      <!--section class="ball-stage">
        <span class="redball"></span>
        <figure class="greenball"></figure>
        <figure class="yellowball"></figure>
        <figure class="blueball"></figure>
      </section>
      <section class="text-stage">
        <span>&nbsp;&nbsp;&nbsp;Loading..</span>
      </section-->
    </div>
    <div id="backImage">
      <img src="../../img/logo-jempol.png" alt="background-image"/>
    </div>
    <script>
      $("#insetBgd").css("width",$(window).width());
    </script>
  </body>
</html>
