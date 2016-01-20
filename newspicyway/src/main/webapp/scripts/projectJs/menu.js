var up_num =0;
var menu_num =0;
var dataStyle = new Object();
var thumb_num = 0;
var drop_id = 0;

//add by lisafan
var map = null;
var g_typeid = "";

var tmpllist = new Object();
var operate = "";
var modmenu = null;

//原图高度和宽度
var originalWidth=0;
var originalHeight=0;
var menuobj = new Object();
var menubrachobj = new Object();
//菜品分类排序
var columnSortMap = null;

//查看菜谱、编辑菜谱使用
var columnTmplMap = null;
var columnObjMap = null;
//存放编辑菜谱时菜谱上所有菜品
var columnIdMap = null;

//存放每个菜品分类下，缩略图位置（按左右键使用）
var menuNumMap = null;

var currMenu = {};
var T_role = {
	A : "menu-detail1",
	B : "menu-detail2",
	C : "menu-detail3",
	D : "menu-detail4",
	E : "menu-detail5",
	F : "menu-detail6",
	G : "menu-detail7",
	H : "menu-detail8",
	I : "menu-detail9",
	J : "menu-detail10",
	L : "menu-detail12"
};
var T_role_reverse = {
	"menu-detail1": "A",
	"menu-detail2":	"B",
	"menu-detail3":	"C",
	"menu-detail4":	"D",
	"menu-detail5":	"E",
	"menu-detail6":	"F",
	"menu-detail7":	"G",
	"menu-detail8":	"H",
	"menu-detail9":	"I",
	"menu-detail10": "J",
	"menu-detail12":"L"
};
//裁剪图片
var jcrop_api =null;
//菜谱状态（4未启用,1已启用,2定时启用,3删除）
$(document).ready(function(){
	$.ajaxSetup ({
	    cache: false //关闭AJAX相应的缓存
	});
	var menuid = $("#menuId_param").val();
	var operate_type = $("#operateType_param").val();
	
	columnIdMap = new HashMap();
	if(menuid != null && menuid!="" ){
		if(operate_type != null && operate_type == "edit_menu"){
			operate = "edit_menu";
			initEditTmpl(menuid);
		}else{
			operate = "edit";
			getMenuById(menuid);
		}
	}else{
		operate = "create";
		initDisheType();
	}
	
	if(operate == "create" || operate == "edit_menu"){
		$("#dish_menu_prop").validate({
			submitHandler : function(form) {
				var vcheck = true;
				if (vcheck) {
					dishesDetailSave();
				}
			}
		});
	}
	$(document).click(function(){
		if($(".menu-add-type").attr("class") != undefined){
			if($(".menu-add-type").attr("class").indexOf("hidden")==-1){
				$(".menu-add-type").addClass("hidden");
			}
		}
		$(".menu-detail-change").addClass("hidden");
	});
	
	$(".nav-dishes-prev").css("visibility","hidden");
	$(".nav-dishes-next").css("visibility","hidden");
	
	$(".nav-menu-prev").css("visibility","hidden");
	$(".nav-menu-next").css("visibility","hidden");
	
	menuNumMap = new HashMap();
	//编辑菜谱
	$("#menuAdd-edit-btn").click(function(){
		if($(".effecttime_span").attr("status") == 1){
			$('#menuControl-prompt-modal').modal('show');
			$('#menuControl-ok').click(function() {
				$('#menuControl-prompt-modal').modal('hide');
				$(parent.document.all("detail")).attr("src",global_Path + "/menu/editmenu?menuId="+ menuid +"");
				$("#allSearch").css("visibility","hidden");
			});
		}else{
			$(parent.document.all("detail")).attr("src",global_Path + "/menu/editmenu?menuId="+ menuid +"");
			$("#allSearch").css("visibility","hidden");
		}
		
	});
	// add zt 423 
	/*菜品分类*/
	$(".menu-type").hover(function(){ 
		var _this =$(".nav-dishes-menu"); 
		if(_this.children().length>7){ 
			_this.prev().css("visibility","visible");
			_this.next().css("visibility","visible");
		} 
	},function(){
		var _this =$(".nav-dishes-menu");
		_this.prev().css("visibility","hidden");
		_this.next().css("visibility","hidden");
	} ); 
	/*缩略图*/
	$(".menu-thumb").hover(function(){ 
		var _this = $(".menu-count");
		if( _this.children().not(".hidden").length>8){ 
			_this.prev().css("visibility","visible");
			_this.nextAll(".nav-menu-next").css("visibility","visible");
		} 
	},function(){
		var _this = $(".menu-count");
		_this.prev().css("visibility","hidden");
		_this.nextAll(".nav-menu-next").css("visibility","hidden");
	}); 
	
	/*菜品滚动 初始化*/
	$(".nav-dishes-prev").click(function(){
		if(up_num>=1){	
			$(".nav-dishes-menu").find("li").eq(up_num-1).css("margin-left","0");
			up_num--;
		}
	});
	/*菜品分类*/
	$(".nav-dishes-next").click(function(){
		var count = $(".nav-dishes-menu").find("li").length;
		if(up_num<count-7){
			$(".nav-dishes-menu").find("li").eq(up_num).css("margin-left","-14.2%");
			up_num++;
		}
	});
	/*菜谱缩略图点击*/
	$(".nav-menu-prev").click(function(){
		if(menu_num>=1){	
			$(".menu-count").find("div").not(".hidden").eq(menu_num-1).css("margin-left","0");	
			menu_num--;
			menuNumMap.put(g_typeid, menu_num);
		}
	});
	/*缩略图*/
	$(".nav-menu-next").click(function(){
		var count = $(".menu-count").find("div").not(".hidden").length;
		if(menu_num<count-8){
			$(".menu-count").find("div").not(".hidden").eq(menu_num).css("margin-left","-60px");//55
			menu_num++;
			menuNumMap.put(g_typeid, menu_num);
		}
	});
	/*菜品鼠标滚动*/	
	 var dom =$("#nav-dishes-scroll")[0];
     var user_agent = navigator.userAgent;
     if(user_agent.indexOf("Firefox")!=-1){// Firefox
             dom.addEventListener("DOMMouseScroll",addEvent_,!1);

     } else if(user_agent.indexOf("MSIE")!=-1){// Firefox
              dom.attachEvent("onmousewheel",addEvent_,!1);

     }else{
              dom.addEventListener("mousewheel",addEvent_,!1);

     }
     /*菜谱缩略图滚动*/
      var dom1 =$("#menu-count-scroll")[0];
     if(typeof(dom1) == 'object'){
	     if(user_agent.indexOf("Firefox")!=-1){// Firefox
	             dom1.addEventListener("DOMMouseScroll",addEvent1_,!1);

	     } else if(user_agent.indexOf("MSIE")!=-1){// Firefox
	              dom1.attachEvent("onmousewheel",addEvent1_,!1);

	     }else{
	              dom1.addEventListener("mousewheel",addEvent1_,!1);

	     }
	 }


	/*点击将菜品从本页移除按钮*/
	$(".menu-right-tab3").click(function(){
		var obj = $(this).parent().parent().parent();
		var curr_num = obj.attr("thumb-detail");
		var drop_id = obj.attr("id");
		dataStyle[curr_num].menu_content[drop_id] ={
						menu_content_pos: drop_id,
						menu_content_img: '',
						menu_content_desc: '',
						recommend_img: ''

		};
		
		obj.find(".menu-desc").remove();
		obj.find("img").remove();
		//删除推荐标签
		obj.find("div.recommend_div").remove();
		
		//删除已选择菜品上的标签
		var dishitem = tmpllist[curr_num].detaillist[drop_id];
		removeSelIcon(dishitem.redishid);
		
		//add by lisafan
		delete tmpllist[curr_num].detaillist[drop_id];
	});
	$(".menu-detail-box").dblclick(function(){
		if($(this).attr("isdbclick") == "no"){
			return false;
		}else{
			adjustPic($(this).find(".menu-right-tab2"));
		}
	});
	/*点击调整图片按钮*/
	$(".menu-right-tab2").click(function(){
		adjustPic($(this));
	});
	
	//关闭裁剪图片dialog
	$("#menuImg-adjust-dialog .img-close").click(function(){
		destroyJcrop();
	});
	//关闭裁剪图片dialog
	$("#menuImg-adjust-dialog #cancel-btn").click(function(){
		destroyJcrop();
	});
	//load图片
	var imgLoad = function (src, callback, img$, pobj) {
		var img_= document.createElement('img');
		img_.src=src;
	    if (img_.complete) {
	        callback(img_.width, img_.height, img$, pobj);
	    } else {
	    	img_.onload = function (status) {
	    		console.log(status);
	            callback(img_.width, img_.height, img$, pobj);
	            img_.onload = null;
	        };
	        img_.onerror=function(){
	        	$("#menuadd-prompt-modal #prop-msg").text("图片异常或找不到该图片");
				$("#menuadd-prompt-modal").modal("show");
	        };
	    };
	};
	/**
	 * 计算图片宽高
	 */
	function calculateImg(w, h, img$, pobj){
		var maxW = 700;
		var minW = 360;
		var maxH = 450;
		var minH = 200;
		originalWidth = w;
		originalHeight = h;
		var img_w = w;
		var img_h = h;
//		if(img_w > img_h){
		if(img_w/maxW > img_h/maxH){
			// use width
			if(img_w < minW ){
				img_w = minW;
			}else if(img_w > maxW ){
				img_w= maxW;
			}
			img$.width(img_w);
			var heightV = (h/w)*img_w;
			img$.height(heightV);
			$("#menuImg-adjust-dialog .modal-content").width(img_w+20);
			$("#menuImg-adjust-dialog .modal-content").height(heightV+110);
			
		}else{
			// use height
			if(img_h < minH ){
				img_h = minH;
			}else if(img_h > maxH ){
				img_h= maxH;
			}
			img$.height(img_h);
			var widthV = (w/h)*img_h;
			$("#menuImg-adjust-dialog .modal-dialog").width(widthV+20);
			$("#menuImg-adjust-dialog .modal-content").width(widthV+20);
			$("#menuImg-adjust-dialog .modal-content").height(img_h+110);
			img$.width(widthV);
		}
		var cropW=0,croph=0;
		$('#menuImg-adjust-dialog').on('shown.bs.modal', function () {
			console.log(jcrop_api);
			$("#target-img").Jcrop({
				aspectRatio: pobj.width()/pobj.height(),
				onSelect: updateCoords,
				onChange: function(c){
					cropW = c.w;
					croph = c.h;
				},
				onDblClick: function(c){
					doTail();
				}
			},function(){
				jcrop_api = this;
				jcrop_api.animateTo([100, 100, 400, 300]);
				
				fillVal(100, 100, cropW, croph);
			});
			
			$("#menuImg-adjust-dialog .jcrop-holder").removeClass("hide");
			$(".jcrop-keymgr").css("width", "0px");
			$(".jcrop-keymgr").css("display", "none");
			$(".jcrop-holder").css("margin","0 auto");
		 });
		$("#menuImg-adjust-dialog").modal("show");
	}
	function adjustPic($obj){
		//初始化dialog，清空上一次记录
		$("#menuImg-adjust-dialog .menu-img-adjust").html('<img src="" id="target-img">');
		var pobj= $obj.parent().parent().parent();
		var curr_num = $(pobj).attr("thumb-detail");
		var dropid = $(pobj).attr("id");
		var src = null;
		if(tmpllist[curr_num].detaillist[dropid]!=null && tmpllist[curr_num].detaillist[dropid] != undefined){
			src = tmpllist[curr_num].detaillist[dropid].originalImage;
		}
		if(operate == "edit_menu" && src !=null && src.indexOf("http")<0){
			src = img_Path +"/" + src;
		}
		
		if(src != null && src != ""){
			$("#menuImg-adjust-dialog #thumbnum").val(curr_num);
			$("#menuImg-adjust-dialog #dropid").val(dropid);
			var img$  = $(".menu-img-adjust img");
			img$.attr("src",src);
			img$.removeAttr("style");
			imgLoad(src, calculateImg, img$, pobj);
		}else{
			$("#menuadd-prompt-modal #prop-msg").text("该菜品没有图片");
			$("#menuadd-prompt-modal").modal("show");
		}
	}
	function updateCoords(c){
		fillVal(c.x, c.y, c.w, c.h);
	};
	function fillVal(x, y, w, h){
		var o = $("#target-img");
		var width = $(o).width();
		var height = $(o).height();
		w = (originalWidth*w)/width;
		h = (originalHeight*h)/height;
		x = (originalWidth*x)/width;
		y = (originalHeight*y)/height;
		$('#menuImg-adjust-dialog #x').val(x);
		$('#menuImg-adjust-dialog #y').val(y);
		$('#menuImg-adjust-dialog #w').val(w);
		$('#menuImg-adjust-dialog #h').val(h);
	}
	//点击添加菜谱按钮
	$("#menu-add-btn").click(function(event){
		var len = $(this).parent().find("div").not(".hidden").length;
		if(len>8) len = 8;
		var left = $(".menu-add-type").css("left");
		left = 60*len+27+'px';
		$(".menu-add-type").css("left",left);
		$(".menu-add-type").toggleClass("hidden");
		event.stopPropagation();
	});
	/*点击添加一页按钮*/
	$(".menu-add-type .menu-add-img").mouseover(function(){
		var src = $(this).attr("src");
		if(src.match("active") == null){
			src = src.substr(0,src.lastIndexOf('.'))+'-active.png';
			$(this).attr("src",src);
		}

	});
	$(".menu-add-type .menu-add-img").mouseout(function(){
		var src = $(this).attr("src");
		if(src.match("active")){
			src =  src.substr(0,src.lastIndexOf('-'))+'.png';
			$(this).attr("src",src);
		}

	});
	//添加一页模版
	$(".menu-add-type .menu-add-img").click(function(){
		var num = 0;
		$(".menu-count").find(".menu-count-box").each(function(){
			var temp = $(this).find("img").attr("id").substr(3);
			var src = $(this).find("img").attr("src");
			if(src.match("active")){
				 src = src.substr(0,src.lastIndexOf("-"))+'.png';
				 $(this).find("img").attr("src", src);
				 $(this).find("img").removeClass("active");
			}
			if(parseInt(temp)>parseInt(num)){ num = temp;}

		});
		id = parseInt(num)+1;

		var dishtypeid = $("ul#nav-dishes-scroll li.active").attr("dishtypeid");//菜品分类ID
		var src = $(this).attr("src");
		var detail_id = src.substring(src.lastIndexOf('/')+1,src.lastIndexOf('-'));
		
		//换成图片
		if(detail_id.indexOf("9")<0){
			//若是九版式的话 不用换图片
			src = src.substr(src.lastIndexOf("/")+1, src.length);
			src = global_Path + "/images/menubox/" + src ;
		}
		//版式menu-count-box上添加typeid区分属于哪个分类
		var temp = '<div class="menu-count-box '+dishtypeid+'" dishtypeid = "'+dishtypeid+'"> ';
			temp +='<img src="'+src+'" draggable="true" class="active" target-type="'+g_typeid+'" id="box'+id+'" onmouseover="menuImgActive(this)" onmouseout="menuImgDisable(this)" onclick="menuDetailDis(this)" ondragstart="drag(event)"  ondrop="menuDrop(event,1)" ondragover="allowDrop(event)"></div>'; 
		$(".menu-add").before(temp);	
		$(".menu-add-type").addClass("hidden");	
		//对应的明细调整框架变化
		$(".menu-detail").addClass("hidden");
		$("#"+detail_id).removeClass("hidden");
		$("#"+detail_id).find("div.recommend_div").remove();
		$("#"+detail_id).find("img").not(".show-pic").remove();
		$("#"+detail_id).find("div.menu-desc").remove();
//		$("#"+detail_id).find(".menu-detail-box").css("background",'url("../images/menu-detail-bg.png") no-repeat center center #e6e6e6 ');

		thumb_num = 'box'+id;
		$("#"+detail_id).find("div.menu-detail-box").attr("thumb-detail",thumb_num);
		
		//若是L（第十二版式），第一栏只显示图片，并且默认显示默认图片
		if(T_role_reverse[detail_id] == "L"){
			$("#"+detail_id).find("img.show-pic").attr("src",global_Path+"/images/menu-detail-bg-upload.png");
		}else if(T_role_reverse[detail_id] == "J"){
			$("#"+detail_id).find("img.show-pic").attr("src",global_Path+"/images/menu-detail-bg-upload-ver.png");
		}
		//相当于一次向右滚动操作
		var count = $(".menu-count").find("div").not(".hidden").length;
		if(menu_num < count-8){
			$(".menu-count").find("div").not(".hidden").eq(menu_num).css("margin-left","-60px");//55
			menu_num ++;
			menuNumMap.put(g_typeid, menu_num);
		}
		//end
		
		//存储对应缩略图的响应信息
		dataStyle[thumb_num] = new Object();
		//添加菜品分类
		dataStyle[thumb_num].dishtype = dishtypeid;
		dataStyle[thumb_num].menu_pos = 'box'+id; //缩略图ID，也对应于缩略图的位置
		dataStyle[thumb_num].menu_id = detail_id; //缩略图对应的具体样式
		dataStyle[thumb_num].menu_content = new Object(); //缩略图中的内容详情
		//若是L（第十二版式），第一栏只显示图片，并且默认显示默认图片
		if(T_role_reverse[detail_id] == "L"){
			dataStyle[thumb_num].menu_content["L1"]={menu_content_img: global_Path+"/images/menu-detail-bg-upload.png"};
		}else if(T_role_reverse[detail_id] == "J"){
			dataStyle[thumb_num].menu_content["J1"]={menu_content_img: global_Path+"/images/menu-detail-bg-upload-ver.png"};
		}
		
		//add by lisafan
		tmpllist[thumb_num]= new Object();
		tmpllist[thumb_num].type = T_role_reverse[detail_id]; //缩略图对应的具体样式
		tmpllist[thumb_num].columnid = dishtypeid;
		tmpllist[thumb_num].detaillist = new Object();
		thumSort();
	});
	
	/*点击创建完成按钮*/
	$("#menuAdd-complete-btn").click(function(){
		$("#menuid").val("");
		var f = true;
		//判断是否有菜品选择
		if(isNullObj(tmpllist)){
			f = false;
		}else{
			var boxid = $(".menu-count-box").find("img").eq(0).attr("id");
			var datalist = tmpllist[boxid].detaillist;
			if(isNullObj(datalist)){
				f = false;
			}
		}
		if(f){
			$("#menuAdd-complete-dialog input[name='status'][value=2]").prop("checked",true);
			$("#menuAdd-complete-dialog #effecttime_div").removeClass("hidden");
			clearProForm();
			$("#menuAdd-complete-dialog").modal("show");
		}else{
			$("#menuadd-prompt-modal #prop-msg").text("没有选择任何菜品！");
			$("#menuadd-prompt-modal").modal("show");
		}
	});

	/*菜谱详情图片hover效果*/
	$(".menu-detail-edit").hover(function(){
		$(this).find(".menu-detail-oper").removeClass("hidden");
	},function () {
		$(this).find(".menu-detail-oper").addClass("hidden");
	});
	/*点击更换板式按钮*/
	$(".p-change").click(function(event){
		$(".menu-detail-change").toggleClass("hidden");
		event.stopPropagation();
	});
	$(".menu-detail-change .menu-add-img").mouseover(function(){
		var src = $(this).attr("src");
		if(src.match("active") == null){
		src =  src.substr(0,src.lastIndexOf('.'))+'-active.png';
		$(this).attr("src",src);}

	});
	$(".menu-detail-change .menu-add-img").mouseout(function(){
		var src = $(this).attr("src");
		if(src.match("active")){
			src =  src.substr(0,src.lastIndexOf('-'))+'.png';
			$(this).attr("src",src);
		}

	});
	/*更换版式  对应数据存储也应该更改*/
	$(".menu-detail-change .menu-add-img").click(function(){
		var src = $(this).attr("src");
		var detail_id = src.substring(src.lastIndexOf('/')+1,src.lastIndexOf('-'));
		var obj = $(".menu-detail").not(".hidden");
		var curr_num = obj.find(".menu-detail-box").attr("thumb-detail");
		obj.find("img").not(".show-pic").remove();
		obj.find(".menu-desc").remove();
		$("#"+detail_id).find("img").not(".show-pic").remove();
		$("#"+detail_id).find(".menu-desc").remove();
		var change_src = "../images/menubox/"+detail_id+'-active.png';
		$("#"+curr_num).attr("src",change_src);
		//删除推荐标签
		obj.find("div.recommend_div").remove();
		
		//删除菜品中标签
		$.each(tmpllist[curr_num].detaillist, function(i, detail){
			removeSelIcon(detail.redishid);
		});
		
		dataStyle[curr_num].menu_content = new Object();
		
		//add by lisafan
		tmpllist[curr_num].detaillist = new Object();
	
		$(".menu-detail-change").addClass("hidden");
		$(".menu-detail").addClass("hidden");
		$("#"+detail_id).removeClass("hidden");
		$("#"+detail_id).find(".menu-detail-box").attr("thumb-detail",curr_num);
		dataStyle[curr_num].menu_id = detail_id;
		
		tmpllist[curr_num].type = T_role_reverse[detail_id];

		//判断顺序
		thumSort();
	});
	/**
	 * 循环判断下一个版式
	 */
	function hideNext(curobj){
		var nextobj = null;
		if(curobj.hasClass("menu-count-box")){
			nextobj = curobj.next();
			if(nextobj.hasClass("hidden")){
				nextobj = nextobj.next();
				if(nextobj.hasClass("hidden")){
					nextobj = hideNext(nextobj);
				}
			}
			return nextobj;
		}
		return nextobj;
	}
	
	/**
	 * 循环判断是否存在上一个版式
	 */
	function hidePrev(curobj){
		var prevobj = null;
		if(curobj.hasClass("menu-count-box")){
			prevobj = curobj.prev();
			if(prevobj.hasClass("hidden")){
				prevobj = prevobj.prev();
				if(prevobj.hasClass("hidden")){
					prevobj = hidePrev(prevobj);
				}
			}
			return prevobj;
		}
		return prevobj;
	}
	/**
	 * 判断下一个分类下是否有版式
	 */
	function hasboxNext(curtype){
		var nexttype = curtype.next();
		if(nexttype.parent().hasClass("nav-dishes-menu")){
			var nexttypeid = nexttype.attr("dishtypeid");
			if($("#menu-count-scroll").find(".menu-count-box."+nexttypeid).length <= 0){
				//没有
				nexttype = hasboxNext(nexttype);
			}
		}
		return nexttype;
	}
	/**
	 * 判断上一个分类下是否有版式
	 */
	function hasboxPrev(curtype){
		var prevtype = curtype.prev();
		if(prevtype.parent().hasClass("nav-dishes-menu")){
			var prevtypeid = prevtype.attr("dishtypeid");
			if($("#menu-count-scroll").find(".menu-count-box."+prevtypeid).length <= 0){
				//没有
				prevtype = hasboxPrev(prevtype);
			}
		}
		return prevtype;
	}
	/*点击删除*/
	$(".p-remove").click(function(event){
		event.stopPropagation();
		var obj = $(".menu-detail-edit").find(".menu-detail").not(".hidden");
		var curr_num = obj.find(".menu-detail-box").attr("thumb-detail");	
		$(".menu-detail").addClass("hidden");
		obj.find("img").not(".show-pic").remove();
		obj.find("div.menu-desc").remove();
		
		/**
		 * 当删除一个菜谱版式时，判断逻辑：
		 * 1、先判断当前分类下是否有其他版式:
		 * 	1)若当前分类下有其他版式，则默认显示后一个版式，若当前删除的是最后一个，则显示前一个版式
		 *  2)若当前分类下没有其他菜谱版式，则显示其他分类下的版式：
		 *   默认显示后一个分类下的第一个版式，若当前分类是最后一个分类，则显示前一个分类下的第一个版式
		 */
		
		if($("#menu-count-scroll").find(".menu-count-box."+g_typeid).length > 1){
			//若当前分类下有其他菜谱
			//判断前面是否存在菜谱
			var curobj = $("#"+curr_num).parent();
			var nextobj = oo = hideNext(curobj);
			if(nextobj == null || !nextobj.hasClass("menu-count-box")){
				nextobj = hidePrev(curobj);
			}
			
			if($(nextobj).attr("dishtypeid") == g_typeid){
				$(nextobj).find("img").addClass("active");
				menuDetailDis($(nextobj).find("img"));
			}
			
			//删除菜品中标签
			$.each(tmpllist[curr_num].detaillist, function(i, detail){
				removeSelIcon(detail.redishid);
			});
			//注意，需要对应删除缩略图中的信息
			var _this = $(".menu-count");
			if( _this.children().not(".hidden").length>8){
				$("#"+curr_num).parent().remove();
				if(menu_num>=1){	
					$(".menu-count").find("div").not(".hidden").eq(menu_num-1).css("margin-left","0");	
					menu_num--;
					menuNumMap.put(g_typeid, menu_num);
				}
			}else{
				$("#"+curr_num).parent().remove();
			}
			//清空存储的信息
			delete dataStyle[curr_num];
			delete tmpllist[curr_num];
		}else{
			//若存在，先删除上一个分类下的相关：
			//删除菜品中标签
			$.each(tmpllist[curr_num].detaillist, function(i, detail){
				removeSelIcon(detail.redishid);
			});
			//注意，需要对应删除缩略图中的信息
			var _this = $(".menu-count");
			if( _this.children().not(".hidden").length>8){
				$("#"+curr_num).parent().remove();
				if(menu_num>=1){	
					$(".menu-count").find("div").not(".hidden").eq(menu_num-1).css("margin-left","0");	
					menu_num--;
					menuNumMap.put(g_typeid, menu_num);
				}
			}else{
				$("#"+curr_num).parent().remove();
			}
			//清空存储的信息
			delete dataStyle[curr_num];
			delete tmpllist[curr_num];
			
			
			//当前分类下无其他版式
			var curtype = $("#nav-dishes-scroll").find("li.active");
			var nexttype = hasboxNext(curtype);
			var nexttypeid = nexttype.attr("dishtypeid");
			if($("#menu-count-scroll").find(".menu-count-box."+nexttypeid).length <= 0){
				//取上一个分类
				nexttype = hasboxPrev(curtype);
				nexttypeid = nexttype.attr("dishtypeid");
			}
			if($("#menu-count-scroll").find(".menu-count-box."+nexttypeid).length > 0){
				
				$("#nav-dishes-scroll").find("li").removeClass("active");
				nexttype.addClass("active");

				menu_num = menuNumMap.get(nexttypeid);
		     	initDishes(nexttypeid);
		     	showBox(nexttypeid);
			}
		}
		thumSort();
	});
	/*左侧信息hover效果显示滚动条*/
	$(".menu-left").hover(
		  function () {
		    $(this).css("overflow-y","auto");
		  },
		  function () {
			  $(this).css("overflow-y","hidden");
		  }
	);

	/*菜谱详情查看*/
	/*菜谱详情hover效果*/
	$(".menu-left-desc tr").hover(function(){
		$(this).find("img").toggleClass("hidden");
	});
	/*菜谱详情点击编辑按钮*/
	$(".menu-left-desc img").click(function(){
		clearProForm();
		
		//菜谱属性信息回填
		$("#menuAdd-complete-dialog #addmenu-name").val(currMenu.menuname);
		$("#menuAdd-complete-dialog #effecttime").val(currMenu.effecttime);
		$("#menuAdd-complete-dialog #selectBranchs").val(currMenu.branchids);
		$("#menuAdd-complete-dialog #selectBranchNames").val(currMenu.branchnames);
		showSelectStoreDiv(currMenu.branchnames);
		changeMenuStatus(currMenu.status);
		
		if(currMenu.status == 1){
			$('#menuControl-prompt-modal').modal('show');
			$('#menuControl-ok').click(function() {
				$('#menuControl-prompt-modal').modal('hide');
				$("#menuAdd-complete-dialog").modal("show");
			});
		}else{
			$("#menuAdd-complete-dialog").modal("show");
		}
	});
	/*右键控制*/
	$('.menu-detail-box').contextmenu({
		  before: function (e, element, target) {
		      e.preventDefault();
		      var $o = $(e.target);
		      if(e.target.tagName == 'SPAN'){
		    	  $o = $(e.target).parent().parent().parent();
		      }
		      if(e.target.tagName == 'P'){
		    	  $o = $(e.target).parent().parent();
		      }
		      if(e.target.tagName == 'IMG'){
		    	  $o = $(e.target).parent();
		      }
		      if(e.target.tagName == 'DIV'){
		    	  if($(e.target).hasClass("menu-desc")){
		    		  return false;
		    	  }
		    	  if($(e.target).hasClass("recommend_div")){
		    		  $o = $(e.target).parent();
		    	  }
		      }
		      drop_id = $o.attr("id");
		      thumb_num =  $o.attr("thumb-detail");
		      if(tmpllist[thumb_num].detaillist[drop_id] != null){
		    	  $(".menu-detail-box .open").removeClass("open");
		    	  /*推荐*/
		    	  var recommend = tmpllist[thumb_num].detaillist[drop_id].recommend;
		    	  var contextmenu = $o.attr("data-target");
		    	  if(recommend == 1){
		    		  //1表示推荐；0表示不推荐
		    		  $(contextmenu).find(".menu-right-tab4").text("取消推荐");
		    	  }else{
		    		  $(contextmenu).find(".menu-right-tab4").text("推荐");
		    	  }
		    	  return true;
		      }
		      return false;
		  }
	});
	/*点击推荐或取消推荐*/
	$(".menu-right-tab4").click(function(){
	
		var recommend_img = '';
		var recommend = tmpllist[thumb_num].detaillist[drop_id].recommend;
		if(recommend == 0 || recommend == null){
			tmpllist[thumb_num].detaillist[drop_id].recommend = 1;
			recommend_img = '<div class="recommend_div"><div class="img"></div></div>';
			$(this).parent().parent().before(recommend_img);
		}else{
			tmpllist[thumb_num].detaillist[drop_id].recommend = 0;
			recommend_img = '';
			$(this).parent().parent().parent().find('div.recommend_div').remove();
		}
		dataStyle[thumb_num].menu_content[drop_id].recommend_img = recommend_img;
	});
	/*点击修改菜品属性按钮*/
    $(".menu-right-tab1").click(function(){
    	$("label.error").remove();// 清除之前的验证提未信息。
    	$("input").removeClass('error');
   		 var seldishs = tmpllist[thumb_num].detaillist[drop_id].dishunitlist;
       	 var dishtype = tmpllist[thumb_num].detaillist[drop_id].dishtype;
   		 var htm = '<label class="col-xs-2 control-label"><span class="required-span">*</span>菜品价格：</label>';
   		 if(dishtype == 0 || dishtype == 1){
   			 //鱼锅、菜品
   			 htm +='<div class="col-xs-10 menuAttr-multi-price">';
   		 }
   		 for(var i in seldishs){
       		 var item = seldishs[i];
       		 $("#menuAttr-change-dialog").modal("show");
       		 if(i == 0){
       			 $("#menuAttr-change-dialog #dishid").val(item.dishid);
        	     $("#menuAttr-change-dialog #dishname").val(item.dishname);
        	     $("#menuAttr-change-dialog textarea").val(item.dishintroduction);
       		 }
       		 var keyup = 'onkeyup="this.value= this.value.match(/\\d+(\\.\\d{0,2})?/) ? this.value.match(/\\d+(\\.\\d{0,2})?/)[0] : \'\'"';
       		 var vipprice = "";
       		 if(item.vipprice !=null && item.vipprice!=undefined){
       			 vipprice = item.vipprice;
       		 }
       		 if(dishtype == 2){
	     		 //套餐
	     		htm +='<div class="col-xs-3">'
					+' <input type="text" value="'+item.price+'" class="form-control required" placeholder="最多两位小数的数字" '+keyup+'>'
					+' </div>'
					+' <label class="col-xs-2 control-label">会员价：</label>'
					+' <div class="col-xs-3">'
					+' <input type="text" value="'+vipprice+'" class="form-control" placeholder="最多两位小数的数字" '+keyup+'>'
					+' </div>';
	     	 }else if(dishtype == 1){
	     		 if(i > 0){
	     			//鱼锅
	   				htm +='<div class="form-group"><span class="sp1" title="'+item.dishname+'">'+item.dishname+'</span>'
	       				+ '<span class="sp2">价格：</span>'
	       				+ '<input type="text" value="'+item.price+'" name="price'+i+'" class="form-control valid" required="required" placeholder="最多两位小数的数字" '+keyup+'>'
	       				+ '<span class="sp3">会员价：</span><input type="text" value="'+vipprice+'" class="form-control" placeholder="最多两位小数的数字" '+keyup+'>'
	       				+ '</div>';
	     		 }
	     		 
	     	 }else{
    			htm +='<div class="form-group"><span class="sp1" title="'+item.unit+'">'+item.unit+'</span>'
    				+ '<span class="sp2">价格：</span>'
    				+ '<input type="text" value="'+item.price+'" maxlength="12" class="form-control required" placeholder="最多两位小数的数字" '+keyup+'>'
    				+ '<span class="sp3">会员价：</span><input type="text" value="'+vipprice+'" class="form-control" placeholder="最多两位小数的数字" '+keyup+'>'
    				+ '</div>';
	     	 }
       	 }
   		 if(dishtype == 0 || dishtype == 1){
   			 htm += '</div>';
   		 }
   		 $("#dish_price").html(htm);
   		 $("#menuAttr-change-dialog #count").text(100-document.getElementById("dishintroduction").value.length);
			
	});
	//add by lisafan  编辑菜品属性
	function dishesDetailSave(){
		var currdishlist = tmpllist[thumb_num].detaillist[drop_id].dishunitlist;
		var dishtype = tmpllist[thumb_num].detaillist[drop_id].dishtype;
		for(var i in currdishlist){
			item = currdishlist[i];
			var price = 0.00;
			var vipprice = 0.00;
			if(dishtype == 1){
				if(i == 0){
					item.dishintroduction = $("#menuAttr-change-dialog textarea").val();
				}else{
					price = $("#dish_price .form-group").eq(i-1).find("input").eq(0).val();
					vipprice = $("#dish_price .form-group").eq(i-1).find("input").eq(1).val();
				}
			}else{
				item.dishintroduction = $("#menuAttr-change-dialog textarea").val();
				item.dishname = $("#dishname").val();
				if(dishtype == 2){
					price = $("#dish_price").find("input").eq(0).val();
					vipprice = $("#dish_price").find("input").eq(1).val();
				}else{
					price = $("#dish_price .form-group").eq(i).find("input").eq(0).val();
					vipprice = $("#dish_price .form-group").eq(i).find("input").eq(1).val();
				}
			}
			if(price>0 && price.indexOf(".") < 0){
				price = price+".00";
			}
			if(vipprice>0 && vipprice.indexOf(".") < 0){
				vipprice = vipprice+".00";
			}
			item.price = price;
			item.vipprice = vipprice;
		}
		$("#menuAttr-change-dialog").modal("hide");
		getDishDesc(tmpllist[thumb_num].detaillist[drop_id], $("#"+drop_id));
		//设置菜品属性值（更新dataStyle中的menu_content_desc）
		dataStyle[thumb_num].menu_content[drop_id].menu_content_desc = '<div class="menu-desc">'+$("#"+drop_id).find(".menu-desc").html()+"</div>";
	}
	//菜品信息div鼠标经过显示滚动条
	$(".menu-left").hover(function () { 
		 $(this).css("overflow-y","auto"); 
	}, function () { 
		$(this).css("overflow-y","hidden"); 
	});
	
	//裁剪图片
	$("#adjust-save").click(function(){
		doTail();
	});
	
	//菜品分类鼠标经过事件
	$(".nav-dishes-menu li").hover(function () { 
		 $(this).css("background","#fd7762");
		 $(this).css("color", "#fff");
		 $(this).css("border-radius", "4");
	}, function () { 
		$(this).css("overflow-y","hidden"); 
	});
	
	//查看菜谱时，鼠标放上去显示左右键
	/*
	$(".menu-detail-view").hover(function(event){
		//当模版大于一个时，显示左右箭头
		if($(this).find(".menu-detail").length > 1){
			isShowPrevNext();
		}
	}, function(){
		$(".menu-detail-prev").addClass("hidden");
		$(".menu-detail-next").addClass("hidden");
	});
	*/
	//暂时应该没用 start
	$(".menu-detail-prev").click(function(){
		var obj = $(".menu-detail-view").find(".menu-detail").not(".hidden");
		if(obj.prev().hasClass("menu-detail")){
			$(".menu-detail-view").find(".menu-detail").addClass("hidden");
			obj.prev().removeClass("hidden");
		}		
	});
	$(".menu-detail-next").click(function(){
		var obj = $(".menu-detail-view").find(".menu-detail").not(".hidden");
		if(obj.next().hasClass("menu-detail")){
			$(".menu-detail-view").find(".menu-detail").addClass("hidden");
			obj.next().removeClass("hidden");
		}
	});
	//暂时应该没用 end	
});
/**
 * 销毁jcrop
 */
