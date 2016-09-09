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
                    <form action="">
                        <div class="form-group form-group-base">
                            <span class="form-label">员工编号:</span>
                            <input value="" name="uesr"  type="text" class="form-control" autocomplete="off">
                        </div>
                        <div class="form-group form-group-base">
                            <span class="form-label">权限密码:</span>
                            <input value="" name="psd"  type="text" class="form-control" autocomplete="off">
                        </div>
                        <div class="virtual-keyboard-base">
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
                                <li>.</li><li>0</li><li>←</li>
                            </ul>
                        </div>
                        <div class="form-group form-group-base">
                            <button class="btn-default btn-lg btn-base btn-base-flex2">取消</button>
                            <button class="btn-default btn-lg btn-base btn-base-flex2">确定</button>
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

%>
    var title = "<%=title%>";
    var clearType = "<%=clearType%>";
    $("#pagestitle").text(title)
</script>
</body>
</html>