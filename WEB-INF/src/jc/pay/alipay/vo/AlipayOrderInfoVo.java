package jc.pay.alipay.vo;

import java.io.Serializable;

import jc.pay.alipay.config.AlipayConfig;


public class AlipayOrderInfoVo implements Serializable {
	private String app_id = AlipayConfig.APP_ID;
	// // NOTE: 支付接口名称
	// order.method = @"alipay.trade.app.pay";
	private String method = "alipay.trade.app.pay";

	// private String format = "JSON";
	// // NOTE: 参数编码格式
	// order.charset = @"utf-8";
	private String charset = "utf-8";

	// // NOTE: 当前时间点
	// order.timestamp = [formatter stringFromDate:[NSDate date]];
	private String timestamp;

	// // NOTE: 支付版本
	// order.version = @"1.0";
	private String version = "1.0";

	// // NOTE: sign_type设置
	// order.sign_type = @"RSA";
	private String sign_type = "RSA";

	// private String notify_url =
	// "http://106.38.95.114:11111/hxp-app-web/alipay/receiveNotify";
	private String notify_url = "/alipay/receiveNotify";

	private String biz_content;

	private String sign = "";

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getBiz_content() {
		return biz_content;
	}

	public void setBiz_content(String biz_content) {
		this.biz_content = biz_content;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
