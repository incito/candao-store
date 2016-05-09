    function showLoading(){
		$("#prompt-dialog").modal("show");
	}
	function hideLoading(){
		$("#prompt-dialog").modal("hide");
	}
	
	/**
	 * 判断开始时间与结束时间
	 * @returns {Boolean}
	 */
	function compareBeginEndTime(){
		beginT = $("#beginTime").val();
		endT = $("#endTime").val();
		if(beginT == null || beginT == ""){
			alert("开始时间不能为空！");
			$("#beginTime").val(getNowDate());
			$("#beginTime").focus();
			return false;
		}
		if(endT == null || endT == ""){
			alert("结束时间不能为空！");
			$("#endTime").val(getNowDate1());
			$("#endTime").focus();
			return false;
		}
		if(beginT > endT){
			alert("结束时间不能小于开始时间！");
			$("#endTime").val(getNowDate1());
			$("#endTime").focus();
			return false;
		}
		return true;
	}

	/**
	 * 显示结账单
	 * @param orderId
	 */
	function showReckon(orderId){
		$("#orderid_param").val(orderId);
		initReckonData(function(){
			$("#reckoning-dialog").modal("show");
		});
	}
	
	/**
	 * 切换查询类型的时候，为searchType赋值
	 * @param type
	 * @param o
	 */
	function changeDataType(type, o){
		searchType = type;
		gethiddenId(type, o);
	}
	
    //查询挂账信息表总表
    function getCreditData(){
		showLoading();
		beginTime = $("#beginTime").val();
		endTime = $("#endTime").val();
		billName = $("#billName").val();
		clearStatus = $("#clearStatus").val();
		if(compareBeginEndTime()){
			$.post(global_Path + "/gisterBill/getBillCount.json", {
				beginTime : beginTime,
				endTime : endTime,
				billName : billName,
				clearStatus : clearStatus
			}, function(result) {
				hideLoading();
				if(result.code == 1){
					initCreditTb(result.resultList);
				}else{
					alert(result.msg);
				}
			},'json');
		}
	}
	
	//初始化总表数据
	function initCreditTb(datalist) {
		var tHtml = "";
		if (datalist != null && datalist != "") {
			$.each(datalist, function(i, obj) {
				var gzds = obj.gzds;
				var yjje = obj.yjje == "" ? 0 : parseFloat(obj.yjje);
				var gzze = obj.gzze == "" ? 0 : parseFloat(obj.gzze);
				var wjje = obj.wjje == "" ? 0 : parseFloat(obj.wjje);
				var zcgzsj = obj.zcgzsj;
				var gzdw = obj.gzdw;
				tHtml += '<tr beginTime="'+beginTime+'"endTime="'+endTime+'" +gzdw="'+gzdw+'" gzds="'+gzds+'" gzze= "'+gzze.toFixed(2)+'" ondblclick="showCreditSubTb(\''
				        + gzdw + '\',\''
				        + gzds + '\',\''
				        + gzze +'\')">'
						+ '<td width="25%">' + gzdw + '</td>'
						+ '<td width="25%">' + gzds + '</td>'
						+ '<td width="25%">' + gzze.toFixed(2) + '</td>'
						+ '<td width="25%">' + yjje.toFixed(2) + '</td>'
						+ '<td width="25%">' + wjje.toFixed(2) + '</td>'
						+ '<td width="25%">' + zcgzsj + '</td>'
						+ '</tr>';
			});
		}else{
			tHtml += '<tr><td colspan="6">没有数据</td></tr>';
		}
		$("#credit-tb tbody").html(tHtml);
	}
	
	//查询第二个弹出表
	function showCreditSubTb(gzdw,gzds,gzze){
		$("#p_gzdw").val(gzdw);
		$("#gzdw").text(gzdw);
		$("#gzds").text(gzds);
		$("#gzze").text(gzze);
		$("#p_gzze").val(gzze);
		$("#credit-dialog").modal("show");
		getCreditDetails(gzdw,gzze);
	}
	
	//第二个弹出表拼数据
	function getCreditDetails(gzdw,gzze){
		$.post(global_Path+"/gisterBill/getBillDetail.json", {
			beginTime: beginTime,
			endTime: endTime,
			billName: $("#p_gzdw").val(),
			clearStatus: $("#clearStatus").val()
		}, function(result){
			if(result.code == 1){
				var data = result.resultList;
				var htm = '';
				if(data != null && data.length>0){
					$.each(data, function(i, item){
						var gzje = item.gzje == "" ? 0 : parseFloat(item.gzje);
						var yjje = item.yjje == "" ? 0 : parseFloat(item.yjje);
						var wjje = item.wjje == "" ? 0 : parseFloat(item.wjje);
						var qsbz = item.qsbz;
						var qsbzdesc = qsbz == 1 ? "已清算" : "未清算";
						var operate = "";
						if(qsbz == 1){
							operate = '<a>查看</a>';
						}else if(qsbz == 2){
							operate = '<a>挂账清算</a>';
						}
						htm += '<tr>'
						    + '<td nowrap="nowrap" ondblclick="showReckon(\''+item.ddbh+'\')">'+item.ddsj+'</td>'
							+ '<td ondblclick="showReckon(\''+item.ddbh+'\')">'+item.ddbh+'</td>'
							+ '<td ondblclick="showReckon(\''+item.ddbh+'\')">'+gzje.toFixed(2)+'</td>'
							+ '<td ondblclick="showReckon(\''+item.ddbh+'\')">'+yjje.toFixed(2)+'</td>'
							+ '<td ondblclick="showReckon(\''+item.ddbh+'\')">'+wjje.toFixed(2)+'</td>'
							+ '<td ondblclick="showReckon(\''+item.ddbh+'\')">'+qsbzdesc+'</td>'
							+ '<td nowrap="nowrap" ondblclick="showReckon(\''+item.ddbh+'\')">'+item.qssj+'</td>'
							+ '<td ondblclick="showReckon(\''+item.ddbh+'\')">'+item.beizhu+'</td>'
							+ '<td onclick="showCreditView(\''+item.ddbh
							+'\',\''+gzdw
							+'\',\''+gzje
							+'\',\''+yjje
							+'\',\''+wjje
							+'\',\''+item.ddsj
							+'\',\''+qsbz
							+'\')">'+operate+'</td></tr>';
					});
				}else{
					htm = '<tr><td colspan="9">无数据</td></tr>';
				}
				$("#credit-details-tb tbody").html(htm);
			}else{
				alert(result.msg);
			}
		},'json');
	}
	
	//挂账清算或清算历史赋值
	function showCreditView(orderid,gzdw,gzje,yjje,wjje,ddsj,qsbz){
		$("#jieshushijian").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
		$("#creaditname").text(gzdw);
		$("#orderId").val(orderid);
		$("#p_wjje").val(wjje);
		if(qsbz == 1){
			$("#p_title").text("清算历史");
		}else if(qsbz == 2){
			$("#p_title").text("挂账清算");
		}
		var qsbzdesc = qsbz == 1 ? "已清算" : "未清算";
		//拼挂账清算数据
		var htm = '<tr>'
	    + '<td>挂账单位：'+gzdw+'</td>'
	    + '<td>挂账金额：'+gzje+'</td>'
	    + '<td>已结金额：'+yjje+'</td>'
	    + '<td>未结金额：'+wjje+'</td></tr>'
	    + '<tr><td>订单编号：'+orderid+'</td>'
	    + '<td>挂账时间：'+ddsj+'</td>'
	    + '<td>清算标志：'+qsbzdesc+'</td></tr>';
		$("#credit-details-tb-1").html(htm);
		//拼结算历史数据
		showCreditDetailView(orderid);
		//拼本次结算数据
		if(qsbz == '2'){
			$("#p_credit").text("本次结算");
			$("#jiesuajine").val("");
			$("#youmianjine").val("");
			$("#remark").val("");
			$("#credit-details-tb-3").show();
			$("#_submitBtn").show();
		}else{
			$("#p_credit").text("");
			$("#credit-details-tb-3").hide();
			$("#_submitBtn").hide();
		}
		$("#credit-history-dialog").show();
	}
	
	//挂账清算或清算历史
	function showCreditDetailView(orderid){
		$.post(global_Path+"/gisterBill/getBillHistory.json", {
			orderid : orderid
		}, function(result){
			if(result.code == 1){
				var data = result.resultList;
				var htm = '';
				if(data != null && data.length>0){
					$.each(data, function(i, item){
						var payamount = item.payamoun == "" ? 0 : parseFloat(item.payamount);
						var disamount = item.disamount == "" ? 0 : parseFloat(item.disamount);
						var seq = i+1;
						htm += '<tr>'
							+ '<td>'+seq+'</td>'
						    + '<td nowrap="nowrap">'+item.inserttime+'</td>'
							+ '<td nowrap="nowrap">已结'+payamount.toFixed(2)+'元，优免'+disamount.toFixed(2)+'</td>'
							+ '<td nowrap="nowrap">操作员:'+item.operator+'</td>'
							+ '<td>备注:'+item.remark+'</td></tr>';
					});
				}
				$("#credit-details-tb-2").html(htm);
			}else{
				alert(result.msg);
			}
		},'json');
	}
	
	//提交清算信息
	function submitCredit(){
		var inserttime = $("#jieshushijian").val();
		var payamount = $("#jiesuajine").val();
		var disamount = $("#youmianjine").val();
		var wjje = $("#p_wjje").val();
		if(inserttime == null || inserttime == ""){
			alert("结算时间不能为空！");
			$("#inserttime").val(getNowDate());
			$("#inserttime").focus();
			return false;
		}
		if(payamount == "" && disamount == ""){
			alert("结算金额和优免金额不能同时为空!");
			return false;
		}
		if(payamount == "" || payamount == null){
			payamount = 0;
		}else{
			if(isNaN(payamount)){
				alert("结算金额需要输入数字!");
				return false;
			}else{
				payamount = parseFloat(payamount);
			}
		}
		if(disamount == "" || disamount == null){
			disamount = 0;
		}else{
			if(isNaN(disamount)){
				alert("优免金额需要输入数字!");
				return false;
			}else{
				disamount = parseFloat(disamount);
			}
		}
		var amount = payamount + disamount;
		if(amount > parseFloat(wjje)){
			alert("结算金额加优免金额不能大于未结金额!");
			return false;
		}
		$.post(global_Path+"/gisterBill/Billing.json", {
			orderid : $("#orderId").val(),
			creaditname : $("#creaditname").text(),
			inserttime : inserttime,
			payamount : payamount,
			disamount : disamount,
			remark : $("#remark").val()
		}, function(result){
			if(result.code == 1){
				$("#credit-history-dialog").hide();
			}else{
				alert(result.msg);
			}
		},'json');
		getCreditDetails($("#creaditname").text(),$("#p_gzze").val());
	}
	
	function closedCredit(){
		$("#credit-history-dialog").hide();
	}
	
	//导出
	function exportCreditReport(){
		$("#_beginTime").val(beginTime);
		$("#_endTime").val(endTime);
		$("#_billName").val(billName);
		$("#_clearStatus").val(clearStatus);
		$("#_searchType").val(searchType);
		$("#creditForm").attr("action", global_Path + "/gisterBill/exportRegisterBill.json");
		$("#creditForm").submit();
	}
	
	//设置挂账单位
	function setDzdw(){
		$.get(global_Path+"/gisterBill/getBillUnit.json",
		    function(result){
				var option = '<option value="0">全部</option>';
				$.each(result.resultList, function(i, item){
					option += '<option value="'+item.bankcardno+'">'+item.bankcardno+'</option>';
				});
				$("#billName").html(option);
		});
	}
	
	