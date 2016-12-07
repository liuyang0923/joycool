package net.joycool.wap.spec.pay;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import other.util.Http;
import other.util.HttpResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.spec.shop.ShopUtil;
import net.joycool.wap.util.Base64;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 支付
 * @datetime:1007-10-24
 */
public class PayAction extends CustomAction{
	
	public static int CARD_CHINAMOBILE = 0;//神州行
	public static int CARD_UNICOM = 1;//联通
	public static int CARD_TEST = 2;//测试
	
	public PayBean[] payBeans = {
		new PayBean("","","","","","",""),//神州行
		new PayBean("7608","2.00","RMB","LTJFK","LTJFK00020000","eo7ogrrcktifoftsvzsh3io9s945qmnyva29v9qf1mkyjqx0vozjsvt2kbf8jihnubukn6a61wklwtr6a385150mc2pkekvc66p2hiu7yvq00biqrp7bxn7hwzrasgow",""),//全国联通一卡充
		new PayBean("7608","2.00","RMB","CMJFK","CMJFK00010001","123456789","http://219.238.200.40/z/tmp/test.jsp"),
	};
	
	public static String testServer = "http://114.255.7.208/pgworder/orderdirect.do";
	
	public static String server = "http://pay.19pay.com/pgworder/orderdirect.do";
	
	UserBean loginUser;
	public static PayService service = new PayService();
	
