<%@page import="java.util.Date"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.dpabpm.account.search.AccountDisplayTerms"%>

<%@include file="init.jsp"%>

<liferay-portlet:actionURL name="createAccount" var="createAccountURL" />

<aui:row>
	<aui:col span="6"></aui:col>
	<aui:col span="6">
		<aui:form name="fm" action="<%= createAccountURL %>">
			<aui:row>
				<aui:col span="6">
					<aui:input name="<%= AccountDisplayTerms.LAST_NAME %>" label="<%= LanguageUtil.get(locale, AccountDisplayTerms.LAST_NAME) %>" />
				</aui:col>
				<aui:col span="6">
					<aui:input name="<%= AccountDisplayTerms.FIRST_NAME %>" label="<%= LanguageUtil.get(locale, AccountDisplayTerms.FIRST_NAME) %>" />
				</aui:col>
			</aui:row>
			
			<aui:input name="<%= AccountDisplayTerms.EMAIL %>" />
			
			<aui:input name="<%= AccountDisplayTerms.PASSWORD_1 %>" type="password" />
			<aui:input name="<%= AccountDisplayTerms.PASSWORD_2 %>" type="password" />
			
			<aui:row>
				<aui:col span="3">
					<aui:input name="<%= AccountDisplayTerms.BIRTH_DAY %>" type="number" min="1" max="31"/>
				</aui:col>
				
				<aui:col span="6">
					<aui:select name="<%= AccountDisplayTerms.BIRTH_MONTH %>">
						<%
						for (int i = 1; i <= 12; i++){
							%>
							<aui:option value="<%= i %>"><%= LanguageUtil.get(locale, "month") + i %></aui:option>
							<%
						}
						%>
					</aui:select>
				</aui:col>
				
				<aui:col span="3">
					<aui:input name="<%= AccountDisplayTerms.BIRTH_YEAR %>" type="number" min="1900" max="2010"/>
				</aui:col>
			</aui:row>
			
			<aui:select name="<%= AccountDisplayTerms.GENDER %>">
				<aui:option value="0"><liferay-ui:message key="female" /></aui:option>
				<aui:option value="1"><liferay-ui:message key="male" /></aui:option>
			</aui:select>
			
			<aui:input name="<%= AccountDisplayTerms.TEL_NO %>" />
			
			<aui:button type="submit" value="save"/>
		</aui:form>
	</aui:col>
</aui:row>
