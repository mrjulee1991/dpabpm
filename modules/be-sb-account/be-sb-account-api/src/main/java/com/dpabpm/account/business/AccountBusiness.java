/**
 * 
 */

package com.dpabpm.account.business;

import java.util.Date;

import com.dpabpm.account.model.Account;
import com.dpabpm.account.service.AccountLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;

/**
 * @author phucnv
 * @date Sep 5, 2017
 */
public class AccountBusiness {

	/**
	 * @param accountId
	 * @return
	 * @throws PortalException
	 */
	public static Account verifyMail(long accountId)
		throws PortalException {

		return AccountLocalServiceUtil.verifyMail(accountId);
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
	 * @param status
	 * @param password1
	 * @param password2
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 */
	public static Account createAccount(
		long groupId, long companyId, long userId, String userName,
		String lastName, String firstName, String fullName, int gender,
		Date birthdate, String address, String telNo, String email, int status,
		String password1, String password2, ServiceContext serviceContext)
		throws PortalException {

		return AccountLocalServiceUtil.createAccount(
			groupId, companyId, userId, userName, lastName, firstName, fullName,
			gender, birthdate, address, telNo, email, status, password1,
			password2, serviceContext);
	}

	private static Log _log = LogFactoryUtil.getLog(AccountBusiness.class);
}
