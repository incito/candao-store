<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.candao.www.utils.SessionUtils"%>
<%@page import="com.candao.common.utils.PropertiesUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String isbranch = PropertiesUtils.getValue("isbranch");
	String host = PropertiesUtils.getValue("cloud.host");
	String webRoot = PropertiesUtils.getValue("cloud.webroot");
	String webPath = "http://" + host + "/"+ webRoot;
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>餐道后台管理平台</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/preferential.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery.mCustomScrollbar.css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/dishes.css">
	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/global.js"></script>
	<script type="text/javascript">
		var global_isbranch = '<%=isbranch %>';
		var global_cloudPath = '<%=webPath %>';
	</script>
</head>
<body style="overflow-y:hidden" onload="">
	<div class="ky-navbar ky-navbar-default">
		<div class="ky-navbar-header">
			<img src="../images/logo.png" alt="">
			<p>餐道后台管理平台</p>
		</div>
		<div class="ky-navbar-menu">
			<ul class="ky-nav ky-nav-pills" id="ul_left_menu">
			<c:forEach items="${menumap }" var="item">
				    <%-- <c:if test="${item.key  == '0101' }">
				<li>
					<a href="#" class="ky-menu-primary   " onclick="toShop()">门店管理</a>
				</li>
				 </c:if>
				    <c:if test="${item.key  == '0102'}">
				<li>
					<a href="#" class="ky-menu-info" onclick="todish()">菜品管理</a>
				</li>
				</c:if> --%>
			   <c:if test="${item.key  == '0304' || item.key=='020101'}">
				<li>
					<a href="#" class="ky-menu-info" onclick="tobranchdish()">菜品管理</a>
				</li>
				</c:if>
				 <c:if test="${item.key  == '0306' || item.key=='020101'}">
				<li class="ky-dropdown" id="ct">
					<a href="#" class="ky-menu-warning" onclick="toTable()">餐台管理</a>
				</li>
				</c:if>
				  <c:if test="${item.key  == '0305' || item.key=='020101'}">
				<li>
					<a href="#" class="ky-menu-danger" onclick="toPrinterManager()">打印管理</a>
				</li>
				 </c:if>
				  <%-- <c:if test="${item.key  == '0103'}">
				<li>
					<a href="#" class="ky-menu-minor" onclick="toPreferterial()">优惠管理</a>
				</li>
				</c:if> --%>
			 <c:if test="${item.key  == '0308' || item.key=='020101'}">
				<li>
					<a href="#" class="ky-menu-minor" onclick="toPreferterial()">优惠活动</a>
				</li>
				</c:if>
			  <c:if test="${item.key  == '0308' || item.key=='020101'}">
				<li  class="ky-dropdown" id="bb">
					<a href="#" class="ky-menu-success ky-menu-report" onclick="toAnalysis()">报表分析</a>
					 <ul class="ky-dropdown-menu ky-nav ky-nav-pills ky-dropdown-menu-report">
						<li><a href="#" class="ky-menu-success" onclick="toAnalysis()">营业分析</a></li>
						<li><a href="#" class="ky-menu-success sub_bussi_menu" onclick="toSaleRept()">营业数据明细表</a></li>
						<li><a href="#" class="ky-menu-success" onclick="toSettleDetailRept()">结算方式明细表</a></li>
						<li><a href="#" class="ky-menu-success sub_item_menu" onclick="toDishSaleRept()">品项销售明细表</a></li>
						<li><a href="#" class="ky-menu-success sub_coup_menu" onclick="toCouponsRept()">优惠活动明细表</a></li>
						<li><a href="#" class="ky-menu-success" onclick="toAskedForARefund()">退菜明细表</a></li>
						<li><a href="#" class="ky-menu-success" onclick="toDataStatistics()">详细数据统计表</a></li>
					</ul>
				</li>
				</c:if>
				<%-- <c:if test="${item.key  == '0106'}">
 					<li> 
 						<a href="#" class="ky-menu-tenant" onclick="toTenant()">租户管理</a> 
 					</li>
 				</c:if>
             	<c:if test="${item.key  == '0105'}">
					<li>
						<a href="#" class="ky-menu-tenant" onclick="toNormal()">账户管理</a>
					</li>
				</c:if> --%>
			
				<c:if test="${item.key  == '0309' || item.key=='020101'}">
					<li>
 						<a href="#" class="ky-menu-employees" onclick="toEmployees()">员工管理</a>
 					</li> 
				</c:if>
			   	<c:if test="${item.key  == '0311' || item.key=='020101'}">
					<li>
						<a href="#" class="ky-menu-setup" onclick="toSetup()">系统设置</a>
					</li>
				</c:if>
				<%-- <c:if test="${item.key  == '0311' || item.key=='020101'}">
				<li  class="ky-dropdown" id="psi">
					<a href="#" class="ky-menu-psi ky-menu-erp" onclick="toBasic()">进销存管理</a>
					 <ul class="ky-dropdown-menu ky-nav ky-nav-pills ky-dropdown-menu-erp">
						<li><a href="#" class="ky-menu-psi ky-menu-erp" onclick="toBasic()">基础信息管理</a></li>
						<li><a href="#" class="ky-menu-psi ky-menu-erp sub_bussi_menu" onclick="toInStorage()">入库管理</a></li>
						<li><a href="#" class="ky-menu-psi ky-menu-erp" onclick="toOutStorage()">出库管理</a></li>
						<li><a href="#" class="ky-menu-psi ky-menu-erp sub_item_menu" onclick="toInventory()">库存一览</a></li>
						<li><a href="#" class="ky-menu-psi ky-menu-erp sub_coup_menu" onclick="toInventoryBill()">库存盘点</a></li>
						<li><a href="#" class="ky-menu-psi ky-menu-erp sub_coup_menu" onclick="toOrder()">销售管理</a></li>
						<li><a href="#" class="ky-menu-psi ky-menu-erp sub_coup_menu" onclick="toChart()">报表分析</a></li>
					</ul>
				</li>
				</c:if> --%>
			</c:forEach>	
			</ul>
		</div>
	</div>
	<div class="ky-container-iframe">
		<div class="ky-title">
			<p id="title_n" style="width:auto; float:left ; padding-left:20px;">${showName}</p>
			<p id="title_p">门店管理</p>
			<div class="ky-title-right">
				<form class="navbar-form navbar-left" role="search">
					<div class="form-group" id="allSearch" >		
						<div class="input-group">
						  <span class="input-group-addon" id="basic-addon1" style="cursor:pointer;"><i class="icon-search"></i></span>
						  <input type="text" class="form-control" placeholder="搜索" aria-describedby="basic-addon1" id="searchText">
						</div>
					</div>
				</form>
				<img src="<%=request.getContextPath()%>/images/user.png" onclick="toMyAccount();" alt="">
				<%-- <span class="ky-user">${currentUser.fullname}</span> --%>
 				 <span class="ky-user">${currentUser.name}</span> 
				<img src="<%=request.getContextPath()%>/images/logout.png" onclick="doExit()"/>
			</div>
		</div>
		<%--默认页面不跳转
		 <%=request.getContextPath()%>/shopMg
		--%>
		<iframe onload="" id="detail" src="" width="100%" frameBorder="0" scrolling="auto" height="95%" style="overflow-x: hidden;"> 
		</iframe>
		<!-- style="overflow-x:hidden; overflow-y:auto;" -->
		<div class="modal fade dialog-sm in " id="modal_confirmExit"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-body">
					<div class="dialog-sm-header">				  
				         <span class=" " style="color:black;">确认退出</span>
				        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
				    </div>
						<form action="" method="post" class="form-horizontal " name="">
							<div class="dialog-sm-info">
							<p class="p1" style="color:black;"><img src="<%=request.getContextPath()%>/images/del-tip.png">确认退出“餐道后台管理平台”吗?</p>
							</div>
							<div class="btn-operate  ">
								<button class="btn btn-cancel  " type="button" data-dismiss="modal"   >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save  " id="dishes-type-save" type="button" data-dismiss="modal"  onclick="confirmExit()">确认</button>
							</div>
						</form>
					</div>
					

				</div>
				
			</div>
		</div>
		
	</div>
