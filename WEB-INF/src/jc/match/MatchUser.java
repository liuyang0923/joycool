package jc.match;

import net.joycool.wap.util.StringUtil;

/**
 *参赛用户表
 */
public class MatchUser {
	int userId;
	int voteCount;				//本赛季得票总数
	int consume;				//本赛季消费总数
	String encounce;			//个人宣言
	String photo;				//参赛照片
	String photo2;				//照片的缩略图
	int del;					//是否已删除
	int checked;				//0:未审 1:已通过 2:未通过 3:取消资格
	long createTime;			//创建时间
	int photoFrom;				//照片的来源:0=从手机上传；1=从家园中选择
	int consCount;				//兑换总数
	int good[] = new int[8]; 	//所持有的物品
	int totalVote;				//得票总数
	int totalConsume;			//消费总数
	int blogCount;				//博客总数
	int placeId;				//所在地区
	int areaId;					//所在的大区
	public int getPlaceId() {
		return placeId;
	}
	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}
	public int[] getGood() {
		return good;
	}
	public void setGood(int[] good) {
		this.good = good;
	}
	public int getConsCount() {
		return consCount;
	}
	public void setConsCount(int consCount) {
		this.consCount = consCount;
	}
	public int getPhotoFrom() {
		return photoFrom;
	}
	public void setPhotoFrom(int photoFrom) {
		this.photoFrom = photoFrom;
	}
	public String getEncounce() {
		return encounce;
	}
	public String getEncounceWml() {
		return StringUtil.toWml(encounce);
	}
	public void setEncounce(String encounce) {
		this.encounce = encounce;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public int getConsume() {
		return consume;
	}
	public void setConsume(int consume) {
		this.consume = consume;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getPhoto2() {
		return photo2;
	}
	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}
	public int getDel() {
		return del;
	}
	public void setDel(int del) {
		this.del = del;
	}
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getTotalVote() {
		return totalVote;
	}
	public void setTotalVote(int totalVote) {
		this.totalVote = totalVote;
	}
	public int getTotalConsume() {
		return totalConsume;
	}
	public void setTotalConsume(int totalConsume) {
		this.totalConsume = totalConsume;
	}
	/**
	 * 获得本赛季正确的靓点数=总数-已消费的数
	 */
	public int getCurrentVote(){
		return voteCount - consume;
	}
	/**
	 * 获得靓点数=总数-已消费的数
	 */
	public int getCurrentVote2(){
		int tmp = totalVote - totalConsume;
		if (tmp > 0){
			return tmp;
		} 
		return 0;
	}
	public int getBlogCount() {
		return blogCount;
	}
	public void setBlogCount(int blogCount) {
		this.blogCount = blogCount;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	
}