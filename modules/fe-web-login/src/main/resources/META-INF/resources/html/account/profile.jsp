<%@include file="init.jsp"%>

<%
Account acc = null;
try {
	acc = AccountLocalServiceUtil.findByEmail(user.getEmailAddress());
} catch (Exception e){
	_log.error(e);
}
%>

<c:if test="<%= Validator.isNotNull(acc) %>">
	
	<portlet:actionURL name="/account/profile" var="updateProfileURL">
		<portlet:param name="mvcRenderCommandName" value="/account/profile" />
	</portlet:actionURL>

	<aui:form name="fm" action="<%= updateProfileURL %>" method="POST" enctype="multipart/form-data">
		<aui:fieldset>
			<aui:input name="<%= AccountDisplayTerms.FULL_NAME %>" label="<%= AccountDisplayTerms.FULL_NAME %>" value="<%= acc.getFullName() %>"/>
			
			<aui:input name="<%= AccountDisplayTerms.AVATAR %>" type="file">
				<aui:validator name="acceptFiles">.png,.jsp,.jpeg,.gif</aui:validator>
			</aui:input>
			
			<aui:button-row>
				<aui:button name="save" type="submit"/>
			</aui:button-row>
		</aui:fieldset>
	</aui:form>
</c:if>

<%!
private Log _log = LogFactoryUtil.getLog("fe-web-login/resources/html/account/profile.jsp");
%>