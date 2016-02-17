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
  <body ng-app="adminApp" ng-controller="adminCtrl">
    <div id="adminSelect" class="left-front-pos">
      <span id="adminSelectCaption" class="label label-default">
        Select Table&nbsp;&nbsp;<span class="glyphicon glyphicon-hand-down"></span>
      </span>
    </div>
    <div id="adminData" class="center-front-pos">
      <br/>
      <table id="adminTableData" class="table table-bordered table-hover">
        <thead>
          <tr class="admin-table-header">
            <th id="btn-support">
              <button id="btn-new" class="btn btn-default btn-sm" title="New Record"><span style="color: lightgreen" class="glyphicon glyphicon-plus"></span></button>
              <button id="btn-find" class="btn btn-default btn-sm" title="Find Record"><span style="color: blue" class="glyphicon glyphicon-search"></span></button>
            </th>
          </tr>
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
            <td id="td-button">
              <button id="btn-edit" class="btn btn-default btn-sm" title="Edit Record"><span style="color: orange" class="glyphicon glyphicon-pencil"></span></button>
              <button id="btn-delete" class="btn btn-default btn-sm" title="Delete Record"><span style="color: red" class="glyphicon glyphicon-trash"></span></button>
            </td>
          </tr>
        </tbody>
      </table>
      <div id="pagination" style="text-align: center">
        <ul class="pagination"></ul>
      </div>
    </div>
    <div class="modal fade" id="mdl-find" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <div class="modal-title" id="myModalLabel">
              <h3><span class="glyphicon glyphicon-search"></span>&nbsp;Find</h3>
            </div>
          </div>
          <div class="modal-body" id="content">
            <div>  
              <p>
                <label>Type text to find:&nbsp;
                  <input id="search-text" placeholder="ENTER to execute" title="ENTER to execute" ng-model="searchText">
                </label>
              </p>
              <table id="searchTextResults" class="table table-striped">
                <thead>
                  <tr class="admin-table-header" id="header-list-find"></tr>
                </thead>
                <tbody>
                  <tr ng-repeat="dat in dataFindList | limitTo: limitNum" id="data-list-find">
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
                    <td class="admin-table-button" data-id="{{dat.id}}">
                      <button class="btn btn-default btn-sm" title="Go To Record">
                        <span style="color: mediumaquamarine" class="glyphicon glyphicon-share-alt"></span>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
              <p style="text-align: right; font-weight: bold">
                <span id="prevPageFind">Previous</span>
                <span>&nbsp;&VerticalSeparator;&nbsp;</span>
                <span id="nextPageFind">Next</span>
              </p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
