var OpenPage = {
	init: function(){
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
    }
};

function toLogin(){
    OpenPage.open()
}


$(function(){
	OpenPage.init();
});


