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
					'<button type="button" class="btn-base btn-yellow btn-base-sm ok">[BtnOkTxt]</button>' +
					'<button type="button" class="btn-base btn-base-sm  cancel" data-dismiss="modal">[BtnCancelTxt]</button>' +
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
			show:function(){
				modal.modal('show');
			}
		};
	};

	var _confirm = function (options) {
		var id = _dialog(options);
		var modal = $('#' + id);

		return {
			id: id
		};
	};


	var _getId = function () {
		var date = new Date();
		return 'mdl' + date.valueOf();
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
		var $content = $modal.find('.modal-content');



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
				})
			}
		}


		if(ops.vertical) {
			$modal.css({
				'display': 'flex',
				'align-items': 'center'
			});
		}


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


