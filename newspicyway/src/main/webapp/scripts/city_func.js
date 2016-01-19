 
var limit=0;
var cityid_;

var getOffsetPosition=function(elem){
    if ( !elem ) return {left:0, top:0};
    var top = 0, left = 0;
    if ( "getBoundingClientRect" in document.documentElement ){
        //jquery方法
        var box = elem.getBoundingClientRect(),
        doc = elem.ownerDocument,
        body = doc.body,
        docElem = doc.documentElement,
        clientTop = docElem.clientTop || body.clientTop || 0,
        clientLeft = docElem.clientLeft || body.clientLeft || 0,
        top  = box.top  + (self.pageYOffset || docElem && docElem.scrollTop  || body.scrollTop ) - clientTop,
        left = box.left + (self.pageXOffset || docElem && docElem.scrollLeft || body.scrollLeft) - clientLeft;
    }else{
        do{
            top += elem.offsetTop || 0;
            left += elem.offsetLeft || 0;
            elem = elem.offsetParent;
        } while (elem);
  }
    return {left:left, top:top};   
}

$(function(){
	
	$(".cityOne").click(function(){
		citySelect(1,$(this));//343 231
		if($(this).attr("external")=="special"){
			var offset =getOffsetPosition(this);
			$('.citySelect').css({'display':'block','top':offset.top+30+'px','left':offset.left+'px','margin-left':0});
		}else{
			 
			$('.citySelect').css({'display':'block','top':'32px'});
		}
		 
	});
	
	$(".cityMany").click(function(){
		citySelect(5,$(this));
		if($(this).attr("external")=="special"){
			var offset =getOffsetPosition(this);
			$('.citySelect').css({'display':'block','top':offset.top+30+'px','left':offset.left+'px','margin-left':0});
		}else{
			  
			$('.citySelect').css({'display':'block','top':'32px'});
		}
	});
	
	//根据县市ID，获得上级全称名称。省、市、县
	function getParent(id){
		if(id=="0100" || id=="0200"  || id=="0300"  || id=="0400" || id=="3200"|| id=="3300"|| id=="3400" ){
			return ja[id];
		}else if(id.length==6){
			return ja[id.substring(0,2)+"00"]+" "+ja[id.substring(0,4)]+" "+ja[id];
		}else if(id.length==4){
			return ja[id.substring(0,2)+"00"]+" "+ja[id];
		}
	}

	//城市选择div  确定按钮点击
	$('#cityOK_').click(function(){
		cityid_.focus();
		$('.citySelect').css('display','none');
		res_="";
		if($("button[class*='sCity']").size()==0) {cityid_.val(""); return;}
		var   reg=new   RegExp( "区$");
		if(limit==1){
			if(cityid_.attr("flag")=="all"){//需要显示省市县。
						//getCityIdbyContent
				var content=$("button[class*='sCity']:eq(0)").text();
				var myid=$("button[class*='sCity']:eq(0)").attr("id").replace("id","")  ;// id="id1802"
						if (maincity.join("、").indexOf(
								myid.substring(0, 2) + "00") != -1) {//直辖市
							//if (reg.test(content)) {
							// 如果选择【区】，获取它的上级。
								//cityid_.val(ja[myid.substring(0,2)+"00"]);
							//}else
							if(myid.length==6){//县,区
								cityid_.val(ja[myid.substring(0,2)+"00"]+" "+ja[myid]);
							}else{//市
								cityid_.val(ja[myid.substring(0,2)+"00"]);
							}
						}else{
							if (reg.test(content)) {
								var pid=myid.substring(0,4);
								cityid_.val(ja[pid.substring(0, 2) + "00"]
										+ " " + ja[pid]+" "+ja[myid]);// ja
								// code
								// 编码规则获得要的结果。
							} else {

								 
									if (myid.length == 6) {// 选择的县城
										cityid_.val(ja[myid.substring(0, 2)
												+ "00"]
												+ " "
												+ ja[myid.substring(0, 4)]
												+ " " + ja[myid]);
									} else {// 选择的市
										var prefix_2=myid.substring(0, 2);
										//香港  澳门 台湾
										if(prefix_2=="32" || prefix_2=="33" || prefix_2=="34"){
											cityid_.val(ja[myid.substring(0, 2)
															+ "00"]);
										}else{
											cityid_.val(ja[myid.substring(0, 2)
													+ "00"]
													+ " "
													+ ja[myid.substring(0, 4)]);
										}
									}
								 
							}
						}
			 
				
			}else{
				cityid_.val($("button[class*='sCity']:eq(0)").text());
				var myid=$("button[class*='sCity']:eq(0)").attr("id").replace("id","")  ;// id="id1802"
				var result =cityid_.val();
				  
				if(reg.test(result)){
					//alert(result);
					cityid_.val(ja[myid.substring(0,4)]);
				}else{
					cityid_.val(ja[myid]);
				}
			}
			 
			
		}else if(limit==5){
			var resultQ="";
			$("button[class*='sCity']").each(function(index,obj){
				  resultQ+=$(this).attr("id").replace("id","") +"、";
				
			 });
			var id_arr =  resultQ==""?"":getParentCity(resultQ.substring(0,resultQ.length-1));
			arr=id_arr;
			 
			getparents(arr);
			var ids = res_.substring(0,res_.length-1).split("、");
			var city='';
			for(var i =0;i<ids.length;i++){
				city+=ja[ids[i]]+"、";
			}
			cityid_.val(city.substring(0,city.length-1));
		}
	});
	
	//如果选择了区，显示时转换成它的上级[市]显示。 所以域的所有的code ，以“、”分隔。把以“区”结尾的换成上一级code.
	function getParentCity(resultQ){
		 
		var   reg=new   RegExp( "区$");
		 var arr = resultQ.split("、");
		 for (var i in arr){
			 if(reg.test( ja[arr[i]])){//以“区”结尾
				 arr[i]=arr[i].substring(0,4);
			 }
		 }
		 
		 return arr;
	}
	
	//多个重复的城市保留一个。
	var res_ = "";
	//var arr = [  ];
	var flg = true;
	function getparents(arr) {
		//alert(arr+"==inner");
		if (arr.length == 1) {
			res_ += arr[0] + "、";
			return;
		}
		res_ += arr[0] + "、";
		 //alert(res_+'---res_');
		var temp  = arr[0];
		arr = arr.slice(1);
		for (var i = 0; i < arr.length; i++) {
				//alert("another=="+temp);
				//两个区是否为同一个市。
			if (arr[i] == temp && i == 0) {
				arr = arr.slice(1);
				i = -1;
				flg = true;
			} else if (temp ==arr[i] && i != 0) {
				arr = arr.slice(0, i).concat(arr.slice(i + 1));
				i = -1;
				flg = true;
			} else {
				flg = false;
			}
			//alert(arr+"??????");
		}
		if (!flg) {
			getparents(arr);
		} 
		 
	}
	
	//把区转换成市
	function areaToCity(arr){
		for(var i in arr){
			var   reg=new   RegExp( "区$");
			if(reg.test(arr[i])){
				var id=getCityIdbyContent(arr[i]);
				arr[i]=ja[id.substring(0,4)];
			}
		}
		return arr;
	}
	
	//城市选择div  取消按钮点击
	$('#cityCancel_').click(function(){
		$('.citySelect').css('display','none');
	});
	 
	
	
	
});

