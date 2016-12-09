package jc.pay.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息 -- 帐号名，帐号 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public static String ACCOUNT_NAME="北京好欣晴移动医疗科技有限公司";
	public static String EMAIL="zhangmin@haoxinqing.cn";
	
	
	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息 -- APP支付 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public static String APP_ID = "2016091301896311";
	public static String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALGdBpqJG6WR/flAatI+lb7lYPjhZVFf/Up7wyQmAnL3WFqMwfgH//zw2od4ew2Y1PDtMkKL7JarawaU0aX/coD38kNTv80M8MB74jIvC/ZcTh/RmEj+6G1MuOc3JhefUnMHbWmQSdx0+meGM7mXsctL/rR/8g1Bl1hkfzI7BK9PAgMBAAECgYAJ/gKMGSCUDQWTyzr3PwwfpDnzZoLTtCnE0felhfnnS+ENnxOeh5ywN0CkFHQfzLRWJZIJSmPyCgzIXwD4bMmdPdaCoJMX0Lu6AfiI7P2kKJLJqa9IuiN93T07TOOh1cR/yT2Ko7i9rMzDIvnvbnkZDqHAeZg8Kkc98DQeHSzpwQJBANlOSRn7klSuyuv0DFi6ioHQREoxkwSTjhpgmqy8qp2hlRDYGGyAl6jcMSQX28ool8Cp4x4hzzvfnBzKovSL/OECQQDRPWRz1QFPg8zBrRibvx2Em0fhmTrGLAuDgW91B/X4bt37NmkBqwmsFDftzagIhHFtjsmDF5EZIXeUbNAsx4IvAkEAnlQBwyKe3YB1bTFlhYh0vlyGRSCVo7H2yZU1XUIhdbRzLdClCvkuhuPYEhooE8U7vC6LwjqUCWXFEt05mcAdQQJBAIVvg1ojIpzUzoVMn/7i2C4y4JpMH6E1gXox1yRpGUF/YmzL/BcmPhrRIgGzP09/Nt7JSlUheFTwdNPguLl12d0CQCdXsccJN6qUItIn1MBMBF8m6WUnW78ek8uj6uM0z3zhCbU0o1ldsBjm6MX0srWAi5Llf7COZELdpCb8sITBbug=";
	public static String CHARSET = "UTF-8";
	public static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	public static String ALIPAY_URL = "https://openapi.alipay.com/gateway.do";
	public static String CONTENT_TYPE = "JSON";
	
	
	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息 -- 批量付款 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String PARTNER = "2088421807909703";
	public static String KEY = "dljlfh55jb2t586952kda7qmhxoi7x82";
	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	public static String INPUT_CHARSET = "utf-8";
	public static String SIGN_TYPE = "MD5";
	

}