function destroyJcrop(){
	jcrop_api.destroy();
}
/**
 * 鼠标滑过menu-detail-view div 左右半部分分别显示左右箭头
 * @param oDiv
 * @param event
 */
function displayCoord(oDiv, event){
	if($(oDiv).find(".menu-detail").length > 1){
		var left = $(oDiv).offset().left;
		var w = $(oDiv).width();
		var s = event.clientX - left + document.documentElement.scrollLeft;
		if(s/w < 0.5){
			$(".menu-detail-prev").removeClass("hidden");
			$(".menu-detail-next").addClass("hidden");
		}else{
			$(".menu-detail-next").removeClass("hidden");
			$(".menu-detail-prev").addClass("hidden");
		}
	}
} 
/**
 * 判断上一页下一页按钮是否显示
 */
var intervalP = null;
function isShowPrevNext(){
	clearIntervalProcess(intervalP);
	if($(".current_page").text() == 1){
		$(".menu-detail-prev").attr("title", "第一页");
		intervalP = setInterval(function(){
			$(".menu-detail-prev").attr("title", "");
		}, 1000);
	}else{
//		$(".menu-detail-prev").attr("title", "");
//		$(".menu-detail-prev").removeClass("hidden");
	}
	if($(".current_page").text() == $(".total_page").text()){
		$(".menu-detail-next").attr("title", "最后一页");
		intervalP = setInterval(function(){
			$(".menu-detail-next").attr("title", "");
		}, 2000);
	}else{
//		$(".menu-detail-next").attr("title", "");
//		$(".menu-detail-next").removeClass("hidden");
	}
}
/**
 * 鼠标经过菜品分类事件
 */
