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