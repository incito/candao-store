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
            console.log(res)
         }
         })

    }
}