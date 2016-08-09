<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/resource.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/datagrid-groupview.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/editcell.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
<script type="text/javascript">
		$(function(){	
			$('#dishShow').datagrid({    
				url : global_Path+"/coupons/page.json",
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
  				idField : 'couponid',
				loadMsg : '数据装载中......',
				columns : [ [{
					field : 'couponname',
					title : '名称',
					width : 10,
					align : 'center',
				},{
					field : 'couponparent',					
					title : '分类',
					width : 10,
					align : 'center',
					formatter:function(value, row, index){  
						switch(value){
							case "1":
								return "菜品优惠";
							case "2":
								return "订单优惠";
							case "3":
								return "储值优惠";
							case "4":
								return "团购优惠";
							case "5":
								return "信用卡优惠";
							case "6":
								return "更多优惠";
						}
					}
				},{
					field : 'coupontype',					
					title : '优惠时段类型',
					width : 10,
					align : 'center',
					formatter:function(value, row, index){  
						switch(value){
// 						0 每天 1 每周2 每月
							case "0":
								return "每天";
							case "1":
								return "每周";
							case "2":
								return "每月";
						}
					}
				},{
					field : 'couponperiod',					
					title : '优惠时段间隔',
					width : 10,
					align : 'center',
				},{
					field : 'begintime',					
					title : '开始时间 ',
					width : 10,
					align : 'center',
					formatter :function(value,row,index){
						if(value!='')
						return value.substring(0,10);
					}
				},{
					field : 'endtime',					
					title : '结束时间 ',
					width : 10,
					align : 'center',
					formatter :function(value,row,index){
						if(value!='')
						return value.substring(0,10);
					}
				},{
					field : 'couponid',
					title : '操作',
					width : 10,
					align : 'center',
					formatter : function(value, row, index) {
						var modify ="<a href=\"###\" onclick=\"modify_coupon('" + value + "')\">查看/修改</a>";
						var del = "&nbsp;&nbsp;<a href=\"###\" onclick=\"del_coupon('" + value + "')\">删除</a>";
					    return modify+del;
					}
				}
				] ],
				onBeforeLoad:function(){
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
			
			
			$('#addCouponManageDialog').dialog({   
			    title: '优惠管理(*号部分为必填项)',   
			    width:350,   
			    height: 350,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [{
					text : '关闭',
					handler : function() {
						$('#addCouponManageDialog').dialog('close');
					}
				}]
			});	
			
			$('#addCouponDialog1').dialog({   
			    title: '优惠管理-菜品优惠(*号部分为必填项)',   
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
						    $('#couponsName1').validatebox({required: true,validType:'maxLength[1,50]',missingMessage:"优惠名称不能为空！"});					
			                $('#begintime1').validatebox({required: true,missingMessage:"有效期不能为空！"});
						    $('#endtime1').validatebox({required: true,validType:'comparedata["#begintime2"]',missingMessage:"有效期不能为空！"}); 
						    $('#description1').validatebox({required: true,validType:'maxLength[1,200]',missingMessage:"活动简介不能为空！"});
						    $('#ruledescription1').validatebox({required: true,validType:'maxLength[1,200]',missingMessage:"活动规则简介不能为空！"});						    
						    $("#couponsName1").validatebox('enableValidation');						
						    $("#begintime1").validatebox('enableValidation'); 
						    $("#endtime1").validatebox('enableValidation');
						    $("#description1").validatebox('enableValidation');
						    $("#ruledescription1").validatebox('enableValidation');						    
						    if ($('#couponsName1').validatebox('isValid') &&  $('#begintime1').validatebox('isValid') && $('#endtime1').validatebox('isValid') && $('#description1').validatebox('isValid') && $('#ruledescription1').validatebox('isValid')){
						        if(editLists.length!=0){
						        	save();
						        }
						    	save_coupons_order("1");						    
							}						
					}						
				}, {
					text : '取消',
					handler : function() {				
						init_data("1");
						$('#addCouponDialog1').dialog('close');
					}
				}
				],
				onClose : function() {
					init_data("1");
				}
			});
			
			$('#addCouponDialog2').dialog({
			    title: '优惠管理-整单优惠(*号部分为必填项)',   
			    width:750,   
			    height: 550,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [{
					text : '保存',
					handler : 
					function(){
						    $('#couponsName2').validatebox({required: true,validType:'maxLength[1,50]',missingMessage:"优惠名称不能为空！"});
						    $('#orderMoney2').validatebox({required: true,validType:'intOrFloat',missingMessage:"订单金额不能为空！"});
						    $('#reduceMoney2').validatebox({required: true,validType:'intOrFloat',missingMessage:"减扣金额不能为空！"});
			                $('#begintime2').validatebox({required: true,missingMessage:"有效期不能为空！"});
						    $('#endtime2').validatebox({required: true,validType:'comparedata["#begintime2"]',missingMessage:"有效期不能为空！"}); 
						    $('#description2').validatebox({required: true,validType:'maxLength[1,200]',missingMessage:"活动简介不能为空！"});
						    $("#couponsName2").validatebox('enableValidation');
						    $("#orderMoney2").validatebox('enableValidation');
					        $("#reduceMoney2").validatebox('enableValidation');
						    $("#begintime2").validatebox('enableValidation'); 
						    $("#endtime2").validatebox('enableValidation');
						    $("#description2").validatebox('enableValidation');
						    if ($('#couponsName2').validatebox('isValid') && $('#orderMoney2').validatebox('isValid') && $('#reduceMoney2').validatebox('isValid') &&  $('#begintime2').validatebox('isValid') && $('#endtime2').validatebox('isValid') && $('#description2').validatebox('isValid')){
						    	save_coupons_order("2");
						    
							}
					}						
				},{
					text : '关闭',
					handler : function() {
						init_data("2");
						$('#addCouponDialog2').dialog('close');
					}
				}],
				onClose : function() {
					init_data("2");
				}
			});	
			
			$('#addCouponDialog3').dialog({
			    title: '优惠管理-储值优惠(*号部分为必填项)',   
			    width:750,   
			    height: 550,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [{
					text : '关闭',
					handler : function() {						
						$('#addCouponDialog3').dialog('close');
					}
				}],
				onClose : function() {
					init_data("3");
				}
			});	
			
			$('#addCouponDialog4').dialog({   
			    title: '优惠管理-团购优惠(*号部分为必填项)',   
			    width:750,   
			    height: 550,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [{
					text : '保存',
					handler : 
					function(){
						    $('#couponsName4').validatebox({required: true,validType:'maxLength[1,50]',missingMessage:"优惠名称不能为空！"});
						    $('#couponcash4').validatebox({required: true,validType:'intOrFloat',missingMessage:"代金券金额不能为空！"});
						    $('#freeamount4').validatebox({required: true,validType:'intOrFloat',missingMessage:"可抵用金额不能为空！"});
						    $('#couponnum4').validatebox({required: true,validType:'number',missingMessage:"可抵用金额不能为空！"});
			                $('#begintime4').validatebox({required: true,missingMessage:"有效期不能为空！"});
						    $('#endtime4').validatebox({required: true,validType:'comparedata["#begintime4"]',missingMessage:"有效期不能为空！"}); 
						    $('#description4').validatebox({required: true,validType:'maxLength[1,200]',missingMessage:"活动简介不能为空！"});
						    $("#couponsName4").validatebox('enableValidation');
						    $("#couponcash4").validatebox('enableValidation');
					        $("#freeamount4").validatebox('enableValidation');
					        $("#couponnum4").validatebox('enableValidation');
						    $("#begintime4").validatebox('enableValidation'); 
						    $("#endtime4").validatebox('enableValidation');
						    $("#description4").validatebox('enableValidation');
						    if ($('#couponsName4').validatebox('isValid') && $('#couponcash4').validatebox('isValid') && $('#freeamount4').validatebox('isValid') &&$('#couponnum4').validatebox('isValid') &&  $('#begintime4').validatebox('isValid') && $('#endtime4').validatebox('isValid') && $('#description4').validatebox('isValid')){
						    	save_coupons_order("4");
						    
							}
					}						
				}, {
					text : '取消',
					handler : function() {			
						init_data("4");
						$("#addCouponDialog4").dialog('close');
					}
				}],
				onClose : function() {
					init_data("4");
				}
			});
			
			$('#addCouponDialog5').dialog({   
			    title: '优惠管理-信用卡优惠(*号部分为必填项)',   
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
						    $('#couponsName5').validatebox({required: true,validType:'maxLength[1,50]',missingMessage:"优惠名称不能为空！"});
						    $('#totalamount5').validatebox({required: true,validType:'intOrFloat',missingMessage:"单笔金额不能为空！"});
						    $('#couponnum5').validatebox({required: true,validType:'intOrFloat',missingMessage:"打折金额不能为空！"});
					//	    $('#couponnum5').validatebox({required: true,validType:'intOrFloat',missingMessage:"可抵用金额不能为空！"});
			                $('#begintime5').validatebox({required: true,missingMessage:"有效期不能为空！"});
						    $('#endtime5').validatebox({required: true,validType:'comparedata["#begintime5"]',missingMessage:"有效期不能为空！"}); 
						    $('#description5').validatebox({required: true,validType:'maxLength[1,200]',missingMessage:"活动简介不能为空！"});
						    $("#couponsName5").validatebox('enableValidation');
						    $("#totalamount5").validatebox('enableValidation');
					        $("#couponnum5").validatebox('enableValidation');
					//        $("#couponnum4").validatebox('enableValidation');
						    $("#begintime5").validatebox('enableValidation'); 
						    $("#endtime5").validatebox('enableValidation');
						    $("#description5").validatebox('enableValidation');
						    if ($('#couponsName5').validatebox('isValid') && $('#totalamount5').validatebox('isValid') && $('#couponnum5').validatebox('isValid') &&  $('#begintime5').validatebox('isValid') && $('#endtime5').validatebox('isValid') && $('#description5').validatebox('isValid')){
						    	save_coupons_order("5");
						    
							}
					}						
				}, {
					text : '取消',
					handler : function() {
						init_data("5");
						$('#addCouponDialog5').dialog('close');
					}
				}
				],
				onClose : function() {
					init_data("5");
				}
			});
			
			$('#addCouponDialog6').dialog({   
			    title: '优惠管理-更多优惠(*号部分为必填项)',   
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
						$('#couponsName6').validatebox({required: true,validType:'maxLength[1,50]',missingMessage:"优惠名称不能为空！"});
					    $('#couponrate6').validatebox({required: true,validType:'intOrFloat',missingMessage:"打折金额不能为空！"});
// 					    $('#partnername6').validatebox({required: true,alidType:'maxLength[1,50]',missingMessage:"合作单位不能为空！"});
		                $('#begintime6').validatebox({required: true,missingMessage:"有效期不能为空！"});
					    $('#endtime6').validatebox({required: true,validType:'comparedata["#begintime6"]',missingMessage:"有效期不能为空！"}); 
					    $('#description6').validatebox({required: true,validType:'maxLength[1,200]',missingMessage:"活动简介不能为空！"});
					    $("#couponsName6").validatebox('enableValidation');
					    $("#couponrate6").validatebox('enableValidation');				      
// 				        $("#partnername6").validatebox('enableValidation');
					    $("#begintime6").validatebox('enableValidation'); 
					    $("#endtime6").validatebox('enableValidation');
					    $("#description6").validatebox('enableValidation');
					    if ($('#couponsName6').validatebox('isValid') && $('#couponrate6').validatebox('isValid') && $('#begintime6').validatebox('isValid') && $('#endtime6').validatebox('isValid') && $('#description6').validatebox('isValid')){
					        if(editLists.length!=0){
					        	saveSecond();
					        }
					    	save_coupons_order("6");					    	
						}
					}						
				}, {
					text : '取消',
					handler : function() {
						init_data("6");
						$('#addCouponDialog6').dialog('close');
					}
				}],
				onClose : function() {
					init_data("6");					
				}
			});
			//合作单位
			$('#partnername6').combobox({
			    url:global_Path + '/parterner/findlist.json?type=94',
			    valueField:'parternerId',
			    textField:'name',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
			//团购网站
			$('#groupWeb4').combobox({
			    url:global_Path + '/parterner/findlist.json?type=90',
			    valueField:'parternerId',
			    textField:'name',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
			//发卡银行
			$('#banktype5').combobox({
			    url:global_Path + '/parterner/findlist.json?type=91',
			    valueField:'parternerId',
			    textField:'name',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
			//合作单位类型
			$('#cooperateType').combobox({
			    url:global_Path + '/datadictionary/getDatasByType/COUPONSCATEGORY.json',
			    valueField:'id',
			    textField:'itemDesc',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				},
				onSelect : function(row){
					 $("#partnername6").combobox('setValue','');
					 $('#partnername6').combobox("options").url = global_Path + '/parterner/findlist.json?type='+row.id;
			    	 $('#partnername6').combobox("reload");
				}
			});
			
			$('#dishDetial').dialog({   
			    title: '选择菜品',   
			    width: 750,   
			    height: 480,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '确认',
					handler : 
					function(){
						save();
					}						
				}, {
					text : '取消',
					handler : function() {
						$('#dishDetial').dialog('close');
					}
				}
				],
			onClose : function() {
			}
			});
			
			$('#dishDetialSecond').dialog({   
			    title: '选择菜品',   
			    width: 750,   
			    height: 480,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '确认',
					handler : 
					function(){
						saveSecond();
					}						
				}, {
					text : '取消',
					handler : function() {
						$('#dishDetialSecond').dialog('close');
					}
				}
				],
			onClose : function() {
			}
			});	
		
			
});
//------------------------------------------------------------------------------------------------------------		
//-----------------------------------------------------------------------------------------------------------
 
        function show_main_dialog(){
    		$('#addCouponManageDialog').dialog('open');  		
}
 
        function getlist(value){
			$('#dishShow').datagrid('load',{
				"couponparent": $(".dishTypeSelected").attr("id"),
				 "couponname":value
			});
	    }
		function gethiddenId(id){
			$("#dishTypeTr td").attr("class","dishTypeUnSelected");
			$("#"+id).attr("class","dishTypeSelected");
			$('#dishShow').datagrid('load',{
				"couponparent": id,
				 "couponname":$('#dishNameSearch').searchbox('getValue')
			});
		}
		
