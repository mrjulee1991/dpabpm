<%@page import="com.dpabpm.account.constants.AccountConstants"%>
<%@include file="init.jsp"%>

<%
int action = GetterUtil.getInteger(PortalUtil.getOriginalServletRequest(request).getParameter("action"));
boolean isValidTicketKey = GetterUtil.getBoolean(PortalUtil.getOriginalServletRequest(request).getParameter("isValidTicketKey"));
String ticketKey = GetterUtil.getString(PortalUtil.getOriginalServletRequest(request).getParameter("ticketKey"));

int postAction = ParamUtil.getInteger(request, "postAction");

Account acc = null;
try {
	acc = AccountLocalServiceUtil.findByEmail(TicketLocalServiceUtil.getTicket(ticketKey).getExtraInfo());
} catch (Exception e){
	_log.error(e);
}
%>

<portlet:actionURL name="/login/reset_password" var="resetPasswordURL">
	<portlet:param name="mvcRenderCommandName" value="/login/reset_password" />
</portlet:actionURL>

<aui:form action="<%= resetPasswordURL %>" method="post" name="fm">
	
	<div class="inline-alert-container lfr-alert-container"></div>

	<aui:fieldset>
		<c:choose>
			<c:when test="<%= isValidTicketKey && Validator.isNotNull(acc) || postAction == AccountConstants.ACTION_RESET_PASSWORD %>">
			
				<aui:input name="ticketKey" value="<%= ticketKey %>" type="hidden" />

				<aui:input name="<%= AccountDisplayTerms.PASSWORD_1 %>" type="password" >
					<aui:validator name="required" />
					<aui:validator name="minLength" >6</aui:validator>
				</aui:input>
				
				<aui:input name="<%= AccountDisplayTerms.PASSWORD_2 %>" type="password" >
					<aui:validator name="required" />
					<aui:validator name="minLength" >6</aui:validator>
					<aui:validator name="equalTo">'#<portlet:namespace /><%= AccountDisplayTerms.PASSWORD_1 %>'</aui:validator>
				</aui:input>

				<aui:button-row>
					<aui:button cssClass="btn-lg" type="submit" value="confirm" />
				</aui:button-row>
			</c:when>
			
			<c:otherwise>
				<div class="alert alert-warning">
					<liferay-ui:message key="invalid-url" />
				</div>
			</c:otherwise>
		</c:choose>
	</aui:fieldset>
</aui:form>

<%!
private Log _log = LogFactoryUtil.getLog("fe-web-login/html/register/reset_password.jsp");
%>