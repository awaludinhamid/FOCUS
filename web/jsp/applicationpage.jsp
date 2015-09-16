<%-- 
    Document   : showpage
    Created on : Jun 24, 2015, 9:24:59 AM
    Author     : awal
--%>

<%@include file="header.jsp" %>
<%@include file="support/modalpage.jsp" %>
<html>
  <head>
    <link rel="stylesheet" href="../../d3/css/tree.css"/>
    <script src="../../js/json_data1.js"></script>
    <script src="../../d3/js/d3.js"></script>
    <script src="../../d3/js/d3.layout.tree.js"></script>
    <script src="../../js/application.js"></script>
  </head>
  <body>
    <div id="kpi" hidden>
      <span id="spanKpi"></span><br/><br/>
      <span id="spanKpiDet"></span>
    </div>
    <div id="kpiSelect">
    </div>
    <div id="kpiMember">
    </div>
    <div id="body">
    </div>
    <div id="map">
      <div id="mapClose">
        <button id="btn-close-map" class="btn btn-danger btn-sm" style="position: absolute; left: 0; top: 75px; z-index: 10">
          <span class="glyphicon glyphicon-remove"></span><span>&nbsp;Close Map</span>
        </button>
      </div>
      <iframe id="iframeMap"></iframe>
    </div>
  </body>
</html>
