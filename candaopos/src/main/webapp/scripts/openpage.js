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


