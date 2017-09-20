/**
 * 
 */

package com.dpabpm.login.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

import com.dpabpm.account.constants.AccountConstants;
import com.dpabpm.account.search.AccountDisplayTerms;
import com.dpabpm.account.service.AccountLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

/**
 * @author phucnv
 * @date Sep 21, 2017
 */
@Component(property = {
	"javax.portlet.name=com_dpabpm_login_portelt_LoginPortlet",
	"javax.portlet.name=com_dpabpm_account_portelt_AccountPortlet",
	"mvc.command.name=/login/reset_password"
}, service = MVCActionCommand.class)
public class ResetPasswordMVCActionCommand extends BaseMVCActionCommand {

	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand#
	 * doProcessAction(javax.portlet.ActionRequest,
	 * javax.portlet.ActionResponse)
	 */
	@Override
	protected void doProcessAction(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		String ticketKey = ParamUtil.getString(actionRequest, "ticketKey");

		AccountDisplayTerms accountTerms = new AccountDisplayTerms(
			PortalUtil.getHttpServletRequest(actionRequest));

		try {
			AccountLocalServiceUtil.updatePassword(
				ticketKey, accountTerms.getPassword1(),
				accountTerms.getPassword2());
		}
		catch (PortalException e) {
			_log.info(e);
		}

		actionResponse.setRenderParameter(
			"postAction",
			String.valueOf(AccountConstants.ACTION_RESET_PASSWORD));

		actionResponse.setRenderParameter(
			"mvcPath", "/html/account/reset_password.jsp");

	}

	private Log _log = LogFactoryUtil.getLog(this.getClass());
}
