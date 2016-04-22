<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<div>
    <h2>链接管理：</h2>
    <label>ip：</label><input id="ip" type="text" style="width:100px" value="192.168.10.5"/>
    <label>port：</label><input id="port" type="text" style="width:100px" value="9192"/>
    <input id="on" type="button" value="开启连接"/>
    <input id="off" type="button" value="关闭连接"/>
</div>
<br/>
<br/>
<br/>

<div>
    <h2>机具查询：</h2>
    <label>例子：{"group":"group_store","id":"00-50-56-c0-00-08"}</label>
    <label>为空查询全部机具</label>
    <br/>
    <input id="mqt" type="text" style="width:500px"/>
    <input id="qt" type="button" value="查询机具信息"/>

    <div id="ts"></div>
</div>
<br/>
<br/>
<br/>

<div>
    <h2>消息转发：</h2>
    <label>例子：group_store:00-50-56-c0-00-08,group_pad:5284047F4FFB4E04824A2FD1D1F0CD62</label>
    <br/>
    <label>机具地址：</label>
    <%--<input id="add" type="text" style="width:500px;height: 600px;"/>--%>
    <textarea id="add" style="width: 700px;height: 300px"></textarea>
    <br/>
    <label>例子：say 嗨！</label>
    <br/>
    <label>转发信息：</label>
    <input id="msg" type="text" style="width:500px"/>
    <input id="zf" type="button" value="异步转发"/>
    <input id="zfsync" type="button" value="同步转发"/>
</div>
<br/>
<br/>
<br/>

<div>
    <h2>单个机具是否在线：</h2>
    <label>机具类型(group_store,group_pad)：</label>
    <input id="targetType" type="text"/>
    <label>机具id(00-50-56-c0-00-08)：</label>
    <input id="targetId" type="text" style="width:500px"/>
    <input id="isOnline" type="button" value="查询"/>
</div>
<br/>
<br/>
<hr/>
<hr/>
<hr/>
<div>
    <h2>查询所有设备：</h2>
    <input id="getAllDevice" type="button" value="查询该门店所有设备"/>

    <div id="allDevice"></div>
</div>
<div>
    <h2>给指定发送同步消息：</h2>
    <label>消息内容：</label>
    <textarea id="content" style="width: 300px;height: 300px"></textarea>
    <label>设备ID：</label>
    <input id="sbID" type="text"/>
    <input id="sbSyncSend" type="button" value="发送"/>
</div>
<div>
    <h2>给设备定时发送消息：</h2>
    <label>消息内容：</label>
    <textarea id="msgTimeContent" style="width: 300px;height: 300px"></textarea>
    <br/>
    <label>是否添加内容凑32K消息：</label>
    <label>是</label>
    <input type="radio" name="isPlus" checked="checked" value="1"/>
    <label>否</label>
    <input type="radio" name="isPlus" value="0"/>
    <label>是否是互斥消息：</label>
    <label>是</label>
    <input type="radio" name="isTimeSingle" checked="checked" value="1"/>
    <label>否</label>
    <input type="radio" name="isTimeSingle" value="0"/>
    <label>每隔多少秒</label>
    <input type="text" id="seconds"/>
    <br/>
    <br/>

    <div>
        <input id="sendTimeMsgAll" type="button" value="给所有设备发送"/>
        <input id="sendTimeMsg" type="button" value="给所有在线设备发送"/>
        <input id="sendTimeMsgWatch" type="button" value="给所有在线watch发送"/>
        <input id="sendTimeMsgPad" type="button" value="给所有在线pad发送"/>
        <input id="closeTime" type="button" value="关闭定时任务"/>
    </div>
</div>
<div>
    <h2>给设备发送消息：</h2>
    <label>消息内容：</label>
    <textarea id="msgContent" style="width: 300px;height: 300px"></textarea>
    <label>是否是互斥消息：</label>
    <label>是</label>
    <input type="radio" name="isSingle" checked="checked" value="1"/>
    <label>否</label>
    <input type="radio" name="isSingle" value="0"/>
    <input id="sendMsgAll" type="button" value="给所有设备发送"/>
    <input id="sendMsg" type="button" value="给所有在线设备发送"/>
    <input id="sendMsgWatch" type="button" value="给所有在线watch发送"/>
    <input id="sendMsgPad" type="button" value="给所有在线pad发送"/>
