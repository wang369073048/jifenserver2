package org.trc.annotation.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.trc.util.RedisUtil;

import java.lang.reflect.Method;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/30
 */
@Component
@Aspect
public class CacheableInterceptor extends BaseInterceptor{
    /**
     * 定义缓存逻辑
     * @throws Throwable
     */
    @Around("@annotation(org.trc.annotation.cache.Cacheable)")
    public Object cache(ProceedingJoinPoint pjp ) throws Throwable {
        Object result=null;
        String key = "";
        String listKey = "";
        int expireTime = 3600;
        boolean isList = false;
        //是否应该将结果放入缓存
        boolean shouldSet = false;
        try{
            Method method=getMethod(pjp);
            Cacheable cacheable=method.getAnnotation(org.trc.annotation.cache.Cacheable.class);
            //是否是列表
            isList = cacheable.isList();
            expireTime = cacheable.expireTime();
            String className = pjp.getTarget().getClass().getName();
            //取对应的缓存结果
            if(isList){
                key = className + "LIST";
                listKey = method.toString() + parseKey(cacheable.key(),method,pjp.getArgs());
                result= RedisUtil.hget(key, listKey);
            }else{
                key = className + parseKey(cacheable.key(),method,pjp.getArgs());
                result = RedisUtil.getObject(key);
            }

            //到达这一步证明参数正确，没有exception，应该放入缓存
            shouldSet = true;
        }catch(Exception e){
            //出exception了,继续即可,不需要处理
        }finally{
            //没有缓存执行结果
            if(result==null){
                if(isList){
                    result=pjp.proceed();
                    if(shouldSet){
                        RedisUtil.hset(key,listKey, result,expireTime);
                    }
                }else{
                    result = pjp.proceed();
                    if(shouldSet){
                        Assert.notNull(key);
                        RedisUtil.setObject(key, result,expireTime);
                    }
                }
            }
        }
        return result;
    }
}
