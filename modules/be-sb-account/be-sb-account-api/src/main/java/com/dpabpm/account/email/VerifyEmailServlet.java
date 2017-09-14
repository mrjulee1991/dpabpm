/**
 * 
 */

package com.dpabpm.account.email;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * @author phucnv
 * @date Sep 8, 2017
 */
@Component(immediate = true, property = {
	"osgi.http.whiteboard.context.path=/",
	"osgi.http.whiteboard.servlet.name=com.dpabpm.util.mail.VerifyEmailServlet",
	"osgi.http.whiteboard.servlet.pattern=/verify-email",
}, service = Servlet.class)
public class VerifyEmailServlet extends HttpServlet {

	// Servlet URL : http://localhost:8080/o/verify-email?key=xxxxxxxxxx

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

		String key = request.getParameter("key");

		boolean isValidEmail = false;

		try {
			isValidEmail = VerifyEmail.verify(key);
		}
		catch (PortalException e) {
			_log.error(e);
		}

		response.sendRedirect(
			"/web/guest/verify-email?isValidEmail=" + isValidEmail);

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