</body>
<script>
// $(document.all("detail")).height(document.body.clientHeight-46);

/*************进销存 start*********************/

//基础信息
function toBasic(){
	$(parent.document.all("detail")).attr("src",global_Path+"/psi/basic/container");
	$("#allSearch").css("visibility","hidden");
}
function toCategory(){
	$(parent.document.all("detail")).attr("src",global_Path+"/psi/basic/category");
	$("#allSearch").css("visibility","hidden");
}
//入库
function toInStorage(){
	$(parent.document.all("detail")).attr("src",global_Path+"/psi/basic/inStorage");
	$("#allSearch").css("visibility","hidden");
}
//出库
function toOutStorage(){
	$(parent.document.all("detail")).attr("src",global_Path+"/psi/basic/outStorage");
	$("#allSearch").css("visibility","hidden");
}
//库存一览
function toInventory(){
	$(parent.document.all("detail")).attr("src",global_Path+"/psi/basic/inventory");
	$("#allSearch").css("visibility","hidden");
}
//库存盘点
function toInventoryBill(){
	$(parent.document.all("detail")).attr("src",global_Path+"/psi/basic/inventoryBill");
	$("#allSearch").css("visibility","hidden");
}
//销售管理
function toOrder(){
	$(parent.document.all("detail")).attr("src",global_Path+"/psi/basic/order");
	$("#allSearch").css("visibility","hidden");
}
//报表分析
function  toChart(){
	$(parent.document.all("detail")).attr("src",global_Path+"/psi/chart/main");
	$("#allSearch").css("visibility","hidden");
}
/*************进销存 end***********************/
//门店管理
function toShop(){
	$(parent.document.all("detail")).attr("src",global_Path+"/shopMg");
	$("#allSearch").css("visibility","hidden");
}
//餐台区域管理
function toTable(){
	$(parent.document.all("detail")).attr("src",global_Path+"/table/index");
	$("#allSearch").css("visibility","hidden");
}
//打印管理
function toPrinterManager(){
	$(parent.document.all("detail")).attr("src",global_Path+"/printerManager/index");
	$("#allSearch").css("visibility","hidden");
}
function todish(){
	$(parent.document.all("detail")).attr("src",global_Path+"/menu/menucontrol");
	$("#allSearch").css("visibility","hidden");
}
//门店菜谱
function tobranchdish(){
	$(parent.document.all("detail")).attr("src",global_Path+"/menu/branchDindex");
	$("#allSearch").css("visibility","hidden");
}

