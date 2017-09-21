/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.dpabpm.mappingfile.service.impl;

import java.util.Date;

import com.dpabpm.mappingfile.model.MappingFile;
import com.dpabpm.mappingfile.service.base.MappingFileLocalServiceBaseImpl;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import aQute.bnd.annotation.ProviderType;

/**
 * The implementation of the mapping file local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link com.dpabpm.mappingfile.service.MappingFileLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author phucnv
 * @see MappingFileLocalServiceBaseImpl
 * @see com.dpabpm.mappingfile.service.MappingFileLocalServiceUtil
 */
@ProviderType
public class MappingFileLocalServiceImpl
	extends MappingFileLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this class directly. Always use
	 * {@link com.dpabpm.mappingfile.service.MappingFileLocalServiceUtil} to
	 * access the mapping file local service.
	 */

	/**
	 * @param tableName
	 * @param dataPk
	 * @param fileEntryId
	 * @param attFileTypeCode
	 * @param fileDownloadURL
	 * @param currentUse
	 * @param serviceContext
	 * @return
	 */
	@Override
	public MappingFile createMappingFile(
		String tableName, String dataPk, long fileEntryId,
		String attFileTypeCode, String fileDownloadURL, boolean isCurrentUse,
		ServiceContext serviceContext) {

		long mappingFileId =
			counterLocalService.increment(MappingFile.class.getName());

		MappingFile mappingFile = mappingFilePersistence.create(mappingFileId);

		User user = userLocalService.fetchUser(serviceContext.getUserId());

		Date now = new Date();

		mappingFile.setUuid(PortalUUIDUtil.generate());

		mappingFile.setGroupId(serviceContext.getScopeGroupId());
		mappingFile.setCompanyId(serviceContext.getCompanyId());
		mappingFile.setUserId(user.getUserId());
		mappingFile.setUserName(user.getFullName());
		mappingFile.setCreateDate(now);
		mappingFile.setModifiedDate(now);

		mappingFile.setTableName(tableName);
		mappingFile.setDataPk(dataPk);
		mappingFile.setFileEntryId(fileEntryId);
		mappingFile.setAttFileTypeCode(attFileTypeCode);
		mappingFile.setFileDownloadURL(fileDownloadURL);
		mappingFile.setIsCurrentUse(isCurrentUse);

		mappingFile = mappingFilePersistence.update(mappingFile);

		return mappingFile;
	}
}
