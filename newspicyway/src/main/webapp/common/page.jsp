<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <c:if test="${totalpage!='1' && totalpage!='0'}">			  
				<p class="paging">
					<c:if test="${current >1}">
					<a id="prev" class="prev" onclick="jumpPage('${current-1}');"><</a>
					</c:if>
					
					<c:if test="${1<(current-3)}">
					<a id="less" href="javascript:void(0)"  >...</a>
					</c:if>
					
					<c:forEach var="x" begin="1" end="${totalpage}">
						<c:if test="${x>=(current-3) && x<=(current +3)}">
							<c:choose>
								<c:when test="${x== current }">
									<a id="cur" href="javascript:void(0)" onclick="jumpPage('${x}');" class="active">${x}</a>
								</c:when>
								<c:otherwise>
									<a id="cur" href="javascript:void(0)" onclick="jumpPage('${x}');" >${x}</a>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
					
					<c:if test="${(totalpage)>(current+3)}">
								 <a href="javascript:void(0)">....</a> 
					</c:if>
					<c:if test="${totalpage >current}">
									<a id="next" href="javascript:void(0)" onclick="jumpPage('${current+1}');" class="next">></a>
					</c:if>
	
				</p>
			</c:if>