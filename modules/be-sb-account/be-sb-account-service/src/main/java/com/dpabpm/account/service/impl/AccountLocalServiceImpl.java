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

import java.util.Date;
import java.util.Locale;

import com.dpabpm.account.model.Account;
import com.dpabpm.account.service.base.AccountLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
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
	public Account updateAccount(
		String uuid, long groupId, long companyId, long userId, String userName,
		String lastName, String firstName, String fullName, int gender,
		Date birthdate, String address, String telNo, String email, int status,
		long mappingUserId, ServiceContext serviceContext)
		throws PortalException {

		Date now = new Date();

		Account account = null;
		User mappingUser = null;

		if (Validator.isNotNull(uuid)) {
			account = accountPersistence.findByUuid(uuid);
			mappingUser = userPersistence.findByPrimaryKey(mappingUserId);
		}
		else {
			long id = counterLocalService.increment(Account.class.getName());
			account = accountPersistence.create(id);
			account.setUuid(PortalUUIDUtil.generate());
			account.setCreateDate(now);
		}
		account.setModifiedDate(now);

		account.setGroupId(groupId);
		account.setCompanyId(companyId);
		account.setUserId(userId);
		account.setUserName(userName);
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

		if (Validator.isNotNull(uuid)) {
			// TODO update mappingUser
		}
		else {
			mappingUser = _addUserWithWorkflow(
				uuid, groupId, companyId, userId, userName, lastName, firstName,
				fullName, gender, birthdate, address, telNo, email, status,
				mappingUserId, serviceContext);

			_log.info(
				"8=================o mapping user id created: " +
					mappingUser.getUserId());
			_log.info(
				"8=================o " + mappingUser.getPasswordUnencrypted());

			// TODO turn off portal sender and send mail
			_sendConfirmationMail();
		}

		return account;
	}

	private void _sendConfirmationMail() {

	}

	private User _addUserWithWorkflow(
		String uuid, long groupId, long companyId, long userId, String userName,
		String lastName, String firstName, String fullName, int gender,
		Date birthdate, String address, String telNo, String email, int status,
		long mappingUserId, ServiceContext serviceContext)
		throws PortalException {

		boolean autoScreenName = true;
		boolean autoPassword = true;
		boolean male = gender > 0;
		boolean sendEmail = true;

		String password1 = StringPool.BLANK;
		String password2 = StringPool.BLANK;
		String openId = StringPool.BLANK;
		String screenName = StringPool.BLANK;
		String middleName = StringPool.BLANK;
		String jobTitle = StringPool.BLANK;

		long facebookId = 0;
		long prefixId = 0;
		long suffixId = 0;
		long creatorUserId = userId;

		int birthdayDay = 19;
		int birthdayMonth = 9;
		int birthdayYear = 1991;

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