function liHover(){
	$(".nav-dishes-menu li").not(".active").hover(function () { 
		 $(this).css("background","#fd7762");
		 $(this).css("color", "#fff");
		 $(this).css("border-radius", "4px");
    }, function () { 
	   	 $(this).css("background","");
	   	 $(this).css("color", "");
    });
}
/**
 * 菜品分类排序
 */
function columnSort(){
	columnSortMap = new HashMap();
	$("ul#nav-dishes-scroll li").each(function(i){
		var keyid = $(this).attr("dishtypeid");
		columnSortMap.put(keyid, i);
	});
}
/**
 * 板式排序
 */
function thumSort(){
	$("#menu-count-scroll .menu-count-box").not(".hidden").each(function(i){
		var thumb_num = $(this).find("img").attr("id");
		tmpllist[thumb_num].sort = i;
	});
}
/**
 * 裁剪图片动作及裁剪后操作
 */
function doTail(){
	var thumbnum = $("#menuImg-adjust-dialog #thumbnum").val();
	var dropid = $("#menuImg-adjust-dialog #dropid").val();
	doAdjust(2, null, function(json){
		var imgsrc = json.image;
		if(checkSrc(imgsrc)){
			var src = img_Path + imgsrc;
			
			$("#"+dropid).find("img").attr("src", src);
			$("#menuImg-adjust-dialog").modal("hide");
			
			$imgObj = $("#"+dropid).find("img");
			var temp_img = '<img dishid="'+$imgObj.attr("dishid")+'" value="'+$imgObj.attr("value")+'" text="'+$imgObj.attr("text")+'" src="'+src+'">';
			tmpllist[thumbnum].detaillist[dropid].image = imgsrc;
			dataStyle[thumbnum].menu_content[dropid].menu_content_img = temp_img;
		}else{
			$("#menuImg-adjust-dialog").modal("hide");
			alert("裁剪图片失败！请重试..");
		}
		destroyJcrop();//销毁jcrop
	});
}

