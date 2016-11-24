var tbPrinterAreaList = [];
var flag_prev = 0;
var serviceTablesJson = [];//有餐台服务费的餐台集合
var tableJson=[];//全部餐台
/************
 * 工具类 utils
 ************/
var utils = utils || {};
$(document).ready(function () {
    $(document).bind('ajaxError', function () {
        if ($('.errorAlert').length < 1) {
            widget.modal.alert({
                cls: 'fade in errorAlert',
                content: '<div><img src="../images/del-tip.png" style="margin-right: 20px">后台服务错误，请稍后重试</div>',
                width: 360,
                height: 500,
                btnOkTxt: '确定 ',
                btnCancelTxt: '',
                btnOkCb: function () {
                    $('.modal-alert:last,.fade:last').remove()
                }
            });
        }
    });
    /*餐台分类鼠标滚动*/
    var dom = $("#nav-tables")[0];
    var user_agent = navigator.userAgent;
    if (user_agent.indexOf("Firefox") != -1) {// Firefox
        dom.addEventListener("DOMMouseScroll", addEvent_T, !1);

    } else if (user_agent.indexOf("MSIE") != -1) {// Firefox
        dom.attachEvent("onmousewheel", addEvent_T, !1);

    } else {
        dom.addEventListener("mousewheel", addEvent_T, !1);

    }

    $("#personNum").change(function () {
        $("#personNum_tip").text("");
    });
    $("#tableName").change(function () {
        $("#tableName_tip").text("");
    });
    $("#areanameB").change(function () {
        $("#areanameB_tip").text("");
    });
    /*当餐台服务费开启时候，输入清空提示信息*/
    $(".serviceCharge_count_timer,.serviceCharge_count_timer1,.serviceCharge_count_timer2").keyup(function () {
        $(".serviceCharge_count_timer_tip").text("");
    });
    findPrinterArea();
    $("img.img-close").hover(function () {
        $(this).attr("src", global_Path + "/images/close-active.png");
    }, function () {
        $(this).attr("src", global_Path + "/images/close-sm.png");
    });
    var temp = document.getElementById('nav-tables');
    temp.oncontextmenu = function () {
        return false;
    };

    document.querySelector(".tables-right-tab").oncontextmenu = function () {
        return false;
    };

    showAndHidden();
    $("#add-form1").validate(
        {

            submitHandler: function (form) {
                var vcheck = true;
                if ($("#personNum").val().trim() == "") {
                    $("#personNum_tip").text("就餐人数不能为空");
                    $("#personNum").focus();
                    $("#personNum").addClass("error");
                    vcheck = false;
                }
                if ($("#tableName").val().trim() == "") {
                    $("#tableName_tip").text("餐台名称不能为空");

                    $("#tableName").focus();
                    $("#tableName").addClass("error");
                    vcheck = false;
                }
                var chargeOn = $('#serviceCharge_onoff input:checked').val();//是否开启服务费 0关闭 1开启
                if (chargeOn == "1") {
                    var chargeType = $('.serviceCharge_count_select').val()//服务费计算方式 0比例 1 固定 2 时长
                    if ($(".serviceCharge_count_timer").val().trim() == "" && chargeType == '0') {
                        $(".serviceCharge_count_timer_tip").text("服务费比例不能为空");

                        $(".serviceCharge_count_timer").focus();
                        $(".serviceCharge_count_timer").addClass("error");
                        vcheck = false;
                    }
                    if ($(".serviceCharge_count_timer").val().trim() != "" && chargeType == '0') {
                        var reg = /^(100|[1-9]\d|\d)$/ //只能输入0-100的整数
                        reg.lastIndex = 0;//正则开始为0
                        var val = $.trim($(".serviceCharge_count_timer").val());
                        if (reg.test(val) === false) {
                            $(".serviceCharge_count_timer").val().trim()
                            $(".serviceCharge_count_timer_tip").text("服务费比例只能输入0~100的整数");
                            $(".serviceCharge_count_timer").focus();
                            $(".serviceCharge_count_timer").addClass("error");
                            vcheck = false;
                        }

                    }

                    if ($(".serviceCharge_count_timer1").val().trim() == "" && chargeType == '1') {
                        $(".serviceCharge_count_timer_tip").text("服务费金额不能为空");

                        $(".serviceCharge_count_timer1").focus();
                        $(".serviceCharge_count_timer1").addClass("error");
                        vcheck = false;
                    }
                    if ($(".serviceCharge_count_timer1").val().trim() != "" && chargeType == '1') {
                        var val = $.trim($(".serviceCharge_count_timer1").val());
                        if (customTable.ismoney(val) === false) {
                            $(".serviceCharge_count_timer_tip").text("服务费金额只能输入最多两位小数或整数");

                            $(".serviceCharge_count_timer1").focus();
                            $(".serviceCharge_count_timer1").addClass("error");
                            vcheck = false;
                        }

                    }
                    if ($(".serviceCharge_count_timer2").val().trim() == "" && chargeType == '2') {
                        $(".serviceCharge_count_timer_tip").text("服务费金额不能为空");
                        $(".serviceCharge_count_timer2").focus();
                        $(".serviceCharge_count_timer2").addClass("error");
                        vcheck = false;
                    }
                    if ($(".serviceCharge_count_timer2").val().trim() != "" && chargeType == '2') {
                        var val = $.trim($(".serviceCharge_count_timer2").val());
                        if (customTable.ismoney(val) === false) {
                            $(".serviceCharge_count_timer_tip").text("服务费金额只能输入最多两位小数或整数");
                            $(".serviceCharge_count_timer2").focus();
                            $(".serviceCharge_count_timer2").addClass("error");
                            vcheck = false;
                        }

                    }
                }


                if ($("#minp").is(":checked") && ($('#minprice').val() === '0' || $('#minprice').val() === '')) {
                    vcheck = false;
                    $('#minConsu_tip').text('必须大于0');
                }

                if ($("#fixp").is(":checked") && ($('#fixprice').val() === '0' || $('#fixprice').val() === '')) {
                    vcheck = false;
                    $('#fixConsu_tip').text('必须大于0');
                }
                if (vcheck) {
                    $(".serviceCharge_count_timer_tip").text('')//提示信息为空;
                    if (check_validate()) {
                        save_table();
                    } else {
                    }
                }
            }
        });
    $("#add-form2").validate(
        {
            submitHandler: function (form) {
                var vcheck = true;
                if ($("#areanameB").val().trim() == "") {
                    $("#areanameB_tip").text("分区名称不能为空");
                    $("#areanameB").focus();
                    $("#areanameB").addClass("error");
                    vcheck = false;
                }
                if (vcheck) {
                    if (check_area()) {
                        save_Area();
                    } else {
                    }
                }

            }
        });
    $("body").click(function () {


        $(".tables-right-tab").addClass("hidden");
    });

    getTableTag();
    getPrinterTag();
//	getPersonNumTag();
    getTabletypeTag();

//	getbuildingNoANDTableTypeTag();


    $(".icon-remove").mouseover(function () {
        $(this).show();

    });


//	$("button.tables-type").click(function(e){
//		$(".nav-tables li").removeClass("active");
//		$(this).parent().addClass("active");
//		$(".tables-right-tab").addClass("hidden");
//		stopEvent(window.event||event);
//	});


    /*select */
    $(".select-box").click(function () {
        $(".select-content").toggleClass("hidden");
    });
    $(".select-multi input[type='checkbox']").click(function () {
        var text = '';
        $(".select-multi").find("input[type='checkbox']").each(function () {
            if ($(this).is(":checked")) {
                text = text + "," + $(this).parent().text();
            }
        });
        text = text.substr(1, text.length - 1);
        $(".select-multi").prev(".select-box").find("input").val(text)

    });
//	$("#tables-detail-save").click(function(){
//		var name = $("input[name='tablesName']").val();
//		var code = $("input[name='tablesCode']").val();
//		var temp = '<div class="tables-detail-box" onmouseover="delDisplay(this)" onmouseout="delHidden(this)"><p class="tables-detail-name">'+name+'</p><p class="tables-detail-code">（'+code+'）</p><i class="icon-remove hidden" onclick="deltablesDetail(this)"></i></div>';
//		$(".tables-detail-add").before(temp);
//		$("#tables-detailAdd-dialog").modal("hide");
//	});


    /*菜品口味,菜品标签中添加按钮*/
    $(".tagAdd-btn button").click(function () {
        var id = $(this).parent().attr("id");
        var num = id.substr(id.length - 1, 1);
        var text = $("#tagName" + num).val();
        var temp = '<div><button class="btn btn-default" onclick="addTag(this)" type="button">' + text + '</button></div>';
        $("#tables-tag-table" + num).append(temp);

    });
    /*菜品口味弹出框显示*/
    $("#tables-taste-add").click(function () {
        $("#tables-tasteAdd-dialog").modal("show");
    });
    /*菜品口味确定按钮*/
    $("#tables-taste-save").click(function () {
        var text = "";
        $(".tables-tasteAdd-dialog .tables-tag-select").find("button").each(function () {
            text = text + $(this).text() + ",";
        });
        text = text.substr(0, text.length - 1);
        $("#tables-taste-add").text(text);
        $("#tables-tasteAdd-dialog").modal("hide");

    });
    /*菜品标签弹出框显示*/
    $("#tables-label-add").click(function () {
        $("#tables-labelAdd-dialog").modal("show");

    });
    /*菜品标签确定按钮*/
    $("#tables-label-save").click(function () {
        $(".tables-labelAdd-dialog .tables-tag-select").find("button").each(function () {
            var temp = '<label class="checkbox-inline"><input type="checkbox" value="">' + $(this).text() + '</label>';
            $(".tables-checkbox").append(temp);
        });
        $("#tables-labelAdd-dialog").modal("hide");

    });
    /*添加一组计价单位*/
    $(".charge-unit-addBtn").click(function () {
        var temp = '<div class="form-group">';
        temp = temp + '<label class="col-xs-2 control-label">计价单位：</label><div class="col-xs-2"><input type="text" value="" class="form-control"></div>'
        temp = temp + '<label class="col-xs-2 control-label">价格：</label><div class="col-xs-2"><input type="text" value="" class="form-control"></div>';
        temp = temp + '<label class="col-xs-2 control-label">会员价：</label><div class="col-xs-2"><input type="text" value="" class="form-control"><div></div>';
        $(".tables-charge-unit hr").before(temp);

    });
    /*添加已有菜品至该分类*/
    $("#tables-other-tab3").click(function () {
        $("#tables-existAdd-dialog").modal("show");
        $(".tables-other-right").addClass("hidden");
    });
    $('.glyphicon').click(function () {
        if ($(this).hasClass('glyphicon-chevron-up')) {
            $(this).removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
        } else if ($(this).hasClass('glyphicon-chevron-down')) {
            $(this).removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
        }
    });
    $("#tables-existAdd-dialog input[type='checkbox']").click(function () {

        var existText = '';
        var panelId = $(this).parent().parent().parent().attr("id");
        var obj = $("#" + panelId).prev().find(".panel-tableName span");
        $(this).parent().parent().find("label.checkbox-inline").find("input[type='checkbox']").each(function () {
            if ($(this).is(":checked")) {
                existText = existText + "," + $.trim($(this).parent().text());
            }
        });
        if (obj.next()[0].nodeName == 'SPAN') {
            obj.next().remove();
        }
        var temp = '<span class="tables-existAdd-checked">(' + existText.substr(1, existText.length - 1) + ')</span>';
        obj.after(temp);

    });
    /*菜品分类下具体菜品添加*/
    $("#tables-detailMain-Add").click(function () {
        init_object();
        $("#tables-detailAdd-dialog").modal("show");
        $("#editTitle2").text("添加餐台");

    });
    /*	$("#tables-detailOther-Add").mousedown(function(e){

     if(e.button == 2){
     $(".tables-other-right").removeClass("hidden")
     }

     });*/

    $(".nav-counter-next").click(function () {
        var count = $(".nav-counter").find("li").length;
        if (flag_prev < count - 12) {
            $(".nav-counter").find("li").eq(flag_prev).css("margin-left", "-10%");
            flag_prev++;
        }

    });
    $(".nav-counter-prev").click(function () {
        if (flag_prev >= 1) {
            $(".nav-counter").find("li").eq(flag_prev - 1).css("margin-left", "0px");
            flag_prev--;
        }
    });


    $("#tables-detailOther-Add").click(function (e) {
        $(".tables-other-right").toggleClass("hidden");
    });
    /*select */
    $(".select-box").click(function () {
        $(".select-content").toggleClass("hidden");
    });
    $(".select-multi input[type='checkbox']").click(function () {
        var text = '';
        $(".select-multi").find("input[type='checkbox']").each(function () {
            if ($(this).is(":checked")) {
                text = text + "," + $(this).parent().text();
            }
        });
        text = text.substr(1, text.length - 1);
        $(".select-multi").prev(".select-box").find("input").val(text)

    });
    /*双击菜餐台名称弹出餐台编辑框*/
    $('.counter-content').on('dblclick', '.counter-detail-box', function () {
        if ($('#dinnerTable').attr('isclickType')) {//自定义餐台排序是不能编辑餐台
            customTable.noClick('自定义餐台排序时，不能编辑餐台')
            return false
        }
        doEdit($(this).attr("id"));
    });
    /*$(".counter-detail-box").dblclick(function(){
     debugger


     });*/

    /*菜品口味,菜品标签中添加按钮*/
    $(".tagAdd-btn button").click(function () {
        var id = $(this).parent().attr("id");
        var num = id.substr(id.length - 1, 1);
        var text = $("#tagName" + num).val();
        var temp = '<div><button class="btn btn-default" onclick="addTag(this)" type="button">' + text + '</button></div>';
        $("#tables-tag-table" + num).append(temp);

    });
    /*菜品口味弹出框显示*/
    $("#tables-taste-add").click(function () {
        $("#tables-tasteAdd-dialog").modal("show");
    });
    /*菜品口味确定按钮*/
    $("#tables-taste-save").click(function () {
        var text = "";
        $(".tables-tasteAdd-dialog .tables-tag-select").find("button").each(function () {
            text = text + $(this).text() + ",";
        });
        text = text.substr(0, text.length - 1);
        $("#tables-taste-add").text(text);
        $("#tables-tasteAdd-dialog").modal("hide");

    });
    /*菜品标签弹出框显示*/
    $("#tables-label-add").click(function () {
        $("#tables-labelAdd-dialog").modal("show");

    });
    /*菜品标签确定按钮*/
    $("#tables-label-save").click(function () {
        $(".tables-labelAdd-dialog .tables-tag-select").find("button").each(function () {
            var temp = '<label class="checkbox-inline"><input type="checkbox" value="">' + $(this).text() + '</label>';
            $(".tables-checkbox").append(temp);
        });
        $("#tables-labelAdd-dialog").modal("hide");

    });
    /*添加一组计价单位*/
    $(".charge-unit-addBtn").click(function () {
        var temp = '<div class="form-group">';
        temp = temp + '<label class="col-xs-2 control-label">计价单位：</label><div class="col-xs-2"><input type="text" value="" class="form-control"></div>'
        temp = temp + '<label class="col-xs-2 control-label">价格：</label><div class="col-xs-2"><input type="text" value="" class="form-control"></div>';
        temp = temp + '<label class="col-xs-2 control-label">会员价：</label><div class="col-xs-2"><input type="text" value="" class="form-control"><div></div>';
        $(".tables-charge-unit hr").before(temp);

    });
    /*添加已有菜品至该分类*/
    $("#tables-other-tab3").click(function () {
        $("#tables-existAdd-dialog").modal("show");
        $(".tables-other-right").addClass("hidden");
    });
    $('.glyphicon').click(function () {
        if ($(this).hasClass('glyphicon-chevron-up')) {
            $(this).removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
        } else if ($(this).hasClass('glyphicon-chevron-down')) {
            $(this).removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
        }
    });
    $("#tables-existAdd-dialog input[type='checkbox']").click(function () {

        var existText = '';
        var panelId = $(this).parent().parent().parent().attr("id");
        var obj = $("#" + panelId).prev().find(".panel-tableName span");
        $(this).parent().parent().find("label.checkbox-inline").find("input[type='checkbox']").each(function () {
            if ($(this).is(":checked")) {
                existText = existText + "," + $.trim($(this).parent().text());
            }
        });
        if (obj.next()[0].nodeName == 'SPAN') {
            obj.next().remove();
        }
        var temp = '<span class="tables-existAdd-checked">(' + existText.substr(1, existText.length - 1) + ')</span>';
        obj.after(temp);

    });
});

