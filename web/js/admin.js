/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/* global btnPrimary, btnDefault, glyTriDown, glyClose, translation, unpickStyle, list_admin, bkLabel, lblStyle */

$(document).ready(function(){
  loadingStateChange("show");
  
  /* VARIABLE DECLARATION */
  var optFlag = 0, //0=show all option, 1=show only selected option
      currPageNo = 1, //page no of table to show on paging
      currBtn, //0=button new, 1=button edit, 2=button delete
      currId, //id (in this case table name) to show
      prevId = "", //previous id, used to apply style for previous id
      maxPage, //maximum page of table
      maxId, //next id of PK's table has NUMBER datatype
      columns, //list of column property (id,name,datatype)
      contents, //list of table contents on current paging
      idColumn, //column name of PK's table
      idRow, //current value of PK's table
      rowContents, //table contents on current row
      sql, //sql statement to execute
      dropDownList, //list of value of column which has reference
      columnsCons, //list of constraint of column
      columnCons, //list of constraint of current column
      consTypeStat = [], //saved array containing flag of current column constrain (PK, reference, etc.)
      delimiter = ",", //char to separate list of value in one line
      maxNumOfCol = 10, //max num of table column available to show
      pagePerLoad = 10, //max page view on loading
      columnsSerial, //columns name of table separated by comma used in SELECT statement
      columnsSerialExt, //columnsSerial with some additional column such an ID or virtual column COLXX
      orderByColumn, //columns name of table separated by comma used in ORDER BY statement
      currPageNoFind = 1, //page no of find table to show on paging
      maxRecordPerPageFind = 10; //max record view per find page
  /* *** */

  //init process early
  generateOption();
  generateData();
  
  /* ITEM CLICKED FUNCTION */
  // Click on admin option selected
  $("#adminSelect").on("click","ul li",function() {
    var thisId = this.id; //current item id
    var parentId = $(this).parent().attr("id"); //current parent (group)
    //avoid execution on label
    //toggle selected/unselected item class 
    //toggle selected/unselected item view
    //generate data if current and previous clicked item different
    if(thisId !== "select"+parentId) {
      $("#adminSelect ul li#"+thisId+" span").removeClass(btnDefault).addClass(btnPrimary);
      $("#adminSelect ul li#"+thisId+" span").toggleClass(glyTriDown).toggleClass(glyClose);
      if(optFlag === 0) {
        $("#adminSelect ul:not('#"+parentId+"')").slideDown(translation);
        $("#adminSelect ul li:not('#select"+parentId+",#"+thisId+"')").slideDown(translation);
        optFlag = 1;
      } else {
        $("#adminSelect ul:not('#"+parentId+"')").slideUp(translation);
        $("#adminSelect ul li:not('#select"+parentId+",#"+thisId+"')").slideUp(translation);
        if(thisId !== prevId) {
          loadingStateChange("show");
          $("#adminSelect ul li#"+prevId+" span").attr("class",unpickStyle);
          currId = thisId;
          currPageNo = 1;
          idRow = null;
          setTabelTitle();
          generateData();
        }
        optFlag = 0;
      }
      prevId = thisId;
    }
  });

  // Click on button refresh
  $("#idRefresh").on("click", function() {
    loadingStateChange("show");
    //reload data
    generateData();
  });

  // Click on page number
  $("div#pagination ul").on("click", "li", function() {
    var thisId = this.id;
    //avoid execution on current page, disable page
    //page no must be less or equal then maximum page allowed
    //reload data on clicked
    if(!$("#"+thisId).hasClass("active") && !$("#"+thisId).hasClass("disabled") && currPageNo <= maxPage) {
      loadingStateChange("show");
      if(thisId === "firstPage")
        currPageNo = 1;
      else if(thisId === "nextPage")
        currPageNo++;
      else if(thisId === "prevPage")
        currPageNo--;
      else if(thisId === "lastPage")
        currPageNo = maxPage;
      else
        currPageNo = parseInt(thisId.replace("page",""));
      generateData();
    }
  });

  // Click on button new record
  $("#adminTableData thead tr").on("click", "th button#btn-new", function() {
    currBtn = 0;
    //show input form
    modalTableShow();
  });

  // Click on button edit record
  $("#adminTableData tbody").on("click", "tr td button#btn-edit", function() {
    currBtn = 1;
    //grab current record id, show input
    idRow = $(this).parents("tr").data("id");
    modalTableShow();
  });

  // Click button delete record
  $("#adminTableData tbody").on("click", "tr td button#btn-delete", function() {
    currBtn = 2;
    //grab current record id, show confirmation
    idRow = $(this).parents("tr").data("id");
    modalCommonShowAdmin();
  });

  // Click button save new/changed record
  $("#mdl-table .modal-footer button#btn-save").click(function(){
    //show confirmation
    modalCommonShowAdmin();
  });

  // Click button find record
  $("#adminTableData thead tr").on("click", "th button#btn-find", function() {
    $("#search-text").val("");
    var listFind = $("div#mdl-find div#content div"); //current find scope element
    listFind.find("p span#prevPageFind, p span#nextPageFind").css("cursor","text").css("color","black").css("text-decoration","none");
    var scope = listFind.scope();
    scope.dataFindList = [];
    scope.$apply();
    var listFindHdr = listFind.find("tr#header-list-find"); //current header element
    listFindHdr.children("th").remove();
    for(var idx = 0; idx < columns.length; idx++)
      listFindHdr.append(
        "<th>"+columns[idx].columnName.replace(/_/g," ")+"</th>");
    for(var idx = 0; idx < maxNumOfCol - columns.length + 1; idx++)
      listFindHdr.append(
        "<th></th>");
    var tableDataWidth = Number($("#adminTableData").css("width").replace("px","")) + 40 + (columns.length * 15) +
            (maxNumOfCol > columns.length ? (maxNumOfCol-columns.length)*5 : 0); //resize find table depend on its original
    $("#mdl-find .modal-dialog").css("width",tableDataWidth+"px");
    $("#mdl-find").modal("show");
  });
  
  // Press enter to execute process
  $("div#mdl-find input#search-text").keypress(function(e) {
    if(e.which === 13) {
      loadFindData(this.id);
    }
  });

  // Click button previous and next page find
  $("p span#prevPageFind, p span#nextPageFind").click(function() {
    if($(this).css("cursor") === "pointer") {
      loadFindData(this.id);
    }  
  });
  
  // Click button yes confirmation
  $("#mdl-common1 .modal-footer button#btn-yes").click(function(){
    loadingStateChange("show");
    var elementInput = $("#mdl-table #tbl-modal td input, #mdl-table #tbl-modal td select");
    //created sql statement on update, insert and delete
    //execute sql statement on database
    if(currBtn === 0) {
      sql = "INSERT INTO "+currId+" (" + columnsSerial + ") VALUES (";
      elementInput.each(function() {
        if($(this).data("type") === "NUMBER")
          sql += $(this).val() + delimiter;
        else if($(this).data("type") === "DATE")
          sql += "TO_DATE('" + $(this).val() + "','DD-MON-YY')" + delimiter;
        else
          sql += "'" + $(this).val() + "'" + delimiter;
      });
      sql = sql.substr(0,sql.lastIndexOf(delimiter)) + ")";
    } else if(currBtn === 1) {
      var idx = 0;
      sql = "UPDATE "+currId+" SET ";
      elementInput.each(function() {
        if(columns[idx].columnName !== idColumn) {
          if($(this).data("type") === "NUMBER")
            sql += columns[idx].columnName + " = " + $(this).val() + delimiter + " ";
          else if($(this).data("type") === "DATE")
            sql += columns[idx].columnName + " = " + "TO_DATE('" + $(this).val() + "','DD-MON-YY')" + delimiter + " ";
          else
            sql += columns[idx].columnName + " = '" + $(this).val() + "'" + delimiter + " ";
        }
        idx++;
      });
      sql = sql.substr(0,sql.lastIndexOf(delimiter)) + " WHERE " +
        (idColumn === "" ? "ROWID" : idColumn) + " = '" + idRow + "'";
    } else if(currBtn === 2) {
      sql = "DELETE FROM "+currId+" WHERE " +
        (idColumn === "" ? "ROWID" : idColumn) + " = '" + idRow + "'";
    }
    if(sql != null) {
      var execFunc = function() {
        $.post("../../apps/data/table/save",{sql: sql},function(data,status) {
          if(status === "success")
            generateData();
          else
            modalCommonShow(3,"Execute sql statement unsuccessfully: status = " + status);
        }).fail(function(d) {
          modalCommonShow(2,"Failed to execute sql statement: ", d);
        }).error(function(d) {
          modalCommonShow(1,"Error execute sql statement: ", d);
        });
      };
      checkCurrSessAndExec(execFunc);
    } else {
      loadingStateChange("hide");
    }
  });
  
  // Click button go to record on find
  $("#mdl-find div#content div table#searchTextResults").on("click","tr td button", function() {
    loadingStateChange("show");
    $("#mdl-find").modal("hide");
    idRow = $(this).parent().data("id");
    //go to page of selected record and reload data
    var execFunc = function() {
      $.get("../../apps/data/table/pageno",{tableName: currId, id: idRow, columnsSerialExt: columnsSerialExt, orderByColumn: orderByColumn},
        function(data,status) {
        if(status === "success") {
          currPageNo = data;
          generateData();
        } else {
          modalCommonShow(3,"Generate page no unsuccessfully: status = " + status);
        }
      }).fail(function(reqObj,status) {
        modalCommonShow(2,"Failed to generate page no: ", reqObj);
      }).error(function(reqObj,status) {
        modalCommonShow(1,"Error generate page no: ", reqObj);
      });
    };
    checkCurrSessAndExec(execFunc);
  });
  /* *** */

  /* SUPPORTING FUNCTION */
  
  // Generate admin option list
  function generateOption() {
    var prevParent = ""; //flag to avoid repeating label creation
    //generate option list from json file (has generated on scheduler process)
    //styling each list
    //setup first record id and table title
    for(var idx = 0; idx < list_admin.length; idx++) {
      if(prevParent !== list_admin[idx].parentCode) {
        $("#adminSelect").append(
          "<ul id='"+list_admin[idx].parentCode+"' class='list-unstyled'"+(idx === 0 ? "" : " style='display: none'")+">"+
            "<li id='select"+list_admin[idx].parentCode+"'><span id='spanselect"+list_admin[idx].parentCode+
              "'>&nbsp;&nbsp;"+list_admin[idx].parentName+"</span></li>"+
          "</ul>");
        $("#spanselect"+list_admin[idx].parentCode)
            .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
        prevParent = list_admin[idx].parentCode;
      }
      if(idx === 0) {
        currId = list_admin[idx].code;
        setTabelTitle();
        createList(list_admin[idx].parentCode,list_admin[idx].code,list_admin[idx].name,true);
      } else {
        createList(list_admin[idx].parentCode,list_admin[idx].code,list_admin[idx].name,false);
      }
    }
  }

  // Generate table content 
  function generateData() {
    var prevIdTemp = prevId; //set previous id (table) before it changed by other process
    //grab data based on page no (paging based)
    //regenerate header
    //re-apply angular var data
    //generate paging and its status
    var execFunc = function() {
      $.get("../../apps/data/table",{tableName: currId, pageNo: currPageNo},function(data,status) {
        if(status === "success") {
          contents = data.contents;
          columns = data.columns;
          maxPage = data.maxPage;
          maxId = data.maxId;
          if(currId !== prevIdTemp) {
            dropDownList = data.dropDownList;
            columnsCons = data.columnsCons;
            idColumn = data.idColumn;
            columnsSerial = data.columnsSerial;
            columnsSerialExt = data.columnsSerialExt;
            orderByColumn = data.orderByColumn;
          }
          $("#adminTableData thead tr th:not('#btn-support')").remove();
          for(var idx = columns.length - 1; idx >= 0; idx--) {
            $("#adminTableData thead tr").prepend(
              "<th>"+columns[idx].columnName.replace(/_/g," ")+"</th>"
            );
          }
          var listData = $("#adminTableData tbody");
          var scope = listData.scope();
          scope.dataList = contents;
          scope.$apply();
          listData.find("tr td").each(function() {
            if($(this).index() > columns.length - 1 && $(this).children("button").attr("id") == null)
              $(this).hide();
          });
          setPagination();
          switchPageStatus();
          setCurrentRecordBack();
          loadingStateChange("hide");
        } else {
          modalCommonShow(3,"Generate table content unsuccessfully: status = " + status);
        }
      }).fail(function(d) {
        modalCommonShow(2,"Failed to generate table content: ", d);
      }).error(function(d) {
        modalCommonShow(1,"Error generate table content: ", d);
      });
    };
    checkCurrSessAndExec(execFunc);
  }

  // Set table title
  function setTabelTitle() {
    for(var idx = 0; idx < list_admin.length; idx++) {
      if(list_admin[idx].code === currId) {
        $("div#right-title span").html("<span class='glyphicon glyphicon-th-list'></span>&nbsp;&nbsp;"+list_admin[idx].name+" ["+currId +"]");
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

  // Set paging (on first page, last page, previous page, next page and number page)
  function setPagination() {
    //recreated paging list
    $("div#pagination ul li").remove();
    $("div#pagination ul").append(
      "<li id='firstPage' title='First'><a href='#' aria-label='First'><span aria-hidden='true'>&laquo;</span></a></li>"+
      "<li id='prevPage' title='Previous'><a href='#' aria-label='Previous'><span aria-hidden='true'>&lt;</span></a></li>"
    );
    var startPageNo = Math.floor(currPageNo/pagePerLoad) * pagePerLoad + (currPageNo%pagePerLoad === 0 ? 0 : 1); //start page no of serial page on page selected
    var endPageNo = maxPage < (startPageNo + pagePerLoad) ? maxPage : (startPageNo + pagePerLoad -1); //end page no of serial page on page selected
    for(var idx = startPageNo; idx <= endPageNo; idx++) {
      $("div#pagination ul").append(
        "<li id='page"+idx+"'"+(idx === currPageNo ? " class='active' " : "")+"><a href='#'>"+idx+"</a></li>"
      );
    }
    $("div#pagination ul").append(
      "<li id='nextPage' title='Next'><a href='#' aria-label='Next'><span aria-hidden='true'>&gt;</span></a></li>"+
      "<li id='lastPage' title='Last'><a href='#' aria-label='Last'><span aria-hidden='true'>&raquo;</span></a></li>"
    );
  }

  // Show table form
  function modalTableShow() {
    loadingStateChange("show");
    //set current row content on edit and current PK on new record
    //prepare drop down list on referential column
    //populate data and button into table form
    var content = "<table id='tbl-modal' class='table'>"; //html statement temporer container
    var currRowContent = []; //current row content
    if(currBtn === 1)
      setRowContents();
    for(var idx = 0; idx < columns.length; idx++) {
      if(currBtn === 1)
        currRowContent = rowContents["col"+(idx+1)] === null ? "" : rowContents["col"+(idx+1)];
      columnCons = columnsCons[columns[idx].columnName];
      setConsTypeStat();
      content +=
        "<tr><td>"+columns[idx].columnName.replace(/_/g," ")+
        "</td><td class='admin-edit-data'>";
      if(consTypeStat["R"] === "Y") {
        var dropDownListTemp = dropDownList[columns[idx].columnName]; //drop down list temporer container
        content += "<select class='admin-edit-data' data-type='"+columns[idx].dataType+"'>";
        for(var idx1 = 0; idx1 < dropDownListTemp.length; idx1++)
          content += "<option value='"+dropDownListTemp[idx1].code+"' "+
                (currBtn === 1 && currRowContent === dropDownListTemp[idx1].code ? "selected" : "")+
                ">"+dropDownListTemp[idx1].code+(columns[idx].dataType === "NUMBER" ? " | "+dropDownListTemp[idx1].name : "")+"</option>";
        content += "</select></td></tr>";
      } else {
        content +=
          "<input class='admin-edit-data' type='"+(columns[idx].dataType === "NUMBER" ? "number" : "text")+"' data-type='"+columns[idx].dataType+"'"+
          (currBtn === 1 ? (" value='"+currRowContent+"'"+(consTypeStat["P"] === "Y" ? " disabled" : "")) :
          (consTypeStat["P"] === "Y" && columns[idx].dataType === "NUMBER" ? " value='"+maxId+"' disabled" : ""))+
          "></td></tr>";
      }
    }
    content += "</table>";
    $("#mdl-table #title").html("<span "+(currBtn === 1 ? "class='glyphicon glyphicon-pencil'></span>&nbsp;Edit" : 
            "class='glyphicon glyphicon-plus'></span>&nbsp;New Record")+" ["+currId+"]");
    $("#mdl-table #content").html(content);
    $("#mdl-table").modal("show");
    loadingStateChange("hide");
  }
  
  // Show admin prompt message confirmation
  function modalCommonShowAdmin() {
    var htmlTitle = "<h3>Undefined</h3>"; //message title
    var content = ""; //message content
    //set title and content message
    if(currBtn === 0) {
      htmlTitle = "<h3><span class='glyphicon glyphicon-plus'></span>&nbsp;New Record</h3>";
      content = "<span>Save new record?</span>";
    } else if (currBtn === 1) {
      htmlTitle = "<h3><span class='glyphicon glyphicon-pencil'></span>&nbsp;Edit</h3>";
      content = "<span>Save changed?</span>";
    } else if (currBtn === 2) {
      htmlTitle = "<h3><span class='glyphicon glyphicon-trash'></span>&nbsp;Delete</h3>";
      content = "<span>Delete this record?</span>";
    }
    $("#mdl-common1 #myModalLabel").html(htmlTitle);
    $("#mdl-common1 #content").html(content);
    $("#mdl-common1").modal("show");
  }

  // Set current row content
  function setRowContents() {
    rowContents = [];
    for(var idx = 0; idx < contents.length; idx++) {
      if(Number(contents[idx].id) === Number(idRow) || contents[idx].id === idRow) {
        rowContents = contents[idx];
        break;
      }
    }
  }
  
  // Set current record constraint type status (Yes/No) of PK dan Referential
  function setConsTypeStat() {    
    consTypeStat["P"] = "N";
    consTypeStat["R"] = "N";
    for(j = 0; j < columnCons.length; j++) {
      if(columnCons[j].constraintType === "P")
        consTypeStat["P"] = "Y";
      else if(columnCons[j].constraintType === "R")
        consTypeStat["R"] = "Y";
    }
  }
  
  // Set current record background highlight
  function setCurrentRecordBack() {
    $("#adminTableData tbody tr").each(function() {
      if(idRow == null || $(this).data("id") === idRow) {
        $(this).addClass("current-record");
        return false;
      }
    });
  }
  
  // Loading find data based on given page number
  function loadFindData(thisId) {
    loadingStateChange("show");
    $("div#mdl-find").css("opacity","0.2");
    //set page no by item clicked/enter
    if(thisId.indexOf("prev") > -1)
      currPageNoFind--;
    else if(thisId.indexOf("next") > -1)
      currPageNoFind++;
    else
      currPageNoFind = 1;
    //populate table data from database
    //save data to angular var (read AngularJS doc to learn this) that will be used on population
    //re-generate header
    //show data
    var execFunc = function() {
      $.get("../../apps/data/table/value",{
        tableName: currId, columnsSerialExt: columnsSerialExt, orderByColumn: orderByColumn,
        searchText: $("#search-text").val(), currPageNoFind: currPageNoFind},
        function(data,status) {
        if(status === "success") {
          var listFind = $("div#mdl-find div#content div"); //current find scope element
          var scope = listFind.scope(); //current find scope
          scope.dataFindList = data;
          scope.columnsLength = columns.length;
          scope.$apply();
          //apply format on previous and next page find
          if(currPageNoFind > 1) {
            listFind.find("p span#prevPageFind")
                    .css("cursor","pointer").css("color","blue").css("text-decoration","underline");
          } else {
            listFind.find("p span#prevPageFind")
                    .css("cursor","text").css("color","black").css("text-decoration","none");
          }
          if(data.length > maxRecordPerPageFind) {
            listFind.find("p span#nextPageFind")
                    .css("cursor","pointer").css("color","blue").css("text-decoration","underline");
          } else {
            listFind.find("p span#nextPageFind")
                    .css("cursor","text").css("color","black").css("text-decoration","none");
          }
          $("div#mdl-find").css("opacity","1");
          loadingStateChange("hide");
        } else {
          modalCommonShow(3,"Loading table value json unsuccessfully: status = " + status);
        }
      }).fail(function(reqObj,status) {
        modalCommonShow(2,"Failed to load table value json: ", reqObj);
      }).error(function(reqObj,status) {
        modalCommonShow(1,"Error loading table value json: ", reqObj);
      });
    };
    checkCurrSessAndExec(execFunc);
  }
  /* *** */
});
  
/****** Angular Section ******/

//Setup angular modul and controller
var adminApp = angular.module("adminApp",[]);
adminApp.controller("adminCtrl", function($scope) {
  //init angular var
  $scope.dataList = [];
  $scope.dataFindList = [];
  $scope.columnsLength = 0;
  $scope.limitNum = 10;
  $scope.changeData = function(){
    //alert("ok");
  };
});
