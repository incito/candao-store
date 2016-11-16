<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/counter.css?v=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/tools/switchery/style.css"/>
    <script src="<%=request.getContextPath()%>/scripts/jquery-3.1.0.min.js"></script>
    <script src="<%=request.getContextPath()%>/scripts/jquery-ui.min.js"></script>
    <script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
    <script src="<%=request.getContextPath()%>/scripts/projectJs/tables.js?v=b"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/commons.js"></script>
    <script type="text/javascript">
        var global_Path = '<%=request.getContextPath()%>';
    </script>
    <script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
    <script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
</head>
<body>
<div class="ky-content content-iframe">
    <div class="counter-content">
        <p class="counter-content-title">
            <span class="btn  counter-type-add counter-add tables-type-active"
                  style="float: left;padding: 5px 12px;border-radius: 0px;"
                  type="0" id="allTables">全部餐台
            </span>
            <span class="btn counter-type-add counter-add"
                  style="float: left;margin-left: 10px;padding: 5px 12px;border-radius: 0px;"
                  type="1" id="serviceTables">服务费餐台
            </span>


            <button class="btn btn-default counter-type-add counter-add" style="float: right" type="button"
                    id="counter-type-add"
                    onclick="addArea()"><i class="icon-plus"></i> 餐厅分区
            </button>
            <button class="btn btn-default counter-type-add counter-add"
                    style="float: right;display: none;margin-left: 10px" type="button" id="dinnerTablecancel"><i
                    class="icon-minus"></i> <span>取消</span></button>
            <button class="btn btn-default counter-type-add counter-add" style="float: right;margin-right:10px;"
                    type="button"
                    id="dinnerTable"><i class="icon-edit"></i> <span>自定义餐台排序</span></button>
        </p>

        <div class="nav-counter-prev" style="display: none"><i class="icon-chevron-left"></i></div>

        <ul class="nav-counter" id="nav-tables">
            <c:forEach var="item" items="${areanames}" varStatus="i">
                <c:if test="${i.index==0}">
                    <li id="${item.areaid}" areaSort="${item.areaSort}" class="active" onmousedown="doMenu(event,this)"
                        onmouseover="delDisplay(this)" onmouseout="delHidden(this)"
                        onclick="oneclickTableType(this.id)" ondblclick="editArea(this.id)">
                        <span>${item.areaname}</span><span>(${item.tableCount})</span>
                        <i class="icon-remove hidden" onclick="showDeleteArea(this.id)"></i>
                    </li>
                </c:if>
                <c:if test="${i.index!=0}">
                    <li id="${item.areaid}" areaSort="${item.areaSort}" class="" onmouseover="delDisplay(this)"
                        onmouseout="delHidden(this)"
                        onclick="oneclickTableType(this.id)" onmousedown="doMenu(event,this)"
                        ondblclick="editArea(this.id)">
                        <span>${item.areaname}</span><span>(${item.tableCount})</span>
                        <i class="icon-remove hidden" onclick="showDeleteArea()"></i>
                    </li>

                </c:if>

            </c:forEach>
        </ul>
        <div class="nav-counter-next" style="display: none"><i class="icon-chevron-right"></i></div>
        <ul class="tables-right-tab right-tab hidden">
            <li id="tables-right-tab1" onclick="addArea()"><i class="icon-plus"></i><span>添加分区</span></li>
            <li id="tables-right-tab2" onclick="editArea()"><i class="icon-edit"></i><span>编辑分区</span></li>
            <li id="tables-right-tab3" onclick="showDeleteArea()"><i class="icon-minus"></i><span>删除分区</span></li>
        </ul>
        <div class="nav-counter-tab">
            <c:forEach var="item" items="${datas}" varStatus="i">
                <c:choose>
                    <c:when test="${item.chargeOn==0}">
                        <div class="counter-detail-box" tabletype='${item.tabletype}' id="${item.tableid}"
                             chargeOn="${item.chargeOn}"
                             onmouseover="delDisplay(this)" onmouseout="delHidden(this)">
                            <p>${item.tableName }</p>
                            <p>(${item.personNum }人桌)</p>
                            <i class="icon-remove hidden"
                               onclick="delTablesDetail(&apos;${item.tableid }&apos;,&apos;${item.tableName }&apos;,event)"></i>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="counter-detail-box counter-detailService-box" tabletype='${item.tabletype}'
                             id="${item.tableid}" chargeOn="${item.chargeOn}"
                             onmouseover="delDisplay(this)" onmouseout="delHidden(this)">
                            <p>${item.tableName }</p>
                            <p>(${item.personNum }人桌)</p>
                            <i class="icon-remove hidden"
                               onclick="delTablesDetail(&apos;${item.tableid }&apos;,&apos;${item.tableName }&apos;,event)"></i>
                        </div>
                    </c:otherwise>
                </c:choose>

            </c:forEach>

            <button class="btn btn-default counter-add" type="button" id="tables-detailMain-Add"><i
                    class="icon-plus"></i> 添加餐台
            </button>
        </div>

    </div>
