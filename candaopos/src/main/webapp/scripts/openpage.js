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
	}
};

function toLogin(){
	$("#mg-login-dialog").modal("hide");
}


$(function(){
	OpenPage.init();
});


