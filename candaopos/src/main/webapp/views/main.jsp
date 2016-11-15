<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport"
          content="width=device-width,initial-scale=1, user-scalable=no, minimum-scale=1.0,maximum-scale=1.0"/>
    <!-- 让 IE 浏览器运行最新的渲染模式下 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 让部分国产浏览器默认采用高速模式渲染页面 -->
    <meta name="renderer" content="webkit">

    <title>主页</title>
    <link rel="stylesheet" href="../tools/bootstrap-3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/main.css">
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="../scripts/jquery-3.1.0.min.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
    <script src="../scripts/common.js"></script>
    <script src="../scripts/main.js"></script>
    <script src="../lib/md5.js"></script>

    <script type="text/javascript">



        $(document).ready(function () {
//		var dom =$("#nav-room-types")[0];
//		var user_agent = navigator.userAgent;
//		if(typeof(dom) == 'object'){
//			if(user_agent.indexOf("Firefox")!=-1){// Firefox
//	            dom.addEventListener("DOMMouseScroll",addEvent,!1);
//		    } else if(user_agent.indexOf("MSIE")!=-1){// Firefox
//		        dom.attachEvent("onmousewheel",addEvent,!1);
//		    }else{
//		        dom.addEventListener("mousewheel",addEvent,!1);
//		    }
//		}
//
//		var startX = 0, startY = 0, moveEndX=0, moveEndY=0, X, Y;
//		$("#nav-room-types").on("touchstart", function(e) {
//		    e.preventDefault();
//		    e.stopPropagation();
//		    startX = e.originalEvent.changedTouches[0].pageX,
//		    startY = e.originalEvent.changedTouches[0].pageY;
//		});
//		$("#nav-room-types").on("touchmove", function(e) {
//		    e.preventDefault();
//		    e.stopPropagation();
//		    moveEndX = e.originalEvent.changedTouches[0].pageX,
//		    moveEndY = e.originalEvent.changedTouches[0].pageY,
//		    X = moveEndX - startX,
//		    Y = moveEndY - startY;
//		    var count = $("#nav-room-types").children("li").length;
//		    if ( Math.abs(X) > Math.abs(Y) && X > 0 ) {
//		        if(roomtype_prev > 0){
//		        	$(".rooms-type .nav-types").find("li").eq(roomtype_prev-1).css("margin-left","0");
//					$(".rooms-type .nav-types").find("li").eq(roomtype_prev-1).click();
//					roomtype_prev--;
//		        }
//		    }
//		    else if ( Math.abs(X) > Math.abs(Y) && X < 0 ) {
//				if(count-roomtype_prev>10){
//					$(".rooms-type .nav-types").find("li").eq(roomtype_prev).css("margin-left", "-10%");
//					$(".rooms-type .nav-types").find("li").eq(roomtype_prev+1).click();
//					roomtype_prev++;
//				}
//		    }
//		    else if ( Math.abs(Y) > Math.abs(X) && Y > 0) {
////		        alert("top 2 bottom");
//		    }
//		    else if ( Math.abs(Y) > Math.abs(X) && Y < 0 ) {
////		        alert("bottom 2 top");
//		    }
//		    else{
////		        alert("just touch");
//		    }
//		});


        });
        //外带
//        function takeOut() {
//            g_isopened = true;
//            g_eatType = "TAKE-OUT";
//            $("#order-dialog").load("../views/order.jsp");
//            $("#order-dialog").modal("show");
//            $("#adddish-dialog").load("../views/orderdish.jsp");
//            $("#adddish-dialog").modal("show");
//        }
//        function doPage(currPage) {
//            var o = $(".menu-tab ul li.active").attr("loaddiv") + " li";
//            var options = {
//                obj: o,
//                listNum: 40,
//                currPage: currPage,
//                totleNums: $(o).length,
//                curPageObj: "#curr-page",
//                pagesLenObj: "#pages-len",
//                prevBtnObj: ".page .prev-btn",
//                nextBtnObj: ".page .next-btn"
//            };
//            nowPage = page(options);
//        }
//        function addEvent(event) {
//            event.preventDefault();
//            event = event || window.event;
//
//            var type = event.type;
//            if (type == 'DOMMouseScroll' || type == 'mousewheel' || type == 'touchmove') {
//                event.delta = (event.wheelDelta) ? event.wheelDelta / 120 : -(event.detail || 0) / 3;
//            }
//            var count = $("#nav-room-types").children("li").length;
//            if (event.delta > 0) {
//                if (count - roomtype_prev > 10) {
//                    $("#nav-room-types").find("li").eq(roomtype_prev).css("margin-left", "-10%");
//                    $(".rooms-type .nav-types").find("li").eq(roomtype_prev + 1).click();
//                    roomtype_prev++;
//                }
//
//            } else {
//                if (roomtype_prev >= 1) {
//                    $("#nav-room-types").find("li").eq(roomtype_prev - 1).css("margin-left", "0");
//                    $(".rooms-type .nav-types").find("li").eq(roomtype_prev - 1).click();
//                    roomtype_prev--;
//                }
//            }
//
//            if (document.all) {
//                event.cancelBubble = false;
//                return false;
//            } else {
//                event.preventDefault();
//            }
//        }
    </script>
