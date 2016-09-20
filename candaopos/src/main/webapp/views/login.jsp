<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1, user-scalable=no, minimum-scale=1.0,maximum-scale=1.0"/>
<!-- 让 IE 浏览器运行最新的渲染模式下 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 让部分国产浏览器默认采用高速模式渲染页面 -->
<meta name="renderer" content="webkit">
<title>登录</title>
<link rel="stylesheet" href="../tools/bootstrap-3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="../css/common.css">
<link rel="stylesheet" href="../css/login.css">
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="../scripts/jquery-3.1.0.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script src="../scripts/common.js"></script>
<script type="text/javascript">
var activeinputele;
	$(document).ready(function(){
		$("img.img-close").hover(function(){
		 	$(this).attr("src","../images/close-active.png");
		},function(){
			$(this).attr("src","../images/close-sm.png");
		});
		$(".virtual-keyboard ul li").click(function(e){
			var keytext = $(this).text();
			if(activeinputele != null && activeinputele != undefined){
				if(keytext == "←"){
					activeinputele.focus();
					backspace();
				}else if(keytext == "."){
					return false;
				}else{
					var val = activeinputele.val();
					val = val + keytext;
					activeinputele.val(val);
					activeinputele.focus();
				}
			}
			keyUp("#change_val");
		});
		$("input").focus(function(event){
	        activeinputele = $(this);
		});
		
		$("#submit_btn").click(function(e){
			$("#change_val").val("");
			$("#thechange-dialog").modal("show");
		});
		
		$("#ok_btn").click(function(e){
			$("#confirm-change-val").text($("#change_val").val());
			$("#confirm-dialog").modal("show");
		});
	});
	function keyUp(o){
		if($(o).val() != null && $(o).val() != ""){
			$("#ok_btn").attr("disabled", false);
		}else{
			$("#ok_btn").attr("disabled", true);
		}
	}
	function toMain(){
		window.location = "../views/main.jsp";
	}

$.ajax({
	url: '/newspicyway/bankinterface/getallbank.json',
	method: 'get',
	success: function(res){
		console.log(res);
	}

})

</script>
</head>
<body>
	<header>
		<div class="fl">餐道</div>
		<div class="fr">欢迎登录餐道POS收银系统</div>
	</header>
	<article>
		<div class="content">
			<div class="login-form">
				<form action="">
					<div class="form-group" style="margin-top: 8px;">
						<span class="span-user">用户:</span>
						<input id="user" value="" name="user" type="text" placeholder="" class="form-control x319 in" autocomplete="off" ><!-- readonly="readonly" -->
					</div>
					<div class="form-group">
						<span class="span-user">密码:</span>
						<input id="pwd" value="" name="pwd" type="password" class="form-control x319 in" autocomplete="off" ><!-- readonly="readonly" -->
					</div>
					<div class="form-group">
						<label for="j_remember" class="m">
						<input id="j_remember" type="checkbox" value="true">&nbsp;保存登录信息</label>
					</div>
					<div class="form-group space">
						<input type="button" value="&nbsp;取&nbsp;消&nbsp;" class="btn btn-default btn-lg">
						<button type="button"  id="submit_btn" class="btn btn-primary btn-lg">&nbsp;登&nbsp;录&nbsp </button>
					</div>
				</form>
			</div>
			<div class="virtual-keyboard">
				<ul>
					<li>1</li><li>2</li><li>3</li>
				</ul>
				<ul>
					<li>4</li><li>5</li><li>6</li>
				</ul>
				<ul>
					<li>7</li><li>8</li><li>9</li>
				</ul>
				<ul>
					<li>.</li><li>0</li><li>←</li>
				</ul>
			</div>
		</div>
	</article>
	<!-- 输入零找金 -->
	<div class="modal fade in thechange-dialog" id="thechange-dialog" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<div class="fl">餐道</div>
					<div class="fr">零找金输入</div>
				</div>
				<div class="modal-body" style="padding-top: 0px;">
					<div class="login-form">
						<form action="">
							<div class="form-group" style="margin-top: 8px;">
								<span class="span-user">零找金金额:</span>
								<input id="change_val" value="" name="change_val" onkeyup="this.value= this.value.match(/\d+/) ? this.value.match(/\d+/) : ''; keyUp(this)" type="text" class="form-control x319 in" autocomplete="off" ><!-- readonly="readonly" -->
							</div>
							<div class="form-group space" style="margin-top: 138px;">
								<input type="button" value="&nbsp;取&nbsp;消&nbsp;" data-dismiss="modal" class="btn btn-default btn-lg">
								<button type="button"  id="ok_btn" class="btn btn-primary btn-lg" disabled="disabled">&nbsp;确&nbsp;定&nbsp; </button>
							</div>
						</form>
					</div>
					<div class="virtual-keyboard">
						<ul>
							<li>1</li><li>2</li><li>3</li>
						</ul>
						<ul>
							<li>4</li><li>5</li><li>6</li>
						</ul>
						<ul>
							<li>7</li><li>8</li><li>9</li>
						</ul>
						<ul>
							<li>.</li><li>0</li><li>←</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--确认零找金弹出框  -->
	<div class="modal fade dialog-sm confirm-dialog in " id="confirm-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title"></div>
	                <img src="../images/close-sm.png" class="img-close" data-dismiss="modal">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	                    <p class="p1">确定输入零找金：<span id="confirm-change-val"></span>
	                    </p>
	                </div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消
	                    </button>
	                    <button class="btn btn-save in-btn135" id="" type="button" onclick="toMain()">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>