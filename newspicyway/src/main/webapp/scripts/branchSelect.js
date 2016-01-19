/**
 * 门店选择js对象
 * @author lishoukun
 * @date 2015/04/28
 * @describe
 * 引入该js后，页面使用分3个步骤
 * 1，在页面中添加下拉列表容器代码,其中openSelectTr为外层容器（隐藏时使用），openSelectId为内层容器
 * 		如：
  				<tr id="openSelectTr" class="shops" style="display: none;">
					<td class="labelText">分店：</td>
					<td>
						<div id="openSelectId"></div>
					</td>
				</tr>
	2，在脚本中初始化,如：
		var branchSelectObj = new BranchSelect();//选择门店对象
		//初始化选择门店对象
		branchSelectObj.init("openSelectTr","openSelectId");
		
	3，使用对象中的各种方法，如：
		branchSelectObj.showDialog();//显示面板
		branchSelectObj.resetCombo();//重置下拉列表
		branchSelectObj.resetDialog();//重置对话框
		branchSelectObj.disabledDialog();//禁用对话框
		branchSelectObj.setData(branchs);赋值
		branchSelectObj.getJsonData();获取json格式的值
			注：赋值数据格式如下(数组)
			[
			{id:"3d10300e77ab4325b9190fd0d3e0a720",userId:"18c89ecf4f8c4d6696be126104cb95e6",branchId:"183268",branchName:" 新辣道鱼火锅(华侨城店) "},
			{id:"1dde0ff86da34d5eb983cd2fde2fea7b",userId:"18c89ecf4f8c4d6696be126104cb95e6",branchId:"204557",branchName:" 新辣道鱼火锅(维多利店)"},
			{id:"57579489b64f4810a3a96046c336a847",userId:"18c89ecf4f8c4d6696be126104cb95e6",branchId:"268841",branchName:" 新辣道鱼火锅(南京新一城店)"},
			....
			]
			注：获取值的数据结构如下(json数组)：
			[
			{"id":"3d10300e77ab4325b9190fd0d3e0a720","userId":"18c89ecf4f8c4d6696be126104cb95e6","branchId":"183268","branchName":" 新辣道鱼火锅(华侨城店) "},
			{"id":"1dde0ff86da34d5eb983cd2fde2fea7b","userId":"18c89ecf4f8c4d6696be126104cb95e6","branchId":"204557","branchName":" 新辣道鱼火锅(维多利店)"}
			....
			]
 */
