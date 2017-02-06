var OpenPage = {
    init: function () {
        //清除缓存
        utils.clearLocalStorage.clear()
        var that=this;
        $('#openTo').hide()

        //设置钱箱地址参数到缓存
        if(utils.getUrl.get("cashIp")) {
            utils.storage.setter('cashIp', utils.getUrl.get("cashIp"));
            utils.storage.setter('OpenCashPWD', '123456');
        } else {
            utils.printError.alert('钱箱地址配置错误')
            return false;
        }
        //设置ipaddress参数到缓存
        if(utils.getUrl.get("ipaddress")) {
            var ipaddress = utils.getUrl.get("ipaddress")//设置ipaddress参数到缓存
            utils.storage.setter("ipaddress", ipaddress);
        } else {
            utils.printError.alert('ipaddress参数配置错误')
            return false;
        }

        //设置posid参数到缓存
        if(utils.getUrl.get("posid")) {
            var posid = utils.getUrl.get("posid")//设置posid参数到缓存
            utils.storage.setter("posid", posid)
        } else {
            utils.printError.alert('posid配置错误')
            return false;
        }

        this.isYesterdayEndWork();
        this.bindEvent();

        var autoFill=utils.getUrl.get("autoFill")//自动填充现金金额。0：不填充，1：填充, 默认为填充
        utils.storage.setter("autoFill", autoFill ? autoFill : '1');
        /*if (ipaddress != null || posid != null) {
            utils.storage.setter("ipaddress", ipaddress);
            utils.storage.setter("posid", posid)

        }*/
        if (utils.storage.getter('cashbox')) {
            utils.storage.setter('cashbox', utils.storage.getter('cashbox'))//钱箱状态设置已设置状态
        }
        else {
            utils.storage.setter('cashbox', '0')//钱箱状态设置默认为0
        }

    },

    bindEvent: function () {

        /*$('.J-submit').click(function(){
         window.location = "../views/login.jsp";
         });*/

        $("#confirm-opening-btn").click(function () {
            $("#mg-login-dialog").modal("show");
            widget.keyboard();
        });

        $("#mg-login-dialog input").focus(function (event) {
            activeinputele = $(this);
        });
    },
    open: function () {
        /*验证开业权限*/
        if ($.trim($('#manager_num').val()) == '' || $.trim($('#perm_pwd').val()) == '') {
            utils.printError.alert('员工编号，权限密码不能为空');
            return false
        }
        var iSuserRight = utils.userRight.get($.trim($('#manager_num').val()), '030202');
        Log.send(2, '验证开业权限iSuserRight:' + iSuserRight);
        if (iSuserRight) {//验证是否有开业权限，iSuserRight为true时验证零找金，false直接跳转登录页面
            Log.send(2, '验证开业权限url:' + _config.interfaceUrl.RestaurantOpened + '' + $.trim($('#manager_num').val()) + '/' + $.trim($('#perm_pwd').val()) + '/' + utils.storage.getter('ipaddress') + '/1/');
            $.ajax({
                url: _config.interfaceUrl.RestaurantOpened + '' + $.trim($('#manager_num').val()) + '/' + $.trim($('#perm_pwd').val()) + '/' + utils.storage.getter('ipaddress') + '/1/',
                method: 'GET',
                dataType: 'text',
                success: function (res) {
                    Log.send(2, '验证开业权限返回:' + res);
                    var res = JSON.parse(res.substring(12, res.length - 3));
                    if (res.Data === '1') {//开业
                        utils.storage.setter("isOpen",true);
                        $("#mg-login-dialog").modal("hide");
                        window.location = "../views/login.jsp";
                    }
                    else {
                        utils.storage.setter("isOpen",false);
                        widget.modal.alert({
                            cls: 'fade in',
                            content: '<strong>' + res.Info + '</strong>',
                            width: 500,
                            height: 500,
                            btnOkTxt: '确定',
                            btnCancelTxt: ''
                        });
                    }
                },
            })
        }
        else if (iSuserRight == null) {
            return false
        }
        else {
            Log.send(3, '开业失败，您没有开业权限')
            utils.printError.alert('开业失败，您没有开业权限')
        }


    },
    isOpen: function () {
        $.ajax({
            url: _config.interfaceUrl.RestaurantOpened + '//' + utils.storage.getter('ipaddress') + '/0/',
            method: 'GET',
            dataType: 'text',
            success: function (res) {
                var res = JSON.parse(res.substring(12, res.length - 3));
                Log.send(2, '是否为开业返回:' + JSON.stringify(res));
                if (res.Data === '1') {//开业
                    utils.storage.setter("isOpen",true);
                    $('#openTo').hide();
                    window.location = "../views/login.jsp";
                }
                if(res.Data === '0'){//未开业
                    utils.storage.setter("isOpen",false);
                    $('#openTo').show();
                }
            },
            error: function () {
                Log.send(3, '获取当日结业信息失败');
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<strong>获取当日结业信息失败</strong>',
                    width: 500,
                    height: 500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
            }
        })
    },
    isYesterdayEndWork: function () {//昨天是否结业
        var that = this
        $.ajax({
            url: _config.interfaceUrl.CheckTheLastEndWork,
            method: 'GET',
            dataType: 'json',
            success: function (res) {
                Log.send(2, '昨天是否结业返回:' + JSON.stringify(res));
                if (res['result'] === '0') {//昨天已经结业返回成功
                    if (res['detail']) {//昨天已经结业
                        Log.send(2, '昨天已经结业返回成功,昨天已经结业');
                        $("#openTo").hide();
                        utils.storage.setter('isYesterdayEndWork', '0');//设置昨天是否结业状态0为已结业，1为未结业；
                        that.isOpen();
                    }
                    else {//昨天没有结业
                        utils.storage.setter('isYesterdayEndWork', '1');//设置昨天是否结业状态0为已结业，1为未结业；
                        $("#openTo").hide();
                        Log.send(2, '昨天没有结业');
                        $.ajax({//查询是否有为结业的餐台
                            url: _config.interfaceUrl.GetAllTableInfos,
                            method: 'GET',
                            dataType: 'json',
                            success: function (res) {
                                var noCheack = [];//没有结账的餐台数组
                                Log.send(2,'查询是否有为结业的餐台返回:' + JSON.stringify(res));
                                for (var i = 0; i < res.data.length; i++) {
                                    if (res.data[i].status === '1') {
                                        noCheack.push(res.data[i]);
                                        break
                                    }
                                }
                                ;
                                if (noCheack.length > 0) {
                                    var modalIns = widget.modal.alert({
                                        cls: 'fade in',
                                        content: '<div style="text-align: left;font-size: 20px;font-weight:bold ">昨日还有未结账的餐台，请先登录收银员账号结账，然后进行清机和结业。</div>',
                                        title: '',
                                        width: 500,
                                        height: 500,
                                        btnOkTxt: '确定',
                                        btnCancelTxt: '',
                                        btnOkCb: function () {
                                           window.location = "../views/login.jsp"
                                        }
                                    });
                                    $('#' + modalIns.id).find('.close').click(function () {
                                        window.location = "../views/login.jsp";
                                    })
                                }
                                else {
                                    widget.modal.alert({
                                        cls: 'fade in',
                                        content: '<div style="text-align: left;font-size: 20px;font-weight:bold ">昨日未清机且未结业，请先清机后结业。</div>',
                                        title: '',
                                        width: 500,
                                        height: 500,
                                        btnOkTxt: '确定',
                                        btnCancelTxt: '',
                                        btnOkCb: function () {
                                            $(".modal-alert:last,.modal-backdrop:last").remove();
                                            that.checkout();//清机结业
                                        }
                                    });

                                }
                            }
                        });
                    }
                }
                else {
                    /*widget.modal.alert({
                     cls: 'fade in',
                     content:'<strong>'+res.Info+'</strong>',
                     width:500,
                     height:500,
                     btnOkTxt: '确定',
                     btnCancelTxt: ''
                     });*/
                }
            },
        })
    },
    /*结业清机*/
    checkout: function () {
        var that = this;
        var Uncleandata = that.getFindUncleanPosList();
        var arrylength = Uncleandata.LocalArry.length - 1;
        var LocalArry = Uncleandata.LocalArry;
        if (Uncleandata.LocalArry.length > 0) {
            $("#J-btn-checkout-dialog").load("../views/check/impower.jsp", {
                'title': '清机授权',
                'userNmae': Uncleandata.LocalArry[arrylength].username,
                'usernameDisble': '2',
                'cbd': 'OpenPage.clearAllcheckOut()',
                'userRightNo': '030204'
            });
            $("#J-btn-checkout-dialog").modal('show')
        }
        if (Uncleandata.LocalArry.length == 0 && Uncleandata.OtherArry.length > 0) {
            Log.send(2, '还有其他POS机未清机,请到其他POS机上先清机');
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
                'cbd': 'OpenPage.checkoutCallback()',
                'userRightNo': '030205'
            });
            $("#J-btn-checkout-dialog").modal('show')
        }


    },
    clearAllcheckOut: function () {
        var that = this;
        var aUserid=$.trim($('#user').val())
        $("#J-btn-checkout-dialog").modal('hide')
        var that = this;
        widget.modal.alert({
            cls: 'fade in',
            content: '<strong>清机中，请稍后</strong>',
            width: 500,
            height: 500,
            hasBtns: false,
        });
        Log.send(2, '清机' + _config.interfaceUrl.Clearner + '' + aUserid + '/' + utils.storage.getter('checkout_fullname') + '/' + utils.storage.getter('ipaddress') + '/' + utils.storage.getter('posid') + '/' + utils.storage.getter('checkout_fullname') + '/')
        $.ajax({
            url: _config.interfaceUrl.Clearner + '' + aUserid + '/' + utils.storage.getter('checkout_fullname') + '/' + utils.storage.getter('ipaddress') + '/' + utils.storage.getter('posid') + '/' + utils.storage.getter('checkout_fullname') + '/',
            type: "get",
            dataType: "text",
            success: function (data) {
                var data = JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                Log.send(2, '清机返回:' + JSON.stringify(data));
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
                    Log.send(2, '清机成功, 打印清机单');
                    utils.reprintClear.get()//打印清机单
                    $(".modal-alert:last,.modal-backdrop:last").remove();
                    that.checkout()
                }
            }
        });
    },
    /*结业回调*/
    checkoutCallback: function () {//结业回调
        $.ajax({
            url: _config.interfaceUrl.EndWork,//不需要传递参数
            type: "get",
            dataType: 'text',
            success: function (data) {
                $("#J-btn-checkout-dialog").modal('hide')
                var data = JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                Log.send(2, '结业返回:' + JSON.stringify(data));
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
                        Log.send(2, '结业数据上传:' + JSON.stringify({
                                'synkey': 'candaosynkey'
                            }));
                        $.ajax({
                            url: _config.interfaceUrl.EndWorkSyncData,//结业数据上传
                            method: 'POST',
                            contentType: "application/json",
                            dataType: 'json',
                            data: JSON.stringify({
                                'synkey': 'candaosynkey'
                            }),
                            success: function (msg) {
                                //成功
                                Log.send(2, '结业数据上传返回:' + JSON.stringify(msg));
                                $(".modal-alert:last,.modal-backdrop:last").remove();
                                if (msg.code == '0000') {
                                    Log.send(2, '结业数据上传:' + data.Info + ',即将退出程序');
                                    widget.modal.alert({
                                        cls: 'fade in',
                                        content: '<strong>' + data.Info + ',即将退出程序</strong>',
                                        width: 500,
                                        height: 500,
                                        btnOkTxt: '确定',
                                        btnCancelTxt: '',
                                        btnOkCb: function () {
                                            $(".modal-alert:last,.modal-backdrop:last").remove();
                                            window.location = '../views/openpage.jsp?ipaddress=' + utils.storage.getter('ipaddress') + '&posid=' + utils.storage.getter('posid') + '&cashIp=' +utils.storage.getter('cashIp');
                                            //结业成功清除缓存
                                            utils.clearLocalStorage.clear()
                                            Log.send(2, '清空缓存');
                                        }
                                    });
                                    $('.modal-alert:last .modal-header .close').hide();//隐藏X关闭按钮
                                }
                                //失败
                                else {
                                    Log.send(2, '上传营业数据失败，请重新上传！');
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
                                            window.location = '../views/openpage.jsp?ipaddress=' + utils.storage.getter('ipaddress') + '&posid=' + utils.storage.getter('posid') + '&cashIp='+utils.storage.getter('cashIp');
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
                findUncleanPosList = JSON.parse(data);
                Log.send(2, '获取未清机数据列表:' + JSON.stringify(data));
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
};

function toLogin() {
    OpenPage.open()
}


$(function () {
    OpenPage.init();
});


