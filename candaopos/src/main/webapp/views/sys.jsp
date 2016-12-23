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
    <link rel="stylesheet" href="../css/sys.css">
    <style>

    </style>
    <script>

        $(function () {
            var $tabBox = $('.tab-box');
            $('.J-g-menu li').click(function () {
                var me = $(this),
                        idx = me.index();
                me.addClass('active').siblings().removeClass('active');
                $tabBox.find('.tab-item').hide().eq(idx).show();
                if(idx=='2'){
                    cashbox.initPayWay()
                }
            })
        })
    </script>
</head>
<body>
<div class="modal-dialog main-modal-dialog" style="height: 600px; width:960px" id="sys-modal"
     data-backdrop="static">
    <div class="modal-content">
        <div class="modal-body">
            <header style="min-width:960px">
                <div class="fl">餐道</div>
                <div class="fr close-win" data-dismiss="modal">返回</div>
            </header>
            <article style="height: 540px;min-width: 960px;">
                <div class="content">
                    <div class="g-l J-g-menu print_list">
                        <ul>
                            <%--<li >启动打印复写卡</li>--%>
                            <li class="active">打印机列表<i style="display: none">i</i></li>
                            <li>钱箱密码验证</li>
                            <li>结算方式设置</li>
                        </ul>
                    </div>
                    <div class="g-r">
                        <div class="g-r-content tab-box">
                            <%--启动打印复写--%>
                            <%--<div class="tab-item">
                                <div class="item-card">
                                    <h3>启动打印复写</h3>
                                    <p>打开此功能后，注册会员、会员结账、充值时可以打印会员信息到复写卡上。</p>

                                    <div class="switch">
                                        <input type="checkbox" checked="checked">
                                        <label><i></i></label>
                                    </div>
                                </div>
                            </div>--%>

                            <%--打印机列表--%>
                            <div class="tab-item" id="printList">
                                <table class="table table-bordered table-hover table-list" style="background: #fff">
                                    <thead>
                                    <tr>
                                        <th></th>
                                        <th>打印机IP</th>
                                        <th>打印机名称</th>
                                        <th>打印机状态</th>
                                    </tr>
                                    </thead>
                                    <tbody>

                                    </tbody>
                                </table>
                                <div class="foot-menu" style="position: fixed;bottom: 0px;width: 70px;right: 40px;">
                                    <div class=""
                                         style="margin-right: 10px;background: #fff;height: 40px; line-height:40px;border-radius: 5px;width: 100%;text-align: center;cursor: pointer"
                                         onclick="cashbox.clickNo()">刷新(<span id="show"></span>)
                                    </div>
                                </div>
                            </div>

                            <%--打印机列表--%>
                            <div class="tab-item" style="display: none">
                                <div class="item-card">
                                    <h3>开启钱箱密码验证</h3>
                                    <p>启用该功能后，点击“开钱箱”按钮后需要输入密码验证方可打开钱箱</p>

                                    <div class="switch">
                                        <input type="checkbox" checked="checked">
                                        <label><i></i></label>
                                    </div>
                                </div>
                            </div>

                            <div class="tab-item" style="display: none;" id="payWayContainer">
                                <table class="table table-bordered table-hover table-list" style="background: #fff;">
                                    <thead>
                                    <tr>
                                        <th>支付方式</th>
                                        <th>操作</th>
                                        <th>调整显示顺序</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                                <div class="oper-div" id="adddish-sys" style="position: fixed;right:20px;bottom: 10px;">
                                    <div class="btns">
                                        <button class="btn oper-btn prev-btn disabled">
                                            <span class="glyphicon glyphicon-chevron-left"></span>
                                        </button>
                                        <div class="page-info" style="display: inline">
                                            <span id="curr-page">0</span>/<span id="pages-len">0</span>
                                        </div>
                                        <button class="btn oper-btn next-btn disabled">
                                            <span class="glyphicon glyphicon-chevron-right"></span>
                                        </button>
                                    </div>
                                </div>
                                <div id="getPayTypeList" style=" float: right;">
                                    <div class=" demo">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </article>
        </div>
    </div>
