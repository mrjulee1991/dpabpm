/**
 * 
 */

package com.dpabpm.login.auth;

import java.util.Map;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.AuthException;
import com.liferay.portal.kernel.security.auth.AuthFailure;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

/**
 * @author phucnv
 * @date Sep 10, 2017
 */

@Component(immediate = true, property = {
	"key=auth.failure"
}, service = AuthFailure.class)
public class LogAuthFailure implements AuthFailure {

	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.kernel.security.auth.AuthFailure#
	 * onFailureByEmailAddress(long, java.lang.String, java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public void onFailureByEmailAddress(
		long companyId, String emailAddress, Map<String, String[]> headerMap,
		Map<String, String[]> parameterMap)
		throws AuthException {

		try {
			User user = UserLocalServiceUtil.getUserByEmailAddress(
				companyId, emailAddress);

			int failures = user.getFailedLoginAttempts();

			if (_log.isInfoEnabled()) {
				_log.info(
					"onFailureByEmailAddress: " + emailAddress +
						" has failed to login " + failures + " times");
			}
		}
		catch (PortalException pe) {
			_log.error(pe);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.security.auth.AuthFailure#onFailureByScreenName
	 * (long, java.lang.String, java.util.Map, java.util.Map)
	 */
	@Override
	public void onFailureByScreenName(
		long companyId, String screenName, Map<String, String[]> headerMap,
		Map<String, String[]> parameterMap)
		throws AuthException {

		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.security.auth.AuthFailure#onFailureByUserId(
	 * long, long, java.util.Map, java.util.Map)
	 */
	@Override
	public void onFailureByUserId(
		long companyId, long userId, Map<String, String[]> headerMap,
		Map<String, String[]> parameterMap)
		throws AuthException {

		// TODO Auto-generated method stub

	}

	private static final Log _log = LogFactoryUtil.getLog(LogAuthFailure.class);

}
