/**
 * 
 */

package com.dpabpm.login.portlet;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.captcha.CaptchaUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;

/**
 * @author phucnv
 * @date Sep 17, 2017
 */
@Component(property = {
	"javax.portlet.name=com_dpabpm_login_portelt_LoginPortlet",
	"mvc.command.name=/login/captcha"
}, service = MVCResourceCommand.class)
public class CaptchaMVCResourceCommand implements MVCResourceCommand {

	@Override
	public boolean serveResource(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		try {
			CaptchaUtil.serveImage(resourceRequest, resourceResponse);

			return false;
		}
		catch (Exception e) {
			_log.error(e);

			return true;
		}
	}

	private static final Log _log =
		LogFactoryUtil.getLog(CaptchaMVCResourceCommand.class);

}
