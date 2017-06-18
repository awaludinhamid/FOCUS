/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
   
  loadingStateChangeAlt("show");
   
  /* VARIABLE DECLARATION */
  //
  var m = [80, 120, 20, 220], //svg padding to tree(up,left,down,right)
      minW = 2040, //min tree diagram width
      minH = 1020, //min tree diagram height
      w = 0, //tree diagram width
      h = 0, //tree diagram height
      cnt = 0; //node count
  var root, tree, diagonal, vis, tooltip, linktip, linGradRed, linGradGreen, linGradYellow, linGradCyan, linGradGrey, radGrad; //tree var holder
  var rCircle = 10, //circle node radiant
      radius = 600; //radial radius
  //
  var includeColumn = []; //storage of picked column to proceed
  var currKpiId, //current KPI
      currTableName = "", //current tabel name
      columns, //current column prop
      currOrderBy = "", //current order by
      resultPerPage = 10, //max record view per page
      pagePerLoad = 10, //max page view on loading
      currPageNo = 1, //page no of table to show on paging
      currWhereClause = "",
      currMaxPage, //maximum page of current data
      uid = $("div#logUser span#uid").text(), //current user name
      cnName = $("div#logUser span#cnname").text(), //current user complete name
      maxItemList = 20, //maximum item per dropdown list
      currOptPageNo = 1, //page no of table option to show on paging
      currColumnCode, //column code of current option
      maxColSize = 60, //maximum column size for all tables
      currParentKpi = 0, //current parent kpi to load
      fileName = "";  //nama file yg akan dibentuk
  //
  var currPageNoProg = 1, //page no of progress table to show on paging
      currMaxPageProg; //maximum page of progress table
  //
  var centerBackPosLeft = $(".center-back-pos").css("left"); //default position of center background
  
  /* *** */


  /* EXECUTION SEGMENT */
  showJson(getUrlParamValue(window.location.href,"kpiId"));
  
  /* *** */


  /* EVENT FUNCTION */

  //Click on kpi
  $("div#kpi-option>ul>li#selectkpi").click(function() {
    //uncomment this section when you want to pick kpi manual
     $("div#body, div#div-collapse").show();
     $(".left-front-pos,.center-front-pos,.right-front-pos,.left-back-pos,.right-back-pos,.menu-back-pos,.menu-front-pos").hide();
     $(".center-back-pos").css("left","0px");
     //setDefTreeView();
  });

  //Click on main, addition and column option select all
  $("div#main-option, div#addition-option>table tr, div.right-front-pos").on("click", "ul>li>span>label>input", function() {
    //unchecked all member when checked
    if(this.checked) {
      $("ul#"+$(this).parents("ul").attr("id")+">li>label>input").each(function() {
          if(this.checked)
            this.checked = false;
      });  
    }
  });

  //Click on main and addition option select each
  $("div#main-option, div#addition-option>table tr").on("click", "ul>li>label>input", function() {
    //unchecked select all when checked
    $(this).parents("ul").find("li>span>label>input").prop("checked",false);
  });
  
  //Click on column option select all
  $("div.right-front-pos").on("click", "ul>li>span>label>input", function() {
    //remove all columns in column storage first
    //if checked, fill storage with all columns then
    includeColumn.splice(0,includeColumn.length);
    if(this.checked) {
      for(var idx in columns)
        includeColumn.push(idx.toString());
    }
  });
  
  //Click on column option select each
  $("div.right-front-pos").on("click", "ul>li>label>input", function() {
    //if checked, if select all option checked then unchecked it and remove all columns in column storage, and finally add current column to storage 
    //otherwise, remove current column from storage
    if(this.checked) {
      if($(this).parents("ul").find("li>span>label>input").prop("checked")) {
        $(this).parents("ul").find("li>span>label>input").prop("checked",false);
        includeColumn.splice(0,includeColumn.length);
      }
      includeColumn.push(($(this).parents("li").index()-1).toString());
    } else {
      var idxArr = includeColumn.indexOf(($(this).parents("li").index()-1).toString());
      if(idxArr > -1)
        includeColumn.splice(idxArr,1);
    }
  });

  // Click on main data page number
  $("div#pagination ul").on("click", "li", function() {
    var thisId = this.id;
    //avoid execution on current page, disable page
    //page no must be less or equal then maximum page allowed
    //reload data on clicked
    if(!$("div#pagination ul li#"+thisId).hasClass("active") && !$("div#pagination ul li#"+thisId).hasClass("disabled") && currPageNo <= currMaxPage) {
      if(thisId === "firstPage")
        currPageNo = 1;
      else if(thisId === "nextPage")
        currPageNo += 1;
      else if(thisId === "prevPage")
        currPageNo -= 1;
      else if(thisId === "lastPage")
        currPageNo = currMaxPage;
      else
        currPageNo = parseInt(thisId.replace("page",""));
      var execFunc = function() {
        $.post("../../apps/keypro/data/content",{tableName: currTableName, pageNo: currPageNo, whereClause: currWhereClause},
        function(data,status) {
          if(status == "success") {
            generateData(data); 
          } else {
            modalCommonShow(3,"Get data content unsuccessfully: status = " + status);
          }
        }).fail(function(d) {
          modalCommonShow(2,"Failed to get data content: ", d);
        }).error(function(d) {
          modalCommonShow(1,"Error get data content: ", d);
        });
      };
      checkCurrSessAndExec(execFunc);
    }
  });

  // Click on progress data page number
  $("div#pagination-progress ul").on("click", "li", function() {
    var thisId = this.id;
    //avoid execution on current page, disable page
    //page no must be less or equal then maximum page allowed
    //reload data on clicked
    if(!$("div#pagination-progress ul li#"+thisId).hasClass("active") &&
            !$("div#pagination-progress ul li#"+thisId).hasClass("disabled") && currPageNoProg <= currMaxPageProg) {
      if(thisId === "firstPage")
        currPageNoProg = 1;
      else if(thisId === "nextPage")
        currPageNoProg += 1;
      else if(thisId === "prevPage")
        currPageNoProg -= 1;
      else if(thisId === "lastPage")
        currPageNoProg = currMaxPageProg;
      else
        currPageNoProg = parseInt(thisId.replace("page",""));
      generateDataProg();
    }
  });
  
  //Enter mouse on addition option list
  $("div#addition-option>table tr").on("mouseenter", "td>ul", function() {
    //show all members list, hide horizontal bar, and blur other view and fixed their position 
    $(this).find("li:not([id*=select])").each(function() {
      $(this).fadeIn();
    });
    $("div#addition-option,div#data div:not(#pagination)").css("overflow-x","hidden");
    $("div#data").css("opacity","0.1").css("pointer-events","none").css("position","fixed").css("top","140px");
    $(".left-front-pos").css("opacity","0.1").css("position","fixed");
    $(".right-front-pos").css("opacity","0.1").css("position","fixed");
  });
  //Leave mouse from addition option list
  $("div#addition-option>table tr").on("mouseleave", "td>ul", function() {
    //hide all members list, show horizontal bar if necessary, and restore other view appearance and position
    $(this).find("li:not([id*=select])").each(function() {
      $(this).hide();
    });
    $("div#addition-option,div#data div:not(#pagination)").css("overflow-x","auto");
    $("div#data").css("opacity","1").css("pointer-events","").css("position","absolute").css("top","");
    $(".left-front-pos").css("opacity","1").css("position","absolute");
    $(".right-front-pos").css("opacity","1").css("position","absolute");
  });
    
  //Click on button created file
  $("button#btn-download").on("click", function() {
    //show confirmation
    var date = new Date();
    fileName = "kpi_"+
            $("div#kpi-option>ul>li#selectkpi>span").text().trim().replace(/\s/g,"_").replace(/\W/,"").toLowerCase()+
            "_"+date.getFullYear()+""+padZero(date.getMonth()+1,2)+""+padZero(date.getDate(),2)+
            "_"+padZero(date.getHours(),2)+""+padZero(date.getMinutes(),2)+""+padZero(date.getSeconds(),2)+
            "_"+uid;
    $("div#mdl-confirm-create-file>.modal-dialog>.modal-content>.modal-body>input").val(fileName);
    $("div#mdl-confirm-create-file").modal("show");
  });
    
  //Click to proceed file creation
  $("div#mdl-confirm-create-file div.modal-footer>button#btn-yes").on("click", function() {
    //init file name
    //prepare column selection original statement (without transform to text), column selection statement (with tranform to text) and sql query
    fileName = $("div#mdl-confirm-create-file>.modal-dialog>.modal-content>.modal-body>input").val() + ".csv";
    var columnSelectOrig = "";
    var columnSelect = "";
    var delimiter = ",";
    for(var idx in columns) {
      if($.inArray(idx.toString(),includeColumn) > -1) {
        columnSelectOrig = columnSelectOrig + columns[idx].columnName + delimiter;
        var columnName = "";
        if(columns[idx].dataType === "DATE")
          columnName = "TO_CHAR("+columns[idx].columnName+",'DD-MON-YYYY')";
        else if(columns[idx].dataType === "NUMBER")
          columnName = "TO_CHAR("+columns[idx].columnName+")";
        else
          columnName = columns[idx].columnName;
        columnSelect = columnSelect + columnName + delimiter;
      }
    }
    columnSelectOrig = columnSelectOrig.substring(0,columnSelectOrig.lastIndexOf(delimiter));
    for(var idx = includeColumn.length; idx < maxColSize; idx++)
      columnSelect = columnSelect + "NULL col" + (idx+1) + delimiter;
    columnSelect = columnSelect.substring(0,columnSelect.lastIndexOf(delimiter));
    var query = "SELECT " + columnSelect +
                " FROM " + currTableName +
                " WHERE " + currWhereClause +
                " ORDER BY " + currOrderBy;
    //request to execute job
    var execFunc = function() {
      $.get("../../apps/keypro/data/execjob",{
        userName: uid, fileName: fileName, query: query, columns: columnSelectOrig, receipt: cnName},
      function(data,status) {
        if(status == "success") {
          //job info
          $("div#mdl-exec-create-file div.modal-body>p>span").html(
            "<br/>"+
            "File Name: <b>"+fileName+"</b><br/>"+
            "Process/job id: <b>"+data+"</b>"
          );
          $("div#mdl-exec-create-file").modal("show");
          loadingStateChangeAlt("hide");  
        } else {
          modalCommonShow(3,"Execution job unsuccessfully: status = " + status);
        }
      }).fail(function(d) {
        modalCommonShow(2,"Failed to execute job: ", d);
      }).error(function(d) {
        modalCommonShow(1,"Error execute job: ", d);
      });
    };
    checkCurrSessAndExec(execFunc);
  });
  
  //Click on search addition option
  $("div#addition-option>table tr").on("click", "ul>li>span:has(i)", function() {
    //define what option to search, clear list content and search to default
    currColumnCode = $(this).parents("ul").attr("id");
    $("div#mdl-search-list input#search-text").val("");
    $("div#mdl-search-list table#search-table td ul#selected-list li").remove();
    var listData = $("div#mdl-search-list div#search-list");
    listData.find("p span#prevPageFind, p span#nextPageFind").css("cursor","text").css("color","black").css("text-decoration","none");
    var scope = listData.scope();
    scope.dataFindList = [];
    scope.$apply();
    //add checked option to selected list search
    $("div#addition-option ul#"+currColumnCode+">li:not([id*=select],[id*=search])>label>input").each(function() {
      if(this.checked) {
        $("div#mdl-search-list table#search-table td ul#selected-list").append(
          "<li>"+         
            "<label style='margin: 0px'>"+
              "<input id='"+this.id+"' type='checkbox' style='margin: 0px'>&nbsp;"+
                breakLongText($(this).parent().text().trim())+
            "</label>" +
          "</li>"
        );
      }
    });
    $("label#find-select-all").hide();
    $("label#selected-select-all").hide();
    $("div#mdl-search-list").modal("show");
  });
  
  //Press enter button to execute searching process
  $("div#mdl-search-list input#search-text").keypress(function(e) {
    if(e.which === 13) {
      currOptPageNo = 1;
      loadFindData();
    }
  });

  // Click button previous and next page find
  $("p span#prevPageFind, p span#nextPageFind").click(function() {
    if($(this).css("cursor") === "pointer") {
      //set page no by item clicked/enter
      if(this.id.indexOf("prev") > -1)
        currOptPageNo--;
      else
        currOptPageNo++;
      loadFindData();
    }
  });
  
  //Click button switch right when searching to shift data from left to right
  $("div#mdl-search-list table#search-table td button#btn-switch-right").click(function() {
    var selectedCount = 0; //number to check selected option amount
    //counting on checked
    $("div#search-list ul#data-list-find li>label>input").each(function() {
      if(this.checked)
        selectedCount++;
    });
    //counting may not exceeded maximum option selected available
    //if allowed, shift checked option from left to right listing and reload searched data
    if(selectedCount + $("div#mdl-search-list table#search-table td ul#selected-list li").length > maxItemList) {
      alert("Exceeded maximum selected option (20 items)");
    } else {
      $("div#search-list ul#data-list-find li>label>input").each(function() {
        if(this.checked) {
          $("div#mdl-search-list table#search-table td ul#selected-list").append(
            "<li>"+         
              "<label style='margin: 0px'>"+
                "<input id='"+this.id+"' type='checkbox' style='margin: 0px'>&nbsp;"+
                  breakLongText($(this).parent().text().trim())+
              "</label>" +
            "</li>"
          );
        }
      });
      loadFindData();
    }
  });
  
  //Click button switch left when searching to return data from right to left
  $("div#mdl-search-list table#search-table td button#btn-switch-left").click(function() {
    //if checked, return checked option from right to left listing and reload searched data
    $("ul#selected-list li>label>input").each(function() {
      if(this.checked) {
        $(this).parents("li").remove();
      }
    });
    loadFindData();
  });
  
  //Click on proceed button to re-execute data view when change the option
  $("div#btn-menu button#btn-proceed").click(function() {    
    currPageNo = 1;
    //define current where clause
    setWhereClause();
    //requesting data view
    var execFunc = function() {
      $.post("../../apps/keypro/data/content",{tableName: currTableName, pageNo: currPageNo, whereClause: currWhereClause},
      function(data,status) {
        if(status == "success") {
          generateData(data); 
        } else {
          modalCommonShow(3,"Get data content unsuccessfully: status = " + status);
        }
      }).fail(function(d) {
        modalCommonShow(2,"Failed to get data content: ", d);
      }).error(function(d) {
        modalCommonShow(1,"Error get data content: ", d);
      });
    };
    checkCurrSessAndExec(execFunc);
  });
  
  //Click on ok button when searching to confirm selected option
  $("div#mdl-search-list div.modal-footer button#btn-ok").click(function() {
    //clear list to default
    //add option list from selected option
    $("div#addition-option ul#"+currColumnCode+">li:not([id*=select],[id*=search])").remove();
    $("ul#selected-list li>label>input").each(function() {
      $("div#addition-option ul#"+currColumnCode).append(
        "<li hidden>"+
          "<label class='btn btn-default btn-sm' style='padding-top: 1px; padding-bottom: 1px'>"+
            "<input id='"+this.id+"' type='checkbox' value='"+this.id+"' checked/>&nbsp;&nbsp;"+
            breakLongText($(this).parent().text().trim())+
          "</label>"+
        "</li>"
      );
    });
    //if there are selected option unchecked select all option, otherwise checked it
    if($("ul#selected-list li>label>input").length > 0)
      $("div#addition-option ul#"+currColumnCode).find("li>span>label>input").prop("checked",false);
    else
      $("div#addition-option ul#"+currColumnCode).find("li>span>label>input").prop("checked",true);
  });
  
  //Click on progress button to launch progress monitoring list
  $("div#btn-menu button#btn-progress").click(function() {
    generateDataProg();
    $("div#mdl-show-progress").modal("show");
  });
  
  //Click on refresh button in progress section to refresh progress data view 
  $("div button#btn-refresh").click(function() {
    generateDataProg();
  });
  
  //Click on select all option on searching to checked/unchecked search/selected option list
  $("label#find-select-all>input,label#selected-select-all>input").click(function() {
    var currUl = $(this).parents("td").find("ul");
    if(this.checked) {
      currUl.children("li").each(function() {
        $(this).find("label>input").prop("checked",true);
      });
    } else {
      currUl.children("li").each(function() {
        $(this).find("label>input").prop("checked",false);
      });      
    }
  });
  
  $("div#kpi-option>ul>li#main-kpi").click(function() {
    currParentKpi = 0;
    showJson("");
  });
  
  $("div#kpi-option>ul>li#det-kpi").click(function() {
    currParentKpi = currKpiId;
    showJson("");
  });   
    
  //click on expand/collapse button
  $("#div-collapse button").click(function() {
    //toggle between button (expand or collapse), identify through its current css class
    if($(this).children("span:first-child").hasClass("glyphicon-resize-small")) {
      $(this).children("span:last-child").text("Expand All");
      collapseAll();
    } else {
      $(this).children("span:last-child").text("Collapse All");
      expandAll();
    }
    $(this).children("span:first-child").toggleClass("glyphicon-resize-small").toggleClass("glyphicon-resize-full");
  });
  
  /* *** */


  /* DATA MANIPULATION FUNCTION */

  // Grnerate option list (main, addition adn column>
  function generateOption() {
    loadingStateChangeAlt("show");
    var execFunc = function() {
      $.get("../../apps/keypro/data/kpicolumn",{kpiId: currKpiId, pageNo: currOptPageNo},function(data,status) {
        if(status == "success" && data !== null && !jQuery.isEmptyObject(data)) {
          //init data, generate and add main and addition option list
          $("div#main-option ul").remove();
          $("div#addition-option>table tr td").remove();
          for(var idx in data) {
            //main option
            if(data[idx].type == 1) {
              $("div#main-option").append(
                "<ul id='"+data[idx].code+"' class='list-unstyled' data-type='"+data[idx].dataType+"'>"+ 
                  "<li id='select"+data[idx].code+"'>"+
                    "<span id='spanselect"+data[idx].code+"' class='btn btn-primary btn-sm glyphicon glyphicon-tags'>"+
                      "&nbsp;&nbsp;"+breakLongText(data[idx].name)+
                      "<label style='position: absolute; right: 10px'>"+
                        "<input id='all"+data[idx].code+"' type='checkbox' value='all"+data[idx].code+"' checked/>&nbsp;All"+
                      "</label>"+
                    "</span>"+
                  "</li>"+
                "</ul>"
              );
              var detailList = data[idx].listProp.listOptions;
              for(var idx1 in detailList) {
                $("div#main-option ul#"+data[idx].code).append(
                  "<li>"+
                    "<label class='btn btn-default btn-sm' style='padding-top: 1px; padding-bottom: 1px'>"+
                      "<input id='"+detailList[idx1].code+"' type='checkbox' value='"+detailList[idx1].code+"'/>&nbsp;&nbsp;"+
                      breakLongText(detailList[idx1].name)+
                    "</label>"+
                  "</li>"
                );
              }
            //additional option
            } else {
              $("div#addition-option>table tr").append(
                "<td style='padding: 3px'>"+
                  "<ul id='"+data[idx].code+"' class='list-unstyled' data-type='"+data[idx].dataType+"'>"+ 
                    "<li id='select"+data[idx].code+"'>"+
                      "<span id='spanselect"+data[idx].code+
                        "' class='btn btn-primary btn-sm glyphicon glyphicon-triangle-bottom'>&nbsp;&nbsp;"+
                        breakLongText(data[idx].name)+
                        "<label style='position: absolute; right: 10px'>"+
                          "<input id='all"+data[idx].code+"' type='checkbox' value='all"+data[idx].code+"' checked/>&nbsp;All"+
                        "</label>"+
                      "</span>"+
                    "</li>"+
                  "</ul>"+
                "</td>"
              );
              if(data[idx].listProp.recordCount > maxItemList) {
                $("div#addition-option table tr td ul#"+data[idx].code).append(
                  "<li hidden id='search"+data[idx].code+"'>"+
                    "<span class='btn btn-default btn-sm glyphicon glyphicon-search' style='word-spacing: -4px'>"+
                      "&nbsp;<i>Pick options.. (All result > 20)</i>"+
                    "</span>"+
                  "</li>"
                );
              } else {
                var detailList = data[idx].listProp.listOptions;
                for(var idx1 in detailList) {
                  $("div#addition-option table tr td ul#"+data[idx].code).append(
                    "<li hidden>"+
                      "<label class='btn btn-default btn-sm' style='padding-top: 1px; padding-bottom: 1px'>"+
                        "<input id='"+detailList[idx1].code+"' type='checkbox' value='"+detailList[idx1].code+"'/>&nbsp;&nbsp;"+
                        breakLongText(detailList[idx1].name)+
                      "</label>"+
                    "</li>"
                  );
                }
              }
            }
          }
          setWhereClause();
          //column list & data populate
          $.post("../../apps/keypro/data/content",{tableName: currTableName, pageNo: currPageNo, whereClause: currWhereClause},function(data,status) {
            if(status == "success") {
              //init data, view and column storage
              //generate column list and data
              columns = data.columns;
              currOrderBy = data.orderBy;
              $("div.right-front-pos ul li:not([id*=select])").remove();
              $("div.right-front-pos ul li[id*=select]").find("span>label>input").prop("checked",true);
              includeColumn.splice(0,includeColumn.length);
              for(var idx in columns) {
                includeColumn.push(idx.toString());
                $("div.right-front-pos ul").append(
                  "<li>"+
                    "<label class='btn btn-default btn-sm' style='padding-top: 1px; padding-bottom: 1px'>"+
                      "<input id='"+columns[idx].columnName+"' type='checkbox' value='"+columns[idx].columnName+"'/>&nbsp;&nbsp;"+
                      breakLongText(columns[idx].columnName.replace(/_/g," "))+
                    "</label>"+
                  "</li>"
                );
              }
              generateData(data);  
            } else {
              modalCommonShow(3,"Get data content unsuccessfully: status = " + status);
            }
          }).fail(function(d) {
            modalCommonShow(2,"Failed to get data content: ", d);
          }).error(function(d) {
            modalCommonShow(1,"Error get data content: ", d);
          });
        } else {
          modalCommonShow(3,"Get column prop unsuccessfully: status = " + status);
        }
      }).fail(function(d) {
        modalCommonShow(2,"Failed to get column prop: ", d);
      }).error(function(d) {
        modalCommonShow(1,"Error get column prop: ", d);
      });
    };
    checkCurrSessAndExec(execFunc);
  }

  // Generate data to view
  function generateData(dataTemp) { 
    loadingStateChangeAlt("show");
    //data filling
    var contents = dataTemp.data;
    //generate header fit selected column
    $("table#data-table>thead>tr>th").remove();
    for(var idx in columns) {
      if($.inArray(idx.toString(),includeColumn) > -1)
        $("table#data-table>thead>tr").append(
          "<th>"+columns[idx].columnName.replace(/_/g," ")+"</th>"
        );
    }  
    //generate data via angular
    //show only data belongs to selected column
    var listData = $("table#data-table>tbody");
    var scope = listData.scope();
    scope.dataList = contents;
    scope.$apply();
    listData.find("tr>td").each(function() {
      if($(this).index() > columns.length - 1)
        $(this).hide();
      if($.inArray($(this).index().toString(),includeColumn) === -1)
        $(this).hide();
    });
    //rearrange pagination
    setPagination(dataTemp.recordCount);
    switchPageStatus();
    loadingStateChangeAlt("hide");
  }

  // Generate data to view in progress section
  function generateDataProg() { 
    loadingStateChangeAlt("show","div#mdl-show-progress");
    //data filling
    var execFunc = function() {
      $.get("../../apps/keypro/data/submitjob",{userName: uid, pageNo: currPageNoProg},function(data,status) {
        if(status == "success") {
          //generate data via angular
          var listData = $("table#job-table>tbody");
          var scope = listData.scope();
          scope.dataProgList = data.data;
          scope.$apply();
          //rearrange pagination
          setPaginationProg(data.recordCount);
          switchPageStatusProg();
          loadingStateChangeAlt("hide","div#mdl-show-progress");   
        } else {
          modalCommonShow(3,"Submit job unsuccessfully: status = " + status);
        }
      }).fail(function(d) {
        modalCommonShow(2,"Failed to submit job: ", d);
      }).error(function(d) {
        modalCommonShow(1,"Error submit job: ", d);
      });
    };
    checkCurrSessAndExec(execFunc);
  }

  // Show data tree through json
  function showJson(kpiIdTemp) {
    var execFunc = function() {
      $.get("../../apps/keypro/data/kpi",{parentKpi: currParentKpi},function(data,status) {
        if(status == "success" && data !== null && !jQuery.isEmptyObject(data)) {
          //kpi initialization
          var initKpi;
          if(kpiIdTemp === "") {
            //uncomment when using tree data
            updateTree(data); 
            $("div#body, div#div-collapse").show();
            $(".left-front-pos,.center-front-pos,.right-front-pos,.left-back-pos,.right-back-pos,.menu-back-pos,.menu-front-pos").hide();
            $(".center-back-pos").css("left","0px");
            setDefTreeView();
            loadingStateChangeAlt("hide");
          } else {
            initKpi = traverseNode(data,"kpiId",parseInt(kpiIdTemp));
            $("div#body, div#div-collapse").hide();
            $(".left-front-pos,.center-front-pos,.right-front-pos,.left-back-pos,.right-back-pos,.menu-back-pos,.menu-front-pos").show();
            $(".center-back-pos").css("left",centerBackPosLeft);
            //set kpi name and fact/table name, regenerate the option
            $("div#kpi-option>ul>li#selectkpi>span").html(breakLongText(initKpi.kpiName));
            currKpiId = kpiIdTemp;
            currTableName = initKpi.factName;
            generateOption();
          }
        } else {
          modalCommonShow(3,"Get kpi data unsuccessfully: status = " + status);
        }
      }).fail(function(d) {
        modalCommonShow(2,"Failed to get kpi data: ", d);
      }).error(function(d) {
        modalCommonShow(1,"Error getting kpi data: ", d);
      });
    };
    checkCurrSessAndExec(execFunc);
  }

  //update tree object and data view
  function updateTree(treeLinked) {
    //clean tree contanier
    d3.selectAll("#body").remove();
    $("body").append("<div id='body'></div>");
    //save json data to temporer var, init variable needed (including window size)
    //show first children only, update view, and restore scroll position
    root = treeLinked;
    initTreeVariable();
    update(root);
  }

  //update tree object
  function update(source) {
    var duration = d3.event && d3.event.altKey ? 5000 : 500;  //node transition duration
    //Compute the new tree layout
    var nodes = tree
      .nodes(root)
      .reverse();
    // Update the nodes?
    var node = vis
      .selectAll("g.node")
      .data(nodes, function(d) {return d.kpiId || (d.kpiId = ++cnt);});
    // Enter any new nodes at the parent's previous position.
    var nodeEnter = node
      .enter()
      .append("svg:g")
      .attr("class", "node")
      .attr("transform", function(d) {return "translate(" + source.y0 + "," + source.x0 + ")";});

    //put growth value on foreignObject or text node (if IE browser) and its properties
    if((navigator.userAgent.indexOf("MSIE") != -1 ) || (!!document.documentMode == true )) {//IF IE  
      nodeEnter
        .append("svg:rect")
        .attr("width", function(d) {if(d == root) return 0; return (d.kpiName.length >= 25 ? 25 : d.kpiName.length) * 6 + 32;})
        .attr("height", function(d) {if(d == root) return 0; return 20 * getLine(d.kpiName);})
        .attr("rx", 5)
        .attr("ry", 5)
        .style("fill", "grey")
        .style("cursor", function(d) {return d == root ? "default" : "pointer";})
        .attr("y", "7")
        .attr("x", "0")
        //regenerate option and data if fact table exist
        .on("click",function(d) {
          if(d.color === "green") {
            $("div#kpi-option>ul>li>span#spanselectkpi").html("&nbsp;&nbsp;"+breakLongText(d.kpiName));
            $("div#body, div#div-collapse").hide();
            $(".left-front-pos,.center-front-pos,.right-front-pos,.left-back-pos,.right-back-pos,.menu-back-pos,.menu-front-pos").show();
            $(".center-back-pos").css("left",centerBackPosLeft);
            currTableName = d.factName;
            currPageNo = 1;
            currKpiId = d.kpiId;
            generateOption();
          } else {
            showMessage("W","Object table must be created first!")
          }
        });
      nodeEnter
        .append("svg:text")
        .attr("fill","white")
        .attr("y", 0)
        .attr("x", 0)
        .style("font-size","10px")
        .attr("data-txtLine",function(d) {
          if(d == root)
            return "";
          var txtLine = getLine(d.kpiName);
          var txtArr = (breakLongText(d.kpiName)+"<br/>").split("<br/>");
          for(var idx = 0; idx < txtLine; idx++) {
            d3.select(this)
            .append("svg:tspan")
            .attr("x", 15)
            .attr("dy", 20)
            .text(txtArr[idx]);            
          }
          return txtLine;
        });
    } else {
      nodeEnter
        .append("svg:foreignObject")
        .attr("width", function(d) {return ((d.kpiName.length >= 25 ? 25 : d.kpiName.length) * 6 + 32) + "px";})
        .attr("height", function(d) { var txtLine = getLine(d.kpiName); return d == root ? "0px" : (txtLine*23+10)+"px";})
        .attr("id","foreignObject")
        .attr("y", "0.6em")
        .attr("x", "-0.2em")
        .style("opacity","0.8")
        .html(function(d) {
          return "<button class='btn btn-primary btn-sm' style='font-size: 10px; padding: 3px 10px'>"+breakLongText(d.kpiName)+"</button>";
        })
        //regenerate option and data if fact table exist
        .on("click",function(d) {
          if(d.color === "green") {
            $("div#kpi-option>ul>li#selectkpi>span").html("&nbsp;&nbsp;"+breakLongText(d.kpiName));
            $("div#body, div#div-collapse").hide();
            $(".left-front-pos,.center-front-pos,.right-front-pos,.left-back-pos,.right-back-pos,.menu-back-pos,.menu-front-pos").show();
            $(".center-back-pos").css("left",centerBackPosLeft);
            currTableName = d.factName;
            currPageNo = 1;
            currKpiId = d.kpiId;
            generateOption();
          } else {
            showMessage("W","Object table must be created first!")
          }
        });
    }
    //polyline nodes and its properties
    nodeEnter.append("svg:polygon")
      .attr("points", "10,-3 10,3 11,3 11,7 16,0 11,-7 11,-3")
      .attr("fill","grey");
    //circle nodes and its properties
    nodeEnter
      .append("svg:circle")
      .attr("r", rCircle)
      .on("click", function(d) {
        if(d != root) {toggle(d);update(d);}
      })
      .style("fill", function(d){return d == root ? "url(#cyan)":"url(#"+d.color+")";})
      .style("cursor", function(d) {return d._children ? "pointer" : "default";});

    // Transition nodes to their new position.
    var nodeUpdate = node.transition()
      .duration(duration)
      .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; });
    nodeUpdate.selectAll("foreignObject,rect,text")
      .attr("transform", function(d) {
        var txtLine = getLine(d.kpiName);
        var rotateDeg = d.x - 90;
        var nameLength = d.kpiName.length;
        if(txtLine > 1) {
          var arrName = (breakLongText(d.kpiName)+"<br/>").split("<br/>");
          var nameLengthTemp = 0;
          for(var idx in arrName) {
            if(arrName[idx].length > nameLengthTemp)
              nameLengthTemp = arrName[idx].length;
          }
          nameLength = nameLengthTemp;
        }
        if((navigator.userAgent.indexOf("MSIE") != -1 ) || (!!document.documentMode == true )) {//IF IE
          if(txtLine > 1)
            nameLength = 25;
          return "rotate(" + (-rotateDeg) + ")" +
                (rotateDeg < -90 || rotateDeg > 90 ?
                  "translate(-" + (nameLength*6 + 30) + ",-" +
                  (15 + (19*txtLine)) + ")" : "");
        }
        return "rotate(" + (-rotateDeg) + ")" +
                (rotateDeg < -90 || rotateDeg > 90 ?
                  "translate(-" + (nameLength*6 + 20) + ",-" +
                  (25 + (15*txtLine)) + ")" : "");});
    //update polyline
    nodeUpdate.select("polygon")
      .style("opacity",function(d){return d._children ? ".8":"0";});

    // Transition exiting nodes to the parent's new position.
    var nodeExit = node.exit().transition()
      .duration(duration)
      .attr("transform", function(d) {return "translate(" + source.y + "," + source.x + ")";})
      .remove();
    //exit circle properties
    nodeExit.select("circle")
      .attr("r", 1e-6);

    // Update the links?
    var link = vis.selectAll("path.link")
      .data(tree.links(nodes), function(d) {return d.target.id;});
    // Enter any new links at the parent's previous position.
    link.enter().insert("svg:path", "g")
      .attr("class", "link")
      .attr("d", function(d) {
              var o = {x: source.x0, y: source.y0};
              return diagonal({source: o, target: o});
      })
      .transition()
      .duration(duration)
      .attr("d", diagonal);
    // Transition links to their new position.
    link.transition()
      .duration(duration)
      .attr("d", diagonal);
    // Transition exiting nodes to the parent's new position.
    link.exit().transition()
      .duration(duration)
      .attr("d", function(d) {
        var o = {x: source.x, y: source.y};
        return diagonal({source: o, target: o});
      })
      .remove();

    // Stash the old positions for transition.
    nodes.forEach(function(d) {
      d.x0 = d.x;
      d.y0 = d.y;
      });
  }

  /*function toggleAll(d) {
    if (d.children) {
      d.children.forEach(toggleAll);
      toggle(d);
    }
  }*/

  // Toggle children.
  function toggle(d) {
    if (d.children) {
      d._children = d.children;
      d.children = null;
    } else {
      d.children = d._children;
      d._children = null;
    }
  }
    
  //expand children only
  function expand(d){   
    var children = (d.children)?d.children:d._children;
    if (d._children) {        
        d.children = d._children;
        d._children = null;       
    }
    if(children)
      children.forEach(expand);
  }

  //toggle children only
  function collapse(d) {
    if(d.children) {
      d._children = d.children;
      d._children.forEach(collapse);
      d.children = null;
    }
  }

  //expand children of all nodes
  function expandAll(){
      expand(root); 
      update(root);
  }

  //collapse children of all nodes
  function collapseAll(){
    root.children.forEach(collapse);
    //collapse(root);
    update(root);
  }

  function initTreeVariable() {
      //init line
      diagonal = d3
        .svg
        .diagonal
        .radial()
        .projection(function(d) {return [d.y, d.x/180*Math.PI];});
      //init tooltip
      tooltip = d3
        .select("#body")
        .append("div") //declare the tooltip div
        .attr("class", "tooltip") //apply the 'tooltip' class
        .style("opacity", 0);
      linktip = d3
        .select("#body")
        .append("div") 
        .attr("class", "tooltip") 
        .style("opacity", 0);
      linktip.style("display","none");//remark to show this object
      //sizing tree
      var depth = 0;
      var depthTemp = 0;
      var nodeCount = 0;
      setDepth(root);
      setNodeCount(root);
      var tempR = (rCircle+radius)*depth;
      w =  tempR > minW ? tempR : minW;
      h = tempR > minH ? tempR : minH;
      root.x0 = h / 2;
      root.y0 = 0;
      tree = d3
        .layout
        .tree()
        .size([360, (nodeCount < 10 ? radius/3 : (nodeCount < 60 ? radius/3 : radius))]);
      vis = d3
        .select("#body")
        .append("svg:svg")
        .attr("width", w + m[1] + m[3])
        .attr("height", h + m[0] + m[2])
        .append("svg:g")
        .attr("transform", "translate(" + w/2 + "," + h/2 + ")");
      //gradient color
      linGradRed = vis
        .append("svg:defs")
        .append("svg:linearGradient")
        .attr("id","red")
        .attr("x1","70%")
        .attr("y1","80%")
        .attr("x2","10%")
        .attr("y2","0%")
        .attr("spreadMethod", "pad");
      linGradRed
        .append("svg:stop")
        .attr("offset","0%")
        .attr("style","stop-color:red;stop-opacity:1");
      linGradRed
        .append("svg:stop")
        .attr("offset","100%")
        .attr("style","stop-color:white;stop-opacity:1");
      linGradGreen = vis
        .append("svg:defs")
        .append("svg:linearGradient")
        .attr("id","green")
        .attr("x1","70%")
        .attr("y1","80%")
        .attr("x2","10%")
        .attr("y2","0%")
        .attr("spreadMethod", "pad");
      linGradGreen
        .append("svg:stop")
        .attr("offset","0%")
        .attr("style","stop-color:green;stop-opacity:1");
      linGradGreen
        .append("svg:stop")
        .attr("offset","100%")
        .attr("style","stop-color:white;stop-opacity:1");
      linGradYellow = vis
        .append("svg:defs")
        .append("svg:linearGradient")
        .attr("id","yellow")
        .attr("x1","70%")
        .attr("y1","80%")
        .attr("x2","10%")
        .attr("y2","0%")
        .attr("spreadMethod", "pad");
      linGradYellow
        .append("svg:stop")
        .attr("offset","0%")
        .attr("style","stop-color:yellow;stop-opacity:1");
      linGradYellow
        .append("svg:stop")
        .attr("offset","100%")
        .attr("style","stop-color:white;stop-opacity:1");
      linGradCyan = vis
        .append("svg:defs")
        .append("svg:linearGradient")
        .attr("id","cyan")
        .attr("x1","70%")
        .attr("y1","80%")
        .attr("x2","10%")
        .attr("y2","0%")
        .attr("spreadMethod", "pad");
      linGradCyan
        .append("svg:stop")
        .attr("offset","0%")
        .attr("style","stop-color:darkcyan;stop-opacity:1");
      linGradCyan
        .append("svg:stop")
        .attr("offset","100%")
        .attr("style","stop-color:white;stop-opacity:1");
      linGradGrey = vis
        .append("svg:defs")
        .append("svg:linearGradient")
        .attr("id","grey")
        .attr("x1","70%")
        .attr("y1","80%")
        .attr("x2","10%")
        .attr("y2","0%")
        .attr("spreadMethod", "pad");
      linGradGrey
        .append("svg:stop")
        .attr("offset","0%")
        .attr("style","stop-color:#444;stop-opacity:1");
      linGradGrey
        .append("svg:stop")
        .attr("offset","100%")
        .attr("style","stop-color:white;stop-opacity:1");
      radGrad = vis
        .append("svg:defs")
        .append("svg:radialGradient")
        .attr("id","radGrad")
        .attr("x1","70%")
        .attr("y1","80%")
        .attr("x2","10%")
        .attr("y2","0%")
        .attr("spreadMethod", "pad");
      radGrad
        .append("svg:stop")
        .attr("offset","0%")
        .attr("style","stop-color:green;stop-opacity:1");
      radGrad
        .append("svg:stop")
        .attr("offset","100%")
        .attr("style","stop-color:white;stop-opacity:1");

      //set how many tree node along a horizontal line
      function setDepth(obj) {
        if (obj.children) {
            obj.children.forEach(function (d) {
                depthTemp++;
                setDepth(d);
                if (depthTemp > depth) {
                    depth = depthTemp;
                }
                depthTemp = 0;
            });
        }
        depthTemp++;
      }
      
      function setNodeCount(obj) {
        if(typeof obj === "object") {
          nodeCount++;
          $.each(obj,function(key,val) {
            if(val !== null)
              setNodeCount(val);
          });
        }
      }
  }
  
  /* *** */

  /* SUPPORTING FUNCTION */
    
  // Set pagination based on data length
  function setPagination(dataLength) {
    //recreated paging list
    $("div#pagination ul li").remove();
    if(dataLength === 0) return false;
    $("div#pagination ul").append(
      "<li id='firstPage' title='First'><a href='#' aria-label='First'><span aria-hidden='true'>&laquo;</span></a></li>"+
      "<li id='prevPage' title='Previous'><a href='#' aria-label='Previous'><span aria-hidden='true'>&lt;</span></a></li>"
    );
   //set max page, start and end page no, and active page
    currMaxPage = Math.floor(dataLength/resultPerPage) + (dataLength%resultPerPage === 0 ? 0 : 1);
    var startPageNo = Math.floor(currPageNo/pagePerLoad) * pagePerLoad + (currPageNo%pagePerLoad === 0 ? 0 : 1); //start page no of serial page on page selected
    var endPageNo = currMaxPage < (startPageNo + pagePerLoad) ? currMaxPage : (startPageNo + pagePerLoad -1); //end page no of serial page on page selected
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
  
  // Set pagination based on data length on progress section
  function setPaginationProg(dataLength) {
    //recreated paging list
    $("div#pagination-progress ul li").remove();
    if(dataLength === 0) return false;
    $("div#pagination-progress ul").append(
      "<li id='firstPage' title='First'><a href='#' aria-label='First'><span aria-hidden='true'>&laquo;</span></a></li>"+
      "<li id='prevPage' title='Previous'><a href='#' aria-label='Previous'><span aria-hidden='true'>&lt;</span></a></li>"
    );
   //set max page, start and end page no, and active page
    currMaxPageProg = Math.floor(dataLength/resultPerPage) + (dataLength%resultPerPage === 0 ? 0 : 1);
    var startPageNo = Math.floor(currPageNoProg/pagePerLoad) * pagePerLoad + (currPageNoProg%pagePerLoad === 0 ? 0 : 1); //start page no of serial page on page selected
    var endPageNo = currMaxPageProg < (startPageNo + pagePerLoad) ? currMaxPageProg : (startPageNo + pagePerLoad -1); //end page no of serial page on page selected
    for(var idx = startPageNo; idx <= endPageNo; idx++) {
      $("div#pagination-progress ul").append(
        "<li id='page"+idx+"'"+(idx === currPageNoProg ? " class='active' " : "")+"><a href='#'>"+idx+"</a></li>"
      );
    }
    $("div#pagination-progress ul").append(
      "<li id='nextPage' title='Next'><a href='#' aria-label='Next'><span aria-hidden='true'>&gt;</span></a></li>"+
      "<li id='lastPage' title='Last'><a href='#' aria-label='Last'><span aria-hidden='true'>&raquo;</span></a></li>"
    );
  }

  // Switch page status
  function switchPageStatus() {
    if(currPageNo === 1)
      $("div#pagination li#firstPage").addClass("disabled");
    else
      $("div#pagination li#firstPage").removeClass("disabled");
    if(currPageNo > 1)
      $("div#pagination li#prevPage").removeClass("disabled");
    else
      $("div#pagination li#prevPage").addClass("disabled");
    if(currPageNo < currMaxPage)
      $("div#pagination li#nextPage").removeClass("disabled");
    else
      $("div#pagination li#nextPage").addClass("disabled");
    if(currPageNo === currMaxPage)
      $("div#pagination li#lastPage").addClass("disabled");
    else
      $("div#pagination li#lastPage").removeClass("disabled");
  }

  // Switch page status on progress section
  function switchPageStatusProg() {
    if(currPageNoProg === 1)
      $("div#pagination-progress li#firstPage").addClass("disabled");
    else
      $("div#pagination-progress li#firstPage").removeClass("disabled");
    if(currPageNoProg > 1)
      $("div#pagination-progress li#prevPage").removeClass("disabled");
    else
      $("div#pagination-progress li#prevPage").addClass("disabled");
    if(currPageNoProg < currMaxPageProg)
      $("div#pagination-progress li#nextPage").removeClass("disabled");
    else
      $("div#pagination-progress li#nextPage").addClass("disabled");
    if(currPageNoProg === currMaxPageProg)
      $("div#pagination-progress li#lastPage").addClass("disabled");
    else
      $("div#pagination-progress li#lastPage").removeClass("disabled");
  }

  //set tree view default position
  function setDefTreeView() {
    $(window).scrollLeft(w/4);
    $(window).scrollTop(h/4);
  }
  
  // Set where clause statement to limit data loading/printing
  function setWhereClause() {
    var whereDelim = ",";
    var thisIdTemp;
    currWhereClause = "";
    var idx = 0;
    $("div#main-option ul,div#addition-option ul").each(function() {
      var thisDataType = $(this).data("type");
      var whereClauseTemp = "";
      //if all option selected then allow all data processed (1 = 1 state.) to avoid longline statement
      //remember to transform all data type into character
      if($(this).find("li>span>label>input").prop("checked") === true) {
        whereClauseTemp = "1 = 1";
      } else {
        $(this).find("li>label>input").each(function() {
          if(this.checked) {
            if(thisDataType === "NUMBER")
              thisIdTemp = this.id;
            else
              thisIdTemp = "'"+this.id+"'";
            whereClauseTemp = whereClauseTemp + thisIdTemp + whereDelim;
          }
        });
      }
      if(whereClauseTemp === "1 = 1") {
        if(idx !== 0)
          currWhereClause = currWhereClause + " AND ";
        currWhereClause = currWhereClause + whereClauseTemp;
        idx++;
      } else if(whereClauseTemp !== "") {
        if(idx !== 0)
          currWhereClause = currWhereClause + " AND ";
        if(thisDataType === "DATE")
          thisIdTemp = "TO_CHAR(" + this.id + ",'DD-MON-YYYY')";
        else
          thisIdTemp = this.id;
        currWhereClause = currWhereClause + thisIdTemp + " IN (" + whereClauseTemp.substring(0,whereClauseTemp.lastIndexOf(whereDelim)) + ")";
        idx++;
      } else {
        currWhereClause = "1 = 2";
        return false;
      }
    });
    if(currWhereClause === "")
      currWhereClause = "1 = 1";
  }
  
  // Pad leading zero on number based on serial length
  function padZero(numTemp,leadZero) {
    var numStr = numTemp.toString();
    if(numStr.length === leadZero)
      return numStr;
    for(var idx = 0; idx < leadZero - numStr.length; idx++)
      numStr = "0" + numStr;
    return numStr;
  }

  // Load data on searching
  function loadFindData() {
    loadingStateChangeAlt("show","div#mdl-search-list");
    //generate exclude text statement
    var excludeText = "";
    var delimiter = ",";
    $("ul#selected-list li>label>input").each(function() {
        excludeText = excludeText + "'" + this.id + "'" + delimiter;
    });
    if(excludeText !== "")
      excludeText = "AND code NOT IN (" + excludeText.substring(0,excludeText.lastIndexOf(delimiter)) + ")";
    var execFunc = function() {
      $.get("../../apps/keypro/data/listoption",
        {columnCode: currColumnCode, pageNo: currOptPageNo, searchText: $("div#mdl-search-list input#search-text").val(), excludeText: excludeText},
        function(data,status) {  
        if(status == "success") {    
          //generate data using angular
          var listData = $("div#mdl-search-list div#search-list");
          var scope = listData.scope();
          scope.dataFindList = data.listOptions;
          scope.$apply();
          //apply format on previous and next page find
          if(currOptPageNo > 1) {
            listData.find("p span#prevPageFind")
                    .css("cursor","pointer").css("color","blue").css("text-decoration","underline");
          } else {
            listData.find("p span#prevPageFind")
                    .css("cursor","text").css("color","black").css("text-decoration","none");
          }
          if((data.recordCount/maxItemList) > currOptPageNo) {
            listData.find("p span#nextPageFind")
                    .css("cursor","pointer").css("color","blue").css("text-decoration","underline");
          } else {
            listData.find("p span#nextPageFind")
                    .css("cursor","text").css("color","black").css("text-decoration","none");
          }
          //unchecked select all option and show it if more than one record available
          if($("ul#data-list-find>li").length > 1) {
            $("label#find-select-all").show();
            $("label#find-select-all>input").prop("checked",false);
          } else {
            $("label#find-select-all").hide();
          }
          if($("ul#selected-list>li").length > 1) {
            $("label#selected-select-all").show();
            $("label#selected-select-all>input").prop("checked",false);
          } else {
            $("label#selected-select-all").hide();
          }
          loadingStateChangeAlt("hide","div#mdl-search-list"); 
        } else {
          modalCommonShow(3,"Load option data unsuccessfully: status = " + status);
        }
      }).fail(function(d) {
        modalCommonShow(2,"Failed to load option data: ", d);
      }).error(function(d) {
        modalCommonShow(1,"Error loading option data: ", d);
      });
    };
    checkCurrSessAndExec(execFunc);
  }
  
  /* *** */
});
  
/****** Angular Section ******/

//Setup angular modul and controller
var dataApp = angular.module("dataApp",[]);
dataApp.controller("dataCtrl", function($scope) {
  //init angular var
  $scope.dataList = [];
  $scope.dataFindList = [];
  $scope.dataProgList = [];
});