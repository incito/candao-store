<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>优惠管理</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/preferential.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">

	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/preferential.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/jquery.twbsPagination.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/jquery.mCustomScrollbar.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/jquery.mousewheel.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/global.js"></script>

	<style type="text/css">
		.btn-add {
		    margin-top: 15px;
		    margin-bottom: 15px;
		}
		.table-list {
			margin-top:0;
		}
		
		.preferential-table td{font-size: 12px;}
		
	</style>
</head>
<body>
		
		<div class="ky-content preferential-content">
<%-- 			<c:if test="'<%=Constant.ISBRANCH%>'=='Y'">  --%>
			<!-- button样式添加-->
			<div class="preferential-btn btn-add">
				<div class="btn-group" role="group" aria-label="...">
				  <button type="button" class="btn  btn-first">新增优惠</button>
				</div>
			</div>
<%-- 			</c:if> --%>
			<ul class="nav-preferential "  id="nav-preferential-main">
				<li class="active" data-type="">
					<span class="top"></span>
					<em></em>
					<a>全部</a>
					<span class="bottom"></span>
				</li>
				<li data-type="01">
					<span class="top"></span>
					<em></em>
					<a>特价券</a>
					<span class="bottom"></span>

				</li>
				<li data-type="02">
					<span class="top"></span>
					<em></em>
					<a>折扣券</a>
					<span class="bottom"></span>
				</li>
				<li data-type="03">
					<span class="top"></span>
					<em></em><a>代金券</a>
					<span class="bottom"></span>
				</li>
				<li data-type="04">
					<span class="top"></span>
					<em></em>
					<a>礼品券</a>
					<span class="bottom"></span>

				</li>
				<li data-type="05">
					<span class="top"></span>
					<em></em><a>团购券</a>
					<span class="bottom"></span>
				</li>
				<li data-type="06">
					<span class="top"></span>
					<em></em><a>其他优惠</a>
					<span class="bottom"></span>
				</li>
			</ul>	
			<!--样式更改 添加table-list -->
			<table class="table preferential-table table-list">
				<thead>
					<tr>
						<th class="count">序号</th>
						<th class="code">编号</th>
						<th class="name">名称</th>
						<th class="type">分类</th>
						<th class="color">颜色</th>
						<th class="limite">有效期</th>
						<th class="opera">操作</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<div class="pagingWrap">
			
			</div>
		</div>
		
			<!-- 确认删除框 -->
		<div class="modal fade dialog-sm in " id="deleteComfirm"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-body">
					<div class="dialog-sm-header">				  
				        <span class=" ">确认删除</span>
				        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
				    </div>
				    
						<form action="" method="post" class="form-horizontal " name="">
							<!-- 仅存在一个分类中-->
							<div class="dialog-sm-info">
							<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png"></i>确认删除“<span id="showName"></span>”吗?</p>
							</div>
							<div class="btn-operate">
								<button class="btn btn-cancel  " type="button" data-dismiss="modal"   >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save  " id="dishes-type-save" type="button" onclick="doDel()">确认</button>
							</div>
						</form>
					</div>
					

				</div>
				
			</div>
		</div><!-- end delete  -->
		
		 <div class="modal fade .dishes-detailDel-dialog in " id="successPrompt"  >
			<div class="modal-dialog" style="width:310px;">
				<div class="modal-content" style="width:310px;">	
					<div class="modal-body pop-div-content" style="height:210px;">
						<br/>
							<p class="tipP"> <i class="icon-ok"></i><label id="promptMsg">删除成功</label></p>
					</div>
				</div>
			</div>
		</div>
		
		<!--点击按钮弹出添加界面 -->
		<div class="modal fade preferential-dialog in " id="preferential-add" data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-header addDelicon">				  
				        <img data-dismiss="modal" class="img-close" src="/newspicyway/images/close.png">
				    </div>
					<div class="modal-body">
					
						<div class="row">
							<div class="col-xs-4" data-type="toSpecialStamp?isModify=true">
								
								<img src="<%=request.getContextPath()%>/images/preferential-type1.png"/>
								<p class="preferential-type">特价券</p>
								<p class="preferential-detail">川味香肠特价8元</p>

							</div>
							<div class="col-xs-4" data-type="toDiscountCoupon">
								<img src="<%=request.getContextPath()%>/images/preferential-type2.png"/>
								<p class="preferential-type">折扣券</p>
								<p class="preferential-detail">整单满200元8折</p>

							</div>
							<div class="col-xs-4" data-type="toVoucher">
								<img src="<%=request.getContextPath()%>/images/preferential-type3.png"/>
								<p class="preferential-type">代金券</p>
								<p class="preferential-detail">50元鱼券</p>

							</div>
						</div>
						<div class="row">
							<div class="col-xs-4" data-type="toGiftCoupon">
								<img src="<%=request.getContextPath()%>/images/preferential-type4.png"/>
								<p class="preferential-type">礼品券</p>
								<p class="preferential-detail">梭边鱼试吃券</p>

							</div>
							<div class="col-xs-4" data-type="toGroupBuying?isModify=true">
								<img src="<%=request.getContextPath()%>/images/preferential-type5.png"/>
								<p class="preferential-type">团购券</p>
								<p class="preferential-detail">美团88抵100</p>

							</div>
							<div class="col-xs-4" data-type="toOtherCoupon">
								<img src="<%=request.getContextPath()%>/images/preferential-type6.png"/>
								<p class="preferential-type">其他优惠</p>
								<p class="preferential-detail">人工优免</p>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script src="<%=request.getContextPath()%>/scripts/projectJs/autoComplete.js"></script>
		<script type="text/javascript">

			var global_Path = '<%=request.getContextPath()%>';
		
			var contextPath = "<%=request.getContextPath()%>";
			if(isbranch=='Y'){
				$(".preferential-content .preferential-btn").remove();
			}
			$("#preferential-add div.col-xs-4").click(function(){
				$(parent.document.all("detail")).attr("src","<%=request.getContextPath()%>/preferential/"+$(this).attr("data-type"));
			});
			$(parent.document.all("allSearch")).css("visibility","visible");
			var type, page;
			if(window.localStorage){
				type = typeof(localStorage.currentType) == "undefined" ? "" : localStorage.currentType;
				page = typeof(localStorage.currentPage) == "undefined" ? "1" : localStorage.currentPage;
			}else{
				type = "";
				page = "1";
			}
			var thisLi = $("ul.nav-preferential li[data-type='"+type+"']");
			thisLi.addClass("active").siblings().removeClass("active");
			var left= (thisLi.prevAll("li").length*14+7)+'%';
			thisLi.find("em").css("left",left);
			findData(type, page, null);
			// 查询数据
			function findData(type, subPage, id){
				var dataJson = '{"page":"'+subPage+'","rows":"10"';
				if(type && type != ""){
					dataJson += ',"type":"'+type+'"';
				}
				if(id && id != ""){
					dataJson += ',"id":"'+id+'"';
				}
				dataJson += '}';
				$.ajax({
					url:"<%=request.getContextPath()%>/preferential/page.json",
					data:$.parseJSON(dataJson),
					type:"post",
					dataType:"json",
					success:function(data){
						var table = $("table.preferential-table");
						var dataBlock = table.find("tbody");
						var str, classToggle, starttime, endtime;
						$.each(data.rows, function(i, v){
							classToggle = i % 2 == 0 ? "odd" : "";
							if(v.type == "06"){
								starttime = endtime= "";
							}else{
								starttime = timestampformat(parseInt(v.starttime));
								endtime = timestampformat(parseInt(v.endtime));
							}
							
							if(isbranch=='N'){
								str += '<tr class="'+classToggle+'" tr-id="tr'+i+'">\
								<td>'+((parseInt(subPage)-1)*10 + (1 + i))+'</td>\
								<td>'+v.code+'</td>\
								<td class="data_name">'+v.name+'</td>\
								<td>'+v.typeName+'</td>\
								<td><div class="td-color" style="background-color:'+v.color+'"></div></td>\
								<td>'+starttime+' - '+endtime+'</td>\
								<td  class="td-last">\
									<div class="operate">\
										<a href="javascript:void(0)" onclick="operaPreferntial(\''+v.id+'\',\''+v.type+'\',\''+v.subType+'\',\'look\')">查看</a>\
										<a href="javascript:void(0)" onclick="operaPreferntial(\''+v.id+'\',\''+v.type+'\',\''+v.subType+'\',\'modify\')">修改</a>\
										<a href="javascript:void(0)" class="deleteBtn" onclick="deletePreferntial(\''+v.id+'\',\''+i+'\',\''+v.name+'\')">删除</a>\
									</div>\
								</td>\
							</tr>';
							}else if(isbranch=='Y'){
								str += '<tr class="'+classToggle+'" tr-id="tr'+i+'">\
								<td>'+((parseInt(subPage)-1)*10 + (1 + i))+'</td>\
								<td>'+v.code+'</td>\
								<td class="data_name">'+v.name+'</td>\
								<td>'+v.typeName+'</td>\
								<td><div class="td-color" style="background-color:'+v.color+'"></div></td>\
								<td>'+starttime+' - '+endtime+'</td>\
								<td  class="td-last">\
									<div class="operate">\
										<a href="javascript:void(0)" onclick="operaPreferntial(\''+v.id+'\',\''+v.type+'\',\''+v.subType+'\',\'look\')">查看</a>\
									</div>\
								</td>\
							</tr>';
							}
							
							
						})
						dataBlock.empty().append(str);
						$(".pagingWrap").find("ul.paging").remove();
						if(data.total > 10 && $("ul.paging").length == 0){
							$(".pagingWrap").html('<ul class="paging clearfix">');
			        		$(".paging").twbsPagination({
								totalPages: data.pageCount,
			        			visiblePages: 7,
			        			startPage : parseInt(subPage),
			        			first: '...',
			        			prev : '<',
			        			next : '>',
						        last: '...',
						        onPageClick: function (event, page){
						        	localStorage.currentPage = page;
						        	var _t=$("#nav-preferential-main  li.active").attr("data-type");
						        	findData(_t, page, null);
						        }
							});
			        		
						}else if(data.total <= 10){
							$(".pagingWrap").empty();
						}
					}
				});
			}
			// 自动补全
			$(parent.document.all("allSearch")).find("input:text").autoComplete({
				delay: 300,
				showCount: 6,
				showMore: true,
				url : global_Path+"/preferential/page.json",
				onTextChange : function(e, id, type){
					$("#nav-preferential-main").children("li[data-type='"+type+"']").addClass("active").siblings().removeClass("active");
					findData(null, 1, id);
				},
				searchBtn : $(parent.document.all("allSearch")).find("#basic-addon1"),
				onBtnAction : function(){
					$("#nav-preferential-main").children("li[data-type='']").addClass("active").siblings().removeClass("active");
					findData(null, 1, null);
				}
			});
			$("img.img-close").hover(function(){
			 	$(this).attr("src",global_Path+"/images/close-active.png");	 
			},function(){
					$(this).attr("src",global_Path+"/images/close-sm.png");
			});
		</script>
		
</body>
</html>
