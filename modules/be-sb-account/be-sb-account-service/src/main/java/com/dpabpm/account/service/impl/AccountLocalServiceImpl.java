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
import java.util.List;
import java.util.Locale;

import com.dpabpm.account.constants.AccountConstants;
import com.dpabpm.account.exception.NoSuchAccountException;
import com.dpabpm.account.model.Account;
import com.dpabpm.account.service.base.AccountLocalServiceBaseImpl;
import com.dpabpm.util.SendEmailMessageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.Ticket;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

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
	public Account updatePassword(
		String ticketKey, String password1, String password2)
		throws NoSuchAccountException, PortalException {

		Ticket ticket = ticketLocalService.getTicket(ticketKey);

		Account account = accountPersistence.findByEmail(ticket.getExtraInfo());

		userLocalService.updatePassword(
			account.getMappingUserId(), password1, password2, false);

		ticket.setExpirationDate(new Date(0));
		ticketLocalService.updateTicket(ticket);

		return account;
	}

	/**
	 * @param email
	 * @return
	 * @throws PortalException
	 */
	@Override
	public Account findByEmail(String email)
		throws PortalException {

		return accountPersistence.findByEmail(email);
	}

	/**
	 * @param mappingUserId
	 * @return
	 * @throws NoSuchAccountException
	 */
	public Account findByMappingUserId(long mappingUserId)
		throws NoSuchAccountException {

		return accountPersistence.findByMappingUserId(mappingUserId);
	}

	/**
	 * @param accountId
	 * @return
	 * @throws PortalException
	 */
	@Override
	public Account verifyMail(long accountId)
		throws PortalException {

		Account account = accountPersistence.findByPrimaryKey(accountId);

		account.setStatus(WorkflowConstants.STATUS_APPROVED);

		accountPersistence.update(account);

		User user =
			userPersistence.findByPrimaryKey(account.getMappingUserId());

		user.setStatus(WorkflowConstants.STATUS_APPROVED);

		userPersistence.update(user);

		return account;
	}

	/**
	 * @param groupId
	 * @param companyId
	 * @param userId
	 * @param userName
	 * @param lastName
	 * @param firstName
	 * @param fullName
	 * @param gender
	 * @param birthdate
	 * @param address
	 * @param telNo
	 * @param email
	 * @param password1
	 * @param password2
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 */
	@Override
	public Account createAccount(
		String lastName, String firstName, String fullName, int gender,
		Date birthdate, String address, String telNo, String email,
		String password1, String password2, ServiceContext serviceContext)
		throws PortalException {

		// create account
		long id = counterLocalService.increment(Account.class.getName());

		Account account = accountPersistence.create(id);

		User user = userLocalService.fetchUser(serviceContext.getUserId());

		Date now = new Date();

		account.setUuid(PortalUUIDUtil.generate());

		account.setGroupId(serviceContext.getScopeGroupId());
		account.setCompanyId(serviceContext.getCompanyId());
		account.setUserId(user.getUserId());
		account.setUserName(user.getFullName());
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
		account.setStatus(WorkflowConstants.STATUS_PENDING);

		// create mapping user
		User mappingUser = _addUserWithWorkflow(
			serviceContext.getCompanyId(), user.getUserId(), user.getFullName(),
			lastName, firstName, gender, birthdate, email, account.getStatus(),
			password1, password2, serviceContext);

		account.setMappingUserId(mappingUser.getUserId());;

		account = accountPersistence.update(account);

		// send mail confirmation
		_sendConfirmationMail(account, serviceContext);

		return account;
	}

	/**
	 * @param account
	 * @param overdueTime
	 * @param timeUnit
	 * @param serviceContext
	 */
	private Ticket _addTicket(
		Account account, int overdueTime, int timeUnit, int ticketType,
		ServiceContext serviceContext) {

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(timeUnit, overdueTime);

		return ticketLocalService.addTicket(
			account.getCompanyId(), account.getClass().getName(),
			account.getId(), ticketType, account.getEmail(), c.getTime(),
			serviceContext);

	}

	/**
	 * @param account
	 * @param ticket
	 * @throws PortalException
	 */
	@Override
	public void _sendConfirmationMail(
		Account account, ServiceContext serviceContext)
		throws PortalException {

		// create ticket
		Ticket ticket = _addTicket(
			account, 24, Calendar.HOUR, AccountConstants.ACTION_VERIFY_EMAIL,
			serviceContext);

		String templateFileURL =
			SendEmailMessageUtil.PATH_ACCOUNT_CREATED_NOTIFICATION_TEMP;

		String verifyEmailURL = _generateVerifyEmailURL(account, ticket);

		String[] replaceParameters = {
			"[$TO_NAME$]", "[$VERIFY_EMAIL_URL$]"
		};
		String[] replaceVariables = {
			account.getFullName(), verifyEmailURL
		};

		String mailBody = SendEmailMessageUtil.getEmailBodyFromTemplateFile(
			templateFileURL, replaceParameters, replaceVariables);

		SendEmailMessageUtil.send(
			SendEmailMessageUtil.SENDER_EMAIL_ADDRESS, account.getEmail(),
			SendEmailMessageUtil.MAIL_SUBJECT_CONFIRMATION_EMAIL, mailBody,
			true);

	}

	/**
	 * @param email
	 * @param serviceContext
	 * @throws SystemException
	 * @throws PortalException
	 */
	@Override
	public void _sendResetPasswordURL(
		String email, ServiceContext serviceContext)
		throws SystemException, PortalException {

		Account account = accountPersistence.findByEmail(email);

		// create ticket
		Ticket ticket = _addTicket(
			account, 24, Calendar.HOUR, AccountConstants.ACTION_RESET_PASSWORD,
			serviceContext);

		String templateFileURL = SendEmailMessageUtil.PATH_RESET_PASSWORD_TEMP;

		String resetPasswordURL = _gennerateResetPasswordURL(account, ticket);

		String[] replaceParameters = {
			"[$TO_NAME$]", "[$RESET_PASSWORD_URL$]"
		};
		String[] replaceVariables = {
			account.getFullName(), resetPasswordURL
		};

		String mailBody = SendEmailMessageUtil.getEmailBodyFromTemplateFile(
			templateFileURL, replaceParameters, replaceVariables);

		SendEmailMessageUtil.send(
			SendEmailMessageUtil.SENDER_EMAIL_ADDRESS, account.getEmail(),
			SendEmailMessageUtil.MAIL_SUBJECT_RESET_PASSWORD, mailBody, true);
	}

	/**
	 * @param account
	 * @param ticket
	 * @return
	 * @throws PortalException
	 */
	private String _gennerateResetPasswordURL(Account account, Ticket ticket)
		throws PortalException {

		Company company =
			companyLocalService.getCompany(account.getCompanyId());

		StringBuilder portalURL = new StringBuilder(
			PortalUtil.getPortalURL(
				company.getVirtualHostname(),
				PortalUtil.getPortalServerPort(false), false));

		portalURL.append(
			"/o/account?action=" + ticket.getType() + "&ticketKey=" +
				ticket.getKey());

		return portalURL.toString();
	}

	/**
	 * @param account
	 * @param ticket
	 * @return
	 * @throws PortalException
	 */
	private String _generateVerifyEmailURL(Account account, Ticket ticket)
		throws PortalException {

		Company company =
			companyLocalService.getCompany(account.getCompanyId());

		StringBuilder portalURL = new StringBuilder(
			PortalUtil.getPortalURL(
				company.getVirtualHostname(),
				PortalUtil.getPortalServerPort(false), false));

		portalURL.append(
			"/o/account?" + "action=" + ticket.getType() + "&ticketKey=" +
				ticket.getKey());

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
		String firstName, int gender, Date birthdate, String email, int status,
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

		// get administrator user
		Role adminRole = roleLocalService.getRole(
			serviceContext.getCompanyId(), "Administrator");
		long creatorUserId = 0;
		List<User> adminUsers =
			userLocalService.getRoleUsers(adminRole.getRoleId());

		if (adminUsers.size() != 0) {
			creatorUserId = adminUsers.get(0).getUserId();
		}

		// default role
		Role roleDefault = roleLocalService.fetchRole(
			serviceContext.getCompanyId(), "DPABPM_DEFAULT_ROLE");

		if (Validator.isNull(roleDefault)) {
			roleDefault = _addRegularRole(
				"DPABPM_DEFAULT_ROLE", adminUsers.get(0), serviceContext);
		}

		long[] roleIds = new long[] {
			roleDefault.getRoleId()
		};

		user = userLocalService.addUserWithWorkflow(
			creatorUserId, companyId, false, password1, password2, true,
			StringPool.BLANK, email, 0, StringPool.BLANK, locale, firstName,
			StringPool.BLANK, lastName, 0, 0, gender > 0, birthdayMonth,
			birthdayDay, birthdayYear, StringPool.BLANK, null, null, roleIds,
			null, false, serviceContext);

		user.setStatus(status);
		user.setReminderQueryQuestion(StringPool.SLASH);
		user.setReminderQueryAnswer(StringPool.SLASH);

		return userPersistence.update(user);
	}

	/**
	 * @param roleName
	 * @param admin
	 * @param serviceContext
	 * @return
	 */
	private Role _addRegularRole(
		String roleName, User admin, ServiceContext serviceContext) {

		long roleId = counterLocalService.increment(Role.class.getName());

		Role role = rolePersistence.create(roleId);

		Date now = new Date();

		role.setCompanyId(serviceContext.getCompanyId());
		role.setUserId(admin.getUserId());
		role.setUserName(admin.getFullName());
		role.setCreateDate(now);
		role.setModifiedDate(now);
		role.setClassNameId(classNameLocalService.getClassNameId(Role.class));
		role.setClassPK(roleId);
		role.setName(roleName);
		role.setType(RoleConstants.TYPE_REGULAR);

		return rolePersistence.update(role);
	}

	private Log _log = LogFactoryUtil.getLog(this.getClass());

}
