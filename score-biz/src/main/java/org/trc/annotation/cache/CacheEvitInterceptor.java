package org.trc.annotation.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.trc.util.RedisUtil;

import java.lang.reflect.Method;

@Component
@Aspect
public class CacheEvitInterceptor extends BaseInterceptor{
	/**
	 * 定义缓存逻辑
	 * @throws Throwable 
	 */
	@Around("@annotation(org.trc.annotation.cache.CacheEvit)")
	public Object cache(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		try {
			Method method = getMethod(pjp);
			CacheEvit cacheevit = method.getAnnotation(org.trc.annotation.cache.CacheEvit.class);
			String className = pjp.getTarget().getClass().getName();
			// 列表
			RedisUtil.delObject(className + "LIST");
			String[] keys = cacheevit.key();
			for (String key : keys) {
				key = className + parseKey(key, method, pjp.getArgs());
				// 本体
				RedisUtil.delObject(key);
			}

		} catch (Throwable e) {
			//出exception了,继续即可,不需要处理
		}finally{
			result = pjp.proceed();
		}
		
		return result;
	}


}
