/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/* global btnPrimary, btnDefault, glyTriDown, glyClose, translation, unpickStyle, list_admin, bkLabel, lblStyle */

$(document).ready(function(){
  $("#loading").show();
  
  /* VARIABLE DECLARATION */
  var optFlag = 0, //0=show all option, 1=show only selected option
      currPageNo = 1, //page no of table to show on paging
      currPageNoUpl = 1, //page no of table to show on paging (on upload file)
      currId, //id (in this case table name) to show
      prevId, //previous id, used to apply style for previous id
      maxPage, //maximum page of table
      maxPageUpl, //maximum page of table (on upload file)
      columns, //list of column property (id,name,datatype)
      contents, //list of table contents on current paging
      idColumn, //column name of PK's table
      pagePerLoad = 10, //max page view on loading
      resultPerPage = 10, //max row data per page
      columnsSerial, //columns name of table separated by comma used in SELECT statement
      file, //file to upload
      headers, //upload file header
      values; //upload file content
  
  //init process
  generateOption();
  checkTableAccess();
  /* *** */

  /* ITEM CLICKED FUNCTION */
  // Click on admin option selected
  $("#uploadSelect").on("click","ul li",function() {
    var thisId = this.id; //current item id
    //toggle selected/unselected item class 
    //toggle selected/unselected item view
    //generate data if current and previous clicked item different
    $("#uploadSelect ul li#"+thisId+" span").removeClass(btnDefault).addClass(btnPrimary);
    $("#uploadSelect ul li#"+thisId+" span").toggleClass(glyTriDown).toggleClass(glyClose);
    if(optFlag === 0) {
      $("#uploadSelect ul li:not('#"+thisId+"')").slideDown(translation);
      optFlag = 1;
    } else {
      $("#uploadSelect ul li:not('#"+thisId+"')").slideUp(translation);
      if(thisId !== prevId) {
        $("#loading").show();
        $("#uploadSelect ul li#"+prevId+" span").attr("class",unpickStyle);
        currId = thisId;
        currPageNo = 1;
        setTabelTitle();
        clearUploadFile();
        $(".nav-tabs a[href=#current-data]").tab("show");
        checkTableAccess();
      }
      optFlag = 0;
    }
    prevId = thisId;
  });

  // Click on button refresh
  $("#idRefresh").on("click", function() {
    $("#loading").show();
    //reload data
    generateData();
  });

  // Click on page number
  $("div#current-data #pagination ul").on("click", "li", function() {
    var thisId = this.id;
    //avoid execution on current page, disable page
    //page no must be less or equal then maximum page allowed
    //reload data on clicked
    if(!$("#"+thisId).hasClass("active") && !$("#"+thisId).hasClass("disabled") && currPageNo <= maxPage) {
      $("#loading").show();
      if(thisId === "firstPage")
        currPageNo = 1;
      else if(thisId === "nextPage")
        currPageNo += 1;
      else if(thisId === "prevPage")
        currPageNo -= 1;
      else if(thisId === "lastPage")
        currPageNo = maxPage;
      else
        currPageNo = parseInt(thisId.replace("page",""));
      generateData();
    }
  });

  // Click on page number on upload tab
  $("div#upload-data #pagination ul").on("click", "li", function() {
    var thisId = this.id;
    //avoid execution on current page, disable page
    //page no must be less or equal then maximum page allowed
    //reload data on clicked
    if(!$("#"+thisId).hasClass("active") && !$("#"+thisId).hasClass("disabled") && currPageNoUpl <= maxPageUpl) {
      if(thisId === "firstPageUpl")
        currPageNoUpl = 1;
      else if(thisId === "nextPageUpl")
        currPageNoUpl += 1;
      else if(thisId === "prevPageUpl")
        currPageNoUpl -= 1;
      else if(thisId === "lastPageUpl")
        currPageNoUpl = maxPageUpl;
      else
        currPageNoUpl = parseInt(thisId.replace("pageUpl",""));
      generateDataUpl();
    }
  });
  
  // Change on file to be uploaded
  $("#file-upload").on("change", function(){    
    $("#loading").show();
    $("#btn-upload").prop("disabled",false);
    file = this.files[0];//update one file only
    //init file information
    if (file) {
      var fileSize = 0;
      if (file.size > 1024 * 1024)
        fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
      else
        fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
      $("#file-name").html("&nbsp;File Name: " + file.name +"&nbsp;");
      $("#file-size").html("&nbsp;Size: " + fileSize +"&nbsp;");
      $("#file-type").html("&nbsp;Type: " + file.type.substring(file.type.indexOf("/")+1) +"&nbsp;");
      $("#upload-status").html("&nbsp;Upload Status: PENDING");
    }   
    //attach file and posting to server to validate
    //populate into table then
    var formData = new FormData();
    formData.append("file", file);
    formData.append("fileType", file.type);
    $.ajax({
      type: "POST",
      url: "/FOCUS/apps/data/upload/current",
      data: formData,
      contentType: false,
      processData: false,
      cache: false,
      success: function (data,status) {
        if (status === "success") {
          headers = data.headers;
          values = data.values;
          $("#upload-data table thead tr").empty();
          for(i = 0; i < headers.length; i++) {
            $("#upload-data table thead tr#header-row").append(
              "<th>"+headers[i]+"</th>"
            );
          }
          generateDataUpl();
          $(".nav-tabs a[href=#upload-data]").tab("show");
          $("#loading").hide();
        } else {
          clearUploadFile();
          modalCommonShow(3,"Read file unsuccessfully: status = " + status);
        }
      },
      error: function (d) {
        clearUploadFile();
        modalCommonShow(1,"Error reading file: ", d);
      },
      fail: function(d) {
        clearUploadFile();
        modalCommonShow(2,"Failed to read file: ", d);
      }
    });
  });
  
  // Click on upload button
  $("#btn-upload").click(function() {
    $("#loading").show();
    //upload file into database
    $.post("/FOCUS/apps/data/upload",{tableName: currId, columnsSerial: columnsSerial},function(data,status) {
      if (status === "success") {
        $(".nav-tabs a[href=#current-data]").tab("show");
        generateData();
        $("#upload-status").html("&nbsp;Upload Status: SUCCESS");
      } else {
        modalCommonShow(3,"Upload file unsuccessfully: status = " + status);
        $("#upload-status").html("&nbsp;Upload Status: NOT SUCCESS");
      }
    }).error(function (d) {
      modalCommonShow(1,"Error upload file: ", d);
      $("#upload-status").html("&nbsp;Upload Status: ERROR");
    }).fail(function(d) {
      modalCommonShow(2,"Failed to upload file: ", d);
      $("#upload-status").html("&nbsp;Upload Status: FAILED");
    });
  });
  /* *** */

  /* SUPPORTING FUNCTION */
  
  // Generate admin option list
  function generateOption() {
    //generate option list from json file (has generated on scheduler process)
    //styling each list
    //setup first record id and table title
    for(i = 0; i < list_upload.length; i++) {
      if(i === 0) {
        currId = list_upload[i].code;
        clearUploadFile();
        setTabelTitle();
        createList("upload",list_upload[i].code,list_upload[i].name,true);
      } else {
        createList("upload",list_upload[i].code,list_upload[i].name,false);
      }
    }
  }

  // Generate table content 
  function generateData() {
    //grab data based on page no (paging based)
    //regenerate header
    //re-apply angular var data
    //generate paging and its status
    $.get("/FOCUS/apps/data/table",{tableName: currId, pageNo: currPageNo},function(data,status) {
      if(status === "success") {
        contents = data.contents;
        columns = data.columns;
        maxPage = data.maxPage;
        idColumn = data.idColumn;
        columnsSerial = data.columnsSerial;
        $("#current-data table thead tr").empty();
        for(i = 0; i < columns.length; i++) {
          var colDataType = columns[i].dataType;
          $("#current-data table thead tr#header-row").append(
            "<th>"+columns[i].columnName+"</th>"
          );
          $("#current-data table thead tr#datatype-row").append(
            "<td>"+colDataType+"</td>"
          );
          $("#current-data table thead tr#format-row").append(
            "<td>"+
              (colDataType === "DATE" ? "DD-MON-YY | CHARACTER" : 
                (colDataType === "NUMBER" ? "ANY FORMAT | NUMERIC" : "ANY FORMAT | CHARACTER"))+
            "</td>"
          );
        }
        var listData = $("#current-data table tbody");
        var scope = listData.scope();
        scope.dataList = contents;
        scope.$apply();
        listData.find("tr td").each(function() {
          if($(this).index() > columns.length - 1 && $(this).children("button").attr("id") == null)
            $(this).hide();
        });
        setPagination();
        switchPageStatus();
        $("#loading").hide();
      } else {
        modalCommonShow(3,"Generate table content unsuccessfully: status = " + status);
      }
    }).fail(function(d) {
      modalCommonShow(2,"Failed to generate table content: ", d);
    }).error(function(d) {
      modalCommonShow(1,"Error generate table content: ", d);
    });
  }
  
  // Generate uploaded data into table
  function generateDataUpl() {
    var valuesTemp = [];//storing data paging
    var idx = 0;
    //only show current page
    for(i = (currPageNoUpl-1)*resultPerPage; i < (resultPerPage*currPageNoUpl) && i < values.length; i++)
      valuesTemp[idx++] = values[i];
    var listData = $("#upload-data table tbody");
    var scope = listData.scope();
    scope.dataListUpl = valuesTemp;
    scope.$apply();
    listData.find("tr td").each(function() {
      if($(this).index() > headers.length - 1 && $(this).children("button").attr("id") == null)
        $(this).hide();
    });
    //redefine paging and page status
    setPaginationUpl();
    switchPageStatusUpl();
  }

  // Set table title
  function setTabelTitle() {
    for(i = 0; i < list_upload.length; i++) {
      if(list_upload[i].code === currId) {
        $("div#right-title span").html("<span class='glyphicon glyphicon-th-list'></span>&nbsp;&nbsp;"+list_upload[i].name+" ["+currId +"]");
        break;
      }
    }
  }

  // Switch page status
  function switchPageStatus() {
    if(currPageNo === 1)
      $("#firstPage").addClass("disabled");
    else
      $("#firstPage").removeClass("disabled");
    if(currPageNo > 1)
      $("#prevPage").removeClass("disabled");
    else
      $("#prevPage").addClass("disabled");
    if(currPageNo < maxPage)
      $("#nextPage").removeClass("disabled");
    else
      $("#nextPage").addClass("disabled");
    if(currPageNo === maxPage)
      $("#lastPage").addClass("disabled");
    else
      $("#lastPage").removeClass("disabled");
  }

  // Switch page status on upload tab
  function switchPageStatusUpl() {
    if(currPageNoUpl === 1)
      $("#firstPageUpl").addClass("disabled");
    else
      $("#firstPageUpl").removeClass("disabled");
    if(currPageNoUpl > 1)
      $("#prevPageUpl").removeClass("disabled");
    else
      $("#prevPageUpl").addClass("disabled");
    if(currPageNoUpl < maxPageUpl)
      $("#nextPageUpl").removeClass("disabled");
    else
      $("#nextPageUpl").addClass("disabled");
    if(currPageNoUpl === maxPageUpl)
      $("#lastPageUpl").addClass("disabled");
    else
      $("#lastPageUpl").removeClass("disabled");
  }

  // Set paging (on first page, last page, previous page, next page and number page)
  function setPagination() {
    //recreated paging list
    $("div#current-data #pagination ul li").detach();
    $("div#current-data #pagination ul").append(
      "<li id='firstPage' title='First'><a href='#' aria-label='First'><span aria-hidden='true'>&laquo;</span></a></li>"+
      "<li id='prevPage' title='Previous'><a href='#' aria-label='Previous'><span aria-hidden='true'>&lt;</span></a></li>"
    );
    var startPageNo = Math.floor(currPageNo/pagePerLoad) * pagePerLoad + (currPageNo%pagePerLoad === 0 ? 0 : 1); //start page no of serial page on page selected
    var endPageNo = maxPage < (startPageNo + pagePerLoad) ? maxPage : (startPageNo + pagePerLoad -1); //end page no of serial page on page selected
    for(i = startPageNo; i <= endPageNo; i++) {
      $("div#current-data #pagination ul").append(
        "<li id='page"+i+"'"+(i === currPageNo ? " class='active' " : "")+"><a href='#'>"+i+"</a></li>"
      );
    }
    $("div#current-data #pagination ul").append(
      "<li id='nextPage' title='Next'><a href='#' aria-label='Next'><span aria-hidden='true'>&gt;</span></a></li>"+
      "<li id='lastPage' title='Last'><a href='#' aria-label='Last'><span aria-hidden='true'>&raquo;</span></a></li>"
    );
  }

  // Set paging (on first page, last page, previous page, next page and number page) on upload tab
  function setPaginationUpl() {
    //recreated paging list
    $("div#upload-data #pagination ul li").detach();
    $("div#upload-data #pagination ul").append(
      "<li id='firstPageUpl' title='First'><a href='#' aria-label='First'><span aria-hidden='true'>&laquo;</span></a></li>"+
      "<li id='prevPageUpl' title='Previous'><a href='#' aria-label='Previous'><span aria-hidden='true'>&lt;</span></a></li>"
    );
    maxPageUpl = (values.length === 0 ? 1 : (Math.floor(values.length/resultPerPage) + (values.length%resultPerPage === 0 ? 0 : 1)));
    var startPageNo = Math.floor(currPageNoUpl/pagePerLoad) * pagePerLoad + (currPageNoUpl%pagePerLoad === 0 ? 0 : 1); //start page no of serial page on page selected
    var endPageNo = maxPageUpl < (startPageNo + pagePerLoad) ? maxPageUpl : (startPageNo + pagePerLoad -1); //end page no of serial page on page selected
    for(i = startPageNo; i <= endPageNo; i++) {
      $("div#upload-data #pagination ul").append(
        "<li id='pageUpl"+i+"'"+(i === currPageNoUpl ? " class='active' " : "")+"><a href='#'>"+i+"</a></li>"
      );
    }
    $("div#upload-data #pagination ul").append(
      "<li id='nextPageUpl' title='Next'><a href='#' aria-label='Next'><span aria-hidden='true'>&gt;</span></a></li>"+
      "<li id='lastPageUpl' title='Last'><a href='#' aria-label='Last'><span aria-hidden='true'>&raquo;</span></a></li>"
    );
  }
  
  // Change page status/data into default
  function clearUploadFile() {
    file = null;
    $("#btn-upload").prop("disabled",true);
    $("#file-upload").empty();
    $("#file-name").empty();
    $("#file-size").empty();
    $("#file-type").empty();
    $("#upload-status").html("&nbsp;Upload Status: NO FILE SELECTED&nbsp;");
    $("#upload-data #pagination ul").empty();
    var listData = $("#upload-data table thead");
    listData.find("tr").empty();
    var scope = listData.scope();
    scope.dataListUpl = [];
    scope.$apply();
  }
  
  // Identify logged user privilege on current table
  function checkTableAccess() {
    $.get("/FOCUS/apps/data/upload/access",{tableName: currId},function(data,status) {
      if(status === "success") {
        //avoid showing page to unauthorized user
        if(data === $("#uid").text()) {
          $("#uploadData").show();
          $("#uploadDataDenied").hide();
          generateData();
        } else {        
          $("#uploadData").hide();
          $("#uploadDataDenied").show();
          $("#loading").hide();
        }
      } else {
        modalCommonShow(3,"Get user access unsuccessfully: status = " + status);
      }
    }).error(function (d) {
      modalCommonShow(1,"Error get user access : ", d);
    }).fail(function(d) {
      modalCommonShow(2,"Failed to get user access : ", d);
    });    
  }
  /* *** */
});
  
/****** Angular Section ******/

//Setup angular modul and controller
var uploadApp = angular.module("uploadApp",[]);
uploadApp.controller("uploadCtrl", function($scope) {
  //init angular var
  $scope.dataList = [];
  $scope.dataListUpl = [];
  $scope.changeData = function(){
  };
});