/**
 * 图片拖拽压缩或裁剪 api调用
 * @param f(1:压缩；2：裁剪)
 * @param param 参数
 * @param callback 回调函数
 */
function doAdjust(f, param, callback){
	var option = {
			image: "",
			oldimage: "",
			flag: f,
			type: "",
			x: "",
			y: "",
			h: "",
			w: ""
	};
	if(f == 1){
		option.image = cutPath(param.src);
		option.oldimage = cutPath(param.oldimage);
		option.type = param.type;
	}else{
		option.image = cutPath($("#target-img").attr("src"));
		option.x = $('#menuImg-adjust-dialog #x').val();
		option.y = $('#menuImg-adjust-dialog #y').val();
		option.h = $('#menuImg-adjust-dialog #h').val();
		option.w = $('#menuImg-adjust-dialog #w').val();
	}
	$.ajax({
		url : global_Path + "/menu/adjustpic.json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(option),
		success : function(json) {
			var jsontype = typeof(json);
			if(jsontype.toUpperCase() == "STRING"){
				json = $.parseJSON(json);
			}
			callback(json);
		}
	},'json');
}
/**
 * 设置高亮显示并选中  修改菜谱的时候使用
 * @param typeid
 */
function activeType(typeid){
	$("#nav-dishes-scroll li").removeClass("active");
	$("#nav-dishes-scroll li").each(function(n, obj){
		if(typeid == $(this).attr("dishtypeid")){
			$(this).addClass("active");
			if(n >= 7){
				for(var i=0;i<= n-7;i++){
					$(".nav-dishes-menu").find("li").eq(up_num).css("margin-left","-14.2%");//-14%
					up_num ++;
				}
			}
			return;
		}
	});
}
/**
 * 设置高亮显示并选中 查看菜谱的时候使用
 * @param typeid
 */
