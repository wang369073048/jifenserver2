package org.trc.task;

import com.trc.mall.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SMSTask implements Runnable{

	private Logger logger = LoggerFactory.getLogger(SMSTask.class);

	private static final String PREFIX = "【泰然城】";

	public static final String platformCode = "pub";	//elife_cl
	
	public static final String templateCode = "pub_score_kcgj";//elife_test

	//开发服
	//public static final String url = "http://172.30.251.246:8080/trcsms/sms/message/render";

	//正式服
	public static final String url = "https://open.trc.com/trcsms/sms/message/rendered";

	private String smsContent;

	private String phone;

	public SMSTask(String smsContent, String phone){
		super();
		this.smsContent = smsContent;
		this.phone = phone;
	}

//	private static SMSSendService smsSendService;
//
//	static {
//		smsSendService = (SMSSendService) SpringContextHolder.getBean("smsSendService");
//    }

	@Override
	public void run() {
		if(phone ==null){
			return;
		}
//		try {
//			boolean result = smsSendService.sendSMSCodeWithRenderedContent(smsCodeInfo, smsContent);
//		} catch (Exception e) {
//			logger.error("调用短信接口异常");
//		}
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("phone", phone);
			params.put("platformCode", platformCode);
			params.put("type", templateCode);
			if(smsContent.contains(PREFIX)) {
				params.put("content", smsContent);
			}else{
				params.put("content", PREFIX + smsContent);
			}
			HttpClientUtil.httpPostRequest(url, params, 1000);
		} catch (Exception e) {
			logger.error("调用短信接口异常:" + e.getMessage());
		}
	}

}
