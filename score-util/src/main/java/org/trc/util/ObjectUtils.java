package org.trc.util;


import org.apache.commons.lang3.StringUtils;

/**
 * @description 对象工具类
 * @author wulg
 * @Date   2015年7月4日
 */
public class ObjectUtils {
	/**
	 * 转换对象工具
	 * @param val
	 * @param defaultVal
	 * @return
	 */
	public static Object convertVal(Object val, Object defaultVal) {
		return val==null?defaultVal:val;
	}
	
	//判断两个对象是否相等
	public static boolean isEquals(Object actual, Object expected) {
        return actual == null ? expected == null : actual.equals(expected);
    }
	
	/**
	 * 校验参数是否为null
	 * @param object
	 * @return
	 */
	public static boolean isBlank(Object...object){
		if(object == null){
			return true;
		}
		for(int i = 0 ; i < object.length ; i++){
			Object obj = object[i];
			if(obj instanceof String){
				if(obj == null || ((String) obj).trim().equals("")){
					return true;
				}
			}else if(obj instanceof Double){ 
				if(obj == null ||  ((Double) obj).compareTo(0.00) <= 0){
					return true;
				}
			}else{
				if(obj == null){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 *数组各对象都不存在 (null或空格)
	 */
	public static boolean isNotBlank(Object...object){
		return !isBlank(object);
	}
	
	/**
	 * Double 转成   double
	 */
	public static double doubleValue(Double obj){
		if(obj == null){
			return 0.00;
		}
		return obj.doubleValue();
	}
	
	/** 
     * 压缩字符串，每个单词的间隔都是1个空格 
     */
	public static String compress(String str) { 
		if(StringUtils.isBlank(str)){
			return "";
		}
        return str.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");  
    }  
	
	/**
	 *压缩字符串 去除多余空格及换行符
	 */
	public static String compressAll(String str) { 
		if(StringUtils.isBlank(str)){
			return "";
		}
        return str.replaceAll("\\s*", "");  
    }  
}
