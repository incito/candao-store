<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>门店角色-表单</title>
<%-- <link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet"> --%>
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/tenant.css"/> --%>
<%-- <script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script> --%>
</head>
<body>
<form id="limiteForm" name="limiteForm">
	<%-- <input id="roleId" value="${roleVO.role.id}" type="hidden" />
	 --%>
	<div class="modal-header limiteInfo">
		<label>角色名称：</label> 
		<%-- 
		<input id="roleName" value="${roleVO.role.name }" disabled="disabled"/>
		 --%>
	</div>
	<div class="">
		<div class="limiteInner">
		<c:forEach items="${functions}" var="topfn">
			<%-- <div>${topfn.name }</div> --%>
			<ul>
				<c:forEach var="fn" items="${topfn.childFuns}">
					<li>
						<dl>
							<dt>${fn.name}</dt>
							<c:forEach items="${fn.childFuns }" var="subFn">
								<dd >
									<c:choose>
										<c:when test="${!empty selectedMap[subFn.id] }">
											<input type="checkbox" checked="true" value="${subFn.id}"  disabled="disabled"/>
										</c:when>
										<c:otherwise>
											<input type="checkbox" value="${subFn.id}"  disabled="disabled"/>
										</c:otherwise>
									</c:choose>
									<label for="">${subFn.name}</label>
								</dd>
							</c:forEach>
						</dl>
					</li>
				</c:forEach>
			</ul>
		</c:forEach>
		</div>

	</div>
</form>


</body>
</html>