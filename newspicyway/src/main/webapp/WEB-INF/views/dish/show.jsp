<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/datagrid-groupview.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
 	
	<script type="text/javascript">
		$(function(){
			//判断页面是第一次加载
			var flag=0;
			//话题表格配置
			$('#dishShow').datagrid({    
				url : global_Path+"/dish/page.json",
				method : 'post',
				fit : false,
// 				title : '所有用户',
				fitColumns : true,
				singleSelect : true,
				pagination : true,
				rownumbers : true,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
				remoteSort : false,
  				idField : 'dishid',
				loadMsg : '数据装载中......',
				columns : [ [{
					field : 'dishno',
					title : '编号',
					width : 10,
					align : 'left',
				},{
					field : 'title',
					title : '名称',
					width : 10,
					align : 'center',
				},{
					field : 'itemDesc',					
					title : '分类',
					width : 10,
					align : 'center',
				},{
					field : 'dishtype',					
					title : '菜品类别',
					width : 10,
					align : 'center',
					formatter:function(value, row, index){  
						if(value==0){
							return "单品";
						}
						if(value==1){
							return "组合";
						}
					}
				},{
					field : 'unit',					
					title : '单位',
					width : 10,
					align : 'center',
					formatter:function(value, row, index){  
						if(value=="/"){
							return "";
						}else{
							return value;
						}
					}
				},{
					field : 'price',					
					title : '单价',
					width : 10,
					align : 'center',
					formatter:function(value, row, index){  
						if(value=="/"){
							return "";
						}else{
							return value;
						}
					}
				},{
					field : 'vipprice',					
					title : '会员价',
					width : 10,
					align : 'center',
					formatter:function(value, row, index){  
						if(value=="/"){
							return "";
						}else{
							return value;
						}
					}
				},{
					field : 'printer',					
					title : '打印口',
					width : 10,
					align : 'center',
				},{
					field : 'dishid',
					title : '操作',
					width : 10,
					align : 'center',
					formatter : function(value, row, index) {
						var modify ="<a href=\"###\" onclick=\"modify_dish('" + value + "')\">查看/修改</a>";
						var del = "&nbsp;&nbsp;<a href=\"###\" onclick=\"del_dish('" + value + "')\">删除</a>";
					    return modify+del;
					}
				}
				] ],
				onBeforeLoad:function(){
					if(flag==0){
					$.ajax({
						type : "post",
						async : false,
						url : global_Path+"/dishtype/findAll/0.json",
						dataType : "json",
						success : function(result) {
							$("#0").css("width",(1/(result.length+1))*100+"%");
							$.each(result,function(index,item){
								$('#dishTypeTr').append("<td id=\""+item.id+"\"align=\"center\" width=\""+(1/(result.length+1))*100+"%\" class=\"dishTypeUnSelected\" onclick=\"gethiddenId('"+item.id+"')\">"+item.itemdesc+"</td>");
							});
							
						}
					});
					flag=1;
					}
				},
				
				toolbar : '#toolbar_datadictionary'
			});
			//分页参数配置
			$('#dishShow').datagrid('getPager').pagination({
				displayMsg : '当前显示从{from}到{to}共{total}记录',
				beforePageText : '第',
				afterPageText : '页 共 {pages} 页',
				onBeforeRefresh : function(pageNumber, pageSize) {
					$(this).pagination('loading');
					$(this).pagination('loaded');
				}
			});
			$('#addDishDialog').dialog({   
			    title: '菜品管理(*号部分为必填项)',   
			    width:1150,   
			    height: 580,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存',
					handler : 
					function(){//dishno,title
						    $('#dishno').validatebox({required: true,validType:'maxLength[1,20]',missingMessage:"菜品编号不能为空！"});
						    $('#title').validatebox({required:true,validType:'maxLength[1,20]',missingMessage:"菜品名称不能为空！"});
							$("#dishno").validatebox('enableValidation');
							$("#title").validatebox('enableValidation');
						    if ($('#dishno').validatebox('isValid') && $('#title').validatebox('isValid')){
								save_dish();
							}	
					}						
				}, {
					text : '取消',
					handler : function() {
						init_data();
						$('#addDishDialog').dialog('close');
					}
				}, {
					text : '重置',
					handler :function(){
						init_data();
					}
				}
				],
			onClose : function() {
				init_data();
			}
			});	
			$('#columnid').combobox({
			    url:global_Path + '/dishtype/getDataDictionaryTag.json',
			    valueField:'id',
			    textField:'itemdesc',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
			$('#printer').combobox({
			    url:global_Path + '/dish/getPrintersList.json',
			    valueField:'printerid',
			    textField:'printername',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
			
			$('#unit1').combobox({
			    url:global_Path + '/datadictionary/getDatasByType/UNIT.json',
			    valueField:'id',
			    textField:'itemDesc',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
// 			$('#special').combobox({
// 			    url:global_Path + '/datadictionary/getDatasByType/SPECIAL.json',
// 			    valueField:'id',
// 			    textField:'itemDesc',
// 			    panelHeight : 'auto',
// 			    multiple:true,
// 			    loadFilter : function(row){
// 			    	return row;
// 				}
// 			});
			$('#dishtype').combobox({
				onChange:function(newValue, oldValue){
				var value=$('#dishtype').combobox('getValue');
				if(value==0){
					$("#showdiff").css("display","");
					$("#dishListDiv").css("display","none");
				}else{
					$("#showdiff").css("display","none");
					$("#dishListDiv").css("display","");
					$("tr[id^='addtr']").remove();
					trflag=1;
					$("#unit1").combobox('setValue','');
		    	    $("#vipprice1").val("");
		    	    $("#price1").val("");
				}
				$('#dishList').datagrid({    
					url : global_Path+"/dish/getDishList.json",
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
						width : 20,
						align : 'left',
					}
					] ],
					onLoadSuccess:function(data){
						$('#dishList').datagrid('collapseGroup');
						var dishidlist=$('#content').val();
						if(dishidlist!=''&&typeof(dishidlist) != "undefined"){
						$.each(dishidlist.split(","),function(dishindex,dishid){
							$.each(data.rows, function(index, item){
			     			 if(item.dishid==dishid){
			     				$('#dishList').datagrid('checkRow', index);
			     			}
							});
						});
						}
					},
		});
	}
});
});
//------------------------------------------------------------------------------------------------------------		
//------------------------------------------------------------------------------------------------------------
       var trflag=1;
 		function setCommoboxValue(id){
			$('#'+id).combobox({
				data:$('#unit1').combobox('getData'),
			    valueField:'id',
			    textField:'itemDesc',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
		}
//添加一组计量单位
//	 			var str=$(trObj).parent().parent().clone();
//	 			$(trObj).parent().parent().after(str);
		function addtrStr(){
			var str="<tr id='addtr"+trflag+"'>"+
			"<td>计量单位:</td>"+
			"<td><input id='unit"+trflag+"'/></td>"+
			"<td>原价:</td><td><input id='price"+trflag+"'/></td>"+
			"<td>会员价:</td><td nowrap='nowrap'><input id='vipprice"+trflag+"'/> "+
			"<img  src='"+global_Path+"/images/del.png' onclick='delUnitTr(this)'  style='width: 12px;height: 12px;cursor: pointer;'/></td></tr>";
			return str;
		}
		function addUnitTr(trobj){
			trflag=trflag+1;
			var str=addtrStr();
// 			$(trobj).parent().parent().after(str);
			$("#beforetr").before(str);
			setCommoboxValue('unit'+trflag);
		}
		//删除行
		function delUnitTr(trobj){
			$(trobj).parent().parent().remove();
			trflag=trflag-1;
		}
		function init_data(){
			$("#id").val("");
			$("#content").val("");
    	    $("#dishno").val("");
    	    $("#dishtype").combobox('select','0');
    	    $("#abbrdesc").combobox('setValue','');
    	    $("#title").val("");
    	    $("#columnid").combobox('setValue','');
    	    $("#printer").combobox('setValue','');
    	    $("#label").combobox('setValue','');
    	    $("#special").combobox('setValue','');
    	    $("#introduction").val("");
    	    $("#unit1").combobox('setValue','');
    	    $("#vipprice1").val("");
    	    $("#price1").val("");
    	    $("#removepic").hide(); 
    	    imagePath='';
    	    $("#file1Img").attr("src","");
			$("#file1Img").attr("style","visible:hidden");
  	      	$("#file1").val("");
  	      	$("tr[id^='addtr']").remove();
  	      	trflag=1;
		}
		//获取checkbox选择的子类
		function getSecletdish(){
			var idlist=[];
			$.each($('#dishList').datagrid('getChecked'),function(index,item){
				idlist.push(item.dishid);
			});	
			return idlist.join(',');
		}
		//判断是否是多计量单位的菜品
		//多计量单位标识  标识这个菜是否是多计量的   0有多计量   1没有多计量
		function validDishUnit(){
			if(trflag==1){
				$("#unitflag").val("1");
			}else{
				$("#unitflag").val("0");
			}
		}
		//获取多计量单位值的字符串 flag=0（  input框）   flag=1（ combobox框）
		function getvaluesList(name,flag){
			var valueList=[];
			 if(flag==0){
				 $.each($("input[id^='"+name+"']"),function(index,item){
			     valueList.push(changeTwoDecimal_f($(this).val()));
				 });
			 }else{
				 $.each($("input[id^='"+name+"']"),function(index,item){
			     var object=this;
			     if(object.id!='unitflag'){
			  	  valueList.push($("#"+object.id).combobox("getText")); 
			     }
			   });
			 }
			 return valueList.join("/");
		}
		
		var imagePath='';
		function save_dish(){
			var childrendishList=getSecletdish();
			validDishUnit();
			var unitList=getvaluesList('unit',1);
			var vippriceList=getvaluesList('vipprice',0);
			var priceList=getvaluesList('price',0);
			 var image;
			  if($("#file1").val()==''){
			    	image=imagePath;
			    }else{
			    	image="";
			    } 
			  //dishno,dishtype,abbrdesc,title,columnid,printer,unit,label,introduction
			  $.ajaxFileUpload({
				    fileElementId: ['file1'],  
				    url: global_Path+'/dish/save',  
				    dataType: 'json',
				    contentType:'application/json;charset=UTF-8',
				    data: { 
				    		dishid:	$("#id").val(),
				    	    dishno:	$("#dishno").val(),
				    	    dishtype:	$("#dishtype").combobox('getValue'),
				    	    abbrdesc:	$("#abbrdesc").combobox('getValue'),
				    	    title: $("#title").val().replace("\"","“").replace("\"","”"),
			        	    columnid:$("#columnid").combobox('getValue'),
			        	    printer:	$("#printer").combobox('getValue'),
			        	    unit:	unitList,
			        	    label:	$("#label").combobox('getValue'),
				    	    introduction: $("#introduction").val().replace("\"","“").replace("\"","”"),
				    	    vipprice: vippriceList,
				    	    price: priceList,
			        	    image:image,
			        	    content:childrendishList,
// 			        	    imagetitle:$("#special").combobox('getValues')
			        	    imagetitle:$("#special").combobox('getValues'),
			        	    headsort:$("#unitflag").val()
				    	   },  
				    beforeSend: function (XMLHttpRequest) {
				    	
				    },  
				    success: function (data, textStatus) { 
				    	saveDishUnit(data.tdish.dishid);
				        alert(data.message);
			    		$('#dishShow').datagrid('reload');
			    		$('#addDishDialog').dialog('close');
			    		init_data(); 
				    },  
				    complete: function (XMLHttpRequest, textStatus) {  
				    } 
			    });
		}
		function saveDishUnit(thisdishid){
			var json=getUnitvalues(thisdishid);
			$.ajax({
				type:"post",
				async:false,
				url : global_Path+'/dishunit/save.json',
				contentType:'application/json;charset=UTF-8',
			    data:json, 
				dataType : "json",
				success : function(result) {
				}
			});
			init_data();
		}
		//获取多计量单位的值
		function getUnitvalues(thisdishid){
			var unitList=[];
			 $.each($("input[id^='price']"),function(index,item){
				var object=this;
				var i=object.id.substring(5);
				if(i!=''){			
				  temp={};
				  temp.dishid=thisdishid;
				  temp.unit=$("#unit"+i).combobox("getValue");
				  temp.price=$("#price"+i).val();
				  temp.vipprice=$("#vipprice"+i).val();
				  temp.status=0;
				  temp.ordernum=0;
				  unitList.push(temp);
				}
			 });
			  return JSON.stringify(unitList); 
		}
		//修改查询
		function modify_dish(id){
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/dish/findById/"+id+".json",
				dataType : "json",
				success : function(result) {
					imagePath=result.image;
					$("#id").val(result.dishid);					
					$("#dishno").val(result.dishno);				
					$("#title").val(result.title);
// 					$("#price").val(result.price);
// 					$("#vipprice").val(result.vipprice);
					$("#content").val(result.content);
					$("#introduction").val(result.introduction);
					$('#dishtype').combobox('select', result.dishtype);
					$('#abbrdesc').combobox('select',result.abbrdesc);
					$('#columnid').combobox('select',result.columnid);
					$('#printer').combobox('select',result.printer);
					$('#label').combobox('select',result.label);
// 					if(typeof(result.imagetitle) != "undefined"){
// 					$('#special').combobox('setValues',result.imagetitle.split(','));
// 					}
// 					$('#unit').combobox('select',result.unit);
					$('#special').combobox('select',result.imagetitle);
					if(result.image!=""&&typeof(result.image) != "undefined"){
						$("#file1Img").attr("src",global_Path+'/'+result.image);
						$("#file1Img").attr("style","width:200px;height:160px");
						$("#removepic").show();
					};
				}
			});
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/dishunit/getunitList/"+id+".json",
				dataType : "json",
				success : function(result) {
					$.each(result,function(index,item){
						trflag=index+1;
						if(index==0){
							$("#price1").val(item.price);
		 					$("#vipprice1").val(item.vipprice);
		 					$('#unit1').combobox('select',item.unit);
						}else{
							var str=addtrStr();
							$("#beforetr").before(str);
							setCommoboxValue('unit'+trflag);
							$("#price"+trflag).val(item.price);
		 					$("#vipprice"+trflag).val(item.vipprice);
		 					$('#unit'+trflag).combobox('select',item.unit);
						}
					});
				}
			});
				$('#addDishDialog').dialog('open');  
		}
		function removepic(){
			$("#removepic").hide();
			$("#file1Img").hide();
			imagePath='';
		}

		function del_dish(id){
			$.messager.confirm("菜品管理","确认删除此数据吗？",function(r){
				if (r){
					$.ajax({
						type : "post",
						async : false,
						url : global_Path+"/dish/delete/"+id+".json",
						dataType : "json",
						success : function(result) {							
							$('#dishShow').datagrid('reload');
							$.messager.alert('菜品管理',result,'info',null);
						}
					});
				}
			});
		}
		
		function add_datadictionary(){
			$('#addDishDialog').dialog('open');  
			init_data();		
		}
		function getlist(value){
			$('#dishShow').datagrid('load',{
				"source": $(".dishTypeSelected").attr("id"),
				 "title":value
			});
	    }
		function gethiddenId(id){
			$("#dishTypeTr td").attr("class","dishTypeUnSelected");
			$("#"+id).attr("class","dishTypeSelected");
			$('#dishShow').datagrid('load',{
				"source": id,
				 "title":$('#dishNameSearch').searchbox('getValue')
			});
		}
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding: 10px 10px;">
		<table id="dishShow"></table>  
		<div id="toolbar_datadictionary" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr style="border-bottom: 1px solid #9A9691;">
					<td align="left" width="400" ><font style="font-size: 18px; color: #000; font-weight: 700;">菜品管理</font></td>
				    <td align="right" width="80"><input type="button" onclick="add_datadictionary()" value="新增菜品" class="xld_addbutton"></input></td>
				    <td align="center" width="80"><input id="dishNameSearch" class="easyui-searchbox" style="width:150px;"  data-options="searcher:getlist"  prompt="菜品搜索" /></td>
				</tr>
			</table>
			<table cellspacing="0" cellpadding="0" border="1px" style="width: 100%;margin: 10px 0px; border:1px solid #9A9691; " id="selectItem">
				<tr style="height: 32px;" id="dishTypeTr">
					<td id="0" align="center" class="dishTypeSelected" onclick="gethiddenId('0')">全部</td>
				</tr>
			</table>
		</div>
	 	<div id="addDishDialog" style="overflow: hidden;">
			<input type="hidden" id="id" name="id" />
			<input type="hidden" id="unitflag" name="unitflag" />
			<input type="hidden" id="content" name="content" />
			<div style="width: 65%;float: left;">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
			<!-- dishno,dishtype,abbrdesc,title,columnid,printer,unit,price,vipprice,label,introduction -->
				<tr align="left">
					<td>菜品编号:</td>
					<td><input type="text" id="dishno" name="dishno" /><span style="color: red">*</span></td>
					<td>菜品类别:</td>
					<td><input class="easyui-combobox" id="dishtype" name="dishtype"
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '单品'},{label: '1',value: '组合'}]" /> </td>
					
				</tr>
				<tr align="left">
					<td>菜品名称:</td>
					<td><input type="text" id="title" name="title" /><span style="color: red">*</span></td>
					<td>菜品分类:</td>
					<td><input id="columnid" name="columnid"/></td>
					<td>打印口:</td>
					<td>