function activeTypeForView(typeid, pageTo){
	var currTypeid = $("#nav-dishes-scroll li.active").attr("dishtypeid");
	if(currTypeid != typeid){
		$("#nav-dishes-scroll li").removeClass("active");
		$("#nav-dishes-scroll li").each(function(n, obj){
			if(typeid == $(this).attr("dishtypeid")){
				$(this).addClass("active");
				if(pageTo == "N"){
					if(n >= 7){
						$(".nav-dishes-menu").find("li").eq(up_num).css("margin-left","-14.2%");
						up_num ++;
					}
				}else if(pageTo == "P"){
					if(up_num >= 1){
			 			$(".nav-dishes-menu").find("li").eq(up_num-1).css("margin-left","0");
			 			up_num--;
			 		}
				}
				
				return;
			}
		});
	}
}

/**
 * 查看菜谱信息
 * @param menuid
 */
var pnum = 1;
function getMenuById(menuid){
	$.getJSON(global_Path+"/menu/getMenuById/"+menuid+".json", function(json){
		modmenu = json;
		var menu = json.menu;
		$(".menuname_span").text(menu.menuname);
		var status = menu.status;
		$(".effecttime_span").attr("status", status);
		var effecttime = "";
		if(status == 1){
			$(".effecttime_span").text("已启用");
		}else if(status == 2){
			effecttime = menu.effecttime;
			if(effecttime != null && effecttime != ""){
				effecttime = effecttime.substring(0, menu.effecttime.length-2);
				$(".effecttime_span").text(effecttime);
			}
		}else{
			$(".effecttime_span").text("未启用");
		}
		//选择门店
		var branchids = "";
		var branchnames = "";
		if(menu.branchMap != null && menu.branchMap.length>0){
			$.each(menu.branchMap, function(i, branch){
				branchids += branch.branchid+",";
				branchnames += branch.branchname +",";
				$("#all_sel_branchname ul").append("<li>"+branch.branchname+"</li>");
			});
			branchids = branchids.substring(0, branchids.length-1);
			branchnames = branchnames.substring(0, branchnames.length-1);
		}
		
		var sub_branchname = branchnames;
		if(branchnames.length > 8){
			sub_branchname = branchnames.substring(0, 8);
			$(".branch_span").text(sub_branchname+"......("+menu.branchMap.length+")");
			$(".branch_span").attr("title", branchnames);
		}else{
			$(".branch_span").text(sub_branchname+"("+menu.branchMap.length+")");
		}
		
		//保存当前菜谱属性，属性页面回填使用
		currMenu.menuname = menu.menuname;
		currMenu.effecttime = effecttime;
		currMenu.branchids = branchids;
		currMenu.branchnames = branchnames;
		currMenu.status = menu.status;
		
		//模板
		showAllColumn(json.templatelist);
		showAllTmpl(json.templatelist);
		/*菜品分类点击*/
	     $("ul.nav-dishes-menu li").click(function(){
	     	$(this).parent().find("li").removeClass("active");
	     	$(this).addClass("active");
	     	var typeid = $(this).attr("dishtypeid");
	     	changeTmplByType(typeid);
	     });
	     liHover();
	     /*菜品滚动 查看页面*/
	 	$(".nav-dishes-prev").click(function(){
	 		if(up_num>=1){
	 			$(".nav-dishes-menu").find("li").eq(up_num-1).css("margin-left","0");
	 			up_num--;
	 		}
	 	});
	 	/*菜品分类*/
	 	$(".nav-dishes-next").click(function(){
	 		var count = $(".nav-dishes-menu").find("li").length;
			if(up_num<count-7){
				$(".nav-dishes-menu").find("li").eq(up_num).css("margin-left","-14.2%");
				up_num++;
			}
	 	});
	 	
	     //查看页面 上一页
		$(".menu-detail-prev").click(function(){
			var obj = $(".menu-detail-view").find(".menu-detail").not(".hidden");
			if(obj.prev().hasClass("menu-detail")){
				$(".menu-detail-view").find(".menu-detail").addClass("hidden");
				obj.prev().removeClass("hidden");
				var target_type = obj.prev().attr("target-type");
				activeTypeForView(target_type, "P");
			}
			$(".menu-detail").each(function(){
				if(!$(this).hasClass("hidden")){
					$(".current_page").text($(this).attr("pnum"));
					return;
				}
			});
			isShowPrevNext();
		});
		//查看页面 下一页
		$(".menu-detail-next").click(function(){
			var obj = $(".menu-detail-view").find(".menu-detail").not(".hidden");
			if(obj.next().hasClass("menu-detail")){
				$(".menu-detail-view").find(".menu-detail").addClass("hidden");
				obj.next().removeClass("hidden");
				var target_type = obj.next().attr("target-type");
				activeTypeForView(target_type, "N");
			}
			$(".menu-detail").each(function(){
				if(!$(this).hasClass("hidden")){
					$(".current_page").text($(this).attr("pnum"));
					return;
				}
			});
			isShowPrevNext();
		});
	});
}
/**
 * 点击分类变化菜谱板式 查看菜谱时使用
 * @param typeid
 */
function changeTmplByType(typeid){
	g_typeid = typeid;
	$("#menu-detail-view .menu-detail ").addClass("hidden");
	$("#menu-detail-view .menu-detail ").each(function(i, item){
		if($(this).attr("target-type") == typeid){
			$(this).removeClass("hidden");
			$(".current_page").text($(this).attr("pnum"));
			return false;
		}
	});
}
/**
 * 获取所有分类 并且排序
 * @param templatelist
 */
function showAllColumn(templatelist){
	columnTmplMap = new HashMap();
	columnObjMap = new HashMap();
	
	$.each(templatelist, function(index, tmpl){
		var columnsort = tmpl.columnsort;
		var list = [];
		if(columnTmplMap.containsKey(columnsort)){
			list = columnTmplMap.get(columnsort);
		}
		list.push(tmpl);
		columnTmplMap.put(columnsort, list);
		
		if(!columnObjMap.containsKey(columnsort)){
			columnObjMap.put(columnsort, {columnid: tmpl.columnid, itemDesc: tmpl.itemDesc, columnsort: columnsort});
		}
		
		//编辑菜谱的时候，获取所有菜品id
		var dishListMap = new HashMap();
		if(columnIdMap.containsKey(tmpl.columnid)){
			dishListMap = columnIdMap.get(tmpl.columnid);
		}
		$.each(tmpl.detaillist, function(i, detail){
			var dishid = detail.redishid;
			var num = 0;
			if(dishListMap.containsKey(dishid)){
				num = dishListMap.get(dishid);
			}
			num ++;
			dishListMap.put(detail.redishid, num);
		});
		columnIdMap.put(tmpl.columnid, dishListMap);
	});
}
/**
 * 数组按数值排序
 * @param a
 * @param b
 * @returns {Number}
 */
function sortNumber(a,b) { 
	return a - b;
} 
/**
 * 分类对应菜谱版式
 * @param templatelist
 * @param columnTmplMap
 * @param columnObjMap
 */
function showAllTmpl(templatelist){
	$("#menu-detail-view").append('<div class="menu-detail-prev hidden"><img src="../images/menu-view-prev.png"></div>');
	$(".total_page").text(templatelist.length);
	$(".current_page").text(1);
	
	var columnsortArr = columnTmplMap.keySet();
	columnsortArr.sort(sortNumber);
	$.each(columnsortArr, function(i, columnsort){
		var columnObj = columnObjMap.get(columnsort);
		var cla = "";
		if(i == 0){//columnsort == 0
			g_typeid = columnid;
			cla = "active";
		}
		var columnid = columnObj.columnid;
		var columndesc = columnObj.itemDesc;
		
		var ulhtm = '<li class="'
			+ cla
			+ '" dishtypeid="'
			+ columnid
			+ '" draggable="true" ondragstart="drag(event)" ondrop="menuDrop(event,0)" ondragover="allowDrop(event)" id="button'
			+ i + '">' + columndesc + '</li>';
		$("#nav-dishes-scroll").append(ulhtm);
		
		var column_tmpl_list = columnTmplMap.get(columnsort);
		$.each(column_tmpl_list, function(j, tmpl){
			//菜谱版式
			var hide = "";
			if(pnum > 1){
				hide = "hidden";
			}
			var tmplHtm = '';
			var box = "";
			var menu_detail_id = T_role[tmpl.type];
			if(menu_detail_id != null){
				tmplHtm = '<div class="menu-detail '+hide+' '+menu_detail_id+'" id="'+menu_detail_id+'-'+columnid+'-'+j+'" pnum="'+pnum+'" target-type="'+columnid+'">';
				box = $("#hide_tmpl #"+ menu_detail_id).html();
				tmplHtm += box + '</div>';
			}
			$("#menu-detail-view").append(tmplHtm);
			$.each(tmpl.detaillist, function(i, detail){
				var $obj = $("#"+menu_detail_id+"-"+columnid+"-"+j+" #"+detail.location);
				getDishDesc(detail, $obj, true);
			});
			pnum ++;
		});
	});
	
	//门店菜谱功能（点击查看菜品详情）
	if($("#only_show").val() == "true"){
		$(".menu-detail-box").click(function(){
			var dishid = $(this).find("#menu_dishid").val();
			editMenuView(dishid);
		});
	}
	
	$("#menu-detail-view").append('<div class="menu-detail-next hidden"><img src="../images/menu-view-next.png"></div>');
}
/**
 * 显示版式上的菜品信息（编辑菜品属性、查看菜谱显示菜品的时候使用）
 * @param detail
 * @param $obj
 * @param f（=true的时候为查看菜谱）
 */
