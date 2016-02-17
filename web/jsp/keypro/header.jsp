<%-- 
    Document   : header
    Created on : Dec 8, 2015, 10:32:05 AM
    Author     : awal
--%>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" href="../../fificon.png" />
    <link rel="stylesheet" href="../../jQuery/css/jquery-ui.min.css"/>
    <link rel="stylesheet" href="../../bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../css/keypro/header.css"/>
    <script src="../../jQuery/js/jquery.min.js"></script>
    <script src="../../jQuery/js/jquery-ui.min.js"></script>
    <script src="../../bootstrap/js/bootstrap.min.js"></script>
    <script src="../../angular/angular.min.js"></script>
    <script src="../../js/globalfunc.js"></script>
    <script src="../../js/keypro/header.js"></script>
    <title>FIFGROUP Data Loading and Inquiry</title>
  </head>
  <body>
    <div class="kp-title">
      <div>FIFGROUP Data Loading and Inquiry</div>
      <div class="mirror">FIFGROUP Data Loading and Inquiry</div>
      <div id="logUser">
        <!--sec:authentication property="principal.username"/-->
        <span id="cnname" hidden>${sessionScope.cnname}</span>
        <span id="uid" hidden>${sessionScope.uid}</span>
        <span id="sessionid" hidden>${sessionScope.sessionid}</span>
        <div id="userMenu" class="btn-group">
        </div>
      </div>
      <div id="otherMenu">
        <span>Powered by MIS<sup>&trade;</sup></span>
        <!--span id="idLogging"></span-->
      </div>
    </div>
    <div class="loading-anime" hidden>
      <img src="../../img/logo-jempol.png" alt="Currently"/><br/>
      <span>Loading..</span>
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
  </body>
</html>
