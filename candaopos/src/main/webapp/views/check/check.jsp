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
    <link rel="stylesheet" href="../tools/bootstrap-3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/main.css">
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="../scripts/jquery-3.1.0.min.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
    <script src="../scripts/common.js"></script>
    <script src="../scripts/page.js"></script>
    <script src="../scripts/main.js"></script>
    <link rel="stylesheet" href="../css/check.css">
    <link rel="stylesheet" href="../css/reporting.css">

</head>
<body>
<style>
    .tablistActive td{
        background: #FF5803;
        color: #fff;
    }
</style>
<header>
    <div class="fl">餐道</div>
    <div class="fr close-win" data-dismiss="modal" onclick="goBack();">返回</div>
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
                            <input id="billNo" value="" name="billNo" type="text" class="form-control" style="height: 40px;line-height: 40px;padding-left: 60px" autocomplete="off">
                        </div>

                        <div class="form-group form-group-base form-input">
                            <span class="form-label" style="line-height: 40px">桌号:</span>
                            <input value="" name="deskNo" type="text" class="form-control" style="height: 40px;line-height: 40px;padding-left: 48px" autocomplete="off">
                        </div>
                        <div class="keyboard print">
                            <div>1</div><div>2</div><div>3</div>
                            <div>4</div><div>5</div><div>6</div><div>7</div><div> 8</div><div> 9</div><div> 0</div><div> C</div>
                        </div>
                    </div>
                    <div class="check-search clear">
                        <span class="check-searchText">账单状态:</span>
                        <div class="check-type ">
                            <div class="active" ordertype="">全部</div><div ordertype="已结">已结账</div><div ordertype="未结">未结账</div>
                        </div>
                        <div class="print">
                            <div class="form-group form-group-base form-input">
                                <span class="form-label" style="line-height: 40px">开始时间:</span>
                                <input value="" name="startDate" type="date" class="form-control" style="height: 40px;line-height: 40px;padding-left: 75px" autocomplete="off">
                            </div>

                            <div class="form-group form-group-base form-input">
                                <span class="form-label" style="line-height: 40px">结束时间:</span>
                                <input value="" name="endDate" type="date" class="form-control" style="height: 40px;line-height: 40px;padding-left: 75px" autocomplete="off">
                            </div>
                            <div class="check-type print">
                                <div class="active">查询</div>
                            </div>
                        </div>
                    </div>
                    <table id="checklist" class="table table-bordered table-hover table-list clear " style="background: #fff">
                        <thead>
                            <tr>
                                <th>账单号111</th>
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
                        <tr>
                            <td>H20160906702001031</td>
                            <td>已结</td>
                            <td>一楼</td>
                            <td>风雨112</td>
                            <td>013</td>
                            <td>10:31:21</td>
                            <td>12:30:45</td>
                            <td>12</td>
                            <td>430.98</td>
                            <td></td>
                            <td>13312344321</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>H20160906702001031</td>
                            <td>已结</td>
                            <td>一楼</td>
                            <td>风雨112</td>
                            <td>013</td>
                            <td>10:31:21</td>
                            <td>12:30:45</td>
                            <td>12</td>
                            <td>430.98</td>
                            <td></td>
                            <td>13312344321</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>H20160906702001031</td>
                            <td>已结</td>
                            <td>一楼</td>
                            <td>风雨112</td>
                            <td>013</td>
                            <td>10:31:21</td>
                            <td>12:30:45</td>
                            <td>12</td>
                            <td>430.98</td>
                            <td></td>
                            <td>13312344321</td>
                            <td></td>
                        </tr>
                        </tbody>
                    </table>


                    <div class="contentInfo" style="height: 75px">
                        <div class="foot-menu">
                            <ul>
                                <button onclick="refresh()">刷新</button>
                                <button  >重印账单</button>
                                <button >交易凭条</button>
                                <button>重印清机</button>
                                <button class="c-mod-js">结算</button>
                                <button class="c-mod-fjs">反结算</button>
                            </ul>
                            <div class="page print"><div class="page-btn prev-btn">&#60;</div><span id="curr-page">0</span>/<span id="pages-len">0</span><div class="page-btn next-btn">&#62;</div></div>
                        </div>
                        <div class="info"><span>店铺编号：</span><span>0012</span><span>&nbsp;登录员工：</span><span>&nbsp;收银员(008)</span><span>&nbsp;当前时间：</span><span>2016-08-19 12:00:00</span><span>&nbsp;版本号：</span><span>1.01</span></div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</article>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="c-mod-fjs" style="overflow: auto;"></div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="modify-pwd-dialog" style="overflow: auto;"></div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="modify-phone-dialog" style="overflow: auto;"></div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="modify-cancellation-dialog" style="overflow: auto;"></div>



<script src="../scripts/check.js"></script>
<script>

    $(function () {
        firstActive();
    })




</script>

</body>
</html>