function getDishDesc(detail, $obj, f){
	//只返回图片的情况 如第十二版式
	if(detail.dishunitlist == null || detail.dishunitlist == undefined || detail.redishid == "TEMPLATE-IMAGE"){
		$obj.find("img.show-pic").attr("src", replaceEscape(img_Path+detail.image));
	}else{
		var img = detail.image;
		if(f){
			if(checkSrc(img)){
				$obj.find("img").attr("src", replaceEscape(img_Path + img));
			}
		}
		var dish_desc = $obj.find(".menu-desc");
		var dishtype = detail.dishtype;
		var level = detail.level;
		if(dishtype == 1){
			//鱼锅，查询关联鱼锅名称
			var desc = '<input type="hidden" id="menu_dishid" value="'+detail.redishid+'"/>';
			if(f){
				//鱼锅名称
				desc += detail.dishunitlist[0].dishname;
				if(level == 1){
					//双拼锅
				}else{
					//非 双拼锅
					$.each(detail.fishpotlist, function(i, item){
						desc += "<p><span>"+item.dishname+"</span>";
						var va = item.price+"元/"+item.unit;
						if(item.vipprice!=null && item.vipprice!=""){
							va += "(会员价："+item.vipprice+"元/"+item.unit+")";
						}
						desc += "<span>"+va+"</span></p>";
					});
				}
				
			}else{
				$.each(detail.dishunitlist, function(i, item){
					if(i != 0){
						desc += "<p><span>"+item.dishname+"</span>";
						var va = item.price+"元/"+item.unit;
						if(item.vipprice!=null && item.vipprice!=""){
							va += "(会员价："+item.vipprice+"元/"+item.unit+")";
						}
						desc += "<span>"+va+"</span></p>";
					}else{
						//鱼锅名称
						desc += "<p><span>"+item.dishname+"</span></p>";
					}
				});
			}
			$(dish_desc).html(desc);
		}else{
			var desc = '<input type="hidden" id="menu_dishid" value="'+detail.redishid+'"/>';
			$.each(detail.dishunitlist, function(i, item){
				var unit = item.unit;
				if(dishtype == 2){
					if(unit == null || unit == undefined){
						unit = "套";
					}
				}
				var price = item.price+"元/"+unit;
				if(item.vipprice != null && item.vipprice != "" && item.vipprice != undefined){
					price += "(会员价："+item.vipprice+"元/"+unit+")";
				}
				if(i == 0){
					desc += "<p><span>"+item.dishname+"</span></p>";
					desc += "<p><span>"+price+"</span></p>";
				}else if(i == 1){
					desc += "<p><span>"+price+"</span></p>";
				}
			});
			$(dish_desc).html(desc);
		}
	}
}
/**
 * add by fandan
 * @param id
 * @returns {Array}
 */
function getfishpotDetail(id){
	var list=[];
	$.ajax({
		url : global_Path + "/combodish/findfishpotDetailById/"+id+".json",
		type : "post",
		async : false,
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			$.each(result,function(index,item){
				var groupDetai={};
				 groupDetai.contactdishid=item.contactdishid;
				 groupDetai.contactdishname=item.contactdishname;
				 groupDetai.dishunitid=item.dishunitid;
				 groupDetai.unitflag=item.unitflag;
				 groupDetai.dishtype=item.dishtype;
				 groupDetai.price=item.price;
				 groupDetai.vipprice=item.vipprice;
				 groupDetai.dishnum=1;
				 groupDetai.status=0;
				 list.push(groupDetai);
			});
		}});
	return list;
}
/**
 * 创建、编辑菜谱
 * 
 * 初始化菜品类别
 * @param menuid
 */
function initDisheType(menuid){
	//解析获取到的菜品/菜品分类，然后显示在 弹出层中。
	columnSortMap = new HashMap();
	var ulhtm="";
	if(columnObjMap != null){
		var columnsortArr = columnTmplMap.keySet();
		columnsortArr.sort(sortNumber);
		$.each(columnsortArr, function(i, columnsort){
			var column = columnObjMap.get(columnsort);
			var cla = "";
			if(i == 0){
				g_typeid = column.columnid;
				cla = "active";
			}
			ulhtm += '<li class="'+cla+'" dishtypeid="'+column.columnid+'" draggable="true" ondragstart="drag(event)" ondrop="menuDrop(event,0)" ondragover="allowDrop(event)" id="button'+i+'">'+column.itemDesc+'</li>';
			columnSortMap.put(column.columnid, columnsort);
		});
	}
	
	$.getJSON(global_Path+"/dish/getTypeAndDishMap.json", function(json){
		var tmpJson={};
		$.each(json, function(index, item) {
			$.each(item, function(key, list) {
				tmpJson=JSON.parse(key);
				var cla = "";
				if(index == 0){
					if(columnObjMap == null){
						g_typeid = tmpJson.id;
						cla = "active";
					}
				}
				if(columnIdMap != null && columnIdMap.containsKey(tmpJson.id)){
					return;
				}else{
					columnSortMap.put(tmpJson.id, index);
					ulhtm += '<li class="'+cla+'" dishtypeid="'+tmpJson.id+'" draggable="true" ondragstart="drag(event)" ondrop="menuDrop(event,0)" ondragover="allowDrop(event)" id="button'+index+'">'+tmpJson.itemdesc+'</li>';
				}
			});
		});
		$("#nav-dishes-scroll").html(ulhtm);
		initDishes(g_typeid);
		/*菜品分类点击*/
	     $("ul.nav-dishes-menu li").click(function(){
	     	$(this).parent().find("li").removeClass("active");
	     	$(this).addClass("active");
	     	$(".menu-detail-edit .menu-detail").addClass("hidden");
	     	var typeid = $(this).attr("dishtypeid");
	     	menu_num = menuNumMap.get(typeid);
	     	initDishes(typeid);
	     	showBox(typeid);
	     });
	     
	     liHover();
	});
}

/**
 * 点击菜品分类显示缩略图
 * @param typeid
 */
function showBox(typeid){
	$("#menu-count-scroll .menu-count-box").addClass("hidden");
	$("#menu-count-scroll .menu-count-box img").removeClass("active");
	var obj = null;
	var num = 0;
	$("#menu-count-scroll .menu-count-box").each(function(i, box){
		if($(this).attr("dishtypeid") == typeid){
			obj = $(this).find("img");
			if(num == 0){
				$(obj).addClass("active");
				menuDetailDis($(obj));
			}
			num ++;
			$(this).removeClass("hidden");
		}
	});
}
/**
 * 修改页面信息
 * add by fandan
 * @param menuid
 */
function initEditTmpl(menuid){
	$.getJSON(global_Path+"/menu/getMenuById/"+menuid+".json", function(json){
		console.log(json);
		showAllColumn(json.templatelist);
		initDisheType(menuid);
		
		menuobj = json.menu;
		menubrachobj["branch"] = json.menuBranchlist;
		var htm = '';
		$.each(json.templatelist, function(index, tmpl){
			var cla = "";
			var img = "";
			if(index == 0){
				cla = "active";
				img = T_role[tmpl.type]+'-active.png';
			}else{
				img = T_role[tmpl.type]+'.png';
			}
			//若是第九板式或是第十二板式，显示无缩略图
			var imgSrc = '../images/menubox/' + img;
			if(T_role[tmpl.type] == "menu-detail9" || T_role[tmpl.type] == "menu-detail12"){
				imgSrc = '../images/' + img;
			}
			//版式menu-count-box上添加typeid区分属于哪个分类
			htm += '<div class="menu-count-box '+tmpl.columnid+'" dishtypeid="'+tmpl.columnid+'">'
				+ '<img src="' + imgSrc + '" draggable="true" class="'+cla+'" id="box'+(index+1)+'" onmouseover="menuImgActive(this)" onmouseout="menuImgDisable(this)" onclick="menuDetailDis(this)" ondragstart="drag(event)" ondrop="menuDrop(event,1)" ondragover="allowDrop(event)">'
				+ '</div>';
			thumb_num = "box"+(index+1);
			$("#"+T_role[tmpl.type]).find("div.menu-detail-box").attr("thumb-detail",thumb_num);
			dataStyle[thumb_num] = new Object();
			dataStyle[thumb_num].dishtype = tmpl.columnid;
			dataStyle[thumb_num].menu_pos = thumb_num;
			dataStyle[thumb_num].menu_id = T_role[tmpl.type];
			dataStyle[thumb_num].menu_content = new Object();
			
			tmpllist[thumb_num]= new Object();
			tmpllist[thumb_num].type = tmpl.type; //缩略图对应的具体样式
			tmpllist[thumb_num].columnid = tmpl.columnid;//菜品分类ID
			tmpllist[thumb_num].sort = tmpl.sort;//菜品分类中菜谱版式排序
			tmpllist[thumb_num].detaillist = new Object();
			$.each(tmpl.detaillist, function(i, detail){
				var dishname = "";
				value = "";
				var dishtype = detail.dishtype;
				//只返回图片的情况 如第十二版式
				if(detail.dishunitlist == null || detail.dishunitlist == undefined || detail.redishid == "TEMPLATE-IMAGE"){
					$("#"+detail.location).find("img").attr("src", detail.image);
					dataStyle[thumb_num].menu_content[detail.location] = {
							menu_content_img: detail.image==null?global_Path+"/images/menu-detail-bg-upload.png":replaceEscape(img_Path + detail.image)
					};
					// TODO
					tmpllist[thumb_num].detaillist[detail.location] = {
							location: detail.location,
							image: detail.image,
							originalImage: detail.originalImage
					};
				}else{
					var temp ='<div class="menu-desc">';
					if(dishtype == 1){
						$.each(detail.dishunitlist, function(j, item){
							if(j != 0){
								var va = item.price+"元/"+item.unit;
								if(item.vipprice != null && item.vipprice != ""){
									va += "(会员价："+item.vipprice+"元/"+item.unit+")";
								}
								temp += '<p><span>'+item.dishname+'</span><span >'+va+'</span></p>';
							}else{
								//鱼锅名称
								temp += '<p><span>'+item.dishname+'</span></p>';
							}
						});
					}else{
						$.each(detail.dishunitlist, function(j, item){
							if(j == 0){
								dishname = item.dishname;
								temp += '<p><span>'+dishname+'</span></p>';
							}
							var va = item.price+"元";
							if(item.unit!=null && item.unit != undefined)
								va += "/"+item.unit;
							if(item.vipprice!=null && item.vipprice!=undefined){
								va += "(会员价："+item.vipprice+"元/"+item.unit+")";
							}else{
								detail.dishunitlist[j].vipprice = "";
							}
							value += va;
							temp += '<p><span >'+va+'</span></p>';
						});
					}
					temp += "</div>";
				
					var obj = $("#"+detail.location);
					var hide = '';
					if(tmpl.type == "I" || tmpl.type == "L" || tmpl.type == "J"){
						hide = 'style="display: none"';
						obj.css("background","#e6e6e6");
					}else{
						hide = 'style="display: block"';
					}
					var imgsrc = "";
					if(checkSrc(detail.image)){
						imgsrc = replaceEscape(img_Path + detail.image);
					}
					var temp_img = '<img dishid="'+detail.redishid+'" value="'+value+'" text="'+dishname+'" src="'+imgsrc+'" '+hide+'>';
					var recommend_img = '';
					var width = obj.width();
					var height = obj.height();
					if(detail.recommend == 1){
						recommend_img = '<div class="recommend_div"><div class="img"></div></div>';
					}
					dataStyle[thumb_num].menu_content[detail.location] = {
							menu_content_pos: detail.location,
							menu_content_img:temp_img,
							recommend_img:recommend_img,
							menu_content_imgW:width,
							menu_content_imgH:height,
							menu_content_desc:temp
					};
					tmpllist[thumb_num].detaillist[detail.location] = detail;
					tmpllist[thumb_num].detaillist[detail.location].dishtype = dishtype;
				}
			});//end detaillist
		});
		$("#menu-count-scroll #menu-add-btn").before(htm);//showbox
		activeType(g_typeid);
		menuDetailDis($(".menu-count-box img.active"));
		showBox(g_typeid);
		$("#dishintroduction").on('keyup', function() {
	 	    var len = this.value.length;
	 	     $("#count").html(100-len);
	 	});
		
		 /*菜品滚动  修改页面*/
	 	$(".nav-dishes-prev").click(function(){
	 		if(up_num>=1){
	 			$(".nav-dishes-menu").find("li").eq(up_num-1).css("margin-left","0");
	 			up_num--;
	 		}
	 	});
	 	/*菜品分类*/
	 	$(".nav-dishes-next").click(function(){
	 		var count = $(".nav-dishes-menu").find("li").length;
			if(up_num<count-7){
				$(".nav-dishes-menu").find("li").eq(up_num).css("margin-left","-14.2%");
				up_num++;
			}
	 	});
	});
}
/**
 * 加载菜品分类下的菜品
 * @param typeid
 */
