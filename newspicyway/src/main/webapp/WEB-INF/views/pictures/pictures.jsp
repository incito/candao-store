<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
 	
	<script type="text/javascript">
	var changePicture=false;
	var pictureStatus;
		$(function(){
				
			//话题表格配置
			$('#picturesShow').datagrid({
				url : global_Path + '/pictures/page.json',
				method : 'post',
				fit : false,
				fitColumns : true,
				singleSelect : true,
				pagination : true,
				rownumbers : true,
				pageSize : 10,	
				pageList : [ 10, 20, 30 ],
				remoteSort : false,
  				idField : 'a',
				loadMsg : '数据装载中......',
				columns : [ [ {
					field : 'orderNo',
					title : '图片编号',
					width : 50,
					align : 'left'
				},
				{
					field : 'picname',
					title : '图片名称',
					width : 50,
					align : 'center',
					
					
				},
				{
					field : 'picpath',					
					title : '图片路径',
					width : 50,
					align : 'center',
					formatter:function(value, row, index){
	
						return "<img src=<%=request.getContextPath()%>/"+value+" width='50px' height='70px'>";
					},
				},
				{
					field : 'detail',					
					title : '图片介绍',
					width : 50,
					align : 'center',
					
				}
				,
				{
					field : 'status',					
					title : '图片状态',
					width : 50,
					align : 'center',
					formatter:function(value, row, index){
						if(value==1){
							return "已启用";	
						}else if(value==2){
							return "已禁用";
						}
							
					
					},
				}
				,{
					field : 'picid',
					title : '操作',
					width : 60,
					align : 'center',
					formatter : function(value, row, index) {
						if(row.status==1){
							pictureStatus="禁用";
						}else {
							pictureStatus="启用";
						}
// 						alert(row.status);
						
						var modify ="<a href=\"###\" onclick=\"modify_pictures('" + value + "')\">查看/修改</a>";
						var changePicture = "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"###\" onclick=\"change_pictures('" + value + "')\">"+pictureStatus+"</a>";
						var del = "&nbsp;&nbsp;<a href=\"###\" onclick=\"del_pictures('" + value + "')\">删除</a>";
						
					    return modify+del+changePicture;
					}
				}
				
				] ],
				toolbar : '#toolbar_pictures'
			});
			//分页参数配置
			$('#picturesShow').datagrid('getPager').pagination({
				displayMsg : '当前显示从{from}到{to}共{total}记录',
				beforePageText : '第',
				afterPageText : '页 共 {pages} 页',
				onBeforeRefresh : function(pageNumber, pageSize) {
					$(this).pagination('loading');
					$(this).pagination('loaded');
				}
			});
			
			$('#addPicturesDialog').dialog({   
			    title: '屏保管理 (*号部分为必填项)',   
			    width: 500,   
			    height: 350,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存',
					handler : 
					function(){
						    $('#orderNo').validatebox({required: true,missingMessage:"屏保编号不能为空！"});

						    $("#orderNo").validatebox('enableValidation');

						    if ($('#orderNo').validatebox('isValid')){
// 						    	if($("#picid").combobox('getValue')!=""&&$("#custPrinter").combobox('getValue')!=""){
						    		save_pictures();
// 						    	}else{
// 						    		alert("所在区域和客用打印机不能为空");
// 						    	}
								
							}														
					}						
				}, {
					text : '取消',
					handler : function() {
						$('#addPicturesDialog').dialog('close');
					}
				}, {
					text : '重置',
					handler :function(){
						init_object();
					}
				}
				],
			onClose : function() {
				init_object();			
				}
			});	
			
		});
//-----------------------------------------------------------------------------------------------------------		
//-----------------------------------------------------------------------------------------------------------

