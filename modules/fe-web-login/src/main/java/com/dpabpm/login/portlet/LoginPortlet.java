
package com.dpabpm.login.portlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

/**
 * @author phucnv
 * @date Sep 10, 2017
 */
@Component(immediate = true, property = {
	"com.liferay.portlet.display-category=category.dpabpm.login",
	"com.liferay.portlet.instanceable=true",
	"javax.portlet.display-name=Login Portlet",
	"javax.portlet.name=MyLoginPortlet",
	"javax.portlet.init-param.template-path=/html/login/",
	"javax.portlet.init-param.view-template=/html/login/login.jsp",
	"javax.portlet.resource-bundle=content.Language_vi",
	"javax.portlet.security-role-ref=power-user,user"
}, service = Portlet.class)
public class LoginPortlet extends MVCPortlet {

}
