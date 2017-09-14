/**
 * 
 */

package com.dpabpm.account.email;

import java.util.Date;

import com.dpabpm.account.business.AccountBusiness;
import com.dpabpm.account.model.Account;
import com.dpabpm.account.service.AccountLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Ticket;
import com.liferay.portal.kernel.service.TicketLocalServiceUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

/**
 * @author phucnv
 * @date Sep 8, 2017
 */
public class VerifyEmail {

	/**
	 * @param key
	 * @return
	 * @throws PortalException
	 */
	public static boolean verify(String key)
		throws PortalException {

		boolean isValidEmail = false;

		Ticket ticket = TicketLocalServiceUtil.getTicket(key);

		isValidEmail = ticket.getExpirationDate().after(new Date());

		if (isValidEmail) {
			AccountBusiness.verifyEmail(ticket.getClassPK());
		}
		TicketLocalServiceUtil.deleteTicket(ticket);

		return isValidEmail;
	}

	/**
	 * @param email
	 * @return
	 */
	public static boolean checkEmaiExisted(String email) {

		try {
			AccountLocalServiceUtil.findByEmail(email);
		}
		catch (PortalException e) {
			return false;
		}

		return true;
	}

	/**
	 * @param email
	 * @return
	 * @throws PortalException
	 */
	public static boolean checkEmailVerified(String email)
		throws PortalException {

		Account account = AccountLocalServiceUtil.findByEmail(email);

		if (account.getStatus() == WorkflowConstants.STATUS_PENDING) {
			return false;
		}

		return true;
	}

	private static Log _log = LogFactoryUtil.getLog(VerifyEmail.class);
}