/* 		function gethiddenIds(id){
			$("#dishTypeTrs td").attr("class","dishTypeUnSelected");
			$("#"+id).attr("class","dishTypeSelected");
			
		} */
	//结束编辑
	function endEdit() {
		var rows = $('#dishList').datagrid('getRows');
		for (var i = 0; i < rows.length; i++) {
			$('#dishList').datagrid('endEdit', i);
		}
	}
	var editList=[];
	var editLists=[];
	
	function save() {
		endEdit();
		editList=[];
		var row=$('#dishList').datagrid("getData");
		for(var i=0;i<row.rows.length;i++){
			if((row.rows[i].specialprice!="" && typeof(row.rows[i].specialprice) != "undefined") || (row.rows[i].num!="" && typeof(row.rows[i].num) != "undefined" )|| (row.rows[i].discount!="" && typeof(row.rows[i].discount) != "undefined")){
				editList.push(row.rows[i]);
			}
 
		}
 
		//获取到所有改变的数据
//		  editList = $('#dishList').datagrid("getChanges", "updated");
		$('#dishDetial').dialog("close");
	}
	//显示菜品
	// 		function showdishlist(){
	function shast(){
		$('#dishList').datagrid({
			url : global_Path + "/dish/getDishList.json",
			singleSelect : false,
			collapsible : true,
			rownumbers : true,
			fitColumns : true,
			method : 'post',
			fit : false,
			view : groupview,
			remoteSort : false,
			loadMsg : '数据装载中......',
			groupField : 'fitemDesc',
			onClickCell : onClickCell,
			idField : 'dishid',
			groupFormatter : function(value, rows) {
				return value + '(' + rows.length + ')';
			},
			columns : [ [{
				field : 'dishno',
				title : '编号',
				width : 20,
				align : 'left',
			},{
				field : 'title',
				title : '名称',
				width : 20,
				align : 'left',
			}, {
				field : 'specialprice',
				title : '特价',
				width : 20,
				align : 'left',
				editor : {
					type : 'numberbox',
					options : {
						precision : 1
					}
				}
			}, {
				field : 'num',
				title : '满足数量',
				width : 20,
				align : 'left',
				editor : {
					type : 'numberbox'
				}
			}, {
				field : 'discount',
				title : '折扣',
				width : 20,
				align : 'left',
				editor : {
					type : 'numberbox',
					options : {
						precision : 1
					}
				}
			}, ] ],
			onLoadSuccess : function(data) {
				 $('#dishList').datagrid('collapseGroup');
				if(editLists!='' && typeof(editLists) != "undefined"){
				$.each(editLists,function(editindex,editItem){
					$.each(data.rows, function(index, item){
	     			 if(item.dishid==editItem.dishid){
	     			    if(editItem.couponamount!="" && editItem.couponamount !=null){
	     			    	$('#dishList').datagrid('endEdit', index);
		                    $('#dishList').datagrid('updateRow', { index: index, row: { specialprice: editItem.couponamount} });		                   
	     			    }else{
		                       $('#dishList').datagrid('endEdit', index);
		                       $('#dishList').datagrid('updateRow', { index: index, row: {num: editItem.dishnum,discount: editItem.couponrate} }); 				
	     			    }
	     			 }	           	     			 
					});
				});
				} 				
				if(editList!='' && typeof(editList) != "undefined"){
					$.each(editList,function(editindex,editItem){
						$.each(data.rows, function(index, item){
		     			 if(item.dishid==editItem.dishid){	     			   		     			   		     		
		     			    if(editItem.specialprice!="" && editItem.specialprice !=null){
		     			    	$('#dishList').datagrid('endEdit', index);
			                    $('#dishList').datagrid('updateRow', { index: index, row: { specialprice: editItem.specialprice} });		                   
		     			    }else{
			                     $('#dishList').datagrid('endEdit', index);
			                     $('#dishList').datagrid('updateRow', { index: index, row: {num: editItem.num,discount: editItem.discount} }); 				
		     			    }
		     		 }
						});
					});
					} 
				
			},
		});
	}
		
	function add_datadictionary() {
		shast();
		$('#dishDetial').dialog('open');
	}
	
	function addCheckBox(str,strs){
		var html="";
		$("#"+str).html("");
		var coupontype = $("#"+strs).val();
		if(coupontype=="1"){
			for(var i=1;i<8;i++){
				html += "<input type='checkbox' id='checkedBox"+i+"' name='checkedBox' value='"+i+"' />"+i+"&nbsp;&nbsp;" ;
			}
			$("#"+str).html(html);
		}else if(coupontype=="2"){
			for(var i=1;i<32;i++){
				if(i%17==0){				
					 html += "<input type='checkbox' id='checkedBox"+i+"' name='checkedBox' value='"+i+"' />"+i+"&nbsp;<br/>" ;	
				 } else if(i<10){						 
					 html += "<input type='checkbox' id='checkedBox"+i+"' name='checkedBox' value='"+i+"' />"+i+"&nbsp;&nbsp;" ;	
				}else{
				html += "<input type='checkbox' id='checkedBox"+i+"' name='checkedBox' value='"+i+"' />"+i+"&nbsp;" ;
				}				
			}
			$("#"+str).html(html);
		}else{
			$("#"+str).html("");
		}
	}
				
		
		function couponwayChange(str,strs){
			if($("#"+str).val()=="0"){
				$("#"+strs).val("0");
			}else{
				$("#"+strs).val("1");
			}
		}
		
		function init_data(i){
			$("#couponsName"+i).val("");
			$("#showpositionPad"+i).attr("checked","checked");
			$("#coupontype"+i).val("0");
			$("#selectBox"+i).html("");
			$("#begintime"+i).val("");
			$("#endtime"+i).val("");
			$("#couponid").val("");
			$("#ruleid").val("");
			$("#couponchild").val("");
			$("#couponparent").val("");
			$("#description"+i).val("");
			if(i=="1"){
				 $.each(editList,function(index,item){
					 if(item.specialprice!="" && item.specialprice !=null){
						 $('#dishList').datagrid('endEdit', index);
			     		 $('#dishList').datagrid('updateRow', { index: index, row: { specialprice:""} });
					 }else{
						 $('#dishList').datagrid('endEdit', index);
			     		 $('#dishList').datagrid('updateRow', { index: index, row: { num:"",discount:""} });
					 }					
				 });
				 $.each(editLists,function(index,item){
					 if(item.specialprice!="" && item.specialprice !=null){
					 $('#dishList').datagrid('endEdit', index);
		     		 $('#dishList').datagrid('updateRow', { index: index, row: { specialprice:""} });
				 }else{
					 $('#dishList').datagrid('endEdit', index);
		     		 $('#dishList').datagrid('updateRow', { index: index, row: { num:"",discount:""} });
				 }	
				 });
				editList=[];
				editLists=[];
				$("#ruledescription"+i).val("");
			}else if(i=="2"){
				$("#couponcustomer"+i).attr("checked","checked");
				$("#orderMoney"+i).val("");
				$("#reduceMoney"+i).val("");
			}else if(i=="4"){
				$("#groupWeb"+i).combobox('setValue','');	
		        $("#couponcash"+i).val("");
		        $("#freeamount"+i).val("");
		        $("#couponnum"+i).val("");	 
			}else if(i=="5"){
				$("#totalamount"+i).val("");
	        	$("#banktype"+i).combobox('setValue','');	
	        	$("#couponway"+i).val("");
	        	$("#couponnum"+i).val("");
			}else{
				 $.each(editList,function(index,item){
					 $('#dishListSecond').datagrid('endEdit', index);
		     		 $('#dishListSecond').datagrid('updateRow', { index: index, row: { dismoneys:"",discounts:""} });
				 });
				 $.each(editLists,function(index,item){
					 $('#dishListSecond').datagrid('endEdit', index);
		     		 $('#dishListSecond').datagrid('updateRow', { index: index, row: { dismoneys:"",discounts:""} });
				 });
				editList=[];
				editLists=[];
//				$('#dishListSecond').datagrid('loadData',{total:0,rows:[]});               		            
				$("#coupontypeChild"+i).val("13"); 
	        	$("#orderMoney"+i).val("");
	        	$("#partnername"+i).combobox('setValue','');
	        	$("#cooperateType").combobox('setValue','');
	        	$("#totalamount"+i).val("");
	        	$("#wholesingle"+i).val("0");
	        	$("#couponrate"+i).val("");
	        	$("#debitamount"+i).val("");
	        	$("#freeamount"+i).val("");	        	
			}
		}

		function getOrderJson(i){
			var strs="";
			   var str="";
	           $("input[name='checkedBox']:checked").each(function(){ 
	        	   strs += $(this).val()+",";           
	           });
	           if(strs.length!=0){
	        	 str = strs.substring(0, strs.length-1);
	           }          	           
			var dataOrderObject={};
			var tCoupons={};
			var TCouponRule={};
			var list=[];
			var listpac=[];
			tCoupons.couponname=$("#couponsName"+i).val();
			tCoupons.couponparent=i;
			tCoupons.couponid=$("#couponid").val();
			TCouponRule.ruleid=$("#ruleid").val();
			tCoupons.showposition=$("input[name='showposition"+i+"']:checked").val();
			tCoupons.coupontype=$("#coupontype"+i).val();
			tCoupons.couponperiod=str;			
			tCoupons.description=$("#description"+i).val();
			tCoupons.begintime=$("#begintime"+i).val();
			tCoupons.endtime=$("#endtime"+i).val();			
	        if(i=="1"){
	        tCoupons.ruledescription= $("#ruledescription"+i).val();
	           $.each(editList,function(index,item){
	           if(item.specialprice!=''&&typeof(item.specialprice) != "undefined"){
	        	   var tCouponRule={};
		        	tCouponRule.dishid=item.dishid;
		        	tCouponRule.couponamount=item.specialprice;
		        	tCouponRule.couponway=1;
		        	list.push(tCouponRule);
	           }
	           if(item.discount!=''&&typeof(item.discount) != "undefined"&&item.num!=''&&typeof(item.num) != "undefined"){
	        	   var tCouponRule={};
		        	tCouponRule.dishid=item.dishid;
		        	tCouponRule.dishnum=item.num;
		        	tCouponRule.couponrate=item.discount;
		        	tCouponRule.couponway=0;
		        	list.push(tCouponRule);
	           }
	        });
	        }else if(i=="2"){	        	
	        tCoupons.couponcustomer=$("input[name='couponcustomer"+i+"']:checked").val();
	        TCouponRule.totalamount=$("#orderMoney"+i).val();
	        TCouponRule.couponamount=$("#reduceMoney"+i).val();
	        }else if(i=="4"){
	        TCouponRule.groupweb=$("#groupWeb"+i).combobox('getValue');
	        TCouponRule.couponcash=$("#couponcash"+i).val();
	        TCouponRule.freeamount=$("#freeamount"+i).val();
	        TCouponRule.couponnum=$("#couponnum"+i).val();	 				
	        }else if(i=="5"){
	        	TCouponRule.totalamount=$("#totalamount"+i).val();
	        	TCouponRule.banktype=$("#banktype"+i).combobox('getValue');
	        	TCouponRule.couponway=$("#couponway"+i).val();
	        	TCouponRule.couponrate=$("#couponnum"+i).val();
	        }else{	        	 
	        	tCoupons.couponchild=$("#coupontypeChild"+i).val();
	        	tCoupons.wholesingle=$("#wholesingle"+i).val();
	        	if($("#coupontypeChild"+i).val()=="14"){
	        		TCouponRule.partnername=$("#partnername"+i).combobox('getValue');
		        	TCouponRule.debitamount= $("#debitamount6").val();
		        	TCouponRule.freeamount= $("#freeamount6").val();
	        	}	        
	        	TCouponRule.couponrate=$("#couponrate"+i).val();
	        	if($("#wholesingle"+i).val()=="1"){	        			      
	        		  $.each(editList,function(index,item){
	        			  var tParternerCoupon={};
	        			  tParternerCoupon.dishid=item.dishid;
	       	           if(item.dismoneys!='' && typeof(item.dismoneys) != "undefined"){	       				   	       				    
	       				  tParternerCoupon.discountamount=item.dismoneys;	       				    
	       	           }
	       	           if(item.discounts!='' && typeof(item.discounts) != "undefined"){
	       		          tParternerCoupon.discountrate=item.discounts;
	       	           }
	       	        tParternerCoupon.parternerid=$("#partnername"+i).combobox('getValue');
	       	        listpac.push(tParternerCoupon);
	       	        });
	        	}
	        	
	        }
	        if(i!="1"){
	        	 list.push(TCouponRule);
	        }	        
			dataOrderObject.tCoupons=tCoupons;
			dataOrderObject.list=list;
			dataOrderObject.listpac=listpac;
			return $.toJSON(dataOrderObject);			
		}
		
