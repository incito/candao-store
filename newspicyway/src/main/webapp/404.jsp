<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>404</title>
<script type="text/javascript">
	var global_ctxPath = '<%=request.getContextPath()%>'; 
</script>
<script type="text/javascript">
	function index(){
		window.parent.location.href = global_ctxPath + "/login/login";
	}
</script>
</head>
<body>
页面未找到。<br/>
您请求的页面未找到。您可以选择返回到 <a href="###" onclick="index()">首页</a>。或者选择在此休息一下。
</body>
</html>
