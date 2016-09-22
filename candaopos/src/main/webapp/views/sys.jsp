<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1, user-scalable=no, minimum-scale=1.0,maximum-scale=1.0" />
<!-- 让 IE 浏览器运行最新的渲染模式下 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 让部分国产浏览器默认采用高速模式渲染页面 -->
<meta name="renderer" content="webkit">
<title>订单</title>
<link rel="stylesheet" href="../css/sys.css">
	<link rel="stylesheet" href="../lib/pagination.css">
	<script>
		$(function(){
			var $tabBox = $('.tab-box');
			$('.J-g-menu li').click(function(){
				var me = $(this),
						idx = me.index();
				me.addClass('active').siblings().removeClass('active');
				$tabBox.find('.tab-item').hide().eq(idx).show();
			})
		})
	</script>
</head>
<body>
	<div class="modal-dialog main-modal-dialog" style="height: 600px;" id="sys-modal"
		data-backdrop="static" >
		<div class="modal-content">
			<div class="modal-body">
				<header>
					<div class="fl">餐道</div>
					<div class="fr close-win" data-dismiss="modal">返回</div>
				</header>
				<article style="height: 540px;">
					<div class="content">
						<div class="g-l J-g-menu">
							<ul>
								<li class="active">启动打印复写卡</li>
								<li>打印机列表<i>i</i></li>
								<li>钱箱密码验证</li>
							</ul>
						</div>
						<div class="g-r">
							<div class="g-r-content tab-box">
								<%--启动打印复写--%>
								<div class="tab-item">
									<div class="item-card">
										<h3>启动打印复写</h3>
										<p>打开此功能后，注册会员、会员结账、充值时可以打印会员信息到复写卡上。</p>

										<div class="switch">
											<input type="checkbox" checked="checked">
											<label><i></i></label>
										</div>
									</div>
								</div>

								<%--打印机列表--%>
								<div class="tab-item" id="printList"style="display: none">
									<table class="table table-bordered table-hover table-list" style="background: #fff">
										<thead>
											<tr>
												<th></th>
												<th>打印机IP</th>
												<th>打印机名称</th>
												<th>打印机状态</th>
											</tr>
										</thead>
										<tbody>

										</tbody>
									</table>
								</div>

								<%--打印机列表--%>
								<div class="tab-item" style="display: none">
									<div class="item-card">
										<h3>开启钱箱密码验证</h3>
										<p>启用该功能后，点击“开钱箱”按钮后需要输入密码验证方可打开钱箱</p>

										<div class="switch">
											<input type="checkbox" checked="checked">
											<label><i></i></label>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</article>
			</div>
		</div>
	</div>
<script>
	$(function () {
		getItemSellDetail()
	})
	function getItemSellDetail() {//获取打印机列表
		$.ajax({
			url:'/newspicyway/pos/printerlist.json',
			type: "get",
			dataType: "json",
			success: function (data) {
				var str="",num=0;
				for( var i=0;i<data.data.length;i++) {
				    num=i+1
					str+='<tr>';
					str+='   <td width="200">'+num+'</td>';
					str+='   <td width="276">'+data.data[i].ip+'</td>';
					str+='   <td width="200">'+data.data[i].name+'</td>';
					str+='   <td width="200">'+data.data[i].statusTitle+'</td>';
					str+='</tr>';

				};
				$("#printList tbody").html(str);
			},
		});
	}
</script>
</body>
</html>