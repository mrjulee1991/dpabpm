/**
 * 
 */

package com.dpabpm.account.search;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.portlet.ActionRequest;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;

/**
 * @author phucnv
 * @date Aug 24, 2017
 */
public class AccountDisplayTerms {

	public static final String ID = "id";
	public static final String UUID = "uuid";

	public static final String GROUP_ID = "groupId";
	public static final String COMPANY_ID = "companyId";
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String FULL_NAME = "fullName";
	public static final String GENDER = "gender";
	public static final String BIRTH_DATE = "birthdate";
	public static final String ADDRESS = "address";
	public static final String TEL_NO = "telNo";
	public static final String EMAIL = "email";
	public static final String STATUS = "status";
	public static final String MAPPING_USER_ID = "mappingUserId";

	public AccountDisplayTerms(ActionRequest request) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

		_uuid = ParamUtil.getString(request, UUID);

		_groupId = themeDisplay.getScopeGroupId();
		_companyId = themeDisplay.getCompanyId();
		_userId = themeDisplay.getUserId();
		_userName = themeDisplay.getUser().getFullName();

		_firstName = ParamUtil.getString(request, FIRST_NAME);
		_lastName = ParamUtil.getString(request, LAST_NAME);
		_gender = ParamUtil.getInteger(request, GENDER);
		_birthdate = ParamUtil.getDate(
			request, BIRTH_DATE, SimpleDateFormat.getDateInstance());
		_address = ParamUtil.getString(request, ADDRESS);
		_telNo = ParamUtil.getString(request, TEL_NO);
		_email = ParamUtil.getString(request, EMAIL);
		_status = ParamUtil.getInteger(request, STATUS);
		_mappingUserId = themeDisplay.getUserId();

		_fullName = _lastName + StringPool.SPACE + _firstName;

	}

	private String _uuid;

	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;

	private String _firstName;
	private String _lastName;
	private String _fullName;
	private int _gender;
	private Date _birthdate;
	private String _address;
	private String _telNo;
	private String _email;
	private int _status;
	private long _mappingUserId;

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

	public String getFirstName() {

		return _firstName;
	}

	public void setFirstName(String firstName) {

		this._firstName = firstName;
	}

	public String getLastName() {

		return _lastName;
	}

	public void setLastName(String lastName) {

		this._lastName = lastName;
	}

	public String getFullName() {

		return _fullName;
	}

	public void setFullName(String fullName) {

		this._fullName = fullName;
	}

	public int getGender() {

		return _gender;
	}

	public void setGender(int gender) {

		this._gender = gender;
	}

	public Date getBirthdate() {

		return _birthdate;
	}

	public void setBirthdate(Date birthdate) {

		this._birthdate = birthdate;
	}

	public String getAddress() {

		return _address;
	}

	public void setAddress(String address) {

		this._address = address;
	}

	public String getTelNo() {

		return _telNo;
	}

	public void setTelNo(String telNo) {

		this._telNo = telNo;
	}

	public String getEmail() {

		return _email;
	}

	public void setEmail(String email) {

		this._email = email;
	}

	public int getStatus() {

		return _status;
	}

	public void setStatus(int status) {

		this._status = status;
	}

	public long getMappingUserId() {

		return _mappingUserId;
	}

	public void setMappingUserId(long mappingUserId) {

		this._mappingUserId = mappingUserId;
	}

}
