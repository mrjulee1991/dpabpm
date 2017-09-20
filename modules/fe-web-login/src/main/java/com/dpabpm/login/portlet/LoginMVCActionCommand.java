/**
 * 
 */

package com.dpabpm.login.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import com.dpabpm.account.email.AccountEmailUtil;
import com.liferay.portal.kernel.exception.NoSuchEmailAddressException;
import com.liferay.portal.kernel.exception.UserActiveException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.session.AuthenticatedSessionManagerUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

/**
 * @author phucnv
 * @date Sep 10, 2017
 */
@Component(property = {
	"javax.portlet.name=com_dpabpm_login_portelt_LoginPortlet",
	"mvc.command.name=/login/login"
}, service = MVCActionCommand.class)
public class LoginMVCActionCommand extends BaseMVCActionCommand {

	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand#
	 * doProcessAction(javax.portlet.ActionRequest,
	 * javax.portlet.ActionResponse)
	 */
	@Override
	protected void doProcessAction(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		HttpServletRequest request = PortalUtil.getOriginalServletRequest(
			PortalUtil.getHttpServletRequest(actionRequest));

		HttpServletResponse response =
			PortalUtil.getHttpServletResponse(actionResponse);

		String login = ParamUtil.getString(actionRequest, "login");
		String password = actionRequest.getParameter("password");
		boolean rememberMe = ParamUtil.getBoolean(actionRequest, "rememberMe");
		String authType = CompanyConstants.AUTH_TYPE_EA;

		hideDefaultErrorMessage(actionRequest);
		hideDefaultSuccessMessage(actionRequest);

		if (!AccountEmailUtil.checkEmaiExisted(login)) {
			SessionErrors.add(actionRequest, NoSuchEmailAddressException.class);
		}
		else if (!AccountEmailUtil.checkEmailVerified(login)) {
			SessionErrors.add(actionRequest, UserActiveException.class);
		}
		else {
			AuthenticatedSessionManagerUtil.login(
				request, response, login, password, rememberMe, authType);
		}

		actionResponse.sendRedirect(themeDisplay.getPathMain());

	}

	private Log _log = LogFactoryUtil.getLog(this.getClass());
}
