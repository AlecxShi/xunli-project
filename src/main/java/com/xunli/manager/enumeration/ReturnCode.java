package com.xunli.manager.enumeration;

public enum ReturnCode {

	// 账号相关
	AUTH_PHONE_IS_NULL(16001,"Phone number is null","手机号码为空"),
	AUTH_NO_ACCOUNT_NUMBER(16002,"No account number","没有该账号"),// 登陆LoginResultCode
	AUTH_ACCOUNT_STATUS_ERROR(16003,"Account status error","账号状态错误"),
	AUTH_OLD_PASSWORD_ERROR(16004,"Old password error","旧密码错误"),
	AUTH_NEW_PASSWORD_ERROR(16005,"New password error","新密码错误"),
	AUTH_MOBILE_PHONE_NUMBER_ERROR(16006,"Mobile phone number error","手机号码错误"), // 手机长度、格式问题
	AUTH_EMAIL_ERROR(16007,"Email error","邮箱错误"), // 邮箱格式问题
	AUTH_LACK_OF_REAL_NAME(16008,"Lack of real name","缺少姓名"), //缺少真实姓名
	AUTH_NAME_LENGTH_ERROR(16009,"Name length error","姓名长度错误"),
	AUTH_CERTIFICATE_CATEGORY_MISTAKE(16010,"Certificate category mistake","证件类别错误"), // 证件类别空、获得类别代码非已有的类别代码
	AUTH_CERTIFICATE_NUMBER_WRONG(16011,"Certificate number wrong","证件号码错误"), // 证件号码为空、格式、长度、与证件类别不对应
	AUTH_ACCOUNT_ALREADY_EXISTS(16012,"Account already exists","用户已存在 "),
	AUTH_MOBILE_PHONE_NUMBER_HAS_BEEN_USED(16013,"Mobile phone number has been used","手机号码已使用"),
	AUTH_EMAIL_HAS_BEEN_USED(16014,"Email has been used","邮箱已使用"),
	AUTH_LOGIN_FAILURE(16015,"Login failure","登录失败"),
	AUTH_ACCOUNT_IS_NULL(16016,"Account is not exist","账号不存在"),
	AUTH_ACCOUNT_CHILDREN_IS_NULL(16017,"User's children is not exist","用户子女信息不存在"),
	AUTH_ACCOUNT_NOT_LOGIN(16018,"Account not login","账号未登录"),
	AUTH_TARGET_CHILDREN_IS_NULL(16041,"Target children is null","目标用户子女信息为空"),
	
	//公用系列======================================================
	PUBLIC_SUCCESS(16019,"Success","成功"),
	PUBLIC_PARAMETER_MISSING(16020,"Parameter missing","参数丢失"),
	PUBLIC_OTHER_ERROR(16021,"Other error","其他错误"),
	PUBLIC_NO_DATA(16022,"No data","数据库中没有相应的数据"),
	PUBLIC_PARAMETER_ERROR(16023,"Parameter error","参数错误"),
	PUBLIC_TOKEN_MISSING(16024,"Token missing","Token缺失"),//
	PUBLIC_TOKEN_IS_INVALID(16025,"Token is invalid","Token无效"),//
	PUBLIC_PASSWORD_ERROR(16026,"Password error","密码错误"),
	PUBLIC_ACCOUNT_ERROR(16027,"Account error","用户名错误"), // 账号用户名格式不对等
	PUBLIC_NO_ACCESS_RIGHTS(16028,"No access rights","没有访问权限"),
	PUBLIC_EXCEED_THE_NUMBER_OF_REQUESTS(16029,"Exceed the number of requests","超出请求次数"),
	PUBLIC_OTHER_ERROR_OF_THIRD_PARTY(16030,"Other error of third party","第三方平台返回的其他错误"),
	PUBLIC_ACCESS_DATABASE_ERROR(16031,"Access database error","访问数据库错误"),
	PUBLIC_START_TIME_ERROR(16032,"Start time error","开始时间错误"),
	PUBLIC_END_TIME_ERROR(16033,"End time error","结束时间错误"),
	PUBLIC_SERVER_BUSY(16034,"Server busy","服务器忙"),
	PUBLIC_MODIFY_FAILURE(16035,"Modify failure","操作失败"),
	PUBLIC_USER_INFORMATION_IS_INCORRECT_OR_NO_LOGIN(16036,"User information is incorrect or no login","用户信息有误或没有登录"),
	PUBLIC_DATE_CONVERSION_ERROR(16037,"Date conversion error","日期转换错误"),
	PUBLIC_UPLOAD_IMAGE_FAIL(16038,"Upload image Fail","图片上传失败"),

	// 系统
	SYSTEM_SYSTEM_ERROR(16039,"System error","系统错误"),
	SYSTEM_ACCESS_INTERNAL_RESOURCE_ERROR(16040,"Access internal resource error","访问内部资源错误"), // HTTP请求访问系统内部接口错误

	;
	private Integer code;

    private String msg;

    private String detail;
	
	private ReturnCode(Integer code, String msg, String detail){
        this.code = code;
        this.msg = msg;
        this.detail = detail;
    }

    public Integer getCode() {
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
