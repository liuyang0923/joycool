package net.joycool.wap.action.user;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.ShortcutBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.app.AppAction;
import net.joycool.wap.spec.app.AppBean;
import net.joycool.wap.util.LinkBuffer;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/** 
 * @author macq
 * @explain：
 * @datetime:2007-6-19 9:12:12
 */
public class UserAction extends CustomAction{
	
	UserBean loginUser = null;
	
	IUserService service = ServiceFactory.createUserService();
	
	public UserAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}
	
	public void verify() {

	}
	
	public void shortcut() {
		HashMap map = UserInfoUtil.getShortcutMap();

		request.setAttribute("sMap", map);
	}
	
	public void shortcutList() {
		List sList = UserInfoUtil.getShortcutList();

		PagingBean page = new PagingBean(this, sList.size(), 20, "p");
		request.setAttribute("page", page);
		
		int start = page.getStartIndex();
		int end = page.getEndIndex();
		request.setAttribute("sList", sList.subList(start, end));
	}
	
	public void shortcutResult() {
		int id = getParameterInt("id");

		HashMap map = UserInfoUtil.getShortcutMap();
		if(map.get(Integer.valueOf(id)) != null) {	// 存在这个栏目
			UserSettingBean setting = loginUser.getUserSetting();
			String strId = String.valueOf(id);
			String shortcut = setting.getShortcut();
			String match = ',' + strId + ',';
			int pos = shortcut.indexOf(match);
			if(pos != -1) {		// 如果有，删除，如果没有，添加
				shortcut = shortcut.substring(0, pos) + shortcut.substring(pos + match.length() - 1);
				tip("del", "删除成功");
			} else {
				if(shortcut.length() < 100) {
					shortcut += strId + ",";
					tip("add", "添加成功");
					
				} else {
					tip(null, "无法添加更多链接");
					return;
				}
			}
			setting.setShortcut(shortcut);
			
			if(!isResult("failure")) {
				if (service.getUserSetting("user_id=" + loginUser.getId()) == null) {
					setting.setUserId(loginUser.getId());
					service.addUserSetting(setting);
				} else {
					service.updateUserSetting("shortcut='" + shortcut + "'", "user_id="
							+ loginUser.getId());
				}
				
				UserInfoUtil.shortcutCache.srm(Integer.valueOf(loginUser.getId()));
			}
		}

	}
	
	
	// add by leihy 08-12-18
	public void shortcut2Result() {
		int id = getParameterInt("id");

		HashMap map = UserInfoUtil.getShortcutMap();
		if(id<500 && map.get(Integer.valueOf(id)) != null || 
				AppAction.getApp(id - 500) != null) {	// 存在这个栏目
			UserSettingBean setting = loginUser.getUserSetting();
			String strId = String.valueOf(id);
			String shortcut2 = setting.getShortcut2();
			String match = ',' + strId + ',';
			int pos = shortcut2.indexOf(match);
			if(pos != -1) {		// 如果有，删除，如果没有，添加
				shortcut2 = shortcut2.substring(0, pos) + shortcut2.substring(pos + match.length() - 1);
				tip("del", "删除成功");
			} else {
				if(shortcut2.length() < 100) {
					shortcut2 += strId + ",";
					tip("add", "添加成功");
					
				} else {
					tip(null, "无法添加更多链接");
					return;
				}
			}
			setting.setShortcut2(shortcut2);
			
			if(!isResult("failure")) {
				if (service.getUserSetting("user_id=" + loginUser.getId()) == null) {
					setting.setUserId(loginUser.getId());
					service.addUserSetting(setting);
				} else {
					service.updateUserSetting("shortcut2='" + shortcut2 + "'", "user_id="
							+ loginUser.getId());
				}
				
				UserInfoUtil.shortcutCache.srm(Integer.valueOf(loginUser.getId()));
			}
		}

	}

	/**
	 * @return Returns the loginUser.
	 */
	public UserBean getLoginUser() {
		return loginUser;
	}
	
	public void setting() {
		if(loginUser == null)
			return;
		UserSettingBean setting = loginUser.getUserSetting();
		if(setting.getId() == 0 && service.getUserSetting("user_id=" + loginUser.getId()) == null) {
			UserSettingBean setting2 = new UserSettingBean();
			setting2.setUserId(loginUser.getId());
			service.addUserSetting(setting2);
		}
			
		int fo = getParameterIntS("fo");	// forum order change 论坛阅读顺序
		if(fo == 0 || fo == 1) {
			setting.setForumOrder(fo);

			service.updateUserSetting("forum_order=" + fo, "user_id="
						+ loginUser.getId());

		}
/*		
		int ha = getParameterIntS("ha");	// home allow change 家园保密设置
		if(ha >= 0) {
			if(ha > 2)
				ha = 0;
			String pw = request.getParameter("hpw");	// 家园密码
			if(pw == null)
				pw = "";
			setting.setHomeAllow(ha);
			setting.setHomePassword(pw);

			service.updateUserSetting("home_allow=" + ha + ",home_password='" + StringUtil.toSql(pw) + "'", "user_id="
						+ loginUser.getId());

		}*/
	}
	
	public void inviteTong() {
		UserStatusBean status = UserInfoUtil.getUserStatus(loginUser.getId());
		int tong = status.getTong();
		int id = getParameterInt("userId");
		if(tong > 0 && id > 0) {
			UserStatusBean status2 = UserInfoUtil.getUserStatus(id);
			if(status2.getTong() == 0 && isCooldown("inviteTong", 60 * 1000)) {
				NoticeAction.sendNotice(id, loginUser.getNickName() + "邀请你加入帮会", "", NoticeBean.GENERAL_NOTICE, "", "/tong/tongApply.jsp?tongId=" + tong);
				tip("success", "邀请成功！");
				return;
			}
		}
		tip("failure", "无法邀请！");
	}
	
	public void editNote() {
		if(isMethodGet()) {
			if(hasParam("d")) {		// 删除备注
				int toUserId = getParameterInt("tid");
				UserInfoUtil.removeUserNote(loginUser.getId(), toUserId);
				tip("failure", "备注已删除");
			} else {
				tip("edit");
			}
		} else {
			int toUserId = getParameterInt("tid");
			String shortNote = getParameterNoEnter("shortNote");
			if(shortNote.length() > 16)
				shortNote = shortNote.substring(0, 16);
			String note = getParameterNoCtrl("note");
			if(note != null && note.length() > 200)
				note = note.substring(0, 200);
			if(toUserId > 0 && shortNote != null && note != null) {
				UserInfoUtil.addUserNote(loginUser.getId(), toUserId, shortNote, note);
				tip("success", "编辑成功");
			} else {
				tip("failure", "编辑失败");
			}
		}
	}
	
	public void removeNote() {
		int toUserId = getParameterInt("tid");
		if(toUserId > 0)
			UserInfoUtil.removeUserNote(loginUser.getId(), toUserId);
	}
	
	public static int PUBLIC_NUMBER_PER_PAGE = 10;
	public void searchUser() {

		String nickName = request.getParameter("nickname");
		if(nickName != null)
			nickName = nickName.trim();

		int id = getParameterInt("id");
		int pageIndex = getParameterInt("p");
		if(pageIndex > 9)
			pageIndex = 9;
		if (nickName != null && nickName.length() != 0) {

			String condition = "nickname like '" + StringUtil.toSqlLike(nickName) + "%'";

			String prefixUrl = "searchUserResult.jsp?nickname=" + nickName;

			int start = pageIndex * PUBLIC_NUMBER_PER_PAGE;

			List userList = service.getUserList(condition + " limit " + start
					+ ", " + (PUBLIC_NUMBER_PER_PAGE+1));

			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("userList", userList);
		} else {
			if(id<=0){
				id=431;
			}
			String condition = "id = " + id ;
			String prefixUrl = "searchUserResult.jsp?id=" + id;

			int start = pageIndex * PUBLIC_NUMBER_PER_PAGE;

			List userList = service.getUserList(condition + " ORDER BY id limit " + start
						+ "," + (PUBLIC_NUMBER_PER_PAGE+1));
			
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("userList", userList);

		}
	}
	
	public static String getUserShortcut2String(UserBean user, HttpServletResponse response) {
		
		int	userId = user.getId();
		
		
		//synchronized(shortcutCache) {
			//s = (List) shortcutCache.get(key);
			//if(s == null) {
		LinkBuffer lb = new LinkBuffer(response);
				HashMap map = UserInfoUtil.getShortcutMap();
				//String shortcut = "1";
				String[] ss = user.getUserSetting().getShortcut2().split(",");
				
				for(int i = 0;i < ss.length;i++) {
					int ssid = StringUtil.toInt(ss[i]);
					if(ssid > 500) {	// 插件的链接
						AppBean bean = AppAction.getApp(ssid - 500);
						if(bean != null) {
							lb.appendLink(bean.getDirFull(), bean.getShortName());
						}
					} else if(ssid > 0) {
						ShortcutBean bean = (ShortcutBean)map.get(Integer.valueOf(ssid));
						if(bean != null) {
							lb.appendLink(bean.getUrl(), bean.getShortName());
						}
						
					}
				}			
				//shortcutCache.put(key, s);
			//}
		//}
//		LinkBuffer lb = new LinkBuffer(response);
//		//int col = 0;
//		Iterator iter = s.iterator();
//		while(iter.hasNext()) {
//			ShortcutBean bean = (ShortcutBean)iter.next();
//			System.out.println(bean.getUrl());
//			System.out.println(bean.getShortName());
//			lb.appendLink(bean.getUrl(), bean.getShortName());
//		}
		
		return lb.toString();
	}
}