</div>
<script>
    var timerClear;

    //清除60S倒计时

    $(function () {

        cashbox.init();

    })

    var cashbox = {
        init: function () {

            clearTimeout(timerClear);//清楚setTimeout

            this.getCashbox();

            this.setCashbox();

            this.getPrintDetail();

            this.abc();

            //this.initPayWay();

            $("body").on("click", "#payWayContainer tbody tr", function () {
                $(this).addClass("tablistActive").siblings("tr").removeClass("tablistActive");
            })//点击tr变色

            return {
                get: this.getCashbox,
                set: this.setCashbox,
                getPrint: this.getPrintDetail
            }
        },
        getCashbox: function () {//获取开钱箱状态
            var that = this;
            var cashboxtype = utils.storage.getter('cashbox');
            if (cashboxtype == null || cashboxtype == "0") {
                $(".switch input").attr("checked", false)
            }
            if (cashboxtype == "1") {
                $(".switch input").attr("checked", true)
            }
        },
        setCashbox: function () {//设置是否开启钱箱 1为开启，0位关闭
            var that = this;

            $(".switch input").click(function () {
                if ($(this).prop("checked") == true) {
                    utils.storage.setter('cashbox', "1");
                }
                else {
                    utils.storage.setter('cashbox', "0");
                }
            })
        },
        getPrintDetail: function () {//获取打印机列表
            var that = this;
            $.ajax({
                url: _config.interfaceUrl.GetPrinterList,
                type: "get",
                dataType: "json",
                global: false,
                success: function (data) {
                    //console.log(data)
                    var str = "", num = 0, arr = [];
                    for (var i = 0; i < data.data.length; i++) {
                        if (data.data[i].status == '5') {
                            arr.push(data.data[i])
                        }
                        if (data.data[i].statusTitle.indexOf('上盖打开') > -1) {
                            arr.push(data.data[i])
                        }
                        if (data.data[i].statusTitle.indexOf('打印纸已用尽') > -1) {
                            arr.push(data.data[i])
                        }
                        num = i + 1
                        str += '<tr>';
                        str += '   <td width="200">' + num + '</td>';
                        str += '   <td width="276">' + data.data[i].ip + '</td>';
                        if (data.data[i].name) {
                            str += '   <td width="200">' + data.data[i].name + '</td>';
                        }
                        else {
                            str += '   <td width="200"> </td>';
                        }
                        str += '   <td width="200">' + data.data[i].statusTitle + '</td>';
                        str += '</tr>';
                    }
                    ;
                    $("#printList tbody").html(str);
                    if (arr.length > 0) {
                        $('.J-btn-sys').css({'background': '#FF5803', 'color': '#fff'});//系统设置高亮
                        $('.print_list li').eq(0).find('i').show()//显示感叹号！
                    }
                    else {
                        $('.J-btn-sys').css({'background': '#fff', 'color': '#000'});
                        $('.print_list li').eq(0).find('i').hide()//隐藏感叹号！
                    }
                },
            });
        },
        abc: function () {
            var that = this, timeLeft = 10 * 1000;//这里设定的时间是10秒;
            countTime();
            function countTime() {
                if (timeLeft == 0) {
                    timeLeft = 10 * 1000;
                    that.getPrintDetail()
                }
                var startMinutes = parseInt(timeLeft / (60 * 1000), 10);
                var startSec = parseInt((timeLeft - startMinutes * 60 * 1000) / 1000);
                timeLeft = timeLeft - 1000;
                timerClear = setTimeout(function () {
                    countTime();
                }, 1000);
                $('#show').text(startSec);
            }
        },
        initPayWay: function () {
            var $payWayContainer = $('#payWayContainer tbody');
            var _savePayWay = function () {
                var ret = [];
                $payWayContainer.find('tr').each(function () {
                    var me = $(this);
                    ret.push({
                        itemId: me.attr('itemid'),
                        status: me.attr('status')
                    })
                });
                Log.send(2, '保存支付方式:' + JSON.stringify(ret));
                $.ajax({
                    url: _config.interfaceUrl.SavePayWay,
                    type: "POST",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(ret)
                }).then(function (res) {
                    Log.send(2, '保存支付方式返回:' + JSON.stringify(res));
                    if (res.code === '0') {
                        rightBottomPop.alert({
                            content: "修改结算方式成功"
                        })
                    } else {
                        widget.modal.alert({
                            content: '<strong>' + res.msg + '</strong>',
                            btnOkTxt: '确定',
                            btnCancelTxt: ''
                        });
                    }
                })
            };
            $.ajax({
                url: _config.interfaceUrl.PayWay,
                type: "get",
                dataType: "json",
            }).then(function (res) {
                var ret = [];
                var vipstatus = JSON.parse(utils.storage.getter('memberAddress')).vipstatus;

                if (res.code === '0') {
                    $.each(res.data, function (k, v) {
                        var isopen = (v.status === 1 ? true : false);
                        var memmberCls = (function () {
                            if (vipstatus === false && v.itemId === '8') {
                                return 'f-dn'
                            } else {
                                return ''
                            }
                        })();
                        ret.push('<tr status=' + v.status + ' itemId=' + v.itemId + ' class="' + memmberCls + '"><td width="50%">' + v.title + '</td><td width="20%"><span class="btn-toggle J-btn-toggle">' + (isopen ? '禁用' : '启用') + '</span></td><td><span class="arrow arrow-up J-btn-up"></span><span class="arrow arrow-down J-btn-down"></span></td></tr>');
                    });
                    $payWayContainer.html(ret.join(''));
                    widget.loadPage({
                        obj : "#payWayContainer tbody tr",
                        listNum : 10,
                        currPage : 0,
                        totleNums : $("#payWayContainer tbody tr").length,
                        curPageObj : "#adddish-sys #curr-page",
                        pagesLenObj : "#adddish-sys #pages-len",
                        prevBtnObj : "#adddish-sys  .prev-btn",
                        nextBtnObj : "#adddish-sys  .next-btn",
                        callback : function() {
                        }
                    });

                    $payWayContainer.find('.J-btn-toggle').off('click').on('click', function () {
                        var me = $(this);
                        var $parent = me.parents('tr');
                        var modalIns = widget.modal.alert({
                            content: (function () {
                                var tips = ''
                                if ($parent.attr('status') === '1') {
                                    tips = '确认禁用后, 在结算方式区域里不在显示该结算方式!'
                                } else {
                                    tips = '确认启用后, 在结算方式区域里显示出该结算方式!'
                                }
                                return '<strong>' + tips + '</strong>'
                            })(),
                            btnOkCb: function () {
                                if ($parent.attr('status') === '0') {
                                    $parent.attr('status', '1');
                                    me.text('禁用');
                                } else {
                                    $parent.attr('status', '0');
                                    me.text('启用')
                                }
                                _savePayWay();
                                modalIns.close();
                            }
                        })

                    });
                    $payWayContainer.find('.J-btn-up').off('click').on('click', function () {
                        var me = $(this);
                        var $parent = me.parents('tr');
                        if ($parent.index() == 0) {
                            return false;
                        }
                        if($parent.prev().hasClass('hide')){
                            $parent.insertBefore($parent.prev().removeClass('hide'));
                            _savePayWay();
                            widget.loadPage({
                                obj : "#payWayContainer tbody tr",
                                listNum : 10,
                                currPage : parseFloat($('#curr-page').text())-2,
                                totleNums : $("#payWayContainer tbody tr").length,
                                curPageObj : "#adddish-sys #curr-page",
                                pagesLenObj : "#adddish-sys #pages-len",
                                prevBtnObj : "#adddish-sys  .prev-btn",
                                nextBtnObj : "#adddish-sys  .next-btn",
                                callback : function() {
                                }
                            });
                        }
                        else{
                            $parent.insertBefore($parent.prev());
                            _savePayWay();
                        }

                    });
                    $payWayContainer.find('.J-btn-down').off('click').on('click', function () {
                        var me = $(this);
                        var $parent = me.parents('tr');
                        if ($parent.attr('itemid') == $('#payWayContainer tbody tr:last').attr('itemid')) {
                            return false;
                        }
                        if($parent.next().hasClass('hide')){
                            $parent.insertAfter($parent.next().removeClass('hide'));
                            _savePayWay();
                            widget.loadPage({
                                obj : "#payWayContainer tbody tr",
                                listNum : 10,
                                currPage : parseFloat($('#curr-page').text()),
                                totleNums : $("#payWayContainer tbody tr").length,
                                curPageObj : "#adddish-sys #curr-page",
                                pagesLenObj : "#adddish-sys #pages-len",
                                prevBtnObj : "#adddish-sys  .prev-btn",
                                nextBtnObj : "#adddish-sys  .next-btn",
                                callback : function() {
                                }
                            });
                        }
                        else{
                            $parent.insertAfter($parent.next());
                            _savePayWay();
                        }

                    });


                } else {
                    widget.modal.alert({
                        content: '<strong>' + res.msg + '</strong>',
                        btnOkTxt: '确定',
                        btnCancelTxt: ''
                    });
                }
            });
        },
        /*手动点击刷新*/
        clickNo: function () {
            var that = this;
            clearTimeout(timerClear);//清楚setTimeout
            that.getPrintDetail();
            that.abc()


        }
    }
</script>
</body>
</html>