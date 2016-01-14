$(document).ready(function(){
	$('.ky-navbar-menu > .ky-nav > li > a.ky-menu-primary').css({'background-color':'#7AC454','color':'#ffffff'});

	$('.ky-nav li a').click(function(){
		var _this = $(this);
		var bgcolor = _this.css('border-left-color');
		$('.ky-nav li a').css({'background-color':'#E8E7E4','color':'#282828'})
		_this.css({'background-color':bgcolor,'color':'#ffffff'});
		$('.ky-title').css('background-color',bgcolor);
		if(_this.parent().hasClass('ky-dropdown')){
			_this.next('ul').find('a').css({'background-color':bgcolor,'color':'#ffffff'});
		}
		if(_this.parent().parent().hasClass('ky-dropdown-menu')){
			_this.parent().parent().prev('a').css({'background-color':bgcolor,'color':'#ffffff'});
			_this.parent().parent('ul.ky-dropdown-menu').css('display','none');
		}
	});

	$('.ky-dropdown').mouseover(function(){
		$('.ky-dropdown > ul.ky-dropdown-menu').css('display','block');
		var bgcolor = $(this).children('a').css('border-left-color');
		$(this).find('.ky-dropdown-menu a').css({'background-color':bgcolor,'color':'#ffffff'});
	});

	$('.ky-dropdown').mouseout(function(){
		$('.ky-dropdown > ul.ky-dropdown-menu').css('display','none');
	});

	$('.ky-dropdown-menu a').mouseover(function(){
		var bgcolor = $(this).css('border-left-color');
		$(this).parent().parent().prev('a').css({'background-color':bgcolor,'color':'#ffffff'});
	});

			/*select */
	$(".select-box").click(function(){
		$(".select-content").toggleClass("hidden");
	});
	$(".select-content-detail").click(function(){
		$(this).parent().prev().find("input").val($(this).text());
		$(this).parent().toggleClass("hidden");

	});

 
	$("img.img-close").hover(function(){
	 	$(this).attr("src",global_Path+"/images/close-active.png");	 
	},function(){
			$(this).attr("src",global_Path+"/images/close-sm.png");
	});
	

});

