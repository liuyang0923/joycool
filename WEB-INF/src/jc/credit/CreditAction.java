package jc.credit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DSUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import net.joycool.wap.bean.tong.TongUserBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.jcforum.ForumUserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;

public class CreditAction extends CustomAction{
	
	public static String ATTACH_ROOT = Constants.RESOURCE_ROOT_PATH + "credit";
	public static String ATTACH_URL_ROOT ="/rep/credit";
	
	//按日期查寻星座时使用
	public static int[] dateSplit = {0,20,19,21,20,21,21,23,23,23,23,22,22};
	public static int[] astroNum = {0,10,11,12,1,2,3,4,5,6,7,8,9};
	public static String[] astro = {"","白羊座","金牛座","双子座","巨蟹座","狮子座","处女座","天秤座","天蝎座","射手座","摩羯座","水瓶座","双鱼座"};
	
	public static String TianGan[] = {"甲", "乙", "丙", "丁", "戊", "己", "庚","辛", "壬", "癸"};
	public static String DiZhi[] = { "子", "丑", "寅", "卯", "辰", "巳", "午","未", "申", "酉", "戌", "亥"};
	public static String[] Animals ={ "鼠", "牛", "虎", "兔", "龙", "蛇","马", "羊", "猴", "鸡", "狗", "猪"};
	
	public static HashMap propertyMap = null;
	public static byte[] initLock = new byte[0];
	
	public static CreditService service = new CreditService();
	
	public CreditAction(){}
	
	public CreditAction(HttpServletRequest request){
		super(request);
		propertyMap = getPropertyMap();
	}

	// 取得属性
	public static HashMap getPropertyMap(){
		if (propertyMap != null) {
			return propertyMap;
		}
		synchronized (initLock) {
			if (propertyMap != null) {
				return propertyMap;
			}
			// 取得属性
			propertyMap = service.getPropertyMap(" 1");
		}
		return propertyMap;
	}
	
	//从缓存中取得一位用户
	public static ICacheMap userBaseCache = CacheManage.addCache(new LinkedCacheMap(1000, true), "userBase");
	public static UserBase getUserBaseBean(int uid) {
		UserBase userBase = null;
		synchronized(userBaseCache) {
			userBase = (UserBase)userBaseCache.get(new Integer(uid));
			if(userBase == null) {
				userBase = service.getUserBase(" user_id=" + uid);
				if(userBase != null) {
					userBaseCache.put(new Integer(uid), userBase);
				}
			}
		}
		return userBase;
	}
	
	// 取得属性字符串
	public static String getPropertyString(int id){
		if (id < 1 || id > propertyMap.size()){
			return "error";
		} else {
			return propertyMap.get(Integer.valueOf(id)).toString();
		}
	}
	
	// 根据月,日,返回相应的星座名称
	public static String getAstroByDate(int month,int day){
		return astro[getAstroIdByDate(month,day)];
	}
	
	// 根据ID返回星座的名子
	public static String getAstroString(int astroId){
		if (astroId < 1 || astroId > 12){
			return "error";
		} else {
			return astro[astroId];
		}
	}
	
	// 根据月,日,返回相应的星座ID
	public static int getAstroIdByDate(int month,int day){
		if (month <=0 || month >12 || day <= 0 || day >31){
			return 0;
		}
		if (day < dateSplit[month]){
			// 上一个星座
			return astroNum[month];
		} else {
			// 下一个星座
			if ((month + 1) > 12){
				// 摩羯座
				return astroNum[1];
			} else {
				return astroNum[month + 1];
			}
		}
	}
	
	// 取得一个用户信息表.若用户不存在,则创建一个新用户.
	public UserInfo getUserInfo(int userId){
		UserInfo userInfo = service.getUserInfo(" user_id=" + userId);
		if (userInfo == null){
			userInfo = new UserInfo();
			userInfo.setMobile("");
			userInfo.setUsers("");
			userInfo.setUserId(userId);
			service.addNewInfo(userInfo);
		}
		return userInfo;
	}
	
