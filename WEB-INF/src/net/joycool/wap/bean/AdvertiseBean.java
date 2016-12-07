package net.joycool.wap.bean;
/**
 * 电子书项目的广告列表
 * @author han_yan
 *
 */
public class AdvertiseBean {

	int    id; 		//广告的id
	String ad_name;	//广告的名称
	String ad_wap;	//广告的网址
	int	   status;	//标志广告当前有效
	
	public void setID(int id){
		this.id=id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setAd_name(String ad_name){
		this.ad_name=ad_name;
	}
	
	public String getAd_name(){
		return this.ad_name;
	}
	
	public void setAd_wap(String ad_wap){
		this.ad_wap=ad_wap;
	}
	
	public String getAd_wap(){
		return this.ad_wap;
	}
	
	public void setStatus(int status){
		this.status=status;
	}
	
	public int getStatus(){
		return this.status;
	}
}
