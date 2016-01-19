<%@page import="com.candao.www.data.model.User"%>
<%@page import="com.candao.www.permit.common.Constants"%>
<%@page import="com.candao.common.utils.Constant"%>
<%@page import="com.candao.common.utils.PropertiesUtils"%>
<%
String url=request.getParameter("url");
//RequestDispatcher rd = request.getRequestDispatcher(url);
response.addHeader("P3P","CP=CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR");
String session1 = (String) request.getSession().getAttribute("session1");
String branchid = (String) request.getSession().getAttribute("_BRANCH_ID");
if(branchid == null){
  branchid = PropertiesUtils.getValue("current_branch_id");//获取默认门店
}
	//此处先注释，待userservice包完全提交后再放开
	String host = PropertiesUtils.getValue("cloud.host");
	String webRoot = PropertiesUtils.getValue("cloud.webroot");
	String webPath = "http://" + host + "/"+ webRoot;
 	User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER);
 	
 	String currentTenantId = (String)request.getSession().getAttribute(Constant.CURRENT_TENANT_ID);
%>
<script src="scripts/jquery.js"></script>
<script type="text/javascript">
var session1;
$(function(){  
    jQuery.getJSON("<%=webPath%>/login/ssoLogin.json?tenantId=<%=currentTenantId%>&username=<%=user.getAccount()%>&password=<%=user.getPassword()%>&jsonp=?",function(data){
    	//session1=data.session1;
    	if( data.isSuccess){
    		$(parent.document.all("detail")).attr("src",'<%=webPath+"/"+url%>?branchid=<%=branchid%>');
    	}else{
    		alert(data.message);
    	}
    });   
});  
</script>  
