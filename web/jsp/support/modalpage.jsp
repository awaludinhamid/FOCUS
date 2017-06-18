<%-- 
    Document   : modalpage
    Created on : Jul 16, 2015, 4:57:23 AM
    Author     : awal
--%>

<html>
  <body>
    <div class="modal fade" id="mdl-common" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <div class="modal-title" id="myModalLabel"></div>
          </div>
          <div class="modal-body">
            <span id="content"></span>
          </div>
          <div class="modal-footer">
            <button id="btn-ok" type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="mdl-common1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <div class="modal-title" id="myModalLabel"></div>
          </div>
          <div class="modal-body">
            <span id="content"></span>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-dismiss="modal">No</button>
            <button id="btn-yes" type="button" class="btn btn-primary" data-dismiss="modal">Yes</button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="mdl-detail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">Detail</h4>
          </div>
          <div class="modal-body">
            <span id="title"></span>
            <table class="table table-striped table-bordered">
              <thead style="background-color: #37b; color: white">
                <tr style="text-align: center">
                  <td>Name</td><td>Value</td>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>Actual M-1</td><td id="actualLastMonth" style="text-align: right"></td>
                </tr>
                <tr>
                  <td>Achieve M-1</td><td id="achieveLastMonth" style="text-align: right"></td>
                </tr>
                <tr>
                  <td>Target M</td><td id="target" style="text-align: right"></td>
                </tr>
                <tr>
                  <td>Actual M</td><td id="actual" style="text-align: right"></td>
                </tr>
                <tr>
                  <td>Achieve M</td><td id="achieve" style="text-align: right"></td>
                </tr>
                <!--tr>
                  <td nowrap>Last Month</td><td id="lastMonth" style="text-align: right"></td>
                </tr-->
                <tr>
                  <td>Growth</td><td id="growth" style="text-align: right"></td>
                </tr>
                <tr>
                  <td>Proyeksi</td><td id="proyeksi" style="text-align: right"></td>
                </tr>
                <tr>
                  <td>Actual H-2</td><td id="actualBeforeYest" style="text-align: right"></td>
                </tr>
                <tr>
                  <td>Achieve Proyeksi</td><td id="achieveProyeksi" style="text-align: right"></td>
                </tr>
                <tr>
                  <td>Tgl Proses</td><td id="populate" style="text-align: center"></td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="modal-footer">
            <button id="btn-ok" type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="mdl-table" tabindex="-1" role="dialog" aria-labelledby="title" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="title"></h4>
          </div>
          <div class="modal-body">
            <span id="content"></span>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
            <button id="btn-save" type="button" class="btn btn-primary" data-dismiss="modal">Save</button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="mdl-download" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">
              <span><span class='glyphicon glyphicon-download'></span>&nbsp;Download</span>
            </h4>
          </div>
          <div class="modal-body">
            <span>Select download target file type</span>
          </div>
          <div class="modal-footer">
            <button id="btn-xlsx" type="button" class="btn btn-default" data-dismiss="modal">
              <img src="../../img/excel_icon.jpg" alt="Excel" width="16" height="16"/> | Excel
            </button>
            <button id="btn-pdf" type="button" class="btn btn-default" data-dismiss="modal">
              <img src="../../img/pdf_icon.png" alt="PDF" width="16" height="16"/> | PDF
            </button>
            <button id="btn-txt" type="button" class="btn btn-default" data-dismiss="modal">
              <img src=../../img/text_icon.jpg alt="Text" width="16" height="16"/> | Text
            </button>
            <button id="btn-cancel" type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>