</head>
<body>
<header>
    <div class="fl">餐道</div>
    <!--div class="fl menu-top">
        <div class="J-btn-memberView">会员查询</div><div class="J-btn-storge">会员储值</div><div class="J-btn-register">会员注册</div>
    </div-->
    <div class="menu-tab">
        <ul>
            <li class="active" loaddiv="#standard-tables">标准台</li>
            <li loaddiv="#coffee-tables">咖啡台</li>
        </ul>
    </div>
    <div class="exit-sys">退出系统</div>
</header>
<article>
    <div class="rooms-type">
        <div class="nav-type-btn nav-type-prev unclick">
            <span class="glyphicon glyphicon-chevron-left"></span>
        </div>
        <ul class="nav-types" id="nav-room-types">
        </ul>
        <div class="nav-type-btn nav-type-next unclick">
            <span class="glyphicon glyphicon-chevron-right"></span>
        </div>
    </div>
    <div class="content">
        <ul id="standard-tables" class="standard tables">
        </ul>
        <ul id="coffee-tables" class="hide coffee tables">
            <li>1台
                <div class="tb-info tb-person">2人桌</div>
            </li>
            <li>2台
                <div class="tb-info tb-person">2人桌</div>
            </li>
            <li>3台
                <div class="tb-info tb-person">2人桌</div>
            </li>
        </ul>
    </div>
</article>
<footer>
    <div class="table-nums J-table-nums">
        <div class="total-num active all">全部（<span class="num">0</span>）</div>
        <div class="free-num free">空闲（<span class="num">0</span>）</div>
        <div class="opened">就餐（<span class="num">0</span>）</div>
    </div>
    <div class="foot-menu">
        <ul class="menu">
            <li class="J-btn-takeout">外卖</li>
            <li class="J-btn-check">账单</li>
            <li class="J-btn-rep">报表</li>
            <li class="J-btn-clear">清机 / 结业</li>
            <!--li class="J-btn-checkout">结业</li-->
            <li class="member-btns">会员</li>
            <div class="m-member popover fade top in">
                <div class="arrow " style="left: 50%;"></div>
                <ul class="arrowMember">
                    <li class="J-btn-storge">会员储值</li>
                    <li class="J-btn-register">会员注册</li>
                    <li class="J-btn-memberView">会员查询</li>
                </ul>
            </div>
            <li class="J-btn-sys">系统设置</li>
        </ul>
        <div class="page" id="J-table-pager"></div>
    </div>
    <div class="form-group amount-div">
        <span class="highlight-color" style="color: #333">总金额：</span><span class="highlight-color totalAmount" style="color: #333">0</span>
        <span>&nbsp;&nbsp;未结金额：</span><span class="dueamount">0</span>
        <span>&nbsp;&nbsp;已结单数：</span><span class="orderCount">0</span>
        <span>&nbsp;&nbsp;已结金额：</span><span class="ssamount">0</span>
        <span>&nbsp;&nbsp;累计就餐人数：</span><span class="custnum">0</span>
    </div>
    <div class="info J-sys-info"><span>店铺编号：</span><span class="branch-num">- -</span><span>&nbsp;登录员工：</span><span>&nbsp;<span
            class="user-info">- -</span></span><span>&nbsp;当前时间：</span><span
            class="time">- -</span><span>&nbsp;版本号：</span><span>1.01</span></div>
</footer>


<div class="modal fade in main-dialog" data-backdrop="static" id="order-dialog" style="overflow: auto;">

</div>

