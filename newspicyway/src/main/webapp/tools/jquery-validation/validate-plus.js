	//定义中文消息
	var cnmsg = {
	    required: "必填字段",
	    remote: "请修正该字段",
	    email: "请输入正确格式的电子邮件",
	    url: "请输入合法的网址",
	    date: "请输入合法的日期",
	    dateISO: "请输入合法的日期 (ISO).",
	    number: "请输入合法的数字",
	    digits: "只能输入整数",
	    creditcard: "请输入合法的信用卡号",
	    equalTo: "请再次输入相同的值",
	    accept: "请输入拥有合法后缀名的字符串",
	    maxlength: jQuery.format("请输入一个长度最多是 {0} 的字符串"),
	    minlength: jQuery.format("请输入一个长度最少是 {0} 的字符串"),
	    rangelength: jQuery.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
	    range: jQuery.format("请输入一个介于 {0} 和 {1} 之间的值"),
	    max: jQuery.format("请输入一个最大为 {0} 的值"),
	    min: jQuery.format("请输入一个最小为 {0} 的值")
	};
	$.extend($.validator.messages, cnmsg);

	//单独定义 验证是否折扣的方法
	$.validator.addMethod("isDiscount", function(value, element) {  
	    var elem = $(element);
	    var reg = /^\d{1}(\.\d{1})?$/;
	    //TODO 使用正则表达式判断 大小以及 小数点后 只能一位
	    return reg.test(elem.val());
	}, "请输入有效的折扣额");
	
	//定义价格
	$.validator.addMethod("isPrice", function(value, element) {  
	    var elem = $(element);
	    var reg = /^\d{1,6}(\.\d{1,2})?$/;
	    return reg.test(elem.val());
	}, "请输入有效的价格");
	
	$.validator.setDefaults({
//		errorClass : "popover-content",
		errorClass : "popover-errorTips",
		errorPlacement: function(error, element) {  
			element.parent().append(error);
//			element.parents().css("position","relative");
//			var classToggle = "top";
//			var top = element.position().top - 50 + "px";
//			var left = element.position().left + 50 + "px";
//			if(element.hasClass("bottom")){
//				top = element.position().top + 40 + "px";
//				left = element.position().left + 20 + "px";
//				classToggle = "bottom";
//			}
//			var div = $("<div>").addClass("popover fade "+classToggle+" in").css({
//					display : "block",
//					top : top,
//					left : left,
//				}).append('<div class="arrow" style="left: 50%;"></div>').append(error);
//			
//		    element.after(div);
		    
		},
		success: function(label) {
			label.parents("div.popover").remove();
		}
		
	}) ;
	function picked(){
		if($(this).val() != ""){
			$(this).parent().find("div.popover").remove();
		}
	}
