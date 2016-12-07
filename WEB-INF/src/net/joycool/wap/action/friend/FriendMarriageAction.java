/*
 * Created on 2006-10-27
 *
 */
package net.joycool.wap.action.friend;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jc.family.FamilyAction;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.friend.FriendMarriageBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IFriendService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.NoticeUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class FriendMarriageAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		HttpSession session = request.getSession();
		if(session.getAttribute("engage")==null){
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		session.removeAttribute("engage");
		
		IUserService service = ServiceFactory.createUserService();
		IFriendService friendService = ServiceFactory.createFriendService();
		UserBean loginUser = null;
		String tip = null;
		/**
		 * 取得参数
		 */

		String result = null;
		
		// 喜糖价格数量不能为负
		if (request.getParameter("marriageId") == null) {
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		int marriageId = StringUtil.toInt(request.getParameter("marriageId"));
		if (request.getParameter("content") != null) {
			int candyNum = StringUtil.toInt(request.getParameter("number"));
			int candyPrice = StringUtil.toInt(request.getParameter("price"));
			int marriageform = StringUtil.toInt(request.getParameter("marriageform"));
			String pledge = request.getParameter("content");
			if (pledge == null)
				pledge = " ";
			String marriageDate = request.getParameter("marriagedate");
			String marriageTime = request.getParameter("marriagetime");
			
			marriageDate = marriageDate.trim();
			marriageTime = marriageTime.trim();
			Date date = DateUtil.parseDate(marriageDate, "yyyy-MM-dd");

			int hour = StringUtil.toInt(marriageTime);
			if (hour < 0)
				hour = 0;

			date = DateUtil.rollHour(date, hour);
			marriageDate = DateUtil.formatDate(date, "yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			String nowDate = DateUtil.formatDate(now, "yyyy-MM-dd HH:mm:ss");
			loginUser = getLoginUser(request);
			UserStatusBean user = (UserStatusBean) UserInfoUtil
					.getUserStatus(loginUser.getId());

			/**
			 * 检查参数
			 */
			if (nowDate.compareTo(marriageDate) > 0) {
				result = "failure";
				tip = " 婚礼时间必须大于当前时间,如果从答应求婚到定婚期超过7天则视为不举行婚礼！";
				request.setAttribute("tip", tip);

			}

			else if (candyPrice <= 0 || candyNum <= 0) {
				result = "failure";
				tip = " 喜糖价格和数量不能为负";
				request.setAttribute("tip", tip);

			} else if ((candyPrice > 20000000) || candyNum > 20000000) {
				result = "failure";
				tip = " 喜糖价格和数量不能超过20000000";
				request.setAttribute("tip", tip);

			}else if (user.getGamePoint() < candyNum * candyPrice) {
				result = "failure";
				tip = " 新事新办，没那么多钱婚礼就简朴一点吧。";
				request.setAttribute("tip", tip);

			}else if ((long)candyNum * candyPrice>=2000000000) {
				result = "failure";
				tip = " 新事新办，没那么多钱婚礼就简朴一点吧。";
				request.setAttribute("tip", tip);

			} else {
				UserInfoUtil.updateUserStatus("game_point=game_point-"
						+ candyNum * candyPrice,
						"user_id=" + loginUser.getId(), loginUser.getId(),
						UserCashAction.OTHERS, "买喜糖扣钱" + candyNum * candyPrice);
				friendService.updateFriendMarriage("candy_num=" + candyNum
						+ ",marriage_form="+marriageform+",candy_price=" + candyPrice + ",candy_remain="
						+ candyNum + ",pledge= '" + StringUtil.toSql(pledge)
						+ "',marriage_datetime='" + marriageDate + "'", "id="
						+ marriageId);

				tip = " 您已经成功定下婚期，恭喜您啦";
				OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP, "query");
				OsCacheUtil
						.flushGroup(
								OsCacheUtil.MARRIAGE_TO_GROUP,
								"select id from jc_friend_marriage where mark=0 and marriage_datetime>now() order by marriage_datetime");
				OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP, "id="
						+ marriageId);
				// 发结婚请求
				
				//
				
				FriendMarriageBean friendMarriage = friendService
						.getFriendMarriage("id=" + marriageId);
				int toId = friendMarriage.getToId();
				int fromId = friendMarriage.getFromId();
				UserBean toUser = UserInfoUtil.getUser(toId);
				UserBean fromUser = UserInfoUtil.getUser(fromId);
				ArrayList vec1 = (ArrayList) UserInfoUtil.getUserFriends(toId);
				ArrayList vec2 = (ArrayList) UserInfoUtil.getUserFriends(fromId);
				
				
				// 给家族的家人们发通知
				int fmId1 = FamilyAction.getFmId(toId);
				int fmId2 = FamilyAction.getFmId(fromId);
				List fm1List = null;
				List fm2List = null;
				int totalCount = 0;
				
				if(fmId1 > 0) {
					fm1List = FamilyAction.service.selectUserIdList(fmId1,"");	//最多取1000个成员
					totalCount += fm1List.size();
				}
				
				if(fmId2 > 0 && fmId2 != fmId1) {
					fm2List = FamilyAction.service.selectUserIdList(fmId2,"");
					totalCount += fm2List.size();
				}
				
				HashSet familyMember = new HashSet(totalCount);
				if(fm1List != null) {
					familyMember.addAll(fm1List);
					familyMember.remove(new Integer(fromId));
				}
				if(fm2List != null) {
					familyMember.addAll(fm2List);
					familyMember.remove(new Integer(toId));
				}
				
				
				int length1 = 0;
				int length2 = 0;
				if (vec1 != null && vec2 != null
						&& (vec1.size() + vec2.size()) < 50) {
					length1 = vec1.size();
					length2 = vec2.size();
				} else if (vec1 != null && vec2 != null	&& (vec1.size() + vec2.size()) > 50) {
					if (vec1.size() < 25) {
						length1 = vec1.size();
						length2 = 50 - vec1.size();
					} else if (vec2.size() < 25) {
						length2 = vec2.size();
						length1 = 50 - vec2.size();
					} else {
						length1 = 25;
						length2 = 15;

					}
				}
				else if(vec1==null){
					if(vec2!=null && vec2.size()>50)
						length2=50;
					else if(vec2!=null && vec2.size()<50)
						
						length2=vec2.size();
					
				}
				else if(vec2==null){
					if(vec1!=null && vec1.size()>50)
						length1=50;
					else if(vec1!=null && vec1.size()<50)
						
						length1=vec1.size();
				}

				for (int i = 0; i < length1; i++) {
					String friendId = (String) vec1.get(i);
					int friendId2 = Integer.parseInt(friendId);
					if (friendId2 != toId && friendId2 != fromId) {
						familyMember.remove(new Integer(friendId2));
						NoticeBean notice = new NoticeBean();
						notice.setUserId(Integer.parseInt(friendId));
						notice.setTitle("您的好友" + toUser.getNickName() + "和"
								+ fromUser.getNickName() + "邀请您于"
								+ marriageDate + "参加他们的结婚庆典，敬请光临！");
						notice.setContent("");
						notice.setHideUrl("");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice
								.setLink("/friend/redbag.jsp?marriageId="
										+ marriageId);
						NoticeUtil.getNoticeService().addNotice(notice);
					}
				}
				for (int i = 0; i < length2; i++) {
					String friendId = (String) vec2.get(i);
					int friendId2 = Integer.parseInt(friendId);
					if (friendId2 != toId && friendId2 != fromId) {
						familyMember.remove(new Integer(friendId2));
						NoticeBean notice = new NoticeBean();
						notice.setUserId(Integer.parseInt(friendId));
						notice.setTitle("您的好友" + toUser.getNickName() + "和"
								+ fromUser.getNickName() + "邀请您于"
								+ marriageDate + "参加他们的结婚庆典，敬请光临！");
						notice.setContent("");
						notice.setHideUrl("");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice
								.setLink("/friend/redbag.jsp?marriageId="
										+ marriageId);
						NoticeUtil.getNoticeService().addNotice(notice);
					}

				}
				
				Iterator iter = familyMember.iterator();
				while(iter.hasNext()) {
					Integer iid = (Integer)iter.next();
					NoticeBean notice = new NoticeBean();
					notice.setUserId(iid.intValue());
					notice.setTitle("您的家人" + toUser.getNickName() + "和"
							+ fromUser.getNickName() + "邀请您于"
							+ marriageDate + "参加他们的结婚庆典，敬请光临！");
					notice.setContent("");
					notice.setHideUrl("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice
							.setLink("/friend/redbag.jsp?marriageId="
									+ marriageId);
					NoticeUtil.getNoticeService().addNotice(notice);
				}
				

				request.setAttribute("tip", tip);
			}
		} else if (request.getParameter("number") == null) {
			String marriageDate = request.getParameter("marriagedate");
			String marriageTime = request.getParameter("marriagetime");
			marriageDate = marriageDate.trim();
			marriageTime = marriageTime.trim();
			Date date = DateUtil.parseDate(marriageDate, "yyyy-MM-dd");

			int hour = StringUtil.toInt(marriageTime);
			if (hour < 0)
				hour = 0;

			date = DateUtil.rollHour(date, hour);
			marriageDate = DateUtil.formatDate(date, "yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			String nowDate = DateUtil.formatDate(now, "yyyy-MM-dd HH:mm:ss");
			loginUser = getLoginUser(request);
			UserStatusBean user = (UserStatusBean) UserInfoUtil
					.getUserStatus(loginUser.getId());

			/**
			 * 检查参数
			 */
			if (nowDate.compareTo(marriageDate) > 0) {
				result = "failure";
				tip = " 婚礼时间必须大于当前时间,如果从答应求婚到定婚期超过7天则视为不举行婚礼！";
				request.setAttribute("tip", tip);

			}

			else {
				int marriageform = StringUtil.toInt(request.getParameter("marriageform"));
				tip = " 您已经成功更改婚期和婚礼方式，恭喜您啦";
				OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP,"id=" + marriageId);
				OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP,"select id from jc_friend_marriage where mark=0 and marriage_datetime>now() order by marriage_datetime");
				OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP,"query");
				friendService.updateFriendMarriage("marriage_form="+marriageform+",marriage_datetime='"
						+ marriageDate + "'", "id=" + marriageId);
				// 发结婚请求
				//
				FriendMarriageBean friendMarriage = friendService
						.getFriendMarriage("id=" + marriageId);
				int toId = friendMarriage.getToId();
				int fromId = friendMarriage.getFromId();
				UserBean toUser = UserInfoUtil.getUser(toId);
				UserBean fromUser = UserInfoUtil.getUser(fromId);
//				ArrayList vec1 = (ArrayList) UserInfoUtil.getUserFriends(toId);
//				ArrayList vec2 = (ArrayList) UserInfoUtil
//						.getUserFriends(fromId);
//				int length1 = 0;
//				int length2 = 0;
//				if (vec1 != null && vec2 != null
//						&& (vec1.size() + vec2.size()) < 50) {
//					length1 = vec1.size();
//					length2 = vec2.size();
//				} else if (vec1 != null && vec2 != null	&& (vec1.size() + vec2.size()) > 50) {
//					if (vec1.size() < 25) {
//						length1 = vec1.size();
//						length2 = 50 - vec1.size();
//					} else if (vec2.size() < 25) {
//						length2 = vec2.size();
//						length1 = 50 - vec2.size();
//					} else {
//						length1 = 25;
//						length2 = 15;
//
//					}
//				}
//				else if(vec1==null){
//					if(vec2!=null && vec2.size()>50)
//						length2=50;
//					else if(vec2!=null && vec2.size()<50)
//						
//						length2=vec2.size();
//					
//				}
//				else if(vec2==null){
//					if(vec1!=null && vec1.size()>50)
//						length1=50;
//					else if(vec1!=null && vec1.size()<50)
//						
//						length1=vec1.size();
//				}
//				
//				for (int i = 0; i < length1; i++) {
//					String friendId = (String) vec1.get(i);
//					if (Integer.parseInt(friendId) != toId
//							&& Integer.parseInt(friendId) != fromId) {
//						NoticeBean notice = new NoticeBean();
//						notice.setUserId(Integer.parseInt(friendId));
//						notice.setTitle("您的好友" + toUser.getNickName() + "和"
//								+ fromUser.getNickName() + "更改婚期了，现邀请您于"
//								+ marriageDate + "参加他们的结婚庆典，敬请光临！");
//						notice.setContent("");
//						notice.setHideUrl("");
//						notice.setType(NoticeBean.GENERAL_NOTICE);
//						notice
//								.setLink(BaseAction.URL_PREFIX
//										+ "/friend/redbag.jsp?marriageId="
//										+ marriageId);
//						NoticeUtil.getNoticeService().addNotice(notice);
//					}
//				}
//
//				for (int i = 0; i < length2; i++) {
//					String friendId = (String) vec2.get(i);
//					if (Integer.parseInt(friendId) != toId
//							&& Integer.parseInt(friendId) != fromId) {
//						NoticeBean notice = new NoticeBean();
//						notice.setUserId(Integer.parseInt(friendId));
//						notice.setTitle("您的好友" + toUser.getNickName() + "和"
//								+ fromUser.getNickName() + "更改婚期了，现邀请您于"
//								+ marriageDate + "参加他们的结婚庆典，敬请光临！");
//						notice.setContent("");
//						notice.setHideUrl("");
//						notice.setType(NoticeBean.GENERAL_NOTICE);
//						notice
//								.setLink(BaseAction.URL_PREFIX
//										+ "/friend/redbag.jsp?marriageId="
//										+ marriageId);
//						NoticeUtil.getNoticeService().addNotice(notice);
//					}
//
//				}

				request.setAttribute("tip", tip);
			}
		} else if (request.getParameter("marriagedate") == null) {
			int candyNum = StringUtil.toInt(request.getParameter("number"));
			int candyPrice = StringUtil.toInt(request.getParameter("price"));

			loginUser = getLoginUser(request);
			UserStatusBean user = (UserStatusBean) UserInfoUtil
					.getUserStatus(loginUser.getId());

			/**
			 * 检查参数
			 */
			if (candyPrice <= 0 || candyNum <= 0) {
				result = "failure";
				tip = " 喜糖价格和数量不能为负";
				request.setAttribute("tip", tip);

			} else if ((candyPrice > 20000000) || candyNum > 20000000) {
				result = "failure";
				tip = " 喜糖价格和数量不能超过20000000";
				request.setAttribute("tip", tip);

			}else if (user.getGamePoint() < candyNum * candyPrice) {
				result = "failure";
				tip = " 新事新办，没那么多钱婚礼就简朴一点吧。";
				request.setAttribute("tip", tip);

			}else if ((long)candyNum * candyPrice>=2000000000) {
				result = "failure";
				tip = " 新事新办，没那么多钱婚礼就简朴一点吧。";
				request.setAttribute("tip", tip);

			} else {
				UserInfoUtil.updateUserStatus("game_point=game_point-"
						+ candyNum * candyPrice,
						"user_id=" + loginUser.getId(), loginUser.getId(),
						UserCashAction.OTHERS, "买喜糖扣钱" + candyNum * candyPrice);
				friendService.updateFriendMarriage("candy_num=" + candyNum
						+ ",candy_price=" + candyPrice + ",candy_remain="
						+ candyNum, "id=" + marriageId);
				tip = " 您已经成功更改喜糖，恭喜您啦";
				OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP,"id=" + marriageId);

			}

			request.setAttribute("tip", tip);
		}

		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
