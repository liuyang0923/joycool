package jc.pay.alipay.config;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;


import jc.pay.alipay.util.BeanToMapUtil;
import jc.pay.alipay.vo.AlipayBizContentVo;
import jc.pay.alipay.vo.AlipayOrderInfoVo;



public class Demo {
	
	public static void main(String[] args){
		
	}
	
	private Logger LOGGER = Logger.getLogger(Demo.class);
	
	public Map<String, Object> getAliPaySinInfo(HttpServletRequest request) throws Exception {

		Map<String, Object> map = new HashMap<>();
		AlipayOrderInfoVo aoi = new AlipayOrderInfoVo();
		String scheme=request.getScheme();
		if("https".equals(request.getHeader("X-Forwarded-Proto"))){
			scheme=request.getHeader("X-Forwarded-Proto");
		}
		String url = scheme + "://" + request.getServerName() + request.getContextPath();
		String requestURL = request.getRequestURL().toString();
		requestURL = requestURL.substring(0, requestURL.indexOf(request.getContextPath())) + request.getContextPath();
		LOGGER.info("支付宝回调地址："+url + aoi.getNotify_url());
		aoi.setNotify_url(url + aoi.getNotify_url());
		aoi.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		AlipayBizContentVo biz_content = new AlipayBizContentVo();
		biz_content.setTotal_amount("24");
		biz_content.setOut_trade_no("order_no");
//		TradeServiceInfo serviceInfo = tradeServiceInfoService.selectByPrimaryKey(orderInfo.getServiceType());
		biz_content.setSubject("name");
		biz_content.setBody("body");
		aoi.setBiz_content(JSONObject.toJSONString(biz_content));
		Map<String, String> convertBean = BeanToMapUtil.convertBean(aoi);
		String sign = AlipaySignature.rsaSign(convertBean, AlipayConfig.PRIVATE_KEY, AlipayConfig.CHARSET);
		aoi.setSign(URLEncoder.encode(sign, AlipayConfig.CHARSET));
		map.put("aoi", aoi);
		map.put("bizContent", biz_content);
		// 支付类型
	/*	map.put("payType", Constant.alipay);
		map.put("orderMoney", orderInfo.getOrderMoney());*/
		return map;
	}
}
