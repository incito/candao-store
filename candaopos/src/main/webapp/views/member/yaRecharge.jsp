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
    <title>雅座会员储值</title>
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
<style>
    .pay-type-select {
         position:initial;
        left: 0;
        top:0;
        width: 100%;
        height: 48px;
        padding: 8px 0;
        background: #fff;
        margin: 10px 0;
    }
</style>
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
                    <input value="" name="phone"  type="text" class="form-control f-fl ya_memberNumber" autocomplete="off"
                           style="width: 320px; float: left; margin-right: 20px;">
                    <button class="btn-default btn-lg btn-base btn-yellow f-fl btn-sendMsg ya-btn-query">查询</button>
                </div>
                <div class="row ya_cardInfo" style="">
                    <div class="col-md-6">储值余额∶<span class="ya_balance">111111</span></div>
                    <div class="col-md-6">积分余额∶<span class="ya_point">111111</span></div>
                </div>
                <div class="form-group form-group-base" style="margin-bottom: 10px;">
                    <span class="form-label">储值金额:</span>
                    <input value="" disabled="disabled" name="phone" type="text" class="form-control ya-Stored-value"
                           autocomplete="off">
                </div>
                <div class="pay-type-select">
                    <ul class="cnt">
                        <li class="active" ChargeType="0">现金</li>
                        <li ChargeType="1">银行卡</li>
                    </ul>
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
    //console.log(ya_Member.ya_formatDate(new Date(), "yyyy-MM-dd"))
    //console.log(ya_Member.ya_formatDate(new Date(), "HH:mm:ss"))
    widget.keyboard({
        target: '.virtual-keyboard-base'
    });
    $('.pay-type-select li').click(function () {
        $(this).addClass('active').siblings('li').removeClass('active');
    });
    $('.ya-Stored-value').on('input propertychange', function(){
        if ($(this).val() != '') {
            $('.ya_btn .btnOk').removeClass('disabled').attr('disabled', false);
        }
        else {
            $('.ya_btn .btnOk').addClass('disabled').attr('disabled', true);
        }
    });
    $('.ya_btn .btnOk ').click(function () {
        ya_Member.ya_rechargeSave()
    })
</script>
</body>
</html>