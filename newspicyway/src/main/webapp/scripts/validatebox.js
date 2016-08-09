		$.extend($.fn.validatebox.defaults.rules, {   
		    maxLength: {   
		        validator: function(value, param){   
		            return value.length >= param[0] && value.length <= param[1];   
		        },   
		        message: '请输入为{0}到{1}个字段！' 
		    } ,
		    phone : {// 验证电话号码
    	        validator : function(value) {
    		            return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
    		        },
    		        message : "格式不正确,请使用下面格式:010-84603677"
    		    },
		    fileType: {
		    	validator: function(value, param){
		    	    return	/^.*?\.(bmp|gif|jpg|jpeg|ico|png|tif)$/.test(value.toLowerCase());
		    	},
		    	message: '请选择(bmp|gif|jpg|jpeg|ico|png|tif)格式的图片！'
		    },
		    number: {
		        validator: function (value, param) {
		            return /^\d+$/.test(value);
		        },
		        message: '请输入整数',
		    },
		    selectSort: {
		    	validator: function(value,param){
		    			 pattern1 = new RegExp("\^\\d{"+param[0]+","+param[1]+"}\$");	
		    		return pattern1.test(value);
		    	},
		    		message: '请输入{0}到{1}位数字！',
		    },
		    safepass: {
		        validator: function (value, param) {
		            return safePassword(value);
		        },
		        message: '密码由字母、数字和特殊符号中至少两种组成且至少6位！',
		    },
		    equalpass: {
		        validator: function (value, param) {		           
		        	return value == $(param[0]).val();
		        },
		        message: '两次输入的密码不一致！',
		    },
		    selectSorts: {
		    	validator: function(value,param){
	    			 pattern1 = new RegExp("\^\\d{"+param[0]+"}\$");
                    return pattern1.test(value);
		    	},
		    	message: "请输入{0}位数字手机号码!",
		    },
		    intOrFloat: {// 验证整数或小数
                validator: function (value) {
                    return /^\d+(\.\d+)?$/i.test(value);
                },
                message: '请输入整数或小数！'
            },
            comparedata: {
		        validator: function (value, param) {		           
		        	return value > $(param[0]).val();
		        },
		        message: '结束日期必须大于开始日期！',
		    }
		});  
		
		/* 密码由字母、数字和特殊符号中至少两种组成，至少6位 */
		var safePassword = function (value) {
		    return !(/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/.test(value));
		};
		