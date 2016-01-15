<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="menus" tagdir="/WEB-INF/tags/menus"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="blue" uri="/functions/blue"%>
<%@ page import="com.candao.file.common.Constant" language="java"%>
<script type="text/javascript">
	var global_Path = '<%=request.getContextPath()%>';
	var img_Path = '<%=Constant.FILEURL_PREFIX%>';
	var isbranch = '<%=Constant.ISBRANCH%>';
</script>
