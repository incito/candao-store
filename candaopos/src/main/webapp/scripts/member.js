$(function () {
    SetBotoomIfon.init()//设置底部信息
    member.int();
    ya_Member.int();
})
var loadMember=null,memberAddress=JSON.parse(utils.storage.getter('memberAddress'));
var member={
    int:function () {
        this.bindEvent();
    },
    /*餐绑定操作按钮*/
    bindEvent:function () {
        var that=this;
        var $dishType = $('.coupon-box');
        var flag_prev = 0;
        $('.virtual-keyboard-ok,.btn-search').click(function () {
            that.memberSearch();
        });
        $('.member-op-list li').on('click', function(){
            var me = $(this);
            if(me.hasClass('J-modify-base')) {
                if (that.isClick()){
                    $("#modify-base-dialog").load("../member/modifyBase.jsp");
                    $("#modify-base-dialog").modal("show");
                }
            }

            if(me.hasClass('J-modify-phone')) {
                if (that.isClick()){
                    $("#modify-phone-dialog").load("../member/modifyPhone.jsp");
                    $("#modify-phone-dialog").modal("show");
                }
            }

            if(me.hasClass('J-modify-pwd')) {
                if(that.isClick()){
                    $("#modify-pwd-dialog").load("../member/modifyPwd.jsp");
                    $("#modify-pwd-dialog").modal("show");
                }
            }

            if(me.hasClass('J-cancellation')) {
                if(that.isClick()){
                    var status=$.trim($('.member_status').attr('status'));
                    if(status==1){
                       var member_card=$.trim($('.member_card').text());
                        widget.modal.alert({
                            cls: 'fade in',
                            content:'<strong>确定要注销'+member_card+'吗？</strong>',
                            width:500,
                            height:500,
                            btnOkTxt: '确定',
                            btnCancelTxt: '取消',
                            btnOkCb:function () {
                                that.memberDelete(member_card);
                            }
                        });

                    }
                }
            }

            if(me.hasClass('J-modify-lost')){
                if(that.isClick()){
                    var card_type=$.trim($('.member_card').attr('card_type'));
                    if(card_type!='1'){
                        that.errorAlert('该会员还没有绑定实体卡，不能挂失');
                        return false
                    }
                    else {
                        var member_card=$.trim($('.member_card').text());
                        widget.modal.alert({
                            cls: 'fade in',
                            content:'<strong>确定要挂失 '+member_card+'吗？</strong>',
                            width:500,
                            height:500,
                            btnOkTxt: '确定',
                            btnCancelTxt: '取消',
                            btnOkCb:function () {
                                that.memberlost(member_card);
                            }
                        });
                    }
                }
            }

            if(me.hasClass('J-modify-cancellost')){
                if(that.isClick()){
                    var card_type=$.trim($('.member_card').attr('card_type'));
                    if(card_type!='1'){
                        that.errorAlert('该会员还没有绑定实体卡，不能解除挂失');
                        return false
                    }
                    else {
                        var member_card=$.trim($('.member_card').text());
                        widget.modal.alert({
                            cls: 'fade in',
                            content:'<strong>确定要解除挂失 '+member_card+'吗？</strong>',
                            width:500,
                            height:500,
                            btnOkTxt: '确定',
                            btnCancelTxt: '取消',
                            btnOkCb:function () {
                                that.memberCancel(member_card);
                            }
                        });
                    }
                }
            }
        });
        //充值优惠向左向右按钮
        $(".coupon-box .next").click(function(){
            var count = $dishType.find( ".coupon-List .coupon-item").length;
            if (flag_prev < count - 8) {
                $dishType .find(".coupon-item").eq(flag_prev).css({'margin-left': '-142px'}).hide();
                flag_prev++;
                if(flag_prev==count - 8){
                    $(".coupon-box .next").addClass('unclick');
                    $(".coupon-box .prev").removeClass('unclick');

                }

            }
        });
        $(".coupon-box .prev").click(function(){
            if(flag_prev>=1){
                $dishType.find(".coupon-item").eq(flag_prev-1).css({'margin-left': '2px'}).show();
                flag_prev--;
                if(flag_prev==0){
                    $(".coupon-box .prev").addClass('unclick');
                    $(".coupon-box .next").removeClass('unclick');
                }

            }

        });
        /*选择优惠*/{}
        $('.coupon-box').on('click','.coupon-item', function(){
            if($(this).hasClass('active')){
                $(this).removeClass('active')
            }
            else {
                $(this).addClass('active').siblings('.coupon-item').removeClass('active');
            }

        })
    },
    /*会员查询*/
    memberSearch:function (msg) {
        var memberCardno='',that=this
        that.clearInfo();
        if (msg){
            memberCardno=msg;
        }
        else {
            memberCardno=$.trim($('#Member_cardno').val())
        }
        if(memberCardno==''){
            that.errorAlert('查询信息不能为空');
            return
        }
        $.ajax({
         url:memberAddress.vipcandaourl+ _config.interfaceUrl.VipQuery,
         method: 'POST',
         contentType: "application/json",
         dataType:'json',
         data:JSON.stringify({
         securityCode: '',
         branch_id: utils.storage.getter('branch_id'),
         cardno:memberCardno
         }),
         success: function(res){
             var gender,status;
             //console.log(res)
             if(res.retcode==0){
                 if(res.result.length>1){
                     var memberCard=[];
                     for(var i=0;i<res.result.length;i++){
                         memberCard.push(res.result[i].MCard)
                     }
                     utils.chooseMember.choose({
                         'data':memberCard,'callback':'member.memberSearch(chooseNo)'
                     })
                     return false
                 }
                 loadMember=res;
                 $('.member_card').text(res.result[0].MCard).attr('card_type',res.result[0].card_type)//卡号
                 $('.member_mobile').text(res.mobile)//手机号
                 $('.member_nanme').text(res.name)//姓名
                 $('.member_level_name').text(res.result[0].level_name)//会员卡等级名称
                 $('.member_birthday').text((res.birthday).substring(0,res.birthday.length-8))//生日
                 switch (res.gender){
                     case 0 :gender='男';break;
                     case 1 :gender='女'; break;
                     default : gender='未知'; break;
                 }
                 $('.member_gender').text(gender)//性别
                 $('.member_StoreCardBalance').text(res.result[0].StoreCardBalance)//余额
                 $('.member_IntegralOverall').text(res.result[0].IntegralOverall)//会员积分
                 switch (res.result[0].status){
                     case 0 :status='注销';break;
                     case 1 :status='正常'; break;
                     case 2 :status='挂失'; break;
                     default : status='未知'; break;
                 }
                 $('.member_status').text(status).attr('status',res.result[0].status)//会员卡状态
                 if(res.result[0].status==2 && res.result[0].card_type=='1'){
                     $('.J-modify-cancellost').show();
                     $('.J-modify-lost').hide();
                 }
                 if(res.result[0].status==1){
                     $('.J-modify-lost').show();
                     $('.J-modify-cancellost').hide();
                 }
             }
             if (res.retcode==1){
                 that.errorAlert('会员查询错：'+res.retInfo)
             }
         }
         })

    },
    /*充值优惠列表*/
    getCouponList:function () {
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.GetCouponList+'.json',
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                branch_id: utils.storage.getter('branch_id'),
                current:'',
                pageSize:''
            }),
            success: function(res){
                var str='';
                for( var i=0;i<res.datas.length;i++){
                    str+='<div class="coupon-item" presentValue="'+res.datas[i].presentValue+'" dealValue="'+res.datas[i].dealValue+'">'+res.datas[i].name+'</div>'
                }
                if(res.datas.length>8){
                    $(".coupon-box .next").removeClass('unclick')
                }
                $('.coupon-List').html(str)
            }
        })
    },

    /*发送验证嘛*/
    sendVerifyCode:function (msg) {
        var that=this
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.SendVerifyCode,
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                mobile:msg
            }),
            success: function(res){
                console.log(res)
                if(res.Retcode==0){
                    valicode=res.valicode
                }
                else {
                    that.errorAlert(res.RetInfo)
                }
            }
        })
    },
    /*修改手机号码*/
    changephone:function () {
        var that=this,
            newphone=$.trim($('#newphone').val()),
            oldphone=$.trim($('#oldphone').val()),
            phoneCode=$.trim($('#phoneCode').val());
        if(that.isPhoneNo(newphone)===false){
            that.errorAlert('请输入正确的手机号码');
            return false
        }
        if(phoneCode!=valicode ||phoneCode==''){
            that.errorAlert('请输入正确的验证嘛');
            return false
        }
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.VipChangeInfo,
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile:oldphone,
                new_mobile:newphone
            }),
            success: function(res){
                if(res.retcode==0){
                    $('#modify-phone-dialog').modal('hide').html('');
                    that.errorAlert('修改手机号码成功')
                }
                if(res.retcode==1){
                    that.errorAlert(res.retInfo)
                }
                console.log(res)
            }
        })

    },
    /*修改会员基本信息*/
    changebaseInfo:function () {
        var birthday=$.trim($('.base_memberbirthday').val()),
            name=$.trim( $('.base_membername_input').val()),
            mobile=$.trim($('.base_memberphone').text()),
            gender=$('.radio-box input:checked').val(),
            that=this;
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.VipChangeInfo,
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                branch_id: utils.storage.getter('branch_id'),
                mobile:mobile,
                name:name,
                gender:gender,
                birthday:birthday
            }),
            success: function(res){
                debugger
                console.log(res)
                if(res.retcode==0){
                    $('#modify-base-dialog').modal('hide').html('');
                    that.errorAlert('修改会员基本信息成功')
                }
                if(res.retcode==1){
                    that.errorAlert(res.retInfo)
                }

            }
        })
    },
    /*设置会员基本信息*/
    setBaseinfo:function () {
        $('.base_membercard').text(loadMember.result[0].MCard)//卡号
        $('.base_memberphone').text(loadMember.mobile)//手机
        $('.base_membernanme').text(loadMember.name)//姓名
        $('.base_membername_input').val(loadMember.name)//姓名
        $('.base_memberbirthday').val((loadMember.birthday).substring(0,loadMember.birthday.length-8))//卡号
        $('.radio-box input').each(function () {
            if(loadMember.gender==$(this).val()){
                $(this).attr('checked',true)
            }
        })
    },
    /*修改会员消费密码*/
    changePassword:function () {
        var that=this,
            mobile=$.trim($('#changPsd').val()),
            password=$.trim($('#newPsd').val()),
            Rpassword=$.trim($('#rnewPsd').val()),
            phoneCode=$.trim($('#phoneCode').val());
        if(phoneCode!=valicode ||phoneCode==''){
            that.errorAlert('请输入正确的验证嘛');
            return false
        }
        if(that.isNumber(password)===false || that.isNumber(Rpassword)===false){
            that.errorAlert('只能输入六位数字的密码');
            return false
        }
        if(password!=Rpassword){
            that.errorAlert('两次输入的密码不一致，请重新输入');
            return false
        }
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.VipChangeInfo,
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile:mobile,
                password:Rpassword
            }),
            success: function(res){
                if(res.retcode==0){
                    $('#modify-pwd-dialog').modal('hide').html('');
                    that.errorAlert('修改消费密码成功')
                }
                if(res.retcode==1){
                    that.errorAlert(res.retInfo)
                }
                console.log(res)
            }
        })
    },
    /*挂失会员卡*/
    memberlost:function (member_card) {
        var that=this;
        $(".modal-alert:last,.modal-backdrop:last").remove();
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.ReportLossCanDao,
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                branch_id:utils.storage.getter('branch_id'),
                cardno:member_card,
                FMemo:'',
            }),
            success: function(res){
                //console.log(res)
                if(res.Retcode==0){
                    that.errorAlert('挂失会员卡'+member_card+'成功')
                }
                else {
                    that.errorAlert('挂失会员卡'+member_card+'失败')
                }
            }
        })
    },
    /*解除会员挂失*/
    memberCancel:function (member_card) {
        var that=this;
        $(".modal-alert:last,.modal-backdrop:last").remove();
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.CancelLossCanDao,
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                branch_id:utils.storage.getter('branch_id'),
                cardno:member_card,
                FMemo:'',
            }),
            success: function(res){
                console.log(res)
                if(res.Retcode==0){
                    that.errorAlert('挂失会员卡'+member_card+'成功')
                }
                else {
                    that.errorAlert('挂失会员卡'+member_card+'失败')
                }
            }
        })
    },
    /*会员注销*/
    memberDelete:function (member_card) {
        var that=this;
        $(".modal-alert:last,.modal-backdrop:last").remove();
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.CancelCanDao,
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                branch_id:utils.storage.getter('branch_id'),
                cardno:member_card
            }),
            success: function(res){
                if(res.Retcode==0){
                    that.errorAlert('注销'+member_card+'成功')
                }
                else {
                    that.errorAlert(res.RetInfo)
                }
            }
        })

    },
    /*会员注册*/
    register:function () {
        var that=this,
            moblie=$.trim($('#phone').val()),
            phoneCode=$.trim($('#phoneCode').val()),
            gender=$.trim($('#gender input:checked').val()),
            birthday=$.trim($('#birthday').val()),
            psd=$.trim($('#psd').val()),
            rpsd=$.trim($('#rpsd').val()),
            name=$.trim($('#nmae').val());

        if(that.isPhoneNo(moblie)===false){
            that.errorAlert('请输入正确的手机号码');
            return false
        };
        if(that.isPhoneRepeat(moblie)===false){
            that.errorAlert('请输入正确的手机号码');
            return false
        }
        if(phoneCode==''||phoneCode!=valicode){
            that.errorAlert('请输入正确的验证嘛');
            return false
        }
        if(name==''){
            that.errorAlert('请输入姓名');
            return false
        }
        if(birthday==''){
            that.errorAlert('请选择生日');
            return false
        }
        if(that.isNumber(psd)===false ||that.isNumber(rpsd)===false){
            that.errorAlert('只能输入六位数字的密码');
            return false
        }
        if(psd!=rpsd){
            that.errorAlert('两次输入的密码不一致，请重新输入');
            return false
        };
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.RegistCanDao,
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile:moblie,
                cardno:'',
                password:rpsd,
                name:name,
                gender:gender,
                birthday:birthday,
                channel:'1',
                branch_addr:utils.storage.getter('branch_branchaddress')
            }),
            success: function(res){
                if(res.Retcode==0){
                    that.errorAlert('注册会员成功');
                    $('#phone').val('');
                    $('#phoneCode').val('');
                    $('#birthday').val('');
                    $('#psd').val('');
                    $('#rpsd').val('')
                    $('#nmae').val('');
                }
                if(res.Retcode==1){
                    that.errorAlert(res.retInfo)
                }
                //console.log(res)
            }
        })
    },
    /*会员储值*/
    stored_value:function (msg) {
        var that=this,
            cardno=null,
            ChargeType=$.trim($('.pay-type-select .active').attr('ChargeType')),
            Amount=$.trim($('#rechargeMoney').val());
        if(msg){
            cardno=msg
        }
        else {
            cardno=$.trim($('#rechargeMoblie').val());
            if(cardno==''){
                that.errorAlert('手机号码或卡号不能为空，请检查');
                return false
            }
        }
        if(that.ismoney(Amount)===false){
            that.errorAlert('充值金额为空或格式不正确');
            return false
        }
        /*会员卡是否存在*/
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.QueryCanDao,
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                cardno:cardno,
                password:'',
            }),
            success: function(res){
                if(res.Retcode==0){
                    if(res.CardList.length>1){
                        var memberCard=[];
                        for(var i=0;i<res.CardList.length;i++){
                            memberCard.push(res.CardList[i].cardno)
                        }
                        utils.chooseMember.choose({
                            'data':memberCard,'callback':'member.stored_value(chooseNo)'
                        })
                        return false
                    };
                }
                if(res.Retcode==1){
                    that.errorAlert(res.RetInfo);
                }
            }
        })
    },
    /*是否点击*/
    isClick:function () {
        var member_card=$.trim($('.member_card').text()),member_mobile=$.trim($('.member_mobile').text())
        if(member_card!=''||member_mobile!=''){
            return true
        }
        else {
            return false
        }
    },
    /*输入的是否为金额*/
    ismoney:function (money) {
        var pattern= /^[0-9]+(\.[0-9]{2})?$/;
        pattern.lastIndex=0;//正则开始为0
        return pattern.test(money);
    },
    /*验证是否手机号*/
    isPhoneNo:function (phone) {
        var pattern = /^1[34578]\d{9}$/;
        pattern.lastIndex=0;//正则开始为0
        return pattern.test(phone);
    },
    /*密码只能输入数字*/
    isNumber:function (number) {
        var isNumber=/^\d{6}$/;
        isNumber.lastIndex=0;//正则开始为0
        return isNumber.test(number);
    },
    /*验证手机号是否重复*/
    isPhoneRepeat:function (phone) {
        var isPhoneRepeat=null
        $.ajax({
            url:memberAddress.vipcandaourl+ _config.interfaceUrl.MobileRepeatCheck+'.json',
            async: false,
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            data:JSON.stringify({
                securityCode: '',
                branch_id: utils.storage.getter('branch_id'),
                mobile:phone,
            }),
            success: function(res){
                console.log(res)
                if(res.Retcode==0){
                    isPhoneRepeat=true
                }
                if(res.Retcode==1){
                    isPhoneRepeat=false
                }
            }
        })
        return isPhoneRepeat
    },
    /*60s倒计时*/
    countDown:function (msg) {

        var msgId='.btn-sendMsg',startSec='发送';
        $(msgId).attr('disabled', true).css({'background':'#f6f6f6','color':'#000'});
        var that=this, timeLeft = msg*1000;//这里设定的时间是10秒;
        countTime();
        function countTime(){
            if(timeLeft ==0){
                $(msgId).attr('disabled', false).css({'background':'#FF5803','color':'#fff'}).text('发送');
            }
            else {
                startSec  = parseInt(timeLeft/1000);
                timeLeft=timeLeft-1000;
                timer=setTimeout(function () {
                    countTime();
                },1000);
                $(msgId).text(startSec);
            }
        }


    },
    errorAlert:function (msg) {
        widget.modal.alert({
            cls: 'fade in',
            content:'<strong>'+msg+'</strong>',
            width:500,
            height:500,
            btnOkTxt: '',
            btnCancelTxt: '确定'
        });
    },
    clearInfo:function () {
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
var ya_Member={
    int:function () {
        this.bindEvent();
    },
    /*雅座会员绑定操作按钮*/
    bindEvent:function () {
        var that=this;
        $('.ya_btn .btnOk').click(function () {
            that.ya_memberSearch()
        })
    },
    /*雅座会员查询*/
    ya_memberSearch:function (msg) {
        var that=this,
            ya_memberNumber=$.trim($('.ya_cardNumber').val()),
            memberNumber=null;
        if(msg){
            memberNumber=msg
        }else {
            memberNumber=ya_memberNumber;
        }
        $.ajax({
            url:memberAddress.vipotherurl+ _config.interfaceUrl.Yafindmember+''+memberNumber+'/',
            method: 'POST',
            contentType: "application/json",
            dataType:'json',
            success: function(res){
                if(res.Data==0){
                    member.errorAlert('雅座会员查询失败：'+res.Info)
                }
                if(res.Data==1){
                    var memberCardlength=res.pszTrack2.split(',');
                    if(memberCardlength.length>1){
                        utils.chooseMember.choose(
                            { 'data':memberCardlength,'callback':'ya_Member.ya_memberSearch(chooseNo);$(".ya_cardNumber").val(chooseNo)'}
                            )
                        return false
                    }
                    $('.ya_balance').val(res.psStoredCardsBalance/100);
                    $('.ya_point').val(res.psIntegralOverall/100)
                }
                console.log(res)
            }
        })
    },
}