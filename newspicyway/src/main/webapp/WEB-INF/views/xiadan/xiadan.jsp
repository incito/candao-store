<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/datagrid-groupview.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
 
	<script type="text/javascript">
		$(function(){
			$('#kaitaiDialog').dialog({   
			    title: '开台',   
			    width:350,   
			    height: 350,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存',
					handler : 
					function(){
						addDesk();			
					}						
				}, {
					text : '取消',
					handler : function() {				
						$('#kaitaiDialog').dialog('close');
					}
				}
				],
				onClose : function() {
				}
			});
			
			$('#xiadanDialog').dialog({   
			    title: '下单',   
			    width:750,   
			    height: 550,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存',
					handler : 
					function(){
					padXiadan();	
			//	alert(getOrderJsons());
					}						
				}, {
					text : '取消',
					handler : function() {				
						$('#xiadanDialog').dialog('close');
					}
				}
				],
				onClose : function() {
				}
			});
			
			
			$('#showDetialDialog').dialog({   
			    title: '份额',   
			    width:450,   
			    height: 350,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存',
					handler : 
					function(){						
						add_num();							
					}						
				}, {
					text : '取消',
					handler : function() {				
						$('#showDetialDialog').dialog('close');
						init_date2();
					}
				}
				],
				onClose : function() {
				}
			});
					
});
		
	/* 	function exportReports(){
			location.href=global_Path + "/daliyReports/exportxls.json";
		} */
		var editList=[];
		var holDishtype = "";
		var holDishid = "";
		var userName = "";
		var orderid = "";
		var deskNum = "";
		function xiandan(){
			init_date();
			$('#kaitaiDialog').dialog('open');
		}
		
		function getOrderJson(){
		   var Torder={};
		   $("#employeeNum").val();
		   $("#deskNum").val();
		   $("#manNum").val();
		   $("#womanNum").val();
		   Torder.userid =  $("#employeeNum").val();
		   Torder.tableNo = $("#deskNum").val();
		   Torder.manNum = $("#manNum").val();
		   Torder.womanNum = $("#womanNum").val();
		   return $.toJSON(Torder);
		}
		
		function addDesk(){
			var json=getOrderJson();
			$.ajax({
				type:"post",
				async:false,
				url : global_Path+'/padinterface/setorder',
				contentType:'application/json;charset=UTF-8',
			    data:json, 
				dataType : "json",
				success : function(data) {
					$('#kaitaiDialog').dialog('close');
					if(data.result==0){
						xiandanl();
					 orderid = data.orderid;
					 deskNum =$("#deskNum").val();
					 userName = $("#employeeNum").val();
					}else{
						$.messager.alert('数据字典','开台失败！','info',null);
					}
					
				}				
			});
		}
		
		
	function xiandanl(){
		$('#dishList').datagrid({    
			url : global_Path+"/dish/getDishLists.json",
		    singleSelect:false,
		    collapsible:true,
		    rownumbers:true,
		    fitColumns:true,
		    method : 'post',
			fit : false,
			view:groupview,
			remoteSort : false,
			loadMsg : '数据装载中......',
		    groupField:'fitemDesc',
		    groupFormatter:function(value,rows){
		    return value + '(' + rows.length + ')';
		    },
			columns : [ [{
				field : 'dishid',
				title : '请选择',
				width : 5,
				align : 'left',
				checkbox: true
			},{
				field : 'dishno',
				title : '编号',
				width : 10,
				align : 'left',
			},{
				field : 'title',
				title : '名称',
				width : 10,
				align : 'left',
//				hidden:true
			},{
				field : 'price',
				title : '价格',
				width : 10,
				align : 'left',
			},{
				field : 'shs',
				title : '菜品',
				width : 10,
				align : 'left',
				formatter : function(value, row, index) {
						var modify ="<a href=\"###\" onclick=\"add_dish('" + row.dishtype + "','"+row.dishid+"')\">份额</a>";
					    return modify;
				}
			},{
				field : 'fitemDesc1',
				title : '单位',
				width : 10,
				align : 'left',
//				hidden:true
			}			
			] ],
			onLoadSuccess:function(data){
				$('#dishList').datagrid('collapseGroup');
			}
        });
		$('#xiadanDialog').dialog('open');
	}	
	
	//获取checkbox选择的子类
	function getSecletdish(){
		var idlist=[];
		$.each($('#dishList').datagrid('getChecked'),function(index,item){
			idlist.push(item.dishid);
		});	
		return idlist.join(',');
	}
	
	function add_dish(dishtype,dishid,content){
		 init_date2();
         holDishtype = dishtype;
		 holDishid = dishid;
		$("#showDetialDialog").dialog('open');
	}
	
	function add_num(){
		var dish={};		
			dish.dishid = holDishid;
			dish.num = $("#tabNum").val();
			editList.push(dish);

		/* $.ajax({
			type:"post",
			async:false,
			url : global_Path+'/padinterface/setorder',
			contentType:'application/json;charset=UTF-8',
		    data:json, 
			dataType : "json",
			success : function(data) {	
				
				
			}
			
		}); */
		$("#showDetialDialog").dialog('close');

	}
	
	function init_date(){
		 editList=[];
		 holDishtype = "";
		 holDishid = "";
		 userName = "";
		 orderid = "";
		$("#employeeNum").val("");
		$("#deskNum").val("");
		$("#manNum").val("");
		$("#womanNum").val("");
	}
	
	function init_date2(){
		$("#tabNum").val("");
	}
	
	
	function getOrderJsons(){
		var list = [];
		var sdd = {};
		sdd.globalsperequire = "";
	    var row = $('#dishList').datagrid('getChecked');
		$.each(row,function(index,item){
			var rowObj = {};
			$.each(editList,function(indexs,items){
				if(items.dishid==item.dishid){					
					rowObj.printtype="";
					rowObj.orderprice = item.price;
					rowObj.dishstatus = "1";
					rowObj.dishid = item.dishid;
					if(item.dishtype=="0"){
					rowObj.dishes = "";
					rowObj.dishnum = items.num;
					}else if(item.dishtype=="1" && item.content!=""){
					var lists = [];
				    lists = item.content.split(",");
				    var listss = [];
				    for(var i=0;i<lists.length;i++){
				    	$.ajax({
							type:"post",
							async:false,
							url : global_Path+"/dish/findById/"+lists[i]+".json",
							contentType:'application/json;charset=UTF-8',
							dataType : "json",
							success : function(data) {
						    var rowssObj={};	
						    rowssObj.printtype=""
						    rowssObj.orderprice = data.price;
						    rowssObj.dishstatus = "1";
						    rowssObj.dishid=data.dishid; 
						    rowssObj.dishes = "";
						    rowssObj.userName = userName;
						    rowssObj.dishunit = data.unit;
						    rowssObj.orderid = orderid;
						    rowssObj.relatedishid = "";
						    rowssObj.dishtype = data.dishtype;
						    rowssObj.orderseq = "";
						    if(i==0){
						    	 rowssObj.dishnum = "1";
						    }else{
						    	 rowssObj.dishnum = items.num;
						    }					   
						    rowssObj.sperequire = "";
						    listss.push(rowssObj);						   
							}							
						});
				    }
				        rowObj.dishes = listss;
				        rowObj.dishnum = "1";
					}else if(item.dishtype="2"){
						var relatedishid = "dd8ec00b-afb5-4106-ade9-b5175104a13c,a3b1e93e-54ea-4065-a7bf-8745eeadfc1e,a3b1e93e-54ea-4065-a7bf-8745eeadfc1e,51aa380d-5b72-4468-8274-f7bfadb1abe1,c605141a-1af4-4a4a-ae6c-51d5bd0c28c4,bf464b1d-d86b-48e7-947c-30d62da724db";
						var lists = [];
					    lists = relatedishid.split(",");
					    var listss = [];	
					    for(var i=0;i<lists.length;i++){
					    	$.ajax({
								type:"post",
								async:false,
								url : global_Path+"/dish/findById/"+lists[i]+".json",
								contentType:'application/json;charset=UTF-8',
								dataType : "json",
								success : function(data) {
					//			alert(data.dishid);
							    var rowssObj={};	
							    rowssObj.printtype=""
							    rowssObj.orderprice = data.price;
							    rowssObj.dishstatus = "1";
							    rowssObj.dishid=data.dishid; 
							    rowssObj.userName = userName;
							    rowssObj.dishunit = data.unit;
							    rowssObj.orderid = orderid;
							    rowssObj.relatedishid = "";
							    rowssObj.dishtype = data.dishtype;
							    rowssObj.orderseq = "";
							    if(data.dishid=="dd8ec00b-afb5-4106-ade9-b5175104a13c"){							    	
							    	 rowssObj.dishnum = "1";
							    	 var relatedishidy = "a22b37b1-b7cd-4ef3-b143-5ea8ed877bb6,d815ac17-a5ad-4563-9107-1710c8294079,ebbb2173-75d6-4abe-b2b6-fe0476511cd9"
							    		var listsy = [];
									    listsy = relatedishidy.split(",");
									    var listssy = [];	
									    for(var i=0;i<listsy.length;i++){
									    	$.ajax({
												type:"post",
												async:false,
												url : global_Path+"/dish/findById/"+listsy[i]+".json",
												contentType:'application/json;charset=UTF-8',
												dataType : "json",
												success : function(data) {
											    var rowssyObj={};	
											    rowssyObj.printtype="";
											    rowssyObj.orderprice = data.price;
											    rowssyObj.dishstatus = "1";
											    rowssyObj.dishid=data.dishid; 
											    rowssyObj.dishes = "";
											    rowssyObj.userName = userName;
											    rowssyObj.dishunit = data.unit;
											    rowssyObj.orderid = orderid;
											    rowssyObj.relatedishid = "";
											    rowssyObj.dishtype = data.dishtype;
											    rowssyObj.orderseq = (i+1).toString();											   
											    if(i==0){
											    	 rowssyObj.dishnum = "1";
											    }else{
											    	 rowssyObj.dishnum = "2";
											    }											   				   
											    rowssyObj.sperequire = "";
											    listssy.push(rowssyObj);						   
												}							
											});
									    }
									    rowssObj.dishes = listssy;
							    }else{
							    	 rowssObj.dishes = "";
							    	 rowssObj.dishnum ="1";
							    }					   
							    rowssObj.sperequire = "";
							    listss.push(rowssObj);						   
								}							
							});
					    }
						 rowObj.dishes = listss;
					     rowObj.dishnum = "1"; 
						
					}					
					rowObj.userName = userName;
					rowObj.dishunit = item.fitemDesc1;
					rowObj.orderid = orderid;
					if(item.dishtype=="2"){
					rowObj.relatedishid = "dd8ec00b-afb5-4106-ade9-b5175104a13c,a3b1e93e-54ea-4065-a7bf-8745eeadfc1e,a3b1e93e-54ea-4065-a7bf-8745eeadfc1e,51aa380d-5b72-4468-8274-f7bfadb1abe1,c605141a-1af4-4a4a-ae6c-51d5bd0c28c4,bf464b1d-d86b-48e7-947c-30d62da724db";
					}else{
					rowObj.relatedishid = "";
					}					
					rowObj.dishtype = item.dishtype;
					rowObj.orderseq = "";					
					rowObj.sperequire = "";					
				}
			
			});
			list.push(rowObj);
		});		
		sdd.currenttableid=deskNum;
		sdd.rows = list;
		sdd.orderid = orderid;
		return	JSON.stringify(sdd);
	}
	
	
	function padXiadan(){
		var json = getOrderJsons();
		$.ajax({
			type:"post",
			async:false,
			url : global_Path+"/padinterface/bookorderList",
			data:json,
			contentType:'application/json;charset=UTF-8',
			dataType : "json",
			success : function(data) {
				$('#xiadanDialog').dialog('close');
				if(data.result==0){
					$.messager.alert('下单','下单成功！','info',null);
				}else{
					$.messager.alert('下单','下单失败！','info',null);
				}
			}
		});
	}
	
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
<input type="button" id="xiadan" name="xiadan" value="下单" onclick="xiandan();" style="height:50px;width:200px;"/>
<div id="kaitaiDialog" style="overflow: hidden;">
	<input type="hidden" id="content" name="content" />
    <div style="width: 100%; float: left;">
         <div style="width: 100%; float: left;margin-top:10px;">
                &nbsp;&nbsp;&nbsp;&nbsp;服务员编号：<input type="text" id="employeeNum" name="employeeNum"  />
         </div>
         <div style="width: 100%; float: left;margin-top:10px;">
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;桌号:<input id="deskNum" name="deskNum" />
         </div>
         <div style="width: 100%; float: left;margin-top:10px;">
                                          用餐人数（男）：<input type="text" id="manNum" name="manNum"/>
         </div>
         <div style="width: 100%; float: left;margin-top:10px;">
                                          用餐人数（女）：<input type="text" id="womanNum" name="womanNum"/>
         </div>  
    </div>
</div>
	
<div id="xiadanDialog">
    <div style="width: 100%; float: left;">
         <table id="dishList"></table> 
    </div>
</div>	

<div id="showDetialDialog">
    <div style="width: 100%; float: left;">
        <div id="danpinNum" style="width: 100%; float: left;margin-top:10px;">
                                          数量：<input type="text" id="tabNum" name="tabNum"/>
         </div>   
    </div>
</div>	

</body>
</html>