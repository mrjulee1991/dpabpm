<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.dpabpm.account.search.AccountDisplayTerms"%>

<%@include file="init.jsp"%>

<liferay-portlet:actionURL name="createAccount" var="createAccountURL" />

<aui:form name="fm" action="<%= createAccountURL %>">
	<aui:input name="<%= AccountDisplayTerms.LAST_NAME %>" label="<%= LanguageUtil.get(locale, AccountDisplayTerms.LAST_NAME) %>" />
	<aui:input name="<%= AccountDisplayTerms.FIRST_NAME %>" label="<%= LanguageUtil.get(locale, AccountDisplayTerms.FIRST_NAME) %>" />
	<aui:select name="<%= AccountDisplayTerms.GENDER %>">
		<aui:option value="0"><liferay-ui:message key="female" /></aui:option>
		<aui:option value="1"><liferay-ui:message key="male" /></aui:option>
	</aui:select>
	<aui:input name="<%= AccountDisplayTerms.BIRTH_DATE %>" type="Date" />
	<aui:input name="<%= AccountDisplayTerms.EMAIL %>" />
	<aui:input name="<%= AccountDisplayTerms.TEL_NO %>" />
	<aui:button type="submit" value="save"/>
</aui:form>

