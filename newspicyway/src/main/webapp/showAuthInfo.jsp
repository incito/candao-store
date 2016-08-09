<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>显示个人权限信息</title>
	<link rel="stylesheet" href="<%=basePath%>/tools/bootstrap/css/bootstrap.css">
	<script src="<%=basePath%>/scripts/jquery.js"></script>
	<script src="<%=basePath%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		var global_baseurl = '<%=basePath%>';
		var global_account = '<%=request.getParameter("account")%>';
		
		$(function(){
			//初始化列表
			$.post(global_baseurl+"/login/showAuthInfo", {account:global_account}, function(result){
				$("#infoTable").empty();
				var html = '';
				var dataMap = $.parseJSON(result);
				//遍历角色
				for(var roleStr in dataMap){
					var roleStrArray = roleStr.split(';');
					html += '<tr><td>';
					html += '   角色id:'+roleStrArray[0];
					html += '<td></td><td></td><td></tr>';
					
					html += '<tr><td>';
					html += '   角色名称:'+roleStrArray[1];
					html += '<td></td><td></td><td></tr>';
					//遍历功能
					var roleMap = dataMap[roleStr];
					for(var funStr in roleMap){
						var funStrArray = funStr.split(';');
						html += '<tr><td></td><td>';
						html += '   功能Id:'+funStrArray[0];
						html += '</td><td></td></tr>';
						
						html += '<tr><td></td><td>';
						html += '   功能Code:'+funStrArray[2];
						html += '</td><td></td></tr>';
						
						html += '<tr><td></td><td>';
						html += '   功能名称:'+funStrArray[1];
						html += '</td><td></td></tr>';
						//遍历URL
						var funMap = roleMap[funStr];
						for(var urlStr in funMap){
							var urlObj = funMap[urlStr];
							html += '<tr><td></td><td></td><td>';
							html += '	URL的Id:'+urlObj['urlId'];
							html += '</td></tr>';
							
							html += '<tr><td></td><td></td><td>';
							html += '	URL名称:'+urlObj['urlName'];
							html += '</td></tr>';
							
 							html += '<tr><td></td><td></td><td>';
							html += '   <i><u>URL路径:'+urlObj['url']+"</u></i>";
							html += '</td></tr>';
						}
					}
				}
				$("#infoTable").html(html);
			});
		});
	</script>
</head>
<body>
	<table>
		<thead>
		<tr>
			<td>角色</td>
			<td>功能</td>
			<td>URL</td>
		</tr>
		</thead>
		<tbody id="infoTable">
		
		</tbody>
	</table>
</body>