function initDishes(typeid){
	g_typeid = typeid;
	map = new HashMap();
	var trhtm = "";
	var dishListMap = columnIdMap.get(typeid);
	$.get(global_Path + "/dish/getDishesByDishType/"+typeid+".json", function(json){
		var cou = 0;
		$.each(json, function(index, item) {
			map.put(item.dishid, item);
			cou ++;
			var dishid = item.dishid;
			var title = item.title;
			var img = item.image;
			if(img != null && img != ""){
				img = img_Path+"/"+img;
			}
			var flag = false;
			var num = 0;
			if(dishListMap != null && dishListMap.size() > 0){
				if(dishListMap.containsKey(dishid)){
					num = dishListMap.get(dishid);
					if(num>0)
						flag = true;
				}
			}
			var imghtm = '';
			if(flag){
				imghtm = '<img class="seled-dish-icon" src="../images/sel-icon.png" />';
			}
			var td = '<td draggable="true" drag-num="'+num+'" ondragstart="drag(event)" value="" src="'+img+'" id="'+dishid+'">'
				+ title
				+ imghtm
				+'</td>';
			if(cou == 1){
				trhtm += '<tr>'+td;
			}else{
				trhtm += td+'</tr>';
				cou = 0;
			}
		});
		$("#dishes_tb").html(trhtm);
	});
	
}

function allowDrop(ev)
{
	ev.preventDefault();
}

function drag(ev)
{
	ev.dataTransfer.setData("Text",ev.target.id);
	ev.stopPropagation();
}
function stopDefault(e) {
	if (e && e.preventDefault)
		e.preventDefault();
	else
		window.event.returnValue = false;
	return false;
}

/* 分类拖动 */
function menuDrop(ev,flag){	
	ev.stopPropagation();
//	ev.preventDefault();
	stopDefault(ev);
	var data=ev.dataTransfer.getData("Text");
	var drag_text=null; 
	var drag_num=0;
	var drop_text=null;
	var drop_num=0;
	if(flag =='0'){
		//菜品分类拖动
		drag_text = $("#"+data);
		drag_num =drag_text.prevAll().length;
		drop_text = $("#"+ev.target.id);
		drop_num = drop_text.prevAll().length;
		if(drag_num > drop_num){
			drop_text.before(drag_text);
		}else{
			drop_text.after(drag_text);
		}
		columnSort();
		ev.stopPropagation();
	}else if(flag =='1'){//缩略图相互拖动
		drag_text = $("#"+data).parent();
		drag_num =drag_text.prevAll().length;
		drop_text = $("#"+ev.target.id).parent();
		drop_num = drop_text.prevAll().length;
		if(drag_num > drop_num){
			drop_text.before(drag_text);
		}else{
			drop_text.after(drag_text);
		}
		thumSort();
	}else if(flag == '2')//从菜品拖动到布局中
	{
		var src = $("#"+data).attr("src");
		var manyTimes = false;
		if(ev.target.id == '')//多次拖曳的情况
		{
			manyTimes = true;
			ev.target.id = ev.currentTarget.id;
		}
		drop_id = ev.target.id;
		var obj = $("#"+ev.target.id);
		//删除推荐标签
		obj.find("div.recommend_div").remove();
		if(src == null || src == ""){
			showDishDesc(obj, data, src, src, manyTimes);
		}else{
			var type = 1;
			if (drop_id == "A1") {
				type = 1;
			} else if (drop_id == "B1" || drop_id == "B2"
					|| drop_id == "C1" || drop_id == "D3") {
				type = 2;
			} else if (drop_id == "C2" || drop_id == "C3"
					|| drop_id == "D1" || drop_id == "D2"
					|| drop_id == "C2" || drop_id.indexOf("E") >= 0
					|| drop_id == "F1" || drop_id == "F2"
					|| drop_id == "F5" || drop_id == "G1"
					|| drop_id == "G2" || drop_id == "G3") {
				type = 3;
			} else if (drop_id.indexOf("H") >= 0 || drop_id == "F3"
					|| drop_id == "F4" || drop_id == "G4"
					|| drop_id == "G5") {
				type = 4;
			}
			doAdjust(1,{
				src: src,
				oldimage: obj.find("img").attr("src"),//被替换的菜品图片
				type: type
			},function(json){
				console.log(json.image);
				showDishDesc(obj, data, json.image, src, manyTimes);
			});
		}
	}
}
/**
 * 在图片上显示菜品信息（将菜品拖入版式的时候使用）
 * @param obj
 * @param data
 * @param menu_detail_box
 * @param src
 */
function showDishDesc(obj, data, src, oldsrc, manyTimes){
	var curr_num = obj.attr("thumb-detail");
	var text = $("#"+data).text();
	var dishid = $("#"+data).attr("id");
	if(manyTimes){
		//多次拖拽
		var olddishid = tmpllist[curr_num].detaillist[drop_id].redishid;
		//多次拖动菜品进同一个板式的时候，删除上一次拖入的菜品标签
		removeSelIcon(olddishid);
	}
	var show_src = '';
	if(checkSrc(src)){
		show_src = replaceEscape(img_Path + src);
	}
	
	obj.find("img").remove();
	obj.find("div.menu-desc").remove();
	var hide = '';
	if(drop_id.indexOf("I") < 0 && drop_id.indexOf("L")<0  && drop_id.indexOf("J")<0){
		hide = 'style="display: block;"';
	}else{
		hide = 'style="display: none;"';
	}
	var temp_img = '<img dishid="'+dishid+'" value="'+$("#"+data).attr("value")+'" text="'+text+'" src="'+show_src+'" '+hide+'>';
	obj.append(temp_img);
	
	if(drop_id.indexOf("I") < 0 && drop_id.indexOf("L") < 0 && drop_id.indexOf("J") < 0){//样式九和样式十二的图片不用放入
	}else{
		obj.css("background","#e6e6e6");
	}
	var width = obj.width();
	var height = obj.height();

	//add by lisafan
	var dish = map.get(dishid);
	var dishtype = dish.dishtype;
	tmpllist[curr_num].detaillist[drop_id] = {
			location: drop_id,
			redishid: dish.dishid,
			originalImage: oldsrc,//压缩前原图片
			image: src,
			dishunitlist: new Object()
	};
	tmpllist[curr_num].detaillist[drop_id].dishtype = dishtype;
	
	var temp ='<div class="menu-desc">';
	if(dishtype == 1){
		//鱼锅
		tmpllist[curr_num].detaillist[drop_id].dishunitlist[0] = {
				dishid : dish.dishid,
				dishname : dish.title,
				dishintroduction : dish.introduction,
				unit : dish.unit,
				price : '',
				vipprice : ''
		};
		//鱼锅名称
		temp += '<p><span>'+dish.title+'</span></p>';
		//level为1的时候 是双拼锅
		var level = dish.level;
		if(level == 1){
			//双拼锅，不需要显示明细
		}else{
			//非双拼锅，显示明细
			var li = getfishpotDetail(dishid);
			$.each(li, function(index, detail){
				var value = detail.price+"元/"+detail.dishunitid;
				if(detail.vipprice!=null && detail.vipprice!=""){
					value += "(会员价："+detail.vipprice+"元/"+detail.dishunitid+")";
				}
				temp += '<p><span>'+detail.contactdishname+'</span><span>'+value+"</span></p>";
				
				tmpllist[curr_num].detaillist[drop_id].dishunitlist[index+1] = {
						dishid : detail.contactdishid,
						dishname : detail.contactdishname,
						dishintroduction : dish.introduction,
						unit : detail.dishunitid,
						price : detail.price,
						vipprice : detail.vipprice
				};
			});
		}
	}else{
		temp += '<p><span>'+dish.title+'</span></p>';
		if(dishtype == 0 && dish.unit.indexOf("/")){
			var units = dish.unit.split("/");
			var prices = dish.price.split("/");
			var vipprices = dish.vipprice.split("/");
			for(var j=0;j<units.length;j++){
				var value = prices[j]+"元/"+units[j];
				if(vipprices[j]!=null && vipprices[j]!=""){
					value += "(会员价："+vipprices[j]+"元/"+units[j]+")";
				}
				temp += '<p><span>'+value+'</span></p>';
				tmpllist[curr_num].detaillist[drop_id].dishunitlist[j] = {
						dishid : dish.dishid,
						dishname : dish.title,
						dishintroduction : dish.introduction,
						unit : units[j],
						price : prices[j],
						vipprice : vipprices[j]
				};
			}
		}else{
			var v = dish.price+"元/";
			var unit = "";
			if(dishtype == 2){
				unit = "套";
			}else{
				unit = dish.unit;
			}
			v += unit;
			if(dish.vipprice != null && dish.vipprice !="" && dish.vipprice!=undefined){
				v += "(会员价："+dish.vipprice+"元/"+unit+")";
			}
			temp += '<p><span>'+v+'</span></p>';
			tmpllist[curr_num].detaillist[drop_id].dishunitlist[0] = {
					dishid : dish.dishid,
					dishname : dish.title,
					dishintroduction : dish.introduction,
					unit : unit,
					price : dish.price,
					vipprice : dish.vipprice
			};
		}
	}
	temp += '</div>';

	dataStyle[curr_num].menu_content[drop_id] ={
					menu_content_img:temp_img,
					recommend_img:'',//拖拽过来的时候 为空
					menu_content_imgW:width,
					menu_content_imgH:height,
					menu_content_desc:temp

	};	
	obj.append(temp);
	
	//拖动后的菜品上添加标签
	addSelIcon(data);
}
/**
 * 选择菜品的时候，添加已选择的标签
 * @param data
 */
