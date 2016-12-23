/**
 * Created by Alex on 16/9/20.
 */
'use strict';
$(function () {
    $('input:text:first').focus();

});
var activeinputele;

var dom = {
    remember: $('#J_remember_pwd'),
    pwd: $('#pwd'),
    user: $('#user'),
    thechangeSubmit: $('#J-thechange-submit')
};
/*$(document).keydown(function(event){
    if(event.keyCode==13){
        $('.btn-primary').each(function () {
            var me=$(this);
            if(me.hasClass('J-login-form-submit')){
                me.click();
                return false
            }
            if(me.attr('id')=='J-thechange-submit'){
                me.click();
                return false
            }

        })

    }
});*/
var Login = {
    init: function(){
        /*绑定虚拟键盘*/
        widget.keyboard({
            target: '.virtual-keyboard'
        });

        this.saveConfigInfo();

        this.bindEvent();

        this.setDefaultLoginForm();

        //this.getBranchInfo();


    },

    bindEvent: function(){
        var that = this;

        //记住密码
        dom.remember.click(function(){
            var me = $(this);

            if(me.is(':checked')) {
                utils.storage.setter('remember_user', $.trim(dom.user.val()));
                utils.storage.setter('remember_pwd', $.trim(dom.pwd.val()));
            } else {
                utils.storage.remove('remember_user');
                utils.storage.remove('remember_pwd');
            }

        });
        //登录
        $('.J-login-form-submit').click(function(){
            Log.send(2, '登录参数:' + JSON.stringify({
                    username: $.trim(dom.user.val()),
                    password: hex_md5($.trim(dom.pwd.val())),
                    macAddress: utils.storage.getter('ipaddress'),//IP地址
                    loginType: '030201'
                }));
            $.ajax({
                url: _config.interfaceUrl.AuthorizeLogin,
                method: 'POST',
                contentType: "application/json",
                data: JSON.stringify({
                    username: $.trim(dom.user.val()),
                    password: hex_md5($.trim(dom.pwd.val())),
                    macAddress: utils.storage.getter('ipaddress'),//IP地址
                    loginType: '030201'
                }),
                dataType:'json',
                success: function(res){
                    Log.send(2, '登录返回:' + JSON.stringify(res));
                    if(res.code === '0') {
                        utils.storage.setter('fullname', res.data.fullname);
                        utils.storage.setter('loginTime', res.data.loginTime);
                        utils.storage.setter('aUserid',  $.trim(dom.user.val()));
                        utils.storage.setter('aUserid', dom.user.val(),1)//设置sessionStorage
                        that.setUserRight($.trim(dom.user.val()));
                        that.getBranchInfo();
                        var iSuserRight=utils.userRight.get($.trim(dom.user.val()),'030206');
                        if(iSuserRight){//验证是否有收银权限，iSuserRight为true时验证零找金，false直接跳转登录页面
                            $.ajax({//验证零找金
                                url: _config.interfaceUrl.PettyCashInput+''+utils.storage.getter('aUserid')+'/'+utils.storage.getter('ipaddress')+'/0/0/',
                                type: "get",
                                dataType: "text",
                                success: function (data) {
                                    var  data=JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                                    Log.send(2, '验证零找金:' + JSON.stringify(data));
                                    if(data.Data=='0'){
                                        $("#change_val").val("");
                                        focusIpt = $('#change_val');
                                        $("#thechange-dialog").modal("show");
                                    }
                                    else {
                                        window.location = "../views/main.jsp";
                                    }
                                }
                            });
                        }
                        else {
                            if(utils.storage.getter('isYesterdayEndWork')==='1'){
                                Log.send(2, '请登录收银员账号，以便餐台结账')
                                widget.modal.alert({
                                    cls: 'fade in',
                                    content:'<div style="text-align: center;font-size: 20px;font-weight:bold ">请登录收银员账号，以便餐台结账。</div>',
                                    title:'',
                                    width:500,
                                    height:500,
                                    btnOkTxt: '确定',
                                    btnCancelTxt: '',
                                });
                            }
                            else {
                                window.location = "../views/main.jsp";
                            }

                        }


                    } else {
                        widget.modal.alert({
                            cls: 'fade in',
                            content:'<strong>' + res.msg + '</strong>',
                            width:500,
                            height:500,
                            btnOkTxt: '',
                            btnCancelTxt: '确定'
                        });
                    }
                }

            });

            return false;
        });
        //找零
        dom.thechangeSubmit.click(function(){
            $("#confirm-change-val").text($("#change_val").val());
            $('#confirm-dialog').modal('show');
        });
    },

    setUserRight: function(username){
        Log.send(2, 'GetUserRight: ' + username)
        $.ajax({
            url: _config.interfaceUrl.GetUserRight,
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify({
                username: username
            }),
            dataType:'json',
            async: false,
            success: function(res){
                Log.send(2, 'GetUserRight:' +  JSON.stringify(res));
                if(res.result === '0') {
                    utils.storage.setter('user_rights', JSON.stringify(res.rights));
                } else {
                    widget.modal.alert({
                        cls: 'fade in',
                        content:'<strong>' + res.msg + '</strong>',
                        width:500,
                        height:500,
                        btnOkTxt: '',
                        btnCancelTxt: '确定'
                    });
                }
            }

        })
    },

    getBranchInfo: function(){
        $.ajax({
            url: _config.interfaceUrl.GetBranchInfo,
            method: 'GET',
            type: 'json',
            async: false,
            success: function(res){
                if(res.code === '0') {
                    $.each(res.data,function(k,v){
                        utils.storage.setter('branch_' + k, v);
                    })
                } else {
                    widget.modal.alert({
                        cls: 'fade in',
                        content:'<strong>' + res.msg + '</strong>',
                        width:500,
                        height:500,
                        btnOkTxt: '',
                        btnCancelTxt: '确定'
                    });
                }
            }
        })
    },

    setDefaultLoginForm: function(){
        var sUser = utils.storage.getter('remember_user');
        var sPwd = utils.storage.getter('remember_pwd');

        if(sUser !== null &&  sUser !== '') {
            dom.remember.attr("checked", true);
            dom.user.val(sUser);
            dom.pwd.val(sPwd);

        } else {
            dom.remember.removeAttr('checked');
        }
    },

    toMain: function(){
        Log.send(2,' InputTellerCash:' + _config.interfaceUrl.PettyCashInput+''+utils.storage.getter('aUserid')+'/'+utils.storage.getter('ipaddress')+'/'+$.trim($('#change_val').val())+'/1/');
        $.ajax({
            url: _config.interfaceUrl.PettyCashInput+''+utils.storage.getter('aUserid')+'/'+utils.storage.getter('ipaddress')+'/'+$.trim($('#change_val').val())+'/1/',
            type: "get",
            dataType: "text",
            success: function (data) {
                var  data=JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                Log.send(2, 'InputTellerCash' + JSON.stringify(data))
                if(data.Data=='1'){
                    window.location = "../views/main.jsp";
                    $("#thechange-dialog").modal("hide");
                }
            }
        });

    },

    keyUp: function(o){
        var me = $(o);
        if(me.val() != null && me.val() != ""){
            $("#J-thechange-submit").attr("disabled", false);
        }else{
            $("#J-thechange-submit").attr("disabled", true);
        }
    },

    /*保存配置信息*/
    saveConfigInfo: function () {
        //设置配置信息
        $.ajax({
            url: _config.interfaceUrl.Config,
            type: "get",
            dataType: 'text',
            cache: false,
        }).then(function (res) {
            utils.storage.setter('config', res.split("*/")[1]);
            var config = JSON.parse(utils.storage.getter('config'));
            /*设置钱箱地址*/
            var cashIp = utils.getUrl.get("cashIp")//设置钱箱地址参数到缓存
            config['OpenCashIp'] = cashIp
            utils.storage.setter('config', JSON.stringify(config));
            Log.send(2, '设置配置信息' + JSON.stringify(config))
        });
        /*获取营业时间*/
        $.ajax({
            url: _config.interfaceUrl.GetTradeTime,
            type: "get",
            dataType: 'text',
            success: function (res) {
                Log.send(2, '获取营业时间' + JSON.stringify(res));
                var res = JSON.parse(res);
                if(res.result=='0'){
                    utils.storage.setter('getOpenEndTime',JSON.stringify(res.detail))//会员地址状态 viptype 1为餐道会员 2为雅坐会员
                }
                else {
                    utils.printError.alert('获取营业时间失败')
                }
                /*var member = {
                 'vipcandaourl': res.data.vipcandaourl,//餐道
                 'vipotherurl': res.data.vipotherurl//雅座
                 }

                 utils.storage.setter('vipType', res.data.viptype)//会员地址状态 viptype 1为餐道会员 2为雅坐会员
                 utils.storage.setter('memberAddress', JSON.stringify(member))//设置会员地址
                 utils.storage.setter('vipstatus', res.data.vipstatus)//会员是否开启*/
            }
        });

        //获取会员配置地址
        $.ajax({
            url: _config.interfaceUrl.GetMemberAddress,
            type: "get",
            dataType: 'text',
            success: function (res) {
                Log.send(2, '获取会员配置地址' + res);
                var res = JSON.parse(res);
                var member = {
                    'vipstatus': res.data.vipstatus,
                    'vipcandaourl': res.data.vipcandaourl,//餐道
                    'vipotherurl': res.data.vipotherurl//雅座
                }

                utils.storage.setter('vipType', res.data.viptype)//会员地址状态 viptype 1为餐道会员 2为雅坐会员
                utils.storage.setter('memberAddress', JSON.stringify(member))//设置会员地址
                utils.storage.setter('vipstatus', res.data.vipstatus)//会员是否开启
            }
        });

        //银行信息
        $.get(_config.interfaceUrl.GetAllBankInfo).then(function (res) {
            Log.send(2, '银行信息' + JSON.stringify(res));
            utils.storage.setter('banklist', JSON.stringify(res));
        });

        //门店信息
        $.get(_config.interfaceUrl.GetBranchInfo).then(function (res) {
            Log.send(2, '门店信息' + JSON.stringify(res));
            if (res.code === '0') {
                $.each(res.data, function (k, v) {
                    utils.storage.setter('branch_' + k, v);
                })
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
        });

        //零头信息
        $.ajax({
            url: _config.interfaceUrl.GetSystemSetData,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                    type: 'ROUNDING'
                }
            )
        }).then(function (res) {
            if(res.code=='0'){
                Log.send(2, '零头信息:' + JSON.stringify(res));
                utils.storage.setter('ROUNDING', JSON.stringify(res.data.rows));
            }
            else {
                utils.printError.alert(res.msg)
            }

        });

        //忌口
        $.ajax({
            url: _config.interfaceUrl.GetSystemSetData,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                    type: 'JI_KOU_SPECIAL'
                }
            )
        }).then(function (res) {
            if(res.code=='0'){
                Log.send(2, '忌口:' + JSON.stringify(res));
                utils.storage.setter('JI_KOU_SPECIAL', JSON.stringify(res.data.rows));
            }
            else {
                utils.printError.alert(res.msg)
            }

        });

        //餐具
        $.ajax({
            url: _config.interfaceUrl.GetSystemSetData,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                    type: 'DISHES'
                }
            )
        }).then(function (res) {
            if(res.code=='0'){
                Log.send(2, '餐具:' + JSON.stringify(res));
                utils.storage.setter('DISHES2', JSON.stringify(res.data.rows));
            }
            else {
                utils.printError.alert(res.msg)
            }
        });

        $.ajax({
            url: _config.interfaceUrl.GetDinnerWareInfo + utils.storage.getter('aUserid') + '/',
            method: 'get',
            contentType: "application/json",
            dataType: 'text',
        }).then(function (res) {
            Log.send(2, 'GetDinnerWareInfo:' + JSON.stringify(res));
            utils.storage.setter('DISHES', JSON.stringify(JSON.parse(res.substring(12, res.length - 3)).OrderJson));
        });


        //挂账单位
        $.ajax({
            url: _config.interfaceUrl.GetAllOnCpyAccountInfo,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
        }).then(function (res) {
            Log.send(2, '挂账单位:' + JSON.stringify(res));
            utils.storage.setter('payCompany', JSON.stringify(res));
        });
    }
};

$(document).ready(function(){
    Login.init();
});
