package net.joycool.wap.spec.shop;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import net.joycool.wap.service.impl.DummyServiceImpl;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;

public class ShopAction extends CustomAction {	
	
	public static String SHOP_PHOTO_PATH = Constants.RESOURCE_ROOT_PATH + "shop/";// D:\\eclipse\\workspace\\mcoolwap\\joycool-rep\\shop\\
	public static ShopService shopService = ShopService.getInstance();
	public static DummyServiceImpl dummyService = new DummyServiceImpl();
	public ShopAction() {
	}

	public ShopAction(HttpServletRequest request) {
		super(request);
	}

	public ShopAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void buy(){
		
		int id = this.getParameterInt("id");
		if(id == 0) {
			tip("no");
			//request.setAttribute("msg", "没有该商品");
			session.removeAttribute(ShopUtil.SESSION_SHOP_BUY);
			return;
		}
		int uid = this.getLoginUser().getId();
		
		//是否是赠送给别人
		int userId = this.getParameterInt("userId");
		UserBean toUser = null;
		UserBean loginUser = getLoginUser();
		if(userId > 0) {
			toUser = UserInfoUtil.getUser(userId);
			if(toUser == null) {
				tip("lack");
				session.removeAttribute(ShopUtil.SESSION_SHOP_BUY);
				return;
			}
		}
		UserInfoBean bean = shopService.getUserInfo(uid);
		
		if(bean == null) {
			shopService.addUserInfo(uid);
			tip("lack");
			session.removeAttribute(ShopUtil.SESSION_SHOP_BUY);
			//request.setAttribute("msg", "您的金币不足导致您购买失败,请您<a href=\"/pay/pay.jsp\">充值</a>后再来购买");
			return;
		}
		
		
		ItemBean itemBean = shopService.getShopItemById(id);
		
		if(itemBean.getHidden() == 0) {
			tip("nobuy");
			return;
		}
		
		if(itemBean.getMax() > -1) {
			if(itemBean.getOdd() <= 0) {
				tip("nostock");
				//request.setAttribute("msg", "该物品已经卖完,请下次再来");
				session.removeAttribute(ShopUtil.SESSION_SHOP_BUY);
				return;
			}
		}
		
		float price = itemBean.getPrice();
		
		if(itemBean.getType() == 1)		//装饰才给打折
			price = ShopUtil.discount(bean.getConsumeCount(), itemBean.getPrice()); //add new
		
		//if(!ShopUtil.hasEnoughMoney(itemBean.getPrice(), bean.getGold())) {
		if(!ShopUtil.hasEnoughMoney(price, bean.getGold())) {
			tip("lack");
			session.removeAttribute(ShopUtil.SESSION_SHOP_BUY);
			return;
		}
		DummyProductBean productBean = UserBagCacheUtil.getItem(itemBean.getItemId());
		
		
		//if(itemBean.getType() == ShopUtil.TYPE_GIFT) {
			
			
		//	if(shopService.isContainGift(itemBean.getItemId(), uid)) {
		//		shopService.updateUserGiftCount(itemBean.getItemId(), uid, 1);
		//	} else {
		//		UserGiftBean gift = new UserGiftBean();
		//		gift.setCount(1);
		//		gift.setItemId(itemBean.getItemId());
		//		gift.setTime(new Date());
		//		gift.setUid(uid);
		//		shopService.addUserGift(gift);
		//	}
		//} else {
			//UserBagBean userBag = new UserBagBean();
			//if(userId > 0) {
			//	userBag.setUserId(userId);
			//} else {
			//	userBag.setUserId(uid);
			//}
			//userBag.setProductId(productBean.getId());
			//userBag.setTypeId(productBean.getDummyId());
			//userBag.setTime(productBean.getTime());
			//userBag.setMark(0);
			UserBagCacheUtil.addUserBagCache(userId>0?userId:uid, productBean.getId(), itemBean.getTimes(), itemBean.getDue());
			//UserBagCacheUtil.addUserBagCache(userBag);
			
			if(userId > 0) {
				//通知到我收到赠送的物品列表
				NoticeAction.sendNotice(userId, loginUser.getNickName() + "送给你" + productBean.getName(), NoticeBean.GENERAL_NOTICE, "/shop/receiveRecord.jsp" );
				request.setAttribute("send", "send");
				if(itemBean.getType() == 1)	// 只有装饰品才显示到好友动态
					ActionTrend.addTrend(loginUser.getId(), 0, "%1给%2买了" + itemBean.getName(), loginUser.getNickName(), userId, toUser.getNickName());
			}
		//}
		
		//bean.decreaseGold(itemBean.getPrice());	//add new
		bean.decreaseGold(price);	//add new
		ShopUtil.updateUserGold(uid, bean.getGold(), price, ShopUtil.BUY, id, userId);
		
		if(itemBean.getMax() < 0) {
			shopService.updateItemCount(itemBean.getId(), 1);
		} else {
			shopService.updateItemCountAndOdd(itemBean.getId(), 1, -1);
		}
		
		request.setAttribute("name", productBean.getName());
		tip("success");
		session.removeAttribute(ShopUtil.SESSION_SHOP_BUY);
	}
	
	
	public void store(){
		int id = this.getParameterInt("id");
		if(id == 0) {
			tip("no");
			//request.setAttribute("msg", "没有该商品");
			return;
		}
		int uid = this.getLoginUser().getId();
		UserInfoBean userInfo = shopService.getUserInfo(uid);
		
		if(userInfo == null) {
			shopService.addUserInfo(uid);
		}
		
		FavoriteBean bean = new FavoriteBean(uid, id);
		
		ItemBean itemBean = shopService.getShopItemById(id);
		DummyProductBean productBean = UserBagCacheUtil.getItem(itemBean.getItemId());
		if(shopService.isContainFavorite(bean.getItemId(), uid)) {
			tip("a");
			request.setAttribute("name", productBean.getName());
			//request.setAttribute("msg", "您的收藏夹中已经添加了【"+productBean.getName()+"】,无法重复添加同一个商品");
			return;
		}
		
		if(shopService.addFavorite(bean)){
			shopService.updateUserFavoriteCount(uid,1);
			tip("b");
			request.setAttribute("name", productBean.getName());
			//request.setAttribute("msg", "您已经将【"+productBean.getName()+"】添加到您的收藏夹中");
		}
	}
	
