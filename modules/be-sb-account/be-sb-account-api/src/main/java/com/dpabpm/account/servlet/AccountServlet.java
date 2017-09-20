/**
 * 
 */

package com.dpabpm.account.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import com.dpabpm.account.constants.AccountConstants;
import com.dpabpm.account.util.AccountUtil;
import com.liferay.portal.kernel.exception.NoSuchTicketException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;

/**
 * @author phucnv
 * @date Sep 20, 2017
 */
@Component(immediate = true, property = {
	"osgi.http.whiteboard.context.path=/",
	"osgi.http.whiteboard.servlet.name=com.dpabpm.util.mail.VerifyEmailServlet",
	"osgi.http.whiteboard.servlet.pattern=/account",
}, service = Servlet.class)
public class AccountServlet extends HttpServlet {

	// Servlet URL : http://localhost:8080/o/account?key=xxxxxxxxxx

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(
		HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		String ticketKey =
			GetterUtil.getString(request.getParameter("ticketKey"));

		int action = GetterUtil.getInteger(request.getParameter("action"));

		boolean isValidTicketKey = false;

		try {
			if (action == AccountConstants.ACTION_VERIFY_EMAIL) {

				isValidTicketKey = AccountUtil.verifyEmail(ticketKey);

			}
			else if (action == AccountConstants.ACTION_RESET_PASSWORD) {
				isValidTicketKey =
					AccountUtil.checkTicketKeyResetPassword(ticketKey);
			}

			// goto
			// fe-web-login/src/main/resources/META-INF/resources/html/account/view.jsp
			response.sendRedirect(
				"/web/guest/account?action=" + action + "&isValidTicketKey=" +
					isValidTicketKey + "&ticketKey=" + ticketKey);
		}
		catch (PortalException e) {
			_log.error(e);

			if (e instanceof NoSuchTicketException) {
				response.sendRedirect(
					"/web/guest/account?action=" + action +
						"&isValidTicketKey=" + isValidTicketKey +
						"&ticketKey=" + ticketKey);
			}
			else {
				// redirect to home page
				response.sendRedirect(PortalUtil.getPortalURL(request));
			}

		}

		// super.doGet(request, response);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(
		HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		super.doPost(request, response);
	}

	private Log _log = LogFactoryUtil.getLog(this.getClass());
}
