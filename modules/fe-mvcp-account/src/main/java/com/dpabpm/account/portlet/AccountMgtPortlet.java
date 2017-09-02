/**
 * 
 */

package com.dpabpm.account.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

import com.dpabpm.account.search.AccountDisplayTerms;
import com.dpabpm.account.service.AccountLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;

/**
 * @author phucnv
 * @date Sep 2, 2017
 */
@Component(immediate = true, property = {
	"com.liferay.portlet.display-category=category.sample",
	"com.liferay.portlet.instanceable=true",
	"javax.portlet.display-name=Account mgt Portlet",
	"javax.portlet.init-param.template-path=/",
	"javax.portlet.init-param.view-template=/html/view.jsp",
	"javax.portlet.resource-bundle=content.Language",
	"javax.portlet.security-role-ref=power-user,user"
}, service = Portlet.class)
public class AccountMgtPortlet extends MVCPortlet {

	/**
	 * @author phucnv
	 * @date Sep 3, 2017
	 */
	public void createAccount(ActionRequest request, ActionResponse response) {

		try {
			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(request);

			AccountDisplayTerms accountTerms = new AccountDisplayTerms(request);

			AccountLocalServiceUtil.updateAccount(
				accountTerms.getUuid(), accountTerms.getGroupId(),
				accountTerms.getCompanyId(), accountTerms.getUserId(),
				accountTerms.getUserName(), accountTerms.getLastName(),
				accountTerms.getFirstName(), accountTerms.getFullName(),
				accountTerms.getGender(), accountTerms.getBirthdate(),
				accountTerms.getAddress(), accountTerms.getTelNo(),
				accountTerms.getEmail(), accountTerms.getStatus(),
				accountTerms.getMappingUserId(), serviceContext);
		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			response.setRenderParameter("mvcPath", "/html/view.jsp");
		}
	}

	private Log _log = LogFactoryUtil.getLog(this.getClass());
}
