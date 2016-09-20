var tableList=[{ order:"H20160906702001031",ordertype:"已结", address:"一楼",deskNo:"风雨112",waiterNo:"013",startTime:"10:31:21",endTime:"12:30:45",people:"12",money:"480",orderUnit:"",phone:"13412344321",contacts:""},
    { order:"H20160906702001032",ordertype:"未结", address:"一楼",deskNo:"风雨112",waiterNo:"013",startTime:"10:31:21",endTime:"12:30:45",people:"12",money:"480",orderUnit:"",phone:"13412344321",contacts:""},
    { order:"H20160906702001033",ordertype:"已结", address:"一楼",deskNo:"风雨112",waiterNo:"013",startTime:"10:31:21",endTime:"12:30:45",people:"12",money:"480",orderUnit:"",phone:"13412344321",contacts:""},
    { order:"H20160906702001034",ordertype:"未结", address:"一楼",deskNo:"风雨112",waiterNo:"013",startTime:"10:31:21",endTime:"12:30:45",people:"12",money:"480",orderUnit:"",phone:"13412344321",contacts:""},
]

$("body").on("click","#checklist tr" ,function(){//点击tr变色
    $(this).addClass("tablistActive").siblings("tr").removeClass("tablistActive");
    var ordertype= $(this).find("td").eq(1).text();
    disabled(ordertype );
});
function disabled(ordertype ) {//选择tr判断按钮不可点击
    if(ordertype=="已结"){
        $(".foot-menu ul button").removeAttr("disabled");
        $(".foot-menu ul button").eq(4).attr("disabled","disabled").addClass("disabled").siblings("button").removeClass("disabled");
    }
    else {
        $(".foot-menu ul button").removeClass("disabled");
        $(".foot-menu ul button").removeAttr("disabled");
        $(".foot-menu ul button").eq(1).attr("disabled","disabled").addClass("disabled")
        $(".foot-menu ul button").eq(5).attr("disabled","disabled").addClass("disabled")
    }
}
/*选中默认第一个tr*/
function firstActive() {
    var ordertype= $("#checklist tr").find("td").eq(1).text();
    $("#checklist tr").eq(1).addClass("tablistActive");
    disabled(ordertype );
};
$(".check-type div").click(function () {//账单状态
    var ordertype=$(this).attr("ordertype");
    $(this).addClass("active").siblings("div").removeClass("active");
    orderlist(ordertype);
    firstActive();

});
$('.foot-menu button').on('click', function(){
    var me = $(this);
    var orderNumber=getOrderNumber();
    if(me.hasClass('c-mod-fjs')) {//反结算
        var str =
            '<strong>订单号：'+orderNumber+'</strong><br/><br/>' +
            '<strong>确定反结算吗？</strong>';

        var alertModal = widget.modal.alert({
            cls: 'fade in',
            content:str,
            width:500,
            height:500,
            title: "",
            btnOkTxt: '确定',
            btnOkCb: function(){
                reason();
            },
            btnCancelCb: function(){

            }
        })
    }
    if(me.hasClass('c-mod-js')) {//结算
        var str =
            '<strong>订单号：'+orderNumber+'</strong><br/><br/>' +
            '<strong>确定结算吗？</strong>';

        var alertModal = widget.modal.alert({
            cls: 'fade in',
            content:str,
            width:500,
            height:500,
            title: "",
            btnOkTxt: '确定',
            btnOkCb: function(){
                $(".modal-alert").modal("hide")
            },
            btnCancelCb: function(){

            }
        })
    }
});

function refresh() {//刷新
    var s=".check-type div";
    var ordertype
    $(s).each (function () {
        if($(this).hasClass("active")) {
            ordertype = $(this).attr("ordertype");
            return false
        }
    })
    orderlist(ordertype);
    firstActive();
    return ordertype
}
function getOrderNumber() {//获取选中行的账单号
    var s=$("#checklist .tablistActive" );
    var orderNumber=s.find("td").eq(0).text()
    return orderNumber
}
function reason() {//反结原因
    $(".modal-alert").modal("hide")
    var str =
        '<div class="selectReason" style="text-align: left">'+
        '<div class="form-group form-group-base form-input">'+
        '   <span class="form-label" style="line-height: 40px">反结原因:</span>'+
        '   <input id="selectReason" value="" name="selectReason" type="text" class="form-control" style="height: 40px;line-height: 40px;padding-left: 75px;width: 250px;" autocomplete="off">'+
        '</div><br/>' +
        '<label><input name="Fruit" type="radio" value="结错账" />结错账 </label><br/>'+
        '<label><input name="Fruit" type="radio" value="用错优惠" />用错优惠 </label><br/>'+
        '<label><input name="Fruit" type="radio" value="用错会员" />用错会员 </label><br/>'+
        '<label><input name="Fruit" type="radio" value="客人投诉" />客人投诉 </label><br/>'+
        '</div>';

    var alertModal = widget.modal.alert({
        cls: 'fade in',
        content: str,
        width: 500,
        height: 500,
        title: "请选择反结原因",
        btnOkTxt: '确定',
        btnOkCb: function () {
            $(".modal-alert").modal("hide")
            $("#c-mod-fjs").load("../check/impower.jsp",{"title" : "反结算授权"});
            $("#c-mod-fjs").modal("show");
        },
        btnCancelCb: function () {

        }
    });
    //选择给input赋值
    $(".selectReason input").click(function () {

        $("#selectReason").val($(this).val())
    })
}

function orderlist(ordertype) {//表格赋值
    var str="";
    for( var i=0;i<tableList.length;i++){
        if(tableList[i].ordertype==ordertype){
            str+='<tr>';
            str+='   <td>'+tableList[i].order+'</td>'
            str+='   <td>'+tableList[i].ordertype+'</td>'
            str+='   <td>'+tableList[i].address+'</td>'
            str+='   <td>'+tableList[i].deskNo+'</td>'
            str+='   <td>'+tableList[i].waiterNo+'</td>'
            str+='   <td>'+tableList[i].startTime+'</td>'
            str+='   <td>'+tableList[i].endTime+'</td>'
            str+='   <td>'+tableList[i].people+'</td>'
            str+='   <td>'+tableList[i].money+'</td>'
            str+='   <td>'+tableList[i].orderUnit+'</td>'
            str+='   <td>'+tableList[i].phone+'</td>'
            str+='   <td>'+tableList[i].contacts+'</td>'
            str+="</tr>";
        }
        if(ordertype=="") {
            str+='<tr>';
            str+='   <td>'+tableList[i].order+'</td>'
            str+='   <td>'+tableList[i].ordertype+'</td>'
            str+='   <td>'+tableList[i].address+'</td>'
            str+='   <td>'+tableList[i].deskNo+'</td>'
            str+='   <td>'+tableList[i].waiterNo+'</td>'
            str+='   <td>'+tableList[i].startTime+'</td>'
            str+='   <td>'+tableList[i].endTime+'</td>'
            str+='   <td>'+tableList[i].people+'</td>'
            str+='   <td>'+tableList[i].money+'</td>'
            str+='   <td>'+tableList[i].orderUnit+'</td>'
            str+='   <td>'+tableList[i].phone+'</td>'
            str+='   <td>'+tableList[i].contacts+'</td>'
            str+="</tr>";
        }
    }
    $("#checklist tbody").html(str);
}