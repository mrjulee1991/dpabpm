<%@page import="com.dpabpm.account.constants.AccountConstants"%>
<%@ include file="init.jsp" %>

<%
int action = GetterUtil.getInteger(PortalUtil.getOriginalServletRequest(request).getParameter("action"));
boolean isValidTicketKey = GetterUtil.getBoolean(PortalUtil.getOriginalServletRequest(request).getParameter("isValidTicketKey"));
String ticketKey = GetterUtil.getString(PortalUtil.getOriginalServletRequest(request).getParameter("ticketKey"));
%>

<c:choose>
	<c:when test="<%= action == AccountConstants.ACTION_VERIFY_EMAIL %>">
		<liferay-util:include page="/html/account/verify_email.jsp" servletContext="<%= application %>" />
	</c:when>
	<c:when test="<%= action == AccountConstants.ACTION_RESET_PASSWORD %>">
		<liferay-util:include page="/html/account/reset_password.jsp" servletContext="<%= application %>" />
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/html/account/register.jsp" servletContext="<%= application %>" />
	</c:otherwise>
</c:choose>


