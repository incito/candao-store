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
    <link rel="stylesheet" href="../../tools/bootstrap-3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../css/common.css">
    <link rel="stylesheet" href="../../css/main.css">
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="../../scripts/jquery-3.1.0.min.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="../../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
    <script src="../../scripts/common.js"></script>
    <script src="../../lib/md5.js"></script>
    <link rel="stylesheet" href="../../css/check.css">
    <link rel="stylesheet" href="../../css/reporting.css">
    <link type="text/css" rel="stylesheet" href="../../lib/jedate/skin/jedate.css">

</head>
<body>
<style>

    .table>tbody>.tablistActive td {
        background: #FF5803;
        color: #fff;
    }
</style>
<header>
    <div class="fl">餐道</div>
    <div class="fr close-win" data-dismiss="modal" onclick="window.location.href='../main.jsp';">返回</div>
</header>
<article style="height: 540px;">
    <div class="content">
        <div class="g-r">
            <div class="g-a-content tab-box">
                <%--品项销售明细--%>
                <div class="tab-item">
                    <div class="check-key">
                        <div class="form-group form-group-base form-input">
                            <span class="form-label" style="line-height: 40px">账号单:</span>
                            <input id="orderNo" value="" name="billNo" type="text" class="form-control" style="height: 40px;line-height: 40px;padding-left: 60px" autocomplete="off">
                        </div>

                        <div class="form-group form-group-base form-input">
                            <span class="form-label" style="line-height: 40px">桌号:</span>
                            <input value="" id="deskNo" name="deskNo" type="text" class="form-control" style="height: 40px;line-height: 40px;padding-left: 48px" autocomplete="off">
                        </div>
                        <div class="keyboard print">
                            <li>1</li><li>2</li><li>3</li>
                            <li>4</li><li>5</li><li>6</li><li>7</li><li>8</li><li>9</li><li>0</li><li>C</li>
                        </div>
                    </div>
                    <div class="check-search clear">
                        <span class="check-searchText">账单状态:</span>
                        <div class="check-type ">
                            <div class="active" orderstatus="">全部</div><div orderstatus="3">已结账</div><div orderstatus="0">未结账</div>
                        </div>
                        <%--<div class="print">
                            <div class="form-group form-group-base form-input">
                                <span class="form-label" style="line-height: 40px">开始时间:</span>
                                <input value="" name="startDate" type="text" id="inpstart" class="form-control" style="height: 40px;line-height: 40px;padding-left: 75px" autocomplete="off">
                            </div>

                            <div class="form-group form-group-base form-input">
                                <span class="form-label" style="line-height: 40px">结束时间:</span>
                                <input value="" name="endDate" type="text" id="end" class="form-control" style="height: 40px;line-height: 40px;padding-left: 75px" autocomplete="off">
                            </div>
                            <div class="check-type print">
                                <div class="active">查询</div>
                            </div>
                        </div>--%>
                    </div>
                    <table id="checklist" class="table table-bordered table-hover table-list clear " style="background: #fff">
                        <thead>
                            <tr>
                                <th>账单号</th>
                                <th>状态</th>
                                <th>区域</th>
                                <th>桌号</th>
                                <th>服务员</th>
                                <th>开台时间</th>
                                <th>结账时间</th>
                                <th>人数</th>
                                <th>应收金额</th>
                                <th>订单单位</th>
                                <th>电话</th>
                                <th>联系人</th>
                            </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>


                    <div class="contentInfo" style="height: 75px;position: fixed;bottom: 0px;width: 100%;background: none">
                        <div class="foot-menu">
                            <ul>
                                <button class="refresh">刷新</button>
                                <button  class="reprintCheck">重印账单</button>
                                <button  class="receipt">交易凭条</button>
                                <button class="reprintClear">重印清机单</button>
                                <button class="c-mod-js">结算</button>
                                <button class="c-mod-fjs">反结算</button>
                            </ul>
                            <div class="page print" id="pagDome" style="margin-right: 10px"></div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</article>
<footer>
    <div class="info J-sys-info"><span>店铺编号：</span><span class="branch-num">- -</span><span>&nbsp;登录员工：</span><span>&nbsp;<span class="user-info">- -</span></span><span>&nbsp;当前时间：</span><span class="time">- -</span><span>&nbsp;版本号：</span><span>1.01</span></div>
</footer>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="c-mod-fjs" style="overflow: auto;"></div>
<div class="modal fade in main-dialog" data-backdrop="static" id="order-dialog" style="overflow: auto;">

</div>
<script type="text/javascript" src="../../lib/jedate/jedate.min.js"></script>
<script src="../../scripts/check.js"></script>

</body>
</html>