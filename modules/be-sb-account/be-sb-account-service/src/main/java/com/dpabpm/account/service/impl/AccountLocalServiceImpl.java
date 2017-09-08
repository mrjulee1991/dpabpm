/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.dpabpm.account.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.dpabpm.account.model.Account;
import com.dpabpm.account.service.base.AccountLocalServiceBaseImpl;
import com.dpabpm.util.mail.SendMailMessageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Ticket;
import com.liferay.portal.kernel.model.TicketConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.TicketLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import aQute.bnd.annotation.ProviderType;

/**
 * The implementation of the account local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link com.dpabpm.account.service.AccountLocalService} interface. <p> This is
 * a local service. Methods of this service will not have security checks based
 * on the propagated JAAS credentials because this service can only be accessed
 * from within the same VM. </p>
 *
 * @author phucnv
 * @see AccountLocalServiceBaseImpl
 * @see com.dpabpm.account.service.AccountLocalServiceUtil
 */
@ProviderType
public class AccountLocalServiceImpl extends AccountLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this class directly. Always use
	 * {@link com.dpabpm.account.service.AccountLocalServiceUtil} to access the
	 * account local service.
	 */

	/*
	 * (non-Javadoc)
	 * @see com.dpabpm.account.service.AccountLocalService#verifyMail(long)
	 */
	@Override
	public Account verifyMail(long accountId)
		throws PortalException {

		Account account = accountPersistence.findByPrimaryKey(accountId);
		User user =
			userPersistence.findByPrimaryKey(account.getMappingUserId());

		user.setEmailAddressVerified(true);
		user.setReminderQueryQuestion(StringPool.SLASH);
		user.setReminderQueryAnswer(StringPool.SLASH);

		userPersistence.update(user);

		_log.info("8===============o verified mail: " + account.getEmail());

		return account;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.dpabpm.account.service.AccountLocalService#createAccount(java.lang.
	 * String, long, long, long, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, int, java.util.Date,
	 * java.lang.String, java.lang.String, java.lang.String, int, long,
	 * java.lang.String, java.lang.String,
	 * com.liferay.portal.kernel.service.ServiceContext)
	 */
	@Override
	public Account createAccount(
		long groupId, long companyId, long userId, String userName,
		String lastName, String firstName, String fullName, int gender,
		Date birthdate, String address, String telNo, String email, int status,
		String password1, String password2, ServiceContext serviceContext)
		throws PortalException {

		long id = counterLocalService.increment(Account.class.getName());
		Account account = accountPersistence.create(id);

		Date now = new Date();

		account.setUuid(PortalUUIDUtil.generate());

		account.setGroupId(groupId);
		account.setCompanyId(companyId);
		account.setUserId(userId);
		account.setUserName(userName);
		account.setCreateDate(now);
		account.setModifiedDate(now);

		account.setLastName(lastName);
		account.setFirstName(firstName);
		account.setFullName(fullName);
		account.setGender(gender);
		account.setBirthdate(birthdate);
		account.setAddress(address);
		account.setTelNo(telNo);
		account.setEmail(email);
		account.setStatus(status);

		_log.info("8=================o account id created: " + account.getId());

		// create mapping user
		User mappingUser = _addUserWithWorkflow(
			companyId, userId, userName, lastName, firstName, gender, birthdate,
			email, password1, password2, serviceContext);

		account.setMappingUserId(mappingUser.getUserId());;

		account = accountPersistence.update(account);

		_log.info(
			"8=================o mapping user id created: " +
				mappingUser.getUserId() + "/" +
				mappingUser.getPasswordUnencrypted());

		// add ticket for account
		// TODO configure overdue time and time unit
		Ticket ticket = _addTicket(account, 5, Calendar.MINUTE, serviceContext);

		// send confirmation mail
		_sendConfirmationMail(account, ticket);

		return account;
	}

	/**
	 * @param account
	 * @param overdueTime
	 * @param timeUnit
	 * @param serviceContext
	 */
	private Ticket _addTicket(
		Account account, int overdueTime, int timeUnit,
		ServiceContext serviceContext) {

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(timeUnit, overdueTime);

		Date expirationDate = c.getTime();

		_log.info("8============o add ticket for account: " + account.getId());

		return TicketLocalServiceUtil.addDistinctTicket(
			account.getCompanyId(), account.getClass().getName(),
			account.getId(), TicketConstants.TYPE_PASSWORD, StringPool.BLANK,
			expirationDate, serviceContext);

	}

	/**
	 * @param account
	 * @param mappingUser
	 * @throws SystemException
	 * @throws PortalException
	 */
	private void _sendConfirmationMail(Account account, Ticket ticket)
		throws PortalException {

		String templateFileURL =
			SendMailMessageUtil.PATH_ACCOUNT_CREATED_NOTIFICATION;

		String verificationURL = _generateVerificationURL(account, ticket);

		String[] replaceParameters = {
			"[$TO_NAME$]", "[$VERIFICATION_URL$]"
		};
		String[] replaceVariables = {
			account.getFullName(), verificationURL
		};

		String mailBody = SendMailMessageUtil.getEmailBodyFromTemplateFile(
			templateFileURL, replaceParameters, replaceVariables);

		SendMailMessageUtil.send(
			SendMailMessageUtil.SENDER_EMAIL_ADDRESS, account.getEmail(),
			"Confirm registration", mailBody, true);

		_log.info(
			"8==================o sended confirmation mail to: " +
				account.getEmail());

	}

	/**
	 * @param account
	 * @param ticket
	 * @return
	 * @throws PortalException
	 */
	private String _generateVerificationURL(Account account, Ticket ticket)
		throws PortalException {

		Company company =
			CompanyLocalServiceUtil.getCompany(account.getCompanyId());

		StringBuilder portalURL = new StringBuilder(
			PortalUtil.getPortalURL(
				company.getVirtualHostname(),
				PortalUtil.getPortalServerPort(false), false));

		portalURL.append("/o/verify-mail?key=" + ticket.getKey());

		return portalURL.toString();
	}

	/**
	 * @param companyId
	 * @param userId
	 * @param userName
	 * @param lastName
	 * @param firstName
	 * @param gender
	 * @param birthdate
	 * @param email
	 * @param password1
	 * @param password2
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 */
	private User _addUserWithWorkflow(
		long companyId, long userId, String userName, String lastName,
		String firstName, int gender, Date birthdate, String email,
		String password1, String password2, ServiceContext serviceContext)
		throws PortalException {

		User user = userPersistence.create(
			counterLocalService.increment(User.class.getName()));

		Calendar bdc = Calendar.getInstance();
		bdc.setTime(birthdate);

		int birthdayDay = bdc.get(Calendar.DAY_OF_MONTH);
		int birthdayMonth = bdc.get(Calendar.MONTH);
		int birthdayYear = bdc.get(Calendar.YEAR);

		Locale locale = serviceContext.getLocale();

		user = userLocalService.addUserWithWorkflow(
			userId, companyId, false, password1, password2, true,
			StringPool.BLANK, email, 0, StringPool.BLANK, locale, firstName,
			StringPool.BLANK, lastName, 0, 0, gender > 0, birthdayMonth,
			birthdayDay, birthdayYear, StringPool.BLANK, null, null, null, null,
			false, serviceContext);

		return user;
	}

	private Log _log = LogFactoryUtil.getLog(this.getClass());

}
