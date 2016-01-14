<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>优惠活动统计</title>
<link rel="shortcut icon" href="../asset/ico/favicon.png">
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/font-awesome.min.css"
	rel="stylesheet">
<!--link
	href="<%=request.getContextPath()%>/tools/echarts/css/bootstrap.css"
	rel="stylesheet"-->
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/carousel.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/echartsHome.css"
	rel="stylesheet">
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<script src="<%=request.getContextPath()%>/tools/echarts/js/echarts.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/echarts/js/codemirror.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/echarts/js/javascript.js"></script>
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/codemirror.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/tools/echarts/css/monokai.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/prefreport.css"
	rel="stylesheet">
<!-- Fixed navbar -->
</head>
<body>
	<div class="report_head">
		<button class="btn btn-default floatleft active" onclick="changeActivy(0);">活动名称</button>
		<button class="btn btn-default floatleft" onclick="changeActivy(1);">活动类别</button>
		<button class="btn btn-default" id="to_detail">查看明细</button>
	</div>
	<div class="bottom-div">
		<div class="row-fluid example">
			<!--/span-->
			<div id="nums_graphic" class="col-md-14 graphic">
			<div class="p-main lf">
				<div id="nums_main" class="main"
					style='width: 100%; float: left; margin-right: 0; padding-right: 0; border-right-width: 0'>
				</div>
			</div>
			<div class="p-main rt">
				<div id="nums_trend_main" class="main"
					style='width: 100%;margin-left: 0; padding-left: 0; border-left-width: 0'>
				</div>
			</div>
			</div>
			<!--/span-->
			<div id="amount_graphic" class="col-md-14 graphic">
			<div class="p-main lf">
				<div id="amount_main" class="main"
					style='width: 100%;float: left; margin-right: 0; padding-right: 0; border-right-width: 0'>
				</div>
			</div>
			<div class="p-main rt">
				<div id="amount_trend_main" class="main"
					style='width: 100%;margin-left: 0; padding-left: 0; border-left-width: 0'>
				</div>
			</div>
			</div>
			<!--/span-->
			<div id="should_graphic" class="col-md-14 graphic">
			<div class="p-main lf">
				<div id="should_amount_main" class="main"
					style='width: 100%;float: left; margin-right: 0; padding-right: 0; border-right-width: 0'>
				</div>
			</div>
			<div class="p-main rt">
				<div id="should_amount_trend_main" class="main"
					style='width: 100%;margin-left: 0; padding-left: 0; border-left-width: 0'>
				</div>
			</div>
			</div>
			<!--/span-->
			<div id="paidin_graphic" class="col-md-14 graphic">
			<div class="p-main lf">
				<div id="paidin_amount_main" class="main"
					style='width: 100%;float: left; margin-right: 0; padding-right: 0; border-right-width: 0'>
				</div>
			</div>
			<div class="p-main rt">
				<div id="paidin_amount_trend_main" class="main"
					style='width: 100%;margin-left: 0; padding-left: 0; border-left-width: 0'>
				</div>
			</div>
			</div>
		</div>
		<!--/row-->
	</div>

	<script src="<%=request.getContextPath()%>/scripts/jquery.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/prefreport.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/zxx.color_exchange.js"></script>
	<script type="text/javascript">
		var type = "";
		$(function() {
			$(".report_head button").click(function() {
				$(this).parent().find("button").removeClass("active");
				$(this).addClass("active");
			});
			$(".report_head button").removeClass("active");
			if(activiyType == ""){
				activiyType = 0;
				$(".report_head button").eq(0).addClass("active");
			}else{
				if(activiyType == 0){
					$(".report_head button").eq(0).addClass("active");
				}else{
					$(".report_head button").eq(1).addClass("active");
				}
			}
			changeActivy(activiyType);
			initPreferentialData();
			$("#to_detail").click(function(){
				orgTime();
				toDetail(2,beginTime ,endTime);
			});
		});
	</script>
</body>
</html>