//----------------------------------------------------------------------------------------------
utils.loading = {
    open: function (msg) {
        var str = '<div class="lading-shade" ></div><div class="spinner">' +
            '<div class="rect1"></div>' +
            '<div class="rect2"></div>' +
            '<div class="rect3"></div>' +
            '<div class="rect4"></div>' +
            '<div class="rect5"></div>' +
            '<p>' + msg + '</p>'
        '</div>';
        if ($('.spinner').length < 1) {
            $('body').append(str);
        }
    },
    remove: function () {
        $('.lading-shade,.spinner').remove();
    }
}
/*禁止浏览器自带的鼠标右键功能*/
window.onload = function () {

};
function delDisplay(e) {
    $(e).find("i.icon-remove").removeClass("hidden");

}
function delHidden(e) {
    $(e).find("i.icon-remove").addClass("hidden");

}
function removeTag(e) {
    $(e).parent().remove();
}
function addTag(e) {
    var id = $(e).parent().parent().attr("id");
    var num = id.substr(id.length - 1, 1);
    var text = $(e).text();
    var temp = '<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">' + text + '<i onclick="removeTag(this)" class="icon-remove hidden"></i></button>';
    $("#tables-tag-select" + num).append(temp);
    removeTag();
}


/*禁止浏览器自带的鼠标右键功能*/

