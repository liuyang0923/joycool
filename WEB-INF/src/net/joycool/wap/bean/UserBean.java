package net.joycool.wap.bean;


import java.util.*;

import net.joycool.wap.bean.item.ShowBean;
import net.joycool.wap.bean.pk.PKUserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.LockUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class UserBean {
	public static int FLAG_FRIEND = (1 << 0);	// 开通了交友资料
	public static int FLAG_FRIEND_CREDIT = (1 << 1);	// 开通了可信度
	public static int FLAG_HOME = (1 << 2);	// 开通了家园
	
	int id;

	String password;

	int gender;

	String mobile;

	String nickName;

	String selfIntroduction;

	int age;

	String ipAddress;

	String lastVisitPage;

	String userAgent;

	int placeno;

	String cityname;

	int cityno;

	// mcq_判断用户是否开通免骚扰功能_2006-10-22_start
	int harass;

	// mcq_判断用户是否开通免骚扰功能_2006-10-22_end

	UserStatusBean us;

	// mcq_判断用户是否开通过家园标志位_2006-9-19_start
	int home;

	// mcq_判断用户是否开通过家园标志位_2006-9-19_end
	// mcq_判断用户是否开通个人交友信息标志位_2006-10-26_start
	int friend;	// 用于标志位，第一位是是否开通交友资料

	// mcq_判断用户是否开通个人交友信息标志位_2006-10-26_end

	int positionId;

	// mcq_pk系统属性_2006-1-31_start
	PKUserBean pkUser;

	// mcq_pk系统属性_2006-1-31_end

	String shortNickName;


	UserSettingBean userSetting;
	
	Date createDatetime = null;

	public static IUserService uService = ServiceFactory.createUserService();
	
	Object lock = null;	// 用户操作的锁
	
	public int latestTrend;		// 最后看到的动态id
	
	public int[] notice = new int[2];		// 0是聊天通知数量，1是信件通知数量
	public String status = null;	// 当前状态
	
	public Object getLock() {
		if(lock == null) {
			lock = LockUtil.userLock.getLock(id);
		}
		return lock;
	}

	/**
	 * @return 返回 createDatetime。
	 */
	public Date getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime
	 *            要设置的 createDatetime。
	 */
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	
	public static HashMap userSettingCache = new HashMap();
	/**
	 * @return 返回 userSetting。
	 */
	public UserSettingBean getUserSetting() {
		return this.userSetting;
	}

	/**
	 * @param userSetting
	 *            要设置的 userSetting。
	 */
	public void setUserSetting(UserSettingBean userSetting) {
		this.userSetting = userSetting;
	}

	public String getShortNickName() {
		return shortNickName;
	}

	public void setShortNickName(String shortNickName) {
		this.shortNickName = shortNickName;
	}

	/**
	 * @return Returns the us.
	 */
	public UserStatusBean getUs() {
		return us;
	}

	/**
	 * @param us
	 *            The us to set.
	 */
	public void setUs(UserStatusBean us) {
		this.us = us;
	}

	/**
	 * @return Returns the cityname.
	 */
	public String getCityname() {
		return cityname;
	}

	/**
	 * @param cityname
	 *            The cityname to set.
	 */
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	/**
	 * @return Returns the cityno.
	 */
	public int getCityno() {
		return cityno;
	}

	/**
	 * @param cityno
	 *            The cityno to set.
	 */
	public void setCityno(int cityno) {
		this.cityno = cityno;
	}

	/**
	 * @return Returns the placeno.
	 */
	public int getPlaceno() {
		return placeno;
	}

	/**
	 * @param placeno
	 *            The placeno to set.
	 */
	public void setPlaceno(int placeno) {
		this.placeno = placeno;
	}

	/**
	 * @return Returns the userAgent.
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent
	 *            The userAgent to set.
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @return Returns the ipAddress.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            The ipAddress to set.
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return Returns the lastVisitPage.
	 */
	public String getLastVisitPage() {
		return lastVisitPage;
	}

	/**
	 * @param lastVisitPage
	 *            The lastVisitPage to set.
	 */
	public void setLastVisitPage(String lastVisitPage) {
		this.lastVisitPage = lastVisitPage;
	}

	/**
	 * @return Returns the age.
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            The age to set.
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return Returns the gender.
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            The gender to set.
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the mobile.
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            The mobile to set.
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickNameWml() {
		return StringUtil.toWml(nickName);
	}
	/**
	 * @return Returns the nickName.
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName
	 *            The nickName to set.
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
		this.shortNickName = StringUtil.limitString(nickName, 12);
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the selfIntroduction.
	 */
	public String getSelfIntroduction() {
		return selfIntroduction;
	}

	/**
	 * @param selfIntroduction
	 *            The selfIntroduction to set.
	 */
	public void setSelfIntroduction(String selfIntroduction) {
		this.selfIntroduction = selfIntroduction;
	}

	public boolean isFriend(int userId) {
		int isFriend = -1;
		if (isFriend != -1) {
			if (isFriend == 1) {
				return true;
			} else {
				return false;
			}
		}

		Vector friends = uService.getFriends("user_id = " + userId
				+ " AND friend_id = " + id);
		if (friends.size() == 0) {
			isFriend = 0;
			return false;
		} else {
			isFriend = 1;
			return true;
		}
	}

	// mcq_start_添加position的get\set方法 时间:2006-6-6
	/**
	 * @return Returns the positionId.
	 */
	public int getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId
	 *            The positionId to set.
	 */
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	// mcq_end 时间:2006-6-6

	public UserBean getOnlineStatus() {
		return (UserBean)OnlineUtil.getOnlineBean(String.valueOf(id));
	}
	public boolean isOnline() {
		return getOnlineStatus() != null;
	}

	public UserStatusBean getUs2() {
		return UserInfoUtil.getUserStatus(id);
	}

	/**
	 * @return 返回 home。
	 */
	public int getHome() {
		return home;
	}

	/**
	 * @param home
	 *            要设置的 home。
	 */
	public void setHome(int home) {
		this.home = home;
	}

	/**
	 * @return 返回 harass。
	 */
	public int getHarass() {
		return harass;
	}

	/**
	 * @param harass
	 *            要设置的 harass。
	 */
	public void setHarass(int harass) {
		this.harass = harass;
	}

	/**
	 * @return 返回 friend。
	 */
	public int getFriend() {
		return friend;
	}

	
	public boolean isFlagFriend() {
		return (friend & FLAG_FRIEND) != 0;
	}
	public boolean isFlagFriendCredit() {
		return (friend & FLAG_FRIEND_CREDIT) != 0;
	}
	public boolean isFlagHome() {
		return (friend & FLAG_HOME) != 0;
	}
	public void setFlag(int flag) {
		friend = flag;
	}
	public int getFlag() {
		return friend;
	}
	
	public boolean isFlag(int seq) {
		return (friend & (1 << seq)) != 0;
	}
	public void setFlag(int flag, boolean set) {
		if(set)
			friend |= (1 << flag);
		else
			friend &= ~(1 << flag);
	}
	public void toggleFlag(int flag) {
		friend ^= (1 << flag);
	}
	
	/**
	 * @param friend
	 *            要设置的 friend。
	 */
	public void setFriend(int friend) {
		this.friend = friend;
	}
	// 对所有人公开
	public int getBirthdayOpenMark() {
		return 1;
	}

	/**
	 * @return pkUser
	 */
	public PKUserBean getPkUser() {
		return pkUser;
	}

	/**
	 * @param pkUser
	 *            要设置的 pkUser
	 */
	public void setPkUser(PKUserBean pkUser) {
		this.pkUser = pkUser;
	}

	/**
	 * 
	 * @author macq
	 * @explain：判断是否显示图片
	 * @datetime:2007-7-23 13:46:25
	 * @param imgPath
	 * @return
	 * @return String
	 */
	public String showImg(String imgPath) {
		String path = "";
		if (userSetting != null && !userSetting.isFlagHideLogo()) {
			return "<img src=\"" + imgPath + "\" alt=\"logo\" /><br/>";
		}
		return path;
	}
	
	public boolean isShowImg() {
		return userSetting != null && !userSetting.isFlagHideLogo();
	}

	/**
	 * 
	 * @author macq
	 * @explain：图片显示设置
	 * @datetime:2007-7-23 14:53:07
	 * @return
	 * @return boolean
	 */
	public boolean picMark() {
		if (userSetting != null && userSetting.isFlagHideLogo()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @author macq
	 * @explain：免骚扰功能
	 * @datetime:2007-7-23 13:47:05
	 * @param imgPath
	 * @return
	 * @return boolean
	 */
	public boolean noticeMark() {
		if (userSetting != null && userSetting.getNoticeMark() == 1) {
			return true;
		}
		return false;
	}

	public static ICacheMap itemShowCache = CacheManage.itemShow;
//	/**
//	 * @return 得到特殊信息图片wml
//	 */
//	public String getExtraInfo(int limit) {
//		Integer key = Integer.valueOf(id);
//		synchronized(itemShowCache) {
//			String info = (String) itemShowCache.get(key);
//			if(info == null) {
//				info = "";
//				try {
//					List ei = getEIList();
//					UserHonorBean honor = getUserHonor();
//					if (ei.isEmpty() && honor.getRank() <= 1)
//						return null;
//			
//					StringBuilder extra = new StringBuilder();
//					if(honor.getRank() > 1) {
//						extra.append("<img src=\"/img/honor/h");
//						extra.append(honor.getRank());
//						extra.append(".gif\" alt=\"");
//						extra.append(honor.getRankNameShort());
//						extra.append("\" />");
//					}
//					
//					Iterator iter = ei.iterator();
//					while (iter.hasNext() && limit != 0) {
//						ShowBean bean = (ShowBean) iter.next();
//		//				if(bean.getItemId() == 47 || bean.getItemId() == 33)
//		//					if(createDatetime != null && System.currentTimeMillis() - createDatetime.getTime() > 3600l * 1000 * 30 * 24)
//		//						continue;
//						extra.append("<img src=\"/img/honor/e");
//						extra.append(bean.getItemId());
//						extra.append(".gif\" alt=\"");
//						extra.append(bean.getName());
//						extra.append("\" />");
//						limit--;
//					}
//					info = extra.toString();
//				} catch(Exception e) {}
//				itemShowCache.put(key, info);
//			}
//			return info;
//		}
//	}
//	// 把图片显示为文字[星]
//	public String getExtraInfoText(int limit) {
//		try {
//			List ei = getEIList();
//			UserHonorBean honor = getUserHonor();
//			if (ei.isEmpty() && honor.getRank() <= 1)
//				return null;
//	
//			StringBuilder extra = new StringBuilder();
//			extra.append('|');
//			if(honor.getRank() > 1) {
//				extra.append(honor.getRankNameShort());
//				if(!ei.isEmpty())
//					extra.append('|');
//			}
//			
//			Iterator iter = ei.iterator();
//			while (iter.hasNext() && limit > 0) {
//				ShowBean bean = (ShowBean) iter.next();
//				extra.append(bean.getName());
//				limit--;
//				extra.append('|');
//			}
//			return extra.toString();
//		} catch(Exception e) {
//			return "";
//		}
//	}
//
//	public String getExtraInfoFull(int limit) {
//		String info = "";
//		try {
//			List ei = getEIList();
//			UserHonorBean honor = getUserHonor();
//			if (ei.isEmpty() && honor.getRank() <= 1)
//				return null;
//	
//			StringBuilder extra = new StringBuilder();
//			if(honor.getRank() > 1) {
//				extra.append("<img src=\"/img/honor/h");
//				extra.append(honor.getRank());
//				extra.append(".gif\" alt=\"");
//				extra.append(honor.getRankNameShort());
//				extra.append("\" />乐酷荣誉的");
//				extra.append(honor.getRankName());
//				extra.append("勋章<br/>");
//			}
//			
//			Iterator iter = ei.iterator();
//			while (iter.hasNext() && limit != 0) {
//				ShowBean bean = (ShowBean) iter.next();
////				if(bean.getItemId() == 47 || bean.getItemId() == 33)
////					if(createDatetime != null && System.currentTimeMillis() - createDatetime.getTime() > 3600l * 1000 * 30 * 24)
////						continue;
//				extra.append("<img src=\"/img/honor/e");
//				extra.append(bean.getItemId());
//				extra.append(".gif\" alt=\"");
//				extra.append(bean.getName());
//				extra.append("\" />");
//				extra.append(bean.getIntro());
//				extra.append("<br/>");
//				limit--;
//			}
//			info = extra.toString();
//		} catch(Exception e) {}
//		return info;
//	}

	

//	public List getEIList() {
//		HashMap map = uService.getItemShowMap("1");
//
//		List itemList = UserBagCacheUtil.getUserBagListCacheById(id);
//		Iterator iter = itemList.iterator();
//		TreeMap tm = new TreeMap();
//		while (iter.hasNext()) {
//			Integer iid = (Integer) iter.next();
//			UserBagBean item = UserBagCacheUtil.getUserBagCache(iid
//					.intValue());
//			ShowBean show = (ShowBean) map.get(Integer.valueOf(item
//					.getProductId()));
//			if (show != null)
//				tm.put(Integer.valueOf(show.getId()), show);
//		}
//
//		return new ArrayList(tm.values());
//	}
	
	public List getEIList(List seq) {
		HashMap map = uService.getItemShowMap("1");

		List itemList = UserBagCacheUtil.getUserBagListCacheById(id);
		Iterator iter = itemList.iterator();
		TreeMap tm = new TreeMap();
		while (iter.hasNext()) {
			Integer iid = (Integer) iter.next();
			UserBagBean item = UserBagCacheUtil.getUserBagCache(iid
					.intValue());
			ShowBean show = (ShowBean) map.get(Integer.valueOf(item
					.getProductId()));
			if (show != null){
				int id = seq.indexOf(new Integer(show.getItemId()));
				if(id != -1) {
					tm.put(new Integer(id - 100), new Integer(show.getItemId()));
				} else {
					tm.put(new Integer(show.getId()), new Integer(show.getItemId()));
				}
			}
		}
		return new ArrayList(tm.values());
	}

	public String getGenderText() {
		return gender == 1 ? "他" : "她";
	}
	
	public String getUserHat(){
		String content ="";
		UserStatusBean us = getUs2();
		if(us!=null){
			content=us.getHatShow();
		}
		return content;
	}
	public String getUserHatText(){
		String content ="";
		UserStatusBean us = getUs2();
		if(us!=null){
			content=us.getHatShowText();
		}
		return content;
	}
	
	public UserHonorBean getUserHonor() {
		return UserInfoUtil.getUserHonor(id);
	}

	public String getUserName() {
		return "乐客" + id;
	}
	
	
	public String getExtraInfoNoCache(int limit) {
		try {
			List ei = getEIList(StringUtil.toInts(UserInfoUtil.getUserSettingSeq(id)));
			UserHonorBean honor = getUserHonor();
			if (ei.isEmpty() && honor.getRank() <= 1)
				return null;
	
			StringBuilder extra = new StringBuilder();
			if(honor.getRank() > 1) {
				extra.append("<img src=\"/rep/lx/h");
				extra.append(honor.getRank());
				extra.append(".gif\" alt=\"");
				extra.append(honor.getRankNameShort());
				extra.append("\" />");
			}
			HashMap map = uService.getItemShowMap("1");
			Iterator iter = ei.iterator();
			while (iter.hasNext() && limit != 0) {
				ShowBean bean = (ShowBean)map.get((Integer)iter.next());
				extra.append("<img src=\"/rep/lx/e");
				extra.append(bean.getItemId());
				extra.append(".gif\" alt=\"");
				extra.append(bean.getName());
				extra.append("\" />");
				limit--;
			}
			return extra.toString();
		} catch(Exception e) {e.printStackTrace();}
		return "";
	}
	
	
	public String getExtraInfoFull(int limit) {
		String info = "";
		try {
			//UserSettingBean setting = UserInfoUtil.getUserSetting(id);
			List ei = getEIList(StringUtil.toInts(UserInfoUtil.getUserSettingSeq(id)));
			UserHonorBean honor = getUserHonor();
			if (ei.isEmpty() && honor.getRank() <= 1)
				return null;
	
			StringBuilder extra = new StringBuilder();
			if(honor.getRank() > 1) {
				extra.append("<img src=\"/img/honor/h");
				extra.append(honor.getRank());
				extra.append(".gif\" alt=\"");
				extra.append(honor.getRankNameShort());
				extra.append("\" />乐酷荣誉的");
				extra.append(honor.getRankName());
				extra.append("勋章<br/>");
			}
			
			//Iterator iter = ei.iterator();
			//List seq = StringUtil.toInts(this.getUserSetting().getBagSeq());
			//List result = sortSeq(ei, seq,request);
			HashMap map = uService.getItemShowMap("1");
			Iterator iter = ei.iterator();
			while (iter.hasNext() && limit != 0) {
				//Integer id = iter.next();
				ShowBean bean = (ShowBean)map.get((Integer)iter.next());
				//ShowBean bean = (ShowBean) iter.next();
//				if(bean.getItemId() == 47 || bean.getItemId() == 33)
//					if(createDatetime != null && System.currentTimeMillis() - createDatetime.getTime() > 3600l * 1000 * 30 * 24)
//						continue;
				extra.append("<img src=\"/rep/lx/e");
				extra.append(bean.getItemId());
				extra.append(".gif\" alt=\"");
				extra.append(bean.getName());
				extra.append("\" />");
				extra.append(bean.getIntro());
				extra.append("<br/>");
				limit--;
			}
			info = extra.toString();
		} catch(Exception e) {e.printStackTrace();}
		return info;
	}
	
	// 把图片显示为文字[星]
	public String getExtraInfoText(int limit) {
		try {
			//UserSettingBean setting = UserInfoUtil.getUserSetting(id);
			List ei = getEIList(StringUtil.toInts(UserInfoUtil.getUserSettingSeq(id)));
			UserHonorBean honor = getUserHonor();
			if (ei.isEmpty() && honor.getRank() <= 1)
				return null;
	
			StringBuilder extra = new StringBuilder();
			extra.append('|');
			if(honor.getRank() > 1) {
				extra.append(honor.getRankNameShort());
				if(!ei.isEmpty())
					extra.append('|');
			}
			
			HashMap map = uService.getItemShowMap("1");
			Iterator iter = ei.iterator();
			while (iter.hasNext() && limit > 0) {
				ShowBean bean = (ShowBean)map.get((Integer)iter.next());
				extra.append(bean.getName());
				limit--;
				extra.append('|');
			}
			return extra.toString();
		} catch(Exception e) {
			return "";
		}
	}
	
	/**
	 * @return 得到特殊信息图片wml
	 */
	public String getExtraInfo(int limit) {
		Integer key = Integer.valueOf(id);
		synchronized(itemShowCache) {
			String info = (String) itemShowCache.get(key);
			if(info == null) {
				info = "";
				try {
					//UserSettingBean setting = UserInfoUtil.getUserSetting(id);
					List ei = getEIList(StringUtil.toInts(UserInfoUtil.getUserSettingSeq(id)));
					UserHonorBean honor = getUserHonor();
					if (ei.isEmpty() && honor.getRank() <= 1)
						return null;
			
					StringBuilder extra = new StringBuilder();
					if(honor.getRank() > 1) {
						extra.append("<img src=\"/img/honor/h");
						extra.append(honor.getRank());
						extra.append(".gif\" alt=\"");
						extra.append(honor.getRankNameShort());
						extra.append("\" />");
					}
					
					HashMap map = uService.getItemShowMap("1");
					Iterator iter = ei.iterator();
					while (iter.hasNext() && limit != 0) {
						ShowBean bean = (ShowBean)map.get((Integer)iter.next());
						extra.append("<img src=\"/rep/lx/e");
						extra.append(bean.getItemId());
						extra.append(".gif\" alt=\"");
						extra.append(bean.getName());
						extra.append("\" />");
						limit--;
					}
					info = extra.toString();
					
				} catch(Exception e) {}
				itemShowCache.put(key, info);
			}
			return info;
		}
	}
	
	public String toSeqString(List seqList) {
		if(seqList == null || seqList.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder(64);
		
		int limit = seqList.size() > 10 ? 10 : seqList.size();
		
		for(int i = 0; i < limit; i ++) {
			int ii = ((Integer)seqList.get(i)).intValue();
			sb.append(",");
			sb.append(ii);
		}
		
		return sb.toString();
	}
	
	//最前
	public List bagSeqTop(List seqList, int id) {
		
		int index = seqList.indexOf(new Integer(id));
		
		if(index != -1) {
			Integer iid = (Integer)seqList.get(index);
			seqList.remove(index);
			seqList.add(0, iid);
		}
		return seqList;
	}
	
	//末尾
	public List bagSeqUp(List seqList, int id) {
		
		int index = seqList.indexOf(new Integer(id));
		
		if(index != -1) {
			int index2 = index > 0 ? index - 1 : 0;
			Integer id1 = (Integer)seqList.get(index);
			//Integer id2 = (Integer)seqList.get(index2);
			seqList.remove(index);
			seqList.add(index2, id1);
		}
		return seqList;
	}
	//移动前一位
	public List bagSeqTail(List seqList, int id) {
		
		int index = seqList.indexOf(new Integer(id));
		
		if(index != -1) {
			Integer iid = (Integer)seqList.get(index);
			seqList.remove(index);
			seqList.add(seqList.size(), iid);
		}
		return seqList;
	}

	public String getFriendString() {
		List list = UserInfoUtil.getUserFriends(id);
		if(list.size() == 0)
			return "";
		
		StringBuilder sb = new StringBuilder(list.size() * 8);
		int size = list.size() - 1;
		for(int i = 0;i < size;i++) {
			sb.append(list.get(i));
			sb.append(',');
		}
		sb.append(list.get(size));
		return sb.toString();
	}
	public List getFriendList() {
		return UserInfoUtil.getUserFriends(id);
	}
	public List getOnlineFriendList() {
		List list = UserInfoUtil.getUserFriends(id);
		List list2 = new ArrayList(16);
		for(int i=0;i<list.size();i++){
			String userIdKey=(String)list.get(i);
			if(OnlineUtil.isOnline(userIdKey))
				list2.add(userIdKey);
		}
		return list2;
	}
	public String getOnlineFriendString() {
		List list = UserInfoUtil.getUserFriends(id);
		StringBuilder sb = new StringBuilder(list.size());
		int size = list.size();
		for(int i = 0;i < size;i++) {
			String userIdKey=(String)list.get(i);
			if(OnlineUtil.isOnline(userIdKey)) {
				sb.append(userIdKey);
				sb.append(',');
			}
		}
		if(sb.length() == 0)
			return "";
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
}