</div>
<!--添加餐台详情框 -->
<div class="modal fade counter-dialog in " id="tables-detailAdd-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 435px;">
            <div class="modal-body">
                <div class="counter-dialog-header">
                    <span id="editTitle2">添加餐台</span>
                    <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
                </div>
                <hr class="ky-hr">
                <form action="" method="post" class="form-horizontal " name="add-form1" id="add-form1">

                    <input type="hidden" name="tableid" id="tableid">
                    <div class="form-group" style="display:none;">
                        <label class="col-xs-5 control-label "><span class="required-span">*</span>餐台编号11：</label>
                        <div class="col-xs-8">
                            <input type="text" name="tableNo" id="tableNo" maxlength="200"
                                   class="form-control required">
                            <font color="red" id="tableNo_tip" class="error"></font>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-xs-4 control-label"><span class="required-span">*</span>餐台名称：</label>
                        <div class="col-xs-7">
                            <input type="text" name="tableName" id="tableName" maxlength="5" class="form-control  ">
                            <font color="red" id="tableName_tip" class="error"></font>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-4 control-label"><span class="required-span">*</span>餐台类型：</label>
                        <div class="col-xs-7">
                            <select class="form-control myInfo-select-addrW tabletype" id="tabletype" name="tabletype">

                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-4 control-label"><span class="required-span">*</span>就餐人数：</label>
                        <div class="col-xs-7">
                            <div class="input-group  unit-style">
                                <input type="text" name="personNum" id="personNum" maxlength="3"
                                       onkeyup="clearNoNum(this);" class="form-control ">
                                <span class="input-group-addon" style="color: #282828;">人</span>

                            </div>
                            <font color="red" id="personNum_tip" class="error"></font>
                        </div>
                    </div>
                    <div class="form-group" style="display:none;">
                        <input id="areaid" name="areaid"/>
                    </div>
                    <%--服务费--%>

                    <div class="form-group">
                        <label class="col-xs-4 control-label">餐台服务费：</label>
                        <div class="col-xs-7" id="serviceCharge_onoff">
                            <label class="col-xs-4 control-label" style="text-align:left"><input type="radio"
                                                                                                 name="kaiq"
                                                                                                 checked="checked"
                                                                                                 value="0"><span
                                   >关闭</span></label>
                            <label class="col-xs-4 control-label" style="text-align:left"><input type="radio"
                                                                                                 name="kaiq"
                                                                                                 value="1"><span
                                    >开启</span></label>
                        </div>
                    </div>
                    <%--服务费--计算方式--%>
                    <div class="form-group" id="serviceCharge_count" style="display: none">
                        <label class="col-xs-4 control-label">计算方式：</label>
                        <div class="col-xs-2" style="padding-right: 2px">
                            <select class="form-control serviceCharge_count_select" style="padding: 2px 2px ">
                                <option value="0">比例</option>
                                <option value="1">固定</option>
                                <option value="2">时长</option>
                            </select>
                        </div>
                        <%--比例--%>
                        <div class="col-xs-6 serviceCharge_count_proportion"
                             style="padding-left: 2px;padding-right: 2px;">
                            <div class="col-xs-5" style="padding-left: 2px;padding-right: 2px">
                                <select class="form-control" style="padding: 2px 2px; ">
                                    <option value="0">应收金额</option>
                                    <option value="1">实收金额</option>
                                </select>
                            </div>
                            <div class="col-xs-6" style="padding-left: 2px;padding-right: 2px;width: 72px">
                                <input type="text" class="form-control  serviceCharge_count_timer" maxlength="3">
                            </div>
                        </div>
                        <%--固定--%>
                        <div class="col-xs-5 serviceCharge_count_fixed"
                             style="padding-left: 2px;padding-right: 2px;display: none;width: 158px">
                            <input type="text" class="form-control  serviceCharge_count_timer1">
                        </div>
                        <%--时长--%>
                        <div class="col-xs-6 serviceCharge_count_time "
                             style=";padding-left: 2px;padding-right: 2px;display: none">
                            <div class="col-xs-5" style="padding-left: 2px;padding-right: 2px">
                                <select class="form-control timerLength" style="padding: 2px 2px; ">
                                    <option value="10">10分钟</option>
                                    <option value="15">15分钟</option>
                                    <option value="30">30分钟</option>
                                    <option value="60">1小时</option>
                                    <option value="120">2小时</option>
                                </select>
                            </div>
                            <div class="col-xs-6 " style="padding-left: 2px;padding-right: 2px;width: 72px;">
                                <input type="text" name="personNum" class="form-control  serviceCharge_count_timer2">
                            </div>
                        </div>
                        <font color="red" class="error serviceCharge_count_timer_tip" style="margin-left: 151px"></font>
                    </div>
                    <%--服务费--参与折扣--%>
                    <%--<div class="form-group" id="serviceCharge_favorable" style="display: none">
                        <label class="col-xs-4 control-label">参与折扣：</label>
                        <div class="col-xs-7">
                            <label class="col-xs-4 control-label" style="text-align:left"><input type="radio"
                                                                                                 name="favorable" value="0" checked="checked"><span
                                    class="minpCheckboxSpan">关闭</span></label>
                            <label class="col-xs-4 control-label" style="text-align:left"><input type="radio" value="1"
                                                                                                 name="favorable"><span
                                    class="minpCheckboxSpan">开启</span></label>
                        </div>
                    </div>--%>

                    <div class="form-group disabled-input counter-input-select">
                        <label class="col-xs-4 control-label"><input type="checkbox" name="enableCheck" id="minp"
                                                                     onclick="checkit(this.checked,this.id)"><span
                                class="minpCheckboxSpan">最低消费：</span></label>
                        <div class="col-xs-7">
                            <div class="input-group   unit-style">
                                <input type="text" name="minprice" id="minprice" aria-describedby="basic-addon1"
                                       maxlength="8" onkeyup="oneDecimal(this,2,8,99999.99);" class="form-control ">
                                <span class="input-group-addon">元</span>

                            </div>
                            <font color="red" id="minConsu_tip" class="error">必须大于0</font>
                        </div>
                    </div>
                    <div class="form-group disabled-input counter-input-select">
                        <label class="col-xs-4 control-label"><input type="checkbox" id="fixp"
                                                                     onclick="checkit(this.checked,this.id)"
                                                                     name="enableCheck"><span class="fixpCheckboxSpan">固定使用费：</span></label>
                        <div class="col-xs-7">
                            <div class="input-group  unit-style ">
                                <input type="text" name="fixprice" id="fixprice" aria-describedby="basic-addon1"
                                       maxlength="8" onkeyup="oneDecimal(this,2,8,99999.99);"
                                       class="form-control ">
                                <span class="input-group-addon">元</span>

                            </div>
                            <font color="red" id="fixConsu_tip" class="error">必须大于0</font>
                        </div>
                    </div>


                    <div class="btn-operate">
                        <button class="btn btn-cancel in-btn135" type="button" onclick="hideDialog()">取消</button>
                        <div class="btn-division"></div>
                        <button class="btn btn-save in-btn135" id="btnsave1">确认</button>
                    </div>
                </form>
            </div>


        </div>

    </div>
