/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
    loadingStateChange("show");
    
    /* VARIABLE DECLARATION */
    //
    var m = [100, 80, 100, 80], //svg padding to tree(up,left,down,right)
        minW = 1360, //min tree diagram width
        minH = 768, //min tree diagram height
        w = 0, //tree diagram width
        h = 0, //tree diagram height
        cnt = 0; //node count
    var root, tree, diagonal, vis, tooltip, linktip, linGradRed, linGradGreen, linGradYellow, linGradBlue, linGradGrey, linGradPurple, radGrad; //tree var holder
    var rCircle = 15; //circle node radiant
    var lLine = 320; //hierarchy line length
    var linkTree = "";	//json file name
    var hoverFlag = 0;//avoid hover on click
    var delimiter = "_";//kpi param value separator
    var kpiDefTopPos = $(".app-def-position").css("top"), //default top position of apps
        kpiOptLastTopPos; //last top position of kpi option div
    //	
    var kpiBrkdwnFlag = 0, //0=by kpi 1=by area
        kpiDetFlag, //0=level sebelum child terakhir 1=level child terakhir
        memberShowFlag = 0; //0=current option show is not member 1=current option show is member
    //
    var arrFlag = [], //switch flagging, for hide/unhide item
        arrLayer = [], //parent-child collection, for hide/unhide child of parent
        arrVal = [{name: "dept",value:""},  // current option selected
                  {name: "members",value:""},
                  {name: "kpiCode",value:""},
                  {name: "dts",value:""},
                  {name: "coy",value:""},
                  {name: "lob",value:""},
                  {name: "initMembers",value:""}];
    //
    var arrZoomPoint = [{left:"0px",scale:0.4},
                        {left:"12px",scale:0.7},
                        {left:"27px",scale:1},
                        {left:"42px",scale:1.25},
                        {left:"54px",scale:1.5}];
    var currZoomPoint = 2;//init index of arrZoomPoint array
    var bodyProp;//holder of <body> property like width,height
    var extType;//download file extension
    //hardcode top position of hover info depend on various zooming, next time might be replaced with formulation found
    var arrTopPoint = {1:100,0.9:200,0.8:340,0.7:520,0.6:770,0.5:1100,0.4:1600,0.3:2420,0.2:4100,0.1:9100};
    /* *** */

    /* MEMBER & DEPARTMENT GENERATE */
    //load user detail json data
    var execFunc = function() {
      $.get("../../apps/data/profile",function(data,status) {
        if(status === "success") {
          //identify user authorization has been set
          if(data.length === 0) {
            $("#mdl-common #myModalLabel").html("<h3><span style='color: yellow' class='glyphicon glyphicon-exclamation-sign'>&nbsp;Warning</span></h3>");
            $("#mdl-common #content").text("Layer authorization must be set first");
            $("#mdl-common").modal("show"); 
            $(".app-def-position, #zoom-slider, #userMenu ul li:not('#idLogout,#idHome')").hide();
            return false;       
          }
          var deptTemp = ""; //dept/layer comparison while looping list
          //set current and initial (used when started new dept/layer) member/level
          setValue("members",data[0].memberListCode);
          setValue("initMembers",data[0].memberListCode);
          //labelling current user layer
          $("div#right-title span").html("<span class='"+glyTags+"'></span>&nbsp;&nbsp;Layer: "+
                  data[0].memberName+(data[0].memberListCode === "00000" ? "" : " "+data[0].memberListName));
          //flag to switch data [un]loading and flag to switch detail[breakdown] view
          arrFlag.push({code: "dept", flag: 0, detFlag: 0});
          //append/labelling/styling distinct dept/layer, show first item only and set current dept/layer
          $("#kpiSelect").append(
            "<ul id='dept' class='list-unstyled' title='Department'>"+
              "<li id='selectdept' hidden><span id='spanselectdept'>&nbsp;&nbsp;Department</span></li>"+
            "</ul>");
          $("#spanselectdept")
              .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
          for(var idx = 0; idx < data.length; idx++) {
            if(data[idx].deptCode !== deptTemp) {
              deptTemp = data[idx].deptCode;
              if(idx === 0) {
                createList("dept",deptTemp,data[idx].deptName,true);
                setValue("dept",deptTemp);
              } else {
                createList("dept",deptTemp,data[idx].deptName,false);
              }
            }
          }
          deptTemp = "";
          //generate member/level
          for(var idx = 0; idx < data.length; idx++) {
            //hide root member/level
            if(data[idx].memberCode.replace(data[idx].deptCode+"-","") != data[idx].parentMemberCode) {
              //flag to switch data [un]loading and flag to switch detail[breakdown] view
              arrFlag.push({code: data[idx].memberCode, flag: 0, detFlag: 0});
              //last member/level on list only view detail
              if(idx == data.length - 1) {
                setDetFlag(data[idx].memberCode,1);
              }else if(data[idx].deptCode != data[idx+1].deptCode) {
                setDetFlag(data[idx].memberCode,1);
              }
              //add parent-child member/level collection
              arrLayer.push({code: data[idx].memberCode, parent: data[idx].deptCode});
              //each high level member have children at all lower levels and must be member/level at same group
              for(var idx1 = idx - 1; idx1 > 0; idx1--) {
                if(data[idx].parentMemberCode == data[idx1].parentMemberCode && data[idx].deptCode == data[idx1].deptCode) {
                  arrLayer.push({code: data[idx].memberCode, parent: data[idx1].memberCode});
                }
              }
              //append member/level to the tags list, only first member/level should be display
              //each member has label, all list option (if necessery) and styling
              $("#kpiMember").append(
                "<ul id='"+data[idx].memberCode+"' class='list-unstyled' title='"+data[idx].memberName+"'"+(idx == 1 ? "" : " style='display: none'")+">"+
                  "<li id='select"+data[idx].memberCode+"' hidden><span id='spanselect"+data[idx].memberCode+"'>&nbsp;&nbsp;"+data[idx].memberName+"</span></li>"+
                  "<li id='all"+data[idx].memberCode+"'"+(data[idx].deptCode == deptTemp ? "" : " data-id='"+getValue("members")+"'")+
                    "><span id='spanall"+data[idx].memberCode+"'>&nbsp;&nbsp;"+breakLongText("All "+data[idx].memberName)+"</span></li>"+
                "</ul>");
              $("#spanselect"+data[idx].memberCode)
                  .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
              $("#spanall"+data[idx].memberCode)
                  .attr("class",pickStyle).css("text-align","left");//.css("background-color",bkColPick);
              //generate list option of member/level, execution is only one per dept/layer
              if(data[idx].deptCode != deptTemp) {
                var firstMemberCode = data[idx].memberCode;
                var memberList = window["list_"+firstMemberCode.replace(data[idx].deptCode+"-","")+"_"+data[idx].parentMemberCode+"_"+getValue("members")];
                for(var idx1 = 0; idx1 < memberList.length; idx1++) {
                        createList(firstMemberCode,memberList[idx1].code,memberList[idx1].name,false);
                }
                deptTemp = data[idx].deptCode;
              }
              kpiDetFlag = 0;
            } else {
              kpiDetFlag = 1;
            }
          }
          //generate option
          generateOption();
          //show data tree
          showJson();
        } else {
          modalCommonShow(3,"Loading profile json unsuccessfully: status = " + status);
        }
      }).fail(function(d) {
        modalCommonShow(2,"Failed to load profile json: ", d);
      }).error(function(d) {
        modalCommonShow(1,"Error loading profile json: ", d);
      });
    };
    checkCurrSessAndExec(execFunc);
    /* *** */

    /* OPTION GENERATE */
    function generateOption() {
      //append/labelling/styling distinct option
      for(var idx = 0; idx < list_option.length; idx++) {
        $("#kpiSelect").append(
          "<ul id='"+list_option[idx].code+"' class='list-unstyled' title='"+list_option[idx].name+"'>"+
            "<li id='select"+list_option[idx].code+"' hidden><span id='spanselect"+list_option[idx].code+
              "'>&nbsp;&nbsp;"+list_option[idx].name+"</span></li>"+
          "</ul>");
        $("#spanselect"+list_option[idx].code)
            .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
        //var optionList generated by other process, got from js/ folder, show first item only
        var optionList = window["list_"+list_option[idx].code];
        for(var idx1 = 0; idx1 < optionList.length; idx1++) {
          if(idx1 === 0) {
            setValue(list_option[idx].code,optionList[idx1].code);
            createList(list_option[idx].code,optionList[idx1].code,optionList[idx1].name,true);
          } else {
            createList(list_option[idx].code,optionList[idx1].code,optionList[idx1].name,false);
          }
        }
        //flag to switch data [un]loading
        arrFlag.push({code: list_option[idx].code, flag: 0});
      }
    }
    /* *** */

    /* ITEM CLICKED FUNCTION */
    //click on kpi option group (layer/coy/lob)
    $("#kpiSelect").on("click","ul li",function() {
      var thisId = this.id; //current item id
      var parentId = $(this).parent().attr("id"); //current parent (group)
      //label static, toggle list view and when current value different from previous one reload data
      //specific to dept group including change member/level item list
      if(thisId != "select"+parentId) {
        if(getFlag(parentId) == 1 && thisId != getValue(parentId)) {
          //set current group value
          setValue(parentId,thisId);
          if(parentId == "dept") {
            kpiDetFlag = getDetFlag("dept");
            setValue("members",getValue("initMembers"));
            //hide all current member/level
            //show member/level related to current dept
            $("#kpiMember ul").each(function() {
              $(this).slideUp(translation);
            });
            var childList = getChildList(getValue("dept"));
            toggleChildList(childList[0],"all"+childList[0]);
            for(var idx = 0; idx < childList.length; idx++) {
              setFlag(childList[idx],0);
            }
          }
          showJson();
          setFlag(parentId,0);
        } else {
          setFlag(parentId,1);
        }
        toggleList(parentId,thisId);
        $("#kpiMember").slideToggle(translation);
      }
    });

    //click on kpi member/level
    $("#kpiMember").on("click","ul li",function(event) {
      var thisId = this.id; //current item id
      var parentId = $(this).parent().attr("id"); //current parent (group)
      //label static, toggle list view and when current value different from previous one reload data
      //including change member/level first-level child item list
      if(thisId != "select"+parentId) {
        //hide all current member/level, and set flag to hide
        var childList = getChildList(parentId);
        for(var idx = 0; idx < childList.length; idx++) {
          $("#"+childList[idx]).slideUp(translation);
          setFlag(childList[idx],0);
        }
        if(getFlag(parentId) == 1) {
          //if option 'all' selected, set show data to detail, else get from current value
          if(thisId.substr(0,3) == "all")
            kpiDetFlag = 0;
          else
            kpiDetFlag = getDetFlag(parentId);
          if(thisId != "all"+parentId) {
            setValue("members",thisId);
            //if current member/level has child, show its child
            if(childList.length > 0) {
              //show only first option
              $("#"+childList[0]+" li:not('#select"+childList[0]+",#all"+childList[0]+"')").remove();
              $("#"+childList[0]+" li#all"+childList[0]).data("id",getValue("members"));
              //create child list from json file, generated by other process, got from js/ folder
              var childDetList = window["list_"+childList[0].replace(getValue("dept")+"-","")+"_"+
                  parentId.replace(getValue("dept")+"-","")+"_"+getValue("members")];
              for(var idx = 0; idx < childDetList.length; idx++) {
                createList(childList[0],childDetList[idx].code,childDetList[idx].name);
              }
              toggleChildList(childList[0],"all"+childList[0]);
            }
          }else {
            setValue("members",$(this).data("id"));
          }
          showJson();
          setFlag(parentId,0);
          memberShowFlag = 0;
          $("div:has('#kpiMember')").css("position", "fixed").css("top", kpiDefTopPos);
        } else {
          setFlag(parentId,1);
          setDefTreeView();
          memberShowFlag = 1;
          kpiOptLastTopPos = h/2 + 100;
        }
        toggleList(parentId,thisId);
        $("#kpiSelect,#kpiMember hr").slideToggle(translation);
      }
    });
    
    //click on refresh data menu
    $("#idRefresh").on("click", function() {
      linkTree = "";
      showJson();
    });
    
    //click on download file
    $("#btn-download").on("click", function() {
      $("#mdl-download").modal("show");
    });
    //click on download file type option
    $("#mdl-download .modal-footer button:not('#btn-cancel')").click(function() {
      extType = this.id.replace("btn-","");//grab extention from id
      loadingStateChange("show");
      //request file to server w/ report parameter
      //show download prompted when succeed
      var execFunc = function() { 
        $.post("../../apps/data/download/request",{
          deptName: getName("#kpiSelect #dept li"),
          dtsName: getName("#kpiSelect #dts li"),
          coyName: getName("#kpiSelect #coy li"),
          lobName: getName("#kpiSelect #lob li"),
          membersName: getName("#kpiMember li"),
          kpiName: $("#kpi ul li:nth(0)").text().trim(" "),
          kpiBrkdwnFlag: kpiBrkdwnFlag,
          extType: extType},
        function(data,status) {
          if(status === "success") {
            window.location.replace("../../apps/data/download");
            loadingStateChange("hide");
          } else {
            modalCommonShow(3,"Download report unsuccessfully: status = " + status);
          }
        }).fail(function(d) {
          modalCommonShow(2,"Failed to download report: ", d);
        }).error(function(d) {
          modalCommonShow(1,"Error download report: ", d);
        });
      };
      checkCurrSessAndExec(execFunc);
    });
    
    //click on zoom in button
    $("#zoom-slider #btn-zoom-in").click(function() {        
      for(var idx = 0; idx < arrZoomPoint.length; idx++) {
        //execute on current zoom index, apply if index greater than 1
        //see arrZoomPoint array to check the value of each position
        //decreased zooming by one position
        //redefine left/top tree position if zooming is greater than normal to avoid hidden view
        if(idx === currZoomPoint && idx > 0) {
          currZoomPoint -= 1;
          var bodyObj = $("#body");
          bodyObj.css("transform", "scale("+arrZoomPoint[currZoomPoint].scale+")");
          if(currZoomPoint >= 2) {
            bodyObj.css("left", (bodyProp.width*arrZoomPoint[currZoomPoint].scale-bodyProp.width)/2+bodyProp.left+"px");
            bodyObj.css("top", (bodyProp.height*arrZoomPoint[currZoomPoint].scale-bodyProp.height)/2+bodyProp.top+"px");
          }
          $("#zoom-slider #btn-slider").css("left",arrZoomPoint[currZoomPoint].left);
          break;
        }          
      }      
    });
    
    //click on zoom out button
    $("#zoom-slider #btn-zoom-out").click(function() {        
      for(var idx = 0; idx < arrZoomPoint.length; idx++) {
        //execute on current zoom index, apply if index less than array length
        //see arrZoomPoint array to check the value of each position
        //increased zooming by one position
        //redefine left/top tree position if zooming is greater than normal to avoid hidden view
        if(idx === currZoomPoint && idx < arrZoomPoint.length-1) {
          currZoomPoint += 1;
          var bodyObj = $("#body");
          bodyObj.css("transform", "scale("+arrZoomPoint[currZoomPoint].scale+")");
          if(currZoomPoint >= 2) {
            bodyObj.css("left", (bodyProp.width*arrZoomPoint[currZoomPoint].scale-bodyProp.width)/2+bodyProp.left+"px");
            bodyObj.css("top", (bodyProp.height*arrZoomPoint[currZoomPoint].scale-bodyProp.height)/2+bodyProp.top+"px");
          }
          $("#zoom-slider #btn-slider").css("left",arrZoomPoint[currZoomPoint].left);
          break;
        }          
      }      
    });    
    
    //click on expand/collapse button
    $("#zoom-slider #btn-expand-collapse").click(function() {
      //toggle between button (expand or collapse), identify through its current css class
      if($(this).hasClass("glyphicon-resize-small")) {
        $(this).attr("title","Expand All");
        collapseAll();
      } else {
        $(this).attr("title","Collapse All");
        expandAll();
      }
      $(this).toggleClass("glyphicon-resize-small").toggleClass("glyphicon-resize-full");
    });

    //click on map closing button (next future?)
    $("div#mapClose button").click(function() {
      $("div#map iframe").attr("src","");
      $("div#map").hide("slow");
    });

    //listener when mouse/key scroll up or down
    //apply position when member list option displayed, depend on scroll position to member option position difference
    $(window).on("scroll", function() {
      if(memberShowFlag == 1) {
        if($(window).scrollTop() > kpiOptLastTopPos)
          $("div:has('#kpiMember')").css("position", "absolute").css("top", kpiOptLastTopPos);
        else
          $("div:has('#kpiMember')").css("position", "fixed").css("top", kpiDefTopPos);
      }
    });
    
    //click on back button
    $("#zoom-slider #btn-back").click(function(){
      backToMain();
    });
    
    /* *** */

    /* DATA MANIPULATION FUNCTION */
    //show data tree through json
    function showJson() {
      var linkTreeTemp = getKpiName(); //json name to create
      //generate json, if current json different from previous one and GET status is success then reload data
      if(linkTree != linkTreeTemp) {
        loadingStateChange("show");
        var execFunc = function() {
          $.get("../../apps/data/kpi",{jsonName: linkTreeTemp, delimiter: delimiter},function(data,status) {
            if(status === "success") {
              linkTree = linkTreeTemp;
              currZoomPoint = 2;
              $("#zoom-slider #btn-slider").css("left",arrZoomPoint[currZoomPoint].left);
              updateTree(data);
            } else {
              modalCommonShow(3,"Loading kpi json unsuccessfully: status = " + status);
            }
          }).fail(function(d) {
            modalCommonShow(2,"Failed to load kpi json: ", d);
          }).error(function(d) {
            modalCommonShow(1,"Error loading kpi json: ", d);
          });
        };
        checkCurrSessAndExec(execFunc);
      }
    }

    //update tree object and data view
    function updateTree(treeLinked) {
      //clean tree contanier and create a new one
      d3.selectAll("#body").remove();
      $("body").append("<div id='body'></div>");
      //save json data to temporer var, init variable needed (including window size)
      //show first children only, update view, and restore scroll position
      root = treeLinked;
      initTreeVariable();
      if(bodyProp == null)
        bodyProp = {left:m[3],top:m[0],width:$("#body").css("width").replace("px",""),height:$("#body").css("height").replace("px","")};
      $("#body").css("left", (bodyProp.width*arrZoomPoint[currZoomPoint].scale-bodyProp.width)/2+bodyProp.left+"px");
      $("#body").css("top", (bodyProp.height*arrZoomPoint[currZoomPoint].scale-bodyProp.height)/2+bodyProp.top+"px");
      if(kpiBrkdwnFlag === 1) {
        $("#zoom-slider #btn-expand-collapse").addClass("glyphicon-resize-full").removeClass("glyphicon-resize-small");
        $("#zoom-slider #btn-expand-collapse").attr("title","Expand All");
        collapseAll();
      } else {
        $("#zoom-slider #btn-expand-collapse").addClass("glyphicon-resize-small").removeClass("glyphicon-resize-full");
        $("#zoom-slider #btn-expand-collapse").attr("title","Collapse All");
      }
      update(root);
      setDefTreeView();
      loadingStateChange("hide");
    }

    //update tree object
    function update(source) {
      var duration = d3.event && d3.event.altKey ? 5000 : 500;  //node transition duration
      //Compute the new tree layout
      var nodes = tree
        .nodes(root)
        .reverse();
      //Normalize for fixed-depth
      nodes.forEach(function(d) {d.y = d.depth * lLine;});
      // Update the nodes?
      var node = vis
        .selectAll("g.node")
        .data(nodes, function(d) {return d.id || (d.id = ++cnt);});
      // Enter any new nodes at the parent's previous position.
      var nodeEnter = node
        .enter()
        .append("svg:g")
        .attr("class", "node")
        .attr("transform", function(d) {return "translate(" + source.y0 + "," + source.x0 + ")";});

      //put growth value on foreignObject or text node (if IE browser) and its properties
      if((navigator.userAgent.indexOf("MSIE") != -1 ) || (!!document.documentMode == true )) {//If IE
        nodeEnter
          .append("svg:rect")//only for background
          .attr("width",function(d) {return (
            d.name.length < 10 && d.name.toUpperCase() === d.name ? 9 :
            (d.name.length < 10 || d.name.toUpperCase() === d.name? 8 : 6)) * (d.name.length);})
          .attr("height", 20)
          .attr("rx", 5)
          .attr("ry", 5)
          //filled object with color related to its value, except root has additional color if it not in breakdown view
          .style("fill", function(d){
            if(kpiBrkdwnFlag == 0 && d == root) {
                return "purple";
              } else {
                if(d.button == "btn btn-success btn-sm") {
                  return "green";
                } else if(d.button == "btn btn-danger btn-sm") {
                  return "red";
                } else if(d.button == "btn btn-warning btn-sm") {
                  return "orange";
                } else {
                  return "grey";
                }
              }})
          .style("cursor", function(d) {return (kpiBrkdwnFlag == 0 && d == root) ? "default" : "pointer";})
          .attr("y", "4")
          .attr("x", function(d) {return d.systemId === 3 ? "40" : "13";})
          //show info by breakdown or detail depend on flag
          .on("click",function(d) {
            showInfo(root,d);
          });
        nodeEnter
          .append("svg:text")
          .text(function(d) {return d.name;})
          .style("font-size","10px")
          .attr("fill",function(d){if(d.button == "btn btn-default btn-sm") return "black"; else return "white";})
          .attr("y", "17")
          .attr("x", function(d) {return d.systemId === 3 ? "44" : "17";})
          .style("cursor",function(d) {return (kpiBrkdwnFlag == 0 && d == root) ? "default" : "pointer";})
          .on("click", function(d) {
            showInfo(root,d);
          });
        //add link to direct to key process
        nodeEnter
          .append("svg:text")
          .text(function(d) {return d.systemId === 3 ? "Link" : "";})
          .attr("fill","blue")
          .attr("y", "17")
          .attr("x", "15")
          .style("text-decoration","underline")
          .style("font-style","italic")
          .style("cursor","pointer")
          .on("click", function(d) {
            //window.location.replace("../../apps/keypro/application?kpiId="+d.kpi);
            window.open("../../apps/keypro/application?kpiId="+d.kpi,"_blank");
          });
      } else {
        nodeEnter
          .append("svg:foreignObject")
          .attr("width", function(d) {if(d.name.length * 10 > 320) return 320; return (d.name.length < 10? 15 : 8) * (d.name.length);})
          .attr("height", "22px")
          .attr("id","foreignObject")
          .attr("y", "0em")
          .attr("x", function(d) {return d.systemId === 3 ? "2.3em" : "1.0em";})
          .style("opacity","0.8")
          //filled object with color and icon related to its value, except root has additional color if it not in breakdown view
          .html(function(d) {
            var htmlElement = "<button class='"+d.button+"' title='Show breakdown'>"+              
              d.name+"&nbsp;"+(navigator.userAgent.indexOf("Chrome") == -1 ? "<span class='"+d.icon+"'></span>" : "")+"</button>"; //If Chrome
            if(kpiBrkdwnFlag == 0) {
              if(d == root) {
                htmlElement = htmlElement.replace("title='Show breakdown'","style='cursor: not-allowed'");
              } else {
                if(kpiDetFlag == 1) {
                  htmlElement = htmlElement.replace("Show breakdown","Show detail");
                }
              }
            } else if(kpiBrkdwnFlag == 1) {
              if(d == root) {
                htmlElement = htmlElement.replace("Show breakdown","Back to KPI");
              } else {
                htmlElement = htmlElement.replace("Show breakdown","Show detail");
              }
            } else {
              htmlElement = htmlElement.replace("class='"+d.icon+"' style='cursor: pointer'","");
            }
            return htmlElement;
          })
          //show info by breakdown or detail depend on flag
          .on("click",function(d) {
            //$("div#map").hide("slow");
            showInfo(root,d);
          });
        //add link to direct to key process
        nodeEnter
          .append("svg:foreignObject")
          .attr("width", function(d) {return d.systemId === 3 ? "20px":"0px";})
          .attr("height", function(d) {return d.systemId === 3 ? "22px":"0px";})
          .attr("y", "0em")
          .attr("x", "1.0em")
          .html(function(d) {
            var iconClass = navigator.userAgent.indexOf("Chrome") === -1 ? "class='glyphicon glyphicon-link'" : "";
            var linkTxt = navigator.userAgent.indexOf("Chrome") === -1 ? "" : "&circlearrowleft;";
            return d.systemId === 3 ?
              "<a "+iconClass+" href='../../apps/keypro/application?kpiId="+d.kpi+"' target='_blank' title='Link to process'>"+linkTxt+"</a>":"";});
      }
      //polyline nodes and its properties
      nodeEnter.append("svg:polygon")
        .attr("points", "14,-4 14,4 16,4 16,9 20,0 16,-9 16,-4")
        .attr("fill",function(d){return d.color;})
        ;
      //circle nodes and its properties
      nodeEnter
        //.append("svg:circle")
        .append("svg:rect")
        .attr("class",function(d) {
          if(d.color === "green") return "ball-rounding";
          if(d.color === "yellow") return "ball-rounding-fast";
          if(d.color === "red") return "ball-beat";
          return "";})
        //.attr("r", rCircle)
        .attr("width","30px")
        .attr("height","30px")
        .attr("x","-15px")
        .attr("y","-15px")
        .attr("rx",function(d){return d.systemId === 3 ? "4px" : "15px";})
        .attr("ry",function(d){return d.systemId === 3 ? "4px" : "15px";})
        .style("fill", function(d){return "url(#"+d.color+")";})
        .style("fill-opacity","1")
        .style("cursor", function(d) {return d._children ? "pointer" : "default";})
        .on("click", function(d) {
          hoverFlag = 1;
          if(d != root && (d.children || d._children)) {toggle(d);update(d);}
          tooltip
            .transition()
            .duration(0)
            .style("opacity", 0)
            .style("left", "0px")
            .style("top", "0px");
          linktip
            .transition()
            .duration(0)
            .style("opacity", 0)
            .style("left", "0px")
            .style("top", "0px");
        })
        .on("mouseover", function(d) {
          var suffix = d.satuan == "P" ? "%" : ""; //satuan, mis. %
          var prefix = d.satuan == "A" ? "Rp " : ""; //simbol mata uang
          var suffix1 = d.achieve == null ? suffix : "%"; //satuan, mis. % di batas atas/bawah
          var prefix1 = d.achieve == null ? prefix : ""; //simbol mata uang di batas atas/bawah
          if(hoverFlag == 0 && d.color != "purple" && d.color != "grey") {
            //alert(d.satuan);
            tooltip
              .transition()
              .duration(500)
              .style("opacity", .9);
            tooltip
              .html("<div>"+
                      "<table class='table-data-hover'>"+
                        "<thead><tr><td>"+d.name+"</td><td>Value</td></tr></thead>"+
                        "<tbody>"+
                        "<tr><td>Target</td><td>"+numberFormat(d.target,suffix,prefix)+"</td></tr>" +
                        "<tr><td>Batas Atas</td><td>"+numberFormat(d.batasAtas,suffix1,prefix1)+"</td></tr>" +
                        "<tr><td>Batas Bawah</td><td>"+numberFormat(d.batasBawah,suffix1,prefix1)+"</td></tr>" +
                        "<tr><td>Actual</td><td>"+(d.satuan == "B"?(d.actual == 1?"Sudah":"Belum"):numberFormat(d.actual,suffix,prefix))+"</td></tr>" +
                        "<tr><td>Achieve</td><td>"+numberFormat(d.achieve,"%")+"</td></tr>" +
                        "<tr><td nowrap>Last Month</td><td>"+numberFormat(d.lastMonth,suffix,prefix)+"</td></tr>" +
                        "<tr><td>Growth</td><td>"+numberFormat(d.growth,"%")+"</td></tr>"+
                        "<tr><td>Populate</td><td>"+d.datePopulate.replace(" ","<br>")+"</td></tr>"+
                        "</tbody>"+
                      "</table>"+
                    "</div>")
              .style("left",function() {
                if(currZoomPoint >= 2)
                  return (d3.event.pageX +
                        (d.depth  * (lLine * (1-arrZoomPoint[currZoomPoint].scale))) -
                        (bodyProp.left * arrZoomPoint[currZoomPoint].scale)) + "px";
                return (d3.event.pageX +
                        (d.depth  * (lLine * (1-arrZoomPoint[currZoomPoint].scale))) +
                        (bodyProp.width*(arrZoomPoint[currZoomPoint].scale-1))/2 -
                        (bodyProp.left * arrZoomPoint[currZoomPoint].scale)) + "px";
              })
              .style("top",function() {
                var pageY = d3.event.pageY;
                if(currZoomPoint >= 2)
                  return (1/arrZoomPoint[currZoomPoint].scale*pageY -
                        1/arrZoomPoint[currZoomPoint].scale*bodyProp.top -
                        (pageY > h ? 200 : 0)) + "px";
                return (pageY * 1/arrZoomPoint[currZoomPoint].scale -
                        arrTopPoint[arrZoomPoint[currZoomPoint].scale] +
                        (kpiBrkdwnFlag == 0 ? 0 : 70/Math.pow(arrZoomPoint[currZoomPoint].scale,2))) + "px";
              });
            linktip
              .transition()
              .duration(500)
              .style("opacity", .9);
            linktip
              .html("<div><span class='btn btn-primary btn-sm'><span class='glyphicon glyphicon-map-marker'></span>&nbsp;Show Map</span></div>")
              .style("left", (d3.event.pageX + 20) + "px")
              .style("top", (d3.event.pageY - 28) + "px")
              .on("click", function() {
                tooltip
                  .transition()
                  .duration(0)
                  .style("opacity", 0)
                  .style("left", "0px")
                  .style("top", "0px");
                linktip
                  .transition()
                  .duration(0)
                  .style("opacity", 0)
                  .style("left", "0px")
                  .style("top", "0px");
                mapShow(d.kpi);
              });
          }
          hoverFlag = 0;
        })
        .on("mouseout", function(d) {
          if(hoverFlag == 0 && d.color != "purple" && d.color != "grey") {
            tooltip
              .transition()
              .duration(1500)
              .style("opacity", 0)
              .style("left", "0px")
              .style("top", "0px");
            linktip
              .transition()
              .delay(1000)
              .duration(500)
              .style("opacity", 0)
              .style("left", "0px")
              .style("top", "0px");
          }
        });

      // Transition nodes to their new position.
      var nodeUpdate = node.transition()
        .duration(duration)
        .attr("transform", function(d) {return "translate(" + d.y + "," + d.x + ")";});
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

    //toggle children between expand or collapse.
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

    //init tree variable
    function initTreeVariable() {
        //init line
        diagonal = d3
          .svg
          .diagonal()
          .projection(function(d) {return [d.y, d.x];});
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
        linktip.style("display","none");//remark to show this object, prepare for map
        //sizing tree
        var depth = 0;
        var depthTemp = 0;
        setDepth(root);
        var stack = root.children.length;
        var tempW = (rCircle+lLine)*depth;
        var tempH = kpiBrkdwnFlag === 0 ? stack*(m[0]+m[2]) : stack*(m[0]+m[2])/2;
        w =  tempW > minW ? tempW : minW;
        h = tempH > minH ? tempH : minH;
        //x,y node start position
        root.x0 = h / 2;
        root.y0 = 0;
        //created tree object
        tree = d3
          .layout
          .tree()
          .size([h, w]);
        //created svg object on tree (container)
        vis = d3
          .select("#body")
          .append("svg:svg")
          .attr("width", w + m[1] + m[3])
          .attr("height", h + m[0] + m[2])
          .append("svg:g")
          .attr("transform", "translate(" + m[3] + "," + m[0] + ")");
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
          .attr("style","stop-color:#c00;stop-opacity:1");
        linGradRed
          .append("svg:stop")
          .attr("offset","100%")
          .attr("style","stop-color:#fdd;stop-opacity:1");
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
          .attr("style","stop-color:#090;stop-opacity:1");
        linGradGreen
          .append("svg:stop")
          .attr("offset","100%")
          .attr("style","stop-color:#dfd;stop-opacity:1");
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
          .attr("style","stop-color:#ee0;stop-opacity:1");
        linGradYellow
          .append("svg:stop")
          .attr("offset","100%")
          .attr("style","stop-color:#ffd;stop-opacity:1");
        linGradBlue = vis
          .append("svg:defs")
          .append("svg:linearGradient")
          .attr("id","blue")
          .attr("x1","70%")
          .attr("y1","80%")
          .attr("x2","10%")
          .attr("y2","0%")
          .attr("spreadMethod", "pad");
        linGradBlue
          .append("svg:stop")
          .attr("offset","0%")
          .attr("style","stop-color:#009;stop-opacity:1");
        linGradBlue
          .append("svg:stop")
          .attr("offset","100%")
          .attr("style","stop-color:#ddf;stop-opacity:1");
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
          .attr("style","stop-color:#ddd;stop-opacity:1");
        linGradPurple = vis
          .append("svg:defs")
          .append("svg:linearGradient")
          .attr("id","purple")
          .attr("x1","70%")
          .attr("y1","80%")
          .attr("x2","10%")
          .attr("y2","0%")
          .attr("spreadMethod", "pad");
        linGradPurple
          .append("svg:stop")
          .attr("offset","0%")
          .attr("style","stop-color:#337;stop-opacity:1");
        linGradPurple
          .append("svg:stop")
          .attr("offset","100%")
          .attr("style","stop-color:#ddf;stop-opacity:1");
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
        //grab the most length of nodes
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
    }
    /* *** */

    /* SUPPORTING FUNCTION */

    //show map (next future?)
    function mapShow(kpiTemp) {
      loadingStateChange("show");
      $("div#map iframe").attr("src","http://10.17.18.123:8081/poi/index.jsp?kpi="+kpiTemp);
      $("div#map").show("slow");
      setDefTreeView();      
      loadingStateChange("hide");
    }

    //show info by breakdown (new tree hierarchy) or detail depend on flag
    function showInfo(dataRoot,dataDet) {
      if(!(dataDet == dataRoot && kpiBrkdwnFlag == 0)) {
        if(kpiDetFlag == 0) {
          if(dataDet == dataRoot && kpiBrkdwnFlag == 1) {
            /*setValue("kpiCode","");
            showJson();
            //hide all option
            $("div:has('#kpiMember')").slideDown(translation);
            $("#kpi").slideUp(translation);
            $("#kpi ul li").remove();
            kpiBrkdwnFlag = 0;*/
            backToMain();
          } else if(dataDet != dataRoot && kpiBrkdwnFlag == 0)  {
            setValue("kpiCode",dataDet.kpi);
            showJson();
            //show back all option
            $("div:has('#kpiMember')").slideUp(translation);
            $("#kpi ul").append("<li><span>&nbsp;&nbsp;"+dataRoot.name+": "+dataDet.name+"</span></li>");
            $("#kpiSelect ul li").each(function() {
              if($(this).css("display") != "none" && !$(this).children("span").hasClass("glyphicon-tags")
                && $(this).children("span").hasClass("btn-primary"))
                $("#kpi ul").append(
                  "<li><span>&nbsp;&nbsp;"+$(this).text().trim()+"</span></li>");
            });   
            $("#kpi ul li span")
              .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
            $("#kpi").slideDown(translation);
            $("#zoom-slider #btn-back").slideDown(translation);
            kpiBrkdwnFlag = 1;
          //just popup info
          } else {
            showInfoDet(dataDet);
          }
        } else {
          showInfoDet(dataDet);
        }
      }
    }

    //detail info from dataDetTemp json object
    function showInfoDet(dataDetTemp) {
      var suffix = dataDetTemp.satuan == "P" ? "%" : ""; //satuan, mis. %
      var prefix = dataDetTemp.satuan == "A" ? "Rp " : ""; //simbol mata uang
      var suffix1 = dataDetTemp.achieve == null ? suffix : "%"; //satuan, mis. % di batas atas/bawah
      var prefix1 = dataDetTemp.achieve == null ? prefix : ""; //simbol mata uang di batas atas/bawah
      $("#mdl-detail .modal-body #title").text(dataDetTemp.name);
      $("#mdl-detail .modal-body #target").text(numberFormat(dataDetTemp.target,suffix,prefix));
      $("#mdl-detail .modal-body #batasAtas").text(numberFormat(dataDetTemp.batasAtas,suffix1,prefix1));
      $("#mdl-detail .modal-body #batasBawah").text(numberFormat(dataDetTemp.batasBawah,suffix1,prefix1));
      $("#mdl-detail .modal-body #actual").text((dataDetTemp.satuan == "B" ? (dataDetTemp.actual == 1 ? "Sudah" : "Belum") :
              numberFormat(dataDetTemp.actual,suffix,prefix)));
      $("#mdl-detail .modal-body #achieve").text(numberFormat(dataDetTemp.achieve,"%"));
      $("#mdl-detail .modal-body #lastMonth").text(numberFormat(dataDetTemp.lastMonth,suffix,prefix));
      $("#mdl-detail .modal-body #growth").text(numberFormat(dataDetTemp.growth,"%"));
      $("#mdl-detail .modal-body #populate").text(dataDetTemp.datePopulate);
      $("#mdl-detail").modal("show");
    }

    //get current show/hide flag of codeTemp
    function getFlag(codeTemp) {
      for(idx = 0; idx < arrFlag.length; idx ++) {
        if(arrFlag[idx].code == codeTemp) {
          return arrFlag[idx].flag;
        }
      }
      return 0;
    }

    //set current show/hide flag of codeTemp to flagTemp
    function setFlag(codeTemp,flagTemp) {
      for(idx = 0; idx < arrFlag.length; idx ++) {
        if(arrFlag[idx].code == codeTemp) {
          arrFlag[idx].flag = flagTemp;
          break;
        }
      }
    }

    //get breakdown/detail flag of codeTemp
    function getDetFlag(codeTemp) {
      for(idx = 0; idx < arrFlag.length; idx ++) {
        if(arrFlag[idx].code == codeTemp) {
          return arrFlag[idx].detFlag;
        }
      }
      return 0;
    }

    //set breakdown/detail flag of codeTemp with flagTemp
    function setDetFlag(codeTemp,flagTemp) {
      for(idx = 0; idx < arrFlag.length; idx ++) {
        if(arrFlag[idx].code == codeTemp) {
          arrFlag[idx].detFlag = flagTemp;
          break;
        }
      }
    }

    //get member child list of member parent
    function getChildList(parent) {
      var childList = [];
      var index = 0;
      for(idx = 0; idx < arrLayer.length; idx++) {
        if(arrLayer[idx].parent == parent) {
          childList[index++] = arrLayer[idx].code;
        }
      }
      return childList;
    }

    //get value of nameTemp
    function getValue(nameTemp) {
      for(idx = 0; idx < arrVal.length; idx ++) {
        if(arrVal[idx].name == nameTemp) {
          return arrVal[idx].value;
        }
      }
      return "";
    }

    //set value of nameTemp with valueTemp
    function setValue(nameTemp,valueTemp) {
      for(idx = 0; idx < arrVal.length; idx ++) {
        if(arrVal[idx].name == nameTemp) {
          arrVal[idx].value = valueTemp;
          break;
        }
      }
    }

    //get current kpi patam value
    //template:
    //kpi_[timeseries]_[layer]_[level]_[coy]_[lob]_[kpi_id|null]_[timestamp]
    function getKpiName() {
      var kpiCodeTemp = getValue("kpiCode");
      return "kpi"+delimiter+getValue("dts")+delimiter+getValue("dept")+delimiter+getValue("members")+
        delimiter+getValue("coy")+delimiter+getValue("lob")+delimiter+
        (kpiCodeTemp == null || kpiCodeTemp == ""? "" : kpiCodeTemp+delimiter)+vTimeStamp;
    }

    //set tree view default position
    function setDefTreeView() {
      $(window).scrollLeft(0);
      $(window).scrollTop(kpiBrkdwnFlag === 0 ? h/2 : h/3);
    }
    
    //get label of current option, used as report parameter
    function getName(pSelector) {
      var name = "";
      $(pSelector).each(function() {
        if($(this).css("display") != "none" && !$(this).children("span").hasClass("glyphicon-tags")
                && $(this).children("span").hasClass("btn-primary")) {
          name = $(this).text().trim(" ");
          return false;
        }
      });
      return name;
    }
    
    //back application to its main page
    function backToMain() {      
      setValue("kpiCode","");
      showJson();
      //hide all option
      $("div:has('#kpiMember')").slideDown(translation);
      $("#kpi").slideUp(translation);
      $("#zoom-slider #btn-back").slideUp(translation);
      $("#kpi ul li").remove();
      kpiBrkdwnFlag = 0;
    }
    /* *** */
});

