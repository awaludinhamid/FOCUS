/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
  
    /* VARIABLE DECLARATION */
    //
    var m = [80, 120, 20, 220], //svg padding to tree(up,left,down,right)
        minW = 2040, //min tree diagram width
        minH = 1020, //min tree diagram height
        w = 0, //tree diagram width
        h = 0, //tree diagram height
        cnt = 0; //node count
    var root, tree, diagonal, vis, tooltip, linktip, linGradRed, linGradGreen, linGradYellow, linGradCyan, linGradGrey, radGrad; //tree var holder
    var rCircle = 10; //circle node radiant
    var lLine = 400; //hierarchy line length
    var linkTree = "";	//json file name
    var hoverFlag = 0;//avoid hover on click
    var delimiter = "_";
    var radius = 300;
    //	
    var kpiBrkdwnFlag = 0, //0=by kpi 1=by area
        kpiDetFlag; //0=level sebelum child terakhir 1=level child terakhir
    //
    var arrFlag = [], //switch flagging, for hide/unhide item
        arrLayer = [], //parent-child collection, for hide/unhide child of parent
        arrVal = [{name: "dept",value:""},  // current option selected
                  {name: "members",value:""},
                  {name: "kpiCode",value:""},
                  {name: "dts",value:"D"},
                  {name: "coy",value:"01"},
                  {name: "lob",value:"nmc"},
                  {name: "initMembers",value:""}];
    /* *** */

    /* MEMBER & DEPARTMENT GENERATE */
    //load user detail json data
    $.get("/FOCUS/apps/data/profile",function(data,status) {
      if(status == "success") {
        var deptTemp = ""; //dept/layer comparison while looping list
        //set current and initial (used when started new dept/layer) member/level
        setValue("members",data[0].memberListCode);
        setValue("initMembers",data[0].memberListCode);
        //labelling current user layer
        $("div#right-title span").html("<span class='"+glyTags+"'></span>&nbsp;&nbsp;Layer: "+
                data[0].memberName+(data[0].memberListCode == "00000" ? "" : " "+data[0].memberListName));
        //flag to switch data [un]loading and flag to switch detail[breakdown] view
        arrFlag.push({code: "dept", flag: 0, detFlag: 0});
        //append/labelling/styling distinct dept/layer, show first item only and set current dept/layer
        $("#kpiSelect").append(
          "<ul id='dept' class='list-unstyled'>"+
            "<li id='selectdept'><span id='spanselectdept'>&nbsp;&nbsp;Department</span></li>"+
          "</ul>");
        $("#spanselectdept")
            .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
        for(i = 0; i < data.length; i++) {
          if(data[i].deptCode != deptTemp) {
            deptTemp = data[i].deptCode;
            if(i == 0) {
              createList("dept",deptTemp,data[i].deptName,true);
              setValue("dept",deptTemp);
            } else {
              createList("dept",deptTemp,data[i].deptName,false);
            }
          }
        }
        deptTemp = "";
        //generate member/level
        for(i = 0; i < data.length; i++) {
          //hide root member/level
          if(data[i].memberCode.replace(data[i].deptCode+"-","") != data[i].parentMemberCode) {
            //flag to switch data [un]loading and flag to switch detail[breakdown] view
            arrFlag.push({code: data[i].memberCode, flag: 0, detFlag: 0});
            //last member/level on list only view detail
            if(i == data.length - 1) {
              setDetFlag(data[i].memberCode,1);
            }else if(data[i].deptCode != data[i+1].deptCode) {
              setDetFlag(data[i].memberCode,1);
            }
            //add parent-child member/level collection
            arrLayer.push({code: data[i].memberCode, parent: data[i].deptCode});
            //each high level member have children at all lower levels and must be member/level at same group
            for(j = i - 1; j > 0; j--) {
              if(data[i].parentMemberCode == data[j].parentMemberCode && data[i].deptCode == data[j].deptCode) {
                arrLayer.push({code: data[i].memberCode, parent: data[j].memberCode});
              }
            }
            //append member/level to the tags list, only first member/level should be display
            //each member has label, all list option (if necessery) and styling
            $("#kpiMember").append(
              "<ul id='"+data[i].memberCode+"' class='list-unstyled'"+(i == 1 ? "" : " style='display: none'")+">"+
                "<li id='select"+data[i].memberCode+"'><span id='spanselect"+data[i].memberCode+"'>&nbsp;&nbsp;"+data[i].memberName+"</span></li>"+
                "<li id='all"+data[i].memberCode+"'"+(data[i].deptCode == deptTemp ? "" : " data-id='"+getValue("members")+"'")+"><span id='spanall"+data[i].memberCode+"'>&nbsp;&nbsp;All</span></li>"+
              "</ul>");
            $("#spanselect"+data[i].memberCode)
                .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
            $("#spanall"+data[i].memberCode)
                .attr("class",pickStyle).css("text-align","left");//.css("background-color",bkColPick);
            //generate list option of member/level, execution is only one per dept/layer
            if(data[i].deptCode != deptTemp) {
              var firstMemberCode = data[i].memberCode;
              var memberList = window["list_"+firstMemberCode.replace(data[i].deptCode+"-","")+"_"+data[i].parentMemberCode+"_"+getValue("members")];
              for(k = 0; k < memberList.length; k++) {
                      createList(firstMemberCode,memberList[k].code,memberList[k].name,false);
              }
              deptTemp = data[i].deptCode;
            }
            kpiDetFlag = 0;
          } else {
            kpiDetFlag = 1;
          }
        }
        //genrate data time series
        generateDts();
        //generate company
        generateCoy();
        //generate product
        generateLob();
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
    //label styling
    $("#kpi span")
      .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
    /* *** */

    /* DATA TIME SERIES GENERATE */
    function generateDts() {
      //append/labelling/styling distinct data time series
      $("#kpiSelect").append(
        "<ul id='dts' class='list-unstyled'>"+
          "<li id='selectdts'><span id='spanselectdts'>&nbsp;&nbsp;Data Time Series</span></li>"+
        "</ul>");
      $("#spanselectdts")
          .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
      //var list_dts generated by other process, got from js/ folder, show first item only
      for(i = 0; i < list_dts.length; i++) {
        if(i == 0)
          createList("dts",list_dts[i].code,list_dts[i].name,true);
        else
          createList("dts",list_dts[i].code,list_dts[i].name,false);
      }
      //flag to switch data [un]loading
      arrFlag.push({code: "dts", flag: 0});
    }
    /* *** */

    /* COMPANY GENERATE */
    function generateCoy() {
      //append/labelling/styling distinct company
      $("#kpiSelect").append(
        "<ul id='coy' class='list-unstyled'>"+
          "<li id='selectcoy'><span id='spanselectcoy'>&nbsp;&nbsp;Company</span></li>"+
        "</ul>");
      $("#spanselectcoy")
          .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
      //var list_coy generated by other process, got from js/ folder, show first item only
      for(i = 0; i < list_coy.length; i++) {
        if(i == 0)
          createList("coy",list_coy[i].code,list_coy[i].name,true);
        else
          createList("coy",list_coy[i].code,list_coy[i].name,false);
      }
      //flag to switch data [un]loading
      arrFlag.push({code: "coy", flag: 0});
    }
    /* *** */

    /* PRODUCT GENERATE */
    function generateLob() {
      //append/labelling/styling distinct product
      $("#kpiSelect").append(
        "<ul id='lob' class='list-unstyled'>"+
          "<li id='selectlob'><span id='spanselectlob'>&nbsp;&nbsp;LOB</span></li>"+
        "</ul>");
      $("#spanselectlob")
          .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
      //var list_lob generated by other process, got from js/ folder, show first item only
      for(i = 0; i < list_lob.length; i++) {
        if(i == 0)
          createList("lob",list_lob[i].code,list_lob[i].name,true);
        else
          createList("lob",list_lob[i].code,list_lob[i].name,false);
      }
      //flag to switch data [un]loading
      arrFlag.push({code: "lob", flag: 0});
    }
    /* *** */

    /* ITEM CLICKED FUNCTION */
    //click on kpi option group (layer/coy/lob)
    $("#kpiSelect").on("click","ul li",function() {
      var thisId = this.id; //current item id
      var parentId = $(this).parent().attr("id"); //current parent (group)
      //set current group value
      setValue(parentId,thisId);
      //label static, toggle list view and when current value different from previous one reload data
      //specific to dept group including change member/level item list
      if(thisId != "select"+parentId) {
        if(getFlag(parentId) == 1) {
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
            for(i = 0; i < childList.length; i++) {
              setFlag(childList[i],0);
            }
          }
          showJson();
          setFlag(parentId,0);
        } else {
          setFlag(parentId,1);
        }
        toggleList(parentId,thisId);
      }
    });

    //click on kpi member/level
    $("#kpiMember").on("click","ul li",function() {
      var thisId = this.id; //current item id
      var parentId = $(this).parent().attr("id"); //current parent (group)
      //label static, toggle list view and when current value different from previous one reload data
      //including change member/level first-level child item list
      if(thisId != "select"+parentId) {
        //hide all current member/level, and set flag to hide
        var childList = getChildList(parentId);
        for(i = 0; i < childList.length; i++) {
          $("#"+childList[i]).slideUp(translation);
          setFlag(childList[i],0);
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
              for(i = 0; i < childDetList.length; i++) {
                createList(childList[0],childDetList[i].code,childDetList[i].name);
              }
              toggleChildList(childList[0],"all"+childList[0]);
            }
          }else {
            setValue("members",$(this).data("id"));
          }
          showJson();
          setFlag(parentId,0);
          $("#kpiMember").css("position", "fixed");
        } else {
          setFlag(parentId,1);
          setDefTreeView();
          $("#kpiMember").css("position", "absolute");
        }
        toggleList(parentId,thisId);
      }
    });
    
    //click on refresh data menu
    $("#idRefresh").on("click", function() {
      linkTree = "";
      showJson();
    });

    $("div#mapClose button").click(function() {
      $("div#map iframe").attr("src","");
      $("div#map").hide("slow");
    });

    /* *** */

    /* DATA MANIPULATION FUNCTION */
    //show data tree through json
    function showJson() {
      var linkTreeTemp = getKpiName(); //json name to create
      //generate json, if current json different from previous one and GET status is success then reload data
      if(linkTree != linkTreeTemp) {
        $("#loading").show();
        $.get("/FOCUS/apps/data/kpi",{jsonName: linkTreeTemp, delimiter: delimiter},function(data,status) {
          if(status == "success") {
            linkTree = linkTreeTemp;
            updateTree(data);
          } else {
            modalCommonShow(3,"Loading kpi json unsuccessfully: status = " + status);
          }
        }).fail(function(d) {
          modalCommonShow(2,"Failed to load kpi json: ", d);
        }).error(function(d) {
          modalCommonShow(1,"Error loading kpi json: ", d);
        });
      }
    }

    //update tree object and data view
    function updateTree(treeLinked) {
      //clean tree contanier and create a new one
      d3.selectAll("#body").remove();
      $("body").append("<div id='body' style='position: relative; left: 80px; z-index: 0'></div>");
      //save json data to temporer var, init variable needed (including window size)
      //show first children only, update view, and restore scroll position
      root = treeLinked;
      initTreeVariable();
      //root.children.forEach(toggleAll);
      update(root);
      setDefTreeView();
      $("#loading").hide();
    }

    //update tree object
    function update(source) {
      var duration = d3.event && d3.event.altKey ? 5000 : 500;  //node transition duration
      //Compute the new tree layout
      var nodes = tree
        .nodes(root)
        .reverse();
      //Normalize for fixed-depth
      //nodes.forEach(function(d) {d.y = d.depth * lLine;});
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
      if((navigator.userAgent.indexOf("MSIE") != -1 ) || (!!document.documentMode == true )) {//IF IE
        nodeEnter
          .append("svg:rect")
          .attr("width",function(d) {return 10*(d.name.length);})
          .attr("height", 30)
          .attr("rx", 5)
          .attr("ry", 5)
          //filled object with color related to its value, except root has additional color if it not in breakdown view
          .style("fill", function(d){
            if(kpiBrkdwnFlag == 0 && d == root) {
                return "cyan";
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
          .attr("y", "7")
          .attr("x", "25")
          //show additional info when hover object
          .on("mouseover",function(d) {
            div
              .html(function() {
                if(kpiBrkdwnFlag == 0) {
                  return "<span>Click to show " + ((d == root || kpiDetFlag == 1) ? "detail" : "breakdown") + "</span>";
                } else {
                  return "<span>Click to " + (d == root ? "back to KPI" : "show detail") + "</span>";
                }
              })
              .style("height","30px")
              .style("left", (d3.event.pageX + 28) + "px")
              .style("top", (d3.event.pageY - 28) + "px")
              .style("opacity",0.9);
          })
          //hide additional info when mouse leaving object
          .on("mouseout",function(d) {
            div
              .style("height","130px")
              .style("opacity",0);
          })
          //show info by breakdown or detail depend on flag
          .on("click",function(d) {
            showInfo(root,d);
          });
        nodeEnter
          .append("svg:text")
          .text(function(d) {return d.name;})
          .attr("fill",function(d){if(d.button == "btn btn-default btn-sm") return "black"; else return "white";})
          .attr("y", "25")
          .attr("x", "35");
      } else {
        nodeEnter
          .append("svg:foreignObject")
          .attr("width", function(d) {return (10*d.name.length)+10;})
          .attr("height", "20px")
          .attr("id","foreignObject")
          .attr("y", "0em")
          .attr("x", "0.7em")
          .style("opacity","0.8")
          //filled object with color and icon related to its value, except root has additional color if it not in breakdown view
          .html(function(d) {
            var htmlElement = "<button class='"+d.button+"' style='font-size: 10px; padding: 0' title='Show breakdown'>"+d.name+
              "&nbsp;<span class='"+d.icon+"'></span></button>";
            if(kpiBrkdwnFlag == 0) {
              if(d == root) {
                htmlElement = htmlElement.replace("' title='Show breakdown'","; cursor: not-allowed'");
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
            $("div#map").hide("slow");
            showInfo(root,d);
          });
      }
      //polyline nodes and its properties
      nodeEnter.append("svg:polygon")
        .attr("points", "10,-3 10,3 11,3 11,7 16,0 11,-7 11,-3")
        .attr("fill",function(d){return d.color;});
      //circle nodes and its properties
      nodeEnter
        .append("svg:circle")
        .attr("r", rCircle)
        .on("click", function(d) {
          hoverFlag = 1;
          if(d != root) {toggle(d);update(d);}
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
        .style("fill", function(d){return "url(#"+d.color+")";})
        .style("cursor", function(d) {return d._children ? "pointer" : "default";})
        .on("mouseover", function(d) {          
          if(hoverFlag == 0 && d.color != "cyan" && d.color != "grey") {
            tooltip
              .transition()
              .duration(500)
              .style("opacity", .9);
            tooltip
              .html("<div>"+
                      "<table style='font-weight: bolder; table-layout: fixed'>"+
                        "<thead style='background-color: lightgrey; font-weight: bold'><td>"+d.name+"</td><td>Value</td></thead>"+
                        "<tbody><tr><td>Target</td><td>"+numberWithCommas(d.target)+"</td></tr>" +
                        "<tr><td>Actual</td><td>"+numberWithCommas(d.actual)+"</td></tr>" +
                        "<tr><td>Achieve</td><td>"+d.achieve+"%</td></tr>" +
                        "<tr><td nowrap>Last Month</td><td>"+numberWithCommas(d.lastMonth)+"</td></tr>" +
                        "<tr><td>Growth</td><td>"+d.growth+"%</td></tr></tbody>"+
                      "</table>"+
                    "</div>")
              .style("left", (d3.event.pageX-70) + "px")
              .style("top", (d3.event.pageY) + "px");
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
          if(hoverFlag == 0 && d.color != "cyan" && d.color != "grey") {
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
        .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; });
        //.attr("transform", function(d) { return "rotate(" + (0) + ")translate(" + d.y + "," + d.x + ")"; });
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

    function toggleAll(d) {
      if (d.children) {
        d.children.forEach(toggleAll);
        toggle(d);
      }
    }

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
        setDepth(root);
        //var stack = root.children.length;
        var tempR = (rCircle+radius)*depth;
        //var tempH = stack*(m[0]+m[2]);
        w =  tempR > minW ? tempR : minW;
        h = tempR > minH ? tempR : minH;
        root.x0 = h / 2;
        root.y0 = 0;
        tree = d3
          .layout
          .tree()
          .size([360, radius]);
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

    function mapShow(kpiTemp) {
      $("#loading").show();
      $("div#map iframe").attr("src","http://10.17.18.123:8081/poi/index.jsp?kpi="+kpiTemp);
      $("div#map").show("slow");
      setDefTreeView();
      $("#loading").hide();
    }

    //show info by breakdown (new tree hierarchy) or detail depend on flag
    function showInfo(dataRoot,dataDet) {
      if(!(dataDet == dataRoot && kpiBrkdwnFlag == 0)) {
        if(kpiDetFlag == 0) {
          if(dataDet == dataRoot && kpiBrkdwnFlag == 1) {
            setValue("kpiCode","");
            showJson();
            //hide all option
            $("#kpiSelect,#kpiMember").slideDown(translation);
            $("#kpi").slideUp(translation);
            kpiBrkdwnFlag = 0;
          } else if(dataDet != dataRoot && kpiBrkdwnFlag == 0)  {
            setValue("kpiCode",dataDet.kpi);
            showJson();
            //show back all option
            $("#kpiSelect,#kpiMember").slideUp(translation);
            $("#spanKpi").html("&nbsp;&nbsp;" + dataRoot.name);
            $("#spanKpiDet").html("&nbsp;&nbsp;" + dataDet.name);
            $("#kpi").slideDown(translation);
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
      $("#mdl-detail .modal-body #title").text(dataDetTemp.name);
      $("#mdl-detail .modal-body #target").text(numberWithCommas(dataDetTemp.target));
      $("#mdl-detail .modal-body #actual").text(numberWithCommas(dataDetTemp.actual));
      $("#mdl-detail .modal-body #achieve").text(dataDetTemp.achieve+"%");
      $("#mdl-detail .modal-body #lastMonth").text(numberWithCommas(dataDetTemp.lastMonth));
      $("#mdl-detail .modal-body #growth").text(dataDetTemp.growth+"%");
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
      $(window).scrollLeft(w/4);
      $(window).scrollTop(h/4);
    }
    /* *** */
});

