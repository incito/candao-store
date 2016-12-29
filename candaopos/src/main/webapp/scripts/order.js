var pref_prev = 0;
var pay_prev = 0;
var addDIshCurPager=0;//点菜页面列表页数
var couponListCurPager=0;//优惠卷页面列表页数
var orderdishtableInfoselect=0//选中的菜谱列表的第几个
var selpreferentialtableInfoselect=0//选中的优惠列表的第几个
var invoice_Flag={/*发票信息全局变量*/
    'orderid':'',
    'amount':'',
    'flag':''

};

var g_eatType = utils.getUrl.get('type');
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
    })(),
    backDishReasons: JSON.parse(utils.storage.getter('RETURNDISH')),//退菜原因
    memberInfo: null,
    otherPay: [],
    payLoaded: false,
    otherPayStr: '',
};

var dom = {
    doc: $(document),
    order: $("#order"),
    addDishDialog: $("#adddish-dialog"),//点菜弹窗
    giveDishDialog: $("#givedish-dialog"),//赠菜弹窗
    membershipCard: $('#membership-card'),
    selCompanyDialog: $('#selCompany-dialog'),
    backfoodDialog: $("#backfood-dialog"), //退菜弹窗,
    backfoodNumDialog: $('#backfoodnum-dialog') //退菜数量弹窗,
};