</div>

<!--删除弹出框-->
<div class="modal fade counter-dialog dialog-sm in counter-dialog-del" id="tables-detailDel-dialog"
     data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="dialog-sm-header">
                    <span>确认删除</span>
                    <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
                </div>
                <hr class="ky-hr">
                <form action="" method="post" class="form-horizontal " name="">
                    <input id="showTableId" value="" type="hidden">
                    <div class="del-sm-info">
                        <p class="counter-del-p1"><img src="../images/del-tip.png">确认删除该餐台？</p>
                    </div>
                    <div class="btn-operate">
                        <button class="btn btn-cancel" type="button" data-dismiss="modal">取消</button>
                        <div class="btn-division"></div>
                        <button class="btn btn-save" id="counter-type-del" type="button" onclick="del()">确认</button>
                    </div>

                </form>
            </div>
        </div>
    </div>

</div>


<div class="modal fade counter-dialog in counter-dialog-del " id="areas-detailDel-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="counter-dialog-header">
                    <span>确认删除</span>
                    <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
                </div>
                <hr class="ky-hr">
                <form action="" method="post" class="form-horizontal " name="">
                    <input id="showTableTypeId" value="" type="hidden"/>
                    <div class="del-info">
                        <p class="counter-del-p1"><img src="../images/del-tip.png"></i>确认删除该分区？</p>
                    </div>
                    <div class="btn-operate">
                        <button class="btn btn-cancel" type="button" data-dismiss="modal">取消</button>
                        <div class="btn-division"></div>
                        <button class="btn btn-save" id="counter-type-del2" type="button" onclick="delAreaAndTables()">
                            确认
                        </button>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>


