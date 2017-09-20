/**
 * 
 */

package com.dpabpm.login.portlet;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

/**
 * @author phucnv
 * @date Sep 16, 2017
 */
@Component(property = {
	"javax.portlet.name=com_dpabpm_login_portelt_LoginPortlet",
	"mvc.command.name=/login/forgot_password"
}, service = MVCRenderCommand.class)
public class ForgotPasswordMVCRenderCommand implements MVCRenderCommand {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand#render(
	 * javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

		renderResponse.setTitle(themeDisplay.translate("forgot-password"));

		return "/html/account/forgot_password.jsp";
	}

}