	// 判断选项的输入
	public void validateSelect(int type,int modify,HttpServletResponse response){
		Property property = null;
		int tmp = 0;
		String pid = this.getParameterNoCtrl("pid");
		if (type <= 0 || type > 10){
			this.setAttribute("tip", "参数错误.");
		} else {
			try {
			   switch(type){
				   case 3:
					// 提交学历
					   if (!SqlUtil.exist("select user_id from credit_user_base where user_id=" + this.getLoginUser().getId(),5)){
						   this.setAttribute("tip", "没有用户记录,请从头开始填写.");
					   } else {
						   tmp = StringUtil.toInt(pid);
						   property = service.getProperty(" id=" + tmp);
						   if (tmp <= 0 || property.getFlag() != 0){
							   this.setAttribute("tip", "提交的参数错误.");
						   } else {
							   SqlUtil.executeUpdate("update credit_user_base set education=" + tmp + " where user_id=" + this.getLoginUser().getId(),5);
							   UserBase userBase = getUserBaseBean(this.getLoginUser().getId());
							   if (userBase != null){
								   userBase.setEducation(tmp);
								   userBaseCache.put(new Integer(userBase.getUserId()), userBase);
							   }
							   updateModifyTime(1);
							   // 跳到下一项
							   if (modify==1){
								   response.sendRedirect("navi.jsp");
							   } else{
								   response.sendRedirect("select2.jsp?t=4");   
							   }
						   	   return;
						   }
					   }
					break;
			   		case 4:
						// 提交个性
			   			if (!SqlUtil.exist("select user_id from credit_user_base where user_id=" + this.getLoginUser().getId(),5)){
			   				this.setAttribute("tip", "没有用户记录,请从头开始填写.");
			   			} else {
						   tmp = StringUtil.toInt(pid);
						   property = service.getProperty(" id=" + tmp);
						   if (tmp <= 0 || property.getFlag() != 1){
							   this.setAttribute("tip", "提交的参数错误.");
						   } else {
							   SqlUtil.executeUpdate("update credit_user_base set personality=" + tmp + " where user_id=" + this.getLoginUser().getId(),5);
							   UserBase userBase = getUserBaseBean(this.getLoginUser().getId());
							   if (userBase != null){
								   userBase.setPersonality(tmp);
								   userBaseCache.put(new Integer(userBase.getUserId()), userBase);
							   }
							   updateModifyTime(1);
							   // 跳到下一项
							   if (modify==1){
								   response.sendRedirect("navi.jsp");
							   } else {
								   response.sendRedirect("select2.jsp?t=5");
							   }
							   return;
						   }
			   			}
					break;
			   		case 5:
						// 提交血型
			   			if (!SqlUtil.exist("select user_id from credit_user_base where user_id=" + this.getLoginUser().getId(),5)){
			   				this.setAttribute("tip", "没有用户记录,请从头开始填写.");
			   			} else {
						   tmp = StringUtil.toInt(pid);
						   property = service.getProperty(" id=" + tmp);
						   if (tmp <= 0 || property.getFlag() != 2){
							   this.setAttribute("tip", "提交的参数错误.");
						   } else {
							   SqlUtil.executeUpdate("update credit_user_base set blood=" + tmp + " where user_id=" + this.getLoginUser().getId(),5);
							   UserBase userBase = getUserBaseBean(this.getLoginUser().getId());
							   if (userBase != null){
								   userBase.setBlood(tmp);
								   userBaseCache.put(new Integer(userBase.getUserId()), userBase);
							   }
							   updateModifyTime(1);
							   // 跳到下一项
							   response.sendRedirect("navi.jsp");
						   	   return;
						   }
			   			}
					break;
			   		case 6:
						// 提交身份、行业
				   		   int shenfen = this.getParameterInt("sf");
				   		   int hangye = this.getParameterInt("hy");
						   if (hangye > 0 && shenfen > 0){
							   	   int sf = 0;
							   	   int hy = 0;
								   property = service.getProperty(" id=" + shenfen);
								   sf = property==null?0:property.getFlag();
								   property = service.getProperty(" id=" + hangye);
								   hy = property==null?0:property.getFlag();
								   if (sf!=3||hy!=4){
									   this.setAttribute("tip", "提交的参数错误.");
								   } else {
									   if (SqlUtil.exist("select user_id from credit_user_work where user_id=" + this.getLoginUser().getId(),5)){
										   SqlUtil.executeUpdate("update credit_user_work set identity=" + shenfen + ",trade=" + hangye + " where user_id=" + this.getLoginUser().getId(),5);
									   } else {
										   SqlUtil.executeUpdate("insert into credit_user_work (user_id,identity,trade) values(" + this.getLoginUser().getId() + "," + shenfen + "," + hangye + ")",5);
									   }
									   updateModifyTime(2);
									   if (modify==1){
										   response.sendRedirect("navi.jsp?t=1");
									   }else {
										   response.sendRedirect("select2.jsp?t=7");
									   }
								   	   return;
								   }   
						   } else {
							   response.sendRedirect("select2.jsp?t=6");
							   return;
						   }
					break;
			   		case 7:
						// 提交收入、梦想
				   		   int shouru = this.getParameterInt("sr");
				   		   int mengxiang = this.getParameterInt("mx");
						   if (shouru > 0 && mengxiang > 0){
							   	   int sr = 0;
							   	   int mx = 0;
								   property = service.getProperty(" id=" + shouru);
								   sr = property==null?0:property.getFlag();
								   property = service.getProperty(" id=" + mengxiang);
								   mx = property==null?0:property.getFlag();
								   if (sr!=5||mx!=6){
									   this.setAttribute("tip", "提交的参数错误.");
								   } else {
//									   if (SqlUtil.exist("select user_id from credit_user_work where user_id=" + this.getLoginUser().getId(),5)){
										   SqlUtil.executeUpdate("update credit_user_work set earning=" + shouru + ",dream=" + mengxiang + " where user_id=" + this.getLoginUser().getId(),5);
										   updateModifyTime(2);
//									   } else {
//										   this.setAttribute("tip", "您的数据不存在,请从头开始填写.");
//									   }
									   response.sendRedirect("navi.jsp?t=1");
								   	   return;
								   }   
						   } else {
							   response.sendRedirect("select2.jsp?t=7");
							   return;
						   }
					break;
			   		case 8:
						// 提交恋爱时长、埋单、是否抽烟
				   		   int lianai = this.getParameterInt("la");
				   		   int maidan = this.getParameterInt("md");
				   		   int chouyan = this.getParameterInt("cy");
						   if (lianai > 0 && maidan > 0 && chouyan > 0){
							   	   int la = 0;
							   	   int md = 0;
							   	   int cy = 0;
								   property = service.getProperty(" id=" + lianai);
								   la = property==null?0:property.getFlag();
								   property = service.getProperty(" id=" + maidan);
								   md = property==null?0:property.getFlag();
								   property = service.getProperty(" id=" + chouyan);
								   cy = property==null?0:property.getFlag();
								   if (la!=7||md!=8||cy!=9){
									   this.setAttribute("tip", "提交的参数错误.");
								   } else {
									   if (SqlUtil.exist("select user_id from credit_user_live where user_id=" + this.getLoginUser().getId(),5)){
										   SqlUtil.executeUpdate("update credit_user_live set love_time=" + lianai + ",bill=" + maidan + ",smoke=" + chouyan+ " where user_id=" + this.getLoginUser().getId(),5);
									   } else {
										   SqlUtil.executeUpdate("insert into credit_user_live (user_id,love_time,bill,smoke) values (" + this.getLoginUser().getId() + "," + lianai + "," + maidan + "," + chouyan + ")",5);
									   }
									   updateModifyTime(3);
									   if (modify==1){
										   response.sendRedirect("navi.jsp?t=2");
									   } else {
										   response.sendRedirect("in.jsp?t=1");
									   }
								   	   return;
								   }   
						   } else {
							   response.sendRedirect("select2.jsp?t=8");
							   return;
						   }
					break;
			   		case 9:
						// 提交婚姻状况、有无孩子
				   		   int hunyin = this.getParameterInt("hy");
				   		   int haizi = this.getParameterInt("hz");		   			
						   if (hunyin > 0 && haizi > 0){
							   	   int hy = 0;
							   	   int hz = 0;
								   property = service.getProperty(" id=" + hunyin);
								   hy = property==null?0:property.getFlag();
								   property = service.getProperty(" id=" + haizi);
								   hz = property==null?0:property.getFlag();
								   if (hy!=10||hz!=11){
									   this.setAttribute("tip", "提交的参数错误.");
								   } else {
//									   if (SqlUtil.exist("select user_id from credit_user_live where user_id=" + this.getLoginUser().getId(),5)){
										   SqlUtil.executeUpdate("update credit_user_live set marrage=" + hunyin + ",child=" + haizi + " where user_id=" + this.getLoginUser().getId(),5);
										   updateModifyTime(3);
//									   } else {
//										   this.setAttribute("tip", "您的数据不存在,请从头开始填写.");
//									   }
									   if (modify==1){
										   response.sendRedirect("navi.jsp?t=2");
									   } else {
										   response.sendRedirect("in.jsp?t=2");
									   }
								   	   return;
								   }   
						   } else {
							   response.sendRedirect("select2.jsp?t=9");
							   return;
						   }
					break;
			   		case 10:
						// 提交体形、外貌、魅力部位
					   	   int tixing = this.getParameterInt("tx");
					   	   int waimao = this.getParameterInt("wm");
					   	   int meili = this.getParameterInt("ml");
						   if (tixing > 0 && waimao > 0 && meili > 0){
							   	   int tx = 0;
							   	   int wm = 0;
							   	   int ml = 0;
								   property = service.getProperty(" id=" + tixing);
								   tx = property==null?0:property.getFlag();
								   property = service.getProperty(" id=" + waimao);
								   wm = property==null?0:property.getFlag();
								   property = service.getProperty(" id=" + meili);
								   ml = property==null?0:property.getFlag();
								   if (tx!=12||wm!=13||ml!=14){
									   this.setAttribute("tip", "提交的参数错误.");
								   } else {
									   if (SqlUtil.exist("select user_id from credit_user_looks where user_id=" + this.getLoginUser().getId(),5)){
										   SqlUtil.executeUpdate("update credit_user_looks set body_type=" + tixing + ",looks=" + waimao + ",charm=" + meili + " where user_id=" + this.getLoginUser().getId(),5);
									   } else {
										   SqlUtil.executeUpdate("insert into credit_user_looks (user_id,body_type,looks,charm) values (" + this.getLoginUser().getId() + "," + tixing + "," + waimao + "," + meili + ")",5);
									   }
									   updateModifyTime(4);
								   	   response.sendRedirect("navi.jsp?t=3");
								   	   return;
								   }   
						   } else {
							   response.sendRedirect("select2.jsp?t=10");
							   return;
						   }
					break;
					default:
						this.setAttribute("tip", "提交的参数错误.");
			   }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 判断文本框的输入
	public boolean validateInput(int type,int modify,HttpServletResponse response){
		try{
			switch (type){
			   case 1:
					// 提交我最喜欢的影星与歌手
					String yingxing = this.getParameterNoCtrl("yx");
					String geshou = this.getParameterNoCtrl("gs");
					yingxing = yingxing != null ? yingxing.trim() : ""; 
					geshou = geshou != null ? geshou.trim() : "";
					if ("".equals(yingxing) || yingxing.length() > 30){
						this.setAttribute("tip", "您没有输入喜欢的影星或输入的字符太长了.");
					} else if("".equals(geshou) || geshou.length() > 30){
						this.setAttribute("tip", "您没有输入喜欢的歌手或输入的字符太长了.");
					} else {
//						   if (SqlUtil.exist("select user_id from credit_user_live where user_id=" + this.getLoginUser().getId(),5)){
							   SqlUtil.executeUpdate("update credit_user_live set film_start='" + StringUtil.toSql(yingxing) + "',singer='" + StringUtil.toSql(geshou) + "' where user_id=" + this.getLoginUser().getId(),5);
							   updateModifyTime(3);
//						   } else {
//							   this.setAttribute("tip", "您的数据不存在,请从头开始填写.");
//						   }
						   if (modify == 1){
							   response.sendRedirect("navi.jsp?t=2");
						   } else {
							   response.sendRedirect("select2.jsp?t=9");
						   }
					   	   return true;
					}
				break;
		   		case 2:
					// 提交我喜欢的食物等等东西
					String shiwu = this.getParameterNoCtrl("sw");
					String yundong = this.getParameterNoCtrl("yd");
					String guanzhu = this.getParameterNoCtrl("gz");
					String shanchang = this.getParameterNoCtrl("sc");
					shiwu = shiwu != null ? shiwu.trim() : "";
					yundong = yundong != null ? yundong.trim() : "";
					guanzhu = guanzhu != null ? guanzhu.trim() : "";
					shanchang = shanchang != null ? shanchang.trim() : "";
					if ("".equals(shiwu) || shiwu.length() > 30){
						this.setAttribute("tip", "您没有输入喜欢的食物或输入的字符太长了.");
					} else if("".equals(yundong) || yundong.length() > 30){
						this.setAttribute("tip", "您没有输入喜欢的运动或输入的字符太长了.");
					} else if("".equals(guanzhu) || guanzhu.length() > 30){
						this.setAttribute("tip", "您没有输入关注或输入的字符的字符长太了.");
					} else if("".equals(shanchang) || shanchang.length() > 30){
						this.setAttribute("tip", "您没有输入擅长或输入的字符的字符长太了.");
					} else {
//						   if (SqlUtil.exist("select user_id from credit_user_live where user_id=" + this.getLoginUser().getId(),5)){
							   SqlUtil.executeUpdate("update credit_user_live set food='" + StringUtil.toSql(shiwu) + "',sport='" + StringUtil.toSql(yundong) + "',focus_on='" + StringUtil.toSql(guanzhu) +"',good_at='" + StringUtil.toSql(shanchang) + "' where user_id=" + this.getLoginUser().getId(),5);
							   updateModifyTime(3);
//						   } else {
//							   this.setAttribute("tip", "您的数据不存在,请从头开始填写.");
//						   }
					   	   response.sendRedirect("navi.jsp?t=2");
					   	   return true;
					}
				break;
		   		case 3:
		   			// 提交联系方式等
		   			String xingming = this.getParameterNoCtrl("xm");
		   			String lianxi = this.getParameterNoCtrl("lx");
		   			String shenfenzheng = this.getParameterNoCtrl("sfz");
		   			String dizhi = this.getParameterNoCtrl("dz");
		   			xingming = xingming != null ? xingming.trim() : "";
		   			lianxi = lianxi != null ? lianxi.trim() : "";
		   			shenfenzheng = shenfenzheng != null ? shenfenzheng.trim().toUpperCase() : "";
		   			dizhi = dizhi != null ? dizhi.trim() : "";
		   			
		   			int baomi = this.getParameterInt("bm");
	   				Pattern pattern = Pattern.compile("(1\\d{10})|(0\\d{2,3}-\\d{7,8})|(\\d{7,8})");
	   				Pattern pattern2 = Pattern.compile("(\\d{17}X)|(\\d{18})");
	   				Matcher isPhone = pattern.matcher(lianxi);
	   				Matcher isIdCard = pattern2.matcher(shenfenzheng);
		   			if ("".equals(xingming) || xingming.length() > 4){
		   				this.setAttribute("tip", "没有输入真实姓名或输入的太长了.");
		   			} else if (!isPhone.matches()){
		   				this.setAttribute("tip", "输入错误,只能输入固话或手机号.固话若有区号,请用\"-\"号区分.如\"010-12345678\"");
		   			} else if (!isIdCard.matches()){
		   				this.setAttribute("tip", "身份证号输入错误.");
		   			} else if ("".equals(dizhi) || dizhi.length() > 200){
		   				this.setAttribute("tip", "没有输入地址或输入的太长了.");
		   			} else {
				   		   if (baomi < 1 || baomi > 3){
				   			   baomi = 1;
				   		   }
						   if (SqlUtil.exist("select user_id from credit_user_contacts where user_id=" + this.getLoginUser().getId(),5)){
							   SqlUtil.executeUpdate("update credit_user_contacts set contacts='" + StringUtil.toSql(lianxi) + "',id_card='" + StringUtil.toSql(shenfenzheng) + "',address='" + StringUtil.toSql(dizhi) + "',true_name='" + StringUtil.toSql(xingming) + "',private=" + baomi + " where user_id=" + this.getLoginUser().getId(),5);
						   } else {
							   SqlUtil.executeUpdate("insert into credit_user_contacts (user_id,contacts,id_card,address,true_name,private) values ('" + this.getLoginUser().getId() + "','" + StringUtil.toSql(lianxi) + "','" + StringUtil.toSql(shenfenzheng) + "','" + StringUtil.toSql(dizhi) + "','" + StringUtil.toSql(xingming) + "'," + baomi + ")",5);
						   }
						   updateModifyTime(5);
					   	   response.sendRedirect("navi.jsp?t=4");
					   	   return true;
		   			}
//		   			response.sendRedirect("in.jsp?t=3&m=0");
//		   			return;
		   			break;
			default:
				this.setAttribute("tip", "提交的参数错误.");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/* 判断某一用户表是否完整填完。填完返回0，否则返回下一步的编号，错误返回-1
	 * id=1:userBase
	 * id=2:userWork
	 * id=3:userLive
	 * id=4:userLooks
	 * id=5:userContacts
	 */
	public int integrality(int id){
		if (id < 1 || id > 5){
			return -1;
		}
		switch (id){
			case 1:
				UserBase userBase = getUserBaseBean(this.getLoginUser().getId());
//				service.getUserBase(" user_id=" + this.getLoginUser().getId());
				if (userBase == null){
					return -1;
				} else {
					if (userBase.getBirYear() == 0){
						// 生日与身高没填
						return 1;
					} else if (userBase.getProvince() == 0){
						// 居住地
						return 2;
					} else if (userBase.getEducation() == 0){
						// 学历
						return 3;
					} else if (userBase.getPersonality() == 0){
						// 个性
						return 4;
					} else if (userBase.getBlood() == 0){
						// 血型
						return 5;
					} else {
						return 0;
					}
				}
			case 2:
				UserWork userWork = service.getUserWork(" user_id=" + this.getLoginUser().getId());
				if (userWork == null){
					return -1;
				} else {
					if (userWork.getIdentity() == 0){
						// 身份、行业
						return 6;
					} else if (userWork.getEarning() == 0){
						// 收入、职位
						return 7;
					} else {
						return 0;
					}
				}
			case 3:
				UserLive userLive = service.getUserLive(" user_id=" + this.getLoginUser().getId());
				if (userLive == null){
					return -1;
				} else {
					if (userLive.getLoveTime() == 0){
						// 恋爱时间、谁买单、抽烟
						return 8;
					} else if ("".equals(userLive.getFilmStart())){
						// 喜欢的影星、歌手 (返回in.jsp?t=1)
						return 1;
					} else if (userLive.getMarrage() == 0){
						// 婚姻、是否有孩子
						return 9;
					} else if ("".equals(userLive.getFood())){
						// 喜欢的食物、运动、关注、擅长 (in.jsp?t=2)
						return 2;
					} else {
						return 0;
					}
				}
			case 4:
				UserLooks userLooks = service.getUserLooks(" user_id=" + this.getLoginUser().getId());
				if (userLooks == null){
					return -1;
				} else {
					if (userLooks.getBodyType() == 0){
						// 体形、外貌、魅力
						return 10;
					} else {
						return 0;
					}
				}
			case 5:
				UserContacts userContacts = service.getUserContacts(" user_id=" + this.getLoginUser().getId());
				if (userContacts == null){
					return -1;
				} else {
					if ("".equals(userContacts.getTrueName())){
						// 姓名、身份证等 (in.jsp?t=3)
						return 3;
					} else {
						return 0;
					}
				}
			default:
				return -1;
		}
	}
	
	/*更新最后修改时间,并且检查是否需要加分(只加完整度的),根据id来判断要加分到哪一张表.
	 * 
	 * id=1:userBase
	 * id=2:userWork
	 * id=3:userLive
	 * id=4:userLooks
	 * id=5:userContacts
	 */
	public void updateModifyTime(int id){
		if (!SqlUtil.exist("select user_id from credit_user_info where user_id=" + this.getLoginUser().getId(),5)){
			UserInfo userInfo = new UserInfo();
			userInfo.setUserId(this.getLoginUser().getId());
			userInfo.setMobile("");
			userInfo.setPoint(0);
			userInfo.setFlag(0);
			service.addNewInfo(userInfo);
		} else {
			SqlUtil.executeUpdate("update credit_user_info set modify_time=now() where user_id=" + this.getLoginUser().getId(), 5);
		}
		// 加分(先在自己的表中加上分数，再在user_info表中记录相应的分数，以便加总分)
		switch (id){
		case 1:
			if (this.integrality(1)==0 && SqlUtil.getIntResult("select point from credit_user_base where user_id=" + this.getLoginUser().getId(), 5) == 0){
				SqlUtil.executeUpdate("update credit_user_base set point=8 where user_id=" + this.getLoginUser().getId(),5);
			    UserBase userBase = getUserBaseBean(this.getLoginUser().getId());
			    if (userBase != null){
				    userBase.setPoint(8);
				    userBaseCache.put(new Integer(userBase.getUserId()), userBase);
			    }
				SqlUtil.executeUpdate("update credit_user_info set intact_point=intact_point+8 where user_id=" + this.getLoginUser().getId(),5);
			}
			break;
		case 2:
			if (this.integrality(2)==0 && SqlUtil.getIntResult("select point from credit_user_work where user_id=" + this.getLoginUser().getId(), 5) == 0){
				SqlUtil.executeUpdate("update credit_user_work set point=8 where user_id=" + this.getLoginUser().getId(),5);
				SqlUtil.executeUpdate("update credit_user_info set intact_point=intact_point+8 where user_id=" + this.getLoginUser().getId(),5);
			}
			break;
		case 3:
			if (this.integrality(3)==0 && SqlUtil.getIntResult("select point from credit_user_live where user_id=" + this.getLoginUser().getId(), 5) == 0){
				SqlUtil.executeUpdate("update credit_user_live set point=8 where user_id=" + this.getLoginUser().getId(),5);
				SqlUtil.executeUpdate("update credit_user_info set intact_point=intact_point+8 where user_id=" + this.getLoginUser().getId(),5);
			}
			break;
		case 4:
			if (this.integrality(4)==0 && SqlUtil.getIntResult("select point from credit_user_looks where user_id=" + this.getLoginUser().getId(), 5) == 0){
				SqlUtil.executeUpdate("update credit_user_looks set point=8 where user_id=" + this.getLoginUser().getId(),5);
				SqlUtil.executeUpdate("update credit_user_info set intact_point=intact_point+8 where user_id=" + this.getLoginUser().getId(),5);
			}
			break;
		case 5:
			if (this.integrality(5)==0 && SqlUtil.getIntResult("select point from credit_user_contacts where user_id=" + this.getLoginUser().getId(), 5) == 0){
				SqlUtil.executeUpdate("update credit_user_contacts set point=8 where user_id=" + this.getLoginUser().getId(),5);
				SqlUtil.executeUpdate("update credit_user_info set intact_point=intact_point+8 where user_id=" + this.getLoginUser().getId(),5);
			}
			break;
		}
	}
	
	// 修改填写
	public int modifyIn(int type){
		 int backId = -1;
		 switch (type){
			case 1:
				String yingxing = this.getParameterNoCtrl("yx").trim();
				if ("".equals(yingxing) || yingxing.length() > 30){
					request.setAttribute("tip", "没有输入,或输入的字符太长了.");
				} else {
					SqlUtil.executeUpdate("update credit_user_live set film_start='" + StringUtil.toSql(yingxing) + "' where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(3);
					backId = 2;
				}
				break;
			case 2:
				String gexing = this.getParameterNoCtrl("gs").trim();
				if ("".equals(gexing) || gexing.length() > 30){
					request.setAttribute("tip", "没有输入,或输入的字符太长了.");
				} else {
					SqlUtil.executeUpdate("update credit_user_live set singer='" + StringUtil.toSql(gexing) + "' where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(3);
					backId = 2;
				}
				break;
			case 3:
				String shiwu = this.getParameterNoCtrl("sw").trim();
				if ("".equals(shiwu) || shiwu.length() > 30){
					request.setAttribute("tip", "没有输入,或输入的字符太长了.");
				} else {
					SqlUtil.executeUpdate("update credit_user_live set food='" + StringUtil.toSql(shiwu) + "' where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(3);
					backId = 2;
				}
				break;
			case 4:
				String yundong = this.getParameterNoCtrl("yd").trim();
				if ("".equals(yundong) || yundong.length() > 30){
					request.setAttribute("tip", "没有输入,或输入的字符太长了.");
				} else {
					SqlUtil.executeUpdate("update credit_user_live set sport='" + StringUtil.toSql(yundong) + "' where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(3);
					backId = 2;
				}
				break;
			case 5:
				String guanzhu = this.getParameterNoCtrl("gz").trim();
				if ("".equals(guanzhu) || guanzhu.length() > 30){
					request.setAttribute("tip", "没有输入,或输入的字符太长了.");
				} else {
					SqlUtil.executeUpdate("update credit_user_live set focus_on='" + StringUtil.toSql(guanzhu) + "' where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(3);
					backId = 2;
				}
				break;
			case 6:
				String shanchang = this.getParameterNoCtrl("sc").trim();
				if ("".equals(shanchang) || shanchang.length() > 30){
					request.setAttribute("tip", "没有输入,或输入的字符太长了.");
				} else {
					SqlUtil.executeUpdate("update credit_user_live set good_at='" + StringUtil.toSql(shanchang) + "' where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(3);
					backId = 2;
				}
				break;
			case 7:
				String xingming = this.getParameterNoCtrl("xm").trim();
				if ("".equals(xingming) || xingming.length() > 30){
					request.setAttribute("tip", "没有输入,或输入的字符太长了.");
				} else {
					SqlUtil.executeUpdate("update credit_user_contacts set true_name='" + StringUtil.toSql(xingming) + "' where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(5);
					backId = 4;
				}
				break;
			case 8:
				String lianxi = this.getParameterNoCtrl("lx");
   				Pattern pattern = Pattern.compile("(1\\d{10})|(0\\d{2,3}-\\d{7,8})|(\\d{7,8})");
   				Matcher isPhone = pattern.matcher(lianxi);
				if (!isPhone.matches()){
					request.setAttribute("tip", "输入错误,只能输入固话或手机号.固话若有区号,请用\"-\"号区分.如\"010-12345678\"");
				} else {
					SqlUtil.executeUpdate("update credit_user_contacts set contacts='" + StringUtil.toSql(lianxi) + "' where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(5);
					backId = 4;
				}
				break;
			case 9:
				String shenfenzheng = this.getParameterNoCtrl("sfz").trim().toUpperCase();
				Pattern pattern2 = Pattern.compile("(\\d{17}X)|(\\d{18})");
				Matcher isIdCard = pattern2.matcher(shenfenzheng);
				if (!isIdCard.matches()){
					request.setAttribute("tip", "身份证号输入错误.");
				} else {
					SqlUtil.executeUpdate("update credit_user_contacts set id_card='" + StringUtil.toSql(shenfenzheng) + "' where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(5);
					backId = 4;
				}
				break;
			case 10:
				String dizhi = this.getParameterNoCtrl("dz").trim();
				if ("".equals(dizhi) || dizhi.length() > 200){
					request.setAttribute("tip", "没有输入,或输入的字符太长了.");
				} else {
					SqlUtil.executeUpdate("update credit_user_contacts set address='" + StringUtil.toSql(dizhi) + "' where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(5);
					backId = 4;
				}
				break;
			case 11:
				int baomi = this.getParameterInt("bm");
				if (baomi < 0 || baomi > 3){
					request.setAttribute("tip", "参数错误.");
				} else {
					SqlUtil.executeUpdate("update credit_user_contacts set private=" + baomi + " where user_id=" + this.getLoginUser().getId(),5);
					updateModifyTime(5);
					backId = 4;
				}
				break;
			}
		 return backId;
	}
	
	// 计算网站信用担保
	public int getNetPoint(UserStatusBean userStatus,ForumUserBean forumUser,TongUserBean tongUser){
		int point = 0;
		if (userStatus == null){
			return point;
		} else {
			// 在线时长超过168小时
			if (userStatus.getOnlineTime() > 168){
				point += 3;
			}
			// 等级超过15
			if (userStatus.getRank() > 15){
				point += 3;
			}
		}
		// 是帮会的帮主
		if (tongUser != null && tongUser.getMark() == 2){
			point += 3;
		}
		// 论坛经验大于15
		if (forumUser != null && forumUser.getExp() > 100){
			point ++;
		}
		SqlUtil.executeUpdate("update credit_user_info set net_point=" + point + " where user_id=" + this.getLoginUser().getId(),5);
		return point;
	}
	
	// 用玩家打分,正确返回0,错误返回-1
	public int doPlayerGrade(int uid,int score){
		UserInfo userInfo = service.getUserInfo(" user_id=" + uid);
//		UserGrade userGrade = this.service.getUserGrade(" user_id=" + uid);
		// 该玩家第一次被打分
		if (userInfo == null){
			userInfo = new UserInfo();
			userInfo.setUsers("");
			SqlUtil.executeUpdate("insert into credit_user_info (user_id,create_time,modify_time,users) values (" + uid + ",now(),now(),'')", 5);
		}
		// if (!isContacts(userInfo.getUsers(),String.valueOf(this.getLoginUser().getId())))if (userInfo.getUsers().indexOf(this.getLoginUser().getId() + ",") > 0)
		if (!isContacts(userInfo.getUsers(),String.valueOf(this.getLoginUser().getId()))){
			// 没打过,打分.
			SqlUtil.executeUpdate("update credit_user_info set player_point=player_point+" + score + ",player_count=player_count+1,users='"  + this.getLoginUser().getId() + "," + userInfo.getUsers() + "' where user_id=" + uid,5);
//			SqlUtil.executeUpdate("update credit_user_grade set users='"  + this.getLoginUser().getId() + "," + userGrade.getUsers() + "' where user_id=" + uid, 5);
		} else {
			// 已经打过一次分了
			return -1;
		}
		return 0;
	}
	
	/*
	 * 检查str1中是否有str2
	 * str1的格式：xxx,xxx,xxx
	 */
	public boolean isContacts(String str1,String str2){
		if ("".equals(str1) || "".equals(str2)){
			return false;
		}
		String tmp[] = str1.split(",");
		for (int i = 0 ; i < tmp.length ; i++){
			if (tmp[i].equals(str2) ){
				return true;
			}
		}
		return false;
	}
	
	// 上传图片
	public boolean updatePic(SmartUpload smUpload){
		String fileName = "";
		try {
			File upFile = smUpload.getFiles().getFile(0);
			if ("".equals(upFile.getFileName())){
				this.setAttribute("tip", "请选择一张照片.");
				return false;
			}
			fileName = System.currentTimeMillis() + "." + upFile.getFileExt();
			upFile.saveAs(ATTACH_ROOT + "/" + fileName,SmartUpload.SAVE_PHYSICAL);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SmartUploadException e) {
			e.printStackTrace();
		}
		SqlUtil.executeUpdate("update credit_user_base set photo='o.gif' where user_id=" + this.getLoginUser().getId(), 5);
		UserBase userBase = getUserBaseBean(this.getLoginUser().getId());
		// 删除以前的照片
		if(userBase.getPhoto() != null && userBase.getPhoto().length() > 5) {
			java.io.File pic = new java.io.File(ATTACH_ROOT + "/" + userBase.getPhoto()); // "E:\\eclipse\\workspace\\joycool-portal\\img/home/"
			if (pic.exists())
				pic.delete();
		}
		
		if (userBase != null){
			 userBase.setPhoto("o.gif");
			 userBaseCache.put(new Integer(userBase.getUserId()), userBase);
		} else {
			userBase = new UserBase();
			userBase.setUserId(this.getLoginUser().getId());
			userBase.setPhoto("o.gif");
			userBase.setGender(UserInfoUtil.getUser(this.getLoginUser().getId()).getGender());
			SqlUtil.executeUpdate("insert into credit_user_base set user_id=" + this.getLoginUser().getId() + ",photo='o.gif',gender=" + userBase.getGender(), 5);
		}
		SqlUtil.executeUpdate("insert into img_check set id2=" + this.getLoginUser().getId() + ",type=7,create_time=now(),file='" + fileName + "',bak=''");

		return true;
	}
	
	/**
	 * 点评人列表.
	 * maxCount：需要显示的点评人的人数。如果输入-1，则返回全部点评人。
	 */
	public List getMoreGradeUserList(int userId,int maxCount){
		List list = new ArrayList();
//		UserGrade grade = service.getUserGrade(" user_id=" + userId);
		UserInfo userInfo = service.getUserInfo(" user_id=" + userId);
		if (userInfo != null && !"".equals(userInfo.getUsers())){
			String tmp[] = userInfo.getUsers().split(",");
			list = Arrays.asList(tmp);
			if (maxCount != -1){
				list = DSUtil.sublist(list, 0, maxCount);
			}	
		}
		return list;
	}
	
	// 点评人列表字符串(横向)
	public String getGradeUserString(int userId,int maxCount){
		StringBuilder builder = new StringBuilder();
		String tmp = "";
		List list = getMoreGradeUserList(userId,maxCount);
		if (list.size() == 0){
			tmp = "暂无";
		} else {
			UserBean user = null;
			for (int i=0;i<list.size();i++){
				userId = StringUtil.toInt(list.get(i).toString());
				if (userId > 0){
					user = UserInfoUtil.getUser(userId);
					if (user != null){
						builder.append(user.getNickNameWml() + ",");
					}
				}
			}
			tmp = builder.substring(0, builder.length()-1);
		}
		return tmp;
	}

	// 返回所在地字符串
	public String getPlace(UserBase userBase){
		if (userBase == null || userBase.getProvince() == 0 || userBase.getCity() ==0){
			return "";
		} else {
			CreditProvince cp = service.getProvince(" id=" + userBase.getProvince());
			CreditCity cc = service.getCity(" id=" + userBase.getCity());
			if (cp != null && cc != null){
				return cp.getProvince() + "," + cc.getCity();
			} else {
				return "";
			}
		}
	}
	
	/**
	 * @author maning
	 * @param UserBase:登陆用户自己的基本信息
	 * @param count:一次查找出来的信息总数
	 * @param randomStart:随机的超始位置
	 * @show 搜友:是否有异性>头像>年龄>地区
	 */
	public List searchFriend(UserBase userBase,int count,int randomStart){
		/*
		 * [此思路暂时作废→]先找出90个有照片的异性好友。之后再遍历这个list，找出最符合规定的。若没有，就只能随便来了。
		 */
		List tmpList = new ArrayList();
		List list = new ArrayList();
		int gender = 0;
//		String tmp = "";
		gender = UserInfoUtil.getUser(this.getLoginUser().getId()).getGender() == 0?1:0;
		// 随机给他25个人吧
		if (userBase == null || (userBase.getProvince() == 0 && userBase.getBirYear() == 0)){
			tmpList = service.searchUserBase(" gender=" + gender + " and  photo<>'' limit " + randomStart + "," + count);
//			count = count - tmpList.size();
//			if (tmpList.size() == count){
//				return tmpList;
//			} else {
//				list.addAll(tmpList);
//			}
//			tmp = listToString(list);
//			tmp = "".equals(tmp)?"''":tmp;
//			if (count > 0){
//				tmpList = service.searchUserBase(" gender= " + gender + " and user_id not in (" + tmp +") limit " + randomStart + "," + count);
//				list.addAll(tmpList);
//				return list;
//			}
			return tmpList;
		}
		int randomCount = 1;//搜索的次数
		int tmpId = 0;
//		int ageUpper = 0;	// 年龄上限
//		int ageLower = 0;	// 年龄下限
		if (userBase == null){
			return list;
		}
		if (count <= 0){
			count = 0;
		}
		if (randomStart < 0){
			randomStart = 0;
		}
		UserBase userBase2 = null;
		// 异性
//		if (userBase.getGender() == 0){
//			gender = 1;
//		} else {
//			gender = 0;
//		}
//************************************以下代码废弃************************************		
		// 找出一定数量的异性
		tmpList = service.searchUserBase(" gender=" + gender + " limit " + randomStart + "," + count);
		
		while(randomCount <= 3){
			for (int i = 0 ; i < tmpList.size() ; i++){
				tmpId = StringUtil.toInt(tmpList.get(i)!=null?tmpList.get(i).toString():"0");
				if (tmpId > 0){
					userBase2 = getUserBaseBean(tmpId);
					if (userBase2 != null){
						switch (randomCount){
							case 1:		// 第一轮搜索:有头像，异性，地区相同,年龄上下相差5岁的
								if (!"".equals(userBase2.getPhoto()) && userBase2.getProvince() == userBase.getProvince() &&  (userBase2.getAge() > userBase.getAge() - 5 && userBase2.getAge() < userBase.getAge() + 5)){
									list.add(new Integer(tmpId));
									tmpList.remove(i);
									i--;
								}
								break;
							case 2:		// 第二轮搜索:有头像，异性，地区相同
								if (!"".equals(userBase2.getPhoto()) && userBase2.getProvince() == userBase.getProvince()){
									list.add(new Integer(tmpId));
									tmpList.remove(i);
									i--;
								}
								break;
							case 3:		// 第三轮搜索:有头像，异性
								if (!"".equals(userBase2.getPhoto())){
									list.add(new Integer(tmpId));
									tmpList.remove(i);
									i--;
								}
								break;
						}
					}
				}
			}
//			System.out.println("第 " + randomCount + " 轮搜索完毕,共找到 " + list.size() + " 人.");
			if (list.size() >= 25){
				return list;
			}
			randomCount++;
		}
		// 还不符合要求,就顺序填满25个...
		int endNum = tmpList.size() > 25 ? 25-list.size() : tmpList.size();
		for (int i = 0 ; i < endNum ; i++){
			tmpId = StringUtil.toInt(tmpList.get(i)!=null?tmpList.get(i).toString():"0");
			list.add(new Integer(tmpId));
		}
//		System.out.println("人数不够,补全,最终找到 " + list.size() + " 人.");
		return list;
//************************************以上代码废弃************************************
//		ageUpper = userBase.getBirYear() + 5;
//		ageLower = userBase.getBirYear() - 5;
//		
//		while (randomCount <= 3){
//			switch (randomCount){
//				case 1:
//					tmpList = service.searchUserBase(" gender=" + gender + " and photo<> '' and province=" + userBase.getProvince() + " and bir_year>=" + ageLower + " and bir_year<=" + ageUpper + " limit " + randomStart + "," + count);
////					System.out.println("第一轮找到:" + tmpList.size());
//					break;
//				case 2:
//					tmp = listToString(list);
//					tmp = "".equals(tmp)?"''":tmp;
//					tmpList = service.searchUserBase(" gender=" + gender + " and photo<>'' and province=" + userBase.getProvince() + " and user_id not in (" + tmp + ") limit " + randomStart + "," + count);
////					System.out.println("第二轮找到:" + tmpList.size());
//					break;
//				case 3:
//					tmp = listToString(list);
//					tmp = "".equals(tmp)?"''":tmp;
//					tmpList = service.searchUserBase(" gender=" + gender + " and photo<>'' and user_id not in (" + tmp + ") limit " + randomStart + "," + count);
////					System.out.println("第三轮找到:" + tmpList.size());
//					break;
//			}
//			randomCount++;
//			if (tmpList.size() == count){
//				return tmpList;
//			} else {
//				count = count - tmpList.size();
//				list.addAll(tmpList);
//			}
//		}
//		if (count > 0){
//			// 第四轮搜索,补全,只搜索异性
//			tmp = listToString(list);
//			tmp = "".equals(tmp)?"''":tmp;
//			tmpList = service.searchUserBase(" gender=" + gender + " and user_id not in (" + tmp + ") limit " + randomStart + "," + count);
////			System.out.println("最后一轮找到:" + tmpList.size());
////			if (tmpList.size() == count){
////				list.addAll(tmpList);
////				return list;
////			} else {
////				list.addAll(tmpList);
////			}
//			list.addAll(tmpList);
//		}
//		return list;
	}
	
//	// 把一个list改成"XX,XX,XX..."的形式
//	public static String listToString(List list){
//		StringBuilder sb = new StringBuilder();
//		if (list == null || list.size() == 0){
//			return sb.toString();
//		}
//		for (int i=0;i<list.size();i++){
//			sb.append(list.get(i).toString());
//			sb.append(",");
//		}
//		return sb.substring(0, sb.length()-1);
//	}
	
	// 判断一个日期是否存在
	public boolean isDateExist(int year,int month,int day){
		try{
			Calendar c = Calendar.getInstance();
			c.setLenient(false);
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month - 1);
			c.set(Calendar.DATE, day);
			c.get(Calendar.YEAR);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
