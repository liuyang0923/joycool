/** 
 *作者:macq
 *日期:2006-9-14
 *功能:我的家园数据接口封装 
 */
package net.joycool.wap.bean.home;

import java.util.*;

import net.joycool.wap.spec.buyfriends.BeanVisit;
import net.joycool.wap.spec.buyfriends.ServiceTrend;

public class HomeUserBean {
	int id;

	int userId;

	String name;

	String mobile;

	String city;

	int constellation;

	int height;

	int weight;

	String work;

	int personality;
	
	int marriage;

	int aim;

	String friendCondition;

	int hits;
	
	int totalHits;

	int mark;

	String createDatetime;
	
	int gender;
	
	int age;
	
	int diaryCount;
	int photoCount;
	int reviewCount;	// 家园留言数量
	String lastModifyTime;
	int typeId;
	int notice;
	int recommended;	// 推荐的照片ID
	int photoCatCount;	// 照片分类总数
	int diaryCatCount;	// 日记分类总数
	int diaryDefCount;	// 默认日记分类总数
	int photoDefCount;	// 默认相册分类总数
	
	public int getDiaryDefCount() {
		return diaryDefCount;
	}

	public void setDiaryDefCount(int diaryDefCount) {
		this.diaryDefCount = diaryDefCount;
	}

	public int getPhotoDefCount() {
		return photoDefCount;
	}

	public void setPhotoDefCount(int photoDefCount) {
		this.photoDefCount = photoDefCount;
	}

	public int getDiaryCatCount() {
		return diaryCatCount;
	}

	public void setDiaryCatCount(int diaryCatCount) {
		this.diaryCatCount = diaryCatCount;
	}

	public int getPhotoCatCount() {
		return photoCatCount;
	}

	public void setPhotoCatCount(int photoCatCount) {
		this.photoCatCount = photoCatCount;
	}
	
	public int getRecommended() {
		return recommended;
	}

	public void setRecommended(int recommended) {
		this.recommended = recommended;
	}

	int allow = 0;		// 家园是否允许所有人看，0允许，1好友，2都不允许
	String password = "";		// 家园访问密码
	
	//LinkedHashSet recentVisit = new LinkedHashSet(10);
	List recentVisit = Collections.synchronizedList(
			new ArrayList(BeanVisit.CACHE_RECENT_VISIT_COUNT));	//最近访客
	LinkedList trend = new LinkedList();	//我的动态
	
	

	public List getRecentVisit() {
		return recentVisit;
	}

	public void setRecentVisit(List recentVisit) {
		this.recentVisit = recentVisit;
	}

	public LinkedList getTrend() {
		return trend;
	}

	public void setTrend(LinkedList trend) {
		this.trend = trend;
	}

	/**
	 * @return Returns the notice.
	 */
	public int getNotice() {
		return notice;
	}

	/**
	 * @param notice The notice to set.
	 */
	public void setNotice(int notice) {
		this.notice = notice;
	}

	/**
	 * @return Returns the typeId.
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId The typeId to set.
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return 返回 weight。
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight 要设置的 weight。
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @return 返回 city。
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city 要设置的 city。
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return 返回 constellation。
	 */
	public int getConstellation() {
		return constellation;
	}

	/**
	 * @param constellation 要设置的 constellation。
	 */
	public void setConstellation(int constellation) {
		this.constellation = constellation;
	}

	/**
	 * @return 返回 createDatetime。
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime 要设置的 createDatetime。
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return 返回 friendCondition。
	 */
	public String getFriendCondition() {
		return friendCondition;
	}

	/**
	 * @param friendCondition 要设置的 friendCondition。
	 */
	public void setFriendCondition(String friendCondition) {
		this.friendCondition = friendCondition;
	}

	/**
	 * @return 返回 hits。
	 */
	public int getHits() {
		return hits;
	}

	/**
	 * @param hits 要设置的 hits。
	 */
	public void setHits(int hits) {
		this.hits = hits;
	}

	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id 要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 mark。
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark 要设置的 mark。
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return 返回 mobile。
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile 要设置的 mobile。
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 height。
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height 要设置的 height。
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return 返回 userId。
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId 要设置的 userId。
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return 返回 work。
	 */
	public String getWork() {
		return work;
	}

	/**
	 * @param work 要设置的 work。
	 */
	public void setWork(String work) {
		this.work = work;
	}

	/**
	 * @return 返回 marriage。
	 */
	public int getMarriage() {
		return marriage;
	}

	/**
	 * @param marriage 要设置的 marriage。
	 */
	public void setMarriage(int marriage) {
		this.marriage = marriage;
	}

	/**
	 * @return 返回 aim。
	 */
	public int getAim() {
		return aim;
	}

	/**
	 * @param aim 要设置的 aim。
	 */
	public void setAim(int aim) {
		this.aim = aim;
	}

	/**
	 * @return 返回 personality。
	 */
	public int getPersonality() {
		return personality;
	}

	/**
	 * @param personality 要设置的 personality。
	 */
	public void setPersonality(int personality) {
		this.personality = personality;
	}

	/**
	 * @return 返回 totalHits。
	 */
	public int getTotalHits() {
		return totalHits;
	}

	/**
	 * @param totalHits 要设置的 totalHits。
	 */
	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	/**
	 * @return Returns the diaryCount.
	 */
	public int getDiaryCount() {
		return diaryCount;
	}

	/**
	 * @param diaryCount The diaryCount to set.
	 */
	public void setDiaryCount(int diaryCount) {
		this.diaryCount = diaryCount;
	}

	/**
	 * @return Returns the lastModifyTime.
	 */
	public String getLastModifyTime() {
		return lastModifyTime;
	}

	/**
	 * @param lastModifyTime The lastModifyTime to set.
	 */
	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	/**
	 * @return Returns the photoCount.
	 */
	public int getPhotoCount() {
		return photoCount;
	}

	/**
	 * @param photoCount The photoCount to set.
	 */
	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount;
	}

	/**
	 * @return 返回 age。
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age 要设置的 age。
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return 返回 gender。
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender 要设置的 gender。
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * @return Returns the allow.
	 */
	public int getAllow() {
		return allow;
	}

	/**
	 * @param allow The allow to set.
	 */
	public void setAllow(int allow) {
		this.allow = allow;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
}