function delDisplay(e) {
    $(e).find("i.icon-remove").removeClass("hidden");

}
function delHidden(e) {
    $(e).find("i.icon-remove").addClass("hidden");

}
function removeTag(e) {
    $(e).parent().remove();
}
function addTag(e) {
    var id = $(e).parent().parent().attr("id");
    var num = id.substr(id.length - 1, 1);
    var text = $(e).text();
    var temp = '<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">' + text + '<i onclick="removeTag(this)" class="icon-remove hidden"></i></button>';
    $("#tables-tag-select" + num).append(temp);
    removeTag();
}
/*菜品详情删除*/
function deltablesDetail(e) {
    $("#tables-detailDel-dialog").modal("show");
}


function delDisplay(e) {
    $(e).find("i.icon-remove").removeClass("hidden");

}
function delHidden(e) {
    $(e).find("i.icon-remove").addClass("hidden");

}
function removeTag(e) {
    $(e).parent().remove();
}
function addTag(e) {
    var id = $(e).parent().parent().attr("id");
    var num = id.substr(id.length - 1, 1);
    var text = $(e).text();
    var temp = '<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">' + text + '<i onclick="removeTag(this)" class="icon-remove hidden"></i></button>';
    $("#tables-tag-select" + num).append(temp);
    removeTag();
}
/*菜品详情删除*/
function deltablesDetail(e) {
    $("#tables-detailDel-dialog").modal("show");
}

/**
 * 在这个删除事件后面调下这个方法，就不会调用按钮事件
 * @param evt
 */
function stoppro(evt) {
    var e = evt ? evt : window.event; //判断浏览器的类型，在基于ie内核的浏览器中的使用cancelBubble
    if (window.event) {
        e.cancelBubble = true;
    } else {
        e.preventDefault(); //在基于firefox内核的浏览器中支持做法stopPropagation
        e.stopPropagation();
    }
}
/*菜品分类拖曳*/
function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("Text", ev.target.id);
}
/*分类拖动*/
function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("Text");
    var drag_text = document.getElementById(data).parentNode;
    var drag_num = $("#" + data).parent().prevAll().length;
    var drop_text = ev.target.parentNode;
    var drop_num = $("#" + ev.target.id).parent().prevAll().length;
    if (drag_num > drop_num) {
        $(drop_text).before(drag_text);

    } else {
        $(drop_text).after(drag_text);
    }
    updateAreaOrder();
}
/**
 * 更新菜品分类的顺序
 */
function updateAreaOrder() {
    var idList = [];
    $("#nav-tables li").each(function (index, item) {
        var temp = {};
        temp.areaid = $(this).attr('id');
        temp.areaSort = index;
        idList.push(temp);

    });
//	alert(idList);
    $.ajax({
        type: "post",
        async: false,
        url: global_Path + '/tableArea/updateListOrder.json',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(idList),
        dataType: "json",
        success: function (result) {
        }
    });
}

