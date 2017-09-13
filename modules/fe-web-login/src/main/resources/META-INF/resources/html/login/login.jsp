
<%@page import="com.liferay.portal.kernel.security.auth.AuthException"%>
<%@include file="init.jsp"%>

<c:choose>
    <c:when test="<%= themeDisplay.isSignedIn() %>">

        <%
        String signedInAs = HtmlUtil.escape(user.getFullName());

        if (themeDisplay.isShowMyAccountIcon() && (themeDisplay.getURLMyAccount() != null)) {
            String myAccountURL = String.valueOf(themeDisplay.getURLMyAccount());

            signedInAs = "<a class=\"signed-in\" href=\"" + HtmlUtil.escape(myAccountURL) + "\">" + signedInAs + "</a>";
        }
        %>

        <liferay-ui:message arguments="<%= signedInAs %>" key="you-are-signed-in-as-x" translateArguments="<%= false %>" />
    </c:when>
    <c:otherwise>
    
        <%
        String redirect = ParamUtil.getString(request, "redirect");
        %>
        
        <portlet:actionURL name="/login/login" var="loginURL">
            <portlet:param name="mvcRenderCommandName" value="/login/login" />
        </portlet:actionURL>

        <aui:form action="<%= loginURL %>" autocomplete='on' cssClass="sign-in-form" method="post" name="loginForm">
        
        	<liferay-ui:error exception="<%= AuthException.class %>" message="authentication-failed" />
        	<liferay-ui:error key="verify-email-and-login-again" message="verify-email-and-login-again" />
        	<liferay-ui:error key="email-not-exist" message="email-not-exist" />
        	
        	<aui:fieldset>
	            <aui:input name="saveLastPath" type="hidden" value="<%= false %>" />
	            <aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	            <aui:input autoFocus="true" cssClass="clearable" label="email-address" name="login" showRequiredLabel="<%= false %>" type="text" value="">
	                <aui:validator name="required" />
	            </aui:input>
	            <aui:input name="password" showRequiredLabel="<%= false %>" type="password">
	                <aui:validator name="required" />
	            </aui:input>
	        </aui:fieldset>

            <aui:button-row>
                <aui:button cssClass="btn-lg" type="submit" value="sign-in" />
            </aui:button-row>
        </aui:form>
    </c:otherwise>
</c:choose>