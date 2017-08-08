package org.trc.enums;

/***
 * 
 * @Description:访问服务端操作类型枚举
 * @author hzxab
 * @date 2017年8月7日 下午1:53:25
 */
public enum RequestTypeEnum {

    GET(1,"GET"),
    POST(2,"POST"),
    PUT(3,"PUT"),
	DELETE(4,"DELETE");

    private Integer code;
    private String name;

    RequestTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }
    
 // 普通方法
 	public static Integer getCode(String desc) {
 		for (RequestTypeEnum c : RequestTypeEnum.values()) {
 			if (c.getName().equals(desc)) {
 				return c.getCode();
 			}
 		}
 		return null;
 	}

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