function del() {

    $.ajax({
        type: "post",
        async: false,
        url: global_Path + "/table/delete/" + $("#showTableId").val() + ".json",
//			url : global_Path + "/dish/delete/"+$("#showDishId").val()+".json",
        dataType: "json",
        success: function (result) {
            if (result.code == '0') {
                $(".img-close").click();
                customTable.getTableJson();//重新获取餐台数据
                /*当在餐台服务费分类下时，或者当前选中的分区餐台数量为0时*/
                var tableNum = $("#nav-tables .active").find("span").eq(1).text().split("(")[1].split(")")[0] - (1);
                $("#nav-tables .active").find("span").eq(1).text("(" + tableNum + ")");

                if(tableNum=='0'&& $('.counter-content-title .tables-type-active').attr('type') == '1'){
                    $('.counter-content-title .tables-type-active').click()
                }
                else {
                    oneclickTableType($("#nav-tables .active").attr("id"));
                }
            }
            else {
                customTable.errorAlert(result.msg)
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }

    });


}
function getTableTag() {


    $.ajax({
        type: "post",
        async: false,
        url: global_Path + '/table/getTableTag.json',
        dataType: "json",
        success: function (result) {


            $.each(result, function (i, val) {
                if (val.areaname == null) {
                    val.areaname = "";
                }
                $(".areaid").append("<option value =" + val.areaid + "  class='form-control myInfo-select-addrW'>" + val.areaname + "</option>");

            });


        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }

    });
}
function getPrinterTag() {
    $.ajax({
        type: "post",
        async: false,
        url: global_Path + '/table/getPrinterTag.json',
        dataType: "json",
        success: function (result) {

            $.each(result, function (i, val) {

                $(".printerid").append("<option value =" + val.printerid + "  class='form-control myInfo-select-addrW'>" + val.printername + "</option>");
            });


        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }

    });
}
function getPersonNumTag() {
    var result = [{'personNum': '0'}, {'personNum': '2'}, {'personNum': '4'}, {'personNum': '6'}, {'personNum': '8'}, {'personNum': '10'}];


    $.each(result, function (i, val) {

        $(".personNum").append("<option value =" + val.personNum + "  class='form-control myInfo-select-addrW'>" + val.personNum + "人</option>");
    });
}
function getTabletypeTag() {
    var result = [{'tabletype': '0', 'tabletypename': '散台'}, {
        'tabletype': '1',
        'tabletypename': '包间'
    }, {'tabletype': '2', 'tabletypename': '外卖'}, {'tabletype': '3', 'tabletypename': '咖啡外卖'}, {
        'tabletype': '4',
        'tabletypename': '咖啡台'
    }];


    $.each(result, function (i, val) {

        $(".tabletype").append("<option value =" + val.tabletype + "  class='form-control myInfo-select-addrW'>" + val.tabletypename + "</option>");
    });
}

//保存餐台信息
function save_table() {
    var chargeOn = $('#serviceCharge_onoff input:checked').val();//是否开启服务费 0关闭 1开启
    $("#areaid").val($("#nav-tables .active").attr("id"));
    var tableInfo = {};
    $("#tableNo,#tableName,#areaid,#minprice,#fixprice,#personNum,#tableid").each(function (index) {
        tableInfo["" + $(this).attr("name") + ""] = $(this).val();
    });
    $("#tabletype option:selected,#areaid option:selected").each(function (index) {
        tableInfo["" + $(this).parent().attr("name") + ""] = $(this).val();
    });
    tableInfo['chargeOn'] = chargeOn;//是否开启服务费 0关闭 1开启
    if (chargeOn == '1') {
        var chargeType = $('.serviceCharge_count_select').val()//服务费计算方式 0比例 1 固定 2 时长
        tableInfo['chargeType'] = chargeType;//是否开启服务费 0关闭 1开启
        if (chargeType == '0') {//服务费计算方式 0比例
            var chargeRateRule = $('.serviceCharge_Real_Price').val()//0:实收 1:应收
            var chargeRate = $.trim($('.serviceCharge_count_timer').val())//比例计算方式 比率
            tableInfo['chargeRateRule'] = chargeRateRule;//0:实收 1:应收
            tableInfo['chargeRate'] = chargeRate;//比例计算方式 比率 数字100%

            tableInfo['chargeAmount'] = null;//固定方式金额

            tableInfo['chargeTime'] = null;//时长方式计算
        }
        if (chargeType == '1') {//服务费计算方式 1 固定
            var chargeAmount = $.trim($('.serviceCharge_count_timer1').val())//固定金额
            tableInfo['chargeAmount'] = chargeAmount;//固定金额

            tableInfo['chargeRateRule'] = null;//比例计算方式 0:实收 1:应收
            tableInfo['chargeRate'] = null;//比例计算方式 比率 数字100%

            tableInfo['chargeTime'] = null;//时长方式计算
        }
        if (chargeType == '2') {//服务费计算方式 2 时长
            var chargeTime = $('.serviceCharge_count_time .timerLength').val();//时长
            var chargeAmount = $.trim($('.serviceCharge_count_timer2').val())//固定金额
            tableInfo['chargeTime'] = chargeTime;//时长
            tableInfo['chargeAmount'] = chargeAmount;//金额

            tableInfo['chargeRateRule'] = null;// 比例计算方式 0:实收 1:应收
            tableInfo['chargeRate'] = null;//比例计算方式 比率 数字100%
        }
    }
    /*不开启服务费*/
    if (chargeOn == '0') {
        tableInfo['chargeTime'] = null;//时长
        tableInfo['chargeAmount'] = null;//金额

        tableInfo['chargeRateRule'] = null;// 比例计算方式 0:实收 1:应收
        tableInfo['chargeRate'] = null;//比例计算方式 比率 数字100%
    }
    $.ajax({
        type: "post",
        async: false,
        url: global_Path + '/table/save',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(tableInfo),
        dataType: "json",
        success: function (result) {
            if (result.code == '0') {
                $(".img-close").click();
                $('#serviceCharge_onoff input[value="0"]').prop('checked', true).click();//重新赋餐台服务费值为关闭
                customTable.getTableJson()//添加餐台重新赋值

                oneclickTableType($("#nav-tables .active").attr("id"));
                var printeridHave = [];
                var printerHave = [];
                $.each(tbPrinterAreaList, function (i, item) {
                    if (item.areaid == $("#nav-tables .active").attr("id") && printeridHave.indexOf(item.printerid) == -1) {
                        if ($("#editTitle2").text() === "添加餐台") {
                            item.tableid = result.data.tableid;
                        }
                        else {
                            item.tableid = tableInfo.areaid;
                        }
                        printeridHave.push(item.printerid);
                        printerHave.push(item);
                    }
                });
                if (printerHave != "" && $("#tableid").val() == "") {
                    //在打印配置单选中餐台
                    addPrinterArea(printerHave);
                }
                if ($("#editTitle2").text() === "添加餐台") {
                    var tableNum = $("#nav-tables .active").find("span").eq(1).text().split("(")[1].split(")")[0] - (-1);
                    $("#nav-tables .active").find("span").eq(1).text("(" + tableNum + ")");
                    console.log($("#nav-tables .active").find("span").eq(1).text().split("(")[1].split(")")[0] - (-1));
                }
            }
            else {
                customTable.errorAlert(result.msg)
            }


        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}
//编辑
function doEdit(id) {
    $("#tables-detailAdd-dialog").modal("show");
    $("#editTitle2").text("编辑餐台");
    $("#table-add").modal("show");
    init_object();
    $.ajax({
        type: "post",
        async: false,
        url: global_Path + "/table/findById/" + id + ".json",
        dataType: "json",
        success: function (result) {
            //console.log(result)
            if (result.chargeOn == '1') {
                $('#serviceCharge_onoff input[value="1"]').prop('checked', true).click();//是否开启餐台服务费
                $('.serviceCharge_count_proportion,.serviceCharge_count_fixed,.serviceCharge_count_time').hide();//计算方式隐藏
                $('.serviceCharge_count_select').val(result.chargeType)//服务费类型
                switch (result.chargeType) {
                    case '0':
                        $('.serviceCharge_count_proportion select').val(result.chargeRateRule);
                        $('.serviceCharge_count_timer').val(result.chargeRate);
                        $('.serviceCharge_count_proportion').show();
                        break;

                    case '1':
                        $('.serviceCharge_count_timer1').val(result.chargeAmount);
                        $('.serviceCharge_count_fixed').show();
                        break;

                    case '2':
                        $('.serviceCharge_count_time .timerLength').val(result.chargeTime);
                        $('.serviceCharge_count_timer2').val(result.chargeAmount);
                        $('.serviceCharge_count_time').show();
                        break;
                }
            }
            else {
                $('#serviceCharge_onoff input[value="0"]').prop('checked', true).click();
            }

            if (result.minprice == 0.00) {
                result.minprice = "";
            } else {
                $("#minp").prop("checked",true);
                checkit(true,'minp')

            }
            $("#fixpriceLable").html('<input type="checkbox" id="fixp" onclick="checkit(this.checked,this.id)" >固定使用费：');
            if (result.fixprice == 0.00) {
                result.fixprice = "";
            } else {
                $("#fixp").prop("checked",true);
                checkit(true,'fixp')
            }

            $("#tableid").val(result.tableid);
            $("#tableNo").val(result.tableNo);
            $("#tabletype  option[value=" + result.tabletype + "] ").attr("selected", true);
            $("#personNum").val(result.personNum);
            $("#areaid2").val(result.areaname);
            $("#tableName").val(result.tableName);

            $("#minpriceLable").html('<input type="checkbox" id="minp" onclick="checkit(this.checked,this.id)" >最低消费：');

            $("#minprice").val(result.minprice);
            $("#fixprice").val(result.fixprice);


        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}
function init_object() {
// 	tableid,tableNo,tabletype,tableName,personNum,areaid,minprice,fixprice
    $("#minprice").attr("disabled", true);
    $("#fixprice").attr("disabled", true);

    $("#minp").prop("checked",false);
    $("#fixp").prop("checked",false);
    $("#minprice").next("span").css({"color": "#CECECE"});
    $(".minpCheckboxSpan").css({"color": " #CECECE"});
    $("#fixprice").next("span").css({"color": "#CECECE"});
    $(".fixpCheckboxSpan").css({"color": " #CECECE"});
    $("#tableid").val("");
    $("#tableName_tip").text("");
    $("#personNum_tip").text("");
    $('#minConsu_tip').text("");
    $('#fixConsu_tip').text("");
    $("#tabletype").val("");
    $("#tableName").val("");
    $("#personNum").val("");
    $("#areaid").val("");
    $(".printerid").html("");
    $(".tabletype").html("");
    getPrinterTag();
    getTabletypeTag();
    $("#fixprice").val("");
    $("#tableid").val("");
    $("#tableNo").val("");
    $("#minprice").val("");
    $("#buildingNo").val("");

}

/**
 * 单击分类事件
 */
function oneclickTableType(id) {
    $(".nav-counter li").removeClass("active");
    $("#" + id).addClass("active");
    /*自定义排序点击隐藏咖啡和外卖台*/
    if ($('#dinnerTable').attr('isclicktype')) {//自定义排序时切换分区用tableJson缓存数据
        var _html = ''
        $(".counter-detail-box").remove();
        $.each(tableJson, function (index, item) {
            if (id == item.areaid) {
                item.tables.sort(function (a, b) {
                    return a.position - b.position
                });//按照position排序
                for (var i = 0; i < item.tables.length; i++) {
                    if(item.tables[i].chargeOn=='0'){//没有开启餐台服务费
                        _html += "<div class='counter-detail-box' tabletype='" + item.tables[i].tabletype + "'"
                        _html +=    " id='" + item.tables[i].tableid + "' onmouseover='delDisplay(this)' onmouseout='delHidden(this)'>";
                        _html += "<p  >" + item.tables[i].tableName + "</p>";
                        _html += "<p  >(" + item.tables[i].personNum + "人桌)</p>";
                        _html += "<i class='icon-remove hidden'  onclick='delTablesDetail(" + "&apos;" + item.tables[i].tableid + "&apos;" + "," + "&apos;" + item.tables[i].tableName + "&apos;" + ",event)'></i></div>";
                    }
                    if(item.tables[i].chargeOn=='1'){//开启餐台服务费添加counter-detailService-box区分
                        _html += "<div class='counter-detail-box counter-detailService-box' tabletype='" + item.tables[i].tabletype + "'"
                        _html +=    " id='" + item.tables[i].tableid + "' onmouseover='delDisplay(this)' onmouseout='delHidden(this)'>";
                        _html += "<p  >" + item.tables[i].tableName + "</p>";
                        _html += "<p  >(" + item.tables[i].personNum + "人桌)</p>";
                        _html += "<i class='icon-remove hidden'  onclick='delTablesDetail(" + "&apos;" + item.tables[i].tableid + "&apos;" + "," + "&apos;" + item.tables[i].tableName + "&apos;" + ",event)'></i></div>";
                    }

                }
                $('#tables-detailMain-Add').before(_html)
                customTable.isHide();
                return false
            }
        });
        return false
    }
    /*在服务费餐台分类下*/
    if ($('.counter-content-title .tables-type-active').attr('type') == '1') {
        $(".counter-detail-box").remove();
        $.each(serviceTablesJson, function (key, val) {
            /*当前areaid=选中的分区id并且餐台个数大于0*/
            if (id == val.areaid){
                $.each(val.tables, function (index, item) {
                    $('#tables-detailMain-Add').before("<div class='counter-detail-box counter-detailService-box' tabletype='" + item.tabletype + "' chargeOn='" + item.chargeOn + "' id='" + item.tableid + "' onmouseover='delDisplay(this)' onmouseout='delHidden(this)'>" +
                        "<p  >" + item.tableName + "</p>" +
                        "<p  >(" + item.personNum + "人桌)</p>" +
                        "<i class='icon-remove hidden'  onclick='delTablesDetail(" + "&apos;" + item.tableid + "&apos;" + "," + "&apos;" + item.tableName + "&apos;" + ",event)'></i></div>");
                })
            }

        });
        return false
    }
    $.ajax({
        url: global_Path + "/table/getTablesByTableType/" + id + ".json",
        type: "post",
        datatype: "json",
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            $(".counter-detail-box").remove();
            $.each(result, function (index, item) {
                if (item.chargeOn == '1') {
                    $('#tables-detailMain-Add').before("<div class='counter-detail-box counter-detailService-box' tabletype='" + item.tabletype + "' chargeOn='" + item.chargeOn + "' id='" + item.tableid + "' onmouseover='delDisplay(this)' onmouseout='delHidden(this)'>" +
                        "<p  >" + item.tableName + "</p>" +
                        "<p  >(" + item.personNum + "人桌)</p>" +
                        "<i class='icon-remove hidden'  onclick='delTablesDetail(" + "&apos;" + item.tableid + "&apos;" + "," + "&apos;" + item.tableName + "&apos;" + ",event)'></i></div>");
                }
                else {
                    $('#tables-detailMain-Add').before("<div class='counter-detail-box' tabletype='" + item.tabletype + "' chargeOn='" + item.chargeOn + "' id='" + item.tableid + "' onmouseover='delDisplay(this)' onmouseout='delHidden(this)'>" +
                        "<p  >" + item.tableName + "</p>" +
                        "<p  >(" + item.personNum + "人桌)</p>" +
                        "<i class='icon-remove hidden'  onclick='delTablesDetail(" + "&apos;" + item.tableid + "&apos;" + "," + "&apos;" + item.tableName + "&apos;" + ",event)'></i></div>");
                }

                /*$('#'+item.tableid).dblclick(function(){
                 doEdit($(this).attr("id"));
                 })*/
                ;
            });
            /*自定义排序点击隐藏咖啡和外卖台*/
            if ($('#dinnerTable').attr('isclicktype')) {
                customTable.isHide();
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}
/*餐台详情删除*/
function delTablesDetail(tableid, tableName, e) {
    if ($('#dinnerTable').attr('isclickType')) {
        customTable.noClick('自定义餐台排序时，不能删除餐台')
        return false
    }
    $("#tables-detailDel-dialog").modal("show");
    $("#showTableName").html(tableName);
    $("#showTableId").val(tableid);
}
function initArea() {
    $("#areanameB").val("");
    $("#areaidB").val("");
    $("#areanameB_tip").text("");
    $(".error").text("");
}
function addArea() {
    if ($('#dinnerTable').attr('isclickType')) {
        customTable.noClick('自定义餐台排序时，不能添加分区')
        return false
    }
    initArea();
    $("#areas-detailAdd-dialog").modal("show");
    $("#editTitle1").text("添加分区");

}
function editArea(id) {
    if ($('#dinnerTable').attr('isclickType')) {
        customTable.noClick('自定义餐台排序时，不能编辑分区')
        return false
    }
    initArea();
    $("#areas-detailAdd-dialog").modal("show");
    $("#editTitle1").text("编辑分区");
    $("#table-add").modal("show");
    var str = id;
    if (id == null || id == undefined) {
        str = $(".nav-counter .active").attr("id");
    }
    $.ajax({
        type: "post",
        async: false,
        url: global_Path + "/tableArea/findById/" + str + ".json",
        dataType: "json",
        success: function (result) {
            $("#areaidB").val(result.areaid);
            $("#areanameB").val(result.areaname);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}
var areaLength;
function save_Area() {
    var areaInfo = {};
    areaInfo["" + "areaname" + ""] = $("#areanameB").val();
    areaInfo["" + "areaid" + ""] = $("#areaidB").val();
    //areaLength = $("#nav-tables").find("li").length + 1;
    areaLength=tableJson.length+1//全部餐台分区的个数+1
    $.ajax({
        type: "post",
        async: false,
        url: global_Path + '/tableArea/save/' + areaLength + ".json",
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(areaInfo),
        dataType: "json",
        success: function (result) {
            if (result.code == '0') {
                window.location.reload();
            }
            else {
                customTable.errorAlert(result.msg)
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}
function check_validate() {
    var TN = $("#tableName").val();
    $("#tableNo").val(TN);
    var flag = true;
    $.ajax({
        type: "post",
        async: false,
        data: {
            tableid: $("#tableid").val(),
            tableName: $("#tableName").val()
        },
        url: global_Path + "/table/validateTable.json",
        dataType: "json",
        success: function (result) {
            if (result.message == '餐台名称不能重复') {
                $("#tableName_tip").text(result.messageDetail);
                $("#tableName").focus();
                flag = false;
            }
        }

    });
    return flag;
}
function check_area() {
    var TN = $("#areanameB").val();
    $("#areaNoB").val(TN);
    var flag = true;
    $.ajax({
        type: "post",
        async: false,
        data: {
            areaid: $("#areaidB").val(),
            areaname: $("#areanameB").val()
        },
        url: global_Path + "/tableArea/validateArea.json",
        dataType: "json",
        success: function (result) {

            if (result.message == '分区名称不能重复') {
                $("#login").focus();
                $("#areanameB_tip").text(result.message);
                $("#areanameB").focus();
                flag = false;
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
    return flag;
}
function delAreaAndTables() {
    if ($('.counter-detail-box').length < 1) {
        delArea();
        return false
    }
    $.ajax({
        type: "post",
        async: false,
        url: global_Path + "/table/deleteTablesByAreaid/" + $("#nav-tables .active").attr("id") + ".json",
        dataType: "json",
        success: function (result) {
            if (result.code == '0') {
                delArea()
            }
            else {
                customTable.errorAlert(result.msg)
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}
/*删除区域*/
function delArea() {
    $.ajax({
        type: "post",
        async: false,
        url: global_Path + "/tableArea/delete/" + $("#nav-tables .active").attr("id") + ".json",
        dataType: "json",
        success: function (result) {
            if (result.code == '0') {
                updateAreaOrder();
                window.location.reload();
            }
            else {
                customTable.errorAlert(result.msg)
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}
function stopEvent(e) {
    try {
        if (window.event) {
            window.event.cancelBubble = true;
        } else e.stopPropagation();
    } catch (e) {
        trace("stopEvent error=" + e);
    }
}
function checkit(isChecked, id) {
    if (isChecked) {
        $("#" + id + "rice").attr("disabled", false);
        $("#" + id + "rice").next("span").css({"color": "#282828"});
        $("." + id + "CheckboxSpan").css({"color": "#282828"});
    }
    else {
        $("#" + id + "rice").val('');
        $("#" + id + "rice").attr("disabled", "disabled");
        $("#" + id + "rice").next("span").css({"color": "#CECECE"});
        $("." + id + "CheckboxSpan").css({"color": " #CECECE"});

    }
}
$("#tables-detailAdd-dialog").click(function () {
    a(document.body).removeClass("modal-open");
});
$('#tables-detailAdd-dialog').modal('show');
function showAndHidden() {
    var count = $("#nav-tables").find("li").length;

    if (count > parseInt(($('#nav-tables').width() - 1 ) / $('#nav-tables li').eq(0).outerWidth())) {
        $(".nav-counter-prev").css({"display": "inline"});
        $(".nav-counter-next").css({"display": "inline"});
    } else {
        $(".nav-counter-prev").css({"display": "none"});
        $(".nav-counter-next").css({"display": "none"});
    }
    if (count > 0) {


        $("#tables-detailMain-Add").css({"display": "inline-block"});
        $(".tables-content-title span").text("餐台管理");
    } else {


        $("#tables-detailMain-Add").css({"display": "none"});
        $(".tables-content-title span").html("&nbsp;");
    }
    if (count > 10) {
        $("#dishTypeSelect").css({"height": "365px"});
    }
}
/*v右键弹出框*/
function doMenu(e, _this) {
    if (e.button == 2) {
        $(".nav-counter li").removeClass("active");
        $(_this).addClass("active");
        $(".tables-right-tab").addClass("hidden")


        oneclickTableType($(_this).attr("id"));
        var num = 0;
        $(_this).prevAll().each(function () {
            if ($(_this).css("margin-left") == '0px')
                num++;
        });
        var left = num * 99 / 12 + '%';
        console.log(left);
        $(".tables-right-tab").removeClass("hidden");
        $(".tables-right-tab").css("left", left);
        var area_name = "\"" + $(_this).text() + "\"";
        $("li#tables-right-tab2 span").text("编辑" + area_name);
        $("li#tables-right-tab3 span").text("删除" + area_name);


    }
}
function showDeleteArea() {
    if ($('#dinnerTable').attr('isclickType')) {
        customTable.noClick('自定义餐台排序时，不能删除分区')
        return false
    }
    $("#areas-detailDel-dialog").modal("show");
    $("#showAreaName").html($("#nav-tables .active button").text());
//	$("#showTableId").val(tableid);
}
function hideDialog() {
    $(".img-close").click();
}
//在打印配置单选中餐台
function addPrinterArea(list) {
    $.ajax({
        type: "post",
        async: false,
        url: global_Path + "/printerManager/addOnePrinterTable.json",
        contentType: 'application/json;charset=UTF-8',
        dataType: "json",
        data: JSON.stringify(list),
        success: function (result) {


        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}
function findPrinterArea() {
    $.ajax({
        type: "post",
        async: false,
        url: global_Path + "/printerManager/findPrinterArea.json",
        dataType: "json",
        data: {
            status: 1,

        },
        success: function (result) {
            tbPrinterAreaList = result.tbPrinterAreaList;

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}
function addEvent_T(event) {
    event = event || window.event;

    var type = event.type;
    if (type == 'DOMMouseScroll' || type == 'mousewheel') {
        event.delta = (event.wheelDelta) ? event.wheelDelta / 120 : -(event.detail || 0) / 3;
    }

    var count = $("#nav-tables").children("li").length;
    if (event.delta > 0) {
        if (count - flag_prev > 12) {
            $("#nav-tables").find("li").eq(flag_prev).css("margin-left", "-14%");
            flag_prev++;
        }

    } else {
        if (flag_prev >= 1) {
            $("#nav-tables").find("li").eq(flag_prev - 1).css("margin-left", "0");
            flag_prev--;
        }
    }

    if (document.all) {
        event.cancelBubble = false;
        return false;
    } else {
        event.preventDefault();
    }

}
/*提示弹窗*/
var widget = widget || {};
/*
 * 公共模态框
 * ***参数***
 *
 *  - `content` {String} 填充内容
 *  - `title` {String} 模态框标题
 *  - `width` {Number} 默认500
 *  - `height` {Number} 默认auto
 *  - `vertical` {Boolean} 默认true
 *  - `cls` {String} 为modal添加样式,多个用空格分开
 *  - `hasBtns` {Boolean} 是否有按钮 默认为true
 *  - `btnOkTxt` {String} ok按钮文字 为''时,不显示
 *  - `btnCancelTxt` {String} cancle按钮文字 为''时,不显示
 *  - `btnOkCb` {Function} ok按钮回调
 *  - `btnCancelCb` {Function} cancel按钮回调 默认带有关闭事件
 *  - `onReady` {Function} 创建完成后回调
 * ***方法***
 *
 * - `close()` 销毁modal
 * - `hide()`  隐藏modal
 * - `show()`  对hide的modal 显示
 * */
widget.modal = function () {
    var _tplHtml =
        '<div class="modal counter-dialog [Cls]" id="[Id]">' +
        '<div class="modal-content" style="width:[Width]px;margin: 0px auto;">' +
        '<div class="modal-body" style="width:[Width]px;margin: 0px auto;">' +
        '<div class="counter-dialog-header" >' +
        '<span style="padding-left: 0px;"> [Title]</span>' +
        /*'<img src="/newspicyway/images/close-sm.png" class="img-close"  data-dismiss="modal">'+*/
        '</div>' +
        '<hr class="ky-hr">' +
        '<div class="" style="text-align: center;margin-bottom: 20px;margin-top: 20px">' +
        '[Content]' +
        '</div>' +
        '<div class="btn-operate" style="margin-top: 40px;">' +
        '<button type="button" class="btn btn-cancel cancel" data-dismiss="modal" style="padding: 6px 30px;margin-right: 40px">[BtnCancelTxt]</button>' +
        '<button type="button" class="btn btn-save ok" style="padding: 6px 30px">[BtnOkTxt]</button>' +
        '</div>' +
        '</div>' +
        '</div>';
    '</div>';

    var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');

    var _alert = function (options) {
        var id = _dialog(options);
        var modal = $('#' + id);
        modal.addClass('modal-alert');

        return {
            id: id,
            hide: function () {
                modal.modal('hide');
            },
            close: function () {
                _close(id);
            },
            show: function () {
                modal.modal('show');
            }
        };
    };

    var _confirm = function (options) {
        var id = _dialog(options);
        var modal = $('#' + id);
        modal.addClass('modal-confirm');
        return {
            id: id,
            hide: function () {
                modal.modal('hide');
            },
            close: function () {
                _close(id);
            },
            show: function () {
                modal.modal('show');
            }
        };
    };


    var _getId = function () {
        var date = new Date();
        return 'mdl' + date.valueOf();
    };

    var _close = function (id) {
        var $modal = $('#' + id);
        $modal.next().remove();
        $modal.remove();
    };

    var _dialog = function (options) {
        var modalId = _getId();
        var ops = {
            content: "提示内容",
            title: "操作提示",
            width: 500,
            height: 'auto',
            auto: false,
            cls: '',
            hasBtns: true,
            btnCancelTxt: '取消',
            btnOkTxt: '确定',
            vertical: true,
            onReady: null,
            hasFooter: true,
            btnOkCb: function () {
                _close(modalId);
            },
            btnCancelCb: function () {
                _close(modalId);
            }

        };

        ops = $.extend({}, ops, options);

        var html = _tplHtml.replace(reg, function (node, key) {
            return {
                Id: modalId,
                Title: ops.title,
                Cls: ops.cls,
                Width: ops.width,
                Content: ops.content,
                BtnCancelTxt: ops.btnCancelTxt,
                BtnOkTxt: ops.btnOkTxt,
            }[key];
        });


        $('body').append(html);

        var $modal = $('#' + modalId);

        //按钮逻辑
        if (!ops.hasBtns) {
            $modal.find('.modal-footer').remove();
        } else {

            if (ops.btnOkTxt === '') {
                $modal.find('.ok').remove();
            } else {
                $modal.find('.ok').bind('click', function () {
                    if ($(this).hasClass('disabled')) return false;
                    ops.btnOkCb && ops.btnOkCb.call(this);
                })
            }


            if (!ops.hasFooter) {
                $modal.find('.modal-footer').remove();
            }

            if (ops.btnCancelTxt === '') {
                $modal.find('.cancel').remove();
            } else {
                $modal.find('.cancel').bind('click', function () {
                    ops.btnCancelCb && ops.btnCancelCb.call(this);
                    _close(modalId);
                })
            }
        }


        ops.onReady && ops.onReady.call(this);

        //关闭按钮
        $modal.find('.close').bind('click', function () {
            _close(modalId);
        });

        if (ops.vertical) {
            $modal.css({
                'display': 'flex',
                'align-items': 'center'
            });
        }

        $modal.next('.modal-backdrop').addClass('.modal-backdrop-' + modalId);
        $modal.modal({
            width: ops.width,
            backdrop: 'static'
        });

        return modalId;
    };
    return {
        alert: _alert,
        confirm: _confirm
    }
}();
/*自定义餐台*/

var customTable = {
    int: function () {
        this.isClick();
        this.getTableJson();
        this.serviceCharge_onoff();
    },
    /*获取所以餐台数据*/
    getTableJson: function () {
        tableJson=[];
        serviceTablesJson=[];
        var tmpJson = [];
        $.ajax({
            url: global_Path + '/table/getTypeAndTableMap.json',
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            async :false,
            success: function (res) {
                //console.log(res)
                $.each(res, function (index, item) {
                    $.each(item, function (key, obj) {
                        var tmpJson1 = JSON.parse(key);
                        var chargeOff = [];//有餐台服务费
                        $.each(obj, function (k, v) {
                            if (v.chargeOn == '1') {
                                chargeOff.push(obj[k])
                            }
                        })
                        //全部餐台
                        var abcd = {
                            'areaid': tmpJson1.areaid,
                            'areaname': tmpJson1.areaname,
                            'areaSort': tmpJson1.areaSort,
                            'tables': obj
                        }
                            //全部餐台
                        tmpJson.push(abcd);
                        //有服务费的餐台
                        if (chargeOff.length > 0) {
                            var a = {
                                'areaid': tmpJson1.areaid,
                                'areaname': tmpJson1.areaname,
                                'areaSort': tmpJson1.areaSort,
                                'tables': chargeOff
                            }
                            serviceTablesJson.push(a)
                        }

                    })
                });
                tableJson = tmpJson;
                /*给开启餐台服务的分区添加样式*/
                $('#nav-tables li').each(function () {
                    var me = $(this).attr('id'),
                        _this = $(this);
                    $.each(serviceTablesJson, function (ke, val) {
                        if (val.areaid == me) {
                            _this.addClass('counter-detailService-box')
                        }
                    })
                })
                //console.log(JSON.stringify(tableJson))
            }
        })

    },
    isClick: function () {
        var that = this, cont = 0;
        $('#dinnerTable').click(function () {
            if($('.counter-content-title .tables-type-active').attr('type') == '1'){
                that.errorAlert('在服务费餐台分类下，不能自定义餐台排序')
                return false
            }
            cont++
            var me = $(this).find('span');
            $('#dinnerTablecancel').show();
            if (me.text() == '自定义餐台排序') {
                $(this).attr('isclickType', '0')
                me.text('保存');
                $('#counter-type-add,#tables-detailMain-Add').hide();
                if (cont > 1) {
                    $(".nav-counter").sortable('enable')
                    $(".nav-counter-tab").sortable('enable');
                }
                that.areaDrag();

                that.tableDrag();

                that.isHide();

            }
            /*保存自定义餐台*/
            else {
                me.text('自定义餐台排序');
                $(this).removeAttr('isclickType');
                $('#dinnerTablecancel').hide();
                $('#counter-type-add,#tables-detailMain-Add').show();
                that.isShow();
                that.saveTable();
                $(".nav-counter").sortable('disable')
                $(".nav-counter-tab").sortable('disable');
            }
        });
        /*取消自定义餐台排序*/
        $('#dinnerTablecancel').click(function () {
            window.location.reload();//刷新页面
        })
    },
    isHide: function () {
        var that = this;
        $('.counter-detail-box').each(function () {
            var me = $(this);
            if (me.attr('tabletype') == '2' || me.attr('tabletype') == '3') {
                $(this).hide();
            }
        })
    },
    isShow: function () {
        var that = this;
        $('.counter-detail-box').each(function () {
            var me = $(this)
            if (me.attr('tabletype') == '2' || me.attr('tabletype') == '3') {
                $(this).show()
            }
        })

    },
    /*区域拖动*/
    areaDrag: function () {
        var that = this;
        $(".nav-counter").sortable({
            cursor: "move",
            items: "li",                        //只是li可以拖动
            //axis:'x',                           //横向拖动
            opacity: 1,                       //拖动时，透明度为0.6
            revert: true,                       //释放时，增加动画
            start: function (event, ui) {
                (ui.item).removeAttr('onclick')
            },
            stop: function (event, ui) {
                (ui.item).attr('onclick', 'oneclickTableType(this.id)')
            },
            update: function (event, ui) {       //更新排序之后
                var _thisItem = (ui.item).attr('id')//ui.item当前移动的元素
                var abc = $(this).sortable("toArray");//更新排序后ID的集合
                $.each(abc, function (i, value) {
                    $('.ui-sortable-handle').each(function () {
                        if ($(this).attr('id') == value) {
                            $(this).attr('areaSort', i)
                        }
                    })
                });
                (ui.item).attr('onclick', 'oneclickTableType(this.id)')
                that.setJson()


            }
        });
    },
    /*餐台拖动*/
    tableDrag: function () {
        var that = this;

        $(".nav-counter-tab").sortable({
            cursor: "move",
            items: "div",                        //只是div可以拖动
            //axis:'x',                           //横向拖动
            opacity: 1,                       //拖动时，透明度为0.6
            revert: true,                       //释放时，增加动画
            start: function (event, ui) {
                //(ui.item).removeAttr('onclick')
            },
            stop: function (event, ui) {
                //(ui.item).attr('onclick','oneclickTableType(this.id)')
            },
            update: function (event, ui) {       //更新排序之后
                var _thisItem = (ui.item).attr('id')//ui.item当前移动的元素
                var abc = $(this).sortable("toArray");//更新排序后ID的集合
                $('.counter-detail-box').each(function (i) {
                    if ($(this).is(":hidden")) {
                        abc.splice($.inArray($(this).attr('id'), abc), 1);//删除数组中隐藏的外卖咖啡台id
                        //$(this).attr('position',$('.counter-detail-box').length+i+1)
                    }
                })
                // console.log(abc)
                //var len=abc.length+1  排序后去掉外卖咖啡台id的数组集合的长度+1
                $.each(abc, function (i, value) {
                    //len--  长度每次减1
                    $('.counter-detail-box').each(function () {
                        if ($(this).is(":hidden")) {
                        }
                        else {
                            if ($(this).attr('id') == value) {
                                //$(this).attr('position',-len);按长度的负数排序
                                $(this).attr('position', i)
                            }
                        }
                    })
                });
                that.setJson()
            }
        });
    },
    /*拼装回传数据*/
    setJson: function () {
        for (var i = 0; i < tableJson.length; i++) {
            $('.nav-counter li').each(function () {
                if (tableJson[i].areaid == $(this).attr('id')) {
                    tableJson[i].areaSort = $(this).attr('areaSort')
                    for (var j = 0; j < tableJson[i].tables.length; j++) {
                        $('.counter-detail-box').each(function () {
                            if (tableJson[i].tables[j].tableid == $(this).attr('id') && !$(this).is(":hidden")) {//!$(this).is(":hidden")不是外卖和咖啡台才改变position
                                //alert(tableJson[i].tables[j].tabletype)
                                tableJson[i].tables[j].position = $(this).attr('position')
                            }
                        })
                    }

                }
            });
        }
        // console.log(JSON.stringify(tableJson))
    },
    /*保存自定义餐台*/
    saveTable: function () {
        $.ajax({
            url: global_Path + '/table/saveSortedTypeAndTable.json',
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            beforeSend: function () {
                utils.loading.open('正在加载');
            },
            complete: function () {
                utils.loading.remove();
            },
            data: JSON.stringify(tableJson),
            success: function (res) {
                if (res.code == '0') {
                    widget.modal.alert({
                        cls: 'fade in',
                        content: '<div><img src="../images/del-tip.png" style="margin-right: 20px">' + res.msg + '</div>',
                        width: 360,
                        height: 500,
                        btnOkTxt: '确定 ',
                        btnCancelTxt: '',
                        btnOkCb: function () {
                            $('.modal-alert:last,.fade:last').remove()
                            window.location.reload();//刷新页面
                        }
                    });

                }
                else {
                    widget.modal.alert({
                        cls: 'fade in',
                        content: '<div><img src="../images/del-tip.png" style="margin-right: 20px">' + res.msg + '</div>',
                        width: 360,
                        height: 500,
                        btnOkTxt: '确定 ',
                        btnCancelTxt: '',
                        btnOkCb: function () {
                            $('.modal-alert:last,.fade:last').remove()
                        }
                    });
                }
                //console.log(res)
            },
            error: function () {
                widget.modal.alert({
                    cls: 'fade in',
                    content: '<div><img src="../images/del-tip.png" style="margin-right: 20px">保存失败请稍后重试</div>',
                    width: 360,
                    height: 500,
                    btnOkTxt: '确定 ',
                    btnCancelTxt: '',
                    btnOkCb: function () {
                        $('.modal-alert:last,.fade:last').remove()
                        //window.location.reload();//刷新页面
                    }
                });
            }
        })

    },
    /*点击自定义餐台时禁用*/
    noClick: function (msg) {
        widget.modal.alert({
            cls: 'fade in',
            content: '<div><img src="../images/del-tip.png" style="margin-right: 20px">' + msg + '</div>',
            width: 360,
            height: 500,
            btnOkTxt: '确定 ',
            btnCancelTxt: '',
            btnOkCb: function () {
                $('.modal-alert:last,.fade:last').remove()
            }
        });

    },
    /*餐台服务费开启关闭*/
    serviceCharge_onoff: function () {
        var that=this;
        $('#serviceCharge_onoff input').click(function () {
            var me = $(this).val();
            $('.serviceCharge_count_timer_tip').text('')//错误信息
            //恢复成默认值
            $('.serviceCharge_count_timer,.serviceCharge_count_timer1,.serviceCharge_count_timer2').val('');
            $('.serviceCharge_count_proportion,.serviceCharge_count_fixed,.serviceCharge_count_time').hide();
            $('.serviceCharge_count_proportion').show();
            $('.serviceCharge_count_select').val(0);
            $('.serviceCharge_count_proportion select').val(0)
            if (me == '0') {
                //$('#serviceCharge_favorable input[value="0"]').prop('checked',true);//是否参与优惠，不参与
                //$('#serviceCharge_count,#serviceCharge_favorable').hide();//是否参与折扣
                $('#serviceCharge_count').hide();
            }
            if (me == '1') {
                //$('#serviceCharge_count,#serviceCharge_favorable').show();//是否参与折扣
                $('#serviceCharge_count').show();
            }

        });
        /*餐台服务费计算方式选择*/
        $('.serviceCharge_count_select').change(function () {
            var me = $(this).val();
            $('.serviceCharge_count_timer,.serviceCharge_count_timer1,.serviceCharge_count_timer2').val('');
            $('.serviceCharge_count_proportion,.serviceCharge_count_fixed,.serviceCharge_count_time').hide();
            $('.serviceCharge_count_timer_tip').text('')//错误信息
            if (me == '0') {
                $('.serviceCharge_count_proportion').show();
            }
            if (me == '1') {
                $('.serviceCharge_count_fixed').show();
            }
            if (me == '2') {
                $('.serviceCharge_count_time').show();
            }
        });
        /*选择时长*/
        /*$('.timerLength').change(function () {
         $('.serviceCharge_count_timer').val('');
         })*/
        /*餐台切换*/
        $('#serviceTables,#allTables ').click(function () {
            $('#serviceTables,#allTables').removeClass('tables-type-active')
            var me = $(this).attr('type');
            if (me == '0') {
                $('#allTables').addClass('tables-type-active')
                window.location.reload();//刷新页面
            }
            if (me == '1') {
                if(serviceTablesJson.length<'1'){
                    that.errorAlert('该门店不存在服务费餐台');
                    $('#allTables').addClass('tables-type-active')
                    return false
                }
                $('#serviceTables').addClass('tables-type-active')
                $('#nav-tables').html('');
                /*清空餐台区域和显示餐台*/
                var html = '';
                for (var i = 0; i < serviceTablesJson.length; i++) {
                    html += '<li id="' + serviceTablesJson[i].areaid + '" areaSort="' + serviceTablesJson[i].areaSort + '"  class="counter-detailService-box" onmouseover="delDisplay(this)"';
                    html += ' onmouseout="delHidden(this)"  onclick="oneclickTableType(this.id)" onmousedown="doMenu(event,this)" ondblclick="editArea(this.id)"> ';
                    html += '    <span>' + serviceTablesJson[i].areaname + '</span><span>(' + serviceTablesJson[i].tables.length + ')</span>';
                    html += '    <i class="icon-remove hidden" onclick="showDeleteArea()"></i>';
                    html += '</li>';
                }
                $('#nav-tables').html(html);
                $('#nav-tables li').eq(0).click();
            }
        })


    },
    /*错误信息弹窗*/
    errorAlert: function (msg) {
        widget.modal.alert({
            cls: 'fade in',
            content: '<div><img src="../images/del-tip.png" style="margin-right: 20px">' + msg + '</div>',
            width: 360,
            height: 500,
            btnOkTxt: '确定 ',
            btnCancelTxt: '',
            btnOkCb: function () {
                $('.modal-alert:last,.fade:last').remove()
            }
        });
    },
    /*输入的是否为金额非零开始的两位小数*/
    ismoney: function (money) {
        var pattern = /^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$/;
        pattern.lastIndex = 0;//正则开始为0
        return pattern.test(money);
    },


}