function addSelIcon(data){
	$("#"+data).find("img.seled-dish-icon").remove();
	var inghtm = '<img class="seled-dish-icon" src="../images/sel-icon.png" />';
	$("#"+data).append(inghtm);
	var drag_num = $("#"+data).attr("drag-num");
	if(drag_num == null || drag_num == ""){
		drag_num = 0;
	}
	drag_num = parseInt(drag_num)+1;
	$("#"+data).attr("drag-num", drag_num);
	
	//将菜品分类对应的菜品的属性变化
	var dishid = $("#"+data).attr("id");
	var dishmap = new HashMap();
	if(columnIdMap.containsKey(g_typeid)){
		dishmap = columnIdMap.get(g_typeid);
	}
	dishmap.put(dishid, drag_num);
	columnIdMap.put(g_typeid, dishmap);
}
/**
 * 将菜品从版式上移除的时候，判断是否将已选择标签删除
 * @param data
 */
function removeSelIcon(data){
	var drag_num = $("#"+data).attr("drag-num");
	drag_num = parseInt(drag_num) - 1;
	$("#"+data).attr("drag-num", drag_num);
	if(drag_num == 0){
		$("#"+data).find("img.seled-dish-icon").remove();
	}
	//将菜品分类对应的菜品的属性变化
	var dishid = $("#"+data).attr("id");
	var dishmap = new HashMap();
	if(columnIdMap.containsKey(g_typeid)){
		dishmap = columnIdMap.get(g_typeid);
	}
	dishmap.put(dishid, drag_num);
	columnIdMap.put(g_typeid, dishmap);
}
function menuDetailDis(e){

	$(".menu-count").find(".menu-count-box").each(function(){
		var src = $(this).find("img").attr("src");
		src = src.match("active")?src.substr(0,src.lastIndexOf("-"))+'.png':src;
		$(this).find("img").attr("src",src);
		$(this).find("img").removeClass("active");

	});
	var src = $(e).attr("src");
	src = src.match("active")?src:(src.substr(0,src.lastIndexOf("."))+'-active.png');
	$(e).attr("src",src);
	$(e).addClass("active");


	var curr_num = $(e).attr("id");
	var detail_id = dataStyle[curr_num].menu_id;
	
	$(".menu-detail").addClass("hidden");
	var obj = $("#"+detail_id);
	obj.find(".menu-detail-box").attr("thumb-detail",curr_num);
	obj.removeClass("hidden");
	obj.find("img").not("img.show-pic").remove();
	obj.find(".menu-desc").remove();
	obj.find(".menu-detail-box").each(function(){
		var id = $(this).attr("id");
		$("#"+id).find("div.recommend_div").remove();
		if(typeof(dataStyle[curr_num].menu_content[id])!='undefined'){
			if(id == "L1" || id == "J1"){
				//L1栏上传图片，只展示图片
				$("#"+id).find("img.show-pic").attr("src", dataStyle[curr_num].menu_content[id].menu_content_img);
			}else{
				$(this).append(dataStyle[curr_num].menu_content[id].recommend_img);
				$(this).append(dataStyle[curr_num].menu_content[id].menu_content_img);
				$(this).append(dataStyle[curr_num].menu_content[id].menu_content_desc);
			}
		}else {
			if(id == "L1"){
				$("#"+id).find("img.show-pic").attr("src", global_Path+"/images/menu-detail-bg-upload.png");
			}else if(id == "J1"){
				$("#"+id).find("img.show-pic").attr("src", global_Path+"/images/menu-detail-bg-upload-ver.png");
			}
		}
	});
}
 function addEvent_(event) {      
	    event=event || window.event;
	    
        var type = event.type;
        if (type == 'DOMMouseScroll' || type == 'mousewheel') {
            event.delta = (event.wheelDelta) ? event.wheelDelta / 120 : -(event.detail || 0) / 3;
        }
        /*菜品分类*/
        var count = $(".nav-dishes-menu").children("li").length;		 
		if(event.delta >0){
			if(count-up_num>7)
			{
				$(".nav-dishes-menu").find("li").eq(up_num).css("margin-left","-14.2%");
			 	 up_num++;
			}

		}else{ 
			if(up_num>=1){	
				$(".nav-dishes-menu").find("li").eq(up_num-1).css("margin-left","0");						
				up_num--;
			}
		}

		if(document.all){
	    	event.cancelBubble = false;
	    	return false;
	    }else{
	    	event.preventDefault();
	    }

 }
 function addEvent1_(event) {      
	    event=event || window.event;
	    
        var type = event.type;
        if (type == 'DOMMouseScroll' || type == 'mousewheel') {
            event.delta = (event.wheelDelta) ? event.wheelDelta / 120 : -(event.detail || 0) / 3;
        }
      
        	var count = $(".menu-count").find("div").not(".hidden").length;		
			if(event.delta >0){
				if(count-menu_num>8)
				{
					$(".menu-count").find("div").not(".hidden").eq(menu_num).css("margin-left","-60px");//55
				 	 menu_num++;
				}

			}else{ 
				if(menu_num>=1){	
					$(".menu-count").find("div").not(".hidden").eq(menu_num-1).css("margin-left","0");						
					menu_num--;
				}
			}

			if(document.all){
		    	event.cancelBubble = false;
		    	return false;
		    }else{
		    	event.preventDefault();
		    }

 }
 function menuImgActive(e){
	 var src =$(e).attr("src");
	 src = src.match("active")?src:src.substr(0,src.lastIndexOf("."))+'-active.png';
	 $(e).attr("src",src);


 }
 function menuImgDisable(e){
 	if($(e).hasClass("active") == false){
 		 var src =$(e).attr("src");
		 src = src.match("active")?src.substr(0,src.lastIndexOf("-"))+'.png':src;
		 $(e).attr("src",src);
	}
 }
 /**
  * 判断图片路径是否存在
  * @param imgSrc
  */
 function checkSrc(imgSrc){
	 var f = false;
	 if(imgSrc != null && imgSrc != ""){
		 f = true;
	 }
	 return f;
 }
 /**
  * 
  * @param img
  * @returns
  */
 function cutPath(img){
	 var image = "";
	 if(img != null && img != ""){
		 
		 image = img.split(img_Path)[1];
		 if(image.indexOf("\/") == 0){
			 image = image.substring(1, image.length);
		 }
	 }
	 return replaceEscape(image);
 }
 /**
  * 将路径中的\转换为/
  * @param img
  * @returns
  */
 function replaceEscape(img){
 	if(img!=null && img!=""){
 		img = img.replace("\\", "/");
 	}
 	return img;
 }
 /**
 * 点击图片触发点击input file上传
 */
function imgtempClick(o){
	$(o).parent().find("input[type='file']").click();
}
/**
 * 上传图片压缩处理
 * @param o
 * @param src 压缩前图片
 */
function compress($o, src){
	var type = 5;
	var curr_num = $o.attr("thumb-detail");
	var id = $o.attr("id");
	if(id == "L1"){//上图下菜
		type = 5;
	} else if(id == "J1"){//左图右菜
		type = 6;
	}
	doAdjust(1,{
		src: src,
		oldimage: src,//被替换的菜品图片
		type: type
	},function(json){
		console.log(json.image);
		tmpllist[curr_num].detaillist[id] = {
    			location: id,
    			image: json.image,
    			originalImage: img_Path+"/"+json.image
    	};
    	dataStyle[curr_num].menu_content[id] = {
    			menu_content_img: img_Path+"/"+json.image
    	};
    	$o.find("img.show-pic").attr("src", img_Path+"/"+json.image);
    	console.log("上传图片后：");
    	console.log(tmpllist);
    	console.log(dataStyle);
	});
}
/**
 * 选择图片的时候 显示图片
 */
function showpic(o){
	var strsrc=getObjectURL(o.files[0]);
	var inpId = $(o).attr("id");
	if(strsrc!=null){
		//设置显示图片
		var $o = $(o).parent();
		//判断图片格式
		judgePicType(o);
		//新板式提交图片
	 	$.ajaxFileUpload({
	 		fileElementId: [inpId],  
		    url: global_Path+"/menu/saveimg",  
		    dataType: 'json',
		    contentType:'application/json;charset=UTF-8',
		    data:{},
		    success: function (data, textStatus) {
		    },  
		    complete: function (XMLHttpRequest, textStatus) {
		    	console.log("complete");
		    	//新板式特殊图片处理
		    	var result = $.parseJSON(XMLHttpRequest.responseText);//jQuery.parseJSON(jsonstr),可以将json字符串转换成json对象 
		    	if(result.code == 1){
		    		// TODO
		    		compress($o, img_Path+"/"+result.data);
		    	}else{
		    		alert("图片上传失败，请重新选择上传！");
		    	}
		    }
	 	});
	}
}
/**
 * 获取路径
 * @param file
 * @returns
 */
function getObjectURL(file) {
    var url = null ;
    if (window.createObjectURL!=undefined) { // basic
        url = window.createObjectURL(file) ;
    } else if (window.URL!=undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file) ;
    } else if (window.webkitURL!=undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file) ;
    }
    return url ;
}
/**
  * 判断图片是否正确
  */
 var allowExt = ['jpg', 'gif', 'bmp', 'png', 'jpeg'];
 function judgePicType(o){
 	var name = $(o).val();
 	var ext = name.substr(name.lastIndexOf(".")+1, name.length);
 	 if(allowExt.indexOf(ext) == -1){
 		 alert("图片格式错误！");
 	 }
 }
 