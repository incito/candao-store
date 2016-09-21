/**
 * Created by Alex on 16/9/20.
 */
'use strict';

var activeinputele;

var dom = {
    remember: $('#J_remember_pwd'),
    pwd: $('#pwd'),
    user: $('#user'),
    thechangeSubmit: $('#J-thechange-submit')
};

var Login = {
    init: function(){

        this.bindEvent();

        this.setDefaultLoginForm();

        this.getBranchInfo();
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
            $.ajax({
                url: _config.interfaceUrl.AuthorizeLogin,
                method: 'POST',
                contentType: "application/json",
                data: JSON.stringify({
                    username: $.trim(dom.user.val()),
                    password: hex_md5($.trim(dom.pwd.val())),
                    macAddress: '96121CBC21EF02256E9C5F2E602C5441',
                    loginType: '030201'
                }),
                dataType:'json',
                success: function(res){
                    if(res.code === '0') {

                        utils.storage.setter('fullname', res.data.fullname);
                        utils.storage.setter('loginTime', res.data.loginTime);
                        utils.storage.setter('aUserid', dom.user.val());

                        that.setUserRight(dom.user.val());

                        $("#change_val").val("");
                        $("#thechange-dialog").modal("show");

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


        //虚拟键盘
        $(".virtual-keyboard ul li").click(function(){
            var keytext = $(this).text();
            if(activeinputele != null && activeinputele != undefined){
                if(keytext == "←"){
                    activeinputele.focus();
                    backspace();
                }else if(keytext == "."){
                    return false;
                }else{
                    var val = activeinputele.val();
                    val = val + keytext;
                    activeinputele.val(val);
                    activeinputele.focus();
                }
            }
            that.keyUp("#change_val");
        });
        $("input").focus(function(event){
            activeinputele = $(this);
        });

        //找零
        dom.thechangeSubmit.click(function(){
            $("#confirm-change-val").text($("#change_val").val());
            $('#confirm-dialog').show();
        });
    },

    setUserRight: function(username){
        $.ajax({
            url: _config.interfaceUrl.GetUserRight,
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify({
                username: username
            }),
            dataType:'json',
            success: function(res){
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
        window.location = "../views/main.jsp";
    },

    keyUp: function(o){
        var me = $(o);
        if(me.val() != null && me.val() != ""){
            $("#J-thechange-submit").attr("disabled", false);
        }else{
            $("#J-thechange-submit").attr("disabled", true);
        }
    }
};

$(document).ready(function(){
    Login.init();
});
