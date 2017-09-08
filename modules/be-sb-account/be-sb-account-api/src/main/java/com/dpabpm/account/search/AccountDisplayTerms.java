/**
 * 
 */

package com.dpabpm.account.search;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.dpabpm.util.datetime.DateTimeUtil;
import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;

/**
 * @author phucnv
 * @date Aug 24, 2017
 */
public class AccountDisplayTerms extends DisplayTerms {

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
	public static final String BIRTH_DAY = "birthday";
	public static final String BIRTH_MONTH = "birthmonth";
	public static final String BIRTH_YEAR = "birthyear";
	public static final String ADDRESS = "address";
	public static final String TEL_NO = "telNo";
	public static final String EMAIL = "email";
	public static final String STATUS = "status";
	public static final String MAPPING_USER_ID = "mappingUserId";

	public static final String PASSWORD_1 = "password1";
	public static final String PASSWORD_2 = "password2";

	/**
	 * @param request
	 */
	public AccountDisplayTerms(HttpServletRequest request) {
		super(request);

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
		_birthday = ParamUtil.getInteger(request, BIRTH_DAY);
		_birthmonth = ParamUtil.getInteger(request, BIRTH_MONTH);
		_birthyear = ParamUtil.getInteger(request, BIRTH_YEAR);
		_address = ParamUtil.getString(request, ADDRESS);
		_telNo = ParamUtil.getString(request, TEL_NO);
		_email = ParamUtil.getString(request, EMAIL);
		_status = ParamUtil.getInteger(request, STATUS);
		_mappingUserId = ParamUtil.getInteger(request, MAPPING_USER_ID);

		_password1 = ParamUtil.getString(request, PASSWORD_1);
		_password2 = ParamUtil.getString(request, PASSWORD_2);

		_fullName = _lastName + StringPool.SPACE + _firstName;

		_birthdate = DateTimeUtil.getDate(_birthday, _birthmonth, _birthyear);
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
	private int _birthday;
	private int _birthmonth;
	private int _birthyear;

	private String _address;
	private String _telNo;
	private String _email;
	private int _status;
	private long _mappingUserId;

	private String _password1;
	private String _password2;

	/**
	 * @return the uuid
	 */
	public String getUuid() {

		return _uuid;
	}

	/**
	 * @param uuid
	 *            the uuid to set
	 */
	public void setUuid(String uuid) {

		this._uuid = uuid;
	}

	/**
	 * @return the groupId
	 */
	public long getGroupId() {

		return _groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(long groupId) {

		this._groupId = groupId;
	}

	/**
	 * @return the companyId
	 */
	public long getCompanyId() {

		return _companyId;
	}

	/**
	 * @param companyId
	 *            the companyId to set
	 */
	public void setCompanyId(long companyId) {

		this._companyId = companyId;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {

		return _userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {

		this._userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {

		return _userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {

		this._userName = userName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {

		return _firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {

		this._firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {

		return _lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {

		this._lastName = lastName;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {

		return _fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {

		this._fullName = fullName;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {

		return _gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(int gender) {

		this._gender = gender;
	}

	/**
	 * @return the birthdate
	 */
	public Date getBirthdate() {

		return _birthdate;
	}

	/**
	 * @param birthdate
	 *            the birthdate to set
	 */
	public void setBirthdate(Date birthdate) {

		this._birthdate = birthdate;
	}

	/**
	 * @return the birthday
	 */
	public int getBirthday() {

		return _birthday;
	}

	/**
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(int birthday) {

		this._birthday = birthday;
	}

	/**
	 * @return the birthmonth
	 */
	public int getBirthmonth() {

		return _birthmonth;
	}

	/**
	 * @param birthmonth
	 *            the birthmonth to set
	 */
	public void setBirthmonth(int birthmonth) {

		this._birthmonth = birthmonth;
	}

	/**
	 * @return the birthyear
	 */
	public int getBirthyear() {

		return _birthyear;
	}

	/**
	 * @param birthyear
	 *            the birthyear to set
	 */
	public void setBirthyear(int birthyear) {

		this._birthyear = birthyear;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {

		return _address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {

		this._address = address;
	}

	/**
	 * @return the telNo
	 */
	public String getTelNo() {

		return _telNo;
	}

	/**
	 * @param telNo
	 *            the telNo to set
	 */
	public void setTelNo(String telNo) {

		this._telNo = telNo;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {

		return _email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {

		this._email = email;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {

		return _status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {

		this._status = status;
	}

	/**
	 * @return the mappingUserId
	 */
	public long getMappingUserId() {

		return _mappingUserId;
	}

	/**
	 * @param mappingUserId
	 *            the mappingUserId to set
	 */
	public void setMappingUserId(long mappingUserId) {

		this._mappingUserId = mappingUserId;
	}

	/**
	 * @return the password1
	 */
	public String getPassword1() {

		return _password1;
	}

	/**
	 * @param password1
	 *            the password1 to set
	 */
	public void setPassword1(String password1) {

		this._password1 = password1;
	}

	/**
	 * @return the password2
	 */
	public String getPassword2() {

		return _password2;
	}

	/**
	 * @param password2
	 *            the password2 to set
	 */
	public void setPassword2(String password2) {

		this._password2 = password2;
	}

}