<!-- 开台权限验证 -->
<div class="modal fade in open-dialog" data-backdrop="static" style="display: none" id="open-dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="dialog-sm-header">
                <div class="modal-title">开台</div>
                <img src="../images/close-sm.png" class="img-close" data-dismiss="modal">
            </div>
            <div class="modal-body">
                <div style="padding: 13px; float: left;">
                    <div class="hori-lf-div">
                        <div>
                            <span>服务员编号:</span>
                            <input type="text" validType='number'
                                   class="form-control serverName J-server-name">
                        </div>
                        <%--<div>--%>
                        <%--<span>桌号:</span>--%>
                        <%--<input type="text" class="form-control tableno">--%>
                        <%--</div>--%>
                        <div>
                            <span>就餐人数(男):</span>
                            <input type="text" validType='number'
                                   class="form-control personnum J-male-num">
                        </div>
                        <div>
                            <span>就餐人数(女):</span>
                            <input type="text" validType='number'
                                   class="form-control personnum J-female-num">
                        </div>
                        <div>
                            <span>餐具数量:</span>
                            <input type="text" disabled validType='number'
                                   class="form-control J-tableware-num">
                        </div>
                    </div>
                    <div class="hori-rt-div">
                        <div class="virtual-keyboard">
                            <ul>
                                <li>1</li>
                                <li>2</li>
                                <li>3</li>
                            </ul>
                            <ul>
                                <li>4</li>
                                <li>5</li>
                                <li>6</li>
                            </ul>
                            <ul>
                                <li>7</li>
                                <li>8</li>
                                <li>9</li>
                            </ul>
                            <ul>
                                <li>.</li>
                                <li>0</li>
                                <li>←</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="age-type">
                    <div>儿童</div>
                    <div>青年</div>
                    <div>中年</div>
                    <div>老年</div>
                </div>
                <div class="btn-operate ">
                    <button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消
                    </button>
                    <button class="btn btn-save in-btn135 J-btn-submit" type="button">确认开台
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 外卖 -->
<div class="modal fade default-dialog J-takeout-dialog in " id="J-takeout-dialog"
     data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="dialog-sm-header">
                <div class="modal-title" style="text-align: center;">备注桌号</div>
                <img src="../images/close-sm.png" class="img-close" data-dismiss="modal">
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <ul class="take-out-list">
                    </ul>
                    <ul class="take-out-list-normal" style="display: none"></ul>
                </div>
                <div class="btn-operate  ">
                    <button class="btn btn-cancel in-btn135 clear-btn" style="float: left;" type="button"
                            data-dismiss="modal" onclick="MainPage.setTakeOutOrder(0)">外带
                    </button>
                    <div style="text-align: right;">
                        <button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消
                        </button>
                        <button class="btn btn-save in-btn135"  onclick="MainPage.setTakeOutOrder(1)" type="button" data-dismiss="modal">确认
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 选择挂账单位 -->
<div class="modal fade in default-dialog" id="select-paycompany-dialog"
     data-backdrop="static">
    <div class="modal-dialog" style="width: 800px;">
        <div class="modal-content">
            <div class="dialog-sm-header">
                <div class="modal-title">餐道</div>
                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('select-paycompany-dialog')">
            </div>
            <div class="modal-body">
                <div style="font-size: 20px;font-weight: bold;">请选择挂账单位</div>
                <div class="form-group search-btns">
                    <div class="search-btn">A</div>
                    <div class="search-btn">B</div>
                    <div class="search-btn">C</div>
                    <div class="search-btn">D</div>
                    <div class="search-btn">E</div>
                    <div class="search-btn">F</div>
                    <div class="search-btn">G</div>
                    <div class="search-btn">H</div>
                    <div class="search-btn">I</div>
                    <div class="search-btn">J</div>
                    <div class="search-btn">K</div>
                    <div class="search-btn">L</div>
                    <div class="search-btn">M</div>
                    <div class="search-btn">N</div>
                    <div class="search-btn">O</div>
                    <div class="search-btn">P</div>
                    <div class="search-btn">Q</div>
                    <div class="search-btn">R</div>
                    <div class="search-btn">S</div>
                    <div class="search-btn">T</div>
                    <div class="search-btn">U</div>
                    <div class="search-btn">V</div>
                    <div class="search-btn">W</div>
                    <div class="search-btn">X</div>
                    <div class="search-btn">Y</div>
                    <div class="search-btn">Z</div>
                </div>
                <div class="form-group search">
                    <span class="glyphicon glyphicon-search"></span>
                    <input type="search" class="form-control" placeholder="输入首字母过滤">
                    <div class="delsearch-btn">C</div>
                </div>
                <div class="form-group paycompany-content">
                    <ul>
                        <li>合作单位1111111111111111111111111</li>
                        <li>合作单位1111111111111111111111111</li>
                        <li>合作单位1111111111111111111111111</li>
                        <li>合作单位1111111111111111111111111</li>
                        <li>合作单位1111111111111111111111111</li>
                        <li>合作单位1111111111111111111111111</li>
                        <li>合作单位1111111111111111111111111</li>
                        <li>合作单位1111111111111111111111111</li>
                        <li>合作单位1111111111111111111111111</li>
                    </ul>
                </div>
                <div class="btn-operate  ">
                    <div class="page">
                        <button class="btn page-btn prev-btn">
                            <span class="glyphicon glyphicon-chevron-left"></span>
                        </button>
                        <div class="page-info" style="display: inline-block;">
                            <span id="pay-curr-page">0</span>/<span id="pay-pages-len">0</span>
                        </div>
                        <button class="btn page-btn next-btn">
                            <span class="glyphicon glyphicon-chevron-right"></span>
                        </button>
                    </div>
                    <div style="text-align: right;">
                        <button class="btn btn-cancel in-btn135" type="button"
                                onclick="closeConfirm('select-paycompany-dialog')">取消
                        </button>
                        <button class="btn btn-save in-btn135"  type="button" onclick="">确认
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade in main-dialog" data-backdrop="static" id="sys-dialog" style="overflow: auto;">
</div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="register-dialog"
     style="overflow: auto;"></div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="J-btn-checkout-dialog"
     style="overflow: auto;"></div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="J-btn-clear-dialog"
     style="overflow: auto;"></div>
</body>
</html>