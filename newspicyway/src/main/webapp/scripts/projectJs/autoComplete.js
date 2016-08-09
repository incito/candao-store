/**
 * jQuery自动补全插件
 * author : 刘根（liugen）
 * date : 2015-03-14
 * version : v1.0
 * 说明：输入英文时按首字母查询
 */

(function($, w){
	
	var old = $.fn.autoComplete;
	
	$.fn.autoComplete = function(option){
		
		var args = Array.prototype.slice.call(arguments, 1);
        var methodReturn;

        var $this = $(this);
        var data = $this.data('autoComplete');
        var options = typeof option === 'object' && option;

        if (!data) $this.data('autoComplete', (data = new AutoComplete(this, options) ));
        if (typeof option === 'string') methodReturn = data[ option ].apply(data, args);

        return ( methodReturn === undefined ) ? $this : methodReturn;
	}
	// 索引， 用于使用箭头选择项目
	var index = 0;
	
	// 计时，用于延时提交
	var timeout;
	
	var AutoComplete = function(element, options){
		this.$element = $(element);
        this.options = $.extend({}, defaults, options);
        this.$itemContainerWrap = $("<div></div>");
        this.$itemContainerWrap.addClass(this.options.containerClass).css({
    		position:"absolute",
			left:this.$element.position().left,
			top:this.$element.position().top + 34 +"px",
			width :this.$element.outerWidth()
    	});
        this.$divPos = this.$element.parent().parent();

        var d = this.$divPos.children(".autoComplete");
        if(d != null && d.length > 0){
        	this.$divPos.children(".autoComplete").remove();
        }
        this.$divPos.append(this.$itemContainerWrap);
       
        if (this.options.onTextChange instanceof Function) {
            this.$element.first().bind('auto', this.options.onTextChange);
        }
        this.$SearchBtn = this.options.searchBtn.jquery ? this.options.searchBtn : $(this.options.searchBtn);
        this.setupEvents();
        return this;
	};
	
	AutoComplete.prototype = {	
			
		version : "v1.0",
			
		constructor: AutoComplete,
		
		// 销毁
		destroy: function () {
            this.$element.removeData('autoComplete');
            this.$element.unbind('auto');
            return this;
        },
        
        // 拼接查询条件
        buildData : function(value){
		  	var thisValue = $.trim(value);
			//var firstCase = thisValue.substring(0, 1);
		  //	var regExp = /[^A-Za-z]/;
		  	var dataJson = '{"'+this.options.currentPage+'":"1","'+this.options.pageSize+'":"'+this.options.showCount+'"';
		   // if(!regExp.test(firstCase)){
		  //  	thisValue = firstCase.toUpperCase();
		  //  	dataJson += ',"nameFirstLetter":"'+thisValue+'"';
		  //  }else{
		   // 	dataJson += ',"name":"'+thisValue+'"';
		  //  }
		  	dataJson += ',"fullName":"'+thisValue+'"';
		    dataJson += '}';
		    return dataJson;
        },
        
        // 创建文档结构
        buildItems : function(data, total, textValue){
        	var item;
        	var _this = this;
        	this.$itemContainerWrap.empty();
        	
        	var $itemContainer = $("<ul></ul>");
        	if(data.length > 0){
	        	$.each(data, function(i, v){
	        		item = $("<li></li>");
	            	var itemText = v[_this.options.nameKey].replace(textValue,"<strong>"+textValue+"</strong>");
	            	item.addClass(_this.options.itemClass).html(itemText);
	            	item.attr("item-id", v.id);
	            	item.attr("item-type", v.type);
	            	$itemContainer.append(item);
	  			});
	        	//显示“查看更多”
	        	if(_this.options.showMore && total > _this.options.showCount){
	        		var moreli = $("<li class='show-more' id='show-more'>查看更多</li>");
	        		moreli.addClass(_this.options.itemClass);
	        		moreli.attr("total", total);
	        		$itemContainer.append(moreli);
	        	}else{
//	        		this.$itemContainerWrap.css("height",32*total+"px");
	        	}
	        	this.$itemContainerWrap.append($itemContainer);
	        	customScrollbar($(this.$itemContainerWrap));
			}
        },
        
        // Ajax查询
        postSubmit : function(value){
        	var dataJson = this.buildData(value);
    		var _this = this;
    		timeout = setTimeout(function(){
        		$.post(_this.options.url, $.parseJSON(dataJson), function(result){
        			_this.buildItems(result.rows, result.total, value);
        		}, "json");
    		},this.options.delay);
    		return true;
        },
        
        // 键盘事件
        keyChoose : function(keyCode){
        	
        	var items = this.$itemContainerWrap.find("li");
        	var count = items.length;
        	if(keyCode == 40){
        		items.eq(index).addClass(this.options.activeClass).siblings().removeClass(this.options.activeClass);
        		index++;
        		if(index >= count){
        			index = 0;
        		}
        	}
        	if(keyCode == 38){
        		if(index < 0){
        			index = count - 1;
        		}
        		items.eq(index).addClass(this.options.activeClass).siblings().removeClass(this.options.activeClass);
        		index--;
        	}
        	if(keyCode == 13){
        		items.filter(function(){
        			return $(this).hasClass("active");
        		}).trigger("click");
        	}
        },
        
        // 事件绑定
        setupEvents : function(){
        	var _this = this;
        	this.$element.on("keyup", function(e){
        		clearTimeout(timeout);
        		var $this = $(this);
        		var e = e || w.event;
        		var keyCode = e.keyCode;
        		if($.trim($this.val()) == ""){
        			_this.$itemContainerWrap.empty();
        			return false;
        		}
        		if((keyCode == 8 || keyCode == 32 || (keyCode == 13 && _this.$itemContainerWrap.children().length == 0) ||(keyCode >= 46 && keyCode <= 108))){
        			_this.options.showCount = 6;
            		_this.postSubmit($this.val());
            		
        		} 
    			if(_this.$itemContainerWrap.children().length > 0 && (keyCode == 13 || keyCode == 38 || keyCode == 40)){
    				_this.keyChoose(keyCode);
    			}
        	})/*.focus(function(){
        		var $this = $(this);
        		if($this.val() != ""){
        			_this.postSubmit($this.val());
        		}
        	})*/;
        	
        	this.$itemContainerWrap.on("click", "li", function(){
        		var $this = $(this);
        		if($this.attr("id") != "show-more"){//查看更多除外
        			_this.$element.val($this.text());
            		_this.$itemContainerWrap.empty();
        			_this.$element.first().trigger('auto', [$this.attr("item-id"), $this.attr("item-type")]);
        			//$.fn.autoComplete.findData.call(_this, null, 1, $this.attr("item-id"))
        			//_this.options.onTextChange.call(_this, $this.attr("item-id"));
        		}
        		
        	});
        	/**
        	 * 点击显示更多
        	 */
        	this.$itemContainerWrap.on("click", "li.show-more", function(){
        		var $this = $(this);
        		_this.options.showCount = $this.attr("total");
        		_this.postSubmit(_this.$element.val());
        	});
        	this.$SearchBtn.click(function(){
        		var $this = $(this);
        		if(_this.$element.val() == ""){
        			_this.options.onBtnAction.call(_this);
        		}
        	});
        	$(w.document).click(function(){
        		_this.$itemContainerWrap.empty();
        	});
        	
        }
	}
	
	var defaults = {
			
		// 输入延时
		delay : 300,
		
		// 下拉显示个数
		showCount : 5,
		
		// 请求url
		url : "",
		
		// 选择后触发的事件
		onTextChange : null,
		
		// 指定搜索按钮
		searchBtn : "#basic-addon1",
		
		// 点击搜索按钮
		onBtnAction : null,
		
		// 容器的class
		containerClass : "autoComplete",
		
		// 下拉项的class
		itemClass : "auto_item",
		
		// 选中时的class
		activeClass : "active",
		
		//当前页的参数名称。用来传递到后台接收
		currentPage:"page" ,
		//分页大小的 参数名称
		pageSize:"rows",
		
		//返回的数据JSON 用于取显示内容的key
		nameKey:"name",
		
		//是否显示更多操作
		showMore: false
		
	};
	
	$.fn.autoComplete.Constructor = AutoComplete;

    $.fn.autoComplete.noConflict = function () {
        $.fn.autoComplete = old;
        return this;
    };
	
})(jQuery, window);
