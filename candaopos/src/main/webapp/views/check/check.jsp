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
    <link rel="stylesheet" href="../../css/check.css">
    <link rel="stylesheet" href="../../css/reporting.css">
    <link type="text/css" rel="stylesheet" href="../../lib/jedate/skin/jedate.css">

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
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="modify-pwd-dialog" style="overflow: auto;"></div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="modify-phone-dialog" style="overflow: auto;"></div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="modify-cancellation-dialog" style="overflow: auto;"></div>



<%--<script src="../../scripts/check.js"></script>--%>
<script type="text/javascript" src="../../lib/jedate/jedate.min.js"></script>
<script>
    var orderdata='',orderId=''
    $(function () {

        checkOrder.int("");
    });
    var aUserid=utils.storage.getter('aUserid')//获取登录用户


var checkOrder={
        int:function (orderstatus) {
            SetBotoomIfon.init();//设置底部信息
            this.selectTr();
            this.search();
            this.getOrderdata;
            this.getOrderlist(orderstatus);
            this.switchOrderlist();
            this.bottomEvent();
            //加载虚拟键盘组件
            widget.keyboard({
                target: '.keyboard'
            });


        },
    getOrderdata: {
        getData: function () {//获取接口数据
            $.ajax({
                url: '/newspicyway/datasnap/rest/TServerMethods1/getAllOrderInfo2/' + aUserid + '/',
                type: "get",
                async: false,
                dataType: "text",
                success: function (data) {
                    orderdata = JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                }
            });
            return orderdata

        }

    },
    getOrderlist:function (orderstatus ) {
        var that=this ,data="";
            $("#checklist tbody,#pagDome").html("");
        filtrateDatd();
        function filtrateDatd() {//数据筛选
            if (orderdata == "") {
                that.getOrderdata.getData();
            }
            data = orderdata.OrderJson;
            if (orderdata.Data == 1) {
                if (data.length == 0) {
                    $('#pagDome').hide()
                }
                else {
                    $('#pagDome').show()
                }
                if (orderstatus == "") {
                    data = data
                }
                if (orderstatus == "3") {
                    var arry = [];
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].orderstatus == 3) {
                            arry.push(data[i])
                        }
                    }
                    data = arry
                }
                if (orderstatus == "0") {
                    var arry = [];
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].orderstatus == 0) {
                            arry.push(data[i])
                        }
                    }
                    data = arry
                }
                ;
                ;

                var orderNo = $.trim($("#orderNo").val())
                var deskNo = $.trim($("#deskNo").val())
                if (orderNo != "" || deskNo != "") {
                    var arry = [];
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].orderid.indexOf(orderNo) > -1 && data[i].tableName.indexOf(deskNo) > -1) {
                            arry.push(data[i])
                        }
                    }
                    data = arry;
                    console.log(data)
                }

            }
        }
                $('#pagDome').pagination({
                    dataSource: data,
                    pageSize: 12,
                    showPageNumbers: false,
                    showNavigator: true,
                    callback: function(data, pagination) {
                        var str="";
                        for( var i=0;i<data.length;i++) {

                            if(data[i].memberno){//判断是否是会员登录
                                str+='<tr orderid='+data[i].orderid+' orderstatus='+data[i].orderstatus+' memberno='+data[i].memberno+'>';
                            }
                            else {
                                str+='<tr orderid='+data[i].orderid+' orderstatus='+data[i].orderstatus+'>';
                            }
                            str+='   <td>'+data[i].orderid+'</td>'
                            var  ordertype='';
                            switch (data[i].orderstatus+""){
                                case "0": ordertype="未结"; break;
                                case "3": ordertype="已结"; break;
                                default:  ordertype="未知";break;
                            };
                            str+='   <td>'+ordertype+'</td>'
                            str+='   <td>'+data[i].areaname+'</td>'
                            str+='   <td>'+data[i].tableName+'</td>'
                            str+='   <td>'+data[i].userid+'</td>'
                            str+='   <td>'+(data[i].begintime).substring(8)+'</td>'
                            if(data[i].endtime===undefined){
                                str+='   <td></td>'
                            }
                            else{
                                str+='   <td>'+(data[i].endtime).substring(8)+'</td>'
                            }
                            str+='   <td>'+data[i].custnum+'</td>'
                            str+='   <td>'+data[i].dueamount+'</td>'
                            if(data[i].gzname===undefined){
                                str+='   <td></td>'
                            }
                            else{
                                str+='   <td>'+data[i].gzname+'</td>'
                            }
                            if(data[i].gztele===undefined){
                                str+='   <td></td>'
                            }
                            else{
                                str+='   <td>'+data[i].gztele+'</td>'
                            }
                            if(data[i].gzuser===undefined){
                                str+='   <td></td>'
                            }
                            else{
                                str+='   <td>'+data[i].gzuser+'</td>'
                            }

                            str+="</tr>";
                        };
                        $("#checklist tbody").html(str);
                        $("#checklist tr").eq(1).trigger("click")//默认选中第一行
                    }
                });

    },
    switchOrderlist:function () {
        var that = this
        $(".check-type div").click(function () {//账单状态
            var orderstatus = $(this).attr("orderstatus");
            $(this).addClass("active").siblings("div").removeClass("active");
            that.getOrderlist(orderstatus)
        })
    },
    refreshOrderlist:function () {//刷新
        var that=this;
        var orderstatus=$(".check-type .active").attr("orderstatus");
        that.getOrderdata.getData();
        that.getOrderlist(orderstatus)
    },
    reprintClear:function () {//重印清机单
        var posId='001',jsorder=" ";
        $.ajax({
            url:'/newspicyway/print4POS/getClearMachineData/' + aUserid + '/'+jsorder+'/001/',
            type: "get",
            success: function (data) {
                rightBottomPop.alert({
                    content:"清机单打印完成",
                })
            }
        });
    },
    reprintCheck:function () {//重印账单
        $.ajax({
            url:'/newspicyway/print4POS/getOrderInfo/' + aUserid + '/'+orderId+'/2/',
            type: "get",
            success: function (data) {
                rightBottomPop.alert({
                    content:"结账单打印完成",
                })
            }
        });
    },
    receipt:function () {//会员交易凭条
        $.ajax({
            /*url:'/newspicyway/print4POS/getMemberSaleInfo/' + aUserid + '/'+orderId+'/',*/
            url:'/newspicyway/print4POS/getMemberSaleInfo/' + aUserid + '/'+orderId+'/',
            type: "get",
            success: function (data) {
                rightBottomPop.alert({
                    content:"交易凭条打印完成",
                })
            }
        });
    },
    rebackOrder:function () {//反结算
        $.ajax({
            /*url:'/newspicyway/print4POS/getMemberSaleInfo/' + aUserid + '/'+orderId+'/',*/
            url:'/newspicyway/datasnap/rest/TServerMethods1/rebackorder/' + aUserid + '/'+orderId+'/',
            type: "get",
            dataType: "text",
            success: function (data) {
                data = JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                console.log(data)
                console.log(data.Data+","+data.Info);
                alert("1212")
            }
        });
    },
    selectTr:function () {
        var that=this;
        $("body").on("click","#checklist tr" ,function(){//点击tr变色
            $(this).addClass("tablistActive").siblings("tr").removeClass("tablistActive");
            var orderstatus=$.trim($(this).attr("orderstatus")) ,memberno=$(this).attr("memberno");
            orderId= $.trim($(this).attr("orderid"))
            console.log(memberno+","+orderId+","+orderstatus)
            if(memberno !=undefined){//判断是否存在会员登录
                $(".receipt").removeAttr("disabled").removeClass("disabled");
            }
            else {
                $(".receipt").attr("disabled","disabled").addClass("disabled");
            };
            if(orderstatus=="0"){
                $(".c-mod-fjs,.reprintCheck,.receipt").attr("disabled","disabled").addClass("disabled");//反结算按钮，重印账单按钮disabled
                $(".c-mod-js").removeAttr("disabled","disabled").removeClass("disabled");//结算按钮移除disabled
            }
            if(orderstatus=="3"){
                $(".c-mod-js").attr("disabled","disabled").addClass("disabled");//结算按钮disabled
                $(".c-mod-fjs ,.reprintCheck,.receipt").removeAttr("disabled","disabled").removeClass("disabled");//反结算按钮，重印账单按钮移除disabled
            }
        });
    },
    search:function () {
        var that=this;
        $('#orderNo,#deskNo').keydown(function(){//如果是键盘点击
            $('#orderNo,#deskNo').on('input propertychange', function() {
                var orderstatus=$(".check-type .active").attr("orderstatus");
                that.getOrderlist(orderstatus)
            });
        })
        $('#orderNo,#deskNo').on("focus",function() {//如果是虚拟键盘点击
            var orderstatus=$(".check-type .active").attr("orderstatus");
            that.getOrderlist(orderstatus)
        });
    },
    bottomEvent:function () {
        var that=this;
        $(".foot-menu button").click(function () {
            var me=$(this);
            if(me.hasClass("refresh")){//刷新
                that.refreshOrderlist();
            }
            if(me.hasClass("reprintClear")){//重印清机单
                that.reprintClear();
            }
            if(me.hasClass("reprintCheck")){//重印账单
                that.reprintCheck()
            }
            if(me.hasClass("receipt")){//会员交易凭条
                that.receipt()
            }
            if(me.hasClass("c-mod-fjs")){//反结算
                that.rebackOrder()
            }
        })

    },
}

</script>

</body>
</html>