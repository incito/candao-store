<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reckoning.css" />
<script type="text/javascript">
	function initReckonData(callback) {
		$.post(global_Path+"/dishtypeSettlement/queryStatement.json",{
			orderid: $("#orderid_param").val()
		},function(result){
			console.log(result);
			if(result.flag == 1){
				initReckonTb(result.data);
				callback();
			}else{
				alert(result.desc);
			}
		},'json');
		
	}
	function initReckonTb(data){
		if(data != null){
			$("#branchname-show").text(data.branchname);
			$("#orderid").html($("#orderid_param").val());
			$("#area").html(data.area);
			$("#tableno").html(data.tableno);
			$("#personnum").text(data.personnum);
			$("#waiter").html(data.waiter);
			$("#begintime").html(data.begintime);
			$("#endtime").html(data.endtime);

			var dishTr = '';
			//菜品
			if (data.dishes != null && data.dishes.length > 0) {
				$.each(data.dishes, function(i, dish) {
					dishTr += '<tr><td class="text">'
						+ dish.itemdesc + '</td><td>'
						+ dish.count + '</td><td>'
						+ dish.price
						+ '</td><td>'
						+ dish.amount + '</td></tr>';
				});
			}
			//结算方式
			if (data.coupons != null && data.coupons.length > 0) {
				dishTr += '<tr><td colspan="4"><hr class="dotted-line"/></td></tr>'
						+ '<tr><td>结算方式</td><td>金额</td><td colspan="2">备注</td></tr>';
				$.each(data.coupons, function(i, coupon) {
					dishTr += '<tr><td class="text">'
						+ coupon.name + '</td><td>' + coupon.amount
						+ '</td><td colspan="2">' + coupon.bankcardno
						+ '</td></tr>';
				});
			}
			
			dishTr += '<tr><td colspan="4"><hr class="dotted-line" /></td></tr>'
				+ '<tr><td class="text">结算备注：</td> <td colspan="3"></td></tr>'
				+ '<tr><td class="text">合计：</td> <td></td> <td class="right-align">'+data.totalconsumption+'</td><td></td></tr>';
			var colname = "抹零";
			if(data.payway == 20){
				colname = "四舍五入";
			}
			dishTr += '<tr><td class="text">'+colname+'：</td> <td></td> <td class="right-align">'+data.payamount+'</td><td></td></tr>'
				+ '<tr><td class="text">赠送金额：</td> <td></td> <td class="right-align">'+data.giveamount+'</td><td></td></tr>'
				+ '<tr><td class="text">总优惠：</td> <td></td> <td class="right-align">'+data.couponamount+'</td><td></td></tr>'
				+ '<tr><td class="text">实收：</td> <td></td> <td class="right-align">'+data.paidamount+'</td><td></td></tr>'
				+ '<tr><td class="text">开发票金额：</td> <td></td> <td class="right-align">'+data.invoiceamount+'</td><td></td></tr>';
					
			$("#dishes-tb tbody").html(dishTr);		
		}
	}
</script>
<div class="modal fade reckoning-dialog in " id="reckoning-dialog"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 500px;">
		<div class="modal-content">
			<div class="">
				<div class="modal-title">
					<img src="../images/close.png" class="img-close"
						data-dismiss="modal" />
				</div>
			</div>
			<div class="modal-body">
				<div>
					<div style="text-align: center;">
						<h3><span id="branchname-show"></span></h3>
						<h2>结账单</h2>
						<hr />
						<hr />
						<input type="hidden" id="orderid_param" value=""/>
					</div>
					<table class="table table-list reckon-tb order-head">
						<tr>
							<td width="80px">结账单号：</td>
							<td style="text-align: left;" colspan="3" id="orderid"></td>
						</tr>
						<tr>
							<td>厅：</td>
							<td width="120px" style="text-align: left;" id="area"></td>
							<td>桌号：</td>
							<td style="text-align: left;" id="tableno"></td>
						</tr>
						<tr>
							<td>人数:</td>
							<td width="120px" style="text-align: left;" id="personnum"></td>
							<td>服务员：</td>
							<td style="text-align: left;" id="waiter">1012316</td>
						</tr>
						<tr>
							<td>开始时间:</td>
							<td style="text-align: left;" colspan="3" id="begintime"></td>
						</tr>
						<tr>
							<td>结束时间:</td>
							<td style="text-align: left;" colspan="3" id="endtime"></td>
						</tr>
					</table>
					<hr class="dotted-line" />
					<table class="table table-list reckon-tb" id="dishes-tb">
						<thead>
							<tr>
								<th width="45%">品项</th>
								<th width="15%">数量</th>
								<th width="10%">单价</th>
								<th>金额</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="btn-operate btn-operate-dishes">
					<button class="btn btn-cancel in-btn135" type="button"
						data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
</div>
