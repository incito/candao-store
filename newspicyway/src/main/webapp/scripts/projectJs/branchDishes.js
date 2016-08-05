var flag_prev =0;
$(document).ready(function(){

	$("img.img-close").hover(function(){
	 	$(this).attr("src",global_Path+"/images/close-active.png");	 
	},function(){
			$(this).attr("src",global_Path+"/images/close-sm.png");
	});
	/*菜品分类向左向右按钮*/
	$(".nav-dishes-next").click(function(){
		var count = $(".nav-dishes").find("li.nav-dishes-type").length;
		if(flag_prev<count-10){
				$(".nav-dishes").find("li.nav-dishes-type").eq(flag_prev).css("margin-left","-10%");

				flag_prev++;
		}

	});
	$(".nav-dishes-prev").click(function(){
		console.log("inner"+flag_prev);
		if(flag_prev>=1){	
			$(".nav-dishes").find("li.nav-dishes-type").eq(flag_prev-1).css("margin-left","0");			

			flag_prev--;
		}
	});

	$("img.img-close").click(function(){
		$("div.modal-backdrop").css("z-index","1030");
	});

	$("#menuViewStatus").click(function(){
		if($(this).find("img").attr("value")=="0"){
			
			updateDishStatus(1);
		}else if($(this).find("img").attr("value")=="1"){
			
			updateDishStatus(0);
		}
	});
	getDishMenuData();
});
//--------------------------------------------------------------------------------------------------------
/**
 * 获取数据
 */
function getDishMenuData(){
	$.get(global_Path+"/menu/branchDishMenuData.json", function(result){
		console.log(result);
		if(result != null){
			if(result.columList!=null && result.columList!=""){
				//if(result.columList.length>10){
				//	$(".nav-dishes-prev").css("display", "");
				//	$(".nav-dishes-next").css("display", "");
				//}else{
				//	$(".nav-dishes-prev").css("display", "none");
				//	$(".nav-dishes-next").css("display", "none");
				//}
				var colHtm = "";
				$.each(result.columList, function(i, column){
					var active = "";
					if(i == 0){
						active = "active";
					}
					colHtm += '<li class="nav-dishes-type '+active+' " value="'+column.itemDesc+'"  id="'+column.id+'" '
						+'onclick="oneclickDishType(this.id,this)">'
						+'<span>'+column.itemDesc+'</span><span>('+column.countDish+')</span></li>';
				});
				$("#nav-dishes").html(colHtm);
			}
			if(result.listdish!=null && result.listdish!=""){
				initDishes(result.listdish);
			}


			if($('#nav-dishes li').length > parseInt($('#nav-dishes').width()/111)) {
				$(".nav-dishes-next,.nav-dishes-prev").show();
			}
			
			var menu = result.menu;
			if(menu != null && menu != ""){
				$(".menuControl-list").removeClass("hide");
				var menuid = menu.menuid;
				$("#menuid").val(menuid);
				$("p.menuControl-list-name").text(menu.menuname);
				$("#branchname").text(menu.branchname);
				
				$(".menuControl-list-sigle").not(".menuControl-list-operate").click(function(){
					viewMenu(menuid);
				});
			}
		}
	});
}
//进入菜谱查看页面
function viewMenu(menuid){
	$(parent.document.all("detail")).attr("src",global_Path + "/menu/branchViewmenu?menuId="+ menuid +"");
	$("#allSearch").css("visibility","hidden");
}
function initDishes(listdish){
	$(".nav-dishes-tab .dishes-detail-box").remove();
	$.each(listdish, function(i, dish){
		var dishhtm = '<div class="dishes-detail-box"  id="'+dish.dishid+'" >'
			+'<p class="dishes-detail-name">'+dish.dishname+'</p></div>';
		$('#dishDetailList').before(dishhtm);
	});
	dishClick();
}
/**
 * 单击分类事件
 */
