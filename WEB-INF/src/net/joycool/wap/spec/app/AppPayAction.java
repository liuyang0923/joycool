package net.joycool.wap.spec.app;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import other.util.Http;
import other.util.HttpResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.PayUtil;
import net.joycool.wap.spec.shop.ShopService;
import net.joycool.wap.spec.shop.ShopUtil;
import net.joycool.wap.spec.shop.UserInfoBean;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;


public class AppPayAction extends CustomAction {

	public static byte[] lock = new byte[0];
	UserBean loginUser;

	public AppPayAction() {
	}

	public AppPayAction(HttpServletRequest request) {
		super(request);

	}
	public static ShopService shopService = ShopService.getInstance();
	public void pay() {
		int userId = getLoginUser().getId();
		if(!isMethodGet()){		// 用户提交确认
			AppPayBean pay = (AppPayBean)session.getAttribute("appPay");
			if(pay == null) {
				tip("tip", "支付已结束");
				return;
			}
			request.setAttribute("appPay", pay);
			session.removeAttribute("appPay");
			String conf = request.getParameter("conf");	// 确认判断，提交用户的sessionid
			if(session.getId().equals(conf)) {
				synchronized(lock) {
					if(pay.getItem() == 0){	// 酷币支付
						float price = (float)pay.getCount() / 100;
						UserInfoBean bean = shopService.getUserInfo(userId);
						if(bean == null || !ShopUtil.hasEnoughMoney(price, bean.getGold())) {
							tip("tip", "支付失败,酷币不足");
							return;
						}
						bean.decreaseGold(price);	//add new
						ShopUtil.updateUserGold(userId, bean.getGold(), price, 2, pay.getAppId(), 0);
					} else {		// 行囊支付
						int userBagId = UserBagCacheUtil.getUserBagById(pay.getItem(), userId);
						if(userBagId <= 0 || !UserBagCacheUtil.UseUserBagCacheById(userId,
								userBagId)) {
							tip("tip", "支付失败,物品不足");
							return;
						}
					}
					AppBean appBean = AppAction.getApp(pay.getAppId());
					if(appBean == null) {
						tip("tip", "支付失败");
						return;
					}
					
					String ret = "Error";
					try {
						addApp(pay);
						
						StringBuilder sb = new StringBuilder(256);
						sb.append("pid=");
						sb.append(pay.getProductId());

						sb.append("&item=");
						sb.append(pay.getItem());
						sb.append("&count=");
						sb.append(pay.getCount());
						sb.append("&id=");
						sb.append(pay.getId());
						sb.append("&userId=");
						sb.append(pay.getUserId());
						
						String temp = sb.toString();
						sb.append("&verify=");
						try {
							sb.append(PayUtil.desEncryptMd5(temp, appBean.getSecretKey()));
						} catch (Exception e) {
							tip("tip", "支付失败,数据传输故障,请联系管理员!");
							return;
						}
						
						
						String post = sb.toString();
						HttpResponse res = Http.doRequest(appBean.getUrl() + "enter/notify.jsp", "post", post, "utf8");
						ret = new String(res.getContent(), "utf8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if(ret == null)
						ret = "null";
					SqlUtil.executeUpdate("update app_pay set ret='" + StringUtil.toSql(ret) + "' where id=" + pay.getId(), 5);
					if(ret.equals("Y"))
						tip("tip", "购买成功!");
					else
						tip("tip", "支付失败,请联系管理员!");
				}
			}
		} else {
			int count = getParameterInt("count");
			int productId = getParameterInt("pid");
			int item = getParameterInt("item");
			int appId = getParameterInt("app");
			String verify = request.getParameter("verify");
			if(appId == 0) {
				tip("tip", "支付已结束!");
				return;
			}
			
			String info = getParameterString("info");
			AppPayBean pay = new AppPayBean();
			pay.setProductId(productId);
			pay.setAppId(appId);
			pay.setItem(item);
			pay.setAppId(appId);
			pay.setCount(count);
			pay.setUserId(userId);
			pay.setInfo(info);
			
			StringBuilder sb = new StringBuilder(256);
			sb.append("pid=");
			sb.append(pay.getProductId());
			sb.append("&app=");
			sb.append(pay.getAppId());

			sb.append("&item=");
			sb.append(pay.getItem());
			sb.append("&count=");
			sb.append(pay.getCount());
			
			String temp = sb.toString();
			AppBean appBean = AppAction.getApp(pay.getAppId());
			if(appBean == null) {
				tip("tip", "支付失败");
				return;
			}
			try {
				temp = PayUtil.desEncryptMd5(temp, appBean.getSecretKey());
			} catch (Exception e) {
			}
			if(!temp.equals(verify)){	// 校验密钥
				tip("tip", "支付失败!");
				return;
			}
			
			session.setAttribute("appPay", pay);
			request.setAttribute("appPay", pay);
		}
	}

	public static boolean addApp(AppPayBean pay) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO app_pay(product_id,user_id,app_id,item,`count`,info,create_time) values(?,?,?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, pay.getProductId());
			pstmt.setInt(2, pay.getUserId());
			pstmt.setInt(3, pay.getAppId());
			pstmt.setInt(4, pay.getItem());
			pstmt.setInt(5, pay.getCount());
			pstmt.setString(6, pay.getInfo());
			pstmt.execute();
			pay.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
		
	}
	
}