<!-- 					<input class="easyui-combobox" id="printer" name="printer" -->
<!-- 						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '无',value: '无'},{label: '厨打01',value: '厨打01'}, -->
<!-- 						{label: '厨打02',value: '厨打02'},{label: '厨打03',	value: '厨打03'},{label: '厨打04',	value: '厨打04'}]" />  -->
					    <input id="printer" name="printer"/>
					</td>
				</tr>
				<tr align="left" id="showdiff" >
					<td>计量单位:</td>
<!-- 					<input class="easyui-combobox" id="unit" name="unit" -->
<!-- 						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '份'},{label: '1',value: '盘'}, -->
<!-- 						{label: '2',value: '斤'},{label: '3',	value: '个'},{label: '4',	value: '只'}]" />  -->
					<td>
						<input id="unit1" name="unit1"/>
					</td>
					<td>原价:</td>
					<td><input id="price1" name="price1"/></td>
					<td>会员价:</td>
					<td nowrap="nowrap"><input id="vipprice1" name="vipprice1"/>
					<img name="addimg" src="<%=request.getContextPath() %>/images/add.png" onclick="addUnitTr(this)"  style="width: 12px;height: 12px;cursor: pointer;"/>
				    </td>
				</tr>
				<tr align="left" id="beforetr">
					<td>特色菜品:</td>
					<td><input class="easyui-combobox" id="abbrdesc" name="abbrdesc" 
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '是'},
						{label: '1',value: '否'}]" /> </td>
					<td>菜品状态:</td>
					<td><input class="easyui-combobox" id="label" name="label"
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '充足 '},{label: '1',value: '缺少'}]" /> </td>
					<td>忌口:</td>
					<td>
<!-- 					<input  id="special" name="special"  />  -->
					<input class="easyui-combobox" id="special" name="special"
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '是 '},{label: '1',value: '否'}]" />
					</td>
				</tr>
				<tr align="left">
					<td>菜品简介:</td>
					<td colspan="3"><textarea rows="1" cols="1" id="introduction" style="width: 98%;height: 70px;font-size: 12px;"></textarea></td>
				</tr>
				<tr align="left">
					<td>图片:</td>
					<td colspan="5"><input type="file"  id="file1" name="file1"/>
           			</td>
				</tr>
				<tr align="left">
					<td></td>
					<td colspan="6">
            		<img id="file1Img" src="" alt="" style="visible:hidden"/>
           			<input id="removepic" type="button" value="删除" onclick="removepic();" style="display:none;"/>
           			</td>
				</tr>
			</table>
			</div>
			<div  id="dishListDiv" style="width: 34%;height:100%; float: left;display: none;overflow-y: scroll; ">
				<table id="dishList"></table> 
			</div>
		</div> 
	</div>
</body>
</html>
