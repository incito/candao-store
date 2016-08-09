var easyuiPanelOnMove = function(left, top) {

	    var parentObj = $(this).panel('panel').parent();

	    if (left < 0) {

	        $(this).window('move', {

	            left : 1

	        });

	    }

	    if (top < 0) {

	        $(this).window('move', {

	            top : 1

	        });

	    }

	    var width = $(this).panel('options').width;

	    var height = $(this).panel('options').height;

	    var right = left + width;

	    var buttom = top + height;

	    var parentWidth = parentObj.width();

	    var parentHeight = parentObj.height();

	    if(parentObj.css("overflow")=="hidden"){

	        if(left > parentWidth-width){

	            $(this).window('move', {

	                "left":parentWidth-width

	            });

	        }

	        if(top > parentHeight-height){

	            $(this).window('move', {

	                "top":parentHeight-height

	            });

	        }

	    }

	};

	$.fn.panel.defaults.onMove = easyuiPanelOnMove;

	$.fn.window.defaults.onMove = easyuiPanelOnMove;

	$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
 
    var easyuiPanelOnMove = function(left, top) {
        var parentObj = $(this).panel('panel').parent();
        if (left < 0) {
            $(this).window('move', {
                left : 1
            });
        }
        if (top < 0) {
            $(this).window('move', {
                top : 1
            });
        }
        var width = $(this).panel('options').width;
        var height = $(this).panel('options').height;
        var right = left + width;
        var buttom = top + height;
        var parentWidth = parentObj.width();
        var parentHeight = parentObj.height();
        if(parentObj.css("overflow")=="hidden"){
            if(left > parentWidth-width){
                $(this).window('move', {
                    "left":parentWidth-width
                });
        }
            if(top > parentHeight-$(this).parent().height()){
                $(this).window('move', {
                    "top":parentHeight-$(this).parent().height()
                });
        }
        }
    };
$.fn.panel.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;