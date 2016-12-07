<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.infc.ITongService" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.bean.tong.TongFundBean" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%response.setHeader("Cache-Control", "no-cache");
if(!group.isFlag(1)) return;
int  tongId=StringUtil.toInt(request.getParameter("tongId"));
if(tongId<=0){
response.sendRedirect("index.jsp");
return;
}
TongBean tong = TongCacheUtil.getTong(tongId);
if(tong==null){
response.sendRedirect("index.jsp");
return;
}
String money1 = request.getParameter("money");

long money = StringUtil.toLong(money1);

int  operate=StringUtil.toInt(request.getParameter("operate"));
if(operate<0 || operate>100){
response.sendRedirect("index.jsp");
return;
}
// 更新帮会总基金
if(operate==0){
TongCacheUtil.updateTong("fund=fund-" + money, "id=" + tong.getId(),tong.getId());
TongFundBean tongFund = TongCacheUtil.getTongFund(tong.getId(),
					0, 0);// 判断用户是否捐献过基金
if (tongFund != null) {
	TongCacheUtil.updateTongFund("money=money+" + money,
			"tong_id=" + tong.getId() + " and user_id=0 and mark=0", tong
					.getId(), 0, 0, "id");
	OsCacheUtil.flushGroup(OsCacheUtil.TONG_FUND_BY_ID_CACHE_GROUP,tongFund.getId()+"");
} else {
	tongFund = new TongFundBean();
	tongFund.setTongId(tong.getId());
	tongFund.setCurrentTongId(0);
	tongFund.setUserId(0);
	tongFund.setMark(0);
	tongFund.setMoney(money);
	TongCacheUtil.addTongFund(tongFund, "money");// 增加帮会基金捐赠记录
}
LogUtil.logAdmin(adminUser.getName()+"给帮会"+tong.getId()+"扣除基金"+money);
}else if(operate==1){
TongCacheUtil.updateTong("fund=fund+" + money+ ",fund_total=fund_total+" + money, "id=" + tong.getId(),tong.getId());
TongFundBean tongFund = TongCacheUtil.getTongFund(tong.getId(),0, 1);// 判断用户是否捐献过基金
if (tongFund != null) {
	TongCacheUtil.updateTongFund("money=money+" + money,
			"tong_id=" + tong.getId() + " and user_id=0 and mark=1", tong
					.getId(), 0, 1, "money");
	OsCacheUtil.flushGroup(OsCacheUtil.TONG_FUND_BY_ID_CACHE_GROUP,tongFund.getId()+"");
} else {
	tongFund = new TongFundBean();
	tongFund.setTongId(tong.getId());
	tongFund.setCurrentTongId(0);
	tongFund.setUserId(0);
	tongFund.setMark(1);
	tongFund.setMoney(money);
	TongCacheUtil.addTongFund(tongFund, "money");// 增加帮会基金捐赠记录
}
LogUtil.logAdmin(adminUser.getName()+"给帮会"+tong.getId()+"增加基金"+money);
}else if(operate==2){	
	// 修改商店
	TongCacheUtil.updateTong("shop=shop+(" + money+")", "id=" + tong.getId(),tong.getId());
}else if(operate==3){	
	TongCacheUtil.updateTongEndure("now_endure=now_endure+("
							+ money+")", "id=" + tong.getId(), tong.getId(), (int)money, 0);
}else if(operate==4){	
	// 修改城墙上限
	TongCacheUtil.updateTongEndure("high_endure=high_endure+("
							+ money+")", "id=" + tong.getId(), tong.getId(), 1, (int)money);
}else if(operate==5){	
	// 修改名字
	TongCacheUtil.updateTong("title='" + StringUtil.toSql(request.getParameter("name")) + "'", "id="
						+ tong.getId(), tong.getId());
}else if(operate==6){	
	TongCacheUtil.updateTongDepot(tong.getId(),(int)money);
}else if(operate==7){	
	TongCacheUtil.updateTongHockshopDevelop("develop=develop+(" + (int)money+")",// 更新帮会当铺开发度
					"tong_id=" + tong.getId(), tong.getId(), (int)money);
}

%>
<script>
alert("操作成功!");
window.navigate("search.jsp?tongId="+<%=tong.getId()%>);
</script>
<%return;%>