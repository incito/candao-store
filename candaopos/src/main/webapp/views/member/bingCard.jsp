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
	<title>修改手机号</title>
</head>
<body>
<div class="modal-dialog" style="width: 400px;">
	<div class="modal-content">
		<div class="modal-header">
			<div class="fl">餐道</div>
		</div>
		<div class="modal-body">
			<div class="row" style="height: 260px;padding: 0 10px">
				<div class="bingTitle"></div>
				<div class="form-group form-group-nor inputCard" style="margin-top: 40px">
					<span class="form-label">会员实体卡:</span>
					<div class="form-group-info">
						<input value="" id="inputCard" placeholder="请刷会员实体卡" type="text" class="form-control">
					</div>
				</div>
				<div class="bingContent" style="height: 40px">

				</div>
				<div class="form-group form-group-base" id="permission" style="margin-top: 10px">
					<button class="btn-default btn-lg btn-base btn-base-flex2 btn_Cancel">取消</button>
					<button class="btn-default btn-lg btn-base btn-base-flex2 btnOk disabled" disabled="disabled">确定</button>
				</div>
			</div>

		</div>
	</div>
</div>
<style>
	input:focus{
		outline:none;
	}
</style>
<script>
	<%
       String title = request.getParameter("title");
       String clearType = request.getParameter("type");
       String cbd=request.getParameter("cbd");
    %>
	var bingtitle = "<%=title%>",
		bingtype = "<%=clearType%>",
		bingcallBackFun="<%=cbd%>",
	    inputEnd=null;
	    inputVal=null
	$('.bingTitle').text(bingtitle);
	setTimeout(function () {
		$('#inputCard').val('').focus()
	},400)
	$('#inputCard').on('input propertychange', function(){
		var val=$.trim($('#inputCard').val())
		$('#inputCard').attr('placeholder','请刷会员实体卡')
		inputVal=val;
	})
	inputEnd = setInterval(function(){
		var val=$.trim($('#inputCard').val());
		if(val==inputVal &&val !='') {
			Log.send(2, '查询会员卡是否被绑定参数为：'+JSON.stringify({
						url:  memberAddress.vipcandaourl + _config.interfaceUrl.VipCheckCard,
						cardno: val,
						branch_id: utils.storage.getter('branch_id'),
					}));
				$.ajax({
					url: memberAddress.vipcandaourl + _config.interfaceUrl.VipCheckCard,
					method: 'POST',
					contentType: "application/json",
					dataType: 'json',
					data: JSON.stringify({
						cardno: val,
						branch_id: utils.storage.getter('branch_id'),
					}),
					success: function (data) {
						Log.send(2, '查询会员卡是否被绑定成功返回参数：'+JSON.stringify(data));
						var html=null;
						if(data.code=='0'){
							if (bingtype=='1'){
								html='会员卡序列号：<span id="bingMemberCard">'+inputVal+'</span>，确认新增该实体会员卡吗？'
							}
							if(bingtype=='2'){
								var oldeCard=loadMember.result[0].MCard
								html='确定将会员卡号：<span id="bingMemberCard">'+oldeCard+'</span>，修改为：<span>'+inputVal+'</span>？'
							}
							if(bingtype=='3'){
								html='会员卡序列号：<span id="bingMemberCard">'+inputVal+'</span>，确认新增该实体会员卡吗？'
							}
							$('.btnOk').attr('disabled',false).removeClass('disabled');
							$('#inputCard').val('').attr('placeholder',inputVal)
						}
						else {
							html='<span style="color: red" >会员卡号：<span id="bingMemberCard">'+inputVal+'</span>已注册,请重新刷卡!</span>';
							$('#inputCard').val('')
						}

						$('.bingContent').html(html);
					}
				});
		}
	},1000);

	$('.btnOk').click(function () {
		eval(bingcallBackFun);
	})
	$('.btn_Cancel').click(function () {
		$('#modify-binding-dialog').modal('hide').html('')
	})
</script>
</body>
</html>