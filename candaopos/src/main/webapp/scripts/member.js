$(function () {
    SetBotoomIfon.init()//设置底部信息
    member.int();
    ya_Member.int();
})
var loadMember = null, memberAddress = JSON.parse(utils.storage.getter('memberAddress'));
var member = {
    int: function () {
        this.bindEvent();
    },
    /*餐绑定操作按钮*/
    bindEvent: function () {
        var that = this;
        var $dishType = $('.coupon-box');
        var flag_prev = 0;

        $('.member-op-list li').on('click', function () {
            var me = $(this);
            if (me.hasClass('J-modify-base')) {
                if (that.isClick()) {
                    if(that.isLossisClick()) {
                        $("#modify-base-dialog").load("../member/modifyBase.jsp");
                        $("#modify-base-dialog").modal("show");
                    }
                    else {
                        that.errorAlert('会员卡处于挂失状态，不能修改基本信息')
                    }
                }
            }

            if (me.hasClass('J-modify-phone')) {
                if (that.isClick()) {
                    if(that.isLossisClick()){
                        $("#modify-phone-dialog").load("../member/modifyPhone.jsp");
                        $("#modify-phone-dialog").modal("show");
                    }
                    else {
                        that.errorAlert('会员卡处于挂失状态，不能修改手机号码')
                    }
                }
            }

            if (me.hasClass('J-modify-pwd')) {
                if (that.isClick()) {
                    if(that.isLossisClick()) {
                        $("#modify-pwd-dialog").load("../member/modifyPwd.jsp");
                        $("#modify-pwd-dialog").modal("show");
                    }
                    else {
                        that.errorAlert('会员卡处于挂失状态，不能修改消费密码')
                    }
                }
            }

            if(me.hasClass('J-modify-storge')){
                if (that.isClick()) {
                    if(that.isLossisClick()){
                        var card_type = loadMember.result[0].card_type,
                            cardNo= null;
                        /*如果该会员卡是虚拟卡传递电话号码*/
                        if(card_type=='0'){
                            cardNo=loadMember.mobile
                        }
                        /*如果该会员卡是实体卡传递卡号*/
                        else {
                            cardNo=loadMember.result[0].MCard
                        }
                        var _url='../member/storge.jsp?cardMember=' +$.trim(cardNo) + '';//传递会员电话号码
                        window.location.href=encodeURI(encodeURI(_url));
                    }
                    else {
                        that.errorAlert('会员卡处于挂失状态，不能会员储值')
                    }
                }
            }

            if (me.hasClass('J-cancellation')) {
                if (that.isClick()) {
                    var status = $.trim($('.member_status').attr('status'));
                    if (status == 1) {
                        var member_card = $.trim($('.member_card').text());
                        widget.modal.alert({
                            cls: 'fade in',
                            content: '<strong>确定要注销' + member_card + '吗？</strong>',
                            width: 500,
                            height: 500,
                            btnOkTxt: '确定',
                            btnCancelTxt: '取消',
                            btnOkCb: function () {
                                that.memberDelete(member_card);
                            }
                        });

                    }
                }
            }

            if (me.hasClass('J-modify-lost')) {
                if (that.isClick()) {
                    var card_type = $.trim($('.member_card').attr('card_type'));
                    if (card_type != '1') {
                        that.errorAlert('该会员还没有绑定实体卡，不能挂失');
                        return false
                    }
                    else {
                        var member_card = $.trim($('.member_card').text());
                        widget.modal.alert({
                            cls: 'fade in',
                            content: '<strong>确定要挂失 ' + member_card + '吗？</strong>',
                            width: 500,
                            height: 500,
                            btnOkTxt: '确定',
                            btnCancelTxt: '取消',
                            btnOkCb: function () {
                                that.memberlost(member_card);
                            }
                        });
                    }
                }
            }

            if (me.hasClass('J-modify-cancellost')) {
                if (that.isClick()) {
                    var card_type = $.trim($('.member_card').attr('card_type'));
                    if (card_type != '1') {
                        that.errorAlert('该会员还没有绑定实体卡，不能解除挂失');
                        return false
                    }
                    else {
                        var member_card = $.trim($('.member_card').text());
                        widget.modal.alert({
                            cls: 'fade in',
                            content: '<strong>确定要解除挂失 ' + member_card + '吗？</strong>',
                            width: 500,
                            height: 500,
                            btnOkTxt: '确定',
                            btnCancelTxt: '取消',
                            btnOkCb: function () {
                                that.memberCancel(member_card);
                            }
                        });
                    }
                }
            }
            /*绑定会员卡*/
            if (me.hasClass('J-modify-binding')) {
                if (that.isClick()) {
                    if (that.isClick()) {
                        if(that.isLossisClick()==false){
                            that.errorAlert('会员卡处于挂失状态，不能新增实体卡')
                            return false
                        }
                        $("#modify-binding-dialog").load("../member/bingCard.jsp", {
                            'title': '新增实体卡-请刷卡',
                            'type': '1',
                            'cbd': 'member.bindingCard()',
                        });
                        $("#modify-binding-dialog").modal("show");
                    }
                }
            }
            /*修改会员卡*/
            if (me.hasClass('J-modify-edit')) {
                if (that.isClick()) {
                    if (that.isClick()) {
                        if(loadMember.result[0].card_type=='0'){
                            that.errorAlert('该会员还没有绑定实体卡，不能修改会员卡号');
                            return false
                        }
                        if(that.isLossisClick()==false){
                            that.errorAlert('会员卡处于挂失状态，不能修改会员卡号')
                            return false
                        }
                        $("#modify-binding-dialog").load("../member/bingCard.jsp", {
                            'title': '修改会员卡号-请刷卡',
                            'type': '2',
                            'cbd': 'member.modificationCard()',
                        });

                        $("#modify-binding-dialog").modal("show");
                    }
                }
            }

        });
        //充值优惠向左向右按钮
        $(".coupon-box .next").click(function () {
            var count = $dishType.find(".coupon-List .coupon-item").length;
            if (flag_prev < count - 8) {
                $dishType.find(".coupon-item").eq(flag_prev).css({'margin-left': '-142px'}).hide();
                flag_prev++;
                $(".coupon-box .prev").removeClass('unclick');
                if (flag_prev == count - 8) {
                    $(".coupon-box .next").addClass('unclick');


                }

            }
        });
        $(".coupon-box .prev").click(function () {
            if (flag_prev >= 1) {
                $dishType.find(".coupon-item").eq(flag_prev - 1).css({'margin-left': '2px'}).show();
                flag_prev--;
                $(".coupon-box .next").removeClass('unclick');
                if (flag_prev == 0) {
                    $(".coupon-box .prev").addClass('unclick');

                }

            }

        });
        /*选择优惠*/
        {
        }
        $('.coupon-box').on('click', '.coupon-item', function () {
            if ($(this).hasClass('active')) {
                $(this).removeClass('active')
            }
            else {
                $(this).addClass('active').siblings('.coupon-item').removeClass('active');
                var rechargeMoney=$.trim($('#rechargeMoney').val());
                var presentvalue=$.trim($(this).attr('presentvalue'));//赠送比例
                var dealvalue=$.trim($(this).attr('dealvalue'));//满多少赠送
                if(rechargeMoney!=''&& member.ismoney(rechargeMoney)===true){
                    if(dealvalue){
                        $('.giveMoney').text(parseInt(rechargeMoney/dealvalue)*presentvalue)
                    }
                    else {
                        $('.giveMoney').text(presentvalue)
                    }

                }
                else {
                    $('.giveMoney').text(0)
                }
            }

        })
    },
    /*会员查询*/
    memberSearch: function (msg) {
        var memberCardno = '', that = this
        that.clearInfo();
        if (msg) {
            memberCardno = msg;
        }
        else {
            memberCardno = $.trim($('#Member_cardno').val())
        }
        if (memberCardno == '') {
            that.errorAlert('查询信息不能为空');
            return
        }
        Log.send(2, '会员查询回传参数：'+JSON.stringify({
                url: memberAddress.vipcandaourl + _config.interfaceUrl.VipQuery,
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                cardno: memberCardno
            }));
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.VipQuery,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                cardno: memberCardno
            }),
            success: function (res) {
                var gender, status;
                //console.log(res)
                Log.send(2, '会员查询返回参数：'+JSON.stringify(res));
                if (res.retcode == 0) {
                    if (res.result.length > 1) {
                        var memberCard = [];
                        for (var i = 0; i < res.result.length; i++) {
                            memberCard.push(res.result[i].MCard)
                        }
                        utils.chooseMember.choose({
                            'data': memberCard, 'callback': 'member.memberSearch(chooseNo)'
                        })
                        return false
                    }
                    loadMember = res;
                    if(res.result[0].card_type=='0'){
                        $('.member_card').text('').attr('card_type', res.result[0].card_type)//卡号
                    }
                    else {
                        $('.member_card').text(res.result[0].MCard).attr('card_type', res.result[0].card_type)//卡号
                    }

                    $('.member_mobile').text(res.mobile)//手机号
                    $('.member_nanme').text(res.name)//姓名
                    $('.member_level_name').text(res.result[0].level_name)//会员卡等级名称
                    $('.member_birthday').text((res.birthday).substring(0, res.birthday.length - 8))//生日
                    switch (res.gender) {
                        case '0' :
                            gender = '男';
                            break;
                        case '1' :
                            gender = '女';
                            break;
                        default :
                            gender = '未知';
                            break;
                    }
                    $('.member_gender').text(gender)//性别
                    $('.member_StoreCardBalance').text(res.result[0].StoreCardBalance)//余额
                    $('.member_IntegralOverall').text(res.result[0].IntegralOverall)//会员积分
                    switch (res.result[0].status) {
                        case '0' :
                            status = '注销';
                            break;
                        case '1' :
                            status = '正常';
                            break;
                        case '2' :
                            status = '挂失';
                            break;
                        default :
                            status = '未知';
                            break;
                    }
                    $('.member_status').text(status).attr('status', res.result[0].status)//会员卡状态
                    if (res.result[0].status == 2 && res.result[0].card_type == '1') {
                        $('.J-modify-cancellost').show();
                        $('.J-modify-lost').hide();
                    }
                    if (res.result[0].status == 1) {
                        $('.J-modify-lost').show();
                        $('.J-modify-cancellost').hide();
                    }
                }
                if (res.retcode == 1) {
                    that.errorAlert('会员查询错误：' + res.retInfo)
                }
            }
        })

    },
    /*充值优惠列表*/
    getCouponList: function () {
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.GetCouponList,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                branch_id: utils.storage.getter('branch_id'),
                current: '',
                pageSize: ''
            }),
            success: function (res) {
                //console.log(res)
                var str = '';
                for (var i = 0; i < res.datas.length; i++) {
                    str += '<div class="coupon-item" preferentialid="' + res.datas[i].id + '" presentValue="' + res.datas[i].presentValue + '" dealValue="' + res.datas[i].dealValue + '">' + res.datas[i].name + '</div>'
                }
                if (res.datas.length > 8) {
                    $(".coupon-box .next").removeClass('unclick')
                }
                $('.coupon-List').html(str)
            }
        })
    },

    /*发送验证嘛*/
    sendVerifyCode: function (msg) {
        var that = this
        Log.send(2, '发送验证开始：'+JSON.stringify({
                url: memberAddress.vipcandaourl + _config.interfaceUrl.SendVerifyCode,
                mobile: msg,
            }));
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.SendVerifyCode,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                mobile: msg
            }),
            success: function (res) {
                Log.send(2, '发送验证结束返回参数：'+JSON.stringify(msg));
               // console.log(res)
                if (res.Retcode == 0) {
                    valicode = res.valicode
                }
                else {
                    that.errorAlert('发送验证码'+res.RetInfo+'，请稍后重试！')
                }
            }
        })
    },
    /*修改手机号码*/
    changephone: function () {
        var that = this,
            newphone = $.trim($('#newphone').val()),
            oldphone = $.trim($('#oldphone').val()),
            phoneCode = $.trim($('#phoneCode').val());
        if (that.isPhoneNo(newphone) === false) {
            that.errorAlert('请输入正确的手机号码');
            return false
        }
        if (phoneCode != valicode || phoneCode == '') {
            that.errorAlert('请输入正确的验证码');
            return false
        }
        Log.send(2, '会员修改手机号回传参数：'+JSON.stringify({
                url: memberAddress.vipcandaourl + _config.interfaceUrl.VipChangeInfo,
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile: oldphone,
                new_mobile: newphone
            }));
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.VipChangeInfo,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile: oldphone,
                new_mobile: newphone
            }),
            success: function (res) {
                Log.send(2, '会员修改手机号返回参数：'+JSON.stringify(res));
                if (res.retcode == 0) {
                    $('#modify-phone-dialog').modal('hide').html('');
                    that.succeedAlert({'info':'修改手机号码成功','callBack':'member.memberSearch($(".member_card").text())'});
                }
                if (res.retcode == 1) {
                    that.errorAlert(res.retInfo)
                }
                //console.log(res)
            }
        })

    },
    /*修改会员基本信息*/
    changebaseInfo: function () {
        var birthday = $.trim($('.base_memberbirthday').val()),
            name = $.trim($('.base_membername_input').val()),
            mobile = $.trim($('.base_memberphone').text()),
            gender = $('.radio-box input:checked').val(),
            that = this;
        Log.send(2, '修改会员基本信息回传参数：'+JSON.stringify({
                url: memberAddress.vipcandaourl + _config.interfaceUrl.VipChangeInfo,
                branch_id: utils.storage.getter('branch_id'),
                mobile: mobile,
                name: name,
                gender: gender,
                birthday: birthday
            }));
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.VipChangeInfo,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                branch_id: utils.storage.getter('branch_id'),
                mobile: mobile,
                name: name,
                gender: gender,
                birthday: birthday
            }),
            success: function (res) {
                Log.send(2, '修改会员基本信息返回参数：'+JSON.stringify(res));
                //console.log(res)
                if (res.retcode == 0) {
                    $('#modify-base-dialog').modal('hide').html('');
                    that.succeedAlert({'info':'修改会员基本信息成功','callBack':'member.memberSearch($(".member_card").text())'
                    })
                }
                if (res.retcode == 1) {
                    that.errorAlert(res.retInfo)
                }

            }
        })
    },
    /*设置会员基本信息*/
    setBaseinfo: function () {
        $('.base_membercard').text(loadMember.result[0].MCard)//卡号
        $('.base_memberphone').text(loadMember.mobile)//手机
        $('.base_membernanme').text(loadMember.name)//姓名
        $('.base_membername_input').val(loadMember.name)//姓名
        $('.base_memberbirthday').val((loadMember.birthday).substring(0, loadMember.birthday.length - 8))//卡号
        $('.radio-box input').each(function () {
            if (loadMember.gender == $(this).val()) {
                $(this).attr('checked', true)
            }
        })
    },
    /*修改会员消费密码*/
    changePassword: function () {
        var that = this,
            mobile = $.trim($('#changPsd').val()),
            password = $.trim($('#newPsd').val()),
            Rpassword = $.trim($('#rnewPsd').val()),
            phoneCode = $.trim($('#phoneCode').val());
        if (phoneCode != valicode || phoneCode == '') {
            that.errorAlert('请输入正确的验证码');
            return false
        }
        if (that.isNumber(password) === false || that.isNumber(Rpassword) === false) {
            that.errorAlert('只能输入六位数字的密码');
            return false
        }
        if (password != Rpassword) {
            that.errorAlert('两次输入的密码不一致，请重新输入');
            return false
        }
        Log.send(2, '修改会员消费密码回传参数：'+JSON.stringify({
                url: memberAddress.vipcandaourl + _config.interfaceUrl.VipChangeInfo,
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile: mobile,
                password: Rpassword
            }));
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.VipChangeInfo,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile: mobile,
                password: Rpassword
            }),
            success: function (res) {
                Log.send(2, '修改会员消费密码返回参数：'+JSON.stringify(res));
                if (res.retcode == 0) {
                    $('#modify-pwd-dialog').modal('hide').html('');
                    that.errorAlert('修改消费密码成功')
                }
                if (res.retcode == 1) {
                    that.errorAlert(res.retInfo)
                }
                //console.log(res)
            }
        })
    },
    /*挂失会员卡*/
    memberlost: function (member_card) {
        var that = this;
        $(".modal-alert:last,.modal-backdrop:last").remove();
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.ReportLossCanDao,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                branch_id: utils.storage.getter('branch_id'),
                cardno: member_card,
                FMemo: '',
            }),
            success: function (res) {
                //console.log(res)
                if (res.Retcode == 0) {
                    that.succeedAlert({'info':'挂失会员卡' + member_card + '成功','callBack':'member.memberSearch($(".member_card").text())'});
                }
                else {
                    that.errorAlert('挂失会员卡' + member_card + '失败')
                }
            }
        })
    },
    /*解除会员挂失*/
    memberCancel: function (member_card) {
        var that = this;
        $(".modal-alert:last,.modal-backdrop:last").remove();
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.CancelLossCanDao,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                branch_id: utils.storage.getter('branch_id'),
                cardno: member_card,
                FMemo: '',
            }),
            success: function (res) {
                //console.log(res)
                if (res.Retcode == 0) {
                    that.succeedAlert({'info':'解除挂失会员卡' + member_card + '成功','callBack':'member.memberSearch($(".member_card").text())'});
                }
                else {
                    that.errorAlert('解除挂失会员卡' + member_card + '失败')
                }
            }
        })
    },
    /*会员注销*/
    memberDelete: function (member_card) {
        var that = this;
        $(".modal-alert:last,.modal-backdrop:last").remove();
        Log.send(2, '会员注销开始：'+JSON.stringify({
                url: memberAddress.vipcandaourl + _config.interfaceUrl.CancelCanDao,
                branch_id: utils.storage.getter('branch_id'),
                cardno: member_card
            }));
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.CancelCanDao,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                branch_id: utils.storage.getter('branch_id'),
                cardno: member_card
            }),
            success: function (res) {
                Log.send(2, '会员注销接口返回数据：'+JSON.stringify(res));
                if (res.Retcode == 0) {
                    that.succeedAlert({'info':'注销' + member_card + '成功','callBack':'window.location.href="../member/view.jsp"'});
                }
                else {
                    that.errorAlert(res.RetInfo)
                }
            }
        })

    },
    /*会员注册*/
    register: function () {
        var that = this,
            moblie = $.trim($('#phone').val()),
            phoneCode = $.trim($('#phoneCode').val()),
            gender = $.trim($('#gender input:checked').val()),
            birthday = $.trim($('#birthday').val()),
            psd = $.trim($('#psd').val()),
            rpsd = $.trim($('#rpsd').val()),
            name = $.trim($('#nmae').val()),
            cardno=$.trim($('#sitiCard').val());


        if (that.isPhoneNo(moblie) === false) {
            that.errorAlert('请输入正确的手机号码');
            return false
        }
        ;
        if (that.isPhoneRepeat(moblie) === false) {
            that.errorAlert('请输入正确的手机号码');
            return false
        }
        if (phoneCode == '' || phoneCode != valicode) {
            that.errorAlert('请输入正确的验证码');
            return false
        }
        if (name == '') {
            that.errorAlert('请输入姓名');
            return false
        }
        if (that.isNumber(psd) === false || that.isNumber(rpsd) === false) {
            that.errorAlert('只能输入六位数字的密码');
            return false
        }
        if (psd != rpsd) {
            that.errorAlert('两次输入的密码不一致，请重新输入');
            return false
        }
        if(cardno){
            cardno=cardno
        }
        else {
            cardno=''
        }
        Log.send(2, '会员注册开始：'+JSON.stringify({
                url: memberAddress.vipcandaourl + _config.interfaceUrl.RegistCanDao,
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile: moblie,
                cardno: cardno,
                password: rpsd,
                name: name,
                gender: gender,
                birthday: birthday,
                channel: '1',
                branch_addr: utils.storage.getter('branch_branchaddress')
            }));
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.RegistCanDao,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile: moblie,
                cardno: cardno,
                password: rpsd,
                name: name,
                gender: gender,
                birthday: birthday,
                channel: '1',
                branch_addr: utils.storage.getter('branch_branchaddress')
            }),
            success: function (res) {
                Log.send(2, '会员注册接口返回数据：'+JSON.stringify(res));
                if (res.Retcode == 0) {
                    that.succeedAlert({
                        'info':'注册会员成功',
                        'callBack':'window.location = "../main.jsp"'
                    })
                    $('#phone').val('');
                    $('#phoneCode').val('');
                    $('#birthday').val('');
                    $('#psd').val('');
                    $('#rpsd').val('')
                    $('#nmae').val('');
                    $('#sitiCard').val('');
                    $('#shitcard_info').show();//会员实体绑定按钮
                    $('#shitcard_input').hide();//会员实体卡卡哈显示信息
                    clearTimeout(timer)//清除60S倒计时
                }
                if (res.Retcode == 1) {
                    that.errorAlert(res.retInfo)
                }
                //console.log(res)
            }
        })
    },
    /*会员储值*/
    stored_value: function (msg) {
        var that = this,
            cardno = null,//卡号
            ChargeType = $.trim($('.pay-type-select .active').attr('ChargeType')),//支付类型
            Amount = $.trim($('#rechargeMoney').val()),//充值金额
            preferential_id=$.trim($('.coupon-List .active').attr('preferentialid')),//优惠券ID
            giveValue=$.trim($('.giveMoney').text())//赠送金额

        if (msg) {
            cardno = msg
        }
        else {
            cardno = $.trim($('#rechargeMoblie').val());
            if (cardno == '') {
                that.errorAlert('手机号码或卡号不能为空，请检查');
                return false
            }
        }
        if (that.ismoney(Amount) === false) {
            that.errorAlert('充值金额为空或格式不正确');
            return false
        }
        if(preferential_id){
            preferential_id=preferential_id
        }
        else {
            preferential_id=''
        }
        str = '<strong>您确定给会员号“' + cardno + '”<br>储值“' + Amount + '”元吗？</strong>'
        widget.modal.alert({
            cls: 'fade in ya_rechargeSave',
            content: str,
            width: 500,
            height: 500,
            btnOkTxt: '确定',
            btnCancelTxt: '取消',
            btnOkCb:function () {
                $(".modal-alert:last,.modal-backdrop:last").remove();
                saveInfomsg();
            }
        });
        /*会员卡是否存在*/
        function  saveInfomsg() {
            $.ajax({
                url: memberAddress.vipcandaourl + _config.interfaceUrl.QueryCanDao,
                method: 'POST',
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify({
                    securityCode: '',
                    branch_id: utils.storage.getter('branch_id'),
                    cardno: cardno,
                    password:''
                }),
                success: function (res) {
                    //console.log(res)
                    if (res.Retcode == 0) {
                        //注释会员卡
                        /*if (res.CardList.length > 1) {
                            var memberCard = [];
                            for (var i = 0; i < res.CardList.length; i++) {
                                memberCard.push(res.CardList[i].cardno)
                            }
                            utils.chooseMember.choose({
                                'data': memberCard, 'callback': 'member.stored_value(chooseNo)'
                            })
                            return false
                        }
                        cardno=res.CardList[0].cardno*/
                        savevale()
                    }
                    if (res.Retcode == 1) {
                        that.errorAlert(res.RetInfo);
                    }
                }
            });
        }

        function savevale() {
            Log.send(2, '会员储值开始：'+JSON.stringify({
                    /*'url':memberAddress.vipcandaourl + _config.interfaceUrl.StorageCanDao,*/
                    'url':_config.interfaceUrl.NweStorageCanDao,
                    /*'Serial':utils.storage.getter('branch_id'),*/
                    'Serial':ya_Member.ya_formatDate(new Date(), "yyyyMMddHHmmssffff"),/*生成唯一编号如201701091543310804*/
                    'branch_id':utils.storage.getter('branch_id'),
                    'cardno':cardno,
                    'Amount':Amount,
                    'TransType':0,
                    'ChargeType':ChargeType,
                    'preferential_id':preferential_id,
                    'giveValue':giveValue,
                    'securityCode':'',
                    'user_id':utils.storage.getter('aUserid'),
                }));
            $.ajax({
                /*url:memberAddress.vipcandaourl + _config.interfaceUrl.StorageCanDao,*/
                url:_config.interfaceUrl.NweStorageCanDao,
                method: 'POST',
                contentType: "application/json",
                dataType: 'json',
                data:JSON.stringify({
                    /*'Serial':utils.storage.getter('branch_id'),*/
                    'Serial':ya_Member.ya_formatDate(new Date(), "yyyyMMddHHmmssffff"),/*生成唯一编号如201701091543310804*/
                    'branch_id':utils.storage.getter('branch_id'),
                    'cardno':cardno,
                    'Amount':Amount,
                    'TransType':0,
                    'ChargeType':ChargeType,
                    'preferential_id':preferential_id,
                    'giveValue':giveValue,
                    'securityCode':'',
                    'user_id':utils.storage.getter('aUserid'),
                }),
                success: function (res) {
                    Log.send(2, '会员储值结束返回数据：'+JSON.stringify(res));
                    //console.log(res)
                    if(res.Retcode=='0'){
                        utils.openCash(1);//弹钱箱
                        that.succeedAlert({'info':'会员充值成功,<br>交易流水号:'+res.TraceCode+'',
                            'callBack':'if(utils.getUrl.get("cardMember")){window.location.href="../member/view.jsp?cardMember='+utils.getUrl.get("cardMember")+'"}else{$("#rechargeMoblie,#rechargeMoney").val("");$(".giveMoney").text("");$(".coupon-List .coupon-item").removeClass("active")}'
                        });
                        /*rightBottomPop.alert({
                            content:'会员充值成功,<br>交易流水号:'+res.TraceCode+'',
                        });*/


                        $.ajax({
                            url: memberAddress.vipcandaourl + _config.interfaceUrl.VipQuery,
                            method: 'POST',
                            contentType: "application/json",
                            dataType: 'json',
                            data: JSON.stringify({
                                securityCode: '',
                                branch_id: utils.storage.getter('branch_id'),
                                cardno: cardno
                            }),
                            success: function (data) {
                                //console.log(data)
                                if (data.retcode == 0) {
                                     ya_Member.print({
                                         'pzh':res.TraceCode,//交易流水号
                                         'cardno':data.result[0].MCard,//卡号
                                         'storedBalance':data.result[0].StoreCardBalance,//余额
                                        'storedPoint':data.result[0].IntegralOverall ,//积分余额
                                        'storedAmount':Amount//充值金额
                                    })
                                }
                                if (res.retcode == 1) {
                                    that.errorAlert(data.retInfo);
                                }
                            }
                        });
                    }
                    else {
                        utils.printError.alert(res.RetInfo)
                    }
                },
            });
        }
    },
    /*会员卡修改*/
    modificationCard:function () {
        var that=this,
            oldcardno=loadMember.result[0].MCard,
            entity_cardNo=$.trim($('#bingMemberCard').next('span').text());;
        Log.send(2, '会员卡修改开始参数：'+JSON.stringify({
                url:memberAddress.vipcandaourl + _config.interfaceUrl.VipChangeCardNum,
                cardno: oldcardno,
                branch_id: utils.storage.getter('branch_id'),
                new_cardno: entity_cardNo
            }));
    $.ajax({
        url: memberAddress.vipcandaourl + _config.interfaceUrl.VipChangeCardNum,
        method: 'POST',
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify({
            cardno: oldcardno,
            branch_id: utils.storage.getter('branch_id'),
            new_cardno: entity_cardNo
        }),
        success: function (data) {
            Log.send(2, '会员卡修改返回参数：'+JSON.stringify(data));
            console.log(data)
            if (data.code == '1') {
                var msg={
                    'info':data.msg,
                    'callBack':'$("#inputCard").focus();$(".modal-alert:last,.modal-backdrop:last").remove();'
                }
                that.succeedAlert(msg);
            }
            if (data.code == '0') {
                var msg={
                    'info':'修改会员卡成功！',
                    'callBack':'$("#modify-binding-dialog").modal("hide").html("");member.memberSearch('+entity_cardNo+');$("#Member_cardno").val('+entity_cardNo+')'
                }
                that.succeedAlert(msg);
            }

        }
    });
},
    /*绑定会员实体卡*/
    bindingCard:function () {
        var that=this,
            mobile=loadMember.mobile,
            level=loadMember.result[0].level,
            entity_cardNo=$.trim($('#bingMemberCard').text());
        Log.send(2, '绑定会员实体卡开始：'+JSON.stringify({
                url:  memberAddress.vipcandaourl + _config.interfaceUrl.VipCheckCard,
                entity_cardNo: entity_cardNo,
                mobile:mobile,
                level:level,
                branch_id: utils.storage.getter('branch_id'),
            }));
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.VipInsertCard,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                entity_cardNo: entity_cardNo,
                mobile:mobile,
                level:level,
                branch_id: utils.storage.getter('branch_id'),
            }),
            success: function (data) {
                Log.send(2, '绑定会员实体卡返回参数：'+JSON.stringify(data));
                //console.log(data)
                if (data.code == '1') {
                    var msg={
                       'info':data.msg,
                        'callBack':'$("#inputCard").focus();$(".modal-alert:last,.modal-backdrop:last").remove();'
                    }
                    that.succeedAlert(msg);
                }
                if (data.code == '0') {
                    var msg={
                        'info':'绑定会员卡成功！',
                        'callBack':'$("#modify-binding-dialog").modal("hide").html("");member.memberSearch()'
                    }
                    that.succeedAlert(msg);
                }
            }
        });
    },
    /*是否点击*/
    isClick: function () {
        var member_card = $.trim($('.member_card').text()), member_mobile = $.trim($('.member_mobile').text())
        if (member_card != '' || member_mobile != '') {
            return true
        }
        else {
            return false
        }
    },
    /*是否挂失点击*/
    isLossisClick:function () {
        var member_status= $.trim($('.member_status').attr('status'));
        if(member_status!='1'){
            return false
        }
        else {
            return true
        }
    },
    /*输入的是否为金额*/
    ismoney: function (money) {
        var pattern =/^[1-9]{1}\d*(.\d{1,2})?$|^0.\d{1,2}$/;
        pattern.lastIndex = 0;//正则开始为0
        return pattern.test(money);
    },
    /*验证是否手机号*/
    isPhoneNo: function (phone) {
        var pattern = /^1[34578]\d{9}$/;
        pattern.lastIndex = 0;//正则开始为0
        return pattern.test(phone);
    },
    /*密码只能输入数字*/
    isNumber: function (number) {
        var isNumber = /^\d{6}$/;
        isNumber.lastIndex = 0;//正则开始为0
        return isNumber.test(number);
    },
    /*验证手机号是否重复*/
    isPhoneRepeat: function (phone) {
        var isPhoneRepeat = null
        $.ajax({
            url: memberAddress.vipcandaourl + _config.interfaceUrl.MobileRepeatCheck + '.json',
            async: false,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify({
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile: phone,
            }),
            success: function (res) {
                //console.log(res)
                if (res.Retcode == 0) {
                    isPhoneRepeat = true
                }
                if (res.Retcode == 1) {
                    isPhoneRepeat = false
                }
            }
        })
        return isPhoneRepeat
    },
    /*60s倒计时*/
    countDown: function (msg) {
        var msgId = '.btn-sendMsg', startSec = '发送';
        $(msgId).attr('disabled', true).css({'background': '#f6f6f6', 'color': '#000'});
        var that = this, timeLeft = msg * 1000;//这里设定的时间是10秒;
        countTime();
        function countTime() {
            if (timeLeft == 0) {
                $(msgId).attr('disabled', false).css({'background': '#FF5803', 'color': '#fff'}).text('发送');
            }
            else {
                startSec = parseInt(timeLeft / 1000);
                timeLeft = timeLeft - 1000;
                timer = setTimeout(function () {
                    countTime();
                }, 1000);
                $(msgId).text(startSec);
            }
        }


    },
    errorAlert: function (msg) {
        widget.modal.alert({
            cls: 'fade in',
            content: '<strong>' + msg + '</strong>',
            width: 500,
            height: 500,
            btnOkTxt: '',
            btnCancelTxt: '确定'
        });
    },
    /*成功回调弹窗*/
    succeedAlert:function (msg) {
        widget.modal.alert({
            cls: 'fade in memberSucceed',
            content: '<strong>' + msg.info + '</strong>',
            width: 500,
            height: 500,
            btnOkTxt: '',
            btnCancelTxt: '确定',
            btnCancelCb:function () {
                eval(msg.callBack)
            }
        });
    },
    clearInfo: function () {
        $('.member_card').text('')//卡号
        $('.member_mobile').text('')//手机号
        $('.member_nanme').text('')//姓名
        $('.member_level_name').text('')//会员卡等级名称
        $('.member_birthday').text('')//生日
        $('.member_gender').text('')//性别
        $('.member_StoreCardBalance').text('')//余额
        $('.member_IntegralOverall').text('')//会员积分
        $('.member_status').text('')//会员卡状态
    }
}
var ya_Member = {
    int: function () {
        this.bindEvent();
    },
    /*雅座会员绑定操作按钮*/
    bindEvent: function () {
        var that = this;
        $('.ya-btn-query').click(function () {
            if($(this).text()=='退出'){
                that.ya_loginOut()
            }
            else {
                that.ya_recharge()
            }

        })
    },
    /*雅座会员查询*/
    ya_memberSearch: function (msg) {
        var that = this,
            ya_memberNumber = $.trim($('.ya_cardNumber').val()),
            memberNumber = null;
        if (msg) {
            memberNumber = msg
        } else {
            memberNumber = ya_memberNumber;
        }
        $.ajax({
            url: memberAddress.vipotherurl + _config.interfaceUrl.Yafindmember + '' + memberNumber + '/',
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            success: function (res) {
                //console.log(res)
                if (res.Data == 0) {
                    member.errorAlert('雅座会员查询失败：' + res.Info);
                    $('.ya_cardNumber').val('');
                    $('.ya_balance').val('');
                    $('.ya_point').val('');
                }
                if (res.Data == 1) {
                    var memberCardlength = res.pszTrack2.split(',');
                    if (memberCardlength.length > 1) {
                        utils.chooseMember.choose(
                            {
                                'data': memberCardlength,
                                'callback': 'ya_Member.ya_memberSearch(chooseNo);$(".ya_cardNumber").val(chooseNo)'
                            }
                        )
                        return false
                    }
                    $('.ya_balance').val(res.psStoredCardsBalance / 100);
                    $('.ya_point').val(res.psIntegralOverall / 100);
                    if (res.psTicketInfo == '') {
                        $('.discount_yaList').html('')
                        return false
                    }
                    var couponsList = res.psTicketInfo.split(';'), shtml = '';
                    for (var i = 0; i < couponsList.length; i++) {
                        shtml += '<div class="discount-info">' +
                            '<div class="dish-name">' + couponsList[i].split('|')[3] + '</div>' +
                            '<hr>' +
                            '<div class="dish-price dish-price-border">' + couponsList[i].split('|')[1] / 100 + '元</div>' +
                            '<div class="dish-price">' + couponsList[i].split('|')[4] + '张</div>' +
                            '</div>';
                    }
                    $('#pagDome').pagination({//分页
                        dataSource: couponsList,
                        pageSize: 12,
                        showPageNumbers: false,
                        showNavigator: true,
                        callback: function(couponsList, pagination) {
                            //console.log(data)
                            var shtml = '';
                            for (var i = 0; i < couponsList.length; i++) {
                                shtml += '<div class="discount-info">' +
                                    '<div class="dish-name">' + couponsList[i].split('|')[3] + '</div>' +
                                    '<hr>' +
                                    '<div class="dish-price dish-price-border">' + couponsList[i].split('|')[1] / 100 + '元</div>' +
                                    '<div class="dish-price">' + couponsList[i].split('|')[4] + '张</div>' +
                                    '</div>';
                            }

                            $('.discount_yaList').html(shtml)
                        }
                    });

                }
            }
        })
    },
    /*雅座储值*/
    ya_recharge: function (msg) {
        var that = this,
            cardNumber = $.trim(($('.ya_memberNumber').val())),
            memberNumber = null;
        if (cardNumber == '') {
            member.errorAlert('会员卡号不能为空，请输入正取的会员卡号');
            return false
        }
        ;
        if (msg) {
            memberNumber = msg
        } else {
            memberNumber = cardNumber;
        }
        $.ajax({
            url: memberAddress.vipotherurl + _config.interfaceUrl.Yafindmember + '' + memberNumber + '/',
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            success: function (res) {
                //console.log(res)
                if (res.Data == 0) {
                    member.errorAlert('雅座会员查询失败：' + res.Info)
                }
                if (res.Data == 1) {
                    var memberCardlength = res.pszTrack2.split(',');
                    if (memberCardlength.length > 1) {
                        utils.chooseMember.choose(
                            {
                                'data': memberCardlength,
                                'callback': 'ya_Member.ya_recharge(chooseNo);$(".ya_memberNumber").val(chooseNo)'
                            }
                        )
                        return false
                    }
                    $('.ya-btn-query').text('退出');
                    $('.ya_memberNumber').attr('disabled', true);
                    $('.ya-Stored-value').attr('disabled', false);
                    $('.ya_cardInfo').show();
                    $('.ya_cardInfo .ya_balance').text(res.psStoredCardsBalance / 100);
                    $('.ya_cardInfo .ya_point').text(res.psIntegralOverall / 100);
                }
            }
        })
    },
    /*雅座会员储值保存*/
    ya_rechargeSave: function () {
        var that = this,
            str,
            cardNumber = $.trim(($('.ya_memberNumber').val())),//会员卡号
            pszAmount = $.trim($('.ya-Stored-value').val()),//储值金额
            paytype = $.trim($('.pay-type-select .active').attr('ChargeType'));//'0位现金，1为银行卡'
        if (member.ismoney(pszAmount) === false) {
            member.errorAlert('充值金额为空或格式不正确');
            return false
        }
        str = '<strong>您确定给会员号“' + cardNumber + '”<br>储值“' + pszAmount + '”元吗？</strong>'
        widget.modal.alert({
            cls: 'fade in ya_rechargeSave',
            content: str,
            width: 500,
            height: 500,
            btnOkTxt: '确定',
            btnCancelTxt: '取消',
            btnOkCb:function () {
                $(".modal-alert:last,.modal-backdrop:last").remove();
                saveBtn();
            }
        });
        function saveBtn() {
            Log.send(2, '雅座会员储值开始：'+'url:'+  memberAddress.vipotherurl + _config.interfaceUrl.Yarecharge + '' + utils.storage.getter('aUserid') + '/' + cardNumber + '/' + pszAmount + '/' + that.ya_formatDate(new Date(), "yyyyMMddHHmmssffff") + '/0/' + paytype + '/');
            $.ajax({
                url: memberAddress.vipotherurl + _config.interfaceUrl.Yarecharge + '' + utils.storage.getter('aUserid') + '/' + cardNumber + '/' + pszAmount + '/' + that.ya_formatDate(new Date(), "yyyyMMddHHmmssffff") + '/0/' + paytype + '/',
                method: 'get',
                contentType: "application/json",
                dataType: 'json',
                success: function (res) {
                    Log.send(2,'雅座会员储值结束：'+JSON.stringify(res))
                    //console.log(res)
                    if (res.Data == 0) {
                        member.errorAlert(res.Info)
                    }
                    if (res.Data == 1) {
                        //member.errorAlert('充值成功');
                        utils.openCash(1);//弹钱箱
                        rightBottomPop.alert({
                            content:'会员充值成功,<br>交易流水号:'+res.pszTrace+'',
                        });
                        $.ajax({
                            url: memberAddress.vipotherurl + _config.interfaceUrl.Yafindmember + '' + res.pszPan + '/',
                            method: 'POST',
                            contentType: "application/json",
                            dataType: 'json',
                            success: function (data) {
                                if (data.Data == 0) {
                                    member.errorAlert('雅座会员查询失败：' + res.Info)
                                }
                                if (data.Data == 1) {
                                    that.print({
                                        'pzh':res.pszTrace,
                                        'cardno':res.pszPan,
                                        'storedBalance':res.psStoreCardBalance/100,
                                        'storedPoint':data.psIntegralOverall / 100,
                                        'storedAmount':pszAmount
                                    })
                                }
                            }
                        });
                        that.ya_recharge(cardNumber);
                    }
                }
            })
        };

    },
    /*雅座会员退出登录*/
    ya_loginOut:function () {
        $('.ya-btn-query').text('查询');
        $('.ya_memberNumber').attr('disabled', false).val('');
        $('.ya-Stored-value').attr('disabled', true).val('');
        $('.ya_cardInfo').hide();
        $('.ya_cardInfo .ya_balance').text('');
        $('.ya_cardInfo .ya_point').text('');
    },
    /*雅座会员注册激活*/
    ya_Register:function () {
        var that=this,
            ya_pad=$.trim($('.ya_pad').val()),
            ya_Register=$.trim($('.ya_Register').val()),
            ya_phone=$.trim($('.ya_phone').val());
        if(member.isNumber(ya_pad)===false){
            member.errorAlert('只能输入六位数字的密码');
            return false
        }
        if(member.isPhoneNo(ya_phone)===false){
            member.errorAlert('请输入正确的手机号码');
            return false
        }
        str = '<strong>您确定激活卡号“' + ya_Register + '”给手机号“' + ya_phone + '”吗？</strong>'
        widget.modal.alert({
            cls: 'fade in ya_rechargeSave',
            content: str,
            width: 500,
            height: 500,
            btnOkTxt: '确定',
            btnCancelTxt: '取消',
            btnOkCb:function () {
                $(".modal-alert:last,.modal-backdrop:last").remove();
                regMsg();
            }
        });
        function regMsg() {
            $.ajax({
                url: memberAddress.vipotherurl + _config.interfaceUrl.YaCardActive + '' + ya_Register + '/' + ya_pad + '/' + ya_phone + '/',
                method: 'GET',
                contentType: "application/json",
                dataType: 'json',
                success: function (res) {
                    if (res.Data == 0) {
                        member.errorAlert('雅座会员激活失败：' + res.Info)
                    }
                    if (res.Data == 1) {
                        member.errorAlert('雅座会员激活成功')
                    }
                }
            })
        }

    },
    /*充值成功后打印详情*/
    print:function (msg) {
        $.ajax({
            url:_config.interfaceUrl.PrintMemberStore,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data:JSON.stringify({
                'cardno':msg.cardno,
                'memberTitle':utils.storage.getter('branch_branchname'),
                'pzh':msg.pzh,//交易号
                'date':ya_Member.ya_formatDate(new Date(), "yyyy-MM-dd"),
                'time':ya_Member.ya_formatDate(new Date(), "HH:mm:ss"),
                'storeName':utils.storage.getter('branch_branchname'),
                'storedBalance':msg.storedAmount,//充值金额
                'storedAmount':msg.storedBalance,//充值后余额
                'storedPoint':msg.storedPoint,//充值后积分余额
                'deviceid':utils.storage.getter('posid')
            }),
            success: function (res) {
                //console.log(res)
                if(res.result=='0'){
                    rightBottomPop.alert({
                        content:"会员充值详情打印完成",
                    })
                }
                else {
                    utils.printError.alert(res.msg)
                }
            }
        });
    },
    /*时间格式化为201610181432350103*/
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
    }
}