package net.joycool.wap.spec.pay;

public class PayBean {

	public int id;
	public String name;
	public String merchantId;// = "7608";
	public String versionId;// = "2.00";
	public String currency;// = "RMB";
	public String pmId;// = "LTJFK";//"CMJFK";
	public String pcId;// = "LTJFK00020000";//"CMJFK00010001";
	public String merchantKey;// = "123456789";//"eo7ogrrcktifoftsvzsh3io9s945qmnyva29v9qf1mkyjqx0vozjsvt2kbf8jihnubukn6a61wklwtr6a385150mc2pkekvc66p2hiu7yvq00biqrp7bxn7hwzrasgow";//"123456789";
	public String notifyURL;// = "http://219.238.200.40/z/tmp/test.jsp";
	public String searchURL;
	public String submitURL;//
	//public static String testServer = "http://114.255.7.208/pgworder/orderdirect.do";测试提交地址
	//public static String server = "http://pay.19pay.com/pgworder/orderdirect.do";正式提交地址
	
	
	public PayBean(String merchantId, String versionId, String currency,
			String pmId, String pcId, String merchantKey, String notifyURL) {
		this.merchantId = merchantId;
		this.versionId = versionId;
		this.currency = currency;
		this.pmId = pmId;
		this.pcId = pcId;
		this.merchantKey = merchantKey;
		this.notifyURL = notifyURL;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPmId() {
		return pmId;
	}
	public void setPmId(String pmId) {
		this.pmId = pmId;
	}
	public String getPcId() {
		return pcId;
	}
	public void setPcId(String pcId) {
		this.pcId = pcId;
	}
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	public String getNotifyURL() {
		return notifyURL;
	}
	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSearchURL() {
		return searchURL;
	}
	public void setSearchURL(String searchURL) {
		this.searchURL = searchURL;
	}
	
	public PayBean() {
	}
	public String getSubmitURL() {
		return submitURL;
	}
	public void setSubmitURL(String submitURL) {
		this.submitURL = submitURL;
	}
}
