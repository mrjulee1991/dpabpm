
<%@page import="com.liferay.portal.kernel.exception.UserPasswordException"%>
<%@page import="com.liferay.portal.kernel.exception.UserEmailAddressException"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.dpabpm.account.search.AccountDisplayTerms"%>

<%@include file="init.jsp"%>

<%
String emailRegister = ParamUtil.getString(request, AccountDisplayTerms.EMAIL);
%>

<liferay-portlet:actionURL name="createAccount" var="createAccountURL" />

<liferay-ui:success 
	key="register-successfull.-url-verify-had-been-send-to-x" 
	message="<%= LanguageUtil.format(resourceBundle, "register-successfull.-url-verify-had-been-send-to-x", emailRegister) %>"
/>
<liferay-ui:error 
	exception="<%= UserEmailAddressException.MustNotBeDuplicate.class %>" 
	message="<%= LanguageUtil.format(resourceBundle, "email-x-address-already-existed", emailRegister) %>"
/>

<aui:form name="fm" action="<%= createAccountURL %>">
	<aui:row>
		<aui:col span="6">
			<aui:input name="<%= AccountDisplayTerms.LAST_NAME %>" label="<%= LanguageUtil.get(locale, AccountDisplayTerms.LAST_NAME) %>" >
				<aui:validator name="required" />
			</aui:input>
		</aui:col>
		<aui:col span="6">
			<aui:input name="<%= AccountDisplayTerms.FIRST_NAME %>" label="<%= LanguageUtil.get(locale, AccountDisplayTerms.FIRST_NAME) %>" >
				<aui:validator name="required" />
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:input name="<%= AccountDisplayTerms.EMAIL %>" >
		<aui:validator name="email" />
		<aui:validator name="required" />
	</aui:input>
	
	<aui:input name="<%= AccountDisplayTerms.PASSWORD_1 %>" type="password" >
		<aui:validator name="required" />
		<aui:validator name="minLength" >6</aui:validator>
	</aui:input>
	<aui:input name="<%= AccountDisplayTerms.PASSWORD_2 %>" type="password" >
		<aui:validator name="required" />
		<aui:validator name="minLength" >6</aui:validator>
		<aui:validator name="equalTo">'#<portlet:namespace /><%= AccountDisplayTerms.PASSWORD_1 %>'</aui:validator>
	</aui:input>
	
	<aui:row>
		<aui:col span="4">
			<aui:select name="<%= AccountDisplayTerms.BIRTH_DAY %>">
			<%
			for (int i = 1; i <= 31; i++){
				%>
				<aui:option value="<%= i %>" selected="<%= i == calender_now.get(Calendar.DAY_OF_MONTH) %>"><%= i %></aui:option>
				<%
			}
			%>
			</aui:select>
		</aui:col>
		
		<aui:col span="4">
			<aui:select name="<%= AccountDisplayTerms.BIRTH_MONTH %>" label="&nbsp;">
			<%
			for (int i = 0; i <= 11; i++){
				%>
				<aui:option value="<%= i %>" selected="<%= i == calender_now.get(Calendar.MONTH) %>"><%= LanguageUtil.get(locale, "month") + StringPool.SPACE + (i + 1) %></aui:option>
				<%
			}
			%>
			</aui:select>
		</aui:col>
		
		<aui:col span="4">
			<aui:select name="<%= AccountDisplayTerms.BIRTH_YEAR %>" label="&nbsp;">
			<%
			for (int i = 1900; i <= calender_now.get(Calendar.YEAR); i++){
				%>
				<aui:option value="<%= i %>" selected="<%= i == calender_now.get(Calendar.YEAR) - 18 %>"><%= i %></aui:option>
				<%
			}
			%>
			</aui:select>
		</aui:col>
	</aui:row>
	
	<aui:select name="<%= AccountDisplayTerms.GENDER %>">
		<aui:option value="0"><liferay-ui:message key="female" /></aui:option>
		<aui:option value="1"><liferay-ui:message key="male" /></aui:option>
	</aui:select>
	
	<aui:input name="<%= AccountDisplayTerms.TEL_NO %>" >
		<aui:validator name="digits" />
		<aui:validator name="minLength">10</aui:validator>
	</aui:input>
	
	<aui:button type="submit" value="sign-up"/>
</aui:form>