	public void deleteFavorite(){
		int id = this.getParameterInt("id");
		
		shopService.deleteFavorite(id, this.getLoginUser().getId());
		
		tip("success");
		//request.setAttribute("msg", "删除成功");
	}
	
	
	public void sendGift(){
		int itemId = this.getParameterInt("id");
		int toUid = this.getParameterInt("uid");
		int uid = this.getLoginUser().getId();
		if(shopService.isContainGift(itemId, uid)) {
			//shopService.updateUserGiftCount(itemId, uid, count)
		} else {
			return;
		}
	}
	
	public void doAdmin() throws Exception{
		String a = request.getParameter("a");

		if(a == null) {
			return;
		} else if(a.equals("a")) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
				
			List items = upload.parseRequest(request);
				
			Iterator it = items.iterator();
				
			HashMap params = new HashMap();
				
			while(it.hasNext()) {
				FileItem item = (FileItem) it.next();
					
				if(item.isFormField()) {
					params.put(item.getFieldName(), item.getString());
				} else {
					
					String name = item.getFieldName();
					String name2 = item.getName();
					String fileExt = StringUtil.convertNull(
								FileUtil.getFileExt(name2)).toLowerCase();
						
					String filePath = SHOP_PHOTO_PATH;// "E:\\eclipse\\workspace\\joycool-portal\\img/home";
					String fileName = FileUtil.getUniqueFileName() + "." + fileExt;
					
					if(name2 != null && name2.length() > 0) {
						int id = StringUtil.toInt((String)params.get("id"));
						
						if(id > 0) {
							//删除图片 start
							ItemBean itemBean = ShopAction.shopService.getShopItemById(id);
							if(itemBean.getPhotoUrl() != null) {
								String path = new String(SHOP_PHOTO_PATH + itemBean.getPhotoUrl());
								File file = new File(path);
								file.delete();
							}
							//删除图片 end
						}
						
						
						File fullFile=new File(fileName);
				        File savedFile=new File(filePath,fullFile.getName());
				        item.write(savedFile);
				        
				        params.put(name, fileName);
					}
			        
				}	
			}
			
			int id = StringUtil.toInt((String)params.get("id"));
			
			if(id > 0) {	
				int itemId = StringUtil.toInt((String)params.get("item_id"));//this.getParameterInt("item_id");
				String photoUrl = (String)params.get("photo");
				float price = StringUtil.toFloat((String)params.get("price"));//this.getParameterFloat("price");
				if(price < 1) {
					request.setAttribute("msg", "价格不能小于1,修改失败");
					return;
				}
				int max = StringUtil.toInt((String)params.get("max"));//this.getParameterInt("max");
				int odd = StringUtil.toInt((String)params.get("odd"));//this.getParameterInt("odd");
				
				if(max >= 0) {
					if(odd > max) {
						request.setAttribute("msg", "剩余不能大于库存,修改失败");
						return;
					}
				} else {
					odd = max;
				}
				
				
				int type = StringUtil.toInt((String)params.get("type"));//this.getParameterInt("type");
				String desc = new String(((String)params.get("desc")).getBytes("iso-8859-1"), "utf-8");//this.getParameterString("desc");
				int seq = StringUtil.toInt((String)params.get("seq"));
				String name = new String(((String)params.get("name")).getBytes("iso-8859-1"), "utf-8");
				int times = StringUtil.toInt((String)params.get("times"));
				int due = StringUtil.toInt((String)params.get("due"));
				
				ItemBean itemBean = new ItemBean();
				itemBean.setId(id);
				itemBean.setName(name);
				itemBean.setItemId(itemId);
				itemBean.setPrice(price);
				itemBean.setMax(max);
				itemBean.setOdd(odd);
				itemBean.setType(type);
				itemBean.setDesc(desc);
				itemBean.setSeq(seq);
				itemBean.setPhotoUrl(photoUrl==null?"":photoUrl);
				itemBean.setTimes(times);
				itemBean.setDue(due);
				
				ShopAction.shopService.updateItem(itemBean);
				
				request.setAttribute("msg", "修改成功");
			} else {
				int itemId = StringUtil.toInt((String)params.get("item_id"));//this.getParameterInt("item_id");
				
//				ItemBean item = ShopAction.shopService.getShopItemByItemId(itemId);
//				
//				if(item != null) {
//					request.setAttribute("msg", "添加失败，该物品已经添加");
//					return;
//				}
				float price = StringUtil.toFloat((String)params.get("price"));//this.getParameterFloat("price");
				if(price < 1) {
					request.setAttribute("msg", "价格不能小于1,增加失败");
					return;
					
				}
				int max = StringUtil.toInt((String)params.get("max"));//this.getParameterInt("max");
				int odd = StringUtil.toInt((String)params.get("odd"));//this.getParameterInt("odd");
				
				if(max >= 0) {
					if(odd > max) {
						request.setAttribute("msg", "剩余不能大于库存,增加失败");
						return;
					}
				} else {
					odd = max;
				}
				
				int type = StringUtil.toInt((String)params.get("type"));//this.getParameterInt("type");
				String desc = new String(((String)params.get("desc")).getBytes("iso-8859-1"), "utf-8");//this.getParameterString("desc");
				String name = new String(((String)params.get("name")).getBytes("iso-8859-1"), "utf-8");
				int times = StringUtil.toInt((String)params.get("times"));
				int due = StringUtil.toInt((String)params.get("due"));
				
				String photoUrl = (String)params.get("photo");
				ItemBean itemBean = new ItemBean();
				itemBean.setItemId(itemId);
				itemBean.setPrice(price);
				itemBean.setName(name);
				itemBean.setMax(max);
				itemBean.setOdd(odd);
				itemBean.setType(type);
				itemBean.setDesc(desc);
				itemBean.setPhotoUrl(photoUrl==null?"":photoUrl);
				itemBean.setTimes(times);
				itemBean.setDue(due);
				
				shopService.addItem(itemBean);
				
				request.setAttribute("msg", "增加成功");
			}
			
			
		} else if(a.equals("u")) {
		} else if(a.equals("s")) {
			
		} else if(a.equals("d")) {
			
		}
	}
	
}