<div class="modal fade counter-dialog in" id="areas-detailAdd-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="counter-dialog-header">
                    <span id="editTitle1">添加分区</span>
                    <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
                </div>
                <hr class="ky-hr">
                <form action="" method="post" class="form-horizontal " name="" id="add-form2">
                    <input type="hidden" id="areaidB" name="areaNoB"/>
                    <div class="form-group">
                        <label class="col-xs-4 control-label">分区名称：</label>
                        <div class="col-xs-8">
                            <input type="text" name="areanameB" id="areanameB" maxlength="5" class="form-control ">
                            <font color="red" id="areanameB_tip" class="error"></font>
                        </div>
                    </div>

                    <div class="btn-operate">
                        <button class="btn btn-cancel" type="button" onclick="hideDialog()">取消</button>
                        <div class="btn-division"></div>
                        <button class="btn btn-save" id="btnsave2">确认</button>
                    </div>
                </form>
            </div>


        </div>

    </div>
</div>


<script>
    var tableJson = null;
    customTable.int();//餐台自定义排序
    var count = 0;
    $('.tables-roll-left').click(function () {
        var marginl = -147.5 * (++count) + 'px';
        $('.tables-comboList:eq(0)').css('margin-left', marginl);
    });
    $('.tables-roll-right').click(function () {
        var marginl = -147.5 * (--count) + 'px';
        $('.tables-comboList:eq(0)').css('margin-left', marginl);
    });


    function clearNoNum(obj) {
        // 先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d]/g, "");
    }


    function oneDecimal(obj, num, maxlength, maxvalue) {

        // 先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d.]/g, "");
        // 必须保证第一个为数字而不是.
        obj.value = obj.value.replace(/^\./g, "");
        // 保证只连续出现一个.而没有多个.
        //obj.value = obj.value.replace(/\.{2,}/g,".");
        // 保证.只出现一次，而不能出现两次以上
        obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        if (num != undefined && num != null && num != "") {
            if (num == "2") {
                // 小数点后只保留3位小数
                obj.value = obj.value.replace(/(\.\d\d)\d+/ig, "$1");
            } else if (num == "3") {
                // 小数点后只保留3位小数
                obj.value = obj.value.replace(/(\.\d\d\d)\d+/ig, "$1");
            }
        } else {
            // 小数点后只保留一位小数
            obj.value = obj.value.replace(/(\.\d)\d+/ig, "$1");
        }

        if (parseFloat(obj.value) > maxvalue) {
            //obj.value=maxvalue;
            obj.value = obj.value.substring(0, 5);
            //$(obj).parent().find("label").show().delay(4000).hide(500);
        }

        // 字符串长度最大为maxlength
        if (maxlength != undefined && maxlength != null && maxlength != "") {
            if (obj.value.length > maxlength) {
                obj.value = obj.value.substr(0, maxlength);
            }
        }
    }


</script>
</body>
</html>