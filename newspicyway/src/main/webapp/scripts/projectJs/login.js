$(function(){
	$("input.input-block-level").focus(function(){
		$(this).parent().addClass("active");
	}).blur(function(){
		$(this).parent().removeClass("active");
	});
	
	
	
});