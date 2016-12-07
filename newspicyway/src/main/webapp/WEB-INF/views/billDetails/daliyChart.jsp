<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>营业数据明细报表</title>
<link rel="shortcut icon" href="../asset/ico/favicon.png">
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/font-awesome.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/report.css" />
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/carousel.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/echartsHome.css"
	rel="stylesheet">
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
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid example">
			<!--/span-->
			<div id="daliy_yingye_graphic" class="col-md-14">
				<div id="daliy_yingye_main" class="main" style="width: 100%"></div>
			</div>
			<!--/span-->
		</div>
		<!--/row-->
		<div class="report_data_div">
			<table class="table table-list" id="income_tb">
				<thead>
					<tr>
						<th class="count">应收总额</th>
						<th class="code">实收总额</th>
						<th class="name">折扣总额</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	
	<div class="container-fluid">
		<div class="row-fluid example">
			<!--/span-->
			<div id="daliy_shishou_graphic" class="col-md-14">
				<div id="daliy_shishou_main" class="main" style="width: 100%"></div>
			</div>
			<!--/span-->
		</div>
		<!--/row-->
		<div class="report_data_div" style="overflow-x: auto;">
			<table class="table table-list" id="paidIn_tb">
				<thead>
					<tr id="settlementDesc">
						<!-- <th width="14%">现金</th>
						<th width="14%">挂账</th>
						<th width="14%">微信</th>
						<th width="14%">支付宝</th>
						<th width="14%">刷工行卡</th>
						<th width="14%">刷他行卡</th>
						<th width="15%">会员储值消费净值</th> -->
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row-fluid example">
			<!--/span-->
			<div id="daliy_zhekou_graphic" class="col-md-14">
				<div id="daliy_zhekou_main" class="main" style="width: 100%"></div>
			</div>
			<!--/span-->
		</div>
		<!--/row-->
		<div class="report_data_div">
			<table class="table table-list" id="discount_tb">
				<thead>
					<tr>
						<th class="count">优免</th>
						<th class="code">会员积分消费</th>
						<th class="name">会员券消费</th>
						<th class="name">会员优惠</th>
						<th class="name">抹零</th>
						<th class="name">四舍五入</th>
						<th class="name">赠送金额</th>
						<th class="name">会员储值消费虚增</th>
						<th class="name">套餐优惠</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<div class="report_data_div">
			<h4>营业数据统计</h4>
			<p style="font-size: 14px; margin-left: 10px;">堂食</p>
			<table class="table table-list" id="business_data_tb">
				<thead>
					<tr>
						<th class="count">桌数</th>
						<th class="code">桌均消费</th>
						<th class="name">结算人数</th>
						<th class="name">应收人均</th>
						<th class="name">实收人均</th>
						<th class="name">翻座率(%)</th>
						<th class="name">翻台率(%)</th>
						<th class="name">平均消费时间</th>
						<th class="name">堂食应收</th>
						<th class="name">服务费</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<p style="font-size: 14px; margin: 10px 10px;">外卖</p>
			<table class="table table-list" id="takeout_data_tb">
				<thead>
					<tr>
						<th>外卖应收</th>
						<th>外卖实收</th>
						<th class="name">外卖单数</th>
						<th class="name">外卖单均</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<div class="bottom-div">
			<div class="report_data_div">
				<h4>会员数据统计</h4>
				<table class="table table-list" id="meber_data_tb">
					<thead>
						<tr>
							<th class="count">会员消费笔数</th>
							<th class="count">会员消费占比(%)</th>
							<th class="count">会员券消费</th>
							<th class="code">会员积分消费</th>
							<th class="name">会员储值消费净值</th>
							<th class="name">会员储值消费虚增</th>
							<th class="name">合计</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="report_data_div">
				<h4>账单信息统计</h4>
				<table class="table table-list" id="bill_data_tb">
					<thead>
						<tr>
							<th nowrap="nowrap">已结账单</th>
							<th nowrap="nowrap">已结账单应收</th>
							<th nowrap="nowrap">已结人数</th>
							<th nowrap="nowrap">未结账单数</th>
							<th nowrap="nowrap">未结账单应收</th>
							<th nowrap="nowrap">未结人数</th>
							<th nowrap="nowrap">全部账单数</th>
							<th nowrap="nowrap">全部账单应收</th>
							<th nowrap="nowrap">全部人数</th>
							<th nowrap="nowrap">在台数</th>
							<th nowrap="nowrap">开台数</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<script src="<%=request.getContextPath()%>/scripts/jquery.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/scripts/projectJs/reportChart.js"></script>
	<script
		src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
	<script type="text/javascript">
	$(function(){
		initDaliyData();
	});
</script>
</body>
</html>