function BranchSelect(){
	var branchurl = "shopMg/getall.json";//门店URL
	var saveBranchArray = new Array();//存储上次已保存
	var branchArray = new Array();
	var isDialogInit = false;
	var isComboInit = false;
	var comboDivId;
	var comboContainerId;
	var randomId;
	/*
	 * 初始化
	 */
    this.init = function(comboContainerId1,comboDivId1){
    	randomId = parseInt(Math.random()*10000);
    	//初始化弹出框html部分
    	var html = 	'<div class="modal fade storeSelect-dialog in " id="branchSelect'+randomId+'" style="z-index:99999">';
    		html+=	'<div class="modal-dialog">';
    		html+=	'<div class="modal-content">';	
    		html+=	'	<div class="modal-header addDelicon">';				  
    		html+=	'       <img data-dismiss="modal" class="img-close" src="/newspicyway/images/close.png">';
    		html+=	'    </div>';
    		html+=	'	<div class="modal-body">';
    		html+=	'		<div class="row store-select-title">';
    		html+=	'			<div class="col-xs-9"><span id="alreadySelectTitle">选择门店</span></div>';
    		html+=	'			<div class="col-xs-3 pull-right">';
    		html+=	'				<label class="radio-inline">';
    		html+=	'					<input type="radio" value="1" name="checkAll" id="checkAll'+randomId+'" >全选';
    		html+=	'				</label>';
    		html+=	'				<label class="radio-inline">';
    		html+=	'					<input type="radio" value="0" name="checkAll" id="noAll'+randomId+'" >全不选';
    		html+=	'				</label>';
    		html+=	'			</div>';
    		html+=	'		</div>';
    		html+=	'		<hr>';
    		html+=	'		<div class="table store-select-content" id="branchContent'+randomId+'" >';	
    		html+=	'		</div>';
    		html+=	'		<div class="btn-operate">';
    		html+=	'			<button class="btn btn-cancel in-btn135" id="selectCancel'+randomId+'">取消</button>';
    		html+=	'			<div  class="btn-division"></div>';
    		html+=	'			<button class="btn btn-save in-btn135 preferential-btn-bgcolor" id="selectConfirm'+randomId+'">确认</button>';
    		html+=	'		</div>';
    		html+=	'	</div>';
    		html+=	'</div>';
    		html+=	'</div>';
    		html+=	'</div>';
    		
    		$("body").append(html);
    	//全选按钮
    	$("#checkAll"+randomId).click(function(){
    		$("#branchSelect"+randomId+" input[name='branch']").prop("checked",true);
    		var length = $("#branchSelect"+randomId+" :checkbox:checked").length;
			if(length > 0){
				$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店("+"已选<span style='color:red'>"+length + "</span>家店"+")");
			}else{
				$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店");
			}
    	});
    	//全不选按钮
    	$("#noAll"+randomId).click(function(){
    		$("#branchSelect"+randomId+" input[name='branch']").prop("checked",false);
    		$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店");
    	});
    	//确定选中门店
    	$("#selectConfirm"+randomId).click(function(){
			branchArray = [];
			var text='';
			
			var ul = $("<ul/>").addClass("storesDiv");
			$("#branchSelect"+randomId+" :checkbox:checked").each(function(i, v){
					text += $(this).next().text();
					if(text != ''){
						text += ",";
					}
					var obj = new Object();
					obj.branchId = $(this).val();
					obj.branchName = $(this).next().text();
					branchArray.push(obj);
					ul.append("<li>"+obj.branchName+"</li>")
			});
			
			if(branchArray.length > 0){
				$(".add-shop-select").find("div.popover").remove();
				var top = ileft = iwidth ="";
				if(branchArray.length >= 3){
					iwidth = "460px";
					ileft = "-70px";
					
				}
				var div = $("<div>").addClass("popover fade bottom in").css({
					width : iwidth,
					top : "30px",
					left: ileft
				}).append('<div class="arrow" style="left: 50%;"></div>');
				div.append(ul);
				$('#'+comboDivId).text("已选中"+branchArray.length + "家店").append(div);
				$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店("+"已选<span style='color:red'>"+branchArray.length + "</span>家店"+")");
			}else{
				$('#'+comboDivId).html('<img src="/newspicyway/images/add.png" alt="">').next(".popover").remove();
				$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店");
			}
			
			//如果选择数等于全部多选框的数量，则选中全选，如果选择数为0，则选中全不选的按钮
			var allLength = $("#branchSelect"+randomId+" :checkbox").length;
			if(branchArray.length==0){
		    	$("#noAll"+randomId).prop("checked",true);
			}else if(allLength==branchArray.length){
		    	$("#checkAll"+randomId).prop("checked",true);
			}else{
		    	$("#checkAll"+randomId).prop("checked",false);
		    	$("#noAll"+randomId).prop("checked",false);
			}
			
			$("#branchSelect"+randomId).modal("hide");
			saveBranchArray = branchArray;
			
    	});
    	//取消选择门店
    	$("#selectCancel"+randomId).click(function(){
    		$("#branchSelect"+randomId).modal("hide");
    	});
    	if(comboContainerId1!=null){
    		comboContainerId = comboContainerId1;
    	}
    	if(comboDivId1!=null){
    		//初始化下拉列表
    		comboDivId = comboDivId1;
    		$("#"+comboDivId).addClass("add-shop-select").addClass("col-xs-1");
    		$("#"+comboDivId).html('<img src="/newspicyway/images/add.png" alt="">');
    		//下拉列表点击事件
    		$("#"+comboDivId).click(function(){
    			if(!$("#openSelectId").hasClass("disabled")){
    				$("#branchContent"+randomId).empty();
    		    	$.get(global_branchurl, "", function(dataList){
    		    		$.each(dataList, function(i, v) { 
    		    			var html = 	'<label class="checkbox-inline">';
    		    			html	+=  '<input type="checkbox" name="branch" value="'+v.branchid+'"><span>'+v.branchname+'</span>';
    		    			html	+=  '</label>';
    		    			$("#branchContent"+randomId).append(html);
    		    		});
    		    		//为初始化后的列表，做点击事件
    		    		$("#branchContent"+randomId+" input[name='branch']").click(function(){
    		    			var length = $("#branchSelect"+randomId+" :checkbox:checked").length;
    		    			if(length > 0){
    		    				$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店("+"已选<span style='color:red'>"+length + "</span>家店"+")");
    		    			}else{
    		    				$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店");
    		    			}
    		    			//如果选择数等于全部多选框的数量，则选中全选，如果选择数为0，则选中全不选的按钮
    		    			var allLength = $("#branchSelect"+randomId+" :checkbox").length;
    		    			if(length==0){
    		    		    	$("#noAll"+randomId).prop("checked",true);
    		    			}else if(allLength==length){
    		    		    	$("#checkAll"+randomId).prop("checked",true);
    		    			}else{
    		    		    	$("#checkAll"+randomId).prop("checked",false);
    		    		    	$("#noAll"+randomId).prop("checked",false);
    		    			}
    		    		});
    		    		
    		    		$("#branchSelect"+randomId).modal({backdrop: 'static', keyboard: false});
        	    		
        	    		//每次打开选择门店面板后，都是上次已保存的数据
        	    		$("#branchSelect"+randomId+" input[name='branch']").prop('checked',false);
        	        	$.each(saveBranchArray,function(i, v){
        	        		$("#branchSelect"+randomId+" input[name='branch'][value='"+v.branchId+"']").prop('checked',true);
        	        	});
        	        	
        	        	if(saveBranchArray.length > 0){
        	        		$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店("+"已选<span style='color:red'>"+saveBranchArray.length + "</span>家店"+")");
        	        	}else{
        	        		$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店");
        	        	}
        	        	//如果选择数等于全部多选框的数量，则选中全选，如果选择数为0，则选中全不选的按钮
        	    		var allLength = $("#branchSelect"+randomId+" :checkbox").length;
        	    		if(saveBranchArray.length==0){
        	    	    	$("#noAll"+randomId).prop("checked",true);
        	    		}else if(allLength==saveBranchArray.length){
        	    	    	$("#checkAll"+randomId).prop("checked",true);
        	    		}else{
        	    	    	$("#checkAll"+randomId).prop("checked",false);
        	    	    	$("#noAll"+randomId).prop("checked",false);
        	    		}
    		    	});
    	    		
    			}
    		});
    	}
    };
    /*
     * 打开选择门店对话框
     */
    this.showDialog = function(){
    	if(!$("#openSelectId").hasClass("disabled")){
    		$("#branchSelect"+randomId).modal({backdrop: 'static', keyboard: false});
    	}
    };
    /*
     * 隐藏选择门店对话框 
     */
    this.hideDialog = function(){
    	$("#branchSelect"+randomId).modal("hide");
    };
    /*
     * 重置门店对话框
     */
    this.resetDialog = function(){
    	branchArray = new Array();
    	$("#branchSelect"+randomId+" input[name='branch']").prop("checked",false);
    };
    /*
     * 禁用门店对话框
     */
    this.disabledDialog = function(){
    	//禁用确定选择门店按钮
    	$("#selectConfirm"+randomId).attr("disabled","disabled"); 
    	//禁用全选和全不选单选框
    	$("#checkAll"+randomId).attr("disabled","disabled"); 
    	$("#noAll"+randomId).attr("disabled","disabled"); 
    	//禁用所有门店复选框
    	$("#branchSelect"+randomId+" input[name='branch']").prop('disabled',"disabled");
    };
    /*
     * 启用门店对话框
     */
    this.enabledDialog = function(){
    	//确定选择门店按钮
    	$("#selectConfirm"+randomId).removeAttr("disabled"); 
    	//全选和全不选单选框
    	$("#checkAll"+randomId).removeAttr("disabled"); 
    	$("#noAll"+randomId).removeAttr("disabled"); 
    	//所有门店复选框
    	$("#branchSelect"+randomId+" input[name='branch']").removeAttr('disabled');
    };
    /*
     * 获取json格式数据 
     */
    this.getJsonData = function(){
    	return JSON.stringify(branchArray);
    };
    /*
     * 设置值 
     */
    this.setData = function(branchs){
    	//为门店id赋值
    	branchArray = branchs;
    	saveBranchArray = branchs;
    	var branchText = '';
    	$.each(branchs,function(i, v){
    		if(branchText!=''){
    			branchText += ',';
    		}
    		branchText += v.branchName;
    		$("#branchSelect"+randomId+" input[name='branch'][value='"+v.branchId+"']").prop('checked',true);
    	});
    	

    	if(branchArray.length > 0){
    		var ul = $("<ul/>").addClass("storesDiv");
    		
    		$.each(branchArray, function(i, v){
    			ul.append("<li>"+v.branchName+"</li>")
    		});
    		$(".add-shop-select").find("div.popover").remove();
    		var top = ileft = iwidth ="";
    		if(branchArray.length >= 3){
    			iwidth = "460px";
    			ileft = "-70px";
    			
    		}
    		var div = $("<div>").addClass("popover fade bottom in").css({
    			width : iwidth,
    			top : "30px",
    			left: ileft
    		}).append('<div class="arrow" style="left: 50%;"></div>');
    		div.append(ul);
    		$('#'+comboDivId).text("已选中"+branchArray.length + "家店").append(div);
    		$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店("+"已选<span style='color:red'>"+branchArray.length + "</span>家店"+")");
    	}else{
    		$('#'+comboDivId).html('<img src="/newspicyway/images/add.png" alt=""> ').next(".popover").remove();
    		$("#branchSelect"+randomId+" #alreadySelectTitle").html("选择门店");
    	}
    	//如果选择数等于全部多选框的数量，则选中全选，如果选择数为0，则选中全不选的按钮
		var allLength = $("#branchSelect"+randomId+" :checkbox").length;
		if(branchArray.length==0){
	    	$("#noAll"+randomId).prop("checked",true);
		}else if(allLength==branchArray.length){
	    	$("#checkAll"+randomId).prop("checked",true);
		}else{
	    	$("#checkAll"+randomId).prop("checked",false);
	    	$("#noAll"+randomId).prop("checked",false);
		}
    	
//    	
//    	
//    	//如果没有选择，则将图标显示为图片
//    	if(branchText==''){
//    		$('#'+comboDivId).html('<img src="/newspicyway/images/add.png" alt="">');
//    	}else{
//    		if(branchText.length>28){
//    			branchText = branchText.substring(0,28)+'...';
//    		}
//    		$('#'+comboDivId).html(branchText);
//    	}
    };
    /*
     * 显示下拉列表
     */
    this.showCombo = function(){
    	$('#'+comboContainerId).show();
    };
    /*
     * 隐藏下拉列表
     */
    this.hideCombo = function(){
    	$('#'+comboContainerId).hide();
    };
    /*
     * 重置下拉列表
     */
    this.resetCombo = function(){
    	$('#'+comboDivId).html('<img src="/newspicyway/images/add.png" alt="">');
    	$("#branchSelect"+randomId+" #alreadySelectTitle").text("选择门店");
    };
    /*
     * 禁用下拉列表
     */
    this.disabledCombo = function(){
    	//暂无操作
    };
    /*
     * 启用下拉列表
     */
    this.enabledCombo = function(){
    	//暂无操作
    };
}