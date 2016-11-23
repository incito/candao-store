
$(function(){
    reporting.int();
})
var reporting={
    int:function () {
        SetBotoomIfon.init();//设置底部信息
        this.selectTab();
    },
    selectTab:function () {//切换选项卡
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
    },
    getItemSellDetail:function (flag) {//获取品项消费明细
            var me = $(flag),flag=me.attr("flag");
            me.addClass("active").siblings().removeClass('active')
            $("#getItemSellDetail tbody").html("");
            $.ajax({
                url:_config.interfaceUrl.GetReportDishInfo,
                type: "get",
                dataType: "json",
                data:{"flag":flag},
                success: function (data) {
                    //console.log(data)
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
                                str+='   <td width="476">'+data[i].dishName.split('#')[0]+'</td>';
                                str+='   <td width="200">'+data[i].dishCount+'</td>';
                                str+='   <td width="200">'+data[i].totlePrice+'</td>';
                                str+='</tr>';

                            };
                            $("#getItemSellDetail tbody").html(str);
                        }
                    });
                    /*如果没有数据分页显示统计1/1*/
                    if(data.data.length<1){
                        $('#getItemSellDetail .demo .J-paginationjs-nav').text('1/1')
                    }

                    $("#getItemSellDetail .reportingInfo i").eq(0).text(total);
                    $("#getItemSellDetail .reportingInfo i").eq(1).text(count.toFixed(1));
                    $("#getItemSellDetail .reportingInfo i").eq(2).text(sum.toFixed(2));

                },
            });
    },
    PrintItemSell:function () {//消费品项打印
        var flag=$("#getItemSellDetail .dataSelect-type .active" ).attr("flag"),
            that=this,
            TipListPrintLength=$('#getItemSellDetail tbody').find('tr').length;

        if(TipListPrintLength<1){
            utils.printError.alert('没有需要打印的报表数据')
            return false
        }
        $.ajax({
            url:_config.interfaceUrl.PrintItemSell,
            type: "get",
            dataType: "json",
            data:{"flag":flag,'deviceid':utils.storage.getter('posid')},
            success: function (data) {
                if(data.result=='0'){
                    rightBottomPop.alert({
                        content:"品项销售明细打印完成",
                    })
                }
                else {
                    utils.printError.alert('品项销售明细打印失败，请稍后重试')
                }
            },
        });
    },
    getTipList: function(flag) {//获取小费明细
        $("#getTipList tbody").html("");
        var me = $(flag), flag = me.attr("flag");
        me.addClass("active").siblings().removeClass('active')
        $.ajax({
            url: _config.interfaceUrl.GetReportTipInfo,
            type: "get",
            dataType: "json",
            data: {"flag": flag},
            success: function (data) {
                var str = "", total = data.data.length, count = 0, sum = 0;
                for (var i = 0; i < total; i++) {
                    count += Number(data.data[i].serviceCount);
                    sum += Number(data.data[i].tipMoney);
                    /*str += '<tr>';
                    str += '   <td>' + data.data[i].waiterName + '</td>';
                    str += '   <td>' + data.data[i].serviceCount + '</td>';
                    str += '   <td>' + data.data[i].tipMoney + '</td>';*/
                };
                $('#getTipList .demo').pagination({
                    dataSource: data.data,
                    pageSize: 11,
                    showPageNumbers: false,
                    showNavigator: true,
                    callback: function(data, pagination) {
                        var str="";
                        for( var i=0;i<data.length;i++) {
                            str+='<tr>';
                            str+='   <td width="476">'+data[i].waiterName+'</td>';
                            str+='   <td width="200">'+data[i].serviceCount+'</td>';
                            str+='   <td width="200">'+data[i].tipMoney+'</td>';
                            str+='</tr>';

                        };
                        $("#getTipList tbody").html(str);
                    }
                });
                /*如果没有数据分页显示统计1/1*/
                if(data.data.length<1){
                    $('#getTipList .demo .J-paginationjs-nav').text('1/1')
                }
                $("#getTipList .reportingInfo i").eq(0).text(total);
                $("#getTipList .reportingInfo i").eq(1).text(count.toFixed(1));
                $("#getTipList .reportingInfo i").eq(2).text(sum.toFixed(2));

            },
        });
    },
    TipListPrint:function(){//服务员小费打印
        var flag=$("#getTipList .dataSelect-type .active" ).attr("flag"),
            that=this,
            TipListPrintLength=$('#getTipList tbody').find('tr').length;
        if(TipListPrintLength<1){
            utils.printError.alert('没有需要打印的报表数据')
            return false
        }
        $.ajax({
            url:_config.interfaceUrl.PrintTip,
            type: "get",
            dataType: "json",
            data:{"flag":flag,'deviceid':utils.storage.getter('posid')},
            success: function (data) {
                if(data.result=='0'){
                    rightBottomPop.alert({
                        content:"服务员小费统计明细打印完成",
                    })
                }
                else {
                    utils.printError.alert('服务员小费统计明细打印失败，请稍后重试')
                }
            },
        });
    },
    printBusinessDetail:function () {//营业数据打印
        var flag=$("#getTipList .dataSelect-type .active" ).attr("flag"),
            that=this;
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
        $.ajax({
            url:_config.interfaceUrl.PrintBusinessDetail,
            type: "get",
            dataType: "json",
            data:{"beginTime":beginTime,"endTime":endTime,"operationname":operationname,'deviceid':utils.storage.getter('posid')},
            success: function (data) {
                if(data.result=='0'){
                    rightBottomPop.alert({
                        content:"营业数据明细打印完成",
                    })
                }
                else {
                    utils.printError.alert('营业数据明细打印失败，请稍后重试')
                }
            },
        });
    },
}

