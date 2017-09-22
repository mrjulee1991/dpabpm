/**
 * 
 */

package com.dpabpm.login.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

import com.dpabpm.account.model.Account;
import com.dpabpm.account.search.AccountDisplayTerms;
import com.dpabpm.account.service.AccountLocalServiceUtil;
import com.dpabpm.mappingfile.bussiness.MappingFIleBussiness;
import com.dpabpm.util.FileUtil;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

/**
 * @author phucnv
 * @date Sep 21, 2017
 */
@Component(property = {
	"javax.portlet.name=com_dpabpm_login_portelt_LoginPortlet",
	"mvc.command.name=/account/profile"
}, service = MVCActionCommand.class)
public class ProfileMVCActionCommand extends BaseMVCActionCommand {

	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand#
	 * doProcessAction(javax.portlet.ActionRequest,
	 * javax.portlet.ActionResponse)
	 */
	@Override
	protected void doProcessAction(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DLFileEntry.class.getName(), actionRequest);

		UploadPortletRequest uploadPortletRequest =
			PortalUtil.getUploadPortletRequest(actionRequest);

		String fileName =
			uploadPortletRequest.getFileName(AccountDisplayTerms.AVATAR);

		File file = uploadPortletRequest.getFile(AccountDisplayTerms.AVATAR);

		String mimeType =
			uploadPortletRequest.getContentType(AccountDisplayTerms.AVATAR);

		InputStream is = new FileInputStream(file);

		String description = themeDisplay.getUser().getFullName() + "'s avatar";

		FileEntry fileEntry = FileUtil.uploadFile(
			serviceContext.getUserId(), serviceContext.getCompanyId(),
			serviceContext.getScopeGroupId(), is, fileName, mimeType,
			file.length(), AccountDisplayTerms.AVATAR, description,
			serviceContext);

		Account account = AccountLocalServiceUtil.findByMappingUserId(
			serviceContext.getUserId());

		MappingFIleBussiness.createMappingFile(
			Account.class.getName(), account.getUuid(),
			fileEntry.getFileEntryId(), AccountDisplayTerms.AVATAR, true,
			serviceContext);
	}

}
