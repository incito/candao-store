<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport"
          content="width=device-width,initial-scale=1, user-scalable=no, minimum-scale=1.0,maximum-scale=1.0"/>
    <!-- 让 IE 浏览器运行最新的渲染模式下 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 让部分国产浏览器默认采用高速模式渲染页面 -->
    <meta name="renderer" content="webkit">
    <title>雅座会员激活</title>
    <link rel="stylesheet" href="../../tools/bootstrap-3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../css/common.css">
    <link rel="stylesheet" href="../../css/main.css">
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="../../scripts/jquery-3.1.0.min.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="../../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
    <script src="../../scripts/common.js"></script>
    <link rel="stylesheet" href="../../css/member.css">
</head>
<body>
<header>
    <div class="fl logo">餐道</div>
    <div class="fr" onclick="goBack();">返回</div>
</header>
<article>
    <div class="member-view" style="width: 740px;margin:0 auto;">
        <div style="margin-top:20px"></div>
        <div class="row">
            <div class="col-md-7">
                <div class="form-group form-group-base f-oh" style="margin-bottom: 10px">
                    <span class="form-label f-fl">会员卡号:</span>
                    <input value=""  validType='noPecial2' maxlength="20"  type="text" class="form-control f-fl ya_Register" autocomplete="off">
                </div>
                <div class="form-group form-group-base" style="margin-bottom: 10px;">
                    <span class="form-label">会员密码:</span>
                    <input value=""  type="password" maxlength="6" class="form-control ya_pad" autocomplete="off">
                </div>
                <div class="form-group form-group-base" style="margin-bottom: 10px;">
                    <span class="form-label">手机号码:</span>
                    <input value=""   type="text" class="form-control ya_phone" maxlength="11" autocomplete="off">
                </div>

                <div class="form-group form-group-base ya_btn" style="margin-top: 10px">
                    <button class="btn-default btn-lg btn-base btn-base-flex2" onclick="goBack();">取消</button>
                    <button class="btn-default btn-lg btn-base btn-base-flex2 btnOk disabled" disabled="disabled">确定
                    </button>
                </div>
            </div>
            <div class="col-md-5">
                <div class="virtual-keyboard-base" style="position: absolute;top:0;right:10px;">
                    <ul>
                        <li>1</li>
                        <li>2</li>
                        <li>3</li>
                    </ul>
                    <ul>
                        <li>4</li>
                        <li>5</li>
                        <li>6</li>
                    </ul>
                    <ul>
                        <li>7</li>
                        <li>8</li>
                        <li>9</li>
                    </ul>
                    <ul>
                        <li>.</li>
                        <li>0</li>
                        <li>←</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</article>
<footer>
    <div class="info J-sys-info"><span>店铺编号：</span><span class="branch-num">- -</span><span>&nbsp;登录员工：</span><span>&nbsp;<span
            class="user-info">- -</span></span><span>&nbsp;当前时间：</span><span
            class="time">- -</span><span>&nbsp;版本号：</span><span>1.01</span></div>
</footer>
<script type="text/javascript" src="../../scripts/member.js"></script>
<script>
    widget.keyboard({
        target: '.virtual-keyboard-base'
    });
    $('.ya_pad,.ya_Register,.ya_phone').on('input propertychange', function(){
        var  ya_pad=$('.ya_pad').val(),
             ya_Register=$('.ya_Register').val(),
             ya_phone=$('.ya_phone').val();
        if (ya_pad != '' && ya_Register!='' && ya_phone!='') {
            $('.ya_btn .btnOk').removeClass('disabled').attr('disabled', false);
        }
        else {
            $('.ya_btn .btnOk').addClass('disabled').attr('disabled', true);
        }
    });
    $('.ya_btn .btnOk ').click(function () {
        ya_Member.ya_Register()
    })
</script>
</body>
</html>