function init_object(){


	$("#picname").val("");
	$("#orderNo").val("");
    $("#file1").val("");
	$("#detail").val("");
	$("#removepic").hide(); 
}
	function add_pictures(){
		$('#addPicturesDialog').dialog('open'); 
		$("#picid").val("");
		init_object();
	
		
	}
	function check_validate(){
		
 	  	var flag=true;
		$.ajax({
			type : "post",
			async : false,
			data:{
				orderNo:$("#orderNo").val(),
	    	    picid:$("#picid").val()
			},
			url : global_Path+"/pictures/validatePictures.json",
			dataType : "json",
			success : function(result) {
				
				if(result.message=='图片编号不能重复'){
					alert(result.message);
					flag=false;
					
				}
				}
			
		});
		return flag;
	} 


	var imagePath='';
	function save_pictures(){
		var check=check_validate();
		if(check){
		 var image;
		  if($("#file1").val()==''){
		    	image=imagePath;
		    }else{
		    	image="";
		    } 
		 
		  $.ajaxFileUpload({
			    fileElementId: ['file1'],  
			    url: global_Path+'/pictures/save',  
			    dataType: 'json',
			    contentType:'application/json;charset=UTF-8',
			    data: { 
			    	 picid : $("#picid").val(),
					 picname : $("#picname").val(),
					 orderNo : $("#orderNo").val(),
					 file1 : $("#file1").val(),
					 detail : $("#detail").val(),
					 status:$("#status").val(),
					 image:image
			    	   },  
			    beforeSend: function (XMLHttpRequest) {
			    	
			    },  
			    success: function (data, textStatus) { 
			    	
			        alert(data.message);
			        $('#picturesShow').datagrid('reload');
			        $('#addPicturesDialog').dialog('close');
		    		init_data(); 
			    },  
			    complete: function (XMLHttpRequest, textStatus) {  
			    } 
		    });
		}
	}
		//修改查询
		function modify_pictures(id){
		
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/pictures/findById/"+id+".json",
				dataType : "json",
				success : function(result) {
					
					
					if(changePicture){
						if(result.status==1){
							$("#status").val(2);
						}else{
							$("#status").val(1);
						}
					}
// 					alert($("#status").val());
					$("#picid").val(result.picid);
					$("#picname").val(result.picname);
					$("#orderNo").val(result.orderNo);				
				
// 					$("#file1").val(result.);
					$("#detail").val(result.detail);
					
					if(result.picpath!=""&&typeof(result.picpath) != "undefined"){
						$("#file1Img").attr("src",global_Path+'/'+result.picpath);
						$("#file1Img").attr("style","width:50px;height:70px");
						$("#removepic").show();
					};
					
					
					if(!changePicture){
						
						$('#addPicturesDialog').dialog('open');
// 						alert(changePicture);
					}else{
						save_pictures();
// 						alert(changePicture);
					}
					  
				}
			});
		}
		function del_pictures(picid){
			$.messager.confirm("屏保管理","确认删除此数据吗？",function(r){
				if (r){
					$.ajax({
						type : "post",
						async : false,
						url : global_Path+"/pictures/delete/"+picid+".json",
						dataType : "json",
						success : function(result) {							
							$('#picturesShow').datagrid('reload');
							$.messager.alert('屏保管理',result,'info',null);
						}
					});
				}
			});
		}
		
		//改变图片启用、禁用状态
		function change_pictures(id){
			 changePicture = true;
			 modify_pictures(id);
			 changePicture =false;
		}
		function removepic(){
			$("#removepic").hide();
			$("#file1Img").hide();
			imagePath='';
		}
	</script>
</head>
<body class="easyui-layout" data-options="fit:true" style="overflow-y:auto" >
	<div data-options="picid:'center',border:false" style="padding: 10px 0px 0px 0px;">
		<div id="toolbar_datadictionary" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr>
					<td align="left" width="400"><font style="font-size: 18px; color: #000; font-weight: 700;">屏保管理</font></td>
				    <td width="100px"><input type="button" style="width: 90px" onclick="add_pictures()" value="新增屏保图片" class="xld_addbutton"></input></td>
				</tr>
			</table>
		</div>
		<table id="picturesShow"></table>
		
		<div id="toolbar_pictures" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				

			</table>
		</div>
	 	<div id="addPicturesDialog">
			<input type="hidden" id="picid" name="picid" />
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;">
				<tr align="left">
					<td>图片编号:</td>
					<td><input type="text" id="orderNo" name="orderNo" /><span style="color: red">*</span></td>
				</tr>
				
				<tr align="left">
					<td>图片名称:</td>
					<td><input type="text" id="picname" name="picname" /></td>        
				</tr>
				<tr align="left" style="display:none">
					<td>图片状态:</td>
					<td><input type="text" id="status" name="status" /></td>        
				</tr>
				<tr align="left">
					<td>图片加载:</td>
					<td colspan="5"><input type="file"  id="file1" name="file1"/>
           			</td>
				</tr>
					<tr align="left">
					<td></td>
					<td colspan="6">
            		<img id="file1Img" src="" alt="" style="visible:hidden"/>
           			<input id="removepic" type="button" value="隐藏" onclick="removepic();" style="display:none;"/>
           			</td>
				</tr>
				<tr align="left">
					<td>图片简介:</td>
					<td colspan="3"><textarea rows="1" cols="1" id="detail" style="width: 98%;height: 70px;font-size: 12px;"></textarea></td>
				</tr>
				
			</table>
		</div> 
	</div>

	
</body>

</html>
