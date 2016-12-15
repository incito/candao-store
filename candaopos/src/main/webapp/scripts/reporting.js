
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
            var me = $(flag),flag=me.attr("flag"),that=this;
            me.addClass("active").siblings().removeClass('active');
            $("#getItemSellDetail tbody").html("");
            if(flag=='5'){
                $('#getItemSellDetail-when').show();
                $('.getItemSellDetailStart').focus().val('');
                $('.getItemSellDetailEnd').val('');
                return false
            }else {
                $('#getItemSellDetail-when').hide();
            }
            Log.send(2, me.text()+'品项销售明细接口请求参数有:'+JSON.stringify({"flag":flag}))
            $.ajax({
                url:_config.interfaceUrl.GetReportDishInfo,
                method: 'POST',
                contentType: "application/json",
                dataType: 'json',
                data:JSON.stringify({"flag":flag}),
                success: function (data) {
                    Log.send(2, me.text()+'品项销售明细返回:'+JSON.stringify(data))
                    if(data.code=='1'){
                        utils.printError.alert(data.msg);
                        return false
                    }
                    var total=data.data.data.length,count=0,sum=0;
                    for( var i=0;i<total;i++) {
                        count+=Number(data.data.data[i].dishCount);
                        sum+=Number(data.data.data[i].totlePrice);

                    };

                    $('#getItemSellDetail .demo').pagination({
                        dataSource: data.data.data,
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
    getItemSellDetail_when:function () {
        var that=this,beginTime=$.trim($('.getItemSellDetailStart').val()),endTime=$.trim($('.getItemSellDetailEnd').val());
        if(beginTime==''||endTime==''){
            utils.printError.alert('开始时间和结束时间不能为空');
            return false
        }
        if(Date.parse(new Date(endTime))/1000-Date.parse(new Date(beginTime))/1000<0){
            utils.printError.alert('开始时间不能大于结束时间');
            return false
        }
        $("#getItemSellDetail tbody").html("");
        Log.send(2, '品项销售明细时间段选择：开始时间：'+beginTime+'，结束时间：'+endTime)
        $.ajax({
            url:_config.interfaceUrl.GetReportDishInfo,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data:JSON.stringify({"startTime":beginTime,'endTime':endTime}),
            success: function (data) {
                Log.send(2, '品项销售明细时间段选择数据返回'+JSON.stringify(data))
                if(data.code=='1'){
                    utils.printError.alert(data.msg);
                    return false
                }
                var total=data.data.data.length,count=0,sum=0;
                for( var i=0;i<total;i++) {
                    count+=Number(data.data.data[i].dishCount);
                    sum+=Number(data.data.data[i].totlePrice);

                };

                $('#getItemSellDetail .demo').pagination({
                    dataSource: data.data.data,
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
            TipListPrintLength=$('#getItemSellDetail tbody').find('tr').length,
            res=null,
            beginTime=$.trim($('.getItemSellDetailStart').val()),
            endTime=$.trim($('.getItemSellDetailEnd').val());
        if(flag=='5'){
            if(beginTime==''||endTime==''){
                utils.printError.alert('开始时间和结束时间不能为空');
                return false
            }
            if(Date.parse(new Date(endTime))/1000-Date.parse(new Date(beginTime))/1000<0){
                utils.printError.alert('开始时间不能大于结束时间');
                return false
            }
            res={
                'deviceid':utils.storage.getter('posid'),
                "startTime":beginTime,
                'endTime':endTime
            }
        }
        else {
            res={
                "flag":flag,
                'deviceid':utils.storage.getter('posid'),
            }
        }
        if(TipListPrintLength<1){
            utils.printError.alert('没有需要打印的报表数据')
            return false
        }
        Log.send(2, '消费品项打印参数：'+JSON.stringify(res))
        $.ajax({
            url:_config.interfaceUrl.PrintItemSell,
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            data:JSON.stringify(res),
            success: function (data) {
                Log.send(2, '消费品项打印参数返回参数：'+JSON.stringify(data))
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
                if(data.result=='1'){
                    utils.printError.alert(data.mag);
                    return false
                }
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
                Log.send(2, '服务员小费统计明细打印参数返回：'+JSON.stringify(data))
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
        var abc=Date.parse(new Date(beginTime))/1000
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
        if(Date.parse(new Date(endTime))/1000-Date.parse(new Date(beginTime))/1000<0){
            utils.printError.alert('开始时间不能大于结束时间');
            return false
        }
        Log.send(2, '营业数据打印：开始时间：'+beginTime+'，结束时间：'+endTime+"，operationname："+operationname+'，deviceid：'+utils.storage.getter('posid'));
        $.ajax({
            url:_config.interfaceUrl.PrintBusinessDetail,
            type: "get",
            dataType: "json",
            data:{"beginTime":beginTime,"endTime":endTime,"operationname":operationname,'deviceid':utils.storage.getter('posid')},
            success: function (data) {
                if(data.result=='0'){
                    Log.send(2, '营业数据打印返回成功参数：'+JSON.stringify(data));
                    rightBottomPop.alert({
                        content:"营业数据明细打印完成",
                    })
                }
                else {
                    Log.send(2, '营业数据打印失败参数：'+JSON.stringify(data));
                    utils.printError.alert('营业数据明细打印失败，请稍后重试')
                }
            },
        });
    },
}

