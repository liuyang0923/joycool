/**
 *作者:macq
 *日期:2006-9-14
 *功能:我的家园接口 
 */
package net.joycool.wap.service.infc;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.home.HomeBean;
import net.joycool.wap.bean.home.HomeDiaryBean;
import net.joycool.wap.bean.home.HomeDiaryReviewBean;
import net.joycool.wap.bean.home.HomeEnounce;
import net.joycool.wap.bean.home.HomeHitsBean;
import net.joycool.wap.bean.home.HomeImageBean;
import net.joycool.wap.bean.home.HomeImageTypeBean;
import net.joycool.wap.bean.home.HomeNeighborBean;
import net.joycool.wap.bean.home.HomePhotoBean;
import net.joycool.wap.bean.home.HomePhotoReviewBean;
import net.joycool.wap.bean.home.HomePlayer;
import net.joycool.wap.bean.home.HomePlayerRank;
import net.joycool.wap.bean.home.HomePlayerVote;
import net.joycool.wap.bean.home.HomeReviewBean;
import net.joycool.wap.bean.home.HomeTypeBean;
import net.joycool.wap.bean.home.HomeUserBean;
import net.joycool.wap.bean.home.HomeUserImageBean;

public interface IHomeService {
	public HomeUserBean getHomeUser(String condition);
	public Vector getHomeUserList(String condition);
	public boolean addHomeUser(HomeUserBean bean);
	public boolean deleteHomeUser(String condition);
	public boolean updateHomeUser(String set, String condition);
	public int getHomeUserCount(String condition);
	
	public HomeImageBean getHomeImage(String condition);
	public Vector getHomeImageList(String condition);
	public boolean addHomeImage(HomeImageBean bean);
	public boolean deleteHomeImage(String condition);
	public boolean updateHomeImage(String set, String condition);
	public int getHomeImageCount(String condition);
	
	public HomeImageTypeBean getHomeImageType(String condition);
	public Vector getHomeImageTypeList(String condition);
	public boolean addHomeImageType(HomeImageTypeBean bean);
	public boolean deleteHomeImageType(String condition);
	public boolean updateHomeImageType(String set, String condition);
	public int getHomeImageTypeCount(String condition);
	
	public HomeNeighborBean getHomeNeighbor(String condition);
	public Vector getHomeNeighborList(String condition);
	public boolean addHomeNeighbor(HomeNeighborBean bean);
	public boolean deleteHomeNeighbor(String condition);
	public boolean updateHomeNeighbor(String set, String condition);
	public int getHomeNeighborCount(String condition);
	
	public HomePhotoBean getHomePhoto(String condition);
	public Vector getHomePhotoList(String condition);
	public Vector getHomePhotoTopList(String condition);
	public boolean addHomePhoto(HomePhotoBean bean);
	public boolean deleteHomePhoto(String condition);
	public boolean updateHomePhoto(String set, String condition);
	public int getHomePhotoCount(String condition);
	public int getHomePhotoTopCount(String condition);
	//zhul_20006-09-14 查询用户图片及用户相关信息
	public Vector getPhotoList(String condition);
	
	public HomePhotoReviewBean getHomePhotoReview(String condition);
	public Vector getHomePhotoReviewList(String condition);
	public boolean addHomePhotoReview(HomePhotoReviewBean bean);
	public boolean deleteHomePhotoReview(String condition);
	public boolean updateHomePhotoReview(String set, String condition);
	public int getHomePhotoReviewCount(String condition);
	
	public HomeReviewBean getHomeReview(String condition);
	public Vector getHomeReviewList(String condition);
	public boolean addHomeReview(HomeReviewBean bean);
	public boolean deleteHomeReview(String condition);
	public boolean updateHomeReview(String set, String condition);
	public int getHomeReviewCount(String condition);
	
	public HomeUserImageBean getHomeUserImage(String condition);
	public Vector getHomeUserImageList(String condition);
	public boolean addHomeUserImage(HomeUserImageBean bean);
	public boolean deleteHomeUserImage(String condition);
	public boolean updateHomeUserImage(String set, String condition);
	public int getHomeUserImageCount(String condition);
	
	public HomeDiaryBean getHomeDiary(String condition);
	public Vector getHomeDiaryList(String condition);
	public Vector getHomeDiaryTopList(String condition);
	public boolean addHomeDiary(HomeDiaryBean bean);
	public boolean deleteHomeDiary(String condition);
	public boolean updateHomeDiary(String set, String condition);
	public int getHomeDiaryCount(String condition);
	public int getHomeDiaryTopCount(String condition);
	
	public HomeDiaryReviewBean getHomeDiaryReview(String condition);
	public Vector getHomeDiaryReviewList(String condition);
	public boolean addHomeDiaryReview(HomeDiaryReviewBean bean);
	public boolean deleteHomeDiaryReview(String condition);
	public boolean updateHomeDiaryReview(String set, String condition);
	public int getHomeDiaryReviewCount(String condition);
	
	public HomeHitsBean getHomeHits(String condition);
	public Vector getHomeHitsList(String condition);
	public boolean addHomeHits(HomeHitsBean bean);
	public boolean deleteHomeHits(String condition);
	public boolean updateHomeHits(String set, String condition);
	public int getHomeHitsCount(String condition);
	//用户家园人气排名
	public ArrayList getHomeOrder();
	//wucx2006-10-10
	public int getHomeUserDiaryCount(String condition);
	public int getHomeUserPhotoCount(String condition);
//	wucx2006-10-10
	//wucx2006-10-16 优化SQL语句的IN
	public String getHomeNeighborID(String condition);
//	wucx2006-10-16
	
	
	public HomeTypeBean getHomeType(String condition);
	public Vector getHomeTypeList(String condition);
	public boolean addHomeType(HomeTypeBean bean);
	public boolean deleteHomeType(String condition);
	public boolean updateHomeType(String set, String condition);
	public int getHomeTypeCount(String condition);
	
	public HomeBean getHome(String condition);
	public Vector getHomeList(String condition);
	public boolean addHome(HomeBean bean);
	public boolean deleteHome(String condition);
	public boolean updateHome(String set, String condition);
	public int getHomeCount(String condition);
	
//	// 用户宣言
//	public HomeEnounce getEnounce(String cond);
//	public List getEnounceList(String cond);
//	public boolean addNewEnounce(HomeEnounce bean);
//	
//	// 参赛用户
//	public HomePlayer getPlayer(String cond);
//	public boolean addNewPlayer(HomePlayer bean);
//	public boolean vote(HomePlayerVote bean);
//	
//	// 参赛用户投票
//	public boolean votePlayer(HomePlayerVote bean);
//	
//	public List getRankList(String cond);
//	public HomePlayerRank getRank(String cond);
}

