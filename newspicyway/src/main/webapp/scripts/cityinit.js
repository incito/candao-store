
// 初始化省下拉框
function initProvince(){
	$("#province").append("<option > </option>");
	for(var key in ja){
		if(key.length ==4 && key.substring(2)=='00'){
			$("#province").append("<option value='"+key+"' >"+ja[key]+"</option>");
		}
	}
}
// 根据省的选择，初始化市的下拉框数据
function initCity(obj){
	$("#city").empty();
	$("#region").empty();
	$("input[name='province']").val(ja[$("#province").val()]);
	// 直辖市
	if(obj.value=="0100" || obj.value=="0200" || obj.value=="0300" || obj.value=="0400"){
		$("#city").append("<option value='"+obj.value.substring(0,2)+"02' >"+ja[obj.value]+"</option>");
		initRegion(obj.value.substring(0,2)+"02");
	}else if(obj.value=="3200" || obj.value=="3300" || obj.value=="3400"){//特别行政区
		$("#city").append("<option value='"+obj.value+"' >"+ja[obj.value]+"</option>");
		initRegion(obj.value);
	}else{
		$("#city").append("<option > </option>");
		for(var i=2;i<60;i++){	
			var code=add(obj.value,i);
			if(ja[code]==null){
				break;
			}else{
				$("#city").append("<option value='"+code+"' >"+ja[code]+"</option>");
			}
	
		}
	}
}

//根据市的选择，初始化区县的下拉框数据
function initRegion(obj){
	
	$("input[name='city']").val(ja[$("#city").val()]);
	
	$("#region").empty();
	$("input[name='region']").val("");
	
	$("#region").append("<option > </option>");
	for(var i=1;i<60;i++){	
		var code=add_(obj,i);
		if(ja[code]==null && obj !='01'){
			break;
		}else{
			$("#region").append("<option value='"+code+"' >"+ja[code]+"</option>");
		}
	}
	if($("#city_la")){
		$("#city_la").remove();
	}
}
//非两位数在前面加0补充两位
function add_(id,n){
		if( (n) <10){
			return id +'0'+  n ;
		}else{
			return n;
		}
}

//赋值给区域
function changeRegion(val){
	$("input[name='region']").val(ja[val]);
	if($("#city_la")){
		$("#city_la").remove();
	}
}


/**
 * select form 控件 - 用于城市反选
 * @param id
 * @param text
 */
function selectedByText(id, text) {
    var count = $("#" + id + " option").length;

    for (var i = 0; i < count; i++) {
        if ($("#" + id).get(0).options[i].text == text) {
            $("#" + id).get(0).options[i].selected = true;
            break;
        }
    }
}