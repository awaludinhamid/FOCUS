<%-- 
    Document   : adminpage
    Created on : Jun 19, 2015, 12:36:21 PM
    Author     : awal
--%>

<%@include file="header.jsp" %>
<%@include file="support/modalpage.jsp" %>
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="../../css/admin.css"/>
    <script src="../../angular/angular.min.js"></script>
    <script src="../../js/json_data2.js"></script>
    <script src="../../js/admin.js"></script>
  </head>
<body>
  <div id="adminSelect">
    <h4 style="position: relative; left: 40px">
      <span class="btn btn-warning" style="width: 170px" disabled>
        Select Table Below <span class="glyphicon glyphicon-arrow-down"></span>
      </span>
    </h4>
  </div>
  <div id="data">
    <h4><span id="tableTitle"></span></h4>
    <hr/>
    <table id="tableData" class="table table-bordered table-hover">
      <thead>
        <tr style="background-color: darkblue; color: white">
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
    <div id="pagination" style="text-align: center">
      <ul class="pagination"></ul>
    </div>
  </div>
</body>
</html>
