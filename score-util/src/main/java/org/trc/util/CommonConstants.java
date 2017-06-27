package org.trc.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Rest 框架应用常量
 * Created by hzxjy on 2016/6/29.
 */
public class CommonConstants {
	// 统一默认编码
	public static final String ENCODING = "UTF-8";
	public static final String BASIC_FILTER = "com.tairanchina.filter";
	/**
	 * 过滤器常量
	 */
	public final static class FilterConstants {
		public static final int FILTER_MAINTENANCE 		= Integer.MIN_VALUE+10;
		public static final int FILTER_SQLSECURITY		= Integer.MIN_VALUE+20;
		public static final int FILTER_XSSSECURITY		= Integer.MIN_VALUE+30;
		public static final int FILTER_AUTHORIZATION	= Integer.MIN_VALUE+40;
		public static final int FILTER_QUERY			= Integer.MIN_VALUE+50;
		public static final int FILTER_TICKER 			= Integer.MIN_VALUE+60;
		public static final int FILTER_POWERED			= Integer.MIN_VALUE+70;
		public static final int FILTER_ECHO 			= Integer.MIN_VALUE+80;
	}

	/**
	 * 错误代码
	 */
	public enum ErrorCode {
		ERROR_TEMP(						0, "临时变量，为了保留原先code值"),
		ERROR_ILLEGAL_PARAMETER(		1, "缺少参数或参数不合法"),
		ERROR_IP_BLOCKED(				2, "您的 ip 被限制访问"),
		ERROR_ACCOUNT_BLOCKED(			3, "账号被锁定，请30分钟后尝试"),
		ERROR_ILLEGAL_PHONE(			4, "请输入正确的手机号码"),
		ERROR_DETECT_HTMLINJECT(        5, "发现 html 注入攻击"),
		ERROR_ILLEGAL_UUID(				6, "uuid 不合法"),
		ERROR_INTERNAL_ERROR(			7, "您的操作有误或服务器繁忙，请稍后重试。如有问题请联系客服热线 0571-96056"),
		ERROR_SMS_CODE(					8, "请输入正确的验证码"),
		ERROR_CAPTCHA_CODE(				9, "图形验证码无效"),
		ERROR_NO_AUTHORITY(				10, "没有权限"),
		ERROR_PHONE_EXISTS(				11, "账号已被注册"),
		ERROR_PHONE_NO_EXISTS(			12, "账号不存在"),
		ERROR_PASSWORD_ERROR(			13, "账号或者密码错误，请重新输入"),
		ERROR_MAX_TRY_TIME(				14, "操作过于频繁，手机号码被锁定。如有问题请联系客服热线 0571-96056"),
		ERROR_OPERATOR_FAILED(			15, "运营商发送失败"),
		ERROR_PHONE_SAME(				16, "相同的手机号码"),
		ERROR_NO_DATA(					17, "没有数据"),
		ERROR_DETECT_INJECT(            18, "发现 sql 注入攻击"),
		ERROR_SERVER_MAINTENANCE(       19, "服务器正在维护。如有问题请联系客服热线 0571-96056"),
		ERROR_SMS_CODE_LIMITED(         20, "手机验证码超过校验次数"),
		ERROR_EMAIL_ERROR(              21, "邮箱无效"),
		ERROR_EMAIL_EXISTS(				22, "邮箱已被绑定"),
		ERROR_SMS_CODE_TYPE(            23, "手机验证码类型无效"),
		ERROR_SMS_CODE_RESEND(          24, "验证码已失效，请重新获取"),
		ERROR_SMS_CODE_FAILED(			25, "短信发送失败"),
		ERROR_DETECT_XSSINJECT(         26, "发现xss攻击"),
		ERROR_PAYTIMES_BLOCKED(			27, "账号被锁定，请30分钟后尝试"),
		ERROR_AWARD_MAXNUMBER(			28, "抽奖次数已达上限"),
		ERROR_OAUTHUID_EXISTS(	    	29, "该第三方平台用户已经绑定"),
		ERROR_OAUTH_REJECTED(	    	30, "未授权访问该第三方平台"),
		ERROR_OAUTHTOKEN_ILLEGAL(		30, "该平台OPENID非法"),
		ERROR_ACCOUNT_BLACK_USER(		32, "您的账号异常"),
		ERROR_USER_REFERER(				33, "推荐码无效"),
		ERROR_PAY_PASSWORD_ERROR(		34, "支付密码错误"),
		ERROR_SIGN_VERFIY_FAIL(	    	35, "签名校验失败"),
		ERROR_SERVICE_IN_REST(          36, "您的操作有误或服务器繁忙，请稍后重试。如有问题请联系客服热线 0571-96056"),
        ERROR_EMAIL_CODE_FAILED(	    37, "邮件发送失败"),
		ERROR_MAIL_CODE_TYPE(           38, "邮件类型无效"),
		ERROR_NOT_FOUND(          		39, "请求地址不存在"),
		ERROR_NO_LOGIN(					40, "没有登录"),
		ERROR_ILLEGAL_OPERATION(		41, "操作不合法"),
		ERROR_CUSTOM(					99, "自定义的错误码");
		private Integer code;
		private String description;
		private String tempDesc;
		public String getTempDesc() {
			return tempDesc;
		}
		public void setTempDesc(String tempDesc) {
			this.tempDesc = tempDesc;
		}
		public Integer getCode() {
			return code;
		}
		public String getDescription() {
			return description;
		}
		ErrorCode(Integer code, String description) {
			this.code = code;
			this.description = description;
		}

		/**
		 * 自定义错误码
         */
		public ErrorCode customCode(int customCode) {
			ERROR_TEMP.code = code;
			ERROR_TEMP.description = description;
			this.code = customCode;

			return this;
		}

		/**
		 * 自定义错误描述
		 */
		public ErrorCode customDescription(String customDescription) {
			ERROR_TEMP.code = code;
			ERROR_TEMP.description = description;
			if (customDescription != null) {
				this.description = customDescription;
			}

			return this;
		}

		@Override
	    public String toString() {
	        JSONObject json = new JSONObject();
	        JSONObject error = new JSONObject();
	        error.put("code", code);
	        error.put("description", description);
	        json.put("error", error);
	        
	        if (ERROR_TEMP.code.equals(code)) {
				this.description = ERROR_TEMP.description;
			}
	        return json.toString();
	    }
	}
}
