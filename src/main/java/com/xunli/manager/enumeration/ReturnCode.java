package com.xunli.manager.enumeration;

public enum ReturnCode {

	// 账号相关
	AUTH_PHONE_IS_NULL(1201,"Phone number is null","手机号码为空"),
	AUTH_NO_ACCOUNT_NUMBER(1203,"No account number","没有该账号"),// 登陆LoginResultCode
	AUTH_ACCOUNT_STATUS_ERROR(1205,"Account status error","账号状态错误"),
	AUTH_OLD_PASSWORD_ERROR(1207,"Old password error","旧密码错误"),
	AUTH_NEW_PASSWORD_ERROR(1209,"New password error","新密码错误"),
	AUTH_MOBILE_PHONE_NUMBER_ERROR(1211,"Mobile phone number error","手机号码错误"),
	AUTH_ACCOUNT_ALREADY_EXISTS(1213,"Account already exists","用户已存在 "),
	AUTH_MOBILE_PHONE_NUMBER_HAS_BEEN_USED(1215,"Mobile phone number has been used","手机号码已使用"),
	AUTH_LOGIN_FAILURE(1217,"Login failure","登录失败"),
	AUTH_ACCOUNT_IS_NULL(1219,"Account is not exist","账号不存在"),
	AUTH_ACCOUNT_CHILDREN_IS_NULL(1221,"User children is not exist","用户子女信息不存在"),
	AUTH_TARGET_CHILDREN_IS_NULL(1223,"Target children is null","目标用户子女信息为空"),
	
	//公用系列======================================================
	PUBLIC_SUCCESS(0,"Success","成功"),
	PUBLIC_PARAMETER_MISSING(1001,"Parameter missing","参数丢失"),
	PUBLIC_OTHER_ERROR(1003,"Other error","其他错误"),
	PUBLIC_NO_DATA(1005,"No data","没有数据"),
	PUBLIC_PARAMETER_ERROR(1007,"Parameter error","参数错误"),
	PUBLIC_TOKEN_MISSING(1009,"Token missing","Token缺失"),//
	PUBLIC_TOKEN_IS_INVALID(1011,"Token is invalid","Token无效"),
	PUBLIC_MODIFY_FAILURE(1013,"Modify failure","操作失败"),
	PUBLIC_USER_INFORMATION_IS_INCORRECT_OR_NO_LOGIN(1015,"User not login","用户没有登录或登录信息过期"),
	PUBLIC_UPLOAD_IMAGE_FAIL(1017,"Upload image Fail","图片上传失败"),
	PUBLIC_UPLOAD_IMAGE_FULL(1019,"Only upload 8 iages","最多允许上传8张图片"),
	PUBLIC_UPLOAD_IMAGE_TOO_LARGE(1021,"Image too large","图片过大"),
	PUBLIC_UPLOAD_IMAGE_PATTERN_ERROR(1023,"Image pattern error","图片格式不支持"),
	PUBLIC_MISS_NECESSARY_INFO(1025,"Miss necessary param","缺少必要信息"),
	PUBLIC_ENCRYP_FAIL(1027,"Encryp Fail","加密失败"),

	// 系统
	SYSTEM_SYSTEM_ERROR(1101,"System error","系统错误")

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
