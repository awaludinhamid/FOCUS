<%-- 
    Document   : keypro
    Created on : Nov 22, 2015, 6:31:36 AM
    Author     : awal
--%>

<%@include file="header.jsp" %>
<%@include file="../../jsp/support/modalpage.jsp" %>
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="../../d3/css/tree.css"/>
    <link rel="stylesheet" href="../../css/keypro/application.css"/>
    <script src="../../d3/js/d3.js"></script>
    <script src="../../d3/js/d3.layout.tree.js"></script>
    <!--script src="../../js/json_data1.js"></script-->
    <script src="../../js/keypro/application.js"></script>
  </head>
  <body ng-app="dataApp" ng-controller="dataCtrl">
    <div class="left-back-pos back-def-style" hidden></div>
    <div class="center-back-pos back-def-style"></div>
    <div class="right-back-pos back-def-style" hidden></div>
    <div class="menu-anime"></div>
    <div class="menu-back-pos" hidden></div>
    <div id="btn-menu" class="menu-front-pos" hidden>
      <ul class="list-inline">
        <li><button id="btn-proceed" class="blue-pill font-color"><span class="glyphicon glyphicon-check"></span>&nbsp;PROCEED</button></li>
        <li><button id="btn-download" class="blue-pill"><span class="glyphicon glyphicon-file"></span>&nbsp;Create File</button></li>
        <li><button id="btn-progress" class="blue-pill"><span class="glyphicon-hourglass"></span>&nbsp;Show Progress</button></li>        
      </ul>
    </div>
    <div class="left-front-pos" hidden>
      <div id="kpi-option">
        <ul class="list-unstyled">
          <li id="selectkpi"><span></span></li>
          <li id="main-kpi"><span class="glyphicon glyphicon-triangle-left"></span><span>&nbsp;Main Tree</span></li>
          <li id="det-kpi"><span class="glyphicon glyphicon-triangle-left"></span><span>&nbsp;Detail Tree</span></li>
        </ul>
      </div>
      <div id="main-option"></div>  
    </div>
    <div class="center-front-pos" hidden>
      <div id="addition-option">
        <table>
          <tr style="vertical-align: top"></tr>
        </table>
      </div>
      <div id="data">
        <div>
          <table id="data-table" class="table table-bordered table-condensed table-hover">
            <thead>
              <tr>
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
                <td>{{dat.col21}}</td>
                <td>{{dat.col22}}</td>
                <td>{{dat.col23}}</td>
                <td>{{dat.col24}}</td>
                <td>{{dat.col25}}</td>
                <td>{{dat.col26}}</td>
                <td>{{dat.col27}}</td>
                <td>{{dat.col28}}</td>
                <td>{{dat.col29}}</td>
                <td>{{dat.col30}}</td>
                <td>{{dat.col31}}</td>
                <td>{{dat.col32}}</td>
                <td>{{dat.col33}}</td>
                <td>{{dat.col34}}</td>
                <td>{{dat.col35}}</td>
                <td>{{dat.col36}}</td>
                <td>{{dat.col37}}</td>
                <td>{{dat.col38}}</td>
                <td>{{dat.col39}}</td>
                <td>{{dat.col40}}</td>
                <td>{{dat.col51}}</td>
                <td>{{dat.col52}}</td>
                <td>{{dat.col53}}</td>
                <td>{{dat.col54}}</td>
                <td>{{dat.col55}}</td>
                <td>{{dat.col56}}</td>
                <td>{{dat.col57}}</td>
                <td>{{dat.col58}}</td>
                <td>{{dat.col59}}</td>
                <td>{{dat.col60}}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div id="pagination" style="text-align: center">
          <ul class="pagination"></ul>
        </div>
      </div>
    </div>
    <div class="right-front-pos" hidden>
      <ul class="list-unstyled" id="column">
        <li id="selectcolumn">
          <span id="spanselectcolumn" class="btn btn-primary btn-sm glyphicon glyphicon-tag">&nbsp;&nbsp;Columns
            <label style="position: absolute; right: 10px">
              <input id="allcolumn" type="checkbox" value="allcol"/>&nbsp;All
            </label>
          </span>
        </li>
      </ul>
    </div>
    <div class="modal fade" id="mdl-confirm-create-file" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">
              <span><span class='glyphicon glyphicon-download'></span>&nbsp;Create File Confirmation</span>
            </h4>
          </div>
          <div class="modal-body">
            <p>Do you want to create a file?</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-dismiss="modal">NO</button>
            <button id="btn-yes" type="button" class="btn btn-primary" data-dismiss="modal">YES</button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="mdl-exec-create-file" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">
              <span><span class='glyphicon glyphicon-download'></span>&nbsp;Create File</span>
            </h4>
          </div>
          <div class="modal-body">
            <p>
              Task has been submit to the server to create a new data file:
              <span></span>
            </p>
            <p>
              An email will be sent once file has created.<br/>
              Use <b>Show Progress</b> button to see the progress of file creation.
            </p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="mdl-search-list" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">
              <span><span class='glyphicon glyphicon-search'></span>&nbsp;Pick Option from List</span>
            </h4>
          </div>
          <div class="modal-body">
            <div> 
              <table id="search-table" style="height: 400px">
                <thead>
                  <tr style="height: 20px">
                    <th>Searching List</th>
                    <th></th>
                    <th>Selected List</th>
                  </tr>
                </thead>
                <tbody>
                  <tr style="margin: 5px">
                    <td style="vertical-align: top; border: lightgrey solid 1px;; width: 46%">
                      <div id="search-list">
                        <p>
                          <label>TYPE NAME TO FIND:&nbsp;
                            <input id="search-text" placeholder="ENTER to execute" title="ENTER to execute" ng-model="searchText">
                          </label><br/>
                          <label id="find-select-all" style="float: right; display: none"><input type="checkbox">&nbsp;Select All</label>
                        </p>
                        <ul ng-repeat="dat in dataFindList" id="data-list-find" class="list-unstyled">
                          <li>
                            <label style="margin: 0px"><input id="{{dat.code}}" type="checkbox" style="margin: 0px">&nbsp;{{dat.name}}</label>
                          </li>
                        </ul>
                        <p style="text-align: right; font-weight: bold; vertical-align: bottom">
                          <span id="prevPageFind">PREVIOUS</span>
                          <span>&nbsp;&VerticalSeparator;&nbsp;</span>
                          <span id="nextPageFind">NEXT</span>
                        </p>
                      </div>
                    </td>
                    <td>
                      <ul class="list-unstyled" style="text-align: center">
                        <li><button id="btn-switch-right" class="btn btn-primary btn-sm glyphicon glyphicon-triangle-right"></button></li>
                        <hr/>
                        <li><button id="btn-switch-left" class="btn btn-danger btn-sm glyphicon glyphicon-triangle-left"></button></li>
                      </ul>
                    </td>
                    <td style="vertical-align: top; border: lightgrey solid 1px; width: 46%">
                      <label id="selected-select-all" style="float: right; display: none"><input type="checkbox">&nbsp;Select All</label>
                      <ul class="list-unstyled" id="selected-list"></ul>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
            <button id="btn-ok" type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="mdl-show-progress" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">
              <span><span class='glyphicon glyphicon-hourglass'></span>&nbsp;Show Progress</span>
            </h4>
          </div>
          <div class="modal-body">
            <div id="job">
              <div>
                <table id="job-table" class="table table-bordered table-condensed table-hover">
                  <thead>
                    <tr>
                      <th>JOB ID</th>
                      <th>USER NAME</th>
                      <th>FILE NAME</th>
                      <th>STATUS</th>
                      <th>STARTING TIME</th>
                      <th>ENDING TIME</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr ng-repeat="dat in dataProgList" data-id="{{dat.id}}">
                      <td>{{dat.jobId}}</td>
                      <td>{{dat.userName}}</td>
                      <td>{{dat.fileName}}</td>
                      <td>{{dat.status}}</td>
                      <td>{{dat.startingTime}}</td>
                      <td>{{dat.endingTime}}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div id="pagination-progress" style="text-align: center">
                <ul class="pagination"></ul>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-danger" data-dismiss="modal">Close</button>
            <button id="btn-refresh" class="btn btn-primary"><span class="glyphicon glyphicon-refresh"></span>&nbsp;Refresh</button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="mdl-message" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">
              <span></span>
            </h4>
          </div>
          <div class="modal-body">
            <span></span>
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" data-dismiss="modal">OK</button>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
