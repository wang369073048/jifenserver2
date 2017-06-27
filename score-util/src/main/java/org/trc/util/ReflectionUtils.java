package org.trc.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hzwzhen on 2017/6/23.
 */
public class ReflectionUtils {

	/**
	 * 判断obj参数是否存在fiedlName字段
	 */
	public static boolean hasField(Object obj, String fieldName) {
		return findField(obj, fieldName) != null;
	}

	  /**
	 * @Description
	 */
	public static Field findField(Class<?> clazz, String name)
	  {
	    return findField(clazz, name, null);
	  }

	/**
	 * @Description
	 */
	public static Field findField(Object obj, String name)
	  {
	    return findField(obj.getClass(), name, null);
	  }

	  /**
	 * @Description
	 */
	public static Field findField(Class<?> clazz, String name, Class<?> type)
	  {
		if(clazz == null ){
			throw new RuntimeException("Class must not be null");
		}
		if(name == null && type == null){
			throw new RuntimeException("Either name or type of the field must be specified");
		}
	    Class<?> searchType = clazz;
	    while ((!Object.class.equals(searchType)) && (searchType != null)) {
	      Field[] fields = searchType.getDeclaredFields();
	      for (Field field : fields) {
	        if (((name == null) || (name.equals(field.getName()))) && ((type == null) || (type.equals(field.getType())))) {
	          return field;
	        }
	      }
	      searchType = searchType.getSuperclass();
	    }
	    return null;
	  }

    /**
     * @Description 直接设置对象属性值,无视private/protected修饰符,不经过setter函数
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
            Field field = getDeclaredField(object, fieldName);

            if (field == null) {
                    throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
            }
            makeAccessible(field);

            try {
                    field.set(object, value);
            } catch (IllegalAccessException e) {
                    e.printStackTrace();
            }
    }

    /**
     * @Description 直接读取对象属性值,无视private/protected修饰符,不经过getter函数
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
            Field field = getDeclaredField(object, fieldName);

            if (field == null) {
                    throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
            }
            makeAccessible(field);

            Object result = null;
            try {
                    result = field.get(object);
            } catch (IllegalAccessException e) {

            }
            return result;
    }
    
    /**
     * @Description 直接读取对象属性类型,无视private/protected修饰符,不经过getter函数
     */
    public static Class<?> getFieldType(final Object object, final String fieldName) {
            Field field = getDeclaredField(object, fieldName);

            if (field == null) {
                    throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
            }
            makeAccessible(field);
            return field.getType();
    }

