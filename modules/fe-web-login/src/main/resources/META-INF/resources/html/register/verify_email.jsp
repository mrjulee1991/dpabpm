
<%@include file="init.jsp"%>

<%
boolean isValidKey = GetterUtil.getBoolean(PortalUtil.getOriginalServletRequest(request).getParameter("isValidKey"));
String key = GetterUtil.getString(PortalUtil.getOriginalServletRequest(request).getParameter("key"));

Account acc = null;
try {
	acc = AccountLocalServiceUtil.findByEmail(TicketLocalServiceUtil.getTicket(key).getExtraInfo());
} catch (Exception e){
	_log.error(e);
}
%>

<div class="inline-alert-container lfr-alert-container"></div>
<liferay-ui:success key="key-had-been-send-to-your-email" message="key-had-been-send-to-your-email" />

<liferay-portlet:actionURL name="resendKey" var="resendKeyURL">
	<liferay-portlet:param name="<%= AccountDisplayTerms.EMAIL %>" value="<%= acc == null ? "" : HtmlUtil.escape(acc.getEmail()) %>"/>
</liferay-portlet:actionURL>

<c:choose>
	<c:when test="<%= isValidKey && Validator.isNotNull(acc) %>">
		<liferay-ui:message key="verify-email-successful" />
		<aui:button-row>
			<aui:button href="<%= themeDisplay.getPortalURL() %>" value="home-page" />
		</aui:button-row>
	</c:when>
	<c:when test="<%= !isValidKey && Validator.isNotNull(acc) && acc.getStatus() != WorkflowConstants.STATUS_APPROVED %>">
		<liferay-ui:message key="verify-email-failure.-your-key-is-expired-or-not-available" />
		<aui:button-row>
			<aui:button href="<%= resendKeyURL.toString() %>" value="resend-key" />
			<aui:button href="<%= themeDisplay.getPortalURL() %>" value="home-page" />
		</aui:button-row>
	</c:when>
	<c:when test="<%= !isValidKey && Validator.isNotNull(acc) && acc.getStatus() == WorkflowConstants.STATUS_APPROVED %>">
		<liferay-ui:message key="your-account-is-allready-actived" />
		<aui:button-row>
			<aui:button href="<%= themeDisplay.getPortalURL() %>" value="home-page" />
		</aui:button-row>
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