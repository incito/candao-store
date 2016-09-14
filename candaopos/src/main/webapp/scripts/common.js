$(document).ready(function(){
	// Copyright 2014-2015 Twitter, Inc.
	// Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
	  var msViewportStyle = document.createElement('style');
	  msViewportStyle.appendChild(
	    document.createTextNode(
	      '@-ms-viewport{width:auto!important}'
	    )
	  );
	  document.querySelector('head').appendChild(msViewportStyle);
	}
});

function backspace(){
    var inputtext = activeinputele.val();
    if(inputtext.length>0){
        inputtext = inputtext.substring(0,inputtext.length-1);
        activeinputele.val(inputtext);
    }    
}

function goBack(){
	window.history.back(-1);
}

/*
* 公共模态框
* ***参数***
*
*  - `content` {String} 填充内容
*  - `title` {String} 模态框标题
*  - `width` {Number} 默认500
*  - `height` {Number} 默认auto
*  - `vertical` {Boolean} 默认true
*  - `cls` {String} 为modal添加样式,多个用空格分开
*  - `hasBtns` {Boolean} 是否有按钮 默认为true
*  - `btnOkTxt` {String} ok按钮文字 为''时,不显示
*  - `btnCancelTxt` {String} cancle按钮文字 为''时,不显示
*  - `btnOkCb` {Function} ok按钮回调
*  - `btnCancelCb` {Function} cancel按钮回调 默认带有关闭事件
*  - `onReady` {Function} 创建完成后回调
* ***方法***
*
* - `close()` 销毁modal
* - `hide()`  隐藏modal
* - `show()`  对hide的modal 显示
* */
window.Modal = function () {
	var _tplHtml =
		'<div class="modal dialog-normal bg-gray created-modal [Cls]" id="[Id]">' +
			'<div class="modal-content" style="width:[Width]px;margin: 30px auto;">' +
				'<div class="modal-header">' +
					'<span class="close" data-dismiss="modal">×</span>' +
					'<h5 class="modal-title"><i class="icon-exclamation-sign"></i> [Title]</h5>' +
				'</div>' +
				'<div class="modal-body">' +
					'[Content]' +
				'</div>' +
				'<div class="modal-footer" >' +
					'<button type="button" class="btn-base btn-base-sm cancel" data-dismiss="modal">[BtnCancelTxt]</button>' +
					'<button type="button" class="btn-base btn-yellow btn-base-sm ok">[BtnOkTxt]</button>' +
				'</div>' +
			'</div>';
		'</div>';


	var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');

	var _alert = function (options) {
		var id = _dialog(options);
		var modal = $('#' + id);
		modal.addClass('modal-alert');

		return {
			id: id,
			hide: function(){
				modal.modal('hide');
			},
			close: function(){
				_close(id);
			},
			show:function(){
				modal.modal('show');
			}
		};
	};

	var _confirm = function (options) {
		var id = _dialog(options);
		var modal = $('#' + id);
		modal.addClass('modal-confirm');
		return {
			id: id,
			hide: function(){
				modal.modal('hide');
			},
			close: function(){
				_close(id);
			},
			show:function(){
				modal.modal('show');
			}
		};
	};


	var _getId = function () {
		var date = new Date();
		return 'mdl' + date.valueOf();
	};

	var _close = function(id){
		var $modal = $('#' + id);
		$modal.next().remove();
		$modal.remove();
	};

	var _dialog = function (options) {
		var ops = {
			content: "提示内容",
			title: "操作提示",
			width: 500,
			height: 'auto',
			auto: false,
			cls:'',
			hasBtns: true,
			btnCancelTxt: '取消',
			btnOkTxt: '确定',
			vertical: true,
			onReady: null,
		};

		ops = $.extend({},ops, options);

		var modalId = _getId();

		var html = _tplHtml.replace(reg, function (node, key) {
			return {
				Id: modalId,
				Title: ops.title,
				Cls: ops.cls,
				Width: ops.width,
				Content: ops.content,
				BtnCancelTxt: ops.btnCancelTxt,
				BtnOkTxt: ops.btnOkTxt,
			}[key];
		});


		$('body').append(html);

		var $modal = $('#' + modalId);

		//按钮逻辑
		if(!ops.hasBtns) {
			$modal.find('.modal-footer').remove();
		} else {

			if(ops.btnOkTxt === '') {
				$modal.find('.ok').remove();
			} else {
				$modal.find('.ok').bind('click', function(){
					ops.btnOkCb && ops.btnOkCb.call(this);
				})
			}

			if(ops.btnCancelTxt === ''){
				$modal.find('.cancel').remove();
			} else {
				$modal.find('.cancel').bind('click', function(){
					ops.btnCancelCb && ops.btnCancelCb.call(this);
					_close(modalId);

				})
			}
		}


		ops.onReady && ops.onReady.call(this);

		//关闭按钮
		$modal.find('.close').bind('click', function(){
			_close(modalId);
		});

		if(ops.vertical) {
			$modal.css({
				'display': 'flex',
				'align-items': 'center'
			});
		}

		$modal.next('.modal-backdrop').addClass('.modal-backdrop-' + modalId);
		$modal.modal({
			width: ops.width,
			backdrop: 'static'
		});

		return modalId;
	};



	return {
		alert: _alert,
		confirm: _confirm
	}

}();



Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};
/**
 * 自定义Map
 */
function HashMap(){
	/** 存放键的数组(遍历用到) */
	this.keys = new Array();
    //创建一个对象
    var obj = new Object();

    /**
    * 判断Map是否为空
    */
    this.isEmpty = function(){
        return this.keys.length == 0;
    };

    /**
    * 判断对象中是否包含给定Key
    */
    this.containsKey=function(key){
        return (key in obj);
    };

    /**
    * 判断对象中是否包含给定的Value
    */
    this.containsValue=function(value){
        for(var key in obj){
            if(obj[key] == value){
                return true;
            }
        }
        return false;
    };

    /**
    *向map中添加数据
    */
    this.put=function(key,value){
        if(!this.containsKey(key)){
        	this.keys.push(key);
        }
        obj[key] = value;
    };

    /**
    * 根据给定的Key获得Value
    */
    this.get=function(key){
        return this.containsKey(key)?obj[key]:null;
    };

    /**
    * 根据给定的Key删除一个值
    */
    this.remove=function(key){
        if(this.containsKey(key)&&(delete obj[key])){
        	this.keys.remove(key);
        }
    };

    /**
    * 获得Map中的所有Value
    */
    this.values=function(){
        var _values= new Array();
        for(var key in obj){
            _values.push(obj[key]);
        }
        return _values;
    };

    /**
    * 获得Map中的所有Key
    */
    this.keySet=function(){
        var _keys = new Array();
        for(var key in obj){
            _keys.push(key);
        }
        return _keys;
    };

    /**
    * 获得Map的长度
    */
    this.size = function(){
        return this.keys.length;
    };

    /**
    * 清空Map
    */
    this.clear = function(){
    	this.keys = new Array();
        obj = new Object();
    };

	/**
	 * 遍历Map,执行处理函数
	 *
	 * @param {Function}
	 *            回调函数 function(key,value,index){..}
	 */
	this.each = function(fn) {
		if (typeof fn != 'function') {
			return;
		}
		var len =this.keys.length;
		for (var i = 0; i < len; i++) {
			var k = this.keys[i];
			fn(k, this.get(k), i);
		}
	};
}