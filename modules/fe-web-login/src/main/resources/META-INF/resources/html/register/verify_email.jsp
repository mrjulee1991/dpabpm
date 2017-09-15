<%@page import="com.liferay.portal.kernel.service.TicketLocalServiceUtil"%>
<%@page import="com.dpabpm.account.service.AccountLocalServiceUtil"%>
<%@page import="com.dpabpm.account.model.Account"%>

<%@include file="init.jsp"%>

<%
boolean isValidEmail = GetterUtil.getBoolean(PortalUtil.getOriginalServletRequest(request).getParameter("isValidEmail"));
String key = GetterUtil.getString(PortalUtil.getOriginalServletRequest(request).getParameter("key"));

String email = StringPool.BLANK;
try {
	email = TicketLocalServiceUtil.getTicket(key).getExtraInfo();
} catch (Exception e){
	_log.error(e);
}
%>

<div class="inline-alert-container lfr-alert-container"></div>
<liferay-ui:success key="key-had-been-send-to-your-email" message="key-had-been-send-to-your-email" />

<liferay-portlet:actionURL name="resendKey" var="resendKeyURL">
	<liferay-portlet:param name="<%= AccountDisplayTerms.EMAIL %>" value="<%= email %>"/>
</liferay-portlet:actionURL>

<c:choose>
	<c:when test="<%= isValidEmail && Validator.isNotNull(email) %>">
		<liferay-ui:message key="verify-email-successful" />
		<aui:button-row>
			<aui:button href="<%= themeDisplay.getPortalURL() %>" value="home-page" />
		</aui:button-row>
	</c:when>
	<c:when test="<%= !isValidEmail %>">
		<liferay-ui:message key="verify-email-failure.-your-key-is-expired-or-not-available" />
		<c:choose>
			<c:when test="<%= Validator.isNotNull(email) %>">
				<aui:button-row>
					<aui:button href="<%= resendKeyURL.toString() %>" value="resend-key" />
					<aui:button href="<%= themeDisplay.getPortalURL() %>" value="home-page" />
				</aui:button-row>
			</c:when>
		</c:choose>
	</c:when>
	<c:otherwise>
		<liferay-ui:message key="invalid-url" />
		<aui:button-row>
			<aui:button href="<%= themeDisplay.getPortalURL() %>" value="home-page" />
		</aui:button-row>
	</c:otherwise>
</c:choose>

<%!
private Log _log = LogFactoryUtil.getLog("fe-web-login/html/register/verify_email.jsp");
%>