<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<div class="modal fade storeSelect-dialog in " id="store-select-dialog">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-header addDelicon">				  
				        <img data-dismiss="modal" class="img-close" src="/newspicyway/images/close.png">
				    </div>
					<div class="modal-body">
						<div class="row store-select-title">
							<div class="col-xs-9">选择门店<font id="store-count"></font></div>
							<div class="col-xs-3 pull-right">
								<label class="radio-inline">
									<input type="radio" value="1" name="store">全选
								</label>
								<label class="radio-inline">
									<input type="radio" value="0" name="store">全不选
								</label>
							</div>
						</div>
						<hr>
						<table class="table store-select-content">
				
						</table>
						<div class="btn-operate">
							<button class="btn btn-cancel in-btn135" data-dismiss="modal">取消</button>
							<div  class="btn-division"></div>
							<button class="btn btn-save in-btn135 preferential-btn-bgcolor" id="store-select-confirm">确认</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
		
		(function($, window, document,undefined) {
		    //定义SelectBranch的构造函数
		    var SelectBranch = function(ele, opt) {
		        this.$element = ele,
		        this.defaults = {
		        		'dialog_id': 'store-select-dialog',
				        'target_id':'select_branchs'
		        },
		        this.options = $.extend({}, this.defaults, opt);
		    };
		    
		    var methods={
		    	init:function(){
		    		var opt=this.options;
		    		var $ele=this.$element;
		    		var $this=this;
		    		//从数据库加载所有门面
					$.getJSON(global_Path+"/shopMg/getall.json", function(json){
						var dialogObj=$("#"+opt.dialog_id);
						var targetObj=$("#"+opt.target_id);
						dialogObj.find(".store-select-content").html();
						var html="<tr>";
						$.each(json, function(i, obj) {
							html=html+" <td>"
								+"<input type='checkbox' value="+obj.branchid+"><span>"+obj.branchname+"</span>"
								+"</td>";
							if( (i+1)%4==0){//因为计数从0开始，所以要加一个才能显示正常
								html+="</tr><tr>"
							}
						});
						html=html+"</tr>"
						dialogObj.find(".store-select-content").html(html);
						
						//重新初始化 门店点击事件
						dialogObj.find(".store-select-content input[type='checkbox']").click(function(){
							var branchLen = dialogObj.find(".store-select-content input[type='checkbox']:checked").length;
							methods.uploadStoreCount.apply($this, [branchLen]);
						});
						//选中门店名字，也可以发生变化
						// 点击文字选中
						dialogObj.find(".store-select-content span").click(function(){
							$(this).prev("input[type='checkbox']").trigger("click");
							//var obj = $(this).prev("input").each(function(i,ch){
							//	if(ch.checked == false){
							//		ch.checked = true;
							//	}else{
							//		ch.checked =false;
							//	}
							//});
							//dialogObj.find("#store-count").text( dialogObj.find(".store-select-content input[type='checkbox']:checked").length );
					
						});
						//绑定全选与全不选功能
						dialogObj.find(".store-select-title input[type=radio][name=store]").click(function(){
							if( $(this).val() ==1 ){
								dialogObj.find(".store-select-content input[type='checkbox']").prop("checked",true);
							}else{
								dialogObj.find(".store-select-content input[type='checkbox']").prop("checked",false);
							}
							methods.uploadStoreCount.apply($this,  [dialogObj.find(".store-select-content input[type='checkbox']:checked").length ]);
						});
						methods.updateData.apply($this);
						/*点击确认按钮*/
						dialogObj.find("#store-select-confirm").click(function(){
							var storeName= dialogObj.find(".store-select-content input:checked[type='checkbox']").next("span").text();
							storeName = storeName.substr(0,3)+"...";
							var selectedStores=dialogObj.find(".store-select-content input[type='checkbox']:checked");
							$("#store-select").find("div.popover").remove();
							if(selectedStores.length > 0){
								
								var selectBranchs=[];
								var ul = $("<ul/>").addClass("storesDiv");
								$.each(selectedStores,function(i,obj){
									selectBranchs.push($(this).val());
									var name = $(this).next("span").text();
									ul.append("<li>"+name+"</li>")
								});
								var top = ileft = iwidth ="";
								if(selectedStores.length >= 3){
									iwidth = "460px";
									ileft = "-155px";
									
								}
								var div = $("<div>").addClass("popover fade bottom in").css({
									width : iwidth,
									top : "38px",
									left: ileft
								}).append('<div class="arrow" style="left: 50%;"></div>');
								div.append(ul);
								targetObj.val( selectBranchs.join(","));
								$("#store-select").append(div);
								$("#addstore").text("已选中"+selectedStores.length + "家店").addClass("selectBranch");
							}else{
								$("#select_branchs").val("");
								$("#addstore").html('<i class="icon-plus"></i>选择门店 ').removeClass("selectBranch").next(".popover").remove();
							}
							
							dialogObj.modal("hide");
						});
						
						//如果存在已经选择的门店，那么初始化 选择的数量与弹出提示
						methods.initPoppver.apply($this);
						
					});
		    		
		    	},
		    	initPoppver:function(){ //用于初次初始化的时候，如果有隐藏的选择的门店，则初始化已经选择门店和弹出层。
		    		var opt=this.options;
		    		var $ele=this.$element;
		    		//从数据库加载所有门面
					var dialogObj=$("#"+opt.dialog_id);
					var targetObj=$("#"+opt.target_id);
		    		var selectedStores=dialogObj.find(".store-select-content input[type='checkbox']:checked");
		    		$("#store-select").find("div.popover").remove();
					if(selectedStores.length > 0){
						
						var selectBranchs=[];
						var ul = $("<ul/>").addClass("storesDiv");
						$.each(selectedStores,function(i,obj){
							selectBranchs.push($(this).val());
							var name = $(this).next("span").text();
							ul.append("<li>"+name+"</li>")
						});
						var top = ileft = iwidth ="";
						if(selectedStores.length >= 3){
							iwidth = "460px";
							ileft = "-155px";
							
						}
						var div = $("<div>").addClass("popover fade bottom in").css({
							width : iwidth,
							top : "38px",
							left: ileft
						}).append('<div class="arrow" style="left: 50%;"></div>');
						div.append(ul);
						targetObj.val( selectBranchs.join(","));
						$("#store-select").append(div);
						$("#addstore").text("已选中"+selectedStores.length + "家店").addClass("selectBranch");
					}else{
						$("#select_branchs").val("");
						$("#addstore").html('<i class="icon-plus"></i>选择门店 ').removeClass("selectBranch").next(".popover").remove();
					}
		    	},
		    	updateData:function(){
		    		var opt=this.options;
		    		var $ele=this.$element;
		    		var _this=this;
		    		//从数据库加载所有门面
					var dialogObj=$("#"+opt.dialog_id);
					var targetObj=$("#"+opt.target_id);
					//先设置两个单选都不选中
					dialogObj.find(".store-select-title input[type=radio][name=store]").prop("checked",false);
					dialogObj.find(".store-select-content input[type='checkbox']").prop("checked",false);
					//如果当前已经有选择的门店，需要将选择的门店，重新在页面显示为选中的状态
					
					var branchLen = dialogObj.find(".store-select-content input[type='checkbox']:checked").length;
					if( targetObj.val() != ""){
						var txtArray=[];
						var arr = targetObj.val().split(",");
						$.each( arr,function(i,obj){
							var chb=dialogObj.find(".store-select-content input[type='checkbox'][value='"+obj+"']");
								chb.prop("checked", true);
						});
					}
					
					methods.uploadStoreCount.apply(_this);
		    	},
		    	uploadStoreCount:function(){
		    		var opt=this.options;
		    		var $ele=this.$element;
		    		//从数据库加载所有门面
					var dialogObj=$("#"+opt.dialog_id);
					var count= dialogObj.find(".store-select-content input[type='checkbox']:checked").length;
					
					var branchLen = dialogObj.find(".store-select-content input[type='checkbox']:checked").length;
					if( count !=0){
						dialogObj.find("#store-count").parent().html("选择门店（已选<font id='store-count'>"+count+"</font>家店）");
					}else{
						dialogObj.find("#store-count").parent().html("选择门店<font id='store-count'></font>");
					}
					//判断全选与不全选状态
					if(branchLen == 0){
						dialogObj.find(".store-select-title input[type=radio][name=store][value='0']").prop("checked", true);
						dialogObj.find(".store-select-title input[type=radio][name=store][value='1']").prop("checked", false);
					} else if(branchLen == $("table.store-select-content").find("input[type='checkbox']").length ){
						dialogObj.find(".store-select-title input[type=radio][name=store][value='0']").prop("checked", false);
						dialogObj.find(".store-select-title input[type=radio][name=store][value='1']").prop("checked", true);
					} else {
						dialogObj.find(".store-select-title input[type=radio][name=store][value='0']").prop("checked", false);
						dialogObj.find(".store-select-title input[type=radio][name=store][value='1']").prop("checked", false);
					}
					
		    	}
		    	
		    		
		    };
		    
		    //定义SelectBranch的方法
		    SelectBranch.prototype = {
		        beautify: function() {
		        	var _this=this;
		        	$(this.$element).click(function(){
		        		$("#store-select-dialog").modal("show");
		        		methods.updateData.apply(_this);
		        	});
		        	
		        	//加载数据
		        	methods.init.apply(this);
		        	
		            return this.$element;
		        }
		    }
		    //在插件中使用Beautifier对象
		    $.fn.selectBranchPlugin = function(options) {
		        //创建Beautifier的实体
		        var selectBranch = new SelectBranch(this, options);
		        //调用其方法
		        return selectBranch.beautify();
		    }
		})(jQuery, window, document);
		
		$("img.img-close").hover(function(){
		 	$(this).attr("src",global_Path+"/images/close-active.png");	 
		},function(){
				$(this).attr("src",global_Path+"/images/close-sm.png");
		});
		</script>
		