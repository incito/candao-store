<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setAttribute("path", request.getContextPath());
%>
<script type="text/javascript">
	var global_Path = '${path}';
</script>
<link rel="stylesheet" type="text/css" href="${path}/css/easyui/metro/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${path}/css/easyui/icon.css"/>
<link rel="stylesheet" type="text/css" href="${path}/css/common/common.css"/>
<link rel="stylesheet" href="${path}/css/ztree/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="${path}/css/xld/xldcommon.css" type="text/css">
<script type="text/javascript" src="${path}/scripts/jquery.min.js"></script>
<script type="text/javascript" src="${path}/scripts/ajaxfileupload.js"></script>
<script type="text/javascript" src="${path}/scripts/jquery.json-2.3.js"></script>
<script type="text/javascript" src="${path}/scripts/uuid.js"></script>
<script type="text/javascript" src="${path}/scripts/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${path}/scripts/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${path}/scripts/validatebox.js"></script>
<script type="text/javascript" src="${path}/scripts/commons.js"></script>
<script type="text/javascript" src="${path}/scripts/easyuibugs.js"></script>
<script type="text/javascript" src="${path}/scripts/jqueryUtil.js"></script>
