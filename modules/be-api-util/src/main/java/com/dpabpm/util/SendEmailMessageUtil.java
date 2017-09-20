/**
 * 
 */

package com.dpabpm.util;

import java.util.Iterator;
import java.util.Locale;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.SubscriptionSender;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.ContentUtil;

/**
 * @author phucnv
 * @date Sep 3, 2017
 */
public class SendEmailMessageUtil {

	public static final String SENDER_EMAIL_ADDRESS = "nvp191@gmail.com";

	public static final String MAIL_SUBJECT_CONFIRMATION_EMAIL =
		LanguageUtil.get(
			Locale.getDefault(), "mail-subject-confirmation-email");

	public static final String PATH_ACCOUNT_CREATED_NOTIFICATION_TEMP =
		"/com/dpabpm/util/mail/templates/create_account.temp";

	public static final String MAIL_SUBJECT_RESET_PASSWORD =
		LanguageUtil.get(Locale.getDefault(), "mail-subject-reset-password");

	public static final String PATH_RESET_PASSWORD_TEMP =
		"/com/dpabpm/util/mail/templates/reset_password.temp";

	/**
	 * @param templateFileURL
	 * @param replaceParameters
	 * @param replaceVariables
	 * @return
	 */
	public static String getEmailBodyFromTemplateFile(
		String templateFileURL, String[] replaceParameters,
		String[] replaceVariables) {

		String body = ContentUtil.get(templateFileURL, true);
		if (Validator.isNotNull(body)) {
			if (replaceParameters.length <= replaceVariables.length) {
				body = replaceString(body, replaceParameters, replaceVariables);
			}
			else {
				for (int i = 0; i < replaceVariables.length; i++) {
					body = StringUtil.replace(
						body, replaceParameters[i], replaceVariables[i]);
				}
				for (int i =
					replaceVariables.length; i < replaceParameters.length; i++) {
					body = StringUtil.replace(body, replaceParameters[i], "");
				}
			}
		}
		return body;
	}

	/**
	 * @param s
	 * @param replaceParameters
	 * @param replaceVariables
	 * @return
	 */
	private static String replaceString(
		String s, String[] replaceParameters, String[] replaceVariables) {

		for (int i = 0; i < replaceParameters.length; i++) {
			s = StringUtil.replace(
				s, replaceParameters[i], replaceVariables[i]);
		}
		return s;
	}

	/**
	 * @param senderEmailAddress
	 * @param receiverEmailAddress
	 * @param subject
	 * @param emailBody
	 * @param isHTMLFormat
	 * @throws SystemException
	 * @throws PortalException
	 */
	public static void send(
		String senderEmailAddress, String receiverEmailAddress, String subject,
		String emailBody, boolean isHTMLFormat)
		throws SystemException, PortalException {

		User user = getAdmin();
		if (Validator.isNotNull(user)) {
			SubscriptionSender subscriptionSender = new SubscriptionSender();
			subscriptionSender.setBody(emailBody);
			subscriptionSender.setCompanyId(user.getCompanyId());
			subscriptionSender.setCurrentUserId(user.getUserId());
			subscriptionSender.setHtmlFormat(isHTMLFormat);
			subscriptionSender.setFrom(senderEmailAddress, "Admin");
			subscriptionSender.setSubject(subject);
			subscriptionSender.addRuntimeSubscribers(
				receiverEmailAddress, "You");
			subscriptionSender.setMailId("User", new Object[] {
				Long.valueOf(user.getUserId())
			});
			subscriptionSender.flushNotificationsAsync();
		}
		else if (_log.isInfoEnabled()) {
			_log.info(
				"SendMailMessageUtils : =========== Default Admin user is Nulllll ");
		}
	}

	/**
	 * @param themeDisplay
	 * @param formAdress
	 * @param toAdress
	 * @param subject
	 * @param body
	 * @param isHTMLFormat
	 */
	public static void send(
		ThemeDisplay themeDisplay, String formAdress, String toAdress,
		String subject, String body, boolean isHTMLFormat) {

		User user = themeDisplay.getUser();

		SubscriptionSender subscriptionSender = new SubscriptionSender();

		subscriptionSender.setBody(body);
		subscriptionSender.setCompanyId(user.getCompanyId());
		subscriptionSender.setCurrentUserId(user.getUserId());
		subscriptionSender.setHtmlFormat(isHTMLFormat);
		subscriptionSender.setFrom(formAdress, "Admin");
		subscriptionSender.setSubject(subject);
		subscriptionSender.addRuntimeSubscribers(toAdress, "You");
		subscriptionSender.setMailId("User", new Object[] {
			Long.valueOf(user.getUserId())
		});

		subscriptionSender.flushNotificationsAsync();
	}

	/**
	 * @param companyId
	 * @param roleStrId
	 * @return
	 */
	private static Role getRoleById(long companyId, String roleStrId) {

		try {
			return RoleLocalServiceUtil.getRole(companyId, roleStrId);
		}
		catch (Exception e) {
			if (_log.isErrorEnabled()) {
				_log.error("SendMailMessageUtils::getRoleById Exception", e);
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	private static User getAdmin() {

		long companyId = PortalUtil.getDefaultCompanyId();
		Role role = null;
		try {
			role = getRoleById(companyId, "Administrator");
			Iterator<User> localIterator =
				UserLocalServiceUtil.getRoleUsers(role.getRoleId()).iterator();
			if (localIterator.hasNext()) {
				return localIterator.next();
			}
		}
		catch (Exception e) {
			if (_log.isErrorEnabled()) {
				_log.error("SendMailMessageUtils::getAdmin Exception", e);
			}
		}
		return null;
	}

	private static Log _log = LogFactoryUtil.getLog(SendEmailMessageUtil.class);
}
