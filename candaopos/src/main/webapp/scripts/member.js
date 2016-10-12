$(function () {
    member.int()
})
var member={
    int:function () {
        SetBotoomIfon.init()//设置底部信息
        this.memberSearch();

    },
    /*餐道会员查询*/
    memberSearch:function () {
        $.ajax({
         url:utils.storage.getter('memberAddress')+ _config.interfaceUrl.VipQuery,
         type: 'POST',
         data:JSON.stringify({
         securityCode: '',
         branch_id: utils.storage.getter('branch_id'),
         //cardno:$.trim($('#cardno').val())
         cardno:'15208158540'
         }),
         success: function(res){
             var res =JSON.parse(res)
             if(res.retcode==0){
                 $('.member_card').text(res.result[0].MCard)//卡号
                 $('.member_mobile').text(res.mobile)//手机号
                 $('.member_nanme').text(res.name)//姓名
                 $('.member_level_name').text(res.result[0].level_name)//会员卡等级名称
                 $('.member_birthday').text((res.birthday).substring(0,res.birthday.length-8))//生日
                 $('.member_gender').text(res.gender)//性别
                 $('.member_StoreCardBalance').text(res.result[0].StoreCardBalance)//余额
                 $('.member_IntegralOverall').text(res.result[0].IntegralOverall)//会员积分
                 $('.member_status').text(res.result[0].status)//会员卡状态
             }
            console.log(res)
         }
         })

    }
}