</div>
<div>
    <h2>查询所有设备离线消息：</h2>
    <label>机具类型(group_store,group_pad)：</label>
    <input id="group" type="text"/>
    <label>机具id(00-50-56-c0-00-08)：</label>
    <input id="id" type="text" style="width:500px"/>
    <input id="getAllOfflineMsg" type="button" value="查询离线消息"/>

    <div id="offlinemsg"></div>
</div>
<script type="text/javascript">
    var global_ctxPath = '<%=request.getContextPath()%>';
</script>
<script src="<%=request.getContextPath()%>/scripts/jquery.js" type="text/javascript">
</script>
<script>
    $("#sbSyncSend").click(function () {
        $.get(global_ctxPath + "/datasnap/rest/TServerMethods1/broadcastmsg/" + $("#sbID").val() + "/" + $("#content").val() + "/", function (data) {
                    alert(data);
                }
        );
    });
    $("#getAllOfflineMsg").click(function () {
        $.get("offlineMsgService?group=" + $("#group").val() + "&id=" + $("#id").val(), function (data) {
                    var d = "";
                    var datas = eval(data);
                    for (var o in datas) {
                        var group = datas[o].deviceGroup;
                        var id = datas[o].deviceId;
                        var msgType = datas[o].msgType;
                        var content = datas[o].content;
                        d += "<label>deviceGroup=" + group + "---deviceId=" + id + "</label>";
                        d += "<label>---msgType=" + msgType + "--content=" + content + "</label>";
                        d += "<br/>";
                    }
                    d += "<label>总共：" + datas.length + "个消息</label>";
                    d += "<br/>";
                    $("#offlinemsg").html(d);
                }
        );
    });
    $("#getAllDevice").click(function () {
        $.get("getAllDevice", function (data) {
                    var d = "";
                    var datas = eval(data);
                    for (var o in datas) {
                        var id = datas[o].id;
                        var group = datas[o].deviceGroup;
                        var deviceId = datas[o].deviceId;
                        var userId = datas[o].userId;
                        var tableNo = datas[o].tableNo;
                        var ssId = datas[o].ssId;
                        d += "<label>业务层设备ID=" + id + "</label>";
                        d += "<label>---deviceGroup=" + group + "--deviceId=" + deviceId + "</label>";
                        d += "<label>---userId=" + userId + "--tableNo=" + tableNo + "</label>";
                        d += "<label>---ssId=" + ssId + "</label>";
                        d += "<br/>";
                    }
                    d += "<label>总共：" + datas.length + "个机具</label>";
                    d += "<br/>"
                    $("#allDevice").html(d);
                }
        );
    });
    $("#sendTimeMsgWatch").click(function () {
        var isS = $('input[name="isTimeSingle"]:checked ').val();
        var isPlus = $('input[name="isPlus"]:checked ').val();
        var seconds = $("#seconds").val();
        $.get(global_ctxPath + "/datasnap/rest/TServerMethods1/broadcastmsg1/group_watch/1/1/" + $("#msgTimeContent").val() + "/" + isS + "/" + seconds + "/" + isPlus + "/", function (data) {
                }
        );
    });
    $("#sendTimeMsgPad").click(function () {
        var isS = $('input[name="isTimeSingle"]:checked ').val();
        var isPlus = $('input[name="isPlus"]:checked ').val();
        var seconds = $("#seconds").val();
        $.get(global_ctxPath + "/datasnap/rest/TServerMethods1/broadcastmsg1/group_pad/1/1/" + $("#msgTimeContent").val() + "/" + isS + "/" + seconds + "/" + isPlus + "/", function (data) {
                }
        );
    });
    $("#sendTimeMsgAll").click(function () {
        var isS = $('input[name="isTimeSingle"]:checked ').val();
        var isPlus = $('input[name="isPlus"]:checked ').val();
        var seconds = $("#seconds").val();
        $.get(global_ctxPath + "/datasnap/rest/TServerMethods1/broadcastmsg1/1/1/" + $("#msgTimeContent").val() + "/" + seconds + "/" + isPlus + "/", function (data) {
                }
        );
    });
    $("#sendTimeMsg").click(function () {
        var isS = $('input[name="isTimeSingle"]:checked ').val();
        var isPlus = $('input[name="isPlus"]:checked ').val();
        var seconds = $("#seconds").val();
        $.get(global_ctxPath + "/datasnap/rest/TServerMethods1/broadcastmsg1/1/1/" + $("#msgTimeContent").val() + "/" + isS + "/" + seconds + "/" + isPlus + "/", function (data) {
                }
        );
    });
    $("#closeTime").click(function () {
        $.get(global_ctxPath + "/datasnap/rest/TServerMethods1/broadcastmsg1/", function (data) {
                }
        );
    });
    $("#sendMsgWatch").click(function () {
        var isS = $('input[name="isSingle"]:checked ').val();
        $.get(global_ctxPath + "/datasnap/rest/TServerMethods1/broadcastmsg/group_watch/1/1/" + $("#msgContent").val() + "/" + isS + "/", function (data) {
                }
        );
    });
    $("#sendMsgPad").click(function () {
        var isS = $('input[name="isSingle"]:checked ').val();
        $.get(global_ctxPath + "/datasnap/rest/TServerMethods1/broadcastmsg/group_pad/1/1/" + $("#msgContent").val() + "/" + isS + "/", function (data) {
                }
        );
    });
    $("#sendMsgAll").click(function () {
        var isS = $('input[name="isSingle"]:checked ').val();
        $.get(global_ctxPath + "/datasnap/rest/TServerMethods1/broadcastmsg/1/1/" + $("#msgContent").val() + "/", function (data) {
                    alert(data.Data);
                }
        );
    });
    $("#sendMsg").click(function () {
        var isS = $('input[name="isSingle"]:checked ').val();
        $.get(global_ctxPath + "/datasnap/rest/TServerMethods1/broadcastmsg/1/1/" + $("#msgContent").val() + "/" + isS + "/", function (data) {
                }
        );
    });
    $("#on").click(function () {
        $.get("on?ip=" + $("#ip").val() + "&port=" + $("#port").val(), function (data) {
                    alert(data);
                }
        );
    });
    $("#off").click(function () {
        $.get("off", function (data) {
                    alert(data);
                }
        );
    });
    $("#isOnline").click(function () {
        $.get("isOnline?targetType=" + $("#targetType").val() + "&targetId=" + $("#targetId").val(), function (data) {
                    alert(data);
                }
        );
    });
    $("#qt").click(function () {
        $.get("queryTerminals?msg=" + $("#mqt").val(), function (result) {
                    var d = "";
                    var cc = eval(result);
                    for (var o in cc) {
                        var group = cc[o].group;
                        var id = cc[o].id;
                        d += "<input type='checkbox'" + "value='" + group + ":" + id + "'/>"
                        d += "<label>" + group + ":" + id + "</label>";
                        d += "<br/>";
                    }
                    d += "<label>总共：" + cc.length + "个机具在线</label>";
                    d += "<br/>"
                    d += " <input id='tsQX' type='button' value='全选' onclick='quan_xuan()'/>"
                    d += " <input id='f' type='button' value='设为转发地址' onclick='xuan_f()'/>"
                    $("#ts").html(d);
                }
        );
    });
    function quan_xuan() {
        var obj = document.getElementById("ts").getElementsByTagName("input");
        for (var i = 0; i < obj.length - 2; i++) {
            obj[i].checked = true;
        }
    }
    function xuan_f() {
        var obj = document.getElementById("ts").getElementsByTagName("input");
        var adds = "";
        for (var i = 0; i < obj.length - 2; i++) {
            if (obj[i].checked) {
                adds += (obj[i].value) + ","
            }
        }
        $("#add").val(adds);
    }
</script>
<script>
    $("#zf").click(function () {
        var a = $("#add").val();
        var b = $("#msg").val();
        $.post("forward", {add: a, msg: b}, function (data) {
//            alert(data);
        });
    });
    $("#zfsync").click(function () {
        var a = $("#add").val();
        var b = $("#msg").val();
        $.post("forwardSync", {add: a, msg: b}, function (data) {
            alert(data);
        });
    });
</script>
</body>
</html>
