var OpenPage = {
	init: function(){
        this.isYesterdayEndWork();

        this.saveConfigInfo();

		this.bindEvent();
		var ipaddress= utils.getUrl.get("ipaddress")//设置ipaddress参数到缓存
		var posid=utils.getUrl.get("posid")//设置posid参数到缓存
		if(ipaddress!=null ||posid!=null){
			utils.storage.setter("ipaddress",ipaddress);
			utils.storage.setter("posid",posid)
		}
	},

	bindEvent: function(){

		$('.J-submit').click(function(){
			window.location = "../views/login.jsp";
		});

		$("#confirm-opening-btn").click(function(){
			$("#mg-login-dialog").modal("show");
			widget.keyboard();
		});

		$("#mg-login-dialog input").focus(function(event){
			activeinputele = $(this);
		});
	},
    open:function () {
        $.ajax({
            url: _config.interfaceUrl.RestaurantOpened + ''+$.trim($('#manager_num').val())+'/'+$.trim($('#perm_pwd').val())+'/'+utils.storage.getter('ipaddress')+'/1/',
            method: 'GET',
            dataType:'text',
            success: function(res){
                var res = JSON.parse(res.substring(12,res.length-3));
                if(res.Data === '1') {//开业
                    $("#mg-login-dialog").modal("hide");
                    window.location = "../views/login.jsp";
                }
                else {
                    widget.modal.alert({
                        cls: 'fade in',
                        content:'<strong>'+res.Info+'</strong>',
                        width:500,
                        height:500,
                        btnOkTxt: '确定',
                        btnCancelTxt: ''
                    });
                }
            },
            error: function(){
                widget.modal.alert({
                    cls: 'fade in',
                    content:'<strong>获取当日结业信息失败</strong>',
                    width:500,
                    height:500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
            }
        })
    },
    isOpen:function () {
        $.ajax({
            url: _config.interfaceUrl.RestaurantOpened + '//'+utils.storage.getter('ipaddress')+'/0/',
            method: 'GET',
            dataType:'text',
            success: function(res){
                var res = JSON.parse(res.substring(12,res.length-3));
                if(res.Data === '1') {//开业
                    window.location = "../views/login.jsp";
                }
            },
            error: function(){
                widget.modal.alert({
                    cls: 'fade in',
                    content:'<strong>获取当日结业信息失败</strong>',
                    width:500,
                    height:500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
            }
        })
    },
    isYesterdayEndWork:function () {//昨天是否结业
        var that=this
        $.ajax({
            url: _config.interfaceUrl.CheckTheLastEndWork,
            method: 'GET',
            dataType:'json',
            success: function(res){
                if(res['result'] === '0') {//昨天已经结业返回成功
                    if(res['detail']){//昨天已经结业
                        $("#openTo").show();
                        utils.storage.setter('isYesterdayEndWork','0');//设置昨天是否结业状态0为已结业，1为未结业；
                        that.isOpen();
                    }
                    else {//昨天没有结业
                        utils.storage.setter('isYesterdayEndWork','1');//设置昨天是否结业状态0为已结业，1为未结业；
                        $("#openTo").hide();
                        $.ajax({//查询是否有为结业的餐台
                            url: _config.interfaceUrl.GetAllTableInfos,
                            method: 'GET',
                            dataType: 'json',
                            success: function (res) {
                                var noCheack=[];//没有结账的餐台数组
                                for(var i=0;i<res.data.length;i++){
                                    if(res.data[i].status==='1'){
                                        noCheack.push(res.data[i]);
                                        break
                                    }
                                };
                                if(noCheack.length>0){
                                    widget.modal.alert({
                                        cls: 'fade in',
                                        content:'<div style="text-align: left;font-size: 20px;font-weight:bold ">昨日还有未结账的餐台，请先登录收银员账号结账，然后进行清机和结业。</div>',
                                        title:'',
                                        width:500,
                                        height:500,
                                        btnOkTxt: '确定',
                                        btnCancelTxt: '',
                                        btnOkCb:function () {
                                            window.location = "../views/login.jsp"
                                        }
                                    });
                                }
                                else {
                                    widget.modal.alert({
                                        cls: 'fade in',
                                        content:'<div style="text-align: left;font-size: 20px;font-weight:bold ">昨日未清机且未结业，请先清机后结业。</div>',
                                        title:'',
                                        width:500,
                                        height:500,
                                        btnOkTxt: '确定',
                                        btnCancelTxt: '',
                                        btnOkCb:function () {
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
            /*error: function(){
                widget.modal.alert({
                    cls: 'fade in',
                    content:'<strong>获取当日结业信息失败</strong>',
                    width:500,
                    height:500,
                    btnOkTxt: '',
                    btnCancelTxt: '确定'
                });
            }*/
        })
    },
    /*结业清机*/
    checkout:function () {
        var that=this;
        var Uncleandata=that.getFindUncleanPosList();
        var arrylength=Uncleandata.LocalArry.length-1;
        var LocalArry=Uncleandata.LocalArry;
        if(Uncleandata.LocalArry.length>0){
            $("#J-btn-checkout-dialog").load("../views/check/impower.jsp",{'title':'清机授权','userNmae':Uncleandata.LocalArry[arrylength].username,'usernameDisble':'2','cbd':'OpenPage.clearAllcheckOut()','userRightNo':'030204'});
            $("#J-btn-checkout-dialog").modal('show')
        }
        if(Uncleandata.LocalArry.length==0&&Uncleandata.OtherArry.length>0){
            widget.modal.alert({
                cls: 'fade in',
                content:'<strong>还有其他POS机未清机,<br><br>请到其他POS机上先清机</strong>',
                width:500,
                height:500,
                btnOkTxt: '重试',
                btnCancelTxt: '',
                btnOkCb:function () {
                    $(".modal-alert:last,.modal-backdrop:last").remove();
                    that.checkout()
                }
            });
        }
        if(Uncleandata.findUncleanPosList.detail.length=='0'){
            $("#J-btn-checkout-dialog").load("../views/check/impower.jsp",{'title':'结业授权','cbd':'OpenPage.checkoutCallback()','userRightNo':'030205'});
            $("#J-btn-checkout-dialog").modal('show')
        }


    },
    clearAllcheckOut:function () {
        var that=this
        $("#J-btn-checkout-dialog").modal('hide')
        var that=this;
        widget.modal.alert({
            cls: 'fade in',
            content:'<strong>清机中，请稍后</strong>',
            width:500,
            height:500,
            hasBtns:false,
        });
        $.ajax({
            url: _config.interfaceUrl.Clearner+''+$.trim($('#user').val())+'/'+utils.storage.getter('checkout_fullname')+'/'+utils.storage.getter('ipaddress')+'/'+utils.storage.getter('posid')+'/'+utils.storage.getter('checkout_fullname')+'/',
            type: "get",
            dataType: "json",
            success: function (data) {
                data=JSON.parse(data.result[0])
                if(data.Data === '0') {//清机失败
                    $(".modal-alert:last,.modal-backdrop:last").remove();
                    widget.modal.alert({
                        cls: 'fade in',
                        content:'<strong>' + data.Info + '</strong>',
                        width:500,
                        height:500,
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
    /*结业回调*/
    checkoutCallback:function () {//结业回调
        $.ajax({
            url: _config.interfaceUrl.EndWork,//不需要传递参数
            type: "get",
            dataType:'text',
            success: function (data) {
                $("#J-btn-checkout-dialog").modal('hide')
                var  data=JSON.parse(data.substring(12, data.length - 3));//从第12个字符开始截取，到最后3位，并且转换为JSON
                if(data.Data=='1'){
                    /*$.ajax({
                     url: _config.interfaceUrl.EndWorkSyncData,//结业数据上传
                     type: "get",
                     dataType:'text',
                     data:{
                     'synkey':'candaosynkey'
                     },
                     success: function (data) {
                     console.log(data)
                     },
                     error:function (data) {
                     console.log(data)
                     alert("1111")
                     }
                     });*/
                    widget.modal.alert({
                        cls: 'fade in',
                        content:'<strong>'+data.Info+',即将退出程序</strong>',
                        width:500,
                        height:500,
                        btnOkTxt: '确定',
                        btnCancelTxt: '',
                        btnOkCb:function () {
                            $(".modal-alert:last,.modal-backdrop:last").remove();
                            window.location = '../views/openpage.jsp?ipaddress='+utils.storage.getter('ipaddress')+'&posid='+utils.storage.getter('posid');
                            utils.clearLocalStorage.clearSelect();
                        }
                    });
                }
                else {
                    widget.modal.alert({
                        cls: 'fade in',
                        content:'<strong>'+data.Info+'</strong>',
                        width:500,
                        height:500,
                        btnOkTxt: '确定',
                        btnCancelTxt: ''
                    });
                }
            }
        });
    },
    getFindUncleanPosList:function () {//获取未清机数据列表
        var findUncleanPosList ,LocalArry=[],OtherArry=[]
        $.ajax({
            url: _config.interfaceUrl.GetAllUnclearnPosInfoes,
            type: "get",
            async:false,
            dataType: "text",
            success: function (data) {
                findUncleanPosList=JSON.parse(data)
                /*console.log(findUncleanPosList.detail)
                 console.log(findUncleanPosList.result)*/
                if(findUncleanPosList.result==='0'){
                    LocalArry=[];//本机数组集合
                    OtherArry=[];//其他pos登录集合
                    for(var i=0;i<findUncleanPosList.detail.length;i++){
                        if(findUncleanPosList.detail[i].ipaddress==utils.storage.getter('ipaddress')){
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
            findUncleanPosList:findUncleanPosList,
            LocalArry:LocalArry,
            OtherArry:OtherArry,
        }
    },

    /*保存配置信息*/
    saveConfigInfo:function () {
        //获取会员配置地址
        $.ajax({
            url: _config.interfaceUrl.GetMemberAddress,
            type:"get",
            dataType:'text',
            success: function(res1){
                var res1=JSON.parse(res1);
                utils.storage.setter('vipType',res1.data.viptype)//会员地址状态
                if(res1.data.viptype==='1'){//viptype 1为餐道会员 2为雅坐会员
                    utils.storage.setter('memberAddress',res1.data.vipcandaourl)
                }
                if(res1.data.viptype==='2'){
                    utils.storage.setter('memberAddress',res1.data.vipotherurl)
                }
            }
        });

        //银行信息
        $.get(_config.interfaceUrl.GetAllBankInfo).then(function(res){
            utils.storage.setter('banklist', JSON.stringify(res));
        });

        //门店信息
        $.get(_config.interfaceUrl.GetBranchInfo).then(function(res){
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
        }).then(function(res){
            utils.storage.setter('ROUNDING', JSON.stringify(res.rows));
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
        }).then(function(res){
            utils.storage.setter('JI_KOU_SPECIAL', JSON.stringify(res.rows));
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
        }).then(function(res){
            utils.storage.setter('DISHES', JSON.stringify(res.rows));
        });

        //挂账单位
        $.ajax({
            url: _config.interfaceUrl.GetAllOnCpyAccountInfo,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
        }).then(function(res){
            utils.storage.setter('payCompany', JSON.stringify(res));
        });
    }
};

function toLogin(){
    OpenPage.open()
}


$(function(){
	OpenPage.init();
});


