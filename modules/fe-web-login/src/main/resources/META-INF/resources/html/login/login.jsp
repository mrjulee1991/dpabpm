<%@page import="com.liferay.portal.kernel.exception.NoSuchEmailAddressException"%>
<%@page import="com.liferay.portal.kernel.exception.UserActiveException"%>
<%@page import="javax.portlet.WindowState"%>
<%@include file="init.jsp"%>

<div class="inline-alert-container lfr-alert-container"></div>

<c:choose>
    <c:when test="<%= themeDisplay.isSignedIn() %>">

        <%
        String signedInAs = HtmlUtil.escape(user.getFullName());

        if (themeDisplay.isShowMyAccountIcon() && (themeDisplay.getURLMyAccount() != null)) {
            String myAccountURL = String.valueOf(themeDisplay.getURLMyAccount());

            signedInAs = "<a class=\"signed-in\" href=\"" + HtmlUtil.escape(myAccountURL) + "\">" + signedInAs + "</a>";
        }
        %>

        <p><liferay-ui:message key="<%= LanguageUtil.format(resourceBundle, "you-are-signed-in-as-x", signedInAs) %>" /></p>
        
        <p><aui:button href="/c/portal/logout" value="sign-out" /></p>
        
        <portlet:renderURL var="profileURL" windowState="<%= WindowState.MAXIMIZED.toString() %>">
			<portlet:param name="mvcRenderCommandName" value="/account/profile" />
		</portlet:renderURL>
        
        <aui:a href="<%= profileURL %>" label="profile" />
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
        	<liferay-ui:error exception="<%= UserActiveException.class %>" message="verify-email-and-login-again" />
        	<liferay-ui:error exception="<%= NoSuchEmailAddressException.class %>" message="email-not-exist" />
        	
        	<aui:fieldset>
	            <aui:input name="saveLastPath" type="hidden" value="<%= false %>" />
	            <aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	            
	            <aui:input autoFocus="true" cssClass="clearable" label="email-address" name="login" showRequiredLabel="<%= false %>" type="text">
	                <aui:validator name="required" />
	            </aui:input>
	            
	            <aui:input name="password" showRequiredLabel="<%= false %>" type="password">
	                <aui:validator name="required" />
	            </aui:input>
	        </aui:fieldset>

            <aui:button-row>
                <aui:button cssClass="btn-lg" type="submit" value="sign-in" />
            </aui:button-row>
            
            <portlet:renderURL var="forgotPasswordURL" windowState="<%= WindowState.MAXIMIZED.toString() %>">
				<portlet:param name="mvcRenderCommandName" value="/login/forgot_password" />
			</portlet:renderURL>
			
			<aui:a href="<%= forgotPasswordURL %>" label="forgot-password" />
			
        </aui:form>
    </c:otherwise>
</c:choose>