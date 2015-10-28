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
    <div id="kpi" class="app-def-position" hidden>
      <ul class="list-unstyled">
        <li><span id="spanKpi"></span></li>
        <li><span id="spanKpiDet"></span></li>
      </ul>
    </div>
    <div class="app-def-position">
      <div id="kpiSelect">
      </div>
      <div id="kpiMember">
        <hr style="border-width: 2px"/>
      </div>
    </div>
    <div id="zoom-slider">
      <button id="btn-zoom-in" class="glyphicon glyphicon-minus" title="Zoom In"></button>
      <button id="btn-slider">|</button>
      <button id="btn-zoom-out" class="glyphicon glyphicon-plus" title="Zoom Out"></button>
      <button id="btn-expand-collapse" class="glyphicon glyphicon-resize-small" title="Collapse All"></button>
    </div>
    <div id="map">
      <div id="mapClose">
        <button id="btn-close-map" class="btn btn-danger btn-sm">
          <span class="glyphicon glyphicon-remove"></span><span>&nbsp;Close Map</span>
        </button>
      </div>
      <iframe id="iframeMap"></iframe>
    </div>
  </body>
</html>
