package org.trc.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.trc.enums.CommonExceptionEnum;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.*;

/**
 * @ClassName: ExceptionUtil
 * @author wangzhen
 */
public class ExceptionUtil {
	private static Logger log = LoggerFactory.getLogger(ExceptionUtil.class);
	/**
	 *
	* @Title: handlerException 
	* @Description: 异常结果处理
	* @param @param appResult 返回结果实例
	* @param @param e 异常对象
	* @param @param invokingMethodName 当前调用方法
	* @return String
	* @throws
	 */
	public static String handlerException(Exception e, Class<?> targetClass, String invokingMethodName){
		StringBuilder builder = new StringBuilder();
		ExceptionEnum exceptionEnum = null;
		CommonExceptionEnum commonExceptionEnum = null;
		String excepMsg = "";
		String errorDtl = "";
		String excepCode = "";
		try {
			String exceptionName = e.getClass().getSimpleName();
			if (StringUtils.equals(exceptionName, ParamValidException.class.getSimpleName())) {
				ParamValidException paramValidException = (ParamValidException)e;
				commonExceptionEnum = paramValidException.getExceptionEnum();
			} else if (StringUtils.equals(exceptionName, ConfigException.class.getSimpleName())) {
				ConfigException configException = (ConfigException)e;
				exceptionEnum = configException.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, FileException.class.getSimpleName())) {
				FileException fileException = (FileException)e;
				exceptionEnum = fileException.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, DuplicateKeyException.class.getSimpleName())) {
				exceptionEnum = ExceptionEnum.DATABASE_DUPLICATE_KEY_EXCEPTION;
			}else if (StringUtils.equals(exceptionName, PermissionDeniedDataAccessException.class.getSimpleName())) {
				exceptionEnum = ExceptionEnum.DATABASE_PERMISSION_DENIED_EXCEPTION;
			}else if (StringUtils.equals(exceptionName, QueryTimeoutException.class.getSimpleName())) {
				exceptionEnum = ExceptionEnum.DATABASE_QUERY_TIME_OUT_EXCEPTION;
			}else if (StringUtils.equals(exceptionName, DeadlockLoserDataAccessException.class.getSimpleName())) {
				exceptionEnum = ExceptionEnum.DATABASE_DEADLOCK_DATA_ACESS_EXCEPTION;
			}else if (StringUtils.equals(exceptionName, BannerException.class.getSimpleName())) {
				BannerException bannerException = (BannerException)e;
				exceptionEnum = bannerException.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, CardCouponException.class.getSimpleName())) {
				CardCouponException cardCouponException = (CardCouponException)e;
				exceptionEnum = cardCouponException.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, AuthException.class.getSimpleName())) {
				AuthException exception = (AuthException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, BannerContentException.class.getSimpleName())) {
				BannerContentException exception = (BannerContentException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, BusinessException.class.getSimpleName())) {
				BusinessException exception = (BusinessException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, CategoryException.class.getSimpleName())) {
				CategoryException exception = (CategoryException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, ConfigException.class.getSimpleName())) {
				ConfigException exception = (ConfigException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, ConverterException.class.getSimpleName())) {
				ConverterException exception = (ConverterException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, CouponException.class.getSimpleName())) {
				CouponException exception = (CouponException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, FileException.class.getSimpleName())) {
				FileException exception = (FileException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, FlowException.class.getSimpleName())) {
				FlowException exception = (FlowException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, GoodsException.class.getSimpleName())) {
				GoodsException exception = (GoodsException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, GoodsRecommendException.class.getSimpleName())) {
				GoodsRecommendException exception = (GoodsRecommendException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, JurisdictionException.class.getSimpleName())) {
				JurisdictionException exception = (JurisdictionException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, ManagerException.class.getSimpleName())) {
				ManagerException exception = (ManagerException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, OrderException.class.getSimpleName())) {
				OrderException exception = (OrderException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, RoleException.class.getSimpleName())) {
				RoleException exception = (RoleException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, ScoreChangeRecordException.class.getSimpleName())) {
				ScoreChangeRecordException exception = (ScoreChangeRecordException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, ScoreConverterException.class.getSimpleName())) {
				ScoreConverterException exception = (ScoreConverterException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, ScoreException.class.getSimpleName())) {
				ScoreException exception = (ScoreException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, ShopException.class.getSimpleName())) {
				ShopException exception = (ShopException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, UserAccreditInfoException.class.getSimpleName())) {
				UserAccreditInfoException exception = (UserAccreditInfoException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else if (StringUtils.equals(exceptionName, UserCenterException.class.getSimpleName())) {
				UserCenterException exception = (UserCenterException)e;
				exceptionEnum = exception.getExceptionEnum();
			}else{
				exceptionEnum = ExceptionEnum.SYSTEM_EXCEPTION;
			}
		} catch (Exception e2) {
			excepMsg = "系统未捕获未知异常";
			errorDtl = e2.getMessage();
		}
		if(null != exceptionEnum){
			excepCode = exceptionEnum.getCode();
			excepMsg = exceptionEnum.getMessage();
			errorDtl = e.getMessage();
		}else if(null != commonExceptionEnum){
			excepCode = commonExceptionEnum.getCode();
			excepMsg = commonExceptionEnum.getMessage();
			errorDtl = e.getMessage();
		}
		builder = new StringBuilder();
		builder.append("外部系统调用");
		builder.append(targetClass.getName());
		builder.append("方法");
		builder.append(invokingMethodName);
		builder.append("发生");
		builder.append(excepMsg);
		builder.append(". 异常代码[").append(excepCode);
		builder.append("],错误明细信息：");
		builder.append(errorDtl);
		log.error(builder.toString());
		StringBuilder builder2 = new StringBuilder();
		builder2.append(excepMsg).append(",异常代码[").append(excepCode).append("],异常明细:").append(errorDtl);
		return builder2.toString();
	}
	
}
