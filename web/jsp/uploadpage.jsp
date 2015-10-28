<%-- 
    Document   : uploadpage
    Created on : Sep 28, 2015, 2:19:31 PM
    Author     : awal
--%>

<%@include file="header.jsp" %>
<%@include file="support/modalpage.jsp" %>
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="../../css/upload.css"/>
    <script src="../../angular/angular.min.js"></script>
    <script src="../../js/json_data2.js"></script>
    <script src="../../js/upload.js"></script>
  </head>
  <body ng-app="uploadApp" ng-controller="uploadCtrl">
    <div id="uploadSelect" class="left-front-pos">
      <span id="spanSelectUpload" class="label label-default">&nbsp;Select Table&nbsp;
        <span class="glyphicon glyphicon-hand-down"></span>              
      </span>
      <ul id="upload" class="list-unstyled">
      </ul>
    </div>
    <div id="uploadDataDenied" class="center-front-pos default-message" style="left: 5%" hidden>
      <h1>
        <span class="glyphicon glyphicon-minus-sign"></span>
        <br/><br/>
        <div>
          <span>Sorry, you have no privilege to access this table</span>
        </div>
      </h1>
    </div>
    <div id="uploadData" class="center-front-pos" hidden>
      <div>
        <p>
          <label class="btn btn-primary" for="file-upload">
            <input id="file-upload" name="file-upload" type="file" style="display:none;" accept=".xls;*.xlsx">
            <span class="glyphicon glyphicon-file"></span>&nbsp;Select Files..
          </label>
          <button id="btn-upload" class="btn btn-info"><span class="glyphicon glyphicon-upload"></span>&nbsp;Upload</button>
        </p>
        <table class="upload-data-table">
          <tr class="upload-status"><td id="upload-status"></td></tr>
          <tr><td id="file-name"></td></tr>
          <tr><td id="file-size"></td></tr>
          <tr><td id="file-type"></td></tr>
        </table>        
      </div>
      <br/>
      <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#current-data">Current Data</a></li>
        <li><a data-toggle="tab" href="#upload-data">Upload Data</a></li>
      </ul>
      <hr class="upload-data-hr"/>
      <div class="tab-content">
        <div id="current-data" class="tab-pane fade in active">          
          <table class="table table-bordered table-hover">
            <thead>
              <tr id="header-row"></tr>
              <tr id="datatype-row"></tr>
              <tr id="format-row"></tr>
            </thead>
            <tbody>
              <tr ng-repeat="dat in dataList" data-id="{{dat.id}}">
                <td>{{dat.col1}}</td>
                <td>{{dat.col2}}</td>
                <td>{{dat.col3}}</td>
                <td>{{dat.col4}}</td>
                <td>{{dat.col5}}</td>
                <td>{{dat.col6}}</td>
                <td>{{dat.col7}}</td>
                <td>{{dat.col8}}</td>
                <td>{{dat.col9}}</td>
                <td>{{dat.col10}}</td>
                <td>{{dat.col11}}</td>
                <td>{{dat.col12}}</td>
                <td>{{dat.col13}}</td>
                <td>{{dat.col14}}</td>
                <td>{{dat.col15}}</td>
                <td>{{dat.col16}}</td>
                <td>{{dat.col17}}</td>
                <td>{{dat.col18}}</td>
                <td>{{dat.col19}}</td>
                <td>{{dat.col20}}</td>
              </tr>
            </tbody>
          </table>
          <div id="pagination">
            <ul class="pagination"></ul>
          </div>
        </div>
        <div id="upload-data" class="tab-pane fade">        
          <table class="table table-bordered table-hover">
            <thead>
              <tr id="header-row"></tr>
            </thead>
            <tbody>
              <tr ng-repeat="dat in dataListUpl" data-id="{{dat.id}}">
                <td>{{dat.col1}}</td>
                <td>{{dat.col2}}</td>
                <td>{{dat.col3}}</td>
                <td>{{dat.col4}}</td>
                <td>{{dat.col5}}</td>
                <td>{{dat.col6}}</td>
                <td>{{dat.col7}}</td>
                <td>{{dat.col8}}</td>
                <td>{{dat.col9}}</td>
                <td>{{dat.col10}}</td>
                <td>{{dat.col11}}</td>
                <td>{{dat.col12}}</td>
                <td>{{dat.col13}}</td>
                <td>{{dat.col14}}</td>
                <td>{{dat.col15}}</td>
                <td>{{dat.col16}}</td>
                <td>{{dat.col17}}</td>
                <td>{{dat.col18}}</td>
                <td>{{dat.col19}}</td>
                <td>{{dat.col20}}</td>
              </tr>
            </tbody>
          </table>
          <div id="pagination">
            <ul class="pagination"></ul>
          </div>          
        </div>
      </div>
    </div>
  </body>
</html>

