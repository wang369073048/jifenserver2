package org.trc.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 工具类
 * Created by wangzhen
 */
public class TxJerseyTools {
    private static final String PASSWORD = "password";
    private static final String NEWPASSWORD = "newPassword";
    private static final String SENSITIVE_VALUE = "******";
    private static final String DEV_URI = "http://devtest.pocketwallet.cn";
    private static Logger logger = LoggerFactory.getLogger(TxJerseyTools.class);
    private static NewCookie userCookie;
    private static NewCookie adminCookie;

    /**
     * 获取request cookies中的token
     */
    public static String getToken(ContainerRequestContext requestContext) {
        String token = null;
        Cookie cookie = requestContext.getCookies().get("token");
        if (cookie != null) {
            token = cookie.getValue();
        }
        return token;
    }

    /**
     * 获取地址栏参数
     */
    public static void getAddrParams(ContainerRequestContext requestContext, Map<String, String> map) throws UnsupportedEncodingException {
        // 地址栏参数的情况
        MultivaluedMap<String, String> queryPath = requestContext.getUriInfo().getQueryParameters();
        Iterator<Map.Entry<String, List<String>>> iterator = queryPath.entrySet().iterator();
        String value;
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = iterator.next();
            try {
                value = URLDecoder.decode(entry.getValue().get(0), "UTF-8");
            } catch (Exception e) {
                value = entry.getValue().get(0);
            }
            map.put(entry.getKey(), value);
        }
    }

    /**
     * 获取路径参数
     */
    public static void getPathParams(ContainerRequestContext requestContext, Map<String, String> map) throws UnsupportedEncodingException {
        MultivaluedMap<String, String> path = requestContext.getUriInfo().getPathParameters();
        Iterator<Map.Entry<String, List<String>>> iterator = path.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = iterator.next();
            String value;
            try {
                value = URLDecoder.decode(entry.getValue().get(0), "UTF-8");
            } catch (Exception e) {
                value = entry.getValue().get(0);
            }
            map.put(entry.getKey(), value);
        }
    }

    /**
     * 解析json格式参数
     */
    public static void getJsonParams(String jsonStr, Map<String, String> map) {
        //最外层解析
        JSONObject json = JSONObject.parseObject(jsonStr);
        for (String k : json.keySet()) {
            Object v = json.get(k);
            //目前不考虑json数组的情况
            if (v != null) {
                map.put(k, v.toString());
            }
        }
    }

    /**
     * 获取IP地址
     */
    public static String getIp(ContainerRequestContext requestContext) {
        // 访问行为统计
        String ip;
        ip = requestContext.getHeaderString("x-forwarded-for");
        if (ip != null && !ip.isEmpty() && ip.compareToIgnoreCase("unknown") != 0) {
            String[] ips = ip.split(",");
            return ips[0];
        }
        ip = requestContext.getHeaderString("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && ip.compareToIgnoreCase("unknown") != 0) {
            String[] ips = ip.split(",");
            return ips[0];
        }
        ip = requestContext.getHeaderString("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && ip.compareToIgnoreCase("unknown") != 0) {
            String[] ips = ip.split(",");
            return ips[0];
        }
        return "unknown";
    }

    /**
     * xss攻击
     */
    public static String stripXSS(String value) {
        if (value != null) {
            value = value.replaceAll("", "");
            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e­xpression
            scriptPattern = Pattern.compile(" src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile(" src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid eval(...) e­xpressions
            scriptPattern = Pattern.compile(" eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid e­xpression(...) e­xpressions
            scriptPattern = Pattern.compile(" e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid javascript:... e­xpressions
            scriptPattern = Pattern.compile(" javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid vbscript:... e­xpressions
            scriptPattern = Pattern.compile(" vbscript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid onload= e­xpressions
            scriptPattern = Pattern.compile(" onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }

    /**
     * 过滤敏感词
     */
    public static void stripSensitive(Map<String, String> map) {
        if (map == null) {
            return;
        }
        if (map.containsKey(PASSWORD)) {
            map.put(PASSWORD, SENSITIVE_VALUE);
        }
        if (map.containsKey(NEWPASSWORD)) {
            map.put(NEWPASSWORD, SENSITIVE_VALUE);
        }
    }

    /**
     * 管理员cookie（测试用）
     */
    public static NewCookie getAdminCookie() {
        if (adminCookie == null) {
            adminCookie = getCookie("phone=18012341234&password=123456");
        }
        return adminCookie;
    }

    /**
     * 用户cookie（测试用）
     */
    public static NewCookie getUserCookie() {
        if (userCookie == null) {
            userCookie = getCookie("phone=15012341234&password=123456");
        }
        return userCookie;
    }

    private static NewCookie getCookie(String requestForm) {
        Client c = ClientBuilder.newClient();
        WebTarget telnetTarget = c.target(DEV_URI);
        // 登陆
        Response response = telnetTarget.path("account/user/login").request().
                buildPost(Entity.entity(requestForm, MediaType.APPLICATION_FORM_URLENCODED_TYPE)).
                invoke();
        response.close();
        userCookie = response.getCookies().get("token");
        return userCookie;
    }

    /**
     * 解析String为Long类型
     */
    public static Long paramsToLong(String params) {
        try {
            return Long.parseLong(params);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析String为Integer类型
     */
    public static Integer paramsToInteger(String params) {
        try {
            return Integer.parseInt(params);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析String为Double类型
     */
    public static Double paramsToDouble(String params) {
        try {
            return Double.parseDouble(params);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 维护保证
     */
    public static String maintenancePackage(String maintenanceMsg) {
        return "亲，请稍候，" + maintenanceMsg + "正在维护";
    }

    /**
     * 异常返回处理
     */
    public static Response returnAbort(CommonConstants.ErrorCode errorCode) {
        String errorJson = errorCode.toString();
        return Response.status(Status.BAD_REQUEST).entity(errorJson).type(MediaType.APPLICATION_JSON).encoding(CommonConstants.ENCODING).build();
    }

    /**
     * 异常返回处理
     */
    public static Response returnAbort(String errorJson) {
        return Response.status(Status.BAD_REQUEST).entity(errorJson).type(MediaType.APPLICATION_JSON).encoding(CommonConstants.ENCODING).build();
    }

    /**
     * 异常返回处理
     */
    public static Response returnServerError(CommonConstants.ErrorCode errorCode) {
        String errorJson = errorCode.toString();
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorJson).type(MediaType.APPLICATION_JSON).encoding(CommonConstants.ENCODING).build();
    }

    /**
     * 异常返回处理
     */
    public static Response returnServerError(String errorJson) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorJson).type(MediaType.APPLICATION_JSON).encoding(CommonConstants.ENCODING).build();
    }

    /**
     * 成功返回处理
     */
    public static Response returnSuccess(String json) {
        if (json != null) {
            return Response.status(Status.OK).entity(json).type(MediaType.APPLICATION_JSON).encoding(CommonConstants.ENCODING).build();
        } else {
            return Response.status(Status.OK).build();
        }
    }

    /**
     * 成功返回处理
     */
    public static Response returnSuccess() {
        return Response.status(Status.OK).build();
    }
}