    /**
     * @Description 直接调用对象方法,无视private/protected修饰符
     */
    public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes,
                    final Object[] parameters) throws InvocationTargetException {
            Method method = getDeclaredMethod(object, methodName, parameterTypes);
            if (method == null) {
                    throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
            }
            method.setAccessible(true);

            try {
                    return method.invoke(object, parameters);
            } catch (IllegalAccessException e) {
            }

            return null;
    }

    /**
     * @Description 循环向上转型,获取对象的DeclaredField
     */
    protected static Field getDeclaredField(final Object object, final String fieldName) {
            for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                            .getSuperclass()) {
                    try {
                            return superClass.getDeclaredField(fieldName);
                    } catch (NoSuchFieldException e) {
                    }
            }
            return null;
    }

    /**
     * @Description 循环向上转型,获取对象的DeclaredField
     */
    protected static void makeAccessible(final Field field) {
            if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
                    field.setAccessible(true);
            }
    }

    /**
     * @Description 循环向上转型,获取对象的DeclaredMethod
     */
    protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
            for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                            .getSuperclass()) {
                    try {
                            return superClass.getDeclaredMethod(methodName, parameterTypes);
                    } catch (NoSuchMethodException e) {
                    }
            }
            return null;
    }

    /**
	 * 获取对象中的注解
	 */
	public static <T  extends Annotation> T getAnnotation(Object o,Class<T> annotationClass) {
		Class<?> cl = o.getClass();
		if (cl.isAnnotationPresent(annotationClass)) {
			return (T)cl.getAnnotation(annotationClass);
		}
		return null;
	}

	/**
	 * 获取对象中的注解
	 */
	public static <T extends Annotation> T getAnnotation(Class<?> o,Class<T> annotationClass) {
		if (o.isAnnotationPresent(annotationClass)) {
			return (T)o.getAnnotation(annotationClass);
		}
		return null;
	}

	/**
	 * 获取Object对象中所有annotationClass类型的注解
	 */
	public static <T extends Annotation> List<T> getAnnotations(Object o,Class<T> annotationClass) {
		return getAnnotations(o.getClass(), annotationClass);
	}

	/**
	 *
	 * 获取对象中的所有annotationClass注解
	 */
	public static <T extends Annotation> List<T> getAnnotations(Class<?> o,Class<T> annotationClass) {

		List<T> result = new ArrayList<T>();
		T annotation = o.getAnnotation(annotationClass);
		if (annotation != null) {
			result.add(annotation);
		}
		Constructor<?>[] constructors = o.getDeclaredConstructors();
		//获取构造方法里的注解
		CollectionUtils.addAll(result,getAnnotations(constructors,annotationClass).iterator());

		Field[] fields = o.getDeclaredFields();
		//获取字段中的注解
		CollectionUtils.addAll(result,getAnnotations(fields,annotationClass).iterator());

		Method[] methods = o.getDeclaredMethods();
		//获取方法中的注解
		CollectionUtils.addAll(result,getAnnotations(methods,annotationClass).iterator());

		for (Class<?> superClass = o.getSuperclass(); superClass != Object.class;superClass = superClass.getSuperclass()) {
			List<T> temp = getAnnotations(superClass,annotationClass);
			if (temp != null && !temp.isEmpty()) {
				CollectionUtils.addAll(result, temp.iterator());
			}
		}

		return result;
	}

	/**
	 * 获取field的annotationClass注解
	 */
	public static <T extends Annotation> T getAnnotation(Field field, Class<T> annotationClass) {
		if (field.isAnnotationPresent(annotationClass)) {
			field.setAccessible(true);// 设置实体类私有属性可访问
			return (T) field.getAnnotation(annotationClass);
		}
		return null;
	}

	/**
	 * 获取field数组中匹配的annotationClass注解
	 */
	public static <T extends Annotation> List<T> getAnnotations(Field[] fields, Class<T> annotationClass) {

		List<T> result = new ArrayList<T>();

		if (ArrayUtils.isEmpty(fields)) {
			return result;
		}

		for (Field field : fields) {
			field.setAccessible(true);
			T annotation = field.getAnnotation(annotationClass);
			if (annotation != null) {
				result.add(annotation);
			}
		}

		return result;
	}

	/**
	 * 获取method的annotationClass注解
	 */
	public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotationClass) {
		if (method.isAnnotationPresent(annotationClass)) {
			return (T) method.getAnnotation(annotationClass);
		}
		return null;
	}

	/**
	 * 获取method数组中匹配的annotationClass注解
	 */
	public static <T extends Annotation> List<T> getAnnotations(Method[] methods, Class<T> annotationClass) {

		List<T> result = new ArrayList<T>();

		if (ArrayUtils.isEmpty(methods)) {
			return result;
		}

		for (Method method : methods) {
			method.setAccessible(true);
			T annotation = method.getAnnotation(annotationClass);
			if (annotation != null) {
				result.add(annotation);
			}
		}

		return result;
	}

	/**
	 * 获取constructor的annotationClass注解
	 */
	public static <T extends Annotation> T getAnnotation(Constructor<T> constructor, Class<T> annotationClass) {
		if (constructor.isAnnotationPresent(annotationClass)) {
			return (T) constructor.getAnnotation(annotationClass);
		}
		return null;
	}

	/**
	 * 获取constructors数组中匹配的annotationClass注解
	 */
	public static <T extends Annotation> List<T> getAnnotations(Constructor<T>[] constructors, Class<T> annotationClass) {


		List<T> result = new ArrayList<T>();

		if (ArrayUtils.isEmpty(constructors)) {
			return result;
		}

		for (Constructor<T> constructor : constructors) {
			constructor.setAccessible(true);
			T annotation = constructor.getAnnotation(annotationClass);
			if (annotation != null) {
				result.add(annotation);
			}
		}

		return result;
	}

    /**
     * @Description 通过反射,获得Class定义中声明的父类的泛型参数的类型
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperClassGenricType(final Class<?> clazz) {
            return (Class<T>) getSuperClassGenricType(clazz, 0);
    }

    /**
     * @Description 通过反射,获得Class定义中声明的父类的泛型参数的类型
     */
    public static Class<?> getSuperClassGenricType(final Class<?> clazz, final int index) {
            Type genType = clazz.getGenericSuperclass();
            if (!(genType instanceof ParameterizedType)) {
                    return Object.class;
            }
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index >= params.length || index < 0) {
                    return Object.class;
            }
            if (!(params[index] instanceof Class)) {
                    return Object.class;
            }
            return (Class<?>) params[index];
    }

    /**
     * @Description 将反射时的checked exception转换为unchecked exception
     */
    public static IllegalArgumentException convertToUncheckedException(Exception e) {
            if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                            || e instanceof NoSuchMethodException) {
                    return new IllegalArgumentException("Refelction Exception.", e);
            } else {
                    return new IllegalArgumentException(e);
            }
    }


	/**
	 * @Description 提取集合中的对象的属性(通过getter函数), 组合成List
	 */
	public static List<Object> convertElementPropertyToList(final Collection<Object> collection, final String propertyName) {
		List<Object> list = new ArrayList<Object>();

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw convertToUncheckedException(e);
		}
		return list;
	}


	/**
	 * @Description 实例化反射类
	 */
	public static final <T> T getNewInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Field> getFieldList(Class<?> clazz1,Class<? extends Annotation> clazz2){
		Field[] allFields = clazz1.getDeclaredFields();// 得到所有定义字段
		List<Field> fields = new ArrayList<Field>();
		// 得到所有field并存放到一个list中.
		for (Field field : allFields) {
			if (field.isAnnotationPresent(clazz2)) {
				fields.add(field);
			}
		}
		return  fields;
	}

}