//退出系统
function doExit(){
	$("#modal_confirmExit").modal("show");
}
//确认退出
function confirmExit(){
	 window.location.href='<%=request.getContextPath()%>/login/logout';
}

$(document).ready(function() {
	
	//dialog中右上角关闭按钮，鼠标经过效果
	$("img.img-close").hover(function(){
	 	$(this).attr("src", "<%=request.getContextPath()%>/images/close-active.png");	 
	},function(){
		$(this).attr("src", "<%=request.getContextPath()%>/images/close-sm.png");
	});
	
	//门店管理 鼠标经过事件
	$('.ky-navbar-menu > .ky-nav > li > a.ky-menu-primary').css({
		'background-color' : '#7AC454',
		'color' : '#ffffff'
	});

	//一、二级菜单 点击事件
	$('.ky-nav li a').click(function() {
		var _this = $(this);
		
		//点击任意菜单，取消报表菜单的选中状态
		$('.ky-menu-success').removeClass("ky-menu-active");
		
		//点击任意菜单，取消进销存菜单的选中状态
		$('.ky-menu-psi').removeClass("ky-menu-erpactive");
		
		//改变文本
		$("#title_p").html(_this.html());
		//若是报表分析，显示第一个子菜单名称
		if(_this.parent().attr("id") == "bb"){
			$("#title_p").html($(".ky-dropdown-menu-report").find("li").eq(0).find("a").text());
		}
		
		//若是进销存，显示第一个子菜单名称
		if(_this.parent().attr("id") == "psi"){
			$("#title_p").html($(".ky-dropdown-menu-erp").find("li").eq(0).find("a").text());
		}
		
		//获取当前菜单对应的颜色代码
		var bgcolor = _this.css('border-left-color');
		
		//将其他菜单置为灰色
		$('.ky-nav li a').css({
			'background-color' : '#E8E7E4',
			'color' : '#282828'
		});
		//设置当前菜单的颜色
		_this.css({
			'background-color' : bgcolor,
			'color' : '#ffffff'
		});
		//将系统header设为当前菜单一致的颜色
		$('.ky-title').css('background-color', bgcolor);
		
		//判断是否有二级菜单
		if (_this.parent().hasClass('ky-dropdown')) {
			_this.next('ul').find('a').css({
				'background-color' : bgcolor,
				'color' : '#ffffff'
			});
		}
		
		//若点击的是二级菜单中一项
		if (_this.parent().parent().hasClass('ky-dropdown-menu')) {
			//将父菜单颜色设为对应颜色（未选中状态的颜色）
			_this.parent().parent().prev('a').css({
				'background-color' : bgcolor,
				'color' : '#ffffff'
			});
			//隐藏二级菜单
			_this.parent().parent('ul.ky-dropdown-menu').css('display', 'none');
		}
	});
//	餐台管理
//	bindEventForsecondMenu("ct");
	
	//报表管理
	bindEventForsecondMenu("bb");
	
	//进销存
	bindEventForsecondMenu("psi");
	
	$("#allSearch").css("visibility","hidden");
	
	//二级菜单鼠标经过事件
	$('.ky-dropdown-menu a').mouseover(function() {
		var bgcolor = $(this).css('border-left-color');
		//将父菜单颜色设为对应色（未选中状态的颜色）
		$(this).parent().parent().prev('a').css({
			'background-color' : bgcolor,
			'color' : '#ffffff'
		});
		//鼠标经过变颜色
		$(this).addClass("ky-menu-sub-hover");
	});
	//鼠标离开二级菜单，将父菜单置为灰色
	$('.ky-dropdown-menu a').mouseout(function() {
		//鼠标移开变颜色
		$(this).removeClass("ky-menu-sub-hover");
		//是否有被选中的报表菜单
		if($(this).parent().parent().parent().attr("id") == "bb"){
			if(!$(".ky-menu-success").hasClass("ky-menu-active")){
				$(this).parent().parent().prev('a').css({
					'background-color' : '#E8E7E4',
					'color' : '#282828'
				});
			}
		}
		if($(this).parent().parent().parent().attr("id") == "psi"){
			//是否有被选中的进销存菜单
			if(!$(".ky-menu-psi").hasClass("ky-menu-erpactive")){
				$(this).parent().parent().prev('a').css({
					'background-color' : '#E8E7E4',
					'color' : '#282828'
				});
			}
		}
	});
	//报表分析子菜单点击事件
	$('.ky-menu-success').click(function(){
		$('.ky-menu-success').removeClass("ky-menu-active");
		$(this).addClass("ky-menu-active");
	});
	
	//进销存子菜单点击事件
	$('.ky-menu-psi').click(function(){
		$('.ky-menu-psi').removeClass("ky-menu-erpactive");
		$(this).addClass("ky-menu-erpactive");
	});
	
	//搜索按钮点击
	$("#basic-addon1").click(function(){
		var text = $("#searchText").val();
		var iframes = $(parent.document.all("detail"));
		if(iframes.length>0){
			iframes[0].contentWindow.searchDataFromMain(text);
		}
	});
	//默认加载第一个左侧的菜单
	if($("#ul_left_menu").find("li").length!=0){ 
		$("#ul_left_menu").find("li").first().find("a").trigger("click");
	}else{
		toMyAccount();
		$(".ky-container-iframe").find(".ky-title p#title_p").html("我的账户");
	}
});

//报表分析一级菜单鼠标经过
function bindEventForsecondMenu(id){
	$('#'+id).mouseover(function() {
		$('#'+id+' > ul.ky-dropdown-menu').css('display', 'block');
	    var bgcolor = $(this).children('a').css('border-left-color');
		$(this).find('.ky-dropdown-menu a').css({
			'background-color' : bgcolor,
			'color' : '#ffffff'
		});
	});

	$('#'+id).mouseout(function() {
		$('.ky-dropdown > ul.ky-dropdown-menu').css('display', 'none');
	});
}
/**
* 调用子页面查询功能
*/
function searchText(){
	$(parent.document.all("detail")).window.queryData();
}
</script>
</html>