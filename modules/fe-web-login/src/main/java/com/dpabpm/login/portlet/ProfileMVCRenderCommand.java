/**
 * 
 */

package com.dpabpm.login.portlet;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;

/**
 * @author phucnv
 * @date Sep 21, 2017
 */
@Component(property = {
	"javax.portlet.name=com_dpabpm_login_portelt_LoginPortlet",
	"mvc.command.name=/account/profile"
}, service = MVCRenderCommand.class)
public class ProfileMVCRenderCommand implements MVCRenderCommand {

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

		return "/html/account/profile.jsp";
	}

}
