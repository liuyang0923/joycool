package jc.news.nba;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.*;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.*;

public class NbaAction extends CustomAction {
	public NbaAction(HttpServletRequest request) {
		super(request);
	}

	public static NbaService service = new NbaService();
	public GregorianCalendar cal = new GregorianCalendar();
	private int[] monthDay = { 30, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30,
			31 };

	String changeYear(int y) {
		String year = "";
		if (y / 10 < 1)
			year = "200" + y;
		else if (y / 100 < 1)
			year = "20" + y;
		else
			return String.valueOf(y);
		return year;
	}

	String changeMonth(int m) {
		String month = "";
		if (m / 10 < 1)
			month = "0" + m;
		else
			return String.valueOf(m);
		return month;
	}

	String changeDay(int d) {
		String day = "";
		if (d / 10 < 1)
			day = "0" + d;
		else
			return String.valueOf(d);
		return day;
	}

	private boolean isLeap(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
			return true;
		else
			return false;
	}

	private String getNow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	private String getDate() {
		int y = this.getParameterInt("y");
		int m = this.getParameterInt("m");
		int d = this.getParameterInt("d");
		int mid = this.getParameterInt("maid");
		String ld = this.getParameterString("ld");
		String date = "";
		if (ld != null) {
			return ld;
		}
		if (mid > 0) {
			BeanMatch bm = this.getMatchById(mid);
			if (bm != null) {
				try {
					return this.format(bm.getStartTime(), "yyyy-MM-dd");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				return null;
		}
		if (y > 0 && m > 0) {
			date = changeYear(y) + "-" + changeMonth(m) + "-" + changeDay(d);
			return date;
		} else
			return null;
	}

	public int totalday(int y, int m) {
		if (m == 2 && isLeap(Integer.parseInt(changeYear(y))))
			return 29;
		else
			return monthDay[m];
	}

	public String format(long l, String fmt) throws Exception {
		SimpleDateFormat sdft = new SimpleDateFormat(fmt);
		return sdft.format(new Date(l)).toString();
	}

	public BeanMatch changeMatch() {
		int mid = this.getParameterInt("chgemid");
		if (mid > 0)
			return service.getMchById(mid);
		else
			return null;
	}

	public BeanMatch getMatchById(int mid) {
		if (mid > 0)
			return service.getMchById(mid);
		else
			return null;
	}

	public List getAllMch() {
		return service.getMatchList(" del=0 order by start_time desc");
	}

	public int getShow() {
		int y = this.getParameterInt("y");
		int m = this.getParameterInt("m");
		int d = this.getParameterInt("d");
		int mid = this.getParameterInt("maid");
		String ld = this.getParameterString("ld");
		if (mid > 0)
			return -1;
		if (ld != null)
			return -1;
		if ("post".equalsIgnoreCase(request.getMethod())) {
			if (y > 0) {
				if (y > 3000 || y < 1900)
					return 1;
				if (m <= 0 || m > 12)
					return 2;
				if (d <= 0 || d > this.totalday(y, m))
					return 3;
			} else if (d > 0) {
				if (y <= 0 || y > 3000)
					return 1;
				if (m <= 0 || m > 12)
					return 2;
			} else if (m > 0 || m > 12) {
				if (y <= 0 || y > 3000)
					return 1;
				if (d <= 0 || d > this.totalday(y, m))
					return 3;
			} else {
				return 1;
			}
		} else {
			if (d > 0) {
				if (y <= 0 || y > 3000)
					return 1;
				if (m <= 0 || m > 12)
					return 2;
			} else if (m > 0) {
				if (y <= 0 || y > 3000)
					return 1;
				if (d <= 0 || d > totalday(y, m))
					return -1;
			} else if (y > 0) {
				if (m <= 0 || m > 12)
					return 2;
				if (d <= 0 || d > totalday(y, m))
					return -1;
			}
		}
		if (y > 0 && m > 0 && d > 0) {
			return -1;
		}
		return 0;
	}

	public List getMLByDate() {
		List matchList = null; 
		if (getShow() < 0) {
			String date = getDate();
			if (date != null) {
				matchList = service.getMatchList("del=0 and left(start_time,10)=left('"+ date + "',10) order by start_time desc");
				if (matchList.size() != 0 && matchList.size() < 10) 
					matchList = service.getMatchList("del=0 and left(start_time,10) <= left('"+date+"',10) order by start_time desc limit 10");
			}	
		} else {
			matchList = service.getMatchList("del=0 and left(start_time,10)=left(now(),10) order by start_time desc");
			if (matchList.size() != 0 && matchList.size() < 10) 
				matchList = service.getMatchList("del=0 and left(start_time,10) <= left(now(),10) order by start_time desc limit 10");
		}
		return matchList;
	}
	
	public List getMLByDate2() {
		List matchList = null; 
		if (getShow() < 0) {
			String date = getDate();
			if (date != null) {
				matchList = service.getMatchList("del=0 and left(start_time,10) <= left('"+date+"',10) order by start_time desc limit 10");
			}
		} else {
			matchList = service.getMatchList("del=0 and left(start_time,10) <= left(now(),10) order by start_time desc limit 10");
		}
		return matchList;
	}

	public int goBack(int mid) {
		int uid = this.getLoginUser().getId();
		String cont = this.getParameterNoEnter("cont");
		if (cont == null || "".equals(cont)) {
			return 1;// 输入不能为空哦!
		} else if (cont.length() > 50) {
			return 2;// 长度不能超过255个字
		} else if (!this.isCooldown("chat", 10000)) {
			return 3;// 操作太快了
		} else {
			if (mid > 0) {
				service
						.upd("update nba_match set sum_reply=sum_reply+1 where id="
								+ mid);
				service
						.upd("insert into nba_reply (create_time,user_id,match_id,cont)values(now(),"
								+ uid
								+ ","
								+ mid
								+ ",'"
								+ StringUtil.toSql(cont) + "')");
				return 4;
			} else
				return -1;
		}
	}

	public boolean updAlive(int mid, HttpServletResponse response) {
		int del = this.getParameterInt("del");
		if (this.getParameterString("clear") != null) {
			service.upd("update nba_match set sum_live = 0 where id=" + mid);
			return service.upd("update nba_live set del=1 where match_id="
					+ mid);
		} else if (del > 0) {
			BeanMatch bm = this.getMatchById(mid);
			if (bm.getSumLive() >= 1)
				service
						.upd("update nba_match set sum_live = sum_live - 1 where id="
								+ mid);
			return service.upd("update nba_live set del=1 where id=" + del);
		} else {
			if ("post".equalsIgnoreCase(request.getMethod())) {
				String cont = this.getParameterNoEnter("cont");
				String code = this.getParameterNoEnter("code");
				String code2 = this.getParameterNoEnter("code2");
				String lt = this.getParameterNoEnter("lt");
				String lt2 = this.getParameterNoEnter("lt2");
				int part = this.getParameterInt("part");
				if (cont != null && !"".equals(cont) && cont.length() <= 100) {
					if (code2 != null && !code2.equals("") && lt2 != null
							&& !lt2.equals("")) {
						service
								.upd("update nba_match set sum_live = sum_live + 1, code='"
										+ StringUtil.toSql(code2)
										+ "',left_time='"
										+ StringUtil.toSql(lt2)
										+ "' where id="
										+ mid);
						service
								.upd("insert into nba_live (cont,match_id,create_time) values('"
										+ StringUtil.toSql(cont)
										+ ","
										+ StringUtil.toSql(code2)
										+ ","
										+ StringUtil.toSql(lt2)
										+ "',"
										+ mid
										+ ",now())");
					} else if (code2 != null && !"".equals(code2)) {
						service
								.upd("update nba_match set sum_live = sum_live + 1, code='"
										+ StringUtil.toSql(code2)
										+ "' where id=" + mid);
						service
								.upd("insert into nba_live (cont,match_id,create_time) values('"
										+ StringUtil.toSql(cont)
										+ ","
										+ StringUtil.toSql(code2)
										+ "',"
										+ mid
										+ ",now())");
					} else if (lt2 != null && !"".equals(lt2)) {
						service
								.upd("update nba_match set sum_live = sum_live + 1, left_time='"
										+ StringUtil.toSql(lt2)
										+ "' where id="
										+ mid);
						service
								.upd("insert into nba_live (cont,match_id,create_time) values('"
										+ StringUtil.toSql(cont)
										+ " "
										+ StringUtil.toSql(lt2)
										+ "',"
										+ mid
										+ ",now())");
					} else {
						service
								.upd("update nba_match set sum_live = sum_live + 1 where id="
										+ mid);
						service
								.upd("insert into nba_live (cont,match_id,create_time) values('"
										+ StringUtil.toSql(cont)
										+ "',"
										+ mid
										+ ",now())");
					}
				} else if (code != null && !"".equals(code) && lt != null
						&& !lt.equals("")) {
					service
							.upd("insert into nba_live (cont,match_id,create_time) values('当前的比分为"
									+ StringUtil.toSql(code)
									+ ",距离本节结束还有"
									+ StringUtil.toSql(lt)
									+ "',"
									+ mid
									+ ",now())");
					service
							.upd("update nba_match set sum_live = sum_live + 1, code='"
									+ StringUtil.toSql(code)
									+ "' where id="
									+ mid);
				} else if (code != null && !"".equals(code)) {
					service
							.upd("insert into nba_live (cont,match_id,create_time) values('当前的比分为"
									+ StringUtil.toSql(code)
									+ "',"
									+ mid
									+ ",now())");
					service
							.upd("update nba_match set sum_live = sum_live + 1, code='"
									+ StringUtil.toSql(code)
									+ "',left_time='"
									+ StringUtil.toSql(lt)
									+ "' where id="
									+ mid);
				} else if (lt != null && !"".equals(lt)) {
					service
							.upd("insert into nba_live (cont,match_id,create_time) values('距离本节结束还有"
									+ StringUtil.toSql(lt)
									+ "',"
									+ mid
									+ ",now())");
					service
							.upd("update nba_match set sum_live = sum_live + 1, left_time='"
									+ StringUtil.toSql(lt)
									+ "' where id="
									+ mid);
				}
				if (part > 0) {
					service.upd("update nba_match set part=" + part
							+ " where id=" + mid);
				}
				try {
					response.sendRedirect("alive.jsp?mid=" + mid);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public int updSupport(int mid) {
		int uid = this.getLoginUser().getId();
		int spt = this.getParameterInt("spt");
		if (this.getSupportById(uid, mid) == null) {
			if (spt == 1) {
				service
						.upd("update nba_match set support_1=support_1+1 where id="
								+ mid);
				service
						.upd("insert into nba_support (match_id,user_id,create_time) values("
								+ mid + "," + uid + ",now())");
				return 1;
			} else if (spt == 2) {
				service
						.upd("update nba_match set support_2=support_2+1 where id="
								+ mid);
				service
						.upd("insert into nba_support (match_id,user_id,create_time) values("
								+ mid + "," + uid + ",now())");
				return 1;
			} else {
				return -3;// 支持不合法
			}
		} else {
			return -2;// 已经支持过了
		}
	}

	public boolean updateMatch() {
		int del = this.getParameterInt("del");
		int updId = this.getParameterInt("updId");
		int oId = this.getParameterInt("oId");
		if (del > 0)
			return service.upd("update nba_match set del=1 where id = " + del);
		else if (updId > 0)
			return service.upd("update nba_match set static_value=2 where id="
					+ updId);
		else if (oId > 0)
			return service.upd("update nba_match set static_value=1 where id="
					+ oId);
		else if ("post".equalsIgnoreCase(request.getMethod())) {
			String tms1 = this.getParameterNoEnter("tm1");
			String tms2 = this.getParameterNoEnter("tm2");
			String sttm = this.getParameterNoEnter("sttm");
			String code = this.getParameterNoEnter("code");
			int sta = this.getParameterInt("sta");
			int mid = this.getParameterInt("mid");
			if (tms1 != null && tms2 != null && sttm != null
					&& !"".equals(tms1) && !"".equals(tms2) && !"".equals(sttm)) {
				String[] dt = sttm.split("/");
				if (dt != null && dt.length == 2) {
					String[] tim = dt[0].split("-");
					if (tim.length == 3 && StringUtil.toInt(tim[0]) > 0
							&& StringUtil.toInt(tim[1]) > 0
							&& StringUtil.toInt(tim[2]) > 0) {
						if (!"".equals(code) && sta > -1 && mid > 0) {
							return service.upd("update nba_match set team1='"
									+ StringUtil.toSql(tms1) + "',team2='"
									+ StringUtil.toSql(tms2) + "',code='"
									+ StringUtil.toSql(code) + "',start_time='"
									+ StringUtil.toSql(sttm)
									+ "',static_value=" + sta + " where id="
									+ mid);
						} else {
							return service
									.upd("insert into nba_match (team1,team2,start_time,create_time) values('"
											+ StringUtil.toSql(tms1)
											+ "','"
											+ StringUtil.toSql(tms2)
											+ "','"
											+ StringUtil.toSql(sttm)
											+ "',now())");
						}
					} else
						return false;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

	public boolean updNews(HttpServletResponse response) {
		int chgId = this.getParameterInt("chgId");
		int del = this.getParameterInt("del");
		if (del != -1)
			service.upd("delete from nba_news where id=" + del);
		String view = this.getParameterNoEnter("view");
		String name = this.getParameterNoEnter("name");
		String cont = this.getParameterString("cont");
		String[] kind = this.request.getParameterValues("kind");
		if ("post".equalsIgnoreCase(request.getMethod()) && name != null
				&& cont != null && !"".equals(name) && !"".equals(cont)) {
			if (chgId > 0)
				service.upd("delete from nba_news where id=" + chgId);
			if (kind != null) {
				for (int i = 0; i < kind.length; i++) {
					if (view != null && !"".equals(view) && view.length() < 3)
						service
								.upd("insert into nba_news (create_time,view,name,cont,flag)values(now(),'"
										+ StringUtil.toSql(view)
										+ "','"
										+ StringUtil.toSql(name)
										+ "','"
										+ StringUtil.toSql(cont)
										+ "',"
										+ Integer.parseInt(kind[i]) + ")");
					else
						service
								.upd("insert into nba_news (create_time,name,cont,flag)values(now(),'"
										+ StringUtil.toSql(name)
										+ "','"
										+ StringUtil.toSql(cont)
										+ "',"
										+ Integer.parseInt(kind[i]) + ")");
				}
			}
			try {
				response.sendRedirect("news.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public boolean instRank() {
		String[] rank = this.request.getParameterValues("loc");
		String s = null;
		if (rank != null && rank.length > 0) {
			for (int i = 0; i < rank.length; i++) {
				s = rank[i];
				if (s == null || s.equals(""))
					return false;
			}
			service.upd("update nba_rank set del=1");
			for (int i = 0; i < rank.length; i++) {
				s = rank[i];
				if (i >= 6)
					service
							.upd("insert into nba_rank (create_time,flag,loc,cont)values(now(),1,"
									+ i
									+ ",'"
									+ StringUtil.toSql(rank[i])
									+ "')");
				else
					service
							.upd("insert into nba_rank (create_time,loc,cont)values(now(),"
									+ i
									+ ",'"
									+ StringUtil.toSql(rank[i])
									+ "')");
			}
		} else
			return false;
		return true;
	}

	public boolean updReply() {
		int del = this.getParameterInt("del");
		int mid = this.getParameterInt("mid");
		if (del > 0 && mid > 0) {
			BeanReply br = this.getReply(del);
			if (br != null && br.getMatchId() == mid) {
				BeanMatch bm = this.getMatchById(mid);
				if (bm != null && bm.getSumReply() >= 1)
					service
							.upd("update nba_match set sum_reply=sum_reply-1 where id="
									+ mid);
				return service.upd("delete from nba_reply where id=" + del);
			}
		}
		return false;
	}

	public boolean updReply(int mid, HttpServletResponse response) {
		int del = this.getParameterInt("del");
		if (del > 0) {
			BeanMatch bm = this.getMatchById(mid);
			if (bm != null && bm.getSumReply() >= 1)
				service
						.upd("update nba_match set sum_reply=sum_reply-1 where id="
								+ mid);
			return service.upd("delete from nba_reply where id=" + del);
		}
		int uid = this.getParameterInt("uid");
		String cont = this.getParameterNoEnter("cont");
		if (uid > -1 && cont != null && !"".equals(cont)
				&& cont.length() <= 255) {
			cont = this.getParameterString("cont");
			service.upd("update nba_match set sum_reply=sum_reply+1 where id="
					+ mid);
			service
					.upd("insert into nba_reply (user_id,match_id,cont,create_time) values("
							+ uid
							+ ","
							+ mid
							+ ",'"
							+ StringUtil.toSql(cont)
							+ "',now())");
			try {
				response.sendRedirect("reply.jsp?mid=" + mid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public List countLiveEvent() {
		return service.getNumLiveEvent();
	}

	public BeanReply getReply(int rid) {
		return service.getReply(rid);
	}

	public BeanNews getNewById() {
		int id = this.getParameterInt("nid");
		if (id > 0)
			return service.getTheNew(id);
		else
			return null;
	}

	public BeanSupport getSupportById(int uid, int mid) {
		return service.getBeanSuport(uid, mid);
	}

	/**
	 * 清空上次发言框中的内容
	 * 
	 * @return
	 */
	public int GetSign() {
		int sign = 0;
		String ss = (String) this.getAttribute2("sign");
		if (ss != null) {
			sign = Integer.parseInt(ss);
		}
		if (sign >= 0 && sign < 99999) {
			sign++;
		} else {
			sign = 0;
		}
		this.setAttribute2("sign", String.valueOf(sign));
		return sign;
	}
}
