var activeinputele;

var OpenPage = {
	init: function(){
		this.bindEvent();
	},

	bindEvent: function(){

		$('.J-submit').click(function(){
			window.location = "../views/login.jsp";
		});

		$("#confirm-opening-btn").click(function(){
			$("#mg-login-dialog").modal("show");
		});

		$(".virtual-keyboard ul li").click(function(e){
			var keytext = $(this).text();
			if(activeinputele != null && activeinputele != undefined){
				if(keytext == "←"){
					activeinputele.focus();
					backspace();
				}else{
					var val = activeinputele.val();
					val = val + keytext;
					activeinputele.val(val);
					activeinputele.focus();
				}
			}
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


