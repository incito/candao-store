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
    <title>订单</title>
    <link rel="stylesheet" href="../../tools/bootstrap-3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../css/common.css">
    <link rel="stylesheet" href="../../css/main.css">
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="../../scripts/jquery-3.1.0.min.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="../../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
    <script src="../../scripts/common.js"></script>
    <link rel="stylesheet" href="../../css/sys.css">
    <link rel="stylesheet" href="../../css/reporting.css">
    <script type="text/javascript" src="../../lib/jedate/jedate.min.js"></script>
    <link type="text/css" rel="stylesheet" href="../../lib/jedate/skin/jedate.css">
</head>
<body>

<header>
    <div class="fl">餐道</div>
    <div class="fr close-win" data-dismiss="modal" onclick="goBack();">返回</div>
</header>
<article style="height: 540px;">
    <div class="content">
        <div class="g-l J-g-menu">
            <ul>
                <li class="active">品项销售明细</li>
                <li>小费统计明细</li>
                <li>营业数据明细</li>
            </ul>
        </div>
        <div class="g-r">
            <div class="g-r-content tab-box">
                <%--品项销售明细--%>
                <div class="tab-item" id="getItemSellDetail">
                    <div class="dataSelect-type">
                        <div class="active" flag="1" onclick="reporting.getItemSellDetail(this)">今天</div>
                        <div flag="2" onclick="reporting.getItemSellDetail(this)">本周</div>
                        <div flag="3" onclick="reporting.getItemSellDetail(this)">本月</div>
                        <div flag="4" onclick="reporting.getItemSellDetail(this)">上月</div>
                    </div>
                    <div class="dataSelect-type print">
                        <div class="active" onclick="reporting.PrintItemSell()">打印</div>
                    </div>
                    <div style="height: 500px;" class="clearfix">
                        <table class="table table-bordered table-hover table-list "
                               style="background: #fff;margin-bottom: 0px">
                            <thead>
                            <tr>
                                <th>品项名称</th>
                                <th>销售数量</th>
                                <th>销售金额</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div class="contentInfo">
                        <div class="foot-menu">
                            <div class="reportingInfo">
                                <span class="reportingInfo-text">品项个数：<i></i></span>
                                <span class="reportingInfo-text">数量总计：<i></i></span>
                                <span class="reportingInfo-text">金额合计：<i></i></span>
                            </div>

                            <div class=" print">
                                <div class=" demo">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <%--小费统计明细--%>
                <div class="tab-item" id="getTipList" style="display: none">
                    <div class="dataSelect-type">
                        <div class="active" flag="1" onclick="reporting.getTipList(this)">今天</div>
                        <div flag="2" onclick="reporting.getTipList(this)">本周</div>
                        <div flag="3" onclick="reporting.getTipList(this)">本月</div>
                        <div flag="4" onclick="reporting.getTipList(this)">上月</div>
                    </div>
                    <div class="dataSelect-type print">
                        <div class="active" onclick="reporting.TipListPrint()">打印</div>
                    </div>
                    <div style="height: 500px;" class="clearfix">
                        <table class="table table-bordered table-hover table-list" style="background: #fff">
                            <thead>
                            <tr>
                                <th>服务员</th>
                                <th>小费次数</th>
                                <th>小费金额</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div class="contentInfo">
                        <div class="foot-menu">
                            <div class="reportingInfo">
                                <span class="reportingInfo-text">服务员个数：<i></i></span>
                                <span class="reportingInfo-text">数量总计：<i></i></span>
                                <span class="reportingInfo-text">金额合计：<i></i></span>
                            </div>
                            <div class="print">
                                <div class=" demo">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <%--营业数据明细--%>
                <div class="tab-item" style="display: none">
                    <span>自定义时间</span></br></br>
                    <div class="form-group form-group-base form-input">
                        <span class="form-label">开始时间:</span>
                        <input value="" id="datetimeStart" type=”text” class="form-control datetimeStart"/>
                    </div>
                    <span style="padding: 0 5px">至</span>
                    <div class="form-group form-group-base form-input">
                        <span class="form-label">结束时间:</span>
                        <input value="" type="text" class="form-control datetimeEnd"/>
                    </div>
                    <div class="dataSelect-type print">
                        <div class="active" onclick="reporting.printBusinessDetail()">打印</div>
                    </div>
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
<script src="../../scripts/reporting.js"></script>

<script>
    var start = {
        dateCell: '.datetimeStart',//input选择框
        skinCell: "jedateorange",//橙色风格
        format: 'YYYY-MM-DD hh:mm:ss',
        festival: true,//是否显示节日
        maxDate: jeDate.now(0), //设定最大日期为当前日期
        choosefun: function (elem, datas) {
            end.minDate = datas; //开始日选好后，重置结束日的最小日期
        }
    };
    jeDate(start)
    var end = {
        dateCell: '.datetimeEnd',
        skinCell: "jedateorange",
        format: 'YYYY-MM-DD hh:mm:ss',
        festival: true,//是否显示节日
        maxDate: jeDate.now(0), //设定最大日期为当前日期
        choosefun: function (elem, datas) {
            start.maxDate = datas; //将结束日的初始值设定为开始日的最大日期
        }
    };
    jeDate(end)
</script>

</body>
</html>