function init(){
	 $("button[class*='sCity']").remove();
	 
	 $('#left_').empty();
	 $('#county').empty();
}
// 主入口
function citySelect(num_,id_){
	init();
	limit=num_;
	cityid_=id_;
	num=0;
	$("#myfont").html("（您最多能选择"+limit+"项）");
	//直辖市
	var zxsHtml='<p><span class="cityTitle"  >直辖市</span>';
	for(var i=0;i<maincity.length;i++ ){
		zxsHtml+='<span class="city-spacing cityContent prov" onclick=getCity("'+maincity[i]+'")  id="'+maincity[i]+'"> '+ja[maincity[i]]+' </span>';

	}
 
	zxsHtml=zxsHtml+'</p>';
	
	var prov ="";
	var departProv="";
	//省份
	for(var i=0;i<allprov.length;i++){
		prov+='<p > <span class="letter cityTitle  col-xs-3 no-lr-padding" style="width: 50px;margin-top:0"  > '+allprov[i][0]+'</span><span	class="col-xs-9 no-lr-padding">';
		departProv =allprov[i][1];
		for(var j =0;j<departProv.length;j++){
			prov+='<span class="city-spacing cityContent prov"  onclick=getCity("'+departProv[j]+'")  id="'+departProv[j]+'"> '+ja[departProv[j]]+'</span>';
		}
		prov+='</span></p>';
	}
	//alert(prov);
	$('#left_').html(zxsHtml+prov);

	//初始化选择城市色彩
	setColor("prov");

	showChoicedCity();

	//jobArea.Show();
	//boxAlpha();
	//draglayer();
}

function getCity(provid){
 
	 //其他，要特别处理，不显示市，直接选中。
	 if(provid=='3200' || provid=='3300' || provid=='3400'  ){
			dochoice(provid);
			$('#county').empty();
			return;
	 }
	

	var inner='';
	var allcounty_;
	for(var i=2;i<60;i++){	
		//code后面两位+1
		var code=add(provid,i);
		//alert(ja[code]);
		if(ja[code]==null){
			//alert('a');
			break;
		}else{
			//alert('ab');
			
			inner+='<p> <span id="'+code+'"  class="cityTitle city-spacing county no-lr-padding cityR" onclick=dochoice("'+code+'") > '+ja[code]+' </span>';
			inner+='<span class="col-xs-9 no-lr-padding" style="float: left;">'+getxian(code)+'</span></p>';
		}

	}
	//alert(inner);
	$('#county').html(inner);
	
	//setColor("county");
	
	cancelColor();
}



