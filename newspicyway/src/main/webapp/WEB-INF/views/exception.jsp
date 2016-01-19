<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%response.setStatus(200);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>500 - 系统内部错误</title>
		<script type="text/javascript">
			var global_ctxPath = '<%=request.getContextPath()%>'; 
		</script>
	</head>
	<body>
		<h2>500 - 系统发生内部错误.</h2>
		<p><a href="<c:url value="/index"/>">返回首页</a></p>
		<pre>${exception }</pre>
	</body>
</html>