//新增优惠
	 function save_coupons_order(i){
		 var json=getOrderJson(i);
		 $.ajax({
			 type:"post",
			 async:false,
			 url:global_Path+"/coupons/save",
			 contentType:'application/json;charset=UTF-8',
			 data:json,
			 dataType:"json",
			 success:function(data){
				    $('#dishShow').datagrid('reload');
					$.messager.alert('优惠管理',data.maessge,'info',null);				   					
		    		$("#addCouponDialog"+i).dialog('close');
		    		init_data(i);		    		
			 }
		 });
	 }
	 

     
     //根据id查询
     function modify_coupon(id){    	 
    	 $.ajax({
    		 type:"get",
    		 async:false,
    		 cache:false,
    		 url:global_Path+"/coupons/findById/"+id+".json",
    	     dataType:"json",
    	     contentType:"application/json;charset=UTF-8",
    	     success:function(data){
    	    var str = data.tCoupons.couponparent;
    	   $("#couponsName"+str).val(data.tCoupons.couponname);
    	   if(data.tCoupons.showposition=="0"){
    		   $("#showpositionPad"+str).attr("checked","checked");
    	   }else{ 
    		   $("#showpositionPos"+str).attr("checked","checked");
    	   }    	   
    	    $("#coupontype"+str).val(data.tCoupons.coupontype);
    	    if(data.tCoupons.coupontype=="0"){
    	    	$("#selectBox"+str).html("");
    	    }else{
    	    	addCheckBox("selectBox"+str,"coupontype"+str);
    	    	var per = data.tCoupons.couponperiod;
    	    	var pers = new Array();
    	    	pers = per.split(",");
    	    	for(var i=0;i<pers.length;i++){
    	    		$("#checkedBox"+pers[i]).attr("checked","checked");   	    			
    	    	}
    	    }			
			$("#begintime"+str).val(data.tCoupons.begintime);
			$("#endtime"+str).val(data.tCoupons.endtime);
			$("#couponid").val(data.tCoupons.couponid);		
			$("#couponchild").val(data.tCoupons.couponchild);
			$("#couponparent").val(data.tCoupons.couponparent);
			$("#description"+str).val(data.tCoupons.description);			
			if(str=="1"){
				$("#ruledescription"+str).val(data.tCoupons.ruledescription);
				editLists=data.list;
        		shast();		        
		        }else if(str=="2"){
		         if(data.tCoupons.couponcustomer=="0"){
		        	 $("#couponcustomerp"+str).attr("checked","checked");
		         }else{
		        	 $("#couponcustomerps"+str).attr("checked","checked");
		         }		 	
	    	    $.each(data.list,function(index,item){
	    	    	 $("#ruleid").val(item.ruleid);	    	    
	    	    	 $("#orderMoney"+str).val(item.totalamount);
	 		         $("#reduceMoney"+str).val(item.couponamount);
	    	    });		       
		        }else if(str=="4"){	
		        	    $.each(data.list,function(index,item){
		        	    $("#ruleid").val(item.ruleid);	  
		        	    $("#groupWeb"+str).combobox('setValue',item.groupweb);
		  		        $("#couponcash"+str).val(item.couponcash);
		  		        $("#freeamount"+str).val(item.freeamount);
		  		        $("#couponnum"+str).val(item.couponnum);	
		        	  
		        });		        				
		        }else if(str=="5"){
		        	 $.each(data.list,function(index,item){
		        		 $("#ruleid").val(item.ruleid);	  
		        		 $("#totalamount"+str).val(item.totalamount);
				         $("#banktype"+str).combobox('setValue',item.banktype);
				         $("#couponway"+str).val(item.couponway);
				         $("#couponnum"+str).val(item.couponrate);
		        	 });
		        	
		        }else{	        	 
		        	$("#coupontypeChild"+str).val(data.tCoupons.couponchild);
		        	$("#wholesingle"+str).val(data.tCoupons.wholesingle);
		        	 $.each(data.list,function(index,item){
		        		      coupontypeChild();
		        		      wholeSingle();
				        	if(data.tCoupons.couponchild=="14"){				        					        	
				        		$("#partnername"+str).combobox('setValue',item.partnername);
				        		$("#debitamount"+str).val(item.debitamount);
				        		$("#freeamount"+str).val(item.freeamount);
					         
				        	}	        
				        	$("#ruleid").val(item.ruleid);	  
				        	$("#couponrate"+str).val(item.couponrate);
		        	 });
		        	 if(data.tCoupons.wholesingle=="1"){		        		 
		        		 editLists=data.listpac;
		        		 shas();		        		 
		        		 
		        	 }
		        }			
			    $("#addCouponDialog"+str).dialog('open');
    	     }    	    	 
    		 });
     }
     
     function del_coupon(id){
        $.messager.confirm("优惠管理","确认删除此数据吗？",function(r){
		 if (r){
    	 $.ajax({
    		 type:"post",
    		 async:false,
    		 url:global_Path+"/coupons/delete/"+id+".json",
    	     dataType:"json",
    	     contentType:"application/json;charset=UTF-8",
    	     success:function(data){
    	    	$('#dishShow').datagrid('reload');
				$.messager.alert('优惠管理',data,'info',null);
    	     }
    		 });
    	 }
        });
     }
     
     function get_parterner(){
    	 $.ajax({
    		 type:"post",
    		 async:false,
    		 url:global_Path+"/coupons/getparterner.json",
    	     dataType:"json",
    	     contentType:"application/json;charset=UTF-8",
    	     success:function(data){
                     
    	     }
    		 });
    	 
     }
     
     function coupontypeChild(){
    	if( $("#coupontypeChild6").val()=="13" || $("#coupontypeChild6").val()==""){
    		$("#partnernameflag").hide();
    		$("#partnernameflags").hide();
    		$("#selectOrderCoupon6").hide();
    		$("#brid").hide();
    		$("#fontc").hide();
    		$("#fontcs").hide();
      		$("#debitfont6").hide();
    		$("#freefont6").hide();
    		$("#debitamount6").hide();
    		$("#freeamount6").hide();
    		$("#orderfont6").hide();
    		$("#wholesingle6").hide();
    	}else{
    		$("#partnernameflag").show();
    		$("#partnernameflags").show();
    		$("#brid").show();
    		$("#debitfont6").show();
    		$("#freefont6").show();
    		$("#debitamount6").show();
    		$("#freeamount6").show();
    		$("#fontc").show();
    		$("#fontcs").show();
    		$("#orderfont6").show();
    		$("#wholesingle6").show();
    		wholeSingle();
    	}
     }
     
        
     function wholeSingle(){
    	 if($("#wholesingle6").val()=="0" || $("#wholesingle6").val()==""){
    		 $("#selectOrderCoupon6").hide();
    	 }else{
    		 $("#selectOrderCoupon6").show();
    	 }
     }
     
     function shas(){
    		$('#dishListSecond').datagrid({
     			url : global_Path + "/dish/getDishList.json",
     			singleSelect : false,
     			collapsible : true,
     			rownumbers : true,
     			fitColumns : true,
     			method : 'post',
     			fit : false,
     			view : groupview,
     			remoteSort : false,
     			loadMsg : '数据装载中......',
     			groupField : 'fitemDesc',
     			onClickCell : onClickCellSecond,
     			idField : 'dishid',
     			groupFormatter : function(value, rows) {
     				return value + '(' + rows.length + ')';
     			},
     			columns : [ [{
     				field : 'dishno',
     				title : '编号',
     				width : 20,
     				align : 'left',
     			},{
     				field : 'title',
     				title : '名称',
     				width : 20,
     				align : 'left',
     			}, {
     				field : 'dismoneys',
     				title : '折扣金额',
     				width : 20,
     				align : 'left',
     				editor : {
     					type : 'numberbox',
     					options : {
     						precision : 1
     					}
     				}
     			}, {
     				field : 'discounts',
     				title : '折扣比率',
     				width : 20,
     				align : 'left',
     				editor : {
     					type : 'numberbox',
     					options : {
     						precision : 1
     					}
     				}
     			}, ] ],
     			onLoadSuccess : function(data) {
     				$('#dishListSecond').datagrid('collapseGroup');
     				if(editLists!='' && typeof(editLists) != "undefined"){
     				$.each(editLists,function(editindex,editItem){
     					$.each(data.rows, function(index, item){
     	     			 if(item.dishid==editItem.dishid){
     	     			    if(editItem.discountamount!="" && editItem.discountamount !=null){
     	     			    	$('#dishListSecond').datagrid('endEdit', index);
     		                    $('#dishListSecond').datagrid('updateRow', { index: index, row: { dismoneys: editItem.discountamount,discounts: editItem.discountrate} });		                   
     	     			    }else{
     		                    $('#dishListSecond').datagrid('endEdit', index);
     		                    $('#dishListSecond').datagrid('updateRow', { index: index, row: {discounts: editItem.discountrate} }); 				
     	     			    }
     	     			 }	           	     			 
     					});
     				});
     				} 				
     				if(editList!='' && typeof(editList) != "undefined"){
     					$.each(editList,function(editindex,editItem){
     						$.each(data.rows, function(index, item){
     		     			 if(item.dishid==editItem.dishid){	     			   		     			   		     		
     		     			    if(editItem.dismoneys!="" && editItem.dismoneys !=null){
     		     			    	$('#dishListSecond').datagrid('endEdit', index);
     			                    $('#dishListSecond').datagrid('updateRow', { index: index, row: { dismoneys: editItem.dismoneys,discounts: editItem.discounts} });		                   
     		     			    }else{
     			                     $('#dishListSecond').datagrid('endEdit', index);
     			                     $('#dishListSecond').datagrid('updateRow', { index: index, row: {discounts: editItem.discounts} }); 				
     		     			    }
     		     		 }
     						});
     					});
     					} 
     			},
     		});
     }
     
     function save_selectOrderCoupon() {
    	 shas();
 		$('#dishDetialSecond').dialog('open');
 	}
     
 	//结束编辑
 	function endEditSecond() {
 		var rows = $('#dishListSecond').datagrid('getRows');
 		for (var i = 0; i < rows.length; i++) {
 			$('#dishListSecond').datagrid('endEdit', i);
 		}
 	}
 	
 	function saveSecond() {
 		endEditSecond();
 		editList=[];
 		var row=$('#dishListSecond').datagrid("getData");
 		for(var i=0;i<row.rows.length;i++){
 			if((row.rows[i].dismoneys!="" && typeof(row.rows[i].dismoneys) != "undefined") || (row.rows[i].discounts!="" && typeof(row.rows[i].discounts) != "undefined")){
 				editList.push(row.rows[i]);
 			}
 		}
 		//获取到所有改变的数据
// 		  editList = $('#dishList').datagrid("getChanges", "updated");
 		$('#dishDetialSecond').dialog("close");
 	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false"
		style="padding: 10px 10px;">
		<table id="dishShow"></table>
		<div id="toolbar_datadictionary" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr style="border-bottom: 1px solid #9A9691;">
					<td align="left" width="400"><font
						style="font-size: 18px; color: #000; font-weight: 700;">优惠管理</font></td>
					<td align="right" width="80"><input type="button"
						onclick="show_main_dialog()" value="新建优惠" class="xld_addbutton"></input></td>
					<td align="center" width="80"><input id="dishNameSearch"
						class="easyui-searchbox" style="width: 150px;"
						data-options="searcher:getlist" prompt="优惠搜索" /></td>
				</tr>
			</table>
			<table cellspacing="0" cellpadding="0" border="1px" style="width: 100%; margin: 10px 0px; border: 1px solid #9A9691;" id="selectItem">
				<tr style="height: 32px;" id="dishTypeTr">
					<td id="0" align="center" class="dishTypeSelected"   width="15%" onclick="gethiddenId('0')">全部</td>
					<td id="1" align="center" class="dishTypeUnSelected" width="15%" onclick="gethiddenId('1')">菜品优惠</td>
					<td id="2" align="center" class="dishTypeUnSelected" width="15%" onclick="gethiddenId('2')">订单优惠</td>
					<td id="3" align="center" class="dishTypeUnSelected" width="15%" onclick="gethiddenId('3')">储值优惠</td>
					<td id="4" align="center" class="dishTypeUnSelected" width="15%" onclick="gethiddenId('4')">团购优惠</td>
					<td id="5" align="center" class="dishTypeUnSelected" width="15%" onclick="gethiddenId('5')">信用卡优惠</td>
					<td id="6" align="center" class="dishTypeUnSelected" width="15%" onclick="gethiddenId('6')">更多优惠</td>
				</tr>
			</table>
		</div>		
	<div id="addCouponManageDialog" style="overflow: hidden;">
		    <table>
		        <tr>
		            <td>
		                <input type="button" style="width:150px;"  value="菜品优惠" onclick="javascript:$('#addCouponDialog1').dialog('open');"/>
		            </td>
		            <td>
		                <input type="button" style="width:150px;" value="订单优惠" onclick="javascript:$('#addCouponDialog2').dialog('open');"/>
		            </td>
		        </tr>
		         <tr>
		            <td>
		                <input type="button" style="width:150px;" value="储值优惠" onclick="javascript:$('#addCouponDialog3').dialog('open');"/>
		            </td>
		            <td>
		               <input type="button" style="width:150px;" value="团购优惠" onclick="javascript:$('#addCouponDialog4').dialog('open');"/>
		            </td>
		        </tr>
		         <tr>
		            <td>
		                <input type="button" style="width:150px;" value="信用卡优惠" onclick="javascript:$('#addCouponDialog5').dialog('open');"/>
		            </td>
		            <td>
		                <input type="button" style="width:150px;" value="其它优惠" onclick="javascript:coupontypeChild();wholeSingle();$('#addCouponDialog6').dialog('open');"/>
		            </td>
		        </tr>
		    </table>
		</div>						    
            <input type="hidden" id="couponid" name="couponid" />			
			<input type="hidden" id="couponparent" name="couponparent" />
			<input type="hidden" id="couponchild" name="couponchild" />
			<input type="hidden" id="ruleid" name="ruleid" />		
	
		<div id="addCouponDialog1" style="overflow: hidden;">
			<!-- 
			<table cellspacing="0" cellpadding="0" border="1px" style="width: 30%; margin: 10px 0px; border: 1px solid #9A9691;" id="selectItems">
				<tr style="height: 32px;" id="dishTypeTrs">
					<td id="001" align="center" class="dishTypeSelected" style="width: 50%;"  onclick="gethiddenIds('7')">特价</td>
					<td id="002" align="center" class="dishTypeUnSelected" style="width: 50%;"  onclick="gethiddenIds('8')">满就减</td>
				</tr>
			</table> -->
			<div style="width: 100%; float: left;">
				<font>活动设置</font>
				<hr/>
				<div style="width: 100%; float: left;">
                    <div style="width: 100%; float: left;margin-top:10px;">
                                                                     优惠名称: <input type="text" id="couponsName1" name="couponsName1" /><span style="color: red">*</span> &nbsp;&nbsp;
					         展示终端: <input type="radio" id="showpositionPad" name="showposition1" checked value="0" />&nbsp;&nbsp;PAD端
					   <input type="radio" id="showpositionPos" name="showposition1" value="1" />&nbsp;&nbsp;POS端
					    &nbsp;&nbsp;  优惠时段:
					    <select id="coupontype1" name="coupontype1" onchange="addCheckBox('selectBox1','coupontype1');">
						    <option value="0">每天</option>
						    <option value="1">每周</option>
						    <option value="2">每月</option>
						</select>
                    </div>
					<div id="selectBox1" style="width: 100%; float: left;margin-top:10px;">					       
					</div>                                       
                    <div style="width: 100%; float: left;margin-top:10px;">
                                                                      优惠对象:<input type="radio" id="couponcustomerw" name="couponcustomer1" value="0" checked />&nbsp;&nbsp;所有顾客
					   <input type="radio" id="couponcustomert" name="couponcustomer1" value="1"/>&nbsp;&nbsp;仅限会员                                                                 
                    </div>
                    <div style="width: 100%; float: left;margin-top:10px;">                                                                 
                                                                       有效期:&nbsp;&nbsp; <input class="" readonly onFocus="WdatePicker({startDate:'%y-%M-%d %h:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="begintime1" name="begintime1" />&nbsp;&nbsp;至: <input class="" onFocus="WdatePicker({startDate:'%y-%M-%d %h:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="endtime1" name="endtime1" readonly />
                    </div>
                    <div style="width:8%; float: left;margin-top:10px;">
                                                                      活动简介:
                    </div>
                    <div style="width:92%; float: left;margin-top:10px;">
                           <textarea rows="3" cols="60" style="resize:none;" id="description1" name="description1"></textarea>
                    </div>
                    <div style="width:8%; float: left;margin-top:10px;">
                                                                      活动规则简介:
                    </div>
                    <div style="width:92%; float: left;margin-top:10px;">
                           <textarea rows="3" cols="60" style="resize:none;" id="ruledescription1" name="ruledescription1"></textarea>
                    </div>		
				</div>
				<div style="width: 100%;float: left;margin-top:10px;">				
				<input type="button" value="请选择菜品" onclick="add_datadictionary();" id="mybutton" name="mybutton" />
				<hr/>
				</div>		
			</div>
		</div>		
		<div id="dishDetial">
		<table id="dishList">
		</table> 
		</div>
		<%@ include file="./showdialog.jsp"%>
</div>		
</body>
</html>
