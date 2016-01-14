<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/OneDblClick.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/datagrid-groupview.js"></script>
 	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/dishtemplate.css"/>
<title>Insert title here</title>
<script type="text/javascript">
var articleids;//保存后选中的文章id
var oldarticleids;//保存前选中的文章id
var thistemplate;//当前模板块
var imagePath='';//图片路径
var i=0;//模板包含的菜品
var typeflag='';//当前选择的模板类型
var selectId='';//当前选择的菜品id
var trflag=1;//多计量单位  行数
$(function(){
	//话题表格配置
	$('#tables').datagrid({
		url : global_Path + '/template/page.json',
		method : 'post',
		fit : false,
		fitColumns : true,
		singleSelect : true,
		pagination : true,
		rownumbers : true,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		remoteSort : false,
		idField : 'id',
		loadMsg : '数据装载中......',
		columns : [ [ {
			field : 'name',
			title : '版式名称',
			width : 50,
			align : 'left'
		}, {
			field : 'type',
			title : '版式类型',
			width : 50,
			align : 'center'
		},  {
			field : 'userName',
			title : '创建人',
			width : 50,
			align : 'center',
			formatter : function(value, row, index){
			 return value;
			}
		},  {
			field : 'itemDesc',
			title : '分类',
			width : 50,
			align : 'center',
			formatter : function(value, row, index){
			 return value;
			}
		},{
			field : 'createTimeateTime',
			title : '创建时间',
			width : 50,
			align : 'center',
			formatter : function(value, row, index){
			 return value;
			}
		}, {
			field : 'sort',
			title : '排序',
			width : 50,
			align : 'center',
			formatter : function(value, row, index){
			 return value;
			}
		}, {
			field : 'id',
			title : '操作',
			width : 48,
			align : 'center',
			formatter : function(value, row, index) {
				var modify ="<a href=\"###\" onclick=\"modify_object('" + value + "')\">查看/修改</a>";
				var del = "&nbsp;&nbsp;<a href=\"###\" onclick=\"delete_object('" + value + "')\">删除</a>";
				return modify+del;
			}
		}
		] ],
		toolbar : '#toolbar_t'
	});
	//分页参数配置
	$('#tables').datagrid('getPager').pagination({
		displayMsg : '当前显示从{from}到{to}共{total}记录',
		beforePageText : '第',
		afterPageText : '页 共 {pages} 页',
		onBeforeRefresh : function(pageNumber, pageSize) {
			$(this).pagination('loading');
			$(this).pagination('loaded');
		}
	});
			
	$('#addObjectDialog').dialog({   
	    title: '增加版式  (单击版式新建菜品,双击从菜品资源选择, *号部分为必填项)',   
	    width: 500,   
	    height: 580,
	    zIndex:1000000,
	    closed: true,   
	    cache: false,  
	    inline : true,
	    modal: true,
		buttons : [ {
			text : '保存',
			handler :function() {
				save_object();
			}
		}, {
			text : '关闭',
			handler : function() {
				$('#addObjectDialog').dialog('close');
				init_data();
				$('#tables').datagrid('reload');
			}
		} ],
	onClose : function() {
		init_data();
	}
	});
	$('#templatedishtype').combobox({
	    url:global_Path + '/dishtype/findAll/0.json',
	    valueField:'id',
	    textField:'itemdesc',
	    panelHeight : 'auto',
	    loadFilter : function(row){
	    	return row;
		}
	});

	//单击事件
	var click_event = function(e){
		var arr=["","A1","B1", "B2","C1", "C2", "C3","D1", "D2", "D3","E1","E2","E3","E4","F1","F2","F3","F4",
		         "G1","G2","G3","G4","H1","H2","H3","H4","H5","H6","H7","H8",
		         "I1","I2","I3","I4","I5","I6","I7","I8","I9","I10","I11","I12","I13","I14"];
		if(jQuery.inArray($(this).html(), arr)=='-1'){//查询修改
			modify_dish($('#'+$(this).attr("id")+'dishid').html());
		} else{
			$('#addDishDialog').dialog('open'); 
		}
		typeflag=$(this).attr("id");
		thistemplate=this;
	};
	//双击事件
	var dblclick_event = function(e){
		getDishDatagrid(this);
		typeflag=$(this).attr("id");
		$('#dishListDialog').dialog('open');
	};
    $("div[template='template']").OneDblClick({oneclick:click_event,dblclick:dblclick_event});
			
		
	$('#columnid').combobox({
	    url:global_Path + '/dishtype/getDataDictionaryTag.json',
	    valueField:'id',
	    textField:'itemdesc',
	    panelHeight : 'auto',
	    loadFilter : function(row){
	    	return row;
		}
	});
	$('#selectTemplate').combobox({
		onSelect: function(record){
			var value=$('#selectTemplate').combobox('getValue');
			$('.tempalteTop'+value).css("display","block");
		}, 
		onChange:function(newValue, oldValue){
		$("div[class^='tempalteTop']").css("display","none");
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
			function(){
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
				dish_init_data();
				$('#addDishDialog').dialog('close');
			}
		}, {
			text : '重置',
			handler :function(){
				dish_init_data();
			}
		}
		],
	onClose : function() {
		dish_init_data();
	}
	});	
	$('#dishListDialog').dialog({   
		title: '请选择菜品 ',   
	    width: 500,   
	    height: 600,   
	    closed: true,   
	    cache: false,  
	    inline : true,
	    modal: true,
	    buttons : [ {
			text : '关闭',
			handler : function() {
				$("#dishListDialog").dialog('close');
			}
		}],
	onClose : function() {
	},
	toolbar : '#toolbar_list'
	});	
	//打印机
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
	//忌口
// 	$('#special').combobox({
// 	    url:global_Path + '/datadictionary/getDatasByType/SPECIAL.json',
// 	    valueField:'id',
// 	    textField:'itemDesc',
// 	    panelHeight : 'auto',
// 	    multiple:true,
// 	    loadFilter : function(row){
// 	    	return row;
// 		}
// 	});
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
//-------------------------------------------------------------------------------------------------------------
//加载菜品信息
 function getDishDatagrid(template){
   	$('#dishListTable').datagrid({
   		url : global_Path + '/dish/page.json?isselect=1',
		method : 'post',
		fit : false,
		fitColumns : true,
		selectOnCheck: true,
		singleSelect: false,
		pagination : true,
		rownumbers : true,
		pageSize : 10,
		pageList : [ 10, 20, 30],
		remoteSort : false,
		idField : 'dishid',
		loadMsg : '数据装载中......',
		cache:false,
		columns : [ [ 
		{
			field : 'dishid',
			title : '请选择',
			width : 50,
			align : 'left',
			checkbox: true
		},{
			field : 'dishno',
			title : '编号',
			width : 20,
			align : 'left'
		},{
			field : 'title',
			title : '名称',
			width : 50,
			align : 'left'
		}		
		]],
   	
   	onLoadSuccess:function(data){
    	$('#dishListTable').datagrid('unselectAll');
    	 var id=$('#'+typeflag+'dishid').html();
		   if(id!=''&&id!='undefined'&&id!=undefined){
		      if(data){
     			$.each(data.rows, function(index, item){
     			 if(item.dishid==id){
     				selectId=id;
     				$('#dishListTable').datagrid('checkRow', index);
     			} 
     			});
		   }
		   }
   	},
   	onSelect:function(rowIndex, rowData){
   		if(selectId!=rowData.dishid){
   		i++;
   		$("#dishListDialog").dialog('close');
   		var imagepath = rowData.image;
		 var img=document.createElement("img");
		 img.src=imagepath;
		 template.innerHTML="<span id='"+typeflag+"dishid' style='display:none'>"+rowData.dishid+"</span>"+
		 "<span id='"+typeflag+"dishname'>"+rowData.title+"</span><br>"+
		 "<span id='"+typeflag+"image' style='display:none'>"+rowData.image+"</span>"+
		 "<span id='"+typeflag+"dishtype' style='display:none'>"+rowData.dishtype+"</span>"+
		 "<span id='"+typeflag+"itemtype' style='display:none'>"+rowData.source+"</span>"+
		 "<span id='"+typeflag+"unit' style='display:none'>"+rowData.unit+"</span>"+
		 "<span id='"+typeflag+"cookietype' style='display:none'>"+rowData.unit+"</span>"+
		 "<span id='"+typeflag+"cusumers' style='display:none'>"+rowData.orderNum+"</span>"+
		 "<span id='"+typeflag+"introduction' style='display:none'>"+rowData.introduction+"</span>"+
		 "<span id='"+typeflag+"unitflag' style='display:none'>"+rowData.headsort+"</span>"+
		 "<span id='"+typeflag+"specialflag' style='display:none'>"+rowData.imagetitle+"</span>"+
		 "<span id='"+typeflag+"speciality' style='display:none'>"+rowData.abbrdesc+"</span>"+
		 "￥<span id='"+typeflag+"normalprice'>"+rowData.price+"</span>"+
		 "<span id='"+typeflag+"couponsprice' style='display:none'>"+rowData.vipprice+"</span>"+
		 "会员价￥<span  id='"+typeflag+"discountprice'>"+rowData.vipprice+"</span>";
		 if(rowData.image!=""&&rowData.image!='undefined'&&rowData!=undefined){//文章图片为空
			 template.style.backgroundImage="url("+global_Path+"/"+imagepath+")";
			 template.style.backgroundRepeat="no-repeat";
		 }
   	}},
   	onUnselect:function(rowIndex, rowData){
   		template.innerHTML="";
   		selectId="";
   		template.style.backgroundImage="";
   		$("#dishListDialog").dialog('close');
   	}
   	
	});
   	//分页参数配置
	$('#dishListTable').datagrid('getPager').pagination({
		displayMsg : '当前显示从{from}到{to}共{total}记录',
		beforePageText : '第',
		afterPageText : '页 共 {pages} 页',
		onBeforeRefresh : function(pageNumber, pageSize) {
			$(this).pagination('loading');
			$(this).pagination('loaded');
		}
	});
   }
//修改查询
function modify_object(id){
	i++;
	var oldarts=[];
	$.ajax({
		type : "post",
		async : false,
		url : global_Path+"/template/findById/"+id+".json",
		dataType : "json",
		success : function(result) {
			$("#templatenameid").val(result.id);
			$("#userId").val(result.createuserid);
			$("#status").val(result.status);
			$("#templatename").val(result.name);
			$("#sort").val(result.sort);
			$('#selectTemplate').combobox('select',result.type);
			$('#templatedishtype').combobox('select',result.dishtype);
			$('#addObjectDialog').dialog('open'); 
			var templatedata= result.data;
			var object=eval('('+templatedata+')');
			for(var i in object){
				 $('#'+i).html("<span id='"+i+"dishid' style='display:none'>"+object[i].dishid+"</span>"+
			    		 "<span id='"+i+"dishname'>"+object[i].dishname+"</span><br>"+
			    		 "<span id='"+i+"image' style='display:none'>"+object[i].image+"</span>"+
			    		 "<span id='"+i+"dishtype' style='display:none'>"+object[i].dishtype+"</span>"+
			    		 "<span id='"+i+"itemtype' style='display:none'>"+object[i].itemtype+"</span>"+
			    		 "<span id='"+i+"unit' style='display:none'>"+object[i].unit+"</span>"+
			    		 "<span id='"+i+"cookietype' style='display:none'>"+object[i].cookietype+"</span>"+
			    		 "<span id='"+i+"cusumers' style='display:none'>"+object[i].cusumers+"</span>"+
			    		 "<span id='"+i+"introduction' style='display:none'>"+object[i].introduction+"</span>"+
			    		 "<span id='"+i+"unitflag' style='display:none'>"+object[i].unitflag+"</span>"+
			    		 "<span id='"+i+"specialflag' style='display:none'>"+object[i].specialflag+"</span>"+
			    		 "<span id='"+i+"speciality' style='display:none'>"+object[i].speciality+"</span>"+
			    		 "￥<span id='"+i+"normalprice'>"+object[i].normalprice+"</span>"+
			    		 "<span id='"+i+"couponsprice' style='display:none'>"+object[i].couponsprice+"</span>"+
			    		 "会员价￥<span  id='"+i+"discountprice'>"+object[i].discountprice+"</span>");
				 var img=object[i].image;
				 if(img!=""&&img!='undefined'&&img!=undefined){
					 $('#'+i).attr({style:"background-repeat: no-repeat;background-image:url('"+global_Path+"/"+img+"');"});
		    		 }
				 oldarts.push(object[i].dishid);
			}
		}
	});
	
	oldarticleids = oldarts.join(',');
}		
function delete_object(id){
   $.messager.confirm("删除确认", "您确认删除选定的记录吗？", function (deleteAction) {
	   if (deleteAction) {
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/template/delete/"+id+".json",
				dataType : "json",
				success : function(result) {
					$('#tables').datagrid('reload');
				}
			});
		 }
        });
}
function open_dialog(){
	$('#addObjectDialog').dialog('open'); 
}
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
//			var str=$(trObj).parent().parent().clone();
//			$(trObj).parent().parent().after(str);
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
// 	$(trobj).parent().parent().after(str);
	$("#beforetr").before(str);
	setCommoboxValue('unit'+trflag);
}
//删除行
function delUnitTr(trobj){
	$(trobj).parent().parent().remove();
	trflag=trflag-1;
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
// 	dish_init_data();
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
//菜品修改查询
function modify_dish(id){//修改查询
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
				$("#price").val(result.price);
				$("#vipprice").val(result.vipprice);
				$("#introduction").val(result.introduction);
				$("#content").val(result.content);
				$('#dishtype').combobox('select', result.dishtype);
				$('#abbrdesc').combobox('select',result.abbrdesc);
				$('#columnid').combobox('select',result.columnid);
				$('#printer').combobox('select',result.printer);
// 				if(typeof(result.imagetitle) != "undefined"){
// 					$('#special').combobox('setValues',result.imagetitle.split(','));
// 				}
				$('#special').combobox('select',result.imagetitle);
				$('#label').combobox('select',result.label);
				$('#unit').combobox('select',result.unit);
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
function init_data(){
	  $.each($("div[template='template']"),function(){
		var object=this;
		$('#'+object.id).html(object.id);
		$('#'+object.id).removeAttr("style");
	  });
    $('#templatename').val("");
    $('#sort').val("");
    $('#templatenameid').val("");
    $('#userId').val("");
    $('#status').val("");
    articleids='';
    oldarticleids='';
    i=0;
    typeflag='';
    $('#selectTemplate').combobox('select','A');
    $('#templatedishtype').combobox('setValue','');
	}
function dish_init_data(){
	$("#id").val("");
    $("#dishno").val("");
    $("#dishtype").combobox('select','0');
    $("#abbrdesc").combobox('setValue','');
    $("#title").val("");
    $("#columnid").combobox('setValue','');
    $("#printer").combobox('setValue','');
    $("#unit1").combobox('setValue','');
    $("#label").combobox('setValue','');
    $("#introduction").val("");
    $("#vipprice1").val("");
    $("#price1").val("");
    $("#removepic").hide(); 
    $("#special").combobox('setValue','');
    imagePath='';
    $("#file1Img").attr("src","");
	$("#file1Img").attr("style","visible:hidden");
   	$("#file1").val("");
   	$("tr[id^='addtr']").remove();
    trflag=1;
}
function removepic(){
	$("#removepic").hide();
	$("#file1Img").hide();
	imagePath='';
}
//获取checkbox选择的子类
function getSecletdish(){
	var idlist=[];
	$.each($('#dishList').datagrid('getChecked'),function(index,item){
		idlist.push(item.dishid);
	});	
//		alert(idlist.join(','));
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
//保存菜品
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
	        	    label:	$("#label").combobox('getValue'),
		    	    introduction: $("#introduction").val().replace("\"","“").replace("\"","”"),
		    	    vipprice: vippriceList,
		    	    price: priceList,
		    	    unit:	unitList,
		    	    content:childrendishList,
		    	    imagetitle:$("#special").combobox('getValue'),
// 		    	    imagetitle:$("#special").combobox('getValues'),
	        	    image:image,
	        	    headsort:$("#unitflag").val()
		    	   },  
		    beforeSend: function (XMLHttpRequest) {  
		    },  
		    success: function (data, textStatus) { 
	    		alert(data.message);
	    		var imagepath = data.tdish.image;
	    		 var img=document.createElement("img");
	    		 img.src=imagepath;
// 	    			dishid :dishname : image : normalprice : discountprice : couponsprice :cookietype : 
// 	    			cusumers : unit :introduction :dishtype:itemtype:
	    		 thistemplate.innerHTML="<span id='"+typeflag+"dishid' style='display:none'>"+data.tdish.dishid+"</span>"+
	    		 "<span id='"+typeflag+"dishname'>"+data.tdish.title+"</span><br>"+
	    		 "<span id='"+typeflag+"image' style='display:none'>"+data.tdish.image+"</span>"+
	    		 "<span id='"+typeflag+"dishtype' style='display:none'>"+data.tdish.dishtype+"</span>"+
	    		 "<span id='"+typeflag+"itemtype' style='display:none'>"+data.tdish.source+"</span>"+
	    		 "<span id='"+typeflag+"unit' style='display:none'>"+data.tdish.unit+"</span>"+
	    		 "<span id='"+typeflag+"cookietype' style='display:none'>"+data.tdish.unit+"</span>"+
	    		 "<span id='"+typeflag+"cusumers' style='display:none'>"+data.tdish.orderNum+"</span>"+
	    		 "<span id='"+typeflag+"introduction' style='display:none'>"+data.tdish.introduction+"</span>"+
	    		 "<span id='"+typeflag+"specialflag' style='display:none'>"+data.tdish.imagetitle+"</span>"+
	    		 "<span id='"+typeflag+"speciality' style='display:none'>"+data.tdish.abbrdesc+"</span>"+
	    		 "<span id='"+typeflag+"unitflag' style='display:none'>"+data.tdish.headsort+"</span>"+
	    		 "￥<span id='"+typeflag+"normalprice'>"+data.tdish.price+"</span>"+
	    		 "<span id='"+typeflag+"couponsprice' style='display:none'>"+data.tdish.vipprice+"</span>"+
	    		 "会员价￥<span  id='"+typeflag+"discountprice'>"+data.tdish.vipprice+"</span>";
	    		 if(data.tdish.image!=""&&data.tdish.image!='undefined'&&data.tdish.image!=undefined){//文章图片为空
		    		 thistemplate.style.backgroundImage="url("+global_Path+"/"+imagepath+")";
		    		 thistemplate.style.backgroundRepeat="no-repeat";
	    		 }
	    		 saveDishUnit(data.tdish.dishid);
		    	$('#addDishDialog').dialog('close');
		    	dish_init_data();
		    },  
		    complete: function (XMLHttpRequest, textStatus) {  
		    } 
	    });
}
function save_object(){//保存板式
	var templateData=get_data();
	if(check_validate()){
    		$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/template/save",
				  data: { 
			    	    id:	$("#templatenameid").val(),
			    	    name: $("#templatename").val(),
			    	    createuserid:$("#userId").val(),
			    	    status:$("#status").val(),
			    	    type: $("#selectTemplate").combobox('getValue'),
			    	    dishtype: $("#templatedishtype").combobox('getValue'),
			    	    data: templateData,
			    	    articleids:articleids,
			    	    sort:$("#sort").val(),
			    	    oldarticleids:oldarticleids
			    	    
			    	   }, 
				dataType : "json",
				success : function(result) {
					$.messager.alert("提示",result.message);
					if(result.message=='添加成功'||result.message=='修改成功')
					init_data();
					$("#addObjectDialog").dialog('close');
					$('#tables').datagrid('reload');
				}
			});
	}
}
function get_data(){
	   var object={};
	   var arts=[];
	   var type='';
	   var getselect=$("#selectTemplate").combobox('getValue');
		   var  temp={};
		   for(var i=1;i<=14;i++){
			   var id=$('#'+getselect+i+'dishid').html();
			   if(id!=''&&id!='undefined'&&id!=undefined){
				   type=getselect+i;
//	    			dishid :dishname : image : normalprice : discountprice : couponsprice :cookietype : 
//	    			cusumers : unit :introduction :dishtype:itemtype:
				   temp={};
				   temp.dishid=validateEmpty($('#'+type+'dishid').html());
				   temp.dishname=validateEmpty($('#'+type+'dishname').html());
				   temp.image=validateEmpty($('#'+type+'image').html());
				   temp.normalprice=validateEmpty($('#'+type+'normalprice').html());
				   temp.discountprice=validateEmpty($('#'+type+'discountprice').html());
				   temp.couponsprice=validateEmpty($('#'+type+'couponsprice').html());
				   temp.cookietype=validateEmpty($('#'+type+'cookietype').html());
				   temp.cusumers=validateEmpty($('#'+type+'cusumers').html());
				   temp.unit=validateEmpty($('#'+type+'unit').html());
				   temp.introduction=validateEmpty($('#'+type+'introduction').html());
				   temp.dishtype=validateEmpty($('#'+type+'dishtype').html());
				   temp.itemtype=validateEmpty($('#'+type+'itemtype').html()); 
				   temp.specialflag=validateEmpty($('#'+type+'specialflag').html()); 
				   temp.speciality=validateEmpty($('#'+type+'speciality').html()); 
				   temp.unitflag=validateEmpty($('#'+type+'unitflag').html()); 
// 				   object[getselect+i]= json2str(temp);   
// 				   object[getselect+i]= JSON.stringify(temp);   
				   object[getselect+i]= temp;   
				   arts.push($('#'+type+'dishid').html());
			   }
		   }
     	articleids=arts.join(',');
	   return JSON.stringify(object);
	}