function  oneclickDishType(id){
	$(".nav-dishes li").removeClass("active");
	$("#"+id).addClass("active");
	$.ajax({
		url : global_Path + "/menu/getMenuDishByType.json",
		type : "post",
		datatype : "json",
		data:JSON.stringify({"menuid":$("#menuid").val(),
			"columnid":id
			}),
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			initDishes(result);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
}
function dishClick(){
	$('.dishes-detail-box').click(function(){
		var id =$(this).attr('id');
	    editMenuView(id);
	});
}
function initMenuView(){
	$("#menuViewDishid").text("");				
	$("#menuViewDishno").text("");	
	$("#menuViewStatus").html("");
	$("#menuViewTitle").text("");
	$("#menuViewDishType").text("");
	$("#menuViewUnit").text("");
	$("#menuViewPrice").text("");
	$("#menuViewVipPrice").text("");
	$("#tasteAndLabel").text("");
	$("#menuViewIntroduction").text("");
	$("#menuidInDish").val("");
	
}
//查看编辑估清
function editMenuView(id){
	initMenuView();
	$("#menuDetail-view-dialog").modal("show");
	
	$.ajax({
		url : global_Path + "/menu/getMenuDishDetailById.json",
		type : "post",
		datatype : "json",
		data:JSON.stringify({"menuid":$("#menuid").val(),
			"dishid":id
			}),
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			if(!jQuery.isEmptyObject(result)){
				$("#menuViewDishid").text(result.dish[0].dishid);
				$("#menuViewDishno").text(result.dish[0].dishNo);
				$("#menuViewTitle").text(result.dish[0].dishname);
				$("#menuViewIntroduction").text(result.dish[0].dishintroduction);
				if(result.dish[0].image!=''){
					$("#imgsrc").attr("src",img_Path+result.dish[0].image);
				}
				if(result.dish[0].status==1){
					$("#menuViewStatus").html('<img src="../images/menu-state-off.png" value="1" title="不足">');
				}else{
					$("#menuViewStatus").html('<img src="../images/menu-state-on.png" value="0" title="充足">');

				}
				$("#tasteAndLabel").append(result.dish[0].imagetitle);
				if(result.dish[0].imagetitle!=""&&result.dish[0].abbrdesc!=""){
					$("#tasteAndLabel").append(",");
				}
				$("#tasteAndLabel").append(result.dish[0].abbrdesc);
				var dishcol='';
				$.each(result.columnList,function(index,item){
					dishcol = dishcol+","+item.itemDesc;
				});
				$("#menuViewDishType").append(dishcol.substr(1,dishcol.length-1));
				var unit='';
				var price='';
				var vipprice='';
				$.each(result.unitList,function(index,item){
					if(item.unit!=''){
						unit = unit+"/"+item.unit;
					}
					if(item.price!=''){
						price = price+"/"+item.price;
					}
					if(item.vipprice!=''){
						vipprice = vipprice+"/"+item.vipprice;
					}
				});
				$("#menuViewUnit").text(unit.substr(1,unit.length-1));
				$("#menuViewPrice").text(price.substr(1,price.length-1));
				$("#menuViewVipPrice").text(vipprice.substr(1,vipprice.length-1));
			}
		}
	});
}

function updateDishStatus(statusTtd){

	$.ajax({
		url : global_Path + "/menu/updateDishStatus.json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		data:JSON.stringify({
			"status":statusTtd,
			"menuid":$("#menuid").val(),
			"dishid":$("#menuViewDishid").text()
		}),
		
		success : function(result) {
	
			if(result=="1"){
				if($("#menuViewStatus").find("img").attr("value")=="0"){
					$("#menuViewStatus").html('<img src="../images/menu-state-off.png" value="1" title="不足">');
				}else if($("#menuViewStatus").find("img").attr("value")=="1"){
					$("#menuViewStatus").html('<img src="../images/menu-state-on.png" value="0" title="充足">');
				}
				
			}
		}
	});
}
var dishTitleLength="";
function substrControl(dishTitle,titleLength){
	dishTitleLength="";
	dishTitleLength = getStrLength(dishTitle);
	if(dishTitleLength<=titleLength){
		return dishTitle;
	}
	for(var i=titleLength/2;i<titleLength;i++){
		dishTitleLength = getStrLength(dishTitle.substr(0,i));
		if(dishTitleLength>titleLength)	{
			return dishTitle.substr(0,i-1)+"...";
		}
	}
	
	
}
function getStrLength(str) { 
    var len = str.length; 
    var reLen = 0; 
    for (var i = 0; i < len; i++) {        
        if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) { 
            // 全角    
            reLen += 2; 
        } else { 
            reLen++; 
        } 
    } 
    return reLen;    
}