	public PayAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		loginUser = super.getLoginUser();
	}
	public PayAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}
	
	
	public String searchOrder(PayOrderBean order) throws Exception{
		PayBean bean = service.getPayBeanById(order.getChannelId());
		StringBuilder sb = new StringBuilder(256);
		sb.append("version_id=");
		sb.append(bean.getVersionId());
		sb.append("&merchant_id=");
		sb.append(bean.getMerchantId());
		sb.append("&order_date=");
		sb.append(DateUtil.formatDate(new Date(order.getCreateTime()), "yyyyMMdd"));
		sb.append("&order_id=");
		sb.append(order.getId());
		sb.append("&merchant_key=");
		sb.append(bean.getMerchantKey());
		
		String dstr = sb.toString();
		//System.out.println(dstr);
		dstr = getMD5(dstr).toLowerCase();
		//System.out.println(dstr);
		sb = new StringBuilder(256);
		sb.append("version_id=");
		sb.append(bean.getVersionId());
		sb.append("&merchant_id=");
		sb.append(bean.getMerchantId());
		sb.append("&verifystring=");
		sb.append(dstr);
		sb.append("&order_date=");
		sb.append(DateUtil.formatDate(new Date(order.getCreateTime()), "yyyyMMdd"));
		sb.append("&order_id=");
		sb.append(order.getId());
		sb.append("&retmode=");
		//sb.append("&order_date=20090420&order_id=31&retmode=");
		//System.out.println(sb.toString());
		HttpResponse res = Http.doRequest(bean.getSearchURL(), "post", sb.toString(), "utf8");
		
		String xmlString = new String(res.getContent(),"utf8");
		//sb.append("version_id=2.0&merchant_id=7608&order_date=20090420&order_id=31&merchant_key=123456789");
		//System.out.println(xmlString);
		return xmlString;
	}
	
	public String submitPay(int orderId, int amount, String card, String cardpwd, int cardType) throws Exception {
		String date = sdf.format(new Date());
		String amountstr = numFormat.format(amount);
		
		PayBean payBean = service.getPayBeanById(cardType);
		
		card = desEncrypt(card, payBean.getMerchantKey());
		cardpwd = desEncrypt(cardpwd, payBean.getMerchantKey());
		
		StringBuilder sb = new StringBuilder(256);
		sb.append("version_id=");
		sb.append(payBean.getVersionId());
		sb.append("&merchant_id=");
		sb.append(payBean.getMerchantId());
		
		sb.append("&order_date=");
		sb.append(date);
		sb.append("&order_id=");
		sb.append(orderId);
		
		sb.append("&amount=");
		sb.append(amountstr);
		sb.append("&currency=");
		sb.append(payBean.getCurrency());
		
		sb.append("&cardnum1=");
		sb.append(card);
		sb.append("&cardnum2=");
		sb.append(cardpwd);
		
		sb.append("&pm_id=");
		sb.append(payBean.getPmId());
		sb.append("&pc_id=");
		sb.append(payBean.getPcId());
		sb.append("&merchant_key=");
		sb.append(payBean.getMerchantKey());
		
		String dstr = sb.toString();
		dstr = getMD5(dstr).toLowerCase();
		
		sb = new StringBuilder(256);
		sb.append("version_id=");
		sb.append(payBean.getVersionId());
		sb.append("&merchant_id=");
		sb.append(payBean.getMerchantId());
		
		sb.append("&order_date=");
		sb.append(date);
		sb.append("&order_id=");
		sb.append(orderId);
		
		sb.append("&amount=");
		sb.append(amountstr);
		sb.append("&currency=");
		sb.append(payBean.getCurrency());
		
		sb.append("&cardnum1=");
		sb.append(card);
		sb.append("&cardnum2=");
		sb.append(cardpwd);
		
		sb.append("&pm_id=");
		sb.append(payBean.getPmId());
		sb.append("&pc_id=");
		sb.append(payBean.getPcId());
		sb.append("&verifystring=");
		sb.append(dstr);
		
		sb.append("&notify_url=");
		sb.append(payBean.getNotifyURL());
		String xmlString = "";
		try {
			String post = sb.toString();
//			System.out.println("postdata:" + post);
			HttpResponse res = Http.doRequest(payBean.getSubmitURL(), "post", post, "utf8");
			xmlString = new String(res.getContent(),"utf8");
			//String result =	parseXmlNodeValue(xmlString, "result");
			
//			if(result.equals("P")) {
//				return true;
//			} else if(result.equals("F")) {
//				return false;
//			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return xmlString;
		}
		return xmlString;
	}
	
	public static String desEncrypt(String src, String key2) throws Exception {
		javax.crypto.SecretKey key = genDESKey(key2.getBytes());
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES");
		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
		return byteArr2HexStr(cipher.doFinal(src.getBytes()));
	}

	public static String desDecrypt(String crypt, String key2) throws Exception {
		javax.crypto.SecretKey key = genDESKey(key2.getBytes());
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES");
		cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(hexStr2ByteArr(crypt)));
	}
	
	public static javax.crypto.SecretKey genDESKey(byte[] key_byte) throws Exception {
		DESKeySpec dks = new DESKeySpec(key_byte);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		return keyFactory.generateSecret(dks);
	}
	
	
	private static boolean isInited = false;
	private static MessageDigest digest = null;
	
	public static synchronized String getMD5(String input) throws Exception {
		// please note that we dont use digest, because if we
		// cannot get digest, then the second time we have to call it
		// again, which will fail again
		if (isInited == false) {
			isInited = true;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (digest == null)
			return input;

		// now everything is ok, go ahead
		try {
			digest.update(input.getBytes("UTF-8"));
		} catch (java.io.UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		byte[] rawData = digest.digest();
		return byteArr2HexStr(rawData);
	}
	
	public static synchronized String getMD5_Base64(String input) {
		// please note that we dont use digest, because if we
		// cannot get digest, then the second time we have to call it
		// again, which will fail again
		if (isInited == false) {
			isInited = true;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (digest == null)
			return input;

		// now everything is ok, go ahead
		try {
			digest.update(input.getBytes("UTF-8"));
		} catch (java.io.UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		byte[] rawData = digest.digest();
		byte[] encoded = Base64.encode(rawData);
		String retValue = new String(encoded);
		return retValue;
	}
	
	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author LiGuoQing
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
	
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private static DecimalFormat numFormat = new DecimalFormat("0.##");
	
	
	public static String parseXmlNodeValue(String xmlString, String nodeName) {
		String result = "";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
		//InputStream is = 
		InputStream   inputStream   =   new ByteArrayInputStream(xmlString.getBytes());
		try{
			DocumentBuilder db = dbf.newDocumentBuilder(); 
			Document doc = db.parse(new InputSource(inputStream));
			//Node node = doc.get
			//NodeList nl = doc.getElementsByTagName(node); 
			Element e = doc.getDocumentElement();
			
			Node node = e.getElementsByTagName(nodeName).item(0);
			result = node.getFirstChild().getNodeValue();
			//System.out.println(node.getFirstChild().getNodeValue());
		}catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
		return result;
	}
	
	/**
	 * String xmlString = "<?xml version=\"1.0\" encoding=\"GB2312\"?><order version=\"2.0\"><heads version=\"2.0\"><head name=\"merchant_id\" value=\"7608\"/><head name=\"order_id\" value=\"31\"/><head name=\"result\" value=\"F\"/></heads><items version=\"2.0\"><item name=\"order_date\" value=\"20090420\"/><item name=\"amount\" value=\"50\"/><item name=\"currency\" value=\"RMB\"/><item name=\"pay_sq\" value=\"GW090420094409904138\"/><item name=\"pay_date\" value=\"2009-04-20 09:44:09\"/><item name=\"pc_id\" value=\"CMJFK00010001\"/><item name=\"verifystring\" value=\"4af43b57701c2e4f77f20f9de4647b82\"/></items></order>"
	 * parseXmlNodeAttribute(xmlString,"head", 2, "value") 返回F
	 * @param xmlString 要被解析的xml串
	 * @param nodeName 节点名称
	 * @param index  第几个节点
	 * @param attribute 要得到这个节点的哪个属性值
	 * @return
	 */
	public static String parseXmlNodeAttribute(String xmlString, String nodeName, int index, String attribute) {
		String result = "";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
		//InputStream is = 
		InputStream   inputStream   =   new ByteArrayInputStream(xmlString.getBytes());
		try{
			DocumentBuilder db = dbf.newDocumentBuilder(); 
			Document doc = db.parse(new InputSource(inputStream));
			//Node node = doc.get
			//NodeList nl = doc.getElementsByTagName(node); 
			Element e = doc.getDocumentElement();
			
			//NodeList nodeList = e.getElementsByTagName(nodeName);
			///e = doc.get
			Node node = e.getElementsByTagName(nodeName).item(index);
			
			//node.getAttributes()
			//result = node.getFirstChild().getNodeValue();
			NamedNodeMap map = node.getAttributes();
			
			Node n = map.getNamedItem(attribute);
			//result = map.get
			//map.get
			result = n.getNodeValue();
			//System.out.println(n.getNodeValue());
		}catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
		return result;
	}
	
	
	public static boolean verify(HttpServletRequest request)  throws Exception{
		
		//String card = request.getParameter("pay_cardno");
		//String cardpwd = request.getParameter("pay_cardpwd");
		int orderId = StringUtil.toInt(request.getParameter("order_id"));
		PayOrderBean orderBean = service.getOrder(" id = " + orderId);
		
		//service.getPayBeanById(id)
		PayBean payBean = service.getPayBeanById(orderBean.getChannelId());
		
		StringBuilder sb = new StringBuilder(256);
		sb.append("version_id=");
		sb.append(request.getParameter("version_id"));
		sb.append("&merchant_id=");
		sb.append(request.getParameter("merchant_id"));
		
		sb.append("&order_id=");
		sb.append(request.getParameter("order_id"));
		sb.append("&result=");
		sb.append(request.getParameter("result"));
		sb.append("&order_date=");
		sb.append(request.getParameter("order_date"));
		
		sb.append("&amount=");
		sb.append(request.getParameter("amount"));
		sb.append("&currency=");
		sb.append(request.getParameter("currency"));
		
		//sb.append("&cardnum1=");
		////sb.append(card);
		//sb.append("&cardnum2=");
		//sb.append(cardpwd);
		
		sb.append("&pay_sq=");
		sb.append(request.getParameter("pay_sq"));
		sb.append("&pay_date=");
		sb.append(request.getParameter("pay_date"));
		sb.append("&pc_id=");
		sb.append(request.getParameter("pc_id"));
		sb.append("&merchant_key=");
		sb.append(payBean.getMerchantKey());
		
		String dstr = sb.toString();
		dstr = getMD5(dstr).toLowerCase();
		//System.out.println("dstr=>>>"+dstr);
		String verifyString = request.getParameter("verifystring");
		//System.out.println("verifyString=>>>"+verifyString);
		return dstr.equals(verifyString);
	}
	
	
	public void checkOrder() throws Exception{
		int id = this.getParameterInt("id");
		if(id == 0) {
			return;
		}
		int uid = this.getLoginUser().getId();
		
		PayOrderBean order = service.getOrder(" id = " + id);
		
		if(order == null) {
			return;
		}
		if(uid != order.getUserId()) {
			return;
		}
		
		if(order.getType() != PayOrderBean.TYPE_DONE) {
			String xmlString = searchOrder(order);
			if(xmlString.startsWith("<?xml")) {
				String result = parseXmlNodeAttribute(xmlString, "head", 2, "value").toUpperCase();
				if(result.equals("Y")) {
					//支付成功 处理 <item name="amount" value="50"/>
					if(order.getType() != PayOrderBean.TYPE_DONE ) {
						String name = parseXmlNodeAttribute(xmlString, "item", 1, "name").toLowerCase();
						if(name.equals("amount")) {
							String amountStr = parseXmlNodeAttribute(xmlString, "item", 1, "value");
							float amount = StringUtil.toFloat(amountStr);
							ShopUtil.processCharge(id, order.getUserId(), amount, result);
							
							//ShopUtil.charge(order.getUserId(), amount);
							//NoticeAction.sendNotice(order.getUserId(), "充值成功获酷币"+amount+"g", NoticeBean.GENERAL_NOTICE, "/shop/info.jsp" );
							//PayAction.service.updateOrderResult2(id, PayOrderBean.TYPE_DONE, result);
						}
					}
				} else if(result.equals("F")) {
					service.updateOrderResult2(id, PayOrderBean.TYPE_DONE, PayOrderBean.FALSE);
					order.setType(PayOrderBean.TYPE_DONE);
					order.setResult2(PayOrderBean.FALSE);
					//支付失败
				} else if(result.equals("P")) {
					//正在处理中 不处理
					//支付成功 处理 <item name="amount" value="50"/>
	//				if(order.getType() != PayOrderBean.TYPE_DONE ) {
	//					String name = parseXmlNodeAttribute(xmlString, "item", 1, "name").toLowerCase();
	//					result = "Y";
	//					if(name.equals("amount")) {
	//						String amountStr = parseXmlNodeAttribute(xmlString, "item", 1, "value");
	//						float amount = StringUtil.toFloat(amountStr);
	//						ShopUtil.processCharge(id, order.getUserId(), amount, result);
	//						
	//						//ShopUtil.charge(order.getUserId(), amount);
	//						//NoticeAction.sendNotice(order.getUserId(), "充值成功获酷币"+amount+"g", NoticeBean.GENERAL_NOTICE, "/shop/info.jsp" );
	//						//PayAction.service.updateOrderResult2(id, PayOrderBean.TYPE_DONE, result);
	//					}
	//				}
				}
			} else {
				System.out.println("pay order (id=" + order.getId() + ") search error!");
			}
		}
		request.setAttribute("order", order);
		
	}
	
}