//增加对象认证
	function check_validate(){
	var flag=true;
	var templatename=$("#templatename").val();
	if($.trim(templatename)==""){
		$.messager.alert("提示","版式名称不能为空");
		flag=false;
		return flag;
	}
	var sort=$("#sort").val();
	if($.trim(sort)==""){
		$.messager.alert("提示","排序不能为空");
		flag=false;
		return flag;
	}
	var templatedishtype=$("#templatedishtype").combobox('getValue');
	if($.trim(templatedishtype)==""){
		$.messager.alert("提示","菜品分类不能为空");
		flag=false;
		return flag;
	}
// 	$.ajax({
// 		type : "post",
// 		async : false,
// 		url : global_Path+"/template/validateTemplate.json",
// 		dataType : "json",
// 		data:{articleids:articleids,tbTemplateid:$("#templatenameid").val()},
// 		success : function(result) {
// 			if(result.message=="fail"){
// 				alert(result.alertmessage);
// 				flag=false;
// 			}
// 		}
// 	});
	return flag;
} 
	function getlist(value){
		$('#tables').datagrid('load',{
			 "name":value
		});
    }
	function getDishListByName(value){
		$('#dishListTable').datagrid('load',{
			 "title":value
		});
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
<div data-options="region:'center',border:false" style="padding:10px 0px 0px 0px;">
 <table id="tables"></table>
	<div id="toolbar_t" class="info">
		<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
			<tr style="border-bottom: 0px solid #9A9691;">
				<td align="left" width="400" ><font style="font-size: 18px; color: #000; font-weight: 700;">菜谱管理</font></td>
			    <td align="right" width="80"><input type="button" onclick="open_dialog()" value="新增模板" class="xld_addbutton"></input></td>
			    <td align="center" width="80"><input id="searchauthor" class="easyui-searchbox" style="width:150px;"  data-options="searcher:getlist"  prompt="模板名称搜索" /></td>
			</tr>
		</table>
	</div>
	<div id="addObjectDialog">
		<div style="margin-top: 10px">
			<strong>选择版式:</strong> 
			<select id="selectTemplate"
				class="easyui-combobox" data-options="panelHeight:'auto'"
				name="dept" style="width: 150px;">
				<option value="A">A版式</option>
				<option value="B">B版式</option>
				<option value="C">C版式</option>
				<option value="D">D版式</option>
				<option value="E">E版式</option>
				<option value="F">F版式</option>
				<option value="G">G版式</option>
				<option value="H">H版式</option>
				<option value="I">I版式</option>
			</select> &nbsp; <strong>版式名称:</strong> <input type="text" id="templatename"
				name="templatename" /><span style="color: red">*</span> <input
				type="hidden" id="templatenameid" name="templatenameid" /> <input
				type="hidden" id="userId" name="userId" /> <input type="hidden"
				id="status" name="status" />
		</div>
		<div style="margin-top: 10px">
			<strong>排&nbsp;&nbsp;&nbsp;序:</strong> <input type="text" id="sort" 
				name="sort" /><span style="color: red">*</span> &nbsp; <strong>菜品分类:</strong>
			<input id="templatedishtype" name="templatedishtype" />
		</div>
		<div id="A" class="tempalteTopA">
			<div id="A1" template="template">A1</div>
		</div>

		<div id="B" class="tempalteTopB" style="display: none;">
			<div id="B1" template="template">B1</div>
			<div id="B2" template="template">B2</div>
		</div>

		<div id="C" class="tempalteTopC" style="display: none;">
			<div id="C1" template="template">C1</div>
			<div id="C2" template="template">C2</div>
			<div id="C3" template="template">C3</div>
		</div>
		<div id="D" class="tempalteTopD" style="display: none;">
			<div id="D1" template="template">D1</div>
			<div id="D2" template="template">D2</div>
			<div id="D3" template="template">D3</div>
		</div>
		<div id="E" class="tempalteTopE" style="display: none;">
			<div id="E1" template="template">E1</div>
			<div id="E2" template="template">E2</div>
			<div id="E3" template="template">E3</div>
			<div id="E4" template="template">E4</div>
		</div>
		<div id="F" class="tempalteTopF" style="display: none;">
			<div id="F1" template="template">F1</div>
			<div id="F2F3">
				<div id="F2" template="template">F2</div>
				<div id="F3" template="template">F3</div>
			</div>
			<div id="F4" template="template">F4</div>
		</div>
		<div id="G" class="tempalteTopG" style="display: none;">
			<div id="G1" template="template">G1</div>
			<div id="G2" template="template">G2</div>
			<div id="G3G4">
				<div id="G3" template="template">G3</div>
				<div id="G4" template="template">G4</div>
			</div>
		</div>
		<div id="H" class="tempalteTopH" style="display: none;">
			<div id="H1" template="template">H1</div>
			<div id="H2" template="template">H2</div>
			<div id="H3" template="template">H3</div>
			<div id="H4" template="template">H4</div>
			<div id="H5" template="template">H5</div>
			<div id="H6" template="template">H6</div>
			<div id="H7" template="template">H7</div>
			<div id="H8" template="template">H8</div>
		</div>
		<div id="I" class="tempalteTopI" style="display: none;">
			<div id="I1" template="template">I1</div>
			<div id="I2" template="template">I2</div>
			<div id="I3" template="template">I3</div>
			<div id="I4" template="template">I4</div>
			<div id="I5" template="template">I5</div>
			<div id="I6" template="template">I6</div>
			<div id="I7" template="template">I7</div>
			<div id="I8" template="template">I8</div>
			<div id="I9" template="template">I9</div>
			<div id="I10" template="template">I10</div>
			<div id="I11" template="template">I11</div>
			<div id="I12" template="template">I12</div>
			<div id="I13" template="template">I13</div>
			<div id="I14" template="template">I14</div>
		</div>
	</div>
	<div id="addDishDialog">
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
				<td><input id="columnid" name="columnid" /></td>
				<td>打印口:</td>
				<td><input id="printer" name="printer"/></td>
			</tr>
			<tr align="left" id="showdiff">
				<td>计量单位:</td>
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
<!-- 				<input  id="special" name="special"  /> -->
				<input class="easyui-combobox" id="special" name="special"
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '是 '},{label: '1',value: '否'}]" />
				</td>
			</tr>
			<tr align="left">
				<td>菜品简介:</td>
				<td colspan="3"><textarea rows="1" cols="1" id="introduction"
						style="width: 98%; height: 70px; font-size: 12px;"></textarea></td>
			</tr>
			<tr align="left">
				<td>图片:</td>
				<td colspan="5"><input type="file" id="file1" name="file1" />
				</td>
			</tr>
			<tr align="left">
				<td></td>
				<td colspan="6"><img id="file1Img" src="" alt=""
					style="visible: hidden" /> <input id="removepic" type="button"
					value="删除" onclick="removepic();" style="display: none;" /></td>
			</tr>
		</table>
		</div>
		<div  id="dishListDiv" style="width: 34%;height:100%; float: left;display: none;overflow-y: scroll;">
				<table id="dishList"></table> 
		</div>
	</div>
		<div id="dishListDialog">
       <table id="dishListTable"></table>
       <div id="toolbar_list" class="info">
		<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
			<tr style="border-bottom: 0px solid #9A9691;">
			    <td align="left" width="80"><input  class="easyui-searchbox" style="width:150px;"  data-options="searcher:getDishListByName"  prompt="菜品名称搜索" /></td>
			</tr>
		</table>
	</div>
      </div>
</div>
</body>
</html>