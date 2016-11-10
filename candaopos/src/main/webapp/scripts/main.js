var g_eatType = "EAT-IN";//堂食
var tackOUttable=[];//外卖咖啡外卖台

var MainPage = {

	CurrentTalbeType: 'all',
	CurrentArea: '-1',//默认为全部
	CurrentSelectedTable: '',


	init: function(){

		this.timeTask();

		this.setTables();

		SetBotoomIfon.init();

		this.bindEvent();

		//加载虚拟键盘组件
		widget.keyboard();

		/*雅座会员餐道会员切换 1为餐道会员；2为雅座会员*/
		if(utils.storage.getter('vipType')=='1'){
			var str='<li class="J-btn-storge">会员储值</li>'+
				'<li class="J-btn-register">会员注册</li>'+
				'<li class="J-btn-memberView">会员查询</li>'
			$('.arrowMember').html(str)
		}
		if(utils.storage.getter('vipType')=='2'){
			var str='<li class="J-btn-storge">会员储值</li>'+
				'<li class="J-btn-register">会员激活</li>'+
				'<li class="J-btn-memberView">会员查询</li>'
			$('.arrowMember').html(str)
		}
	},

	bindEvent: function(){

		var that = this;
		var dom = {
			standardTables : $("#standard-tables"),
			openDialog : $("#open-dialog"),//开台权限验证弹窗,
			roomTypeNav: $(".rooms-type"),//餐台分类导航
		};
		/**
		 * 标准台事件
		 */
		dom.standardTables.on('click','li', function() {
			var me = $(this);
			var cla = me.attr("class");

			that.CurrentSelectedTable = me;

			if (cla !== "opened") {
				$("#open-dialog").modal("show");
				return false;
			}
			var url = "../views/order.jsp?orderid=" + me.attr('orderid') + '&personnum=' + me.attr('personnum') + '&tableno=' + me.attr('tableno')  + '&type=in';
			window.location.href = encodeURI(encodeURI(url));
		});

		/**
		 * 开启服务员权限验证
		 */
		dom.openDialog.on('click', '.age-type>div', function(){
			var me = $(this);
			me.toggleClass('active');
		});

		dom.openDialog.on('change', '.J-male-num, .J-female-num', function(){
			var maleNum = dom.openDialog.find('.J-male-num').val() === '' ? '0' : dom.openDialog.find('.J-male-num').val();
			var femailNum = dom.openDialog.find('.J-female-num').val() === '' ? '0' : dom.openDialog.find('.J-female-num').val();
			$('.J-tableware-num').val(parseInt(maleNum,10) + parseInt(femailNum,10))
		});

		dom.openDialog.on('click','.J-btn-submit', function(){
			var serverName = dom.openDialog.find('.J-server-name').val();
			var maleNum = dom.openDialog.find('.J-male-num').val() === '' ? '0' : dom.openDialog.find('.J-male-num').val();
			var femailNum = dom.openDialog.find('.J-female-num').val() === '' ? '0' : dom.openDialog.find('.J-female-num').val();

			if(parseInt(maleNum,10) + parseInt(femailNum,10) < 1) {
				widget.modal.alert({
					cls: 'fade in',
					content:'<strong>请输入就餐人数</strong>',
					width:500,
					height:500,
					btnOkTxt: '确定',
					btnCancelTxt: ''
				});
				return false;
			}

			if(serverName === undefined || serverName === '') {
				widget.modal.alert({
					cls: 'fade in',
					content:'<strong>请输入服务员编号</strong>',
					width:500,
					height:500,
					btnOkTxt: '确定',
					btnCancelTxt: ''
				});
				return false;
			}

			//验证用户权限,然后开台
			that.verifyUser(serverName, function(){
				var $target = that.CurrentSelectedTable;
				$.ajax({
					url: _config.interfaceUrl.OpenTable,
					method: 'POST',
					contentType: "application/json",
					data: JSON.stringify({
						tableNo: $target.attr('tableno'),
						ageperiod: (function(){
							var str = '';
							dom.openDialog.find('.age-type>div').each(function(){
								var me = $(this);
								if(me.hasClass('active')){
									str += me.index()+1;
								}
							});
							return str;
						})(),
						username: serverName,
						manNum: maleNum,
						womanNum: femailNum
					}),
					dataType:'json',
					success: function(res){
						if(res.code === '0') {
							var url = "../views/orderdish.jsp?orderid=" + res.data.orderid + '&personnum=' + $target.attr('personnum') + '&tableno=' + $target.attr('tableno')  + '&type=in';
							dom.openDialog.modal('hide');
							window.location.href = encodeURI(encodeURI(url));
						} else {
							widget.modal.alert({
								cls: 'fade in',
								content:'<strong>' + res.msg + '</strong>',
								width:500,
								height:500,
								btnOkTxt: '',
								btnCancelTxt: '确定'
							});
						}
					}
				})

			});
		});


		//退出系统
		$(".exit-sys").click(function(){
			window.location = "../views/login.jsp";
			utils.clearLocalStorage.clearSelect()
		});

		//标准台和咖啡台切换
		//$(".menu-tab ul li").click(function(){
		//	var olddiv = $(".menu-tab ul li.active").attr("loaddiv");
		//	$(olddiv).addClass("hide");
		//	$(".menu-tab ul li").removeClass("active");
		//	$(this).addClass("active");
		//	var loaddiv = $(this).attr("loaddiv");
		//	$(loaddiv).removeClass("hide");
		//});

		/*餐台分类事件*/
		var roomtype_prev = 0;
		var navRoomTypes = $("#nav-room-types");

		dom.roomTypeNav.delegate('li', 'click', function() {
			var me = $(this);
			me.siblings().removeClass("active").end().addClass('active');
			me.addClass("active");
			that.CurrentArea = me.attr('areaid');
			that.setTables(that.CurrentTalbeType,me.attr('areaid'));
		});

		dom.roomTypeNav.delegate('.nav-type-next', 'click', function() {
			var count = navRoomTypes.find( "li").length;
			if (roomtype_prev < count - 10) {
				navRoomTypes.find("li").eq(roomtype_prev).css("margin-left", "-10%");
				navRoomTypes.find("li").eq(roomtype_prev+1).click();
				roomtype_prev++;
			}
		});

		dom.roomTypeNav.delegate('.nav-type-prev', 'click', function() {
			if(roomtype_prev>=1){
				navRoomTypes.find("li").eq(roomtype_prev-1).css("margin-left","0");
				navRoomTypes.find("li").eq(roomtype_prev-1).click();
				roomtype_prev--;
			}
		});

		//底部菜单事件绑定
		$('footer').on('click','.foot-menu li',function(e){
			var me = $(this);
			if(me.hasClass("J-btn-takeout")){
				that.initTakeOutModal();
			}
			if(me.hasClass("member-btns")){
				//会员
				$(".m-member.popover").toggle();
				e.stopPropagation();
			}
			if(me.hasClass('J-btn-sys')) {
				$("#sys-dialog").load("../views/sys.jsp");
				$("#sys-dialog").modal("show");
			}
			if(me.hasClass('J-btn-rep')) {
				window.location.href="../views/reporting/reporting.jsp";
			}
			if(me.hasClass('J-btn-check')) {
				window.location.href="../views/check/check.jsp";

			};
			if(me.hasClass('J-btn-clear')) {
				var aUserid=utils.storage.getter('aUserid'),orderLength=0,str//获取登录用户
				$.ajax({
					url: _config.interfaceUrl.QueryOrderInfo+''+ aUserid + '/',
					type: "get",
					async: false,
					dataType: "text",
					success: function (data) {
						data = JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
						var arry = [];
						for (var i = 0; i < data.OrderJson.length; i++) {
							if (data.OrderJson[i].orderstatus == 0) {
								arry.push(data.OrderJson[i])
							}
						}
						orderLength=arry.length
					}
				});

				var str ='<strong>请选择倒班或结业：</strong>'
				str+='<div id="clearchoose" class="form-group form-group-base" style="margin-top: 20px">'
				if(utils.userRight.get(aUserid,"030204")){//判断清机权限
					str+='<button id="clearAll" class="btn-default btn-lg btn-base btn-base-flex2 clearAll"  style="margin-right: 5px">倒班</button>'
				}
				else {
					str+='<button id="clearAll" class="btn-default btn-lg btn-base btn-base-flex2 clearAll" disabled="disabled" style="margin-right: 5px;color: #999; background: #E8E8E8">倒班</button>'
				}
				if(orderLength>0){//判断账单未结业数量
					str+='<button id="completion" class="btn-default btn-lg btn-base btn-base-flex2 clearCompletion" disabled="disabled" style="color: #999; background: #E8E8E8">结业</button>'
					str+='</div>'
					str+='<div class="glyphicon glyphicon-info-sign" style="color: #8c8c8c;">还有未结账的餐台不能结业</div>'
				}
				else {
					str+='<button id="completion" class="btn-default btn-lg btn-base btn-base-flex2 clearCompletion" >结业</button>'
					str+='</div>'
				}
				widget.modal.alert({
					cls: 'fade in',
					content:str,
					width:400,
					height:500,
					title: "",
					hasBtns:false,
				});
				$("#clearchoose button").click(function () {
					$(".modal-alert:last,.modal-backdrop:last").remove();
					var _this = $(this);
					if(_this.hasClass("clearAll")){
						$(".modal-alert:last,.modal-backdrop:last").remove();
						$("#J-btn-checkout-dialog").load("../views/check/impower.jsp",{'title':'清机授权','usernameDisble':'1','cbd':'MainPage.clearAll()','userRightNo':'030204'});
						$("#J-btn-checkout-dialog").modal("show");
					}
					if(_this.hasClass("clearCompletion")){//结业
						var str ='<strong>确定要结业吗？</strong>';
						widget.modal.alert({
							cls: 'fade in',
							content:str,
							width:400,
							height:500,
							title: "结业提醒",
							btnOkTxt: '确定',
							btnOkCb: function(){
								$(".modal-alert:last,.modal-backdrop:last").remove();
								MainPage.checkout();
							},
							btnCancelCb: function(){
							}
						})

					}

				});

			}

			if(me.hasClass('J-btn-register')) {//会员注册
				if(utils.storage.getter('vipType')=='1'){
					window.location.href = '../views/member/register.jsp';
				}
				if(utils.storage.getter('vipType')=='2'){
					window.location.href = '../views/member/yaRegister.jsp';
				}
				/*$("#register-dialog").load("../views/member/register.jsp");
				 $("#register-dialog").modal("show");*/
			}

			if(me.hasClass('J-btn-storge')) {//会员储值
				if(utils.storage.getter('vipType')=='1'){
					window.location.href = '../views/member/storge.jsp';
				}
				if(utils.storage.getter('vipType')=='2'){
					window.location.href = '../views/member/yaRecharge.jsp';
				}
			}

			if(me.hasClass('J-btn-memberView')) {//会员查询
				if(utils.storage.getter('vipType')=='1'){
					window.location.href = './member/view.jsp';
				}
				if(utils.storage.getter('vipType')=='2'){
					window.location.href = '../views/member/yaStorge.jsp';
				}
			}
		});

		$(document).click(function(e){
			$(".m-member.popover").hide();
			e.stopPropagation();
		});

		//设置餐桌统计
		$(".J-table-nums>div").click(function(){
			var me = $(this);
			me.siblings().removeClass("active").end().addClass('active');
			if(me.hasClass('all')) {
				that.setTables('all', that.CurrentArea);
				that.CurrentTalbeType = 'all';
			} else if(me.hasClass('opened')) {
				that.setTables('opened', that.CurrentArea);
				that.CurrentTalbeType = 'opened';
			} else {
				that.setTables('free', that.CurrentArea);
				that.CurrentTalbeType = 'free';
			}
		});
	},

	//定时任务
	timeTask: function(){
		var that = this;
		var running  = true;
		setTimeout(function(){
			$.when(
				$.ajax({
					url: _config.interfaceUrl.ConsumInfo,
					global: false
				})
			).then(function(res){
				if(res.code === '0') {
					$('.custnum').text(res.data.custnum);
					$('.dueamount').text(res.data.dueamount);
					$('.orderCount').text(res.data.orderCount);
					$('.ssamount').text(res.data.ssamount);
					$('.totalAmount').text(res.data.totalAmount);
					that.setTables();
				} else {
					widget.modal.alert({
						cls: 'fade in',
						content:'<strong>' + (res.msg === '' ? '接口错误' : res.msg) + '</strong>',
						width:500,
						height:500,
						btnOkTxt: '',
						btnCancelTxt: '确定'
					});
				}
			});

			if (running){
				setTimeout(arguments.callee, 5000);
			}

		}, 50);
	},

	initTakeOutModal: function(){
		var $modal = $('#J-takeout-dialog');
		var ret = [];
		var retNormal = [];
		$.each(tackOUttable, function(k, v){
			if(v.tabletype == '3') {
				ret.push('<li>' + v.tableNo + '</li>');
			} else {
				retNormal.push('<li>' + v.tableNo + '</li>');
			}
		});
		$(".take-out-list").html(ret.join(''));
		$(".take-out-list").find('li').eq(0).addClass('active');
		$(".take-out-list-normal").html(retNormal.join(''));

		if(ret.length === 0 && retNormal.length === 0){
			widget.modal.alert({
				content:'<strong>获取外卖餐台接口错误</strong>',
				btnOkTxt: '',
				btnCancelTxt: '确定'
			});
			return false;
		}

		$(".take-out-list li").unbind("click").on("click",  function(){
			$(".take-out-list li").removeClass("active");
			$(this).addClass("active");
		});
		$modal.modal("show");
	},

	/**
	 * 外卖开台
	 * @param type 0:普通外卖 1:咖啡外卖
	 */
	setTakeOutOrder: function(type){
		var tableNo = '';
		if(type === 0) {
			tableNo = $('.take-out-list-normal').find('li').eq(0).text();
		} else {
			tableNo = $('.take-out-list').find('li.active').text();
		}

		$.ajax({
			url: _config.interfaceUrl.OpenTable,
			method: 'POST',
			contentType: "application/json",
			data: JSON.stringify({
				tableNo: tableNo,
				ageperiod: '',
				username: utils.storage.getter('aUserid'),
				manNum: 0,
				womanNum: 0
			}),
			dataType:'json'
		}).then(function(res){
			if(res.code === '0') {
				$.ajax({
					url: _config.interfaceUrl.SetOrderTakeout + res.data.orderid + '/',
					method: 'GET',
					dataType:'text'
				}).then(function(data){
					var  data=JSON.parse(data.substring(12, data.length - 3));
					if(data.Data === '1') {
						var url = "../views/orderdish.jsp?orderid=" + res.data.orderid + '&personnum=0&tableno=' + tableNo + '&type=out';
						window.location.href = encodeURI(encodeURI(url));
					} else {
						widget.modal.alert({
							content:'<strong>设置为外卖接口错误</strong>',
							btnOkTxt: '',
							btnCancelTxt: '确定'
						});
					}
				})
			} else {
				widget.modal.alert({
					content:'<strong>' + res.msg + '</strong>',
					btnOkTxt: '',
					btnCancelTxt: '确定'
				});
			}
		})

	},

	//验证用户
	verifyUser: function(serverName, cb){
		$.ajax({
			url: _config.interfaceUrl.VerifyUser,
			method: 'POST',
			contentType: "application/json",
			data: JSON.stringify({
				loginType: '030101',
				username: serverName
			}),
			dataType:'json',
			success: function(res){
				if(res.code === '0') {
					var alertModal = widget.modal.alert({
						cls: 'fade in',
						content:'<strong>' + '确认开台' + '</strong>',
						width:500,
						height:500,
						btnOkCb: function(){
							alertModal.close();
							cb && cb();
						}
					});
				} else {
					widget.modal.alert({
						cls: 'fade in',
						content:'<strong>' + res.msg + '</strong>',
						width:500,
						height:500,
						btnOkTxt: '',
						btnCancelTxt: '确定'
					});
				}
			}
		})
	},

	/**
	 * 设置餐桌
	 * @param type [opened, free, all]
	 * @param areaid
	 */
	setTables: function(type,areaid){

		var type = type || this.CurrentTalbeType;
		var areaid = areaid || this.CurrentArea;
		var isYesterdayEndWork = utils.storage.getter('isYesterdayEndWork') === '1' ? false : true;

		function _getTablesArr(res){

			var isOpend = false;
			var tablesAll = [];
			var tablesFree = [];
			var tablesOpened = [];
			var time = '';

			$.each(res, function(key,val){
				var tmp = '';
				isOpend = val.status == '0' ? false : true;
				if(areaid === val.areaid || areaid === '-1') {
					if(isOpend) {
						time = '';
						var hm = (new Date().getTime() - (new Date(val.begintime.replace(new RegExp("-","gm"),"/"))).getTime());//得到毫秒数
						//计算出小时数
						var leave1=hm%(24*3600*1000);    //计算天数后剩余的毫秒数
						var hours=Math.floor(leave1/(3600*1000));//计算相差分钟数
						var leave2=leave1%(3600*1000);       //计算小时数后剩余的毫秒数
						var minutes=Math.floor(leave2/(60*1000));
						if(hours <= 0 ) {
							time += '00:'
						} else if(hours > 10) {
							time += hours + ':'
						} else {
							time += '0' + hours + ':'
						}

						if(minutes <= 0) {
							time += '00'
						} else if(minutes > 10) {
							time += minutes
						} else {
							time += '0' + minutes
						}

						tmp = '<li class="opened" orderid="'+ val.orderid  + '" personNum="'+ val.custnum  + '" tableno="' + val.tableNo + '" areaid="' + val.areaid + '">'+ val.tableNo +
							'<div class="tb-info tb-status">￥' + (val.amount == undefined ? '0': parseFloat(val.amount).toFixed(2)) + '</div>' +
							'<div class="tb-info meal-time">' + time + '</div> ' +
							'<div class="tb-info tb-person">' + val.custnum + '/' + val.personNum + '</div>' +
							' </li>';

						tablesOpened.push(tmp);
					} else {
						tmp = '<li class="'+ (!isYesterdayEndWork && 'reserved') + '" orderid="'+ val.orderid  + '" personNum="'+ val.personNum  + '" tableno="' + val.tableNo + '" areaid="' + val.areaid + '">'+ val.tableNo +
							'<div class="tb-info tb-person">' + val.personNum + '人桌</div>' +
							' </li>'
						tablesFree.push(tmp);
					}
					tablesAll.push(tmp);
				}
			});

			return {
				all : tablesAll,
				free : tablesFree,
				opened : tablesOpened
			}
		}

		$.ajax({
			url: _config.interfaceUrl.GetTableInfoByTableType,
			method: 'POST',
			contentType: "application/json",
			global: false,
			/*data: JSON.stringify({
			 tableType: [0,1]
			 }),*/
			dataType:'json',
			success: function(res){
				var allTables=[]//全部餐台（不包括外卖和咖啡外卖台）
				tackOUttable=[]//全部外卖和咖啡外卖台
				if(res.code=='1'){
					utils.printError.alert('餐台'+res.msg);
					return false
				}
				$.each(res.data, function(key,val){
					$.each(val.tables, function(k,v){
						if(v.tabletype=='2'){//过滤外卖咖啡外卖
							tackOUttable.push(v)
						}
						else if(v.tabletype=='3'){//过滤外卖咖啡外卖
							tackOUttable.push(v)
						}
						else {
							allTables.push(v)
						}
					})
				})

				var tables = _getTablesArr(allTables);
				var navRoomTypesArr = [];
				var navRoomTypes = $('#nav-room-types');

				//设置餐桌统计
				$('.J-table-nums').find('.all .num').text(tables.all.length)
					.end().find('.free .num').text(tables.free.length)
					.end().find('.opened .num').text(tables.opened.length);

				//设置区域
				if(navRoomTypes.attr('inited') !== 'true'){
					navRoomTypesArr.push('<li class="active" areaid="-1">全部</li>')
					$.each(res.data, function(key, val){
						navRoomTypesArr.push('<li areaid="' + val.areaid  + '">' + val.areaname  + '</li>');
					});
					navRoomTypes.attr('inited', 'true');
					navRoomTypes.html(utils.array.unique(navRoomTypesArr).join(''));
				}

				if(tables[type].length == 0) {
					$('#J-table-pager').html('');
					$("#standard-tables").html('');
				} else {
					//初始化分页
					$('#J-table-pager').pagination({
						dataSource: tables[type],
						pageSize: 40,
						showPageNumbers: false,
						showNavigator: true,
						callback: function(data) {
							$("#standard-tables").html(data.join(''));
						}
					});
				}

			}
		})
	},
	/**
	 * 清机
	 */
	clearAll:function () {
		$("#J-btn-checkout-dialog").modal('hide')
		widget.modal.alert({
			cls: 'fade in',
			content:'<strong>清机中，请稍后</strong>',
			width:500,
			height:500,
			hasBtns:false,
		});
		$.ajax({
			url: _config.interfaceUrl.Clearner+''+utils.storage.getter('aUserid')+'/'+utils.storage.getter('fullname')+'/'+utils.storage.getter('ipaddress')+'/'+utils.storage.getter('posid')+'/'+utils.storage.getter('fullname')+'/',
			type: "get",
			dataType: "text",
			success: function (data) {
				$(".modal-alert:last,.modal-backdrop:last").remove();
			var	data=JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
				if(data.Data === '0') {//清机失败
					widget.modal.alert({
						cls: 'fade in',
						content:'<strong>' + data.Info + '</strong>',
						width:500,
						height:500,
						btnOkTxt: '确定',
						btnCancelTxt: ''
					});
				}
				else {//清机成功
					utils.reprintClear.get()//打印清机单
					window.location = "../views/login.jsp";
				}
			},
			error:function () {
				alert(1111)
			}
		});
	},
	/*结业清机*/
	clearAllcheckOut:function () {
		var that=this
		$("#J-btn-checkout-dialog").modal('hide')
		var that=this;
		widget.modal.alert({
			cls: 'fade in',
			content:'<strong>清机中，请稍后</strong>',
			width:500,
			height:500,
			hasBtns:false,
		});
		$.ajax({
			url: _config.interfaceUrl.Clearner+''+$.trim($('#user').val())+'/'+utils.storage.getter('checkout_fullname')+'/'+utils.storage.getter('ipaddress')+'/'+utils.storage.getter('posid')+'/'+utils.storage.getter('checkout_fullname')+'/',
			type: "get",
			dataType: "text",
			success: function (data) {
				var	data=JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
				if(data.Data === '0') {//清机失败
					$(".modal-alert:last,.modal-backdrop:last").remove();
					widget.modal.alert({
						cls: 'fade in',
						content:'<strong>' + data.Info + '</strong>',
						width:500,
						height:500,
						btnOkTxt: '确定',
						btnCancelTxt: ''
					});
				}
				else {//清机成功
					utils.reprintClear.get()//打印清机单
					$(".modal-alert:last,.modal-backdrop:last").remove();
					that.checkout()
				}
			}
		});
	},
	checkout:function () {
		var that=this;
		var Uncleandata=that.getFindUncleanPosList();
		var arrylength=Uncleandata.LocalArry.length-1;
		var LocalArry=Uncleandata.LocalArry;
		if(Uncleandata.LocalArry.length>0){
			$("#J-btn-checkout-dialog").load("../views/check/impower.jsp",{'title':'清机授权','userNmae':Uncleandata.LocalArry[arrylength].username,'usernameDisble':'2','cbd':'MainPage.clearAllcheckOut()','userRightNo':'030204'});
			$("#J-btn-checkout-dialog").modal('show')
		}
		if(Uncleandata.LocalArry.length==0&&Uncleandata.OtherArry.length>0){
			widget.modal.alert({
				cls: 'fade in',
				content:'<strong>还有其他POS机未清机,<br><br>请到其他POS机上先清机</strong>',
				width:500,
				height:500,
				btnOkTxt: '重试',
				btnCancelTxt: '',
				btnOkCb:function () {
					$(".modal-alert:last,.modal-backdrop:last").remove();
					that.checkout()
				}
			});
		}
		if(Uncleandata.findUncleanPosList.detail.length=='0'){
			$("#J-btn-checkout-dialog").load("../views/check/impower.jsp",{'title':'结业授权','cbd':'MainPage.checkoutCallback()','userRightNo':'030205'});
			$("#J-btn-checkout-dialog").modal('show')
		}


	},
	checkoutCallback:function () {//结业回调
		$.ajax({
			url: _config.interfaceUrl.EndWork,//不需要传递参数
			type: "get",
			dataType:'text',
			success: function (data) {
				$("#J-btn-checkout-dialog").modal('hide')
				var  data=JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
				if(data.Data=='1'){
					/*$.ajax({
					 url: _config.interfaceUrl.EndWorkSyncData,//结业数据上传
					 method: 'GET',
					 contentType: "application/json",
					 dataType: 'json',
					 data:{
					 'synkey':'candaosynkey'
					 },
					 success: function (data) {
					 alert("2222")
					 console.log(data)
					 },
					 error:function (data) {
					 console.log(data)
					 alert("1111")
					 }
					 });*/
					widget.modal.alert({
						cls: 'fade in',
						content:'<strong>'+data.Info+',即将退出程序</strong>',
						width:500,
						height:500,
						btnOkTxt: '确定',
						btnCancelTxt: '',
						btnOkCb:function () {
							$(".modal-alert:last,.modal-backdrop:last").remove();
							window.location = '../views/openpage.jsp?ipaddress='+utils.storage.getter('ipaddress')+'&posid='+utils.storage.getter('posid');
							utils.clearLocalStorage.clearSelect();
						}
					});
				}
				else {
					widget.modal.alert({
						cls: 'fade in',
						content:'<strong>'+data.Info+'</strong>',
						width:500,
						height:500,
						btnOkTxt: '确定',
						btnCancelTxt: ''
					});
				}
			}
		});
	},
	getFindUncleanPosList:function () {//获取未清机数据列表
		var findUncleanPosList ,LocalArry=[],OtherArry=[]
		$.ajax({
			url: _config.interfaceUrl.GetAllUnclearnPosInfoes,
			type: "get",
			async:false,
			dataType: "text",
			success: function (data) {
				findUncleanPosList=JSON.parse(data)
				/*console.log(findUncleanPosList.detail)
				 console.log(findUncleanPosList.result)*/
				if(findUncleanPosList.result==='0'){
					LocalArry=[];//本机数组集合
					OtherArry=[];//其他pos登录集合
					for(var i=0;i<findUncleanPosList.detail.length;i++){
						if(findUncleanPosList.detail[i].ipaddress==utils.storage.getter('ipaddress')){
							LocalArry.push(findUncleanPosList.detail[i])
						}
						else {
							OtherArry.push(findUncleanPosList.detail[i])
						}
					}
				}
			}
		});
		return{
			findUncleanPosList:findUncleanPosList,
			LocalArry:LocalArry,
			OtherArry:OtherArry,
		}
	}
};

$(function(){
	MainPage.init();
});