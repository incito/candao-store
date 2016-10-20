var pref_prev = 0;


//var url = "../views/main.jsp?tips=成功|1|222|成功3";
//window.location.href = encodeURI(encodeURI(url));

var consts = {
    orderid: $('input[name=orderid]').val(),
    tableno: $('input[name=tableno]').val(),
    personnum: $('input[name=personnum]').val(),
    moneyWipeAmount: 0, //抹零
    vipType: utils.storage.getter('vipType'),
    memberAddr: (function () {
        var obj = JSON.parse(utils.storage.getter('memberAddress'));
        if (!utils.object.isEmptyObject(obj)) {
            obj = $.extend(obj, {
                storeurl: obj.vipotherurl.substring(0, obj.vipotherurl.length - 5) + ':8080'
            })
        }
        return obj;
    })()
};

var dom = {
    order: $("#order"),
    openDialog: $("#open-dialog"),//开台权限验证弹窗
    addDishDialog: $("#adddish-dialog"),//点菜弹窗
    giveDishDialog: $("#givedish-dialog"),//赠菜弹窗
    membershipCard: $('#membership-card'),
    selCompanyDialog: $('#selCompany-dialog')
};

var Order = {

    init: function () {

        SetBotoomIfon.init();

        this.initPreferentialType();

        this.updateOrder();

        this.bindEvent();

        this.renderBankList();

        this.renderPayCompany();

        widget.keyboard();

        //搜索键盘初始化
        widget.keyboard({
            target: '.search-btns',
            chirdSelector: 'div'
        });

        //定时更新订单信息
        //setInterval(function(){
        //    Order.updateOrder();
        //},3000)

    },


    bindEvent: function () {

        var that = this;


        /**
         * 开启服务员权限验证
         */
        dom.openDialog.on('click', '.age-type>div', function () {
            var me = $(this);
            me.toggleClass('active');
        });

        dom.openDialog.on('change', '.J-male-num, .J-female-num', function () {
            var maleNum = dom.openDialog.find('.J-male-num').val() === '' ? '0' : dom.openDialog.find('.J-male-num').val();
            var femailNum = dom.openDialog.find('.J-female-num').val() === '' ? '0' : dom.openDialog.find('.J-female-num').val();
            $('.J-tableware-num').val(parseInt(maleNum, 10) + parseInt(femailNum, 10))
        });

        dom.openDialog.on('click', '.J-btn-submit', function () {
            var serverName = dom.openDialog.find('.J-server-name').val();
            var maleNum = dom.openDialog.find('.J-male-num').val() === '' ? '0' : dom.openDialog.find('.J-male-num').val();
            var femailNum = dom.openDialog.find('.J-female-num').val() === '' ? '0' : dom.openDialog.find('.J-female-num').val();

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
                $.ajax({
                    url: _config.interfaceUrl.OpenTable,
                    method: 'POST',
                    contentType: "application/json",
                    data: JSON.stringify({
                        tableNo: consts.tableno,
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
                    }),
                    dataType: 'json',
                    success: function (res) {
                        if (res.code === '0') {
                            console.log(res.data);
                            dom.openDialog.modal('hide');

                            $('input[name=tableno]').val(res.data.orderid)
                            //设置订单信息
                            that.takeOrder();

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

        /**
         * 退菜
         */

        $("#back-dish").click(function () {
            $("#backfood-dialog").find('.avoid').removeClass('active');
            $("#backfood-reason").val('');
            $("#backfood-dialog").modal("show");
            $("#backfood-dialog .avoid").unbind("click").on("click", function () {
                if ($(this).hasClass("active")) {
                    $(this).removeClass("active");
                } else {
                    $(this).addClass("active");
                }
            });
        });

        $('#backfood-dialog .btn-save').click(function () {
            $("#backfood-dialog").modal('hide');
            $('#backfoodnum-dialog').modal('show');
            $('#backfoodnum-dialog').find('input[type=text]').focus();
        });

        $('#backfoodnum-dialog .btn-save').click(function () {
            var dishNum = parseFloat($('#order-dish-table tr.selected .num').text());
            var backDishNum = parseFloat($('#backDishNumIpt').val());
            if (backDishNum > dishNum) {
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>输入的数量不能超过:' + dishNum + '</strong>',
                    width: 500,
                    height: 500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
                return false;
            }

            if ($('#backDishNumIpt').val() === '' || backDishNum === 0) {
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>请输入正确的数量</strong>',
                    width: 500,
                    height: 500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
                return false;
            }

            $("#backfoodnum-dialog").modal('hide');
            $('#backfood-right').load("./check/impower.jsp", {
                "title": "退菜权限",
                "userRightNo": "030102",
                "cbd": "Order.backDish(0)"
            });
            $('#backfood-right').modal('show');
        });

        $("#backfood-reason").click(function () {
            var reason = $("#backfood-reason").val();
            $("#backreasoninput-dialog").modal("show");
            $("#backreason-inp").val(reason);
            $("#backreason-inp").focus();
        });

        var count = 20;
        $("#backreason-inp").keyup(function () {
            var value = $("#backreason-inp").val();
            var c = count;
            if (value != null && value != "") {
                c = count - value.length;
            }
            if (c <= 0) {
                c = 0;
            }
            $("#backreason-count").text(c);

            if (value != null && value != "") {
                $(".clear-btn").removeClass("disabled");
            } else {
                $(".clear-btn").addClass("disabled");
            }
        });

        //清空输入的退菜原因
        $("#backreasoninput-dialog .clear-btn").click(function () {
            $("#backreason-inp").val("");
            $(".clear-btn").addClass("disabled");
        });

        //改变退菜原因
        $("#backreasoninput-dialog .btn-save").click(function () {
            var reason = $("#backreason-inp").val();
            $("#backfood-reason").val(reason);
            $("#backreasoninput-dialog").modal("hide");
        });



        //称重
        $("#weigh-dish").click(function () {
            $('#weight-num').val('');
            $("#weight-dialog").modal("show");
        });

        $("#weight-dialog .btn-save").click(function () {
            var $target = $("#order-dish-table tr.selected");
            var params = {
                "orderId": $('[name=orderid]').val(),
                "dishid": $target.attr('dishid'),
                "primarykey": $target.attr('primarykey'),
                "dishnum": $.trim($('#weight-num').val())
            };
            $.ajax({
                url: _config.interfaceUrl.UpdateDishWeigh,
                method: 'POST',
                contentType: "application/json",
                data: JSON.stringify(params),
                dataType: 'json',
                success: function (res) {
                    if (res.code === '0') {
                        $("#weight-dialog").modal('hide');
                        that.updateOrder();
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

        /**
         * 优惠操作
         */
        dom.order.delegate(".nav-pref-types li.nav-pref-type", 'click', function () {
            var me = $(this);
            me.siblings().removeClass("active").end().addClass("active");
            that.initPreferential(me.attr('preid'));
        });

        //优惠-赠菜
        dom.giveDishDialog.delegate('li', 'click', function () {
            var me = $(this);
            var num = parseInt(me.attr('num'), 10);
            var sel = parseInt(me.attr('sel') || 0, 10);
            if (sel === num) {
                me.removeClass('selected');
                sel = 0;
            } else {
                me.addClass('selected');
                sel++;
            }
            me.attr('sel', sel);
            me.find('.sel').text(sel);
        });

        //选中已选优惠或菜品
        dom.order.delegate("#sel-preferential-table tbody tr, #order-dish-table tbody tr", "click", function () {
            var me = $(this);
            var $btnWeigh = $('#weigh-dish')
            me.siblings().removeClass("selected").end().addClass("selected");

            if (me.parents('table').attr('id') === 'order-dish-table') {
                if (me.attr('dishstatus') === '1') {
                    $btnWeigh.removeClass('disabled');
                } else {
                    $btnWeigh.addClass('disabled');
                }
            }
        });

        //添加优惠
        dom.order.delegate(".preferential-info", 'click', function () {
            var me = $(this);
            var name = me.attr('name');
            var type = me.attr('type');
            var sub_type = me.attr('sub_type');
            var discount = me.attr('discount');
            var free_reason = me.attr('free_reason')
            var $coupnumDialog = $('#coupnum-dialog');
            var $givedishDialog = $('#givedish-dialog');

            that.manageUsePref.set({
                "preferentialid": me.attr('preferential'),
                "disrate": discount === '' ? '0.0' : discount,//折扣
                "type": type,//类型
                "sub_type": sub_type === '' ? null : sub_type,//子类型  优惠券信息

                //"preferentialAmt": "0.00",//总优惠
                //"toalFreeAmount": "0.00",//优免
                //"toalDebitAmount": "0.00",//挂账
                //"toalDebitAmountMany": "0.00",//挂账多收
                //"adjAmout": "0.00",//优免调整

                "preferentialNum": "1",//优惠卷张数 “xx,xx,xx”,
                "preferentialAmout": "0",//手动输入优惠金额
                "isCustom": "0",//是否是用户自己输入

                "dishid": "",//赠菜卷“xx,xx,xx”,
                "unit": "" //“xx,xx,xx”,
            });

            $coupnumDialog.find('.coupname').text('');
            $coupnumDialog.find('.J-pref-ipt').val('');
            //根据不同优惠券,弹出不同modal
            if (type === '05' || type === '03') {
                //团购 || 代金券, 输入数量
                $coupnumDialog.addClass('coupnum-num');
                $coupnumDialog.find('.coupname').text(name);
                $coupnumDialog.find('.inpt-span').text('使用数量');
                $coupnumDialog.modal('show');

            } else if (type === '07' && free_reason === '1') {
                //手工 输入折扣率
                $coupnumDialog.addClass('coupnum-cus-discount');
                $coupnumDialog.find('.inpt-span').text('折扣率');
                $coupnumDialog.modal('show');
            } else if (type === '07' && free_reason === '2') {
                //手工 输入金额(优免)
                $coupnumDialog.addClass('coupnum-cus-free');
                $coupnumDialog.find('.inpt-span').text('优免');
                $coupnumDialog.modal('show');
            } else if (type === '07' && free_reason === '0') {
                //手工 选择赠菜
                $givedishDialog.addClass('coupum-cus-give');

                //更新可选赠菜信息
                that.updateGiveDishInfo(function () {
                    $givedishDialog.modal('show');
                });
            } else {
                //确认框
                var alert = widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>确定使用' + name + '?</strong>',
                    width: 500,
                    height: 500,
                    btnOkCb: function () {
                        alert.close();
                        that.addPref(this);
                    }
                });
            }

            return false;

        });

        //删除或清空优惠
        $('#del-pref,#clear-pref').click(function () {
            var me = $(this);
            var target = $('#sel-preferential-table tbody tr.selected');
            var isClear = me.attr('id') === 'clear-pref';
            if (me.hasClass('disabled')) return false;
            $.ajax({
                url: _config.interfaceUrl.DelPreferential,
                method: 'POST',
                async: true,
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify({
                    clear: isClear ? '1' : '0',
                    DetalPreferentiald: target.attr('preid'),
                    orderid: target.attr('orderid')
                }),
                success: function (res) {
                    if (res.code === '0') {
                        //更新结算信息
                        that.updateTotal(res.data.preferentialInfo);
                        //更新已选优惠
                        that.updateSelectedPref(res.data.preferentialInfo.detailPreferentials, 0);
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

        /*优惠分类向左向右按钮*/
        $(".nav-pretype-next").click(function () {
            var count = $(".nav-pref-types").find("li.nav-pref-type").length;
            if (pref_prev < count - 6) {
                $(".nav-pref-types").find("li.nav-pref-type").eq(pref_prev).css("margin-left", "-16.66%");
                $(".nav-pref-types").find("li.nav-pref-type").eq(pref_prev + 1).click();
                pref_prev++;
            }
        });
        $(".nav-pretype-prev").click(function () {
            if (pref_prev >= 1) {
                $(".nav-pref-types").find("li.nav-pref-type").eq(pref_prev - 1).css("margin-left", "0");
                $(".nav-pref-types").find("li.nav-pref-type").eq(pref_prev - 1).click();
                pref_prev--;
            }
        });


        /**
         * 银行选择
         */

        $('.J-bank-sel').click(function () {
            $("#select-bank-dialog").modal("show");
        });

        $('.bank-icon').delegate('img', 'click', function () {
            var me = $(this);
            me.siblings().removeClass('active').end().addClass('active');
        });

        $('#select-bank-dialog .btn-save').click(function () {
            var target = $('.bank-icon img.active');
            $('input[name=banktype]').val(target.attr('alt'));
            $('input[name=banktype]').attr('btype',target.attr('btype'));
            $('#select-bank-dialog').modal('hide');
        });

        $('.bank-icon').delegate('img', 'click', function () {
            var me = $(this);
            me.siblings().removeClass('active').end().addClass('active');
        });

        $('#select-paycompany-dialog .btn-save').click(function () {
            var target = $('.bank-icon img.active');
            $('input[name=banktype]').val(target.prop('alt'));
            $('#select-bank-dialog').modal('hide');
        });

        /**
         * 合作单位 挂账
         */

        dom.selCompanyDialog.find('[type=search]').bind('input propertychange focus', function () {
            that.renderPayCompany($.trim($(this).val()));
        });

        dom.selCompanyDialog.find('.btn-clear').click(function () {
            dom.selCompanyDialog.find('[type=search]').val('');
            that.renderPayCompany();

        });

        $('.J-selCompany').click(function () {
            dom.selCompanyDialog.modal('show');
        });

        dom.selCompanyDialog.delegate('li', 'click', function () {
            var me = $(this);
            me.addClass('selected').siblings().removeClass('selected');
        });

        dom.selCompanyDialog.find('.btn-save').click(function () {
            var $target = dom.selCompanyDialog.find('li.selected');

            if ($target.length === 0) {
                widget.modal.alert({
                    content: '<strong>请选择挂账单位</strong>',
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
                return;
            }
            $('.payment-unit').val($target.text());
            $('.payment-unit').attr('preferential',$target.attr('preferential'));
            $('#debitAmount').removeAttr('disabled');
            $('.debitAmount').attr({
                'preferential': $target.attr('preferential'),
                'cname': $target.text()
            });

            dom.selCompanyDialog.modal('hide');
        });


        /**
         * 支付方式input修改
         */

        $('.pay-div .J-pay-val,.J-pay-name').bind('input propertychange focus', function () {
            that.payIptEvent($(this));
        });

        /**
         * 会员操作
         */
        dom.membershipCard.find('.card-number').bind('input propertychange focus', function () {
            var me = $(this);
            if (me.val().length > 0) {
                dom.membershipCard.find('.login-btn').removeClass('disabled')
            } else {
                dom.membershipCard.find('.login-btn').addClass('disabled')
            }

        });

        //登录或登出
        dom.membershipCard.find('.login-btn').click(function () {
            that.memberLoginAndOut(!$(this).hasClass('btn-login-out'));
        });
    },

    payIptEvent: function(obj){
        var me = obj;
        var type = me.attr('iptType');
        var $paytotal = $('.pay-total');
        var shouldAmount = parseFloat($("#should-amount").text());
        var iptVal = parseFloat(me.val()).toFixed(2);
        var giveChange = parseFloat(iptVal - shouldAmount).toFixed(2);
        var isGiveChange = giveChange > 0 ? true : false;
        var totalOtherPay = (function () {
            var total = 0;
            $('.pay-div .J-pay-val').each(function () {
                var $me = $(this);
                if ($me.val() !== '' && parseFloat($me.val()) > 0 && $me.attr('iptType') !== 'cash') {
                    total += parseFloat($me.val());
                }
            });
            return total;
        })();

        if(me.hasClass('J-pay-name')) {
            var iptName = me.val();
            var $parent = me.parents('.paytype-input');
            if (iptName.length > 0) {
                $parent.find('.J-pay-val,.J-pay-pwd').removeAttr('disabled');
            } else {
                $parent.find('.J-pay-val, .J-pay-pwd').attr('disabled', 'disabled');
            }
            //$parent.find('.J-pay-val,.J-pay-pwd').val('');

            //return false;
        }

        //if (!this.value.match(/^[\+\-]?\d*?\.?\d*?$/)) this.value = this.t_value; else this.t_value = this.value;

        var target = $paytotal.find('.' + me.attr('iptType'));
        if (iptVal > 0) {
            target.find('span').text(iptVal);
            target.removeClass('hide');
        } else {
            target.find('span').text('');
            target.addClass('hide');
        }

        if (type === 'cash') {
            if (iptVal > 0) {
                $paytotal.find('.payamount').find('span').text(iptVal);
                $paytotal.find('.payamount').removeClass('hide');
                if (isGiveChange) {
                    $paytotal.find('.giveChange span').text(giveChange);
                    $paytotal.find('.giveChange').removeClass('hide');
                } else {
                    $paytotal.find('.giveChange span').text('0.00');
                    $paytotal.find('.giveChange').addClass('hide');
                }
            } else {
                $paytotal.find('.payamount').find('span').text('0.00');
                $paytotal.find('.payamount ,.giveChange').addClass('hide');
            }
        } else {
            if (totalOtherPay >= shouldAmount) {//其他支付大于应收
                $('#cash .J-pay-val').val('0');
                $paytotal.find('.payamount,.giveChange').find('span').text('0.00');
                $paytotal.find('.payamount ,.giveChange').addClass('hide');
            } else {
                $('#cash .J-pay-val').val(shouldAmount - totalOtherPay);
                $paytotal.find('.payamount').find('span').text(shouldAmount - totalOtherPay);
                $paytotal.find('.payamount').removeClass('hide');
            }
        }
    },

    /**
     *
     * @param optype true:登录, false:退出
     * @param memberno 会员编号
     */
    memberLoginAndOut: function (optype, memberno) {
        var that = this;
        var ipt = dom.membershipCard.find('.card-number')
        var cardNumber = memberno || ipt.val();
        var btn = dom.membershipCard.find('.login-btn');

        dom.membershipCard.find('.card-number').val(cardNumber);

        if (optype) {//登录
            if (consts.vipType === '1') {//餐道会员
                $.when(
                    $.ajax({
                        url: consts.memberAddr.vipcandaourl + _config.interfaceUrl.QueryCanDao,
                        method: 'POST',
                        contentType: "application/json; charset=utf-8",
                        dataType: 'json',
                        data: JSON.stringify({
                            "branch_id": utils.storage.getter('branch_id'),
                            "cardno": cardNumber,
                            "password": '',
                            "securityCode": ''
                        })
                    }),
                    $.ajax({
                        url: _config.interfaceUrl.MemberLogin,
                        method: 'POST',
                        contentType: "application/json; charset=utf-8",
                        dataType: 'json',
                        data: JSON.stringify({
                            "mobile": cardNumber,
                            "orderid": consts.orderid,
                        })
                    })
                    )
                    .then(function (res1, res2) {
                        //查询
                        console.log('查询');
                        var res1 = res1[0];
                        if (res1.Retcode === '0') {
                            $('#StoreCardBalance').text(res1.StoreCardBalance);
                            $('#StoreCardBalance').after('<b>(' + res1.CardLevel + ')</b>');
                            $('#IntegralOverall').text(res1.IntegralOverall);
                            btn.text('退出');
                            btn.addClass('btn-login-out');
                            btn.removeClass('disabled');
                            ipt.attr('disabled', 'disabled');


                            //重新刷新订单信息
                            that.updateOrder();

                            rightBottomPop.alert({
                                title: "提示信息",
                                content: res1.RetInfo,
                                width: 320,
                                height: 200,
                                right: 5
                            });
                        } else {
                            widget.modal.alert({
                                cls: 'fade in',
                                content: '<strong>' + res1.RetInfo + '</strong>',
                                width: 500,
                                height: 500,
                                btnOkTxt: '',
                                btnCancelTxt: '确定'
                            });
                        }

                        //登录
                        var res2 = res2[0];
                        if (res2.code === '0') {

                            dom.membershipCard.attr('isLogin','true');

                            rightBottomPop.alert({
                                title: "提示信息",
                                content: res2.msg,
                                width: 320,
                                height: 200,
                                right: 5
                            });
                        } else {
                            widget.modal.alert({
                                cls: 'fade in',
                                content: '<strong>' + res2.msg + '</strong>',
                                width: 500,
                                height: 500,
                                btnOkTxt: '',
                                btnCancelTxt: '确定'
                            });
                        }
                    });
            } else {//雅座
                debugger;
                $.ajax({
                    url: 'http://10.66.21.5:9081/datasnap/rest/TServerMethods1/QueryBalance/' + cardNumber + '/',
                    method: 'POST',
                    contentType: "application/json; charset=utf-8",
                    dataType: 'json',
                }).then(function (res) {
                    debugger;
                    console.log(res);
                })
            }
        } else {//登出
            if (consts.vipType === '1') {//餐道会员
                $.ajax({
                    url: _config.interfaceUrl.MemberLogout,
                    method: 'POST',
                    contentType: "application/json; charset=utf-8",
                    dataType: 'json',
                    data: JSON.stringify({
                        "moblie": cardNumber,
                        "orderid": consts.orderid,
                    })
                }).then(function (res) {
                    if (res.code === '0') {
                        rightBottomPop.alert({
                            title: "提示信息",
                            content: res.msg,
                            width: 320,
                            height: 200,
                            right: 5
                        });
                        $('#StoreCardBalance').text('');
                        $('#IntegralOverall').text('');
                        btn.text('登录');
                        btn.removeClass('btn-login-out');
                        btn.addClass('disabled');
                        ipt.removeAttr('disabled');
                        ipt.val('');
                        dom.membershipCard.attr('isLogin','false');

                        //重新刷新订单信息
                        that.updateOrder();

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
                })
            }
        }
    },

    //挂账单位
    renderPayCompany: function (key) {
        var payCompany = utils.storage.getter('payCompany');
        var ret = [];
        var searchKey = !key ? $.trim(dom.selCompanyDialog.find('[type=search]').val()) : key;
        if (payCompany !== null) {
            $.each(JSON.parse(payCompany), function (k, v) {
                if (v.name_first_letter.indexOf(searchKey.toUpperCase()) !== -1) {
                    ret.push('<li py="' + v.name_first_letter + '" preferential="' + v.preferential + '">' + v.name + '</li>');
                }
            });

            if (ret.length < 11) {
                $('#J-company-pager').html('');
                $('.campany-icon').html(ret.join(''));
            } else {
                $('#J-company-pager').pagination({
                    dataSource: ret,
                    pageSize: 10,
                    showPageNumbers: false,
                    showNavigator: true,
                    callback: function (data) {
                        $('.campany-icon').html(data.join(''));
                    }
                });
            }
        }
    },


    //银行列表
    renderBankList: function () {
        var banklist = utils.storage.getter('banklist');
        var htm = '';

        if (banklist !== null) {
            $.each(JSON.parse(banklist), function () {
                var me = $(this)[0];
                htm += '<img alt="' + me.itemDesc + '" itemDesc="' + me.itemDesc + '"  btype="'+ me.itemid + '" src="../images/bank/' + me.itemid + '.png">'
            });
            $('.bank-icon').html(htm);
        }
    },

    //type:1 预结单, 3:客用单
    printPay: function (type) {
        $.ajax({
            url: _config.interfaceUrl.PrintPay + '/' + utils.storage.getter('aUserid') + '/' + $('[name=orderid]').val() + '/' + type + '/' + utils.storage.getter('posid'),
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            success: function (res) {
                var str = (function(){
                    var ret = ''
                   if(type === 1) {
                       ret = (res.msg === '' ? '预结单打印完毕' : res.msg)
                   } else {
                       ret = (res.msg === '' ? '客用单打印完毕' : res.msg)
                   }
                    return ret;
                })();
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>' + str + '</strong>',
                    width: 500,
                    height: 500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
            }
        });
    },

    //开钱箱
    openCash: function () {
        $.ajax({
            url: _config.interfaceUrl.OpenCash + '/127.0.0.1/',
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            success: function (res) {
                if (res.result[0] === '1') {//成功

                }
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>' + (res.Info === undefined ? '打开钱箱成功' : res.Info) + '</strong>',
                    width: 500,
                    height: 500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
            }
        });
    },

    //抹零不处理
    consumInfo: function () {
        var that = this;
        var $moneyWipeAmount = $('.pay-total .moneyWipeAmount span');
        var moneyWipeAmount = $moneyWipeAmount.text();
        var $target = $('.paytype-input input[type=text]');
        if ($moneyWipeAmount.length === 0 || moneyWipeAmount === '0') return false;
        $moneyWipeAmount.parents('li').remove();
        consts.moneyWipeAmount = 0.0;
        $target.val((parseFloat($target.val()) + parseFloat(moneyWipeAmount)).toFixed(2));
    },

    //取消订单
    cancelOrder: function () {
        var $target = $('#order-dish-table tbody');

        if ($target.find('tr').length > 0) {
            widget.modal.alert({
                cls: 'fade in',
                content: '<strong>只能取消空账单</strong>',
                width: 500,
                height: 500,
                btnOkTxt: '',
                btnCancelTxt: '确定'
            });
        } else {
            $.ajax({
                url: _config.interfaceUrl.ClearTable,
                method: 'POST',
                contentType: "application/json",
                data: JSON.stringify({
                    tableNo: $('[name=tableno]').val()
                }),
                dataType: 'json',
                success: function (res) {
                    debugger;
                    if (res.code === '0') {
                        window.location.href = './main.jsp'
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
            });
        }
    },

    //退菜 0:单个 1:整单
    backDish: function (type) {
        debugger;
        var that = this;
        var $target = $("#order-dish-table tr.selected");
        var tableId = $('[name=tableno]').val();
        var userId = $('#user').val();
        var orderNo = $('[name=orderid]').val();
        var params = {};

        var discardReason = (function () {
            var str = '';
            $('#backfood-dialog .avoid.active').each(function () {
                str += $(this).text();
            });
            str += $.trim($('#backfood-reason').val());
            return str.substring(0, str.length - 1);
        })();


        if (type === 0) {
            params = {
                "operationType": 2,
                "primarykey": $target.attr('primarykey'),
                "sequence": 999999,
                "userName": utils.storage.getter('pos_aUserid'),
                "dishunit": $target.attr('unit'),
                "dishNum": $('#backDishNumIpt').val(),
                "dishtype": $target.attr('dishtype'),
                "dishNo": $target.attr('dishid'),
                "actionType": "0",
                "currenttableid": tableId,
                "discardReason": discardReason,
                "discardUserId": userId,
                "orderNo": orderNo
            };
        } else {
            params = {
                "actionType": "1",
                "currenttableid": tableId,
                "discardReason": discardReason,
                "discardUserId": userId,
                "orderNo": orderNo
            }
        }

        $.ajax({
            url: _config.interfaceUrl.BackDish,
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify(params),
            dataType: 'json',
            success: function (res) {
                if (res.code === '0') {
                    $("#backfood-right").modal('hide');
                    $("#backfood-right .modal-dialog").remove();

                    that.updateOrder();
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
        });

    },

    //切换小键盘
    changeKeyboard: function (type) {
        if (type == "num") {
            $("#num-keyboard").removeClass("hide");
            $("#letter-keyboard").addClass("hide");
        } else if (type == "letter") {
            $("#num-keyboard").addClass("hide");
            $("#letter-keyboard").removeClass("hide");
        }
    },

    //点菜
    takeOrder: function () {
        var url = "../views/orderdish.jsp?orderid=" + consts.orderid + '&personnum=' + consts.personnum + '&tableno=' + consts.tableno;
        window.location.href = encodeURI(encodeURI(url));
    },

    /**
     * 优惠
     */

    //加载优惠分类
    initPreferentialType: function () {
        var ret = [];
        var that = this;
        $.each(_config.preferential, function (k, v) {
            var cla = "";
            if (k === '05') {
                cla = "active";
            }
            ret.push('<li class="nav-pref-type ' + cla + '" preid="' + k + '">' + v + '</li>');
        });

        $(".nav-pref-types").html(ret.join(''));
        that.initPreferential();
    },

    //通过分类获取优惠券信息
    initPreferential: function (id) {
        if (arguments.length < 1) {
            id = '05';
        }

        $.ajax({
            url: _config.interfaceUrl.GetCouponInfos,
            method: 'POST',
            async: true,
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                machineno: "96121CBC21EF02256E9C5F2E602C5441",
                userid: utils.storage.getter('aUserid'),
                orderid: '0',
                typeid: id
            }),
            success: function (res) {
                var htm = '';
                $.each(res, function (k, v) {
                    htm += '<div class="preferential-info"' +
                        ' type="' + v.type + '"' +
                        ' free_reason="' + v.free_reason + '"' +
                        ' preferential="' + v.preferential + '"' +
                        ' name="' + v.name + '"' +
                        ' discount="' + v.discount + '"' +
                        ' sub_type="' + v.sub_type + '"' +
                        ' type_name="' + v.type_name + '" >'
                        + '<div class="dish-name">' + v.name + '</div>' + '</div>';
                });
                $(".main-div .preferentials-content").html(htm);

                widget.loadPage({
                    obj: ".preferentials-content .preferential-info",
                    listNum: 16,
                    currPage: 0,
                    totleNums: $(".preferentials-content .preferential-info").length,
                    curPageObj: "#order-modal #curr-page3",
                    pagesLenObj: "#order-modal #pages-len3",
                    prevBtnObj: "#order-modal .main-div .prev-btn",
                    nextBtnObj: "#order-modal .main-div .next-btn"
                });
            }
        })
    },

    /**
     * 添加优惠
     */
    addPref: function (target) {
        var that = this;
        if (arguments.length > 0) {
            var targetModal = $(target).closest('.modal');
            var val = targetModal.find('.J-pref-ipt').val();

            if (targetModal.hasClass('coupnum-num')) {
                //输入优惠券数量
                that.manageUsePref.set({
                    preferentialNum: val
                });
            } else if (targetModal.hasClass('coupnum-cus-discount')) {
                //手动输入折扣
                that.manageUsePref.set({
                    isCustom: '1',
                    disrate: val
                });
            } else if (targetModal.hasClass('coupnum-cus-free')) {
                //手动输入优免
                that.manageUsePref.set({
                    isCustom: '1',
                    preferentialAmout: val
                });
            } else if (targetModal.hasClass('coupnum-cus-give')) {
                var $giveDishDialog = $('#givedish-dialog');
                var $li = $giveDishDialog.find('li');
                var preferentialNum = [];
                var dishid = [];
                var unit = [];


                $.each($li, function () {
                    var me = $(this);
                    var sel = parseInt(me.attr('sel'), 10);
                    var num = parseInt(me.attr('num'), 10);

                    if (me.hasClass('selected')) {
                        preferentialNum.push(sel);
                        dishid.push(me.attr('dishid'));
                        unit.push(me.attr('unit'));
                    }
                });

                //手动输入赠菜
                that.manageUsePref.set({
                    isCustom: '1',
                    "preferentialNum": preferentialNum.join(','),//优惠卷张数 “xx,xx,xx”,
                    "dishid": dishid.join(','),//赠菜卷“xx,xx,xx”,
                    "unit": unit.join(',') //“xx,xx,xx”,
                });
            }
        }
        $.ajax({
            url: _config.interfaceUrl.CalcDiscountAmount,
            method: 'POST',
            async: true,
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(that.manageUsePref.get()),
            success: function (res) {
                if (res.code === '0') {
                    console.log(res);


                    that.updateTotal(res.data);
                    //更新已选优惠
                    that.updateSelectedPref(res.data.detailPreferentials, 1);

                    $("#coupnum-dialog,#givedish-dialog").modal("hide");

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
     * 更新赠菜信息
     */
    updateGiveDishInfo: function (cb) {
        $.when(
            $.ajax({
                url: _config.interfaceUrl.GetOrderInfo,
                method: 'POST',
                contentType: "application/json",
                data: JSON.stringify({
                    orderid: consts.orderid,
                }),
                dataType: 'json'
            }),
            $.ajax({
                url: _config.interfaceUrl.GivePrefer,
                method: 'POST',
                async: true,
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify({
                    orderId: consts.orderid
                })
            })
        ).then(function (res1, res2) {
            var data = res1[0].data.rows;
            //debugger;
            if (res1[0].code === '0' && res2[0].code === '0') {
                var htm = '';
                $.each(data, function (k, v) {
                    var dishnum = parseInt(v.dishnum, 10);
                    $.each(res2[0].data, function (key, value) {
                        var left = dishnum - parseInt(value.count, 10);
                        if (value.dishid === v.dishid) {
                            v.dishnum = left;
                        }
                    });
                });
                console.log(data);
                $.each(data, function (k, v) {
                    var cls = v.dishnum > 0 ? '' : 'display:none';
                    if (parseInt(v.orderprice, 10) > 0) {
                        htm += "<li style='" + cls + "' dishid='" + v.dishid + "' unit='" + v.dishunit + "' num='" + v.dishnum + "'>" +
                            "<span class='dishname'>" + v.dishname + "</span>" +
                            "<span class='info'><span class='sel'>0</span>/<span class='num'>" + v.dishnum + "</span></span>" +
                            "</li>";
                    }

                });

                $('#givedish-dialog .give-dish-list').html(htm);

                cb && cb();
            }
            else {
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>' + res1.msg + ' ' + res2.msg + '</strong>',
                    width: 500,
                    height: 500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
            }
        }, function () {
            widget.modal.alert({
                cls: 'fade in',
                content: '<strong>接口错误</strong>',
                width: 500,
                height: 500,
                btnOkTxt: '',
                btnCancelTxt: '确定'
            });
        });


    },

    /**
     * 更新已选优惠
     * @param data
     * @param opType 操作类型(0:重新渲染, 1:新增)
     */
    updateSelectedPref: function (data,opType) {
        var tr = '';
        var $body = $("#sel-preferential-table tbody");
        var isToalDebitAmount;//是否是团购挂账
        //设置优惠回传值
        $.each(data, function (k, v) {
            isToalDebitAmount = parseFloat(v.toalDebitAmount) > 0 ? true : false;
            if(isToalDebitAmount) {
                tr += "<tr preid='" + v.id + "' toaldebitamount='" + v.toalDebitAmount + "' toalfreeamount='" + v.toalFreeAmount + "' istoaldebitamount='" + isToalDebitAmount+ "' orderid='" + v.orderid + "' coupondetailid='" + v.coupondetailid + "' preferential = '" + v.preferential + "'><td class='name'>" + v.activity.name + "</td><td class='num'>" + 1 + "</td><td class='amount'>" + v.deAmount + "</td></tr>";
            } else {
                tr += "<tr preid='" + v.id + "' istoaldebitamount='" + isToalDebitAmount+ "' orderid='" + v.orderid + "' coupondetailid='" + v.coupondetailid + "' preferential = '" + v.preferential + "'><td class='name'>" + v.activity.name + "</td><td class='num'>" + 1 + "</td><td class='amount'>" + v.deAmount + "</td></tr>";
            }

        });
        if(opType === 0) {
            $("#sel-preferential-table tbody").html(tr);
        } else {
            $("#sel-preferential-table tbody").prepend(tr);
        }

        widget.loadPage({
            obj: "#sel-preferential-table tbody tr",
            listNum: 6,
            currPage: 0,
            totleNums: $body.find('tr').length,
            curPageObj: ".preferential-oper-btns .page-info span:first",
            pagesLenObj: ".preferential-oper-btns .page-info span:last",
            prevBtnObj: ".preferential-oper-btns .prev-btn",
            nextBtnObj: ".preferential-oper-btns .next-btn",
            callback: function () {
                $body.find('tr').removeClass("selected");
                $body.find('tr').not(".hide").eq(0).addClass(
                    "selected");
            }
        });

        /**
         * 优惠购物车按钮
         */
        if ($body.find("tr.selected").length > 0) {
            $("#del-pref").removeClass("disabled");
            $("#clear-pref").removeClass("disabled");
        } else {
            $("#del-pref").addClass("disabled");
            $("#clear-pref").addClass("disabled");
        }
    },

    /**
     * 更新统计信息
     * @param data
     */
    updateTotal: function (data) {
        var that = this;
        var toalFreeAmount = data.toalFreeAmount;
        var toalDebitAmount = data.toalDebitAmount;
        var moneyWipeAmount = data.moneyWipeAmount;
        var toalDebitAmountMany = data.toalDebitAmountMany;
        var tipAmount = data.tipAmount;
        var adjAmout = data.adjAmout;
        var payamount = data.payamount;
        var originalOrderAmount = data.menuAmount;
        var amount = data.amount;
        var totalHtml = '<ul class="pay-total"> ';

        consts.moneyWipeAmount = moneyWipeAmount;

        //设置统计
        $('#discount-amount').text(amount);
        $('#amount').text(originalOrderAmount);
        $('#should-amount').text(payamount);
        $('#cash input').val(payamount);

        $('.pay-total').remove();


        totalHtml += '<li class="' + (parseFloat(toalDebitAmount) !== 0 ? '' : 'hide') + ' toalDebitAmount">挂账:<span>' + toalDebitAmount + '</span></li> ';
        totalHtml += '<li class="' + (parseFloat(toalFreeAmount) !== 0 ? '' : 'hide') + ' toalFreeAmount">优免:<span>' + toalFreeAmount + '</span></li> ';
        totalHtml += '<li class="' + (parseFloat(moneyWipeAmount) !== 0 ? '' : 'hide') + ' moneyWipeAmount">抹零:<span>' + moneyWipeAmount + '</span></li> ';
        totalHtml += '<li class="' + (parseFloat(adjAmout) !== 0 ? '' : 'hide') + ' adjAmout">优免调整:<span>' + adjAmout + '</span></li> ';
        totalHtml += '<li class="' + (parseFloat(toalDebitAmountMany) !== 0 ? '' : 'hide') + ' toalDebitAmountMany">挂账多收:<span>' + toalDebitAmountMany + '</span></li> ';
        totalHtml += '<li class="' + (parseFloat(payamount) !== 0 ? '' : 'hide') + ' payamount" payway="0">现金:<span>' + payamount + '</span></li> ';
        totalHtml += '<li class="' + (parseFloat(tipAmount) !== 0 ? '' : 'hide') + ' tipAmount" >小费:<span>' + tipAmount + '</span></li> ';

        totalHtml += '<li class="hide giveChange">找零:<span></span></li> ';
        totalHtml += '<li class="hide bank" payway="1">银行卡:<span></span></li> ';
        totalHtml += '<li class="hide memberCash"  payway="8">会员消费:<span></span></li> ';
        totalHtml += '<li class="hide memberJf"  payway="8">会员积分:<span></span></li> ';
        totalHtml += '<li class="hide debitAmount" payway="5">挂账支付:<span></span></li> ';
        totalHtml += '<li class="hide alipay" payway="18">支付宝:<span></span></li> ';
        totalHtml += '<li class="hide wpay" payway="17">微信:<span></span></li> ';

        totalHtml += '</ul>';

        $('.pay-div').after(totalHtml);


        //设置支付信息
        $('.pay-div .J-pay-val,.pay-div .J-pay-name').each(function(){
            that.payIptEvent($(this));
        });

        //设置优惠回传值
        that.manageUsePref.set({
            "preferentialAmt": amount,//总优惠
            "toalFreeAmount": toalFreeAmount,//优免
            "toalDebitAmount": toalDebitAmount,//挂账
            "toalDebitAmountMany": toalDebitAmountMany,//挂账多收
            "adjAmout": adjAmout,//优免调整
        });
    },

    /**
     * 更新支付方式信息
     * @param data
     */
    updatePayTypeInfo: function (data) {

    },


    /**
     * 管理每次使用优惠信息接口
     */
    manageUsePref: (function () {
        var orderId = consts.orderid;
        var prefInfoCache = {
            "orderid": orderId
        }

        var _init = function (obj) {
            _set(obj);
        };

        var _set = function (obj) {
            prefInfoCache = $.extend({}, prefInfoCache, obj);
        };

        var _get = function (key) {
            if (arguments.length > 0) {
                return prefInfoCache[key]
            } else {
                return prefInfoCache;
            }
        };
        return {
            set: _set,
            get: _get,
            init: _init
        }
    })(),

    updateOrderStatus: 0, //1 正在进行 0 空闲

    /**
     * 更新订单信息
     */
    updateOrder: function () {
        var that = this;

        if(that.updateOrderStatus === 1) {
            return false;
        } else {

            that.updateOrderStatus = 1;
            $.ajax({
                url: _config.interfaceUrl.GetOrderInfo,
                method: 'POST',
                contentType: "application/json",
                data: JSON.stringify({
                    orderid: consts.orderid
                }),
                dataType: 'json',
                async: false,
                success: function (res) {
                    if (res.code === '0') {

                        if (utils.object.isEmptyObject(res.data)) return false;

                        that.updateTotal(res.data.preferentialInfo);

                        //初始化会员信息
                        if (res.data.userOrderInfo.memberno.length > 0 && $('.login-btn.btn-login-out').length === 0 ) {
                            //登录
                            that.memberLoginAndOut(true, res.data.userOrderInfo.memberno);
                        }


                        //已经选择菜品
                        var tr = '';
                        var $body = $("#order-dish-table tbody");

                        if (res.data.rows.length > 0) {
                            $.each(res.data.rows, function (k, v) {
                                tr += "<tr dishid='" + v.dishid + "' unit='" + v.dishunit + "' primarykey='" + v.primarykey + "' dishtype='" + v.dishtype + "' dishstatus='" + v.dishstatus + "'><td class='dishname'>" + v.dishname + "</td><td class='num'>" + v.dishnum + "</td><td class='unit'>" + v.dishunit + "</td><td class='orderprice'>" + (v.dishstatus === '0' ? v.orderprice * v.dishnum : '待称重') + "</td></tr>";

                                if(v.dishes !== undefined) {
                                    $.each(v.dishes, function(k1, v1){
                                        tr += "<tr dishid='" + v1.dishid + "' unit='" + v1.dishunit + "' primarykey='" + v1.primarykey + "' dishtype='" + v1.dishtype + "' dishstatus='" + v1.dishstatus + "'><td class='dishname'>" + v1.dishname + "</td><td class='num'>" + v1.dishnum + "</td><td class='unit'>" + v1.dishunit + "</td><td class='orderprice'>" + (v1.dishstatus === '0' ? parseFloat(v1.orderprice * v1.dishnum).toFixed(2) : '待称重') + "</td></tr>";
                                    })
                                }

                            });
                            $('#back-dish, #backDishAll, #reprintOrder').removeClass('disabled');
                        } else {
                            $('#back-dish, #backDishAll, #reprintOrder').addClass('disabled');
                        }

                        $body.html(tr);

                        widget.loadPage({
                            obj: "#order-dish-table tbody tr",
                            listNum: 6,
                            currPage: 0,
                            totleNums: $body.find('tr').length,
                            curPageObj: "#order-modal #curr-page1",
                            pagesLenObj: "#order-modal #pages-len1",
                            prevBtnObj: "#order-modal .dish-oper-btns .prev-btn",
                            nextBtnObj: "#order-modal .dish-oper-btns .next-btn",
                            callback: function () {
                                $body.find('tr').removeClass("selected");
                                $body.find('tr').not(".hide").eq(0).addClass(
                                    "selected");
                            }
                        });

                        //初始化已经使用的优惠
                        if (res.data.preferentialInfo.detailPreferentials.length > 0) {
                            that.updateSelectedPref(res.data.preferentialInfo.detailPreferentials, 0);
                        }
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

                    that.updateOrderStatus = 0;
                },
                error: function(){
                    that.updateOrderStatus = 0;
                }
            });
        }

    },

    /**
     * 结账
     */
    doSettlement: function () {
        //会员卡支付方式检查
        var memberTips = '';

        if($('#order-dish-table tbody tr').length === 0){
            widget.modal.alert({
                content:'<strong>不能结账空账单</strong>',
                btnOkTxt: '确定',
                btnCancelTxt: ''
            });
            return ;
        }
        if(dom.membershipCard.attr('isLogin') === 'true') {
            if(parseFloat($('#memberCash').val()) > parseFloat($("#StoreCardBalance").text())) {
                memberTips += '会员储值余额不足;<br/>';
            }

            if(parseFloat($('#memberJf').val()) > parseFloat($("#IntegralOverall").text())) {
                memberTips += '积分不足;<br/>';
            }

            if($('.J-pay-pwd').val().length === 0 && (parseFloat($('#memberCash').val()) > 0  || parseFloat($('#memberJf').val()) > 0)) {
                memberTips += '请输入会员密码;';
            }

            if(memberTips.length > 0) {
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>' + memberTips + '</strong>',
                    width: 500,
                    height: 500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
                return false;
            }
        }

        var doSettlementModal = widget.modal.alert({
            cls: 'fade in',
            content: '<strong>桌号:[' + consts.tableno + ']确认现在结算?</strong>',
            width: 500,
            height: 500,
            btnOkTxt: '确定',
            btnCancelTxt: '',
            btnOkCb: function () {
                //支付方式：0-现金，1-银行卡，5-团购券挂账，6-优免，8-会员卡消费，11-积分, 13-单位挂账，17-微信，18-支付宝
                var rows = (function () {
                    var result = [{
                        "payWay": "0",
                        "payAmount": parseFloat($("input[name=cash]").val()),
                        "memerberCardNo": "",
                        "bankCardNo": "",
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": ""
                    }, {
                        "payWay": "1",
                        "payAmount": (function(){
                            var result = 0.0;
                            if($('[name=bank]').val().length > 0) {
                                result = parseFloat($('[name=bank]').val())
                            }
                            return result;
                        })(),
                        "memerberCardNo": (function(){
                            var result = '';
                            if($('[name=banktype]').attr('btype') !== undefined) {
                                result = $('[name=banktype]').attr('btype');
                            }
                            return result;
                        })(),
                        "bankCardNo": $('#bankno').val(),
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": ""
                    }, {
                        "payWay": "8",
                        "payAmount": 0.0,
                        "memerberCardNo": $('input[name=cardNumber]').val(),
                        "bankCardNo": "",
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": ""
                    }, {
                        "payWay": "11",
                        "payAmount": 0.0,
                        "memerberCardNo": $('input[name=cardNumber]').val(),
                        "bankCardNo": "",
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": ""
                    }, {
                        "payWay": "18",
                        "payAmount": (function(){
                            var result = 0.0;
                            if($('[name=alipay]').val().length > 0) {
                                result = parseFloat($('[name=alipay]').val())
                            }
                            return result;
                        })(),
                        "memerberCardNo": "",
                        "bankCardNo": $('[name=alipayName]').val(),
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": ""
                    }, {
                        "payWay": "17",
                        "payAmount": (function(){
                            var result = 0.0;
                            if($('[name=wpay]').val().length > 0) {
                                result = parseFloat($('[name=wpay]').val())
                            }
                            return result;
                        })(),
                        "memerberCardNo": "",
                        "bankCardNo": $('[name=wpayName]').val(),
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": ""
                    }, {
                        "payWay": "13",
                        "payAmount": (function(){
                            var result = 0.0;
                            if($('[name=debitAmount]').val().length > 0) {
                                result = parseFloat($('[name=debitAmount]').val())
                            }
                            return result;
                        })(),
                        "memerberCardNo": '',
                        "bankCardNo": $('[name=debitAmountName]').val(),
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": (function(){
                            var result = '';
                            if($('[name=debitAmount]').val().length > 0) {
                                result = $('[name=debitAmountName]').attr('preferential');
                            }
                            return result;
                        })()
                    }];



                    //抹零
                    result.push({
                        "payWay": "7",
                        "payAmount": parseFloat(consts.moneyWipeAmount),
                        "memerberCardNo": "",
                        "bankCardNo": "",
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": ""
                    });

                    //挂账多收
                    if(!$('.toalDebitAmountMany').hasClass('hide')) {
                        result.push( {
                            "payWay": "5",
                            "payAmount": parseFloat($('.toalDebitAmountMany').find('span').text()),
                            "memerberCardNo": "",
                            "bankCardNo": "挂账多收",
                            "couponnum": "0",
                            "couponid": "",
                            "coupondetailid": ""
                        })
                    }

                    //优免调整
                    if(!$('.adjAmout').hasClass('hide')) {
                        result.push({
                            "payWay": "6",
                            "payAmount": parseFloat($('.adjAmout').find('span').text()),
                            "memerberCardNo": "",
                            "bankCardNo": "优免调整",
                            "couponnum": "0",
                            "couponid": "",
                            "coupondetailid": ""
                        })
                    }

                    //优惠相关
                    $('#sel-preferential-table tbody tr').each(function () {
                        var me = $(this);
                        //团购类优惠券挂账
                        if(me.attr('istoaldebitamount') === 'true'){
                        	result.push({
                        		"payWay": "5",
                        		"payAmount": parseFloat(me.attr('toaldebitamount')),
                        		"memerberCardNo": "",
                        		"bankCardNo": me.find('.name').text(),
                        		"couponnum": me.find('.num').text(),
                        		"couponid": me.attr('preferential'),
                        		"coupondetailid": me.attr('coupondetailid')
                        	});
                            result.push({
                                "payWay": "6",
                                "payAmount": parseFloat(me.attr('toalfreeamount')),
                                "memerberCardNo": "",
                                "bankCardNo": me.find('.name').text(),
                                "couponnum": me.find('.num').text(),
                                "couponid": me.attr('preferential'),
                                "coupondetailid": me.attr('coupondetailid')
                            });
                        } else {
                            result.push({
                                "payWay": "6",
                                "payAmount": parseFloat(me.find('.amount').text()),
                                "memerberCardNo": "",
                                "bankCardNo": me.find('.name').text(),
                                "couponnum": me.find('.num').text(),
                                "couponid": me.attr('coupondetailid'),
                                "coupondetailid": me.attr('coupondetailid')
                            });
                        }
                    });

                    return result;
                })();

                doSettlementModal.close();

                //结算
                utils.loading.open('正在加载…')
                $.ajax({
                    url: _config.interfaceUrl.PayTheBill,
                    method: 'POST',
                    contentType: "application/json",
                    dataType: 'json',
                    global: false,
                    data: JSON.stringify({
                            "payDetail": rows, "userName": utils.storage.getter('aUserid'), "orderNo": consts.orderid
                        }
                    ),
                    success: function (res) {
                        if(res.result === '0') {
                            if(utils.getUrl.get('referer') === '1') {
                                window.history.back(-1);
                            } else {
                                window.location.href = './main.jsp?tips=';
                            }

                        } else {
                            utils.loading.remove();
                            widget.modal.alert({
                                content:'<strong>结账失败,请稍后重试</strong>',
                                btnOkTxt: '确定',
                                btnCancelTxt: ''
                            });
                        }
                    }
                });
            }
        })
    }
};


$(document).ready(function () {

    Order.init();

    $("img.img-close").hover(function () {
        $(this).attr("src", "../images/close-active.png");
    }, function () {
        $(this).attr("src", "../images/close-sm.png");
    });
    //未开过台的先开台
    //if(!g_isopened){
    //	$("#open-dialog").modal("show");
    //}

    ////已点菜上一页
    //$(".dish-oper-btns .prev-btn").click(function() {
    //	if ($(this).hasClass("disabled")) {
    //		return false;
    //	}
    //	page1(nowPage1 - 1);
    //});
    ////已点菜下一页
    //$(".dish-oper-btns .next-btn").click(function() {
    //	if ($(this).hasClass("disabled")) {
    //		return false;
    //	}
    //	page1(nowPage1 + 1);
    //});


    //已选优惠上一页
    //$(".preferential-oper-btns .prev-btn").click(
    //		function() {
    //			if ($(this).hasClass("disabled")) {
    //				return false;
    //			}
    //			page2(nowPage2 - 1);
    //		});
    ////已选优惠下一页
    //$(".preferential-oper-btns .next-btn").click(
    //		function() {
    //			if ($(this).hasClass("disabled")) {
    //				return false;
    //			}
    //			page2(nowPage2 + 1);
    //		});
    ////优惠上一页
    //$(".main-div .prev-btn").click(function() {
    //	if ($(this).hasClass("disabled")) {
    //		return false;
    //	}
    //	page3(nowPage3 - 1);
    //});
    ////优惠下一页
    //$(".main-div .next-btn").click(function() {
    //	if ($(this).hasClass("disabled")) {
    //		return false;
    //	}
    //	page3(nowPage3 + 1);
    //});

    $(".tab-payment ul li").click(function () {
        $(".tab-payment ul li").removeClass("active");
        $(this).addClass("active");

        $(".paytype-input").addClass("hide");
        var targetId = $(this).attr("target");
        $(targetId).removeClass("hide");
    });

    //initPreferentialType();


    $(document).click(function (e) {
        $(".more-oper").addClass("hide");
        e.stopPropagation();
    });
    $(".show-more").click(function (e) {
        $(".more-oper").removeClass("hide");
        e.stopPropagation();
    });

    //清空购物车优惠
});


///**
// * 退菜
// * @param type： 0：单个退菜；1:整单退菜
// */
//function backFood(type){
//
//}
// 已点菜品分页
//function page1(currPage) {
//	nowPage1 = widget.loadPage({
//		obj : "#order-dish-table tbody tr",
//		listNum : 6,
//		currPage : currPage,
//		totleNums : $("#order-dish-table tbody tr").length,
//		curPageObj : "#order-modal #curr-page1",
//		pagesLenObj : "#order-modal #pages-len1",
//		prevBtnObj : "#order-modal .dish-oper-btns .prev-btn",
//		nextBtnObj : "#order-modal .dish-oper-btns .next-btn",
//		callback : function() {
//			$("#order-dish-table tbody tr").removeClass("selected");
//			$("#order-dish-table tbody tr").not(".hide").eq(0).addClass(
//					"selected");
//		}
//	});
//}
// 已选优惠分页
//function page2(currPage) {
//	nowPage2 = widget.loadPage({
//		obj : "#sel-preferential-table tbody tr",
//		listNum : 6,
//		currPage : currPage,
//		totleNums : $("#sel-preferential-table tbody tr").length,
//		curPageObj : "#order-modal #curr-page2",
//		pagesLenObj : "#order-modal #pages-len2",
//		prevBtnObj : "#order-modal .preferential-oper-btns .prev-btn",
//		nextBtnObj : "#order-modal .preferential-oper-btns .next-btn",
//		callback : function() {
//			$("#sel-preferential-table tbody tr").removeClass("selected");
//			$("#sel-preferential-table tbody tr").not(".hide").eq(0).addClass(
//					"selected");
//		}
//	});
//	Order.controlOperBtns();
//}
// 优惠分页
//function page3(currPage) {
//	nowPage3 = widget.loadPage({
//		obj : ".preferentials-content .preferential-info",
//		listNum : 16,
//		currPage : currPage,
//		totleNums : $(".preferentials-content .preferential-info").length,
//		curPageObj : "#order-modal #curr-page3",
//		pagesLenObj : "#order-modal #pages-len3",
//		prevBtnObj : "#order-modal .main-div .prev-btn",
//		nextBtnObj : "#order-modal .main-div .next-btn"
//	});
//}

//function trClickEvent(){
//	// 选中已点菜品
//	$("#order-dish-table tbody tr").click(function() {
//		$("#order-dish-table tbody tr").removeClass("selected");
//		$(this).addClass("selected");
//	});
//}


///**
// * 添加优惠
// */
//function addPref(){
//	var tr = "";
//	var name = $("#pref-name").val();
//	var price = $("#pref-price").val();
//	var num = $("#pref-num").val();
//	if(num == null || num== ""){
//		num = 1;
//	}
//	tr = "<tr><td>" + name + "</td><td>"+num+"</td><td>" + price
//			+ "</td></tr>";
//
//	$("#sel-preferential-table tbody").prepend(tr);
//	page2(nowPage2);
//	$("#coupnum-dialog").modal("hide");
//	// 选中已选优惠
//	$("#sel-preferential-table tbody tr").unbind("click").on("click", function(){
//		$("#sel-preferential-table tbody tr").removeClass( "selected");
//		$(this).addClass("selected");
//	});
//}
///**
// * 优惠购物车操作
// */
//function controlOperBtns(){
//	if($("#sel-preferential-table tbody tr.selected").length > 0){
//		$("#del-pref").removeClass("disabled");
//		$("#clear-pref").removeClass("disabled");
//	}else{
//		$("#del-pref").addClass("disabled");
//		$("#clear-pref").addClass("disabled");
//	}
//
//}


// 确认开台
function confirmOpen() {
    $("#open-dialog").modal("hide");
    $("#adddish-dialog").load("../views/orderdish.jsp");
    $("#adddish-dialog").modal("show");
}


//关闭dialog
function closeConfirm(dialogId) {
    $("#" + dialogId).modal("hide");
}
function stoppro(evt) {
    var e = evt ? evt : window.event; //判断浏览器的类型，在基于ie内核的浏览器中的使用cancelBubble
    if (window.event) {
        e.cancelBubble = true;
    } else {
        e.preventDefault(); //在基于firefox内核的浏览器中支持做法stopPropagation
        e.stopPropagation();
    }
}