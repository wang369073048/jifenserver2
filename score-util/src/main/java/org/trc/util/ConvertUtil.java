package org.trc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 类型转换工具
 * Created by huyan on 2016/7/2.
 */
public class ConvertUtil {


    /**
     * 对象转换,将S类型对象转成T类型对象,相同属性复制，用于Dao层与Service层之间的转化
     *
     * @param s   源对象
     * @param t   目标对象
     * @param <T> 目标对象类型
     * @param <S> 源对象类型
     * @return
     */
    @SuppressWarnings("hiding")
    public static <T, S> T convert(S s, T t) {
        if (s == null || t == null) {
            return null;
        }
        Class<? extends Object> classS = s.getClass();
        Class<? extends Object> classT = t.getClass();
        Field[] sFields = classS.getDeclaredFields();
        Field[] tFields = classT.getDeclaredFields();

        for (Field sfield : sFields) {
            try {
                for (Field tfield : tFields) {
                    // 序列化接口要求的final字段不需要转换
                    if ("serialVersionUID".equals(tfield.getName())) {
                        continue;
                    }

                    //属性名称相同 类型相同
                    if (sfield.getName().equals(tfield.getName()) && sfield.getType() == tfield.getType()) {
                        //根据属性名称返回和p相同的一个属性
                        tfield = classT.getDeclaredField(sfield.getName());
                        String methodName = "set" + tfield.getName().substring(0, 1).toUpperCase()
                                + tfield.getName().substring(1);
                        //根据方法名称和参数类型返回和p相同的一个set方法
                        Method method = classT.getDeclaredMethod(methodName, sfield.getType());

                        /**
                         * 这里必须要setAccessible,不然会报错
                         * IllegalAccessException ：can not access a member of class xxx "private"
                         */
                        sfield.setAccessible(true);
                        //执行set方法，类似于 userDO.setUserId(userid)
                        method.invoke(t, sfield.get(s));
                    }

                }
            } catch (NoSuchMethodException noSuchMethodException) {
                noSuchMethodException.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(String.format("转换%s到%s对象失败！", s.toString(), t.toString()), e);
            }
        }

        return t;
    }

    //list转list
    @SuppressWarnings({ "unchecked", "hiding" })
    public static <T, S> List<T> convert(List<S> listS, T t) {
        List<T> list = new ArrayList<>();
        try {
            //返回与带有给定字符串名的类或接口相关联的 Class 对象
            Class<T> c = (Class<T>) Class.forName(t.getClass().getName());
            for (S s : listS) {
                //创建此 Class 对象所表示的类的一个新实例
                T temp = c.newInstance();
                list.add(convert(s, temp));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

}
