package com.xunli.manager.enumeration;

public enum ReturnCode {

	AUTH_PHONE_IS_NULL("AUTH_0001","Phone number is null","手机号码为空"),
	AUTH_NO_ACCOUNT_NUMBER("AUTH_0002","No account number","没有该账号"),// 登陆LoginResultCode
	AUTH_ACCOUNT_STATUS_ERROR("AUTH_0003","Account status error","账号状态错误"),
	AUTH_OLD_PASSWORD_ERROR("AUTH_0004","Old password error","旧密码错误"),
	AUTH_NEW_PASSWORD_ERROR("AUTH_0005","New password error","新密码错误"),
	//AUTH_Forbidden_Login_ERROR("AUTH_0006","Forbidden login","一分钟登录次数超过10次,禁止登录五分钟"),

	// 注册RegisterResultCode
	AUTH_MOBILE_PHONE_NUMBER_ERROR("AUTH_0006","Mobile phone number error","手机号码错误"), // 手机长度、格式问题
	AUTH_EMAIL_ERROR("AUTH_0007","Email error","邮箱错误"), // 邮箱格式问题
	AUTH_LACK_OF_REAL_NAME("AUTH_0008","Lack of real name","缺少姓名"), //缺少真实姓名
	AUTH_NAME_LENGTH_ERROR("AUTH_0009","Name length error","姓名长度错误"), 
	AUTH_CERTIFICATE_CATEGORY_MISTAKE("AUTH_0010","Certificate category mistake","证件类别错误"), // 证件类别空、获得类别代码非已有的类别代码
	AUTH_CERTIFICATE_NUMBER_WRONG("AUTH_0011","Certificate number wrong","证件号码错误"), // 证件号码为空、格式、长度、与证件类别不对应
	AUTH_ACCOUNT_ALREADY_EXISTS("AUTH_0012","Account already exists","用户已存在 "), 
	AUTH_MOBILE_PHONE_NUMBER_HAS_BEEN_USED("AUTH_0013","Mobile phone number has been used","手机号码已使用"), 
	AUTH_EMAIL_HAS_BEEN_USED("AUTH_0014","Email has been used","邮箱已使用"), 
	AUTH_REAL_NAME_AND_CERTIFICATE_NAME_DO_NOT_MATCHING("AUTH_0015","Real name and Certificate name don’t matching","真实姓名与证件姓名不匹配"), // 与证件不匹配上的名字不匹配
	AUTH_COMPANY_NUMBER_ERROR("AUTH_0016","Company number error","机构编号错误"), // 公司编号机构编号空或不存在等错误
	AUTH_LOCK_FLAG_ERROR("AUTH_0017","Lock flag error","锁定标志错误"), 
	AUTH_BUSINESS_ENCODING_ERROR("AUTH_0018","Business encoding error","业务编码错误"), // 业务编号为空或不存在
	
	AUTH_LOGIN_FAILURE("AUTH_0019","Login failure","登录失败"),
	AUTH_ACCOUNT_IS_NULL("AUTH_0020","Account is not exist","账号不存在"),
	AUTH_ACCOUNT_CHILDREN_IS_NULL("AUTH_0021","User's children is not exist","用户子女信息不存在"),
	AUTH_ACCOUNT_NOT_LOGIN("AUTH_0022","Account not login","账号未登录"),
	
	//公用系列======================================================
	PUBLIC_SUCCESS("0000","Success","成功"),
	PUBLIC_PARAMETER_MISSING("0001","Parameter missing","参数丢失"), 
	PUBLIC_OTHER_ERROR("0002","Other error","其他错误"), 
	PUBLIC_NO_DATA("0003","No data","数据库中没有相应的数据"), 
	PUBLIC_PARAMETER_ERROR("0004","Parameter error","参数错误"),
	PUBLIC_TOKEN_MISSING("0005","Token missing","Token缺失"),//
	PUBLIC_TOKEN_IS_INVALID("0006","Token is invalid","Token无效"),//
	PUBLIC_AUTHID_ERROR("0007","AuthId error","authId错误"),//authId为空或无效，暂列，不一定用到。
	PUBLIC_PASSWORD_ERROR("0008","Password error","密码错误"), 
	PUBLIC_ACCOUNT_ERROR("0009","Account error","用户名错误"), // 账号用户名格式不对等
	PUBLIC_NO_ACCESS_RIGHTS("0010","No access rights","没有访问权限"), 
	PUBLIC_EXCEED_THE_NUMBER_OF_REQUESTS("0011","Exceed the number of requests","超出请求次数"),
	PUBLIC_OTHER_ERROR_OF_THIRD_PARTY("0012","Other error of third party","第三方平台返回的其他错误"),
	PUBLIC_ACCESS_DATABASE_ERROR("0013","Access database error","访问数据库错误"),
	PUBLIC_START_TIME_ERROR("0014","Start time error","开始时间错误"),
	PUBLIC_END_TIME_ERROR("0015","End time error","结束时间错误"),
	PUBLIC_SERVER_BUSY("0016","Server busy","服务器忙"),
	PUBLIC_ENCRYTOR_FAILURE("0017","Encrytor failure","加密失败"),
	PUBLIC_DECRYPTOR_FAILURE("0018","Decryptor failure","解密失败"),
	PUBLIC_MODIFY_FAILURE("0019","Modify failure","操作失败"),
	PUBLIC_USER_INFORMATION_IS_INCORRECT_OR_NO_LOGIN("0020","User information is incorrect or no login","用户信息有误或没有登录"),
	PUBLIC_DATE_CONVERSION_ERROR("0021","Date conversion error","日期转换错误"),
	PUBLIC_UPLOAD_IMAGE_FAIL("0022","Upload image Fail","图片上传失败"),

	// 系统
	SYSTEM_SYSTEM_ERROR("SYSTEM_0001","System error","系统错误"), 
	SYSTEM_ACCESS_INTERNAL_RESOURCE_ERROR("SYSTEM_0002","Access internal resource error","访问内部资源错误"), // HTTP请求访问系统内部接口错误

	//
	;
	private String code;

    private String msg;

    private String detail;
	
	private ReturnCode(String code, String msg, String detail){
        this.code = code;
        this.msg = msg;
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

	public String getDetail() {
		return detail;
	}

	public static ReturnCode convert(String code){
		for(ReturnCode returnCode : ReturnCode.values()){
			if(returnCode.getCode().equals(code)){
				return returnCode;
			}
		}
		return ReturnCode.SYSTEM_SYSTEM_ERROR;
	}
}
