<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport"
          content="width=device-width,initial-scale=1, user-scalable=no, minimum-scale=1.0,maximum-scale=1.0" />
    <!-- 让 IE 浏览器运行最新的渲染模式下 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 让部分国产浏览器默认采用高速模式渲染页面 -->
    <meta name="renderer" content="webkit">
    <script src="../../lib/md5.js"></script>
    <title>修改基本信息</title>
</head>
<body>
<div class="modal-dialog" style="width: 340px;">
    <div class="modal-content">
        <div class="modal-header">
            <div class="fl" id="pagestitle" ></div>
            <div class="fr close-win" data-dismiss="modal">x</div>
        </div>
        <div class="modal-body">

            <div class="row">
                <div class="col-md-12">
                    <form action="" >
                        <div class="form-group form-group-base">
                            <span class="form-label">员工编号:</span>
                            <input value=""id="user" name="uesr"  type="text" class="form-control" autocomplete="off">
                        </div>
                        <div class="form-group form-group-base">
                            <span class="form-label">权限密码:</span>
                            <input value="" id="psd" name="psd"  type="password" class="form-control" autocomplete="off">
                        </div>
                        <div class="virtual-keyboard-base" style="overflow: auto;">
                            <ul>
                                <li>1</li><li>2</li><li>3</li>
                            </ul>
                            <ul>
                                <li>4</li><li>5</li><li>6</li>
                            </ul>
                            <ul>
                                <li>7</li><li>8</li><li>9</li>
                            </ul>
                            <ul>
                                <li>.</li><li>0</li><li class="clearLength">←</li>
                            </ul>
                        </div>
                        <div class="form-group form-group-base" id="permission" style="margin-top: 10px">
                            <button class="btn-default btn-lg btn-base btn-base-flex2" data-dismiss="modal">取消</button>
                            <button class="btn-default btn-lg btn-base btn-base-flex2 btnOk disabled" disabled="disabled">确定</button>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </div>
</div>
<script>
    <%
       String title = request.getParameter("title");
       String clearType = request.getParameter("clearType");
       String reason= request.getParameter("reason");
       String orderNo=request.getParameter("orderNo");

    %>
    var title = "<%=title%>";
    var clearType = "<%=clearType%>";
    var reason="<%=reason%>";
    var orderNo="<%=orderNo%>";

    $("#pagestitle").text(title);
    $(function () {
        impower.int();
    });
    var impower={
        int:function () {
            //加载虚拟键盘组件
            widget.keyboard({
                target: '.virtual-keyboard-base'
            });
            this.permission();
            console.log("121212")
        },
        permission:function () {
            var user="",psd=""
            var that=this;
            function notNull() {
                 user=$.trim($("#user").val()),psd=$.trim($("#psd").val())
                if(user!=""&& psd!=""){
                    $('#permission .btnOk').removeAttr("disabled").removeClass("disabled")
                }
                else {
                    $('#permission .btnOk').attr("disabled","disabled").addClass("disabled")
                }
            };
                $('#user,#psd').on('input propertychange focus', function() {
                    notNull();
                });
                $(".clearLength").click(function () {
                    setTimeout(function () {
                        user=$.trim($("#user").val()),psd=$.trim($("#psd").val());
                        if(user==""||psd==""){
                            $('#permission .btnOk').attr("disabled","disabled").addClass("disabled")
                        }
                    },100)

                });

            $(".btnOk").click(function () {
                $('#c-mod-fjs').modal("hide")
                $("#order-dialog").load("../orderdish.jsp",{"fromType":"1"});
                $("#order-dialog").modal('show');
                /*$.ajax({
                    url: _config.interfaceUrl.AuthorizeLogin,
                    method: 'POST',
                    contentType: "application/json",
                    data: JSON.stringify({
                        username:user ,
                        password: hex_md5(psd),
                        macAddress: '96121CBC21EF02256E9C5F2E602C5441',
                        loginType: '030201'
                    }),
                    dataType: "json",
                    success: function (data) {
                        console.log(data)
                        if(data.code === '0') {
                            $.ajax({
                                url: _config.interfaceUrl.AntiSettlementOrder,//反结算
                                method: 'POST',
                                contentType: "application/json",
                                data: JSON.stringify({
                                    'reason':reason,
                                    'orderNo':orderNo,
                                    'userName':user
                                }),
                                dataType: "json",
                                success:function (data) {
                                    console.log(data)
                                    if(data.result==='0'){
                                        //$("#c-mod-fjs").modal("hide")
                                        //window.location.href="../orderdish.jsp";
                                        $("#c-mod-fjs").load("../orderdish.jsp",{"fromType":"1"});
                                    }

                                }
                            })
                        } else {
                            widget.modal.alert({
                                cls: 'fade in',
                                content:'<strong>' + data.msg + '</strong>',
                                width:500,
                                height:500,
                                btnOkTxt: '',
                                btnCancelTxt: '确定'
                            });
                        }
                    }
                });*/
                return false//禁止form表单跳转
            })

        }

    }
</script>
</body>
</html>