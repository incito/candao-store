
$(function(){
    SetBotoomIfon.init();//设置底部信息
    var clickCunt=0
    $("#getItemSellDetail .dataSelect-type div").eq(0).trigger("click")
    var $tabBox = $('.tab-box');
    $('.J-g-menu li').click(function(){
        var me = $(this), idx = me.index()
        me.addClass('active').siblings().removeClass('active');
        if(idx==1){
            clickCunt++;
            if(clickCunt==1){
                $("#getTipList .dataSelect-type div").eq(0).trigger("click")
            }

        }
        $tabBox.find('.tab-item').hide().eq(idx).show();
    });

})
function ladingMore(msg) {//加载信息提示框
    var str = '<div style="height: 100px;"><strong >'+msg+'</strong></div>'
    var alertModal = widget.modal.alert({
        cls: 'fade in',
        content: str,
        width: 500,
        height: 500,
        hasBtns:false,

    });
}
function removeLadingMore(){//移除信息加载提示框
    $(".modal-alert").modal("hide");
}
function getItemSellDetail(flag) {//获取品项消费明细
    ladingMore("正在加载");
    var me = $(flag),flag=me.attr("flag");
    me.addClass("active").siblings().removeClass('active')
    $("#getItemSellDetail tbody").html("");
    $.ajax({
        url:'/newspicyway/padinterface/getItemSellDetail.json',
        type: "get",
        dataType: "json",
        data:{"flag":flag},
        success: function (data) {
            removeLadingMore();
            var total=data.data.length,count=0,sum=0;
            for( var i=0;i<total;i++) {
                count+=Number(data.data[i].dishCount);
                sum+=Number(data.data[i].totlePrice);

            };
            $('#getItemSellDetail .demo').pagination({
                dataSource: data.data,
                pageSize: 11,
                showPageNumbers: false,
                showNavigator: true,

                callback: function(data, pagination) {
                    var str="";
                    for( var i=0;i<data.length;i++) {
                        str+='<tr>';
                        str+='   <td width="476">'+data[i].dishName+'</td>';
                        str+='   <td width="200">'+data[i].dishCount+'</td>';
                        str+='   <td width="200">'+data[i].totlePrice+'</td>';
                        str+='</tr>';

                    };
                    $("#getItemSellDetail tbody").html(str);
                }
            });

            $("#getItemSellDetail .reportingInfo i").eq(0).text(total);
            $("#getItemSellDetail .reportingInfo i").eq(1).text(count.toFixed(1));
            $("#getItemSellDetail .reportingInfo i").eq(2).text(sum.toFixed(2));

        },
    });
}

function ItemSellDetailPrint(){//消费品项打印
    var flag=$("#getItemSellDetail .dataSelect-type .active" ).attr("flag");
    ladingMore("正在打印...");
    $.ajax({
        url:'/newspicyway/print4POS/getItemSellDetail.json',
        type: "get",
        dataType: "json",
        data:{"flag":flag},
        success: function (data) {
            removeLadingMore();

        },
    });
}
function getTipList(flag) {//获取小费明细
    var me = $(flag),flag=me.attr("flag");
    me.addClass("active").siblings().removeClass('active')
    $.ajax({
        url:'/newspicyway/tip/tipList.json',
        type: "get",
        dataType: "json",
        data:{"flag":flag},
        success: function (data) {
            var str="",total=data.data.length,count=0,sum=0;
            for( var i=0;i<total;i++) {
                count+=Number(data.data[i].dishCount);
                sum+=Number(data.data[i].totlePrice);
                str+='<tr>';
                str+='   <td>'+data.data[i].dishName+'</td>';
                str+='   <td>'+data.data[i].dishCount+'</td>';
                str+='   <td>'+data.data[i].totlePrice+'</td>';

            };


            $("#getTipList .reportingInfo i").eq(0).text(total);
            $("#getTipList .reportingInfo i").eq(1).text(count.toFixed(1));
            $("#getTipList .reportingInfo i").eq(2).text(sum.toFixed(2));

        },
    });
}
function TipListPrint(){//服务员小费打印
    var flag=$("#getTipList .dataSelect-type .active" ).attr("flag");
    ladingMore("正在打印...");
    $.ajax({
        url:'/newspicyway/print4POS/getItemSellDetail.json',
        type: "get",
        dataType: "json",
        data:{"flag":flag},
        success: function (data) {
            removeLadingMore();

        },
    });
}
function printBusinessDetail(){//营业数据打印
    var flag=$("#getTipList .dataSelect-type .active" ).attr("flag");
    var beginTime=$.trim($(".datetimeStart").val()),endTime=$.trim($(".datetimeEnd").val()),operationname=utils.storage.getter('aUserid');
    if(beginTime=="" || beginTime==""){
        var str = '<div><strong >开始和结束日期不能为空</strong></div>'
        var alertModal = widget.modal.alert({
            cls: 'fade in',
            content: str,
            width: 500,
            height: 300,
            btnOkTxt: '确定',
            btnCancelTxt:"",
            btnOkCb: function(){
                $(".modal-alert").modal("hide");
            },

        });
        return
    }
    ladingMore("正在打印...");
    $.ajax({
        url:'/newspicyway/print4POS/printBusinessDetail.json',
        type: "get",
        dataType: "json",
        data:{"beginTime":beginTime,"endTime":endTime,"operationname":operationname},
        success: function (data) {
            removeLadingMore();

        },
    });
}
