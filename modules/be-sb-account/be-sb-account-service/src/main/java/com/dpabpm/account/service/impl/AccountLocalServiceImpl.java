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

import com.dpabpm.account.mail.SendEmailMessageUtil;
import com.dpabpm.account.model.Account;
import com.dpabpm.account.service.base.AccountLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.TicketConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.TicketLocalServiceUtil;
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

	@Override
	public Account createAccount(
		String uuid, long groupId, long companyId, long userId, String userName,
		String lastName, String firstName, String fullName, int gender,
		Date birthdate, String address, String telNo, String email, int status,
		long mappingUserId, String password1, String password2,
		ServiceContext serviceContext)
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
		account.setMappingUserId(mappingUserId);;

		account = accountPersistence.update(account);

		_log.info("8=================o account id created: " + account.getId());

		// create mapping user
		User mappingUser = _addUserWithWorkflow(
			uuid, groupId, companyId, userId, userName, lastName, firstName,
			fullName, gender, birthdate, address, telNo, email, status,
			mappingUserId, password1, password2, serviceContext);

		_log.info(
			"8=================o mapping user id created: " +
				mappingUser.getUserId());

		// send confirmation mail
		_sendConfirmationMail(account, mappingUser);

		// add ticket for account
		// TODO configure overdue time and time unit
		_addTicket(account, 5, Calendar.MINUTE, serviceContext);

		return account;
	}

	private void _addTicket(
		Account account, int overdueTime, int timeUnit,
		ServiceContext serviceContext) {

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(timeUnit, overdueTime);

		Date expirationDate = c.getTime();

		TicketLocalServiceUtil.addDistinctTicket(
			account.getCompanyId(), account.getClass().getName(),
			account.getMappingUserId(), TicketConstants.TYPE_PASSWORD,
			StringPool.BLANK, expirationDate, serviceContext);

		_log.info("8============o add ticket for account: " + account.getId());
	}

	private void _sendConfirmationMail(Account account, User mappingUser)
		throws SystemException, PortalException {

		String templateFileURL =
			SendEmailMessageUtil.PATH_ACCOUNT_CREATED_NOTIFICATION;

		String[] replaceParameters = {
			"[$TO_NAME$]", "[$USER_PASSWORD$]"
		};
		String[] replaceVariables = {
			account.getFullName(), mappingUser.getPasswordUnencrypted()
		};

		String mailBody = SendEmailMessageUtil.getEmailBodyFromTemplateFile(
			templateFileURL, replaceParameters, replaceVariables);

		SendEmailMessageUtil.send(
			SendEmailMessageUtil.SENDER_EMAIL_ADDRESS, account.getEmail(),
			"Confirm registration", mailBody, true);

		_log.info(
			"8==================o sended confirmation mail to account id: " +
				account.getId());

	}

	private User _addUserWithWorkflow(
		String uuid, long groupId, long companyId, long userId, String userName,
		String lastName, String firstName, String fullName, int gender,
		Date birthdate, String address, String telNo, String email, int status,
		long mappingUserId, String password1, String password2,
		ServiceContext serviceContext)
		throws PortalException {

		boolean autoScreenName = true;
		boolean autoPassword = false;
		boolean sendEmail = false;
		boolean male = gender > 0;

		String openId = StringPool.BLANK;
		String screenName = StringPool.BLANK;
		String middleName = StringPool.BLANK;
		String jobTitle = StringPool.BLANK;

		long facebookId = 0;
		long prefixId = 0;
		long suffixId = 0;
		long creatorUserId = userId;

		Calendar bdc = Calendar.getInstance();
		bdc.setTime(birthdate);

		int birthdayDay = bdc.get(Calendar.DAY_OF_MONTH);
		int birthdayMonth = bdc.get(Calendar.MONTH);
		int birthdayYear = bdc.get(Calendar.YEAR);

		long[] roleIds = null;
		long[] userGroupIds = null;
		long[] organizationIds = null;
		long[] groupIds = null;

		Locale locale = serviceContext.getLocale();

		return userLocalService.addUserWithWorkflow(
			creatorUserId, companyId, autoPassword, password1, password2,
			autoScreenName, screenName, email, facebookId, openId, locale,
			firstName, middleName, lastName, prefixId, suffixId, male,
			birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds,
			organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);
	}

	private Log _log = LogFactoryUtil.getLog(this.getClass());

}
