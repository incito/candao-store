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
    <link rel="stylesheet" href="../css/reporting.css">
    <link rel="stylesheet" href="../lib/pagination.css">

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
                                    <div class="active" flag="1" onclick="getItemSellDetail(this)">今天</div>
                                    <div flag="2" onclick="getItemSellDetail(this)">本周</div>
                                    <div flag="3" onclick="getItemSellDetail(this)">本月</div>
                                    <div flag="4" onclick="getItemSellDetail(this)">上月</div>
                                </div>
                                <div class="dataSelect-type print" >
                                    <div class="active" onclick="ItemSellDetailPrint()">打印</div>
                                </div>
                                <table class="table table-bordered table-hover table-list " style="background: #fff">
                                    <thead>
                                    <tr>

                                        <th>品项名称</th>
                                        <th>销售数量</th>
                                        <th>销售金额</th>
                                    </tr>
                                    </thead>
                                    <tbody >
                                    </tbody>
                                </table>
                                <div class="contentInfo">
                                    <div class="foot-menu">
                                        <div class="reporting-white" ></div>
                                        <div class="reportingInfo" >
                                            <span class="reportingInfo-text" >品项个数：<i></i></span>
                                            <span class="reportingInfo-text" >数量总计：<i></i></span>
                                            <span class="reportingInfo-text" >金额合计：<i></i></span>
                                        </div>

                                        <div class=" print">
                                            <div  class="paginationjs paginationjs-big demo">
                                        </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <%--小费统计明细--%>
                            <div class="tab-item" id="getTipList" style="display: none">
                                <div class="dataSelect-type">
                                    <div class="active" flag="1" onclick="getTipList(this)">今天</div>
                                    <div flag="1" onclick="getTipList(this)">本周</div>
                                    <div flag="1" onclick="getTipList(this)">本月</div>
                                    <div flag="1" onclick="getTipList(this)">上月</div>
                                </div>
                                <div class="dataSelect-type print" >
                                    <div class="active">打印</div>
                                </div>
                                <table class="table table-bordered table-hover table-list" style="background: #fff">
                                    <thead>
                                    <tr>
                                        <th></th>
                                        <th>服务员</th>
                                        <th>小费次数</th>
                                        <th>小费金额</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                                <div class="contentInfo">
                                    <div class="foot-menu">
                                        <div class="reporting-white" ></div>
                                        <div class="reportingInfo" >
                                            <span class="reportingInfo-text" >服务员个数：<i></i></span>
                                            <span class="reportingInfo-text" >数量总计：<i></i></span>
                                            <span class="reportingInfo-text" >金额合计：<i></i></span>
                                        </div>
                                        <div class="page print"><div class="page-btn prev-btn">&#60;</div><span id="curr-page">0</span>/<span id="pages-len">0</span><div class="page-btn next-btn">&#62;</div></div>
                                    </div>
                                </div>
                            </div>

                            <%--营业数据明细--%>
                            <div class="tab-item" style="display: none">
                                <div class="form-group form-group-base form-input">
                                    <span class="form-label">开始时间:</span>
                                    <input value=""  id="StartData"  type=”datetime” class="form-control" autocomplete="off" />
                                </div>
                                <span style="padding: 0 5px">至</span>
                                <div class="form-group form-group-base form-input" >
                                    <span class="form-label">结束时间:</span>
                                    <input value=""   type="datetime" class="form-control" id="EndData"  />
                                </div>
                                <div class="dataSelect-type print" >
                                    <div class="active">打印</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </article>
        </div>
    </div>
</div>
<script src="../scripts/reporting.js"></script>
</body>
</html>