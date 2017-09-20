/**
 * 
 */

package com.dpabpm.login.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import com.dpabpm.account.service.AccountLocalServiceUtil;
import com.liferay.portal.kernel.captcha.CaptchaConfigurationException;
import com.liferay.portal.kernel.captcha.CaptchaException;
import com.liferay.portal.kernel.captcha.CaptchaTextException;
import com.liferay.portal.kernel.captcha.CaptchaUtil;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.exception.RequiredReminderQueryException;
import com.liferay.portal.kernel.exception.SendPasswordException;
import com.liferay.portal.kernel.exception.UserActiveException;
import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.exception.UserLockoutException;
import com.liferay.portal.kernel.exception.UserReminderQueryException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.util.PropsValues;

/**
 * @author phucnv
 * @date Sep 16, 2017
 */
@Component(property = {
	"javax.portlet.name=com_dpabpm_login_portelt_LoginPortlet",
	"mvc.command.name=/login/forgot_password"
}, service = MVCActionCommand.class)
public class ForgotPasswordMVCActionCommand extends BaseMVCActionCommand {

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

		try {
			checkCaptcha(actionRequest);

			sendResetPasswordURL(actionRequest, actionResponse);
		}
		catch (Exception e) {
			if (e instanceof CaptchaConfigurationException ||
				e instanceof CaptchaTextException ||
				e instanceof UserEmailAddressException) {

				SessionErrors.add(actionRequest, e.getClass());
			}
			else if (e instanceof NoSuchUserException ||
				e instanceof RequiredReminderQueryException ||
				e instanceof SendPasswordException ||
				e instanceof UserActiveException ||
				e instanceof UserLockoutException ||
				e instanceof UserReminderQueryException) {

				if (PropsValues.LOGIN_SECURE_FORGOT_PASSWORD) {
					sendRedirect(actionRequest, actionResponse, null);
				}
				else {
					SessionErrors.add(actionRequest, e.getClass(), e);
				}
			}
			else {
				PortalUtil.sendError(e, actionRequest, actionResponse);
			}
		}
	}

	/**
	 * @param actionRequest
	 * @throws CaptchaException
	 */
	protected void checkCaptcha(ActionRequest actionRequest)
		throws CaptchaException {

		if (PropsValues.CAPTCHA_CHECK_PORTAL_SEND_PASSWORD) {
			CaptchaUtil.check(actionRequest);
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws Exception
	 */
	protected void sendResetPasswordURL(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextFactory.getInstance(actionRequest);

		String email = ParamUtil.getString(actionRequest, "emailAddress");

		AccountLocalServiceUtil._sendResetPasswordURL(email, serviceContext);

		HttpServletRequest request =
			PortalUtil.getHttpServletRequest(actionRequest);

		SessionMessages.add(request, "passwordSent");

		sendRedirect(actionRequest, actionResponse, null);
	}

}
