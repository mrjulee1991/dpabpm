/**
 * 
 */

package com.dpabpm.account.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

import com.dpabpm.account.business.AccountBusiness;
import com.dpabpm.account.search.AccountDisplayTerms;
import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.PortalUtil;

/**
 * @author phucnv
 * @date Sep 2, 2017
 */
@Component(immediate = true, property = {
	"com.liferay.portlet.display-category=category.dpabpm.account",
	"com.liferay.portlet.instanceable=true",
	"javax.portlet.display-name=Account Registration",
	"javax.portlet.init-param.template-path=/",
	"javax.portlet.init-param.view-template=/html/register/register.jsp",
	"javax.portlet.resource-bundle=content.Language_vi",
	"javax.portlet.security-role-ref=power-user,user"
}, service = Portlet.class)
public class AccountRegistrationPortlet extends MVCPortlet {

	/**
	 * @param request
	 * @param response
	 */
	public void createAccount(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		try {
			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(actionRequest);

			AccountDisplayTerms accountTerms = new AccountDisplayTerms(
				PortalUtil.getHttpServletRequest(actionRequest));

			AccountBusiness.createAccount(
				accountTerms.getGroupId(), accountTerms.getCompanyId(),
				accountTerms.getUserId(), accountTerms.getUserName(),
				accountTerms.getLastName(), accountTerms.getFirstName(),
				accountTerms.getFullName(), accountTerms.getGender(),
				accountTerms.getBirthdate(), accountTerms.getAddress(),
				accountTerms.getTelNo(), accountTerms.getEmail(),
				accountTerms.getPassword1(), accountTerms.getPassword2(),
				serviceContext);

			hideDefaultSuccessMessage(actionRequest);
			SessionMessages.add(
				actionRequest,
				"register-successfull.-url-verify-had-been-send-to-x");

		}
		catch (Exception e) {
			_log.error(e);

			hideDefaultErrorMessage(actionRequest);

			if (e instanceof UserEmailAddressException.MustNotBeDuplicate) {
				SessionErrors.add(
					actionRequest,
					UserEmailAddressException.MustNotBeDuplicate.class);
			}
		}

		actionResponse.setRenderParameter(
			"mvcPath", "/html/register/register.jsp");
	}

	private Log _log = LogFactoryUtil.getLog(this.getClass());
}