function myConfirm(){
	var codes = $('#hidarea').val().substring(1);
	//alert(codes);;
	var code_arr= codes.split(',');
	var result;
	for(var i=0;i<code_arr.length;i++){
		result+= ja[code_arr[i]]+ ",";
	}

	self.parent.myarea.value=result;
	self.close();
}

function add(id,n){
	//if(id.substring(0,2)=='00'){
		var twoend = id.substring(2,4);
		var  result= parseInt(twoend,10);
		if( (result+n) <10){
			return id.substring(0,2)+'0'+ (result+n)
		}else{
			return id.substring(0,2)+(result+n)
		}
	//}else{

	//}
}

function getxian(scode){
	var innerHtml='';
	for(var k=0;k<allcounty.length;k++){
		//alert(allcounty[k][0]);
		if(allcounty[k][0]==scode){
			var xian=allcounty[k][1];
			for(var i =0;i<xian.length;i++){
				innerHtml+='<span class="city-spacing cityContent county"  onclick=dochoice("'+xian[i]+'") id="'+xian[i]+'">'+ja[xian[i]]+'</span>';
			}
		}
	}
	
	
	return innerHtml;
}

var num=0;
function dochoice(id){
 
	if(isChoice(id)){
		return ;
	}
	
	//limit==1  当为单选择时，每次选择覆盖上次的选择。
	if(limit==1){
		//去掉已经选择的城市。
		$(".sCity").remove();
		// 去掉之前选中城市的亮显。
		$(".cityColor").removeClass("cityColor");
	}else{
	
		if(num>(limit-1)){
			alert('您最多能选择'+limit+'项');
			return;
		}
	}
	$('#choiced').before('<button type="button" id="id'+id+'" onclick=delete_("'+id+'") class="btn btn-major sCity btn-sm">'+ja[id]+'<i class=""></i></button>');
	
	$('#'+id).toggleClass('cityColor');
	num++;
}

function delete_(id){
	$('#id'+id).remove();
	
	$('#'+id).toggleClass('cityColor');
	num--;
}
// 选择项删除后，对应项色未能取消。再下次显示该县时取消
function cancelColor(){
	var hasCanceled=[];
	$("button[class*='sCity']").each(function(i,obj){
		hasCanceled.push(this.id.replace("id",""));
	});
	//alert(hasCanceled);
	if(hasCanceled.length>0){
		for(var i=0;i<hasCanceled.length;i++){
			if($('#'+hasCanceled[i]).size()==1){
				$('#'+hasCanceled[i]).addClass('cityColor');
				//hasCanceled[i]=null;
			}
		}
		
	}
}
//是否已经选择过，如果已选，不能重复选。返回true;
function isChoice(id){
	 var exists =false;
	 $("button[class*='sCity']").each(function(index,obj){
		
		 if(ja[id]==$(this).text()){
			 exists=true;
			 return ;
		 }
	 });
	return exists;
}
// 对之前选择过城市的，再次打开后，已选择城市要变色。
function setColor(flag){
	var choicedCity =cityid_.val();
	 
	if(choicedCity==""){
		return;
	}
	
	if(cityid_.attr("flag")=="all"){// 省市县
		var ssx_arr=choicedCity.split(" ");
		if(ssx_arr.length==3){
			choicedCity=ssx_arr[2];
		}else if(ssx_arr.length==2){
			choicedCity=ssx_arr[1];
		}
	} 
	
	$("."+flag).each(function(i,obj){
		 
		if(choicedCity.indexOf(ja[this.id])!=-1){//选择过的城市，改变色彩
			$('#'+this.id).toggleClass('cityColor');
			
		}
	});
	
	
}

//初始化已选城市
function showChoicedCity(){
	var choicedCity =cityid_.val();
	 
	if(choicedCity==""){
		return;
	}
	
	if(cityid_.attr("flag")=="all"){// 省市县
		var ssx_arr=choicedCity.split(" ");
		if(ssx_arr.length==3){
			choicedCity=ssx_arr[2];
		}else if(ssx_arr.length==2){
			choicedCity=ssx_arr[1];
		}
	} 
	//alert(choicedCity);
    // 市或县
	var citys= choicedCity.split("、");
	 
	num=citys.length;
	for(var k=0;k<citys.length;k++){
		var id=getCityIdbyContent(citys[k]);
		$('#choiced').before('<button type="button" id="id'+id+'" onclick=delete_("'+id+'") class="btn btn-major sCity">'+ja[id]+'<i class=""></i></button>');
	}
	 
}

//由城市名称获得对应数组的ID
function getCityIdbyContent(content){
	for(var jj in ja){
		 
		if(ja[jj]==$.trim(content)){
			return jj;
		}
	}
}

