<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,java.sql.*"%><%@ page import="net.joycool.wap.util.*, net.joycool.wap.util.db.*, net.joycool.wap.bean.*"%><%!
public static List getStringList(String sql, String dbName){
	List ret = null;
	
	DbOperation dbOp = new DbOperation();
	dbOp.init(dbName);
	
	ResultSet rs = null;
	try {
		rs = dbOp.executeQuery(sql);
		if (rs != null ) {
			ret = new ArrayList();
			while(rs.next())
			{
				ret.add(rs.getString(1));
			}
		}
	} catch (Exception e) {
		e.printStackTrace(System.out);
	} finally {
		dbOp.release();
	}

	return ret;
}

class TypeBean{
	int id = 0;
	String name = "";
	public TypeBean(int id, String name){
		this.id = id;
		this.name = name;
	}
}
%>
<%
Hashtable provinceList = new Hashtable();

provinceList.put("" + 0, new TypeBean(0, "未知"));
provinceList.put("" + 1, new TypeBean(1, "广东"));
provinceList.put("" + 2, new TypeBean(2, "江苏"));
provinceList.put("" + 3, new TypeBean(3, "浙江"));
provinceList.put("" + 4, new TypeBean(4, "北京"));
provinceList.put("" + 5, new TypeBean(5, "上海"));
provinceList.put("" + 6, new TypeBean(6, "辽宁"));
provinceList.put("" + 7, new TypeBean(7, "福建"));
provinceList.put("" + 8, new TypeBean(8, "天津"));
provinceList.put("" + 9, new TypeBean(9, "山东"));
provinceList.put("" + 10, new TypeBean(10, "湖北"));
provinceList.put("" + 11, new TypeBean(11, "甘肃"));
provinceList.put("" + 12, new TypeBean(12, "河北"));
provinceList.put("" + 13, new TypeBean(13, "重庆"));
provinceList.put("" + 14, new TypeBean(14, "四川"));
provinceList.put("" + 15, new TypeBean(15, "陕西"));
provinceList.put("" + 16, new TypeBean(16, "安徽"));
provinceList.put("" + 17, new TypeBean(17, "海南"));
provinceList.put("" + 18, new TypeBean(18, "广西"));
provinceList.put("" + 19, new TypeBean(19, "江西"));
provinceList.put("" + 20, new TypeBean(20, "山西"));
provinceList.put("" + 21, new TypeBean(21, "湖南"));
provinceList.put("" + 22, new TypeBean(22, "河南"));
provinceList.put("" + 23, new TypeBean(23, "青海"));
provinceList.put("" + 24, new TypeBean(24, "贵州"));
provinceList.put("" + 25, new TypeBean(25, "宁夏"));
provinceList.put("" + 26, new TypeBean(26, "云南"));
provinceList.put("" + 27, new TypeBean(27, "吉林"));
provinceList.put("" + 28, new TypeBean(28, "内蒙古"));
provinceList.put("" + 29, new TypeBean(29, "新疆"));
provinceList.put("" + 30, new TypeBean(30, "黑龙江"));
provinceList.put("" + 31, new TypeBean(31, "西藏")); 
%>
<%
Hashtable countHash = new Hashtable();
Hashtable areaHash = new Hashtable();
Hashtable provinceHash = new Hashtable();

String sql = "select mobile from user_info where mobile is not null";
List mobileList = getStringList(sql, Constants.DBShortName);

for(int i=0;i<mobileList.size();i++){
	String mobile = (String)mobileList.get(i);
	if(mobile==null)continue;
	if(mobile.startsWith("86")){
		mobile = mobile.substring(2);
	}
	if(mobile.length()!=11)continue;
	String cacheKey = mobile.substring(0,7);
	
	AreaBean areaBean = (AreaBean)areaHash.get(cacheKey);
	if(areaBean==null){
		areaBean = AreaUtil.getAreaByMobile(mobile);
		if(areaBean!=null){
			String areaName = areaBean.getCityname();
			Integer count = (Integer)countHash.get(areaName);
			if(count==null){
				count = new Integer(0);
			}
			count = new Integer(count.intValue()+1);
			countHash.put(areaName, count);
			
			//省份
			if(provinceHash.get(areaName)==null){
			String provinceName = "未知";
			TypeBean typeBean = (TypeBean)provinceList.get("" + areaBean.getPlaceno());
			if(typeBean!=null)
			{
				provinceName = typeBean.name;
			}
			provinceHash.put(areaName, provinceName);
			}
			
			//加入缓存
			areaHash.put(cacheKey, areaBean);
		}
		else{
			String areaName = "未知地区";
			Integer count = (Integer)countHash.get(areaName);
			if(count==null){
				count = new Integer(0);
			}
			count = new Integer(count.intValue()+1);
			countHash.put(areaName, count);
			
			//加入缓存
			areaBean = new AreaBean();
			areaBean.setCityname(areaName);
			areaBean.setCityno(0);
			areaBean.setPlaceno(0); 
			areaHash.put(cacheKey, areaBean);
			
			//省份
			String provinceName = "未知";
			if(provinceHash.get(areaName)==null){
			provinceHash.put(areaName, provinceName);
			}
		}
	}
	else{
		String areaName = areaBean.getCityname();
		Integer count = (Integer)countHash.get(areaName);
		if(count==null){
			count = new Integer(0);
		}
		count = new Integer(count.intValue()+1);
		countHash.put(areaName, count);
		
		//省份
		if(provinceHash.get(areaName)==null){
		String provinceName = "未知";
		TypeBean typeBean = (TypeBean)provinceList.get("" + areaBean.getPlaceno());
		if(typeBean!=null)
		{
			provinceName = typeBean.name;
		}
		provinceHash.put(areaName, provinceName);
		}
	}
}

%>
<table align=left width=80% border=1>
  <tr>
    <td>省份</td>
    <td>城市</td>
    <td>用户数</td>
  </tr>
<%
Iterator iter = countHash.keySet().iterator();
while(iter.hasNext()){
	String areaName = (String)iter.next();
	Integer count = (Integer)countHash.get(areaName);
	if(count==null)continue;
	
	String province = (String)provinceHash.get(areaName);
%>
  <tr>
    <td><%= province %></td>
    <td><%= areaName %></td>
    <td><%= count.intValue() %></td>
  </tr>
<%	
}
%>
</table>