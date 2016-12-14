<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.candao.common.utils.PropertiesUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
</head>
<body>
<p>Version:V<%= PropertiesUtils.getValue("version")%></p>
<p>Author:xxx</p>
<p>Datetime: yyyy/MM/DD </p>
<p>Commit: xxx</p>
<p>Description:<br/></p>
<p>
<b>系统设置：</b><br>
是否开启一票一控：<%= PropertiesUtils.getValue("PRINTER_CONTROL").equalsIgnoreCase("Y") ? "是" : "否" %><br>
是否启用进销存：<%= PropertiesUtils.getValue("PSI_SHOW").equals("Y") ? "是" : "否" %><br>
上传结业数据的方式：<%= PropertiesUtils.getValue("SYN_DATA_TYPE") %><br>
</p>
</body>