var Order = {

    init: function () {

        SetBotoomIfon.init();

        this.initPreferentialType();

        this.updateOrder();

        this.initPayType();

        this.bindEvent();

        this.renderBankList();

        this.renderPayCompany();

        widget.keyboard();

        //搜索键盘初始化
        widget.keyboard({
            target: '.search-btns',
            chirdSelector: 'div'
        });
        //发票信息键盘初始化
        widget.keyboard({
            target: '.virtual-keyboard-baseOne',
            chirdSelector: 'li'
        });
        //发票信息键盘初始化
       /* widget.keyboard({
            target: '.virtual-keyboard-baseTwo',
            chirdSelector: 'li'
        });*/

        //定时更新订单信息
        setInterval(function () {
            Order.updateOrder();
        }, 10000)
    },


    bindEvent: function () {

        var that = this;

        $('.dish-oper-btns .prev-btn').click(function () {
            addDIshCurPager = parseInt($('#curr-page1').text())
            addDIshCurPager = addDIshCurPager - 2
        })
        $('.dish-oper-btns .next-btn').click(function () {
            addDIshCurPager = parseInt($('#curr-page1').text())
        })
        $('.preferential-oper-btns .prev-btn').click(function () {
            couponListCurPager = parseInt($('#curr-page2').text())
            couponListCurPager = couponListCurPager - 2
        })
        $('.preferential-oper-btns .next-btn').click(function () {
            var $body = $("#sel-preferential-table tbody")
            $body.find('tr').removeClass("selected");
            $body.find('tr').not(".hide").eq(0).addClass("selected");
            couponListCurPager = parseInt($('#curr-page2').text())
        })

        /*账单菜品排序*/
        $('#order-dish-table th').click(function () {
            return false//不再前端自定义排序
             addDIshCurPager=0;//点菜页面列表页数
             orderdishtableInfoselect=0//选中的菜谱列表的第几个
            var me=$(this),
                dishType=me.attr('dishType'),
                sortdata=$.extend(true,{},that.orderDataPre),
                rankdata=sortdata.rows;
            for(var i=0;i<rankdata.length;i++){
                /*菜品名称*/
                var Fdishname=pinyinUtil.getFirstLetter(rankdata[i].dishname);
                rankdata[i]['Fdishname']=Fdishname
                /*菜品数量*/
                var Fdishnum=parseFloat(rankdata[i].dishnum)
                rankdata[i]['Fdishnum']=Fdishnum
                /*菜品单位*/
                var Fdishunit=pinyinUtil.getFirstLetter(rankdata[i].dishunit)
                rankdata[i]['Fdishunit']=Fdishunit
                /*菜品金额*/
                var Fdishnum=parseFloat(rankdata[i].orderprice)*parseFloat(rankdata[i].dishnum)//单价*数量
                rankdata[i]['Fdishnum']=Fdishnum

            }
            /*菜品名称*/
            if(me.hasClass('order-dish-table-dishname')){
                for(var i=0;i<rankdata.length;i++){
                    var FirstLetter=pinyinUtil.getFirstLetter(rankdata[i].dishname)
                    rankdata[i]['FirstLetter']=FirstLetter
                }
                if(dishType=='0'){
                    rankdata=_.sortByOrder(rankdata, ['Fdishname'], ['asc']);
                    me.attr('dishType','1')
                }
                else {
                    rankdata=_.sortByOrder(rankdata, ['Fdishname'], ['desc']);
                    me.attr('dishType','0')
                }
                sortdata['rows']=rankdata;
            }
            /*菜品数量*/
            if(me.hasClass('order-dish-table-dishnum')){
                if(dishType=='0'){
                    rankdata=_.sortByOrder(rankdata, ['Fdishnum'], ['asc']);
                    me.attr('dishType','1')
                }
                else {
                    rankdata=_.sortByOrder(rankdata, ['Fdishnum'], ['desc']);
                    me.attr('dishType','0')
                }
                sortdata['rows']=rankdata;
            }
            /*菜品单位*/
            if(me.hasClass('order-dish-table-dishunit')){
                if(dishType=='0'){
                    rankdata=_.sortByOrder(rankdata, ['Fdishunit'], ['asc']);
                    me.attr('dishType','1')
                }
                else {
                    rankdata=_.sortByOrder(rankdata, ['Fdishunit'], ['desc']);
                    me.attr('dishType','0')
                }
                sortdata['rows']=rankdata;

            }
            /*菜品价格*/
            if(me.hasClass('order-dish-table-orderprice')){
                if(dishType=='0'){
                    rankdata=_.sortByOrder(rankdata, ['Fdishnum'], ['asc']);
                    me.attr('dishType','1')
                }
                else {
                    rankdata=_.sortByOrder(rankdata, ['Fdishnum'], ['desc']);
                    me.attr('dishType','0')
                }
                sortdata['rows']=rankdata;


            }
            that.dishesSort(JSON.stringify(sortdata))
        })

        /*优惠列表排序*/
        $('#sel-preferential-table th').click(function () {
            return false//不再前端自定义排序
            if($(this).hasClass('sel-dish-table-dishnum') || that.orderDataPre.preferentialInfo.detailPreferentials===undefined){
                return false
            }
            couponListCurPager=0;//优惠卷页面列表页数
            selpreferentialtableInfoselect=0//选中的优惠列表的第几个
            var me=$(this),
                dishType=me.attr('dishType'),
                sortdata=$.extend(true,{},that.orderDataPre),
                rankdata=sortdata.preferentialInfo.detailPreferentials;
            /*优惠卷名称*/
            for(var i=0;i<rankdata.length;i++){
                /*优惠名称名称*/
                var Fdishname=pinyinUtil.getFirstLetter(rankdata[i].activity.name);
                rankdata[i]['Fdishname']=Fdishname
                /*优惠金额*/
                var Fdishnum=parseFloat(rankdata[i].deAmount)//金额
                rankdata[i]['Fdishnum']=Fdishnum
            }
            /*优惠名称*/
            if(me.hasClass('sel-preferential-table-name')){
                if(dishType=='0'){
                    rankdata=_.sortByOrder(rankdata, ['Fdishname'], ['asc']);
                    me.attr('dishType','1')
                }
                else {
                    rankdata=_.sortByOrder(rankdata, ['Fdishname'], ['desc']);
                    me.attr('dishType','0')
                }
            }

            /*优惠卷金额*/
            if(me.hasClass('sel-preferential-table-deAmount')){
                if(dishType=='0'){
                    rankdata=_.sortByOrder(rankdata, ['Fdishnum'], ['asc']);
                    me.attr('dishType','1')
                }
                else {
                    rankdata=_.sortByOrder(rankdata, ['Fdishnum'], ['desc']);
                    me.attr('dishType','0')
                }

            }
            console.log(rankdata)
            that.updateSelectedPref(rankdata,0);
        })

        $('.J-btn-settlement').click(function () {
            Log.send(2, '点击结账按钮');
            var needPay = parseFloat($('.needPay span').text());
            var tipAmount = parseFloat($('#tip-amount').text());
            var tipAmountSpan = parseFloat($('.tipAmount span').text());
            var amount = parseFloat($('#amount').text());
            var cash = parseFloat($('input[name=cash]').val());
            var hasTip = !$('.tipAmount').hasClass('hide');
            var alertIns = null;

            var totalOtherPay = (function () {
                var total = 0;
                $('.pay-div .J-pay-val').each(function () {
                    var $me = $(this);
                    if ($me.attr('iptType') !== 'cash') {
                        total += parseFloat($me.val().length > 0 ? $me.val() : 0);
                    }
                });
                return total;
            })();
            var settlementAmount = cash + totalOtherPay + consts.moneyWipeAmount;


            if (needPay > 0) {
                if (needPay > tipAmount) {
                    widget.modal.alert({
                        content: '<strong>还有未收金额</strong>',
                        btnOkTxt: '确定',
                        btnCancelTxt: ''
                    });
                } else {
                    if (!$('.tipAmount').hasClass('hide')) {
                        if(needPay >= amount) {
                            var alertIns = widget.modal.alert({
                                content: '<strong>' + tipAmount + '元小费,必须使用现金结算</strong>',
                                btnOkCb: function () {
                                    alertIns.close();
                                }
                            });
                        } else {
                            if (tipAmountSpan < tipAmount) {
                                var alertIns = widget.modal.alert({
                                    content: '<strong>还有' + (tipAmount - tipAmountSpan).toFixed(2) + '元小费未结算,点击确定继续结算,点击取消取消结算</strong>',
                                    btnOkCb: function () {
                                        alertIns.close();
                                        that.doSettlement();
                                    }
                                });
                            }
                        }

                    } else {
                        widget.modal.alert({
                            content: '<strong>还有未收金额</strong>',
                            btnOkTxt: '确定',
                            btnCancelTxt: ''
                        });
                    }
                }
            } else {
                if (tipAmountSpan < tipAmount) {
                    var alertIns = widget.modal.alert({
                        content: '<strong>' + tipAmount + '元小费,必须使用现金结算</strong>',
                        btnOkCb: function () {
                            alertIns.close();
                        }
                    });
                } else {
                    that.doSettlement();
                }
            }

            //if(!hasTip) {
            //    that.doSettlement();
            //    return;
            //}
            //
            //if(settlementAmount < amount) {
            //    widget.modal.alert({
            //        content: '<strong>还有未收金额</strong>',
            //        btnOkTxt: '确定',
            //        btnCancelTxt: ''
            //    });
            //    return ;
            //} else if(settlementAmount === amount) {
            //    if(hasTip) {
            //         alertIns = widget.modal.alert({
            //            content: '<strong>还有' + (parseFloat($('#tip-amount').text()) - parseFloat($('.tipAmount span').text())).toFixed(2) + '元小费未结算,点击确定继续结算,点击取消取消结算</strong>',
            //            btnOkCb: function () {
            //                alertIns.close();
            //                that.doSettlement();
            //            }
            //        });
            //    } else {
            //        that.doSettlement();
            //    }
            //} else {
            //    if(hasTip) {
            //        if(cash < tipAmount) {
            //            if(totalOtherPay < amount) {
            //                alertIns = widget.modal.alert({
            //                    content: '<strong>还有' + (parseFloat($('#tip-amount').text()) - parseFloat($('.tipAmount span').text())).toFixed(2) + '元小费未结算,点击确定继续结算,点击取消取消结算</strong>',
            //                    btnOkCb: function () {
            //                        alertIns.close();
            //                        that.doSettlement();
            //                    }
            //                });
            //            } else {
            //                alertIns = widget.modal.alert({
            //                    content: '<strong>' + parseFloat($('#tip-amount').text()) + '元小费,必须使用现金结算</strong>',
            //                    btnOkCb: function () {
            //                        alertIns.close();
            //                    }
            //                });
            //            }
            //        } else {
            //            if(needPay <= 0) {
            //                that.doSettlement();
            //                return ;
            //            }
            //             alertIns = widget.modal.alert({
            //                content: '<strong>还有' + (parseFloat($('#tip-amount').text()) - parseFloat($('.tipAmount span').text())).toFixed(2) + '元小费未结算,点击确定继续结算,点击取消取消结算</strong>',
            //                btnOkCb: function () {
            //                    alertIns.close();
            //                    that.doSettlement();
            //                }
            //            });
            //        }
            //    }
            //}
        });

        //点击- +修改发票金额
        $('#Invoice-title .plus_sign').click(function () {
            Log.send(2, '修改发票金额');
            var se = $(this),
                _thisVal = parseFloat($.trim($('#Invoice-title .invoiceMoney').val()));
            /*减号*/
            if (se.hasClass('minus')) {
                _thisVal = _thisVal - 1
            }
            /*加号*/
            if (se.hasClass('Add_key')) {
                _thisVal = _thisVal + 1

            }
            $('#Invoice-title .invoiceMoney').val(_thisVal.toFixed(2))
        })

        //支付方式切换
        dom.doc.delegate('.tab-payment li', 'click', function () {
            $(this).addClass("active").siblings().removeClass("active");
            $(".paytype-input").addClass("hide");
            var targetId = $(this).attr("target");
            $(targetId).removeClass("hide");
        })

        dom.doc.click(function (e) {
            $(".more-oper").addClass("hide");
            e.stopPropagation();
        });

        $(".show-more").click(function (e) {
            $(".more-oper").removeClass("hide");
            e.stopPropagation();
        });

        /**
         * 退菜
         */
        //单品退菜
        $('#backDish').click(function () {
            var $target = $("#order-dish-table tr.selected");
            var groupid = $target.attr('groupid');
            var groupType = $target.attr('grouptype');
            var isGroupMain = $target.attr('groupmain') === 'true';
            //单品 && 组合(鱼锅\套餐) && 组合子菜品
            if (groupid !== undefined && !isGroupMain) {
                //鱼锅 && 锅底
                if (groupType === '1' && $target.attr('ispot') === '1') {
                    var modal = widget.modal.alert({
                        content: '<strong>选择鱼锅锅底退菜会退掉整个鱼锅,确定继续退菜?</strong>',
                        btnOkCb: function () {
                            modal.close();
                            that.initBackFoodDialog(0);
                        }
                    });
                    return false;
                }
                //套餐
                if (groupType === '2') {
                    widget.modal.alert({
                        content: '<strong>请选择套餐主体退整个套餐</strong>',
                        btnOkTxt: '',
                        btnCancelTxt: '确定'
                    });
                    return false;
                }
            }
            that.initBackFoodDialog(0);
        });

        $("#backDishNumIpt").on('input propertychange focus', function () {
            var me = $(this);
            var val = me.val();
            if (!(/^[0-9]{1,3}$/g.test(me.val()) || /^[0-9]{1,3}\.[0-9]{1,2}$/g.test(me.val()) || /^[0-9]{1,3}\.$/g.test(me.val()))) {
                me.val(val.substr(0, me.val().length - 1))
            }
            if ($.trim(me.val()).length > 0) {
                $('#backfoodnum-dialog .btn-save').removeAttr('disabled');
            } else {
                $('#backfoodnum-dialog .btn-save').attr('disabled', 'disabled');
            }
        });

        $('#backfoodnum-dialog .btn-save').click(function () {
            var dishNum = parseFloat($('#order-dish-table tr.selected .num').text());
            var backDishNum = parseFloat($('#backDishNumIpt').val());
            var $target = $("#order-dish-table tr.selected");
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

            if ($target.attr('groupmain') === 'true' && (backDishNum < 1)) {
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

        //称重
        $("#weigh-dish").click(function () {
            $('#weight-num').val('');
            $("#weight-dialog").modal("show");
        });

        $("#weight-dialog .btn-save").click(function () {
            if($.trim($('#weight-num').val())==''){
                $('#weight-num').attr('placeholder','称重菜品，重量不能为空')
                return false
            }
            var $target = $("#order-dish-table tr.selected");
            var params = {
                "orderId": $('[name=orderid]').val(),
                "dishid": $target.attr('dishid'),
                "primarykey": $target.attr('primarykey'),
                "dishnum": $.trim($('#weight-num').val())
            };
            Log.send(2, '称重:' + JSON.stringify(params));
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
                        Log.send(3, '称重:' + res.msg);
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
            if(dom.giveDishDialog.find('li.selected').length > 0) {
                dom.giveDishDialog.find('.btn-save').removeClass('disabled');
            } else {
                dom.giveDishDialog.find('.btn-save').addClass('disabled');
            }
        });

        //选中已选优惠或菜品
        dom.order.delegate("#sel-preferential-table tbody tr, #order-dish-table tbody tr", "click", function () {
            var me = $(this);
            var $btnWeigh = $('#weigh-dish');
            me.siblings().removeClass("selected").end().addClass("selected");
            if(me.parents('table').attr('id') === 'order-dish-table'){
                orderdishtableInfoselect = me.attr('primarykey');
            } else {
                selpreferentialtableInfoselect = me.attr('preid');
            }

            if (me.parents('table').attr('id') === 'order-dish-table') {
                if (me.attr('dishstatus') === '1') {
                    $btnWeigh.removeClass('disabled');
                } else {
                    $btnWeigh.addClass('disabled');
                }
            }
        });

        //添加优惠
        var _time = null;
        //设置优惠
        dom.order.delegate(".preferential-info", 'dblclick', function () {
            clearTimeout(_time);
            var me = $(this);
            var tips = '';
            var name = me.find('.dish-name').text();
            var type = $('.nav-pref-type.active').attr('preid') !== '-1'; // true:设置  false:恢复
            if (type) {
                tips = '设置[' + name + ']为不常用优惠(设置后可在不常用优惠分类里查看、使用)'
            } else {
                tips = '恢复[' + name + ']为常用优惠(恢复后可在对应分类查看,使用)'
            }

            var alertModal = widget.modal.alert({
                content: '<strong>' + tips + '</strong>',
                btnOkCb: function () {
                    Log.send(2, '分类调整:' + JSON.stringify({
                            "preferential": me.attr('preferential'),
                            "operationtype": type ? '1' : '0'
                        }));
                    $.ajax({
                        url: _config.interfaceUrl.SetCouponFavor,
                        method: 'post',
                        contentType: "application/json",
                        dataType: 'json',
                        data: JSON.stringify({
                            "preferential": me.attr('preferential'),
                            "operationtype": type ? '1' : '0'
                        })
                    }).then(function (res) {
                        if (res.result === '0') {
                            me.remove();
                            rightBottomPop.alert({
                                content: '分类调整成功'
                            })
                        } else {
                            Log.send(3, '分类调整失败');
                            rightBottomPop.alert({
                                content: '分类调整失败'
                            })
                        }
                        alertModal.close();
                    })
                }
            })
        });
        dom.order.delegate(".preferential-info", 'click', function () {
            var me = $(this);
            clearTimeout(_time);
            _time = setTimeout(function () {
                //单击事件在这里
                var name = me.attr('name');
                var type = me.attr('type');
                var sub_type = me.attr('sub_type');
                var discount = me.attr('discount');
                var free_reason = me.attr('free_reason')
                var $coupnumDialog = $('#coupnum-dialog');
                var $givedishDialog = $('#givedish-dialog');

                if ($('#order-dish-table tbody tr').length === 0) {
                    var alertIns = widget.modal.alert({
                        cls: 'fade in',
                        content: '<strong>还未下单,不能使用优惠</strong>',
                        width: 500,
                        height: 500,
                        btnCancelTxt: '',
                        btnOkCb: function () {
                            alertIns.close();
                        }
                    });
                    return false;
                }

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

                focusIpt = $coupnumDialog.find('.J-pref-ipt');
                $coupnumDialog.find('.coupname').text('');
                $coupnumDialog.find('.J-pref-ipt').val('');

                $coupnumDialog.find('.J-pref-ipt').off('input propertychange focus').on('input propertychange focus', function () {
                    var me = $(this);
                    me.val(me.val().replace(/[^\d]/g, ''));
                    if ($.trim(me.val()).length > 0) {
                        $coupnumDialog.find('.btn-save').removeAttr('disabled');
                    } else {
                        $coupnumDialog.find('.btn-save').attr('disabled', 'disabled');
                    }
                });

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
                    $givedishDialog.addClass('coupnum-cus-give');

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
            }, 300);


        });

        //删除或清空优惠
        $('#del-pref,#clear-pref').click(function () {
            var me = $(this);
            var target = $('#sel-preferential-table tbody tr.selected');
            var isClear = me.attr('id') === 'clear-pref';
            if (me.hasClass('disabled')) return false;
            Log.send(2, '删除或清空优惠:' + JSON.stringify({
                    clear: isClear ? '1' : '0',
                    DetalPreferentiald: target.attr('preid'),
                    orderid: target.attr('orderid')
                }));
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
                        selpreferentialtableInfoselect = 0;
                        //更新结算信息
                        that.updateTotal(res.data.preferentialInfo);
                        //更新已选优惠
                        that.updateSelectedPref(res.data.preferentialInfo.detailPreferentials, 0);
                    } else {
                        Log.send(3, '删除或清空优惠失败:' + res.msg);
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
                if (parseInt($(".nav-pref-types").find("li.nav-pref-type.active").css('margin-left')) < 0) {
                    $(".nav-pref-types").find("li.nav-pref-type").eq(pref_prev + 1).click();
                }
                pref_prev++;
                $(".nav-pretype-prev").removeClass('disabled');
                if (pref_prev === (count - 6)) {
                    $(this).addClass('disabled');
                }
            }
        });
        $(".nav-pretype-prev").click(function () {
            if (pref_prev >= 1) {
                $(".nav-pref-types").find("li.nav-pref-type").eq(pref_prev - 1).css("margin-left", "0");

                if ($(".nav-pref-types").find("li.nav-pref-type.active").index() === (pref_prev + 5)) {
                    $(".nav-pref-types").find("li.nav-pref-type").eq($(".nav-pref-types").find("li.active").index() - 1).click();
                }
                pref_prev--;
                $(".nav-pretype-next").removeClass('disabled');
                if (pref_prev === 0) {
                    $(this).addClass('disabled');
                }
            }
        });


        /*支付方式向左向右按钮*/
        $(".nav-pay-next").click(function () {
            var count = $(".nav-pay-types").find("li.nav-pay-type").length;
            if (pay_prev < count - 6) {
                $(".nav-pay-types").find("li.nav-pay-type").eq(pay_prev).css("margin-left", "-16.66%");
                if(parseInt($(".nav-pay-types").find("li.nav-pay-type.active").css('margin-left'))  < 0) {
                    $(".nav-pay-types").find("li.nav-pay-type").eq(pay_prev + 1).click();
                }
                pay_prev++;
                $(".nav-pay-prev").removeClass('disabled');
                if(pay_prev === (count - 6)) {
                    $(this).addClass('disabled');
                }
            }
        });
        $(".nav-pay-prev").click(function () {
            if (pay_prev >= 1) {
                $(".nav-pay-types").find("li.nav-pay-type").eq(pay_prev - 1).css("margin-left", "0");

                if($(".nav-pay-types").find("li.nav-pay-type.active").index() ===  (pay_prev + 5)) {
                    $(".nav-pay-types").find("li.nav-pay-type").eq($(".nav-pay-types").find("li.active").index() - 1).click();
                }
                pay_prev--;
                $(".nav-pay-next").removeClass('disabled');
                if(pay_prev === 0) {
                    $(this).addClass('disabled');
                }
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
            $('input[name=banktype]').attr('btype', target.attr('btype'));
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
            $('.payment-unit').attr('preferential', $target.attr('preferential'));
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

        dom.doc.delegate('.pay-div .J-pay-val,.J-pay-name', 'input propertychange focus', function () {
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

    payIptEvent: function (obj) {
        var me = obj;
        var type = me.attr('iptType');
        var $paytotal = $('.pay-total');
        var $cash = $('[name=cash]');
        var shouldAmount = parseFloat($("#should-amount").text());
        var iptVal = parseFloat(me.val().length > 0 ? me.val() : '0').toFixed(2);
        var cash = parseFloat($cash.length > 0 ? $cash.val() : '0').toFixed(2);
        var totalOtherPay = (function () {
            var total = 0;
            $('.pay-div .J-pay-val').each(function () {
                var $me = $(this);
                if ($me.attr('iptType') !== 'cash') {
                    total += parseFloat($me.val().length > 0 ? $me.val() : 0);
                }
            });
            return total;
        })();

        var _updateCash = function(val){
            var val = val;
            if (/^0{1,9}[0-9]{1,4}$/g.test(me.val())) {
                val = parseInt(val);
            }

            $cash.val(val);

            if (/^[0-9]{1,5}\.0{0,2}$/g.test(me.val())) {
                val = parseInt(val);
            }


            var giveChange = (function () {
                var v = 0;
                if (totalOtherPay > shouldAmount) {
                    v = val;
                } else {
                    v = parseFloat(val) - (shouldAmount - totalOtherPay)
                }

                if (v < 0) {
                    v = 0;
                }
                return parseFloat(parseFloat(v).toFixed(2));
            })();
            var needPay = parseFloat(shouldAmount - parseFloat(val) - totalOtherPay);
            var tipAmount = parseFloat($('#tip-amount').text());
            var amount = parseFloat($('#amount').text());
            var prefAmount = parseFloat($('#discount-amount').text());
            var tipAmountCac = (function () {
                var v = 0;
                if (totalOtherPay > amount) {
                    v = val;
                } else {
                    v = parseFloat(val) - (amount - totalOtherPay) + prefAmount;
                }

                if (v > tipAmount) {
                    v = tipAmount
                }
                if (v < 0) {
                    v = 0;
                }
                return v;
            })();

            if (tipAmount > 0) {
                $paytotal.find('.tipAmount span').text(parseFloat(tipAmountCac).toFixed(2));
            }

            if (needPay > 0) {
                $paytotal.find('.needPay span').text(needPay.toFixed(2));
                $paytotal.find('.needPay').removeClass('hide');
            } else {
                $paytotal.find('.needPay span').text('0.00');
                $paytotal.find('.needPay').addClass('hide');
            }

            //console.log(giveChange);
            if (giveChange > 0) {
                $paytotal.find('.giveChange span').text(parseFloat(giveChange).toFixed(2));
                $paytotal.find('.giveChange').removeClass('hide');
                $('.the-change-span').text(parseFloat(giveChange).toFixed(2));
            } else {
                $paytotal.find('.giveChange span').text('0.00');
                $('.the-change-span').text('0.00');
                $paytotal.find('.giveChange').addClass('hide');
            }
            if (val > 0) {
                $paytotal.find('.payamount').find('span').text(parseFloat(val).toFixed(2));
                $paytotal.find('.payamount').removeClass('hide');
            } else {
                $paytotal.find('.payamount').find('span').text('0.00');
                $paytotal.find('.payamount ,.giveChange').addClass('hide');
            }
        };

        if (me.hasClass('J-pay-name')) {
            var iptName = me.val();
            var $parent = me.parents('.paytype-input');
            if (iptName.length > 0) {
                $parent.find('.J-pay-val,.J-pay-pwd').removeAttr('disabled');
            } else {
                if ($parent.attr('itemid') !== 8) {
                    return false;
                }
                $parent.find('.J-pay-val, .J-pay-pwd').attr('disabled', 'disabled');
            }
        } else {
            var target = $paytotal.find('.' + me.attr('iptType'));

            if (iptVal > 0) {
                target.find('span').text(iptVal);
                target.removeClass('hide');
            } else {
                target.find('span').text('');
                target.addClass('hide');
            }

            if (type === 'cash') {
                console.log(focusIpt);
                if(utils.storage.getter('autoFill') === '1') {
                    if($('.tab-payment li[itemid=0]').length === 0) {
                        _updateCash('0');
                    } else {
                        _updateCash(me.val().length ? me.val() : '0');
                    }
                } else {
                    if(focusIpt && (focusIpt.attr('name') === 'cash')) {
                        _updateCash(me.val().length ? me.val() : '0');
                    } else {
                        _updateCash('0');
                    }
                }
            } else {
                if (totalOtherPay >= shouldAmount) {//其他支付大于应收
                    _updateCash('0');
                    $paytotal.find('.payamount,.giveChange,.needPay').find('span').text('0.00');
                    $paytotal.find('.payamount ,.giveChange,.needPay').addClass('hide');
                } else {
                    var cVal = parseFloat(shouldAmount - totalOtherPay).toFixed(2);
                    if(utils.storage.getter('autoFill') === '1') {
                        if($('.tab-payment li[itemid=0]').length === 0) {
                            cVal = 0;
                            _updateCash(cVal);
                            $paytotal.find('.payamount').find('span').text(cVal);
                            $paytotal.find('.payamount').addClass('hide');
                        } else {
                            _updateCash(cVal);
                            $paytotal.find('.payamount').find('span').text(cVal);
                            $paytotal.find('.payamount').removeClass('hide');
                        }
                    } else {
                        _updateCash(me.val().length ? me.val() : '0');


                        //if(focusIpt === )
                        //console.log(focusIpt);
                        //cVal = 0;
                        //_updateCash(cVal);
                        //$paytotal.find('.payamount').find('span').text(cVal);
                        //$paytotal.find('.payamount').addClass('hide');
                    }
                }
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
                Log.send(2, '餐道会员信息查询:' + JSON.stringify({
                        "branch_id": utils.storage.getter('branch_id'),
                        "cardno": cardNumber,
                        "password": '',
                        "securityCode": ''
                    }));
                Log.send(2, '餐道会员登录:' + JSON.stringify({
                        "mobile": cardNumber,
                        "orderid": consts.orderid,
                    }));


                $.ajax({
                    url: consts.memberAddr.vipcandaourl + _config.interfaceUrl.QueryCanDao,
                    method: 'POST',
                    contentType: "application/json; charset=utf-8",
                    dataType: 'json',
                    timeout: 5000,
                    data: JSON.stringify({
                        "branch_id": utils.storage.getter('branch_id'),
                        "cardno": cardNumber,
                        "password": '',
                        "securityCode": ''
                    })
                }).then(function (res1) {
                    Log.send(2, '餐道会员信息查询返回:' + JSON.stringify(res1));
                    if (res1.Retcode === '0') {
                        $('#StoreCardBalance').html('<b>' + res1.StoreCardBalance + '</b>(' + res1.CardLevel + ')');
                        $('#IntegralOverall').text(res1.IntegralOverall);
                        btn.text('退出');
                        btn.addClass('btn-login-out');
                        btn.removeClass('disabled');
                        ipt.attr('disabled', 'disabled');
                        consts.memberInfo = res1;
                        $.ajax({
                            url: _config.interfaceUrl.MemberLogin,
                            method: 'POST',
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            data: JSON.stringify({
                                "mobile": cardNumber,
                                "orderid": consts.orderid,
                            })
                        }).then(function (res2) {
                            Log.send(2, '餐道会员登录返回:' + JSON.stringify(res2));
                            if (res2.code === '0') {
                                //重新刷新订单信息
                                that.updateOrder();
                                dom.membershipCard.attr('isLogin', 'true');
                                dom.membershipCard.find('.J-pay-pwd,.J-pay-val').removeAttr('disabled');
                                rightBottomPop.alert({
                                    title: "提示信息",
                                    content: res2.msg,
                                    width: 320,
                                    height: 200,
                                    right: 5
                                });
                            } else {
                                dom.membershipCard.attr('isLogin', 'false');
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
                })
            } else {//雅座
                Log.send(2, '雅座会员登录:' + JSON.stringify({
                        "mobile": cardNumber,
                        "orderid": consts.orderid,
                    }));
                $.ajax({
                    url: consts.memberAddr.vipotherurl + _config.interfaceUrl.Yafindmember + cardNumber,
                    method: 'GET',
                    contentType: "application/json; charset=utf-8",
                    dataType: 'json'
                }).then(function (res1) {
                    //查询
                    var res1 = res1;
                    Log.send(2, '雅座会员查询返回:' + JSON.stringify(res1));
                    if (res1.Data === '1') {
                        $('#StoreCardBalance').html('<b>' + res1.psStoredCardsBalance / 100 + '</b>');
                        $('#IntegralOverall').text(res1.psIntegralAvail / 100);
                        btn.text('退出');
                        btn.addClass('btn-login-out');
                        btn.removeClass('disabled');
                        ipt.attr('disabled', 'disabled');
                        dom.membershipCard.find('.J-pay-pwd,.J-pay-val').removeAttr('disabled');
                        consts.memberInfo = res1;
                        $.ajax({
                            url: _config.interfaceUrl.MemberLogin,
                            method: 'POST',
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            data: JSON.stringify({
                                "mobile": cardNumber,
                                "orderid": consts.orderid,
                            })
                        }).then(function (res2) {
                            Log.send(2, '雅座会员登录返回:' + JSON.stringify(res2));
                            if (res2.code === '0') {

                                dom.membershipCard.attr('isLogin', 'true');
                                //重新刷新订单信息
                                that.updateOrder();
                                rightBottomPop.alert({
                                    title: "提示信息",
                                    content: res2.msg,
                                    width: 320,
                                    height: 200,
                                    right: 5
                                });
                            } else {
                                dom.membershipCard.attr('isLogin', 'false');
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


                        rightBottomPop.alert({
                            title: "提示信息",
                            content: '雅座会员查询成功',
                            width: 320,
                            height: 200,
                            right: 5
                        });
                    } else {
                        widget.modal.alert({
                            cls: 'fade in',
                            content: '<strong>雅座会员查询失败</strong>',
                            width: 500,
                            height: 500,
                            btnOkTxt: '',
                            btnCancelTxt: '确定'
                        });
                    }
                });
            }
        } else {//登出
            Log.send(2, '会员登出:' + JSON.stringify({
                    "moblie": cardNumber,
                    "orderid": consts.orderid,
                }));
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
                Log.send(2, '会员登出:' + JSON.stringify(res));
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
                    dom.membershipCard.attr('isLogin', 'false');

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
                htm += '<img alt="' + me.itemDesc + '" itemDesc="' + me.itemDesc + '"  btype="' + me.itemid + '" src="../images/bank/' + me.itemid + '.png">'
            });
            $('.bank-icon').html(htm);
        }
    },

    //type:1 预结单 2: 结账单 3:客用单
    printPay: function (type) {
        var that = this;
        var params = {};
        if (that.consumInfoFlag) {
            params = $.extend(params, {
                itemid: '0'
            })
        } else {
            params = $.extend(params, {
                itemid: JSON.parse(utils.storage.getter('ROUNDING'))[0].itemid
            })
        }
        Log.send(2, '打印:' + _config.interfaceUrl.PrintPay + '/' + utils.storage.getter('aUserid') + '/' + $('[name=orderid]').val() + '/' + type + '/' + utils.storage.getter('posid'))
        $.ajax({
            url: _config.interfaceUrl.PrintPay + '/' + utils.storage.getter('aUserid') + '/' + $('[name=orderid]').val() + '/' + type + '/' + utils.storage.getter('posid'),
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(params),
            async: false,
            success: function (res) {
                var str = (function () {
                    var ret = ''
                    if (type === 1) {
                        ret = (res.msg === '' ? '预结单打印完毕' : res.msg)
                    }
                    else if(type === 2) {
                        ret = (res.msg === '' ? '结账单打印完毕' : res.msg)
                    }else {
                        ret = (res.msg === '' ? '客用单打印完毕' : res.msg)
                    }
                    return ret;
                })();
                Log.send(2, '打印返回:' + res);
                rightBottomPop.alert({
                    content: str
                });
            }
        });
    },

    consumInfoFlag: false,
    //抹零不处理
    consumInfo: function () {
        var $moneyWipeAmount = $('.pay-total .moneyWipeAmount span');
        var $shouldAmount = $('#should-amount');
        var $discountAmount = $('#discount-amount');
        var moneyWipeAmount = $moneyWipeAmount.text();
        var $target = $('.paytype-input input[name=cash]');
        if ($moneyWipeAmount.length === 0 || moneyWipeAmount === '0') return false;
        $shouldAmount.text((parseFloat($shouldAmount.text()) + parseFloat(consts.moneyWipeAmount)).toFixed(2));
        $discountAmount.text((parseFloat($discountAmount.text()) - parseFloat(consts.moneyWipeAmount)).toFixed(2));
        $target.val((parseFloat($target.val()) + parseFloat(consts.moneyWipeAmount)).toFixed(2));
        $('.payamount span').text($target.val());
        $moneyWipeAmount.parents('li').remove();
        consts.moneyWipeAmount = 0.0;
        this.consumInfoFlag = true;
        this.updateOrder();
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
            Log.send(2, '取消订单:' + $('[name=tableno]').val());
            widget.modal.alert({
                cls: 'fade in',
                content: '<strong>确定取消桌号:' + consts.tableno + '的账单吗?</strong>',
                width: 500,
                height: 500,
                btnOkCb: function () {
                    $.ajax({
                        url: _config.interfaceUrl.ClearTable,
                        method: 'POST',
                        contentType: "application/json",
                        data: JSON.stringify({
                            //tableNo: $('[name=tableno]').val(),
                            orderNo:consts.orderid
                        }),
                        dataType: 'json',
                        success: function (res) {
                            Log.send(2, '取消订单:' + JSON.stringify(res));
                            if (res.code == '0') {
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
            });
        }
    },

    /**
     * 初始化退菜对话框
     * @param type 0:单品 1:全单
     */
    initBackFoodDialog: function (type) {
        dom.backfoodDialog.find('.breasons').html((function () {
            var str = '';
            $.each(consts.backDishReasons, function (k, v) {
                if(v.itemDesc.length>7){
                    str += '<div class="breason" style="padding-top: 0px">' + v.itemDesc + '</div>';
                }else {
                    str += '<div class="breason">' + v.itemDesc + '</div>';
                }
            });
            return str;
        })());
        $("#backfood-reason").val('');
        dom.backfoodDialog.find(".breason").unbind("click").on("click", function () {
            if ($(this).hasClass("active")) {
                $(this).removeClass("active");
            } else {
                $(this).addClass("active");
            }
        });
        dom.backfoodDialog.find(".btn-save").unbind("click").on("click", function () {
            if (type === 0) {
                dom.backfoodDialog.modal('hide');
                dom.backfoodNumDialog.find('.dishname span').text($("#order-dish-table tr.selected .dishname").text())
                dom.backfoodNumDialog.modal('show');
                dom.backfoodNumDialog.find('input[type=text]').val('');
            } else {
                $('#backfood-right').load("./check/impower.jsp", {
                    "title": "退菜权限",
                    "userRightNo": "030102",
                    "cbd": "Order.backDish(1)"
                });
                dom.backfoodDialog.modal('hide');
                $('#backfood-right').modal('show');
            }
        });
        dom.backfoodDialog.modal("show");
    },

    //退菜 0:单个 1:整单
    backDish: function (type, cb) {
        var that = this;
        var $target = $("#order-dish-table tr.selected");
        var tableId = $('[name=tableno]').val();
        var userId = $('#user').val();
        var orderNo = $('[name=orderid]').val();
        var params = {};
        utils.PromptAlert('退菜中，请稍后')
        if (userId == undefined) {
            userId = utils.storage.getter('aUserid')
        }

        var discardReason = (function () {
            var str = '';
            $('#backfood-dialog .breason.active').each(function () {
                str += $(this).text() + ';';
            });
            str += $.trim($('#backfood-reason').val());
            return str;
        })();


        if (type === 0) {
            params = {
                "operationType": 2,
                "primarykey": $target.attr('primarykey'),
                "sequence": 999999,
                "userName": utils.storage.getter('aUserid'),
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
                "orderNo": orderNo,
                "userName": utils.storage.getter('aUserid'),
            }
        }

        dom.backfoodDialog.modal('hide');
        $("#backfood-right").modal('hide');
        Log.send(2, '退菜:' + JSON.stringify(params));
        $.ajax({
            url: _config.interfaceUrl.BackDish,
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify(params),
            dataType: 'json',
            global: false,
            success: function (res) {
                Log.send(2, '退菜:' + JSON.stringify(res));
                if (res.code === '0') {
                    $(".modal-alert:last,.modal-backdrop:last").remove();//移除提示信息
                    $("#backfood-right .modal-dialog").remove();
                    that.updateOrder();
                    cb && cb();
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
        var url = "../views/orderdish.jsp?orderid=" + consts.orderid + '&personnum=' + consts.personnum + '&tableno=' + encodeURIComponent(encodeURIComponent(consts.tableno)) + '&type=' + g_eatType;
        window.location.href = url;
    },


    //加载支付方式分类
    initPayType: function (cb) {
        var ret = [];
        var that = this;
        var $payOther = $('.pay-other');
        var vipstatus = JSON.parse(utils.storage.getter('memberAddress')).vipstatus;
        pay_prev = 0;
        $.ajax({
            url:_config.interfaceUrl.PayWay,
            type: "get",
            dataType: "json",
        }).then(function(res){
            Log.send(2, '保存支付方式返回:' + JSON.stringify(res));
            if(res.code === '0') {
                $.each(res.data, function (k, v) {
                    var cla = "";
                    var itemid = v.itemId;
                    var target = '';
                    if (k === 0) {
                        cla = "active";
                    }
                    if(v.status === 1) {
                        if(itemid === '0'){
                            target = 'cash'
                        } else if(itemid === '1') {
                            target = 'bank-card'
                        } else if(itemid === '8') {
                            target = 'membership-card';
                            if(vipstatus === false) {
                                return;
                            }
                        } else if(itemid === '13') {
                            target = 'this-card'
                        } else if(itemid === '18') {
                            target = 'pay-treasure'
                        } else if(itemid === '17') {
                            target = 'wechat-pay'
                        } else {
                            target = 'pay' + itemid;
                            $payOther.append('<div class="paytype-input hide ' + target + '" itemid="' + itemid + '" id="' + target + '"> ' +
                                '<div class="form-group"> <span>' + v.title + ':</span> <input type="text" class="form-control J-pay-name" validtype="noPecial2" maxlength="20" name="' + target + 'Name"> </div>' +
                                '<div class="form-group"> <span>金额:</span> <input type="text" validtype="intAndFloat2" class="form-control J-pay-val" name="' + target + '" ipttype="' + target + '"> ' +
                                '</div> </div>');
                            consts.otherPayStr += '<li class="hide '+ target +'" itemid="' + itemid + '">' + v.title + ':<span></span></li> ';
                            consts.otherPay.push(v);

                        }
                        ret.push('<li target="#' + target + '" class="nav-pay-type ' + cla + '" status=' + v.status + ' itemId=' + itemid + '>' + v.title + '</li>');

                    };

                });

                $(".nav-pay-types").html(ret.join(''));
                $('.tab-payment li').eq(0).click();
                if($(".nav-pay-types").find( "li.nav-pay-type").length > 6) {
                    $(".nav-pay-prev").addClass('disabled');
                } else {
                    $(".nav-pay-prev, .nav-pay-next").addClass('disabled');
                }
            } else {
                widget.modal.alert({
                    content:'<strong>' + res.msg + '</strong>',
                    btnOkTxt: '确定',
                    btnCancelTxt: ''
                });
            }
            consts.payLoaded = true;
        })

    },

    /**
     * 优惠
     */
    //加载优惠分类
    initPreferentialType: function () {
        var ret = [];
        var that = this;
        pref_prev = 0;
        $.each(_config.preferential, function (k, v) {
            var cla = "";
            if (k === '05') {
                cla = "active";
            }
            ret.push('<li class="nav-pref-type ' + cla + '" preid="' + k + '">' + v + '</li>');
        });

        $(".nav-pref-types").html(ret.join(''));
        if ($(".nav-pref-types").find("li.nav-pref-type").length > 6) {
            $(".nav-pretype-prev").addClass('disabled');
        } else {
            $(".nav-pretype-prev, .nav-pretype-next").addClass('disabled');
        }

        that.initPreferential();
    },

    //通过分类获取优惠券信息
    initPreferential: function (id) {
        if (arguments.length < 1) {
            id = '05';
        }

        Log.send(2, '通过分类获取优惠券信息:' + JSON.stringify({
                machineno: utils.storage.getter('ipaddress'),
                userid: utils.storage.getter('aUserid'),
                orderid: '0',
                typeid: id
            }));
        $.ajax({
            url: _config.interfaceUrl.GetCouponInfos,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                machineno: utils.storage.getter('ipaddress'),
                userid: utils.storage.getter('aUserid'),
                orderid: '0',
                typeid: id
            }),
            beforeSend: function () {
                utils.loading.open('获取优惠券信息…')
            },
            success: function (res) {
                Log.send(2, '通过分类获取优惠券信息:' + JSON.stringify(res));
                var htm = '';
                $.each(res, function (k, v) {
                    htm += '<div style="background-color:' + v.color + '" class="preferential-info"' +
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
                targetModal.removeClass('coupnum-num');
                that.manageUsePref.set({
                    preferentialNum: val
                });
            } else if (targetModal.hasClass('coupnum-cus-discount')) {
                //手动输入折扣
                targetModal.removeClass('coupnum-cus-discount');
                that.manageUsePref.set({
                    isCustom: '1',
                    disrate: parseInt(val, 10) / 10
                });
            } else if (targetModal.hasClass('coupnum-cus-free')) {
                //手动输入优免
                targetModal.removeClass('coupnum-cus-free');
                that.manageUsePref.set({
                    isCustom: '1',
                    preferentialAmout: val
                });
            } else if (targetModal.hasClass('coupnum-cus-give')) {
                targetModal.removeClass('coupnum-cus-give');
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
        Log.send(2, '添加优惠:' + JSON.stringify(that.manageUsePref.get()));
        $.ajax({
            url: _config.interfaceUrl.CalcDiscountAmount,
            method: 'POST',
            async: true,
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(that.manageUsePref.get()),
            success: function (res) {
                Log.send(2, '添加优惠:' + JSON.stringify(res));
                if (res.code === '0') {
                    that.updateTotal(res.data);
                    //更新已选优惠
                    that.updateSelectedPref(res.data.detailPreferentials, 1);
                    //更新订单信息
                    that.updateOrder()

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
            //Log.send(2, '更新赠菜信息:' + JSON.stringify(res));
            var data = res1[0].data.rows;
            if (res1[0].code === '0' && res2[0].code === '0') {
                var htm = '';
                var ret = [];

                var getAllDishes = (function getArr(data){
                    for(var i = 0, len = data.length; i < len; i++){
                        var item = data[i];
                        if(item.dishtype === '1') {
                            arguments.callee(item.dishes);
                        } else {
                            ret.push(item);
                        }
                    }
                })(data);

                $.each(ret, function (k, v) {
                    var dishnum = parseInt(v.dishnum, 10);
                    $.each(res2[0].data, function (key, value) {
                        if(value) {
                            var left = dishnum - parseInt(value.count, 10);
                            if (value.dishid === v.dishid && value.unit === v.dishunit) {
                                if(left < 0) {
                                    v.dishnum = 0;
                                    value.count = Math.abs(left);
                                } else {
                                    v.dishnum = left;
                                    res2[0].data.splice(key,1)
                                }
                            }
                        }
                    });
                });

                $.each(ret, function (k, v) {
                    var cls = v.dishnum > 0 ? '' : 'hide';
                    if (parseInt(v.orderprice, 10) > 0) {
                        htm += "<li class='" + cls + "' dishname='" + v.dishname + "' dishid='" + v.dishid + "' unit='" + v.dishunit + "' num='" + v.dishnum + "'>" +
                            "<span class='dishname'>" + v.dishname.split('#')[0] + "(" + v.dishunit + ")" + "</span>" +
                            "<span class='info'><span class='sel'>0</span>/<span class='num'>" + v.dishnum + "</span></span>" +
                            "</li>";
                    }
                });
                $('#givedish-dialog .give-dish-list').html(htm);
                if(dom.giveDishDialog.find('li.selected').length > 0) {
                    dom.giveDishDialog.find('.btn-save').removeClass('disabled');
                } else {
                    dom.giveDishDialog.find('.btn-save').addClass('disabled');
                }
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
    updateSelectedPref: function (data, opType) {
        var tr = '';
        var $body = $("#sel-preferential-table tbody");
        var isToalDebitAmount;//是否是团购挂账

        $.each(data, function (k, v) {
            isToalDebitAmount = parseFloat(v.toalDebitAmount) > 0 ? true : false;
            if (isToalDebitAmount) {
                tr += "<tr preid='" + v.id + "' toaldebitamount='" + v.toalDebitAmount + "' toalfreeamount='" + v.toalFreeAmount + "' istoaldebitamount='" + isToalDebitAmount + "' orderid='" + v.orderid + "' coupondetailid='" + v.coupondetailid + "' preferential = '" + v.preferential + "'><td class='name'>" + v.activity.name + "</td><td class='num'>" + 1 + "</td><td class='amount'>" + v.deAmount + "</td></tr>";
            } else {
                tr += "<tr preid='" + v.id + "' istoaldebitamount='" + isToalDebitAmount + "' orderid='" + v.orderid + "' coupondetailid='" + v.coupondetailid + "' preferential = '" + v.preferential + "'><td class='name'>" + v.activity.name + "</td><td class='num'>" + 1 + "</td><td class='amount'>" + v.deAmount + "</td></tr>";
            }
        });
        if (opType === 0) {
            $("#sel-preferential-table tbody").html(tr);
        } else {
            $("#sel-preferential-table tbody").append(tr);
        }

        widget.loadPage({
            obj: "#sel-preferential-table tbody tr",
            listNum: 6,
            currPage: couponListCurPager,
            totleNums: $body.find('tr').length,
            curPageObj: ".preferential-oper-btns .page-info span:first",
            pagesLenObj: ".preferential-oper-btns .page-info span:last",
            prevBtnObj: ".preferential-oper-btns .prev-btn",
            nextBtnObj: ".preferential-oper-btns .next-btn",
            callback: function () {
                $body.find('tr').removeClass("selected");
                if(selpreferentialtableInfoselect === 0 ){
                    $body.find('tr').eq(0).addClass("selected");
                } else {
                    $body.find('tr[preid=' + selpreferentialtableInfoselect + ']').addClass("selected");
                }


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
    updateTotalTimer: null,
    payTypeLoaded: false,//支付方式是否加载
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
        consts.moneyDisType = data.moneyDisType; //0 不处理; 1 四舍五入; 2 抹零
        consts.moneyWipeName = data.moneyWipeName;

        //设置统计
        $('#discount-amount').text(amount);
        $('#amount').text(originalOrderAmount)//消费金额;
        $('#should-amount').text(payamount);
        $('#tip-amount').text(data.tipAmount);//小费设置

        $('.pay-total').remove();

        totalHtml += '<li class="' + (parseFloat(toalDebitAmount) !== 0 ? '' : 'hide') + ' toalDebitAmount">挂账<i class="spangap">:</i><span>' + toalDebitAmount + '</span></li> ';
        totalHtml += '<li class="' + (parseFloat(toalFreeAmount) !== 0 ? '' : 'hide') + ' toalFreeAmount">优免<i class="spangap">:</i><span>' + toalFreeAmount + '</span></li> ';

        totalHtml += (function () {
            var ret = [];
            var wipeAmount = parseFloat(consts.moneyWipeAmount);
            ret.push('<li class="' + (parseFloat(moneyWipeAmount) !== 0 ? '' : 'hide') + ' moneyWipeAmount">');
            if (consts.moneyDisType === '1') {
                if (moneyWipeAmount > 0) {
                    ret.push('<b>舍去</b><i class="spangap">:</i>');
                    ret.push('<span>' + wipeAmount.toFixed(2) + '</span>');
                } else {
                    ret.push('<b>舍入</b><i class="spangap">:</i>');
                    ret.push('<span>' + Math.abs(wipeAmount).toFixed(2) + '</span>');
                }
            } else if (consts.moneyDisType === '2') {
                ret.push('<b>抹零</b><i class="spangap">:</i>');
                ret.push('<span>' + wipeAmount.toFixed(2) + '</span>');
            } else {
                return '';
            }
            ret.push('</li>');
            return ret.join('');
        })();


        totalHtml += '<li class="' + (parseFloat(adjAmout) !== 0 ? '' : 'hide') + ' adjAmout">优免调整<i class="spangap">:</i><span>' + adjAmout + '</span></li> ';
        totalHtml += '<li class="' + (parseFloat(toalDebitAmountMany) !== 0 ? '' : 'hide') + ' toalDebitAmountMany">挂账多收<i class="spangap">:</i><span>' + toalDebitAmountMany + '</span></li> ';
        totalHtml += '<li class="' + (parseFloat(payamount) !== 0 ? '' : 'hide') + ' payamount" itemid="0">现金<i class="spangap">:</i><span>' + parseFloat(payamount).toFixed(2) + '</span></li> ';
        totalHtml += '<li class="' + (parseFloat(tipAmount) !== 0 ? '' : 'hide') + ' tipAmount" >小费<i class="spangap">:</i><span>' + tipAmount + '</span></li> ';

        totalHtml += '<li class="hide giveChange">找零:<span></span></li> ';

        //固定支付方式
        totalHtml += '<li class="hide bank" itemid="1">银行卡:<span></span></li> ';
        totalHtml += '<li class="hide memberCash"  itemid="8">会员消费:<span></span></li> ';
        totalHtml += '<li class="hide memberJf"  itemid="8">会员积分:<span></span></li> ';
        totalHtml += '<li class="hide debitAmount" itemid="5">挂账支付:<span></span></li> ';
        totalHtml += '<li class="hide alipay" itemid="18">支付宝:<span></span></li> ';
        totalHtml += '<li class="hide wpay" itemid="17">微信:<span></span></li> ';
        //other
        totalHtml += '<div id="totalOtherPay"></div>';
        totalHtml += '<li class="hide needPay">还需再收:<span></span></li> ';

        if (invoice_Flag.flag != '') {
            totalHtml += '<li class=" orderInvoiceTitle">发票抬头:&nbsp<span>' + invoice_Flag.flag + '</span></li> ';
        }
        totalHtml += '</ul>';

        $('.pay-div').after(totalHtml);

        if(!that.payTypeLoaded) {
            that.updateTotalTimer = setTimeout(function(){
                if (!consts.payLoaded){
                    setTimeout(arguments.callee, 50);
                } else {
                    if(utils.storage.getter('autoFill') === '1') {
                        if($('.tab-payment li[itemid=0]').length === 0) {
                            $('#cash input').val(0);
                        } else {
                            $('#cash input').val(payamount);
                        }
                    } else {
                        $('#cash input').val(0);
                    }

                    $('#totalOtherPay').html(consts.otherPayStr);
                    that.payTypeLoaded = true;
                    clearTimeout(that.updateTotalTimer);

                    //设置支付信息
                    $('.pay-div .J-pay-val,.pay-div .J-pay-name').each(function () {
                        that.payIptEvent($(this));
                    });
                }
            }, 50);
        } else {
            //设置支付信息
            $('#totalOtherPay').html(consts.otherPayStr);
            $('.pay-div .J-pay-val,.pay-div .J-pay-name').each(function () {
                that.payIptEvent($(this));
            });
        }

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
    isFirstUpdateOrder: true,

    orderDataPre: null,

    /**
     * 更新订单信息
     */
    updateOrder: function () {
        var that = this;
        var params = {
            orderid: consts.orderid
        };
        var isOrderDataDiff = function(s, t){
            if(!s) return false;
            var source = $.extend(true,{}, s);
            var target = $.extend(true,{}, t);
            $.each(source.preferentialInfo.detailPreferentials,function(k,v){
                if(v.isCustom === '2') {
                    source.preferentialInfo.detailPreferentials[k] = '';
                }
            });

            if(source.serviceCharge) {
                source.serviceCharge.mtime = '';
            }

            if(target.serviceCharge) {
                target.serviceCharge.mtime = '';
            }

            $.each(target.preferentialInfo.detailPreferentials,function(k,v){
                if(v.isCustom === '2') {
                    target.preferentialInfo.detailPreferentials[k] = '';
                }
            });
            return (JSON.stringify(source) === JSON.stringify(target));

        };
        if (that.consumInfoFlag) {
            params = $.extend(params, {
                itemid: '0'
            })
        } else {
            params = $.extend(params, {
                itemid: JSON.parse(utils.storage.getter('ROUNDING'))[0].itemid
            })
        }
        if (that.updateOrderStatus === 1) {
            return false;
        } else {
            that.updateOrderStatus = 1;
            $.ajax({
                url: _config.interfaceUrl.GetOrderInfo,
                method: 'POST',
                contentType: "application/json",
                data: JSON.stringify(params),
                beforeSend: function () {
                    utils.loading.open('更新订单信息…')
                },
                global: false,
                dataType: 'json',
                async: false,
                success: function (res) {
                    if (res.code === '0') {
                        utils.loading.remove();
                        if (isOrderDataDiff(that.orderDataPre, res.data)) {
                            that.updateOrderStatus = 0;
                            return false;
                        } else {
                            if (res.data.serviceCharge) {
                                if (res.data.serviceCharge.chargeOn == '1') {
                                    $('#serviceCharge-tip').parent().show();
                                    $('#serviceCharge-tip').parent().next('.tip').css('float', 'right')
                                    $('#serviceCharge-tip').text(res.data.serviceCharge.chargeAmount)
                                    $('#serviceCharge').show()
                                }
                                else {
                                    $('#serviceCharge').show();
                                    $('#serviceCharge-tip').parent().hide();
                                    $('#serviceCharge-tip').parent().next('.tip').css('float', 'left')
                                }
                            }
                            else {
                                $('#serviceCharge').hide();
                                $('#serviceCharge-tip').parent().hide();
                                $('#serviceCharge-tip').parent().next('.tip').css('float', 'left')
                            }
                            that.orderDataPre = res.data
                        }
                        if (res.data.userOrderInfo.orderInvoiceTitle != '') {
                            // $('#Invoice-title').modal('show');
                            //focusIpt=$('#Invoice-title .invoiceMoney');
                            $('.tableNumber').text(res.data.userOrderInfo.tableName + '开发票')
                            $('.orderNumber').text(res.data.userOrderInfo.orderid)
                            $('.invoiceInfo').text(res.data.userOrderInfo.orderInvoiceTitle)
                            $('.orderMoney').text(res.data.preferentialInfo.payamount)
                            $('.invoiceMoney').val(res.data.preferentialInfo.payamount);
                            invoice_Flag = {
                                'orderid': res.data.userOrderInfo.orderid,
                                'amount': res.data.preferentialInfo.payamount,
                                'flag': res.data.userOrderInfo.orderInvoiceTitle
                            }

                        }
                        else {
                            invoice_Flag = {
                                'orderid': res.data.userOrderInfo.orderid,
                                'amount': res.data.preferentialInfo.payamount,
                                'flag': ''
                            }
                        }

                        if (utils.object.isEmptyObject(res.data)) {
                            that.updateOrderStatus = 0;
                            return false;
                        }

                        that.updateTotal(res.data.preferentialInfo);

                        //初始化会员信息
                        if (res.data.userOrderInfo.memberno.length > 0 && $('.login-btn.btn-login-out').length === 0) {
                            //登录
                            that.memberLoginAndOut(true, res.data.userOrderInfo.memberno);
                        }


                        //已经选择菜品数据拼装
                        that.dishesSort(JSON.stringify(res.data))
                        //初始化已经使用的优惠
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
                    that.updateOrderStatus = 0;
                    that.isFirstUpdateOrder = false;
                },
                error: function () {
                    that.updateOrderStatus = 0;
                }
            });
        }

    },

    /**
     * 结账
     * @returns {boolean}
     */
    doSettlement: function () {
        var that = this;
        var $trs = $('#order-dish-table tbody tr');

        //会员卡支付方式检查
        var memberTips = '';
        var isMemberLogin = dom.membershipCard.attr('isLogin') === 'true';

        var url = '';
        var memberCash = $.trim($('#memberCash').val());
        var memberJf = $.trim($('#memberJf').val());

        var totalPay = (function () {
            var total = 0;
            $('.pay-div .J-pay-val').each(function () {
                var $me = $(this);
                if ($me.val() !== '' && parseFloat($me.val()) > 0 && $me.attr('iptType') !== 'cash') {
                    total += parseFloat($me.val());
                }
            });
            return total;
        })();

        if (g_eatType === 'in') {
            url = _config.interfaceUrl.PayTheBill;
            Log.send(2, '堂食结账');
        } else {
            url = _config.interfaceUrl.PayTheBillCf;
            Log.send(2, '外卖结账');
        }

        if ($trs.length === 0) {
            widget.modal.alert({
                content: '<strong>不能结账空账单</strong>',
                btnOkTxt: '确定',
                btnCancelTxt: ''
            });
            Log.send(2, '不能结账空账单');
            return false;
        }

        if (totalPay > parseFloat($('#should-amount').text())) {
            widget.modal.alert({
                content: '<strong>实际支付金额"' + (totalPay + parseFloat($('[name=cash]').val())).toFixed(2) + '"超过应收金额"' + parseFloat($('#should-amount').text()).toFixed(2) + '"</strong>',
                btnOkTxt: '确定',
                btnCancelTxt: ''
            });
            Log.send(2, '实际支付金额"' + (totalPay + parseFloat($('[name=cash]').val())).toFixed(2) + '"超过应收金额"' + parseFloat($('#should-amount').text()).toFixed(2) + '"');
            return false;
        }

        if (parseFloat($('.giveChange span').text()) >= 100) {
            widget.modal.alert({
                content: '<strong>找零金额不能大于100</strong>',
                btnOkTxt: '确定',
                btnCancelTxt: ''
            });
            Log.send(2, '找零金额不能大于100');
            return false;
        }

        for (var i = 0, len = $trs.length; i < len; i++) {
            if ($trs.eq(i).attr('dishstatus') === '1') {
                widget.modal.alert({
                    content: '<strong>还有未称重菜品</strong>',
                    btnOkTxt: '确定',
                    btnCancelTxt: ''
                });
                Log.send(2, '还有未称重菜品');
                return false;
            }
        }

        //会员相关验证
        if (isMemberLogin) {
            if (memberCash.length > 0 && parseFloat(memberCash) > parseFloat($("#StoreCardBalance b").text())) {
                memberTips += '会员储值余额不足;<br/>';
            }

            if (memberJf.length > 0 && parseFloat(memberJf) > parseFloat($("#IntegralOverall").text())) {
                memberTips += '积分余额不足;<br/>';
            }

            //雅座会员需要不需要设置密码
            if (consts.vipType === '1' && $('.J-pay-pwd').val().length === 0 && (memberCash.length > 0 || memberJf.length > 0)) {
                memberTips += '请输入会员密码;';
            }

            if (memberTips.length > 0) {
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>' + memberTips + '</strong>',
                    width: 500,
                    height: 500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
                Log.send(2, memberTips);
                return false;
            }
        }

        //小费 弹钱箱 打印结账单 给pad发送清台消息 页面跳转
        var _fn = function () {

            //如果有小费
            if (parseFloat($('.tipAmount span').text()) >= 0) {
                utils.loading.open('上传小费信息');
                Log.send(2, '上传小费信息:' + JSON.stringify({
                        "paid": $('.tipAmount span').text(),
                        "orderid": consts.orderid
                    }));
                $.ajax({
                    url: _config.interfaceUrl.TipBill,
                    method: 'POST',
                    contentType: "application/json",
                    dataType: 'json',
                    async: false,
                    data: JSON.stringify({
                            "paid": $('.tipAmount span').text(), "orderid": consts.orderid
                        }
                    )
                }).then(function (res) {
                    if (res.code !== '0') {
                        widget.modal.alert({
                            content: '<strong>' + res.msg + '</strong>',
                            btnOkTxt: '确定',
                            btnCancelTxt: ''
                        });
                        Log.send(2, res.msg);
                    } else {
                        Log.send(2, '上传小费信息失败:' + JSON.stringify(res));
                    }

                });
            }

            //弹钱箱
            utils.openCash(1);
            //结账单
            if (!isMemberLogin) {
                that.printPay(2);
            }

            //给pad发送清台消息
            Log.send(2, '给pad发送清台消息:' + JSON.stringify({orderId: consts.orderid, type: 1}));
            $.ajax({
                url: _config.interfaceUrl.SendMsgAsyn,
                method: 'post',
                contentType: "application/json",
                dataType: 'json',
                async: false,
                data: JSON.stringify({
                    orderId: consts.orderid,
                    type: 1
                })
            }).then(function (dataMsg) {
            });


            if (utils.getUrl.get('referer') === '1') {//从账单页面跳转而来
                if (invoice_Flag.flag != '') {

                } else {
                    var url= "./check/check.jsp?tips=" + encodeURIComponent(encodeURIComponent('结账成功|结账单打印成功'));
                    window.location.href = url;
                    //goBack()
                }

            } else {
                if (invoice_Flag.flag != '') {

                } else {
                    var url= "./main.jsp?tips=" + encodeURIComponent(encodeURIComponent('结账成功|结账单打印成功'));
                    window.location.href = url;
                }

            }
        };
        //打印发票信息
        var invoiceMsg = function () {
            if (invoice_Flag.flag != '') {
                _fn()
                utils.loading.remove();
                $('#Invoice-title').modal('show');
                focusIpt = $('#Invoice-title .invoiceMoney');
                $('#Invoice-title #Invoice-title-btncancel,#Invoice-title .dialog-sm-header img').click(function () {
                    Log.send(2, '不打印发票信息,直接结账');
                    if (utils.getUrl.get('referer') === '1') {//从账单页面跳转而来
                        var url= "./check/check.jsp?tips=" + encodeURIComponent(encodeURIComponent('结账成功|结账单打印成功'));
                        window.location.href = url;
                        //goBack()
                    }
                    else {
                        var url= "./main.jsp?tips=" + encodeURIComponent(encodeURIComponent('结账成功|结账单打印成功'));
                        window.location.href = url;
                    }

                })
                $('#Invoice-title #Invoice-title-btnOk ').click(function () {
                    var invoiceAmount=$.trim($('#Invoice-title .invoiceMoney').val());
                    if(invoiceAmount>invoice_Flag.amount){
                        widget.modal.alert({
                            cls: 'fade in memberSucceed',
                            content: '<strong>您的开票金额大于账单金额!</strong>',
                            width: 500,
                            height: 500,
                            btnOkTxt: '',
                            btnCancelTxt: '确定',
                            btnCancelCb:function () {
                                $('#Invoice-title').modal('hide');
                                $(".modal-alert:last,.modal-backdrop:last").remove();
                                _printinvoiceMsg()
                            }
                        });
                        return false
                    }
                    utils.loading.open('打印发票信息');
                    Log.send(2, '打印发票信息:' + JSON.stringify({
                            deviceid: utils.storage.getter('posid'),
                            orderid: invoice_Flag.orderid,
                            amount: $.trim($('#Invoice-title .invoiceMoney').val()),
                        }));
                    _printinvoiceMsg();
                    function _printinvoiceMsg() {
                        $.ajax({
                            url: _config.interfaceUrl.PrintInvoice,
                            method: 'POST',
                            contentType: "application/json",
                            dataType: 'json',
                            data: JSON.stringify({
                                deviceid: utils.storage.getter('posid'),
                                orderid: invoice_Flag.orderid,
                                amount: $.trim($('#Invoice-title .invoiceMoney').val()),
                            }),
                            success: function (res) {
                                //console.log(res)
                                utils.loading.remove();
                                if (res.result == '0') {
                                    if (utils.getUrl.get('referer') === '1') {//从账单页面跳转而来
                                        var url= "./check/check.jsp?tips=" + encodeURIComponent(encodeURIComponent('结账成功|结账单打印成功'));
                                        window.location.href = url;
                                        //goBack()
                                    }
                                    else {
                                        var url= "./main.jsp?tips=" + encodeURIComponent(encodeURIComponent('结账成功|结账单打印成功'));
                                        window.location.href = url;
                                    }
                                }
                                else {
                                    utils.printError.alert('打印开发票信息失败，请稍后重试！')
                                    Log.send(2, '打印开发票信息失败，请稍后重试')
                                }

                            }
                        })
                    }

                });

            }
            else {
                _fn()
            }
        };

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
                        "payAmount": (function () {
                            var v = parseFloat($("input[name=cash]").val()) - parseFloat($('.tipAmount span').text()) - parseFloat($('.giveChange span').text());
                            return v;

                        })(),
                        "memerberCardNo": "",
                        "bankCardNo": "",
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": ""
                    }, {
                        "payWay": "1",
                        "payAmount": (function () {
                            var result = 0.0;
                            if ($('[name=bank]').val().length > 0) {
                                result = parseFloat($('[name=bank]').val())
                            }
                            return result;
                        })(),
                        "memerberCardNo": (function () {
                            var result = '';
                            if ($('[name=banktype]').attr('btype') !== undefined) {
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
                        "payAmount": $('[ipttype=memberCash]').val().length > 0 ? parseFloat($('[ipttype=memberCash]').val()) : 0.0,
                        "memerberCardNo": $('input[name=cardNumber]').val(),
                        "bankCardNo": "",
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": ""
                    }, {
                        "payWay": "11",
                        "payAmount": $('[ipttype=memberJf]').val().length > 0 ? parseFloat($('[ipttype=memberJf]').val()) : 0.0,
                        "memerberCardNo": $('input[name=cardNumber]').val(),
                        "bankCardNo": "",
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": ""
                    }, {
                        "payWay": "18",
                        "payAmount": (function () {
                            var result = 0.0;
                            if ($('[name=alipay]').val().length > 0) {
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
                        "payAmount": (function () {
                            var result = 0.0;
                            if ($('[name=wpay]').val().length > 0) {
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
                        "payAmount": (function () {
                            var result = 0.0;
                            if ($('[name=debitAmount]').val().length > 0) {
                                result = parseFloat($('[name=debitAmount]').val())
                            }
                            return result;
                        })(),
                        "memerberCardNo": '',
                        "bankCardNo": $('[name=debitAmountName]').val(),
                        "couponnum": "0",
                        "couponid": "",
                        "coupondetailid": (function () {
                            var result = '';
                            if ($('[name=debitAmount]').val().length > 0) {
                                result = $('[name=debitAmountName]').attr('preferential');
                            }
                            return result;
                        })()
                    }];

                    //其他支付方式
                    $.each(consts.otherPay, function(k, v){
                        if(v.status === 1) {
                            result.push({
                                "payWay": v.itemId,
                                "payAmount": (function(){
                                    var result = 0.0;
                                    var val = $('[name=pay' + v.itemId + ']').val();
                                    if(val.length > 0) {
                                        result = parseFloat(val)
                                    }
                                    return result;
                                })(),
                                "memerberCardNo": "",
                                "bankCardNo": $.trim($('[name=pay' + v.itemId + 'Name]').val()),
                                "couponnum": "0",
                                "couponid": "",
                                "coupondetailid": ""
                            });
                        }
                    });
                    if(consts.moneyDisType === '2') {
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
                    }

                    if (consts.moneyDisType === '1') {
                        //四舍五入
                        result.push({
                            "payWay": "20",
                            "payAmount": parseFloat(consts.moneyWipeAmount),
                            "memerberCardNo": "",
                            "bankCardNo": "",
                            "couponnum": "0",
                            "couponid": "",
                            "coupondetailid": ""
                        });
                    }

                    //挂账多收
                    if (!$('.toalDebitAmountMany').hasClass('hide')) {
                        result.push({
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
                    if (!$('.adjAmout').hasClass('hide')) {
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
                        if (me.attr('istoaldebitamount') === 'true') {
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
                utils.loading.open('正在结算…');
                Log.send(2, '正在结算:' + JSON.stringify({
                            "payDetail": rows, "userName": utils.storage.getter('aUserid'), "orderNo": consts.orderid
                        }
                    ));
                $.ajax({
                    url: url,
                    method: 'POST',
                    contentType: "application/json",
                    dataType: 'json',
                    global: false,
                    data: JSON.stringify({
                            "payDetail": rows, "userName": utils.storage.getter('aUserid'), "orderNo": consts.orderid
                        }
                    )
                })
                    .then(function (res) {
                        if (res.code === '0') {
                            if (isMemberLogin) {
                                var stored = $('[ipttype=memberCash]').val().length > 0 ? parseFloat($('[ipttype=memberCash]').val()).toFixed(2) : '0.0';
                                var jf = $('[ipttype=memberJf]').val().length > 0 ? parseFloat($('[ipttype=memberJf]').val()).toFixed(2) : '0.0';
                                Log.send(2, '会员方式结算:储值(' + stored + '), 积分(' + jf + ')');

                                //新增积分
                                var scoreAdd = (function () {
                                    var total = 0;
                                    $('.pay-div .J-pay-val').each(function () {
                                        var $me = $(this);
                                        if ($me.val() !== '' && parseFloat($me.val()) > 0 && $me.attr('iptType') !== 'memberJf') {
                                            total += parseFloat($me.val());
                                        }
                                    });
                                    return total;
                                })();

                                var pszCash = (function () {
                                    var total = 0;
                                    $('.pay-div .J-pay-val').each(function () {
                                        var $me = $(this);
                                        if ($me.val() !== '' && parseFloat($me.val()) > 0 && $me.attr('iptType') !== 'memberJf' && $me.attr('iptType') !== 'memberCash') {
                                            total += parseFloat($me.val());
                                        }
                                    });
                                    return total;
                                })();
                                /*后台账单反结算*/
                                function rebackOrderOk() {
                                    Log.send(2, '后台账单反结算:' + JSON.stringify({
                                            'reason': '会员结算失败，系统自动反结',
                                            'orderNo': consts.orderid,
                                            'userName': utils.storage.getter('aUserid')
                                        }));
                                    $.ajax({
                                        url: _config.interfaceUrl.AntiSettlementOrder,//反结算
                                        method: 'POST',
                                        contentType: "application/json",
                                        data: JSON.stringify({
                                            'reason': '会员结算失败，系统自动反结',
                                            'orderNo': consts.orderid,
                                            'userName': utils.storage.getter('aUserid')
                                        }),
                                        dataType: "json",
                                        success: function (data) {
                                            if (data.result === '0') {
                                            }
                                            else {
                                                Log.send(3, '系统自动反结失败，请稍后再试');
                                                widget.modal.alert({
                                                    cls: 'fade in',
                                                    content: '<strong>系统自动反结失败，请稍后再试</strong>',
                                                    width: 500,
                                                    height: 500,
                                                    btnOkTxt: '',
                                                    btnCancelTxt: '确定'
                                                });
                                            }

                                        }
                                    })
                                }

                                if (consts.vipType === '1') {//餐道会员
                                    //餐道会员会员消费
                                    Log.send(2, '餐道会员消费');
                                    Log.send(2, '餐道会员消费url:' + consts.memberAddr.vipcandaourl + _config.interfaceUrl.SaleCanDao);

                                    var params = JSON.stringify({
                                        "Serial": consts.orderid,
                                        "FCash": (function () {
                                            var total = 0;
                                            $('.pay-div .J-pay-val').each(function () {
                                                var $me = $(this);
                                                if ($me.val() !== '' && parseFloat($me.val()) > 0 && $me.attr('iptType') !== 'memberCash' && $me.attr('iptType') !== 'memberJf') {
                                                    total += parseFloat($me.val());
                                                }
                                            });
                                            return total.toFixed(2);
                                        })(),
                                        "FWeChat": '0.0',
                                        "FIntegral": jf,
                                        "FStore": stored,
                                        "FTicketList": null,
                                        "cardno": consts.memberInfo.MCard,
                                        "password": $.trim($('.J-pay-pwd').val()),
                                        "branch_id": utils.storage.getter('branch_id'),
                                        "securityCode": ""
                                    });
                                    Log.send(2, '餐道会员消费请求参数:' + params);

                                    $.ajax({
                                        url: consts.memberAddr.vipcandaourl + _config.interfaceUrl.SaleCanDao,
                                        method: 'post',
                                        contentType: "application/json",
                                        dataType: 'json',
                                        data: params
                                    }).then(function (data) {
                                        console.log('餐道会员会员消费');
                                        if (data.Retcode == '1') {
                                            var alertMemIns = widget.modal.alert({
                                                cls: 'fade in',
                                                content: '<strong>' + data.RetInfo + '</strong>',
                                                width: 500,
                                                height: 500,
                                                btnOkTxt: '',
                                                btnCancelTxt: '确定'
                                            });
                                            Log.send(2, '餐道会员消费请求失败:' + data);
                                            rebackOrderOk();
                                            //后台账单反结算


                                            return false;

                                        } else {
                                            //保存会员消费
                                            var params = JSON.stringify({
                                                "orderid": consts.orderid,
                                                "cardno": consts.memberInfo.MCard,
                                                "userid": utils.storage.getter('aUserid'),
                                                "business": utils.storage.getter('branch_id'),
                                                "terminal": utils.storage.getter('posid'),
                                                "serial": data.TraceCode,
                                                "businessname": utils.storage.getter('branch_branchname'),
                                                "score": scoreAdd - parseFloat(jf),
                                                "coupons": 0.0,
                                                "stored": stored,
                                                "scorebalance": scoreAdd + parseFloat(consts.memberInfo.IntegralOverall) - parseFloat(jf),
                                                "couponsbalance": "0",
                                                "storedbalance": parseFloat(consts.memberInfo.StoreCardBalance) - stored,
                                                "psexpansivity": 0.0,
                                                "netvalue": stored,
                                                "inflated": 0.0
                                            })
                                            Log.send(2, '保存会员消费:' + params);
                                            return $.ajax({
                                                url: _config.interfaceUrl.AddMemberSaleInfo,
                                                method: 'post',
                                                contentType: "application/json",
                                                dataType: 'json',
                                                data: params
                                            });
                                        }
                                    },function () {
                                        Log.send(3, '会员结算失败开始账单反结:');
                                        rebackOrderOk()
                                    }).then(function (data) {
                                        if (data) {
                                            Log.send(2, '打印结账单');
                                            that.printPay(2);
                                            //打印会员消费
                                            Log.send(2, '打印会员消费:' + _config.interfaceUrl.PrintMemberSale + '/' + utils.storage.getter('aUserid') + '/' + consts.orderid + '/' + utils.storage.getter('posid'));
                                            return $.ajax({
                                                url: _config.interfaceUrl.PrintMemberSale + '/' + utils.storage.getter('aUserid') + '/' + consts.orderid + '/' + utils.storage.getter('posid'),
                                                method: 'get',
                                                contentType: "application/json",
                                                dataType: 'json',
                                                async: false,
                                                success: function (res3) {
                                                    console.log('打印会员消费');
                                                    console.log(res3);
                                                }
                                            });
                                        } else {
                                            return false;
                                        }

                                    }).then(function (data) {
                                        if (data) {
                                            invoiceMsg()//发票信息
                                        }

                                    });
                                } else {
                                    Log.send(2, '雅座会员消费');
                                    Log.send(2, '打印结账单');
                                    that.printPay(2);
                                    //雅座会员消费
                                    Log.send(2, '雅座会员消费: ' + consts.memberAddr.vipotherurl + _config.interfaceUrl.SaleYa
                                        + utils.storage.getter('aUserid') + '/' + consts.orderid + '/'
                                        + consts.memberInfo.pszMobile + '/' + consts.orderid + '/' + pszCash + '/'
                                        + jf + '/1/' + stored + '/%20/0/0/127.0.0.1:8080/');
                                    $.ajax({
                                        //002/H20161115023231006967/18655961901/H20161115023231006967/214.00/10/1/20/%20/0/0/10.66.21.8:8080/
                                        ///Sale/{aUserId}/{orderId}/{pszInput}/{pszSerial}/{pszCash}/{pszPoint}/{psTransType}/{pszStore}/{pszTicketList}/{pszPwd}/{memberyhqamount}/{server}/
                                        url: consts.memberAddr.vipotherurl + _config.interfaceUrl.SaleYa
                                        + utils.storage.getter('aUserid') + '/' + consts.orderid + '/'
                                        + consts.memberInfo.pszMobile + '/' + consts.orderid + '/' + pszCash + '/'
                                        + jf + '/1/' + stored + '/%20/0/0/127.0.0.1:8080/',
                                        method: 'get',
                                        contentType: "application/json"
                                    }).then(function (data) {

                                        if (data.Data === '1') {
                                            //打印会员消费
                                            return $.ajax({
                                                url: _config.interfaceUrl.PrintMemberSale + '/' + utils.storage.getter('aUserid') + '/' + consts.orderid + '/' + utils.storage.getter('posid'),
                                                method: 'get',
                                                contentType: "application/json",
                                                dataType: 'json',
                                                async: false,
                                                success: function (res3) {
                                                    console.log('打印会员消费');
                                                    console.log(res3);
                                                }
                                            });
                                        } else {
                                            Log.send(3, '雅座会员消费失败: ' + JSON.stringify(data))
                                            widget.modal.alert({
                                                content: '<strong>' + data.Info + '</strong>',
                                                btnOkTxt: '',
                                                btnCancelTxt: '确定'
                                            });
                                        }
                                    }).then(function () {
                                        invoiceMsg()//发票信息
                                    })
                                }

                            } else {
                                invoiceMsg()//发票信息
                            }
                        } else {
                            utils.loading.remove();
                            widget.modal.alert({
                                content: '<strong>' + res.msg + '</strong>',
                                btnOkTxt: '确定',
                                btnCancelTxt: ''
                            });
                        }
                    });
            }
        })
    },

    ya_formatDate: function (date, format) {
        if (!date) return;
        if (!format) format = "yyyy-MM-dd";
        switch (typeof date) {
            case "string":
                date = new Date(date.replace(/-/, "/"));
                break;
            case "number":
                date = new Date(date);
                break;
        }
        if (!date instanceof Date) return;
        var dict = {
            "yyyy": date.getFullYear(),
            "MM": ("" + (date.getMonth() + 101)).substr(1),
            "dd": ("" + (date.getDate() + 100)).substr(1),
            "HH": ("" + (date.getHours() + 100)).substr(1),
            "mm": ("" + (date.getMinutes() + 100)).substr(1),
            "ss": ("" + (date.getSeconds() + 100)).substr(1),
            "ffff": ("" + (date.getMilliseconds() + 10000)).substr(1),//毫秒
        };
        return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?|ffff?)/g, function () {
            return dict[arguments[0]];
        });
    },

    /**
     * 关闭结算
     */
    closeOrder: function () {
        var that = this;
        var _cancelOrder = function () {
            that.backDish(1, function () {
                $.ajax({
                    url: _config.interfaceUrl.CancelOrder + utils.storage.getter("aUserid") + '/' + consts.orderid + '/' + consts.tableno + '/',
                    method: 'get'
                }).then(function (data) {
                    var data = data.result[0];
                    if (data.Data === '1') {
                        window.location.href = './main.jsp'
                    } else {
                        widget.modal.alert({
                            content: '<strong>取消外卖账单接口错误</strong>',
                            btnOkTxt: '',
                            btnCancelTxt: '确定'
                        });
                    }
                })
            })

        };
        if (g_eatType === 'in') {
            window.location.href = './main.jsp'
        } else {
            if (utils.getUrl.get('referer') === '1') {
                window.location.href = './check/check.jsp'
            }
            else {
                if ($('#order-dish-table tbody tr').length > 0) {
                    var modal = widget.modal.alert({
                        content: '<strong>退出将清空当前已选菜品并取消该订单,确定放弃结算?</strong>',
                        btnOkCb: function () {
                            modal.close();
                            _cancelOrder();
                        }
                    });
                } else {
                    _cancelOrder()
                }
            }

        }

    },
    /**
     * 手动服务开关
     */
    serviceCharge: function (msg) {
        var that = this;
        var data = that.orderDataPre;
        if (msg) {
            _serviceCharge()
        }
        else {
            $('#serviceCharge-dialog').load("check/impower.jsp",
                {
                    "title": "服务费授权",
                    "userRightNo": "030102",
                    "cbd": "Order.serviceCharge(user)"
                });
            $('#serviceCharge-dialog').modal("show");
            setTimeout(function () {
                $('#user').focus()
            },500)
        }
        function _serviceCharge() {
            $('#serviceCharge-dialog').modal("hide");
            $('.Auto-serviceCharge').text(data.serviceCharge.chargeAmount);
            $('.MT-serviceCharge').val(data.serviceCharge.chargeAmount)
            if (data.serviceCharge.chargeOn == '0') {
                $('.serviceCharge-Switch').removeClass('select');
                $('.serviceCharge-Switch').eq(1).addClass('select');
                $('.MT-serviceCharge').attr('disabled', true)
            }
            $('#MT-serviceCharge').modal('show');
            /*服务费开关*/
            $('.serviceCharge-Switch').click(function () {
                var me = $(this)
                $('.serviceCharge-Switch').removeClass('select');
                me.addClass('select');
                if (me.attr('chargeOn') == '0') {
                    $('.MT-serviceCharge').attr('disabled', true)
                }
                else {
                    $('.MT-serviceCharge').attr('disabled', false).focus()
                }
            })
            /*修改服务费提交*/
            $('#serviceCharge-btnOk').click(function () {

                var orderId = data.serviceCharge.orderid,//订单号
                    _serviceCharge_val = $.trim($('.MT-serviceCharge').val()),
                    autho = msg,//授权人（退菜权限）
                    chargeOn = $('#MT-serviceCharge .select').attr('chargeOn'),//服务费开关0为关闭；1为开启
                    chargeAmount = null,//金额
                    custom = null//是否服务员手动输入0为自动，1为手动
                if (_serviceCharge_val == data.serviceCharge.chargeAmount) {
                    custom = 0;
                    chargeAmount = data.serviceCharge.chargeAmount
                }
                else {
                    custom = 1;
                    chargeAmount = _serviceCharge_val
                }
                $.ajax({
                    url: _config.interfaceUrl.ServiceChange,
                    method: 'POST',
                    contentType: "application/json",
                    dataType: 'json',
                    data: JSON.stringify({
                        orderId: orderId,
                        autho: autho,
                        chargeOn: chargeOn,
                        chargeAmount: chargeAmount,
                        custom: custom,
                    }),
                    success: function (res) {
                        if (res.code == '0') {
                            that.updateOrder()
                            $('#MT-serviceCharge').modal('hide');
                        }
                        else {
                            utils.printError.alert(res.msg);
                        }

                    }
                })
            })
        }
    },
    /*点菜排序后更新*/
    dishesSort:function (res) {
        var res=JSON.parse(res),
            that=this;

        var tr = '';
        var $body = $("#order-dish-table tbody");
        var _cutName = function(s){
            return utils.string.cutString(s.split('#')[0], parseInt($('#order-dish-table thead th').eq(0).width()/14, 10)*2)
        };

        if (res.rows.length > 0) {
            $.each(res.rows, function (k, v) {
                var groupid = utils.getUuid();
                var dishname = '';
                if (v.dishes !== undefined) {
                    tr += "<tr groupid='" + groupid + "' groupmain='true' grouptype='" + v.dishtype + "'   dishid='" + v.dishid + "' unit='" + v.dishunit + "' primarykey='" + v.primarykey + "' dishtype='" + v.dishtype + "' dishstatus='" + v.dishstatus + "'><td class='dishname'>" + _cutName(v.dishname) + "</td><td class='num'>" + v.dishnum + "</td><td class='unit'>" + v.dishunit.split('#')[0] + "</td><td class='orderprice " + (v.dishstatus === '1' ? 'weigh' : '') + "'>" + (v.dishstatus === '0' ? (v.orderprice * v.dishnum).toFixed(2) : '待称重') + "</td></tr>";
                    $.each(v.dishes, function (k1, v1) {
                        tr += "<tr groupid='" + groupid + "' ispot='" + v1.ispot + "' grouptype='" + v.dishtype + "'  dishid='" + v1.dishid + "' unit='" + v1.dishunit + "' primarykey='" + v1.primarykey + "' dishtype='" + v1.dishtype + "' dishstatus='" + v1.dishstatus + "'><td class='dishname'>" + _cutName(v1.dishname) + "</td><td class='num'>" + v1.dishnum + "</td><td class='unit'>" + v1.dishunit.split('#')[0] + "</td><td class='orderprice'>" + (v1.dishstatus === '0' ? parseFloat(v1.orderprice * v1.dishnum).toFixed(2) : '待称重') + "</td></tr>";
                        if (v1.dishtype !== 0) {
                            $.each(v1.dishes, function (k2, v2) {
                                tr += "<tr groupid='" + groupid + "' ispot='" + v2.ispot + "' grouptype='" + v2.dishtype + "'  dishid='" + v2.dishid + "' unit='" + v2.dishunit + "' primarykey='" + v2.primarykey + "' dishtype='" + v2.dishtype + "' dishstatus='" + v2.dishstatus + "'><td class='dishname'>" + _cutName(v2.dishname) + "</td><td class='num'>" + v2.dishnum + "</td><td class='unit'>" + v2.dishunit.split('#')[0] + "</td><td class='orderprice'>" + (v2.dishstatus === '0' ? parseFloat(v2.orderprice * v2.dishnum).toFixed(2) : '待称重') + "</td></tr>";
                            })
                        }
                    })
                } else {
                    if (/临时菜/.test(v.dishname)) {
                        dishname = '(' + v.taste + ')' + v.dishname.split('#')[0]
                    } else {
                        dishname = v.dishname.split('#')[0]
                    }

                    tr += "<tr   dishid='" + v.dishid + "' unit='" + v.dishunit + "' primarykey='" + v.primarykey + "' dishtype='" + v.dishtype + "' dishstatus='" + v.dishstatus + "'><td class='dishname'>" + _cutName(dishname) + "</td><td class='num'>" + v.dishnum + "</td><td class='unit'>" + v.dishunit.split('#')[0] + "</td><td class='orderprice " + (v.dishstatus === '1' ? 'weigh' : '') + "'>" + (v.dishstatus === '0' ? (v.orderprice * v.dishnum).toFixed(2) : '待称重') + "</td></tr>";
                }
            });
            $('#back-dish, #backDishAll, #reprintOrder,#prePrinter, #backDish').removeClass('disabled');
        } else {
            $('#back-dish, #backDishAll, #reprintOrder,#prePrinter,#backDish').addClass('disabled');
        }

        $body.html(tr);
        widget.loadPage({
            obj: "#order-dish-table tbody tr",
            listNum: 6,
            currPage: addDIshCurPager,
            totleNums: $body.find('tr').length,
            curPageObj: "#order-modal #curr-page1",
            pagesLenObj: "#order-modal #pages-len1",
            prevBtnObj: "#order-modal .dish-oper-btns .prev-btn",
            nextBtnObj: "#order-modal .dish-oper-btns .next-btn",
            callback: function () {
                $body.find('tr').removeClass("selected");
                if(orderdishtableInfoselect === 0) {
                    $body.find('tr').not(".hide").eq(0).addClass('selected');
                } else {
                    $body.find('tr[primarykey=' + orderdishtableInfoselect + ']').addClass("selected");
                }

                if ($body.find('tr').not(".hide").eq(0).attr('dishstatus') === '1') {
                    $("#weigh-dish").removeClass('disabled');
                } else {
                    $("#weigh-dish").addClass('disabled');
                }
            }
        });

    }

};


$(document).ready(function () {
    //$.ajaxSetup({
    //    global: false,
    //    async: false,
    //    complete: function(){
    //        utils.loading.remove();
    //    },
    //    error: function(){
    //        if($('.errorAlert').length<1){
    //            widget.modal.alert({
    //                cls: 'fade in errorAlert',
    //                content:'<strong>数据加载失败，请稍后重试</strong>',
    //                width:500,
    //                height:500,
    //                btnOkTxt: '',
    //                btnCancelTxt: '确定'
    //            });
    //        }
    //    },
    //    timeout: 5000
    //});

    Order.init();
});

//关闭dialog
function closeConfirm(dialogId) {
    $("#" + dialogId).modal("hide");
}
