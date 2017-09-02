/**
 * 
 */

package com.dpabpm.mappingfile.search;

import javax.portlet.ActionRequest;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

/**
 * @author phucnv
 * @date Sep 1, 2017
 */
public class MappingFileDisplayTerms {

	public static final String ID = "id";
	public static final String UUID = "uuid";

	public static final String GROUP_ID = "groupId";
	public static final String COMPANY_ID = "companyId";
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";

	public static final String TABLE_NAME = "tableName";
	public static final String DATA_PK = "dataPk";
	public static final String FILE_ENTRY_ID = "fileEntryId";
	public static final String ATT_FILE_TYPE_CODE = "attFileTypeCode";

	public MappingFileDisplayTerms(ActionRequest request) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

		_uuid = ParamUtil.getString(request, UUID);

		_groupId = themeDisplay.getScopeGroupId();
		_companyId = themeDisplay.getCompanyId();
		_userId = themeDisplay.getUserId();
		_userName = themeDisplay.getUser().getFullName();

		_tableName = ParamUtil.getString(request, TABLE_NAME);
		_dataPk = ParamUtil.getString(request, DATA_PK);
		_fileEntryId = ParamUtil.getLong(request, FILE_ENTRY_ID);
		_attFileTypeCode = ParamUtil.getString(request, ATT_FILE_TYPE_CODE);

	}

	private String _uuid;

	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;

	private String _tableName;
	private String _dataPk;
	private long _fileEntryId;
	private String _attFileTypeCode;

	public String getUuid() {

		return _uuid;
	}

	public void setUuid(String uuid) {

		this._uuid = uuid;
	}

	public long getGroupId() {

		return _groupId;
	}

	public void setGroupId(long groupId) {

		this._groupId = groupId;
	}

	public long getCompanyId() {

		return _companyId;
	}

	public void setCompanyId(long companyId) {

		this._companyId = companyId;
	}

	public long getUserId() {

		return _userId;
	}

	public void setUserId(long userId) {

		this._userId = userId;
	}

	public String getUserName() {

		return _userName;
	}

	public void setUserName(String userName) {

		this._userName = userName;
	}

	public String getTableName() {

		return _tableName;
	}

	public void setTableName(String tableName) {

		this._tableName = tableName;
	}

	public String getDataPk() {

		return _dataPk;
	}

	public void setDataPk(String dataPk) {

		this._dataPk = dataPk;
	}

	public long getFileEntryId() {

		return _fileEntryId;
	}

	public void setFileEntryId(long fileEntryId) {

		this._fileEntryId = fileEntryId;
	}

	public String getAttFileTypeCode() {

		return _attFileTypeCode;
	}

	public void setAttFileTypeCode(String attFileTypeCode) {

		this._attFileTypeCode = attFileTypeCode;
	}

}
