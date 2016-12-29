var g_eatType = "EAT-IN";//堂食
var tackOUttable = [];//外卖咖啡外卖台
var tablesCurPager = 1;
var tableAreaSelect = -1;//选中的是那个分区
var roomtype_prev = 0;//左右切换的Index
var getOpenEndTimeTimer =null//营业时间定时任务变量

var MainPage = {

    CurrentTalbeType: 'all',
    CurrentArea: '-1',//默认为全部
    CurrentSelectedTable: '',


    init: function () {

        this.timeTask();

        this.setTables();

        SetBotoomIfon.init();

        this.bindEvent();

        //this.getOpenEndTime();

        //加载虚拟键盘组件
        widget.keyboard();

        /*雅座会员餐道会员切换 1为餐道会员；2为雅座会员*/

        if(!JSON.parse(utils.storage.getter('memberAddress')).vipstatus) {
            $('.member-btns').hide();
        }
        if (utils.storage.getter('vipType') == '1') {
            var str = '<li class="J-btn-storge">会员储值</li>' +
                '<li class="J-btn-register">会员注册</li>' +
                '<li class="J-btn-memberView">会员查询</li>'
            $('.arrowMember').html(str)
        }
        if (utils.storage.getter('vipType') == '2') {
            var str = '<li class="J-btn-storge">会员储值</li>' +
                '<li class="J-btn-register">会员激活</li>' +
                '<li class="J-btn-memberView">会员查询</li>'
            $('.arrowMember').html(str)
        }
    },

    bindEvent: function () {

        var that = this;
        var dom = {
            standardTables: $("#standard-tables"),
            openDialog: $("#open-dialog"),//开台权限验证弹窗,
            roomTypeNav: $(".rooms-type"),//餐台分类导航
        };
        /**
         * 标准台事件
         */
        dom.standardTables.on('click', 'li', function () {
            var me = $(this);
            var cla = me.attr("class");

            that.CurrentSelectedTable = me;

            if (cla !== "opened") {
                dom.openDialog.find('.J-server-name,.J-male-num,.J-female-num,.J-tableware-num').val('');
                dom.openDialog.modal("show");
                focusIpt = dom.openDialog.find('.J-server-name')
                return false;
            }
            var url = "../views/order.jsp?orderid=" + me.attr('orderid') + '&personnum=' + me.attr('personnum') + '&tableno=' + encodeURIComponent(encodeURIComponent(me.attr('tableno'))) + '&type=in';
            window.location.href = url;
        });

        /**
         * 开启服务员权限验证
         */
        dom.openDialog.on('click', '.age-type>div', function () {
            var me = $(this);
            me.toggleClass('active');
        });

        dom.openDialog.on('input propertychange focus', '.J-male-num, .J-female-num', function () {
            var maleNum = (dom.openDialog.find('.J-male-num').val() === '' || dom.openDialog.find('.J-male-num').val() === '.') ? '0' : dom.openDialog.find('.J-male-num').val();
            var femailNum = (dom.openDialog.find('.J-female-num').val() === '' || dom.openDialog.find('.J-female-num').val() === '.') ? '0' : dom.openDialog.find('.J-female-num').val();
            $('.J-tableware-num').val(parseInt(maleNum, 10) + parseInt(femailNum, 10))
        });

        dom.openDialog.on('click', '.J-btn-submit', function () {
            var serverName = dom.openDialog.find('.J-server-name').val();
            var maleNum = dom.openDialog.find('.J-male-num').val() === '' ? '0' : dom.openDialog.find('.J-male-num').val();
            var femailNum = dom.openDialog.find('.J-female-num').val() === '' ? '0' : dom.openDialog.find('.J-female-num').val();
            var params = null;

            if (parseInt(maleNum, 10) + parseInt(femailNum, 10) < 1) {
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>请输入就餐人数</strong>',
                    width: 500,
                    height: 500,
                    btnOkTxt: '确定',
                    btnCancelTxt: ''
                });
                return false;
            }

            //if (parseInt(maleNum, 10) + parseInt(femailNum, 10) > 9999) {
            //    widget.modal.alert({
            //        cls: 'fade in',
            //        content: '<strong>总人数不能大于9999</strong>',
            //        width: 500,
            //        height: 500,
            //        btnOkTxt: '确定',
            //        btnCancelTxt: ''
            //    });
            //    return false;
            //}

            if (serverName === undefined || serverName === '') {
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>请输入服务员编号</strong>',
                    width: 500,
                    height: 500,
                    btnOkTxt: '确定',
                    btnCancelTxt: ''
                });
                return false;
            }

            //验证用户权限,然后开台
            that.verifyUser(serverName, function () {
                var $target = that.CurrentSelectedTable;
                params ={
                    tableNo: $target.attr('tableno'),
                    ageperiod: (function () {
                        var str = '';
                        dom.openDialog.find('.age-type>div').each(function () {
                            var me = $(this);
                            if (me.hasClass('active')) {
                                str += me.index() + 1;
                            }
                        });
                        return str;
                    })(),
                    username: serverName,
                    manNum: maleNum,
                    womanNum: femailNum
                };
                Log.send(2, '用户开台:' + JSON.stringify(params));
                $.ajax({
                    url: _config.interfaceUrl.OpenTable,
                    method: 'POST',
                    contentType: "application/json",
                    data: JSON.stringify(params),
                    dataType: 'json',
                    success: function (res) {
                        Log.send(2, '用户开台:' + JSON.stringify(res));
                        if (res.code === '0') {
                            var url = "../views/orderdish.jsp?orderid=" + res.data.orderid + '&personnum=' + $('.J-tableware-num').val() + '&tableno=' + encodeURIComponent(encodeURIComponent($target.attr('tableno'))) + '&type=in';
                            dom.openDialog.modal('hide');
                            window.location.href = url;
                        } else {
                            widget.modal.alert({
                                cls: 'fade in',
                                content: '<strong>' + res.msg + '</strong>',
                                width: 500,
                                height: 500,
                                btnOkTxt: '',
                                btnCancelTxt: '确定'
                            });
                        }
                    }
                })

            });
        });


        //退出系统
        $(".exit-sys").click(function () {
            widget.modal.alert({
                content: '<strong>确定要退出系统吗?</strong>',
                btnOkCb: function(){
                    Log.send(2, '退出系统,清空缓存');
                    Log.upload();
                    window.location = "../views/login.jsp";
                    utils.clearLocalStorage.clearSelect()
                }
            })

        });

        //标准台和咖啡台切换
        //$(".menu-tab ul li").click(function(){
        //	var olddiv = $(".menu-tab ul li.active").attr("loaddiv");
        //	$(olddiv).addClass("hide");
        //	$(".menu-tab ul li").removeClass("active");
        //	$(this).addClass("active");
        //	var loaddiv = $(this).attr("loaddiv");
        //	$(loaddiv).removeClass("hide");
        //});

        /*餐台分类事件*/

        var navRoomTypes = $("#nav-room-types");

        dom.roomTypeNav.delegate('li', 'click', function () {
            var me = $(this);
            me.siblings().removeClass("active").end().addClass('active');
            me.addClass("active");
            that.CurrentArea = me.attr('areaid');
            tableAreaSelect = me.attr('areaid');
            that.setTables(that.CurrentTalbeType, me.attr('areaid'));
        });

        dom.roomTypeNav.delegate('.nav-type-next', 'click', function () {
            var count = navRoomTypes.find("li").length;
            if (roomtype_prev < count - 10) {
                navRoomTypes.find("li").eq(roomtype_prev).css("margin-left", "-10%");
                if(parseInt(navRoomTypes.find("li.active").css('margin-left'))  < 0) {
                    navRoomTypes.find("li").eq(roomtype_prev + 1).click();
                }
                roomtype_prev++;
                $(".nav-type-prev").removeClass('unclick');
                if (roomtype_prev == count - 10) {
                    $(".nav-type-next").addClass('unclick');
                }
            }
        });

        dom.roomTypeNav.delegate('.nav-type-prev', 'click', function () {
            if (roomtype_prev >= 1) {
                navRoomTypes.find("li").eq(roomtype_prev - 1).css("margin-left", "0");
                if(navRoomTypes.find("li.active").index() ===  (roomtype_prev + 9)) {
                    navRoomTypes.find("li").eq(navRoomTypes.find("li.active").index() - 1).click();
                }
                roomtype_prev--;
                $(".nav-type-next").removeClass('unclick');
                if (roomtype_prev == 0) {
                    $(".nav-type-prev").addClass('unclick');
                }
            }
        });

        //底部菜单事件绑定
        $('footer').on('click', '.foot-menu li', function (e) {
            var me = $(this);
            if (me.hasClass("J-btn-takeout")) {
                that.initTakeOutModal();
            }
            if (me.hasClass("member-btns")) {
                //会员
                $(".m-member.popover").toggle();
                e.stopPropagation();
            }
            if (me.hasClass('J-btn-sys')) {
                $("#sys-dialog").load("../views/sys.jsp");
                $("#sys-dialog").modal("show");
            }
            if (me.hasClass('J-btn-rep')) {
                window.location.href = "../views/reporting/reporting.jsp";
            }
            if (me.hasClass('J-btn-check')) {
                window.location.href = "../views/check/check.jsp";

            }
            ;
            if (me.hasClass('J-btn-clear')) {
                var aUserid = utils.storage.getter('aUserid'), orderLength = 0, str//获取登录用户
                Log.send(2, '点击清机/结业按钮aUserid:' + aUserid);
                $.ajax({
                    url: _config.interfaceUrl.QueryOrderInfo + '' + aUserid + '/',
                    type: "get",
                    async: false,
                    dataType: "text",
                    success: function (data) {
                        var data = JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                        Log.send(2, '点击清机/结业按钮' + JSON.stringify(data));
                        var arry = [];
                        for (var i = 0; i < data.OrderJson.length; i++) {
                            if (data.OrderJson[i].orderstatus == 0) {
                                arry.push(data.OrderJson[i])
                            }
                        }
                        orderLength = arry.length
                    }
                });

                var str = '<strong>请选择倒班或结业：</strong>'
                str += '<div id="clearchoose" class="form-group form-group-base" style="margin-top: 20px">'
                if (utils.userRight.get(aUserid, "030204")) {//判断清机权限
                    str += '<button id="clearAll" class="btn-default btn-lg btn-base btn-base-flex2 clearAll"  style="margin-right: 5px">倒班</button>'
                }
                else {
                    str += '<button id="clearAll" class="btn-default btn-lg btn-base btn-base-flex2 clearAll" disabled="disabled" style="margin-right: 5px;color: #999; background: #E8E8E8">倒班</button>'
                }
                if (orderLength > 0) {//判断账单未结业数量
                    str += '<button id="completion" class="btn-default btn-lg btn-base btn-base-flex2 clearCompletion" disabled="disabled" style="color: #999; background: #E8E8E8">结业</button>'
                    str += '</div>'
                    str += '<div class="glyphicon glyphicon-info-sign" style="color: #8c8c8c;">还有未结账的餐台不能结业</div>'
                }
                else {
                    str += '<button id="completion" class="btn-default btn-lg btn-base btn-base-flex2 clearCompletion" >结业</button>'
                    str += '</div>'
                }
                widget.modal.alert({
                    cls: 'fade in',
                    content: str,
                    width: 400,
                    height: 500,
                    title: "",
                    hasBtns: false,
                });
                $("#clearchoose button").click(function () {
                    $(".modal-alert:last,.modal-backdrop:last").remove();
                    var _this = $(this);
                    if (_this.hasClass("clearAll")) {
                        $(".modal-alert:last,.modal-backdrop:last").remove();
                        $("#J-btn-checkout-dialog").load("../views/check/impower.jsp", {
                            'title': '清机授权',
                            'usernameDisble': '1',
                            'cbd': 'MainPage.clearAll()',
                            'userRightNo': '030204'
                        });
                        $("#J-btn-checkout-dialog").modal("show");
                    }
                    if (_this.hasClass("clearCompletion")) {//结业
                        var str = '<strong>确定要结业吗？</strong>';
                        widget.modal.alert({
                            cls: 'fade in',
                            content: str,
                            width: 400,
                            height: 500,
                            title: "结业提醒",
                            btnOkTxt: '确定',
                            btnOkCb: function () {
                                $(".modal-alert:last,.modal-backdrop:last").remove();
                                MainPage.checkout();
                            },
                            btnCancelCb: function () {
                            }
                        })

                    }

                });

            }

            if (me.hasClass('J-btn-register')) {//会员注册
                if (utils.storage.getter('vipType') == '1') {
                    window.location.href = '../views/member/register.jsp';
                }
                if (utils.storage.getter('vipType') == '2') {
                    window.location.href = '../views/member/yaRegister.jsp';
                }
                /*$("#register-dialog").load("../views/member/register.jsp");
                 $("#register-dialog").modal("show");*/
            }

            if (me.hasClass('J-btn-storge')) {//会员储值
                if (utils.storage.getter('vipType') == '1') {
                    window.location.href = '../views/member/storge.jsp';
                }
                if (utils.storage.getter('vipType') == '2') {
                    window.location.href = '../views/member/yaRecharge.jsp';
                }
            }

            if (me.hasClass('J-btn-memberView')) {//会员查询
                if (utils.storage.getter('vipType') == '1') {
                    window.location.href = './member/view.jsp';
                }
                if (utils.storage.getter('vipType') == '2') {
                    window.location.href = '../views/member/yaStorge.jsp';
                }
            }
        });

        $(document).click(function (e) {
            $(".m-member.popover").hide();
            e.stopPropagation();
        });

        //设置餐桌统计
        $(".J-table-nums>div").click(function () {
            var me = $(this);
            me.siblings().removeClass("active").end().addClass('active');
            if (me.hasClass('all')) {
                that.setTables('all', that.CurrentArea);
                that.CurrentTalbeType = 'all';
            } else if (me.hasClass('opened')) {
                that.setTables('opened', that.CurrentArea);
                that.CurrentTalbeType = 'opened';
            } else {
                that.setTables('free', that.CurrentArea);
                that.CurrentTalbeType = 'free';
            }
        });
    },

    //定时任务
    preData: null,
    timeTask: function () {
        var that = this;
        var running = true;
        setTimeout(function () {
            $.when(
                $.ajax({
                    url: _config.interfaceUrl.ConsumInfo,
                    global: false
                })
            ).then(function (res) {
                if(JSON.stringify(that.preData) !== JSON.stringify(res)) {
                    Log.send(2, '定时任务 后去统计信息' + JSON.stringify(res));
                    that.preData = res;
                }
                if (res.code === '0') {
                    $('.custnum').text(res.data.custnum);
                    $('.dueamount').text(res.data.dueamount);
                    $('.orderCount').text(res.data.orderCount);
                    $('.ssamount').text(res.data.ssamount);
                    $('.totalAmount').text(res.data.totalAmount);
                    that.setTables();
                } else {
                    widget.modal.alert({
                        cls: 'fade in',
                        content: '<strong>' + (res.msg === '' ? '接口错误' : res.msg) + '</strong>',
                        width: 500,
                        height: 500,
                        btnOkTxt: '',
                        btnCancelTxt: '确定'
                    });
                }
            });

            if (running) {
                setTimeout(arguments.callee, 5000);
            }
        }, 50);
    },

    initTakeOutModal: function () {
        if(tackOUttable.length=='0'){
            Log.send(3, '该门店还没有配置外卖台');
            utils.printError.alert('该门店还没有配置外卖台');
            return false
        }
        var $modal = $('#J-takeout-dialog');
        var ret = [];
        var retNormal = [];
        $.each(tackOUttable, function (k, v) {
            if (v.tabletype == '3') {
                ret.push('<li>' + v.tableNo + '</li>');
            } else {
                retNormal.push('<li>' + v.tableNo + '</li>');
            }
        });
        $(".take-out-list").html(ret.join(''));
        $(".take-out-list").find('li').eq(0).addClass('active');
        $(".take-out-list-normal").html(retNormal.join(''));

        if (ret.length === 0 && retNormal.length === 0) {
            Log.send(3, '后台没有配置普通外卖台，请联系管理员');
            widget.modal.alert({
                content: '<strong>后台没有配置普通外卖台，请联系管理员</strong>',
                btnOkTxt: '',
                btnCancelTxt: '确定'
            });
            return false;
        }

        $(".take-out-list li").unbind("click").on("click", function () {
            $(".take-out-list li").removeClass("active");
            $(this).addClass("active");
        });
        if(ret.length=='0'){
            MainPage.setTakeOutOrder(0)
        }else {
            $modal.modal("show");
        }

    },

    /**
     * 外卖开台
     * @param type 0:普通外卖 1:咖啡外卖
     */
    setTakeOutOrder: function (type) {
        var tableNo = '';
        var tackout=[];
        if (type === 0) {
            if($('.take-out-list-normal li').length>0){
                tableNo = $('.take-out-list-normal').find('li').eq(0).text();
            }else {
                Log.send(3, '后台没有配置普通外卖台，请联系管理员');
                utils.printError.alert('后台没有配置普通外卖台，请联系管理员');
                return false
            }

        } else {
            tableNo = $('.take-out-list').find('li.active').text();
        }

        Log.send(2, '外卖开台:' + JSON.stringify({
                tableNo: tableNo,
                ageperiod: '',
                username: utils.storage.getter('aUserid'),
                manNum: 0,
                womanNum: 0
            }));
        $.ajax({
            url: _config.interfaceUrl.OpenTable,
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify({
                tableNo: tableNo,
                ageperiod: '',
                username: utils.storage.getter('aUserid'),
                manNum: 0,
                womanNum: 0
            }),
            dataType: 'json'
        }).then(function (res) {
            Log.send(2, '外卖开台返回:' + JSON.stringify(res));
            if (res.code === '0') {
                $.ajax({
                    url: _config.interfaceUrl.SetOrderTakeout + res.data.orderid + '/',
                    method: 'GET',
                    dataType: 'text'
                }).then(function (data) {
                    var data = JSON.parse(data.substring(12, data.length - 3));
                    Log.send(2, '设置为外卖返回:' + JSON.stringify(data));
                    if (data.Data === '1') {
                        var url = "../views/orderdish.jsp?orderid=" + res.data.orderid + '&personnum=0&tableno=' + encodeURIComponent(encodeURIComponent(tableNo)) + '&type=out';
                        window.location.href = url;
                    } else {
                        widget.modal.alert({
                            content: '<strong>设置为外卖接口错误</strong>',
                            btnOkTxt: '',
                            btnCancelTxt: '确定'
                        });
                    }
                })
            } else {
                widget.modal.alert({
                    content: '<strong>' + res.msg + '</strong>',
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
            }
        })

    },

    //验证用户
    verifyUser: function (serverName, cb) {
        Log.send(2, '验证用户:' + JSON.stringify({
                loginType: '030101',
                username: serverName
            }));
        $.ajax({
            url: _config.interfaceUrl.VerifyUser,
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify({
                loginType: '030101',
                username: serverName
            }),
            dataType: 'json',
            success: function (res) {
                Log.send(2, '验证用户返回:' + JSON.stringify(res));
                if (res.code === '0') {
                    var alertModal = widget.modal.alert({
                        cls: 'fade in',
                        content: '<strong>' + '确认开台' + '</strong>',
                        width: 500,
                        height: 500,
                        btnOkCb: function () {
                            alertModal.close();
                            cb && cb();
                        }
                    });
                } else {
                    widget.modal.alert({
                        cls: 'fade in',
                        content: '<strong>' + res.msg + '</strong>',
                        width: 500,
                        height: 500,
                        btnOkTxt: '',
                        btnCancelTxt: '确定'
                    });
                }
            }
        })
    },

    /**
     * 设置餐桌
     * @param type [opened, free, all]
     * @param areaid
     */
    setTables: function (type, areaid) {

        var type = type || this.CurrentTalbeType;
        var areaid = areaid || this.CurrentArea;
        var isYesterdayEndWork = utils.storage.getter('isYesterdayEndWork') === '1' ? false : true;

        function _getTablesArr(res) {
            var isOpend = false;
            var tablesAll = [];
            var tablesFree = [];
            var tablesOpened = [];
            var time = '';

            $.each(res, function (key, val) {
                var tmp = '';
                isOpend = val.status == '0' ? false : true;
                if (areaid === val.areaid || areaid === '-1') {
                    if (isOpend) {
                        time = '';
                        if (val.begintime === undefined) {
                            time = '';
                        } else {
                            var hm = (new Date().getTime() - (new Date(val.begintime.replace(new RegExp("-", "gm"), "/"))).getTime());//得到毫秒数
                            //计算出小时数
                            var leave1 = hm % (24 * 3600 * 1000);    //计算天数后剩余的毫秒数
                            var hours = Math.floor(leave1 / (3600 * 1000));//计算相差分钟数
                            var leave2 = leave1 % (3600 * 1000);       //计算小时数后剩余的毫秒数
                            var minutes = Math.floor(leave2 / (60 * 1000));
                            if (hours <= 0) {
                                time += '00:'
                            } else if (hours > 9) {
                                time += hours + ':'
                            } else {
                                time += '0' + hours + ':'
                            }

                            if (minutes <= 0) {
                                time += '00'
                            } else if (minutes > 9) {
                                time += minutes
                            } else {
                                time += '0' + minutes
                            }

                        }

                        tmp = '<li class="opened" orderid="' + val.orderid + '" personNum="' + val.custnum + '" tableno="' + val.tableNo + '" areaid="' + val.areaid + '">' + val.tableNo +
                            '<div class="tb-info tb-status">￥' + (val.amount == undefined ? '0' : parseFloat(val.amount).toFixed(2)) + '</div>' +
                            '<div class="tb-info meal-time">' + time + '</div> ' +
                            '<div class="tb-info tb-person">' + val.custnum + '/' + val.personNum + '</div>' +
                            ' </li>';

                        tablesOpened.push(tmp);
                    } else {
                        tmp = '<li class="' + (!isYesterdayEndWork && 'reserved') + '" orderid="' + val.orderid + '" personNum="' + val.personNum + '" tableno="' + val.tableNo + '" areaid="' + val.areaid + '">' + val.tableNo +
                            '<div class="tb-info tb-person">' + val.personNum + '人桌</div>' +
                            ' </li>'
                        tablesFree.push(tmp);
                    }
                    tablesAll.push(tmp);
                }
            });

            return {
                all: tablesAll,
                free: tablesFree,
                opened: tablesOpened
            }
        }

        $.ajax({
            url: _config.interfaceUrl.GetTableInfoByTableType,
            method: 'POST',
            contentType: "application/json",
            global: false,
            /*data: JSON.stringify({
             tableType: [0,1]
             }),*/
            dataType: 'json',
            success: function (res) {
                Log.send(2, '获取所有餐台:' + JSON.stringify(res));
                var allTables = []//全部餐台（不包括外卖和咖啡外卖台以及咖啡台）
                var coffeeTable=[]//咖啡餐台
                tackOUttable = []//全部外卖和咖啡外卖台
                if (res.code == '1') {
                    utils.printError.alert('餐台' + res.msg);
                    return false
                }
                $.each(res.data, function (key, val) {
                    $.each(val.tables, function (k, v) {
                        if (v.tabletype == '2') {//过滤外卖台
                            tackOUttable.push(v)
                        }
                        else if (v.tabletype == '3') {//过滤外卖咖啡外卖
                            tackOUttable.push(v)
                        }
                        else if (v.tabletype == '4') {//过滤咖啡台
                            coffeeTable.push(v)
                        }
                        else {
                            allTables.push(v)
                        }
                    })
                })
                var tables = _getTablesArr(allTables);
                var navRoomTypesArr = [];
                var navRoomTypes = $('#nav-room-types');
                navRoomTypes.attr('inited', 'fasle');

                //设置餐桌统计
                $('.J-table-nums').find('.all .num').text(tables.all.length)
                    .end().find('.free .num').text(tables.free.length)
                    .end().find('.opened .num').text(tables.opened.length);

                //设置区域
                if (navRoomTypes.attr('inited') !== 'true') {
                    if (tableAreaSelect != -1) {
                        navRoomTypesArr.push('<li  areaid="-1">全部</li>')
                        $.each(res.data, function (key, val) {
                            //判断分区下是否存在餐台
                            if (val.tables) {
                                if (tableAreaSelect == val.areaid) {
                                    navRoomTypesArr.push('<li class="active" areaid="' + val.areaid + '">' + val.areaname + '</li>');
                                }
                                else {
                                    navRoomTypesArr.push('<li  areaid="' + val.areaid + '">' + val.areaname + '</li>');
                                }

                            }

                        });
                    }
                    else {
                        navRoomTypesArr.push('<li class="active"  areaid="-1">全部</li>')
                        $.each(res.data, function (key, val) {
                            //判断分区下是否存在餐台
                            if (val.tables) {
                                navRoomTypesArr.push('<li  areaid="' + val.areaid + '">' + val.areaname + '</li>');
                            }

                        });
                    }


                    navRoomTypes.attr('inited', 'true');
                    navRoomTypes.html(utils.array.unique(navRoomTypesArr).join(''));
                    if (navRoomTypesArr.length > 10) {//当区域数组大于10时，移除不能点击样式
                        $(".nav-type-next").removeClass('unclick');
                    }
                    if (roomtype_prev > 0) {
                        for (var i = 0; i < roomtype_prev; i++) {
                            $('#nav-room-types li').eq(i).css('margin-left', "-10%");
                        }

                    }
                    if ($('#nav-room-types li').length - roomtype_prev == '10') {
                        $(".nav-type-next").addClass('unclick');
                    }
                }

                if (tables[type].length == 0) {
                    $('#J-table-pager').html('');
                    $("#standard-tables").html('');
                } else {
                    //初始化分页
                    var pager = $('#J-table-pager').pagination({
                        dataSource: tables[type],
                        pageSize: 40,
                        showPageNumbers: false,
                        showNavigator: true,
                        pageNumber: tablesCurPager,
                        callback: function (data, pagination) {

                            tablesCurPager = pagination.pageNumber
                            $("#standard-tables").html(data.join(''));
                        }
                    });

                }

            }
        })
    },
    /**
     * 清机
     */
    clearAll: function () {
        var sendInfo={
            'aUserid':utils.storage.getter('aUserid'),
            'fullname':utils.storage.getter('fullname'),
            'ipaddress':utils.storage.getter('ipaddress'),
            'posid':utils.storage.getter('posid'),
            'checkout_fullname':utils.storage.getter('checkout_fullname')
        }
        Log.send(2, '普通清机回传参数有：'+JSON.stringify(sendInfo));
        $("#J-btn-checkout-dialog").modal('hide')
        widget.modal.alert({
            cls: 'fade in',
            content: '<strong>清机中，请稍后</strong>',
            width: 500,
            height: 500,
            hasBtns: false,
        });
        $.ajax({
            url: _config.interfaceUrl.Clearner +''+utils.storage.getter('aUserid')+'/'+utils.storage.getter('fullname')+'/'+utils.storage.getter('ipaddress')+'/'+utils.storage.getter('posid')+'/'+utils.storage.getter('checkout_fullname')+'/',
            type: "get",
            dataType: "text",
            success: function (data) {
                $(".modal-alert:last,.modal-backdrop:last").remove();
                var data = JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                Log.send(2, '清机返回参数有：'+JSON.stringify(data));
                if (data.Data === '0') {//清机失败
                    widget.modal.alert({
                        cls: 'fade in',
                        content: '<strong>' + data.Info + '</strong>',
                        width: 500,
                        height: 500,
                        btnOkTxt: '确定',
                        btnCancelTxt: ''
                    });
                }
                else {//清机成功
                    utils.reprintClear.get()//打印清机单
                    Log.upload();
                    window.location = "../views/login.jsp";
                }
            },
            error: function () {
                $(".modal-alert:last,.modal-backdrop:last").remove();
            }
        });
    },
    /*结业清机*/
    clearAllcheckOut: function () {
        var sendInfo={
            'aUserid':utils.storage.getter('aUserid'),
            'fullname':utils.storage.getter('fullname'),
            'ipaddress':utils.storage.getter('ipaddress'),
            'posid':utils.storage.getter('posid'),
            'checkout_fullname':utils.storage.getter('checkout_fullname')
        }
        Log.send(2, '结业清机回传参数有'+JSON.stringify(sendInfo));
        var that = this
        $("#J-btn-checkout-dialog").modal('hide')
        var that = this;
        widget.modal.alert({
            cls: 'fade in',
            content: '<strong>清机中，请稍后</strong>',
            width: 500,
            height: 500,
            hasBtns: false,
        });
        $.ajax({
            url: _config.interfaceUrl.Clearner + '' + $.trim($('#user').val()) + '/' + utils.storage.getter('fullname') + '/' + utils.storage.getter('ipaddress') + '/' + utils.storage.getter('posid') + '/' + utils.storage.getter('checkout_fullname') + '/',
            type: "get",
            dataType: "text",
            success: function (data) {
                var data = JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                Log.send(2, '结业清机返回参数有'+JSON.stringify(data));
                if (data.Data === '0') {//清机失败
                    $(".modal-alert:last,.modal-backdrop:last").remove();
                    widget.modal.alert({
                        cls: 'fade in',
                        content: '<strong>' + data.Info + '</strong>',
                        width: 500,
                        height: 500,
                        btnOkTxt: '确定',
                        btnCancelTxt: ''
                    });
                }
                else {//清机成功
                    utils.reprintClear.get()//打印清机单
                    $(".modal-alert:last,.modal-backdrop:last").remove();
                    that.checkout()
                }
            }
        });
    },
    checkout: function () {
        var that = this;
        var Uncleandata = that.getFindUncleanPosList();
        var arrylength = Uncleandata.LocalArry.length - 1;
        var LocalArry = Uncleandata.LocalArry;
        $("#J-btn-checkout-dialog").modal('hide')
        if (Uncleandata.LocalArry.length > 0) {
            $("#J-btn-checkout-dialog").load("../views/check/impower.jsp", {
                'title': '清机授权',
                'userNmae': Uncleandata.LocalArry[arrylength].username,
                'usernameDisble': '2',
                'cbd': 'MainPage.clearAllcheckOut()',
                'userRightNo': '030204'
            });
            $("#J-btn-checkout-dialog").modal('show')
        }
        if (Uncleandata.LocalArry.length == 0 && Uncleandata.OtherArry.length > 0) {
            Log.send(2, '还有其他POS机未清机,请到其他POS机上先清机')
            widget.modal.alert({
                cls: 'fade in',
                content: '<strong>还有其他POS机未清机,<br><br>请到其他POS机上先清机</strong>',
                width: 500,
                height: 500,
                btnOkTxt: '重试',
                btnCancelTxt: '',
                btnOkCb: function () {
                    $(".modal-alert:last,.modal-backdrop:last").remove();
                    that.checkout()
                }
            });
        }
        if (Uncleandata.findUncleanPosList.detail.length == '0') {
            $("#J-btn-checkout-dialog").load("../views/check/impower.jsp", {
                'title': '结业授权',
                'cbd': 'MainPage.checkoutCallback()',
                'userRightNo': '030205'
            });
            $("#J-btn-checkout-dialog").modal('show')
        }


    },
    checkoutCallback: function () {//结业回调
        Log.send(2, '结业请求发送中');
        $.ajax({
            url: _config.interfaceUrl.EndWork,//不需要传递参数
            type: "get",
            dataType: 'text',
            success: function (data) {

                $("#J-btn-checkout-dialog").modal('hide')
                var data = JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                Log.send(2, '结业成功返回参数有'+JSON.stringify(data));
                if (data.Data == '1') {

                    /*结业数据上传*/
                    widget.modal.alert({
                        cls: 'fade in',
                        content: '<strong>结业数据上传中，请稍后</strong>',
                        width: 500,
                        height: 500,
                        hasBtns: false,
                    });
                    _EndWorkSyncData();
                    function _EndWorkSyncData() {
                        $.ajax({
                            url: _config.interfaceUrl.EndWorkSyncData,//结业数据上传
                            method: 'POST',
                            contentType: "application/json",
                            dataType: 'json',
                            data: JSON.stringify({
                                'synkey': 'candaosynkey'
                            }),
                            success: function (msg) {
                                $(".modal-alert:last,.modal-backdrop:last").remove();
                                //成功
                                Log.send(2, '结业数据上传返回参数：'+JSON.stringify(msg));
                                if (msg.code == '0000') {
                                    widget.modal.alert({
                                        cls: 'fade in',
                                        content: '<strong>' + data.Info + ',即将退出程序</strong>',
                                        width: 500,
                                        height: 500,
                                        btnOkTxt: '确定',
                                        btnCancelTxt: '',
                                        btnOkCb: function () {
                                            Log.upload()
                                            $(".modal-alert:last,.modal-backdrop:last").remove();
                                            window.location = '../views/openpage.jsp?ipaddress=' + utils.storage.getter('ipaddress') + '&posid=' + utils.storage.getter('posid') + '&cashIp=' + utils.storage.getter('cashIp');
                                            //结业成功清除缓存
                                            utils.clearLocalStorage.clear()
                                        }
                                    });
                                    $('.modal-alert:last .modal-header .close').hide();//隐藏X关闭按钮
                                }
                                //失败
                                else {
                                    widget.modal.alert({
                                        cls: 'fade in printError endwork',
                                        content: '<strong style="text-align: left">上传营业数据失败，请重新上传！<br/>失败原因：' + msg.message + '</strong><br> <br><span style="font-size: 12px;line-height: 25px;padding: 20px 50px 0px 50px;">您可以选择”重新上传“立即重传，或者点击关闭按钮等待系统在凌晨1点到9点自动上传</span>',
                                        width: 500,
                                        height: 500,
                                        btnOkTxt: '重新上传',
                                        btnCancelTxt: '关闭',
                                        btnOkCb: function () {
                                            $(".modal-alert:last,.modal-backdrop:last").remove();
                                            _EndWorkSyncData();
                                        },
                                        btnCancelCb: function () {
                                            $(".modal-alert:last,.modal-backdrop:last").remove();
                                            window.location = '../views/openpage.jsp?ipaddress=' + utils.storage.getter('ipaddress') + '&posid=' + utils.storage.getter('posid') + '&cashIp=' +utils.storage.getter('cashIp');
                                            //结业成功清除缓存
                                            utils.clearLocalStorage.clear()
                                        }
                                    });
                                    $('.modal-alert:last .modal-header .close').hide();//隐藏X关闭按钮
                                }
                            }
                        });
                    }
                }
                else {
                    $(".modal-alert:last,.modal-backdrop:last").remove();
                    widget.modal.alert({
                        cls: 'fade in',
                        content: '<strong>' + data.Info + '</strong>',
                        width: 500,
                        height: 500,
                        btnOkTxt: '确定',
                        btnCancelTxt: ''
                    });
                }
            }
        });
    },
    getFindUncleanPosList: function () {//获取未清机数据列表
        var findUncleanPosList, LocalArry = [], OtherArry = [];

        $.ajax({
            url: _config.interfaceUrl.GetAllUnclearnPosInfoes,
            type: "get",
            async: false,
            dataType: "text",
            success: function (data) {
                Log.send(2, '获取未清机数据列表:' + data);
                findUncleanPosList = JSON.parse(data);
                /*console.log(findUncleanPosList.detail)
                 console.log(findUncleanPosList.result)*/
                if (findUncleanPosList.result === '0') {
                    LocalArry = [];//本机数组集合
                    OtherArry = [];//其他pos登录集合
                    for (var i = 0; i < findUncleanPosList.detail.length; i++) {
                        if (findUncleanPosList.detail[i].ipaddress == utils.storage.getter('ipaddress')) {
                            LocalArry.push(findUncleanPosList.detail[i])
                        }
                        else {
                            OtherArry.push(findUncleanPosList.detail[i])
                        }
                    }
                }
            }
        });
        return {
            findUncleanPosList: findUncleanPosList,
            LocalArry: LocalArry,
            OtherArry: OtherArry,
        }
    },
    /*营业时间,提示结业时间到了*/
    getOpenEndTime:function () {
        var getOpenEndTime=JSON.parse(utils.storage.getter('getOpenEndTime'))
        var time=utils.date.current();
        var endTime=''
        if(getOpenEndTime) {
            if(getOpenEndTime.datetype=='T'){
                endTime=time.split(' ')[0]+' '+getOpenEndTime.endtime
            }
            if(getOpenEndTime.datetype=='N'){
                var a=time.split(' ')[0]
                var b=parseInt(a.substring(8,a.length))+1;
                if(b<10){
                    b='0'+b
                }
                endTime=a.substring(8,2)+b+' '+getOpenEndTime.endtime

            }
            if(Date.parse(new Date(endTime))-Date.parse(new Date(time))<0){
                Log.send(2, '结业时间到了,请及时结业')
                if($('.getOpenEndTimeTimer').length<1){
                    widget.modal.alert({
                        cls: 'fade in getOpenEndTimeTimer',
                        content:'<strong>结业时间到了,请及时结业</strong>',
                        width:500,
                        height:500,
                        btnOkTxt: '',
                        btnCancelTxt: '确定',
                        btnCancelCb:function () {
                            utils.storage.setter("getOpenEndTimeTimer",true)
                            clearInterval(getOpenEndTimeTimer)
                        }
                    });
                }

            }
        }
        console.log(Date.parse(new Date(endTime))-Date.parse(new Date(time)))
    }
};

$(function () {
    MainPage.init();
    if(utils.storage.getter('getOpenEndTimeTimer')!='true'){
        getOpenEndTimeTimer = setInterval(function(){
            MainPage.getOpenEndTime()
        },2000);
    }

});