/**
 * 
 */

package com.dpabpm.mappingfile.bussiness;

import com.dpabpm.mappingfile.model.MappingFile;
import com.dpabpm.mappingfile.service.MappingFileLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;

/**
 * @author phucnv
 * @date Sep 22, 2017
 */
public class MappingFIleBussiness {

	/**
	 * @param tableName
	 * @param dataPk
	 * @param fileEntryId
	 * @param attFileTypeCode
	 * @param fileDownloadURL
	 * @param isCurrentUse
	 * @param serviceContext
	 * @return
	 */
	public static MappingFile createMappingFile(
		String tableName, String dataPk, long fileEntryId,
		String attFileTypeCode, boolean isCurrentUse,
		ServiceContext serviceContext) {

		return MappingFileLocalServiceUtil.createMappingFile(
			tableName, dataPk, fileEntryId, attFileTypeCode, isCurrentUse,
			serviceContext);
	}
}
