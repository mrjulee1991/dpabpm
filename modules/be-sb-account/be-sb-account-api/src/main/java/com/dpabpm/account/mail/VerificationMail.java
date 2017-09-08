/**
 * 
 */

package com.dpabpm.account.mail;

import java.util.Date;

import com.dpabpm.account.business.AccountBusiness;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Ticket;
import com.liferay.portal.kernel.service.TicketLocalServiceUtil;

/**
 * @author phucnv
 * @date Sep 8, 2017
 */
public class VerificationMail {

	/**
	 * @param key
	 * @return
	 * @throws PortalException
	 */
	public static boolean verify(String key)
		throws PortalException {

		boolean isValidMail = false;
		Ticket ticket = null;

		ticket = TicketLocalServiceUtil.getTicket(key);

		isValidMail = ticket.getExpirationDate().after(new Date());

		if (isValidMail) {
			// TicketLocalServiceUtil.deleteTicket(ticket);
			AccountBusiness.verifyMail(ticket.getClassPK());
		}

		return isValidMail;
	}

	private static Log _log = LogFactoryUtil.getLog(VerificationMail.class);
}
