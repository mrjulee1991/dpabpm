
<%@include file="init.jsp"%>

<%
String action = GetterUtil.getString(PortalUtil.getOriginalServletRequest(request).getParameter("action"));
%>

<c:choose>
	<c:when test="<%= action.equals("verify_email") %>">
		<liferay-util:include page="/html/register/verify_email.jsp" servletContext="<%= application %>" />
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/html/register/_register.jsp" servletContext="<%= application %>" />
	</c:otherwise>
</c:choose>


