package com.xunli.manager.enumeration;

public enum ReturnCode {

	AUTH_FILE_RESOURCE_CAN_NOT_FIND("AUTH_0001","File resources can not find","文件资源找不到"), // 文件资源找不到
	AUTH_NO_ACCOUNT_NUMBER("AUTH_0002","No account number","没有该账号"),// 登陆LoginResultCode
	AUTH_ACCOUNT_STATUS_ERROR("AUTH_0003","Account status error","账号状态错误"),
	AUTH_OLD_PASSWORD_ERROR("AUTH_0004","Old password error","旧密码错误"),
	AUTH_NEW_PASSWORD_ERROR("AUTH_0005","New password error","新密码错误"),
	AUTH_Forbidden_Login_ERROR("AUTH_0006","Forbidden login","一分钟登录次数超过10次,禁止登录五分钟"),

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
	
	// 因恒生返回的这几个消息代码都一样，用中文判断不是很好，所以统一作为一个错误消息返回了。
	AUTH_REGISTRATION_INFORMATION_ERROR("AUTH_0019","Registration information error","注册来源、证件类别、证件号码、用户名、锁定标志不能为空"), 
	AUTH_CERTIFICATE_INFORMATION_HAS_BEEN_CERTIFIED("AUTH_0020","Certificate information has been certified","证件信息已被认证"),
	AUTH_THERE_IS_NO_REGISTERED_SOURCE_INFORMATION("AUTH_0021","There is no registered source information","注册来源信息不存在"),
	AUTH_REGISTRATION_FAILURE("AUTH_0022","Registration failure","注册失败"), //
	AUTH_ACCOUNT_IS_NULL("AUTH_0023","Account is null","用户名为空"),
	AUTH_PASSWORD_IS_NULL("AUTH_0024","Password is null","密码为空"),
	AUTH_EMAIL_IS_NULL("AUTH_0025","Email is null","邮箱为空"),
	AUTH_MOBILE_IS_NULL("AUTH_0026","Mobile is null","手机号码为空"),
	AUTH_CERTIFICATE_CATEGORY_IS_NULL("AUTH_0027","Certificate category is null","证件类别为空"),
	AUTH_CERTIFICATE_NUMBER_IS_NULL("AUTH_0028","Certificate number is null","证件号码为空"),
	AUTH_USERNAME_OR_PASSWORD_ERROR("AUTH_0029","Username or password error","用户名或密码错误"),
	AUTH_NEW_PASSWORD_IS_NULL("AUTH_0030","New password is null","新密码为空"),
	AUTH_MODIFY_PASSWORD_FAILURE("AUTH_0031","Modify password failure","修改密码失败"),
	AUTH_ENTER_THE_NEW_PASSWORD_AGAIN("AUTH_0032","Enter the new password again","再次输入新密码"),
	AUTH_TWO_PASSWORD_DOES_NOT_MATCH("AUTH_0033","Two password does not match","两次密码不匹配"),
	AUTH_AUTHID_OR_TOKEN_ERROR("AUTH_0034","Authid or token error","AuthId或Token错误"),
	AUTH_ACCOUNT_EMAIL_MOBILE_EMAIL_MUST_BE_FILLED_IN_ONE("AUTH_0035","Account email mobile email must be filled in one","账号、手机、邮箱必须填写一个"),
	AUTH_KEY_INFORMATION_IS_EMPTY("AUTH_0036","Key information is empty","关键信息为空"),//用于恒生的一些返回消息不好分级
	AUTH_MODIFY_MOBILE_FAILURE("AUTH_0037","Modify mobile failure","修改手机失败"),
	AUTH_MODIFY_EMAIL_FAILURE("AUTH_0038","Modify mobile failure","修改邮箱失败"),
	AUTH_MODIFY_CERTIFICATE_FAILURE("AUTH_0039","Modify certificate failure","修改证件信息、姓名失败"),
	AUTH_REAL_NAME_HAS_BEEN_AUTHENTICATED("AUTH_0040","Real name has been authenticated","姓名已认证"),
	AUTH_PARTIAL_DATA_SYNCHRONOUS_FAILURE("AUTH_0041","Partial data synchronous failure","部分数据同步到第三方平台失败"),
	AUTH_LOGIN_FAILURE("AUTH_0042","Login failure","登录失败"),
	

	
	//API接口
	API_NO_INTERFACE("API_0001","NO_INTERFACE","接口不存在"),
	API_NO_INTERFACE_RIGHT("API_0002","NO_INTERFACE_RIGHT","没有该接口权限"),
	API_MISS_PARAMETER("API_0003","MISS_PARAMETER","缺少必须参数"),
	API_PARAMETER_RANGE_ERROR("API_0004","PARAMETER_RANGE_ERROR","参数值范围错误"),
	API_PARAMETER_TYPE_ERROR("API_0005","PARAMETER_TYPE_ERROR","参数类型错误"),

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

	//一些可能会在两个一级映射以上会用到的状态码，抽为公共状态码了。


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
